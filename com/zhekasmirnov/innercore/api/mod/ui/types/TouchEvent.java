package com.zhekasmirnov.innercore.api.mod.ui.types;

import android.os.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import android.graphics.*;
import android.view.*;

public class TouchEvent
{
    public float _x;
    public float _y;
    private int callbackId;
    public float downX;
    public float downY;
    private Handler handler;
    private boolean isCallbackOpen;
    private ITouchEventListener listener;
    public float localX;
    public float localY;
    public TouchEventType type;
    public float x;
    public float y;
    
    public TouchEvent(final ITouchEventListener listener) {
        this.callbackId = 0;
        this.isCallbackOpen = false;
        this.handler = new Handler();
        this.listener = listener;
    }
    
    private void cancelCallback() {
        this.isCallbackOpen = false;
        ++this.callbackId;
    }
    
    private boolean hasMovedSinceLastDown() {
        return Math.sqrt((this._x - this.downX) * (this._x - this.downX) + (this._y - this.downY) * (this._y - this.downY)) > 75.0;
    }
    
    private void openCallback(final TouchEventType touchEventType, final int n) {
        ++this.callbackId;
        this.isCallbackOpen = true;
        this.handler.postDelayed((Runnable)new Callback(this, touchEventType, this.callbackId), (long)n);
    }
    
    public ScriptableObject localPosAsScriptable() {
        final ScriptableObject scriptableObject = new ScriptableObject() {
            public String getClassName() {
                return "position";
            }
        };
        scriptableObject.put("x", (Scriptable)scriptableObject, (Object)this.localX);
        scriptableObject.put("y", (Scriptable)scriptableObject, (Object)this.localY);
        return scriptableObject;
    }
    
    public ScriptableObject posAsScriptable() {
        final ScriptableObject scriptableObject = new ScriptableObject() {
            public String getClassName() {
                return "position";
            }
        };
        scriptableObject.put("x", (Scriptable)scriptableObject, (Object)this.x);
        scriptableObject.put("y", (Scriptable)scriptableObject, (Object)this.y);
        return scriptableObject;
    }
    
    public void preparePosition(final UIWindow uiWindow, final Rect rect) {
        this.x = this._x / uiWindow.getScale();
        this.y = this._y / uiWindow.getScale();
        if (rect != null) {
            this.localX = (this.x - rect.left) / (rect.right - rect.left);
            this.localY = (this.y - rect.top) / (rect.bottom - rect.top);
        }
    }
    
    public void update(final MotionEvent motionEvent) {
        this._x = motionEvent.getX();
        this._y = motionEvent.getY();
        final int action = motionEvent.getAction();
        if (action != 3) {
            switch (action) {
                default: {
                    this.type = TouchEventType.MOVE;
                    break;
                }
                case 1: {
                    if (this.isCallbackOpen) {
                        this.cancelCallback();
                        if (!this.hasMovedSinceLastDown()) {
                            this.type = TouchEventType.CLICK;
                            break;
                        }
                    }
                    this.type = TouchEventType.UP;
                    break;
                }
                case 0: {
                    this.downX = this._x;
                    this.downY = this._y;
                    this.type = TouchEventType.DOWN;
                    this.openCallback(TouchEventType.LONG_CLICK, 800);
                    break;
                }
            }
        }
        else {
            this.type = TouchEventType.CANCEL;
            this.cancelCallback();
        }
        this.listener.onTouchEvent(this);
    }
    
    private class Callback implements Runnable
    {
        private TouchEvent event;
        private int id;
        private TouchEventType type;
        
        Callback(final TouchEvent event, final TouchEventType type, final int id) {
            this.event = event;
            this.type = type;
            this.id = id;
        }
        
        @Override
        public void run() {
            if (this.event.callbackId == this.id && !this.event.hasMovedSinceLastDown()) {
                this.event.isCallbackOpen = false;
                this.event.type = this.type;
                this.event.listener.onTouchEvent(this.event);
            }
        }
    }
}
