package org.spongycastle.crypto.ec;

import java.security.*;
import org.spongycastle.crypto.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.crypto.params.*;
import java.math.*;

public class ECNewPublicKeyTransform implements ECPairTransform
{
    private ECPublicKeyParameters key;
    private SecureRandom random;
    
    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        SecureRandom random;
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            if (!(parametersWithRandom.getParameters() instanceof ECPublicKeyParameters)) {
                throw new IllegalArgumentException("ECPublicKeyParameters are required for new public key transform.");
            }
            this.key = (ECPublicKeyParameters)parametersWithRandom.getParameters();
            random = parametersWithRandom.getRandom();
        }
        else {
            if (!(cipherParameters instanceof ECPublicKeyParameters)) {
                throw new IllegalArgumentException("ECPublicKeyParameters are required for new public key transform.");
            }
            this.key = (ECPublicKeyParameters)cipherParameters;
            random = new SecureRandom();
        }
        this.random = random;
    }
    
    @Override
    public ECPair transform(final ECPair ecPair) {
        final ECPublicKeyParameters key = this.key;
        if (key != null) {
            final ECDomainParameters parameters = key.getParameters();
            final BigInteger n = parameters.getN();
            final ECMultiplier basePointMultiplier = this.createBasePointMultiplier();
            final BigInteger generateK = ECUtil.generateK(n, this.random);
            final ECPoint[] array = { basePointMultiplier.multiply(parameters.getG(), generateK), this.key.getQ().multiply(generateK).add(ecPair.getY()) };
            parameters.getCurve().normalizeAll(array);
            return new ECPair(array[0], array[1]);
        }
        throw new IllegalStateException("ECNewPublicKeyTransform not initialised");
    }
}
