package spy.project.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import spy.project.utils.JsonUtils;

import java.io.IOException;
import java.time.Duration;

public class RedisCache implements Cache {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${spring.profiles.active:default}")
    private String activeEnv;

    @Override
    public <T> T get(String key, Class<T> clazz) throws IOException {
        String newKey = generateKey(key);
        Object obj = redisTemplate.opsForValue().get(newKey);
        if(obj instanceof String) {
            return (T)obj;
        } else {
            return JsonUtils.toObject(JsonUtils.toJson(obj), clazz);
        }
    }

    @Override
    public <T> void put(String key, T t, int expireSecond) {
        String newKey = generateKey(key);
        redisTemplate.opsForValue().set(newKey, t, Duration.ofSeconds(expireSecond));
    }

    @Override
    public boolean delete(String key) {
        String newKey = generateKey(key);
        return redisTemplate.delete(newKey);
    }

    private String generateKey(String key) {
        return activeEnv + ":cache:" + key;
    }

}
