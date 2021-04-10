package com.android.dx.ssa;

import com.android.dx.rop.type.*;
import java.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.util.*;

public final class PhiInsn extends SsaInsn
{
    private final ArrayList<Operand> operands;
    private final int ropResultReg;
    private RegisterSpecList sources;
    
    public PhiInsn(final int ropResultReg, final SsaBasicBlock ssaBasicBlock) {
        super(RegisterSpec.make(ropResultReg, Type.VOID), ssaBasicBlock);
        this.operands = new ArrayList<Operand>();
        this.ropResultReg = ropResultReg;
    }
    
    public PhiInsn(final RegisterSpec registerSpec, final SsaBasicBlock ssaBasicBlock) {
        super(registerSpec, ssaBasicBlock);
        this.operands = new ArrayList<Operand>();
        this.ropResultReg = registerSpec.getReg();
    }
    
    @Override
    public void accept(final SsaInsn.Visitor visitor) {
        visitor.visitPhiInsn(this);
    }
    
    public void addPhiOperand(final RegisterSpec registerSpec, final SsaBasicBlock ssaBasicBlock) {
        this.operands.add(new Operand(registerSpec, ssaBasicBlock.getIndex(), ssaBasicBlock.getRopLabel()));
        this.sources = null;
    }
    
    public boolean areAllOperandsEqual() {
        if (this.operands.size() == 0) {
            return true;
        }
        final int reg = this.operands.get(0).regSpec.getReg();
        final Iterator<Operand> iterator = this.operands.iterator();
        while (iterator.hasNext()) {
            if (reg != iterator.next().regSpec.getReg()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean canThrow() {
        return false;
    }
    
    public void changeResultType(final TypeBearer typeBearer, final LocalItem localItem) {
        this.setResult(RegisterSpec.makeLocalOptional(this.getResult().getReg(), typeBearer, localItem));
    }
    
    @Override
    public PhiInsn clone() {
        throw new UnsupportedOperationException("can't clone phi");
    }
    
    @Override
    public Rop getOpcode() {
        return null;
    }
    
    @Override
    public Insn getOriginalRopInsn() {
        return null;
    }
    
    public int getRopResultReg() {
        return this.ropResultReg;
    }
    
    @Override
    public RegisterSpecList getSources() {
        if (this.sources != null) {
            return this.sources;
        }
        if (this.operands.size() == 0) {
            return RegisterSpecList.EMPTY;
        }
        final int size = this.operands.size();
        this.sources = new RegisterSpecList(size);
        for (int i = 0; i < size; ++i) {
            this.sources.set(i, this.operands.get(i).regSpec);
        }
        this.sources.setImmutable();
        return this.sources;
    }
    
    @Override
    public boolean hasSideEffect() {
        return Optimizer.getPreserveLocals() && this.getLocalAssignment() != null;
    }
    
    @Override
    public boolean isPhiOrMove() {
        return true;
    }
    
    @Override
    public boolean isRegASource(final int n) {
        final Iterator<Operand> iterator = this.operands.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().regSpec.getReg() == n) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public final void mapSourceRegisters(final RegisterMapper registerMapper) {
        for (final Operand operand : this.operands) {
            final RegisterSpec regSpec = operand.regSpec;
            operand.regSpec = registerMapper.map(regSpec);
            if (regSpec != operand.regSpec) {
                this.getBlock().getParent().onSourceChanged(this, regSpec, operand.regSpec);
            }
        }
        this.sources = null;
    }
    
    public int predBlockIndexForSourcesIndex(final int n) {
        return this.operands.get(n).blockIndex;
    }
    
    public List<SsaBasicBlock> predBlocksForReg(final int n, final SsaMethod ssaMethod) {
        final ArrayList<SsaBasicBlock> list = new ArrayList<SsaBasicBlock>();
        for (final Operand operand : this.operands) {
            if (operand.regSpec.getReg() == n) {
                list.add(ssaMethod.getBlocks().get(operand.blockIndex));
            }
        }
        return list;
    }
    
    public void removePhiRegister(final RegisterSpec registerSpec) {
        final ArrayList<Operand> list = new ArrayList<Operand>();
        for (final Operand operand : this.operands) {
            if (operand.regSpec.getReg() == registerSpec.getReg()) {
                list.add(operand);
            }
        }
        this.operands.removeAll(list);
        this.sources = null;
    }
    
    @Override
    public String toHuman() {
        return this.toHumanWithInline(null);
    }
    
    protected final String toHumanWithInline(final String s) {
        final StringBuffer sb = new StringBuffer(80);
        sb.append(SourcePosition.NO_INFO);
        sb.append(": phi");
        if (s != null) {
            sb.append("(");
            sb.append(s);
            sb.append(")");
        }
        final RegisterSpec result = this.getResult();
        if (result == null) {
            sb.append(" .");
        }
        else {
            sb.append(" ");
            sb.append(result.toHuman());
        }
        sb.append(" <-");
        final int size = this.getSources().size();
        if (size == 0) {
            sb.append(" .");
        }
        else {
            for (int i = 0; i < size; ++i) {
                sb.append(" ");
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(this.sources.get(i).toHuman());
                sb2.append("[b=");
                sb2.append(Hex.u2(this.operands.get(i).ropLabel));
                sb2.append("]");
                sb.append(sb2.toString());
            }
        }
        return sb.toString();
    }
    
    @Override
    public Insn toRopInsn() {
        throw new IllegalArgumentException("Cannot convert phi insns to rop form");
    }
    
    public void updateSourcesToDefinitions(final SsaMethod ssaMethod) {
        for (final Operand operand : this.operands) {
            operand.regSpec = operand.regSpec.withType(ssaMethod.getDefinitionForRegister(operand.regSpec.getReg()).getResult().getType());
        }
        this.sources = null;
    }
    
    private static class Operand
    {
        public final int blockIndex;
        public RegisterSpec regSpec;
        public final int ropLabel;
        
        public Operand(final RegisterSpec regSpec, final int blockIndex, final int ropLabel) {
            this.regSpec = regSpec;
            this.blockIndex = blockIndex;
            this.ropLabel = ropLabel;
        }
    }
    
    public interface Visitor
    {
        void visitPhiInsn(final PhiInsn p0);
    }
}
