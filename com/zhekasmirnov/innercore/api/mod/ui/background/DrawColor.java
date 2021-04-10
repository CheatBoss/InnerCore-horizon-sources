package com.zhekasmirnov.innercore.api.mod.ui.background;

import android.graphics.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import com.zhekasmirnov.innercore.api.mod.*;

public class DrawColor implements IDrawing
{
    private int color;
    private PorterDuff$Mode mode;
    
    @Override
    public void onDraw(final Canvas canvas, final float n) {
        canvas.drawColor(this.color, this.mode);
    }
    
    @Override
    public void onSetup(final ScriptableObject scriptableObject, final UIStyle uiStyle) {
        this.color = (int)ScriptableObjectHelper.getFloatProperty(scriptableObject, "color", 0.0f);
        final Object property = ScriptableObjectHelper.getProperty(scriptableObject, "mode", ScriptableObjectHelper.getProperty(scriptableObject, "colorMode", PorterDuff$Mode.SRC));
        if (property instanceof PorterDuff$Mode) {
            this.mode = (PorterDuff$Mode)property;
            return;
        }
        this.mode = PorterDuff$Mode.SRC;
    }
}
