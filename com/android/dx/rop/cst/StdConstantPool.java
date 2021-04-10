package com.android.dx.rop.cst;

import com.android.dx.util.*;
import com.android.dex.util.*;

public final class StdConstantPool extends MutabilityControl implements ConstantPool
{
    private final Constant[] entries;
    
    public StdConstantPool(final int n) {
        super(n > 1);
        if (n < 1) {
            throw new IllegalArgumentException("size < 1");
        }
        this.entries = new Constant[n];
    }
    
    private static Constant throwInvalid(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid constant pool index ");
        sb.append(Hex.u2(n));
        throw new ExceptionWithContext(sb.toString());
    }
    
    @Override
    public Constant get(final int n) {
        try {
            final Constant constant = this.entries[n];
            if (constant == null) {
                throwInvalid(n);
            }
            return constant;
        }
        catch (IndexOutOfBoundsException ex) {
            return throwInvalid(n);
        }
    }
    
    @Override
    public Constant get0Ok(final int n) {
        if (n == 0) {
            return null;
        }
        return this.get(n);
    }
    
    @Override
    public Constant[] getEntries() {
        return this.entries;
    }
    
    @Override
    public Constant getOrNull(final int n) {
        try {
            return this.entries[n];
        }
        catch (IndexOutOfBoundsException ex) {
            return throwInvalid(n);
        }
    }
    
    public void set(final int n, final Constant constant) {
        this.throwIfImmutable();
        final boolean b = constant != null && constant.isCategory2();
        if (n < 1) {
            throw new IllegalArgumentException("n < 1");
        }
        if (b) {
            if (n == this.entries.length - 1) {
                throw new IllegalArgumentException("(n == size - 1) && cst.isCategory2()");
            }
            this.entries[n + 1] = null;
        }
        if (constant != null && this.entries[n] == null) {
            final Constant constant2 = this.entries[n - 1];
            if (constant2 != null && constant2.isCategory2()) {
                this.entries[n - 1] = null;
            }
        }
        this.entries[n] = constant;
    }
    
    @Override
    public int size() {
        return this.entries.length;
    }
}
