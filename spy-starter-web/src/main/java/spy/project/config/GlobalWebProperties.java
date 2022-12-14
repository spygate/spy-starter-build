package spy.project.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "spy.web")
@ConditionalOnProperty(name = "spy.web.enable", havingValue = "true", matchIfMissing = false)
public class GlobalWebProperties {

    private Boolean enable = false;

    private Boolean feignEnable = false;

    private List<String> skipPath = new ArrayList<String>();

    private JwtProperties jwt = new JwtProperties();

    private SequenceProperties sequence = new SequenceProperties();

    private LockProperties lock = new LockProperties();

    @Data
    @ConfigurationProperties(prefix = "spy.web.jwt")
    public class JwtProperties {
        private Boolean enable;
        private String secret;
        private Integer expireTimeSecond;//秒
    }

    @Data
    @ConfigurationProperties(prefix = "spy.web.sequence")
    public class SequenceProperties {
        private Boolean enable;
        private Boolean enableDb;
        private Boolean enableRedis;
    }

    @Data
    @ConfigurationProperties(prefix = "spy.web.lock")
    public class LockProperties {
        private Boolean enableRedis;
        private Boolean enableZookeeper;
    }
}
