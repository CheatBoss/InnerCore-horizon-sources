package org.spongycastle.jcajce.provider.asymmetric.dh;

import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.math.*;
import org.spongycastle.crypto.*;
import javax.crypto.*;
import javax.crypto.interfaces.*;
import java.security.spec.*;
import javax.crypto.spec.*;
import org.spongycastle.jcajce.spec.*;
import java.security.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.crypto.agreement.kdf.*;

public class KeyAgreementSpi extends BaseAgreementSpi
{
    private static final BigInteger ONE;
    private static final BigInteger TWO;
    private BigInteger g;
    private BigInteger p;
    private BigInteger result;
    private BigInteger x;
    
    static {
        ONE = BigInteger.valueOf(1L);
        TWO = BigInteger.valueOf(2L);
    }
    
    public KeyAgreementSpi() {
        super("Diffie-Hellman", null);
    }
    
    public KeyAgreementSpi(final String s, final DerivationFunction derivationFunction) {
        super(s, derivationFunction);
    }
    
    protected byte[] bigIntToBytes(final BigInteger bigInteger) {
        final int n = (this.p.bitLength() + 7) / 8;
        final byte[] byteArray = bigInteger.toByteArray();
        if (byteArray.length == n) {
            return byteArray;
        }
        if (byteArray[0] == 0 && byteArray.length == n + 1) {
            final int n2 = byteArray.length - 1;
            final byte[] array = new byte[n2];
            System.arraycopy(byteArray, 1, array, 0, n2);
            return array;
        }
        final byte[] array2 = new byte[n];
        System.arraycopy(byteArray, 0, array2, n - byteArray.length, byteArray.length);
        return array2;
    }
    
    @Override
    protected byte[] calcSecret() {
        return this.bigIntToBytes(this.result);
    }
    
    @Override
    protected Key engineDoPhase(final Key key, final boolean b) throws InvalidKeyException, IllegalStateException {
        if (this.x == null) {
            throw new IllegalStateException("Diffie-Hellman not initialised.");
        }
        if (!(key instanceof DHPublicKey)) {
            throw new InvalidKeyException("DHKeyAgreement doPhase requires DHPublicKey");
        }
        final DHPublicKey dhPublicKey = (DHPublicKey)key;
        if (!dhPublicKey.getParams().getG().equals(this.g) || !dhPublicKey.getParams().getP().equals(this.p)) {
            throw new InvalidKeyException("DHPublicKey not for this KeyAgreement!");
        }
        final BigInteger y = dhPublicKey.getY();
        if (y == null || y.compareTo(KeyAgreementSpi.TWO) < 0 || y.compareTo(this.p.subtract(KeyAgreementSpi.ONE)) >= 0) {
            throw new InvalidKeyException("Invalid DH PublicKey");
        }
        final BigInteger modPow = y.modPow(this.x, this.p);
        this.result = modPow;
        if (modPow.compareTo(KeyAgreementSpi.ONE) == 0) {
            throw new InvalidKeyException("Shared key can't be 1");
        }
        if (b) {
            return null;
        }
        return new BCDHPublicKey(this.result, dhPublicKey.getParams());
    }
    
    @Override
    protected int engineGenerateSecret(final byte[] array, final int n) throws IllegalStateException, ShortBufferException {
        if (this.x != null) {
            return super.engineGenerateSecret(array, n);
        }
        throw new IllegalStateException("Diffie-Hellman not initialised.");
    }
    
    @Override
    protected SecretKey engineGenerateSecret(final String s) throws NoSuchAlgorithmException {
        if (this.x == null) {
            throw new IllegalStateException("Diffie-Hellman not initialised.");
        }
        final byte[] bigIntToBytes = this.bigIntToBytes(this.result);
        if (s.equals("TlsPremasterSecret")) {
            return new SecretKeySpec(BaseAgreementSpi.trimZeroes(bigIntToBytes), s);
        }
        return super.engineGenerateSecret(s);
    }
    
    @Override
    protected byte[] engineGenerateSecret() throws IllegalStateException {
        if (this.x != null) {
            return super.engineGenerateSecret();
        }
        throw new IllegalStateException("Diffie-Hellman not initialised.");
    }
    
    @Override
    protected void engineInit(final Key key, final SecureRandom secureRandom) throws InvalidKeyException {
        if (key instanceof DHPrivateKey) {
            final DHPrivateKey dhPrivateKey = (DHPrivateKey)key;
            this.p = dhPrivateKey.getParams().getP();
            this.g = dhPrivateKey.getParams().getG();
            final BigInteger x = dhPrivateKey.getX();
            this.result = x;
            this.x = x;
            return;
        }
        throw new InvalidKeyException("DHKeyAgreement requires DHPrivateKey");
    }
    
    @Override
    protected void engineInit(final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (key instanceof DHPrivateKey) {
            final DHPrivateKey dhPrivateKey = (DHPrivateKey)key;
            Label_0124: {
                DHParameterSpec params;
                if (algorithmParameterSpec != null) {
                    if (algorithmParameterSpec instanceof DHParameterSpec) {
                        params = (DHParameterSpec)algorithmParameterSpec;
                        this.p = params.getP();
                    }
                    else {
                        if (algorithmParameterSpec instanceof UserKeyingMaterialSpec) {
                            this.p = dhPrivateKey.getParams().getP();
                            this.g = dhPrivateKey.getParams().getG();
                            this.ukmParameters = ((UserKeyingMaterialSpec)algorithmParameterSpec).getUserKeyingMaterial();
                            break Label_0124;
                        }
                        throw new InvalidAlgorithmParameterException("DHKeyAgreement only accepts DHParameterSpec");
                    }
                }
                else {
                    this.p = dhPrivateKey.getParams().getP();
                    params = dhPrivateKey.getParams();
                }
                this.g = params.getG();
            }
            final BigInteger x = dhPrivateKey.getX();
            this.result = x;
            this.x = x;
            return;
        }
        throw new InvalidKeyException("DHKeyAgreement requires DHPrivateKey for initialisation");
    }
    
    public static class DHwithRFC2631KDF extends KeyAgreementSpi
    {
        public DHwithRFC2631KDF() {
            super("DHwithRFC2631KDF", new DHKEKGenerator(DigestFactory.createSHA1()));
        }
    }
}
