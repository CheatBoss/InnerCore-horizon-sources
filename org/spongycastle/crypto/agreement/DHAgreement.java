package org.spongycastle.crypto.agreement;

import java.math.*;
import java.security.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class DHAgreement
{
    private static final BigInteger ONE;
    private DHParameters dhParams;
    private DHPrivateKeyParameters key;
    private BigInteger privateValue;
    private SecureRandom random;
    
    static {
        ONE = BigInteger.valueOf(1L);
    }
    
    public BigInteger calculateAgreement(final DHPublicKeyParameters dhPublicKeyParameters, final BigInteger bigInteger) {
        if (!dhPublicKeyParameters.getParameters().equals(this.dhParams)) {
            throw new IllegalArgumentException("Diffie-Hellman public key has wrong parameters.");
        }
        final BigInteger p2 = this.dhParams.getP();
        final BigInteger y = dhPublicKeyParameters.getY();
        if (y == null || y.compareTo(DHAgreement.ONE) <= 0 || y.compareTo(p2.subtract(DHAgreement.ONE)) >= 0) {
            throw new IllegalArgumentException("Diffie-Hellman public key is weak");
        }
        final BigInteger modPow = y.modPow(this.privateValue, p2);
        if (!modPow.equals(DHAgreement.ONE)) {
            return bigInteger.modPow(this.key.getX(), p2).multiply(modPow).mod(p2);
        }
        throw new IllegalStateException("Shared key can't be 1");
    }
    
    public BigInteger calculateMessage() {
        final DHKeyPairGenerator dhKeyPairGenerator = new DHKeyPairGenerator();
        dhKeyPairGenerator.init(new DHKeyGenerationParameters(this.random, this.dhParams));
        final AsymmetricCipherKeyPair generateKeyPair = dhKeyPairGenerator.generateKeyPair();
        this.privateValue = ((DHPrivateKeyParameters)generateKeyPair.getPrivate()).getX();
        return ((DHPublicKeyParameters)generateKeyPair.getPublic()).getY();
    }
    
    public void init(CipherParameters parameters) {
        if (parameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)parameters;
            this.random = parametersWithRandom.getRandom();
            parameters = parametersWithRandom.getParameters();
        }
        else {
            this.random = new SecureRandom();
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
