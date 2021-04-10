package com.android.dx.cf.iface;

import com.android.dex.util.*;

public class ParseException extends ExceptionWithContext
{
    public ParseException(final String s) {
        super(s);
    }
    
    public ParseException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public ParseException(final Throwable t) {
        super(t);
    }
}
