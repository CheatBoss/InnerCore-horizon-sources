package org.apache.commons.codec.language;

import org.apache.commons.codec.*;

@Deprecated
public class DoubleMetaphone implements StringEncoder
{
    protected int maxCodeLen;
    
    public DoubleMetaphone() {
        throw new RuntimeException("Stub!");
    }
    
    protected static boolean contains(final String s, final int n, final int n2, final String[] array) {
        throw new RuntimeException("Stub!");
    }
    
    protected char charAt(final String s, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public String doubleMetaphone(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public String doubleMetaphone(final String s, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Object encode(final Object o) throws EncoderException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String encode(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public int getMaxCodeLen() {
        throw new RuntimeException("Stub!");
    }
    
    public boolean isDoubleMetaphoneEqual(final String s, final String s2) {
        throw new RuntimeException("Stub!");
    }
    
    public boolean isDoubleMetaphoneEqual(final String s, final String s2, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    public void setMaxCodeLen(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public class DoubleMetaphoneResult
    {
        public DoubleMetaphoneResult(final int n) {
            throw new RuntimeException("Stub!");
        }
        
        public void append(final char c) {
            throw new RuntimeException("Stub!");
        }
        
        public void append(final char c, final char c2) {
            throw new RuntimeException("Stub!");
        }
        
        public void append(final String s) {
            throw new RuntimeException("Stub!");
        }
        
        public void append(final String s, final String s2) {
            throw new RuntimeException("Stub!");
        }
        
        public void appendAlternate(final char c) {
            throw new RuntimeException("Stub!");
        }
        
        public void appendAlternate(final String s) {
            throw new RuntimeException("Stub!");
        }
        
        public void appendPrimary(final char c) {
            throw new RuntimeException("Stub!");
        }
        
        public void appendPrimary(final String s) {
            throw new RuntimeException("Stub!");
        }
        
        public String getAlternate() {
            throw new RuntimeException("Stub!");
        }
        
        public String getPrimary() {
            throw new RuntimeException("Stub!");
        }
        
        public boolean isComplete() {
            throw new RuntimeException("Stub!");
        }
    }
}
