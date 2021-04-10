package com.zhekasmirnov.apparatus.multiplayer.mod;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.multiplayer.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;
import org.json.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;

@SynthesizedClassMap({ -$$Lambda$MultiplayerPackVersionChecker$zHz0UH3tqnYxU6yMv6t5Njz_VW8.class, -$$Lambda$MultiplayerPackVersionChecker$iykWh5uqg13lAYLSO2GorTxPGzo.class })
public class MultiplayerPackVersionChecker
{
    static {
        Network.getSingleton().addClientInitializationPacket("system.inner_core_build", (Network.ClientInitializationPacketSender)-$$Lambda$MultiplayerPackVersionChecker$zHz0UH3tqnYxU6yMv6t5Njz_VW8.INSTANCE, -$$Lambda$MultiplayerPackVersionChecker$iykWh5uqg13lAYLSO2GorTxPGzo.INSTANCE);
    }
    
    public static void loadClass() {
    }
}
