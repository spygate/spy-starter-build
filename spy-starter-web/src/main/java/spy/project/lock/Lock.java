package spy.project.lock;

public interface Lock {
    Boolean lock(String key);
    Boolean lock(String key, int expireSecond);
    Boolean unlock(String key);
}
