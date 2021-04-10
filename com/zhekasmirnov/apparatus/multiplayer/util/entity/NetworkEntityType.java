package com.zhekasmirnov.apparatus.multiplayer.util.entity;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.util.*;
import java.util.function.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;
import java.util.*;
import com.zhekasmirnov.apparatus.multiplayer.util.list.*;

@SynthesizedClassMap({ -$$Lambda$NetworkEntityType$IOB0726gbR7kE9Mtgagtw_y0syw.class, -$$Lambda$NetworkEntityType$gSqnEMNkN6vQevdAGxE3ihpA7xI.class })
public class NetworkEntityType
{
    private static final Map<String, NetworkEntityType> entityTypes;
    private ClientAddPacketFactory clientAddPacketFactory;
    private OnClientEntityAddedListener clientEntityAddedListener;
    private OnClientEntityRemovedListener clientEntityRemovedListener;
    private OnClientListSetupListener clientListSetupListener;
    private final Map<String, List<OnClientPacketListener>> clientPacketListenerMap;
    private ClientRemovePacketFactory clientRemovePacketFactory;
    private OnEntitySetupListener entitySetupListener;
    private boolean isDuplicateAddPacketAllowed;
    private final Map<String, List<OnServerPacketListener>> serverPacketListenerMap;
    private final String typeName;
    
    static {
        entityTypes = new HashMap<String, NetworkEntityType>();
    }
    
    public NetworkEntityType(final String typeName) {
        this.entitySetupListener = null;
        this.clientListSetupListener = null;
        this.clientEntityAddedListener = null;
        this.clientEntityRemovedListener = null;
        this.clientAddPacketFactory = null;
        this.clientRemovePacketFactory = null;
        this.isDuplicateAddPacketAllowed = false;
        this.clientPacketListenerMap = new HashMap<String, List<OnClientPacketListener>>();
        this.serverPacketListenerMap = new HashMap<String, List<OnServerPacketListener>>();
        this.typeName = typeName;
        NetworkEntityType.entityTypes.put(typeName, this);
    }
    
    public static NetworkEntityType getByName(final String s) {
        return NetworkEntityType.entityTypes.get(s);
    }
    
    public NetworkEntityType addClientPacketListener(final String s, final OnClientPacketListener onClientPacketListener) {
        Java8BackComp.computeIfAbsent(this.clientPacketListenerMap, s, -$$Lambda$NetworkEntityType$IOB0726gbR7kE9Mtgagtw_y0syw.INSTANCE).add(onClientPacketListener);
        return this;
    }
    
    public NetworkEntityType addServerPacketListener(final String s, final OnServerPacketListener onServerPacketListener) {
        Java8BackComp.computeIfAbsent(this.serverPacketListenerMap, s, -$$Lambda$NetworkEntityType$gSqnEMNkN6vQevdAGxE3ihpA7xI.INSTANCE).add(onServerPacketListener);
        return this;
    }
    
    public String getTypeName() {
        return this.typeName;
    }
    
    public boolean isDuplicateAddPacketAllowed() {
        return this.isDuplicateAddPacketAllowed;
    }
    
    public Object newClientAddPacket(final NetworkEntity networkEntity, final ConnectedClient connectedClient) {
        if (this.clientAddPacketFactory != null) {
            return this.clientAddPacketFactory.newPacket(networkEntity.getTarget(), networkEntity, connectedClient);
        }
        return "";
    }
    
    public Object newClientRemovePacket(final NetworkEntity networkEntity, final ConnectedClient connectedClient) {
        if (this.clientRemovePacketFactory != null) {
            return this.clientRemovePacketFactory.newPacket(networkEntity.getTarget(), networkEntity, connectedClient);
        }
        return "";
    }
    
    public void onClientEntityAdded(final NetworkEntity networkEntity, final Object o) {
        Object onAdded = null;
        if (this.clientEntityAddedListener != null) {
            onAdded = this.clientEntityAddedListener.onAdded(networkEntity, o);
        }
        networkEntity.setTarget(onAdded);
    }
    
    public void onClientEntityRemoved(final NetworkEntity networkEntity, final Object o) {
        if (this.clientEntityRemovedListener != null) {
            this.clientEntityRemovedListener.onRemoved(networkEntity.getTarget(), networkEntity, o);
        }
    }
    
    public void onClientEntityRemovedDueShutdown(final NetworkEntity networkEntity) {
        this.onClientEntityRemoved(networkEntity, null);
    }
    
    public void onClientPacket(final NetworkEntity networkEntity, final String s, final Object o, final String s2) {
        final List<OnClientPacketListener> list = this.clientPacketListenerMap.get(s);
        if (list != null) {
            final Iterator<OnClientPacketListener> iterator = list.iterator();
            while (iterator.hasNext()) {
                iterator.next().onReceived(networkEntity.getTarget(), networkEntity, o, s2);
            }
        }
    }
    
    public void onServerPacket(final NetworkEntity networkEntity, final ConnectedClient connectedClient, final String s, final Object o, final String s2) {
        final List<OnServerPacketListener> list = this.serverPacketListenerMap.get(s);
        if (list != null) {
            final Iterator<OnServerPacketListener> iterator = list.iterator();
            while (iterator.hasNext()) {
                iterator.next().onReceived(networkEntity.getTarget(), networkEntity, connectedClient, o, s2);
            }
        }
    }
    
    public NetworkEntityType setClientAddPacketFactory(final ClientAddPacketFactory clientAddPacketFactory) {
        this.clientAddPacketFactory = clientAddPacketFactory;
        return this;
    }
    
    public NetworkEntityType setClientEntityAddedListener(final OnClientEntityAddedListener clientEntityAddedListener) {
        this.clientEntityAddedListener = clientEntityAddedListener;
        return this;
    }
    
    public NetworkEntityType setClientEntityRemovedListener(final OnClientEntityRemovedListener clientEntityRemovedListener) {
        this.clientEntityRemovedListener = clientEntityRemovedListener;
        return this;
    }
    
    public NetworkEntityType setClientListSetupListener(final OnClientListSetupListener clientListSetupListener) {
        this.clientListSetupListener = clientListSetupListener;
        return this;
    }
    
    public NetworkEntityType setClientRemovePacketFactory(final ClientRemovePacketFactory clientRemovePacketFactory) {
        this.clientRemovePacketFactory = clientRemovePacketFactory;
        return this;
    }
    
    public NetworkEntityType setEntitySetupListener(final OnEntitySetupListener entitySetupListener) {
        this.entitySetupListener = entitySetupListener;
        return this;
    }
    
    public void setupClientList(final ConnectedClientList list, final NetworkEntity networkEntity) {
        if (this.clientListSetupListener != null) {
            this.clientListSetupListener.setup(list, networkEntity.getTarget(), networkEntity);
        }
    }
    
    public void setupEntity(final NetworkEntity networkEntity) {
        if (this.entitySetupListener != null) {
            this.entitySetupListener.setup(networkEntity, networkEntity.isServer());
        }
    }
    
    public interface ClientAddPacketFactory
    {
        Object newPacket(final Object p0, final NetworkEntity p1, final ConnectedClient p2);
    }
    
    public interface ClientRemovePacketFactory
    {
        Object newPacket(final Object p0, final NetworkEntity p1, final ConnectedClient p2);
    }
    
    public interface OnClientEntityAddedListener
    {
        Object onAdded(final NetworkEntity p0, final Object p1);
    }
    
    public interface OnClientEntityRemovedListener
    {
        void onRemoved(final Object p0, final NetworkEntity p1, final Object p2);
    }
    
    public interface OnClientListSetupListener
    {
        void setup(final ConnectedClientList p0, final Object p1, final NetworkEntity p2);
    }
    
    public interface OnClientPacketListener
    {
        void onReceived(final Object p0, final NetworkEntity p1, final Object p2, final String p3);
    }
    
    public interface OnEntitySetupListener
    {
        void setup(final NetworkEntity p0, final boolean p1);
    }
    
    public interface OnServerPacketListener
    {
        void onReceived(final Object p0, final NetworkEntity p1, final ConnectedClient p2, final Object p3, final String p4);
    }
}
