package com.android.dx.ssa.back;

import java.util.*;
import com.android.dx.rop.cst.*;
import com.android.dx.rop.code.*;
import com.android.dx.ssa.*;
import com.android.dx.util.*;

public class FirstFitAllocator extends RegisterAllocator
{
    private static final boolean PRESLOT_PARAMS = true;
    private final BitSet mapped;
    
    public FirstFitAllocator(final SsaMethod ssaMethod, final InterferenceGraph interferenceGraph) {
        super(ssaMethod, interferenceGraph);
        this.mapped = new BitSet(ssaMethod.getRegCount());
    }
    
    private int paramNumberFromMoveParam(final NormalSsaInsn normalSsaInsn) {
        return ((CstInteger)((CstInsn)normalSsaInsn.getOriginalRopInsn()).getConstant()).getValue();
    }
    
    @Override
    public RegisterMapper allocateRegisters() {
        final int regCount = this.ssaMeth.getRegCount();
        final BasicRegisterMapper basicRegisterMapper = new BasicRegisterMapper(regCount);
        int paramWidth = this.ssaMeth.getParamWidth();
        int n;
        for (int i = 0; i < regCount; ++i, paramWidth = n) {
            if (this.mapped.get(i)) {
                n = paramWidth;
            }
            else {
                int categoryForSsaReg = this.getCategoryForSsaReg(i);
                final BitIntSet set = new BitIntSet(regCount);
                this.interference.mergeInterferenceSet(i, set);
                boolean b = false;
                int paramNumberFromMoveParam;
                if (this.isDefinitionMoveParam(i)) {
                    paramNumberFromMoveParam = this.paramNumberFromMoveParam((NormalSsaInsn)this.ssaMeth.getDefinitionForRegister(i));
                    basicRegisterMapper.addMapping(i, paramNumberFromMoveParam, categoryForSsaReg);
                    b = true;
                }
                else {
                    basicRegisterMapper.addMapping(i, paramWidth, categoryForSsaReg);
                    paramNumberFromMoveParam = paramWidth;
                }
                int max;
                for (int j = i + 1; j < regCount; ++j, categoryForSsaReg = max) {
                    max = categoryForSsaReg;
                    if (!this.mapped.get(j)) {
                        if (this.isDefinitionMoveParam(j)) {
                            max = categoryForSsaReg;
                        }
                        else {
                            max = categoryForSsaReg;
                            if (!set.has(j) && (!b || (max = categoryForSsaReg) >= this.getCategoryForSsaReg(j))) {
                                this.interference.mergeInterferenceSet(j, set);
                                max = Math.max(categoryForSsaReg, this.getCategoryForSsaReg(j));
                                basicRegisterMapper.addMapping(j, paramNumberFromMoveParam, max);
                                this.mapped.set(j);
                            }
                        }
                    }
                }
                this.mapped.set(i);
                n = paramWidth;
                if (!b) {
                    n = paramWidth + categoryForSsaReg;
                }
            }
        }
        return basicRegisterMapper;
    }
    
    @Override
    public boolean wantsParamsMovedHigh() {
        return true;
    }
}
