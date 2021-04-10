package org.spongycastle.crypto.engines;

import java.math.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class RSABlindingEngine implements AsymmetricBlockCipher
{
    private BigInteger blindingFactor;
    private RSACoreEngine core;
    private boolean forEncryption;
    private RSAKeyParameters key;
    
    public RSABlindingEngine() {
        this.core = new RSACoreEngine();
    }
    
    private BigInteger blindMessage(final BigInteger bigInteger) {
        return bigInteger.multiply(this.blindingFactor.modPow(this.key.getExponent(), this.key.getModulus())).mod(this.key.getModulus());
    }
    
    private BigInteger unblindMessage(final BigInteger bigInteger) {
        final BigInteger modulus = this.key.getModulus();
        return bigInteger.multiply(this.blindingFactor.modInverse(modulus)).mod(modulus);
    }
    
    @Override
    public int getInputBlockSize() {
        return this.core.getInputBlockSize();
    }
    
    @Override
    public int getOutputBlockSize() {
        return this.core.getOutputBlockSize();
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        CipherParameters parameters = cipherParameters;
        if (cipherParameters instanceof ParametersWithRandom) {
            parameters = ((ParametersWithRandom)cipherParameters).getParameters();
        }
        final RSABlindingParameters rsaBlindingParameters = (RSABlindingParameters)parameters;
        this.core.init(forEncryption, rsaBlindingParameters.getPublicKey());
        this.forEncryption = forEncryption;
        this.key = rsaBlindingParameters.getPublicKey();
        this.blindingFactor = rsaBlindingParameters.getBlindingFactor();
    }
    
    @Override
    public byte[] processBlock(final byte[] array, final int n, final int n2) {
        final BigInteger convertInput = this.core.convertInput(array, n, n2);
        BigInteger bigInteger;
        if (this.forEncryption) {
            bigInteger = this.blindMessage(convertInput);
        }
        else {
            bigInteger = this.unblindMessage(convertInput);
        }
        return this.core.convertOutput(bigInteger);
    }
}
