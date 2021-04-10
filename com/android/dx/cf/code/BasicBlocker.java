package com.android.dx.cf.code;

import com.android.dx.util.*;
import com.android.dx.rop.cst.*;
import com.android.dx.rop.type.*;
import java.util.*;

public final class BasicBlocker implements Visitor
{
    private final int[] blockSet;
    private final ByteCatchList[] catchLists;
    private final int[] liveSet;
    private final ConcreteMethod method;
    private int previousOffset;
    private final IntList[] targetLists;
    private final int[] workSet;
    
    private BasicBlocker(final ConcreteMethod method) {
        if (method == null) {
            throw new NullPointerException("method == null");
        }
        this.method = method;
        final int n = method.getCode().size() + 1;
        this.workSet = Bits.makeBitSet(n);
        this.liveSet = Bits.makeBitSet(n);
        this.blockSet = Bits.makeBitSet(n);
        this.targetLists = new IntList[n];
        this.catchLists = new ByteCatchList[n];
        this.previousOffset = -1;
    }
    
    private void addWorkIfNecessary(final int n, final boolean b) {
        if (!Bits.get(this.liveSet, n)) {
            Bits.set(this.workSet, n);
        }
        if (b) {
            Bits.set(this.blockSet, n);
        }
    }
    
    private void doit() {
        final BytecodeArray code = this.method.getCode();
        final ByteCatchList catches = this.method.getCatches();
        final int size = catches.size();
        Bits.set(this.workSet, 0);
        Bits.set(this.blockSet, 0);
        while (!Bits.isEmpty(this.workSet)) {
            try {
                code.processWorkSet(this.workSet, (BytecodeArray.Visitor)this);
                for (int i = 0; i < size; ++i) {
                    final ByteCatchList.Item value = catches.get(i);
                    final int startPc = value.getStartPc();
                    final int endPc = value.getEndPc();
                    if (Bits.anyInRange(this.liveSet, startPc, endPc)) {
                        Bits.set(this.blockSet, startPc);
                        Bits.set(this.blockSet, endPc);
                        this.addWorkIfNecessary(value.getHandlerPc(), true);
                    }
                }
                continue;
            }
            catch (IllegalArgumentException ex) {
                throw new SimException("flow of control falls off end of method", ex);
            }
            break;
        }
    }
    
    private ByteBlockList getBlockList() {
        final ByteBlock[] array = new ByteBlock[this.method.getCode().size()];
        final int n = 0;
        int n2 = 0;
        int n3 = 0;
        while (true) {
            final int first = Bits.findFirst(this.blockSet, n3 + 1);
            if (first < 0) {
                break;
            }
            int n4 = n2;
            if (Bits.get(this.liveSet, n3)) {
                IntList list = null;
                final int n5 = -1;
                int n6 = first - 1;
                int n7;
                while (true) {
                    n7 = n5;
                    if (n6 < n3) {
                        break;
                    }
                    list = this.targetLists[n6];
                    if (list != null) {
                        n7 = n6;
                        break;
                    }
                    --n6;
                }
                IntList immutable;
                ByteCatchList list2;
                if (list == null) {
                    immutable = IntList.makeImmutable(first);
                    list2 = ByteCatchList.EMPTY;
                }
                else {
                    final ByteCatchList list3 = this.catchLists[n7];
                    immutable = list;
                    if ((list2 = list3) == null) {
                        list2 = ByteCatchList.EMPTY;
                        immutable = list;
                    }
                }
                array[n2] = new ByteBlock(n3, n3, first, immutable, list2);
                n4 = n2 + 1;
            }
            n3 = first;
            n2 = n4;
        }
        final ByteBlockList list4 = new ByteBlockList(n2);
        for (int i = n; i < n2; ++i) {
            list4.set(i, array[i]);
        }
        return list4;
    }
    
    public static ByteBlockList identifyBlocks(final ConcreteMethod concreteMethod) {
        final BasicBlocker basicBlocker = new BasicBlocker(concreteMethod);
        basicBlocker.doit();
        return basicBlocker.getBlockList();
    }
    
    private void visitCommon(final int n, final int n2, final boolean b) {
        Bits.set(this.liveSet, n);
        if (b) {
            this.addWorkIfNecessary(n + n2, false);
            return;
        }
        Bits.set(this.blockSet, n + n2);
    }
    
    private void visitThrowing(final int n, int n2, final boolean b) {
        n2 += n;
        if (b) {
            this.addWorkIfNecessary(n2, true);
        }
        final ByteCatchList list = this.method.getCatches().listFor(n);
        this.catchLists[n] = list;
        final IntList[] targetLists = this.targetLists;
        if (!b) {
            n2 = -1;
        }
        targetLists[n] = list.toTargetList(n2);
    }
    
    @Override
    public int getPreviousOffset() {
        return this.previousOffset;
    }
    
    @Override
    public void setPreviousOffset(final int previousOffset) {
        this.previousOffset = previousOffset;
    }
    
    @Override
    public void visitBranch(int n, final int n2, final int n3, final int n4) {
        Label_0086: {
            switch (n) {
                case 168: {
                    this.addWorkIfNecessary(n2, true);
                    break;
                }
                case 167: {
                    this.visitCommon(n2, n3, false);
                    this.targetLists[n2] = IntList.makeImmutable(n4);
                    break Label_0086;
                }
            }
            n = n2 + n3;
            this.visitCommon(n2, n3, true);
            this.addWorkIfNecessary(n, true);
            this.targetLists[n2] = IntList.makeImmutable(n, n4);
        }
        this.addWorkIfNecessary(n4, true);
    }
    
    @Override
    public void visitConstant(final int n, final int n2, final int n3, final Constant constant, final int n4) {
        this.visitCommon(n2, n3, true);
        if (constant instanceof CstMemberRef || constant instanceof CstType || constant instanceof CstString) {
            this.visitThrowing(n2, n3, true);
        }
    }
    
    @Override
    public void visitInvalid(final int n, final int n2, final int n3) {
        this.visitCommon(n2, n3, true);
    }
    
    @Override
    public void visitLocal(final int n, final int n2, final int n3, final int n4, final Type type, final int n5) {
        if (n == 169) {
            this.visitCommon(n2, n3, false);
            this.targetLists[n2] = IntList.EMPTY;
            return;
        }
        this.visitCommon(n2, n3, true);
    }
    
    @Override
    public void visitNewarray(final int n, final int n2, final CstType cstType, final ArrayList<Constant> list) {
        this.visitCommon(n, n2, true);
        this.visitThrowing(n, n2, true);
    }
    
    @Override
    public void visitNoArgs(final int n, final int n2, final int n3, final Type type) {
        if (n == 108 || n == 112) {
            this.visitCommon(n2, n3, true);
            if (type == Type.INT || type == Type.LONG) {
                this.visitThrowing(n2, n3, true);
            }
            return;
        }
        if (n == 172 || n == 177) {
            this.visitCommon(n2, n3, false);
            this.targetLists[n2] = IntList.EMPTY;
            return;
        }
        Label_0191: {
            switch (n) {
                default: {
                    switch (n) {
                        default: {
                            switch (n) {
                                default: {
                                    switch (n) {
                                        default: {
                                            this.visitCommon(n2, n3, true);
                                            return;
                                        }
                                        case 194:
                                        case 195: {
                                            break Label_0191;
                                        }
                                    }
                                    break;
                                }
                                case 191: {
                                    this.visitCommon(n2, n3, false);
                                    this.visitThrowing(n2, n3, false);
                                    return;
                                }
                                case 190: {
                                    break Label_0191;
                                }
                            }
                            break;
                        }
                        case 79:
                        case 80:
                        case 81:
                        case 82:
                        case 83:
                        case 84:
                        case 85:
                        case 86: {
                            break Label_0191;
                        }
                    }
                    break;
                }
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53: {
                    this.visitCommon(n2, n3, true);
                    this.visitThrowing(n2, n3, true);
                }
            }
        }
    }
    
    @Override
    public void visitSwitch(int i, final int n, int size, final SwitchList list, final int n2) {
        i = 0;
        this.visitCommon(n, size, false);
        this.addWorkIfNecessary(list.getDefaultTarget(), true);
        for (size = list.size(); i < size; ++i) {
            this.addWorkIfNecessary(list.getTarget(i), true);
        }
        this.targetLists[n] = list.getTargets();
    }
}
