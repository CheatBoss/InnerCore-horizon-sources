package org.spongycastle.util;

public interface Selector<T> extends Cloneable
{
    Object clone();
    
    boolean match(final T p0);
}
