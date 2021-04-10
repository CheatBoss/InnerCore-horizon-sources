package org.spongycastle.crypto.generators;

import java.math.*;
import org.spongycastle.crypto.*;
import java.security.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;

public class DSAParametersGenerator
{
    private static final BigInteger ONE;
    private static final BigInteger TWO;
    private static final BigInteger ZERO;
    private int L;
    private int N;
    private int certainty;
    private Digest digest;
    private int iterations;
    private SecureRandom random;
    private int usageIndex;
    private boolean use186_3;
    
    static {
        ZERO = BigInteger.valueOf(0L);
        ONE = BigInteger.valueOf(1L);
        TWO = BigInteger.valueOf(2L);
    }
    
    public DSAParametersGenerator() {
        this(DigestFactory.createSHA1());
    }
    
    public DSAParametersGenerator(final Digest digest) {
        this.digest = digest;
    }
    
    private static BigInteger calculateGenerator_FIPS186_2(final BigInteger bigInteger, BigInteger divide, final SecureRandom secureRandom) {
        divide = bigInteger.subtract(DSAParametersGenerator.ONE).divide(divide);
        final BigInteger subtract = bigInteger.subtract(DSAParametersGenerator.TWO);
        BigInteger modPow;
        do {
            modPow = BigIntegers.createRandomInRange(DSAParametersGenerator.TWO, subtract, secureRandom).modPow(divide, bigInteger);
        } while (modPow.bitLength() <= 1);
        return modPow;
    }
    
    private static BigInteger calculateGenerator_FIPS186_3_Unverifiable(final BigInteger bigInteger, final BigInteger bigInteger2, final SecureRandom secureRandom) {
        return calculateGenerator_FIPS186_2(bigInteger, bigInteger2, secureRandom);
    }
    
    private static BigInteger calculateGenerator_FIPS186_3_Verifiable(final Digest digest, final BigInteger bigInteger, BigInteger divide, byte[] array, int i) {
        divide = bigInteger.subtract(DSAParametersGenerator.ONE).divide(divide);
        final byte[] decode = Hex.decode("6767656E");
        final int n = array.length + decode.length + 1 + 2;
        final byte[] array2 = new byte[n];
        System.arraycopy(array, 0, array2, 0, array.length);
        System.arraycopy(decode, 0, array2, array.length, decode.length);
        array2[n - 3] = (byte)i;
        array = new byte[digest.getDigestSize()];
        BigInteger modPow;
        for (i = 1; i < 65536; ++i) {
            inc(array2);
            hash(digest, array2, array, 0);
            modPow = new BigInteger(1, array).modPow(divide, bigInteger);
            if (modPow.compareTo(DSAParametersGenerator.TWO) >= 0) {
                return modPow;
            }
        }
        return null;
    }
    
    private DSAParameters generateParameters_FIPS186_2() {
        int n = 20;
        final byte[] array = new byte[20];
        final byte[] array2 = new byte[20];
        final byte[] array3 = new byte[20];
        final byte[] array4 = new byte[20];
        final int l = this.L;
        final int n2 = (l - 1) / 160;
        final int n3 = l / 8;
        final byte[] array5 = new byte[n3];
        if (this.digest instanceof SHA1Digest) {
            BigInteger bigInteger = null;
            int j = 0;
            BigInteger subtract = null;
        Block_6:
            while (true) {
                this.random.nextBytes(array);
                hash(this.digest, array, array2, 0);
                System.arraycopy(array, 0, array3, 0, n);
                inc(array3);
                hash(this.digest, array3, array3, 0);
                for (int i = 0; i != n; ++i) {
                    array4[i] = (byte)(array2[i] ^ array3[i]);
                }
                array4[0] |= 0xFFFFFF80;
                array4[19] |= 0x1;
                bigInteger = new BigInteger(1, array4);
                if (!this.isProbablePrime(bigInteger)) {
                    continue;
                }
                final byte[] clone = Arrays.clone(array);
                inc(clone);
                for (j = 0; j < 4096; ++j, n = 20) {
                    for (int k = 1; k <= n2; ++k) {
                        inc(clone);
                        hash(this.digest, clone, array5, n3 - k * 20);
                    }
                    final int n4 = n3 - n2 * 20;
                    inc(clone);
                    hash(this.digest, clone, array2, 0);
                    System.arraycopy(array2, 20 - n4, array5, 0, n4);
                    array5[0] |= 0xFFFFFF80;
                    final BigInteger bigInteger2 = new BigInteger(1, array5);
                    subtract = bigInteger2.subtract(bigInteger2.mod(bigInteger.shiftLeft(1)).subtract(DSAParametersGenerator.ONE));
                    if (subtract.bitLength() == this.L) {
                        if (this.isProbablePrime(subtract)) {
                            break Block_6;
                        }
                    }
                }
            }
            return new DSAParameters(subtract, bigInteger, calculateGenerator_FIPS186_2(subtract, bigInteger, this.random), new DSAValidationParameters(array, j));
        }
        throw new IllegalStateException("can only use SHA-1 for generating FIPS 186-2 parameters");
    }
    
    private DSAParameters generateParameters_FIPS186_3() {
        final Digest digest = this.digest;
        final int digestSize = digest.getDigestSize();
        final byte[] array = new byte[this.N / 8];
        final int l = this.L;
        final int n = (l - 1) / (digestSize * 8);
        final int n2 = l / 8;
        final byte[] array2 = new byte[n2];
        final int digestSize2 = digest.getDigestSize();
        final byte[] array3 = new byte[digestSize2];
        BigInteger setBit = null;
        int j = 0;
        BigInteger subtract = null;
    Block_5:
        while (true) {
            this.random.nextBytes(array);
            hash(digest, array, array3, 0);
            setBit = new BigInteger(1, array3).mod(DSAParametersGenerator.ONE.shiftLeft(this.N - 1)).setBit(0).setBit(this.N - 1);
            if (!this.isProbablePrime(setBit)) {
                continue;
            }
            final byte[] clone = Arrays.clone(array);
            for (final int i = this.L, j = 0; j < i * 4; ++j) {
                for (int k = 1; k <= n; ++k) {
                    inc(clone);
                    hash(digest, clone, array2, n2 - k * digestSize2);
                }
                final int n3 = n2 - n * digestSize2;
                inc(clone);
                hash(digest, clone, array3, 0);
                System.arraycopy(array3, digestSize2 - n3, array2, 0, n3);
                array2[0] |= 0xFFFFFF80;
                final BigInteger bigInteger = new BigInteger(1, array2);
                subtract = bigInteger.subtract(bigInteger.mod(setBit.shiftLeft(1)).subtract(DSAParametersGenerator.ONE));
                if (subtract.bitLength() == this.L) {
                    if (this.isProbablePrime(subtract)) {
                        break Block_5;
                    }
                }
            }
        }
        final int usageIndex = this.usageIndex;
        if (usageIndex >= 0) {
            final BigInteger calculateGenerator_FIPS186_3_Verifiable = calculateGenerator_FIPS186_3_Verifiable(digest, subtract, setBit, array, usageIndex);
            if (calculateGenerator_FIPS186_3_Verifiable != null) {
                return new DSAParameters(subtract, setBit, calculateGenerator_FIPS186_3_Verifiable, new DSAValidationParameters(array, j, this.usageIndex));
            }
        }
        return new DSAParameters(subtract, setBit, calculateGenerator_FIPS186_3_Unverifiable(subtract, setBit, this.random), new DSAValidationParameters(array, j));
    }
    
    private static int getDefaultN(final int n) {
        if (n > 1024) {
            return 256;
        }
        return 160;
    }
    
    private static int getMinimumIterations(final int n) {
        if (n <= 1024) {
            return 40;
        }
        return (n - 1) / 1024 * 8 + 48;
    }
    
    private static void hash(final Digest digest, final byte[] array, final byte[] array2, final int n) {
        digest.update(array, 0, array.length);
        digest.doFinal(array2, n);
    }
    
    private static void inc(final byte[] array) {
        int length = array.length;
        do {
            --length;
        } while (length >= 0 && (array[length] = (byte)(array[length] + 1 & 0xFF)) == 0);
    }
    
    private boolean isProbablePrime(final BigInteger bigInteger) {
        return bigInteger.isProbablePrime(this.certainty);
    }
    
    public DSAParameters generateParameters() {
        if (this.use186_3) {
            return this.generateParameters_FIPS186_3();
        }
        return this.generateParameters_FIPS186_2();
    }
    
    public void init(final int l, final int certainty, final SecureRandom random) {
        this.L = l;
        this.N = getDefaultN(l);
        this.certainty = certainty;
        this.iterations = Math.max(getMinimumIterations(this.L), (certainty + 1) / 2);
        this.random = random;
        this.use186_3 = false;
        this.usageIndex = -1;
    }
    
    public void init(final DSAParameterGenerationParameters dsaParameterGenerationParameters) {
        final int l = dsaParameterGenerationParameters.getL();
        final int n = dsaParameterGenerationParameters.getN();
        if (l < 1024 || l > 3072 || l % 1024 != 0) {
            throw new IllegalArgumentException("L values must be between 1024 and 3072 and a multiple of 1024");
        }
        if (l == 1024 && n != 160) {
            throw new IllegalArgumentException("N must be 160 for L = 1024");
        }
        if (l == 2048 && n != 224 && n != 256) {
            throw new IllegalArgumentException("N must be 224 or 256 for L = 2048");
        }
        if (l == 3072 && n != 256) {
            throw new IllegalArgumentException("N must be 256 for L = 3072");
        }
        if (this.digest.getDigestSize() * 8 >= n) {
            this.L = l;
            this.N = n;
            this.certainty = dsaParameterGenerationParameters.getCertainty();
            this.iterations = Math.max(getMinimumIterations(l), (this.certainty + 1) / 2);
            this.random = dsaParameterGenerationParameters.getRandom();
            this.use186_3 = true;
            this.usageIndex = dsaParameterGenerationParameters.getUsageIndex();
            return;
        }
        throw new IllegalStateException("Digest output size too small for value of N");
    }
}
