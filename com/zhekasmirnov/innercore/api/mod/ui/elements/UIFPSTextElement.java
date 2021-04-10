package com.zhekasmirnov.innercore.api.mod.ui.elements;

import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import org.mozilla.javascript.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;

public class UIFPSTextElement extends UITextElement
{
    private boolean interpolate;
    private float interpolatedCount;
    private float interpolatedSum;
    private float interpolatedValue;
    private float interpolationPeriod;
    private long lastInterpolatedTime;
    private long lastTime;
    
    public UIFPSTextElement(final UIWindow uiWindow, final ScriptableObject scriptableObject) {
        super(uiWindow, scriptableObject);
        this.interpolate = false;
        this.interpolationPeriod = 0.0f;
        this.lastTime = -1L;
        this.lastInterpolatedTime = -1L;
        this.interpolatedSum = 0.0f;
        this.interpolatedCount = 0.0f;
        this.interpolatedValue = -1.0f;
    }
    
    private float getInterpolatedValue(final long n, final float n2) {
        if (this.lastInterpolatedTime == -1L) {
            this.lastInterpolatedTime = n;
            return this.interpolatedValue;
        }
        if (n - this.lastInterpolatedTime > this.interpolationPeriod) {
            this.lastInterpolatedTime = n;
            this.interpolatedValue = this.interpolatedSum / this.interpolatedCount;
            this.interpolatedSum = 0.0f;
            this.interpolatedCount = 0.0f;
        }
        else {
            ++this.interpolatedCount;
            this.interpolatedSum += n2;
        }
        return this.interpolatedValue;
    }
    
    @Override
    public void onDraw(final Canvas canvas, final float n) {
        final long currentTimeMillis = System.currentTimeMillis();
        if (this.lastTime == -1L) {
            this.setBinding("text", "fps:");
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append("fps: ");
            sb.append((int)this.getInterpolatedValue(currentTimeMillis, 1000.0f / (currentTimeMillis - this.lastTime)));
            this.setBinding("text", sb.toString());
        }
        this.lastTime = currentTimeMillis;
        super.onDraw(canvas, n);
    }
    
    @Override
    public void onSetup(final ScriptableObject scriptableObject) {
        super.onSetup(scriptableObject);
        this.interpolate = ScriptableObjectHelper.getBooleanProperty(scriptableObject, "interpolate", true);
        this.interpolationPeriod = ScriptableObjectHelper.getFloatProperty(scriptableObject, "period", 500.0f);
        this.font = new Font(-1, 20.0f, 0.45f);
    }
}
