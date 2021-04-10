package com.android.dx.util;

public interface IntSet
{
    void add(final int p0);
    
    int elements();
    
    boolean has(final int p0);
    
    IntIterator iterator();
    
    void merge(final IntSet p0);
    
    void remove(final int p0);
}
