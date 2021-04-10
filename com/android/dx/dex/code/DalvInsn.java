package com.android.dx.dex.code;

import com.android.dx.rop.code.*;
import java.util.*;
import com.android.dx.ssa.*;
import com.android.dx.util.*;

public abstract class DalvInsn
{
    private int address;
    private final Dop opcode;
    private final SourcePosition position;
    private final RegisterSpecList registers;
    
    public DalvInsn(final Dop opcode, final SourcePosition position, final RegisterSpecList registers) {
        if (opcode == null) {
            throw new NullPointerException("opcode == null");
        }
        if (position == null) {
            throw new NullPointerException("position == null");
        }
        if (registers == null) {
            throw new NullPointerException("registers == null");
        }
        this.address = -1;
        this.opcode = opcode;
        this.position = position;
        this.registers = registers;
    }
    
    public static SimpleInsn makeMove(final SourcePosition sourcePosition, final RegisterSpec registerSpec, final RegisterSpec registerSpec2) {
        final int category = registerSpec.getCategory();
        boolean b = true;
        if (category != 1) {
            b = false;
        }
        final boolean reference = registerSpec.getType().isReference();
        final int reg = registerSpec.getReg();
        Dop dop;
        if ((registerSpec2.getReg() | reg) < 16) {
            if (reference) {
                dop = Dops.MOVE_OBJECT;
            }
            else if (b) {
                dop = Dops.MOVE;
            }
            else {
                dop = Dops.MOVE_WIDE;
            }
        }
        else if (reg < 256) {
            if (reference) {
                dop = Dops.MOVE_OBJECT_FROM16;
            }
            else if (b) {
                dop = Dops.MOVE_FROM16;
            }
            else {
                dop = Dops.MOVE_WIDE_FROM16;
            }
        }
        else if (reference) {
            dop = Dops.MOVE_OBJECT_16;
        }
        else if (b) {
            dop = Dops.MOVE_16;
        }
        else {
            dop = Dops.MOVE_WIDE_16;
        }
        return new SimpleInsn(dop, sourcePosition, RegisterSpecList.make(registerSpec, registerSpec2));
    }
    
    protected abstract String argString();
    
    public abstract int codeSize();
    
    public DalvInsn expandedPrefix(final BitSet set) {
        final RegisterSpecList registers = this.registers;
        final boolean value = set.get(0);
        if (this.hasResult()) {
            set.set(0);
        }
        final RegisterSpecList subset = registers.subset(set);
        if (this.hasResult()) {
            set.set(0, value);
        }
        if (subset.size() == 0) {
            return null;
        }
        return new HighRegisterPrefix(this.position, subset);
    }
    
    public DalvInsn expandedSuffix(final BitSet set) {
        if (this.hasResult() && !set.get(0)) {
            final RegisterSpec value = this.registers.get(0);
            return makeMove(this.position, value, value.withReg(0));
        }
        return null;
    }
    
    public DalvInsn expandedVersion(final BitSet set) {
        return this.withRegisters(this.registers.withExpandedRegisters(0, this.hasResult(), set));
    }
    
    public final int getAddress() {
        if (this.address < 0) {
            throw new RuntimeException("address not yet known");
        }
        return this.address;
    }
    
    public DalvInsn getLowRegVersion() {
        return this.withRegisters(this.registers.withExpandedRegisters(0, this.hasResult(), null));
    }
    
    public final int getMinimumRegisterRequirement(final BitSet set) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    public final int getNextAddress() {
        return this.getAddress() + this.codeSize();
    }
    
    public final Dop getOpcode() {
        return this.opcode;
    }
    
    public final SourcePosition getPosition() {
        return this.position;
    }
    
    public final RegisterSpecList getRegisters() {
        return this.registers;
    }
    
    public final boolean hasAddress() {
        return this.address >= 0;
    }
    
    public final boolean hasResult() {
        return this.opcode.hasResult();
    }
    
    public final String identifierString() {
        if (this.address != -1) {
            return String.format("%04x", this.address);
        }
        return Hex.u4(System.identityHashCode(this));
    }
    
    public final String listingString(String string, int length, final boolean b) {
        final String listingString0 = this.listingString0(b);
        if (listingString0 == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(string);
        sb.append(this.identifierString());
        sb.append(": ");
        string = sb.toString();
        final int length2 = string.length();
        if (length == 0) {
            length = listingString0.length();
        }
        else {
            length -= length2;
        }
        return TwoColumnOutput.toString(string, length2, "", listingString0, length);
    }
    
    protected abstract String listingString0(final boolean p0);
    
    public final void setAddress(final int address) {
        if (address < 0) {
            throw new IllegalArgumentException("address < 0");
        }
        this.address = address;
    }
    
    @Override
    public final String toString() {
        final StringBuffer sb = new StringBuffer(100);
        sb.append(this.identifierString());
        sb.append(' ');
        sb.append(this.position);
        sb.append(": ");
        sb.append(this.opcode.getName());
        boolean b = false;
        if (this.registers.size() != 0) {
            sb.append(this.registers.toHuman(" ", ", ", null));
            b = true;
        }
        final String argString = this.argString();
        if (argString != null) {
            if (b) {
                sb.append(',');
            }
            sb.append(' ');
            sb.append(argString);
        }
        return sb.toString();
    }
    
    public DalvInsn withMapper(final RegisterMapper registerMapper) {
        return this.withRegisters(registerMapper.map(this.getRegisters()));
    }
    
    public abstract DalvInsn withOpcode(final Dop p0);
    
    public abstract DalvInsn withRegisterOffset(final int p0);
    
    public abstract DalvInsn withRegisters(final RegisterSpecList p0);
    
    public abstract void writeTo(final AnnotatedOutput p0);
}
