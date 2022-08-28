package spy.project.sequence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import spy.project.exceptions.FrameException;
import spy.project.exceptions.codes.ErrorCode;
import spy.project.utils.DateUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

import static spy.project.utils.DateUtils.Date_Format_YMD2;

/**
 * 18位固定长度id
 * 业务编码（1） + YYMMDD (6) + 当日秒数 (5) + 订单序号 （6）
 *
 */
public class FastIdGenerator implements Sequence {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${spring.profiles.active:default}")
    private String activeEnv;

    @Override
    public String getNext(String sequenceCode, BizKey bizKey) {

        StringBuilder sb = new StringBuilder();
        sb.append(bizKey.getKey()).append(DateUtils.getCurrent().format(Date_Format_YMD2));
        String day = sb.substring(sb.length() - 2);
        sb.append(DateUtils.secondDay(LocalDateTime.now()));

        String number = String.valueOf(redisTemplate.opsForValue().increment(activeEnv + ":seq:" + sequenceCode + day));
        if("1".equals(number)) {
            redisTemplate.expire(activeEnv + ":seq:" + sequenceCode + day, Duration.ofSeconds(2*3600));
        }
        if(number.length() > 6) {
            throw new FrameException(ErrorCode.ERR0001);
        }
        number = String.join("", Collections.nCopies(6-number.length(), "0")) + number;
        sb.append(number);
        return sb.toString();
    }


    public enum BizKey {
        ORD("1", "序号1"),
        PAY("2", "序号2"),
        RET("3", "序号3"),
        SQ0("4", "序号4"),
        SQ1("5", "序号5"),
        SQ2("6", "序号6"),
        SQ3("7", "序号7"),
        SQ4("8", "序号8"),
        SQ5("9", "序号9"),
        SQ6("0", "序号0"),
        ;

        private String key;
        private String desc;

        BizKey(String key, String desc) {
            this.key = key;
            this.desc = desc;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

}
