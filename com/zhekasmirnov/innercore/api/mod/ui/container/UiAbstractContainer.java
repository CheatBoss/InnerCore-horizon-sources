package com.zhekasmirnov.innercore.api.mod.ui.container;

import com.zhekasmirnov.innercore.api.mod.ui.elements.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;

public interface UiAbstractContainer
{
    void addElementInstance(final UIElement p0, final String p1);
    
    void close();
    
    Object getBinding(final String p0, final String p1);
    
    UIElement getElement(final String p0);
    
    Object getParent();
    
    UiVisualSlotImpl getSlotVisualImpl(final String p0);
    
    void handleBindingDirty(final String p0, final String p1);
    
    void handleInventoryToSlotTransaction(final int p0, final String p1, final int p2);
    
    void handleSlotToInventoryTransaction(final String p0, final int p1);
    
    void handleSlotToSlotTransaction(final String p0, final String p1, final int p2);
    
    void onWindowClosed();
    
    void openAs(final IWindow p0);
    
    void setBinding(final String p0, final String p1, final Object p2);
}
