package com.zhekasmirnov.innercore.api.mod.ui;

import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.util.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import com.zhekasmirnov.innercore.api.mod.ui.elements.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.api.mod.ui.background.*;

public class ContentProvider
{
    public ScriptableObject content;
    public ScriptableObject drawing;
    public ScriptableWatcher drawingWatcher;
    public HashMap<String, UIElement> elementMap;
    public ScriptableObject elements;
    public UIWindow window;
    
    public ContentProvider(final UIWindow window) {
        this.elementMap = new HashMap<String, UIElement>();
        this.window = window;
    }
    
    private UIStyle buildWindowStyle() {
        final UIStyle uiStyle = new UIStyle();
        for (IWindow window = this.window; window != null; window = ((UIWindow)window).getParentWindow()) {
            if (!(window instanceof UIWindow)) {
                uiStyle.inherit(window.getStyle());
                break;
            }
            uiStyle.addStyle(window.getStyle());
        }
        return uiStyle;
    }
    
    public void invalidateAllContent() {
        this.drawingWatcher.invalidate();
        this.window.getElementProvider().invalidateAll();
    }
    
    public void refreshDrawing() {
        synchronized (this) {
            this.drawingWatcher.refresh();
            if (this.drawingWatcher.isDirty()) {
                this.setupDrawing();
                this.drawingWatcher.validate();
            }
        }
    }
    
    public void refreshElements() {
        while (true) {
            while (true) {
                Label_0354: {
                    synchronized (this) {
                        if (this.elements == null) {
                            return;
                        }
                        final IElementProvider elementProvider = this.window.getElementProvider();
                        elementProvider.setWindowStyle(this.buildWindowStyle());
                        final Object[] allIds = this.elements.getAllIds();
                        for (int length = allIds.length, i = 0; i < length; ++i) {
                            final Object o = allIds[i];
                            final String string = o.toString();
                            final Object value = this.elements.get(o);
                            if (this.elementMap.containsKey(string)) {
                                final UIElement uiElement = this.elementMap.get(string);
                                final ScriptableWatcher descriptionWatcher = uiElement.descriptionWatcher;
                                if (value == null) {
                                    elementProvider.removeElement(uiElement);
                                    this.elementMap.remove(string);
                                }
                                if (!(value instanceof ScriptableObject)) {
                                    break Label_0354;
                                }
                                descriptionWatcher.setTarget((ScriptableObject)value);
                                descriptionWatcher.refresh();
                                if (!descriptionWatcher.isDirty()) {
                                    break Label_0354;
                                }
                                descriptionWatcher.validate();
                                final UIElement construct = ElementFactory.construct(this.window, (ScriptableObject)value);
                                if (construct == null) {
                                    ICLog.i("ERROR", "failed to construct ui element");
                                }
                                else {
                                    this.elementMap.put(string, construct);
                                    construct.descriptionWatcher = descriptionWatcher;
                                    elementProvider.addOrRefreshElement(construct);
                                    elementProvider.removeElement(uiElement);
                                    if (this.window.getContainer() != null) {
                                        this.window.getContainer().addElementInstance(construct, string);
                                        break Label_0354;
                                    }
                                    break Label_0354;
                                }
                            }
                            else if (value != null && value instanceof ScriptableObject) {
                                final UIElement construct2 = ElementFactory.construct(this.window, (ScriptableObject)value);
                                if (construct2 != null) {
                                    this.elementMap.put(string, construct2);
                                    elementProvider.addOrRefreshElement(construct2);
                                    if (this.window.getContainer() != null) {
                                        this.window.getContainer().addElementInstance(construct2, string);
                                    }
                                }
                            }
                        }
                        return;
                    }
                }
                continue;
            }
        }
    }
    
    public void setContentObject(final ScriptableObject content) {
        this.content = content;
        if (content != null) {
            this.drawing = ScriptableObjectHelper.getScriptableObjectProperty(content, "drawing", null);
            this.drawingWatcher = new ScriptableWatcher(this.drawing);
            this.elements = ScriptableObjectHelper.getScriptableObjectProperty(content, "elements", null);
        }
    }
    
    public void setupDrawing() {
        final IBackgroundProvider backgroundProvider = this.window.getBackgroundProvider();
        backgroundProvider.clearAll();
        final UIStyle buildWindowStyle = this.buildWindowStyle();
        if (this.drawing != null) {
            final Object[] allIds = this.drawing.getAllIds();
            for (int length = allIds.length, i = 0; i < length; ++i) {
                final Object o = allIds[i];
                o.toString();
                final Object value = this.drawing.get(o);
                if (value instanceof ScriptableObject) {
                    final IDrawing construct = DrawingFactory.construct((ScriptableObject)value, buildWindowStyle);
                    if (construct != null) {
                        backgroundProvider.addDrawing(construct);
                    }
                }
            }
        }
        this.window.invalidateBackground();
    }
    
    public void setupElements() {
        final IElementProvider elementProvider = this.window.getElementProvider();
        elementProvider.releaseAll();
        elementProvider.setWindowStyle(this.buildWindowStyle());
        this.elementMap.clear();
        if (this.elements == null) {
            return;
        }
        final Object[] allIds = this.elements.getAllIds();
        for (int length = allIds.length, i = 0; i < length; ++i) {
            final Object o = allIds[i];
            final String string = o.toString();
            final Object value = this.elements.get(o);
            if (value != null && value instanceof ScriptableObject) {
                final UIElement construct = ElementFactory.construct(this.window, (ScriptableObject)value);
                if (construct != null) {
                    this.elementMap.put(string, construct);
                    elementProvider.addOrRefreshElement(construct);
                    if (this.window.getContainer() != null) {
                        this.window.getContainer().addElementInstance(construct, string);
                    }
                }
            }
        }
    }
    
    @Override
    public String toString() {
        int n = 0;
        final Object[] allIds = this.elements.getAllIds();
        int n2;
        for (int length = allIds.length, i = 0; i < length; ++i, n = n2) {
            final Object o = allIds[i];
            o.toString();
            n2 = n;
            if (this.elements.get(o) != null) {
                n2 = n + 1;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("[ContentProvider displayed=");
        sb.append(this.elementMap.size());
        sb.append(" contented=");
        sb.append(n);
        sb.append("(");
        sb.append(allIds.length);
        sb.append(")]");
        return sb.toString();
    }
}
