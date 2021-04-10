package org.spongycastle.util.encoders;

public class EncoderException extends IllegalStateException
{
    private Throwable cause;
    
    EncoderException(final String s, final Throwable cause) {
        super(s);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
