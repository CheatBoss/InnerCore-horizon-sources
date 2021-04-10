package com.zhekasmirnov.apparatus.multiplayer.server;

import com.zhekasmirnov.apparatus.multiplayer.socket.*;
import com.zhekasmirnov.apparatus.mcpe.*;
import com.android.tools.r8.annotations.*;
import java.util.concurrent.*;
import com.zhekasmirnov.apparatus.multiplayer.*;
import com.zhekasmirnov.apparatus.util.*;
import java.util.function.*;
import java.util.*;
import com.zhekasmirnov.apparatus.multiplayer.channel.*;
import java.net.*;
import java.io.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;
import com.zhekasmirnov.apparatus.multiplayer.channel.data.*;
import android.util.*;

@SynthesizedClassMap({ -$$Lambda$ModdedServer$7h5_HW05Whvw0mswMNJCpTRHMPs.class, -$$Lambda$ModdedServer$QYuA5EcNxG2xaQUFj6D5oiQOy5k.class, -$$Lambda$ModdedServer$46iz4GdCBq7T4VRc6bzmMerQnoE.class, -$$Lambda$ModdedServer$TIs4wuMBZQ6eMTTprMZbcz2c9aE.class, -$$Lambda$ModdedServer$hkhWxOhJiklUoWAbHfpgDyUfb2c.class, -$$Lambda$ModdedServer$P_sS4ktqwjuowALI1Ukzx-vEAXQ.class, -$$Lambda$TxoQGTf0CEpo5TnTP3BxRmq0Vws.class })
public class ModdedServer implements IClientConnectListener, ConnectionListener, OnStateChangedListener, OnDisconnectListener
{
    private final NetworkConfig config;
    private final Map<Long, ConnectedClient> connectedClientByPlayerUid;
    private final List<ConnectedClient> connectedClients;
    private final Map<String, List<InitializationPacketListener>> initializationPacketListenerMap;
    private final List<ConnectedClient> initializingClients;
    private boolean isRunning;
    private final Queue<Runnable> networkThreadQueue;
    private final Object networkThreadQueueMonitor;
    private final List<OnClientConnectedListener> onClientConnectedListeners;
    private final List<OnClientConnectionRequestedListener> onClientConnectionRequestedListeners;
    private final List<OnClientDisconnectedListener> onClientDisconnectedListeners;
    private final Map<String, List<OnPacketReceivedListener>> onPacketReceivedListenerMap;
    private final List<OnShutdownListener> onShutdownListeners;
    private final SocketServer socketServer;
    
    public ModdedServer(final NetworkConfig config) {
        this.isRunning = false;
        this.initializingClients = new ArrayList<ConnectedClient>();
        this.connectedClients = new ArrayList<ConnectedClient>();
        this.connectedClientByPlayerUid = new HashMap<Long, ConnectedClient>();
        this.networkThreadQueueMonitor = new Object();
        this.networkThreadQueue = new ConcurrentLinkedDeque<Runnable>();
        this.onClientConnectedListeners = new ArrayList<OnClientConnectedListener>();
        this.onClientConnectionRequestedListeners = new ArrayList<OnClientConnectionRequestedListener>();
        this.onClientDisconnectedListeners = new ArrayList<OnClientDisconnectedListener>();
        this.initializationPacketListenerMap = new HashMap<String, List<InitializationPacketListener>>();
        this.onPacketReceivedListenerMap = new HashMap<String, List<OnPacketReceivedListener>>();
        this.onShutdownListeners = new ArrayList<OnShutdownListener>();
        this.config = config;
        (this.socketServer = new SocketServer()).addClientConnectListener((SocketServer.IClientConnectListener)this);
    }
    
    public <T> void addInitializationPacketListener(final String s, final TypedInitializationPacketListener<T> typedInitializationPacketListener) {
        this.addUntypedInitializationPacketListener(s, new -$$Lambda$ModdedServer$7h5_HW05Whvw0mswMNJCpTRHMPs((TypedInitializationPacketListener)typedInitializationPacketListener));
    }
    
    public void addOnClientConnectedListener(final OnClientConnectedListener onClientConnectedListener) {
        this.onClientConnectedListeners.add(onClientConnectedListener);
    }
    
    public void addOnClientConnectionRequestedListener(final OnClientConnectionRequestedListener onClientConnectionRequestedListener) {
        this.onClientConnectionRequestedListeners.add(onClientConnectionRequestedListener);
    }
    
    public void addOnClientDisconnectedListener(final OnClientDisconnectedListener onClientDisconnectedListener) {
        this.onClientDisconnectedListeners.add(onClientDisconnectedListener);
    }
    
    public <T> void addPacketReceivedListener(final String s, final TypedOnPacketReceivedListener<T> typedOnPacketReceivedListener) {
        this.addUntypedPacketReceivedListener(s, (OnPacketReceivedListener)new -$$Lambda$ModdedServer$TIs4wuMBZQ6eMTTprMZbcz2c9aE((TypedOnPacketReceivedListener)typedOnPacketReceivedListener));
    }
    
    public void addShutdownListener(final OnShutdownListener onShutdownListener) {
        this.onShutdownListeners.add(onShutdownListener);
    }
    
    public void addUntypedInitializationPacketListener(final String s, final InitializationPacketListener initializationPacketListener) {
        Java8BackComp.computeIfAbsent(this.initializationPacketListenerMap, s, -$$Lambda$ModdedServer$46iz4GdCBq7T4VRc6bzmMerQnoE.INSTANCE).add(initializationPacketListener);
    }
    
    public void addUntypedPacketReceivedListener(final String s, final OnPacketReceivedListener onPacketReceivedListener) {
        Java8BackComp.computeIfAbsent(this.onPacketReceivedListenerMap, s, -$$Lambda$ModdedServer$P_sS4ktqwjuowALI1Ukzx-vEAXQ.INSTANCE).add(onPacketReceivedListener);
    }
    
    public void addWatchdogAction(final int n, final Runnable runnable) {
        new Thread(new -$$Lambda$ModdedServer$hkhWxOhJiklUoWAbHfpgDyUfb2c(n, runnable)).start();
    }
    
    public NetworkConfig getConfig() {
        return this.config;
    }
    
    public ConnectedClient getConnectedClientForPlayer(final long n) {
        return this.connectedClientByPlayerUid.get(n);
    }
    
    public List<ConnectedClient> getConnectedClients() {
        return this.connectedClients;
    }
    
    public List<Long> getConnectedPlayers() {
        return new ArrayList<Long>(this.connectedClientByPlayerUid.keySet());
    }
    
    public List<ConnectedClient> getInitializingClients() {
        return this.initializingClients;
    }
    
    public void networkThreadLoop() {
        NetworkThreadMarker.markThreadAs(NetworkThreadMarker.Mark.SERVER);
        while (this.isRunning) {
            final Runnable runnable = this.networkThreadQueue.poll();
            if (runnable != null) {
                runnable.run();
            }
            try {
                synchronized (this.networkThreadQueueMonitor) {
                    this.networkThreadQueueMonitor.wait(100L);
                }
            }
            catch (InterruptedException ex) {}
        }
    }
    
    public void onClientConnected(final DataChannel dataChannel) {
        final ConnectedClient connectedClient = new ConnectedClient(this, dataChannel);
        connectedClient.setDisconnectListener((ConnectedClient.OnDisconnectListener)this);
        connectedClient.setStateChangedListener((ConnectedClient.OnStateChangedListener)this);
        final Iterator<OnClientConnectionRequestedListener> iterator = this.onClientConnectionRequestedListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onConnectionRequested(connectedClient);
        }
        for (final String s : this.initializationPacketListenerMap.keySet()) {
            final Iterator<InitializationPacketListener> iterator3 = this.initializationPacketListenerMap.get(s).iterator();
            while (iterator3.hasNext()) {
                connectedClient.addInitializationPacketListener(s, (InitializationPacketListener)iterator3.next());
            }
        }
        connectedClient.getChannelInterface().addListener((ChannelInterface.OnPacketReceivedListener)new -$$Lambda$ModdedServer$QYuA5EcNxG2xaQUFj6D5oiQOy5k(this, connectedClient));
        connectedClient.start();
    }
    
    @Override
    public boolean onClientConnected(final DataChannel dataChannel, final Socket socket, final boolean b) {
        this.onClientConnected(dataChannel);
        return true;
    }
    
    @Override
    public void onDisconnect(final ConnectedClient connectedClient, final String s, final IOException ex) {
        final Iterator<OnClientDisconnectedListener> iterator = this.onClientDisconnectedListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onClientDisconnected(connectedClient, connectedClient.getDisconnectPacket());
        }
    }
    
    @Override
    public void onNativeChannelConnected(final NativeDataChannel nativeDataChannel) {
        if (EngineConfig.isDeveloperMode()) {
            UserDialog.toast("client connected via native protocol");
        }
        this.onClientConnected(nativeDataChannel);
    }
    
    @Override
    public void onStateChanged(final ConnectedClient connectedClient, ClientState clientState) {
        switch (clientState) {
            default: {}
            case INIT_FAILED:
            case CLOSED: {
                synchronized (this.connectedClients) {
                    this.initializingClients.remove(connectedClient);
                    this.connectedClients.remove(connectedClient);
                    this.connectedClientByPlayerUid.remove(connectedClient.getPlayerUid());
                }
            }
            case OPEN: {
                clientState = (ClientState)this.connectedClients;
                synchronized (clientState) {
                    this.initializingClients.remove(connectedClient);
                    this.connectedClients.add(connectedClient);
                    this.connectedClientByPlayerUid.put(connectedClient.getPlayerUid(), connectedClient);
                    // monitorexit(clientState)
                    clientState = (ClientState)this.onClientConnectedListeners.iterator();
                    while (((Iterator)clientState).hasNext()) {
                        ((Iterator<OnClientConnectedListener>)clientState).next().onClientConnected(connectedClient);
                    }
                }
            }
            case INITIALIZING: {
                synchronized (this.connectedClients) {
                    this.initializingClients.add(connectedClient);
                    return;
                }
                break;
            }
        }
    }
    
    public DataChannel openLocalClientChannel() {
        if (!this.config.isLocalNativeProtocolForced()) {
            final Pair<DirectDataChannel, DirectDataChannel> directChannelPair = DirectDataChannel.createDirectChannelPair();
            this.onClientConnected((DataChannel)directChannelPair.first);
            return (DataChannel)directChannelPair.second;
        }
        final NativeDataChannel nativeDataChannel = new NativeDataChannel(NativeNetworking.getOrCreateClientToServerChannel());
        if (!nativeDataChannel.pingPong(10000)) {
            UserDialog.dialog("LAN Server Error", "failed to ping-pong local native channel");
            return nativeDataChannel;
        }
        UserDialog.toast("successfully opened local native channel");
        return nativeDataChannel;
    }
    
    public void runOnNetworkThread(final Runnable runnable) {
        this.networkThreadQueue.add(runnable);
        this.networkThreadQueueMonitor.notifyAll();
    }
    
    public void sendMessageToAll(final String s) {
        synchronized (this.connectedClients) {
            final Iterator<ConnectedClient> iterator = this.connectedClients.iterator();
            while (iterator.hasNext()) {
                iterator.next().sendMessage(s);
            }
        }
    }
    
    public void sendToAll(final String s, final Object o) {
        synchronized (this.connectedClients) {
            final Iterator<ConnectedClient> iterator = this.connectedClients.iterator();
            while (iterator.hasNext()) {
                iterator.next().send(s, o);
            }
        }
    }
    
    public <T> void sendToAll(final String s, final T t, final Class<T> clazz) {
        synchronized (this.connectedClients) {
            final Iterator<ConnectedClient> iterator = this.connectedClients.iterator();
            while (iterator.hasNext()) {
                iterator.next().send(s, t, clazz);
            }
        }
    }
    
    public void shutdown() {
        if (this.isRunning) {
            NativeNetworking.removeConnectionListener((NativeNetworking.ConnectionListener)this);
            final Iterator<OnShutdownListener> iterator = this.onShutdownListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onShutdown(this);
            }
            final ArrayList<ConnectedClient> list = new ArrayList<ConnectedClient>();
            Object o = this.connectedClients;
            synchronized (o) {
                list.addAll((Collection<?>)this.connectedClients);
                list.addAll((Collection<?>)this.initializingClients);
                // monitorexit(o)
                o = list.iterator();
                while (((Iterator)o).hasNext()) {
                    ((Iterator<ConnectedClient>)o).next().disconnect("server closed");
                }
                this.socketServer.close();
                this.isRunning = false;
            }
        }
    }
    
    public void start(final int n) {
        try {
            NativeNetworking.addConnectionListener((NativeNetworking.ConnectionListener)this);
            if (this.config.isSocketConnectionAllowed()) {
                this.socketServer.start(n);
                final StringBuilder sb = new StringBuilder();
                sb.append("modded server started (socket port ");
                sb.append(n);
                sb.append(")");
                UserDialog.toast(sb.toString());
            }
            else {
                this.socketServer.close();
                UserDialog.toast("modded server started (native protocol only)");
            }
            this.isRunning = true;
            new Thread(new -$$Lambda$TxoQGTf0CEpo5TnTP3BxRmq0Vws(this)).start();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public interface OnClientConnectedListener
    {
        void onClientConnected(final ConnectedClient p0);
    }
    
    public interface OnClientConnectionRequestedListener
    {
        void onConnectionRequested(final ConnectedClient p0);
    }
    
    public interface OnClientDisconnectedListener
    {
        void onClientDisconnected(final ConnectedClient p0, final String p1);
    }
    
    public interface OnPacketReceivedListener
    {
        void onPacketReceived(final ConnectedClient p0, final Object p1, final String p2, final Class<?> p3);
    }
    
    public interface OnShutdownListener
    {
        void onShutdown(final ModdedServer p0);
    }
    
    public interface TypedInitializationPacketListener<T>
    {
        void onPacketReceived(final ConnectedClient p0, final T p1) throws InitializationPacketException;
    }
    
    public interface TypedOnPacketReceivedListener<T>
    {
        void onPacketReceived(final ConnectedClient p0, final T p1, final String p2);
    }
}
