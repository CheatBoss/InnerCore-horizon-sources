package com.android.dx.cf.code;

import java.util.*;
import com.android.dx.rop.type.*;
import com.android.dx.util.*;
import com.android.dx.cf.iface.*;
import com.android.dx.rop.cst.*;

public final class BytecodeArray
{
    public static final Visitor EMPTY_VISITOR;
    private final ByteArray bytes;
    private final ConstantPool pool;
    
    static {
        EMPTY_VISITOR = (Visitor)new BaseVisitor();
    }
    
    public BytecodeArray(final ByteArray bytes, final ConstantPool pool) {
        if (bytes == null) {
            throw new NullPointerException("bytes == null");
        }
        if (pool == null) {
            throw new NullPointerException("pool == null");
        }
        this.bytes = bytes;
        this.pool = pool;
    }
    
    private int parseLookupswitch(final int n, final Visitor visitor) {
        final int n2 = n + 4 & 0xFFFFFFFC;
        int n3 = 0;
        for (int i = n + 1; i < n2; ++i) {
            n3 = (n3 << 8 | this.bytes.getUnsignedByte(i));
        }
        final int int1 = this.bytes.getInt(n2);
        final int int2 = this.bytes.getInt(n2 + 4);
        int n4 = n2 + 8;
        final SwitchList list = new SwitchList(int2);
        for (int j = 0; j < int2; ++j) {
            final int int3 = this.bytes.getInt(n4);
            final int int4 = this.bytes.getInt(n4 + 4);
            n4 += 8;
            list.add(int3, int4 + n);
        }
        list.setDefaultTarget(int1 + n);
        list.removeSuperfluousDefaults();
        list.setImmutable();
        final int n5 = n4 - n;
        visitor.visitSwitch(171, n, n5, list, n3);
        return n5;
    }
    
    private int parseNewarray(final int n, final Visitor visitor) {
        final int unsignedByte = this.bytes.getUnsignedByte(n + 1);
        CstType cstType = null;
        switch (unsignedByte) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("bad newarray code ");
                sb.append(Hex.u1(unsignedByte));
                throw new SimException(sb.toString());
            }
            case 11: {
                cstType = CstType.LONG_ARRAY;
                break;
            }
            case 10: {
                cstType = CstType.INT_ARRAY;
                break;
            }
            case 9: {
                cstType = CstType.SHORT_ARRAY;
                break;
            }
            case 8: {
                cstType = CstType.BYTE_ARRAY;
                break;
            }
            case 7: {
                cstType = CstType.DOUBLE_ARRAY;
                break;
            }
            case 6: {
                cstType = CstType.FLOAT_ARRAY;
                break;
            }
            case 5: {
                cstType = CstType.CHAR_ARRAY;
                break;
            }
            case 4: {
                cstType = CstType.BOOLEAN_ARRAY;
                break;
            }
        }
        final int previousOffset = visitor.getPreviousOffset();
        final ConstantParserVisitor constantParserVisitor = new ConstantParserVisitor();
        int value;
        final int n2 = value = 0;
        if (previousOffset >= 0) {
            this.parseInstruction(previousOffset, (Visitor)constantParserVisitor);
            value = n2;
            if (constantParserVisitor.cst instanceof CstInteger) {
                value = n2;
                if (constantParserVisitor.length + previousOffset == n) {
                    value = constantParserVisitor.value;
                }
            }
        }
        int n3 = 0;
        int n4 = 0;
        int n6;
        int n5 = n6 = n + 2;
        final ArrayList<Constant> list = new ArrayList<Constant>();
        int n8;
        if (value != 0) {
            while (true) {
                final boolean b = false;
                final ByteArray bytes = this.bytes;
                final int n7 = n5 + 1;
                if (bytes.getUnsignedByte(n5) != 89) {
                    n3 = n4;
                    n8 = n6;
                    break;
                }
                this.parseInstruction(n7, (Visitor)constantParserVisitor);
                n3 = n4;
                n8 = n6;
                if (constantParserVisitor.length == 0) {
                    break;
                }
                n3 = n4;
                n8 = n6;
                if (!(constantParserVisitor.cst instanceof CstInteger)) {
                    break;
                }
                if (constantParserVisitor.value != n4) {
                    n3 = n4;
                    n8 = n6;
                    break;
                }
                final int n9 = constantParserVisitor.length + n7;
                this.parseInstruction(n9, (Visitor)constantParserVisitor);
                if (constantParserVisitor.length == 0 || !(constantParserVisitor.cst instanceof CstLiteralBits)) {
                    n3 = n4;
                    n8 = n6;
                    break;
                }
                final int n10 = n9 + constantParserVisitor.length;
                list.add(constantParserVisitor.cst);
                final ByteArray bytes2 = this.bytes;
                final int n11 = n10 + 1;
                final int unsignedByte2 = bytes2.getUnsignedByte(n10);
                boolean b2 = false;
                switch (unsignedByte) {
                    default: {
                        b2 = true;
                        break;
                    }
                    case 11: {
                        b2 = b;
                        if (unsignedByte2 != 80) {
                            b2 = true;
                            break;
                        }
                        break;
                    }
                    case 10: {
                        b2 = b;
                        if (unsignedByte2 != 79) {
                            b2 = true;
                            break;
                        }
                        break;
                    }
                    case 9: {
                        b2 = b;
                        if (unsignedByte2 != 86) {
                            b2 = true;
                            break;
                        }
                        break;
                    }
                    case 7: {
                        b2 = b;
                        if (unsignedByte2 != 82) {
                            b2 = true;
                            break;
                        }
                        break;
                    }
                    case 6: {
                        b2 = b;
                        if (unsignedByte2 != 81) {
                            b2 = true;
                            break;
                        }
                        break;
                    }
                    case 5: {
                        b2 = b;
                        if (unsignedByte2 != 85) {
                            b2 = true;
                            break;
                        }
                        break;
                    }
                    case 4:
                    case 8: {
                        b2 = b;
                        if (unsignedByte2 != 84) {
                            b2 = true;
                            break;
                        }
                        break;
                    }
                }
                if (b2) {
                    n3 = n4;
                    n8 = n6;
                    break;
                }
                n6 = n11;
                ++n4;
                n5 = n11;
            }
        }
        else {
            n8 = n6;
        }
        if (n3 >= 2 && n3 == value) {
            visitor.visitNewarray(n, n8 - n, cstType, list);
            return n8 - n;
        }
        visitor.visitNewarray(n, 2, cstType, null);
        return 2;
    }
    
    private int parseTableswitch(final int n, final Visitor visitor) {
        final int n2 = n + 4 & 0xFFFFFFFC;
        int i = n + 1;
        int n3 = 0;
        while (i < n2) {
            n3 = (n3 << 8 | this.bytes.getUnsignedByte(i));
            ++i;
        }
        final int int1 = this.bytes.getInt(n2);
        final int int2 = this.bytes.getInt(n2 + 4);
        final int int3 = this.bytes.getInt(n2 + 8);
        final int n4 = int3 - int2 + 1;
        if (int2 > int3) {
            throw new SimException("low / high inversion");
        }
        final SwitchList list = new SwitchList(n4);
        int j = 0;
        int n5 = n2 + 12;
        while (j < n4) {
            final int int4 = this.bytes.getInt(n5);
            n5 += 4;
            list.add(int2 + j, n + int4);
            ++j;
        }
        list.setDefaultTarget(n + int1);
        list.removeSuperfluousDefaults();
        list.setImmutable();
        final int n6 = n5 - n;
        visitor.visitSwitch(171, n, n6, list, n3);
        return n6;
    }
    
    private int parseWide(final int n, final Visitor visitor) {
        final int unsignedByte = this.bytes.getUnsignedByte(n + 1);
        final int unsignedShort = this.bytes.getUnsignedShort(n + 2);
        if (unsignedByte == 132) {
            visitor.visitLocal(unsignedByte, n, 6, unsignedShort, Type.INT, this.bytes.getShort(n + 4));
            return 6;
        }
        if (unsignedByte == 169) {
            visitor.visitLocal(unsignedByte, n, 4, unsignedShort, Type.RETURN_ADDRESS, 0);
            return 4;
        }
        switch (unsignedByte) {
            default: {
                switch (unsignedByte) {
                    default: {
                        visitor.visitInvalid(196, n, 1);
                        return 1;
                    }
                    case 58: {
                        visitor.visitLocal(54, n, 4, unsignedShort, Type.OBJECT, 0);
                        return 4;
                    }
                    case 57: {
                        visitor.visitLocal(54, n, 4, unsignedShort, Type.DOUBLE, 0);
                        return 4;
                    }
                    case 56: {
                        visitor.visitLocal(54, n, 4, unsignedShort, Type.FLOAT, 0);
                        return 4;
                    }
                    case 55: {
                        visitor.visitLocal(54, n, 4, unsignedShort, Type.LONG, 0);
                        return 4;
                    }
                    case 54: {
                        visitor.visitLocal(54, n, 4, unsignedShort, Type.INT, 0);
                        return 4;
                    }
                }
                break;
            }
            case 25: {
                visitor.visitLocal(21, n, 4, unsignedShort, Type.OBJECT, 0);
                return 4;
            }
            case 24: {
                visitor.visitLocal(21, n, 4, unsignedShort, Type.DOUBLE, 0);
                return 4;
            }
            case 23: {
                visitor.visitLocal(21, n, 4, unsignedShort, Type.FLOAT, 0);
                return 4;
            }
            case 22: {
                visitor.visitLocal(21, n, 4, unsignedShort, Type.LONG, 0);
                return 4;
            }
            case 21: {
                visitor.visitLocal(21, n, 4, unsignedShort, Type.INT, 0);
                return 4;
            }
        }
    }
    
    public int byteLength() {
        return this.bytes.size() + 4;
    }
    
    public void forEach(final Visitor visitor) {
        for (int size = this.bytes.size(), i = 0; i < size; i += this.parseInstruction(i, visitor)) {}
    }
    
    public ByteArray getBytes() {
        return this.bytes;
    }
    
    public int[] getInstructionOffsets() {
        final int size = this.bytes.size();
        final int[] bitSet = Bits.makeBitSet(size);
        for (int i = 0; i < size; i += this.parseInstruction(i, null)) {
            Bits.set(bitSet, i, true);
        }
        return bitSet;
    }
    
    public int parseInstruction(final int n, final Visitor visitor) {
        Visitor empty_VISITOR = visitor;
        if (visitor == null) {
            empty_VISITOR = BytecodeArray.EMPTY_VISITOR;
        }
        int unsignedByte;
        int int1;
        int n2;
        Constant value;
        int value2;
        Constant value3;
        int value4;
        int short1;
        int byte1;
        SimException ex;
        StringBuilder sb;
        StringBuilder sb2;
        Label_2241_Outer:Label_2296_Outer:
        while (true) {
            while (true) {
            Label_2743:
                while (true) {
                Label_2738:
                    while (true) {
                        Label_2731: {
                            while (true) {
                                Label_2728: {
                                    try {
                                        unsignedByte = this.bytes.getUnsignedByte(n);
                                        ByteOps.opInfo(unsignedByte);
                                        switch (unsignedByte) {
                                            case 200:
                                            case 201: {
                                                int1 = this.bytes.getInt(n + 1);
                                                if (unsignedByte == 200) {
                                                    n2 = 167;
                                                    empty_VISITOR.visitBranch(n2, n, 5, int1 + n);
                                                    return 5;
                                                }
                                                break Label_2731;
                                            }
                                            case 197: {
                                                empty_VISITOR.visitConstant(unsignedByte, n, 4, this.pool.get(this.bytes.getUnsignedShort(n + 1)), this.bytes.getUnsignedByte(n + 3));
                                                return 4;
                                            }
                                            case 196: {
                                                return this.parseWide(n, empty_VISITOR);
                                            }
                                            case 188: {
                                                return this.parseNewarray(n, empty_VISITOR);
                                            }
                                            case 186: {
                                                throw new ParseException("invokedynamic not supported");
                                            }
                                            case 185: {
                                                empty_VISITOR.visitConstant(unsignedByte, n, 5, this.pool.get(this.bytes.getUnsignedShort(n + 1)), this.bytes.getUnsignedByte(n + 3) | this.bytes.getUnsignedByte(n + 4) << 8);
                                                return 5;
                                            }
                                            case 178:
                                            case 179:
                                            case 180:
                                            case 181:
                                            case 182:
                                            case 183:
                                            case 184:
                                            case 187:
                                            case 189:
                                            case 192:
                                            case 193: {
                                                empty_VISITOR.visitConstant(unsignedByte, n, 3, this.pool.get(this.bytes.getUnsignedShort(n + 1)), 0);
                                                return 3;
                                            }
                                            case 177:
                                            case 191:
                                            case 194:
                                            case 195: {
                                                empty_VISITOR.visitNoArgs(unsignedByte, n, 1, Type.VOID);
                                                return 1;
                                            }
                                            case 176: {
                                                empty_VISITOR.visitNoArgs(172, n, 1, Type.OBJECT);
                                                return 1;
                                            }
                                            case 175: {
                                                empty_VISITOR.visitNoArgs(172, n, 1, Type.DOUBLE);
                                                return 1;
                                            }
                                            case 174: {
                                                empty_VISITOR.visitNoArgs(172, n, 1, Type.FLOAT);
                                                return 1;
                                            }
                                            case 173: {
                                                empty_VISITOR.visitNoArgs(172, n, 1, Type.LONG);
                                                return 1;
                                            }
                                            case 172: {
                                                empty_VISITOR.visitNoArgs(172, n, 1, Type.INT);
                                                return 1;
                                            }
                                            case 171: {
                                                return this.parseLookupswitch(n, empty_VISITOR);
                                            }
                                            case 170: {
                                                return this.parseTableswitch(n, empty_VISITOR);
                                            }
                                            case 169: {
                                                empty_VISITOR.visitLocal(unsignedByte, n, 2, this.bytes.getUnsignedByte(n + 1), Type.RETURN_ADDRESS, 0);
                                                return 2;
                                            }
                                            case 153:
                                            case 154:
                                            case 155:
                                            case 156:
                                            case 157:
                                            case 158:
                                            case 159:
                                            case 160:
                                            case 161:
                                            case 162:
                                            case 163:
                                            case 164:
                                            case 165:
                                            case 166:
                                            case 167:
                                            case 168:
                                            case 198:
                                            case 199: {
                                                empty_VISITOR.visitBranch(unsignedByte, n, 3, this.bytes.getShort(n + 1) + n);
                                                return 3;
                                            }
                                            case 136:
                                            case 139:
                                            case 142:
                                            case 145:
                                            case 146:
                                            case 147:
                                            case 148:
                                            case 149:
                                            case 150:
                                            case 151:
                                            case 152:
                                            case 190: {
                                                empty_VISITOR.visitNoArgs(unsignedByte, n, 1, Type.INT);
                                                return 1;
                                            }
                                            case 135:
                                            case 138:
                                            case 141: {
                                                empty_VISITOR.visitNoArgs(unsignedByte, n, 1, Type.DOUBLE);
                                                return 1;
                                            }
                                            case 134:
                                            case 137:
                                            case 144: {
                                                empty_VISITOR.visitNoArgs(unsignedByte, n, 1, Type.FLOAT);
                                                return 1;
                                            }
                                            case 133:
                                            case 140:
                                            case 143: {
                                                empty_VISITOR.visitNoArgs(unsignedByte, n, 1, Type.LONG);
                                                return 1;
                                            }
                                            case 132: {
                                                empty_VISITOR.visitLocal(unsignedByte, n, 3, this.bytes.getUnsignedByte(n + 1), Type.INT, this.bytes.getByte(n + 2));
                                                return 3;
                                            }
                                            case 99:
                                            case 103:
                                            case 107:
                                            case 111:
                                            case 115:
                                            case 119: {
                                                empty_VISITOR.visitNoArgs(unsignedByte - 3, n, 1, Type.DOUBLE);
                                                return 1;
                                            }
                                            case 98:
                                            case 102:
                                            case 106:
                                            case 110:
                                            case 114:
                                            case 118: {
                                                empty_VISITOR.visitNoArgs(unsignedByte - 2, n, 1, Type.FLOAT);
                                                return 1;
                                            }
                                            case 97:
                                            case 101:
                                            case 105:
                                            case 109:
                                            case 113:
                                            case 117:
                                            case 121:
                                            case 123:
                                            case 125:
                                            case 127:
                                            case 129:
                                            case 131: {
                                                empty_VISITOR.visitNoArgs(unsignedByte - 1, n, 1, Type.LONG);
                                                return 1;
                                            }
                                            case 96:
                                            case 100:
                                            case 104:
                                            case 108:
                                            case 112:
                                            case 116:
                                            case 120:
                                            case 122:
                                            case 124:
                                            case 126:
                                            case 128:
                                            case 130: {
                                                empty_VISITOR.visitNoArgs(unsignedByte, n, 1, Type.INT);
                                                return 1;
                                            }
                                            case 87:
                                            case 88:
                                            case 89:
                                            case 90:
                                            case 91:
                                            case 92:
                                            case 93:
                                            case 94:
                                            case 95: {
                                                empty_VISITOR.visitNoArgs(unsignedByte, n, 1, Type.VOID);
                                                return 1;
                                            }
                                            case 86: {
                                                empty_VISITOR.visitNoArgs(79, n, 1, Type.SHORT);
                                                return 1;
                                            }
                                            case 85: {
                                                empty_VISITOR.visitNoArgs(79, n, 1, Type.CHAR);
                                                return 1;
                                            }
                                            case 84: {
                                                empty_VISITOR.visitNoArgs(79, n, 1, Type.BYTE);
                                                return 1;
                                            }
                                            case 83: {
                                                empty_VISITOR.visitNoArgs(79, n, 1, Type.OBJECT);
                                                return 1;
                                            }
                                            case 82: {
                                                empty_VISITOR.visitNoArgs(79, n, 1, Type.DOUBLE);
                                                return 1;
                                            }
                                            case 81: {
                                                empty_VISITOR.visitNoArgs(79, n, 1, Type.FLOAT);
                                                return 1;
                                            }
                                            case 80: {
                                                empty_VISITOR.visitNoArgs(79, n, 1, Type.LONG);
                                                return 1;
                                            }
                                            case 79: {
                                                empty_VISITOR.visitNoArgs(79, n, 1, Type.INT);
                                                return 1;
                                            }
                                            case 75:
                                            case 76:
                                            case 77:
                                            case 78: {
                                                empty_VISITOR.visitLocal(54, n, 1, unsignedByte - 75, Type.OBJECT, 0);
                                                return 1;
                                            }
                                            case 71:
                                            case 72:
                                            case 73:
                                            case 74: {
                                                empty_VISITOR.visitLocal(54, n, 1, unsignedByte - 71, Type.DOUBLE, 0);
                                                return 1;
                                            }
                                            case 67:
                                            case 68:
                                            case 69:
                                            case 70: {
                                                empty_VISITOR.visitLocal(54, n, 1, unsignedByte - 67, Type.FLOAT, 0);
                                                return 1;
                                            }
                                            case 63:
                                            case 64:
                                            case 65:
                                            case 66: {
                                                empty_VISITOR.visitLocal(54, n, 1, unsignedByte - 63, Type.LONG, 0);
                                                return 1;
                                            }
                                            case 59:
                                            case 60:
                                            case 61:
                                            case 62: {
                                                empty_VISITOR.visitLocal(54, n, 1, unsignedByte - 59, Type.INT, 0);
                                                return 1;
                                            }
                                            case 58: {
                                                empty_VISITOR.visitLocal(54, n, 2, this.bytes.getUnsignedByte(n + 1), Type.OBJECT, 0);
                                                return 2;
                                            }
                                            case 57: {
                                                empty_VISITOR.visitLocal(54, n, 2, this.bytes.getUnsignedByte(n + 1), Type.DOUBLE, 0);
                                                return 2;
                                            }
                                            case 56: {
                                                empty_VISITOR.visitLocal(54, n, 2, this.bytes.getUnsignedByte(n + 1), Type.FLOAT, 0);
                                                return 2;
                                            }
                                            case 55: {
                                                empty_VISITOR.visitLocal(54, n, 2, this.bytes.getUnsignedByte(n + 1), Type.LONG, 0);
                                                return 2;
                                            }
                                            case 54: {
                                                empty_VISITOR.visitLocal(54, n, 2, this.bytes.getUnsignedByte(n + 1), Type.INT, 0);
                                                return 2;
                                            }
                                            case 53: {
                                                empty_VISITOR.visitNoArgs(46, n, 1, Type.SHORT);
                                                return 1;
                                            }
                                            case 52: {
                                                empty_VISITOR.visitNoArgs(46, n, 1, Type.CHAR);
                                                return 1;
                                            }
                                            case 51: {
                                                empty_VISITOR.visitNoArgs(46, n, 1, Type.BYTE);
                                                return 1;
                                            }
                                            case 50: {
                                                empty_VISITOR.visitNoArgs(46, n, 1, Type.OBJECT);
                                                return 1;
                                            }
                                            case 49: {
                                                empty_VISITOR.visitNoArgs(46, n, 1, Type.DOUBLE);
                                                return 1;
                                            }
                                            case 48: {
                                                empty_VISITOR.visitNoArgs(46, n, 1, Type.FLOAT);
                                                return 1;
                                            }
                                            case 47: {
                                                empty_VISITOR.visitNoArgs(46, n, 1, Type.LONG);
                                                return 1;
                                            }
                                            case 46: {
                                                empty_VISITOR.visitNoArgs(46, n, 1, Type.INT);
                                                return 1;
                                            }
                                            case 42:
                                            case 43:
                                            case 44:
                                            case 45: {
                                                empty_VISITOR.visitLocal(21, n, 1, unsignedByte - 42, Type.OBJECT, 0);
                                                return 1;
                                            }
                                            case 38:
                                            case 39:
                                            case 40:
                                            case 41: {
                                                empty_VISITOR.visitLocal(21, n, 1, unsignedByte - 38, Type.DOUBLE, 0);
                                                return 1;
                                            }
                                            case 34:
                                            case 35:
                                            case 36:
                                            case 37: {
                                                empty_VISITOR.visitLocal(21, n, 1, unsignedByte - 34, Type.FLOAT, 0);
                                                return 1;
                                            }
                                            case 30:
                                            case 31:
                                            case 32:
                                            case 33: {
                                                empty_VISITOR.visitLocal(21, n, 1, unsignedByte - 30, Type.LONG, 0);
                                                return 1;
                                            }
                                            case 26:
                                            case 27:
                                            case 28:
                                            case 29: {
                                                empty_VISITOR.visitLocal(21, n, 1, unsignedByte - 26, Type.INT, 0);
                                                return 1;
                                            }
                                            case 25: {
                                                empty_VISITOR.visitLocal(21, n, 2, this.bytes.getUnsignedByte(n + 1), Type.OBJECT, 0);
                                                return 2;
                                            }
                                            case 24: {
                                                empty_VISITOR.visitLocal(21, n, 2, this.bytes.getUnsignedByte(n + 1), Type.DOUBLE, 0);
                                                return 2;
                                            }
                                            case 23: {
                                                empty_VISITOR.visitLocal(21, n, 2, this.bytes.getUnsignedByte(n + 1), Type.FLOAT, 0);
                                                return 2;
                                            }
                                            case 22: {
                                                empty_VISITOR.visitLocal(21, n, 2, this.bytes.getUnsignedByte(n + 1), Type.LONG, 0);
                                                return 2;
                                            }
                                            case 21: {
                                                empty_VISITOR.visitLocal(21, n, 2, this.bytes.getUnsignedByte(n + 1), Type.INT, 0);
                                                return 2;
                                            }
                                            case 20: {
                                                empty_VISITOR.visitConstant(20, n, 3, this.pool.get(this.bytes.getUnsignedShort(n + 1)), 0);
                                                return 3;
                                            }
                                            case 19: {
                                                value = this.pool.get(this.bytes.getUnsignedShort(n + 1));
                                                if (value instanceof CstInteger) {
                                                    value2 = ((CstInteger)value).getValue();
                                                    empty_VISITOR.visitConstant(18, n, 3, value, value2);
                                                    return 3;
                                                }
                                                break Label_2738;
                                            }
                                            case 18: {
                                                value3 = this.pool.get(this.bytes.getUnsignedByte(n + 1));
                                                if (value3 instanceof CstInteger) {
                                                    value4 = ((CstInteger)value3).getValue();
                                                    empty_VISITOR.visitConstant(18, n, 2, value3, value4);
                                                    return 2;
                                                }
                                                break Label_2743;
                                            }
                                            case 17: {
                                                short1 = this.bytes.getShort(n + 1);
                                                empty_VISITOR.visitConstant(18, n, 3, CstInteger.make(short1), short1);
                                                return 3;
                                            }
                                            case 16: {
                                                byte1 = this.bytes.getByte(n + 1);
                                                empty_VISITOR.visitConstant(18, n, 2, CstInteger.make(byte1), byte1);
                                                return 2;
                                            }
                                            case 15: {
                                                empty_VISITOR.visitConstant(18, n, 1, CstDouble.VALUE_1, 0);
                                                return 1;
                                            }
                                            case 14: {
                                                empty_VISITOR.visitConstant(18, n, 1, CstDouble.VALUE_0, 0);
                                                return 1;
                                            }
                                            case 13: {
                                                empty_VISITOR.visitConstant(18, n, 1, CstFloat.VALUE_2, 0);
                                                return 1;
                                            }
                                            case 12: {
                                                empty_VISITOR.visitConstant(18, n, 1, CstFloat.VALUE_1, 0);
                                                return 1;
                                            }
                                            case 11: {
                                                empty_VISITOR.visitConstant(18, n, 1, CstFloat.VALUE_0, 0);
                                                return 1;
                                            }
                                            case 10: {
                                                empty_VISITOR.visitConstant(18, n, 1, CstLong.VALUE_1, 0);
                                                return 1;
                                            }
                                            case 9: {
                                                empty_VISITOR.visitConstant(18, n, 1, CstLong.VALUE_0, 0);
                                                return 1;
                                            }
                                            case 8: {
                                                empty_VISITOR.visitConstant(18, n, 1, CstInteger.VALUE_5, 5);
                                                return 1;
                                            }
                                            case 7: {
                                                empty_VISITOR.visitConstant(18, n, 1, CstInteger.VALUE_4, 4);
                                                return 1;
                                            }
                                            case 6: {
                                                empty_VISITOR.visitConstant(18, n, 1, CstInteger.VALUE_3, 3);
                                                return 1;
                                            }
                                            case 5: {
                                                empty_VISITOR.visitConstant(18, n, 1, CstInteger.VALUE_2, 2);
                                                return 1;
                                            }
                                            case 4: {
                                                empty_VISITOR.visitConstant(18, n, 1, CstInteger.VALUE_1, 1);
                                                return 1;
                                            }
                                            case 3: {
                                                empty_VISITOR.visitConstant(18, n, 1, CstInteger.VALUE_0, 0);
                                                return 1;
                                            }
                                            case 2: {
                                                empty_VISITOR.visitConstant(18, n, 1, CstInteger.VALUE_M1, -1);
                                                return 1;
                                            }
                                            case 1: {
                                                empty_VISITOR.visitConstant(18, n, 1, CstKnownNull.THE_ONE, 0);
                                                return 1;
                                            }
                                            case 0: {
                                                empty_VISITOR.visitNoArgs(unsignedByte, n, 1, Type.VOID);
                                                return 1;
                                            }
                                            default: {
                                                break Label_2728;
                                            }
                                        }
                                        empty_VISITOR.visitInvalid(unsignedByte, n, 1);
                                        return 1;
                                    }
                                    catch (RuntimeException ex2) {
                                        ex = new SimException(ex2);
                                        sb = new StringBuilder();
                                        sb.append("...at bytecode offset ");
                                        sb.append(Hex.u4(n));
                                        ex.addContext(sb.toString());
                                        throw ex;
                                    }
                                    catch (SimException ex3) {
                                        sb2 = new StringBuilder();
                                        sb2.append("...at bytecode offset ");
                                        sb2.append(Hex.u4(n));
                                        ex3.addContext(sb2.toString());
                                        throw ex3;
                                    }
                                }
                                continue;
                            }
                        }
                        n2 = 168;
                        continue Label_2241_Outer;
                    }
                    value2 = 0;
                    continue Label_2296_Outer;
                }
                value4 = 0;
                continue;
            }
        }
    }
    
    public void processWorkSet(final int[] array, final Visitor visitor) {
        if (visitor == null) {
            throw new NullPointerException("visitor == null");
        }
        while (true) {
            final int first = Bits.findFirst(array, 0);
            if (first < 0) {
                break;
            }
            Bits.clear(array, first);
            this.parseInstruction(first, visitor);
            visitor.setPreviousOffset(first);
        }
    }
    
    public int size() {
        return this.bytes.size();
    }
    
    public static class BaseVisitor implements Visitor
    {
        private int previousOffset;
        
        BaseVisitor() {
            this.previousOffset = -1;
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
        public void visitBranch(final int n, final int n2, final int n3, final int n4) {
        }
        
        @Override
        public void visitConstant(final int n, final int n2, final int n3, final Constant constant, final int n4) {
        }
        
        @Override
        public void visitInvalid(final int n, final int n2, final int n3) {
        }
        
        @Override
        public void visitLocal(final int n, final int n2, final int n3, final int n4, final Type type, final int n5) {
        }
        
        @Override
        public void visitNewarray(final int n, final int n2, final CstType cstType, final ArrayList<Constant> list) {
        }
        
        @Override
        public void visitNoArgs(final int n, final int n2, final int n3, final Type type) {
        }
        
        @Override
        public void visitSwitch(final int n, final int n2, final int n3, final SwitchList list, final int n4) {
        }
    }
    
    class ConstantParserVisitor extends BaseVisitor
    {
        Constant cst;
        int length;
        int value;
        
        private void clear() {
            this.length = 0;
        }
        
        @Override
        public int getPreviousOffset() {
            return -1;
        }
        
        @Override
        public void setPreviousOffset(final int n) {
        }
        
        @Override
        public void visitBranch(final int n, final int n2, final int n3, final int n4) {
            this.clear();
        }
        
        @Override
        public void visitConstant(final int n, final int n2, final int length, final Constant cst, final int value) {
            this.cst = cst;
            this.length = length;
            this.value = value;
        }
        
        @Override
        public void visitInvalid(final int n, final int n2, final int n3) {
            this.clear();
        }
        
        @Override
        public void visitLocal(final int n, final int n2, final int n3, final int n4, final Type type, final int n5) {
            this.clear();
        }
        
        @Override
        public void visitNewarray(final int n, final int n2, final CstType cstType, final ArrayList<Constant> list) {
            this.clear();
        }
        
        @Override
        public void visitNoArgs(final int n, final int n2, final int n3, final Type type) {
            this.clear();
        }
        
        @Override
        public void visitSwitch(final int n, final int n2, final int n3, final SwitchList list, final int n4) {
            this.clear();
        }
    }
    
    public interface Visitor
    {
        int getPreviousOffset();
        
        void setPreviousOffset(final int p0);
        
        void visitBranch(final int p0, final int p1, final int p2, final int p3);
        
        void visitConstant(final int p0, final int p1, final int p2, final Constant p3, final int p4);
        
        void visitInvalid(final int p0, final int p1, final int p2);
        
        void visitLocal(final int p0, final int p1, final int p2, final int p3, final Type p4, final int p5);
        
        void visitNewarray(final int p0, final int p1, final CstType p2, final ArrayList<Constant> p3);
        
        void visitNoArgs(final int p0, final int p1, final int p2, final Type p3);
        
        void visitSwitch(final int p0, final int p1, final int p2, final SwitchList p3, final int p4);
    }
}
