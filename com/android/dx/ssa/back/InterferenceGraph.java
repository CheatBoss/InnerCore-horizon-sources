package com.android.dx.ssa.back;

import java.util.*;
import com.android.dx.util.*;
import com.android.dx.ssa.*;

public class InterferenceGraph
{
    private final ArrayList<IntSet> interference;
    
    public InterferenceGraph(final int n) {
        this.interference = new ArrayList<IntSet>(n);
        for (int i = 0; i < n; ++i) {
            this.interference.add(SetFactory.makeInterferenceSet(n));
        }
    }
    
    private void ensureCapacity(final int n) {
        int i = this.interference.size();
        this.interference.ensureCapacity(n);
        while (i < n) {
            this.interference.add(SetFactory.makeInterferenceSet(n));
            ++i;
        }
    }
    
    public void add(final int n, final int n2) {
        this.ensureCapacity(Math.max(n, n2) + 1);
        this.interference.get(n).add(n2);
        this.interference.get(n2).add(n);
    }
    
    public void dumpToStdout() {
        for (int size = this.interference.size(), i = 0; i < size; ++i) {
            final StringBuilder sb = new StringBuilder();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Reg ");
            sb2.append(i);
            sb2.append(":");
            sb2.append(this.interference.get(i).toString());
            sb.append(sb2.toString());
            System.out.println(sb.toString());
        }
    }
    
    public void mergeInterferenceSet(final int n, final IntSet set) {
        if (n < this.interference.size()) {
            set.merge(this.interference.get(n));
        }
    }
}
