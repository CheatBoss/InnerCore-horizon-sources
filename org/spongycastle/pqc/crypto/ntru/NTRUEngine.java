package org.spongycastle.pqc.crypto.ntru;

import java.security.*;
import org.spongycastle.util.*;
import org.spongycastle.pqc.math.ntru.polynomial.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class NTRUEngine implements AsymmetricBlockCipher
{
    private boolean forEncryption;
    private NTRUEncryptionParameters params;
    private NTRUEncryptionPrivateKeyParameters privKey;
    private NTRUEncryptionPublicKeyParameters pubKey;
    private SecureRandom random;
    
    private IntegerPolynomial MGF(byte[] calcHash, final int n, int i, final boolean b) {
        final Digest hashAlg = this.params.hashAlg;
        final int digestSize = hashAlg.getDigestSize();
        final byte[] array = new byte[i * digestSize];
        byte[] calcHash2 = calcHash;
        if (b) {
            calcHash2 = this.calcHash(hashAlg, calcHash);
        }
        int j;
        for (j = 0; j < i; ++j) {
            hashAlg.update(calcHash2, 0, calcHash2.length);
            this.putInt(hashAlg, j);
            System.arraycopy(this.calcHash(hashAlg), 0, array, j * digestSize, digestSize);
        }
        final IntegerPolynomial integerPolynomial = new IntegerPolynomial(n);
        calcHash = array;
        while (true) {
            int k = 0;
            i = 0;
            while (k != calcHash.length) {
                int n2 = calcHash[k] & 0xFF;
                if (n2 < 243) {
                    final int n3 = 0;
                    int n4 = i;
                    int n5;
                    for (i = n3; i < 4; ++i) {
                        n5 = n2 % 3;
                        integerPolynomial.coeffs[n4] = n5 - 1;
                        ++n4;
                        if (n4 == n) {
                            return integerPolynomial;
                        }
                        n2 = (n2 - n5) / 3;
                    }
                    integerPolynomial.coeffs[n4] = n2 - 1;
                    i = n4 + 1;
                    if (i == n) {
                        return integerPolynomial;
                    }
                }
                ++k;
            }
            if (i >= n) {
                return integerPolynomial;
            }
            hashAlg.update(calcHash2, 0, calcHash2.length);
            this.putInt(hashAlg, j);
            calcHash = this.calcHash(hashAlg);
            ++j;
        }
    }
    
    private byte[] buildSData(final byte[] array, final byte[] array2, final int n, final byte[] array3, final byte[] array4) {
        final byte[] array5 = new byte[array.length + n + array3.length + array4.length];
        System.arraycopy(array, 0, array5, 0, array.length);
        System.arraycopy(array2, 0, array5, array.length, array2.length);
        System.arraycopy(array3, 0, array5, array.length + array2.length, array3.length);
        System.arraycopy(array4, 0, array5, array.length + array2.length + array3.length, array4.length);
        return array5;
    }
    
    private byte[] calcHash(final Digest digest) {
        final byte[] array = new byte[digest.getDigestSize()];
        digest.doFinal(array, 0);
        return array;
    }
    
    private byte[] calcHash(final Digest digest, final byte[] array) {
        final byte[] array2 = new byte[digest.getDigestSize()];
        digest.update(array, 0, array.length);
        digest.doFinal(array2, 0);
        return array2;
    }
    
    private byte[] copyOf(final byte[] array, int length) {
        final byte[] array2 = new byte[length];
        if (length >= array.length) {
            length = array.length;
        }
        System.arraycopy(array, 0, array2, 0, length);
        return array2;
    }
    
    private byte[] decrypt(final byte[] array, final NTRUEncryptionPrivateKeyParameters ntruEncryptionPrivateKeyParameters) throws InvalidCipherTextException {
        final Polynomial t = ntruEncryptionPrivateKeyParameters.t;
        final IntegerPolynomial fp = ntruEncryptionPrivateKeyParameters.fp;
        final IntegerPolynomial h = ntruEncryptionPrivateKeyParameters.h;
        final int n = this.params.N;
        final int q = this.params.q;
        final int db = this.params.db;
        final int maxMsgLenBytes = this.params.maxMsgLenBytes;
        final int dm0 = this.params.dm0;
        final int pkLen = this.params.pkLen;
        final int minCallsMask = this.params.minCallsMask;
        final boolean hashSeed = this.params.hashSeed;
        final byte[] oid = this.params.oid;
        if (maxMsgLenBytes > 255) {
            throw new DataLengthException("maxMsgLenBytes values bigger than 255 are not supported");
        }
        final int n2 = db / 8;
        final IntegerPolynomial fromBinary = IntegerPolynomial.fromBinary(array, n, q);
        final IntegerPolynomial decrypt = this.decrypt(fromBinary, t, fp);
        if (decrypt.count(-1) < dm0) {
            throw new InvalidCipherTextException("Less than dm0 coefficients equal -1");
        }
        if (decrypt.count(0) < dm0) {
            throw new InvalidCipherTextException("Less than dm0 coefficients equal 0");
        }
        if (decrypt.count(1) < dm0) {
            throw new InvalidCipherTextException("Less than dm0 coefficients equal 1");
        }
        final IntegerPolynomial integerPolynomial = (IntegerPolynomial)fromBinary.clone();
        integerPolynomial.sub(decrypt);
        integerPolynomial.modPositive(q);
        final IntegerPolynomial integerPolynomial2 = (IntegerPolynomial)integerPolynomial.clone();
        integerPolynomial2.modPositive(4);
        decrypt.sub(this.MGF(integerPolynomial2.toBinary(4), n, minCallsMask, hashSeed));
        decrypt.mod3();
        final byte[] binary3Sves = decrypt.toBinary3Sves();
        final byte[] array2 = new byte[n2];
        System.arraycopy(binary3Sves, 0, array2, 0, n2);
        final int n3 = 0xFF & binary3Sves[n2];
        if (n3 > maxMsgLenBytes) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Message too long: ");
            sb.append(n3);
            sb.append(">");
            sb.append(maxMsgLenBytes);
            throw new InvalidCipherTextException(sb.toString());
        }
        final byte[] array3 = new byte[n3];
        final int n4 = n2 + 1;
        System.arraycopy(binary3Sves, n4, array3, 0, n3);
        final int length = binary3Sves.length;
        final int n5 = n4 + n3;
        final int n6 = length - n5;
        final byte[] array4 = new byte[n6];
        System.arraycopy(binary3Sves, n5, array4, 0, n6);
        if (!Arrays.constantTimeAreEqual(array4, new byte[n6])) {
            throw new InvalidCipherTextException("The message is not followed by zeroes");
        }
        final IntegerPolynomial mult = this.generateBlindingPoly(this.buildSData(oid, array3, n3, array2, this.copyOf(h.toBinary(q), pkLen / 8)), array3).mult(h);
        mult.modPositive(q);
        if (mult.equals(integerPolynomial)) {
            return array3;
        }
        throw new InvalidCipherTextException("Invalid message encoding");
    }
    
    private byte[] encrypt(final byte[] array, final NTRUEncryptionPublicKeyParameters ntruEncryptionPublicKeyParameters) {
        final IntegerPolynomial h = ntruEncryptionPublicKeyParameters.h;
        final int n = this.params.N;
        final int q = this.params.q;
        final int maxMsgLenBytes = this.params.maxMsgLenBytes;
        final int db = this.params.db;
        final int bufferLenBits = this.params.bufferLenBits;
        final int dm0 = this.params.dm0;
        final int pkLen = this.params.pkLen;
        final int minCallsMask = this.params.minCallsMask;
        final boolean hashSeed = this.params.hashSeed;
        final byte[] oid = this.params.oid;
        final int length = array.length;
        if (maxMsgLenBytes > 255) {
            throw new IllegalArgumentException("llen values bigger than 1 are not supported");
        }
        if (length <= maxMsgLenBytes) {
            IntegerPolynomial fromBinary3Sves;
            IntegerPolynomial mult;
            while (true) {
                final int n2 = db / 8;
                final byte[] array2 = new byte[n2];
                this.random.nextBytes(array2);
                final int n3 = maxMsgLenBytes + 1 - length;
                final byte[] array3 = new byte[n3];
                final byte[] array4 = new byte[bufferLenBits / 8];
                System.arraycopy(array2, 0, array4, 0, n2);
                array4[n2] = (byte)length;
                final int n4 = n2 + 1;
                System.arraycopy(array, 0, array4, n4, array.length);
                System.arraycopy(array3, 0, array4, n4 + array.length, n3);
                fromBinary3Sves = IntegerPolynomial.fromBinary3Sves(array4, n);
                mult = this.generateBlindingPoly(this.buildSData(oid, array, length, array2, this.copyOf(h.toBinary(q), pkLen / 8)), array4).mult(h, q);
                final IntegerPolynomial integerPolynomial = (IntegerPolynomial)mult.clone();
                integerPolynomial.modPositive(4);
                fromBinary3Sves.add(this.MGF(integerPolynomial.toBinary(4), n, minCallsMask, hashSeed));
                fromBinary3Sves.mod3();
                if (fromBinary3Sves.count(-1) < dm0) {
                    continue;
                }
                if (fromBinary3Sves.count(0) < dm0) {
                    continue;
                }
                if (fromBinary3Sves.count(1) < dm0) {
                    continue;
                }
                break;
            }
            mult.add(fromBinary3Sves, q);
            mult.ensurePositive(q);
            return mult.toBinary(q);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Message too long: ");
        sb.append(length);
        sb.append(">");
        sb.append(maxMsgLenBytes);
        throw new DataLengthException(sb.toString());
    }
    
    private int[] generateBlindingCoeffs(final IndexGenerator indexGenerator, final int n) {
        final int[] array = new int[this.params.N];
        for (int i = -1; i <= 1; i += 2) {
            for (int j = 0; j < n; ++j) {
                final int nextIndex = indexGenerator.nextIndex();
                if (array[nextIndex] == 0) {
                    array[nextIndex] = i;
                }
            }
        }
        return array;
    }
    
    private Polynomial generateBlindingPoly(final byte[] array, final byte[] array2) {
        final IndexGenerator indexGenerator = new IndexGenerator(array, this.params);
        if (this.params.polyType == 1) {
            return new ProductFormPolynomial(new SparseTernaryPolynomial(this.generateBlindingCoeffs(indexGenerator, this.params.dr1)), new SparseTernaryPolynomial(this.generateBlindingCoeffs(indexGenerator, this.params.dr2)), new SparseTernaryPolynomial(this.generateBlindingCoeffs(indexGenerator, this.params.dr3)));
        }
        final int dr = this.params.dr;
        final boolean sparse = this.params.sparse;
        final int[] generateBlindingCoeffs = this.generateBlindingCoeffs(indexGenerator, dr);
        if (sparse) {
            return new SparseTernaryPolynomial(generateBlindingCoeffs);
        }
        return new DenseTernaryPolynomial(generateBlindingCoeffs);
    }
    
    private int log2(final int n) {
        if (n == 2048) {
            return 11;
        }
        throw new IllegalStateException("log2 not fully implemented");
    }
    
    private void putInt(final Digest digest, final int n) {
        digest.update((byte)(n >> 24));
        digest.update((byte)(n >> 16));
        digest.update((byte)(n >> 8));
        digest.update((byte)n);
    }
    
    protected IntegerPolynomial decrypt(IntegerPolynomial integerPolynomial, final Polynomial polynomial, final IntegerPolynomial integerPolynomial2) {
        if (this.params.fastFp) {
            final IntegerPolynomial mult = polynomial.mult(integerPolynomial, this.params.q);
            mult.mult(3);
            mult.add(integerPolynomial);
            integerPolynomial = mult;
        }
        else {
            integerPolynomial = polynomial.mult(integerPolynomial, this.params.q);
        }
        integerPolynomial.center0(this.params.q);
        integerPolynomial.mod3();
        if (!this.params.fastFp) {
            integerPolynomial = new DenseTernaryPolynomial(integerPolynomial).mult(integerPolynomial2, 3);
        }
        integerPolynomial.center0(3);
        return integerPolynomial;
    }
    
    protected IntegerPolynomial encrypt(final IntegerPolynomial integerPolynomial, final TernaryPolynomial ternaryPolynomial, final IntegerPolynomial integerPolynomial2) {
        final IntegerPolynomial mult = ternaryPolynomial.mult(integerPolynomial2, this.params.q);
        mult.add(integerPolynomial, this.params.q);
        mult.ensurePositive(this.params.q);
        return mult;
    }
    
    @Override
    public int getInputBlockSize() {
        return this.params.maxMsgLenBytes;
    }
    
    @Override
    public int getOutputBlockSize() {
        return (this.params.N * this.log2(this.params.q) + 7) / 8;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        this.forEncryption = forEncryption;
        NTRUEncryptionParameters params;
        if (forEncryption) {
            if (cipherParameters instanceof ParametersWithRandom) {
                final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
                this.random = parametersWithRandom.getRandom();
                this.pubKey = (NTRUEncryptionPublicKeyParameters)parametersWithRandom.getParameters();
            }
            else {
                this.random = new SecureRandom();
                this.pubKey = (NTRUEncryptionPublicKeyParameters)cipherParameters;
            }
            params = this.pubKey.getParameters();
        }
        else {
            final NTRUEncryptionPrivateKeyParameters privKey = (NTRUEncryptionPrivateKeyParameters)cipherParameters;
            this.privKey = privKey;
            params = privKey.getParameters();
        }
        this.params = params;
    }
    
    @Override
    public byte[] processBlock(final byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        final byte[] array2 = new byte[n2];
        System.arraycopy(array, n, array2, 0, n2);
        if (this.forEncryption) {
            return this.encrypt(array2, this.pubKey);
        }
        return this.decrypt(array2, this.privKey);
    }
}
