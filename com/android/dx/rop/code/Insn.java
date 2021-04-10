package com.android.dx.rop.code;

import com.android.dx.util.*;
import com.android.dx.rop.type.*;

public abstract class Insn implements ToHuman
{
    private final Rop opcode;
    private final SourcePosition position;
    private final RegisterSpec result;
    private final RegisterSpecList sources;
    
    public Insn(final Rop opcode, final SourcePosition position, final RegisterSpec result, final RegisterSpecList sources) {
        if (opcode == null) {
            throw new NullPointerException("opcode == null");
        }
        if (position == null) {
            throw new NullPointerException("position == null");
        }
        if (sources == null) {
            throw new NullPointerException("sources == null");
        }
        this.opcode = opcode;
        this.position = position;
        this.result = result;
        this.sources = sources;
    }
    
    private static boolean equalsHandleNulls(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    public abstract void accept(final Visitor p0);
    
    public final boolean canThrow() {
        return this.opcode.canThrow();
    }
    
    public boolean contentEquals(final Insn insn) {
        return this.opcode == insn.getOpcode() && this.position.equals(insn.getPosition()) && this.getClass() == insn.getClass() && equalsHandleNulls(this.result, insn.getResult()) && equalsHandleNulls(this.sources, insn.getSources()) && StdTypeList.equalContents(this.getCatches(), insn.getCatches());
    }
    
    public Insn copy() {
        return this.withRegisterOffset(0);
    }
    
    @Override
    public final boolean equals(final Object o) {
        return this == o;
    }
    
    public abstract TypeList getCatches();
    
    public String getInlineString() {
        return null;
    }
    
    public final RegisterSpec getLocalAssignment() {
        RegisterSpec registerSpec;
        if (this.opcode.getOpcode() == 54) {
            registerSpec = this.sources.get(0);
        }
        else {
            registerSpec = this.result;
        }
        if (registerSpec == null) {
            return null;
        }
        if (registerSpec.getLocalItem() == null) {
            return null;
        }
        return registerSpec;
    }
    
    public final Rop getOpcode() {
        return this.opcode;
    }
    
    public final SourcePosition getPosition() {
        return this.position;
    }
    
    public final RegisterSpec getResult() {
        return this.result;
    }
    
    public final RegisterSpecList getSources() {
        return this.sources;
    }
    
    @Override
    public final int hashCode() {
        return System.identityHashCode(this);
    }
    
    @Override
    public String toHuman() {
        return this.toHumanWithInline(this.getInlineString());
    }
    
    protected final String toHumanWithInline(final String s) {
        final StringBuffer sb = new StringBuffer(80);
        sb.append(this.position);
        sb.append(": ");
        sb.append(this.opcode.getNickname());
        if (s != null) {
            sb.append("(");
            sb.append(s);
            sb.append(")");
        }
        if (this.result == null) {
            sb.append(" .");
        }
        else {
            sb.append(" ");
            sb.append(this.result.toHuman());
        }
        sb.append(" <-");
        final int size = this.sources.size();
        if (size == 0) {
            sb.append(" .");
        }
        else {
            for (int i = 0; i < size; ++i) {
                sb.append(" ");
                sb.append(this.sources.get(i).toHuman());
            }
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.toStringWithInline(this.getInlineString());
    }
    
    protected final String toStringWithInline(final String s) {
        final StringBuffer sb = new StringBuffer(80);
        sb.append("Insn{");
        sb.append(this.position);
        sb.append(' ');
        sb.append(this.opcode);
        if (s != null) {
            sb.append(' ');
            sb.append(s);
        }
        sb.append(" :: ");
        if (this.result != null) {
            sb.append(this.result);
            sb.append(" <- ");
        }
        sb.append(this.sources);
        sb.append('}');
        return sb.toString();
    }
    
    public abstract Insn withAddedCatch(final Type p0);
    
    public abstract Insn withNewRegisters(final RegisterSpec p0, final RegisterSpecList p1);
    
    public abstract Insn withRegisterOffset(final int p0);
    
    public Insn withSourceLiteral() {
        return this;
    }
    
    public static class BaseVisitor implements Visitor
    {
        @Override
        public void visitFillArrayDataInsn(final FillArrayDataInsn fillArrayDataInsn) {
        }
        
        @Override
        public void visitPlainCstInsn(final PlainCstInsn plainCstInsn) {
        }
        
        @Override
        public void visitPlainInsn(final PlainInsn plainInsn) {
        }
        
        @Override
        public void visitSwitchInsn(final SwitchInsn switchInsn) {
        }
        
        @Override
        public void visitThrowingCstInsn(final ThrowingCstInsn throwingCstInsn) {
        }
        
        @Override
        public void visitThrowingInsn(final ThrowingInsn throwingInsn) {
        }
    }
    
    public interface Visitor
    {
        void visitFillArrayDataInsn(final FillArrayDataInsn p0);
        
        void visitPlainCstInsn(final PlainCstInsn p0);
        
        void visitPlainInsn(final PlainInsn p0);
        
        void visitSwitchInsn(final SwitchInsn p0);
        
        void visitThrowingCstInsn(final ThrowingCstInsn p0);
        
        void visitThrowingInsn(final ThrowingInsn p0);
    }
}
