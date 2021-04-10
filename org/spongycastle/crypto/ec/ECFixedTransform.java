package org.spongycastle.crypto.ec;

import java.math.*;
import org.spongycastle.crypto.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.crypto.params.*;

public class ECFixedTransform implements ECPairFactorTransform
{
    private BigInteger k;
    private ECPublicKeyParameters key;
    
    public ECFixedTransform(final BigInteger k) {
        this.k = k;
    }
    
    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }
    
    @Override
    public BigInteger getTransformValue() {
        return this.k;
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        if (cipherParameters instanceof ECPublicKeyParameters) {
            this.key = (ECPublicKeyParameters)cipherParameters;
            return;
        }
        throw new IllegalArgumentException("ECPublicKeyParameters are required for fixed transform.");
    }
    
    @Override
    public ECPair transform(final ECPair ecPair) {
        final ECPublicKeyParameters key = this.key;
        if (key != null) {
            final ECDomainParameters parameters = key.getParameters();
            final BigInteger n = parameters.getN();
            final ECMultiplier basePointMultiplier = this.createBasePointMultiplier();
            final BigInteger mod = this.k.mod(n);
            final ECPoint[] array = { basePointMultiplier.multiply(parameters.getG(), mod).add(ecPair.getX()), this.key.getQ().multiply(mod).add(ecPair.getY()) };
            parameters.getCurve().normalizeAll(array);
            return new ECPair(array[0], array[1]);
        }
        throw new IllegalStateException("ECFixedTransform not initialised");
    }
}
