package com.zhekasmirnov.apparatus.multiplayer.client;

import com.zhekasmirnov.apparatus.multiplayer.channel.*;
import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;
import java.io.*;
import com.zhekasmirnov.apparatus.util.*;
import java.util.function.*;
import com.zhekasmirnov.apparatus.multiplayer.*;
import com.zhekasmirnov.apparatus.job.*;
import com.zhekasmirnov.apparatus.multiplayer.channel.data.*;
import java.util.*;

@SynthesizedClassMap({ -$$Lambda$ModdedClient$BxIu-r4klhR5RjsqZsoA-SdqbOE.class, -$$Lambda$ModdedClient$smBnuCCF1h9CsWJRqKQ8m6K5wWg.class, -$$Lambda$ModdedClient$E7d5X4B3C5Xcu5Vp7o2232PL754.class, -$$Lambda$ModdedClient$PSAgPovz6a1tP2j5ZM74AFG-VjU.class, -$$Lambda$ModdedClient$N8Qm9Uam2ef-D3Sgye2Qefhq2Qw.class })
public class ModdedClient implements ChannelInterface.OnPacketReceivedListener
{
    private ChannelInterface channel;
    private final NetworkConfig config;
    private boolean isConnectionEstablished;
    private boolean isRunning;
    private final Object lock;
    private final List<OnConnectedListener> onConnectedListeners;
    private final List<OnDisconnectedListener> onDisconnectedListeners;
    private final Map<String, List<OnPacketReceivedListener>> onPacketReceivedListenerMap;
    private final List<OnRequestingConnectionListener> onRequestingConnectionListeners;
    private final List<OnShutdownListener> onShutdownListeners;
    private long playerUid;
    private final Set<String> remainingServerInitializationPackets;
    private String serverDisconnectReason;
    private final Set<String> serverInitializationPackets;
    private final Object serverInitializationPacketsMonitor;
    
    public ModdedClient(final NetworkConfig config) {
        this.lock = new Object();
        this.isRunning = false;
        this.isConnectionEstablished = false;
        this.channel = null;
        this.playerUid = -1L;
        this.serverDisconnectReason = null;
        this.onConnectedListeners = new ArrayList<OnConnectedListener>();
        this.onRequestingConnectionListeners = new ArrayList<OnRequestingConnectionListener>();
        this.onDisconnectedListeners = new ArrayList<OnDisconnectedListener>();
        this.onShutdownListeners = new ArrayList<OnShutdownListener>();
        this.onPacketReceivedListenerMap = new HashMap<String, List<OnPacketReceivedListener>>();
        this.serverInitializationPackets = new HashSet<String>();
        this.remainingServerInitializationPackets = new HashSet<String>();
        this.serverInitializationPacketsMonitor = new Object();
        this.config = config;
    }
    
    public <T> void addInitializationPacketListener(final String s, final TypedOnPacketReceivedListener<T> typedOnPacketReceivedListener) {
        this.addPacketReceivedListener(s, (TypedOnPacketReceivedListener<Object>)typedOnPacketReceivedListener);
        this.serverInitializationPackets.add(s);
    }
    
    public void addOnConnectedListener(final OnConnectedListener onConnectedListener) {
        this.onConnectedListeners.add(onConnectedListener);
    }
    
    public void addOnDisconnectedListener(final OnDisconnectedListener onDisconnectedListener) {
        this.onDisconnectedListeners.add(onDisconnectedListener);
    }
    
    public void addOnRequestingConnectionListener(final OnRequestingConnectionListener onRequestingConnectionListener) {
        this.onRequestingConnectionListeners.add(onRequestingConnectionListener);
    }
    
    public <T> void addPacketReceivedListener(final String s, final TypedOnPacketReceivedListener<T> typedOnPacketReceivedListener) {
        this.addUntypedPacketReceivedListener(s, (OnPacketReceivedListener)new -$$Lambda$ModdedClient$PSAgPovz6a1tP2j5ZM74AFG-VjU((TypedOnPacketReceivedListener)typedOnPacketReceivedListener));
    }
    
    public void addShutdownListener(final OnShutdownListener onShutdownListener) {
        this.onShutdownListeners.add(onShutdownListener);
    }
    
    public void addUntypedInitializationPacketListener(final String s, final OnPacketReceivedListener onPacketReceivedListener) {
        this.addUntypedPacketReceivedListener(s, onPacketReceivedListener);
        this.serverInitializationPackets.add(s);
    }
    
    public void addUntypedPacketReceivedListener(final String s, final OnPacketReceivedListener onPacketReceivedListener) {
        Java8BackComp.computeIfAbsent(this.onPacketReceivedListenerMap, s, -$$Lambda$ModdedClient$E7d5X4B3C5Xcu5Vp7o2232PL754.INSTANCE).add(onPacketReceivedListener);
    }
    
    public boolean awaitAllInitializationPackets() {
        while (!this.remainingServerInitializationPackets.isEmpty()) {
            try {
                synchronized (this.serverInitializationPacketsMonitor) {
                    this.serverInitializationPacketsMonitor.wait();
                }
            }
            catch (InterruptedException ex) {
                return this.remainingServerInitializationPackets.isEmpty();
            }
            break;
        }
        return true;
    }
    
    public boolean awaitAllInitializationPackets(final int n) {
        final long currentTimeMillis = System.currentTimeMillis();
        while (!this.remainingServerInitializationPackets.isEmpty() && currentTimeMillis + n > System.currentTimeMillis()) {
            try {
                synchronized (this.serverInitializationPacketsMonitor) {
                    this.serverInitializationPacketsMonitor.wait(currentTimeMillis + n - System.currentTimeMillis());
                }
            }
            catch (InterruptedException ex) {
                return this.remainingServerInitializationPackets.isEmpty();
            }
            break;
        }
        return this.remainingServerInitializationPackets.isEmpty();
    }
    
    public long getPlayerUid() {
        return this.playerUid;
    }
    
    public boolean isConnectionEstablished() {
        return this.isConnectionEstablished;
    }
    
    public boolean isRunning() {
        return this.isRunning;
    }
    
    @Override
    public void onPacketReceived(final String s, final Object o, final Class<?> clazz) {
        if (s.equals("system.server_disconnect")) {
            synchronized (this.lock) {
                this.serverDisconnectReason = o.toString();
                this.channel.close();
                this.isRunning = false;
                this.isConnectionEstablished = false;
                return;
            }
        }
        if (s.equals("system.client_connection_allowed")) {
            this.isConnectionEstablished = true;
            final Iterator<OnConnectedListener> iterator = this.onConnectedListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onConnected();
            }
            return;
        }
        if (s.equals("system.ping")) {
            this.channel.send("system.ping", o);
            return;
        }
        if (s.equals("system.message")) {
            Network.getSingleton().getClientThreadJobExecutor().add(new -$$Lambda$ModdedClient$smBnuCCF1h9CsWJRqKQ8m6K5wWg(o));
        }
        final int index = s.indexOf(35);
        String substring = null;
        String substring2 = s;
        if (index != -1) {
            substring = s.substring(index + 1);
            substring2 = s.substring(0, index);
        }
        final List<OnPacketReceivedListener> list = this.onPacketReceivedListenerMap.get(substring2);
        if (list != null) {
            final Iterator<OnPacketReceivedListener> iterator2 = list.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().onPacketReceived(o, substring, clazz);
            }
        }
        if (!this.remainingServerInitializationPackets.isEmpty() && this.remainingServerInitializationPackets.remove(substring2)) {
            synchronized (this.serverInitializationPacketsMonitor) {
                this.serverInitializationPacketsMonitor.notifyAll();
            }
        }
    }
    
    public void send(final String s, final Object o) {
        this.channel.send(s, o);
    }
    
    public <T> void send(final String s, final T t, final Class<T> clazz) {
        this.channel.send(s, t, clazz);
    }
    
    public void setPlayerUid(final long playerUid) {
        synchronized (this.lock) {
            this.playerUid = playerUid;
            if (this.isRunning && playerUid != -1L) {
                final ChannelInterface channel = this.channel;
                final StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(playerUid);
                channel.send("system.player_entity", sb.toString());
            }
        }
    }
    
    public void shutdown() {
        this.shutdown("client disconnected due shutdown");
    }
    
    public void shutdown(final String s) {
        synchronized (this.lock) {
            if (this.isRunning) {
                final Iterator<OnShutdownListener> iterator = this.onShutdownListeners.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onShutdown(this);
                }
                this.channel.send("system.client_disconnect", s);
                this.channel.shutdownAndAwaitDisconnect(2000);
                this.isRunning = false;
                this.isConnectionEstablished = false;
            }
        }
    }
    
    public void start(final DataChannel dataChannel) {
        this.shutdown("client restarted");
        synchronized (this.lock) {
            this.isRunning = true;
            this.isConnectionEstablished = false;
            this.channel = new ChannelInterface(dataChannel);
            this.remainingServerInitializationPackets.addAll(this.serverInitializationPackets);
            this.channel.addListener((ChannelInterface.OnPacketReceivedListener)this);
            dataChannel.setBrokenChannelListener((DataChannel.IBrokenChannelListener)new -$$Lambda$ModdedClient$N8Qm9Uam2ef-D3Sgye2Qefhq2Qw(this));
            new Thread(new -$$Lambda$ModdedClient$BxIu-r4klhR5RjsqZsoA-SdqbOE(this)).start();
            final Iterator<OnRequestingConnectionListener> iterator = this.onRequestingConnectionListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onRequestingConnection();
            }
            if (this.playerUid != -1L) {
                final ChannelInterface channel = this.channel;
                final StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(this.playerUid);
                channel.send("system.player_entity", sb.toString());
            }
        }
    }
    
    public interface OnConnectedListener
    {
        void onConnected();
    }
    
    public interface OnDisconnectedListener
    {
        void onDisconnected(final String p0);
    }
    
    public interface OnPacketReceivedListener
    {
        void onPacketReceived(final Object p0, final String p1, final Class<?> p2);
    }
    
    public interface OnRequestingConnectionListener
    {
        void onRequestingConnection();
    }
    
    public interface OnShutdownListener
    {
        void onShutdown(final ModdedClient p0);
    }
    
    public interface TypedOnPacketReceivedListener<T>
    {
        void onPacketReceived(final T p0, final String p1);
    }
}
