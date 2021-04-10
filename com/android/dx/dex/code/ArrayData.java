package com.android.dx.dex.code;

import java.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.util.*;
import com.android.dx.rop.cst.*;

public final class ArrayData extends VariableSizeInsn
{
    private final Constant arrayType;
    private final int elemWidth;
    private final int initLength;
    private final CodeAddress user;
    private final ArrayList<Constant> values;
    
    public ArrayData(final SourcePosition sourcePosition, final CodeAddress user, final ArrayList<Constant> values, final Constant arrayType) {
        super(sourcePosition, RegisterSpecList.EMPTY);
        if (user == null) {
            throw new NullPointerException("user == null");
        }
        if (values == null) {
            throw new NullPointerException("values == null");
        }
        if (values.size() <= 0) {
            throw new IllegalArgumentException("Illegal number of init values");
        }
        this.arrayType = arrayType;
        if (arrayType != CstType.BYTE_ARRAY && arrayType != CstType.BOOLEAN_ARRAY) {
            if (arrayType != CstType.SHORT_ARRAY && arrayType != CstType.CHAR_ARRAY) {
                if (arrayType != CstType.INT_ARRAY && arrayType != CstType.FLOAT_ARRAY) {
                    if (arrayType != CstType.LONG_ARRAY && arrayType != CstType.DOUBLE_ARRAY) {
                        throw new IllegalArgumentException("Unexpected constant type");
                    }
                    this.elemWidth = 8;
                }
                else {
                    this.elemWidth = 4;
                }
            }
            else {
                this.elemWidth = 2;
            }
        }
        else {
            this.elemWidth = 1;
        }
        this.user = user;
        this.values = values;
        this.initLength = values.size();
    }
    
    @Override
    protected String argString() {
        final StringBuffer sb = new StringBuffer(100);
        for (int size = this.values.size(), i = 0; i < size; ++i) {
            sb.append("\n    ");
            sb.append(i);
            sb.append(": ");
            sb.append(this.values.get(i).toHuman());
        }
        return sb.toString();
    }
    
    @Override
    public int codeSize() {
        return (this.elemWidth * this.initLength + 1) / 2 + 4;
    }
    
    @Override
    protected String listingString0(final boolean b) {
        final int address = this.user.getAddress();
        final StringBuffer sb = new StringBuffer(100);
        final int size = this.values.size();
        sb.append("fill-array-data-payload // for fill-array-data @ ");
        sb.append(Hex.u2(address));
        for (int i = 0; i < size; ++i) {
            sb.append("\n  ");
            sb.append(i);
            sb.append(": ");
            sb.append(this.values.get(i).toHuman());
        }
        return sb.toString();
    }
    
    @Override
    public DalvInsn withRegisters(final RegisterSpecList list) {
        return new ArrayData(this.getPosition(), this.user, this.values, this.arrayType);
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput) {
        final int size = this.values.size();
        annotatedOutput.writeShort(768);
        annotatedOutput.writeShort(this.elemWidth);
        annotatedOutput.writeInt(this.initLength);
        final int elemWidth = this.elemWidth;
        if (elemWidth != 4) {
            if (elemWidth != 8) {
                switch (elemWidth) {
                    case 2: {
                        for (int i = 0; i < size; ++i) {
                            annotatedOutput.writeShort((short)((CstLiteral32)this.values.get(i)).getIntBits());
                        }
                        break;
                    }
                    case 1: {
                        for (int j = 0; j < size; ++j) {
                            annotatedOutput.writeByte((byte)((CstLiteral32)this.values.get(j)).getIntBits());
                        }
                        break;
                    }
                }
            }
            else {
                for (int k = 0; k < size; ++k) {
                    annotatedOutput.writeLong(((CstLiteral64)this.values.get(k)).getLongBits());
                }
            }
        }
        else {
            for (int l = 0; l < size; ++l) {
                annotatedOutput.writeInt(((CstLiteral32)this.values.get(l)).getIntBits());
            }
        }
        if (this.elemWidth == 1 && size % 2 != 0) {
            annotatedOutput.writeByte(0);
        }
    }
}
