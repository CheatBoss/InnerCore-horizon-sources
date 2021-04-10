package com.android.dx.dex.file;

import com.android.dx.rop.annotation.*;
import com.android.dx.util.*;
import java.util.*;
import com.android.dex.*;
import com.android.dex.util.*;
import com.android.dx.rop.cst.*;

public final class ValueEncoder
{
    private static final int VALUE_ANNOTATION = 29;
    private static final int VALUE_ARRAY = 28;
    private static final int VALUE_BOOLEAN = 31;
    private static final int VALUE_BYTE = 0;
    private static final int VALUE_CHAR = 3;
    private static final int VALUE_DOUBLE = 17;
    private static final int VALUE_ENUM = 27;
    private static final int VALUE_FIELD = 25;
    private static final int VALUE_FLOAT = 16;
    private static final int VALUE_INT = 4;
    private static final int VALUE_LONG = 6;
    private static final int VALUE_METHOD = 26;
    private static final int VALUE_NULL = 30;
    private static final int VALUE_SHORT = 2;
    private static final int VALUE_STRING = 23;
    private static final int VALUE_TYPE = 24;
    private final DexFile file;
    private final AnnotatedOutput out;
    
    public ValueEncoder(final DexFile file, final AnnotatedOutput out) {
        if (file == null) {
            throw new NullPointerException("file == null");
        }
        if (out == null) {
            throw new NullPointerException("out == null");
        }
        this.file = file;
        this.out = out;
    }
    
    public static void addContents(final DexFile dexFile, final Annotation annotation) {
        final TypeIdsSection typeIds = dexFile.getTypeIds();
        final StringIdsSection stringIds = dexFile.getStringIds();
        typeIds.intern(annotation.getType());
        for (final NameValuePair nameValuePair : annotation.getNameValuePairs()) {
            stringIds.intern(nameValuePair.getName());
            addContents(dexFile, nameValuePair.getValue());
        }
    }
    
    public static void addContents(final DexFile dexFile, final Constant constant) {
        if (constant instanceof CstAnnotation) {
            addContents(dexFile, ((CstAnnotation)constant).getAnnotation());
            return;
        }
        if (constant instanceof CstArray) {
            final CstArray.List list = ((CstArray)constant).getList();
            for (int size = list.size(), i = 0; i < size; ++i) {
                addContents(dexFile, list.get(i));
            }
            return;
        }
        dexFile.internIfAppropriate(constant);
    }
    
    public static String constantToHuman(final Constant constant) {
        if (constantToValueType(constant) == 30) {
            return "null";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(constant.typeName());
        sb.append(' ');
        sb.append(constant.toHuman());
        return sb.toString();
    }
    
    private static int constantToValueType(final Constant constant) {
        if (constant instanceof CstByte) {
            return 0;
        }
        if (constant instanceof CstShort) {
            return 2;
        }
        if (constant instanceof CstChar) {
            return 3;
        }
        if (constant instanceof CstInteger) {
            return 4;
        }
        if (constant instanceof CstLong) {
            return 6;
        }
        if (constant instanceof CstFloat) {
            return 16;
        }
        if (constant instanceof CstDouble) {
            return 17;
        }
        if (constant instanceof CstString) {
            return 23;
        }
        if (constant instanceof CstType) {
            return 24;
        }
        if (constant instanceof CstFieldRef) {
            return 25;
        }
        if (constant instanceof CstMethodRef) {
            return 26;
        }
        if (constant instanceof CstEnumRef) {
            return 27;
        }
        if (constant instanceof CstArray) {
            return 28;
        }
        if (constant instanceof CstAnnotation) {
            return 29;
        }
        if (constant instanceof CstKnownNull) {
            return 30;
        }
        if (constant instanceof CstBoolean) {
            return 31;
        }
        throw new RuntimeException("Shouldn't happen");
    }
    
    public void writeAnnotation(final Annotation annotation, final boolean b) {
        final boolean b2 = b && this.out.annotates();
        final StringIdsSection stringIds = this.file.getStringIds();
        final TypeIdsSection typeIds = this.file.getTypeIds();
        final CstType type = annotation.getType();
        final int index = typeIds.indexOf(type);
        if (b2) {
            final AnnotatedOutput out = this.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("  type_idx: ");
            sb.append(Hex.u4(index));
            sb.append(" // ");
            sb.append(type.toHuman());
            out.annotate(sb.toString());
        }
        this.out.writeUleb128(typeIds.indexOf(annotation.getType()));
        final Collection<NameValuePair> nameValuePairs = annotation.getNameValuePairs();
        final int size = nameValuePairs.size();
        if (b2) {
            final AnnotatedOutput out2 = this.out;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  size: ");
            sb2.append(Hex.u4(size));
            out2.annotate(sb2.toString());
        }
        this.out.writeUleb128(size);
        int n = 0;
        final Iterator<NameValuePair> iterator = nameValuePairs.iterator();
        final StringIdsSection stringIdsSection = stringIds;
        while (iterator.hasNext()) {
            final NameValuePair nameValuePair = iterator.next();
            final CstString name = nameValuePair.getName();
            final int index2 = stringIdsSection.indexOf(name);
            final Constant value = nameValuePair.getValue();
            if (b2) {
                final AnnotatedOutput out3 = this.out;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("  elements[");
                sb3.append(n);
                sb3.append("]:");
                out3.annotate(0, sb3.toString());
                ++n;
                final AnnotatedOutput out4 = this.out;
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("    name_idx: ");
                sb4.append(Hex.u4(index2));
                sb4.append(" // ");
                sb4.append(name.toHuman());
                out4.annotate(sb4.toString());
            }
            this.out.writeUleb128(index2);
            if (b2) {
                final AnnotatedOutput out5 = this.out;
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("    value: ");
                sb5.append(constantToHuman(value));
                out5.annotate(sb5.toString());
            }
            this.writeConstant(value);
        }
        if (b2) {
            this.out.endAnnotation();
        }
    }
    
    public void writeArray(final CstArray cstArray, final boolean b) {
        int i = 0;
        final boolean b2 = b && this.out.annotates();
        final CstArray.List list = cstArray.getList();
        final int size = list.size();
        if (b2) {
            final AnnotatedOutput out = this.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("  size: ");
            sb.append(Hex.u4(size));
            out.annotate(sb.toString());
        }
        this.out.writeUleb128(size);
        while (i < size) {
            final Constant value = list.get(i);
            if (b2) {
                final AnnotatedOutput out2 = this.out;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("  [");
                sb2.append(Integer.toHexString(i));
                sb2.append("] ");
                sb2.append(constantToHuman(value));
                out2.annotate(sb2.toString());
            }
            this.writeConstant(value);
            ++i;
        }
        if (b2) {
            this.out.endAnnotation();
        }
    }
    
    public void writeConstant(final Constant constant) {
        final int constantToValueType = constantToValueType(constant);
        if (constantToValueType != 0 && constantToValueType != 6) {
            switch (constantToValueType) {
                default: {
                    switch (constantToValueType) {
                        default: {
                            switch (constantToValueType) {
                                default: {
                                    throw new RuntimeException("Shouldn't happen");
                                }
                                case 31: {
                                    this.out.writeByte(((CstBoolean)constant).getIntBits() << 5 | constantToValueType);
                                    return;
                                }
                                case 30: {
                                    this.out.writeByte(constantToValueType);
                                    return;
                                }
                                case 29: {
                                    this.out.writeByte(constantToValueType);
                                    this.writeAnnotation(((CstAnnotation)constant).getAnnotation(), false);
                                    return;
                                }
                                case 28: {
                                    this.out.writeByte(constantToValueType);
                                    this.writeArray((CstArray)constant, false);
                                    return;
                                }
                                case 27: {
                                    EncodedValueCodec.writeUnsignedIntegralValue(this.out, constantToValueType, this.file.getFieldIds().indexOf(((CstEnumRef)constant).getFieldRef()));
                                    return;
                                }
                                case 26: {
                                    EncodedValueCodec.writeUnsignedIntegralValue(this.out, constantToValueType, this.file.getMethodIds().indexOf((CstBaseMethodRef)constant));
                                    return;
                                }
                                case 25: {
                                    EncodedValueCodec.writeUnsignedIntegralValue(this.out, constantToValueType, this.file.getFieldIds().indexOf((CstFieldRef)constant));
                                    return;
                                }
                                case 24: {
                                    EncodedValueCodec.writeUnsignedIntegralValue(this.out, constantToValueType, this.file.getTypeIds().indexOf((CstType)constant));
                                    return;
                                }
                                case 23: {
                                    EncodedValueCodec.writeUnsignedIntegralValue(this.out, constantToValueType, this.file.getStringIds().indexOf((CstString)constant));
                                    return;
                                }
                            }
                            break;
                        }
                        case 17: {
                            EncodedValueCodec.writeRightZeroExtendedValue(this.out, constantToValueType, ((CstDouble)constant).getLongBits());
                            return;
                        }
                        case 16: {
                            EncodedValueCodec.writeRightZeroExtendedValue(this.out, constantToValueType, ((CstFloat)constant).getLongBits() << 32);
                            return;
                        }
                    }
                    break;
                }
                case 3: {
                    EncodedValueCodec.writeUnsignedIntegralValue(this.out, constantToValueType, ((CstLiteralBits)constant).getLongBits());
                    return;
                }
                case 2:
                case 4: {
                    break;
                }
            }
        }
        EncodedValueCodec.writeSignedIntegralValue(this.out, constantToValueType, ((CstLiteralBits)constant).getLongBits());
    }
}
