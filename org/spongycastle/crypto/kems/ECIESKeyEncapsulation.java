package org.spongycastle.crypto.kems;

import java.math.*;
import java.security.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;

public class ECIESKeyEncapsulation implements KeyEncapsulation
{
    private static final BigInteger ONE;
    private boolean CofactorMode;
    private boolean OldCofactorMode;
    private boolean SingleHashMode;
    private DerivationFunction kdf;
    private ECKeyParameters key;
    private SecureRandom rnd;
    
    static {
        ONE = BigInteger.valueOf(1L);
    }
    
    public ECIESKeyEncapsulation(final DerivationFunction kdf, final SecureRandom rnd) {
        this.kdf = kdf;
        this.rnd = rnd;
        this.CofactorMode = false;
        this.OldCofactorMode = false;
        this.SingleHashMode = false;
    }
    
    public ECIESKeyEncapsulation(final DerivationFunction kdf, final SecureRandom rnd, final boolean cofactorMode, final boolean oldCofactorMode, final boolean singleHashMode) {
        this.kdf = kdf;
        this.rnd = rnd;
        this.CofactorMode = cofactorMode;
        this.OldCofactorMode = oldCofactorMode;
        this.SingleHashMode = singleHashMode;
    }
    
    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }
    
    public CipherParameters decrypt(final byte[] array, final int n) {
        return this.decrypt(array, 0, array.length, n);
    }
    
    @Override
    public CipherParameters decrypt(final byte[] array, final int n, final int n2, final int n3) throws IllegalArgumentException {
        final ECKeyParameters key = this.key;
        if (key instanceof ECPrivateKeyParameters) {
            final ECPrivateKeyParameters ecPrivateKeyParameters = (ECPrivateKeyParameters)key;
            final ECDomainParameters parameters = ecPrivateKeyParameters.getParameters();
            final ECCurve curve = parameters.getCurve();
            final BigInteger n4 = parameters.getN();
            final BigInteger h = parameters.getH();
            final byte[] array2 = new byte[n2];
            System.arraycopy(array, n, array2, 0, n2);
            final ECPoint decodePoint = curve.decodePoint(array2);
            ECPoint multiply = null;
            Label_0097: {
                if (!this.CofactorMode) {
                    multiply = decodePoint;
                    if (!this.OldCofactorMode) {
                        break Label_0097;
                    }
                }
                multiply = decodePoint.multiply(h);
            }
            BigInteger bigInteger = ecPrivateKeyParameters.getD();
            if (this.CofactorMode) {
                bigInteger = bigInteger.multiply(h.modInverse(n4)).mod(n4);
            }
            return this.deriveKey(n3, array2, multiply.multiply(bigInteger).normalize().getAffineXCoord().getEncoded());
        }
        throw new IllegalArgumentException("Private key required for encryption");
    }
    
    protected KeyParameter deriveKey(final int n, byte[] concatenate, byte[] array) {
        if (!this.SingleHashMode) {
            concatenate = Arrays.concatenate(concatenate, array);
            Arrays.fill(array, (byte)0);
        }
        else {
            concatenate = array;
        }
        try {
            this.kdf.init(new KDFParameters(concatenate, null));
            array = new byte[n];
            this.kdf.generateBytes(array, 0, n);
            return new KeyParameter(array);
        }
        finally {
            Arrays.fill(concatenate, (byte)0);
        }
    }
    
    public CipherParameters encrypt(final byte[] array, final int n) {
        return this.encrypt(array, 0, n);
    }
    
    @Override
    public CipherParameters encrypt(final byte[] array, final int n, final int n2) throws IllegalArgumentException {
        final ECKeyParameters key = this.key;
        if (key instanceof ECPublicKeyParameters) {
            final ECPublicKeyParameters ecPublicKeyParameters = (ECPublicKeyParameters)key;
            final ECDomainParameters parameters = ecPublicKeyParameters.getParameters();
            final ECCurve curve = parameters.getCurve();
            final BigInteger n3 = parameters.getN();
            final BigInteger h = parameters.getH();
            final BigInteger randomInRange = BigIntegers.createRandomInRange(ECIESKeyEncapsulation.ONE, n3, this.rnd);
            BigInteger mod;
            if (this.CofactorMode) {
                mod = randomInRange.multiply(h).mod(n3);
            }
            else {
                mod = randomInRange;
            }
            final ECPoint[] array2 = { this.createBasePointMultiplier().multiply(parameters.getG(), randomInRange), ecPublicKeyParameters.getQ().multiply(mod) };
            curve.normalizeAll(array2);
            final ECPoint ecPoint = array2[0];
            final ECPoint ecPoint2 = array2[1];
            final byte[] encoded = ecPoint.getEncoded(false);
            System.arraycopy(encoded, 0, array, n, encoded.length);
            return this.deriveKey(n2, encoded, ecPoint2.getAffineXCoord().getEncoded());
        }
        throw new IllegalArgumentException("Public key required for encryption");
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) throws IllegalArgumentException {
        if (cipherParameters instanceof ECKeyParameters) {
            this.key = (ECKeyParameters)cipherParameters;
            return;
        }
        throw new IllegalArgumentException("EC key required");
    }
}
