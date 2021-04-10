package org.spongycastle.pqc.crypto;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class DigestingStateAwareMessageSigner extends DigestingMessageSigner
{
    private final StateAwareMessageSigner signer;
    
    public DigestingStateAwareMessageSigner(final StateAwareMessageSigner signer, final Digest digest) {
        super(signer, digest);
        this.signer = signer;
    }
    
    public AsymmetricKeyParameter getUpdatedPrivateKey() {
        return this.signer.getUpdatedPrivateKey();
    }
}
