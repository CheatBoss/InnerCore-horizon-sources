package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.*;

public class RSAEngine implements AsymmetricBlockCipher
{
    private RSACoreEngine core;
    
    @Override
    public int getInputBlockSize() {
        return this.core.getInputBlockSize();
    }
    
    @Override
    public int getOutputBlockSize() {
        return this.core.getOutputBlockSize();
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        if (this.core == null) {
            this.core = new RSACoreEngine();
        }
        this.core.init(b, cipherParameters);
    }
    
    @Override
    public byte[] processBlock(final byte[] array, final int n, final int n2) {
        final RSACoreEngine core = this.core;
        if (core != null) {
            return core.convertOutput(core.processBlock(core.convertInput(array, n, n2)));
        }
        throw new IllegalStateException("RSA engine not initialised");
    }
}
