package org.spongycastle.crypto.kems;

import java.math.*;
import java.security.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class RSAKeyEncapsulation implements KeyEncapsulation
{
    private static final BigInteger ONE;
    private static final BigInteger ZERO;
    private DerivationFunction kdf;
    private RSAKeyParameters key;
    private SecureRandom rnd;
    
    static {
        ZERO = BigInteger.valueOf(0L);
        ONE = BigInteger.valueOf(1L);
    }
    
    public RSAKeyEncapsulation(final DerivationFunction kdf, final SecureRandom rnd) {
        this.kdf = kdf;
        this.rnd = rnd;
    }
    
    public CipherParameters decrypt(final byte[] array, final int n) {
        return this.decrypt(array, 0, array.length, n);
    }
    
    @Override
    public CipherParameters decrypt(final byte[] array, final int n, final int n2, final int n3) throws IllegalArgumentException {
        if (this.key.isPrivate()) {
            final BigInteger modulus = this.key.getModulus();
            final BigInteger exponent = this.key.getExponent();
            final byte[] array2 = new byte[n2];
            System.arraycopy(array, n, array2, 0, n2);
            return this.generateKey(modulus, new BigInteger(1, array2).modPow(exponent, modulus), n3);
        }
        throw new IllegalArgumentException("Private key required for decryption");
    }
    
    public CipherParameters encrypt(final byte[] array, final int n) {
        return this.encrypt(array, 0, n);
    }
    
    @Override
    public CipherParameters encrypt(final byte[] array, final int n, final int n2) throws IllegalArgumentException {
        if (!this.key.isPrivate()) {
            final BigInteger modulus = this.key.getModulus();
            final BigInteger exponent = this.key.getExponent();
            final BigInteger randomInRange = BigIntegers.createRandomInRange(RSAKeyEncapsulation.ZERO, modulus.subtract(RSAKeyEncapsulation.ONE), this.rnd);
            final byte[] unsignedByteArray = BigIntegers.asUnsignedByteArray((modulus.bitLength() + 7) / 8, randomInRange.modPow(exponent, modulus));
            System.arraycopy(unsignedByteArray, 0, array, n, unsignedByteArray.length);
            return this.generateKey(modulus, randomInRange, n2);
        }
        throw new IllegalArgumentException("Public key required for encryption");
    }
    
    protected KeyParameter generateKey(final BigInteger bigInteger, final BigInteger bigInteger2, final int n) {
        this.kdf.init(new KDFParameters(BigIntegers.asUnsignedByteArray((bigInteger.bitLength() + 7) / 8, bigInteger2), null));
        final byte[] array = new byte[n];
        this.kdf.generateBytes(array, 0, n);
        return new KeyParameter(array);
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) throws IllegalArgumentException {
        if (cipherParameters instanceof RSAKeyParameters) {
            this.key = (RSAKeyParameters)cipherParameters;
            return;
        }
        throw new IllegalArgumentException("RSA key required");
    }
}
