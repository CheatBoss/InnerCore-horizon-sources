package com.zhekasmirnov.innercore.api.mod.ui.elements;

import java.util.*;
import com.zhekasmirnov.innercore.api.mod.ui.container.*;
import com.zhekasmirnov.innercore.api.mod.util.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.utils.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;

public abstract class UIElement
{
    private HashMap<String, Object> bindingMap;
    private Bitmap cacheBitmap;
    private Canvas cacheCanvas;
    public UIElementCleaner cleaner;
    protected UiAbstractContainer container;
    public ScriptableObject description;
    public ScriptableWatcher descriptionWatcher;
    public String elementName;
    public Rect elementRect;
    public boolean isDirty;
    private boolean isReleased;
    public boolean isTouched;
    protected UIStyle style;
    public UIWindow window;
    public float x;
    public float y;
    public float z;
    
    public UIElement(final UIWindow window, final ScriptableObject description) {
        this.style = UIStyle.DEFAULT;
        this.isDirty = false;
        this.bindingMap = new HashMap<String, Object>();
        this.isTouched = false;
        this.isReleased = false;
        this.elementRect = new Rect(0, 0, 1, 1);
        this.window = window;
        this.description = description;
        this.cleaner = new UIElementCleaner(this);
        this.container = window.getContainer();
        this.style = window.getElementProvider().getStyleFor(this);
    }
    
    private void refreshRect() {
        if (this.cleaner != null) {
            this.cleaner.set(this.elementRect);
        }
    }
    
    protected Object callDescriptionMethod(final String s, final Object... array) {
        final Object property = ScriptableObjectHelper.getProperty(this.description, s, null);
        if (property != null && property instanceof Function) {
            final Function function = (Function)property;
            return function.call(Compiler.assureContextForCurrentThread(), function.getParentScope(), (Scriptable)this.description, array);
        }
        return null;
    }
    
    protected Object callDescriptionMethodSafe(final String s, final Object... array) {
        try {
            final Object property = ScriptableObjectHelper.getProperty(this.description, s, null);
            if (property != null && property instanceof Function) {
                return ((Function)property).call(Compiler.assureContextForCurrentThread(), this.description.getParentScope(), (Scriptable)this.description, array);
            }
            return null;
        }
        catch (Exception ex) {
            UIUtils.processError(ex);
            return null;
        }
    }
    
    protected void callFixedOnClick(final String s, final UiAbstractContainer uiAbstractContainer, final ScriptableObject scriptableObject) {
        if (uiAbstractContainer == null) {
            UIUtils.log("ui element container is null!");
        }
        final Object o = null;
        Label_0030: {
            if (uiAbstractContainer == null) {
                final Object parent = null;
                break Label_0030;
            }
            try {
                final Object parent = uiAbstractContainer.getParent();
                this.callDescriptionMethod(s, uiAbstractContainer, parent, this, scriptableObject);
            }
            catch (Throwable t) {
                Object parent2;
                if (uiAbstractContainer == null) {
                    parent2 = o;
                }
                else {
                    parent2 = uiAbstractContainer.getParent();
                }
                this.callDescriptionMethod(s, scriptableObject, uiAbstractContainer, parent2, this);
            }
        }
    }
    
    public Texture createTexture(final Object o) {
        return new Texture(o, this.style);
    }
    
    public void debug(final Canvas canvas, final float n) {
        this.cleaner.debug(canvas, n);
    }
    
    protected void drawCache(final Canvas canvas, final float n) {
        this.drawCache(canvas, this.x * n, this.y * n);
    }
    
    protected void drawCache(final Canvas canvas, final float n, final float n2) {
        if (this.cacheBitmap != null) {
            canvas.drawBitmap(this.cacheBitmap, n, n2, (Paint)null);
        }
    }
    
    public Object getBinding(final String s) {
        if (s.equals("element_obj")) {
            return this;
        }
        if (s.equals("element_rect")) {
            return this.elementRect;
        }
        return this.bindingMap.get(s);
    }
    
    protected Canvas getCacheCanvas(final float n, final float n2, final float n3) {
        final int n4 = (int)Math.ceil(n * n3);
        final int n5 = (int)Math.ceil(n2 * n3);
        if (this.cacheBitmap == null) {
            this.cacheBitmap = Bitmap.createBitmap(n4, n5, Bitmap$Config.ARGB_8888);
            this.cacheCanvas = new Canvas(this.cacheBitmap);
        }
        else if (this.cacheBitmap.getWidth() != n4 || this.cacheBitmap.getHeight() != n5) {
            this.cacheBitmap.recycle();
            this.cacheBitmap = Bitmap.createBitmap(n4, n5, Bitmap$Config.ARGB_8888);
            this.cacheCanvas = new Canvas(this.cacheBitmap);
        }
        return this.cacheCanvas;
    }
    
    public UIElementCleaner getCleanerCopy() {
        return this.cleaner.clone();
    }
    
    protected boolean hasDescriptionMethod(final String s) {
        final Object property = ScriptableObjectHelper.getProperty(this.description, s, null);
        return property != null && property instanceof Function;
    }
    
    public void invalidate() {
        this.descriptionWatcher.invalidate();
    }
    
    public boolean isReleased() {
        return this.isReleased;
    }
    
    public abstract void onBindingUpdated(final String p0, final Object p1);
    
    public abstract void onDraw(final Canvas p0, final float p1);
    
    public void onRelease() {
        this.isReleased = true;
        if (this.cacheBitmap != null) {
            this.cacheBitmap.recycle();
        }
        this.cacheCanvas = null;
        this.cacheBitmap = null;
    }
    
    public void onReset() {
    }
    
    public void onSetup() {
        if (this.descriptionWatcher == null) {
            this.descriptionWatcher = new ScriptableWatcher(this.description);
        }
        else {
            this.descriptionWatcher.setTarget(this.description);
        }
        this.setupDefaultValues();
        this.onSetup(this.description);
    }
    
    public abstract void onSetup(final ScriptableObject p0);
    
    public void onTouchEvent(final TouchEvent touchEvent) {
        this.callDescriptionMethodSafe("onTouchEvent", this, touchEvent);
        if (touchEvent.type == TouchEventType.CLICK) {
            this.callFixedOnClick("onClick", this.container, touchEvent.localPosAsScriptable());
        }
        if (touchEvent.type == TouchEventType.LONG_CLICK) {
            if (this.hasDescriptionMethod("onLongClick")) {
                this.callFixedOnClick("onLongClick", this.container, touchEvent.localPosAsScriptable());
                return;
            }
            this.callFixedOnClick("onClick", this.container, touchEvent.localPosAsScriptable());
        }
    }
    
    public void onTouchReleased(final TouchEvent touchEvent) {
    }
    
    protected boolean optBooleanFromDesctiption(final String s, final boolean b) {
        return ScriptableObjectHelper.getBooleanProperty(this.description, s, b);
    }
    
    protected float optFloatFromDesctiption(final String s, final float n) {
        return ScriptableObjectHelper.getFloatProperty(this.description, s, n);
    }
    
    protected String optStringFromDesctiption(final String s, final String s2) {
        return ScriptableObjectHelper.getStringProperty(this.description, s, s2);
    }
    
    protected Object optValFromDescription(final String s, final Object o) {
        return ScriptableObjectHelper.getProperty(this.description, s, o);
    }
    
    public void setBinding(final String s, final Object o) {
        final Object value = this.bindingMap.get(s);
        if (value != null) {
            if (!value.equals(o)) {
                this.bindingMap.put(s, o);
                this.onBindingUpdated(s, o);
            }
        }
        else if (o != null) {
            this.bindingMap.put(s, o);
            this.onBindingUpdated(s, o);
        }
    }
    
    public void setPosition(final float x, final float y) {
        this.x = x;
        this.y = y;
        this.setSize((float)this.elementRect.width(), (float)this.elementRect.height());
        this.refreshRect();
    }
    
    public void setSize(final float n, final float n2) {
        this.elementRect.left = (int)this.x;
        this.elementRect.top = (int)this.y;
        this.elementRect.right = (int)(this.x + n);
        this.elementRect.bottom = (int)(this.y + n2);
        this.refreshRect();
    }
    
    protected void setupDefaultValues() {
        this.x = this.optFloatFromDesctiption("x", 0.0f);
        this.y = this.optFloatFromDesctiption("y", 0.0f);
        this.z = this.optFloatFromDesctiption("z", 0.0f);
        this.setSize(1.0f, 1.0f);
        if (this.description.has("clicker", (Scriptable)this.description)) {
            final ScriptableObject scriptableObjectProperty = ScriptableObjectHelper.getScriptableObjectProperty(this.description, "clicker", null);
            if (scriptableObjectProperty != null) {
                final ScriptableObject scriptableObjectProperty2 = ScriptableObjectHelper.getScriptableObjectProperty(scriptableObjectProperty, "onClick", null);
                final ScriptableObject scriptableObjectProperty3 = ScriptableObjectHelper.getScriptableObjectProperty(scriptableObjectProperty, "onLongClick", null);
                this.description.put("onClick", (Scriptable)this.description, (Object)scriptableObjectProperty2);
                this.description.put("onLongClick", (Scriptable)this.description, (Object)scriptableObjectProperty3);
            }
        }
    }
    
    public void setupInitialBindings(final UiAbstractContainer container, final String elementName) {
        this.container = container;
        this.elementName = elementName;
    }
}
