package com.zhekasmirnov.innercore.api.mod.ui.background;

import android.graphics.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import com.zhekasmirnov.innercore.api.mod.*;

public class DrawText implements IDrawing
{
    private Font font;
    private String text;
    private float x;
    private float y;
    
    @Override
    public void onDraw(final Canvas canvas, final float n) {
        this.font.drawText(canvas, this.x * n, this.y * n, this.text, n);
    }
    
    @Override
    public void onSetup(final ScriptableObject scriptableObject, final UIStyle uiStyle) {
        this.x = ScriptableObjectHelper.getFloatProperty(scriptableObject, "x", 0.0f);
        this.y = ScriptableObjectHelper.getFloatProperty(scriptableObject, "y", 0.0f);
        this.text = ScriptableObjectHelper.getStringProperty(scriptableObject, "text", "");
        final ScriptableObject scriptableObjectProperty = ScriptableObjectHelper.getScriptableObjectProperty(scriptableObject, "font", null);
        if (scriptableObjectProperty != null) {
            this.font = new Font(scriptableObjectProperty);
            return;
        }
        this.font = new Font(scriptableObject);
    }
}
