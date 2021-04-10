package com.android.dx.io.instructions;

import com.android.dx.io.*;
import java.io.*;
import com.android.dex.*;
import com.android.dx.util.*;

public abstract class DecodedInstruction
{
    private final InstructionCodec format;
    private final int index;
    private final IndexType indexType;
    private final long literal;
    private final int opcode;
    private final int target;
    
    public DecodedInstruction(final InstructionCodec format, final int opcode, final int index, final IndexType indexType, final int target, final long literal) {
        if (format == null) {
            throw new NullPointerException("format == null");
        }
        if (!Opcodes.isValidShape(opcode)) {
            throw new IllegalArgumentException("invalid opcode");
        }
        this.format = format;
        this.opcode = opcode;
        this.index = index;
        this.indexType = indexType;
        this.target = target;
        this.literal = literal;
    }
    
    public static DecodedInstruction decode(final CodeInput codeInput) throws EOFException {
        final int read = codeInput.read();
        return OpcodeInfo.getFormat(Opcodes.extractOpcodeFromUnit(read)).decode(read, codeInput);
    }
    
    public static DecodedInstruction[] decodeAll(final short[] array) {
        final DecodedInstruction[] array2 = new DecodedInstruction[array.length];
        final ShortArrayCodeInput shortArrayCodeInput = new ShortArrayCodeInput(array);
        try {
            while (shortArrayCodeInput.hasMore()) {
                array2[shortArrayCodeInput.cursor()] = decode(shortArrayCodeInput);
            }
            return array2;
        }
        catch (EOFException ex) {
            throw new DexException(ex);
        }
    }
    
    public final void encode(final CodeOutput codeOutput) {
        this.format.encode(this, codeOutput);
    }
    
    public int getA() {
        return 0;
    }
    
    public final short getAByte() {
        final int a = this.getA();
        if ((a & 0xFFFFFF00) != 0x0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Register A out of range: ");
            sb.append(Hex.u8(a));
            throw new DexException(sb.toString());
        }
        return (short)a;
    }
    
    public final short getANibble() {
        final int a = this.getA();
        if ((a & 0xFFFFFFF0) != 0x0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Register A out of range: ");
            sb.append(Hex.u8(a));
            throw new DexException(sb.toString());
        }
        return (short)a;
    }
    
    public final short getAUnit() {
        final int a = this.getA();
        if ((0xFFFF0000 & a) != 0x0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Register A out of range: ");
            sb.append(Hex.u8(a));
            throw new DexException(sb.toString());
        }
        return (short)a;
    }
    
    public int getB() {
        return 0;
    }
    
    public final short getBByte() {
        final int b = this.getB();
        if ((b & 0xFFFFFF00) != 0x0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Register B out of range: ");
            sb.append(Hex.u8(b));
            throw new DexException(sb.toString());
        }
        return (short)b;
    }
    
    public final short getBNibble() {
        final int b = this.getB();
        if ((b & 0xFFFFFFF0) != 0x0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Register B out of range: ");
            sb.append(Hex.u8(b));
            throw new DexException(sb.toString());
        }
        return (short)b;
    }
    
    public final short getBUnit() {
        final int b = this.getB();
        if ((0xFFFF0000 & b) != 0x0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Register B out of range: ");
            sb.append(Hex.u8(b));
            throw new DexException(sb.toString());
        }
        return (short)b;
    }
    
    public int getC() {
        return 0;
    }
    
    public final short getCByte() {
        final int c = this.getC();
        if ((c & 0xFFFFFF00) != 0x0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Register C out of range: ");
            sb.append(Hex.u8(c));
            throw new DexException(sb.toString());
        }
        return (short)c;
    }
    
    public final short getCNibble() {
        final int c = this.getC();
        if ((c & 0xFFFFFFF0) != 0x0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Register C out of range: ");
            sb.append(Hex.u8(c));
            throw new DexException(sb.toString());
        }
        return (short)c;
    }
    
    public final short getCUnit() {
        final int c = this.getC();
        if ((0xFFFF0000 & c) != 0x0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Register C out of range: ");
            sb.append(Hex.u8(c));
            throw new DexException(sb.toString());
        }
        return (short)c;
    }
    
    public int getD() {
        return 0;
    }
    
    public final short getDByte() {
        final int d = this.getD();
        if ((d & 0xFFFFFF00) != 0x0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Register D out of range: ");
            sb.append(Hex.u8(d));
            throw new DexException(sb.toString());
        }
        return (short)d;
    }
    
    public final short getDNibble() {
        final int d = this.getD();
        if ((d & 0xFFFFFFF0) != 0x0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Register D out of range: ");
            sb.append(Hex.u8(d));
            throw new DexException(sb.toString());
        }
        return (short)d;
    }
    
    public final short getDUnit() {
        final int d = this.getD();
        if ((0xFFFF0000 & d) != 0x0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Register D out of range: ");
            sb.append(Hex.u8(d));
            throw new DexException(sb.toString());
        }
        return (short)d;
    }
    
    public int getE() {
        return 0;
    }
    
    public final short getENibble() {
        final int e = this.getE();
        if ((e & 0xFFFFFFF0) != 0x0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Register E out of range: ");
            sb.append(Hex.u8(e));
            throw new DexException(sb.toString());
        }
        return (short)e;
    }
    
    public final InstructionCodec getFormat() {
        return this.format;
    }
    
    public final int getIndex() {
        return this.index;
    }
    
    public final IndexType getIndexType() {
        return this.indexType;
    }
    
    public final short getIndexUnit() {
        return (short)this.index;
    }
    
    public final long getLiteral() {
        return this.literal;
    }
    
    public final int getLiteralByte() {
        if (this.literal != (byte)this.literal) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Literal out of range: ");
            sb.append(Hex.u8(this.literal));
            throw new DexException(sb.toString());
        }
        return (int)this.literal & 0xFF;
    }
    
    public final int getLiteralInt() {
        if (this.literal != (int)this.literal) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Literal out of range: ");
            sb.append(Hex.u8(this.literal));
            throw new DexException(sb.toString());
        }
        return (int)this.literal;
    }
    
    public final int getLiteralNibble() {
        if (this.literal >= -8L && this.literal <= 7L) {
            return (int)this.literal & 0xF;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Literal out of range: ");
        sb.append(Hex.u8(this.literal));
        throw new DexException(sb.toString());
    }
    
    public final short getLiteralUnit() {
        if (this.literal != (short)this.literal) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Literal out of range: ");
            sb.append(Hex.u8(this.literal));
            throw new DexException(sb.toString());
        }
        return (short)this.literal;
    }
    
    public final int getOpcode() {
        return this.opcode;
    }
    
    public final short getOpcodeUnit() {
        return (short)this.opcode;
    }
    
    public abstract int getRegisterCount();
    
    public final short getRegisterCountUnit() {
        final int registerCount = this.getRegisterCount();
        if ((0xFFFF0000 & registerCount) != 0x0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Register count out of range: ");
            sb.append(Hex.u8(registerCount));
            throw new DexException(sb.toString());
        }
        return (short)registerCount;
    }
    
    public final int getTarget() {
        return this.target;
    }
    
    public final int getTarget(final int n) {
        return this.target - n;
    }
    
    public final int getTargetByte(int target) {
        target = this.getTarget(target);
        if (target != (byte)target) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Target out of range: ");
            sb.append(Hex.s4(target));
            throw new DexException(sb.toString());
        }
        return target & 0xFF;
    }
    
    public final short getTargetUnit(int target) {
        target = this.getTarget(target);
        if (target != (short)target) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Target out of range: ");
            sb.append(Hex.s4(target));
            throw new DexException(sb.toString());
        }
        return (short)target;
    }
    
    public abstract DecodedInstruction withIndex(final int p0);
}
