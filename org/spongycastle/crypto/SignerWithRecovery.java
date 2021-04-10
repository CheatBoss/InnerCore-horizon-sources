package org.spongycastle.crypto;

public interface SignerWithRecovery extends Signer
{
    byte[] getRecoveredMessage();
    
    boolean hasFullMessage();
    
    void updateWithRecoveredMessage(final byte[] p0) throws InvalidCipherTextException;
}
