package com.zhekasmirnov.innercore.api.mod.ui.elements;

import com.zhekasmirnov.innercore.mod.build.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import org.mozilla.javascript.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;

public class UIScrollElement extends UIElement
{
    private float _value;
    private Texture background;
    private Texture backgroundHover;
    private float backgroundOffset;
    private ScriptableObject bindingObject;
    private String bindingObjectProperty;
    private Config.ConfigValue configValue;
    private float divider;
    private Texture handle;
    private Texture handleHover;
    private float handleOffset;
    private boolean isIntValue;
    private float length;
    private float maxValue;
    private float minValue;
    private float value;
    private float width;
    
    public UIScrollElement(final UIWindow uiWindow, final ScriptableObject scriptableObject) {
        super(uiWindow, scriptableObject);
        this.isIntValue = false;
        this.value = 0.0f;
        this._value = 0.0f;
        this.minValue = 0.0f;
        this.maxValue = 1.0f;
        this.backgroundOffset = 0.0f;
        this.handleOffset = 0.0f;
    }
    
    private float toRaw(final float n) {
        return (n - this.minValue) / (this.maxValue - this.minValue);
    }
    
    @Override
    public void onBindingUpdated(final String s, final Object o) {
        if (s.equals("value")) {
            this.setBinding("raw-value", this.toRaw((float)o));
        }
        if (s.equals("raw-value")) {
            this._value = (float)o;
            this.value = Math.round(this._value * this.divider) / this.divider;
            float n2;
            final float n = n2 = this.value * (this.maxValue - this.minValue) + this.minValue;
            if (this.isIntValue) {
                n2 = (float)Math.round(n);
            }
            this.callDescriptionMethodSafe("onNewValue", n2, this.container, this);
            if (this.bindingObject != null) {
                this.bindingObject.put(this.bindingObjectProperty, (Scriptable)this.bindingObject, (Object)n2);
            }
            if (this.configValue != null) {
                this.configValue.set(n2);
            }
        }
    }
    
    @Override
    public void onDraw(final Canvas canvas, final float n) {
        Texture texture;
        if (this.isTouched) {
            texture = this.backgroundHover;
        }
        else {
            texture = this.background;
        }
        texture.draw(canvas, this.x * n, (this.y + this.backgroundOffset) * n, n);
        Texture texture2;
        if (this.isTouched) {
            texture2 = this.handleHover;
        }
        else {
            texture2 = this.handle;
        }
        texture2.draw(canvas, (this.x - this.handleOffset + this._value * this.length) * n, this.y * n, n);
        if (!this.isTouched) {
            this._value = this.value;
        }
    }
    
    @Override
    public void onRelease() {
        super.onRelease();
        this.handle.release();
        this.handleHover.release();
        this.background.release();
        this.backgroundHover.release();
    }
    
    @Override
    public void onSetup(final ScriptableObject scriptableObject) {
        this.isIntValue = ScriptableObjectHelper.getBooleanProperty(scriptableObject, "isInt", false);
        this.width = ScriptableObjectHelper.getFloatProperty(scriptableObject, "width", 64.0f);
        this.length = ScriptableObjectHelper.getFloatProperty(scriptableObject, "length", 500.0f);
        this.minValue = ScriptableObjectHelper.getFloatProperty(scriptableObject, "min", 0.0f);
        this.maxValue = ScriptableObjectHelper.getFloatProperty(scriptableObject, "max", 1.0f);
        int n;
        if (this.isIntValue) {
            n = (int)Math.abs(this.maxValue - this.minValue);
        }
        else {
            n = 1000;
        }
        this.divider = (float)ScriptableObjectHelper.getIntProperty(scriptableObject, "divider", n);
        if (this.divider <= 0.0f) {
            this.divider = 1.0f;
        }
        this.bindingObject = ScriptableObjectHelper.getScriptableObjectProperty(scriptableObject, "bindingObject", null);
        if (this.bindingObject != null) {
            this.bindingObjectProperty = ScriptableObjectHelper.getStringProperty(scriptableObject, "bindingProperty", null);
            if (this.bindingObjectProperty == null) {
                this.bindingObjectProperty = "value";
            }
            this.setBinding("value", ScriptableObjectHelper.getFloatProperty(this.bindingObject, this.bindingObjectProperty, 0.0f));
        }
        else {
            this.setBinding("value", ScriptableObjectHelper.getFloatProperty(scriptableObject, "value", 0.0f));
        }
        final Object javaProperty = ScriptableObjectHelper.getJavaProperty(scriptableObject, "configValue", Config.ConfigValue.class, null);
        if (javaProperty != null) {
            final Object value = ((Config.ConfigValue)javaProperty).get();
            if (value instanceof Number) {
                this.configValue = (Config.ConfigValue)javaProperty;
                this.setBinding("value", ((Number)value).floatValue());
            }
        }
        this.handle = new Texture(ScriptableObjectHelper.getProperty(scriptableObject, "bitmapHandle", "default_scroll_handle"));
        if (scriptableObject.has("bitmapHandleHover", (Scriptable)scriptableObject)) {
            this.handleHover = new Texture(ScriptableObjectHelper.getProperty(scriptableObject, "bitmapHandleHover", "default_scroll_handle_hover"));
        }
        else if (scriptableObject.has("bitmapHandle", (Scriptable)scriptableObject)) {
            this.handleHover = this.handle;
        }
        else {
            this.handleHover = new Texture("default_scroll_handle_hover");
        }
        final FrameTexture frameTexture = FrameTextureSource.getFrameTexture(ScriptableObjectHelper.getStringProperty(scriptableObject, "bitmapBg", "default_scroll_bg"));
        FrameTexture frameTexture2;
        if (scriptableObject.has("bitmapBgHover", (Scriptable)scriptableObject)) {
            frameTexture2 = FrameTextureSource.getFrameTexture(ScriptableObjectHelper.getStringProperty(scriptableObject, "bitmapBgHover", "default_scroll_bg_hover"));
        }
        else if (scriptableObject.has("bitmapHandle", (Scriptable)scriptableObject)) {
            frameTexture2 = frameTexture;
        }
        else {
            frameTexture2 = FrameTextureSource.getFrameTexture("default_scroll_bg_hover");
        }
        final float floatProperty = ScriptableObjectHelper.getFloatProperty(scriptableObject, "ratio", 0.6f);
        this.background = new Texture(frameTexture.expandAndScale(this.length, this.width * floatProperty, this.width / 16.0f, frameTexture.getCentralColor()));
        this.backgroundHover = new Texture(frameTexture2.expandAndScale(this.length, this.width * floatProperty, this.width / 16.0f, frameTexture2.getCentralColor()));
        final float n2 = this.handle.getHeight() / this.handle.getWidth();
        this.handle.resizeAll((float)(int)(this.width / n2), (float)(int)this.width);
        this.handleHover.resizeAll((float)(int)(this.width / n2), (float)(int)this.width);
        this.backgroundOffset = (this.handle.getHeight() - this.background.getHeight()) / 2.0f;
        this.handleOffset = this.handle.getWidth() / 2.0f;
        this.x += this.handleOffset;
        this.setSize(this.length, this.width);
    }
    
    @Override
    public void onTouchEvent(final TouchEvent touchEvent) {
        super.onTouchEvent(touchEvent);
        this.setBinding("raw-value", (float)Math.min(1.0, Math.max(0.0, touchEvent.localX * 1.05 - 0.025)));
    }
}
