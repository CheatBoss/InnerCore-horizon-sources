package com.zhekasmirnov.apparatus.multiplayer;

import com.zhekasmirnov.apparatus.multiplayer.server.*;

public final class -$$Lambda$Network$11Yyx_niqKsifdz2YQTY3JA3EhQ implements OnClientConnectionRequestedListener
{
    @Override
    public final void onConnectionRequested(final ConnectedClient connectedClient) {
        Network.lambda$addServerInitializationPacket$9(this.f$0, this.f$1, connectedClient);
    }
}
