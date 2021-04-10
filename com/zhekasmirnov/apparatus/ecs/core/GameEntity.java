package com.zhekasmirnov.apparatus.ecs.core;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.ecs.core.network.*;
import com.zhekasmirnov.apparatus.multiplayer.util.entity.*;
import com.zhekasmirnov.apparatus.util.*;
import java.util.function.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;
import org.json.*;
import com.zhekasmirnov.apparatus.ecs.util.*;
import java.util.*;

@SynthesizedClassMap({ -$$Lambda$GameEntity$K6MsouFNr635IaO_v8n9FKQXT8o.class, -$$Lambda$GameEntity$vHY8dnlbpvgClrzrZt6eVFA1MM8.class, -$$Lambda$GameEntity$OB9oXxcoDY5KEClLp3bL7XlcrcA.class, -$$Lambda$GameEntity$-yOfjR3n2tNAAEUGLQmCAtbs-Q8.class, -$$Lambda$GameEntity$j6FPyoXzDDYHRXMerAQ8VaRA12U.class, -$$Lambda$GameEntity$r4v_VQx5YvXFHIfffJlWnbbR9No.class, -$$Lambda$GameEntity$hH4Obb8LzMpt3lPUFKdBrD43RiM.class, -$$Lambda$GameEntity$ztplruTiYWZ9Eyt5E9XfhRK-4RM.class, -$$Lambda$GameEntity$s19XThDLsuk51itBkha7iGO-4hU.class, -$$Lambda$GameEntity$5la9ocvpt_IJETewYEUq8rhUhTY.class, -$$Lambda$GameEntity$tOOpbLoie1hZCT7L0kejiVs6Zio.class })
public class GameEntity
{
    private static final String EVENT_NAME_ADD_COMPONENT = "ca";
    private static final String EVENT_NAME_COMPONENT_EVENT = "ce";
    private static final String EVENT_NAME_REMOVE_COMPONENT = "cr";
    private static final String EVENT_NAME_REMOVE_ENTITY = "remove_entity";
    private static final AbstractPool<NetworkEntity> networkEntityPool;
    private static final NetworkEntityType networkEntityType;
    private final Map<String, List<EntityComponent>> componentByClassCache;
    private final Map<String, EntityComponent> componentByLocalId;
    private final Map<String, EntityComponent> componentByTypeId;
    private final List<EntityComponent> components;
    private EntityEngine engine;
    private boolean isActive;
    private final boolean isServer;
    private int networkComponentCounter;
    private NetworkEntity networkEntity;
    private final Object networkEntityLock;
    private int nextLocalId;
    private final OptimizedPacketQueue packetQueue;
    
    static {
        networkEntityType = new NetworkEntityType("sys.game_entity").setClientAddPacketFactory((NetworkEntityType.ClientAddPacketFactory)-$$Lambda$GameEntity$tOOpbLoie1hZCT7L0kejiVs6Zio.INSTANCE).setClientEntityAddedListener((NetworkEntityType.OnClientEntityAddedListener)-$$Lambda$GameEntity$j6FPyoXzDDYHRXMerAQ8VaRA12U.INSTANCE).setClientEntityRemovedListener((NetworkEntityType.OnClientEntityRemovedListener)-$$Lambda$GameEntity$hH4Obb8LzMpt3lPUFKdBrD43RiM.INSTANCE).addClientPacketListener("ca", (NetworkEntityType.OnClientPacketListener)-$$Lambda$GameEntity$r4v_VQx5YvXFHIfffJlWnbbR9No.INSTANCE).addClientPacketListener("cr", (NetworkEntityType.OnClientPacketListener)-$$Lambda$GameEntity$vHY8dnlbpvgClrzrZt6eVFA1MM8.INSTANCE).addClientPacketListener("ce", (NetworkEntityType.OnClientPacketListener)-$$Lambda$GameEntity$-yOfjR3n2tNAAEUGLQmCAtbs-Q8.INSTANCE).addServerPacketListener("ce", (NetworkEntityType.OnServerPacketListener)-$$Lambda$GameEntity$OB9oXxcoDY5KEClLp3bL7XlcrcA.INSTANCE);
        networkEntityPool = new AbstractPool<NetworkEntity>(-$$Lambda$GameEntity$K6MsouFNr635IaO_v8n9FKQXT8o.INSTANCE, -$$Lambda$GameEntity$ztplruTiYWZ9Eyt5E9XfhRK-4RM.INSTANCE, null);
    }
    
    GameEntity() {
        this.engine = null;
        this.isActive = false;
        this.componentByClassCache = new HashMap<String, List<EntityComponent>>();
        this.componentByTypeId = new HashMap<String, EntityComponent>();
        this.componentByLocalId = new HashMap<String, EntityComponent>();
        this.components = new ArrayList<EntityComponent>();
        this.nextLocalId = 1;
        this.networkComponentCounter = 0;
        this.networkEntity = null;
        this.networkEntityLock = new Object();
        this.packetQueue = new OptimizedPacketQueue(new -$$Lambda$GameEntity$5la9ocvpt_IJETewYEUq8rhUhTY(this));
        this.networkEntity = null;
        this.isServer = false;
    }
    
    GameEntity(final NetworkEntity networkEntity) {
        this.engine = null;
        this.isActive = false;
        this.componentByClassCache = new HashMap<String, List<EntityComponent>>();
        this.componentByTypeId = new HashMap<String, EntityComponent>();
        this.componentByLocalId = new HashMap<String, EntityComponent>();
        this.components = new ArrayList<EntityComponent>();
        this.nextLocalId = 1;
        this.networkComponentCounter = 0;
        this.networkEntity = null;
        this.networkEntityLock = new Object();
        this.packetQueue = new OptimizedPacketQueue(new -$$Lambda$GameEntity$5la9ocvpt_IJETewYEUq8rhUhTY(this));
        this.networkEntity = networkEntity;
        this.isServer = networkEntity.isServer();
    }
    
    private void addComponentByPacket(final JSONObject jsonObject) {
        final String optString = jsonObject.optString("t");
        final String optString2 = jsonObject.optString("i");
        final String optString3 = jsonObject.optString("d");
        final EntityComponent component = EntityComponentRegistry.getSingleton().newComponent(optString);
        if (component != null && component.isNetworkComponent) {
            component.initClientNetworkComponent(SyncedNetworkData.getClientSyncedData(optString3));
            this.addComponentInternal(component, optString2);
        }
    }
    
    private EntityComponent addComponentInternal(final EntityComponent entityComponent, final String s) {
        if (entityComponent.isActive()) {
            throw new IllegalStateException("adding already active component");
        }
        final EntityComponentRegistry.ComponentType componentType = entityComponent.getComponentType();
        if (componentType == null) {
            throw new NullPointerException();
        }
        final String name = componentType.getName();
        final EntityComponent entityComponent2 = this.componentByTypeId.get(name);
        if (entityComponent2 != null) {
            return entityComponent2;
        }
        Object o = this.components;
        synchronized (o) {
            this.components.add(entityComponent);
            this.componentByTypeId.put(name, entityComponent);
            this.componentByLocalId.put(entityComponent.getLocalId(), entityComponent);
            final Iterator<String> iterator = componentType.getAllParentTypeNames().iterator();
            while (iterator.hasNext()) {
                Java8BackComp.computeIfAbsent(this.componentByClassCache, iterator.next(), -$$Lambda$GameEntity$s19XThDLsuk51itBkha7iGO-4hU.INSTANCE).add(entityComponent);
            }
            entityComponent.initComponent(this, s);
            // monitorexit(o)
            if (entityComponent.isNetworkComponent) {
                synchronized (this.networkEntityLock) {
                    if (this.networkEntity != null) {
                        entityComponent.initServerNetworkComponent(this.networkEntity.getClients());
                    }
                }
            }
            entityComponent.onAdded();
            entityComponent.setActive(true);
            synchronized (this.components) {
                o = this.components.iterator();
                while (((Iterator)o).hasNext()) {
                    ((Iterator<EntityComponent>)o).next().onComponentAddedToEntity(entityComponent);
                }
                return entityComponent;
            }
        }
    }
    
    private void addPacketToQueue(final OptimizedPacketQueue.Packet packet) {
        synchronized (this.packetQueue) {
            this.packetQueue.add(packet);
            if (this.packetQueue.size() == 0) {
                this.engine.addEntityToFlushPacketQueue(this);
            }
        }
    }
    
    private void assureNetworkEntity() {
        while (true) {
            while (true) {
                Label_0113: {
                    synchronized (this.networkEntityLock) {
                        if (this.networkEntity == null) {
                            (this.networkEntity = GameEntity.networkEntityPool.get()).setTarget(this);
                            synchronized (this.components) {
                                final Iterator<EntityComponent> iterator = this.components.iterator();
                                if (iterator.hasNext()) {
                                    final EntityComponent entityComponent = iterator.next();
                                    if (entityComponent.isNetworkComponent) {
                                        entityComponent.initServerNetworkComponent(this.networkEntity.getClients());
                                        break Label_0113;
                                    }
                                    break Label_0113;
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
    
    private EntityComponent getComponentByLocalId(final String s) {
        return this.componentByLocalId.get(s);
    }
    
    private void handleAddComponentsPacket(final JSONObject jsonObject) {
        final JSONArray optJSONArray = jsonObject.optJSONArray("c");
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); ++i) {
                final JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                if (optJSONObject != null) {
                    this.addComponentByPacket(optJSONObject);
                }
            }
        }
    }
    
    private void handleComponentEventPacket(final ConnectedClient connectedClient, String substring, final Object o) {
        if (substring == null) {
            return;
        }
        final int index = substring.indexOf(35);
        if (index != -1) {
            final String substring2 = substring.substring(0, index);
            final String substring3 = substring.substring(index + 1);
            String substring4 = null;
            final int index2 = substring3.indexOf(35);
            substring = substring3;
            if (index2 != -1) {
                substring4 = substring3.substring(index2 + 1);
                substring = substring3.substring(0, index2);
            }
            final EntityComponent componentByLocalId = this.getComponentByLocalId(substring2);
            if (componentByLocalId != null && componentByLocalId.isNetworkComponent) {
                if (connectedClient != null) {
                    componentByLocalId.onServerEventReceived(connectedClient, substring, o, substring4);
                    return;
                }
                componentByLocalId.onClientEventReceived(substring, o, substring4);
            }
        }
    }
    
    private void handleRemoveComponentsPacket(final String s) {
        final String[] split = s.split(";");
        for (int length = split.length, i = 0; i < length; ++i) {
            final EntityComponent componentByLocalId = this.getComponentByLocalId(split[i]);
            if (componentByLocalId != null) {
                this.removeComponentInternal(componentByLocalId);
            }
        }
    }
    
    private static JSONObject makeAddComponentsPacket(final List<EntityComponent> list) {
        while (true) {
            final JSONObject jsonObject = new JSONObject();
            final JSONArray jsonArray = new JSONArray();
            while (true) {
                Label_0135: {
                    try {
                        jsonObject.put("c", (Object)jsonArray);
                        for (final EntityComponent entityComponent : list) {
                            if (entityComponent.isNetworkComponent) {
                                final JSONObject jsonObject2 = new JSONObject();
                                final SyncedNetworkData syncedData = entityComponent.getSyncedData();
                                jsonObject2.put("t", (Object)entityComponent.getTypeIdentifier());
                                jsonObject2.put("i", (Object)entityComponent.getLocalId());
                                if (syncedData == null) {
                                    break Label_0135;
                                }
                                final String name = syncedData.getName();
                                jsonObject2.put("d", (Object)name);
                                jsonArray.put((Object)jsonObject2);
                            }
                        }
                        return jsonObject;
                    }
                    catch (JSONException ex) {
                        return jsonObject;
                    }
                }
                final String name = null;
                continue;
            }
        }
    }
    
    private void removeComponentInternal(final EntityComponent entityComponent) {
        Object o = entityComponent.getComponentType();
        if (o == null) {
            throw new NullPointerException();
        }
        while (true) {
            while (true) {
                Label_0214: {
                    synchronized (this.components) {
                        this.components.remove(entityComponent);
                        this.componentByTypeId.remove(entityComponent.getTypeIdentifier());
                        this.componentByLocalId.remove(entityComponent.getLocalId());
                        o = ((EntityComponentRegistry.ComponentType)o).getAllParentTypeNames().iterator();
                        if (((Iterator)o).hasNext()) {
                            final String s = ((Iterator<String>)o).next();
                            final List<EntityComponent> list = this.componentByClassCache.get(s);
                            if (list == null) {
                                break Label_0214;
                            }
                            list.remove(entityComponent);
                            if (list.isEmpty()) {
                                this.componentByClassCache.remove(s);
                                break Label_0214;
                            }
                            break Label_0214;
                        }
                        else {
                            // monitorexit(this.components)
                            entityComponent.setActive(false);
                            entityComponent.onRemoved();
                            final List<EntityComponent> components = this.components;
                            synchronized (this.components) {
                                o = this.components.iterator();
                                while (((Iterator)o).hasNext()) {
                                    ((EntityComponent)((Iterator<String>)o).next()).onComponentRemovedFromEntity(entityComponent);
                                }
                                return;
                            }
                        }
                    }
                }
                continue;
            }
        }
    }
    
    private void removeNetworkEntity() {
        synchronized (this.networkEntityLock) {
            if (this.networkEntity != null) {
                GameEntity.networkEntityPool.store(this.networkEntity);
                this.networkEntity = null;
            }
        }
    }
    
    private void sendAddComponent(final EntityComponent entityComponent) {
        ++this.networkComponentCounter;
        this.addPacketToQueue(new AddComponentsPacket(null, entityComponent));
    }
    
    private void sendRemoveComponent(final EntityComponent entityComponent) {
        --this.networkComponentCounter;
        if (this.networkEntity == null) {
            return;
        }
        this.addPacketToQueue(new RemoveComponentsPacket(null, entityComponent));
        if (this.networkComponentCounter <= 0) {
            this.networkComponentCounter = 0;
            this.addPacketToQueue(new RemoveEntityPacket());
            this.removeNetworkEntity();
        }
    }
    
    public EntityComponent addComponent(final EntityComponent entityComponent) {
        if (!this.isServer) {
            throw new IllegalStateException("addComponent can be called only on server side entities");
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this.nextLocalId++);
        return this.addComponentInternal(entityComponent, sb.toString());
    }
    
    public EntityComponent addComponent(final Class<? extends EntityComponent> clazz) {
        return this.addComponent(clazz.getCanonicalName());
    }
    
    public EntityComponent addComponent(final String s) {
        final EntityComponent component = EntityComponentRegistry.getSingleton().newComponent(s);
        if (component == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid component type: ");
            sb.append(s);
            throw new IllegalArgumentException(sb.toString());
        }
        return component;
    }
    
    public List<EntityComponent> getAllComponents() {
        return this.components;
    }
    
    public <T> T getComponent(final Class<T> clazz) {
        final List<EntityComponent> list = this.componentByClassCache.get(clazz.getCanonicalName());
        if (list != null && list.size() > 0) {
            return (T)list.get(0);
        }
        return null;
    }
    
    public <T> T getComponent(final Class<T> clazz, final String s) {
        return (T)this.componentByTypeId.get(s);
    }
    
    public Set<String> getComponentAndSupertypesNames() {
        return this.componentByClassCache.keySet();
    }
    
    public <T> List<T> getComponents(final Class<T> clazz) {
        Object o = this.componentByClassCache.get(clazz.getCanonicalName());
        if (o == null) {
            o = new ArrayList<Object>();
        }
        return new ImmutableCastingListWrap<T>((List<?>)o);
    }
    
    public Object getComponentsLock() {
        return this.components;
    }
    
    public EntityEngine getEngine() {
        return this.engine;
    }
    
    public boolean hasComponent(final Class<?> clazz) {
        return this.componentByClassCache.containsKey(clazz.getCanonicalName());
    }
    
    public boolean hasComponents(final List<Class<?>> list) {
        final Iterator<Class<?>> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (!this.componentByClassCache.containsKey(iterator.next().getCanonicalName())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean hasComponents(final Class<?>... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (!this.componentByClassCache.containsKey(array[i].getCanonicalName())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isActive() {
        return this.isActive;
    }
    
    public boolean isServer() {
        return this.isServer;
    }
    
    void onComponentActivated(final EntityComponent entityComponent) {
        if (this.isServer && entityComponent != null && entityComponent.isNetworkComponent) {
            this.sendAddComponent(entityComponent);
        }
    }
    
    void onComponentDeactivated(final EntityComponent entityComponent) {
        if (this.isServer && entityComponent != null && entityComponent.isNetworkComponent) {
            this.sendRemoveComponent(entityComponent);
        }
    }
    
    void onPacketQueueFlushed() {
        synchronized (this.packetQueue) {
            this.packetQueue.flush();
        }
    }
    
    public void removeComponent(final EntityComponent entityComponent) {
        if (!this.isServer) {
            throw new IllegalStateException("removeComponent can be called only on server side entities");
        }
        this.removeComponentInternal(entityComponent);
    }
    
    void sendComponentEvent(final EntityComponent entityComponent, final ConnectedClient connectedClient, final String s, final Object o) {
        this.addPacketToQueue(new ComponentEventPacket(connectedClient, entityComponent, s, o));
    }
    
    void sendComponentEvent(final EntityComponent entityComponent, final String s, final Object o) {
        this.addPacketToQueue(new ComponentEventPacket(null, entityComponent, s, o));
    }
    
    public void setActive(final boolean b) {
        synchronized (this) {
            if (!this.isServer) {
                throw new IllegalStateException("setActive must be called only on server-side entity instance");
            }
            if (b && !this.isActive) {
                this.isActive = true;
                synchronized (this.components) {
                    final Iterator<EntityComponent> iterator = this.components.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().onEntityActivated();
                    }
                    // monitorexit(this.components)
                    this.engine.onEntityActivated(this);
                    return;
                }
            }
            if (!b && this.isActive) {
                this.isActive = false;
                synchronized (this.components) {
                    final Iterator<EntityComponent> iterator2 = this.components.iterator();
                    while (iterator2.hasNext()) {
                        iterator2.next().onEntityDeactivated();
                    }
                    // monitorexit(this.components)
                    this.engine.onEntityDeactivated(this);
                }
            }
        }
    }
    
    void setEngine(final EntityEngine engine) {
        if (this.engine != null) {
            new IllegalStateException("entity already has engine");
        }
        this.engine = engine;
    }
    
    private static class AddComponentsPacket extends Packet
    {
        private final List<EntityComponent> components;
        
        protected AddComponentsPacket(final ConnectedClient connectedClient, final EntityComponent entityComponent) {
            super(connectedClient, "ca");
            (this.components = new ArrayList<EntityComponent>()).add(entityComponent);
        }
        
        @Override
        public Object getData() {
            return makeAddComponentsPacket(this.components);
        }
        
        @Override
        public Packet merge(final Packet packet) {
            if (packet instanceof AddComponentsPacket) {
                ((AddComponentsPacket)packet).components.addAll(this.components);
                return packet;
            }
            return null;
        }
    }
    
    private static class ComponentEventPacket extends Packet
    {
        private final Object data;
        
        protected ComponentEventPacket(final ConnectedClient connectedClient, final EntityComponent entityComponent, final String s, final Object data) {
            final StringBuilder sb = new StringBuilder();
            sb.append("ce#");
            sb.append(entityComponent.getLocalId());
            sb.append("#");
            sb.append(s);
            super(connectedClient, sb.toString());
            this.data = data;
        }
        
        @Override
        public Object getData() {
            return this.data;
        }
    }
    
    private static class RemoveComponentsPacket extends Packet
    {
        private final List<String> componentIds;
        
        protected RemoveComponentsPacket(final ConnectedClient connectedClient, final EntityComponent entityComponent) {
            super(connectedClient, "cr");
            (this.componentIds = new ArrayList<String>()).add(entityComponent.getLocalId());
        }
        
        @Override
        public Object getData() {
            return String.join(";", this.componentIds);
        }
        
        @Override
        public Packet merge(final Packet packet) {
            if (packet instanceof RemoveComponentsPacket) {
                ((RemoveComponentsPacket)packet).componentIds.addAll(this.componentIds);
                return packet;
            }
            return null;
        }
    }
    
    private static class RemoveEntityPacket extends Packet
    {
        protected RemoveEntityPacket() {
            super(null, "remove_entity");
        }
        
        @Override
        public boolean overrides(final Packet packet) {
            return true;
        }
    }
}
