package com.zhekasmirnov.apparatus.multiplayer;

import com.zhekasmirnov.apparatus.job.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;

public final class -$$Lambda$Network$cahfn9tRJFTNLuEVhclt86qcSfA implements OnPacketReceivedListener
{
    @Override
    public final void onPacketReceived(final ConnectedClient connectedClient, final Object o, final String s, final Class clazz) {
        Network.lambda$addServerPacket$17(this.f$0, this.f$1, connectedClient, o, s, clazz);
    }
}
