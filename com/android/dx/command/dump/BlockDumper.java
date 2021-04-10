package com.android.dx.command.dump;

import java.io.*;
import com.android.dx.rop.cst.*;
import com.android.dx.util.*;
import com.android.dx.cf.code.*;
import com.android.dx.ssa.*;
import com.android.dx.rop.code.*;
import com.android.dx.cf.direct.*;
import com.android.dx.cf.iface.*;

public class BlockDumper extends BaseDumper
{
    protected DirectClassFile classFile;
    private boolean first;
    private boolean optimize;
    private boolean rop;
    protected boolean suppressDump;
    
    BlockDumper(final byte[] array, final PrintStream printStream, final String s, final boolean rop, final Args args) {
        super(array, printStream, s, args);
        this.rop = rop;
        this.classFile = null;
        this.suppressDump = true;
        this.first = true;
        this.optimize = args.optimize;
    }
    
    public static void dump(final byte[] array, final PrintStream printStream, final String s, final boolean b, final Args args) {
        new BlockDumper(array, printStream, s, b, args).dump();
    }
    
    private void regularDump(final ConcreteMethod concreteMethod) {
        final BytecodeArray code = concreteMethod.getCode();
        final ByteArray bytes = code.getBytes();
        final ByteBlockList identifyBlocks = BasicBlocker.identifyBlocks(concreteMethod);
        final int size = identifyBlocks.size();
        final CodeObserver codeObserver = new CodeObserver(bytes, this);
        this.setAt(bytes, 0);
        this.suppressDump = false;
        int n = 0;
        int i = 0;
        final BytecodeArray bytecodeArray = code;
        while (i < size) {
            final ByteBlock value = identifyBlocks.get(i);
            final int start = value.getStart();
            final int end = value.getEnd();
            if (n < start) {
                final StringBuilder sb = new StringBuilder();
                sb.append("dead code ");
                sb.append(Hex.u2(n));
                sb.append("..");
                sb.append(Hex.u2(start));
                this.parsed(bytes, n, start - n, sb.toString());
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("block ");
            sb2.append(Hex.u2(value.getLabel()));
            sb2.append(": ");
            sb2.append(Hex.u2(start));
            sb2.append("..");
            sb2.append(Hex.u2(end));
            this.parsed(bytes, start, 0, sb2.toString());
            this.changeIndent(1);
            int instruction;
            for (int j = start; j < end; j += instruction) {
                instruction = bytecodeArray.parseInstruction(j, (BytecodeArray.Visitor)codeObserver);
                codeObserver.setPreviousOffset(j);
            }
            final IntList successors = value.getSuccessors();
            final int size2 = successors.size();
            if (size2 == 0) {
                this.parsed(bytes, end, 0, "returns");
            }
            else {
                for (int k = 0; k < size2; ++k) {
                    final int value2 = successors.get(k);
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("next ");
                    sb3.append(Hex.u2(value2));
                    this.parsed(bytes, end, 0, sb3.toString());
                }
            }
            final ByteCatchList catches = value.getCatches();
            for (int size3 = catches.size(), l = 0; l < size3; ++l) {
                final ByteCatchList.Item value3 = catches.get(l);
                final CstType exceptionClass = value3.getExceptionClass();
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("catch ");
                String human;
                if (exceptionClass == CstType.OBJECT) {
                    human = "<any>";
                }
                else {
                    human = exceptionClass.toHuman();
                }
                sb4.append(human);
                sb4.append(" -> ");
                sb4.append(Hex.u2(value3.getHandlerPc()));
                this.parsed(bytes, end, 0, sb4.toString());
            }
            this.changeIndent(-1);
            n = end;
            ++i;
        }
        final int size4 = bytes.size();
        if (n < size4) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("dead code ");
            sb5.append(Hex.u2(n));
            sb5.append("..");
            sb5.append(Hex.u2(size4));
            this.parsed(bytes, n, size4 - n, sb5.toString());
        }
        this.suppressDump = true;
    }
    
    private void ropDump(final ConcreteMethod concreteMethod) {
        final DexTranslationAdvice the_ONE = DexTranslationAdvice.THE_ONE;
        final ByteArray bytes = concreteMethod.getCode().getBytes();
        final RopMethod convert = Ropper.convert(concreteMethod, the_ONE, this.classFile.getMethods());
        final StringBuffer sb = new StringBuffer(2000);
        RopMethod optimize = convert;
        if (this.optimize) {
            final boolean static1 = AccessFlags.isStatic(concreteMethod.getAccessFlags());
            optimize = Optimizer.optimize(convert, BaseDumper.computeParamWidth(concreteMethod, static1), static1, true, the_ONE);
        }
        BasicBlockList blocks = optimize.getBlocks();
        final int[] labelsInOrder = blocks.getLabelsInOrder();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("first ");
        sb2.append(Hex.u2(optimize.getFirstLabel()));
        sb2.append("\n");
        sb.append(sb2.toString());
        final int length = labelsInOrder.length;
        int i = 0;
        RopMethod ropMethod = optimize;
        while (i < length) {
            final int n = labelsInOrder[i];
            final BasicBlock value = blocks.get(blocks.indexOfLabel(n));
            sb.append("block ");
            sb.append(Hex.u2(n));
            sb.append("\n");
            final IntList labelToPredecessors = ropMethod.labelToPredecessors(n);
            for (int size = labelToPredecessors.size(), j = 0; j < size; ++j) {
                sb.append("  pred ");
                sb.append(Hex.u2(labelToPredecessors.get(j)));
                sb.append("\n");
            }
            final InsnList insns = value.getInsns();
            for (int size2 = insns.size(), k = 0; k < size2; ++k) {
                insns.get(k);
                sb.append("  ");
                sb.append(insns.get(k).toHuman());
                sb.append("\n");
            }
            final IntList successors = value.getSuccessors();
            final int size3 = successors.size();
            RopMethod ropMethod2;
            BasicBlockList list;
            if (size3 == 0) {
                sb.append("  returns\n");
                ropMethod2 = ropMethod;
                list = blocks;
            }
            else {
                final int primarySuccessor = value.getPrimarySuccessor();
                int n2 = 0;
                while (true) {
                    ropMethod2 = ropMethod;
                    if (n2 >= size3) {
                        break;
                    }
                    final int value2 = successors.get(n2);
                    sb.append("  next ");
                    sb.append(Hex.u2(value2));
                    if (size3 != 1 && value2 == primarySuccessor) {
                        sb.append(" *");
                    }
                    sb.append("\n");
                    ++n2;
                    ropMethod = ropMethod2;
                }
                list = blocks;
            }
            ++i;
            blocks = list;
            ropMethod = ropMethod2;
        }
        this.suppressDump = false;
        this.setAt(bytes, 0);
        this.parsed(bytes, 0, bytes.size(), sb.toString());
        this.suppressDump = true;
    }
    
    @Override
    public void changeIndent(final int n) {
        if (!this.suppressDump) {
            super.changeIndent(n);
        }
    }
    
    public void dump() {
        final ByteArray byteArray = new ByteArray(this.getBytes());
        (this.classFile = new DirectClassFile(byteArray, this.getFilePath(), this.getStrictParse())).setAttributeFactory(StdAttributeFactory.THE_ONE);
        this.classFile.getMagic();
        final DirectClassFile directClassFile = new DirectClassFile(byteArray, this.getFilePath(), this.getStrictParse());
        directClassFile.setAttributeFactory(StdAttributeFactory.THE_ONE);
        directClassFile.setObserver(this);
        directClassFile.getMagic();
    }
    
    @Override
    public void endParsingMember(final ByteArray byteArray, final int n, final String s, final String s2, final Member member) {
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
        if (this.rop) {
            this.ropDump(concreteMethod);
            return;
        }
        this.regularDump(concreteMethod);
    }
    
    @Override
    public void parsed(final ByteArray byteArray, final int n, final int n2, final String s) {
        if (!this.suppressDump) {
            super.parsed(byteArray, n, n2, s);
        }
    }
    
    protected boolean shouldDumpMethod(final String s) {
        return this.args.method == null || this.args.method.equals(s);
    }
    
    @Override
    public void startParsingMember(final ByteArray byteArray, final int n, final String s, final String s2) {
        if (s2.indexOf(40) < 0) {
            return;
        }
        if (!this.shouldDumpMethod(s)) {
            return;
        }
        this.setAt(byteArray, n);
        this.suppressDump = false;
        if (this.first) {
            this.first = false;
        }
        else {
            this.parsed(byteArray, n, 0, "\n");
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("method ");
        sb.append(s);
        sb.append(" ");
        sb.append(s2);
        this.parsed(byteArray, n, 0, sb.toString());
        this.suppressDump = true;
    }
}
