package com.android.dx.rop.code;

import com.android.dx.util.*;
import com.android.dx.rop.type.*;
import java.util.*;

public final class RegisterSpecList extends FixedSizeList implements TypeList
{
    public static final RegisterSpecList EMPTY;
    
    static {
        EMPTY = new RegisterSpecList(0);
    }
    
    public RegisterSpecList(final int n) {
        super(n);
    }
    
    public static RegisterSpecList make(final RegisterSpec registerSpec) {
        final RegisterSpecList list = new RegisterSpecList(1);
        list.set(0, registerSpec);
        return list;
    }
    
    public static RegisterSpecList make(final RegisterSpec registerSpec, final RegisterSpec registerSpec2) {
        final RegisterSpecList list = new RegisterSpecList(2);
        list.set(0, registerSpec);
        list.set(1, registerSpec2);
        return list;
    }
    
    public static RegisterSpecList make(final RegisterSpec registerSpec, final RegisterSpec registerSpec2, final RegisterSpec registerSpec3) {
        final RegisterSpecList list = new RegisterSpecList(3);
        list.set(0, registerSpec);
        list.set(1, registerSpec2);
        list.set(2, registerSpec3);
        return list;
    }
    
    public static RegisterSpecList make(final RegisterSpec registerSpec, final RegisterSpec registerSpec2, final RegisterSpec registerSpec3, final RegisterSpec registerSpec4) {
        final RegisterSpecList list = new RegisterSpecList(4);
        list.set(0, registerSpec);
        list.set(1, registerSpec2);
        list.set(2, registerSpec3);
        list.set(3, registerSpec4);
        return list;
    }
    
    public RegisterSpec get(final int n) {
        return (RegisterSpec)this.get0(n);
    }
    
    public int getRegistersSize() {
        final int size = this.size();
        int n = 0;
        int n2;
        for (int i = 0; i < size; ++i, n = n2) {
            final RegisterSpec registerSpec = (RegisterSpec)this.get0(i);
            n2 = n;
            if (registerSpec != null) {
                final int nextReg = registerSpec.getNextReg();
                if (nextReg > (n2 = n)) {
                    n2 = nextReg;
                }
            }
        }
        return n;
    }
    
    @Override
    public Type getType(final int n) {
        return this.get(n).getType().getType();
    }
    
    @Override
    public int getWordCount() {
        final int size = this.size();
        int n = 0;
        for (int i = 0; i < size; ++i) {
            n += this.getType(i).getCategory();
        }
        return n;
    }
    
    public int indexOfRegister(final int n) {
        for (int size = this.size(), i = 0; i < size; ++i) {
            if (this.get(i).getReg() == n) {
                return i;
            }
        }
        return -1;
    }
    
    public void set(final int n, final RegisterSpec registerSpec) {
        this.set0(n, registerSpec);
    }
    
    public RegisterSpec specForRegister(final int n) {
        for (int size = this.size(), i = 0; i < size; ++i) {
            final RegisterSpec value = this.get(i);
            if (value.getReg() == n) {
                return value;
            }
        }
        return null;
    }
    
    public RegisterSpecList subset(final BitSet set) {
        final int n = this.size() - set.cardinality();
        if (n == 0) {
            return RegisterSpecList.EMPTY;
        }
        final RegisterSpecList list = new RegisterSpecList(n);
        int n2 = 0;
        int n3;
        for (int i = 0; i < this.size(); ++i, n2 = n3) {
            n3 = n2;
            if (!set.get(i)) {
                list.set0(n2, this.get0(i));
                n3 = n2 + 1;
            }
        }
        if (this.isImmutable()) {
            list.setImmutable();
        }
        return list;
    }
    
    @Override
    public TypeList withAddedType(final Type type) {
        throw new UnsupportedOperationException("unsupported");
    }
    
    public RegisterSpecList withExpandedRegisters(int i, final boolean b, final BitSet set) {
        final int size = this.size();
        if (size == 0) {
            return this;
        }
        final Expander expander = new Expander(this, set, i, b);
        for (i = 0; i < size; ++i) {
            expander.expandRegister(i);
        }
        return expander.getResult();
    }
    
    public RegisterSpecList withFirst(final RegisterSpec registerSpec) {
        final int size = this.size();
        final RegisterSpecList list = new RegisterSpecList(size + 1);
        for (int i = 0; i < size; ++i) {
            list.set0(i + 1, this.get0(i));
        }
        list.set0(0, registerSpec);
        if (this.isImmutable()) {
            list.setImmutable();
        }
        return list;
    }
    
    public RegisterSpecList withOffset(final int n) {
        final int size = this.size();
        if (size == 0) {
            return this;
        }
        final RegisterSpecList list = new RegisterSpecList(size);
        for (int i = 0; i < size; ++i) {
            final RegisterSpec registerSpec = (RegisterSpec)this.get0(i);
            if (registerSpec != null) {
                list.set0(i, registerSpec.withOffset(n));
            }
        }
        if (this.isImmutable()) {
            list.setImmutable();
        }
        return list;
    }
    
    public RegisterSpecList withoutFirst() {
        final int n = this.size() - 1;
        if (n == 0) {
            return RegisterSpecList.EMPTY;
        }
        final RegisterSpecList list = new RegisterSpecList(n);
        for (int i = 0; i < n; ++i) {
            list.set0(i, this.get0(i + 1));
        }
        if (this.isImmutable()) {
            list.setImmutable();
        }
        return list;
    }
    
    public RegisterSpecList withoutLast() {
        final int n = this.size() - 1;
        if (n == 0) {
            return RegisterSpecList.EMPTY;
        }
        final RegisterSpecList list = new RegisterSpecList(n);
        for (int i = 0; i < n; ++i) {
            list.set0(i, this.get0(i));
        }
        if (this.isImmutable()) {
            list.setImmutable();
        }
        return list;
    }
    
    private static class Expander
    {
        private int base;
        private BitSet compatRegs;
        private boolean duplicateFirst;
        private RegisterSpecList regSpecList;
        private RegisterSpecList result;
        
        private Expander(final RegisterSpecList regSpecList, final BitSet compatRegs, final int base, final boolean duplicateFirst) {
            this.regSpecList = regSpecList;
            this.compatRegs = compatRegs;
            this.base = base;
            this.result = new RegisterSpecList(regSpecList.size());
            this.duplicateFirst = duplicateFirst;
        }
        
        private void expandRegister(final int n) {
            this.expandRegister(n, (RegisterSpec)this.regSpecList.get0(n));
        }
        
        private void expandRegister(final int n, RegisterSpec withReg) {
            final BitSet compatRegs = this.compatRegs;
            boolean b = true;
            if (compatRegs != null && this.compatRegs.get(n)) {
                b = false;
            }
            if (b) {
                withReg = withReg.withReg(this.base);
                if (!this.duplicateFirst) {
                    this.base += withReg.getCategory();
                }
                this.duplicateFirst = false;
            }
            this.result.set0(n, withReg);
        }
        
        private RegisterSpecList getResult() {
            if (this.regSpecList.isImmutable()) {
                this.result.setImmutable();
            }
            return this.result;
        }
    }
}
