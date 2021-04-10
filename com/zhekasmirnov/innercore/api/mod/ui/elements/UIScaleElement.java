package com.zhekasmirnov.innercore.api.mod.ui.elements;

import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import org.mozilla.javascript.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.api.mod.*;

public class UIScaleElement extends UIElement
{
    public static final int DIRECTION_DOWN = 3;
    public static final int DIRECTION_LEFT = 2;
    public static final int DIRECTION_RIGHT = 0;
    public static final int DIRECTION_UP = 1;
    private Texture backgroundTexture;
    private int direction;
    private boolean invert;
    private Texture overlayTexture;
    private boolean pixelate;
    private float pixelationStep;
    private float scale;
    private Texture scaleTexture;
    private float value;
    
    public UIScaleElement(final UIWindow uiWindow, final ScriptableObject scriptableObject) {
        super(uiWindow, scriptableObject);
        this.value = 0.0f;
        this.direction = 0;
        this.invert = false;
        this.pixelate = false;
        this.pixelationStep = 0.0f;
    }
    
    private void calcPexilation(final Texture texture) {
        if (this.direction != 1 && this.direction != 3) {
            this.pixelationStep = 1.0f / texture.getWidth();
            return;
        }
        this.pixelationStep = 1.0f / texture.getHeight();
    }
    
    @Override
    public void onBindingUpdated(final String s, final Object o) {
        if (s.equals("value")) {
            this.value = Math.max(0.0f, Math.min(1.0f, (float)Context.jsToJava(o, (Class)Float.TYPE)));
        }
        if (s.equals("texture")) {
            this.scaleTexture.release();
            this.calcPexilation(this.scaleTexture = new Texture(o));
            this.scaleTexture.resizeAll((float)this.elementRect.width(), (float)this.elementRect.height());
        }
    }
    
    @Override
    public void onDraw(final Canvas canvas, final float n) {
        float value;
        final float n2 = value = this.value;
        if (this.pixelate) {
            value = Math.round(n2 / this.pixelationStep) * this.pixelationStep;
        }
        if (this.backgroundTexture != null) {
            this.backgroundTexture.draw(canvas, this.x * n, this.y * n, n);
        }
        float n3 = 0.0f;
        float n4 = 0.0f;
        float n5 = 1.0f;
        float n6 = 1.0f;
        if (this.direction == 0) {
            n5 = value;
        }
        if (this.direction == 1) {
            n4 = 1.0f - value;
        }
        if (this.direction == 2) {
            n3 = 1.0f - value;
        }
        if (this.direction == 3) {
            n6 = value;
        }
        if (this.invert) {
            if (n3 > 0.0f) {}
            if (n4 > 0.0f) {}
        }
        this.scaleTexture.drawCutout(canvas, new RectF(n3, n4, n5, n6), this.x * n, this.y * n, n);
        if (this.overlayTexture != null) {
            this.overlayTexture.draw(canvas, this.x * n, this.y * n, n);
        }
    }
    
    @Override
    public void onRelease() {
        super.onRelease();
        this.scaleTexture.release();
        if (this.backgroundTexture != null) {
            this.backgroundTexture.release();
        }
        if (this.overlayTexture != null) {
            this.overlayTexture.release();
        }
    }
    
    @Override
    public void onSetup(final ScriptableObject scriptableObject) {
        this.scale = this.optFloatFromDesctiption("scale", 1.0f);
        this.direction = ScriptableObjectHelper.getIntProperty(scriptableObject, "direction", 0);
        this.invert = this.optBooleanFromDesctiption("invert", false);
        this.pixelate = this.optBooleanFromDesctiption("pixelate", false);
        this.calcPexilation(this.scaleTexture = new Texture(this.optStringFromDesctiption("bitmap", null)));
        final float optFloatFromDesctiption = this.optFloatFromDesctiption("width", this.scaleTexture.getWidth() * this.scale);
        final float optFloatFromDesctiption2 = this.optFloatFromDesctiption("height", this.scaleTexture.getHeight() * this.scale);
        this.scaleTexture.resizeAll(optFloatFromDesctiption, optFloatFromDesctiption2);
        this.setSize(optFloatFromDesctiption, optFloatFromDesctiption2);
        final String optStringFromDesctiption = this.optStringFromDesctiption("background", null);
        if (optStringFromDesctiption != null) {
            (this.backgroundTexture = new Texture(optStringFromDesctiption)).readOffset(ScriptableObjectHelper.getScriptableObjectProperty(scriptableObject, "backgroundOffset", null));
            this.backgroundTexture.rescaleAll(this.scale);
        }
        final String optStringFromDesctiption2 = this.optStringFromDesctiption("overlay", null);
        if (optStringFromDesctiption2 != null) {
            (this.overlayTexture = new Texture(optStringFromDesctiption2)).readOffset(ScriptableObjectHelper.getScriptableObjectProperty(scriptableObject, "overlayOffset", null));
            this.overlayTexture.rescaleAll(this.scale);
        }
        this.setBinding("value", this.optFloatFromDesctiption("value", 0.0f));
    }
}
