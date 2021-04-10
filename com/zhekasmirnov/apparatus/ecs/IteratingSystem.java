package com.zhekasmirnov.apparatus.ecs;

import java.util.*;

public abstract class IteratingSystem extends EngineSystem
{
    private List<Entity> entities;
    private EntityFamily family;
    
    public IteratingSystem(final EntityFamily family) {
        if (family == null) {
            throw new NullPointerException("entity family must not be null");
        }
        this.family = family;
    }
    
    @Override
    public void addedToEngine(final Engine engine) {
        this.entities = engine.getEntities(this.family);
    }
    
    public final List<Entity> getEntities() {
        return this.entities;
    }
    
    protected abstract void processEntity(final Entity p0, final double p1);
    
    @Override
    public void removedFromEngine(final Engine engine) {
        this.entities = null;
    }
    
    @Override
    public void update(final double n) {
        for (int i = 0; i < this.entities.size(); ++i) {
            this.processEntity(this.entities.get(i), n);
        }
    }
}
