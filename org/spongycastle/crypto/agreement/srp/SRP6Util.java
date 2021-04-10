package org.spongycastle.crypto.agreement.srp;

import java.math.*;
import java.security.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;

public class SRP6Util
{
    private static BigInteger ONE;
    private static BigInteger ZERO;
    
    static {
        SRP6Util.ZERO = BigInteger.valueOf(0L);
        SRP6Util.ONE = BigInteger.valueOf(1L);
    }
    
    public static BigInteger calculateK(final Digest digest, final BigInteger bigInteger, final BigInteger bigInteger2) {
        return hashPaddedPair(digest, bigInteger, bigInteger, bigInteger2);
    }
    
    public static BigInteger calculateKey(final Digest digest, final BigInteger bigInteger, final BigInteger bigInteger2) {
        final byte[] padded = getPadded(bigInteger2, (bigInteger.bitLength() + 7) / 8);
        digest.update(padded, 0, padded.length);
        final byte[] array = new byte[digest.getDigestSize()];
        digest.doFinal(array, 0);
        return new BigInteger(1, array);
    }
    
    public static BigInteger calculateM1(final Digest digest, final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3, final BigInteger bigInteger4) {
        return hashPaddedTriplet(digest, bigInteger, bigInteger2, bigInteger3, bigInteger4);
    }
    
    public static BigInteger calculateM2(final Digest digest, final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3, final BigInteger bigInteger4) {
        return hashPaddedTriplet(digest, bigInteger, bigInteger2, bigInteger3, bigInteger4);
    }
    
    public static BigInteger calculateU(final Digest digest, final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3) {
        return hashPaddedPair(digest, bigInteger, bigInteger2, bigInteger3);
    }
    
    public static BigInteger calculateX(final Digest digest, final BigInteger bigInteger, final byte[] array, final byte[] array2, final byte[] array3) {
        final int digestSize = digest.getDigestSize();
        final byte[] array4 = new byte[digestSize];
        digest.update(array2, 0, array2.length);
        digest.update((byte)58);
        digest.update(array3, 0, array3.length);
        digest.doFinal(array4, 0);
        digest.update(array, 0, array.length);
        digest.update(array4, 0, digestSize);
        digest.doFinal(array4, 0);
        return new BigInteger(1, array4);
    }
    
    public static BigInteger generatePrivateValue(final Digest digest, final BigInteger bigInteger, final BigInteger bigInteger2, final SecureRandom secureRandom) {
        return BigIntegers.createRandomInRange(SRP6Util.ONE.shiftLeft(Math.min(256, bigInteger.bitLength() / 2) - 1), bigInteger.subtract(SRP6Util.ONE), secureRandom);
    }
    
    private static byte[] getPadded(final BigInteger bigInteger, final int n) {
        byte[] unsignedByteArray;
        final byte[] array = unsignedByteArray = BigIntegers.asUnsignedByteArray(bigInteger);
        if (array.length < n) {
            unsignedByteArray = new byte[n];
            System.arraycopy(array, 0, unsignedByteArray, n - array.length, array.length);
        }
        return unsignedByteArray;
    }
    
    private static BigInteger hashPaddedPair(final Digest digest, final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3) {
        final int n = (bigInteger.bitLength() + 7) / 8;
        final byte[] padded = getPadded(bigInteger2, n);
        final byte[] padded2 = getPadded(bigInteger3, n);
        digest.update(padded, 0, padded.length);
        digest.update(padded2, 0, padded2.length);
        final byte[] array = new byte[digest.getDigestSize()];
        digest.doFinal(array, 0);
        return new BigInteger(1, array);
    }
    
    private static BigInteger hashPaddedTriplet(final Digest digest, final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3, final BigInteger bigInteger4) {
        final int n = (bigInteger.bitLength() + 7) / 8;
        final byte[] padded = getPadded(bigInteger2, n);
        final byte[] padded2 = getPadded(bigInteger3, n);
        final byte[] padded3 = getPadded(bigInteger4, n);
        digest.update(padded, 0, padded.length);
        digest.update(padded2, 0, padded2.length);
        digest.update(padded3, 0, padded3.length);
        final byte[] array = new byte[digest.getDigestSize()];
        digest.doFinal(array, 0);
        return new BigInteger(1, array);
    }
    
    public static BigInteger validatePublicValue(BigInteger mod, final BigInteger bigInteger) throws CryptoException {
        mod = bigInteger.mod(mod);
        if (!mod.equals(SRP6Util.ZERO)) {
            return mod;
        }
        throw new CryptoException("Invalid public value: 0");
    }
}
