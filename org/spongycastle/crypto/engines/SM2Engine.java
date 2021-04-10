package org.spongycastle.crypto.engines;

import java.security.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.util.*;
import java.math.*;
import org.spongycastle.math.ec.*;
import java.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class SM2Engine
{
    private int curveLength;
    private final Digest digest;
    private ECKeyParameters ecKey;
    private ECDomainParameters ecParams;
    private boolean forEncryption;
    private SecureRandom random;
    
    public SM2Engine() {
        this(new SM3Digest());
    }
    
    public SM2Engine(final Digest digest) {
        this.digest = digest;
    }
    
    private void addFieldElement(final Digest digest, final ECFieldElement ecFieldElement) {
        final byte[] unsignedByteArray = BigIntegers.asUnsignedByteArray(this.curveLength, ecFieldElement.toBigInteger());
        digest.update(unsignedByteArray, 0, unsignedByteArray.length);
    }
    
    private void clearBlock(final byte[] array) {
        for (int i = 0; i != array.length; ++i) {
            array[i] = 0;
        }
    }
    
    private byte[] decrypt(final byte[] array, int i, int n) throws InvalidCipherTextException {
        final int n2 = this.curveLength * 2 + 1;
        final byte[] array2 = new byte[n2];
        final int n3 = 0;
        System.arraycopy(array, i, array2, 0, n2);
        final ECPoint decodePoint = this.ecParams.getCurve().decodePoint(array2);
        if (decodePoint.multiply(this.ecParams.getH()).isInfinity()) {
            throw new InvalidCipherTextException("[h]C1 at infinity");
        }
        final ECPoint normalize = decodePoint.multiply(((ECPrivateKeyParameters)this.ecKey).getD()).normalize();
        final int n4 = n - n2 - this.digest.getDigestSize();
        final byte[] array3 = new byte[n4];
        System.arraycopy(array, i + n2, array3, 0, n4);
        this.kdf(this.digest, normalize, array3);
        final int digestSize = this.digest.getDigestSize();
        final byte[] array4 = new byte[digestSize];
        this.addFieldElement(this.digest, normalize.getAffineXCoord());
        this.digest.update(array3, 0, n4);
        this.addFieldElement(this.digest, normalize.getAffineYCoord());
        this.digest.doFinal(array4, 0);
        n = 0;
        for (i = n3; i != digestSize; ++i) {
            n |= (array4[i] ^ array[n2 + n4 + i]);
        }
        this.clearBlock(array2);
        this.clearBlock(array4);
        if (n == 0) {
            return array3;
        }
        this.clearBlock(array3);
        throw new InvalidCipherTextException("invalid cipher text");
    }
    
    private byte[] encrypt(final byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        final byte[] array2 = new byte[n2];
        System.arraycopy(array, n, array2, 0, n2);
        byte[] encoded;
        ECPoint normalize;
        do {
            final BigInteger nextK = this.nextK();
            encoded = this.ecParams.getG().multiply(nextK).normalize().getEncoded(false);
            normalize = ((ECPublicKeyParameters)this.ecKey).getQ().multiply(nextK).normalize();
            this.kdf(this.digest, normalize, array2);
        } while (this.notEncrypted(array2, array, n));
        final byte[] array3 = new byte[this.digest.getDigestSize()];
        this.addFieldElement(this.digest, normalize.getAffineXCoord());
        this.digest.update(array, n, n2);
        this.addFieldElement(this.digest, normalize.getAffineYCoord());
        this.digest.doFinal(array3, 0);
        return Arrays.concatenate(encoded, array2, array3);
    }
    
    private void kdf(final Digest digest, final ECPoint ecPoint, final byte[] array) {
        final int digestSize = digest.getDigestSize();
        final int digestSize2 = digest.getDigestSize();
        final byte[] array2 = new byte[digestSize2];
        int i = 1;
        int n = 1;
        int n2 = 0;
        while (i <= (array.length + digestSize - 1) / digestSize) {
            this.addFieldElement(digest, ecPoint.getAffineXCoord());
            this.addFieldElement(digest, ecPoint.getAffineYCoord());
            digest.update((byte)(n >> 24));
            digest.update((byte)(n >> 16));
            digest.update((byte)(n >> 8));
            digest.update((byte)n);
            digest.doFinal(array2, 0);
            final int n3 = n2 + digestSize2;
            if (n3 < array.length) {
                this.xor(array, array2, n2, digestSize2);
            }
            else {
                this.xor(array, array2, n2, array.length - n2);
            }
            ++n;
            ++i;
            n2 = n3;
        }
    }
    
    private BigInteger nextK() {
        final int bitLength = this.ecParams.getN().bitLength();
        BigInteger bigInteger;
        do {
            bigInteger = new BigInteger(bitLength, this.random);
        } while (bigInteger.equals(ECConstants.ZERO) || bigInteger.compareTo(this.ecParams.getN()) >= 0);
        return bigInteger;
    }
    
    private boolean notEncrypted(final byte[] array, final byte[] array2, final int n) {
        for (int i = 0; i != array.length; ++i) {
            if (array[i] != array2[n]) {
                return false;
            }
        }
        return true;
    }
    
    private void xor(final byte[] array, final byte[] array2, final int n, final int n2) {
        for (int i = 0; i != n2; ++i) {
            final int n3 = n + i;
            array[n3] ^= array2[i];
        }
    }
    
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        this.forEncryption = forEncryption;
        if (forEncryption) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            final ECKeyParameters ecKey = (ECKeyParameters)parametersWithRandom.getParameters();
            this.ecKey = ecKey;
            this.ecParams = ecKey.getParameters();
            if (((ECPublicKeyParameters)this.ecKey).getQ().multiply(this.ecParams.getH()).isInfinity()) {
                throw new IllegalArgumentException("invalid key: [h]Q at infinity");
            }
            this.random = parametersWithRandom.getRandom();
        }
        else {
            final ECKeyParameters ecKey2 = (ECKeyParameters)cipherParameters;
            this.ecKey = ecKey2;
            this.ecParams = ecKey2.getParameters();
        }
        this.curveLength = (this.ecParams.getCurve().getFieldSize() + 7) / 8;
    }
    
    public byte[] processBlock(final byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        if (this.forEncryption) {
            return this.encrypt(array, n, n2);
        }
        return this.decrypt(array, n, n2);
    }
}
