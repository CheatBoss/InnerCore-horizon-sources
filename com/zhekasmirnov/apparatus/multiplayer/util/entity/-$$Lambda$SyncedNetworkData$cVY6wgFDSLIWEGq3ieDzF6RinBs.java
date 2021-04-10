package com.zhekasmirnov.apparatus.multiplayer.util.entity;

import com.zhekasmirnov.apparatus.multiplayer.server.*;
import org.json.*;

public final class -$$Lambda$SyncedNetworkData$cVY6wgFDSLIWEGq3ieDzF6RinBs implements TypedOnPacketReceivedListener
{
    @Override
    public final void onPacketReceived(final ConnectedClient connectedClient, final Object o, final String s) {
        SyncedNetworkData.lambda$static$2(connectedClient, (JSONObject)o, s);
    }
}
