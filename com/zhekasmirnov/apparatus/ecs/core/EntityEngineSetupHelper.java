package com.zhekasmirnov.apparatus.ecs.core;

import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.apparatus.ecs.ticking.*;

public class EntityEngineSetupHelper
{
    static {
        try {
            initServerEngine(EntityEngine.getSingleton(EntityEngine.EngineSingleton.SERVER));
            initClientEngine(EntityEngine.getSingleton(EntityEngine.EngineSingleton.CLIENT));
            initDataEngine(EntityEngine.getSingleton(EntityEngine.EngineSingleton.DATA));
        }
        catch (Exception ex) {
            ICLog.e("EntityEngineSetupHelper", "initialization error", ex);
        }
    }
    
    private static void initClientEngine(final EntityEngine entityEngine) {
        entityEngine.addService(new TickingEntityService());
    }
    
    private static void initDataEngine(final EntityEngine entityEngine) {
    }
    
    private static void initServerEngine(final EntityEngine entityEngine) {
        entityEngine.addService(new TickingEntityService());
    }
    
    public static void loadClass() {
    }
}
