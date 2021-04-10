package com.zhekasmirnov.apparatus.api.container;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.ui.elements.*;
import com.zhekasmirnov.apparatus.multiplayer.*;
import com.zhekasmirnov.apparatus.job.*;
import com.zhekasmirnov.innercore.api.mod.ui.container.*;
import com.zhekasmirnov.innercore.api.*;
import java.util.*;
import org.json.*;

@SynthesizedClassMap({ -$$Lambda$ItemContainerUiHandler$D8oL764DiGHEpTQXZP4twTeTv-k.class, -$$Lambda$ItemContainerUiHandler$AhlWxHJNTAcTGVillrDy6-4-c0E.class, -$$Lambda$ItemContainerUiHandler$q3fmOnIpeiFURZBppCo-1mKBQv0.class, -$$Lambda$C3v9v4CdWyVrE2zmv7JMesRaa9k.class })
public class ItemContainerUiHandler implements UiAbstractContainer
{
    private final Map<String, Object> bindingsMap;
    private final ItemContainer container;
    private IWindow window;
    
    public ItemContainerUiHandler(final ItemContainer container) {
        this.window = null;
        this.bindingsMap = new HashMap<String, Object>();
        this.container = container;
    }
    
    private Object validateBindingValue(Object unwrap) {
        while (unwrap instanceof Wrapper) {
            unwrap = ((Wrapper)unwrap).unwrap();
        }
        if (unwrap instanceof CharSequence) {
            return unwrap.toString();
        }
        if (unwrap instanceof Number) {
            return unwrap;
        }
        if (unwrap instanceof Boolean) {
            return unwrap;
        }
        return null;
    }
    
    @Override
    public void addElementInstance(final UIElement uiElement, final String s) {
        uiElement.setupInitialBindings(this, s);
    }
    
    void applyAllBindingsFromMap() {
        while (true) {
            while (true) {
                Label_0115: {
                    synchronized (this.bindingsMap) {
                        final Iterator<Map.Entry<String, Object>> iterator = this.bindingsMap.entrySet().iterator();
                        if (!iterator.hasNext()) {
                            return;
                        }
                        final Map.Entry<String, Object> entry = iterator.next();
                        final String s = entry.getKey();
                        final Object value = entry.getValue();
                        if (value == null) {
                            break Label_0115;
                        }
                        final String[] split = s.split("::");
                        if (split.length != 2) {
                            break Label_0115;
                        }
                        final UIElement element = this.getElement(split[0]);
                        if (element != null) {
                            element.setBinding(split[1], value);
                            break Label_0115;
                        }
                        break Label_0115;
                    }
                }
                continue;
            }
        }
    }
    
    @Override
    public void close() {
        if (this.window != null) {
            this.window.close();
            this.window = null;
            final JobExecutor clientThreadJobExecutor = Network.getSingleton().getClientThreadJobExecutor();
            final ItemContainer container = this.container;
            container.getClass();
            clientThreadJobExecutor.add(new -$$Lambda$C3v9v4CdWyVrE2zmv7JMesRaa9k(container));
        }
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
        if (this.window != null) {
            return this.window.getElements().get(s);
        }
        return null;
    }
    
    @Override
    public Object getParent() {
        return this.container;
    }
    
    @Override
    public UiVisualSlotImpl getSlotVisualImpl(final String s) {
        return new UiVisualSlotImpl() {
            ItemContainerSlot slot;
            
            @Override
            public int getCount() {
                if (this.slot == null) {
                    this.slot = ItemContainerUiHandler.this.container.getSlot(s);
                }
                return this.slot.count;
            }
            
            @Override
            public int getData() {
                if (this.slot == null) {
                    this.slot = ItemContainerUiHandler.this.container.getSlot(s);
                }
                return this.slot.data;
            }
            
            @Override
            public NativeItemInstanceExtra getExtra() {
                if (this.slot == null) {
                    this.slot = ItemContainerUiHandler.this.container.getSlot(s);
                }
                return this.slot.extra;
            }
            
            @Override
            public int getId() {
                this.slot = ItemContainerUiHandler.this.container.getSlot(s);
                return this.slot.id;
            }
        };
    }
    
    public IWindow getWindow() {
        return this.window;
    }
    
    @Override
    public void handleBindingDirty(final String s, final String s2) {
        final Object validateBindingValue = this.validateBindingValue(this.getBinding(s, s2));
        if (validateBindingValue != null) {
            synchronized (this.bindingsMap) {
                final Map<String, Object> bindingsMap = this.bindingsMap;
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append("::");
                sb.append(s2);
                bindingsMap.put(sb.toString(), validateBindingValue);
            }
        }
    }
    
    @Override
    public void handleInventoryToSlotTransaction(final int n, final String s, final int n2) {
        Network.getSingleton().getClientThreadJobExecutor().add(new -$$Lambda$ItemContainerUiHandler$D8oL764DiGHEpTQXZP4twTeTv-k(this, n, s, n2));
    }
    
    @Override
    public void handleSlotToInventoryTransaction(final String s, final int n) {
        Network.getSingleton().getClientThreadJobExecutor().add(new -$$Lambda$ItemContainerUiHandler$q3fmOnIpeiFURZBppCo-1mKBQv0(this, s, n));
    }
    
    @Override
    public void handleSlotToSlotTransaction(final String s, final String s2, final int n) {
        Network.getSingleton().getClientThreadJobExecutor().add(new -$$Lambda$ItemContainerUiHandler$AhlWxHJNTAcTGVillrDy6-4-c0E(this, s, s2, n));
    }
    
    @Override
    public void onWindowClosed() {
        this.close();
    }
    
    @Override
    public void openAs(final IWindow window) {
        this.close();
        (this.window = window).setContainer(this);
        this.window.open();
        final HashMap<String, UIElement> elements = this.window.getElements();
        if (elements != null) {
            for (final String s : new ArrayList<String>((Collection<? extends String>)elements.keySet())) {
                final UIElement uiElement = elements.get(s);
                if (uiElement != null) {
                    uiElement.setupInitialBindings(this, s);
                }
            }
        }
        this.applyAllBindingsFromMap();
    }
    
    void receiveBindingsFromServer(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String s = keys.next();
            final Object validateBindingValue = this.validateBindingValue(jsonObject.opt(s));
            if (validateBindingValue != null) {
                this.setBindingByComposedName(s, validateBindingValue);
            }
        }
    }
    
    @Override
    public void setBinding(final String s, final String s2, final Object o) {
        final Object validateBindingValue = this.validateBindingValue(o);
        if (validateBindingValue == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid binding value for ");
            sb.append(s);
            sb.append("::");
            sb.append(s2);
            sb.append(" value=");
            sb.append(o);
            sb.append(" required types: number, string or boolean");
            throw new IllegalArgumentException(sb.toString());
        }
        final UIElement element = this.getElement(s);
        if (element != null) {
            element.setBinding(s2, validateBindingValue);
        }
        synchronized (this.bindingsMap) {
            final Map<String, Object> bindingsMap = this.bindingsMap;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append("::");
            sb2.append(s2);
            bindingsMap.put(sb2.toString(), validateBindingValue);
        }
    }
    
    void setBindingByComposedName(final String s, final Object o) {
        final String[] split = s.split("::");
        if (split.length == 2) {
            this.setBinding(split[0], split[1], o);
        }
    }
}
