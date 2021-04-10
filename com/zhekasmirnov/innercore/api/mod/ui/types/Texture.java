package com.zhekasmirnov.innercore.api.mod.ui.types;

import com.zhekasmirnov.innercore.api.mod.ui.memory.*;
import org.mozilla.javascript.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.mod.*;
import android.graphics.*;

public class Texture
{
    private static final Paint paint;
    public BitmapWrap[] animation;
    public BitmapWrap bitmap;
    public float delay;
    private float height;
    public boolean isAnimation;
    private float offsetX;
    private float offsetY;
    private float width;
    
    static {
        (paint = new Paint()).setFilterBitmap(false);
        Texture.paint.setDither(false);
        Texture.paint.setAntiAlias(false);
    }
    
    public Texture(final Bitmap bitmap) {
        this.animation = new BitmapWrap[1];
        this.isAnimation = false;
        this.delay = 1.0f;
        this.width = -1.0f;
        this.height = -1.0f;
        this.offsetX = 0.0f;
        this.offsetY = 0.0f;
        this.bitmap = BitmapWrap.wrap(bitmap);
        this.isAnimation = false;
    }
    
    public Texture(final Object o) {
        this(o, null);
    }
    
    public Texture(final Object o, final UIStyle uiStyle) {
        this.animation = new BitmapWrap[1];
        int i = 0;
        this.isAnimation = false;
        this.delay = 1.0f;
        this.width = -1.0f;
        this.height = -1.0f;
        this.offsetX = 0.0f;
        this.offsetY = 0.0f;
        Object unwrap = o;
        if (o != null) {
            unwrap = o;
            if (o instanceof Wrapper) {
                unwrap = ((Wrapper)o).unwrap();
            }
        }
        if (unwrap == null) {
            this.bitmap = BitmapWrap.wrap("missing_texture");
            this.isAnimation = false;
            return;
        }
        if (unwrap instanceof ScriptableObject) {
            final ScriptableObject scriptableObject = (ScriptableObject)unwrap;
            final Object[] allIds = scriptableObject.getAllIds();
            final ArrayList<BitmapWrap> list = new ArrayList<BitmapWrap>();
            while (i < allIds.length) {
                final Object value = scriptableObject.get(allIds[i]);
                if (value instanceof CharSequence) {
                    list.add(UIStyle.getBitmapByDescription(uiStyle, value.toString()));
                }
                ++i;
            }
            list.toArray(this.animation = new BitmapWrap[Math.max(1, list.size())]);
            this.isAnimation = true;
            this.delay = Math.max(0.3f, ScriptableObjectHelper.getFloatProperty(scriptableObject, "delay", 2.0f));
            return;
        }
        if (unwrap instanceof CharSequence) {
            this.bitmap = UIStyle.getBitmapByDescription(uiStyle, unwrap.toString());
            this.isAnimation = false;
            return;
        }
        if (unwrap instanceof Bitmap) {
            this.bitmap = BitmapWrap.wrap((Bitmap)unwrap);
            this.isAnimation = false;
        }
    }
    
    public Texture(final Bitmap[] array) {
        this.animation = new BitmapWrap[1];
        int i = 0;
        this.isAnimation = false;
        this.delay = 1.0f;
        this.width = -1.0f;
        this.height = -1.0f;
        this.offsetX = 0.0f;
        this.offsetY = 0.0f;
        this.animation = new BitmapWrap[array.length];
        while (i < array.length) {
            this.animation[i] = BitmapWrap.wrap(array[i]);
            ++i;
        }
        this.isAnimation = true;
    }
    
    private void validate() {
    }
    
    public void draw(final Canvas canvas, final float n, final float n2, final float n3) {
        this.drawCutout(canvas, new RectF(0.0f, 0.0f, 1.0f, 1.0f), n, n2, n3);
    }
    
    public void drawCutout(final Canvas canvas, final RectF rectF, final float n, final float n2, final float n3) {
        if (rectF.width() > 0.0f && rectF.height() > 0.0f) {
            final Bitmap bitmap = this.getBitmap(this.getFrame());
            if (bitmap != null) {
                final int width = bitmap.getWidth();
                final int height = bitmap.getHeight();
                final Rect rect = new Rect(Math.min(width, Math.max(0, Math.round(rectF.left * width))), Math.min(height, Math.max(0, Math.round(rectF.top * height))), Math.min(width, Math.max(0, Math.round(rectF.right * width))), Math.min(height, Math.max(0, Math.round(rectF.bottom * height))));
                final float width2 = this.getWidth();
                final float height2 = this.getHeight();
                final RectF rectF2 = new RectF(Math.min(1.0f, Math.max(0.0f, rect.left / (float)width)) * width2, Math.min(1.0f, Math.max(0.0f, rect.top / (float)height)) * height2, Math.min(1.0f, Math.max(0.0f, rect.right / (float)width)) * width2, Math.min(1.0f, Math.max(0.0f, rect.bottom / (float)height)) * height2);
                if (rect.width() > 0 && rect.height() > 0) {
                    canvas.drawBitmap(bitmap, rect, new RectF(n + (this.offsetX + rectF2.left) * n3, n2 + (this.offsetY + rectF2.top) * n3, n + (this.offsetX + rectF2.right) * n3, n2 + (this.offsetY + rectF2.bottom) * n3), Texture.paint);
                }
            }
        }
    }
    
    public void fitAllToOneSize() {
        this.resizeAll(this.getWidth(), this.getHeight());
    }
    
    public Bitmap getBitmap(final int n) {
        if (this.isAnimation) {
            return this.animation[n].get();
        }
        return this.bitmap.get();
    }
    
    public BitmapWrap getBitmapWrap(final int n) {
        if (this.isAnimation) {
            return this.animation[n];
        }
        return this.bitmap;
    }
    
    public int getFrame() {
        if (this.isAnimation) {
            return (int)(System.currentTimeMillis() % 1000000000L / (this.delay * 50.0f)) % this.animation.length;
        }
        return 0;
    }
    
    public float getHeight() {
        if (this.height > 0.0f) {
            return this.height;
        }
        if (this.bitmap == null) {
            final BitmapWrap[] animation = this.animation;
            int height = 0;
            if (animation[0] != null) {
                height = this.animation[0].getHeight();
            }
            return (float)height;
        }
        return (float)this.bitmap.getHeight();
    }
    
    public float getWidth() {
        if (this.width > 0.0f) {
            return this.width;
        }
        if (this.bitmap == null) {
            final BitmapWrap[] animation = this.animation;
            int width = 0;
            if (animation[0] != null) {
                width = this.animation[0].getWidth();
            }
            return (float)width;
        }
        return (float)this.bitmap.getWidth();
    }
    
    public boolean isAnimated() {
        return this.animation != null && this.animation.length > 1;
    }
    
    public void readOffset(final ScriptableObject scriptableObject) {
        if (scriptableObject == null) {
            this.offsetY = 0.0f;
            this.offsetX = 0.0f;
            return;
        }
        this.offsetX = ScriptableObjectHelper.getFloatProperty(scriptableObject, "x", 0.0f);
        this.offsetY = ScriptableObjectHelper.getFloatProperty(scriptableObject, "y", 0.0f);
    }
    
    public void release() {
        if (this.bitmap != null) {
            this.bitmap.storeIfNeeded();
        }
        final BitmapWrap[] animation = this.animation;
        int i = 0;
        if (animation[0] != null) {
            for (BitmapWrap[] animation2 = this.animation; i < animation2.length; ++i) {
                animation2[i].storeIfNeeded();
            }
        }
    }
    
    public void rescaleAll(final float n) {
        this.resizeAll(this.getWidth() * n, this.getHeight() * n);
    }
    
    public void resizeAll(final float width, final float height) {
        this.validate();
        this.width = width;
        this.height = height;
    }
}
