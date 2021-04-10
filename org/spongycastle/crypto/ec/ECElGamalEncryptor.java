package org.spongycastle.crypto.ec;

import java.security.*;
import org.spongycastle.math.ec.*;
import java.math.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class ECElGamalEncryptor implements ECEncryptor
{
    private ECPublicKeyParameters key;
    private SecureRandom random;
    
    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }
    
    @Override
    public ECPair encrypt(final ECPoint ecPoint) {
        final ECPublicKeyParameters key = this.key;
        if (key != null) {
            final ECDomainParameters parameters = key.getParameters();
            final BigInteger generateK = ECUtil.generateK(parameters.getN(), this.random);
            final ECPoint[] array = { this.createBasePointMultiplier().multiply(parameters.getG(), generateK), this.key.getQ().multiply(generateK).add(ecPoint) };
            parameters.getCurve().normalizeAll(array);
            return new ECPair(array[0], array[1]);
        }
        throw new IllegalStateException("ECElGamalEncryptor not initialised");
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        SecureRandom random;
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            if (!(parametersWithRandom.getParameters() instanceof ECPublicKeyParameters)) {
                throw new IllegalArgumentException("ECPublicKeyParameters are required for encryption.");
            }
            this.key = (ECPublicKeyParameters)parametersWithRandom.getParameters();
            random = parametersWithRandom.getRandom();
        }
        else {
            if (!(cipherParameters instanceof ECPublicKeyParameters)) {
                throw new IllegalArgumentException("ECPublicKeyParameters are required for encryption.");
            }
            this.key = (ECPublicKeyParameters)cipherParameters;
            random = new SecureRandom();
        }
        this.random = random;
    }
}
