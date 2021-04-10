package com.android.dx.ssa.back;

import java.util.*;
import com.android.dx.rop.cst.*;
import com.android.dx.rop.code.*;
import com.android.dx.ssa.*;
import com.android.dx.util.*;

public class FirstFitLocalCombiningAllocator extends RegisterAllocator
{
    private static final boolean DEBUG = false;
    private final ArrayList<NormalSsaInsn> invokeRangeInsns;
    private final Map<LocalItem, ArrayList<RegisterSpec>> localVariables;
    private final InterferenceRegisterMapper mapper;
    private final boolean minimizeRegisters;
    private final ArrayList<NormalSsaInsn> moveResultPseudoInsns;
    private final int paramRangeEnd;
    private final ArrayList<PhiInsn> phiInsns;
    private final BitSet reservedRopRegs;
    private final BitSet ssaRegsMapped;
    private final BitSet usedRopRegs;
    
    public FirstFitLocalCombiningAllocator(final SsaMethod ssaMethod, final InterferenceGraph interferenceGraph, final boolean minimizeRegisters) {
        super(ssaMethod, interferenceGraph);
        this.ssaRegsMapped = new BitSet(ssaMethod.getRegCount());
        this.mapper = new InterferenceRegisterMapper(interferenceGraph, ssaMethod.getRegCount());
        this.minimizeRegisters = minimizeRegisters;
        this.paramRangeEnd = ssaMethod.getParamWidth();
        (this.reservedRopRegs = new BitSet(this.paramRangeEnd * 2)).set(0, this.paramRangeEnd);
        this.usedRopRegs = new BitSet(this.paramRangeEnd * 2);
        this.localVariables = new TreeMap<LocalItem, ArrayList<RegisterSpec>>();
        this.moveResultPseudoInsns = new ArrayList<NormalSsaInsn>();
        this.invokeRangeInsns = new ArrayList<NormalSsaInsn>();
        this.phiInsns = new ArrayList<PhiInsn>();
    }
    
    private void addMapping(final RegisterSpec registerSpec, final int n) {
        final int reg = registerSpec.getReg();
        if (!this.ssaRegsMapped.get(reg) && this.canMapReg(registerSpec, n)) {
            final int category = registerSpec.getCategory();
            this.mapper.addMapping(registerSpec.getReg(), n, category);
            this.ssaRegsMapped.set(reg);
            this.usedRopRegs.set(n, n + category);
            return;
        }
        throw new RuntimeException("attempt to add invalid register mapping");
    }
    
    private void adjustAndMapSourceRangeRange(final NormalSsaInsn normalSsaInsn) {
        int rangeAndAdjust = this.findRangeAndAdjust(normalSsaInsn);
        final RegisterSpecList sources = normalSsaInsn.getSources();
        int size = sources.size();
        int n = rangeAndAdjust;
        int n2 = 0;
        while (true) {
            final int n3 = n;
            if (n2 >= size) {
                break;
            }
            final RegisterSpec value = sources.get(n2);
            final int reg = value.getReg();
            final int category = value.getCategory();
            final int n4 = n3 + category;
            int n6;
            int n7;
            if (this.ssaRegsMapped.get(reg)) {
                final int n5 = size;
                n6 = rangeAndAdjust;
                n7 = n5;
            }
            else {
                final LocalItem localItemForReg = this.getLocalItemForReg(reg);
                this.addMapping(value, n3);
                int n8 = rangeAndAdjust;
                int n9 = size;
                if (localItemForReg != null) {
                    this.markReserved(n3, category);
                    final ArrayList<RegisterSpec> list = this.localVariables.get(localItemForReg);
                    final int size2 = list.size();
                    int n10 = 0;
                    while (true) {
                        n8 = rangeAndAdjust;
                        n9 = size;
                        if (n10 >= size2) {
                            break;
                        }
                        final RegisterSpec registerSpec = list.get(n10);
                        if (-1 == sources.indexOfRegister(registerSpec.getReg())) {
                            this.tryMapReg(registerSpec, n3, category);
                        }
                        ++n10;
                    }
                }
                n6 = n8;
                n7 = n9;
            }
            ++n2;
            final int n11 = n7;
            rangeAndAdjust = n6;
            size = n11;
            n = n4;
        }
    }
    
    private void analyzeInstructions() {
        this.ssaMeth.forEachInsn(new SsaInsn.Visitor() {
            private void processInsn(final SsaInsn ssaInsn) {
                final RegisterSpec localAssignment = ssaInsn.getLocalAssignment();
                if (localAssignment != null) {
                    final LocalItem localItem = localAssignment.getLocalItem();
                    ArrayList<RegisterSpec> list;
                    if ((list = FirstFitLocalCombiningAllocator.this.localVariables.get(localItem)) == null) {
                        list = new ArrayList<RegisterSpec>();
                        FirstFitLocalCombiningAllocator.this.localVariables.put(localItem, list);
                    }
                    list.add(localAssignment);
                }
                if (ssaInsn instanceof NormalSsaInsn) {
                    if (ssaInsn.getOpcode().getOpcode() == 56) {
                        FirstFitLocalCombiningAllocator.this.moveResultPseudoInsns.add(ssaInsn);
                        return;
                    }
                    if (Optimizer.getAdvice().requiresSourcesInOrder(ssaInsn.getOriginalRopInsn().getOpcode(), ssaInsn.getSources())) {
                        FirstFitLocalCombiningAllocator.this.invokeRangeInsns.add(ssaInsn);
                    }
                }
                else if (ssaInsn instanceof PhiInsn) {
                    FirstFitLocalCombiningAllocator.this.phiInsns.add(ssaInsn);
                }
            }
            
            @Override
            public void visitMoveInsn(final NormalSsaInsn normalSsaInsn) {
                this.processInsn(normalSsaInsn);
            }
            
            @Override
            public void visitNonMoveInsn(final NormalSsaInsn normalSsaInsn) {
                this.processInsn(normalSsaInsn);
            }
            
            @Override
            public void visitPhiInsn(final PhiInsn phiInsn) {
                this.processInsn(phiInsn);
            }
        });
    }
    
    private boolean canMapReg(final RegisterSpec registerSpec, final int n) {
        return !this.spansParamRange(n, registerSpec.getCategory()) && !this.mapper.interferes(registerSpec, n);
    }
    
    private boolean canMapRegs(final ArrayList<RegisterSpec> list, final int n) {
        for (final RegisterSpec registerSpec : list) {
            if (this.ssaRegsMapped.get(registerSpec.getReg())) {
                continue;
            }
            if (!this.canMapReg(registerSpec, n)) {
                return false;
            }
        }
        return true;
    }
    
    private int findAnyFittingRange(final NormalSsaInsn normalSsaInsn, final int n, final int[] array, final BitSet set) {
        Alignment alignment = Alignment.UNSPECIFIED;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i] == 2) {
                if (isEven(n2)) {
                    ++n3;
                }
                else {
                    ++n4;
                }
                n2 += 2;
            }
            else {
                ++n2;
            }
        }
        if (n4 > n3) {
            if (isEven(this.paramRangeEnd)) {
                alignment = Alignment.ODD;
            }
            else {
                alignment = Alignment.EVEN;
            }
        }
        else if (n3 > 0) {
            if (isEven(this.paramRangeEnd)) {
                alignment = Alignment.EVEN;
            }
            else {
                alignment = Alignment.ODD;
            }
        }
        int paramRangeEnd = this.paramRangeEnd;
        int nextUnreservedRopReg;
        while (true) {
            nextUnreservedRopReg = this.findNextUnreservedRopReg(paramRangeEnd, n, alignment);
            if (this.fitPlanForRange(nextUnreservedRopReg, normalSsaInsn, array, set) >= 0) {
                break;
            }
            paramRangeEnd = nextUnreservedRopReg + 1;
            set.clear();
        }
        return nextUnreservedRopReg;
    }
    
    private int findNextUnreservedRopReg(final int n, final int n2) {
        return this.findNextUnreservedRopReg(n, n2, this.getAlignment(n2));
    }
    
    private int findNextUnreservedRopReg(int n, final int n2, final Alignment alignment) {
        n = alignment.nextClearBit(this.reservedRopRegs, n);
        while (true) {
            int n3;
            for (n3 = 1; n3 < n2 && !this.reservedRopRegs.get(n + n3); ++n3) {}
            if (n3 == n2) {
                break;
            }
            n = alignment.nextClearBit(this.reservedRopRegs, n + n3);
        }
        return n;
    }
    
    private int findRangeAndAdjust(final NormalSsaInsn normalSsaInsn) {
        final RegisterSpecList sources = normalSsaInsn.getSources();
        final int size = sources.size();
        final int[] array = new int[size];
        int n = 0;
        for (int i = 0; i < size; ++i) {
            array[i] = sources.get(i).getCategory();
            n += array[i];
        }
        int n2 = 0;
        BitSet set = null;
        int n3 = -1;
        int n4 = Integer.MIN_VALUE;
        int n5 = 0;
        int n6;
        BitSet set2;
        while (true) {
            n6 = n3;
            set2 = set;
            if (n5 >= size) {
                break;
            }
            final int reg = sources.get(n5).getReg();
            int n7 = n2;
            if (n5 != 0) {
                n7 = n2 - array[n5 - 1];
            }
            int n8;
            int n9;
            BitSet set3;
            if (!this.ssaRegsMapped.get(reg)) {
                n8 = n4;
                n9 = n3;
                set3 = set;
            }
            else {
                final int n10 = this.mapper.oldToNew(reg) + n7;
                n8 = n4;
                n9 = n3;
                set3 = set;
                if (n10 >= 0) {
                    if (this.spansParamRange(n10, n)) {
                        n8 = n4;
                        n9 = n3;
                        set3 = set;
                    }
                    else {
                        final BitSet set4 = new BitSet(size);
                        final int fitPlanForRange = this.fitPlanForRange(n10, normalSsaInsn, array, set4);
                        if (fitPlanForRange < 0) {
                            n8 = n4;
                            n9 = n3;
                            set3 = set;
                        }
                        else {
                            final int n11 = fitPlanForRange - set4.cardinality();
                            if (n11 > (n8 = n4)) {
                                n8 = n11;
                                n3 = n10;
                                set = set4;
                            }
                            n9 = n3;
                            set3 = set;
                            if (fitPlanForRange == n) {
                                n6 = n3;
                                set2 = set;
                                break;
                            }
                        }
                    }
                }
            }
            ++n5;
            n4 = n8;
            n3 = n9;
            n2 = n7;
            set = set3;
        }
        int anyFittingRange;
        if ((anyFittingRange = n6) == -1) {
            set2 = new BitSet(size);
            anyFittingRange = this.findAnyFittingRange(normalSsaInsn, n, array, set2);
        }
        for (int j = set2.nextSetBit(0); j >= 0; j = set2.nextSetBit(j + 1)) {
            normalSsaInsn.changeOneSource(j, this.insertMoveBefore(normalSsaInsn, sources.get(j)));
        }
        return anyFittingRange;
    }
    
    private int findRopRegForLocal(int n, final int n2) {
        final Alignment alignment = this.getAlignment(n2);
        n = alignment.nextClearBit(this.usedRopRegs, n);
        while (true) {
            int n3;
            for (n3 = 1; n3 < n2 && !this.usedRopRegs.get(n + n3); ++n3) {}
            if (n3 == n2) {
                break;
            }
            n = alignment.nextClearBit(this.usedRopRegs, n + n3);
        }
        return n;
    }
    
    private int fitPlanForRange(int n, final NormalSsaInsn normalSsaInsn, final int[] array, final BitSet set) {
        final RegisterSpecList sources = normalSsaInsn.getSources();
        final int size = sources.size();
        final int n2 = 0;
        final RegisterSpecList ssaSetToSpecs = this.ssaSetToSpecs(normalSsaInsn.getBlock().getLiveOutRegs());
        final BitSet set2 = new BitSet(this.ssaMeth.getRegCount());
        int n3 = 0;
        int n4 = n;
        n = n2;
        int n5;
        while (true) {
            n5 = n;
            if (n3 >= size) {
                break;
            }
            final RegisterSpec value = sources.get(n3);
            final int reg = value.getReg();
            final int n6 = array[n3];
            int n7 = n4;
            if (n3 != 0) {
                n7 = n4 + array[n3 - 1];
            }
            if (this.ssaRegsMapped.get(reg) && this.mapper.oldToNew(reg) == n7) {
                n += n6;
            }
            else {
                if (this.rangeContainsReserved(n7, n6)) {
                    return -1;
                }
                if (!this.ssaRegsMapped.get(reg) && this.canMapReg(value, n7) && !set2.get(reg)) {
                    n += n6;
                }
                else {
                    if (this.mapper.areAnyPinned(ssaSetToSpecs, n7, n6) || this.mapper.areAnyPinned(sources, n7, n6)) {
                        n5 = -1;
                        break;
                    }
                    set.set(n3);
                }
            }
            set2.set(reg);
            ++n3;
            n4 = n7;
        }
        return n5;
    }
    
    private Alignment getAlignment(final int n) {
        Alignment alignment = Alignment.UNSPECIFIED;
        if (n == 2) {
            if (isEven(this.paramRangeEnd)) {
                return Alignment.EVEN;
            }
            alignment = Alignment.ODD;
        }
        return alignment;
    }
    
    private LocalItem getLocalItemForReg(final int n) {
        for (final Map.Entry<LocalItem, ArrayList<RegisterSpec>> entry : this.localVariables.entrySet()) {
            final Iterator<RegisterSpec> iterator2 = entry.getValue().iterator();
            while (iterator2.hasNext()) {
                if (iterator2.next().getReg() == n) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
    
    private int getParameterIndexForReg(final int n) {
        final SsaInsn definitionForRegister = this.ssaMeth.getDefinitionForRegister(n);
        if (definitionForRegister == null) {
            return -1;
        }
        final Rop opcode = definitionForRegister.getOpcode();
        if (opcode != null && opcode.getOpcode() == 3) {
            return ((CstInteger)((CstInsn)definitionForRegister.getOriginalRopInsn()).getConstant()).getValue();
        }
        return -1;
    }
    
    private void handleCheckCastResults() {
        for (final NormalSsaInsn normalSsaInsn : this.moveResultPseudoInsns) {
            final RegisterSpec result = normalSsaInsn.getResult();
            final int reg = result.getReg();
            final BitSet predecessors = normalSsaInsn.getBlock().getPredecessors();
            if (predecessors.cardinality() != 1) {
                continue;
            }
            final ArrayList<SsaInsn> insns = this.ssaMeth.getBlocks().get(predecessors.nextSetBit(0)).getInsns();
            final SsaInsn ssaInsn = insns.get(insns.size() - 1);
            if (ssaInsn.getOpcode().getOpcode() != 43) {
                continue;
            }
            final RegisterSpec value = ssaInsn.getSources().get(0);
            final int reg2 = value.getReg();
            final int category = value.getCategory();
            final boolean value2 = this.ssaRegsMapped.get(reg);
            boolean b;
            if (value2 & ((b = this.ssaRegsMapped.get(reg2)) ^ true)) {
                b = this.tryMapReg(value, this.mapper.oldToNew(reg), category);
            }
            boolean tryMapReg = value2;
            if ((value2 ^ true) & b) {
                tryMapReg = this.tryMapReg(result, this.mapper.oldToNew(reg2), category);
            }
            if (!tryMapReg || !b) {
                int n = this.findNextUnreservedRopReg(this.paramRangeEnd, category);
                final ArrayList<RegisterSpec> list = new ArrayList<RegisterSpec>(2);
                list.add(result);
                list.add(value);
                while (!this.tryMapRegs(list, n, category, false)) {
                    n = this.findNextUnreservedRopReg(n + 1, category);
                }
            }
            final boolean b2 = ssaInsn.getOriginalRopInsn().getCatches().size() != 0;
            final int oldToNew = this.mapper.oldToNew(reg);
            if (oldToNew == this.mapper.oldToNew(reg2) || b2) {
                continue;
            }
            ((NormalSsaInsn)ssaInsn).changeOneSource(0, this.insertMoveBefore(ssaInsn, value));
            this.addMapping(ssaInsn.getSources().get(0), oldToNew);
        }
    }
    
    private void handleInvokeRangeInsns() {
        final Iterator<NormalSsaInsn> iterator = this.invokeRangeInsns.iterator();
        while (iterator.hasNext()) {
            this.adjustAndMapSourceRangeRange(iterator.next());
        }
    }
    
    private void handleLocalAssociatedOther() {
        for (final ArrayList<RegisterSpec> list : this.localVariables.values()) {
            int paramRangeEnd = this.paramRangeEnd;
            int n = 0;
            int tryMapRegs;
            do {
                final int size = list.size();
                int n2 = 1;
                int n3;
                for (int i = 0; i < size; ++i, n2 = n3) {
                    final RegisterSpec registerSpec = list.get(i);
                    final int category = registerSpec.getCategory();
                    n3 = n2;
                    if (!this.ssaRegsMapped.get(registerSpec.getReg()) && category > (n3 = n2)) {
                        n3 = category;
                    }
                }
                final int ropRegForLocal = this.findRopRegForLocal(paramRangeEnd, n2);
                tryMapRegs = n;
                if (this.canMapRegs(list, ropRegForLocal)) {
                    tryMapRegs = (this.tryMapRegs(list, ropRegForLocal, n2, true) ? 1 : 0);
                }
                paramRangeEnd = ropRegForLocal + 1;
            } while ((n = tryMapRegs) == 0);
        }
    }
    
    private void handleLocalAssociatedParams() {
        for (final ArrayList<RegisterSpec> list : this.localVariables.values()) {
            final int size = list.size();
            int parameterIndexForReg = -1;
            final boolean b = false;
            int n = 0;
            int category;
            while (true) {
                category = (b ? 1 : 0);
                if (n >= size) {
                    break;
                }
                final RegisterSpec registerSpec = list.get(n);
                parameterIndexForReg = this.getParameterIndexForReg(registerSpec.getReg());
                if (parameterIndexForReg >= 0) {
                    category = registerSpec.getCategory();
                    this.addMapping(registerSpec, parameterIndexForReg);
                    break;
                }
                ++n;
            }
            if (parameterIndexForReg < 0) {
                continue;
            }
            this.tryMapRegs(list, parameterIndexForReg, category, true);
        }
    }
    
    private void handleNormalUnassociated() {
        for (int regCount = this.ssaMeth.getRegCount(), i = 0; i < regCount; ++i) {
            if (!this.ssaRegsMapped.get(i)) {
                final RegisterSpec definitionSpecForSsaReg = this.getDefinitionSpecForSsaReg(i);
                if (definitionSpecForSsaReg != null) {
                    int category;
                    int n;
                    for (category = definitionSpecForSsaReg.getCategory(), n = this.findNextUnreservedRopReg(this.paramRangeEnd, category); !this.canMapReg(definitionSpecForSsaReg, n); n = this.findNextUnreservedRopReg(n + 1, category)) {}
                    this.addMapping(definitionSpecForSsaReg, n);
                }
            }
        }
    }
    
    private void handlePhiInsns() {
        final Iterator<PhiInsn> iterator = this.phiInsns.iterator();
        while (iterator.hasNext()) {
            this.processPhiInsn(iterator.next());
        }
    }
    
    private void handleUnassociatedParameters() {
        for (int regCount = this.ssaMeth.getRegCount(), i = 0; i < regCount; ++i) {
            if (!this.ssaRegsMapped.get(i)) {
                final int parameterIndexForReg = this.getParameterIndexForReg(i);
                final RegisterSpec definitionSpecForSsaReg = this.getDefinitionSpecForSsaReg(i);
                if (parameterIndexForReg >= 0) {
                    this.addMapping(definitionSpecForSsaReg, parameterIndexForReg);
                }
            }
        }
    }
    
    private static boolean isEven(final int n) {
        return (n & 0x1) == 0x0;
    }
    
    private boolean isThisPointerReg(final int n) {
        return n == 0 && !this.ssaMeth.isStatic();
    }
    
    private void markReserved(final int n, final int n2) {
        this.reservedRopRegs.set(n, n + n2, true);
    }
    
    private void printLocalVars() {
        System.out.println("Printing local vars");
        for (final Map.Entry<LocalItem, ArrayList<RegisterSpec>> entry : this.localVariables.entrySet()) {
            final StringBuilder sb = new StringBuilder();
            sb.append('{');
            sb.append(' ');
            for (final RegisterSpec registerSpec : entry.getValue()) {
                sb.append('v');
                sb.append(registerSpec.getReg());
                sb.append(' ');
            }
            sb.append('}');
            System.out.printf("Local: %s Registers: %s\n", entry.getKey(), sb);
        }
    }
    
    private void processPhiInsn(final PhiInsn phiInsn) {
        final RegisterSpec result = phiInsn.getResult();
        final int reg = result.getReg();
        final int category = result.getCategory();
        final RegisterSpecList sources = phiInsn.getSources();
        final int size = sources.size();
        final ArrayList<RegisterSpec> list = new ArrayList<RegisterSpec>();
        final Multiset multiset = new Multiset(size + 1);
        if (this.ssaRegsMapped.get(reg)) {
            multiset.add(this.mapper.oldToNew(reg));
        }
        else {
            list.add(result);
        }
        for (int i = 0; i < size; ++i) {
            final RegisterSpec result2 = this.ssaMeth.getDefinitionForRegister(sources.get(i).getReg()).getResult();
            final int reg2 = result2.getReg();
            if (this.ssaRegsMapped.get(reg2)) {
                multiset.add(this.mapper.oldToNew(reg2));
            }
            else {
                list.add(result2);
            }
        }
        for (int j = 0; j < multiset.getSize(); ++j) {
            this.tryMapRegs(list, multiset.getAndRemoveHighestCount(), category, false);
        }
        for (int n = this.findNextUnreservedRopReg(this.paramRangeEnd, category); !this.tryMapRegs(list, n, category, false); n = this.findNextUnreservedRopReg(n + 1, category)) {}
    }
    
    private boolean rangeContainsReserved(final int n, final int n2) {
        for (int i = n; i < n + n2; ++i) {
            if (this.reservedRopRegs.get(i)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean spansParamRange(final int n, final int n2) {
        return n < this.paramRangeEnd && n + n2 > this.paramRangeEnd;
    }
    
    private boolean tryMapReg(final RegisterSpec registerSpec, final int n, final int n2) {
        if (registerSpec.getCategory() <= n2 && !this.ssaRegsMapped.get(registerSpec.getReg()) && this.canMapReg(registerSpec, n)) {
            this.addMapping(registerSpec, n);
            return true;
        }
        return false;
    }
    
    private boolean tryMapRegs(final ArrayList<RegisterSpec> list, final int n, final int n2, final boolean b) {
        int n3 = 0;
        final Iterator<RegisterSpec> iterator = list.iterator();
        while (true) {
            final boolean hasNext = iterator.hasNext();
            final boolean b2 = true;
            if (!hasNext) {
                break;
            }
            final RegisterSpec registerSpec = iterator.next();
            if (this.ssaRegsMapped.get(registerSpec.getReg())) {
                continue;
            }
            final boolean tryMapReg = this.tryMapReg(registerSpec, n, n2);
            boolean b3 = b2;
            if (tryMapReg) {
                b3 = (n3 != 0 && b2);
            }
            if (tryMapReg && b) {
                this.markReserved(n, registerSpec.getCategory());
            }
            n3 = (b3 ? 1 : 0);
        }
        return n3 == 0;
    }
    
    @Override
    public RegisterMapper allocateRegisters() {
        this.analyzeInstructions();
        this.handleLocalAssociatedParams();
        this.handleUnassociatedParameters();
        this.handleInvokeRangeInsns();
        this.handleLocalAssociatedOther();
        this.handleCheckCastResults();
        this.handlePhiInsns();
        this.handleNormalUnassociated();
        return this.mapper;
    }
    
    RegisterSpecList ssaSetToSpecs(final IntSet set) {
        final RegisterSpecList list = new RegisterSpecList(set.elements());
        final IntIterator iterator = set.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            list.set(n, this.getDefinitionSpecForSsaReg(iterator.next()));
            ++n;
        }
        return list;
    }
    
    @Override
    public boolean wantsParamsMovedHigh() {
        return true;
    }
    
    private enum Alignment
    {
        EVEN {
            @Override
            int nextClearBit(final BitSet set, int n) {
                for (n = set.nextClearBit(n); !isEven(n); n = set.nextClearBit(n + 1)) {}
                return n;
            }
        }, 
        ODD {
            @Override
            int nextClearBit(final BitSet set, int n) {
                for (n = set.nextClearBit(n); isEven(n); n = set.nextClearBit(n + 1)) {}
                return n;
            }
        }, 
        UNSPECIFIED {
            @Override
            int nextClearBit(final BitSet set, final int n) {
                return set.nextClearBit(n);
            }
        };
        
        abstract int nextClearBit(final BitSet p0, final int p1);
    }
    
    private static class Multiset
    {
        private final int[] count;
        private final int[] reg;
        private int size;
        
        public Multiset(final int n) {
            this.reg = new int[n];
            this.count = new int[n];
            this.size = 0;
        }
        
        public void add(final int n) {
            for (int i = 0; i < this.size; ++i) {
                if (this.reg[i] == n) {
                    final int[] count = this.count;
                    ++count[i];
                    return;
                }
            }
            this.reg[this.size] = n;
            this.count[this.size] = 1;
            ++this.size;
        }
        
        public int getAndRemoveHighestCount() {
            int n = 0;
            int n2 = -1;
            int n3 = -1;
            int n4;
            for (int i = 0; i < this.size; ++i, n = n4) {
                if ((n4 = n) < this.count[i]) {
                    n3 = i;
                    n2 = this.reg[i];
                    n4 = this.count[i];
                }
            }
            this.count[n3] = 0;
            return n2;
        }
        
        public int getSize() {
            return this.size;
        }
    }
}
