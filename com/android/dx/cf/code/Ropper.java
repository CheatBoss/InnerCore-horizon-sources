package com.android.dx.cf.code;

import com.android.dx.cf.iface.*;
import com.android.dx.rop.type.*;
import com.android.dx.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.rop.cst.*;
import java.util.*;

public final class Ropper
{
    private static final int PARAM_ASSIGNMENT = -1;
    private static final int RETURN = -2;
    private static final int SPECIAL_LABEL_COUNT = 7;
    private static final int SYNCH_CATCH_1 = -6;
    private static final int SYNCH_CATCH_2 = -7;
    private static final int SYNCH_RETURN = -3;
    private static final int SYNCH_SETUP_1 = -4;
    private static final int SYNCH_SETUP_2 = -5;
    private final ByteBlockList blocks;
    private final CatchInfo[] catchInfos;
    private final ExceptionSetupLabelAllocator exceptionSetupLabelAllocator;
    private boolean hasSubroutines;
    private final RopperMachine machine;
    private final int maxLabel;
    private final int maxLocals;
    private final ConcreteMethod method;
    private final ArrayList<BasicBlock> result;
    private final ArrayList<IntList> resultSubroutines;
    private final Simulator sim;
    private final Frame[] startFrames;
    private final Subroutine[] subroutines;
    private boolean synchNeedsExceptionHandler;
    
    private Ropper(final ConcreteMethod method, final TranslationAdvice translationAdvice, final MethodList list) {
        if (method == null) {
            throw new NullPointerException("method == null");
        }
        if (translationAdvice == null) {
            throw new NullPointerException("advice == null");
        }
        this.method = method;
        this.blocks = BasicBlocker.identifyBlocks(method);
        this.maxLabel = this.blocks.getMaxLabel();
        this.maxLocals = method.getMaxLocals();
        this.machine = new RopperMachine(this, method, translationAdvice, list);
        this.sim = new Simulator(this.machine, method);
        this.startFrames = new Frame[this.maxLabel];
        this.subroutines = new Subroutine[this.maxLabel];
        this.result = new ArrayList<BasicBlock>(this.blocks.size() * 2 + 10);
        this.resultSubroutines = new ArrayList<IntList>(this.blocks.size() * 2 + 10);
        this.catchInfos = new CatchInfo[this.maxLabel];
        this.synchNeedsExceptionHandler = false;
        this.startFrames[0] = new Frame(this.maxLocals, method.getMaxStack());
        this.exceptionSetupLabelAllocator = new ExceptionSetupLabelAllocator();
    }
    
    private void addBlock(final BasicBlock basicBlock, final IntList list) {
        if (basicBlock == null) {
            throw new NullPointerException("block == null");
        }
        this.result.add(basicBlock);
        list.throwIfMutable();
        this.resultSubroutines.add(list);
    }
    
    private void addExceptionSetupBlocks() {
        for (int length = this.catchInfos.length, i = 0; i < length; ++i) {
            final CatchInfo catchInfo = this.catchInfos[i];
            if (catchInfo != null) {
                for (final ExceptionHandlerSetup exceptionHandlerSetup : catchInfo.getSetups()) {
                    final SourcePosition position = this.labelToBlock(i).getFirstInsn().getPosition();
                    final InsnList list = new InsnList(2);
                    list.set(0, new PlainInsn(Rops.opMoveException(exceptionHandlerSetup.getCaughtType()), position, RegisterSpec.make(this.maxLocals, exceptionHandlerSetup.getCaughtType()), RegisterSpecList.EMPTY));
                    list.set(1, new PlainInsn(Rops.GOTO, position, null, RegisterSpecList.EMPTY));
                    list.setImmutable();
                    this.addBlock(new BasicBlock(exceptionHandlerSetup.getLabel(), list, IntList.makeImmutable(i), i), this.startFrames[i].getSubroutines());
                }
            }
        }
    }
    
    private boolean addOrReplaceBlock(final BasicBlock basicBlock, final IntList list) {
        if (basicBlock == null) {
            throw new NullPointerException("block == null");
        }
        final int labelToResultIndex = this.labelToResultIndex(basicBlock.getLabel());
        boolean b;
        if (labelToResultIndex < 0) {
            b = false;
        }
        else {
            this.removeBlockAndSpecialSuccessors(labelToResultIndex);
            b = true;
        }
        this.result.add(basicBlock);
        list.throwIfMutable();
        this.resultSubroutines.add(list);
        return b;
    }
    
    private boolean addOrReplaceBlockNoDelete(final BasicBlock basicBlock, final IntList list) {
        if (basicBlock == null) {
            throw new NullPointerException("block == null");
        }
        final int labelToResultIndex = this.labelToResultIndex(basicBlock.getLabel());
        boolean b;
        if (labelToResultIndex < 0) {
            b = false;
        }
        else {
            this.result.remove(labelToResultIndex);
            this.resultSubroutines.remove(labelToResultIndex);
            b = true;
        }
        this.result.add(basicBlock);
        list.throwIfMutable();
        this.resultSubroutines.add(list);
        return b;
    }
    
    private void addReturnBlock() {
        final Rop returnOp = this.machine.getReturnOp();
        if (returnOp == null) {
            return;
        }
        final SourcePosition returnPosition = this.machine.getReturnPosition();
        int n2;
        final int n = n2 = this.getSpecialLabel(-2);
        if (this.isSynchronized()) {
            final InsnList list = new InsnList(1);
            list.set(0, new ThrowingInsn(Rops.MONITOR_EXIT, returnPosition, RegisterSpecList.make(this.getSynchReg()), StdTypeList.EMPTY));
            list.setImmutable();
            n2 = this.getSpecialLabel(-3);
            this.addBlock(new BasicBlock(n, list, IntList.makeImmutable(n2), n2), IntList.EMPTY);
        }
        final InsnList list2 = new InsnList(1);
        final TypeList sources = returnOp.getSources();
        RegisterSpecList list3;
        if (sources.size() == 0) {
            list3 = RegisterSpecList.EMPTY;
        }
        else {
            list3 = RegisterSpecList.make(RegisterSpec.make(0, sources.getType(0)));
        }
        list2.set(0, new PlainInsn(returnOp, returnPosition, null, list3));
        list2.setImmutable();
        this.addBlock(new BasicBlock(n2, list2, IntList.EMPTY, -1), IntList.EMPTY);
    }
    
    private void addSetupBlocks() {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    private void addSynchExceptionHandlerBlock() {
        if (!this.synchNeedsExceptionHandler) {
            return;
        }
        final SourcePosition sourcePosistion = this.method.makeSourcePosistion(0);
        final RegisterSpec make = RegisterSpec.make(0, Type.THROWABLE);
        final InsnList list = new InsnList(2);
        list.set(0, new PlainInsn(Rops.opMoveException(Type.THROWABLE), sourcePosistion, make, RegisterSpecList.EMPTY));
        list.set(1, new ThrowingInsn(Rops.MONITOR_EXIT, sourcePosistion, RegisterSpecList.make(this.getSynchReg()), StdTypeList.EMPTY));
        list.setImmutable();
        final int specialLabel = this.getSpecialLabel(-7);
        this.addBlock(new BasicBlock(this.getSpecialLabel(-6), list, IntList.makeImmutable(specialLabel), specialLabel), IntList.EMPTY);
        final InsnList list2 = new InsnList(1);
        list2.set(0, new ThrowingInsn(Rops.THROW, sourcePosistion, RegisterSpecList.make(make), StdTypeList.EMPTY));
        list2.setImmutable();
        this.addBlock(new BasicBlock(specialLabel, list2, IntList.EMPTY, -1), IntList.EMPTY);
    }
    
    public static RopMethod convert(final ConcreteMethod concreteMethod, final TranslationAdvice translationAdvice, final MethodList list) {
        try {
            final Ropper ropper = new Ropper(concreteMethod, translationAdvice, list);
            ropper.doit();
            return ropper.getRopMethod();
        }
        catch (SimException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("...while working on method ");
            sb.append(concreteMethod.getNat().toHuman());
            ex.addContext(sb.toString());
            throw ex;
        }
    }
    
    private void deleteUnreachableBlocks() {
        final IntList list = new IntList(this.result.size());
        this.resultSubroutines.clear();
        this.forEachNonSubBlockDepthFirst(this.getSpecialLabel(-1), new BasicBlock.Visitor() {
            @Override
            public void visitBlock(final BasicBlock basicBlock) {
                list.add(basicBlock.getLabel());
            }
        });
        list.sort();
        for (int i = this.result.size() - 1; i >= 0; --i) {
            if (list.indexOf(this.result.get(i).getLabel()) < 0) {
                this.result.remove(i);
            }
        }
    }
    
    private void doit() {
        final int[] bitSet = Bits.makeBitSet(this.maxLabel);
        Bits.set(bitSet, 0);
        this.addSetupBlocks();
        this.setFirstFrame();
        while (true) {
            final int first = Bits.findFirst(bitSet, 0);
            if (first < 0) {
                break;
            }
            Bits.clear(bitSet, first);
            final ByteBlock labelToBlock = this.blocks.labelToBlock(first);
            final Frame frame = this.startFrames[first];
            try {
                this.processBlock(labelToBlock, frame, bitSet);
            }
            catch (SimException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("...while working on block ");
                sb.append(Hex.u2(first));
                ex.addContext(sb.toString());
                throw ex;
            }
        }
        this.addReturnBlock();
        this.addSynchExceptionHandlerBlock();
        this.addExceptionSetupBlocks();
        if (this.hasSubroutines) {
            this.inlineSubroutines();
        }
    }
    
    private InsnList filterMoveReturnAddressInsns(final InsnList list) {
        final int size = list.size();
        final int n = 0;
        int n2 = 0;
        int n3;
        for (int i = 0; i < size; ++i, n2 = n3) {
            n3 = n2;
            if (list.get(i).getOpcode() != Rops.MOVE_RETURN_ADDRESS) {
                n3 = n2 + 1;
            }
        }
        if (n2 == size) {
            return list;
        }
        final InsnList list2 = new InsnList(n2);
        int n4 = 0;
        int n5;
        for (int j = n; j < size; ++j, n4 = n5) {
            final Insn value = list.get(j);
            n5 = n4;
            if (value.getOpcode() != Rops.MOVE_RETURN_ADDRESS) {
                list2.set(n4, value);
                n5 = n4 + 1;
            }
        }
        list2.setImmutable();
        return list2;
    }
    
    private void forEachNonSubBlockDepthFirst(final int n, final BasicBlock.Visitor visitor) {
        this.forEachNonSubBlockDepthFirst0(this.labelToBlock(n), visitor, new BitSet(this.maxLabel));
    }
    
    private void forEachNonSubBlockDepthFirst0(final BasicBlock basicBlock, final BasicBlock.Visitor visitor, final BitSet set) {
        visitor.visitBlock(basicBlock);
        set.set(basicBlock.getLabel());
        final IntList successors = basicBlock.getSuccessors();
        for (int size = successors.size(), i = 0; i < size; ++i) {
            final int value = successors.get(i);
            if (!set.get(value)) {
                if (!this.isSubroutineCaller(basicBlock) || i <= 0) {
                    final int labelToResultIndex = this.labelToResultIndex(value);
                    if (labelToResultIndex >= 0) {
                        this.forEachNonSubBlockDepthFirst0(this.result.get(labelToResultIndex), visitor, set);
                    }
                }
            }
        }
    }
    
    private int getAvailableLabel() {
        int minimumUnreservedLabel = this.getMinimumUnreservedLabel();
        final Iterator<BasicBlock> iterator = this.result.iterator();
        while (iterator.hasNext()) {
            final int label = iterator.next().getLabel();
            int n;
            if (label >= (n = minimumUnreservedLabel)) {
                n = label + 1;
            }
            minimumUnreservedLabel = n;
        }
        return minimumUnreservedLabel;
    }
    
    private int getMinimumUnreservedLabel() {
        return this.maxLabel + this.method.getCatches().size() + 7;
    }
    
    private int getNormalRegCount() {
        return this.maxLocals + this.method.getMaxStack();
    }
    
    private RopMethod getRopMethod() {
        final int size = this.result.size();
        final BasicBlockList list = new BasicBlockList(size);
        for (int i = 0; i < size; ++i) {
            list.set(i, this.result.get(i));
        }
        list.setImmutable();
        return new RopMethod(list, this.getSpecialLabel(-1));
    }
    
    private int getSpecialLabel(final int n) {
        return this.maxLabel + this.method.getCatches().size() + ~n;
    }
    
    private RegisterSpec getSynchReg() {
        int normalRegCount = this.getNormalRegCount();
        final int n = 1;
        if (normalRegCount < 1) {
            normalRegCount = n;
        }
        return RegisterSpec.make(normalRegCount, Type.OBJECT);
    }
    
    private void inlineSubroutines() {
        final IntList list = new IntList(4);
        final BasicBlock.Visitor visitor = new BasicBlock.Visitor() {
            @Override
            public void visitBlock(final BasicBlock basicBlock) {
                if (Ropper.this.isSubroutineCaller(basicBlock)) {
                    list.add(basicBlock.getLabel());
                }
            }
        };
        final int n = 0;
        this.forEachNonSubBlockDepthFirst(0, visitor);
        final int availableLabel = this.getAvailableLabel();
        final ArrayList list2 = new ArrayList<IntList>(availableLabel);
        for (int i = 0; i < availableLabel; ++i) {
            list2.add(null);
        }
        for (int j = 0; j < this.result.size(); ++j) {
            final BasicBlock basicBlock = this.result.get(j);
            if (basicBlock != null) {
                list2.set(basicBlock.getLabel(), this.resultSubroutines.get(j));
            }
        }
        for (int size = list.size(), k = n; k < size; ++k) {
            new SubroutineInliner(new LabelAllocator(this.getAvailableLabel()), (ArrayList<IntList>)list2).inlineSubroutineCalledFrom(this.labelToBlock(list.get(k)));
        }
        this.deleteUnreachableBlocks();
    }
    
    private boolean isStatic() {
        return (this.method.getAccessFlags() & 0x8) != 0x0;
    }
    
    private boolean isSubroutineCaller(final BasicBlock basicBlock) {
        final IntList successors = basicBlock.getSuccessors();
        if (successors.size() < 2) {
            return false;
        }
        final int value = successors.get(1);
        return value < this.subroutines.length && this.subroutines[value] != null;
    }
    
    private boolean isSynchronized() {
        return (this.method.getAccessFlags() & 0x20) != 0x0;
    }
    
    private BasicBlock labelToBlock(final int n) {
        final int labelToResultIndex = this.labelToResultIndex(n);
        if (labelToResultIndex < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("no such label ");
            sb.append(Hex.u2(n));
            throw new IllegalArgumentException(sb.toString());
        }
        return this.result.get(labelToResultIndex);
    }
    
    private int labelToResultIndex(final int n) {
        for (int size = this.result.size(), i = 0; i < size; ++i) {
            if (this.result.get(i).getLabel() == n) {
                return i;
            }
        }
        return -1;
    }
    
    private void mergeAndWorkAsNecessary(final int n, final int n2, final Subroutine subroutine, final Frame frame, final int[] array) {
        final Frame frame2 = this.startFrames[n];
        if (frame2 != null) {
            Frame frame3;
            if (subroutine != null) {
                frame3 = frame2.mergeWithSubroutineCaller(frame, subroutine.getStartBlock(), n2);
            }
            else {
                frame3 = frame2.mergeWith(frame);
            }
            if (frame3 != frame2) {
                this.startFrames[n] = frame3;
                Bits.set(array, n);
            }
        }
        else {
            if (subroutine != null) {
                this.startFrames[n] = frame.makeNewSubroutineStartFrame(n, n2);
            }
            else {
                this.startFrames[n] = frame;
            }
            Bits.set(array, n);
        }
    }
    
    private void processBlock(final ByteBlock ex, final Frame frame, final int[] array) {
        final ByteCatchList catches = ((ByteBlock)ex).getCatches();
        this.machine.startBlock(catches.toRopCatchList());
        final Frame copy = frame.copy();
        this.sim.simulate((ByteBlock)ex, copy);
        copy.setImmutable();
        final int extraBlockCount = this.machine.getExtraBlockCount();
        final ArrayList<Insn> insns = this.machine.getInsns();
        final int size = insns.size();
        final int size2 = catches.size();
        IntList list = ((ByteBlock)ex).getSuccessors();
        Subroutine subroutine = null;
        int size3 = 0;
        IntList list2 = null;
        Subroutine subroutine2 = null;
        Label_0289: {
            if (this.machine.hasJsr()) {
                size3 = 1;
                final int value = list.get(1);
                if (this.subroutines[value] == null) {
                    this.subroutines[value] = new Subroutine(value);
                }
                this.subroutines[value].addCallerBlock(((ByteBlock)ex).getLabel());
                subroutine = this.subroutines[value];
            }
            else if (this.machine.hasRet()) {
                final int subroutineAddress = this.machine.getReturnAddress().getSubroutineAddress();
                if (this.subroutines[subroutineAddress] == null) {
                    this.subroutines[subroutineAddress] = new Subroutine(subroutineAddress, ((ByteBlock)ex).getLabel());
                }
                else {
                    this.subroutines[subroutineAddress].addRetBlock(((ByteBlock)ex).getLabel());
                }
                list = this.subroutines[subroutineAddress].getSuccessors();
                this.subroutines[subroutineAddress].mergeToSuccessors(copy, array);
                size3 = list.size();
            }
            else {
                if (!this.machine.wereCatchesUsed()) {
                    list2 = list;
                    subroutine2 = null;
                    size3 = 0;
                    break Label_0289;
                }
                size3 = size2;
            }
            subroutine2 = subroutine;
            list2 = list;
        }
        final int size4 = list2.size();
        int i = size3;
        final ArrayList<PlainInsn> list3 = (ArrayList<PlainInsn>)insns;
        IntList immutable = list2;
        final int n = size4;
        while (i < n) {
            final int value2 = immutable.get(i);
            try {
                final int label = ((ByteBlock)ex).getLabel();
                try {
                    this.mergeAndWorkAsNecessary(value2, label, subroutine2, copy, array);
                    ++i;
                }
                catch (SimException ex) {}
            }
            catch (SimException ex2) {}
            final StringBuilder sb = new StringBuilder();
            sb.append("...while merging to block ");
            sb.append(Hex.u2(value2));
            ex.addContext(sb.toString());
            throw ex;
        }
        final int n2 = extraBlockCount;
        final ArrayList<PlainInsn> list4 = list3;
        int n3;
        if (n == 0 && this.machine.returns()) {
            immutable = IntList.makeImmutable(this.getSpecialLabel(-2));
            n3 = 1;
        }
        else {
            n3 = n;
        }
        int value3;
        if (n3 == 0) {
            value3 = -1;
        }
        else {
            final int primarySuccessorIndex = this.machine.getPrimarySuccessorIndex();
            if ((value3 = primarySuccessorIndex) >= 0) {
                value3 = immutable.get(primarySuccessorIndex);
            }
        }
        final boolean b = this.isSynchronized() && this.machine.canThrow();
        ArrayList<PlainInsn> list5;
        IntList mutableCopy;
        if (!b && size2 == 0) {
            list5 = list4;
            mutableCopy = immutable;
        }
        else {
            final IntList list6 = new IntList(n3);
            boolean b2 = false;
            int j = 0;
            final ByteCatchList list7 = catches;
            final IntList list8 = list6;
            while (j < size2) {
                final ByteCatchList.Item value4 = list7.get(j);
                final CstType exceptionClass = value4.getExceptionClass();
                final int handlerPc = value4.getHandlerPc();
                final boolean b3 = exceptionClass == CstType.OBJECT;
                final Frame exceptionHandlerStartFrame = copy.makeExceptionHandlerStartFrame(exceptionClass);
                try {
                    final int label2 = ((ByteBlock)ex).getLabel();
                    try {
                        this.mergeAndWorkAsNecessary(handlerPc, label2, null, exceptionHandlerStartFrame, array);
                        CatchInfo catchInfo;
                        if ((catchInfo = this.catchInfos[handlerPc]) == null) {
                            catchInfo = new CatchInfo();
                            this.catchInfos[handlerPc] = catchInfo;
                        }
                        list8.add(catchInfo.getSetup(exceptionClass.getClassType()).getLabel());
                        ++j;
                        b2 |= b3;
                    }
                    catch (SimException ex) {}
                }
                catch (SimException ex3) {}
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("...while merging exception to block ");
                sb2.append(Hex.u2(handlerPc));
                ex.addContext(sb2.toString());
                throw ex;
            }
            final IntList list9 = list8;
            ArrayList<PlainInsn> list10 = list4;
            if (b) {
                list10 = list4;
                if (!b2) {
                    list9.add(this.getSpecialLabel(-6));
                    this.synchNeedsExceptionHandler = true;
                    int n4 = size - n2 - 1;
                    while (true) {
                        list10 = list4;
                        if (n4 >= size) {
                            break;
                        }
                        final PlainInsn plainInsn = list4.get(n4);
                        if (plainInsn.canThrow()) {
                            list4.set(n4, (PlainInsn)plainInsn.withAddedCatch(Type.OBJECT));
                        }
                        ++n4;
                    }
                }
            }
            if (value3 >= 0) {
                list9.add(value3);
            }
            list9.setImmutable();
            mutableCopy = list9;
            list5 = list10;
        }
        final int index = mutableCopy.indexOf(value3);
        int k = n2;
        int n5 = size;
        while (k > 0) {
            final int n6 = n5 - 1;
            final PlainInsn plainInsn2 = list5.get(n6);
            final boolean b4 = plainInsn2.getOpcode().getBranchingness() == 1;
            int n7;
            if (b4) {
                n7 = 2;
            }
            else {
                n7 = 1;
            }
            final InsnList list11 = new InsnList(n7);
            list11.set(0, plainInsn2);
            IntList immutable2;
            if (b4) {
                list11.set(1, new PlainInsn(Rops.GOTO, plainInsn2.getPosition(), null, RegisterSpecList.EMPTY));
                immutable2 = IntList.makeImmutable(value3);
            }
            else {
                immutable2 = mutableCopy;
            }
            list11.setImmutable();
            final int availableLabel = this.getAvailableLabel();
            this.addBlock(new BasicBlock(availableLabel, list11, immutable2, value3), copy.getSubroutines());
            mutableCopy = mutableCopy.mutableCopy();
            mutableCopy.set(index, availableLabel);
            mutableCopy.setImmutable();
            value3 = availableLabel;
            --k;
            n5 = n6;
        }
        Insn insn;
        if (n5 == 0) {
            insn = null;
        }
        else {
            insn = list5.get(n5 - 1);
        }
        if (insn == null || insn.getOpcode().getBranchingness() == 1) {
            SourcePosition sourcePosition;
            if (insn == null) {
                sourcePosition = SourcePosition.NO_INFO;
            }
            else {
                sourcePosition = insn.getPosition();
            }
            list5.add(new PlainInsn(Rops.GOTO, sourcePosition, null, RegisterSpecList.EMPTY));
            ++n5;
        }
        final InsnList list12 = new InsnList(n5);
        for (int l = 0; l < n5; ++l) {
            list12.set(l, list5.get(l));
        }
        list12.setImmutable();
        this.addOrReplaceBlock(new BasicBlock(((ByteBlock)ex).getLabel(), list12, mutableCopy, value3), copy.getSubroutines());
    }
    
    private void removeBlockAndSpecialSuccessors(int i) {
        final int minimumUnreservedLabel = this.getMinimumUnreservedLabel();
        final IntList successors = this.result.get(i).getSuccessors();
        final int size = successors.size();
        this.result.remove(i);
        this.resultSubroutines.remove(i);
        int value;
        int labelToResultIndex;
        StringBuilder sb;
        for (i = 0; i < size; ++i) {
            value = successors.get(i);
            if (value >= minimumUnreservedLabel) {
                labelToResultIndex = this.labelToResultIndex(value);
                if (labelToResultIndex < 0) {
                    sb = new StringBuilder();
                    sb.append("Invalid label ");
                    sb.append(Hex.u2(value));
                    throw new RuntimeException(sb.toString());
                }
                this.removeBlockAndSpecialSuccessors(labelToResultIndex);
            }
        }
    }
    
    private void setFirstFrame() {
        this.startFrames[0].initializeWithParameters(this.method.getEffectiveDescriptor().getParameterTypes());
        this.startFrames[0].setImmutable();
    }
    
    private Subroutine subroutineFromRetBlock(final int n) {
        for (int i = this.subroutines.length - 1; i >= 0; --i) {
            if (this.subroutines[i] != null) {
                final Subroutine subroutine = this.subroutines[i];
                if (subroutine.retBlocks.get(n)) {
                    return subroutine;
                }
            }
        }
        return null;
    }
    
    int getFirstTempStackReg() {
        final int normalRegCount = this.getNormalRegCount();
        if (this.isSynchronized()) {
            return normalRegCount + 1;
        }
        return normalRegCount;
    }
    
    private class CatchInfo
    {
        private final Map<Type, ExceptionHandlerSetup> setups;
        
        private CatchInfo() {
            this.setups = new HashMap<Type, ExceptionHandlerSetup>();
        }
        
        ExceptionHandlerSetup getSetup(final Type type) {
            ExceptionHandlerSetup exceptionHandlerSetup;
            if ((exceptionHandlerSetup = this.setups.get(type)) == null) {
                exceptionHandlerSetup = new ExceptionHandlerSetup(type, Ropper.this.exceptionSetupLabelAllocator.getNextLabel());
                this.setups.put(type, exceptionHandlerSetup);
            }
            return exceptionHandlerSetup;
        }
        
        Collection<ExceptionHandlerSetup> getSetups() {
            return this.setups.values();
        }
    }
    
    private static class ExceptionHandlerSetup
    {
        private Type caughtType;
        private int label;
        
        ExceptionHandlerSetup(final Type caughtType, final int label) {
            this.caughtType = caughtType;
            this.label = label;
        }
        
        Type getCaughtType() {
            return this.caughtType;
        }
        
        public int getLabel() {
            return this.label;
        }
    }
    
    private class ExceptionSetupLabelAllocator extends LabelAllocator
    {
        int maxSetupLabel;
        
        ExceptionSetupLabelAllocator() {
            super(Ropper.this.maxLabel);
            this.maxSetupLabel = Ropper.this.maxLabel + Ropper.this.method.getCatches().size();
        }
        
        @Override
        int getNextLabel() {
            if (this.nextAvailableLabel >= this.maxSetupLabel) {
                throw new IndexOutOfBoundsException();
            }
            return this.nextAvailableLabel++;
        }
    }
    
    private static class LabelAllocator
    {
        int nextAvailableLabel;
        
        LabelAllocator(final int nextAvailableLabel) {
            this.nextAvailableLabel = nextAvailableLabel;
        }
        
        int getNextLabel() {
            return this.nextAvailableLabel++;
        }
    }
    
    private class Subroutine
    {
        private BitSet callerBlocks;
        private BitSet retBlocks;
        private int startBlock;
        
        Subroutine(final int startBlock) {
            this.startBlock = startBlock;
            this.retBlocks = new BitSet(Ropper.this.maxLabel);
            this.callerBlocks = new BitSet(Ropper.this.maxLabel);
            Ropper.this.hasSubroutines = true;
        }
        
        Subroutine(final Ropper ropper, final int n, final int n2) {
            this(ropper, n);
            this.addRetBlock(n2);
        }
        
        void addCallerBlock(final int n) {
            this.callerBlocks.set(n);
        }
        
        void addRetBlock(final int n) {
            this.retBlocks.set(n);
        }
        
        int getStartBlock() {
            return this.startBlock;
        }
        
        IntList getSuccessors() {
            final IntList list = new IntList(this.callerBlocks.size());
            for (int i = this.callerBlocks.nextSetBit(0); i >= 0; i = this.callerBlocks.nextSetBit(i + 1)) {
                list.add(Ropper.this.labelToBlock(i).getSuccessors().get(0));
            }
            list.setImmutable();
            return list;
        }
        
        void mergeToSuccessors(final Frame frame, final int[] array) {
            for (int i = this.callerBlocks.nextSetBit(0); i >= 0; i = this.callerBlocks.nextSetBit(i + 1)) {
                final int value = Ropper.this.labelToBlock(i).getSuccessors().get(0);
                final Frame subFrameForLabel = frame.subFrameForLabel(this.startBlock, i);
                if (subFrameForLabel != null) {
                    Ropper.this.mergeAndWorkAsNecessary(value, -1, null, subFrameForLabel, array);
                }
                else {
                    Bits.set(array, i);
                }
            }
        }
    }
    
    private class SubroutineInliner
    {
        private final LabelAllocator labelAllocator;
        private final ArrayList<IntList> labelToSubroutines;
        private final HashMap<Integer, Integer> origLabelToCopiedLabel;
        private int subroutineStart;
        private int subroutineSuccessor;
        private final BitSet workList;
        
        SubroutineInliner(final LabelAllocator labelAllocator, final ArrayList<IntList> labelToSubroutines) {
            this.origLabelToCopiedLabel = new HashMap<Integer, Integer>();
            this.workList = new BitSet(Ropper.this.maxLabel);
            this.labelAllocator = labelAllocator;
            this.labelToSubroutines = labelToSubroutines;
        }
        
        private void copyBlock(int subroutineSuccessor, final int n) {
            final BasicBlock access$300 = Ropper.this.labelToBlock(subroutineSuccessor);
            final IntList successors = access$300.getSuccessors();
            final int n2 = -1;
            final boolean access$301 = Ropper.this.isSubroutineCaller(access$300);
            int i = 0;
            IntList list;
            if (access$301) {
                list = IntList.makeImmutable(this.mapOrAllocateLabel(successors.get(0)), successors.get(1));
                subroutineSuccessor = n2;
            }
            else {
                final Subroutine access$302 = Ropper.this.subroutineFromRetBlock(subroutineSuccessor);
                if (access$302 != null) {
                    if (access$302.startBlock != this.subroutineStart) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("ret instruction returns to label ");
                        sb.append(Hex.u2(access$302.startBlock));
                        sb.append(" expected: ");
                        sb.append(Hex.u2(this.subroutineStart));
                        throw new RuntimeException(sb.toString());
                    }
                    list = IntList.makeImmutable(this.subroutineSuccessor);
                    subroutineSuccessor = this.subroutineSuccessor;
                }
                else {
                    final int primarySuccessor = access$300.getPrimarySuccessor();
                    final int size = successors.size();
                    list = new IntList(size);
                    subroutineSuccessor = n2;
                    while (i < size) {
                        final int value = successors.get(i);
                        final int mapOrAllocateLabel = this.mapOrAllocateLabel(value);
                        list.add(mapOrAllocateLabel);
                        if (primarySuccessor == value) {
                            subroutineSuccessor = mapOrAllocateLabel;
                        }
                        ++i;
                    }
                    list.setImmutable();
                }
            }
            Ropper.this.addBlock(new BasicBlock(n, Ropper.this.filterMoveReturnAddressInsns(access$300.getInsns()), list, subroutineSuccessor), this.labelToSubroutines.get(n));
        }
        
        private boolean involvedInSubroutine(final int n, final int n2) {
            final IntList list = this.labelToSubroutines.get(n);
            return list != null && list.size() > 0 && list.top() == n2;
        }
        
        private int mapOrAllocateLabel(final int n) {
            final Integer n2 = this.origLabelToCopiedLabel.get(n);
            if (n2 != null) {
                return n2;
            }
            if (!this.involvedInSubroutine(n, this.subroutineStart)) {
                return n;
            }
            final int nextLabel = this.labelAllocator.getNextLabel();
            this.workList.set(n);
            this.origLabelToCopiedLabel.put(n, nextLabel);
            while (this.labelToSubroutines.size() <= nextLabel) {
                this.labelToSubroutines.add(null);
            }
            this.labelToSubroutines.set(nextLabel, this.labelToSubroutines.get(n));
            return nextLabel;
        }
        
        void inlineSubroutineCalledFrom(final BasicBlock basicBlock) {
            this.subroutineSuccessor = basicBlock.getSuccessors().get(0);
            this.subroutineStart = basicBlock.getSuccessors().get(1);
            final int mapOrAllocateLabel = this.mapOrAllocateLabel(this.subroutineStart);
            for (int i = this.workList.nextSetBit(0); i >= 0; i = this.workList.nextSetBit(0)) {
                this.workList.clear(i);
                final int intValue = this.origLabelToCopiedLabel.get(i);
                this.copyBlock(i, intValue);
                if (Ropper.this.isSubroutineCaller(Ropper.this.labelToBlock(i))) {
                    new SubroutineInliner(this.labelAllocator, this.labelToSubroutines).inlineSubroutineCalledFrom(Ropper.this.labelToBlock(intValue));
                }
            }
            Ropper.this.addOrReplaceBlockNoDelete(new BasicBlock(basicBlock.getLabel(), basicBlock.getInsns(), IntList.makeImmutable(mapOrAllocateLabel), mapOrAllocateLabel), this.labelToSubroutines.get(basicBlock.getLabel()));
        }
    }
}
