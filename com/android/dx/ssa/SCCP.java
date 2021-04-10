package com.android.dx.ssa;

import java.util.*;
import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;
import com.android.dx.rop.code.*;

public class SCCP
{
    private static final int CONSTANT = 1;
    private static final int TOP = 0;
    private static final int VARYING = 2;
    private ArrayList<SsaInsn> branchWorklist;
    private ArrayList<SsaBasicBlock> cfgPhiWorklist;
    private ArrayList<SsaBasicBlock> cfgWorklist;
    private BitSet executableBlocks;
    private Constant[] latticeConstants;
    private int[] latticeValues;
    private int regCount;
    private SsaMethod ssaMeth;
    private ArrayList<SsaInsn> ssaWorklist;
    private ArrayList<SsaInsn> varyingWorklist;
    
    private SCCP(final SsaMethod ssaMeth) {
        this.ssaMeth = ssaMeth;
        this.regCount = ssaMeth.getRegCount();
        this.latticeValues = new int[this.regCount];
        this.latticeConstants = new Constant[this.regCount];
        this.cfgWorklist = new ArrayList<SsaBasicBlock>();
        this.cfgPhiWorklist = new ArrayList<SsaBasicBlock>();
        this.executableBlocks = new BitSet(ssaMeth.getBlocks().size());
        this.ssaWorklist = new ArrayList<SsaInsn>();
        this.varyingWorklist = new ArrayList<SsaInsn>();
        this.branchWorklist = new ArrayList<SsaInsn>();
        for (int i = 0; i < this.regCount; ++i) {
            this.latticeValues[i] = 0;
            this.latticeConstants[i] = null;
        }
    }
    
    private void addBlockToWorklist(final SsaBasicBlock ssaBasicBlock) {
        if (!this.executableBlocks.get(ssaBasicBlock.getIndex())) {
            this.cfgWorklist.add(ssaBasicBlock);
            this.executableBlocks.set(ssaBasicBlock.getIndex());
            return;
        }
        this.cfgPhiWorklist.add(ssaBasicBlock);
    }
    
    private void addUsersToWorklist(final int n, final int n2) {
        if (n2 == 2) {
            final Iterator<SsaInsn> iterator = this.ssaMeth.getUseListForRegister(n).iterator();
            while (iterator.hasNext()) {
                this.varyingWorklist.add(iterator.next());
            }
        }
        else {
            final Iterator<SsaInsn> iterator2 = this.ssaMeth.getUseListForRegister(n).iterator();
            while (iterator2.hasNext()) {
                this.ssaWorklist.add(iterator2.next());
            }
        }
    }
    
    private static String latticeValName(final int n) {
        switch (n) {
            default: {
                return "UNKNOWN";
            }
            case 2: {
                return "VARYING";
            }
            case 1: {
                return "CONSTANT";
            }
            case 0: {
                return "TOP";
            }
        }
    }
    
    public static void process(final SsaMethod ssaMethod) {
        new SCCP(ssaMethod).run();
    }
    
    private void replaceBranches() {
        for (final SsaInsn ssaInsn : this.branchWorklist) {
            int n = -1;
            final SsaBasicBlock block = ssaInsn.getBlock();
            final int size = block.getSuccessorList().size();
            for (int i = 0; i < size; ++i) {
                final int value = block.getSuccessorList().get(i);
                if (!this.executableBlocks.get(value)) {
                    n = value;
                }
            }
            if (size == 2) {
                if (n == -1) {
                    continue;
                }
                block.replaceLastInsn(new PlainInsn(Rops.GOTO, ssaInsn.getOriginalRopInsn().getPosition(), null, RegisterSpecList.EMPTY));
                block.removeSuccessor(n);
            }
        }
    }
    
    private void replaceConstants() {
        for (int i = 0; i < this.regCount; ++i) {
            if (this.latticeValues[i] == 1) {
                if (this.latticeConstants[i] instanceof TypedConstant) {
                    final SsaInsn definitionForRegister = this.ssaMeth.getDefinitionForRegister(i);
                    if (!definitionForRegister.getResult().getTypeBearer().isConstant()) {
                        definitionForRegister.setResult(definitionForRegister.getResult().withType((TypeBearer)this.latticeConstants[i]));
                        for (final SsaInsn ssaInsn : this.ssaMeth.getUseListForRegister(i)) {
                            if (ssaInsn.isPhiOrMove()) {
                                continue;
                            }
                            final NormalSsaInsn normalSsaInsn = (NormalSsaInsn)ssaInsn;
                            final RegisterSpecList sources = ssaInsn.getSources();
                            final int indexOfRegister = sources.indexOfRegister(i);
                            normalSsaInsn.changeOneSource(indexOfRegister, sources.get(indexOfRegister).withType((TypeBearer)this.latticeConstants[i]));
                        }
                    }
                }
            }
        }
    }
    
    private void run() {
        this.addBlockToWorklist(this.ssaMeth.getEntryBlock());
        while (true) {
            if (this.cfgWorklist.isEmpty() && this.cfgPhiWorklist.isEmpty() && this.ssaWorklist.isEmpty()) {
                if (this.varyingWorklist.isEmpty()) {
                    break;
                }
            }
            while (!this.cfgWorklist.isEmpty()) {
                this.simulateBlock(this.cfgWorklist.remove(this.cfgWorklist.size() - 1));
            }
            while (!this.cfgPhiWorklist.isEmpty()) {
                this.simulatePhiBlock(this.cfgPhiWorklist.remove(this.cfgPhiWorklist.size() - 1));
            }
            while (!this.varyingWorklist.isEmpty()) {
                final SsaInsn ssaInsn = this.varyingWorklist.remove(this.varyingWorklist.size() - 1);
                if (!this.executableBlocks.get(ssaInsn.getBlock().getIndex())) {
                    continue;
                }
                if (ssaInsn instanceof PhiInsn) {
                    this.simulatePhi((PhiInsn)ssaInsn);
                }
                else {
                    this.simulateStmt(ssaInsn);
                }
            }
            while (!this.ssaWorklist.isEmpty()) {
                final SsaInsn ssaInsn2 = this.ssaWorklist.remove(this.ssaWorklist.size() - 1);
                if (!this.executableBlocks.get(ssaInsn2.getBlock().getIndex())) {
                    continue;
                }
                if (ssaInsn2 instanceof PhiInsn) {
                    this.simulatePhi((PhiInsn)ssaInsn2);
                }
                else {
                    this.simulateStmt(ssaInsn2);
                }
            }
        }
        this.replaceConstants();
        this.replaceBranches();
    }
    
    private boolean setLatticeValueTo(final int n, final int n2, final Constant constant) {
        if (n2 != 1) {
            if (this.latticeValues[n] != n2) {
                this.latticeValues[n] = n2;
                return true;
            }
            return false;
        }
        else {
            if (this.latticeValues[n] == n2 && this.latticeConstants[n].equals(constant)) {
                return false;
            }
            this.latticeValues[n] = n2;
            this.latticeConstants[n] = constant;
            return true;
        }
    }
    
    private void simulateBlock(final SsaBasicBlock ssaBasicBlock) {
        for (final SsaInsn ssaInsn : ssaBasicBlock.getInsns()) {
            if (ssaInsn instanceof PhiInsn) {
                this.simulatePhi((PhiInsn)ssaInsn);
            }
            else {
                this.simulateStmt(ssaInsn);
            }
        }
    }
    
    private void simulateBranch(final SsaInsn ssaInsn) {
        final Rop opcode = ssaInsn.getOpcode();
        final RegisterSpecList sources = ssaInsn.getSources();
        final boolean b = false;
        final boolean b2 = false;
        final int branchingness = opcode.getBranchingness();
        final int n = 0;
        boolean b3 = b;
        boolean b4 = b2;
        if (branchingness == 4) {
            final CstInteger cstInteger = null;
            final CstInteger cstInteger2 = null;
            final RegisterSpec value = sources.get(0);
            final int reg = value.getReg();
            Constant constant = cstInteger;
            if (!this.ssaMeth.isRegALocal(value)) {
                constant = cstInteger;
                if (this.latticeValues[reg] == 1) {
                    constant = this.latticeConstants[reg];
                }
            }
            Constant constant2 = cstInteger2;
            if (sources.size() == 2) {
                final RegisterSpec value2 = sources.get(1);
                final int reg2 = value2.getReg();
                constant2 = cstInteger2;
                if (!this.ssaMeth.isRegALocal(value2)) {
                    constant2 = cstInteger2;
                    if (this.latticeValues[reg2] == 1) {
                        constant2 = this.latticeConstants[reg2];
                    }
                }
            }
            if (constant != null && sources.size() == 1) {
                if (((TypedConstant)constant).getBasicType() != 6) {
                    b3 = b;
                    b4 = b2;
                }
                else {
                    b3 = true;
                    final int value3 = ((CstInteger)constant).getValue();
                    switch (opcode.getOpcode()) {
                        default: {
                            throw new RuntimeException("Unexpected op");
                        }
                        case 12: {
                            b4 = (value3 > 0);
                            break;
                        }
                        case 11: {
                            b4 = (value3 <= 0);
                            break;
                        }
                        case 10: {
                            b4 = (value3 >= 0);
                            break;
                        }
                        case 9: {
                            b4 = (value3 < 0);
                            break;
                        }
                        case 8: {
                            b4 = (value3 != 0);
                            break;
                        }
                        case 7: {
                            b4 = (value3 == 0);
                            break;
                        }
                    }
                }
            }
            else {
                b3 = b;
                b4 = b2;
                if (constant != null) {
                    b3 = b;
                    b4 = b2;
                    if (constant2 != null) {
                        if (((CstInteger)constant).getBasicType() != 6) {
                            b3 = b;
                            b4 = b2;
                        }
                        else {
                            b3 = true;
                            final int value4 = ((CstInteger)constant).getValue();
                            final int value5 = ((CstInteger)constant2).getValue();
                            switch (opcode.getOpcode()) {
                                default: {
                                    throw new RuntimeException("Unexpected op");
                                }
                                case 12: {
                                    b4 = (value4 > value5);
                                    break;
                                }
                                case 11: {
                                    b4 = (value4 <= value5);
                                    break;
                                }
                                case 10: {
                                    b4 = (value4 >= value5);
                                    break;
                                }
                                case 9: {
                                    b4 = (value4 < value5);
                                    break;
                                }
                                case 8: {
                                    b4 = (value4 != value5);
                                    break;
                                }
                                case 7: {
                                    b4 = (value4 == value5);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        final SsaBasicBlock block = ssaInsn.getBlock();
        if (b3) {
            int n2;
            if (b4) {
                n2 = block.getSuccessorList().get(1);
            }
            else {
                n2 = block.getSuccessorList().get(0);
            }
            this.addBlockToWorklist(this.ssaMeth.getBlocks().get(n2));
            this.branchWorklist.add(ssaInsn);
            return;
        }
        for (int i = n; i < block.getSuccessorList().size(); ++i) {
            this.addBlockToWorklist(this.ssaMeth.getBlocks().get(block.getSuccessorList().get(i)));
        }
    }
    
    private Constant simulateMath(final SsaInsn ssaInsn, int n) {
        final Insn originalRopInsn = ssaInsn.getOriginalRopInsn();
        final int opcode = ssaInsn.getOpcode().getOpcode();
        final RegisterSpecList sources = ssaInsn.getSources();
        final int reg = sources.get(0).getReg();
        Constant constant;
        if (this.latticeValues[reg] != 1) {
            constant = null;
        }
        else {
            constant = this.latticeConstants[reg];
        }
        Constant constant2;
        if (sources.size() == 1) {
            constant2 = ((CstInsn)originalRopInsn).getConstant();
        }
        else {
            final int reg2 = sources.get(1).getReg();
            if (this.latticeValues[reg2] != 1) {
                constant2 = null;
            }
            else {
                constant2 = this.latticeConstants[reg2];
            }
        }
        if (constant == null) {
            return null;
        }
        if (constant2 == null) {
            return null;
        }
        if (n != 6) {
            return null;
        }
        int n2 = 0;
        n = 0;
        final int value = ((CstInteger)constant).getValue();
        final int value2 = ((CstInteger)constant2).getValue();
        switch (opcode) {
            default: {
                throw new RuntimeException("Unexpected op");
            }
            case 25: {
                n = value >>> value2;
                break;
            }
            case 24: {
                n = value >> value2;
                break;
            }
            case 23: {
                n = value << value2;
                break;
            }
            case 22: {
                n = (value ^ value2);
                break;
            }
            case 21: {
                n = (value | value2);
                break;
            }
            case 20: {
                n = (value & value2);
                break;
            }
            case 18: {
                if (value2 == 0) {
                    n2 = 1;
                    n = 0;
                    break;
                }
                n = value % value2;
                break;
            }
            case 17: {
                if (value2 == 0) {
                    n2 = 1;
                    n = 0;
                    break;
                }
                n = value / value2;
                break;
            }
            case 16: {
                n = value * value2;
                break;
            }
            case 15: {
                if (sources.size() == 1) {
                    final int n3 = value2 - value;
                    n2 = n;
                    n = n3;
                    break;
                }
                n = value - value2;
                break;
            }
            case 14: {
                n = value + value2;
                break;
            }
        }
        if (n2 != 0) {
            return null;
        }
        return CstInteger.make(n);
    }
    
    private void simulatePhi(final PhiInsn phiInsn) {
        final int reg = phiInsn.getResult().getReg();
        if (this.latticeValues[reg] == 2) {
            return;
        }
        final RegisterSpecList sources = phiInsn.getSources();
        int n = 0;
        Constant constant = null;
        final int size = sources.size();
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n;
            if (n2 >= size) {
                break;
            }
            final int predBlockIndexForSourcesIndex = phiInsn.predBlockIndexForSourcesIndex(n2);
            final int reg2 = sources.get(n2).getReg();
            n3 = this.latticeValues[reg2];
            Constant constant2;
            if (!this.executableBlocks.get(predBlockIndexForSourcesIndex)) {
                constant2 = constant;
            }
            else {
                if (n3 != 1) {
                    break;
                }
                if (constant == null) {
                    constant2 = this.latticeConstants[reg2];
                    n = 1;
                }
                else {
                    constant2 = constant;
                    if (!this.latticeConstants[reg2].equals(constant)) {
                        n3 = 2;
                        break;
                    }
                }
            }
            ++n2;
            constant = constant2;
        }
        if (this.setLatticeValueTo(reg, n3, constant)) {
            this.addUsersToWorklist(reg, n3);
        }
    }
    
    private void simulatePhiBlock(final SsaBasicBlock ssaBasicBlock) {
        for (final SsaInsn ssaInsn : ssaBasicBlock.getInsns()) {
            if (!(ssaInsn instanceof PhiInsn)) {
                return;
            }
            this.simulatePhi((PhiInsn)ssaInsn);
        }
    }
    
    private void simulateStmt(final SsaInsn ssaInsn) {
        final Insn originalRopInsn = ssaInsn.getOriginalRopInsn();
        if (originalRopInsn.getOpcode().getBranchingness() != 1 || originalRopInsn.getOpcode().isCallLike()) {
            this.simulateBranch(ssaInsn);
        }
        final int opcode = ssaInsn.getOpcode().getOpcode();
        RegisterSpec registerSpec;
        if ((registerSpec = ssaInsn.getResult()) == null) {
            if (opcode != 17 && opcode != 18) {
                return;
            }
            registerSpec = ssaInsn.getBlock().getPrimarySuccessor().getInsns().get(0).getResult();
        }
        final int reg = registerSpec.getReg();
        int n = 2;
        final Constant constant = null;
        Constant constant2 = null;
        Label_0331: {
            if (opcode != 2) {
                if (opcode != 5) {
                    if (opcode != 56) {
                        Label_0211: {
                            switch (opcode) {
                                default: {
                                    switch (opcode) {
                                        default: {
                                            constant2 = constant;
                                            break Label_0331;
                                        }
                                        case 20:
                                        case 21:
                                        case 22:
                                        case 23:
                                        case 24:
                                        case 25: {
                                            break Label_0211;
                                        }
                                    }
                                    break;
                                }
                                case 14:
                                case 15:
                                case 16:
                                case 17:
                                case 18: {
                                    final Constant constant3 = constant2 = this.simulateMath(ssaInsn, registerSpec.getBasicType());
                                    if (constant3 != null) {
                                        n = 1;
                                        constant2 = constant3;
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        constant2 = constant;
                        if (this.latticeValues[reg] == 1) {
                            n = this.latticeValues[reg];
                            constant2 = this.latticeConstants[reg];
                        }
                    }
                }
                else {
                    final CstInsn cstInsn = (CstInsn)originalRopInsn;
                    n = 1;
                    constant2 = cstInsn.getConstant();
                }
            }
            else {
                constant2 = constant;
                if (ssaInsn.getSources().size() == 1) {
                    final int reg2 = ssaInsn.getSources().get(0).getReg();
                    n = this.latticeValues[reg2];
                    constant2 = this.latticeConstants[reg2];
                }
            }
        }
        if (this.setLatticeValueTo(reg, n, constant2)) {
            this.addUsersToWorklist(reg, n);
        }
    }
}
