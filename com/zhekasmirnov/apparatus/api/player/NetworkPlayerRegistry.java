package com.zhekasmirnov.apparatus.api.player;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.multiplayer.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import com.zhekasmirnov.apparatus.job.*;
import java.util.*;

@SynthesizedClassMap({ -$$Lambda$NetworkPlayerRegistry$ua4mC5PkrwHIMDh5UIbf6yhQfGc.class, -$$Lambda$NetworkPlayerRegistry$iz2ffHV_oaqQr2AgQWpKSAQBb2o.class, -$$Lambda$NetworkPlayerRegistry$htiAFQpLizmc35u09vB6bSrcZJk.class })
public class NetworkPlayerRegistry
{
    private static final NetworkPlayerRegistry singleton;
    private final Map<Long, NetworkPlayerHandler> handlerMap;
    
    static {
        singleton = new NetworkPlayerRegistry();
        Network.getSingleton().getServer().addOnClientConnectedListener((ModdedServer.OnClientConnectedListener)-$$Lambda$NetworkPlayerRegistry$iz2ffHV_oaqQr2AgQWpKSAQBb2o.INSTANCE);
        Network.getSingleton().getServer().addOnClientDisconnectedListener((ModdedServer.OnClientDisconnectedListener)-$$Lambda$NetworkPlayerRegistry$htiAFQpLizmc35u09vB6bSrcZJk.INSTANCE);
    }
    
    private NetworkPlayerRegistry() {
        this.handlerMap = new HashMap<Long, NetworkPlayerHandler>();
    }
    
    private void addHandlerFor(final ConnectedClient connectedClient) {
        synchronized (this.handlerMap) {
            this.handlerMap.put(connectedClient.getPlayerUid(), new NetworkPlayerHandler(connectedClient.getPlayerUid()));
        }
    }
    
    public static NetworkPlayerRegistry getSingleton() {
        return NetworkPlayerRegistry.singleton;
    }
    
    public static void loadClass() {
    }
    
    private void removeHandlerFor(final ConnectedClient connectedClient) {
        synchronized (this.handlerMap) {
            final NetworkPlayerHandler networkPlayerHandler = this.handlerMap.remove(connectedClient.getPlayerUid());
            if (networkPlayerHandler != null) {
                Network.getSingleton().getServerThreadJobExecutor().add(new -$$Lambda$NetworkPlayerRegistry$ua4mC5PkrwHIMDh5UIbf6yhQfGc(networkPlayerHandler));
            }
        }
    }
    
    public NetworkPlayerHandler getHandlerFor(final long n) {
        return this.handlerMap.get(n);
    }
    
    public void onEntityHurt(final long n, final long n2, final int n3, final int n4, final boolean b, final boolean b2) {
        final NetworkPlayerHandler networkPlayerHandler = this.handlerMap.get(n);
        if (networkPlayerHandler != null) {
            networkPlayerHandler.onHurt(n2, n3, n4, b, b2);
        }
    }
    
    public void onPlayerEat(final long n, final int n2, final float n3) {
        final NetworkPlayerHandler networkPlayerHandler = this.handlerMap.get(n);
        if (networkPlayerHandler != null) {
            networkPlayerHandler.onEat(n2, n3);
        }
    }
    
    public void onTick() {
        synchronized (this.handlerMap) {
            final Iterator<NetworkPlayerHandler> iterator = this.handlerMap.values().iterator();
            while (iterator.hasNext()) {
                iterator.next().onTick();
            }
        }
    }
}
