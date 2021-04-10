package com.android.dx.util;

import com.android.dex.util.*;

public class MutabilityException extends ExceptionWithContext
{
    public MutabilityException(final String s) {
        super(s);
    }
    
    public MutabilityException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public MutabilityException(final Throwable t) {
        super(t);
    }
}
