package com.zhekasmirnov.innercore.api.mod.ui.elements;

import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import org.mozilla.javascript.*;
import android.graphics.*;

public class UIButtonElement extends UIElement
{
    private Texture normalTexture;
    private Texture pressedTexture;
    private float scale;
    
    public UIButtonElement(final UIWindow uiWindow, final ScriptableObject scriptableObject) {
        super(uiWindow, scriptableObject);
    }
    
    @Override
    public void onBindingUpdated(final String s, final Object o) {
    }
    
    @Override
    public void onDraw(final Canvas canvas, final float n) {
        if (this.isTouched) {
            this.pressedTexture.draw(canvas, this.x * n, this.y * n, n);
            return;
        }
        this.normalTexture.draw(canvas, this.x * n, this.y * n, n);
    }
    
    @Override
    public void onSetup(final ScriptableObject scriptableObject) {
        this.scale = this.optFloatFromDesctiption("scale", 1.0f);
        (this.normalTexture = new Texture(this.optValFromDescription("bitmap", null), this.style)).rescaleAll(this.scale);
        final Object optValFromDescription = this.optValFromDescription("bitmap2", null);
        if (optValFromDescription != null) {
            (this.pressedTexture = new Texture(optValFromDescription, this.style)).rescaleAll(this.scale);
        }
        else {
            this.pressedTexture = this.normalTexture;
        }
        this.setSize(this.normalTexture.getWidth(), this.normalTexture.getHeight());
    }
}
