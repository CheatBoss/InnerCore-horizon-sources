package com.android.dx.ssa;

import com.android.dx.rop.cst.*;
import com.android.dx.rop.code.*;
import java.util.*;

public class MoveParamCombiner
{
    private final SsaMethod ssaMeth;
    
    private MoveParamCombiner(final SsaMethod ssaMeth) {
        this.ssaMeth = ssaMeth;
    }
    
    private int getParamIndex(final NormalSsaInsn normalSsaInsn) {
        return ((CstInteger)((CstInsn)normalSsaInsn.getOriginalRopInsn()).getConstant()).getValue();
    }
    
    public static void process(final SsaMethod ssaMethod) {
        new MoveParamCombiner(ssaMethod).run();
    }
    
    private void run() {
        final RegisterSpec[] array = new RegisterSpec[this.ssaMeth.getParamWidth()];
        final HashSet<SsaInsn> set = new HashSet<SsaInsn>();
        this.ssaMeth.forEachInsn(new SsaInsn.Visitor() {
            @Override
            public void visitMoveInsn(final NormalSsaInsn normalSsaInsn) {
            }
            
            @Override
            public void visitNonMoveInsn(final NormalSsaInsn normalSsaInsn) {
                if (normalSsaInsn.getOpcode().getOpcode() != 3) {
                    return;
                }
                final int access$000 = MoveParamCombiner.this.getParamIndex(normalSsaInsn);
                if (array[access$000] == null) {
                    array[access$000] = normalSsaInsn.getResult();
                    return;
                }
                final RegisterSpec registerSpec = array[access$000];
                final RegisterSpec result = normalSsaInsn.getResult();
                LocalItem localItem = registerSpec.getLocalItem();
                final LocalItem localItem2 = result.getLocalItem();
                if (localItem == null) {
                    localItem = localItem2;
                }
                else if (localItem2 != null) {
                    if (!localItem.equals(localItem2)) {
                        return;
                    }
                }
                MoveParamCombiner.this.ssaMeth.getDefinitionForRegister(registerSpec.getReg()).setResultLocal(localItem);
                final RegisterMapper registerMapper = new RegisterMapper() {
                    @Override
                    public int getNewRegisterCount() {
                        return MoveParamCombiner.this.ssaMeth.getRegCount();
                    }
                    
                    @Override
                    public RegisterSpec map(final RegisterSpec registerSpec) {
                        if (registerSpec.getReg() == result.getReg()) {
                            return registerSpec;
                        }
                        return registerSpec;
                    }
                };
                final List<SsaInsn> useListForRegister = MoveParamCombiner.this.ssaMeth.getUseListForRegister(result.getReg());
                for (int i = useListForRegister.size() - 1; i >= 0; --i) {
                    useListForRegister.get(i).mapSourceRegisters(registerMapper);
                }
                set.add(normalSsaInsn);
            }
            
            @Override
            public void visitPhiInsn(final PhiInsn phiInsn) {
            }
        });
        this.ssaMeth.deleteInsns(set);
    }
}
