package com.zhekasmirnov.innercore.api.mod.ui.background;

import android.graphics.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import com.zhekasmirnov.innercore.api.mod.*;

public class DrawLine implements IDrawing
{
    private Paint paint;
    private float width;
    private float x1;
    private float x2;
    private float y1;
    private float y2;
    
    @Override
    public void onDraw(final Canvas canvas, final float n) {
        this.paint.setStrokeWidth(this.width * n);
        canvas.drawLine(this.x1 * n, this.y1 * n, this.x2 * n, this.y2 * n, this.paint);
    }
    
    @Override
    public void onSetup(final ScriptableObject scriptableObject, final UIStyle uiStyle) {
        this.x1 = ScriptableObjectHelper.getFloatProperty(scriptableObject, "x1", 0.0f);
        this.y1 = ScriptableObjectHelper.getFloatProperty(scriptableObject, "y1", 0.0f);
        this.x2 = ScriptableObjectHelper.getFloatProperty(scriptableObject, "x2", 0.0f);
        this.y2 = ScriptableObjectHelper.getFloatProperty(scriptableObject, "y2", 0.0f);
        (this.paint = new Paint()).setColor(ScriptableObjectHelper.getIntProperty(scriptableObject, "color", -16777216));
        this.width = ScriptableObjectHelper.getFloatProperty(scriptableObject, "width", 1.0f);
    }
}
