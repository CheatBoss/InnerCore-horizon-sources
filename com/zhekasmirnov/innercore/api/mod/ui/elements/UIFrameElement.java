package com.zhekasmirnov.innercore.api.mod.ui.elements;

import com.zhekasmirnov.innercore.api.mod.ui.memory.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import org.mozilla.javascript.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;

public class UIFrameElement extends UIElement
{
    private BitmapWrap cachedBitmap;
    private float cachedScale;
    protected int color;
    protected FrameTexture frame;
    protected float height;
    protected float scale;
    protected boolean[] sides;
    protected float width;
    
    public UIFrameElement(final UIWindow uiWindow, final ScriptableObject scriptableObject) {
        super(uiWindow, scriptableObject);
        this.cachedBitmap = null;
        this.cachedScale = -1.0f;
    }
    
    @Override
    public void onBindingUpdated(final String s, final Object o) {
    }
    
    @Override
    public void onDraw(final Canvas canvas, final float n) {
        this.frame.draw(canvas, new RectF(this.x * n, this.y * n, (this.x + this.width) * n, (this.y + this.height) * n), n * this.scale, this.color, this.sides);
    }
    
    @Override
    public void onRelease() {
        super.onRelease();
        if (this.cachedBitmap != null) {
            this.cachedBitmap.recycle();
        }
    }
    
    @Override
    public void onSetup(final ScriptableObject scriptableObject) {
        this.frame = FrameTextureSource.getFrameTexture(ScriptableObjectHelper.getStringProperty(scriptableObject, "bitmap", "missing_texture"), this.style);
        this.width = Math.max(16.0f, ScriptableObjectHelper.getFloatProperty(scriptableObject, "width", 0.0f));
        this.height = Math.max(16.0f, ScriptableObjectHelper.getFloatProperty(scriptableObject, "height", 0.0f));
        this.scale = ScriptableObjectHelper.getFloatProperty(scriptableObject, "scale", 1.0f);
        this.color = ScriptableObjectHelper.getIntProperty(scriptableObject, "color", -1);
        this.sides = FrameTextureSource.scriptableAsSides(ScriptableObjectHelper.getScriptableObjectProperty(scriptableObject, "sides", null));
        this.cachedScale = -1.0f;
        this.setSize(this.width, this.height);
        if (this.color == -1) {
            this.color = this.frame.getCentralColor();
        }
    }
}
