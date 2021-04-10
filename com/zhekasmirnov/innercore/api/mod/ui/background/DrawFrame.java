package com.zhekasmirnov.innercore.api.mod.ui.background;

import android.graphics.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;

public class DrawFrame implements IDrawing
{
    private int color;
    private float h;
    private float scale;
    private boolean[] sides;
    private FrameTexture texture;
    private float w;
    private float x;
    private float y;
    
    @Override
    public void onDraw(final Canvas canvas, final float n) {
        this.texture.draw(canvas, new RectF(this.x * n, this.y * n, (this.x + this.w) * n, (this.y + this.h) * n), n * this.scale, this.color, this.sides);
    }
    
    @Override
    public void onSetup(final ScriptableObject scriptableObject, final UIStyle uiStyle) {
        this.texture = FrameTextureSource.getFrameTexture(ScriptableObjectHelper.getStringProperty(scriptableObject, "bitmap", "missing_texture"), uiStyle);
        this.sides = FrameTextureSource.scriptableAsSides(ScriptableObjectHelper.getScriptableObjectProperty(scriptableObject, "sides", null));
        this.x = ScriptableObjectHelper.getFloatProperty(scriptableObject, "x", 0.0f);
        this.y = ScriptableObjectHelper.getFloatProperty(scriptableObject, "y", 0.0f);
        this.scale = ScriptableObjectHelper.getFloatProperty(scriptableObject, "scale", 1.0f);
        this.w = ScriptableObjectHelper.getFloatProperty(scriptableObject, "width", 16.0f);
        this.h = ScriptableObjectHelper.getFloatProperty(scriptableObject, "height", 16.0f);
        this.color = ScriptableObjectHelper.getIntProperty(scriptableObject, "color", ScriptableObjectHelper.getIntProperty(scriptableObject, "bg", -1));
        if (this.color == -1) {
            this.color = this.texture.getCentralColor();
        }
    }
}
