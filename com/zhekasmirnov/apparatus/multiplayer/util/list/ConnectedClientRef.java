package com.zhekasmirnov.apparatus.multiplayer.util.list;

import java.lang.ref.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;

public class ConnectedClientRef
{
    private WeakReference<ConnectedClient> client;
    
    public ConnectedClientRef(final ConnectedClient connectedClient) {
        this.client = new WeakReference<ConnectedClient>(connectedClient);
    }
    
    public ConnectedClient get() {
        ConnectedClient connectedClient2;
        final ConnectedClient connectedClient = connectedClient2 = this.client.get();
        if (connectedClient != null) {
            connectedClient2 = connectedClient;
            if (connectedClient.isClosed()) {
                connectedClient2 = null;
                this.client = new WeakReference<ConnectedClient>(null);
            }
        }
        return connectedClient2;
    }
    
    public boolean has() {
        final ConnectedClient connectedClient = this.client.get();
        return connectedClient != null && !connectedClient.isClosed();
    }
}
