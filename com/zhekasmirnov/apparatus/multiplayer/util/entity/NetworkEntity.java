package com.zhekasmirnov.apparatus.multiplayer.util.entity;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.multiplayer.client.*;
import com.zhekasmirnov.apparatus.multiplayer.util.list.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;
import com.zhekasmirnov.apparatus.multiplayer.*;
import java.util.*;
import com.zhekasmirnov.apparatus.job.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;

@SynthesizedClassMap({ -$$Lambda$NetworkEntity$v4KTk_HDAkqnBe7w34di5OcQ7dk.class, -$$Lambda$NetworkEntity$ux5Nef5duHx_83o7gM8dczELIb8.class, -$$Lambda$NetworkEntity$LmjxNYL5o4NeR2JKCILWNSszzGQ.class, -$$Lambda$NetworkEntity$N57XopVNUcOiKAxtvbEU5UTVoCQ.class, -$$Lambda$NetworkEntity$zofLOu6GudmFSyrVnU60CPiGGbc.class, -$$Lambda$NetworkEntity$6RjqUbuYb2mI-1yNWNzgiiKAWN8.class, -$$Lambda$NetworkEntity$BeQ_emUlms7wjhe_gQU4o8Srp9w.class, -$$Lambda$NetworkEntity$Aj2nDSugul0FvAmx-RUIcTWYuIE.class, -$$Lambda$NetworkEntity$lyOHpZvsIpTd4w5JHRestcAUoYI.class, -$$Lambda$NetworkEntity$Q9DA3rEbadXL5Y3u39YSZ0dsSbA.class, -$$Lambda$NetworkEntity$G6HxXZq79cg3WOvU1u6zUexFjdA.class, -$$Lambda$NetworkEntity$yvCuPwdonie2q7kiWDwPGB80dE4.class })
public class NetworkEntity
{
    private static final Map<String, NetworkEntity> clientEntities;
    private static final ThreadLocal<ConnectedClient> currentClient;
    private static final Map<String, NetworkEntity> serverEntities;
    private JobExecutor clientExecutor;
    private final ModdedClient clientInstance;
    private final ConnectedClientList clients;
    private boolean isRemoved;
    private final boolean isServer;
    private final String name;
    private JobExecutor serverExecutor;
    private Object target;
    private final NetworkEntityType type;
    
    static {
        serverEntities = new HashMap<String, NetworkEntity>();
        clientEntities = new HashMap<String, NetworkEntity>();
        currentClient = new ThreadLocal<ConnectedClient>();
        Network.getSingleton().addClientPacket("system.entity.add", -$$Lambda$NetworkEntity$ux5Nef5duHx_83o7gM8dczELIb8.INSTANCE, null);
        Network.getSingleton().addClientPacket("system.entity.remove", -$$Lambda$NetworkEntity$v4KTk_HDAkqnBe7w34di5OcQ7dk.INSTANCE, null);
        Network.getSingleton().addClientPacket("system.entity.packet", -$$Lambda$NetworkEntity$6RjqUbuYb2mI-1yNWNzgiiKAWN8.INSTANCE, null);
        Network.getSingleton().addServerPacket("system.entity.packet", -$$Lambda$NetworkEntity$yvCuPwdonie2q7kiWDwPGB80dE4.INSTANCE, null);
        Network.getSingleton().addServerShutdownListener(-$$Lambda$NetworkEntity$LmjxNYL5o4NeR2JKCILWNSszzGQ.INSTANCE);
        Network.getSingleton().addClientShutdownListener(-$$Lambda$NetworkEntity$BeQ_emUlms7wjhe_gQU4o8Srp9w.INSTANCE);
    }
    
    public NetworkEntity(final NetworkEntityType networkEntityType) {
        this(networkEntityType, null);
    }
    
    public NetworkEntity(final NetworkEntityType networkEntityType, final Object o) {
        final StringBuilder sb = new StringBuilder();
        sb.append(networkEntityType.getTypeName());
        sb.append(UUID.randomUUID().toString());
        this(networkEntityType, o, sb.toString());
    }
    
    public NetworkEntity(final NetworkEntityType networkEntityType, final Object o, final String s) {
        this(networkEntityType, o, s, true);
        NetworkThreadMarker.assertServerThread();
        NetworkEntity.serverEntities.put(s, this);
    }
    
    protected NetworkEntity(final NetworkEntityType type, final Object target, final String name, final boolean isServer) {
        this.target = null;
        this.isRemoved = false;
        this.clientInstance = Network.getSingleton().getClient();
        this.clients = new ConnectedClientList();
        this.clientExecutor = Network.getSingleton().getClientThreadJobExecutor();
        this.serverExecutor = Network.getSingleton().getServerThreadJobExecutor();
        if (type == null) {
            throw new NullPointerException("networkEntityType cannot be null");
        }
        this.name = name;
        this.type = type;
        this.target = target;
        this.isServer = isServer;
        type.setupEntity(this);
        if (isServer) {
            this.clients.addListener((ConnectedClientList.Listener)new ConnectedClientList.Listener() {
                @Override
                public void onAdd(final ConnectedClient connectedClient) {
                    if (!NetworkEntity.this.isRemoved) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("system.entity.add#");
                        sb.append(type.getTypeName());
                        sb.append("#");
                        sb.append(name);
                        connectedClient.send(sb.toString(), type.newClientAddPacket(NetworkEntity.this, connectedClient));
                    }
                }
                
                @Override
                public void onRemove(final ConnectedClient connectedClient) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("system.entity.remove#");
                    sb.append(name);
                    connectedClient.send(sb.toString(), type.newClientRemovePacket(NetworkEntity.this, connectedClient));
                }
            });
            type.setupClientList(this.clients, this);
        }
    }
    
    public static NetworkEntity getClientEntityInstance(final String s) {
        return NetworkEntity.clientEntities.get(s);
    }
    
    public static void loadClass() {
    }
    
    private void removeOnShutdown() {
        synchronized (this) {
            if (this.isRemoved) {
                return;
            }
            this.isRemoved = true;
            if (this.isServer) {
                this.clients.clear();
            }
            else {
                Network.getSingleton().getInstantJobExecutor().add(new -$$Lambda$NetworkEntity$zofLOu6GudmFSyrVnU60CPiGGbc(this));
            }
        }
    }
    
    public JobExecutor getClientExecutor() {
        return this.clientExecutor;
    }
    
    public ConnectedClientList getClients() {
        return this.clients;
    }
    
    public String getName() {
        return this.name;
    }
    
    public JobExecutor getServerExecutor() {
        return this.serverExecutor;
    }
    
    public Object getTarget() {
        return this.target;
    }
    
    public NetworkEntityType getType() {
        return this.type;
    }
    
    public boolean isRemoved() {
        return this.isRemoved;
    }
    
    public boolean isServer() {
        return this.isServer;
    }
    
    public void refreshClients() {
        this.clients.refresh();
    }
    
    public void remove() {
        synchronized (this) {
            if (this.isServer) {
                NetworkThreadMarker.assertServerThread();
                this.clients.setAddPolicy(null);
                this.clients.clear();
                NetworkEntity.serverEntities.remove(this.name);
                this.isRemoved = true;
                return;
            }
            throw new IllegalStateException("remove() can be called only on server network entity");
        }
    }
    
    public void respond(final String s, final Object o) {
        synchronized (this) {
            if (this.isRemoved) {
                return;
            }
            final ConnectedClient connectedClient = NetworkEntity.currentClient.get();
            if (connectedClient == null) {
                throw new IllegalStateException("respond() must be called only in server packet callback");
            }
            this.send(connectedClient, s, o);
        }
    }
    
    public void send(final ConnectedClient connectedClient, final String s, final Object o) {
        synchronized (this) {
            if (this.isRemoved) {
                return;
            }
            if (!this.isServer) {
                throw new IllegalStateException();
            }
            NetworkThreadMarker.assertServerThread();
            final StringBuilder sb = new StringBuilder();
            sb.append("system.entity.packet#");
            sb.append(this.name);
            sb.append("#");
            sb.append(s);
            connectedClient.send(sb.toString(), o);
        }
    }
    
    public void send(final String s, final Object o) {
        synchronized (this) {
            if (this.isRemoved) {
                return;
            }
            if (this.isServer) {
                NetworkThreadMarker.assertServerThread();
                final ConnectedClientList clients = this.clients;
                final StringBuilder sb = new StringBuilder();
                sb.append("system.entity.packet#");
                sb.append(this.name);
                sb.append("#");
                sb.append(s);
                clients.send(sb.toString(), o);
            }
            else {
                NetworkThreadMarker.assertClientThread();
                final ModdedClient clientInstance = this.clientInstance;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("system.entity.packet#");
                sb2.append(this.name);
                sb2.append("#");
                sb2.append(s);
                clientInstance.send(sb2.toString(), o);
            }
        }
    }
    
    public void setClientExecutor(final JobExecutor clientExecutor) {
        if (clientExecutor == null) {
            throw new NullPointerException("clientExecutor cannot be null");
        }
        this.clientExecutor = clientExecutor;
    }
    
    public void setServerExecutor(final JobExecutor serverExecutor) {
        if (serverExecutor == null) {
            throw new NullPointerException("serverExecutor cannot be null");
        }
        this.serverExecutor = serverExecutor;
    }
    
    public void setTarget(final Object target) {
        this.target = target;
    }
}
