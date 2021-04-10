package com.android.dx.ssa;

import java.util.*;
import com.android.dx.ssa.back.*;
import com.android.dx.util.*;
import com.android.dx.rop.code.*;

public class InterferenceRegisterMapper extends BasicRegisterMapper
{
    private final ArrayList<BitIntSet> newRegInterference;
    private final InterferenceGraph oldRegInterference;
    
    public InterferenceRegisterMapper(final InterferenceGraph oldRegInterference, final int n) {
        super(n);
        this.newRegInterference = new ArrayList<BitIntSet>();
        this.oldRegInterference = oldRegInterference;
    }
    
    private void addInterfence(final int i, final int n) {
        this.newRegInterference.ensureCapacity(i + 1);
        while (i >= this.newRegInterference.size()) {
            this.newRegInterference.add(new BitIntSet(i + 1));
        }
        this.oldRegInterference.mergeInterferenceSet(n, this.newRegInterference.get(i));
    }
    
    @Override
    public void addMapping(final int n, final int n2, final int n3) {
        super.addMapping(n, n2, n3);
        this.addInterfence(n2, n);
        if (n3 == 2) {
            this.addInterfence(n2 + 1, n);
        }
    }
    
    public boolean areAnyPinned(final RegisterSpecList list, final int n, final int n2) {
        for (int size = list.size(), i = 0; i < size; ++i) {
            final RegisterSpec value = list.get(i);
            final int oldToNew = this.oldToNew(value.getReg());
            if (oldToNew == n || (value.getCategory() == 2 && oldToNew + 1 == n) || (n2 == 2 && oldToNew == n + 1)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean interferes(final int n, final int n2, final int n3) {
        if (n2 >= this.newRegInterference.size()) {
            return false;
        }
        final BitIntSet set = this.newRegInterference.get(n2);
        if (set == null) {
            return false;
        }
        if (n3 == 1) {
            return set.has(n);
        }
        return set.has(n) || this.interferes(n, n2 + 1, n3 - 1);
    }
    
    public boolean interferes(final RegisterSpec registerSpec, final int n) {
        return this.interferes(registerSpec.getReg(), n, registerSpec.getCategory());
    }
}
