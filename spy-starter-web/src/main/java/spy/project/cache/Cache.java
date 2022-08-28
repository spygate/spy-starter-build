package spy.project.cache;

import java.io.IOException;

public interface Cache {
    <T> T get(String key, Class<T> clazz) throws IOException;
    <T> void put(String key, T t, int expireSecond);
    boolean delete(String key);
}
