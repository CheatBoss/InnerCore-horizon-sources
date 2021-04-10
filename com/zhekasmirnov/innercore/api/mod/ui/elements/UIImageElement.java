package com.zhekasmirnov.innercore.api.mod.ui.elements;

import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import org.mozilla.javascript.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.api.mod.*;

public class UIImageElement extends UIElement
{
    public float height;
    public Texture overlay;
    public Texture texture;
    public float textureScale;
    public float width;
    
    public UIImageElement(final UIWindow uiWindow, final ScriptableObject scriptableObject) {
        super(uiWindow, scriptableObject);
    }
    
    private void drawImage(final Canvas canvas, final float n) {
        this.texture.draw(canvas, this.x * n, this.y * n, n);
        if (this.overlay != null) {
            this.overlay.draw(canvas, this.x * n, this.y * n, n);
        }
    }
    
    public boolean isAnimated() {
        return this.texture.isAnimation || (this.overlay != null && this.overlay.isAnimation);
    }
    
    @Override
    public void onBindingUpdated(final String s, final Object o) {
    }
    
    @Override
    public void onDraw(final Canvas canvas, final float n) {
        this.drawImage(canvas, n);
    }
    
    @Override
    public void onRelease() {
        super.onRelease();
        this.texture.release();
        if (this.overlay != null) {
            this.overlay.release();
        }
    }
    
    @Override
    public void onSetup(final ScriptableObject scriptableObject) {
        this.width = ScriptableObjectHelper.getFloatProperty(scriptableObject, "width", 0.0f);
        this.height = ScriptableObjectHelper.getFloatProperty(scriptableObject, "height", 0.0f);
        this.textureScale = this.optFloatFromDesctiption("scale", 1.0f);
        this.texture = new Texture(this.optValFromDescription("bitmap", null));
        if (this.width > 0.0f && this.height > 0.0f) {
            this.texture.resizeAll(this.width, this.height);
            this.setSize(this.width, this.height);
        }
        else {
            this.texture.rescaleAll(this.textureScale);
            this.setSize(this.texture.getWidth(), this.texture.getHeight());
        }
        final Object optValFromDescription = this.optValFromDescription("overlay", null);
        if (optValFromDescription != null) {
            (this.overlay = new Texture(optValFromDescription)).rescaleAll(this.textureScale);
        }
    }
}
