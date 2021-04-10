package org.spongycastle.crypto;

import org.spongycastle.crypto.params.*;

public class AsymmetricCipherKeyPair
{
    private AsymmetricKeyParameter privateParam;
    private AsymmetricKeyParameter publicParam;
    
    public AsymmetricCipherKeyPair(final CipherParameters cipherParameters, final CipherParameters cipherParameters2) {
        this.publicParam = (AsymmetricKeyParameter)cipherParameters;
        this.privateParam = (AsymmetricKeyParameter)cipherParameters2;
    }
    
    public AsymmetricCipherKeyPair(final AsymmetricKeyParameter publicParam, final AsymmetricKeyParameter privateParam) {
        this.publicParam = publicParam;
        this.privateParam = privateParam;
    }
    
    public AsymmetricKeyParameter getPrivate() {
        return this.privateParam;
    }
    
    public AsymmetricKeyParameter getPublic() {
        return this.publicParam;
    }
}
