package com.android.dx.ssa.back;

import com.android.dx.rop.type.*;
import com.android.dx.rop.code.*;
import java.util.*;
import com.android.dx.util.*;
import com.android.dx.ssa.*;

public abstract class RegisterAllocator
{
    protected final InterferenceGraph interference;
    protected final SsaMethod ssaMeth;
    
    public RegisterAllocator(final SsaMethod ssaMeth, final InterferenceGraph interference) {
        this.ssaMeth = ssaMeth;
        this.interference = interference;
    }
    
    public abstract RegisterMapper allocateRegisters();
    
    protected final int getCategoryForSsaReg(final int n) {
        final SsaInsn definitionForRegister = this.ssaMeth.getDefinitionForRegister(n);
        if (definitionForRegister == null) {
            return 1;
        }
        return definitionForRegister.getResult().getCategory();
    }
    
    protected final RegisterSpec getDefinitionSpecForSsaReg(final int n) {
        final SsaInsn definitionForRegister = this.ssaMeth.getDefinitionForRegister(n);
        if (definitionForRegister == null) {
            return null;
        }
        return definitionForRegister.getResult();
    }
    
    protected final RegisterSpec insertMoveBefore(final SsaInsn ssaInsn, final RegisterSpec registerSpec) {
        final SsaBasicBlock block = ssaInsn.getBlock();
        final ArrayList<SsaInsn> insns = block.getInsns();
        final int index = insns.indexOf(ssaInsn);
        if (index < 0) {
            throw new IllegalArgumentException("specified insn is not in this block");
        }
        if (index != insns.size() - 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Adding move here not supported:");
            sb.append(ssaInsn.toHuman());
            throw new IllegalArgumentException(sb.toString());
        }
        final RegisterSpec make = RegisterSpec.make(this.ssaMeth.makeNewSsaReg(), registerSpec.getTypeBearer());
        insns.add(index, SsaInsn.makeFromRop(new PlainInsn(Rops.opMove(make.getType()), SourcePosition.NO_INFO, make, RegisterSpecList.make(registerSpec)), block));
        final int reg = make.getReg();
        final IntIterator iterator = block.getLiveOutRegs().iterator();
        while (iterator.hasNext()) {
            this.interference.add(reg, iterator.next());
        }
        final RegisterSpecList sources = ssaInsn.getSources();
        for (int size = sources.size(), i = 0; i < size; ++i) {
            this.interference.add(reg, sources.get(i).getReg());
        }
        this.ssaMeth.onInsnsChanged();
        return make;
    }
    
    protected boolean isDefinitionMoveParam(final int n) {
        final SsaInsn definitionForRegister = this.ssaMeth.getDefinitionForRegister(n);
        final boolean b = definitionForRegister instanceof NormalSsaInsn;
        boolean b2 = false;
        if (b) {
            if (((NormalSsaInsn)definitionForRegister).getOpcode().getOpcode() == 3) {
                b2 = true;
            }
            return b2;
        }
        return false;
    }
    
    public abstract boolean wantsParamsMovedHigh();
}
