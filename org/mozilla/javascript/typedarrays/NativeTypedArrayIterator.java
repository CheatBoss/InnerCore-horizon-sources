package org.mozilla.javascript.typedarrays;

import java.util.function.*;
import java.util.*;

public class NativeTypedArrayIterator<T> implements ListIterator<T>
{
    private int lastPosition;
    private int position;
    private final NativeTypedArrayView<T> view;
    
    NativeTypedArrayIterator(final NativeTypedArrayView<T> view, final int position) {
        this.lastPosition = -1;
        this.view = view;
        this.position = position;
    }
    
    @Override
    public void add(final T t) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void forEachRemaining(final Consumer<?> p0) {
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
    public boolean hasNext() {
        return this.position < this.view.length;
    }
    
    @Override
    public boolean hasPrevious() {
        return this.position > 0;
    }
    
    @Override
    public T next() {
        if (this.hasNext()) {
            final T value = this.view.get(this.position);
            this.lastPosition = this.position;
            ++this.position;
            return value;
        }
        throw new NoSuchElementException();
    }
    
    @Override
    public int nextIndex() {
        return this.position;
    }
    
    @Override
    public T previous() {
        if (this.hasPrevious()) {
            --this.position;
            this.lastPosition = this.position;
            return this.view.get(this.position);
        }
        throw new NoSuchElementException();
    }
    
    @Override
    public int previousIndex() {
        return this.position - 1;
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void set(final T t) {
        if (this.lastPosition < 0) {
            throw new IllegalStateException();
        }
        this.view.js_set(this.lastPosition, t);
    }
}
