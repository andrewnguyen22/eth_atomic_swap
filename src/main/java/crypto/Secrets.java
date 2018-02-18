package crypto;

import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Secrets {

    /**
     * Hashes Secret 1 Way With RIPEMD160
     *
     * @param secret byte array form
     * @return hash     byte array form
     */

    public static byte[] hash_ripemd160(byte[] secret) {
        RIPEMD160Digest d = new RIPEMD160Digest();
        d.update(secret, 0, secret.length);
        byte[] o = new byte[d.getDigestSize()];
        d.doFinal(o, 0);
        return o;
    }

    /**
     * Generates Secure Random Byte Array Size 32
     *
     * @return secret     byte array form
     */

    public static byte[] create_secret() throws NoSuchAlgorithmException {
        byte[] bytes = new byte[32];
        SecureRandom.getInstanceStrong().nextBytes(bytes);
        return bytes;
    }
}
