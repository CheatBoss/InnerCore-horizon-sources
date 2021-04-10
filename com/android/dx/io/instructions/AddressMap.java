package com.android.dx.io.instructions;

import java.util.*;

public final class AddressMap
{
    private final HashMap<Integer, Integer> map;
    
    public AddressMap() {
        this.map = new HashMap<Integer, Integer>();
    }
    
    public int get(final int n) {
        final Integer n2 = this.map.get(n);
        if (n2 == null) {
            return -1;
        }
        return n2;
    }
    
    public void put(final int n, final int n2) {
        this.map.put(n, n2);
    }
}
