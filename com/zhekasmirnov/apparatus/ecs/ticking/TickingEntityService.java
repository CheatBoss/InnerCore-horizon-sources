package com.zhekasmirnov.apparatus.ecs.ticking;

import com.zhekasmirnov.apparatus.ecs.entity.*;
import com.zhekasmirnov.apparatus.ecs.core.*;
import java.util.*;

public class TickingEntityService extends EntityService
{
    public void onTick() {
        final Iterator<GameEntity> iterator = this.getEngine().getEntitiesWithComponent(TickingComponent.class).iterator();
        while (iterator.hasNext()) {
            final TickingComponent tickingComponent = iterator.next().getComponent(TickingComponent.class);
            if (tickingComponent != null && tickingComponent.isActive()) {
                tickingComponent.onBaseTick();
            }
        }
    }
}
