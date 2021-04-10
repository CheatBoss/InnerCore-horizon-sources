package com.zhekasmirnov.innercore.api.mod.ui.elements;

import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import com.zhekasmirnov.innercore.api.mod.ui.container.*;

public class UICustomElement extends UIElement
{
    private ScriptableObject elementScope;
    
    public UICustomElement(final UIWindow uiWindow, final ScriptableObject scriptableObject) {
        super(uiWindow, scriptableObject);
    }
    
    public ScriptableObject getScope() {
        return this.elementScope;
    }
    
    @Override
    public void onBindingUpdated(final String s, final Object o) {
        this.callDescriptionMethodSafe("onBindingUpdated", this, s, o);
    }
    
    @Override
    public void onDraw(final Canvas canvas, final float n) {
        this.callDescriptionMethodSafe("onDraw", this, canvas, n);
    }
    
    @Override
    public void onRelease() {
        super.onRelease();
        this.callDescriptionMethodSafe("onRelease", this);
    }
    
    @Override
    public void onReset() {
        super.onReset();
        this.callDescriptionMethodSafe("onReset", this);
    }
    
    @Override
    public void onSetup(final ScriptableObject scriptableObject) {
        this.elementScope = ScriptableObjectHelper.createEmpty();
        final ScriptableObject scriptableObjectProperty = ScriptableObjectHelper.getScriptableObjectProperty(scriptableObject, "custom", null);
        if (scriptableObjectProperty != null) {
            final Object[] allIds = scriptableObjectProperty.getAllIds();
            for (int length = allIds.length, i = 0; i < length; ++i) {
                final Object o = allIds[i];
                if (o instanceof String) {
                    scriptableObject.put((String)o, (Scriptable)scriptableObject, scriptableObjectProperty.get(o));
                }
                else if (o instanceof Integer) {
                    scriptableObject.put((int)o, (Scriptable)scriptableObject, scriptableObjectProperty.get(o));
                }
            }
            this.callDescriptionMethodSafe("onSetup", this);
            this.descriptionWatcher.refresh();
            this.descriptionWatcher.validate();
        }
    }
    
    @Override
    public void onTouchReleased(final TouchEvent touchEvent) {
        super.onTouchReleased(touchEvent);
        this.callDescriptionMethodSafe("onTouchReleased", this);
    }
    
    @Override
    public void setupInitialBindings(final UiAbstractContainer uiAbstractContainer, final String s) {
        super.setupInitialBindings(uiAbstractContainer, s);
        this.callDescriptionMethodSafe("onContainerInit", this, uiAbstractContainer, s);
    }
}
