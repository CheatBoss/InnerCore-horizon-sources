package com.android.dx.dex.code;

import com.android.dx.rop.code.*;
import com.android.dx.rop.cst.*;
import com.android.dx.util.*;
import java.util.*;

public abstract class InsnFormat
{
    public static boolean ALLOW_EXTENDED_OPCODES;
    
    static {
        InsnFormat.ALLOW_EXTENDED_OPCODES = true;
    }
    
    protected static int argIndex(final DalvInsn dalvInsn) {
        final int value = ((CstInteger)((CstInsn)dalvInsn).getConstant()).getValue();
        if (value < 0) {
            throw new IllegalArgumentException("bogus insn");
        }
        return value;
    }
    
    protected static String branchComment(final DalvInsn dalvInsn) {
        final int targetOffset = ((TargetInsn)dalvInsn).getTargetOffset();
        if (targetOffset == (short)targetOffset) {
            return Hex.s2(targetOffset);
        }
        return Hex.s4(targetOffset);
    }
    
    protected static String branchString(final DalvInsn dalvInsn) {
        final int targetAddress = ((TargetInsn)dalvInsn).getTargetAddress();
        if (targetAddress == (char)targetAddress) {
            return Hex.u2(targetAddress);
        }
        return Hex.u4(targetAddress);
    }
    
    protected static short codeUnit(final int n, final int n2) {
        if ((n & 0xFF) != n) {
            throw new IllegalArgumentException("low out of range 0..255");
        }
        if ((n2 & 0xFF) != n2) {
            throw new IllegalArgumentException("high out of range 0..255");
        }
        return (short)(n2 << 8 | n);
    }
    
    protected static short codeUnit(final int n, final int n2, final int n3, final int n4) {
        if ((n & 0xF) != n) {
            throw new IllegalArgumentException("n0 out of range 0..15");
        }
        if ((n2 & 0xF) != n2) {
            throw new IllegalArgumentException("n1 out of range 0..15");
        }
        if ((n3 & 0xF) != n3) {
            throw new IllegalArgumentException("n2 out of range 0..15");
        }
        if ((n4 & 0xF) != n4) {
            throw new IllegalArgumentException("n3 out of range 0..15");
        }
        return (short)(n2 << 4 | n | n3 << 8 | n4 << 12);
    }
    
    protected static String cstComment(final DalvInsn dalvInsn) {
        final CstInsn cstInsn = (CstInsn)dalvInsn;
        if (!cstInsn.hasIndex()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder(20);
        final int index = cstInsn.getIndex();
        sb.append(cstInsn.getConstant().typeName());
        sb.append('@');
        if (index < 65536) {
            sb.append(Hex.u2(index));
        }
        else {
            sb.append(Hex.u4(index));
        }
        return sb.toString();
    }
    
    protected static String cstString(final DalvInsn dalvInsn) {
        final Constant constant = ((CstInsn)dalvInsn).getConstant();
        if (constant instanceof CstString) {
            return ((CstString)constant).toQuoted();
        }
        return constant.toHuman();
    }
    
    protected static boolean isRegListSequential(final RegisterSpecList list) {
        final int size = list.size();
        if (size < 2) {
            return true;
        }
        int reg = list.get(0).getReg();
        for (int i = 0; i < size; ++i) {
            final RegisterSpec value = list.get(i);
            if (value.getReg() != reg) {
                return false;
            }
            reg += value.getCategory();
        }
        return true;
    }
    
    protected static String literalBitsComment(final CstLiteralBits cstLiteralBits, final int n) {
        final StringBuffer sb = new StringBuffer(20);
        sb.append("#");
        long longBits;
        if (cstLiteralBits instanceof CstLiteral64) {
            longBits = ((CstLiteral64)cstLiteralBits).getLongBits();
        }
        else {
            longBits = cstLiteralBits.getIntBits();
        }
        if (n != 4) {
            if (n != 8) {
                if (n != 16) {
                    if (n != 32) {
                        if (n != 64) {
                            throw new RuntimeException("shouldn't happen");
                        }
                        sb.append(Hex.u8(longBits));
                    }
                    else {
                        sb.append(Hex.u4((int)longBits));
                    }
                }
                else {
                    sb.append(Hex.u2((int)longBits));
                }
            }
            else {
                sb.append(Hex.u1((int)longBits));
            }
        }
        else {
            sb.append(Hex.uNibble((int)longBits));
        }
        return sb.toString();
    }
    
    protected static String literalBitsString(final CstLiteralBits cstLiteralBits) {
        final StringBuffer sb = new StringBuffer(100);
        sb.append('#');
        if (cstLiteralBits instanceof CstKnownNull) {
            sb.append("null");
        }
        else {
            sb.append(cstLiteralBits.typeName());
            sb.append(' ');
            sb.append(cstLiteralBits.toHuman());
        }
        return sb.toString();
    }
    
    protected static int makeByte(final int n, final int n2) {
        if ((n & 0xF) != n) {
            throw new IllegalArgumentException("low out of range 0..15");
        }
        if ((n2 & 0xF) != n2) {
            throw new IllegalArgumentException("high out of range 0..15");
        }
        return n2 << 4 | n;
    }
    
    protected static short opcodeUnit(final DalvInsn dalvInsn) {
        final int opcode = dalvInsn.getOpcode().getOpcode();
        if (opcode >= 256 && opcode <= 65535) {
            return (short)opcode;
        }
        throw new IllegalArgumentException("opcode out of range 0..65535");
    }
    
    protected static short opcodeUnit(final DalvInsn dalvInsn, final int n) {
        if ((n & 0xFF) != n) {
            throw new IllegalArgumentException("arg out of range 0..255");
        }
        final int opcode = dalvInsn.getOpcode().getOpcode();
        if ((opcode & 0xFF) != opcode) {
            throw new IllegalArgumentException("opcode out of range 0..255");
        }
        return (short)(n << 8 | opcode);
    }
    
    protected static String regListString(final RegisterSpecList list) {
        final int size = list.size();
        final StringBuffer sb = new StringBuffer(size * 5 + 2);
        sb.append('{');
        for (int i = 0; i < size; ++i) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(list.get(i).regString());
        }
        sb.append('}');
        return sb.toString();
    }
    
    protected static String regRangeString(final RegisterSpecList list) {
        final int size = list.size();
        final StringBuilder sb = new StringBuilder(30);
        sb.append("{");
        switch (size) {
            default: {
                RegisterSpec registerSpec2;
                final RegisterSpec registerSpec = registerSpec2 = list.get(size - 1);
                if (registerSpec.getCategory() == 2) {
                    registerSpec2 = registerSpec.withOffset(1);
                }
                sb.append(list.get(0).regString());
                sb.append("..");
                sb.append(registerSpec2.regString());
                break;
            }
            case 1: {
                sb.append(list.get(0).regString());
                break;
            }
            case 0: {
                break;
            }
        }
        sb.append("}");
        return sb.toString();
    }
    
    protected static boolean signedFitsInByte(final int n) {
        return (byte)n == n;
    }
    
    protected static boolean signedFitsInNibble(final int n) {
        return n >= -8 && n <= 7;
    }
    
    protected static boolean signedFitsInShort(final int n) {
        return (short)n == n;
    }
    
    protected static boolean unsignedFitsInByte(final int n) {
        return n == (n & 0xFF);
    }
    
    protected static boolean unsignedFitsInNibble(final int n) {
        return n == (n & 0xF);
    }
    
    protected static boolean unsignedFitsInShort(final int n) {
        return n == (0xFFFF & n);
    }
    
    protected static void write(final AnnotatedOutput annotatedOutput, final short n) {
        annotatedOutput.writeShort(n);
    }
    
    protected static void write(final AnnotatedOutput annotatedOutput, final short n, final int n2) {
        write(annotatedOutput, n, (short)n2, (short)(n2 >> 16));
    }
    
    protected static void write(final AnnotatedOutput annotatedOutput, final short n, final int n2, final short n3) {
        write(annotatedOutput, n, (short)n2, (short)(n2 >> 16), n3);
    }
    
    protected static void write(final AnnotatedOutput annotatedOutput, final short n, final int n2, final short n3, final short n4) {
        write(annotatedOutput, n, (short)n2, (short)(n2 >> 16), n3, n4);
    }
    
    protected static void write(final AnnotatedOutput annotatedOutput, final short n, final long n2) {
        write(annotatedOutput, n, (short)n2, (short)(n2 >> 16), (short)(n2 >> 32), (short)(n2 >> 48));
    }
    
    protected static void write(final AnnotatedOutput annotatedOutput, final short n, final short n2) {
        annotatedOutput.writeShort(n);
        annotatedOutput.writeShort(n2);
    }
    
    protected static void write(final AnnotatedOutput annotatedOutput, final short n, final short n2, final short n3) {
        annotatedOutput.writeShort(n);
        annotatedOutput.writeShort(n2);
        annotatedOutput.writeShort(n3);
    }
    
    protected static void write(final AnnotatedOutput annotatedOutput, final short n, final short n2, final short n3, final short n4) {
        annotatedOutput.writeShort(n);
        annotatedOutput.writeShort(n2);
        annotatedOutput.writeShort(n3);
        annotatedOutput.writeShort(n4);
    }
    
    protected static void write(final AnnotatedOutput annotatedOutput, final short n, final short n2, final short n3, final short n4, final short n5) {
        annotatedOutput.writeShort(n);
        annotatedOutput.writeShort(n2);
        annotatedOutput.writeShort(n3);
        annotatedOutput.writeShort(n4);
        annotatedOutput.writeShort(n5);
    }
    
    public boolean branchFits(final TargetInsn targetInsn) {
        return false;
    }
    
    public abstract int codeSize();
    
    public BitSet compatibleRegs(final DalvInsn dalvInsn) {
        return new BitSet();
    }
    
    public abstract String insnArgString(final DalvInsn p0);
    
    public abstract String insnCommentString(final DalvInsn p0, final boolean p1);
    
    public abstract boolean isCompatible(final DalvInsn p0);
    
    public final String listingString(final DalvInsn dalvInsn, final boolean b) {
        final String name = dalvInsn.getOpcode().getName();
        final String insnArgString = this.insnArgString(dalvInsn);
        final String insnCommentString = this.insnCommentString(dalvInsn, b);
        final StringBuilder sb = new StringBuilder(100);
        sb.append(name);
        if (insnArgString.length() != 0) {
            sb.append(' ');
            sb.append(insnArgString);
        }
        if (insnCommentString.length() != 0) {
            sb.append(" // ");
            sb.append(insnCommentString);
        }
        return sb.toString();
    }
    
    public abstract void writeTo(final AnnotatedOutput p0, final DalvInsn p1);
}
