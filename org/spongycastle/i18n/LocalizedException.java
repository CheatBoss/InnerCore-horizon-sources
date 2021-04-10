package org.spongycastle.i18n;

import java.util.*;

public class LocalizedException extends Exception
{
    private Throwable cause;
    protected ErrorBundle message;
    
    public LocalizedException(final ErrorBundle message) {
        super(message.getText(Locale.getDefault()));
        this.message = message;
    }
    
    public LocalizedException(final ErrorBundle message, final Throwable cause) {
        super(message.getText(Locale.getDefault()));
        this.message = message;
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
    
    public ErrorBundle getErrorMessage() {
        return this.message;
    }
}
