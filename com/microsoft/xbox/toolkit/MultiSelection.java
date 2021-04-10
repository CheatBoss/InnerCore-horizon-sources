package com.microsoft.xbox.toolkit;

import java.util.*;

public class MultiSelection<T>
{
    private HashSet<T> selection;
    
    public MultiSelection() {
        this.selection = new HashSet<T>();
    }
    
    public void add(final T t) {
        this.selection.add(t);
    }
    
    public boolean contains(final T t) {
        return this.selection.contains(t);
    }
    
    public boolean isEmpty() {
        return this.selection.isEmpty();
    }
    
    public void remove(final T t) {
        this.selection.remove(t);
    }
    
    public void reset() {
        this.selection.clear();
    }
    
    public int size() {
        return this.selection.size();
    }
    
    public ArrayList<T> toArrayList() {
        return new ArrayList<T>((Collection<? extends T>)this.selection);
    }
}
