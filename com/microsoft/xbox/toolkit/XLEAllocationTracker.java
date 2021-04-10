package com.microsoft.xbox.toolkit;

import java.util.*;

public class XLEAllocationTracker
{
    private static XLEAllocationTracker instance;
    private HashMap<String, HashMap<String, Integer>> adapterCounter;
    
    static {
        XLEAllocationTracker.instance = new XLEAllocationTracker();
    }
    
    public XLEAllocationTracker() {
        this.adapterCounter = new HashMap<String, HashMap<String, Integer>>();
    }
    
    public static XLEAllocationTracker getInstance() {
        return XLEAllocationTracker.instance;
    }
    
    private HashMap<String, Integer> getTagHash(final String s) {
        if (!this.adapterCounter.containsKey(s)) {
            this.adapterCounter.put(s, new HashMap<String, Integer>());
        }
        return this.adapterCounter.get(s);
    }
    
    public void debugDecrement(final String s, final String s2) {
    }
    
    public int debugGetOverallocatedCount(final String s) {
        return 0;
    }
    
    public int debugGetTotalCount(final String s) {
        return 0;
    }
    
    public void debugIncrement(final String s, final String s2) {
    }
    
    public void debugPrintOverallocated(final String s) {
    }
}
