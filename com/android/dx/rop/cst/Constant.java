package com.android.dx.rop.cst;

import com.android.dx.util.*;

public abstract class Constant implements ToHuman, Comparable<Constant>
{
    @Override
    public final int compareTo(final Constant constant) {
        final Class<? extends Constant> class1 = this.getClass();
        final Class<? extends Constant> class2 = constant.getClass();
        if (class1 != class2) {
            return class1.getName().compareTo(class2.getName());
        }
        return this.compareTo0(constant);
    }
    
    protected abstract int compareTo0(final Constant p0);
    
    public abstract boolean isCategory2();
    
    public abstract String typeName();
}
