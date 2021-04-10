package com.android.dx.cf.code;

import com.android.dex.util.*;

public class SimException extends ExceptionWithContext
{
    public SimException(final String s) {
        super(s);
    }
    
    public SimException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public SimException(final Throwable t) {
        super(t);
    }
}
