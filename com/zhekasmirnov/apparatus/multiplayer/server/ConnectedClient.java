package com.zhekasmirnov.apparatus.multiplayer.server;

import com.zhekasmirnov.apparatus.multiplayer.channel.*;
import com.android.tools.r8.annotations.*;
import java.io.*;
import com.zhekasmirnov.apparatus.multiplayer.channel.data.*;
import java.util.*;
import com.zhekasmirnov.apparatus.util.*;
import java.util.function.*;
import com.zhekasmirnov.apparatus.multiplayer.*;

@SynthesizedClassMap({ -$$Lambda$ConnectedClient$FPzVC740xi2XoG1P2y6d9rUr7qQ.class, -$$Lambda$ConnectedClient$jGeQFSw4MMWUzsuDfemqcDuJvE4.class, -$$Lambda$ConnectedClient$CTK1LSfjB7FEnVDOA3AEznh77o0.class, -$$Lambda$ConnectedClient$nSbclW9UvAauwm-cOGl7R7juryQ.class })
public class ConnectedClient extends Thread implements OnPacketReceivedListener
{
    private final ChannelInterface channel;
    private IOException disconnectCause;
    private OnDisconnectListener disconnectListener;
    private String disconnectPacket;
    private InitializationPacketException initializationPacketFailureCause;
    private final Map<String, List<InitializationPacketListener>> initializationPacketListenerMap;
    private long playerUid;
    private final Set<String> remainingInitializationPackets;
    private final ModdedServer server;
    private ClientState state;
    private OnStateChangedListener stateChangedListener;
    
    public ConnectedClient(final ModdedServer server, final DataChannel dataChannel) {
        this.playerUid = -1L;
        this.state = ClientState.CREATED;
        this.disconnectPacket = null;
        this.initializationPacketListenerMap = new HashMap<String, List<InitializationPacketListener>>();
        this.remainingInitializationPackets = new HashSet<String>();
        this.initializationPacketFailureCause = null;
        this.server = server;
        (this.channel = new ChannelInterface(dataChannel)).addListener((ChannelInterface.OnPacketReceivedListener)this);
        this.addInitializationPacketListener("system.player_entity", (InitializationPacketListener)new -$$Lambda$ConnectedClient$CTK1LSfjB7FEnVDOA3AEznh77o0(this));
    }
    
    public void addInitializationPacketListener(final String s, final InitializationPacketListener initializationPacketListener) {
        final List<InitializationPacketListener> list = Java8BackComp.computeIfAbsent(this.initializationPacketListenerMap, s, -$$Lambda$ConnectedClient$FPzVC740xi2XoG1P2y6d9rUr7qQ.INSTANCE);
        final Set<String> remainingInitializationPackets = this.remainingInitializationPackets;
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("$");
        sb.append(list.size());
        remainingInitializationPackets.add(sb.toString());
        list.add(initializationPacketListener);
    }
    
    public void disconnect() {
        this.disconnect("no further information");
    }
    
    public void disconnect(final String disconnectPacket) {
        if (this.state != ClientState.CLOSED) {
            this.disconnectPacket = disconnectPacket;
            this.channel.send("system.server_disconnect", disconnectPacket);
            this.channel.shutdownAndAwaitDisconnect(2000);
        }
    }
    
    public ChannelInterface getChannelInterface() {
        return this.channel;
    }
    
    public ClientState getClientState() {
        return this.state;
    }
    
    public IOException getDisconnectCause() {
        return this.disconnectCause;
    }
    
    public String getDisconnectPacket() {
        return this.disconnectPacket;
    }
    
    public InitializationPacketException getInitializationPacketFailureCause() {
        return this.initializationPacketFailureCause;
    }
    
    public long getPlayerUid() {
        return this.playerUid;
    }
    
    public boolean isClosed() {
        return this.state == ClientState.CLOSED;
    }
    
    @Override
    public void onPacketReceived(final String s, final Object initializationPacketFailureCause, final Class<?> clazz) {
        if (s.equals("system.client_disconnect")) {
            this.disconnectPacket = initializationPacketFailureCause.toString();
            this.channel.close();
        }
        if (this.state != ClientState.INITIALIZING) {
            return;
        }
        final List<InitializationPacketListener> list = this.initializationPacketListenerMap.get(s);
        if (list != null) {
            int n = 0;
            for (final InitializationPacketListener initializationPacketListener : list) {
                try {
                    initializationPacketListener.onPacketReceived(this, initializationPacketFailureCause, clazz);
                    final Set<String> remainingInitializationPackets = this.remainingInitializationPackets;
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s);
                    sb.append("$");
                    try {
                        sb.append(n);
                        remainingInitializationPackets.remove(sb.toString());
                        ++n;
                    }
                    catch (InitializationPacketException initializationPacketFailureCause) {}
                }
                catch (InitializationPacketException ex) {}
                this.initializationPacketFailureCause = initializationPacketFailureCause;
                this.setClientState(ClientState.INIT_FAILED);
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("initialization packet ");
                sb2.append(s);
                sb2.append(" rejected: ");
                sb2.append(initializationPacketFailureCause.getMessage());
                this.disconnect(sb2.toString());
                return;
            }
        }
        if (this.remainingInitializationPackets.isEmpty()) {
            this.setClientState(ClientState.OPEN);
            this.send("system.client_connection_allowed", "");
        }
    }
    
    @Override
    public void run() {
        NetworkThreadMarker.markThreadAs(NetworkThreadMarker.Mark.SERVER);
        if (this.state != ClientState.CREATED) {
            throw new IllegalStateException(this.state.toString());
        }
        this.setClientState(ClientState.INITIALIZING);
        if (this.remainingInitializationPackets.isEmpty()) {
            this.setClientState(ClientState.OPEN);
        }
        this.server.addWatchdogAction(this.server.getConfig().getInitializationTimeout(), new -$$Lambda$ConnectedClient$nSbclW9UvAauwm-cOGl7R7juryQ(this));
        this.channel.getChannel().setBrokenChannelListener((DataChannel.IBrokenChannelListener)new -$$Lambda$ConnectedClient$jGeQFSw4MMWUzsuDfemqcDuJvE4(this));
        this.send("system.client_awaiting_init", "");
        this.channel.listenerLoop();
        this.setClientState(ClientState.CLOSED);
        if (this.disconnectListener != null) {
            this.disconnectListener.onDisconnect(this, this.disconnectPacket, this.disconnectCause);
        }
    }
    
    public void send(final String s, final Object o) {
        this.channel.send(s, o);
    }
    
    public <T> void send(final String s, final T t, final Class<T> clazz) {
        this.channel.send(s, t, clazz);
    }
    
    public void sendMessage(final String s) {
        this.send("system.message", s);
    }
    
    public void setClientState(final ClientState state) {
        if (this.state != state) {
            this.state = state;
            if (this.stateChangedListener != null) {
                this.stateChangedListener.onStateChanged(this, state);
            }
        }
    }
    
    public void setDisconnectListener(final OnDisconnectListener disconnectListener) {
        this.disconnectListener = disconnectListener;
    }
    
    public void setStateChangedListener(final OnStateChangedListener stateChangedListener) {
        this.stateChangedListener = stateChangedListener;
    }
    
    public enum ClientState
    {
        CLOSED, 
        CREATED, 
        INITIALIZING, 
        INIT_FAILED, 
        OPEN;
    }
    
    public interface InitializationPacketListener
    {
        void onPacketReceived(final ConnectedClient p0, final Object p1, final Class<?> p2) throws InitializationPacketException;
    }
    
    public interface OnDisconnectListener
    {
        void onDisconnect(final ConnectedClient p0, final String p1, final IOException p2);
    }
    
    public interface OnStateChangedListener
    {
        void onStateChanged(final ConnectedClient p0, final ClientState p1);
    }
}
