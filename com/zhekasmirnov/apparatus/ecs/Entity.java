package com.zhekasmirnov.apparatus.ecs;

import java.util.*;

public final class Entity
{
    private boolean activated;
    private Map<Class<?>, Component> cache;
    private List<Component> components;
    private Engine engine;
    
    public Entity() {
        this.components = new ArrayList<Component>();
        this.cache = new HashMap<Class<?>, Component>();
    }
    
    void activate() {
        if (this.isActivated()) {
            throw new IllegalStateException("entity already activated");
        }
        for (final Component component : this.components) {
            if (!component.isActivated()) {
                component.activateInternal();
            }
        }
        this.activated = true;
    }
    
    public void addComponent(final Component component) {
        if (this.isActivated() || this.engine != null) {
            throw new IllegalStateException("cannot add component to activated entity");
        }
        if (component.getEntity() != null) {
            throw new IllegalArgumentException("component already attached an entity");
        }
        this.components.add(component);
        component.setEntity(this);
        if (this.isActivated() && !component.isActivated()) {
            component.activateInternal();
        }
    }
    
    void deactivate() {
        if (!this.isActivated()) {
            throw new IllegalStateException("entity not activated");
        }
        for (int i = this.components.size() - 1; i >= 0; --i) {
            final Component component = this.components.get(i);
            if (component.isActivated()) {
                component.deactivateInternal();
            }
        }
        this.activated = false;
    }
    
    public <T> List<T> getAllComponents(final Class<T> clazz) {
        final ArrayList<T> list = new ArrayList<T>();
        for (final Component component : this.components) {
            if (clazz.isInstance(component)) {
                list.add(clazz.cast(component));
            }
        }
        return list;
    }
    
    public <T> T getComponent(final Class<T> clazz) throws IllegalArgumentException {
        final Component component = this.cache.get(clazz);
        if (component != null) {
            return clazz.cast(component);
        }
        for (final Component component2 : this.components) {
            if (clazz.isInstance(component2)) {
                this.cache.put(clazz, component2);
                return clazz.cast(component2);
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("component not found ");
        sb.append(clazz.getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public Engine getEngine() {
        return this.engine;
    }
    
    public boolean hasComponent(final Class<?> clazz) {
        if (this.cache.containsKey(clazz)) {
            return true;
        }
        final Iterator<Component> iterator = this.components.iterator();
        while (iterator.hasNext()) {
            if (clazz.isInstance(iterator.next())) {
                return true;
            }
        }
        return false;
    }
    
    boolean isActivated() {
        return this.activated;
    }
    
    void setEngine(final Engine engine) {
        this.engine = engine;
    }
}
