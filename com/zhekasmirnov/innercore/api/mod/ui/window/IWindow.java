package com.zhekasmirnov.innercore.api.mod.ui.window;

import com.zhekasmirnov.innercore.api.mod.ui.container.*;
import org.mozilla.javascript.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.mod.ui.elements.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;

public interface IWindow
{
    void close();
    
    void frame(final long p0);
    
    UiAbstractContainer getContainer();
    
    ScriptableObject getContent();
    
    HashMap<String, UIElement> getElements();
    
    UIStyle getStyle();
    
    void invalidateDrawing(final boolean p0);
    
    void invalidateElements(final boolean p0);
    
    boolean isDynamic();
    
    boolean isInventoryNeeded();
    
    boolean isOpened();
    
    boolean onBackPressed();
    
    void open();
    
    void setContainer(final UiAbstractContainer p0);
    
    void setDebugEnabled(final boolean p0);
}
