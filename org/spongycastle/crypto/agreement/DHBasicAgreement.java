package org.spongycastle.crypto.agreement;

import java.math.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class DHBasicAgreement implements BasicAgreement
{
    private static final BigInteger ONE;
    private DHParameters dhParams;
    private DHPrivateKeyParameters key;
    
    static {
        ONE = BigInteger.valueOf(1L);
    }
    
    @Override
    public BigInteger calculateAgreement(final CipherParameters cipherParameters) {
        final DHPublicKeyParameters dhPublicKeyParameters = (DHPublicKeyParameters)cipherParameters;
        if (!dhPublicKeyParameters.getParameters().equals(this.dhParams)) {
            throw new IllegalArgumentException("Diffie-Hellman public key has wrong parameters.");
        }
        final BigInteger p = this.dhParams.getP();
        final BigInteger y = dhPublicKeyParameters.getY();
        if (y == null || y.compareTo(DHBasicAgreement.ONE) <= 0 || y.compareTo(p.subtract(DHBasicAgreement.ONE)) >= 0) {
            throw new IllegalArgumentException("Diffie-Hellman public key is weak");
        }
        final BigInteger modPow = y.modPow(this.key.getX(), p);
        if (!modPow.equals(DHBasicAgreement.ONE)) {
            return modPow;
        }
        throw new IllegalStateException("Shared key can't be 1");
    }
    
    @Override
    public int getFieldSize() {
        return (this.key.getParameters().getP().bitLength() + 7) / 8;
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        CipherParameters parameters = cipherParameters;
        if (cipherParameters instanceof ParametersWithRandom) {
            parameters = ((ParametersWithRandom)cipherParameters).getParameters();
        }
        final AsymmetricKeyParameter asymmetricKeyParameter = (AsymmetricKeyParameter)parameters;
        if (asymmetricKeyParameter instanceof DHPrivateKeyParameters) {
            final DHPrivateKeyParameters key = (DHPrivateKeyParameters)asymmetricKeyParameter;
            this.key = key;
            this.dhParams = key.getParameters();
            return;
        }
        throw new IllegalArgumentException("DHEngine expects DHPrivateKeyParameters");
    }
}
