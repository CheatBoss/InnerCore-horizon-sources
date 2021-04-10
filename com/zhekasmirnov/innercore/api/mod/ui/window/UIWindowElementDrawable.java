package com.zhekasmirnov.innercore.api.mod.ui.window;

import android.graphics.drawable.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;
import com.zhekasmirnov.innercore.api.mod.ui.elements.*;
import com.zhekasmirnov.innercore.api.mod.ui.memory.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.log.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import com.zhekasmirnov.innercore.utils.*;
import android.graphics.*;
import android.support.annotation.*;

public class UIWindowElementDrawable extends Drawable implements IElementProvider, ITouchEventListener
{
    private static Bitmap preparationBitmap;
    private static Canvas preparationCanvas;
    private IBackgroundProvider backgroundProvider;
    boolean isDebugEnabled;
    public UIWindow window;
    public ArrayList<UIElement> windowElements;
    private UIStyle windowStyle;
    
    static {
        UIWindowElementDrawable.preparationBitmap = Bitmap.createBitmap(16, 16, Bitmap$Config.ARGB_8888);
        UIWindowElementDrawable.preparationCanvas = new Canvas(UIWindowElementDrawable.preparationBitmap);
    }
    
    public UIWindowElementDrawable(final UIWindow window) {
        this.windowElements = new ArrayList<UIElement>();
        this.windowStyle = UIStyle.DEFAULT;
        this.isDebugEnabled = false;
        this.window = window;
    }
    
    public void addOrRefreshElement(final UIElement uiElement) {
        while (true) {
            while (true) {
                int n = 0;
                Label_0102: {
                    synchronized (this) {
                        if (this.windowElements.contains(uiElement)) {
                            this.windowElements.remove(uiElement);
                        }
                        uiElement.onSetup();
                        final boolean b = false;
                        n = 0;
                        boolean b2 = b;
                        if (n < this.windowElements.size()) {
                            if (this.windowElements.get(n).z <= uiElement.z) {
                                break Label_0102;
                            }
                            this.windowElements.add(n, uiElement);
                            b2 = true;
                        }
                        if (!b2) {
                            this.windowElements.add(uiElement);
                        }
                        return;
                    }
                }
                ++n;
                continue;
            }
        }
    }
    
    public void draw(@NonNull final Canvas canvas) {
        try {
            if (this.backgroundProvider != null) {
                ((Drawable)this.backgroundProvider).draw(canvas);
            }
            if (this.window.isForegroundDirty) {
                this.drawDirty(canvas, this.window.getScale());
                this.window.isForegroundDirty = false;
            }
        }
        catch (OutOfMemoryError outOfMemoryError) {
            BitmapCache.immediateGC();
            NativeItemModel.tryReleaseModelBitmapsOnLowMemory(16777216);
        }
        catch (Exception ex) {
            ICLog.e("UI", "uncaught exception occurred", ex);
        }
    }
    
    public void drawDirty(final Canvas canvas, final float n) {
        // monitorenter(this)
        int i = 0;
        try {
            while (i < this.windowElements.size()) {
                final UIElement uiElement = this.windowElements.get(i);
                if (!uiElement.isReleased()) {
                    uiElement.onDraw(canvas, n);
                    if (this.isDebugEnabled) {
                        uiElement.debug(canvas, n);
                    }
                }
                ++i;
            }
        }
        finally {
        }
        // monitorexit(this)
    }
    
    public int getOpacity() {
        return -3;
    }
    
    public UIStyle getStyleFor(final UIElement uiElement) {
        return this.windowStyle;
    }
    
    public void invalidateAll() {
        final Iterator<UIElement> iterator = this.windowElements.iterator();
        while (iterator.hasNext()) {
            iterator.next().invalidate();
        }
    }
    
    public void onTouchEvent(final TouchEvent touchEvent) {
    Label_0025_Outer:
        while (true) {
            // monitorenter(this)
        Label_0115:
            while (true) {
            Label_0025:
                while (true) {
                    Label_0197: {
                        Label_0195: {
                            Label_0190: {
                                try {
                                    try {
                                        if (touchEvent.type != TouchEventType.MOVE && touchEvent.type != TouchEventType.DOWN) {
                                            break Label_0190;
                                        }
                                        break Label_0195;
                                        // iftrue(Label_0144:, n == 0 || n2 == 0)
                                        // iftrue(Label_0158:, !uiElement.isTouched)
                                        // iftrue(Label_0171:, n3 >= this.windowElements.size())
                                        // iftrue(Label_0126:, n == 0)
                                        // iftrue(Label_0202:, touchEvent2.localX <= 0.0f || touchEvent2.localY <= 0.0f || touchEvent2.localX >= 1.0f || touchEvent2.localY >= 1.0f)
                                        int n3 = 0;
                                        Block_6: {
                                            while (true) {
                                                final UIElement uiElement;
                                                final TouchEvent touchEvent2;
                                            Label_0164:
                                                while (true) {
                                                    while (true) {
                                                        while (true) {
                                                            Label_0171: {
                                                                break Label_0171;
                                                                Label_0144: {
                                                                    break Label_0164;
                                                                }
                                                                break Block_6;
                                                            }
                                                            uiElement.isTouched = true;
                                                            ++n3;
                                                            continue Label_0025;
                                                            Block_12: {
                                                                break Block_12;
                                                                uiElement.isTouched = false;
                                                                continue Label_0164;
                                                                final int n = 1;
                                                                continue Label_0115;
                                                            }
                                                            uiElement.onTouchEvent(touchEvent2);
                                                            continue Label_0025_Outer;
                                                        }
                                                        Label_0060: {
                                                            touchEvent2.preparePosition(this.window, uiElement.elementRect);
                                                        }
                                                        continue;
                                                    }
                                                    continue Label_0164;
                                                }
                                                uiElement.onTouchReleased(touchEvent2);
                                                continue;
                                            }
                                        }
                                        final UIElement uiElement = this.windowElements.get(n3);
                                    }
                                    // iftrue(Label_0060:, !uiElement.isReleased())
                                    finally {}
                                }
                                catch (Exception ex) {
                                    UIUtils.processError(ex);
                                }
                                break Label_0115;
                            }
                            final int n2 = 0;
                            break Label_0197;
                        }
                        final int n2 = 1;
                    }
                    int n3 = 0;
                    continue Label_0025;
                }
                Label_0202: {
                    final int n = 0;
                }
                continue Label_0115;
            }
            // monitorexit(this)
            return;
            // monitorexit(this)
            throw;
        }
    }
    
    public void releaseAll() {
        for (final UIElement uiElement : this.windowElements) {
            uiElement.onReset();
            uiElement.onRelease();
        }
        this.windowElements.clear();
    }
    
    public void removeElement(final UIElement uiElement) {
        synchronized (this) {
            if (this.windowElements.contains(uiElement)) {
                this.windowElements.remove(uiElement);
                uiElement.onRelease();
            }
        }
    }
    
    public void resetAll() {
        final Iterator<UIElement> iterator = this.windowElements.iterator();
        while (iterator.hasNext()) {
            iterator.next().onReset();
        }
    }
    
    public void runCachePreparation() {
        System.currentTimeMillis();
        this.drawDirty(UIWindowElementDrawable.preparationCanvas, this.window.getScale());
        System.currentTimeMillis();
    }
    
    public void setAlpha(@IntRange(from = 0L, to = 255L) final int n) {
    }
    
    public void setBackgroundProvider(final IBackgroundProvider backgroundProvider) {
        this.backgroundProvider = backgroundProvider;
    }
    
    public void setColorFilter(@Nullable final ColorFilter colorFilter) {
    }
    
    public void setWindowStyle(final UIStyle windowStyle) {
        this.windowStyle = windowStyle;
    }
    
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[ElementDrawable elements=");
        sb.append(this.windowElements.size());
        sb.append("]");
        return sb.toString();
    }
}
