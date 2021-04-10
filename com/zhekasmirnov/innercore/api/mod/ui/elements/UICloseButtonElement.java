package com.zhekasmirnov.innercore.api.mod.ui.elements;

import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;

public class UICloseButtonElement extends UIButtonElement
{
    public UICloseButtonElement(final UIWindow uiWindow, final ScriptableObject scriptableObject) {
        super(uiWindow, scriptableObject);
    }
    
    @Override
    public void onTouchEvent(final TouchEvent touchEvent) {
        if (touchEvent.type == TouchEventType.CLICK || touchEvent.type == TouchEventType.LONG_CLICK) {
            this.window.close();
        }
    }
}
