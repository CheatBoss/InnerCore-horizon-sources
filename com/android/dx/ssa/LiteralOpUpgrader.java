package com.android.dx.ssa;

import com.android.dx.rop.cst.*;
import com.android.dx.rop.type.*;
import java.util.*;
import com.android.dx.rop.code.*;

public class LiteralOpUpgrader
{
    private final SsaMethod ssaMeth;
    
    private LiteralOpUpgrader(final SsaMethod ssaMeth) {
        this.ssaMeth = ssaMeth;
    }
    
    private static boolean isConstIntZeroOrKnownNull(final RegisterSpec registerSpec) {
        final TypeBearer typeBearer = registerSpec.getTypeBearer();
        final boolean b = typeBearer instanceof CstLiteralBits;
        boolean b2 = false;
        if (b) {
            if (((CstLiteralBits)typeBearer).getLongBits() == 0L) {
                b2 = true;
            }
            return b2;
        }
        return false;
    }
    
    public static void process(final SsaMethod ssaMethod) {
        new LiteralOpUpgrader(ssaMethod).run();
    }
    
    private void replacePlainInsn(final NormalSsaInsn normalSsaInsn, final RegisterSpecList list, final int n, final Constant constant) {
        final Insn originalRopInsn = normalSsaInsn.getOriginalRopInsn();
        final Rop rop = Rops.ropFor(n, normalSsaInsn.getResult(), list, constant);
        Insn insn;
        if (constant == null) {
            insn = new PlainInsn(rop, originalRopInsn.getPosition(), normalSsaInsn.getResult(), list);
        }
        else {
            insn = new PlainCstInsn(rop, originalRopInsn.getPosition(), normalSsaInsn.getResult(), list, constant);
        }
        final NormalSsaInsn normalSsaInsn2 = new NormalSsaInsn(insn, normalSsaInsn.getBlock());
        final ArrayList<SsaInsn> insns = normalSsaInsn.getBlock().getInsns();
        this.ssaMeth.onInsnRemoved(normalSsaInsn);
        insns.set(insns.lastIndexOf(normalSsaInsn), normalSsaInsn2);
        this.ssaMeth.onInsnAdded(normalSsaInsn2);
    }
    
    private void run() {
        this.ssaMeth.forEachInsn(new SsaInsn.Visitor() {
            final /* synthetic */ TranslationAdvice val$advice = Optimizer.getAdvice();
            
            @Override
            public void visitMoveInsn(final NormalSsaInsn normalSsaInsn) {
            }
            
            @Override
            public void visitNonMoveInsn(final NormalSsaInsn normalSsaInsn) {
                final Rop opcode = normalSsaInsn.getOriginalRopInsn().getOpcode();
                final RegisterSpecList sources = normalSsaInsn.getSources();
                if (LiteralOpUpgrader.this.tryReplacingWithConstant(normalSsaInsn)) {
                    return;
                }
                if (sources.size() != 2) {
                    return;
                }
                if (opcode.getBranchingness() == 4) {
                    if (isConstIntZeroOrKnownNull(sources.get(0))) {
                        LiteralOpUpgrader.this.replacePlainInsn(normalSsaInsn, sources.withoutFirst(), RegOps.flippedIfOpcode(opcode.getOpcode()), null);
                        return;
                    }
                    if (isConstIntZeroOrKnownNull(sources.get(1))) {
                        LiteralOpUpgrader.this.replacePlainInsn(normalSsaInsn, sources.withoutLast(), opcode.getOpcode(), null);
                    }
                }
                else {
                    if (this.val$advice.hasConstantOperation(opcode, sources.get(0), sources.get(1))) {
                        normalSsaInsn.upgradeToLiteral();
                        return;
                    }
                    if (opcode.isCommutative() && this.val$advice.hasConstantOperation(opcode, sources.get(1), sources.get(0))) {
                        normalSsaInsn.setNewSources(RegisterSpecList.make(sources.get(1), sources.get(0)));
                        normalSsaInsn.upgradeToLiteral();
                    }
                }
            }
            
            @Override
            public void visitPhiInsn(final PhiInsn phiInsn) {
            }
        });
    }
    
    private boolean tryReplacingWithConstant(final NormalSsaInsn normalSsaInsn) {
        final Rop opcode = normalSsaInsn.getOriginalRopInsn().getOpcode();
        final RegisterSpec result = normalSsaInsn.getResult();
        if (result != null && !this.ssaMeth.isRegALocal(result) && opcode.getOpcode() != 5) {
            final TypeBearer typeBearer = normalSsaInsn.getResult().getTypeBearer();
            if (typeBearer.isConstant() && typeBearer.getBasicType() == 6) {
                this.replacePlainInsn(normalSsaInsn, RegisterSpecList.EMPTY, 5, (Constant)typeBearer);
                if (opcode.getOpcode() == 56) {
                    final ArrayList<SsaInsn> insns = this.ssaMeth.getBlocks().get(normalSsaInsn.getBlock().getPredecessors().nextSetBit(0)).getInsns();
                    this.replacePlainInsn((NormalSsaInsn)insns.get(insns.size() - 1), RegisterSpecList.EMPTY, 6, null);
                }
                return true;
            }
        }
        return false;
    }
}
