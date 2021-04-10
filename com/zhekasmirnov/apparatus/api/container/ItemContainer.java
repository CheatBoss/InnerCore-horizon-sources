package com.zhekasmirnov.apparatus.api.container;

import com.zhekasmirnov.innercore.api.mod.recipes.workbench.*;
import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.multiplayer.util.entity.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.runtime.saver.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.apparatus.multiplayer.*;
import com.zhekasmirnov.apparatus.util.*;
import java.util.function.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;
import org.json.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import com.zhekasmirnov.apparatus.multiplayer.util.list.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.mod.ui.container.*;
import com.zhekasmirnov.apparatus.mcpe.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.item.*;
import com.zhekasmirnov.apparatus.job.*;

@SynthesizedClassMap({ -$$Lambda$ItemContainer$uEMIsnL6wuEoNCgS2puci62aMH4.class, -$$Lambda$ItemContainer$3WoRr1QvtMFvd4Nfxuwr-MaasZk.class, -$$Lambda$ItemContainer$a_i4dG8Z58aMQKVqafKwMbnxt5w.class, -$$Lambda$ItemContainer$Pc9zB768uwRaPq1HjHhPxZwNY5s.class, -$$Lambda$ItemContainer$BuxeOmjH4FOVhk5tFn-38c2xBl4.class, -$$Lambda$ItemContainer$uV2QY_UpRf9jDPIYEdGfqzyQf2I.class, -$$Lambda$ItemContainer$Z9moVxWnZihcNPnhAnoAvwMnoFc.class, -$$Lambda$ItemContainer$oc_kZJdaplqFRvuBSutaggq760c.class, -$$Lambda$ItemContainer$HK13LJ5Uz-7FJNSZJ-RBwDgPMjU.class, -$$Lambda$ItemContainer$ODQ51KC_j1wTj-RLJ6mAKO0BhI8.class, -$$Lambda$ItemContainer$_SnK6Z2znu5wkNTRXFjZbHdYnrw.class, -$$Lambda$ItemContainer$LmHx_FECP1Yy4hRa08RlFbldGlU.class, -$$Lambda$ItemContainer$D9t_d9BbYiTtryAP89IQA2TkkA8.class, -$$Lambda$ItemContainer$SP4izZCJ8KQQ5lqFx24VnXtPRB8.class, -$$Lambda$ItemContainer$q9BtiSW0fDzKrCiJLqKQKSHDKzo.class, -$$Lambda$ItemContainer$I2h4O_GB0bl7lgUkLeBfBEP1pYI.class, -$$Lambda$ItemContainer$u1bXvcoDnB6PkvSBUvDFMn1XcEM.class, -$$Lambda$ItemContainer$EQ70x4S0Ul9NNBy26WS1SZGogtg.class, -$$Lambda$ItemContainer$cIoydHxqwhb3F5elrsf8wN_yIvc.class, -$$Lambda$ItemContainer$k1W1H1JeG0jhLR5oLBFeifwSYXY.class, -$$Lambda$ItemContainer$AqfweQ5FQCn39QSwHyWVd_XKNZ8.class, -$$Lambda$ItemContainer$4SAyyG3hv6Ht_HaGcdM_9UgmPEc.class, -$$Lambda$ItemContainer$g08fLvuUDAYRDU-0GZzAtmVrrLY.class, -$$Lambda$ItemContainer$KsZxcWXxlknoi9-Dw3SthwQiIeA.class })
public class ItemContainer implements WorkbenchField
{
    private static final Map<String, Map<String, ClientEventListener>> clientEventListenerMap;
    private static final Map<String, List<ClientOnCloseListener>> clientOnCloseListenerMap;
    private static final Map<String, List<ClientOnOpenListener>> clientOnOpenListenerMap;
    private static final NetworkEntityType containerNetworkEntityType;
    private static final Object playerInventoryLock;
    private static final int saverId;
    private static final Map<String, UiScreenFactory> screenFactoryMap;
    private final Map<String, TransferPolicy> addTransferPolicyMap;
    private final Map<String, BindingValidator> bindingValidatorMap;
    private final Map<String, Object> bindingsMap;
    private String clientContainerTypeName;
    private final Map<String, Object> dirtyBindingsMap;
    private final Map<String, DirtySlotListener> dirtySlotListenerMap;
    private final Set<String> dirtySlotSet;
    private final Map<String, TransferPolicy> getTransferPolicyMap;
    private TransferPolicy globalAddTransferPolicy;
    private BindingValidator globalBindingValidator;
    private DirtySlotListener globalDirtySlotListener;
    private TransferPolicy globalGetTransferPolicy;
    private boolean globalSlotSavingEnabled;
    public final boolean isServer;
    private String legacyWorkbenchFieldPrefix;
    private final NetworkEntity networkEntity;
    private final List<ServerOnCloseListener> onCloseListeners;
    private final List<ServerOnOpenListener> onOpenListeners;
    private Object parent;
    private final Map<String, ServerEventListener> serverEventListenerMap;
    private final Map<String, ItemContainerSlot> slotMap;
    public final Scriptable slots;
    public final Object transactionLock;
    private final ItemContainerUiHandler uiAdapter;
    
    static {
        playerInventoryLock = new Object();
        screenFactoryMap = new HashMap<String, UiScreenFactory>();
        clientEventListenerMap = new HashMap<String, Map<String, ClientEventListener>>();
        clientOnOpenListenerMap = new HashMap<String, List<ClientOnOpenListener>>();
        clientOnCloseListenerMap = new HashMap<String, List<ClientOnCloseListener>>();
        saverId = ObjectSaverRegistry.registerSaver("_container2", new ObjectSaver() {
            @Override
            public Object read(final ScriptableObject slots) {
                final Container container = new Container();
                container.slots = slots;
                return new ItemContainer(container);
            }
            
            @Override
            public ScriptableObject save(final Object o) {
                if (o instanceof ItemContainer) {
                    return ((ItemContainer)o).asLegacyContainer(false).slots;
                }
                return null;
            }
        });
        containerNetworkEntityType = new NetworkEntityType("sys.container").setClientListSetupListener((NetworkEntityType.OnClientListSetupListener)-$$Lambda$ItemContainer$a_i4dG8Z58aMQKVqafKwMbnxt5w.INSTANCE).addServerPacketListener("close", (NetworkEntityType.OnServerPacketListener)-$$Lambda$ItemContainer$oc_kZJdaplqFRvuBSutaggq760c.INSTANCE).addServerPacketListener("inv_to_slot", (NetworkEntityType.OnServerPacketListener)-$$Lambda$ItemContainer$cIoydHxqwhb3F5elrsf8wN_yIvc.INSTANCE).addServerPacketListener("slot_to_inv", (NetworkEntityType.OnServerPacketListener)-$$Lambda$ItemContainer$BuxeOmjH4FOVhk5tFn-38c2xBl4.INSTANCE).addServerPacketListener("slot_to_slot", (NetworkEntityType.OnServerPacketListener)-$$Lambda$ItemContainer$uV2QY_UpRf9jDPIYEdGfqzyQf2I.INSTANCE).addServerPacketListener("bindings", (NetworkEntityType.OnServerPacketListener)-$$Lambda$ItemContainer$KsZxcWXxlknoi9-Dw3SthwQiIeA.INSTANCE).addServerPacketListener("event", (NetworkEntityType.OnServerPacketListener)-$$Lambda$ItemContainer$3WoRr1QvtMFvd4Nfxuwr-MaasZk.INSTANCE).setClientAddPacketFactory((NetworkEntityType.ClientAddPacketFactory)-$$Lambda$ItemContainer$g08fLvuUDAYRDU-0GZzAtmVrrLY.INSTANCE).setClientEntityAddedListener((NetworkEntityType.OnClientEntityAddedListener)-$$Lambda$ItemContainer$Z9moVxWnZihcNPnhAnoAvwMnoFc.INSTANCE).setClientEntityRemovedListener((NetworkEntityType.OnClientEntityRemovedListener)-$$Lambda$ItemContainer$EQ70x4S0Ul9NNBy26WS1SZGogtg.INSTANCE).addClientPacketListener("open", (NetworkEntityType.OnClientPacketListener)-$$Lambda$ItemContainer$k1W1H1JeG0jhLR5oLBFeifwSYXY.INSTANCE).addClientPacketListener("slots", (NetworkEntityType.OnClientPacketListener)-$$Lambda$ItemContainer$Pc9zB768uwRaPq1HjHhPxZwNY5s.INSTANCE).addClientPacketListener("bindings", (NetworkEntityType.OnClientPacketListener)-$$Lambda$ItemContainer$HK13LJ5Uz-7FJNSZJ-RBwDgPMjU.INSTANCE).addClientPacketListener("event", (NetworkEntityType.OnClientPacketListener)-$$Lambda$ItemContainer$ODQ51KC_j1wTj-RLJ6mAKO0BhI8.INSTANCE);
    }
    
    public ItemContainer() {
        this((String)null);
    }
    
    private ItemContainer(final NetworkEntity networkEntity) {
        this.uiAdapter = new ItemContainerUiHandler(this);
        this.parent = null;
        this.transactionLock = new Object();
        this.slotMap = new HashMap<String, ItemContainerSlot>();
        this.dirtySlotSet = new HashSet<String>();
        this.bindingsMap = new HashMap<String, Object>();
        this.dirtyBindingsMap = new HashMap<String, Object>();
        this.globalAddTransferPolicy = null;
        this.globalGetTransferPolicy = null;
        this.addTransferPolicyMap = new HashMap<String, TransferPolicy>();
        this.getTransferPolicyMap = new HashMap<String, TransferPolicy>();
        this.globalDirtySlotListener = null;
        this.dirtySlotListenerMap = new HashMap<String, DirtySlotListener>();
        this.globalBindingValidator = null;
        this.bindingValidatorMap = new HashMap<String, BindingValidator>();
        this.clientContainerTypeName = null;
        this.serverEventListenerMap = new HashMap<String, ServerEventListener>();
        this.onOpenListeners = new ArrayList<ServerOnOpenListener>();
        this.onCloseListeners = new ArrayList<ServerOnCloseListener>();
        this.slots = (Scriptable)ScriptableObjectHelper.createEmpty();
        this.globalSlotSavingEnabled = true;
        this.legacyWorkbenchFieldPrefix = null;
        this.networkEntity = networkEntity;
        this.isServer = false;
    }
    
    public ItemContainer(final Container container) {
        this();
        final Object[] allIds = container.slots.getAllIds();
        for (int length = allIds.length, i = 0; i < length; ++i) {
            final Object o = allIds[i];
            final Object value = container.slots.get(o.toString(), (Scriptable)container.slots);
            if (value instanceof ScriptableObject) {
                this.setSlot(o.toString(), new ItemContainerSlot((ScriptableObject)value));
            }
        }
    }
    
    private ItemContainer(final String s) {
        this.uiAdapter = new ItemContainerUiHandler(this);
        this.parent = null;
        this.transactionLock = new Object();
        this.slotMap = new HashMap<String, ItemContainerSlot>();
        this.dirtySlotSet = new HashSet<String>();
        this.bindingsMap = new HashMap<String, Object>();
        this.dirtyBindingsMap = new HashMap<String, Object>();
        this.globalAddTransferPolicy = null;
        this.globalGetTransferPolicy = null;
        this.addTransferPolicyMap = new HashMap<String, TransferPolicy>();
        this.getTransferPolicyMap = new HashMap<String, TransferPolicy>();
        this.globalDirtySlotListener = null;
        this.dirtySlotListenerMap = new HashMap<String, DirtySlotListener>();
        this.globalBindingValidator = null;
        this.bindingValidatorMap = new HashMap<String, BindingValidator>();
        this.clientContainerTypeName = null;
        this.serverEventListenerMap = new HashMap<String, ServerEventListener>();
        this.onOpenListeners = new ArrayList<ServerOnOpenListener>();
        this.onCloseListeners = new ArrayList<ServerOnCloseListener>();
        this.slots = (Scriptable)ScriptableObjectHelper.createEmpty();
        this.globalSlotSavingEnabled = true;
        this.legacyWorkbenchFieldPrefix = null;
        NetworkThreadMarker.assertServerThread();
        ObjectSaverRegistry.registerObject(this, ItemContainer.saverId);
        this.isServer = true;
        if (s != null) {
            this.networkEntity = new NetworkEntity(ItemContainer.containerNetworkEntityType, this, s);
            return;
        }
        this.networkEntity = new NetworkEntity(ItemContainer.containerNetworkEntityType, this);
    }
    
    public static void addClientCloseListener(final String s, final ClientOnCloseListener clientOnCloseListener) {
        Java8BackComp.computeIfAbsent(ItemContainer.clientOnCloseListenerMap, s, -$$Lambda$ItemContainer$4SAyyG3hv6Ht_HaGcdM_9UgmPEc.INSTANCE).add(clientOnCloseListener);
    }
    
    public static void addClientEventListener(final String s, final String s2, final ClientEventListener clientEventListener) {
        Java8BackComp.computeIfAbsent(ItemContainer.clientEventListenerMap, s, -$$Lambda$ItemContainer$I2h4O_GB0bl7lgUkLeBfBEP1pYI.INSTANCE).put(s2, clientEventListener);
    }
    
    public static void addClientOpenListener(final String s, final ClientOnOpenListener clientOnOpenListener) {
        Java8BackComp.computeIfAbsent(ItemContainer.clientOnOpenListenerMap, s, -$$Lambda$ItemContainer$LmHx_FECP1Yy4hRa08RlFbldGlU.INSTANCE).add(clientOnOpenListener);
    }
    
    private void closeUi() {
        if (this.isServer) {
            throw new IllegalStateException();
        }
        this.uiAdapter.close();
    }
    
    public static ItemContainer getClientContainerInstance(final String s) {
        final NetworkEntity clientEntityInstance = NetworkEntity.getClientEntityInstance(s);
        if (clientEntityInstance != null) {
            final Object target = clientEntityInstance.getTarget();
            if (target instanceof ItemContainer) {
                return (ItemContainer)target;
            }
        }
        return null;
    }
    
    public static void loadClass() {
    }
    
    private void openUi(final String s) {
        if (this.isServer) {
            throw new IllegalStateException();
        }
        if (this.clientContainerTypeName != null) {
            final List<ClientOnOpenListener> list = ItemContainer.clientOnOpenListenerMap.get(this.clientContainerTypeName);
            if (list != null) {
                final Iterator<ClientOnOpenListener> iterator = list.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onOpen(this, s);
                }
            }
            final UiScreenFactory uiScreenFactory = ItemContainer.screenFactoryMap.get(this.clientContainerTypeName);
            if (uiScreenFactory != null) {
                final IWindow byName = uiScreenFactory.getByName(this, s);
                if (byName != null) {
                    this.uiAdapter.openAs(byName);
                    return;
                }
            }
        }
        this.sendClosed();
    }
    
    public static void registerScreenFactory(final String s, final UiScreenFactory uiScreenFactory) {
        ItemContainer.screenFactoryMap.put(s, uiScreenFactory);
    }
    
    public void addServerCloseListener(final ServerOnCloseListener serverOnCloseListener) {
        this.onCloseListeners.add(serverOnCloseListener);
    }
    
    public void addServerEventListener(final String s, final ServerEventListener serverEventListener) {
        this.serverEventListenerMap.put(s, serverEventListener);
    }
    
    public void addServerOpenListener(final ServerOnOpenListener serverOnOpenListener) {
        this.onOpenListeners.add(serverOnOpenListener);
    }
    
    public int addToSlot(final String s, final int id, int transfer, final int data, final NativeItemInstanceExtra extra, final long n) {
        if (transfer <= 0) {
            return 0;
        }
        final ItemContainerSlot slot = this.getSlot(s);
        if (slot != null && (slot.id == 0 || (slot.id == id && slot.data == data && slot.extra == null))) {
            final TransferPolicy addTransferPolicy = this.getAddTransferPolicy(s);
            if (addTransferPolicy != null) {
                transfer = addTransferPolicy.transfer(this, s, id, transfer, data, extra, n);
            }
            else {
                transfer = Math.min(slot.count + transfer, NativeItem.getMaxStackForId(id, data)) - slot.count;
            }
            if (transfer > 0) {
                slot.count += transfer;
                slot.id = id;
                slot.data = data;
                slot.extra = extra;
                this.markSlotDirty(s);
                return transfer;
            }
        }
        return 0;
    }
    
    public Container asLegacyContainer() {
        return this.asLegacyContainer(true);
    }
    
    public Container asLegacyContainer(final boolean b) {
        final Container container = new Container(this);
        for (final Map.Entry<String, ItemContainerSlot> entry : this.slotMap.entrySet()) {
            final ItemContainerSlot itemContainerSlot = entry.getValue();
            if (!itemContainerSlot.isEmpty() && (b || itemContainerSlot.isSavingEnabled())) {
                container.slots.put((String)entry.getKey(), (Scriptable)container.slots, (Object)itemContainerSlot.asScriptable());
            }
        }
        return container;
    }
    
    @Override
    public Scriptable asScriptableField() {
        final Object[] array = new Object[9];
        for (int i = 0; i < 9; ++i) {
            array[i] = this.getFieldSlot(i);
        }
        return (Scriptable)ScriptableObjectHelper.createArray(array);
    }
    
    public void clearSlot(final String s) {
        this.getSlot(s).clear();
    }
    
    public void close() {
        if (!this.isServer) {
            throw new IllegalStateException();
        }
        this.networkEntity.getClients().clear();
    }
    
    public void closeFor(final ConnectedClient connectedClient) {
        if (!this.isServer) {
            throw new IllegalStateException();
        }
        this.networkEntity.getClients().remove(connectedClient);
    }
    
    public void dropAt(final NativeBlockSource nativeBlockSource, final float n, final float n2, final float n3) {
        synchronized (this.transactionLock) {
            final Iterator<ItemContainerSlot> iterator = this.slotMap.values().iterator();
            while (iterator.hasNext()) {
                iterator.next().dropAt(nativeBlockSource, n, n2, n3);
            }
            // monitorexit(this.transactionLock)
            this.sendChanges();
        }
    }
    
    public void dropSlot(final NativeBlockSource nativeBlockSource, final String s, final float n, final float n2, final float n3) {
        this.getSlot(s).dropAt(nativeBlockSource, n, n2, n3);
    }
    
    public TransferPolicy getAddTransferPolicy(final String s) {
        return Java8BackComp.getOrDefault(this.addTransferPolicyMap, s, this.globalAddTransferPolicy);
    }
    
    public Object getBinding(final String s) {
        return this.bindingsMap.get(s);
    }
    
    public Object getBinding(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("::");
        sb.append(s2);
        return this.getBinding(sb.toString());
    }
    
    public BindingValidator getBindingValidator(final String s) {
        return Java8BackComp.getOrDefault(this.bindingValidatorMap, s, this.globalBindingValidator);
    }
    
    public String getClientContainerTypeName() {
        return this.clientContainerTypeName;
    }
    
    @Override
    public AbstractSlot getFieldSlot(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.legacyWorkbenchFieldPrefix);
        sb.append(n);
        return this.getSlot(sb.toString());
    }
    
    public int getFromSlot(final String s, final int n, int n2, final int n3, final NativeItemInstanceExtra nativeItemInstanceExtra, final long n4) {
        if (n2 <= 0) {
            return 0;
        }
        final ItemContainerSlot slot = this.getSlot(s);
        if (slot != null && slot.id == n && slot.data == n3 && (slot.extra == null || slot.extra.equals(nativeItemInstanceExtra))) {
            final TransferPolicy getTransferPolicy = this.getGetTransferPolicy(s);
            final int n5 = n2 = Math.min(slot.count, n2);
            if (getTransferPolicy != null) {
                n2 = getTransferPolicy.transfer(this, s, n, n5, n3, nativeItemInstanceExtra, n4);
            }
            if (n2 > 0) {
                slot.count -= n2;
                slot.validate();
                this.markSlotDirty(s);
                return n2;
            }
        }
        return 0;
    }
    
    @Deprecated
    public ItemContainerSlot getFullSlot(final String s) {
        return this.getSlot(s);
    }
    
    public TransferPolicy getGetTransferPolicy(final String s) {
        return Java8BackComp.getOrDefault(this.getTransferPolicyMap, s, this.globalGetTransferPolicy);
    }
    
    public NetworkEntity getNetworkEntity() {
        return this.networkEntity;
    }
    
    public String getNetworkName() {
        if (this.networkEntity != null) {
            return this.networkEntity.getName();
        }
        return null;
    }
    
    public Object getParent() {
        return this.parent;
    }
    
    public ItemContainerSlot getSlot(final String s) {
        if (!this.slotMap.containsKey(s)) {
            final ItemContainerSlot itemContainerSlot = new ItemContainerSlot();
            itemContainerSlot.setContainer(this, s);
            this.slotMap.put(s, itemContainerSlot);
            this.slots.put(s, this.slots, (Object)itemContainerSlot);
            return itemContainerSlot;
        }
        return this.slotMap.get(s);
    }
    
    public String getText(final String s) {
        final Object binding = this.getBinding(s, "text");
        if (binding instanceof CharSequence) {
            return binding.toString();
        }
        return null;
    }
    
    public ItemContainerUiHandler getUiAdapter() {
        if (this.isServer) {
            throw new IllegalStateException();
        }
        return this.uiAdapter;
    }
    
    public Object getValue(final String s, final float n) {
        return this.getBinding(s, "value");
    }
    
    public IWindow getWindow() {
        return this.getUiAdapter().getWindow();
    }
    
    public ScriptableObject getWindowContent() {
        final IWindow window = this.getUiAdapter().getWindow();
        if (window != null) {
            return window.getContent();
        }
        return null;
    }
    
    void handleDirtyBindingsPacket(final ConnectedClient connectedClient, final JSONObject jsonObject) {
        final long playerUid = connectedClient.getPlayerUid();
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String s = keys.next();
            final Object opt = jsonObject.opt(s);
            if (opt instanceof Boolean || opt instanceof Number || opt instanceof String) {
                final BindingValidator bindingValidator = this.getBindingValidator(s);
                Object validate = opt;
                if (bindingValidator != null) {
                    validate = bindingValidator.validate(this, s, opt, playerUid);
                }
                if (validate == null) {
                    continue;
                }
                synchronized (this.bindingsMap) {
                    this.bindingsMap.put(s, validate);
                    this.dirtyBindingsMap.put(s, validate);
                }
            }
        }
    }
    
    void handleInventoryToSlotTransaction(final long n, final int n2, final String s, int addToSlot) {
        if (!this.isServer) {
            throw new IllegalStateException();
        }
        if (s == null || addToSlot <= 0 || n2 < 0 || n2 >= 36) {
            return;
        }
        final NativePlayer nativePlayer = new NativePlayer(n);
        if (!nativePlayer.isValid()) {
            return;
        }
        final Object playerInventoryLock = ItemContainer.playerInventoryLock;
        // monitorenter(playerInventoryLock)
        while (true) {
            try {
                final ItemStack inventorySlot = nativePlayer.getInventorySlot(n2);
            Label_0253:
                while (true) {
                    if (inventorySlot.id == 0 || inventorySlot.count <= 0) {
                        break Label_0241;
                    }
                    final int id = inventorySlot.id;
                    final int count = inventorySlot.count;
                    final int data = inventorySlot.data;
                    final NativeItemInstanceExtra extra = inventorySlot.extra;
                    int count2;
                    if ((count2 = addToSlot) > inventorySlot.count) {
                        try {
                            count2 = inventorySlot.count;
                        }
                        finally {
                            break Label_0253;
                        }
                    }
                    addToSlot = this.addToSlot(s, id, count2, data, extra, n);
                    addToSlot = count - addToSlot;
                    Label_0228: {
                        if (addToSlot <= 0) {
                            break Label_0228;
                        }
                        try {
                            nativePlayer.setInventorySlot(n2, id, addToSlot, data, extra);
                            // monitorexit(playerInventoryLock)
                            return;
                            nativePlayer.setInventorySlot(n2, 0, 0, 0, null);
                            continue;
                            // monitorexit(playerInventoryLock)
                            throw s;
                        }
                        finally {}
                    }
                    break;
                }
                continue;
            }
            finally {
                continue;
            }
            break;
        }
    }
    
    void handleSlotToInventoryTransaction(final long n, final String s, int fromSlot) {
        if (!this.isServer) {
            throw new IllegalStateException();
        }
        if (fromSlot > 0) {
            final ItemContainerSlot slot = this.getSlot(s);
            if (slot.id != 0 && slot.count > 0) {
                final NativePlayer nativePlayer = new NativePlayer(n);
                if (nativePlayer.isValid()) {
                    final int id = slot.id;
                    final int data = slot.data;
                    final NativeItemInstanceExtra extra = slot.extra;
                    fromSlot = this.getFromSlot(s, id, fromSlot, data, extra, n);
                    if (fromSlot > 0) {
                        synchronized (ItemContainer.playerInventoryLock) {
                            nativePlayer.addItemToInventory(id, fromSlot, data, extra, true);
                        }
                    }
                }
            }
        }
    }
    
    void handleSlotToSlotTransaction(final long n, final String s, final String s2, final int n2) {
        if (!this.isServer) {
            throw new IllegalStateException();
        }
    }
    
    public boolean isGlobalSlotSavingEnabled() {
        return this.globalSlotSavingEnabled;
    }
    
    public boolean isLegacyContainer() {
        return false;
    }
    
    public boolean isSlotSavingEnabled(final String s) {
        if (this.slotMap.containsKey(s)) {
            return this.getSlot(s).isSavingEnabled();
        }
        return this.globalSlotSavingEnabled;
    }
    
    public void markAllSlotsDirty() {
        synchronized (this.transactionLock) {
            if (this.isServer) {
                final Iterator<String> iterator = this.slotMap.keySet().iterator();
                while (iterator.hasNext()) {
                    this.markSlotDirty(iterator.next());
                }
            }
        }
    }
    
    public void markSlotDirty(final String s) {
        synchronized (this.transactionLock) {
            if (this.isServer) {
                final ItemContainerSlot itemContainerSlot = this.slotMap.get(s);
                if (itemContainerSlot != null) {
                    this.dirtySlotSet.add(s);
                    if (this.globalDirtySlotListener != null) {
                        this.globalDirtySlotListener.onMarkedDirty(this, s, itemContainerSlot);
                    }
                    final DirtySlotListener dirtySlotListener = this.dirtySlotListenerMap.get(s);
                    if (dirtySlotListener != null) {
                        dirtySlotListener.onMarkedDirty(this, s, itemContainerSlot);
                    }
                }
            }
        }
    }
    
    public void openFor(final ConnectedClient connectedClient, final String s) {
        if (!this.isServer) {
            throw new IllegalStateException();
        }
        this.networkEntity.getClients().add(connectedClient);
        this.networkEntity.send(connectedClient, "open", s);
        final Iterator<ServerOnOpenListener> iterator = this.onOpenListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onOpen(this, connectedClient, s);
        }
    }
    
    public void removeEntity() {
        if (!this.isServer) {
            throw new IllegalStateException();
        }
        NetworkThreadMarker.assertServerThread();
        this.networkEntity.remove();
    }
    
    public void resetSlotSavingEnabled(final String s) {
        this.getSlot(s).resetSavingEnabled();
    }
    
    public void runTransaction(final Transaction transaction) {
        synchronized (this.transactionLock) {
            transaction.run(this);
        }
    }
    
    public void sealAllSlots() {
        this.setGlobalAddTransferPolicy((TransferPolicy)-$$Lambda$ItemContainer$AqfweQ5FQCn39QSwHyWVd_XKNZ8.INSTANCE);
        this.setGlobalGetTransferPolicy((TransferPolicy)-$$Lambda$ItemContainer$D9t_d9BbYiTtryAP89IQA2TkkA8.INSTANCE);
    }
    
    public void sealSlot(final String s) {
        this.setSlotAddTransferPolicy(s, (TransferPolicy)-$$Lambda$ItemContainer$_SnK6Z2znu5wkNTRXFjZbHdYnrw.INSTANCE);
        this.setSlotAddTransferPolicy(s, (TransferPolicy)-$$Lambda$ItemContainer$SP4izZCJ8KQQ5lqFx24VnXtPRB8.INSTANCE);
    }
    
    public void sendChanges() {
        if (this.isServer) {
            if (!this.dirtySlotSet.isEmpty()) {
                while (true) {
                    final JSONObject jsonObject = new JSONObject();
                    final Object transactionLock = this.transactionLock;
                    // monitorenter(transactionLock)
                    while (true) {
                        Label_0193: {
                            try {
                                Label_0117: {
                                    try {
                                        final Iterator<String> iterator = this.dirtySlotSet.iterator();
                                        if (!iterator.hasNext()) {
                                            this.dirtySlotSet.clear();
                                            break Label_0117;
                                        }
                                        final String s = iterator.next();
                                        final ItemContainerSlot itemContainerSlot = this.slotMap.get(s);
                                        if (itemContainerSlot != null) {
                                            jsonObject.put(s, (Object)itemContainerSlot.asJson());
                                            break Label_0193;
                                        }
                                        break Label_0193;
                                    }
                                    finally {
                                        // monitorexit(transactionLock)
                                        // monitorexit(transactionLock)
                                        this.networkEntity.send("slots", jsonObject);
                                    }
                                }
                            }
                            catch (JSONException ex) {}
                            break;
                        }
                        continue;
                    }
                }
            }
            if (!this.dirtyBindingsMap.isEmpty()) {
                synchronized (this.bindingsMap) {
                    this.networkEntity.send("bindings", new JSONObject((Map)this.dirtyBindingsMap));
                    this.dirtyBindingsMap.clear();
                }
            }
        }
    }
    
    public void sendClosed() {
        if (this.isServer) {
            throw new IllegalStateException();
        }
        final List<ClientOnCloseListener> list = ItemContainer.clientOnCloseListenerMap.get(this.clientContainerTypeName);
        if (list != null) {
            final Iterator<ClientOnCloseListener> iterator = list.iterator();
            while (iterator.hasNext()) {
                iterator.next().onClose(this);
            }
        }
        this.networkEntity.send("close", "");
    }
    
    void sendDirtyClientBinding(final String s, final Object o) {
        if (!this.isServer) {
            throw new IllegalStateException();
        }
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(s, o);
            this.networkEntity.send("bindings", jsonObject);
        }
        catch (JSONException ex) {}
    }
    
    public void sendEvent(final ConnectedClient connectedClient, final String s, final Object o) {
        if (!this.isServer) {
            this.networkEntity.getClientExecutor().add(new -$$Lambda$ItemContainer$q9BtiSW0fDzKrCiJLqKQKSHDKzo(this, connectedClient, s, o));
            return;
        }
        final NetworkEntity networkEntity = this.networkEntity;
        final StringBuilder sb = new StringBuilder();
        sb.append("event#");
        sb.append(s);
        networkEntity.send(connectedClient, sb.toString(), o);
    }
    
    public void sendEvent(final String s, final Object o) {
        if (!this.isServer) {
            this.networkEntity.getClientExecutor().add(new -$$Lambda$ItemContainer$uEMIsnL6wuEoNCgS2puci62aMH4(this, s, o));
            return;
        }
        final NetworkEntity networkEntity = this.networkEntity;
        final StringBuilder sb = new StringBuilder();
        sb.append("event#");
        sb.append(s);
        networkEntity.send(sb.toString(), o);
    }
    
    public void sendInventoryToSlotTransaction(final int n, final String s, final int n2) {
        if (this.isServer) {
            throw new IllegalStateException();
        }
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("inv", n);
            jsonObject.put("name", (Object)s);
            jsonObject.put("amount", n2);
            this.networkEntity.send("inv_to_slot", jsonObject);
        }
        catch (JSONException ex) {}
    }
    
    public void sendResponseEvent(final String s, final Object o) {
        if (!this.isServer) {
            this.networkEntity.getClientExecutor().add(new -$$Lambda$ItemContainer$u1bXvcoDnB6PkvSBUvDFMn1XcEM(this, s, o));
            return;
        }
        final NetworkEntity networkEntity = this.networkEntity;
        final StringBuilder sb = new StringBuilder();
        sb.append("event#");
        sb.append(s);
        networkEntity.respond(sb.toString(), o);
    }
    
    public void sendSlotToInventoryTransaction(final String s, final int n) {
        if (this.isServer) {
            throw new IllegalStateException();
        }
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("slot", (Object)s);
            jsonObject.put("amount", n);
            this.networkEntity.send("slot_to_inv", jsonObject);
        }
        catch (JSONException ex) {}
    }
    
    public void sendSlotToSlotTransaction(final String s, final String s2, final int n) {
        if (this.isServer) {
            throw new IllegalStateException();
        }
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("slot1", (Object)s);
            jsonObject.put("slot2", (Object)s2);
            jsonObject.put("amount", n);
            this.networkEntity.send("slot_to_slot", jsonObject);
        }
        catch (JSONException ex) {}
    }
    
    public void setBinding(final String s, final Object o) {
        Object string;
        if (o instanceof CharSequence) {
            string = o.toString();
        }
        else {
            string = o;
            if (!(o instanceof Number)) {
                string = o;
                if (!(o instanceof Boolean)) {
                    throw new IllegalArgumentException("binding value must be number, string or boolean");
                }
            }
        }
        synchronized (this.bindingsMap) {
            this.bindingsMap.put(s, string);
            this.dirtyBindingsMap.put(s, string);
            // monitorexit(this.bindingsMap)
            if (!this.isServer) {
                this.getUiAdapter().setBindingByComposedName(s, string);
            }
        }
    }
    
    public void setBinding(final String s, final String s2, final Object o) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("::");
        sb.append(s2);
        this.setBinding(sb.toString(), o);
    }
    
    public void setBindingValidator(final String s, final BindingValidator bindingValidator) {
        this.bindingValidatorMap.put(s, bindingValidator);
    }
    
    public void setClientBinding(final String s, final Object o) {
        this.getUiAdapter().setBindingByComposedName(s, o);
        synchronized (this.bindingsMap) {
            this.bindingsMap.put(s, o);
        }
    }
    
    public void setClientBinding(final String s, final String s2, final Object o) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("::");
        sb.append(s2);
        this.setClientBinding(sb.toString(), o);
    }
    
    public void setClientContainerTypeName(final String clientContainerTypeName) {
        this.clientContainerTypeName = clientContainerTypeName;
    }
    
    public void setClientScale(final String s, final float n) {
        this.setClientBinding(s, "value", n);
    }
    
    public void setClientText(final String s, final String s2) {
        this.setClientBinding(s, "text", s2);
    }
    
    public ItemContainer setDirtySlotListener(final String s, final DirtySlotListener dirtySlotListener) {
        this.dirtySlotListenerMap.put(s, dirtySlotListener);
        return this;
    }
    
    public ItemContainer setGlobalAddTransferPolicy(final TransferPolicy globalAddTransferPolicy) {
        this.globalAddTransferPolicy = globalAddTransferPolicy;
        return this;
    }
    
    public void setGlobalBindingValidator(final BindingValidator globalBindingValidator) {
        this.globalBindingValidator = globalBindingValidator;
    }
    
    public ItemContainer setGlobalDirtySlotListener(final DirtySlotListener globalDirtySlotListener) {
        this.globalDirtySlotListener = globalDirtySlotListener;
        return this;
    }
    
    public ItemContainer setGlobalGetTransferPolicy(final TransferPolicy globalGetTransferPolicy) {
        this.globalGetTransferPolicy = globalGetTransferPolicy;
        return this;
    }
    
    public void setGlobalSlotSavingEnabled(final boolean globalSlotSavingEnabled) {
        this.globalSlotSavingEnabled = globalSlotSavingEnabled;
    }
    
    public void setParent(final Object parent) {
        this.parent = parent;
    }
    
    public void setScale(final String s, final float n) {
        this.setBinding(s, "value", n);
    }
    
    public void setSlot(final String s, final int n, final int n2, final int n3) {
        this.setSlot(s, n, n2, n3, null);
    }
    
    public void setSlot(final String s, final int n, final int n2, final int n3, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        final ItemContainerSlot itemContainerSlot = this.slotMap.get(s);
        if (itemContainerSlot != null) {
            itemContainerSlot.setSlot(n, n2, n3, nativeItemInstanceExtra);
            return;
        }
        this.setSlot(s, new ItemContainerSlot(n, n2, n3, nativeItemInstanceExtra));
    }
    
    public void setSlot(final String s, final ItemContainerSlot itemContainerSlot) {
        itemContainerSlot.setContainer(this, s);
        this.slotMap.put(s, itemContainerSlot);
        this.slots.put(s, this.slots, (Object)itemContainerSlot);
        this.markSlotDirty(s);
    }
    
    public ItemContainer setSlotAddTransferPolicy(final String s, final TransferPolicy transferPolicy) {
        this.addTransferPolicyMap.put(s, transferPolicy);
        return this;
    }
    
    public ItemContainer setSlotGetTransferPolicy(final String s, final TransferPolicy transferPolicy) {
        this.getTransferPolicyMap.put(s, transferPolicy);
        return this;
    }
    
    public void setSlotSavingEnabled(final String s, final boolean savingEnabled) {
        this.getSlot(s).setSavingEnabled(savingEnabled);
    }
    
    public void setText(final String s, final String s2) {
        this.setBinding(s, "text", s2);
    }
    
    public void setWorkbenchFieldPrefix(final String legacyWorkbenchFieldPrefix) {
        this.legacyWorkbenchFieldPrefix = legacyWorkbenchFieldPrefix;
    }
    
    public void validateAll() {
        synchronized (this.transactionLock) {
            final Iterator<ItemContainerSlot> iterator = this.slotMap.values().iterator();
            while (iterator.hasNext()) {
                iterator.next().validate();
            }
            // monitorexit(this.transactionLock)
            this.sendChanges();
        }
    }
    
    public void validateSlot(final String s) {
        this.getSlot(s).validate();
    }
    
    public interface BindingValidator
    {
        Object validate(final ItemContainer p0, final String p1, final Object p2, final long p3);
    }
    
    public interface ClientEventListener
    {
        void receive(final ItemContainer p0, final IWindow p1, final ScriptableObject p2, final Object p3);
    }
    
    public interface ClientOnCloseListener
    {
        void onClose(final ItemContainer p0);
    }
    
    public interface ClientOnOpenListener
    {
        void onOpen(final ItemContainer p0, final String p1);
    }
    
    public interface DirtySlotListener
    {
        void onMarkedDirty(final ItemContainer p0, final String p1, final ItemContainerSlot p2);
    }
    
    public interface ServerEventListener
    {
        void receive(final ItemContainer p0, final ConnectedClient p1, final Object p2);
    }
    
    public interface ServerOnCloseListener
    {
        void onClose(final ItemContainer p0, final ConnectedClient p1);
    }
    
    public interface ServerOnOpenListener
    {
        void onOpen(final ItemContainer p0, final ConnectedClient p1, final String p2);
    }
    
    public interface Transaction
    {
        void run(final ItemContainer p0);
    }
    
    public interface TransferPolicy
    {
        int transfer(final ItemContainer p0, final String p1, final int p2, final int p3, final int p4, final NativeItemInstanceExtra p5, final long p6);
    }
    
    public interface UiScreenFactory
    {
        IWindow getByName(final ItemContainer p0, final String p1);
    }
}
