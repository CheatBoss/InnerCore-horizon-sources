package org.spongycastle.pqc.crypto;

import org.spongycastle.crypto.*;

public interface MessageEncryptor
{
    void init(final boolean p0, final CipherParameters p1);
    
    byte[] messageDecrypt(final byte[] p0) throws InvalidCipherTextException;
    
    byte[] messageEncrypt(final byte[] p0);
}
