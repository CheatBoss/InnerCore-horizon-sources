package com.android.dx.cf.iface;

import com.android.dx.util.*;

public final class StdMethodList extends FixedSizeList implements MethodList
{
    public StdMethodList(final int n) {
        super(n);
    }
    
    @Override
    public Method get(final int n) {
        return (Method)this.get0(n);
    }
    
    public void set(final int n, final Method method) {
        this.set0(n, method);
    }
}
