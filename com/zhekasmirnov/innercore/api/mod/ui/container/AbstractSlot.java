package com.zhekasmirnov.innercore.api.mod.ui.container;

import com.zhekasmirnov.innercore.api.*;

public interface AbstractSlot
{
    int getCount();
    
    int getData();
    
    NativeItemInstanceExtra getExtra();
    
    int getId();
    
    void set(final int p0, final int p1, final int p2, final NativeItemInstanceExtra p3);
    
    void validate();
}
