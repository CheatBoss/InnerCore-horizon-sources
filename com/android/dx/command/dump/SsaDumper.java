package com.android.dx.command.dump;

import java.io.*;
import com.android.dx.cf.iface.*;
import com.android.dx.cf.code.*;
import com.android.dx.rop.code.*;
import com.android.dx.ssa.*;
import java.util.*;
import com.android.dx.util.*;

public class SsaDumper extends BlockDumper
{
    private SsaDumper(final byte[] array, final PrintStream printStream, final String s, final Args args) {
        super(array, printStream, s, true, args);
    }
    
    public static void dump(final byte[] array, final PrintStream printStream, final String s, final Args args) {
        new SsaDumper(array, printStream, s, args).dump();
    }
    
    @Override
    public void endParsingMember(final ByteArray byteArray, int i, final String s, final String s2, final Member member) {
        if (!(member instanceof Method)) {
            return;
        }
        if (!this.shouldDumpMethod(s)) {
            return;
        }
        if ((member.getAccessFlags() & 0x500) != 0x0) {
            return;
        }
        final ConcreteMethod concreteMethod = new ConcreteMethod((Method)member, this.classFile, true, true);
        DexTranslationAdvice the_ONE = DexTranslationAdvice.THE_ONE;
        RopMethod convert = Ropper.convert(concreteMethod, the_ONE, this.classFile.getMethods());
        SsaMethod ssaMethod = null;
        final boolean static1 = AccessFlags.isStatic(concreteMethod.getAccessFlags());
        i = BaseDumper.computeParamWidth(concreteMethod, static1);
        if (this.args.ssaStep == null) {
            ssaMethod = Optimizer.debugNoRegisterAllocation(convert, i, static1, true, the_ONE, EnumSet.allOf(Optimizer.OptionalStep.class));
        }
        else if ("edge-split".equals(this.args.ssaStep)) {
            ssaMethod = Optimizer.debugEdgeSplit(convert, i, static1, true, the_ONE);
        }
        else if ("phi-placement".equals(this.args.ssaStep)) {
            ssaMethod = Optimizer.debugPhiPlacement(convert, i, static1, true, the_ONE);
        }
        else if ("renaming".equals(this.args.ssaStep)) {
            ssaMethod = Optimizer.debugRenaming(convert, i, static1, true, the_ONE);
        }
        else if ("dead-code".equals(this.args.ssaStep)) {
            ssaMethod = Optimizer.debugDeadCodeRemover(convert, i, static1, true, the_ONE);
        }
        final StringBuffer sb = new StringBuffer(2000);
        sb.append("first ");
        sb.append(Hex.u2(ssaMethod.blockIndexToRopLabel(ssaMethod.getEntryBlockIndex())));
        sb.append('\n');
        final ArrayList list = (ArrayList)ssaMethod.getBlocks().clone();
        Collections.sort((List<Object>)list, (Comparator<? super Object>)SsaBasicBlock.LABEL_COMPARATOR);
        for (final SsaBasicBlock ssaBasicBlock : list) {
            sb.append("block ");
            sb.append(Hex.u2(ssaBasicBlock.getRopLabel()));
            sb.append('\n');
            BitSet predecessors;
            for (predecessors = ssaBasicBlock.getPredecessors(), i = predecessors.nextSetBit(0); i >= 0; i = predecessors.nextSetBit(i + 1)) {
                sb.append("  pred ");
                sb.append(Hex.u2(ssaMethod.blockIndexToRopLabel(i)));
                sb.append('\n');
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  live in:");
            sb2.append(ssaBasicBlock.getLiveInRegs());
            sb.append(sb2.toString());
            sb.append("\n");
            for (final SsaInsn ssaInsn : ssaBasicBlock.getInsns()) {
                sb.append("  ");
                sb.append(ssaInsn.toHuman());
                sb.append('\n');
            }
            DexTranslationAdvice dexTranslationAdvice;
            RopMethod ropMethod;
            if (ssaBasicBlock.getSuccessors().cardinality() == 0) {
                sb.append("  returns\n");
                dexTranslationAdvice = the_ONE;
                ropMethod = convert;
            }
            else {
                final int primarySuccessorRopLabel = ssaBasicBlock.getPrimarySuccessorRopLabel();
                final IntList ropLabelSuccessorList = ssaBasicBlock.getRopLabelSuccessorList();
                final int size = ropLabelSuccessorList.size();
                i = 0;
                while (true) {
                    dexTranslationAdvice = the_ONE;
                    if (i >= size) {
                        break;
                    }
                    sb.append("  next ");
                    sb.append(Hex.u2(ropLabelSuccessorList.get(i)));
                    if (size != 1 && primarySuccessorRopLabel == ropLabelSuccessorList.get(i)) {
                        sb.append(" *");
                    }
                    sb.append('\n');
                    ++i;
                    the_ONE = dexTranslationAdvice;
                }
                ropMethod = convert;
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("  live out:");
            sb3.append(ssaBasicBlock.getLiveOutRegs());
            sb.append(sb3.toString());
            sb.append("\n");
            convert = ropMethod;
            the_ONE = dexTranslationAdvice;
        }
        this.suppressDump = false;
        this.setAt(byteArray, 0);
        this.parsed(byteArray, 0, byteArray.size(), sb.toString());
        this.suppressDump = true;
    }
}
