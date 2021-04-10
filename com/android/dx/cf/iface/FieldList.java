package com.android.dx.cf.iface;

public interface FieldList
{
    Field get(final int p0);
    
    boolean isMutable();
    
    int size();
}
