package com.zhekasmirnov.innercore.api.mod.ui.elements;

import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;

public class UITabElement extends UIFrameElement
{
    private int deselectedColor;
    private boolean isAlwaysSelected;
    private int selectedColor;
    private int tabIndex;
    
    public UITabElement(final UIWindow uiWindow, final ScriptableObject scriptableObject) {
        super(uiWindow, scriptableObject);
        this.isAlwaysSelected = false;
    }
    
    private void callToParent() {
        final IWindow parentWindow = this.window.getParentWindow();
        if (parentWindow != null && parentWindow instanceof UITabbedWindow) {
            ((UITabbedWindow)parentWindow).onTabSelected(this.tabIndex);
        }
    }
    
    private void deselect() {
        this.description.put("isSelected", (Scriptable)this.description, (Object)false);
        this.description.put("z", (Scriptable)this.description, (Object)(-1));
    }
    
    private void select() {
        this.description.put("isSelected", (Scriptable)this.description, (Object)true);
        this.description.put("z", (Scriptable)this.description, (Object)1);
    }
    
    @Override
    public void onReset() {
        super.onReset();
        final IWindow parentWindow = this.window.getParentWindow();
        if (parentWindow instanceof UITabbedWindow) {
            final int defaultTab = ((UITabbedWindow)parentWindow).getDefaultTab();
            final UITabElement uiTabElement = (UITabElement)this.window.getProperty("selectedTab");
            if (uiTabElement != null && defaultTab == this.tabIndex) {
                uiTabElement.deselect();
                this.select();
            }
        }
    }
    
    @Override
    public void onSetup(final ScriptableObject scriptableObject) {
        super.onSetup(scriptableObject);
        this.selectedColor = ScriptableObjectHelper.getIntProperty(scriptableObject, "selectedColor", this.color);
        this.deselectedColor = ScriptableObjectHelper.getIntProperty(scriptableObject, "deselectedColor", this.color);
        this.tabIndex = ScriptableObjectHelper.getIntProperty(scriptableObject, "tabIndex", -1);
        if (ScriptableObjectHelper.getBooleanProperty(scriptableObject, "isAlwaysSelected", false)) {
            this.isAlwaysSelected = true;
            this.z = 1.0f;
            if (this.selectedColor != -1) {
                this.color = this.selectedColor;
            }
        }
        else if (ScriptableObjectHelper.getBooleanProperty(scriptableObject, "isSelected", false)) {
            this.window.putProperty("selectedTab", this);
            this.z = 1.0f;
            if (this.selectedColor != -1) {
                this.color = this.selectedColor;
            }
        }
        else {
            this.z = -1.0f;
            if (this.deselectedColor != -1) {
                this.color = this.deselectedColor;
            }
        }
    }
    
    @Override
    public void onTouchEvent(final TouchEvent touchEvent) {
        super.onTouchEvent(touchEvent);
        if (touchEvent.type == TouchEventType.LONG_CLICK || touchEvent.type == TouchEventType.CLICK) {
            final UITabElement uiTabElement = (UITabElement)this.window.getProperty("selectedTab");
            if (!this.isAlwaysSelected && uiTabElement != this) {
                this.callToParent();
                if (uiTabElement != null) {
                    uiTabElement.deselect();
                }
                this.select();
            }
            if (this.isAlwaysSelected) {
                this.callToParent();
            }
        }
    }
}
