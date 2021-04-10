package com.zhekasmirnov.innercore.api.mod.ui.window;

import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import com.zhekasmirnov.innercore.api.mod.ui.container.*;
import com.zhekasmirnov.innercore.api.mod.ui.elements.*;
import java.util.*;

public class UITabbedWindow implements IWindow
{
    private UIWindow backgroundWindow;
    public boolean closeOnBackPressed;
    public int currentTab;
    private int defaultTabIndex;
    private float frameWidth;
    private boolean hadInitialBackgroundSetup;
    private boolean hasTabsOnLeft;
    private boolean hasTabsOnRight;
    private UIWindowLocation innerWinLoc;
    private boolean isOpened;
    private UIWindow lastOpenedWindow;
    private UIWindowLocation location;
    private float onePixel;
    private float padding;
    private UIStyle style;
    private int tabSize;
    private UIWindow[] tabWindows;
    
    public UITabbedWindow(final UIWindowLocation location) {
        this.tabWindows = new UIWindow[12];
        this.hasTabsOnRight = false;
        this.hasTabsOnLeft = false;
        this.padding = 0.0f;
        this.frameWidth = 1.0f;
        this.onePixel = 0.0f;
        this.innerWinLoc = new UIWindowLocation();
        this.hadInitialBackgroundSetup = false;
        this.defaultTabIndex = -1;
        this.isOpened = false;
        this.lastOpenedWindow = null;
        this.currentTab = -1;
        this.style = new UIStyle();
        this.closeOnBackPressed = false;
        (this.backgroundWindow = new UIWindow(location)).setParentWindow(this);
        this.setLocation(location);
        this.setupBackgroundContents();
    }
    
    public UITabbedWindow(ScriptableObject empty) {
        this(new UIWindowLocation(ScriptableObjectHelper.getScriptableObjectProperty(empty, "location", null)));
        if (!ScriptableObjectHelper.getBooleanProperty(empty, "isButtonHidden", false)) {
            empty = ScriptableObjectHelper.createEmpty();
            final float n = this.getWindowTabSize() / 1.72f;
            final ScriptableObject empty2 = ScriptableObjectHelper.createEmpty();
            empty2.put("type", (Scriptable)empty2, (Object)"closeButton");
            empty2.put("x", (Scriptable)empty2, (Object)(n / -2.0f));
            empty2.put("y", (Scriptable)empty2, (Object)(n / -2.0f));
            empty2.put("scale", (Scriptable)empty2, (Object)(n / 18.0f));
            empty2.put("bitmap", (Scriptable)empty2, (Object)"style:close_button_up");
            empty2.put("bitmap2", (Scriptable)empty2, (Object)"style:close_button_down");
            empty.put("closeButton", (Scriptable)empty, (Object)empty2);
            this.setFakeTab(0, empty);
        }
    }
    
    private void refreshWindowLocation() {
        final UIWindowLocation uiWindowLocation = new UIWindowLocation();
        uiWindowLocation.set(this.location);
        final float n = (float)uiWindowLocation.x;
        final float padding = this.padding;
        final boolean hasTabsOnLeft = this.hasTabsOnLeft;
        final int n2 = 0;
        int tabSize;
        if (hasTabsOnLeft) {
            tabSize = this.tabSize;
        }
        else {
            tabSize = 0;
        }
        final int x = (int)(n + padding + tabSize);
        final float n3 = (float)(uiWindowLocation.x + uiWindowLocation.width);
        final float padding2 = this.padding;
        int tabSize2;
        if (this.hasTabsOnRight) {
            tabSize2 = this.tabSize;
        }
        else {
            tabSize2 = 0;
        }
        final int n4 = (int)(n3 - (padding2 + tabSize2));
        final int y = (int)(uiWindowLocation.y + this.padding * 3.0f);
        final int n5 = (int)(uiWindowLocation.y + uiWindowLocation.height - this.padding * 3.0f);
        this.innerWinLoc.set(x, y, n4 - x, n5 - y);
        int n6 = 0;
        final UIWindow[] tabWindows = this.tabWindows;
        for (int length = tabWindows.length, i = n2; i < length; ++i) {
            final UIWindow uiWindow = tabWindows[i];
            if (uiWindow != null) {
                final UIWindowLocation location = uiWindow.getLocation();
                location.x = x;
                location.y = y;
                if (location.scrollX != location.width) {
                    location.scrollX = Math.max(location.scrollX, n4 - x);
                }
                else {
                    location.scrollX = n4 - x;
                }
                if (location.scrollY != location.height) {
                    location.scrollY = Math.max(location.scrollY, n5 - y);
                }
                else {
                    location.scrollY = n5 - y;
                }
                location.width = n4 - x;
                location.height = n5 - y;
            }
            ++n6;
        }
    }
    
    private void setupBackgroundContents() {
        final float windowTabSize = this.getWindowTabSize();
        final WindowContentAdapter windowContentAdapter = new WindowContentAdapter(this.backgroundWindow.getContent());
        windowContentAdapter.setLocation(this.location);
        if (!this.hadInitialBackgroundSetup) {
            this.hadInitialBackgroundSetup = true;
            final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
            empty.put("type", (Scriptable)empty, (Object)"color");
            empty.put("color", (Scriptable)empty, (Object)0);
            windowContentAdapter.addDrawing(empty);
        }
        final boolean hasTabsOnLeft = this.hasTabsOnLeft;
        float n = 0.0f;
        float n2;
        if (hasTabsOnLeft) {
            n2 = windowTabSize - this.frameWidth;
        }
        else {
            n2 = 0.0f;
        }
        if (this.hasTabsOnRight) {
            n = windowTabSize - this.frameWidth;
        }
        final ScriptableObject empty2 = ScriptableObjectHelper.createEmpty();
        empty2.put("type", (Scriptable)empty2, (Object)"frame");
        empty2.put("x", (Scriptable)empty2, (Object)n2);
        empty2.put("y", (Scriptable)empty2, (Object)0);
        empty2.put("width", (Scriptable)empty2, (Object)(1000.0f - n - n2));
        empty2.put("height", (Scriptable)empty2, (Object)this.location.getWindowHeight());
        empty2.put("scale", (Scriptable)empty2, (Object)(this.frameWidth / this.getStyleSafe().getIntProperty("tab_frame_width", 2)));
        empty2.put("bitmap", (Scriptable)empty2, (Object)"style:frame_background");
        empty2.put("color", (Scriptable)empty2, (Object)this.getStyleSafe().getIntProperty("window_background", -16777216));
        windowContentAdapter.addElement("tabbedWinBackground", empty2);
        this.backgroundWindow.setContent(windowContentAdapter.getContent());
    }
    
    @Override
    public void close() {
        this.isOpened = false;
        if (this.backgroundWindow != null) {
            this.backgroundWindow.close();
        }
        if (this.lastOpenedWindow != null) {
            this.lastOpenedWindow.close();
        }
        this.lastOpenedWindow = null;
        WindowProvider.instance.onWindowClosed(this);
    }
    
    @Override
    public void frame(final long n) {
    }
    
    @Override
    public UiAbstractContainer getContainer() {
        if (this.backgroundWindow == null) {
            return null;
        }
        return this.backgroundWindow.getContainer();
    }
    
    @Override
    public ScriptableObject getContent() {
        if (this.backgroundWindow == null) {
            return null;
        }
        return this.backgroundWindow.getContent();
    }
    
    public int getDefaultTab() {
        return this.defaultTabIndex;
    }
    
    @Override
    public HashMap<String, UIElement> getElements() {
        final HashMap<String, UIElement> hashMap = new HashMap<String, UIElement>();
        final UIWindow[] tabWindows = this.tabWindows;
        for (int length = tabWindows.length, i = 0; i < length; ++i) {
            final UIWindow uiWindow = tabWindows[i];
            if (uiWindow != null) {
                final HashMap<String, UIElement> elements = uiWindow.getElements();
                for (final String s : elements.keySet()) {
                    hashMap.put(s, elements.get(s));
                }
            }
        }
        if (this.backgroundWindow != null) {
            final HashMap<String, UIElement> elements2 = this.backgroundWindow.getElements();
            for (final String s2 : elements2.keySet()) {
                hashMap.put(s2, elements2.get(s2));
            }
        }
        return hashMap;
    }
    
    public float getGlobalTabSize() {
        return this.tabSize - this.location.windowToGlobal(this.frameWidth);
    }
    
    public float getInnerWindowHeight() {
        return (float)this.innerWinLoc.height;
    }
    
    public float getInnerWindowWidth() {
        return (float)this.innerWinLoc.width;
    }
    
    @Override
    public UIStyle getStyle() {
        return this.style;
    }
    
    public UIStyle getStyleSafe() {
        final UIStyle style = this.getStyle();
        if (style != null) {
            return style;
        }
        return UIStyle.DEFAULT;
    }
    
    public UIWindow getWindowForTab(final int n) {
        return this.tabWindows[n];
    }
    
    public float getWindowTabSize() {
        return this.location.globalToWindow((float)this.tabSize) - this.frameWidth;
    }
    
    @Override
    public void invalidateDrawing(final boolean b) {
        if (this.isOpened) {
            if (this.backgroundWindow != null) {
                this.backgroundWindow.invalidateDrawing(b);
            }
            if (this.lastOpenedWindow != null) {
                this.lastOpenedWindow.invalidateDrawing(b);
            }
        }
    }
    
    @Override
    public void invalidateElements(final boolean b) {
        if (this.isOpened) {
            if (this.backgroundWindow != null) {
                this.backgroundWindow.invalidateElements(b);
            }
            if (this.lastOpenedWindow != null) {
                this.lastOpenedWindow.invalidateElements(b);
            }
        }
    }
    
    @Override
    public boolean isDynamic() {
        return false;
    }
    
    @Override
    public boolean isInventoryNeeded() {
        return false;
    }
    
    @Override
    public boolean isOpened() {
        return this.isOpened;
    }
    
    @Override
    public boolean onBackPressed() {
        if (this.closeOnBackPressed) {
            this.close();
            return true;
        }
        return false;
    }
    
    public void onTabSelected(final int currentTab) {
        if (this.isOpened() && currentTab >= 0 && currentTab < 12) {
            final UIWindow lastOpenedWindow = this.tabWindows[currentTab];
            if (lastOpenedWindow != null && lastOpenedWindow != this.lastOpenedWindow) {
                this.currentTab = currentTab;
                lastOpenedWindow.open();
                if (this.lastOpenedWindow != null) {
                    this.lastOpenedWindow.setParentWindow(null);
                    this.lastOpenedWindow.close();
                    this.lastOpenedWindow.setParentWindow(this);
                }
                this.lastOpenedWindow = lastOpenedWindow;
            }
        }
    }
    
    @Override
    public void open() {
        this.isOpened = true;
        if (this.backgroundWindow != null) {
            this.backgroundWindow.preOpen();
        }
        if (this.defaultTabIndex >= 0 && this.defaultTabIndex < 11) {
            final UIWindow lastOpenedWindow = this.tabWindows[this.defaultTabIndex];
            if (lastOpenedWindow != null) {
                this.currentTab = this.defaultTabIndex;
                (this.lastOpenedWindow = lastOpenedWindow).preOpen();
            }
        }
        if (this.backgroundWindow != null) {
            this.backgroundWindow.postOpen();
        }
        if (this.lastOpenedWindow != null) {
            this.lastOpenedWindow.postOpen();
        }
        WindowProvider.instance.onWindowOpened(this);
    }
    
    public void setBlockingBackground(final boolean blockingBackground) {
        this.backgroundWindow.setBlockingBackground(blockingBackground);
    }
    
    public void setCloseOnBackPressed(final boolean closeOnBackPressed) {
        this.closeOnBackPressed = closeOnBackPressed;
    }
    
    @Override
    public void setContainer(final UiAbstractContainer uiAbstractContainer) {
        if (this.backgroundWindow != null) {
            this.backgroundWindow.setContainer(uiAbstractContainer);
        }
        final UIWindow[] tabWindows = this.tabWindows;
        for (int length = tabWindows.length, i = 0; i < length; ++i) {
            final UIWindow uiWindow = tabWindows[i];
            if (uiWindow != null) {
                uiWindow.setContainer(uiAbstractContainer);
            }
        }
    }
    
    @Override
    public void setDebugEnabled(final boolean b) {
        if (this.backgroundWindow != null) {
            this.backgroundWindow.setDebugEnabled(b);
        }
        final UIWindow[] tabWindows = this.tabWindows;
        for (int length = tabWindows.length, i = 0; i < length; ++i) {
            final UIWindow uiWindow = tabWindows[i];
            if (uiWindow != null) {
                uiWindow.setDebugEnabled(b);
            }
        }
    }
    
    public void setDefaultTab(final int defaultTabIndex) {
        this.defaultTabIndex = defaultTabIndex;
    }
    
    public void setEventListener(final IWindowEventListener eventListener) {
        this.backgroundWindow.setEventListener(eventListener);
    }
    
    public void setFakeTab(final int n, final ScriptableObject scriptableObject) {
        this.setTab(n, scriptableObject, null, true);
    }
    
    public void setLocation(final UIWindowLocation uiWindowLocation) {
        (this.location = uiWindowLocation.copy()).removeScroll();
        this.backgroundWindow.getLocation().set(this.location);
        this.padding = this.location.height / 71.25f / 1.4f;
        this.frameWidth = this.location.globalToWindow(this.padding);
        this.tabSize = (int)(this.location.height / 6.0f);
        this.onePixel = 1.0f / this.location.getDrawingScale();
        this.refreshWindowLocation();
    }
    
    public void setStyle(final UIStyle style) {
        this.style = style;
    }
    
    public void setStyle(final ScriptableObject scriptableObject) {
        this.style.addAllBindings(scriptableObject);
    }
    
    public void setTab(final int n, final ScriptableObject scriptableObject, final ScriptableObject scriptableObject2) {
        this.setTab(n, scriptableObject, scriptableObject2, false);
    }
    
    public void setTab(final int defaultTabIndex, final ScriptableObject scriptableObject, final ScriptableObject scriptableObject2, final boolean b) {
        if (defaultTabIndex >= 0 && defaultTabIndex <= 11) {
            final boolean b2 = defaultTabIndex > 5;
            boolean b4;
            final boolean b3 = b4 = false;
            if (b2) {
                b4 = b3;
                if (!this.hasTabsOnRight) {
                    this.hasTabsOnRight = true;
                    b4 = true;
                }
            }
            boolean b5 = b4;
            if (!b2) {
                b5 = b4;
                if (!this.hasTabsOnLeft) {
                    this.hasTabsOnLeft = true;
                    b5 = true;
                }
            }
            final float windowTabSize = this.getWindowTabSize();
            final WindowContentAdapter windowContentAdapter = new WindowContentAdapter(this.backgroundWindow.getContent());
            final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
            String s;
            if (b2) {
                s = "left";
            }
            else {
                s = "right";
            }
            empty.put(s, (Scriptable)empty, (Object)false);
            final int intProperty = this.getStyleSafe().getIntProperty("tab_frame_width", 2);
            final float n = this.onePixel + windowTabSize;
            float n2;
            if (b2) {
                n2 = 1000.0f - n;
            }
            else {
                n2 = 0.0f;
            }
            final float n3 = defaultTabIndex % 6 * (this.frameWidth + windowTabSize);
            final ScriptableObject empty2 = ScriptableObjectHelper.createEmpty();
            empty2.put("type", (Scriptable)empty2, (Object)"tab");
            empty2.put("x", (Scriptable)empty2, (Object)n2);
            empty2.put("y", (Scriptable)empty2, (Object)n3);
            empty2.put("width", (Scriptable)empty2, (Object)n);
            empty2.put("height", (Scriptable)empty2, (Object)windowTabSize);
            empty2.put("tabIndex", (Scriptable)empty2, (Object)defaultTabIndex);
            empty2.put("isAlwaysSelected", (Scriptable)empty2, (Object)b);
            String s2;
            if (b2) {
                s2 = "style:frame_tab_right";
            }
            else {
                s2 = "style:frame_tab_left";
            }
            empty2.put("bitmap", (Scriptable)empty2, (Object)s2);
            empty2.put("deselectedColor", (Scriptable)empty2, (Object)this.getStyleSafe().getIntProperty("tab_background", -16777216));
            empty2.put("selectedColor", (Scriptable)empty2, (Object)this.getStyleSafe().getIntProperty("tab_background_selected", -16777216));
            empty2.put("scale", (Scriptable)empty2, (Object)(this.frameWidth / intProperty));
            empty2.put("sides", (Scriptable)empty2, (Object)empty);
            final StringBuilder sb = new StringBuilder();
            sb.append("windowTab");
            sb.append(defaultTabIndex);
            windowContentAdapter.addElement(sb.toString(), empty2);
            final Object[] allIds = scriptableObject.getAllIds();
            final int length = allIds.length;
            int i = 0;
            final Object[] array = allIds;
            while (i < length) {
                final Object o = array[i];
                if (o instanceof String) {
                    final String s3 = (String)o;
                    final ScriptableObject scriptableObjectProperty = ScriptableObjectHelper.getScriptableObjectProperty(scriptableObject, s3, null);
                    if (scriptableObjectProperty != null) {
                        scriptableObjectProperty.put("x", (Scriptable)scriptableObjectProperty, (Object)(ScriptableObjectHelper.getFloatProperty(scriptableObjectProperty, "x", 0.0f) + (n2 + n / 2.0f)));
                        scriptableObjectProperty.put("y", (Scriptable)scriptableObjectProperty, (Object)(ScriptableObjectHelper.getFloatProperty(scriptableObjectProperty, "y", 0.0f) + (n3 + windowTabSize / 2.0f)));
                        scriptableObjectProperty.put("z", (Scriptable)scriptableObjectProperty, (Object)(ScriptableObjectHelper.getFloatProperty(scriptableObjectProperty, "z", 0.0f) + 2.0f));
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("tab");
                        sb2.append(defaultTabIndex);
                        sb2.append("_");
                        sb2.append(s3);
                        windowContentAdapter.addElement(sb2.toString(), scriptableObjectProperty);
                    }
                }
                ++i;
            }
            if (scriptableObject2 != null) {
                final UIWindow uiWindow = new UIWindow(scriptableObject2);
                uiWindow.setParentWindow(this);
                this.tabWindows[defaultTabIndex] = uiWindow;
                if (!b && this.defaultTabIndex == -1) {
                    this.defaultTabIndex = defaultTabIndex;
                    empty2.put("isSelected", (Scriptable)empty2, (Object)true);
                }
            }
            this.refreshWindowLocation();
            if (b5) {
                this.setupBackgroundContents();
            }
            return;
        }
        throw new IllegalArgumentException("tab index is invalid: it need to be between 0 and 11");
    }
    
    public void setTabEventListener(final int n, final IWindowEventListener eventListener) {
        final UIWindow windowForTab = this.getWindowForTab(n);
        if (windowForTab != null) {
            windowForTab.setEventListener(eventListener);
        }
    }
}
