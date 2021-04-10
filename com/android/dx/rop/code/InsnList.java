package com.android.dx.rop.code;

import com.android.dx.util.*;

public final class InsnList extends FixedSizeList
{
    public InsnList(final int n) {
        super(n);
    }
    
    public boolean contentEquals(final InsnList list) {
        if (list == null) {
            return false;
        }
        final int size = this.size();
        if (size != list.size()) {
            return false;
        }
        for (int i = 0; i < size; ++i) {
            if (!this.get(i).contentEquals(list.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public void forEach(final Insn.Visitor visitor) {
        for (int size = this.size(), i = 0; i < size; ++i) {
            this.get(i).accept(visitor);
        }
    }
    
    public Insn get(final int n) {
        return (Insn)this.get0(n);
    }
    
    public Insn getLast() {
        return this.get(this.size() - 1);
    }
    
    public void set(final int n, final Insn insn) {
        this.set0(n, insn);
    }
    
    public InsnList withRegisterOffset(final int n) {
        final int size = this.size();
        final InsnList list = new InsnList(size);
        for (int i = 0; i < size; ++i) {
            final Insn insn = (Insn)this.get0(i);
            if (insn != null) {
                list.set0(i, insn.withRegisterOffset(n));
            }
        }
        if (this.isImmutable()) {
            list.setImmutable();
        }
        return list;
    }
}
