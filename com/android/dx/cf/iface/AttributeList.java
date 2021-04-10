package com.android.dx.cf.iface;

public interface AttributeList
{
    int byteLength();
    
    Attribute findFirst(final String p0);
    
    Attribute findNext(final Attribute p0);
    
    Attribute get(final int p0);
    
    boolean isMutable();
    
    int size();
}
