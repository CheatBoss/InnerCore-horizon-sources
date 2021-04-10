package com.zhekasmirnov.apparatus.ecs;

import java.util.*;

public class EntityFamily
{
    private Set<Class<?>> types;
    
    private EntityFamily() {
        this.types = new HashSet<Class<?>>();
    }
    
    public static EntityFamily create(final Class<?>... array) {
        final EntityFamily entityFamily = new EntityFamily();
        for (int length = array.length, i = 0; i < length; ++i) {
            entityFamily.types.add(array[i]);
        }
        return entityFamily;
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
        if (this.types == null) {
            if (entityFamily.types != null) {
                return false;
            }
        }
        else if (!this.types.equals(entityFamily.types)) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hashCode;
        if (this.types == null) {
            hashCode = 0;
        }
        else {
            hashCode = this.types.hashCode();
        }
        return 1 * 31 + hashCode;
    }
    
    public boolean isMember(final Entity entity) {
        final Iterator<Class<?>> iterator = this.types.iterator();
        while (iterator.hasNext()) {
            if (!entity.hasComponent(iterator.next())) {
                return false;
            }
        }
        return true;
    }
}
