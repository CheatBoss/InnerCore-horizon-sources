package com.android.dx.ssa;

import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;
import com.android.dx.rop.code.*;
import java.util.*;

public class EscapeAnalysis
{
    private ArrayList<EscapeSet> latticeValues;
    private int regCount;
    private SsaMethod ssaMeth;
    
    private EscapeAnalysis(final SsaMethod ssaMeth) {
        this.ssaMeth = ssaMeth;
        this.regCount = ssaMeth.getRegCount();
        this.latticeValues = new ArrayList<EscapeSet>();
    }
    
    private void addEdge(final EscapeSet set, final EscapeSet set2) {
        if (!set2.parentSets.contains(set)) {
            set2.parentSets.add(set);
        }
        if (!set.childSets.contains(set2)) {
            set.childSets.add(set2);
        }
    }
    
    private int findSetIndex(final RegisterSpec registerSpec) {
        int i;
        for (i = 0; i < this.latticeValues.size(); ++i) {
            if (this.latticeValues.get(i).regSet.get(registerSpec.getReg())) {
                return i;
            }
        }
        return i;
    }
    
    private SsaInsn getInsnForMove(final SsaInsn ssaInsn) {
        final ArrayList<SsaInsn> insns = this.ssaMeth.getBlocks().get(ssaInsn.getBlock().getPredecessors().nextSetBit(0)).getInsns();
        return insns.get(insns.size() - 1);
    }
    
    private SsaInsn getMoveForInsn(final SsaInsn ssaInsn) {
        return this.ssaMeth.getBlocks().get(ssaInsn.getBlock().getSuccessors().nextSetBit(0)).getInsns().get(0);
    }
    
    private void insertExceptionThrow(final SsaInsn ssaInsn, final RegisterSpec registerSpec, final HashSet<SsaInsn> set) {
        final CstType cstType = new CstType(Exceptions.TYPE_ArrayIndexOutOfBoundsException);
        this.insertThrowingInsnBefore(ssaInsn, RegisterSpecList.EMPTY, null, 40, cstType);
        final SsaBasicBlock block = ssaInsn.getBlock();
        final SsaBasicBlock insertNewSuccessor = block.insertNewSuccessor(block.getPrimarySuccessor());
        final SsaInsn ssaInsn2 = insertNewSuccessor.getInsns().get(0);
        final RegisterSpec make = RegisterSpec.make(this.ssaMeth.makeNewSsaReg(), cstType);
        this.insertPlainInsnBefore(ssaInsn2, RegisterSpecList.EMPTY, make, 56, null);
        final SsaBasicBlock insertNewSuccessor2 = insertNewSuccessor.insertNewSuccessor(insertNewSuccessor.getPrimarySuccessor());
        final SsaInsn ssaInsn3 = insertNewSuccessor2.getInsns().get(0);
        this.insertThrowingInsnBefore(ssaInsn3, RegisterSpecList.make(make, registerSpec), null, 52, new CstMethodRef(cstType, new CstNat(new CstString("<init>"), new CstString("(I)V"))));
        set.add(ssaInsn3);
        final SsaBasicBlock insertNewSuccessor3 = insertNewSuccessor2.insertNewSuccessor(insertNewSuccessor2.getPrimarySuccessor());
        final SsaInsn ssaInsn4 = insertNewSuccessor3.getInsns().get(0);
        this.insertThrowingInsnBefore(ssaInsn4, RegisterSpecList.make(make), null, 35, null);
        insertNewSuccessor3.replaceSuccessor(insertNewSuccessor3.getPrimarySuccessorIndex(), this.ssaMeth.getExitBlock().getIndex());
        set.add(ssaInsn4);
    }
    
    private void insertPlainInsnBefore(final SsaInsn ssaInsn, final RegisterSpecList list, final RegisterSpec registerSpec, final int n, final Constant constant) {
        final Insn originalRopInsn = ssaInsn.getOriginalRopInsn();
        Rop rop;
        if (n == 56) {
            rop = Rops.opMoveResultPseudo(registerSpec.getType());
        }
        else {
            rop = Rops.ropFor(n, registerSpec, list, constant);
        }
        Insn insn;
        if (constant == null) {
            insn = new PlainInsn(rop, originalRopInsn.getPosition(), registerSpec, list);
        }
        else {
            insn = new PlainCstInsn(rop, originalRopInsn.getPosition(), registerSpec, list, constant);
        }
        final NormalSsaInsn normalSsaInsn = new NormalSsaInsn(insn, ssaInsn.getBlock());
        final ArrayList<SsaInsn> insns = ssaInsn.getBlock().getInsns();
        insns.add(insns.lastIndexOf(ssaInsn), normalSsaInsn);
        this.ssaMeth.onInsnAdded(normalSsaInsn);
    }
    
    private void insertThrowingInsnBefore(final SsaInsn ssaInsn, final RegisterSpecList list, final RegisterSpec registerSpec, final int n, final Constant constant) {
        final Insn originalRopInsn = ssaInsn.getOriginalRopInsn();
        final Rop rop = Rops.ropFor(n, registerSpec, list, constant);
        Insn insn;
        if (constant == null) {
            insn = new ThrowingInsn(rop, originalRopInsn.getPosition(), list, StdTypeList.EMPTY);
        }
        else {
            insn = new ThrowingCstInsn(rop, originalRopInsn.getPosition(), list, StdTypeList.EMPTY, constant);
        }
        final NormalSsaInsn normalSsaInsn = new NormalSsaInsn(insn, ssaInsn.getBlock());
        final ArrayList<SsaInsn> insns = ssaInsn.getBlock().getInsns();
        insns.add(insns.lastIndexOf(ssaInsn), normalSsaInsn);
        this.ssaMeth.onInsnAdded(normalSsaInsn);
    }
    
    private void movePropagate() {
        for (int i = 0; i < this.ssaMeth.getRegCount(); ++i) {
            final SsaInsn definitionForRegister = this.ssaMeth.getDefinitionForRegister(i);
            if (definitionForRegister != null && definitionForRegister.getOpcode() != null) {
                if (definitionForRegister.getOpcode().getOpcode() == 2) {
                    final ArrayList<SsaInsn>[] useListCopy = this.ssaMeth.getUseListCopy();
                    final RegisterSpec value = definitionForRegister.getSources().get(0);
                    final RegisterSpec result = definitionForRegister.getResult();
                    if (value.getReg() >= this.regCount || result.getReg() >= this.regCount) {
                        final RegisterMapper registerMapper = new RegisterMapper() {
                            @Override
                            public int getNewRegisterCount() {
                                return EscapeAnalysis.this.ssaMeth.getRegCount();
                            }
                            
                            @Override
                            public RegisterSpec map(final RegisterSpec registerSpec) {
                                if (registerSpec.getReg() == result.getReg()) {
                                    return value;
                                }
                                return registerSpec;
                            }
                        };
                        final Iterator<SsaInsn> iterator = useListCopy[result.getReg()].iterator();
                        while (iterator.hasNext()) {
                            iterator.next().mapSourceRegisters(registerMapper);
                        }
                    }
                }
            }
        }
    }
    
    public static void process(final SsaMethod ssaMethod) {
        new EscapeAnalysis(ssaMethod).run();
    }
    
    private void processInsn(final SsaInsn ssaInsn) {
        final int opcode = ssaInsn.getOpcode().getOpcode();
        final RegisterSpec result = ssaInsn.getResult();
        if (opcode == 56 && result.getTypeBearer().getBasicType() == 9) {
            this.processRegister(result, this.processMoveResultPseudoInsn(ssaInsn));
            return;
        }
        if (opcode == 3 && result.getTypeBearer().getBasicType() == 9) {
            final EscapeSet set = new EscapeSet(result.getReg(), this.regCount, EscapeState.NONE);
            this.latticeValues.add(set);
            this.processRegister(result, set);
            return;
        }
        if (opcode == 55 && result.getTypeBearer().getBasicType() == 9) {
            final EscapeSet set2 = new EscapeSet(result.getReg(), this.regCount, EscapeState.NONE);
            this.latticeValues.add(set2);
            this.processRegister(result, set2);
        }
    }
    
    private EscapeSet processMoveResultPseudoInsn(SsaInsn insnForMove) {
        final RegisterSpec result = insnForMove.getResult();
        insnForMove = this.getInsnForMove(insnForMove);
        final int opcode = insnForMove.getOpcode().getOpcode();
        EscapeSet set = null;
        Label_0300: {
            Label_0281: {
                if (opcode != 5) {
                    Label_0176: {
                        if (opcode != 38) {
                            switch (opcode) {
                                default: {
                                    switch (opcode) {
                                        default: {
                                            return null;
                                        }
                                        case 46: {
                                            set = new EscapeSet(result.getReg(), this.regCount, EscapeState.GLOBAL);
                                            break Label_0300;
                                        }
                                        case 45: {
                                            break Label_0176;
                                        }
                                    }
                                    break;
                                }
                                case 41:
                                case 42: {
                                    if (insnForMove.getSources().get(0).getTypeBearer().isConstant()) {
                                        set = new EscapeSet(result.getReg(), this.regCount, EscapeState.NONE);
                                        set.replaceableArray = true;
                                    }
                                    else {
                                        set = new EscapeSet(result.getReg(), this.regCount, EscapeState.GLOBAL);
                                    }
                                    break Label_0300;
                                }
                                case 43: {
                                    break;
                                }
                                case 40: {
                                    break Label_0281;
                                }
                            }
                        }
                    }
                    final RegisterSpec value = insnForMove.getSources().get(0);
                    final int setIndex = this.findSetIndex(value);
                    if (setIndex != this.latticeValues.size()) {
                        final EscapeSet set2 = this.latticeValues.get(setIndex);
                        set2.regSet.set(result.getReg());
                        return set2;
                    }
                    if (value.getType() == Type.KNOWN_NULL) {
                        set = new EscapeSet(result.getReg(), this.regCount, EscapeState.NONE);
                    }
                    else {
                        set = new EscapeSet(result.getReg(), this.regCount, EscapeState.GLOBAL);
                    }
                    break Label_0300;
                }
            }
            set = new EscapeSet(result.getReg(), this.regCount, EscapeState.NONE);
        }
        this.latticeValues.add(set);
        return set;
    }
    
    private void processPhiUse(final SsaInsn ssaInsn, final EscapeSet set, final ArrayList<RegisterSpec> list) {
        final int setIndex = this.findSetIndex(ssaInsn.getResult());
        if (setIndex != this.latticeValues.size()) {
            final EscapeSet set2 = this.latticeValues.get(setIndex);
            if (set2 != set) {
                set.replaceableArray = false;
                set.regSet.or(set2.regSet);
                if (set.escape.compareTo(set2.escape) < 0) {
                    set.escape = set2.escape;
                }
                this.replaceNode(set, set2);
                this.latticeValues.remove(setIndex);
            }
            return;
        }
        set.regSet.set(ssaInsn.getResult().getReg());
        list.add(ssaInsn.getResult());
    }
    
    private void processRegister(RegisterSpec registerSpec, final EscapeSet set) {
        final ArrayList<RegisterSpec> list = new ArrayList<RegisterSpec>();
        list.add(registerSpec);
        while (!list.isEmpty()) {
            registerSpec = list.remove(list.size() - 1);
            for (final SsaInsn ssaInsn : this.ssaMeth.getUseListForRegister(registerSpec.getReg())) {
                if (ssaInsn.getOpcode() == null) {
                    this.processPhiUse(ssaInsn, set, list);
                }
                else {
                    this.processUse(registerSpec, ssaInsn, set, list);
                }
            }
        }
    }
    
    private void processUse(final RegisterSpec registerSpec, final SsaInsn ssaInsn, final EscapeSet set, final ArrayList<RegisterSpec> list) {
        final int opcode = ssaInsn.getOpcode().getOpcode();
        if (opcode != 2) {
            Label_0384: {
                if (opcode != 33 && opcode != 35) {
                    if (opcode != 43) {
                        switch (opcode) {
                            default: {
                                Label_0161: {
                                    switch (opcode) {
                                        default: {
                                            switch (opcode) {
                                                default: {
                                                    return;
                                                }
                                                case 48: {
                                                    set.escape = EscapeState.GLOBAL;
                                                    return;
                                                }
                                                case 47: {
                                                    break Label_0161;
                                                }
                                                case 49:
                                                case 50:
                                                case 51:
                                                case 52:
                                                case 53: {
                                                    break Label_0384;
                                                }
                                            }
                                            break;
                                        }
                                        case 39: {
                                            if (!ssaInsn.getSources().get(2).getTypeBearer().isConstant()) {
                                                set.replaceableArray = false;
                                                break;
                                            }
                                            break;
                                        }
                                        case 38: {
                                            if (!ssaInsn.getSources().get(1).getTypeBearer().isConstant()) {
                                                set.replaceableArray = false;
                                            }
                                            return;
                                        }
                                    }
                                }
                                if (ssaInsn.getSources().get(0).getTypeBearer().getBasicType() != 9) {
                                    return;
                                }
                                set.replaceableArray = false;
                                final RegisterSpecList sources = ssaInsn.getSources();
                                if (sources.get(0).getReg() == registerSpec.getReg()) {
                                    final int setIndex = this.findSetIndex(sources.get(1));
                                    if (setIndex != this.latticeValues.size()) {
                                        final EscapeSet set2 = this.latticeValues.get(setIndex);
                                        this.addEdge(set2, set);
                                        if (set.escape.compareTo(set2.escape) < 0) {
                                            set.escape = set2.escape;
                                        }
                                    }
                                    return;
                                }
                                final int setIndex2 = this.findSetIndex(sources.get(0));
                                if (setIndex2 != this.latticeValues.size()) {
                                    final EscapeSet set3 = this.latticeValues.get(setIndex2);
                                    this.addEdge(set, set3);
                                    if (set3.escape.compareTo(set.escape) < 0) {
                                        set3.escape = set.escape;
                                    }
                                }
                                return;
                            }
                            case 7:
                            case 8: {
                                break;
                            }
                        }
                    }
                    if (set.escape.compareTo(EscapeState.METHOD) < 0) {
                        set.escape = EscapeState.METHOD;
                    }
                    return;
                }
            }
            set.escape = EscapeState.INTER;
            return;
        }
        set.regSet.set(ssaInsn.getResult().getReg());
        list.add(ssaInsn.getResult());
    }
    
    private void replaceDef(final SsaInsn ssaInsn, final SsaInsn ssaInsn2, final int n, final ArrayList<RegisterSpec> list) {
        final Type type = ssaInsn.getResult().getType();
        for (int i = 0; i < n; ++i) {
            final Constant zero = Zeroes.zeroFor(type.getComponentType());
            final RegisterSpec make = RegisterSpec.make(this.ssaMeth.makeNewSsaReg(), (TypeBearer)zero);
            list.add(make);
            this.insertPlainInsnBefore(ssaInsn, RegisterSpecList.EMPTY, make, 5, zero);
        }
    }
    
    private void replaceNode(final EscapeSet set, final EscapeSet set2) {
        for (final EscapeSet set3 : set2.parentSets) {
            set3.childSets.remove(set2);
            set3.childSets.add(set);
            set.parentSets.add(set3);
        }
        for (final EscapeSet set4 : set2.childSets) {
            set4.parentSets.remove(set2);
            set4.parentSets.add(set);
            set.childSets.add(set4);
        }
    }
    
    private void replaceUse(SsaInsn moveForInsn, SsaInsn moveForInsn2, final ArrayList<RegisterSpec> list, final HashSet<SsaInsn> set) {
        final int size = list.size();
        final int opcode = moveForInsn.getOpcode().getOpcode();
        int i = 0;
        if (opcode == 34) {
            final TypeBearer typeBearer = moveForInsn2.getSources().get(0).getTypeBearer();
            moveForInsn = this.getMoveForInsn(moveForInsn);
            this.insertPlainInsnBefore(moveForInsn, RegisterSpecList.EMPTY, moveForInsn.getResult(), 5, (Constant)typeBearer);
            set.add(moveForInsn);
            return;
        }
        if (opcode == 54) {
            return;
        }
        if (opcode != 57) {
            switch (opcode) {
                case 39: {
                    final RegisterSpecList sources = moveForInsn.getSources();
                    final int intBits = ((CstLiteralBits)sources.get(2).getTypeBearer()).getIntBits();
                    if (intBits < size) {
                        final RegisterSpec value = sources.get(0);
                        final RegisterSpec withReg = value.withReg(list.get(intBits).getReg());
                        this.insertPlainInsnBefore(moveForInsn, RegisterSpecList.make(value), withReg, 2, null);
                        list.set(intBits, withReg.withSimpleType());
                        break;
                    }
                    this.insertExceptionThrow(moveForInsn, sources.get(2), set);
                }
                case 38: {
                    moveForInsn2 = this.getMoveForInsn(moveForInsn);
                    final RegisterSpecList sources2 = moveForInsn.getSources();
                    final int intBits2 = ((CstLiteralBits)sources2.get(1).getTypeBearer()).getIntBits();
                    if (intBits2 < size) {
                        final RegisterSpec registerSpec = list.get(intBits2);
                        this.insertPlainInsnBefore(moveForInsn2, RegisterSpecList.make(registerSpec), registerSpec.withReg(moveForInsn2.getResult().getReg()), 2, null);
                    }
                    else {
                        this.insertExceptionThrow(moveForInsn2, sources2.get(1), set);
                        set.add(moveForInsn2.getBlock().getInsns().get(2));
                    }
                    set.add(moveForInsn2);
                }
            }
            return;
        }
        final ArrayList<Constant> initValues = ((FillArrayDataInsn)moveForInsn.getOriginalRopInsn()).getInitValues();
        while (i < size) {
            final RegisterSpec make = RegisterSpec.make(list.get(i).getReg(), (TypeBearer)initValues.get(i));
            this.insertPlainInsnBefore(moveForInsn, RegisterSpecList.EMPTY, make, 5, initValues.get(i));
            list.set(i, make);
            ++i;
        }
    }
    
    private void run() {
        this.ssaMeth.forEachBlockDepthFirstDom(new SsaBasicBlock.Visitor() {
            @Override
            public void visitBlock(final SsaBasicBlock ssaBasicBlock, final SsaBasicBlock ssaBasicBlock2) {
                ssaBasicBlock.forEachInsn(new SsaInsn.Visitor() {
                    @Override
                    public void visitMoveInsn(final NormalSsaInsn normalSsaInsn) {
                    }
                    
                    @Override
                    public void visitNonMoveInsn(final NormalSsaInsn normalSsaInsn) {
                        EscapeAnalysis.this.processInsn(normalSsaInsn);
                    }
                    
                    @Override
                    public void visitPhiInsn(final PhiInsn phiInsn) {
                    }
                });
            }
        });
        for (final EscapeSet set : this.latticeValues) {
            if (set.escape != EscapeState.NONE) {
                for (final EscapeSet set2 : set.childSets) {
                    if (set.escape.compareTo(set2.escape) > 0) {
                        set2.escape = set.escape;
                    }
                }
            }
        }
        this.scalarReplacement();
    }
    
    private void scalarReplacement() {
        for (final EscapeSet set : this.latticeValues) {
            if (set.replaceableArray) {
                if (set.escape != EscapeState.NONE) {
                    continue;
                }
                final int nextSetBit = set.regSet.nextSetBit(0);
                final SsaInsn definitionForRegister = this.ssaMeth.getDefinitionForRegister(nextSetBit);
                final SsaInsn insnForMove = this.getInsnForMove(definitionForRegister);
                final int intBits = ((CstLiteralBits)insnForMove.getSources().get(0).getTypeBearer()).getIntBits();
                final ArrayList list = new ArrayList<RegisterSpec>(intBits);
                final HashSet<SsaInsn> set2 = new HashSet<SsaInsn>();
                this.replaceDef(definitionForRegister, insnForMove, intBits, (ArrayList<RegisterSpec>)list);
                set2.add(insnForMove);
                set2.add(definitionForRegister);
                for (final SsaInsn ssaInsn : this.ssaMeth.getUseListForRegister(nextSetBit)) {
                    this.replaceUse(ssaInsn, insnForMove, (ArrayList<RegisterSpec>)list, set2);
                    set2.add(ssaInsn);
                }
                this.ssaMeth.deleteInsns(set2);
                this.ssaMeth.onInsnsChanged();
                SsaConverter.updateSsaMethod(this.ssaMeth, this.regCount);
                this.movePropagate();
            }
        }
    }
    
    static class EscapeSet
    {
        ArrayList<EscapeSet> childSets;
        EscapeState escape;
        ArrayList<EscapeSet> parentSets;
        BitSet regSet;
        boolean replaceableArray;
        
        EscapeSet(final int n, final int n2, final EscapeState escape) {
            (this.regSet = new BitSet(n2)).set(n);
            this.escape = escape;
            this.childSets = new ArrayList<EscapeSet>();
            this.parentSets = new ArrayList<EscapeSet>();
            this.replaceableArray = false;
        }
    }
    
    public enum EscapeState
    {
        GLOBAL, 
        INTER, 
        METHOD, 
        NONE, 
        TOP;
    }
}
