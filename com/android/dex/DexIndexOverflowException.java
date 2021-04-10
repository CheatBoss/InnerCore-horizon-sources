package com.android.dex;

public final class DexIndexOverflowException extends DexException
{
    public DexIndexOverflowException(final String s) {
        super(s);
    }
    
    public DexIndexOverflowException(final Throwable t) {
        super(t);
    }
}
