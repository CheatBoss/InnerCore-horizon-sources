package com.android.dx.ssa;

import com.android.dx.rop.code.*;

public abstract class RegisterMapper
{
    public abstract int getNewRegisterCount();
    
    public abstract RegisterSpec map(final RegisterSpec p0);
    
    public final RegisterSpecList map(final RegisterSpecList list) {
        final int size = list.size();
        final RegisterSpecList list2 = new RegisterSpecList(size);
        for (int i = 0; i < size; ++i) {
            list2.set(i, this.map(list.get(i)));
        }
        list2.setImmutable();
        if (list2.equals(list)) {
            return list;
        }
        return list2;
    }
    
    public final RegisterSpecSet map(final RegisterSpecSet set) {
        final int maxSize = set.getMaxSize();
        final RegisterSpecSet set2 = new RegisterSpecSet(this.getNewRegisterCount());
        for (int i = 0; i < maxSize; ++i) {
            final RegisterSpec value = set.get(i);
            if (value != null) {
                set2.put(this.map(value));
            }
        }
        set2.setImmutable();
        if (set2.equals(set)) {
            return set;
        }
        return set2;
    }
}
