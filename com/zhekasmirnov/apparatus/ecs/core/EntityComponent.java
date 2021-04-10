package com.zhekasmirnov.apparatus.ecs.core;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.multiplayer.util.list.*;
import com.zhekasmirnov.apparatus.ecs.util.*;
import com.zhekasmirnov.apparatus.multiplayer.util.entity.*;
import java.util.function.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;

@SynthesizedClassMap({ -$$Lambda$EntityComponent$HJDiBJok2gbaHNusK_uD2_sR-T4.class, -$$Lambda$vlkL56bFiIsybda292rsAkpkiJE.class })
public class EntityComponent
{
    private static final ConnectedClientList emptyConnectedClientList;
    private static final AbstractPool<SyncedNetworkData> syncedDataPool;
    private EntityComponentRegistry.ComponentType componentType;
    private GameEntity entity;
    private boolean isActive;
    final boolean isNetworkComponent;
    private boolean isServer;
    private String localId;
    private SyncedNetworkData syncedData;
    
    static {
        emptyConnectedClientList = new ConnectedClientList();
        syncedDataPool = new AbstractPool<SyncedNetworkData>(-$$Lambda$vlkL56bFiIsybda292rsAkpkiJE.INSTANCE, -$$Lambda$EntityComponent$HJDiBJok2gbaHNusK_uD2_sR-T4.INSTANCE, null);
    }
    
    public EntityComponent() {
        this.isActive = false;
        this.isServer = true;
        this.syncedData = null;
        this.componentType = null;
        this.isNetworkComponent = this.isNetworkComponent();
        this.onCreated();
    }
    
    public EntityComponentRegistry.ComponentType getComponentType() {
        if (this.componentType == null) {
            this.componentType = EntityComponentRegistry.getSingleton().getComponentType(this.getTypeIdentifier());
        }
        return this.componentType;
    }
    
    public GameEntity getEntity() {
        return this.entity;
    }
    
    public String getLocalId() {
        return this.localId;
    }
    
    public SyncedNetworkData getSyncedData() {
        if (!this.isNetworkComponent) {
            throw new IllegalStateException("getSyncedData called for non-network component");
        }
        return this.syncedData;
    }
    
    public String getTypeIdentifier() {
        return this.getClass().getCanonicalName();
    }
    
    void initClientNetworkComponent(final SyncedNetworkData syncedData) {
        this.isServer = false;
        this.syncedData = syncedData;
    }
    
    void initComponent(final GameEntity entity, final String localId) {
        this.entity = entity;
        this.localId = localId;
    }
    
    void initServerNetworkComponent(final ConnectedClientList clients) {
        this.isServer = true;
        if (this.syncedData != null) {
            this.syncedData.setClients(clients);
        }
    }
    
    public boolean isActive() {
        return this.isActive;
    }
    
    public boolean isNetworkComponent() {
        return false;
    }
    
    public boolean isUsingSharedData() {
        return false;
    }
    
    protected void onActivated() {
        this.entity.onComponentActivated(this);
    }
    
    protected void onAdded() {
    }
    
    void onClientEventReceived(final String s, final Object o, final String s2) {
    }
    
    void onComponentAdded() {
        this.onAdded();
    }
    
    public void onComponentAddedToEntity(final EntityComponent entityComponent) {
    }
    
    void onComponentRemoved() {
        this.onRemoved();
        if (this.syncedData != null) {
            EntityComponent.syncedDataPool.store(this.syncedData);
            this.syncedData = null;
        }
    }
    
    public void onComponentRemovedFromEntity(final EntityComponent entityComponent) {
    }
    
    protected void onCreated() {
    }
    
    protected void onDeactivated() {
        this.entity.onComponentDeactivated(this);
    }
    
    void onEntityActivated() {
        if (this.isActive) {
            this.onActivated();
        }
    }
    
    void onEntityDeactivated() {
        if (this.isActive) {
            this.onDeactivated();
        }
    }
    
    protected void onRemoved() {
    }
    
    void onServerEventReceived(final ConnectedClient connectedClient, final String s, final Object o, final String s2) {
    }
    
    public void send(final ConnectedClient connectedClient, final String s, final Object o) {
        if (!this.isNetworkComponent) {
            throw new IllegalStateException("send method can be used only for network components");
        }
        this.getEntity().sendComponentEvent(this, connectedClient, s, o);
    }
    
    public void send(final String s, final Object o) {
        if (!this.isNetworkComponent) {
            throw new IllegalStateException("send method can be used only for network components");
        }
        this.getEntity().sendComponentEvent(this, s, o);
    }
    
    public void setActive(final boolean b) {
        if (!this.isServer) {
            throw new IllegalStateException("setActive must be called only on server-side component");
        }
        if (b && !this.isActive) {
            this.isActive = true;
            if (this.entity.isActive()) {
                this.onActivated();
            }
        }
        else if (!b && this.isActive) {
            this.isActive = false;
            if (this.entity.isActive()) {
                this.onDeactivated();
            }
        }
    }
}
