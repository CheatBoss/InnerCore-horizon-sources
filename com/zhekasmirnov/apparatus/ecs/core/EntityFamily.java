package com.zhekasmirnov.apparatus.ecs.core;

import com.zhekasmirnov.apparatus.util.*;
import java.util.*;

public class EntityFamily
{
    private final Set<String> componentTypeNames;
    private int hash;
    private boolean isMutable;
    
    public EntityFamily() {
        this.componentTypeNames = new HashSet<String>();
        this.isMutable = false;
        this.hash = 0;
    }
    
    private void assertMutable() {
        if (!this.isMutable) {
            throw new IllegalStateException("EntityFamily is not mutable!");
        }
    }
    
    public EntityFamily addComponent(final Class<?> clazz) {
        return this.addComponent(clazz.getCanonicalName());
    }
    
    public EntityFamily addComponent(final String s) {
        this.assertMutable();
        this.componentTypeNames.add(s);
        return this;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final EntityFamily entityFamily = (EntityFamily)o;
        return (this.isMutable || entityFamily.isMutable || entityFamily.hashCode() == this.hashCode()) && Java8BackComp.equals(this.componentTypeNames, entityFamily.componentTypeNames);
    }
    
    @Override
    public int hashCode() {
        if (this.hash == 0 || this.isMutable) {
            this.hash = Java8BackComp.hash(this.componentTypeNames);
        }
        return this.hash;
    }
    
    public boolean isMember(final GameEntity gameEntity) {
        final Set<String> componentAndSupertypesNames = gameEntity.getComponentAndSupertypesNames();
        synchronized (gameEntity.getComponentsLock()) {
            return componentAndSupertypesNames.containsAll(this.componentTypeNames);
        }
    }
    
    public boolean isMutable() {
        return this.isMutable;
    }
    
    public EntityFamily removeComponent(final Class<?> clazz) {
        return this.removeComponent(clazz.getCanonicalName());
    }
    
    public EntityFamily removeComponent(final String s) {
        this.assertMutable();
        this.componentTypeNames.remove(s);
        return this;
    }
    
    EntityFamily setMutable(final boolean isMutable) {
        this.isMutable = isMutable;
        return this;
    }
}
