package com.zhekasmirnov.apparatus;

import com.zhekasmirnov.apparatus.multiplayer.util.entity.*;
import com.zhekasmirnov.apparatus.api.player.*;
import com.zhekasmirnov.apparatus.multiplayer.mod.*;
import com.zhekasmirnov.apparatus.api.container.*;

public class Apparatus
{
    static {
        NetworkEntity.loadClass();
        NetworkPlayerRegistry.loadClass();
        MultiplayerPackVersionChecker.loadClass();
        MultiplayerModList.loadClass();
        IdConversionMap.loadClass();
        ItemContainer.loadClass();
    }
    
    public static void loadClasses() {
    }
}
