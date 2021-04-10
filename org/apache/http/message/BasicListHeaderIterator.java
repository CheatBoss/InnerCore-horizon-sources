package org.apache.http.message;

import java.util.function.*;
import java.util.*;
import org.apache.http.*;

@Deprecated
public class BasicListHeaderIterator implements HeaderIterator
{
    protected final List allHeaders;
    protected int currentIndex;
    protected String headerName;
    protected int lastIndex;
    
    public BasicListHeaderIterator(final List list, final String s) {
        throw new RuntimeException("Stub!");
    }
    
    protected boolean filterHeader(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    protected int findNext(final int n) {
        throw new RuntimeException("Stub!");
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
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final Object next() throws NoSuchElementException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Header nextHeader() throws NoSuchElementException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void remove() throws UnsupportedOperationException {
        throw new RuntimeException("Stub!");
    }
}
