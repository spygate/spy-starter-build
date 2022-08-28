package spy.project.sequence;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import java.security.SecureRandom;

public class NanoIdGenerator implements Sequence {
    public static final SecureRandom NUMBER_GENERATOR = new SecureRandom();
    public static final char[] DEFAULT_ALPHABET = "0123456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ".toCharArray();
    public static final int SIZE = 21;

    @Override
    public String getNext() {
        return NanoIdUtils.randomNanoId(NUMBER_GENERATOR, DEFAULT_ALPHABET, SIZE);
    }
}
