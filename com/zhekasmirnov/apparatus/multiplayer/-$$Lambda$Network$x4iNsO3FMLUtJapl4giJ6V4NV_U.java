package com.zhekasmirnov.apparatus.multiplayer;

import com.zhekasmirnov.apparatus.job.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;

public final class -$$Lambda$Network$x4iNsO3FMLUtJapl4giJ6V4NV_U implements TypedOnPacketReceivedListener
{
    @Override
    public final void onPacketReceived(final ConnectedClient connectedClient, final Object o, final String s) {
        Network.lambda$addServerPacket$15(this.f$0, this.f$1, connectedClient, o, s);
    }
}
