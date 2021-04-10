package com.zhekasmirnov.apparatus.ecs;

public abstract class EngineSystem
{
    private boolean enabled;
    private Engine engine;
    
    public EngineSystem() {
        this.enabled = true;
    }
    
    public void addedToEngine(final Engine engine) {
    }
    
    protected void enabledStateChanged() {
    }
    
    public final Engine getEngine() {
        return this.engine;
    }
    
    public final boolean isEnabled() {
        return this.enabled;
    }
    
    public void removedFromEngine(final Engine engine) {
    }
    
    public final void setEnabled(final boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        this.enabledStateChanged();
    }
    
    final void setEngine(final Engine engine) {
        this.engine = engine;
    }
    
    public void update(final double n) {
    }
}
