package com.zhekasmirnov.innercore.api.mod.ui.elements;

import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import com.zhekasmirnov.innercore.api.mod.util.*;
import com.zhekasmirnov.innercore.api.mod.ui.container.*;

public class UIInvSlotElement extends UISlotElement
{
    private int index;
    
    public UIInvSlotElement(final UIWindow uiWindow, final ScriptableObject scriptableObject) {
        super(uiWindow, scriptableObject);
        this.index = 0;
    }
    
    private void _onTouchEvent(final TouchEvent touchEvent) {
        if ((touchEvent.type == TouchEventType.CLICK || touchEvent.type == TouchEventType.LONG_CLICK) && UIInvSlotElement.currentSelectedSlot != null && UIInvSlotElement.currentSelectedSlot.slotName != null) {
            this.refresh();
            int n = Math.min(this.curCount, 1);
            if (touchEvent.type == TouchEventType.LONG_CLICK) {
                n = this.curCount;
            }
            this.container.handleInventoryToSlotTransaction(this.index, UIInvSlotElement.currentSelectedSlot.slotName, n);
        }
    }
    
    @Override
    public void onBindingUpdated(final String s, final Object o) {
    }
    
    @Override
    public void onSetup(final ScriptableObject scriptableObject) {
        super.onSetup(scriptableObject);
        this.background.release();
        (this.background = this.createTexture(this.optStringFromDesctiption("bitmap", "style:inv_slot"))).resizeAll((float)this.size, (float)this.size);
        this.index = (int)this.optFloatFromDesctiption("index", 0.0f);
        this.source = new ScriptableUiVisualSlotImpl(InventorySource.getSource(this.index));
    }
    
    @Override
    public void onTouchEvent(final TouchEvent touchEvent) {
        this._onTouchEvent(touchEvent);
    }
    
    @Override
    public void setupInitialBindings(final UiAbstractContainer container, final String s) {
        this.container = container;
    }
}
