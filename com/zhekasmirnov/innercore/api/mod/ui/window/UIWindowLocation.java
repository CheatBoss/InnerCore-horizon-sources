package com.zhekasmirnov.innercore.api.mod.ui.window;

import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;
import android.view.*;
import android.os.*;
import android.graphics.*;
import android.widget.*;

public class UIWindowLocation
{
    public static final int PADDING_BOTTOM = 1;
    public static final int PADDING_LEFT = 2;
    public static final int PADDING_RIGHT = 3;
    public static final int PADDING_TOP = 0;
    public boolean forceScrollX;
    public boolean forceScrollY;
    public int height;
    public float scale;
    public int scrollX;
    public int scrollY;
    public int width;
    public int x;
    public int y;
    public float zIndex;
    
    public UIWindowLocation() {
        this.zIndex = 0.0f;
        this.set(0, 0, 1000, (int)(UIUtils.screenHeight * 1000.0f / UIUtils.screenWidth));
    }
    
    public UIWindowLocation(final ScriptableObject scriptableObject) {
        this.zIndex = 0.0f;
        if (scriptableObject == null) {
            this.set(0, 0, 1000, (int)(UIUtils.screenHeight * 1000.0f / UIUtils.screenWidth));
            return;
        }
        this.x = ScriptableObjectHelper.getIntProperty(scriptableObject, "x", 0);
        this.y = ScriptableObjectHelper.getIntProperty(scriptableObject, "y", 0);
        this.width = ScriptableObjectHelper.getIntProperty(scriptableObject, "width", 1000 - this.x);
        this.height = ScriptableObjectHelper.getIntProperty(scriptableObject, "height", (int)(UIUtils.screenHeight * 1000.0f / UIUtils.screenWidth) - this.y);
        this.forceScrollX = ScriptableObjectHelper.getBooleanProperty(scriptableObject, "forceScrollX", false);
        this.forceScrollY = ScriptableObjectHelper.getBooleanProperty(scriptableObject, "forceScrollY", false);
        final ScriptableObject scriptableObjectProperty = ScriptableObjectHelper.getScriptableObjectProperty(scriptableObject, "padding", null);
        if (scriptableObjectProperty != null) {
            final int intProperty = ScriptableObjectHelper.getIntProperty(scriptableObjectProperty, "top", -1);
            if (intProperty >= 0) {
                this.setPadding(0, intProperty);
            }
            final int intProperty2 = ScriptableObjectHelper.getIntProperty(scriptableObjectProperty, "bottom", -1);
            if (intProperty2 >= 0) {
                this.setPadding(1, intProperty2);
            }
            final int intProperty3 = ScriptableObjectHelper.getIntProperty(scriptableObjectProperty, "left", -1);
            if (intProperty3 >= 0) {
                this.setPadding(2, intProperty3);
            }
            final int intProperty4 = ScriptableObjectHelper.getIntProperty(scriptableObjectProperty, "right", -1);
            if (intProperty4 >= 0) {
                this.setPadding(3, intProperty4);
            }
        }
        this.scrollX = Math.max(this.width, ScriptableObjectHelper.getIntProperty(scriptableObject, "scrollX", this.width));
        this.scrollY = Math.max(this.height, ScriptableObjectHelper.getIntProperty(scriptableObject, "scrollY", ScriptableObjectHelper.getIntProperty(scriptableObject, "scrollHeight", this.height)));
    }
    
    public ScriptableObject asScriptable() {
        final ScriptableObject scriptableObject = new ScriptableObject() {
            public String getClassName() {
                return "Window Location";
            }
        };
        scriptableObject.put("x", (Scriptable)scriptableObject, (Object)this.x);
        scriptableObject.put("y", (Scriptable)scriptableObject, (Object)this.y);
        scriptableObject.put("width", (Scriptable)scriptableObject, (Object)this.width);
        scriptableObject.put("height", (Scriptable)scriptableObject, (Object)this.height);
        scriptableObject.put("scrollX", (Scriptable)scriptableObject, (Object)this.scrollX);
        scriptableObject.put("scrollY", (Scriptable)scriptableObject, (Object)this.scrollY);
        return scriptableObject;
    }
    
    public UIWindowLocation copy() {
        final UIWindowLocation uiWindowLocation = new UIWindowLocation();
        uiWindowLocation.x = this.x;
        uiWindowLocation.y = this.y;
        uiWindowLocation.width = this.width;
        uiWindowLocation.height = this.height;
        uiWindowLocation.scrollX = this.scrollX;
        uiWindowLocation.scrollY = this.scrollY;
        return uiWindowLocation;
    }
    
    public float getDrawingScale() {
        return Math.max(this.scrollX, this.width) / 1000.0f * this.getScale();
    }
    
    public WindowManager$LayoutParams getLayoutParams(final int n, final int n2, final int n3) {
        final int[] array = new int[2];
        UIUtils.getOffsets(array);
        final WindowManager$LayoutParams windowManager$LayoutParams = new WindowManager$LayoutParams((int)(this.width * this.getScale()), (int)(this.height * this.getScale()), (int)(this.x * this.getScale()) + array[0], (int)(this.y * this.getScale()) + array[1], n, n2, n3);
        windowManager$LayoutParams.gravity = 51;
        windowManager$LayoutParams.dimAmount = 0.3f;
        if (Build$VERSION.SDK_INT >= 28) {
            windowManager$LayoutParams.layoutInDisplayCutoutMode = 2;
        }
        return windowManager$LayoutParams;
    }
    
    public Rect getRect() {
        final Rect rect = new Rect();
        final int[] array = new int[2];
        UIUtils.getOffsets(array);
        final float scale = this.getScale();
        rect.left = (int)(this.x * scale) + array[0];
        rect.top = (int)(this.y * scale) + array[1];
        rect.right = (int)((this.x + this.width) * scale) + 1 + array[0];
        rect.bottom = (int)((this.y + this.height) * scale) + 1 + array[1];
        return rect;
    }
    
    public float getScale() {
        return this.scale = UIUtils.screenWidth / 1000.0f;
    }
    
    public int getWindowHeight() {
        return (int)this.globalToWindow((float)Math.max(this.scrollY, this.height));
    }
    
    public int getWindowWidth() {
        return 1000;
    }
    
    public float globalToWindow(final float n) {
        return n / this.getDrawingScale() * this.getScale();
    }
    
    public void removeScroll() {
        this.scrollX = this.width;
        this.scrollY = this.height;
    }
    
    public void set(final int x, final int y, final int n, final int n2) {
        this.x = x;
        this.y = y;
        this.width = n;
        this.height = n2;
        this.scrollX = n;
        this.scrollY = n2;
    }
    
    public void set(final UIWindowLocation uiWindowLocation) {
        this.x = uiWindowLocation.x;
        this.y = uiWindowLocation.y;
        this.width = uiWindowLocation.width;
        this.height = uiWindowLocation.height;
        this.scrollX = uiWindowLocation.scrollX;
        this.scrollY = uiWindowLocation.scrollY;
    }
    
    public void setPadding(final float n, final float n2, final float n3, final float n4) {
        this.setPadding(0, (int)n);
        this.setPadding(1, (int)n2);
        this.setPadding(2, (int)n3);
        this.setPadding(3, (int)n4);
    }
    
    public void setPadding(int x, int y) {
        final int x2 = this.x;
        final int y2 = this.y;
        final int n = this.x + this.width;
        int n2 = this.y + this.height;
        int n3 = y;
        if (y < 0) {
            n3 = 0;
        }
        int n4 = 0;
        switch (x) {
            default: {
                x = x2;
                y = y2;
                n4 = n;
                break;
            }
            case 3: {
                n4 = 1000 - n3 + 1;
                x = x2;
                y = y2;
                break;
            }
            case 2: {
                x = n3;
                y = y2;
                n4 = n;
                break;
            }
            case 1: {
                n2 = (int)(UIUtils.screenHeight * 1000.0f / UIUtils.screenWidth - n3) + 1;
                x = x2;
                y = y2;
                n4 = n;
                break;
            }
            case 0: {
                y = n3;
                n4 = n;
                x = x2;
                break;
            }
        }
        if (this.scrollX == this.width) {
            this.scrollX = Math.max(1, n4 - x);
        }
        if (this.scrollY == this.height) {
            this.scrollY = Math.max(1, n2 - y);
        }
        this.x = x;
        this.y = y;
        this.width = Math.max(1, n4 - x);
        this.height = Math.max(1, n2 - y);
    }
    
    public void setScroll(final int n, final int n2) {
        this.scrollX = Math.max(this.width, n);
        this.scrollY = Math.max(this.height, n2);
    }
    
    public void setSize(final int n, final int n2) {
        if (this.scrollX == this.width) {
            this.scrollX = n;
        }
        if (this.scrollY == this.height) {
            this.scrollY = n2;
        }
        this.width = n;
        this.height = n2;
        this.setScroll(this.scrollX, this.scrollY);
    }
    
    public void setZ(final float zIndex) {
        this.zIndex = zIndex;
    }
    
    public void setupAndShowPopupWindow(final PopupWindow popupWindow) {
        final int n = (int)(this.x * this.getScale());
        final int n2 = (int)(this.y * this.getScale());
        final int width = (int)(this.width * this.getScale());
        final int height = (int)(this.height * this.getScale());
        popupWindow.setWidth(width);
        popupWindow.setHeight(height);
        popupWindow.showAtLocation(UIUtils.getDecorView(), 51, n, n2);
    }
    
    public void showPopupWindow(final PopupWindow popupWindow) {
        final Rect rect = this.getRect();
        popupWindow.setWidth(rect.width());
        popupWindow.setHeight(rect.height());
        if (Build$VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(this.zIndex);
        }
        popupWindow.showAtLocation(UIUtils.getDecorView(), 51, rect.left, rect.top);
    }
    
    public void updatePopupWindow(final PopupWindow popupWindow) {
        final Rect rect = this.getRect();
        if (Build$VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(this.zIndex);
        }
        popupWindow.update(rect.left, rect.top, rect.width(), rect.height());
    }
    
    public float windowToGlobal(final float n) {
        return this.getDrawingScale() * n / this.getScale();
    }
}
