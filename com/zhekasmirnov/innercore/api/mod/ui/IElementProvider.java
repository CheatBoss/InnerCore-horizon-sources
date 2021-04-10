package com.zhekasmirnov.innercore.api.mod.ui;

import com.zhekasmirnov.innercore.api.mod.ui.elements.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;

public interface IElementProvider
{
    void addOrRefreshElement(final UIElement p0);
    
    UIStyle getStyleFor(final UIElement p0);
    
    void invalidateAll();
    
    void releaseAll();
    
    void removeElement(final UIElement p0);
    
    void resetAll();
    
    void runCachePreparation();
    
    void setBackgroundProvider(final IBackgroundProvider p0);
    
    void setWindowStyle(final UIStyle p0);
}
