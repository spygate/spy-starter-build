package spy.project.sequence;

import java.util.UUID;

public class UUIDGenerator implements Sequence {
    @Override
    public String getNext() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
