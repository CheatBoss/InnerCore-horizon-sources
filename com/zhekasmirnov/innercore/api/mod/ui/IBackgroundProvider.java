package com.zhekasmirnov.innercore.api.mod.ui;

import com.zhekasmirnov.innercore.api.mod.ui.background.*;

public interface IBackgroundProvider
{
    void addDrawing(final IDrawing p0);
    
    void clearAll();
    
    void prepareCache();
    
    void releaseCache();
    
    void setBackgroundColor(final int p0);
}
