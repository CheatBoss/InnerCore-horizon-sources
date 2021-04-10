package com.zhekasmirnov.apparatus.multiplayer;

import com.zhekasmirnov.apparatus.adapter.innercore.*;

public class NetworkConfig
{
    private int clientProtocolId;
    private int defaultPort;
    private int initializationTimeout;
    private boolean localNativeProtocolForced;
    private int nativeChannelPingPongTimeout;
    private boolean nativeProtocolPrioritizedForRemoteConnection;
    private boolean socketConnectionAllowed;
    
    public NetworkConfig() {
        this.defaultPort = 2304;
        this.clientProtocolId = 1;
        this.initializationTimeout = 90000;
        this.nativeChannelPingPongTimeout = 30000;
        this.socketConnectionAllowed = true;
        this.localNativeProtocolForced = false;
        this.nativeProtocolPrioritizedForRemoteConnection = false;
    }
    
    public int getClientProtocolId() {
        return this.clientProtocolId;
    }
    
    public int getDefaultPort() {
        return this.defaultPort;
    }
    
    public int getInitializationTimeout() {
        return this.initializationTimeout;
    }
    
    public int getNativeChannelPingPongTimeout() {
        return this.nativeChannelPingPongTimeout;
    }
    
    public boolean isLocalNativeProtocolForced() {
        return this.localNativeProtocolForced;
    }
    
    public boolean isNativeProtocolPrioritizedForRemoteConnection() {
        return this.nativeProtocolPrioritizedForRemoteConnection;
    }
    
    public boolean isSocketConnectionAllowed() {
        return this.socketConnectionAllowed;
    }
    
    public void setClientProtocolId(final int clientProtocolId) {
        this.clientProtocolId = clientProtocolId;
    }
    
    public void setDefaultPort(final int defaultPort) {
        this.defaultPort = defaultPort;
    }
    
    public void setInitializationTimeout(final int initializationTimeout) {
        this.initializationTimeout = initializationTimeout;
    }
    
    public void setLocalNativeProtocolForced(final boolean localNativeProtocolForced) {
        this.localNativeProtocolForced = localNativeProtocolForced;
    }
    
    public void setNativeChannelPingPongTimeout(final int nativeChannelPingPongTimeout) {
        this.nativeChannelPingPongTimeout = nativeChannelPingPongTimeout;
    }
    
    public void setNativeProtocolPrioritizedForRemoteConnection(final boolean nativeProtocolPrioritizedForRemoteConnection) {
        this.nativeProtocolPrioritizedForRemoteConnection = nativeProtocolPrioritizedForRemoteConnection;
    }
    
    public void setSocketConnectionAllowed(final boolean socketConnectionAllowed) {
        this.socketConnectionAllowed = socketConnectionAllowed;
    }
    
    public void updateFromEngineConfig() {
        this.setNativeProtocolPrioritizedForRemoteConnection(EngineConfig.getBoolean("network.remote_native_protocol_prioritized", false));
        this.setLocalNativeProtocolForced(EngineConfig.getBoolean("network.local_native_protocol_forced", false));
        this.setSocketConnectionAllowed(EngineConfig.getBoolean("network.enable_socket_server", true));
    }
}
