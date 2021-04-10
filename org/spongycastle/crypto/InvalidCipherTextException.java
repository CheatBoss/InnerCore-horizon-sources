package org.spongycastle.crypto;

public class InvalidCipherTextException extends CryptoException
{
    public InvalidCipherTextException() {
    }
    
    public InvalidCipherTextException(final String s) {
        super(s);
    }
    
    public InvalidCipherTextException(final String s, final Throwable t) {
        super(s, t);
    }
}
