package com.zhekasmirnov.apparatus.multiplayer.util.entity;

import com.zhekasmirnov.apparatus.multiplayer.util.list.*;
import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.multiplayer.client.*;
import com.zhekasmirnov.apparatus.job.*;
import com.zhekasmirnov.apparatus.multiplayer.*;
import com.zhekasmirnov.apparatus.util.*;
import java.util.function.*;
import org.mozilla.javascript.annotations.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;
import java.util.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;
import org.json.*;

@SynthesizedClassMap({ -$$Lambda$SyncedNetworkData$PNEJr2B1KEtfILT0PdMtLoUDC2c.class, -$$Lambda$SyncedNetworkData$6efHvo_Gz1N84XDCXeyoUsb4DUo.class, -$$Lambda$SyncedNetworkData$FZcumX9fJIxfbk4TH9YJYhsBLig.class, -$$Lambda$SyncedNetworkData$mYU7k3E6CAs8V8l2WfsrRRVl_YQ.class, -$$Lambda$SyncedNetworkData$cVY6wgFDSLIWEGq3ieDzF6RinBs.class })
public class SyncedNetworkData implements Listener
{
    private static final Map<String, SyncedNetworkData> receivedSyncedData;
    private static final Map<String, SyncedNetworkData> serverSyncedData;
    private final ModdedClient clientInstance;
    private ConnectedClientList clients;
    private final Map<String, Object> data;
    private final List<OnDataChangedListener> dataChangedListeners;
    private final Map<String, Object> dirtyData;
    private DataVerifier globalVerifier;
    private final boolean isServer;
    private final String name;
    private final Map<String, DataVerifier> verifierMap;
    
    static {
        serverSyncedData = new HashMap<String, SyncedNetworkData>();
        receivedSyncedData = new HashMap<String, SyncedNetworkData>();
        Network.getSingleton().addClientPacket("system.synced_data.data", (ModdedClient.TypedOnPacketReceivedListener<Object>)-$$Lambda$SyncedNetworkData$6efHvo_Gz1N84XDCXeyoUsb4DUo.INSTANCE, null);
        Network.getSingleton().addServerPacket("system.synced_data.data", (ModdedServer.TypedOnPacketReceivedListener<Object>)-$$Lambda$SyncedNetworkData$cVY6wgFDSLIWEGq3ieDzF6RinBs.INSTANCE, null);
        Network.getSingleton().addServerShutdownListener(-$$Lambda$SyncedNetworkData$mYU7k3E6CAs8V8l2WfsrRRVl_YQ.INSTANCE);
        Network.getSingleton().addClientShutdownListener(-$$Lambda$SyncedNetworkData$PNEJr2B1KEtfILT0PdMtLoUDC2c.INSTANCE);
    }
    
    public SyncedNetworkData() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SND");
        sb.append(UUID.randomUUID().toString());
        this(sb.toString());
    }
    
    public SyncedNetworkData(final ConnectedClientList clients) {
        this();
        this.setClients(clients);
    }
    
    public SyncedNetworkData(final ConnectedClientList clients, final String s) {
        this(s);
        this.setClients(clients);
    }
    
    public SyncedNetworkData(final String s) {
        this(s, true);
        NetworkThreadMarker.assertServerThread();
        SyncedNetworkData.serverSyncedData.put(s, this);
    }
    
    protected SyncedNetworkData(final String name, final boolean isServer) {
        this.clientInstance = Network.getSingleton().getClient();
        this.clients = new ConnectedClientList();
        this.data = new HashMap<String, Object>();
        this.verifierMap = new HashMap<String, DataVerifier>();
        this.globalVerifier = null;
        this.dirtyData = new HashMap<String, Object>();
        this.dataChangedListeners = new ArrayList<OnDataChangedListener>();
        this.name = name;
        this.isServer = isServer;
        this.clients.addListener((ConnectedClientList.Listener)this);
    }
    
    @JSStaticFunction
    public static SyncedNetworkData getClientSyncedData(final String s) {
        return Java8BackComp.computeIfAbsent(SyncedNetworkData.receivedSyncedData, s, new -$$Lambda$SyncedNetworkData$FZcumX9fJIxfbk4TH9YJYhsBLig(s));
    }
    
    public void addClient(final ConnectedClient connectedClient) {
        if (!this.isServer) {
            throw new IllegalStateException("this is client SyncedNetworkData instance");
        }
        this.clients.add(connectedClient);
    }
    
    public void addOnDataChangedListener(final OnDataChangedListener onDataChangedListener) {
        this.dataChangedListeners.add(onDataChangedListener);
    }
    
    public void addVerifier(final String s, final DataVerifier dataVerifier) {
        this.verifierMap.put(s, dataVerifier);
    }
    
    public void apply() {
        if (!this.dirtyData.isEmpty()) {
            synchronized (this.dirtyData) {
                if (this.isServer) {
                    NetworkThreadMarker.assertServerThread();
                    final ConnectedClientList clients = this.clients;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("system.synced_data.data#");
                    sb.append(this.name);
                    clients.send(sb.toString(), new JSONObject((Map)this.dirtyData));
                }
                else {
                    NetworkThreadMarker.assertClientThread();
                    final ModdedClient clientInstance = this.clientInstance;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("system.synced_data.data#");
                    sb2.append(this.name);
                    clientInstance.send(sb2.toString(), new JSONObject((Map)this.dirtyData));
                }
                this.dirtyData.clear();
            }
        }
    }
    
    public boolean getBoolean(final String s) {
        return this.getBoolean(s, false);
    }
    
    public boolean getBoolean(final String s, final boolean b) {
        final Boolean value = this.data.get(s);
        if (value instanceof Boolean) {
            return value;
        }
        return b;
    }
    
    public ConnectedClientList getClients() {
        return this.clients;
    }
    
    public double getDouble(final String s) {
        return this.getDouble(s, 0.0);
    }
    
    public double getDouble(final String s, final double n) {
        final Number value = this.data.get(s);
        if (value instanceof Number) {
            return value.doubleValue();
        }
        return n;
    }
    
    public float getFloat(final String s) {
        return this.getFloat(s, 0.0f);
    }
    
    public float getFloat(final String s, final float n) {
        final Number value = this.data.get(s);
        if (value instanceof Number) {
            return value.floatValue();
        }
        return n;
    }
    
    public DataVerifier getGlobalVerifier() {
        return this.globalVerifier;
    }
    
    public int getInt(final String s) {
        return this.getInt(s, 0);
    }
    
    public int getInt(final String s, final int n) {
        final Number value = this.data.get(s);
        if (value instanceof Number) {
            return value.intValue();
        }
        return n;
    }
    
    public long getLong(final String s) {
        return this.getLong(s, 0L);
    }
    
    public long getLong(final String s, final long n) {
        final Number value = this.data.get(s);
        if (value instanceof Number) {
            return value.longValue();
        }
        return n;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Object getObject(final String s) {
        return this.data.get(s);
    }
    
    public String getString(final String s) {
        return this.getString(s, null);
    }
    
    public String getString(final String s, final String s2) {
        final String value = this.data.get(s);
        if (value instanceof String) {
            return value;
        }
        return s2;
    }
    
    public boolean isServer() {
        return this.isServer;
    }
    
    @Override
    public void onAdd(final ConnectedClient connectedClient) {
        final StringBuilder sb = new StringBuilder();
        sb.append("system.synced_data.data#");
        sb.append(this.name);
        connectedClient.send(sb.toString(), new JSONObject((Map)this.data));
    }
    
    @Override
    public void onRemove(final ConnectedClient connectedClient) {
    }
    
    protected void put(final String s, final Object o, final boolean b) {
        if (!Java8BackComp.equals(this.data.get(s), o)) {
            this.data.put(s, o);
            if (!b) {
                synchronized (this.dirtyData) {
                    this.dirtyData.put(s, o);
                }
            }
            final Iterator<OnDataChangedListener> iterator = this.dataChangedListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onChanged(this, s, b);
            }
        }
    }
    
    public void putBoolean(final String s, final boolean b) {
        this.put(s, b, false);
    }
    
    public void putDouble(final String s, final double n) {
        this.put(s, n, false);
    }
    
    public void putFloat(final String s, final float n) {
        this.put(s, n, false);
    }
    
    public void putInt(final String s, final int n) {
        this.put(s, n, false);
    }
    
    public void putLong(final String s, final long n) {
        this.put(s, n, false);
    }
    
    public void putObject(final String s, final Object o) {
        this.put(s, o, false);
    }
    
    public void putString(final String s, final String s2) {
        this.put(s, s2, false);
    }
    
    public void removeAllListeners() {
        this.dataChangedListeners.clear();
    }
    
    public void removeOnDataChangedListener(final OnDataChangedListener onDataChangedListener) {
        this.dataChangedListeners.remove(onDataChangedListener);
    }
    
    public void sendChanges() {
        this.apply();
    }
    
    public void setClients(final ConnectedClientList clients) {
        this.clients.removeListener((ConnectedClientList.Listener)this);
        (this.clients = clients).addListener((ConnectedClientList.Listener)this);
        final StringBuilder sb = new StringBuilder();
        sb.append("system.synced_data.data#");
        sb.append(this.name);
        clients.send(sb.toString(), new JSONObject((Map)this.data));
    }
    
    public void setGlobalVerifier(final DataVerifier globalVerifier) {
        this.globalVerifier = globalVerifier;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SyncedNetworkData{name='");
        sb.append(this.name);
        sb.append('\'');
        sb.append(", data=");
        sb.append(this.data);
        sb.append('}');
        return sb.toString();
    }
    
    public interface DataVerifier
    {
        Object verify(final String p0, final Object p1);
    }
    
    public interface OnDataChangedListener
    {
        void onChanged(final SyncedNetworkData p0, final String p1, final boolean p2);
    }
}
