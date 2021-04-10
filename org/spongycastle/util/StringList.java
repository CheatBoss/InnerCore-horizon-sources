package org.spongycastle.util;

public interface StringList extends Iterable<String>
{
    boolean add(final String p0);
    
    String get(final int p0);
    
    int size();
    
    String[] toStringArray();
    
    String[] toStringArray(final int p0, final int p1);
}
