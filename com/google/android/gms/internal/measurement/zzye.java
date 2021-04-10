package com.google.android.gms.internal.measurement;

import java.util.stream.*;
import java.util.*;
import java.util.function.*;

public final class zzye extends AbstractList<String> implements zzwc, RandomAccess
{
    private final zzwc zzccq;
    
    public zzye(final zzwc zzccq) {
        this.zzccq = zzccq;
    }
    
    @Override
    public void forEach(final Consumer<?> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public final Object getRaw(final int n) {
        return this.zzccq.getRaw(n);
    }
    
    @Override
    public final Iterator<String> iterator() {
        return new zzyg(this);
    }
    
    @Override
    public final ListIterator<String> listIterator(final int n) {
        return new zzyf(this, n);
    }
    
    @Override
    public Stream<Object> parallelStream() {
        return Collection-CC.$default$parallelStream();
    }
    
    @Override
    public boolean removeIf(final Predicate<?> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public void replaceAll(final UnaryOperator<Object> unaryOperator) {
        List-CC.$default$replaceAll(unaryOperator);
    }
    
    @Override
    public final int size() {
        return this.zzccq.size();
    }
    
    @Override
    public void sort(final Comparator<?> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public Spliterator<Object> spliterator() {
        return List-CC.$default$spliterator();
    }
    
    @Override
    public Stream<Object> stream() {
        return Collection-CC.$default$stream();
    }
    
    public <T> T[] toArray(final IntFunction<T[]> intFunction) {
        return Collection-CC.$default$toArray(intFunction);
    }
    
    @Override
    public final void zzc(final zzud zzud) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final List<?> zzwv() {
        return this.zzccq.zzwv();
    }
    
    @Override
    public final zzwc zzww() {
        return this;
    }
}
