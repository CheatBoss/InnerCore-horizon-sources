package org.spongycastle.util;

public interface Memoable
{
    Memoable copy();
    
    void reset(final Memoable p0);
}
