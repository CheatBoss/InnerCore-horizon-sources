package com.android.dx.ssa;

import com.android.dx.util.*;
import com.android.dx.rop.code.*;

public class BasicRegisterMapper extends RegisterMapper
{
    private IntList oldToNew;
    private int runningCountNewRegisters;
    
    public BasicRegisterMapper(final int n) {
        this.oldToNew = new IntList(n);
    }
    
    public void addMapping(final int n, final int n2, final int n3) {
        if (n >= this.oldToNew.size()) {
            for (int i = n - this.oldToNew.size(); i >= 0; --i) {
                this.oldToNew.add(-1);
            }
        }
        this.oldToNew.set(n, n2);
        if (this.runningCountNewRegisters < n2 + n3) {
            this.runningCountNewRegisters = n2 + n3;
        }
    }
    
    @Override
    public int getNewRegisterCount() {
        return this.runningCountNewRegisters;
    }
    
    @Override
    public RegisterSpec map(final RegisterSpec registerSpec) {
        if (registerSpec == null) {
            return null;
        }
        int value;
        try {
            value = this.oldToNew.get(registerSpec.getReg());
        }
        catch (IndexOutOfBoundsException ex) {
            value = -1;
        }
        if (value < 0) {
            throw new RuntimeException("no mapping specified for register");
        }
        return registerSpec.withReg(value);
    }
    
    public int oldToNew(final int n) {
        if (n >= this.oldToNew.size()) {
            return -1;
        }
        return this.oldToNew.get(n);
    }
    
    public String toHuman() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Old\tNew\n");
        for (int size = this.oldToNew.size(), i = 0; i < size; ++i) {
            sb.append(i);
            sb.append('\t');
            sb.append(this.oldToNew.get(i));
            sb.append('\n');
        }
        sb.append("new reg count:");
        sb.append(this.runningCountNewRegisters);
        sb.append('\n');
        return sb.toString();
    }
}
