package com.zhekasmirnov.innercore.api.mod.ui.elements;

import com.zhekasmirnov.innercore.mod.build.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import org.mozilla.javascript.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;

public class UISwitchElement extends UIElement
{
    private ScriptableObject bindingObject;
    private String bindingObjectProperty;
    private Config.ConfigValue configValue;
    private boolean state;
    private Texture textureHoverOff;
    private Texture textureHoverOn;
    private Texture textureOff;
    private Texture textureOn;
    
    public UISwitchElement(final UIWindow uiWindow, final ScriptableObject scriptableObject) {
        super(uiWindow, scriptableObject);
        this.state = false;
    }
    
    @Override
    public void onBindingUpdated(final String s, final Object o) {
        if (s.equals("state")) {
            this.callDescriptionMethodSafe("onNewState", o, this.container, this);
            this.state = (boolean)o;
            if (this.bindingObject != null) {
                this.bindingObject.put(this.bindingObjectProperty, (Scriptable)this.bindingObject, (Object)this.state);
            }
            if (this.configValue != null) {
                this.configValue.set(o);
            }
        }
    }
    
    @Override
    public void onDraw(final Canvas canvas, final float n) {
        Texture texture;
        if (this.state) {
            if (this.isTouched) {
                texture = this.textureHoverOn;
            }
            else {
                texture = this.textureOn;
            }
        }
        else if (this.isTouched) {
            texture = this.textureHoverOff;
        }
        else {
            texture = this.textureOff;
        }
        texture.draw(canvas, this.x * n, this.y * n, n);
    }
    
    @Override
    public void onRelease() {
        super.onRelease();
        this.textureOn.release();
        this.textureOff.release();
        this.textureHoverOn.release();
        this.textureHoverOff.release();
    }
    
    @Override
    public void onSetup(final ScriptableObject scriptableObject) {
        this.bindingObject = ScriptableObjectHelper.getScriptableObjectProperty(scriptableObject, "bindingObject", null);
        if (this.bindingObject != null) {
            this.bindingObjectProperty = ScriptableObjectHelper.getStringProperty(scriptableObject, "bindingProperty", null);
            if (this.bindingObjectProperty == null) {
                this.bindingObjectProperty = "on";
            }
            this.setBinding("state", ScriptableObjectHelper.getBooleanProperty(this.bindingObject, this.bindingObjectProperty, false));
        }
        else {
            this.setBinding("state", ScriptableObjectHelper.getBooleanProperty(scriptableObject, "state", false));
        }
        final Object javaProperty = ScriptableObjectHelper.getJavaProperty(scriptableObject, "configValue", Config.ConfigValue.class, null);
        if (javaProperty != null) {
            final Object value = ((Config.ConfigValue)javaProperty).get();
            if (value instanceof Boolean) {
                this.configValue = (Config.ConfigValue)javaProperty;
                this.setBinding("state", value);
            }
        }
        this.textureOn = new Texture(ScriptableObjectHelper.getProperty(scriptableObject, "bitmapOn", "default_switch_on"));
        if (scriptableObject.has("bitmapOnHover", (Scriptable)scriptableObject)) {
            this.textureHoverOn = new Texture(ScriptableObjectHelper.getProperty(scriptableObject, "bitmapOnHover", "default_switch_on_hover"));
        }
        else if (scriptableObject.has("bitmapOn", (Scriptable)scriptableObject)) {
            this.textureHoverOn = this.textureOn;
        }
        else {
            this.textureHoverOn = new Texture("default_switch_on_hover");
        }
        this.textureOff = new Texture(ScriptableObjectHelper.getProperty(scriptableObject, "bitmapOff", "default_switch_off"));
        if (scriptableObject.has("bitmapOffHover", (Scriptable)scriptableObject)) {
            this.textureHoverOff = new Texture(ScriptableObjectHelper.getProperty(scriptableObject, "bitmapOffHover", "default_switch_off_hover"));
        }
        else if (scriptableObject.has("bitmapOff", (Scriptable)scriptableObject)) {
            this.textureHoverOff = this.textureOff;
        }
        else {
            this.textureHoverOff = new Texture("default_switch_off_hover");
        }
        final float floatProperty = ScriptableObjectHelper.getFloatProperty(scriptableObject, "scale", 4.0f);
        this.textureOn.rescaleAll(floatProperty);
        this.textureHoverOn.rescaleAll(floatProperty);
        this.textureOff.rescaleAll(floatProperty);
        this.textureHoverOff.rescaleAll(floatProperty);
        this.setSize(this.textureOn.getWidth(), this.textureOn.getHeight());
    }
    
    @Override
    public void onTouchEvent(final TouchEvent touchEvent) {
        super.onTouchEvent(touchEvent);
        if (touchEvent.type == TouchEventType.CLICK || touchEvent.type == TouchEventType.LONG_CLICK) {
            this.setBinding("state", this.state ^ true);
            this.container.handleBindingDirty(this.elementName, "state");
        }
    }
}
