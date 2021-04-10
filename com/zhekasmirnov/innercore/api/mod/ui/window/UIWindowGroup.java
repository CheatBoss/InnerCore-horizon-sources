package com.zhekasmirnov.innercore.api.mod.ui.window;

import com.zhekasmirnov.innercore.api.mod.ui.elements.*;
import com.zhekasmirnov.innercore.api.mod.ui.container.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import org.mozilla.javascript.*;
import java.util.*;
import com.zhekasmirnov.innercore.utils.*;

public class UIWindowGroup implements IWindow
{
    private static final Object LOCK;
    private HashMap<String, UIElement> allElements;
    public boolean closeOnBackPressed;
    private UiAbstractContainer container;
    private boolean isOpened;
    private UIStyle style;
    private HashMap<String, UIWindow> windowByName;
    private ArrayList<UIWindow> windows;
    
    static {
        LOCK = new Object();
    }
    
    public UIWindowGroup() {
        this.windows = new ArrayList<UIWindow>();
        this.windowByName = new HashMap<String, UIWindow>();
        this.allElements = new HashMap<String, UIElement>();
        this.style = new UIStyle();
        this.isOpened = false;
        this.closeOnBackPressed = false;
    }
    
    public UIWindow addWindow(final String s, final ScriptableObject scriptableObject) {
        final UIWindow uiWindow = new UIWindow(scriptableObject);
        this.addWindowInstance(s, uiWindow);
        return uiWindow;
    }
    
    public void addWindowInstance(final String s, final IWindow window) {
        if (window instanceof UIWindow) {
            this.removeWindow(s);
            final UIWindow uiWindow = (UIWindow)window;
            this.windowByName.put(s, uiWindow);
            this.windows.add(uiWindow);
            if (uiWindow.isOpened()) {
                uiWindow.close();
            }
            if (this.isOpened) {
                this.container.openAs(uiWindow);
            }
            uiWindow.setParentWindow(this);
            return;
        }
        throw new IllegalArgumentException("only default window (UIWindow instance) can be added to the window group.");
    }
    
    @Override
    public void close() {
        synchronized (UIWindowGroup.LOCK) {
            this.isOpened = false;
            final Iterator<UIWindow> iterator = this.windows.iterator();
            while (iterator.hasNext()) {
                iterator.next().close();
            }
            this.allElements.clear();
            WindowProvider.instance.onWindowClosed(this);
        }
    }
    
    @Override
    public void frame(final long n) {
    }
    
    public Collection<UIWindow> getAllWindows() {
        return this.windows;
    }
    
    @Override
    public UiAbstractContainer getContainer() {
        return this.container;
    }
    
    @Override
    public ScriptableObject getContent() {
        return null;
    }
    
    @Override
    public HashMap<String, UIElement> getElements() {
        return this.allElements;
    }
    
    @Override
    public UIStyle getStyle() {
        return this.style;
    }
    
    public UIWindow getWindow(final String s) {
        return this.windowByName.get(s);
    }
    
    public ScriptableObject getWindowContent(final String s) {
        final UIWindow window = this.getWindow(s);
        if (window != null) {
            return window.getContent();
        }
        return null;
    }
    
    public Collection<String> getWindowNames() {
        return this.windowByName.keySet();
    }
    
    public void invalidateAllContent() {
        final Iterator<UIWindow> iterator = this.windows.iterator();
        while (iterator.hasNext()) {
            iterator.next().invalidateAllContent();
        }
    }
    
    @Override
    public void invalidateDrawing(final boolean b) {
        final Iterator<UIWindow> iterator = this.windows.iterator();
        while (iterator.hasNext()) {
            iterator.next().invalidateDrawing(b);
        }
    }
    
    @Override
    public void invalidateElements(final boolean b) {
        final Iterator<UIWindow> iterator = this.windows.iterator();
        while (iterator.hasNext()) {
            iterator.next().invalidateElements(b);
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
    
    public void moveOnTop(final String s) {
        final UIWindow window = this.getWindow(s);
        if (window != null) {
            this.windows.remove(window);
            this.windows.add(window);
        }
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
        while (true) {
            while (true) {
                Label_0226: {
                    synchronized (UIWindowGroup.LOCK) {
                        this.isOpened = true;
                        final long currentTimeMillis = System.currentTimeMillis();
                        this.allElements.clear();
                        final Iterator<UIWindow> iterator = this.windows.iterator();
                        if (iterator.hasNext()) {
                            final UIWindow uiWindow = iterator.next();
                            uiWindow.preOpen();
                            final HashMap<String, UIElement> elements = uiWindow.getElements();
                            for (final String s : elements.keySet()) {
                                this.allElements.put(s, elements.get(s));
                            }
                            break Label_0226;
                        }
                        final Iterator<UIWindow> iterator3 = this.windows.iterator();
                        while (iterator3.hasNext()) {
                            iterator3.next().postOpen();
                        }
                        WindowProvider.instance.onWindowOpened(this);
                        final long currentTimeMillis2 = System.currentTimeMillis();
                        final StringBuilder sb = new StringBuilder();
                        sb.append("window group opened in ");
                        sb.append((currentTimeMillis2 - currentTimeMillis) * 0.001);
                        sb.append(" sec");
                        UIUtils.log(sb.toString());
                        return;
                    }
                }
                continue;
            }
        }
    }
    
    public void refreshAll() {
        final Iterator<String> iterator = this.windowByName.keySet().iterator();
        while (iterator.hasNext()) {
            this.refreshWindow(iterator.next());
        }
    }
    
    public void refreshWindow(final String s) {
        final UIWindow window = this.getWindow(s);
        if (window != null) {
            window.forceRefresh();
        }
    }
    
    public void removeWindow(final String s) {
        if (this.windowByName.containsKey(s)) {
            final UIWindow uiWindow = this.windowByName.get(s);
            uiWindow.setParentWindow(null);
            if (uiWindow.isOpened()) {
                uiWindow.close();
            }
            this.windows.remove(uiWindow);
            this.windowByName.remove(s);
        }
    }
    
    public void setBlockingBackground(final boolean blockingBackground) {
        if (this.windows.size() > 0) {
            this.windows.get(0).setBlockingBackground(blockingBackground);
        }
    }
    
    public void setCloseOnBackPressed(final boolean closeOnBackPressed) {
        this.closeOnBackPressed = closeOnBackPressed;
    }
    
    @Override
    public void setContainer(final UiAbstractContainer uiAbstractContainer) {
        this.container = uiAbstractContainer;
        final Iterator<UIWindow> iterator = this.windows.iterator();
        while (iterator.hasNext()) {
            iterator.next().setContainer(uiAbstractContainer);
        }
    }
    
    @Override
    public void setDebugEnabled(final boolean debugEnabled) {
        final Iterator<UIWindow> iterator = this.windows.iterator();
        while (iterator.hasNext()) {
            iterator.next().setDebugEnabled(debugEnabled);
        }
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
    
    public void setWindowContent(final String s, final ScriptableObject content) {
        final UIWindow window = this.getWindow(s);
        if (window != null) {
            window.setContent(content);
        }
    }
}
