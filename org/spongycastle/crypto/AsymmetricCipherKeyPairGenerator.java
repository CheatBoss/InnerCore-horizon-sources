package org.spongycastle.crypto;

public interface AsymmetricCipherKeyPairGenerator
{
    AsymmetricCipherKeyPair generateKeyPair();
    
    void init(final KeyGenerationParameters p0);
}
