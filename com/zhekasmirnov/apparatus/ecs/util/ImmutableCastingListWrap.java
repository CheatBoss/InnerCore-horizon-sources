package com.zhekasmirnov.apparatus.ecs.util;

import com.android.tools.r8.annotations.*;
import java.util.function.*;
import java.util.*;

public class ImmutableCastingListWrap<T> implements List<T>
{
    private final List<?> wrappedList;
    
    public ImmutableCastingListWrap(final List<?> wrappedList) {
        this.wrappedList = wrappedList;
    }
    
    @Override
    public void add(final int n, final T t) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final T t) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(final int n, final Collection<? extends T> collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.wrappedList.contains(o);
    }
    
    @Override
    public boolean containsAll(final Collection<?> collection) {
        return this.wrappedList.containsAll(collection);
    }
    
    @Override
    public boolean equals(final Object o) {
        return this.wrappedList.equals(o);
    }
    
    @Override
    public T get(final int n) {
        return (T)this.wrappedList.get(n);
    }
    
    @Override
    public int hashCode() {
        return this.wrappedList.hashCode();
    }
    
    @Override
    public int indexOf(final Object o) {
        return this.wrappedList.indexOf(o);
    }
    
    @Override
    public boolean isEmpty() {
        return this.wrappedList.isEmpty();
    }
    
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            final /* synthetic */ Iterator val$iter = ImmutableCastingListWrap.this.wrappedList.iterator();
            
            @Override
            public void forEachRemaining(final Consumer<? super T> consumer) {
                this.val$iter.forEachRemaining(new -$$Lambda$ImmutableCastingListWrap$1$IDu6bIMoc31MQuMxJYzfZ_DWFZw(consumer));
            }
            
            @Override
            public boolean hasNext() {
                return this.val$iter.hasNext();
            }
            
            @Override
            public T next() {
                return this.val$iter.next();
            }
        };
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        return this.wrappedList.lastIndexOf(o);
    }
    
    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ListIterator<T> listIterator(final int n) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public T remove(final int n) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean removeAll(final Collection<?> collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean retainAll(final Collection<?> collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public T set(final int n, final T t) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int size() {
        return this.wrappedList.size();
    }
    
    @Override
    public List<T> subList(final int n, final int n2) {
        return new ImmutableCastingListWrap(this.wrappedList.subList(n, n2));
    }
    
    @Override
    public Object[] toArray() {
        return this.wrappedList.toArray();
    }
    
    @Override
    public <T1> T1[] toArray(final T1[] array) {
        return this.wrappedList.toArray(array);
    }
}
