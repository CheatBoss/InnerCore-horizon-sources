package com.android.dx.dex.code;

import com.android.dx.io.*;

public final class Dop
{
    private final int family;
    private final InsnFormat format;
    private final boolean hasResult;
    private final int nextOpcode;
    private final int opcode;
    
    public Dop(final int opcode, final int family, final int nextOpcode, final InsnFormat format, final boolean hasResult) {
        if (!Opcodes.isValidShape(opcode)) {
            throw new IllegalArgumentException("bogus opcode");
        }
        if (!Opcodes.isValidShape(family)) {
            throw new IllegalArgumentException("bogus family");
        }
        if (!Opcodes.isValidShape(nextOpcode)) {
            throw new IllegalArgumentException("bogus nextOpcode");
        }
        if (format == null) {
            throw new NullPointerException("format == null");
        }
        this.opcode = opcode;
        this.family = family;
        this.nextOpcode = nextOpcode;
        this.format = format;
        this.hasResult = hasResult;
    }
    
    public int getFamily() {
        return this.family;
    }
    
    public InsnFormat getFormat() {
        return this.format;
    }
    
    public String getName() {
        return OpcodeInfo.getName(this.opcode);
    }
    
    public int getNextOpcode() {
        return this.nextOpcode;
    }
    
    public int getOpcode() {
        return this.opcode;
    }
    
    public Dop getOppositeTest() {
        switch (this.opcode) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("bogus opcode: ");
                sb.append(this);
                throw new IllegalArgumentException(sb.toString());
            }
            case 61: {
                return Dops.IF_GTZ;
            }
            case 60: {
                return Dops.IF_LEZ;
            }
            case 59: {
                return Dops.IF_LTZ;
            }
            case 58: {
                return Dops.IF_GEZ;
            }
            case 57: {
                return Dops.IF_EQZ;
            }
            case 56: {
                return Dops.IF_NEZ;
            }
            case 55: {
                return Dops.IF_GT;
            }
            case 54: {
                return Dops.IF_LE;
            }
            case 53: {
                return Dops.IF_LT;
            }
            case 52: {
                return Dops.IF_GE;
            }
            case 51: {
                return Dops.IF_EQ;
            }
            case 50: {
                return Dops.IF_NE;
            }
        }
    }
    
    public boolean hasResult() {
        return this.hasResult;
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
}
