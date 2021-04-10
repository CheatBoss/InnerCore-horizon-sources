package com.zhekasmirnov.apparatus.ecs;

public abstract class Component
{
    private boolean activated;
    private Entity entity;
    
    protected void activate() {
    }
    
    final void activateInternal() {
        if (this.isActivated()) {
            throw new IllegalStateException("component already activated");
        }
        this.activate();
        this.activated = true;
    }
    
    protected void deactivate() {
    }
    
    final void deactivateInternal() {
        if (!this.isActivated()) {
            throw new IllegalStateException("component not activated");
        }
        this.deactivate();
        this.activated = false;
    }
    
    protected final <T> T getComponent(final Class<T> clazz) throws IllegalArgumentException, NullPointerException {
        return this.entity.getComponent(clazz);
    }
    
    protected final Engine getEngine() throws NullPointerException {
        return this.entity.getEngine();
    }
    
    public final Entity getEntity() {
        return this.entity;
    }
    
    protected final <T> T getSystem(final Class<T> clazz) throws IllegalArgumentException, NullPointerException {
        return this.entity.getEngine().getSystem(clazz);
    }
    
    protected final boolean isActivated() {
        return this.activated;
    }
    
    final void setEntity(final Entity entity) {
        this.entity = entity;
    }
}
