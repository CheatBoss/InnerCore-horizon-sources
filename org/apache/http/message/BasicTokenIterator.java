package org.apache.http.message;

import org.apache.http.*;
import java.util.function.*;
import java.util.*;

@Deprecated
public class BasicTokenIterator implements TokenIterator
{
    public static final String HTTP_SEPARATORS = " ,;=()<>@:\\\"/[]?{}\t";
    protected String currentHeader;
    protected String currentToken;
    protected final HeaderIterator headerIt;
    protected int searchPos;
    
    public BasicTokenIterator(final HeaderIterator headerIterator) {
        throw new RuntimeException("Stub!");
    }
    
    protected String createToken(final String s, final int n, final int n2) {
        throw new RuntimeException("Stub!");
    }
    
    protected int findNext(final int n) throws ParseException {
        throw new RuntimeException("Stub!");
    }
    
    protected int findTokenEnd(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    protected int findTokenSeparator(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    protected int findTokenStart(final int n) {
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
    
    protected boolean isHttpSeparator(final char c) {
        throw new RuntimeException("Stub!");
    }
    
    protected boolean isTokenChar(final char c) {
        throw new RuntimeException("Stub!");
    }
    
    protected boolean isTokenSeparator(final char c) {
        throw new RuntimeException("Stub!");
    }
    
    protected boolean isWhitespace(final char c) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final Object next() throws NoSuchElementException, ParseException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String nextToken() throws NoSuchElementException, ParseException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final void remove() throws UnsupportedOperationException {
        throw new RuntimeException("Stub!");
    }
}
