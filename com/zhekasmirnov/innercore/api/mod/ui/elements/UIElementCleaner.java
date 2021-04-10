package com.zhekasmirnov.innercore.api.mod.ui.elements;

import android.graphics.*;

public class UIElementCleaner
{
    private static Paint cleanerPaint;
    public UIElement element;
    public Rect rect;
    
    static {
        (UIElementCleaner.cleanerPaint = new Paint()).setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.SRC));
        UIElementCleaner.cleanerPaint.setColor(Color.argb(0, 255, 0, 0));
    }
    
    public UIElementCleaner(final UIElement element) {
        this.element = element;
    }
    
    public void clean(final Canvas canvas, final float n) {
        if (this.rect != null) {
            final Rect rect = new Rect();
            rect.right = (int)(this.rect.right * n);
            rect.left = (int)(this.rect.left * n);
            rect.top = (int)(this.rect.top * n);
            rect.bottom = (int)(this.rect.bottom * n);
            canvas.drawRect(rect, UIElementCleaner.cleanerPaint);
        }
    }
    
    public UIElementCleaner clone() {
        final UIElementCleaner uiElementCleaner = new UIElementCleaner(this.element);
        uiElementCleaner.set(this.rect);
        return uiElementCleaner;
    }
    
    public void debug(final Canvas canvas, final float n) {
        if (this.rect != null) {
            final Paint paint = new Paint();
            paint.setColor(Color.argb(90, 255, 0, 0));
            final Rect rect = new Rect();
            rect.right = (int)(this.rect.right * n);
            rect.left = (int)(this.rect.left * n);
            rect.top = (int)(this.rect.top * n);
            rect.bottom = (int)(this.rect.bottom * n);
            canvas.drawRect(rect, paint);
        }
    }
    
    public void set(final Rect rect) {
        this.rect = rect;
    }
}
