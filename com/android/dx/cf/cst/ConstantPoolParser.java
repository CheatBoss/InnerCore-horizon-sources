package com.android.dx.cf.cst;

import com.android.dx.cf.iface.*;
import com.android.dx.util.*;
import java.util.*;
import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;

public final class ConstantPoolParser
{
    private final ByteArray bytes;
    private int endOffset;
    private ParseObserver observer;
    private final int[] offsets;
    private final StdConstantPool pool;
    
    public ConstantPoolParser(final ByteArray bytes) {
        final int unsignedShort = bytes.getUnsignedShort(8);
        this.bytes = bytes;
        this.pool = new StdConstantPool(unsignedShort);
        this.offsets = new int[unsignedShort];
        this.endOffset = -1;
    }
    
    private void determineOffsets() {
        int endOffset = 10;
        int n = 0;
    Label_0273:
        for (int i = 1; i < this.offsets.length; i += n) {
            this.offsets[i] = endOffset;
            final int unsignedByte = this.bytes.getUnsignedByte(endOffset);
            Label_0139: {
                switch (unsignedByte) {
                    default: {
                        break Label_0139;
                    }
                    case 18: {
                        break Label_0139;
                    }
                    case 16: {
                        break Label_0139;
                    }
                    case 15: {
                        break Label_0139;
                    }
                    case 1: {
                        Label_0149: {
                            break Label_0149;
                            try {
                                throw new ParseException("InvokeDynamic not supported");
                                throw new ParseException("MethodHandle not supported");
                                final StringBuilder sb = new StringBuilder();
                                sb.append("unknown tag byte: ");
                                sb.append(Hex.u1(unsignedByte));
                                throw new ParseException(sb.toString());
                                throw new ParseException("MethodType not supported");
                                n = 1;
                                endOffset += this.bytes.getUnsignedShort(endOffset + 1) + 3;
                                break;
                            }
                            catch (ParseException ex) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("...while preparsing cst ");
                                sb2.append(Hex.u2(i));
                                sb2.append(" at offset ");
                                sb2.append(Hex.u4(endOffset));
                                ex.addContext(sb2.toString());
                                throw ex;
                            }
                        }
                        break Label_0273;
                    }
                    case 7:
                    case 8: {
                        n = 1;
                        endOffset += 3;
                        break;
                    }
                    case 5:
                    case 6: {
                        n = 2;
                        endOffset += 9;
                        break;
                    }
                    case 3:
                    case 4:
                    case 9:
                    case 10:
                    case 11:
                    case 12: {
                        n = 1;
                        endOffset += 5;
                        break;
                    }
                }
            }
        }
        this.endOffset = endOffset;
    }
    
    private void parse() {
        this.determineOffsets();
        final ParseObserver observer = this.observer;
        final int n = 1;
        if (observer != null) {
            final ParseObserver observer2 = this.observer;
            final ByteArray bytes = this.bytes;
            final StringBuilder sb = new StringBuilder();
            sb.append("constant_pool_count: ");
            sb.append(Hex.u2(this.offsets.length));
            observer2.parsed(bytes, 8, 2, sb.toString());
            this.observer.parsed(this.bytes, 10, 0, "\nconstant_pool:");
            this.observer.changeIndent(1);
        }
        final BitSet set = new BitSet(this.offsets.length);
        for (int i = 1; i < this.offsets.length; ++i) {
            if (this.offsets[i] != 0 && this.pool.getOrNull(i) == null) {
                this.parse0(i, set);
            }
        }
        if (this.observer != null) {
            for (int j = n; j < this.offsets.length; ++j) {
                final Constant orNull = this.pool.getOrNull(j);
                if (orNull != null) {
                    final int n2 = this.offsets[j];
                    final int endOffset = this.endOffset;
                    int n3 = j + 1;
                    int n4;
                    while (true) {
                        n4 = endOffset;
                        if (n3 >= this.offsets.length) {
                            break;
                        }
                        n4 = this.offsets[n3];
                        if (n4 != 0) {
                            break;
                        }
                        ++n3;
                    }
                    StringBuilder sb2;
                    String string;
                    if (set.get(j)) {
                        sb2 = new StringBuilder();
                        sb2.append(Hex.u2(j));
                        sb2.append(": utf8{\"");
                        sb2.append(orNull.toHuman());
                        string = "\"}";
                    }
                    else {
                        sb2 = new StringBuilder();
                        sb2.append(Hex.u2(j));
                        sb2.append(": ");
                        string = orNull.toString();
                    }
                    sb2.append(string);
                    this.observer.parsed(this.bytes, n2, n4 - n2, sb2.toString());
                }
            }
            this.observer.changeIndent(-1);
            this.observer.parsed(this.bytes, this.endOffset, 0, "end constant_pool");
        }
    }
    
    private Constant parse0(final int n, final BitSet set) {
        final Constant orNull = this.pool.getOrNull(n);
        if (orNull != null) {
            return orNull;
        }
        while (true) {
            final int n2 = this.offsets[n];
            while (true) {
                Label_0653: {
                    try {
                        final int unsignedByte = this.bytes.getUnsignedByte(n2);
                        Constant constant = null;
                        switch (unsignedByte) {
                            case 2:
                            case 13:
                            case 14:
                            case 17: {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("unknown tag byte: ");
                                sb.append(Hex.u1(unsignedByte));
                                throw new ParseException(sb.toString());
                            }
                            case 18: {
                                throw new ParseException("InvokeDynamic not supported");
                            }
                            case 16: {
                                throw new ParseException("MethodType not supported");
                            }
                            case 15: {
                                throw new ParseException("MethodHandle not supported");
                            }
                            case 12: {
                                constant = new CstNat((CstString)this.parse0(this.bytes.getUnsignedShort(n2 + 1), set), (CstString)this.parse0(this.bytes.getUnsignedShort(n2 + 3), set));
                                break;
                            }
                            case 11: {
                                constant = new CstInterfaceMethodRef((CstType)this.parse0(this.bytes.getUnsignedShort(n2 + 1), set), (CstNat)this.parse0(this.bytes.getUnsignedShort(n2 + 3), set));
                                break;
                            }
                            case 10: {
                                constant = new CstMethodRef((CstType)this.parse0(this.bytes.getUnsignedShort(n2 + 1), set), (CstNat)this.parse0(this.bytes.getUnsignedShort(n2 + 3), set));
                                break;
                            }
                            case 9: {
                                constant = new CstFieldRef((CstType)this.parse0(this.bytes.getUnsignedShort(n2 + 1), set), (CstNat)this.parse0(this.bytes.getUnsignedShort(n2 + 3), set));
                                break;
                            }
                            case 8: {
                                constant = this.parse0(this.bytes.getUnsignedShort(n2 + 1), set);
                                break;
                            }
                            case 7: {
                                constant = new CstType(Type.internClassName(((CstString)this.parse0(this.bytes.getUnsignedShort(n2 + 1), set)).getString()));
                                break;
                            }
                            case 6: {
                                constant = CstDouble.make(this.bytes.getLong(n2 + 1));
                                break;
                            }
                            case 5: {
                                constant = CstLong.make(this.bytes.getLong(n2 + 1));
                                break;
                            }
                            case 4: {
                                constant = CstFloat.make(this.bytes.getInt(n2 + 1));
                                break;
                            }
                            case 3: {
                                constant = CstInteger.make(this.bytes.getInt(n2 + 1));
                                break;
                            }
                            case 1: {
                                final CstString utf8 = this.parseUtf8(n2);
                                set.set(n);
                                constant = utf8;
                                break;
                            }
                            default: {
                                break Label_0653;
                            }
                        }
                        this.pool.set(n, constant);
                        return constant;
                    }
                    catch (RuntimeException ex2) {
                        final ParseException ex = new ParseException(ex2);
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("...while parsing cst ");
                        sb2.append(Hex.u2(n));
                        sb2.append(" at offset ");
                        sb2.append(Hex.u4(n2));
                        ex.addContext(sb2.toString());
                        throw ex;
                    }
                    catch (ParseException ex3) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("...while parsing cst ");
                        sb3.append(Hex.u2(n));
                        sb3.append(" at offset ");
                        sb3.append(Hex.u4(n2));
                        ex3.addContext(sb3.toString());
                        throw ex3;
                    }
                }
                continue;
            }
        }
    }
    
    private void parseIfNecessary() {
        if (this.endOffset < 0) {
            this.parse();
        }
    }
    
    private CstString parseUtf8(int n) {
        final int unsignedShort = this.bytes.getUnsignedShort(n + 1);
        n += 3;
        final ByteArray slice = this.bytes.slice(n, n + unsignedShort);
        try {
            return new CstString(slice);
        }
        catch (IllegalArgumentException ex) {
            throw new ParseException(ex);
        }
    }
    
    public int getEndOffset() {
        this.parseIfNecessary();
        return this.endOffset;
    }
    
    public StdConstantPool getPool() {
        this.parseIfNecessary();
        return this.pool;
    }
    
    public void setObserver(final ParseObserver observer) {
        this.observer = observer;
    }
}
