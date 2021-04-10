package org.spongycastle.crypto;

public class CryptoException extends Exception
{
    private Throwable cause;
    
    public CryptoException() {
    }
    
    public CryptoException(final String s) {
        super(s);
    }
    
    public CryptoException(final String s, final Throwable cause) {
        super(s);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
