package com.zhekasmirnov.innercore.api.mod.ui.elements;

import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import org.mozilla.javascript.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.api.mod.*;

public class UITextElement extends UIElement
{
    protected Font font;
    private boolean format;
    private float lastMeasuredScale;
    private int maxCharsPerLine;
    private boolean multiline;
    private String text;
    private Rect textBounds;
    private float textHeight;
    private String[] textLines;
    
    public UITextElement(final UIWindow uiWindow, final ScriptableObject scriptableObject) {
        super(uiWindow, scriptableObject);
        this.maxCharsPerLine = 0;
        this.lastMeasuredScale = -1.0f;
        this.textBounds = new Rect();
        this.textHeight = 0.0f;
    }
    
    private void measureTextIfNeeded(float max) {
        if (max != this.lastMeasuredScale) {
            this.lastMeasuredScale = max;
            this.textBounds = this.font.getBounds(this.text, this.x * max, this.y * max, 1.0f);
            this.textHeight = this.font.getTextHeight(this.text, this.x * max, this.y * max, max);
            if (this.multiline) {
                max = 0.0f;
                float n = 0.0f;
                final String[] textLines = this.textLines;
                for (int length = textLines.length, i = 0; i < length; ++i) {
                    final Rect bounds = this.font.getBounds(textLines[i], 0.0f, 0.0f, 1.0f);
                    n += this.textHeight * 1.1f;
                    max = Math.max(max, (float)bounds.width());
                }
                this.setSize(max, n);
                return;
            }
            this.setSize((float)this.textBounds.width(), (float)this.textBounds.height());
        }
    }
    
    @Override
    public void onBindingUpdated(final String s, Object o) {
        if (s.equals("text")) {
            this.text = (String)o;
            if (this.format) {
                this.text = this.text.replaceAll("\n", " \n");
                final String[] split = this.text.split(" ");
                this.text = "";
                int n = 0;
                for (int length = split.length, i = 0; i < length; ++i) {
                    final String s2 = split[i];
                    final StringBuilder sb = new StringBuilder();
                    sb.append(this.text);
                    sb.append(s2);
                    sb.append(" ");
                    this.text = sb.toString();
                    final int length2 = s2.trim().length();
                    int n2 = n + (length2 + 1);
                    if (s2.contains("\n")) {
                        n2 = length2;
                    }
                    if ((n = n2) > this.maxCharsPerLine) {
                        o = new StringBuilder();
                        ((StringBuilder)o).append(this.text);
                        ((StringBuilder)o).append("\n");
                        this.text = ((StringBuilder)o).toString();
                        n = 0;
                    }
                }
            }
            if (this.multiline) {
                this.textLines = this.text.split("\n");
            }
            this.lastMeasuredScale = -1.0f;
        }
    }
    
    @Override
    public void onDraw(final Canvas canvas, final float n) {
        this.measureTextIfNeeded(n);
        if (this.multiline) {
            for (int i = 0; i < this.textLines.length; ++i) {
                this.font.drawText(canvas, this.x * n, this.y * n + this.textHeight + i * this.textHeight * 1.1f, this.textLines[i], n);
            }
        }
        else {
            this.font.drawText(canvas, this.x * n, this.y * n + this.textHeight, this.text, n);
        }
    }
    
    @Override
    public void onSetup(final ScriptableObject scriptableObject) {
        this.font = new Font(ScriptableObjectHelper.getScriptableObjectProperty(scriptableObject, "font", scriptableObject));
        this.multiline = ScriptableObjectHelper.getBooleanProperty(scriptableObject, "multiline", false);
        this.format = ScriptableObjectHelper.getBooleanProperty(scriptableObject, "format", false);
        this.maxCharsPerLine = ScriptableObjectHelper.getIntProperty(scriptableObject, "formatMaxCharsPerLine", 999);
        this.setBinding("text", this.text = ScriptableObjectHelper.getStringProperty(scriptableObject, "text", ""));
    }
}
