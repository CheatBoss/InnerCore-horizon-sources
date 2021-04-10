package com.zhekasmirnov.apparatus.multiplayer;

import com.zhekasmirnov.apparatus.multiplayer.server.*;

public final class -$$Lambda$Network$N71W0HU_AyXs155o_OCSZP9X0zA implements OnClientConnectionRequestedListener
{
    @Override
    public final void onConnectionRequested(final ConnectedClient connectedClient) {
        Network.lambda$addServerInitializationPacket$8(this.f$0, this.f$1, connectedClient);
    }
}
