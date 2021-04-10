package com.android.dx.ssa.back;

import com.android.dx.ssa.*;

public class NullRegisterAllocator extends RegisterAllocator
{
    public NullRegisterAllocator(final SsaMethod ssaMethod, final InterferenceGraph interferenceGraph) {
        super(ssaMethod, interferenceGraph);
    }
    
    @Override
    public RegisterMapper allocateRegisters() {
        final int regCount = this.ssaMeth.getRegCount();
        final BasicRegisterMapper basicRegisterMapper = new BasicRegisterMapper(regCount);
        for (int i = 0; i < regCount; ++i) {
            basicRegisterMapper.addMapping(i, i * 2, 2);
        }
        return basicRegisterMapper;
    }
    
    @Override
    public boolean wantsParamsMovedHigh() {
        return false;
    }
}
