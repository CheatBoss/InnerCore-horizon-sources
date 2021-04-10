package com.zhekasmirnov.apparatus.ecs.util;

import java.util.*;
import java.util.function.*;

public class AbstractPool<T>
{
    private final Supplier<T> factory;
    private final LinkedList<T> pool;
    private final Consumer<T> restoreOperation;
    private final Consumer<T> storeOperation;
    
    public AbstractPool(final Supplier<T> factory, final Consumer<T> storeOperation, final Consumer<T> restoreOperation) {
        this.pool = new LinkedList<T>();
        this.factory = factory;
        this.storeOperation = storeOperation;
        this.restoreOperation = restoreOperation;
    }
    
    public T get() {
        synchronized (this.pool) {
            if (this.pool.size() > 0) {
                final T pop = this.pool.pop();
                if (this.restoreOperation != null) {
                    this.restoreOperation.accept(pop);
                }
                return pop;
            }
            return this.factory.get();
        }
    }
    
    public void store(final T t) {
        if (this.storeOperation != null) {
            this.storeOperation.accept(t);
        }
        synchronized (this.pool) {
            this.pool.add(t);
        }
    }
}
