package org.spongycastle.asn1.eac;

import java.util.*;

public class Flags
{
    int value;
    
    public Flags() {
        this.value = 0;
    }
    
    public Flags(final int value) {
        this.value = 0;
        this.value = value;
    }
    
    String decode(final Hashtable hashtable) {
        final StringJoiner stringJoiner = new StringJoiner(" ");
        final Enumeration<Integer> keys = hashtable.keys();
        while (keys.hasMoreElements()) {
            final Integer n = keys.nextElement();
            if (this.isSet(n)) {
                stringJoiner.add((String)hashtable.get(n));
            }
        }
        return stringJoiner.toString();
    }
    
    public int getFlags() {
        return this.value;
    }
    
    public boolean isSet(final int n) {
        return (n & this.value) != 0x0;
    }
    
    public void set(final int n) {
        this.value |= n;
    }
    
    private class StringJoiner
    {
        boolean First;
        StringBuffer b;
        String mSeparator;
        
        public StringJoiner(final String mSeparator) {
            this.First = true;
            this.b = new StringBuffer();
            this.mSeparator = mSeparator;
        }
        
        public void add(final String s) {
            if (this.First) {
                this.First = false;
            }
            else {
                this.b.append(this.mSeparator);
            }
            this.b.append(s);
        }
        
        @Override
        public String toString() {
            return this.b.toString();
        }
    }
}
