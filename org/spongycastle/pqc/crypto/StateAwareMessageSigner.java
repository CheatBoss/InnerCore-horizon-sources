package org.spongycastle.pqc.crypto;

import org.spongycastle.crypto.params.*;

public interface StateAwareMessageSigner extends MessageSigner
{
    AsymmetricKeyParameter getUpdatedPrivateKey();
}
