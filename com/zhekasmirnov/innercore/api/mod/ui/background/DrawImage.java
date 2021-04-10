package com.zhekasmirnov.innercore.api.mod.ui.background;

import android.graphics.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import com.zhekasmirnov.innercore.api.mod.*;

public class DrawImage implements IDrawing
{
    private float height;
    private boolean needResize;
    private float scale;
    private Texture texture;
    private float width;
    private float x;
    private float y;
    
    @Override
    public void onDraw(final Canvas canvas, final float n) {
        if (this.needResize) {
            this.texture.resizeAll((float)(int)(this.width * n), (float)(int)(this.height * n));
            this.texture.draw(canvas, this.x * n, this.y * n, 1.0f);
            return;
        }
        this.texture.draw(canvas, this.x * n, this.y * n, n);
    }
    
    @Override
    public void onSetup(final ScriptableObject scriptableObject, final UIStyle uiStyle) {
        this.x = ScriptableObjectHelper.getFloatProperty(scriptableObject, "x", 0.0f);
        this.y = ScriptableObjectHelper.getFloatProperty(scriptableObject, "y", 0.0f);
        this.width = ScriptableObjectHelper.getFloatProperty(scriptableObject, "width", 0.0f);
        this.height = ScriptableObjectHelper.getFloatProperty(scriptableObject, "height", 0.0f);
        this.scale = ScriptableObjectHelper.getFloatProperty(scriptableObject, "scale", 1.0f);
        this.texture = new Texture(ScriptableObjectHelper.getProperty(scriptableObject, "bitmap", null));
        if (this.width > 0.0f && this.height > 0.0f) {
            this.needResize = true;
            return;
        }
        this.needResize = false;
        this.texture.rescaleAll(this.scale);
    }
}
