package spy.project.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

public class RedisLock implements Lock {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${spring.profiles.active:default}")
    private String activeEnv;

    @Override
    public Boolean lock(String key) {
        return redisTemplate.opsForValue().setIfAbsent(activeEnv + ":lock:" + key, 1);
    }

    @Override
    public Boolean lock(String key, int expireSecond) {
        return redisTemplate.opsForValue().setIfAbsent(activeEnv + ":lock:" + key, 1, Duration.ofSeconds(expireSecond));
    }

    @Override
    public Boolean unlock(String key) {
        return redisTemplate.delete(key);
    }
}
