package com.zhekasmirnov.apparatus.ecs.core;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.multiplayer.util.list.*;
import com.zhekasmirnov.apparatus.ecs.util.*;
import com.zhekasmirnov.apparatus.multiplayer.util.entity.*;
import java.util.function.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;

@SynthesizedClassMap({ -$$Lambda$NetworkEntityComponent$FjPymKn7a0pbo2kvDcU_792osMQ.class, -$$Lambda$vlkL56bFiIsybda292rsAkpkiJE.class })
public class NetworkEntityComponent extends EntityComponent
{
    private static final ConnectedClientList emptyConnectedClientList;
    private static final AbstractPool<SyncedNetworkData> syncedDataPool;
    private boolean isServer;
    private SyncedNetworkData syncedData;
    
    static {
        emptyConnectedClientList = new ConnectedClientList();
        syncedDataPool = new AbstractPool<SyncedNetworkData>(-$$Lambda$vlkL56bFiIsybda292rsAkpkiJE.INSTANCE, -$$Lambda$NetworkEntityComponent$FjPymKn7a0pbo2kvDcU_792osMQ.INSTANCE, null);
    }
    
    public NetworkEntityComponent() {
        this.isServer = true;
        this.syncedData = null;
        if (this.isUsingSyncedData()) {
            this.syncedData = NetworkEntityComponent.syncedDataPool.get();
        }
    }
    
    private boolean isUsingSyncedData() {
        return false;
    }
    
    @Override
    public SyncedNetworkData getSyncedData() {
        return this.syncedData;
    }
    
    @Override
    void initClientNetworkComponent(final SyncedNetworkData syncedData) {
        this.isServer = false;
        this.syncedData = syncedData;
    }
    
    @Override
    void initServerNetworkComponent(final ConnectedClientList clients) {
        this.isServer = true;
        if (this.syncedData != null) {
            this.syncedData.setClients(clients);
        }
    }
    
    public boolean isServer() {
        return this.isServer;
    }
    
    @Override
    void onClientEventReceived(final String s, final Object o, final String s2) {
    }
    
    @Override
    void onComponentRemoved() {
        super.onComponentRemoved();
        if (this.syncedData != null) {
            NetworkEntityComponent.syncedDataPool.store(this.syncedData);
            this.syncedData = null;
        }
    }
    
    @Override
    void onServerEventReceived(final ConnectedClient connectedClient, final String s, final Object o, final String s2) {
    }
    
    @Override
    public void send(final ConnectedClient connectedClient, final String s, final Object o) {
        this.getEntity().sendComponentEvent(this, connectedClient, s, o);
    }
    
    @Override
    public void send(final String s, final Object o) {
        this.getEntity().sendComponentEvent(this, s, o);
    }
    
    @Override
    public void setActive(final boolean active) {
        if (!this.isServer) {
            throw new IllegalStateException("setActive must be called only on server-side component");
        }
        super.setActive(active);
    }
}
