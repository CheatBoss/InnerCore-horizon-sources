package com.zhekasmirnov.innercore.api.mod.ui.types;

import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;
import android.graphics.*;

public class Font
{
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_DEFAULT = 0;
    public static final int ALIGN_END = 2;
    public int alignment;
    public int color;
    public boolean isBold;
    public boolean isCursive;
    public boolean isUnderlined;
    public float shadow;
    private Paint shadowPaint;
    public float size;
    private Paint textPaint;
    
    public Font(final int color, final float size, final float shadow) {
        this.alignment = 0;
        this.color = color;
        this.size = size;
        this.shadow = shadow;
        (this.textPaint = new Paint(1)).setTypeface(FileTools.getMcTypeface());
        (this.shadowPaint = new Paint(1)).setTypeface(FileTools.getMcTypeface());
    }
    
    public Font(final ScriptableObject scriptableObject) {
        int n = 0;
        this.alignment = 0;
        this.color = ScriptableObjectHelper.getIntProperty(scriptableObject, "color", -16777216);
        this.size = ScriptableObjectHelper.getFloatProperty(scriptableObject, "size", 20.0f);
        this.shadow = ScriptableObjectHelper.getFloatProperty(scriptableObject, "shadow", 0.0f);
        this.alignment = ScriptableObjectHelper.getIntProperty(scriptableObject, "alignment", ScriptableObjectHelper.getIntProperty(scriptableObject, "align", 0));
        if (this.alignment != 1 && this.alignment != 2) {
            this.alignment = 0;
        }
        if (!scriptableObject.has("color", (Scriptable)scriptableObject) && !scriptableObject.has("size", (Scriptable)scriptableObject) && !scriptableObject.has("shadow", (Scriptable)scriptableObject)) {
            this.color = -1;
            this.shadow = 0.45f;
        }
        this.isBold = ScriptableObjectHelper.getBooleanProperty(scriptableObject, "bold", false);
        this.isCursive = ScriptableObjectHelper.getBooleanProperty(scriptableObject, "cursive", false);
        this.isUnderlined = ScriptableObjectHelper.getBooleanProperty(scriptableObject, "underline", false);
        int n2;
        if (this.isBold) {
            n2 = 32;
        }
        else {
            n2 = 0;
        }
        if (this.isUnderlined) {
            n = 8;
        }
        final int n3 = n | n2;
        (this.textPaint = new Paint(1)).setTypeface(FileTools.getMcTypeface());
        this.textPaint.setFlags(n3);
        (this.shadowPaint = new Paint(1)).setTypeface(FileTools.getMcTypeface());
        this.shadowPaint.setFlags(n3);
    }
    
    private float[] getAlignOffset(final String s) {
        if (this.alignment == 0) {
            return new float[] { 0.0f, 0.0f };
        }
        final Rect rect = new Rect();
        this.textPaint.getTextBounds(s, 0, s.length(), rect);
        if (this.alignment == 1) {
            return new float[] { (float)((rect.left + rect.width()) / -2), (float)((rect.bottom + rect.height()) / 2) };
        }
        if (this.alignment == 2) {
            return new float[] { (float)(-rect.right), 0.0f };
        }
        return new float[] { 0.0f, 0.0f };
    }
    
    public ScriptableObject asScriptable() {
        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
        empty.put("size", (Scriptable)empty, (Object)this.size);
        empty.put("color", (Scriptable)empty, (Object)this.color);
        empty.put("shadow", (Scriptable)empty, (Object)this.shadow);
        empty.put("alignment", (Scriptable)empty, (Object)this.alignment);
        empty.put("bold", (Scriptable)empty, (Object)this.isBold);
        empty.put("cursive", (Scriptable)empty, (Object)this.isCursive);
        empty.put("underline", (Scriptable)empty, (Object)this.isUnderlined);
        return empty;
    }
    
    public void drawText(final Canvas canvas, float n, float n2, final String s, float n3) {
        n3 *= this.size;
        this.textPaint.setTextSize(n3);
        this.textPaint.setColor(this.color);
        this.shadowPaint.setTextSize(n3);
        this.shadowPaint.setColor(Color.argb((int)(this.shadow * 255.0f), 0, 0, 0));
        final float[] alignOffset = this.getAlignOffset(s);
        n += alignOffset[0];
        n2 += alignOffset[1];
        if (this.shadow > 0.0f) {
            canvas.drawText(s, this.shadow * 0.25f * n3 + n, this.shadow * 0.25f * n3 + n2, this.shadowPaint);
        }
        canvas.drawText(s, n, n2, this.textPaint);
    }
    
    public Rect getBounds(final String s, final float n, final float n2, final float n3) {
        this.textPaint.setTextSize(this.size * n3);
        final Rect rect = new Rect();
        this.textPaint.getTextBounds(s, 0, s.length(), rect);
        rect.left += (int)n;
        rect.right += (int)(this.shadow * n3 * this.size * 0.25f + n);
        rect.top += (int)n2;
        rect.bottom += (int)(this.shadow * n3 * this.size * 0.25f + n2);
        return rect;
    }
    
    public float getTextHeight(String string, final float n, final float n2, final float n3) {
        this.textPaint.setTextSize(this.size * n3);
        final Rect rect = new Rect();
        final StringBuilder sb = new StringBuilder();
        sb.append(string);
        sb.append("Max Updates Per Tick 69");
        string = sb.toString();
        this.textPaint.getTextBounds(string, 0, string.length(), rect);
        return (float)rect.height();
    }
    
    public float getTextWidth(final String s, final float n) {
        return (float)this.getBounds(s, 0.0f, 0.0f, n).width();
    }
}
