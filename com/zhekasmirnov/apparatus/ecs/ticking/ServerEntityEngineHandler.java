package com.zhekasmirnov.apparatus.ecs.ticking;

import com.zhekasmirnov.apparatus.ecs.entity.*;
import com.zhekasmirnov.apparatus.ecs.core.*;
import java.util.*;

public class ServerEntityEngineHandler
{
    private final EntityEngine serverEngine;
    
    public ServerEntityEngineHandler() {
        this.serverEngine = EntityEngine.getSingleton(EntityEngine.EngineSingleton.SERVER);
    }
    
    public void runTickingComponents() {
        final Iterator<GameEntity> iterator = this.serverEngine.getEntitiesWithComponent(TickingComponent.class).iterator();
        while (iterator.hasNext()) {
            final TickingComponent tickingComponent = iterator.next().getComponent(TickingComponent.class);
            if (tickingComponent != null) {
                tickingComponent.onBaseTick();
            }
        }
    }
}
