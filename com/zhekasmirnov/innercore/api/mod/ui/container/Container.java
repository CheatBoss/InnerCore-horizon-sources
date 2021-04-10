package com.zhekasmirnov.innercore.api.mod.ui.container;

import com.zhekasmirnov.innercore.api.mod.recipes.workbench.*;
import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import com.zhekasmirnov.innercore.api.runtime.saver.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.api.mod.ui.elements.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.api.*;
import java.util.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.runtime.*;

@SynthesizedClassMap({ -$$Lambda$Container$0GWP76PDzV21NSsAFj-Bl1ediFk.class, -$$Lambda$Container$oJI0fd12JVmMG4x1uoKpw3Xu_9Y.class })
public class Container implements WorkbenchField, UiAbstractContainer
{
    public static final boolean isContainer = true;
    private static final int saverId;
    private OnCloseListener closeListener;
    private HashMap<String, UIElement> elements;
    private boolean isOpened;
    private OnOpenListener openListener;
    public Object parent;
    public ScriptableObject slots;
    public Object tileEntity;
    private String wbSlotNamePrefix;
    private IWindow window;
    
    static {
        saverId = ObjectSaverRegistry.registerSaver("_container", new ObjectSaver() {
            @Override
            public Object read(final ScriptableObject slots) {
                final Container container = new Container();
                container.slots = slots;
                return container;
            }
            
            @Override
            public ScriptableObject save(final Object o) {
                if (o instanceof Container) {
                    return ((Container)o).slots;
                }
                return null;
            }
        });
    }
    
    public Container() {
        this.slots = new ScriptableObject() {
            public String getClassName() {
                return "slot_array";
            }
        };
        this.isOpened = false;
        this.elements = new HashMap<String, UIElement>();
        this.wbSlotNamePrefix = "slot";
        ObjectSaverRegistry.registerObject(this, Container.saverId);
    }
    
    public Container(final Object parent) {
        this();
        this.setParent(parent);
    }
    
    private void callOnCloseEvent(final boolean b) {
        if (this.closeListener != null) {
            try {
                this.closeListener.onClose(this, this.window);
            }
            catch (Exception ex) {
                DialogHelper.reportNonFatalError("Exception in container close listener", ex);
            }
        }
        Callback.invokeCallback("ContainerClosed", this, this.window, b);
    }
    
    private void callOnOpenEvent() {
        if (this.openListener != null) {
            try {
                this.openListener.onOpen(this, this.window);
            }
            catch (Exception ex) {
                DialogHelper.reportNonFatalError("Exception in container open listener", ex);
            }
        }
        Callback.invokeCallback("ContainerOpened", this, this.window);
    }
    
    public static void initSaverId() {
    }
    
    private void setupInitialBindings() {
        if (this.elements != null) {
            for (final String s : new ArrayList<String>(this.elements.keySet())) {
                if (this.elements == null) {
                    return;
                }
                final UIElement uiElement = this.elements.get(s);
                if (uiElement == null) {
                    continue;
                }
                uiElement.setupInitialBindings(this, s);
            }
        }
    }
    
    public void _addElement(final UIElement uiElement, final String s) {
        if (this.elements != null) {
            this.elements.put(s, uiElement);
        }
        uiElement.setupInitialBindings(this, s);
    }
    
    public void _removeElement(final String s) {
        if (this.elements != null) {
            this.elements.remove(s);
        }
    }
    
    @Override
    public void addElementInstance(final UIElement uiElement, final String s) {
        this._addElement(uiElement, s);
    }
    
    public void applyChanges() {
    }
    
    @Override
    public Scriptable asScriptableField() {
        final Object[] array = new Object[9];
        for (int i = 0; i < 9; ++i) {
            array[i] = this.getFieldSlot(i);
        }
        return (Scriptable)new NativeArray(array);
    }
    
    public void clearSlot(final String s) {
        this.getFullSlot(s).set(0, 0, 0);
    }
    
    @Override
    public void close() {
        if (this.isOpened) {
            this.isOpened = false;
            this.window.close();
            this.callOnCloseEvent(false);
            this.window = null;
            this.elements = null;
        }
    }
    
    public void dropAt(final float n, final float n2, final float n3) {
        final Object[] allIds = this.slots.getAllIds();
        for (int length = allIds.length, i = 0; i < length; ++i) {
            final Object o = allIds[i];
            if (o instanceof String) {
                this.getFullSlot((String)o).drop(n, n2, n3);
            }
        }
    }
    
    public void dropSlot(final String s, final float n, final float n2, final float n3) {
        this.getFullSlot(s).drop(n, n2, n3);
    }
    
    @Override
    public Object getBinding(final String s, final String s2) {
        final UIElement element = this.getElement(s);
        if (element != null) {
            return element.getBinding(s2);
        }
        return null;
    }
    
    @Override
    public UIElement getElement(final String s) {
        if (this.elements != null) {
            final UIElement uiElement = this.elements.get(s);
            if (uiElement != null && !uiElement.isReleased()) {
                return uiElement;
            }
        }
        return null;
    }
    
    @Override
    public Slot getFieldSlot(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.wbSlotNamePrefix);
        sb.append(n);
        return this.getFullSlot(sb.toString());
    }
    
    public Slot getFullSlot(final String s) {
        if (this.slots.has(s, (Scriptable)this.slots)) {
            final Object value = this.slots.get((Object)s);
            if (value instanceof Slot) {
                return (Slot)value;
            }
            if (value instanceof ScriptableObject) {
                return new Slot((ScriptableObject)value);
            }
        }
        final Slot slot = new Slot();
        this.slots.put(s, (Scriptable)this.slots, (Object)slot);
        return slot;
    }
    
    public ScriptableObject getGuiContent() {
        if (this.window != null) {
            return this.window.getContent();
        }
        return null;
    }
    
    public IWindow getGuiScreen() {
        return this.window;
    }
    
    @Override
    public Object getParent() {
        return this.parent;
    }
    
    public Object getSlot(final String s) {
        if (this.slots.has(s, (Scriptable)this.slots)) {
            return this.slots.get((Object)s);
        }
        final Slot slot = new Slot();
        this.slots.put(s, (Scriptable)this.slots, (Object)slot);
        return slot;
    }
    
    @Override
    public UiVisualSlotImpl getSlotVisualImpl(final String s) {
        return new ScriptableUiVisualSlotImpl((ScriptableObject)this.getSlot(s));
    }
    
    public String getText(final String s) {
        return (String)this.getBinding(s, "text");
    }
    
    public float getValue(final String s) {
        return (float)this.getBinding(s, "value");
    }
    
    public IWindow getWindow() {
        return this.window;
    }
    
    @Override
    public void handleBindingDirty(final String s, final String s2) {
    }
    
    @Override
    public void handleInventoryToSlotTransaction(final int n, final String s, final int n2) {
        MainThreadQueue.serverThread.enqueue(new -$$Lambda$Container$oJI0fd12JVmMG4x1uoKpw3Xu_9Y(this, n2, s, n));
    }
    
    @Override
    public void handleSlotToInventoryTransaction(final String s, final int n) {
        MainThreadQueue.serverThread.enqueue(new -$$Lambda$Container$0GWP76PDzV21NSsAFj-Bl1ediFk(this, n, s));
    }
    
    @Override
    public void handleSlotToSlotTransaction(final String s, final String s2, final int n) {
    }
    
    public void invalidateUI() {
        this.invalidateUI(false);
    }
    
    public void invalidateUI(final boolean b) {
        if (this.window != null) {
            this.window.invalidateDrawing(b);
            this.window.invalidateElements(b);
        }
    }
    
    public void invalidateUIDrawing() {
        this.invalidateUIDrawing(false);
    }
    
    public void invalidateUIDrawing(final boolean b) {
        if (this.window != null) {
            this.window.invalidateDrawing(b);
        }
    }
    
    public void invalidateUIElements() {
        this.invalidateUIElements(false);
    }
    
    public void invalidateUIElements(final boolean b) {
        if (this.window != null) {
            this.window.invalidateElements(b);
        }
    }
    
    public boolean isElementTouched(final String s) {
        final UIElement element = this.getElement(s);
        return element != null && element.isTouched;
    }
    
    public boolean isLegacyContainer() {
        return true;
    }
    
    public boolean isOpened() {
        return this.window != null && this.window.isOpened();
    }
    
    @Override
    public void onWindowClosed() {
        if (this.isOpened && !this.window.isOpened()) {
            this.isOpened = false;
            final IWindow window = this.window;
            this.window = null;
            this.elements = null;
            this.callOnCloseEvent(true);
        }
    }
    
    @Override
    public void openAs(final IWindow window) {
        if (this.isOpened) {
            this.close();
        }
        this.window = window;
        this.elements = window.getElements();
        window.setContainer(this);
        this.isOpened = true;
        window.open();
        this.setupInitialBindings();
        this.callOnOpenEvent();
    }
    
    public void refreshSlots() {
    }
    
    public void sendChanges() {
    }
    
    @Override
    public void setBinding(final String s, final String s2, final Object o) {
        final UIElement element = this.getElement(s);
        if (element != null) {
            element.setBinding(s2, o);
        }
    }
    
    public void setOnCloseListener(final OnCloseListener closeListener) {
        this.closeListener = closeListener;
    }
    
    public void setOnOpenListener(final OnOpenListener openListener) {
        this.openListener = openListener;
    }
    
    public void setParent(final Object o) {
        this.tileEntity = o;
        this.parent = o;
    }
    
    public void setScale(final String s, final float n) {
        this.setBinding(s, "value", n);
    }
    
    public void setSlot(final String s, final int n, final int n2, final int n3) {
        this.getFullSlot(s).set(n, n2, n3);
    }
    
    public void setSlot(final String s, final int n, final int n2, final int n3, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        this.getFullSlot(s).set(n, n2, n3, nativeItemInstanceExtra);
    }
    
    public void setText(final String s, final String s2) {
        this.setBinding(s, "text", s2);
    }
    
    public void setWbSlotNamePrefix(final String wbSlotNamePrefix) {
        this.wbSlotNamePrefix = wbSlotNamePrefix;
    }
    
    public void validateAll() {
        final Object[] allIds = this.slots.getAllIds();
        for (int length = allIds.length, i = 0; i < length; ++i) {
            final Object o = allIds[i];
            if (o instanceof String) {
                this.getFullSlot((String)o).validate();
            }
        }
    }
    
    public void validateSlot(final String s) {
        this.getFullSlot(s).validate();
    }
    
    public interface OnCloseListener
    {
        void onClose(final Container p0, final IWindow p1);
    }
    
    public interface OnOpenListener
    {
        void onOpen(final Container p0, final IWindow p1);
    }
}
