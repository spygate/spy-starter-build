package spy.project.sequence;

/**
 * 子类只需要实现其中一个接口
 */
public interface Sequence {
    default String getNext() {
        return null;
    }

    default String getNext(String sequenceCode, FastIdGenerator.BizKey bizKey) {
        return null;
    }

    default String getNext(String code, String seed) {
        return null;
    }
}
