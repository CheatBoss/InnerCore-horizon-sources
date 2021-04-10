package org.spongycastle.util.encoders;

public class DecoderException extends IllegalStateException
{
    private Throwable cause;
    
    DecoderException(final String s, final Throwable cause) {
        super(s);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
