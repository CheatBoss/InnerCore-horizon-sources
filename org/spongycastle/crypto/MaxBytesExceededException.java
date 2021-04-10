package org.spongycastle.crypto;

public class MaxBytesExceededException extends RuntimeCryptoException
{
    public MaxBytesExceededException() {
    }
    
    public MaxBytesExceededException(final String s) {
        super(s);
    }
}
