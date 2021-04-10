package com.android.dex;

import com.android.dex.util.*;

public class DexException extends ExceptionWithContext
{
    public DexException(final String s) {
        super(s);
    }
    
    public DexException(final Throwable t) {
        super(t);
    }
}
