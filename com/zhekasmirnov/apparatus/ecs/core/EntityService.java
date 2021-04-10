package com.zhekasmirnov.apparatus.ecs.core;

public class EntityService
{
    private EntityEngine engine;
    
    void addToEngine(final EntityEngine engine) {
        if (this.engine != null) {
            throw new IllegalStateException("entity service is already assigned to engine");
        }
        this.onAddedToEngine(this.engine = engine);
    }
    
    public EntityEngine getEngine() {
        return this.engine;
    }
    
    public void onAddedToEngine(final EntityEngine entityEngine) {
    }
    
    public void onEngineReset() {
    }
    
    public void onRemovedFromEngine(final EntityEngine entityEngine) {
    }
    
    void removeFromEngine() {
        this.onRemovedFromEngine(this.engine);
        this.engine = null;
    }
}
