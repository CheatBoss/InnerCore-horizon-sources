package org.spongycastle.crypto.ec;

import java.math.*;
import java.security.*;
import org.spongycastle.crypto.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.crypto.params.*;

public class ECNewRandomnessTransform implements ECPairFactorTransform
{
    private ECPublicKeyParameters key;
    private BigInteger lastK;
    private SecureRandom random;
    
    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }
    
    @Override
    public BigInteger getTransformValue() {
        return this.lastK;
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        SecureRandom random;
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            if (!(parametersWithRandom.getParameters() instanceof ECPublicKeyParameters)) {
                throw new IllegalArgumentException("ECPublicKeyParameters are required for new randomness transform.");
            }
            this.key = (ECPublicKeyParameters)parametersWithRandom.getParameters();
            random = parametersWithRandom.getRandom();
        }
        else {
            if (!(cipherParameters instanceof ECPublicKeyParameters)) {
                throw new IllegalArgumentException("ECPublicKeyParameters are required for new randomness transform.");
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
            final ECPoint[] array = { basePointMultiplier.multiply(parameters.getG(), generateK).add(ecPair.getX()), this.key.getQ().multiply(generateK).add(ecPair.getY()) };
            parameters.getCurve().normalizeAll(array);
            this.lastK = generateK;
            return new ECPair(array[0], array[1]);
        }
        throw new IllegalStateException("ECNewRandomnessTransform not initialised");
    }
}
