package com.zhekasmirnov.innercore.api.mod.ui.window;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.innercore.api.mod.ui.container.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;
import com.zhekasmirnov.innercore.utils.*;
import android.content.*;
import android.graphics.drawable.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import android.app.*;
import android.widget.*;
import android.view.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.api.mod.ui.memory.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import java.util.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.innercore.api.mod.ui.elements.*;
import android.os.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.horizon.*;

@SynthesizedClassMap({ -$$Lambda$UIWindow$7zqmpxXP2J0FSQJ8xQqnkpXUkys.class })
public class UIWindow implements IWindow
{
    private static final Object LOCK;
    private ArrayList<UIWindow> adjacentWindows;
    private IBackgroundProvider backgroundProvider;
    public boolean closeOnBackPressed;
    private UiAbstractContainer container;
    public ScriptableObject content;
    private ContentProvider contentProvider;
    private IElementProvider elementProvider;
    private ImageView elementView;
    private IWindowEventListener eventListener;
    private boolean isApplyingInsets;
    public boolean isBackgroundDirty;
    private boolean isBlockingBackground;
    private boolean isDynamic;
    public boolean isForegroundDirty;
    private boolean isInGameOverlay;
    private boolean isInventoryNeeded;
    private boolean isOpened;
    private boolean isSetup;
    private boolean isTouchable;
    private long lastBackgroundRefresh;
    private long lastElementRefresh;
    public ViewGroup layout;
    private UIWindowLocation location;
    private IWindow parentWindow;
    private UIStyle style;
    private TouchEvent touchEvent;
    private HashMap<String, Object> windowProperties;
    
    static {
        LOCK = new Object();
    }
    
    public UIWindow(final UIWindowLocation location) {
        this.style = new UIStyle();
        this.isOpened = false;
        this.isDynamic = true;
        this.isInventoryNeeded = false;
        this.isApplyingInsets = false;
        this.adjacentWindows = new ArrayList<UIWindow>();
        this.lastElementRefresh = -1L;
        this.lastBackgroundRefresh = -1L;
        this.isTouchable = true;
        this.isBlockingBackground = false;
        this.isInGameOverlay = false;
        this.isSetup = false;
        this.content = null;
        this.isBackgroundDirty = false;
        this.isForegroundDirty = false;
        this.windowProperties = new HashMap<String, Object>();
        this.closeOnBackPressed = false;
        final Activity context = UIUtils.getContext();
        this.location = location;
        this.elementView = new ImageView((Context)context);
        this.contentProvider = new ContentProvider(this);
        this.backgroundProvider = new UIWindowBackgroundDrawable(this);
        (this.elementProvider = new UIWindowElementDrawable(this)).setBackgroundProvider(this.backgroundProvider);
        this.elementView.setImageDrawable((Drawable)this.elementProvider);
        this.elementView.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (UIWindow.this.touchEvent == null) {
                    UIWindow.this.touchEvent = new TouchEvent((ITouchEventListener)UIWindow.this.elementProvider);
                }
                UIWindow.this.touchEvent.update(motionEvent);
                return true;
            }
        });
    }
    
    public UIWindow(final ScriptableObject content) {
        this(new UIWindowLocation());
        this.setContent(content);
    }
    
    private void applyWindowInsets(final WindowInsets windowInsets) {
        synchronized (this) {
            if (!this.isApplyingInsets) {
                this.isApplyingInsets = true;
                try {
                    WindowParent.applyWindowInsets(this, windowInsets);
                }
                catch (Throwable t) {
                    t.printStackTrace();
                }
                this.isApplyingInsets = false;
            }
        }
    }
    
    private void initializeLayout(final Context context) {
        if (this.layout != null) {
            WindowParent.releaseWindowLayout((View)this.layout);
            try {
                this.layout.removeView((View)this.elementView);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            this.layout = null;
        }
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = new RelativeLayout$LayoutParams((int)(this.location.scrollX * this.location.getScale()), (int)(this.location.scrollY * this.location.getScale()));
        Object o = null;
        if (this.location.scrollY > this.location.height || this.location.forceScrollY) {
            final ScrollView layout = new ScrollView(context) {
                public WindowInsets onApplyWindowInsets(final WindowInsets windowInsets) {
                    UIWindow.this.applyWindowInsets(windowInsets);
                    return super.onApplyWindowInsets(windowInsets);
                }
            };
            this.layout = (ViewGroup)layout;
            o = layout;
            layout.setOverScrollMode(2);
            layout.setVerticalScrollBarEnabled(false);
            layout.setHorizontalScrollBarEnabled(false);
        }
        Object o2 = null;
        Label_0271: {
            if (this.location.scrollX <= this.location.width) {
                o2 = o;
                if (!this.location.forceScrollX) {
                    break Label_0271;
                }
            }
            final HorizontalScrollView layout2 = new HorizontalScrollView(context) {
                public WindowInsets onApplyWindowInsets(final WindowInsets windowInsets) {
                    UIWindow.this.applyWindowInsets(windowInsets);
                    return super.onApplyWindowInsets(windowInsets);
                }
            };
            layout2.setOverScrollMode(2);
            layout2.setVerticalScrollBarEnabled(false);
            layout2.setHorizontalScrollBarEnabled(false);
            if (this.layout != null) {
                this.layout.setMinimumWidth((int)(this.location.scrollX * this.location.getScale()));
                this.layout.setMinimumHeight((int)(this.location.scrollY * this.location.getScale()));
                layout2.addView((View)this.layout);
            }
            else {
                o = layout2;
            }
            this.layout = (ViewGroup)layout2;
            o2 = o;
        }
        Object layout3 = o2;
        if (this.layout == null) {
            layout3 = new RelativeLayout(context) {
                public WindowInsets onApplyWindowInsets(final WindowInsets windowInsets) {
                    UIWindow.this.applyWindowInsets(windowInsets);
                    return super.onApplyWindowInsets(windowInsets);
                }
            };
            this.layout = (ViewGroup)layout3;
        }
        ((Activity)context).runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                UIWindow.this.elementView.setMinimumWidth((int)(UIWindow.this.location.scrollX * UIWindow.this.location.getScale()));
                UIWindow.this.elementView.setMinimumHeight((int)(UIWindow.this.location.scrollY * UIWindow.this.location.getScale()));
                if (UIWindow.this.elementView.getParent() != null) {
                    ((ViewGroup)UIWindow.this.elementView.getParent()).removeView((View)UIWindow.this.elementView);
                }
                ((ViewGroup)layout3).addView((View)UIWindow.this.elementView, relativeLayout$LayoutParams);
            }
        });
    }
    
    private void resizeView(final int minimumWidth, final int minimumHeight) {
        try {
            this.elementView.setLayoutParams((ViewGroup$LayoutParams)this.elementView.getLayoutParams().getClass().getDeclaredConstructor(Integer.TYPE, Integer.TYPE).newInstance(minimumWidth, minimumHeight));
            this.elementView.setMinimumWidth(minimumWidth);
            this.elementView.setMinimumHeight(minimumHeight);
        }
        catch (Exception ex) {
            ICLog.e("ERROR", "resizeView error", ex);
        }
    }
    
    private void setup() {
        this.isSetup = true;
        this.contentProvider.setupDrawing();
        this.contentProvider.setupElements();
        this.forceRefresh();
    }
    
    private void setupIfNeeded() {
        if (!this.isSetup) {
            this.setup();
        }
    }
    
    public void addAdjacentWindow(final UIWindow uiWindow) {
        if (!this.adjacentWindows.contains(uiWindow)) {
            this.adjacentWindows.add(uiWindow);
        }
    }
    
    @Override
    public void close() {
        while (true) {
            while (true) {
                Label_0163: {
                    synchronized (UIWindow.LOCK) {
                        if (!this.isOpened) {
                            return;
                        }
                        this.isOpened = false;
                        if (this.eventListener != null) {
                            this.eventListener.onClose(this);
                        }
                        if (this.parentWindow != null) {
                            this.parentWindow.close();
                        }
                        final Iterator<UIWindow> iterator = this.adjacentWindows.iterator();
                        if (!iterator.hasNext()) {
                            if (this.container != null) {
                                this.container.onWindowClosed();
                            }
                            this.getElementProvider().resetAll();
                            this.getBackgroundProvider().releaseCache();
                            WindowProvider.instance.onWindowClosed(this);
                            WindowParent.closeWindow(this);
                            BitmapCache.asyncGC();
                            Callback.invokeAPICallback("CustomWindowClosed", this);
                            return;
                        }
                        final UIWindow uiWindow = iterator.next();
                        if (uiWindow != null) {
                            uiWindow.close();
                            break Label_0163;
                        }
                        break Label_0163;
                    }
                }
                continue;
            }
        }
    }
    
    public void debug() {
        final StringBuilder sb = new StringBuilder();
        sb.append("starting window debug for ");
        sb.append(this);
        sb.append(":\n");
        sb.append("\tcontent provider = ");
        sb.append(this.contentProvider);
        sb.append("\n");
        sb.append("\telement provider = ");
        sb.append(this.elementProvider);
        sb.append("\n");
        sb.append("\tcontainer = ");
        sb.append(this.container);
        sb.append("\n");
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("\ttime since last update elements=");
        sb2.append(System.currentTimeMillis() - this.lastElementRefresh);
        sb2.append(" background=");
        sb2.append(System.currentTimeMillis() - this.lastBackgroundRefresh);
        sb.append(sb2.toString());
        Logger.debug("UI", sb.toString());
    }
    
    public void forceRefresh() {
        this.contentProvider.refreshElements();
        this.contentProvider.refreshDrawing();
    }
    
    @Override
    public void frame(final long n) {
        if (!this.isForegroundDirty) {
            if (this.isDynamic) {
                if (n - this.lastElementRefresh > 150L) {
                    this.contentProvider.refreshElements();
                    this.lastElementRefresh = n;
                }
                if (n - this.lastBackgroundRefresh > 500L) {
                    this.contentProvider.refreshDrawing();
                    this.lastBackgroundRefresh = n;
                }
            }
            this.invalidateForeground();
        }
    }
    
    public IBackgroundProvider getBackgroundProvider() {
        return this.backgroundProvider;
    }
    
    @Override
    public UiAbstractContainer getContainer() {
        return this.container;
    }
    
    @Override
    public ScriptableObject getContent() {
        return this.contentProvider.content;
    }
    
    public ContentProvider getContentProvider() {
        return this.contentProvider;
    }
    
    public IElementProvider getElementProvider() {
        return this.elementProvider;
    }
    
    @Override
    public HashMap<String, UIElement> getElements() {
        return this.contentProvider.elementMap;
    }
    
    public UIWindowLocation getLocation() {
        return this.location;
    }
    
    public IWindow getParentWindow() {
        return this.parentWindow;
    }
    
    public Object getProperty(final String s) {
        return this.windowProperties.get(s);
    }
    
    public float getScale() {
        return this.location.getDrawingScale();
    }
    
    @Override
    public UIStyle getStyle() {
        return this.style;
    }
    
    public void invalidateAllContent() {
        this.contentProvider.invalidateAllContent();
    }
    
    public void invalidateBackground() {
        this.isBackgroundDirty = true;
    }
    
    @Override
    public void invalidateDrawing(final boolean b) {
        if (this.isOpened && b) {
            this.contentProvider.refreshDrawing();
            this.lastBackgroundRefresh = System.currentTimeMillis();
            this.invalidateBackground();
            return;
        }
        this.postBackgroundRefresh();
    }
    
    @Override
    public void invalidateElements(final boolean b) {
        if (this.isOpened && b) {
            this.contentProvider.refreshElements();
            this.lastElementRefresh = System.currentTimeMillis();
            this.invalidateForeground();
            return;
        }
        this.postElementRefresh();
    }
    
    public void invalidateForeground() {
        this.isForegroundDirty = true;
        this.elementView.invalidate();
    }
    
    public boolean isBlockingBackground() {
        return this.isBlockingBackground;
    }
    
    @Override
    public boolean isDynamic() {
        return this.isDynamic;
    }
    
    @Override
    public boolean isInventoryNeeded() {
        return this.isInventoryNeeded;
    }
    
    public boolean isNotFocusable() {
        return this.isInGameOverlay;
    }
    
    @Override
    public boolean isOpened() {
        return this.isOpened;
    }
    
    public boolean isTouchable() {
        return this.isTouchable;
    }
    
    @Override
    public boolean onBackPressed() {
        if (this.closeOnBackPressed) {
            this.close();
            return true;
        }
        return false;
    }
    
    @Override
    public void open() {
        this.preOpen();
        this.postOpen();
    }
    
    public void postBackgroundRefresh() {
        this.lastBackgroundRefresh = -1L;
    }
    
    public void postElementRefresh() {
        this.lastElementRefresh = -1L;
    }
    
    public void postOpen() {
        while (true) {
            while (true) {
                Label_0115: {
                    synchronized (UIWindow.LOCK) {
                        if (this.isOpened) {
                            return;
                        }
                        this.isOpened = true;
                        this.backgroundProvider.prepareCache();
                        UIUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UIWindow.this.invalidateBackground();
                                UIWindow.this.invalidateForeground();
                            }
                        });
                        WindowParent.openWindow(this);
                        WindowProvider.instance.onWindowOpened(this);
                        if (this.eventListener != null) {
                            this.eventListener.onOpen(this);
                        }
                        final Iterator<UIWindow> iterator = this.adjacentWindows.iterator();
                        if (!iterator.hasNext()) {
                            return;
                        }
                        final UIWindow uiWindow = iterator.next();
                        if (uiWindow != null) {
                            uiWindow.postOpen();
                            break Label_0115;
                        }
                        break Label_0115;
                    }
                }
                continue;
            }
        }
    }
    
    public void preOpen() {
        while (true) {
            while (true) {
                Label_0105: {
                    synchronized (UIWindow.LOCK) {
                        this.setupIfNeeded();
                        if (this.isOpened) {
                            return;
                        }
                        Callback.invokeAPICallback("CustomWindowOpened", this);
                        this.initializeLayout((Context)UIUtils.getContext());
                        this.forceRefresh();
                        this.runCachePreparation(false);
                        final Iterator<UIWindow> iterator = this.adjacentWindows.iterator();
                        if (!iterator.hasNext()) {
                            return;
                        }
                        final UIWindow uiWindow = iterator.next();
                        if (uiWindow != null) {
                            uiWindow.setContainer(this.getContainer());
                            uiWindow.preOpen();
                            break Label_0105;
                        }
                        break Label_0105;
                    }
                }
                continue;
            }
        }
    }
    
    public void putProperty(final String s, final Object o) {
        this.windowProperties.put(s, o);
    }
    
    public void removeAdjacentWindow(final UIWindow uiWindow) {
        this.adjacentWindows.remove(uiWindow);
    }
    
    public void runCachePreparation(final boolean b) {
        if (!b) {
            this.elementProvider.runCachePreparation();
            return;
        }
        new Thread(new Runnable() {
            final /* synthetic */ IElementProvider val$elementProvider = UIWindow.this.elementProvider;
            
            @Override
            public void run() {
                Process.setThreadPriority(19);
                this.val$elementProvider.runCachePreparation();
            }
        }).start();
    }
    
    public void setAsGameOverlay(final boolean isInGameOverlay) {
        this.isInGameOverlay = isInGameOverlay;
    }
    
    public void setBackgroundColor(final int backgroundColor) {
        this.backgroundProvider.setBackgroundColor(backgroundColor);
    }
    
    public void setBlockingBackground(final boolean isBlockingBackground) {
        this.isBlockingBackground = isBlockingBackground;
    }
    
    public void setCloseOnBackPressed(final boolean closeOnBackPressed) {
        this.closeOnBackPressed = closeOnBackPressed;
    }
    
    @Override
    public void setContainer(final UiAbstractContainer container) {
        this.container = container;
    }
    
    public void setContent(ScriptableObject scriptableObjectProperty) {
        this.location = new UIWindowLocation(ScriptableObjectHelper.getScriptableObjectProperty(scriptableObjectProperty, "location", null));
        this.contentProvider.setContentObject(scriptableObjectProperty);
        this.content = scriptableObjectProperty;
        scriptableObjectProperty = ScriptableObjectHelper.getScriptableObjectProperty(scriptableObjectProperty, "style", ScriptableObjectHelper.getScriptableObjectProperty(scriptableObjectProperty, "params", null));
        if (scriptableObjectProperty != null) {
            this.setStyle(new UIStyle(scriptableObjectProperty));
        }
        this.setup();
        BitmapCache.immediateGC();
    }
    
    @Override
    public void setDebugEnabled(final boolean isDebugEnabled) {
        ((UIWindowElementDrawable)this.elementProvider).isDebugEnabled = isDebugEnabled;
    }
    
    public void setDynamic(final boolean isDynamic) {
        this.isDynamic = isDynamic;
    }
    
    public void setEventListener(final IWindowEventListener eventListener) {
        this.eventListener = eventListener;
    }
    
    public void setInventoryNeeded(final boolean isInventoryNeeded) {
        this.isInventoryNeeded = isInventoryNeeded;
    }
    
    public void setParentWindow(final IWindow parentWindow) {
        this.parentWindow = parentWindow;
    }
    
    public void setStyle(final UIStyle style) {
        final UIStyle style2 = this.style;
        this.style = style;
        if (style2 != style) {
            this.invalidateAllContent();
        }
    }
    
    public void setStyle(final ScriptableObject scriptableObject) {
        this.style.addAllBindings(scriptableObject);
        this.invalidateAllContent();
    }
    
    public void setTouchable(final boolean isTouchable) {
        this.isTouchable = isTouchable;
    }
    
    public void updateWindowLocation() {
        HorizonApplication.getTopRunningActivity().runOnUiThread((Runnable)new -$$Lambda$UIWindow$7zqmpxXP2J0FSQJ8xQqnkpXUkys(this));
    }
}
