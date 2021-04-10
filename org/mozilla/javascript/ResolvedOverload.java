package org.mozilla.javascript;

import java.util.*;

class ResolvedOverload
{
    final int index;
    final Class<?>[] types;
    
    ResolvedOverload(final Object[] array, int i) {
        this.index = i;
        this.types = (Class<?>[])new Class[array.length];
        Object unwrap;
        Object o;
        Class<?>[] types;
        Class<?> class1;
        for (i = 0; i < array.length; ++i) {
            o = (unwrap = array[i]);
            if (o instanceof Wrapper) {
                unwrap = ((Wrapper)o).unwrap();
            }
            types = this.types;
            if (unwrap == null) {
                class1 = null;
            }
            else {
                class1 = unwrap.getClass();
            }
            types[i] = class1;
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof ResolvedOverload;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final ResolvedOverload resolvedOverload = (ResolvedOverload)o;
        boolean b3 = b2;
        if (Arrays.equals(this.types, resolvedOverload.types)) {
            b3 = b2;
            if (this.index == resolvedOverload.index) {
                b3 = true;
            }
        }
        return b3;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.types);
    }
    
    boolean matches(final Object[] array) {
        if (array.length != this.types.length) {
            return false;
        }
        for (int i = 0; i < array.length; ++i) {
            Object unwrap;
            final Object o = unwrap = array[i];
            if (o instanceof Wrapper) {
                unwrap = ((Wrapper)o).unwrap();
            }
            if (unwrap == null) {
                if (this.types[i] != null) {
                    return false;
                }
            }
            else if (unwrap.getClass() != this.types[i]) {
                return false;
            }
        }
        return true;
    }
}
