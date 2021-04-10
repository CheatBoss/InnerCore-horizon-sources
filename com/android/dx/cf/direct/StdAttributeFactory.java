package com.android.dx.cf.direct;

import com.android.dx.util.*;
import com.android.dx.rop.cst.*;
import com.android.dx.rop.code.*;
import com.android.dx.cf.code.*;
import java.io.*;
import com.android.dx.rop.annotation.*;
import com.android.dx.cf.attrib.*;
import com.android.dx.cf.iface.*;

public class StdAttributeFactory extends AttributeFactory
{
    public static final StdAttributeFactory THE_ONE;
    
    static {
        THE_ONE = new StdAttributeFactory();
    }
    
    private Attribute annotationDefault(final DirectClassFile directClassFile, final int n, final int n2, final ParseObserver parseObserver) {
        if (n2 < 2) {
            throwSeverelyTruncated();
        }
        return new AttAnnotationDefault(new AnnotationParser(directClassFile, n, n2, parseObserver).parseValueAttribute(), n2);
    }
    
    private Attribute code(final DirectClassFile directClassFile, final int n, int n2, final ParseObserver observer) {
        if (n2 < 12) {
            return throwSeverelyTruncated();
        }
        final ByteArray bytes = directClassFile.getBytes();
        final ConstantPool constantPool = directClassFile.getConstantPool();
        final int unsignedShort = bytes.getUnsignedShort(n);
        final int unsignedShort2 = bytes.getUnsignedShort(n + 2);
        final int int1 = bytes.getInt(n + 4);
        if (observer != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("max_stack: ");
            sb.append(Hex.u2(unsignedShort));
            observer.parsed(bytes, n, 2, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("max_locals: ");
            sb2.append(Hex.u2(unsignedShort2));
            observer.parsed(bytes, n + 2, 2, sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("code_length: ");
            sb3.append(Hex.u4(int1));
            observer.parsed(bytes, n + 4, 4, sb3.toString());
        }
        final int n3 = n + 8;
        final int n4 = n2 - 8;
        if (n4 < int1 + 4) {
            return throwTruncated();
        }
        n2 = n3;
        final int n5 = n3 + int1;
        final BytecodeArray bytecodeArray = new BytecodeArray(bytes.slice(n2, n2 + int1), constantPool);
        if (observer != null) {
            bytecodeArray.forEach((BytecodeArray.Visitor)new CodeObserver(bytecodeArray.getBytes(), observer));
        }
        final int unsignedShort3 = bytes.getUnsignedShort(n5);
        ByteCatchList empty;
        if (unsignedShort3 == 0) {
            empty = ByteCatchList.EMPTY;
        }
        else {
            empty = new ByteCatchList(unsignedShort3);
        }
        if (observer != null) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("exception_table_length: ");
            sb4.append(Hex.u2(unsignedShort3));
            observer.parsed(bytes, n5, 2, sb4.toString());
        }
        int n6 = n5 + 2;
        int n7 = n4 - int1 - 2;
        if (n7 < unsignedShort3 * 8 + 2) {
            return throwTruncated();
        }
        for (int i = 0; i < unsignedShort3; ++i) {
            if (observer != null) {
                observer.changeIndent(1);
            }
            final int unsignedShort4 = bytes.getUnsignedShort(n6);
            final int unsignedShort5 = bytes.getUnsignedShort(n6 + 2);
            final int unsignedShort6 = bytes.getUnsignedShort(n6 + 4);
            final CstType cstType = (CstType)constantPool.get0Ok(bytes.getUnsignedShort(n6 + 6));
            empty.set(i, unsignedShort4, unsignedShort5, unsignedShort6, cstType);
            if (observer != null) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append(Hex.u2(unsignedShort4));
                sb5.append("..");
                sb5.append(Hex.u2(unsignedShort5));
                sb5.append(" -> ");
                sb5.append(Hex.u2(unsignedShort6));
                sb5.append(" ");
                String human;
                if (cstType == null) {
                    human = "<any>";
                }
                else {
                    human = cstType.toHuman();
                }
                sb5.append(human);
                observer.parsed(bytes, n6, 8, sb5.toString());
            }
            n6 += 8;
            n7 -= 8;
            if (observer != null) {
                observer.changeIndent(-1);
            }
        }
        empty.setImmutable();
        final AttributeListParser attributeListParser = new AttributeListParser(directClassFile, 3, n6, this);
        attributeListParser.setObserver(observer);
        final StdAttributeList list = attributeListParser.getList();
        list.setImmutable();
        n2 = attributeListParser.getEndOffset() - n6;
        if (n2 != n7) {
            return throwBadLength(n6 - n + n2);
        }
        return new AttCode(unsignedShort, unsignedShort2, bytecodeArray, empty, list);
    }
    
    private Attribute constantValue(final DirectClassFile directClassFile, final int n, final int n2, final ParseObserver parseObserver) {
        if (n2 != 2) {
            return throwBadLength(2);
        }
        final ByteArray bytes = directClassFile.getBytes();
        final TypedConstant typedConstant = (TypedConstant)directClassFile.getConstantPool().get(bytes.getUnsignedShort(n));
        final AttConstantValue attConstantValue = new AttConstantValue(typedConstant);
        if (parseObserver != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("value: ");
            sb.append(typedConstant);
            parseObserver.parsed(bytes, n, 2, sb.toString());
        }
        return attConstantValue;
    }
    
    private Attribute deprecated(final DirectClassFile directClassFile, final int n, final int n2, final ParseObserver parseObserver) {
        if (n2 != 0) {
            return throwBadLength(0);
        }
        return new AttDeprecated();
    }
    
    private Attribute enclosingMethod(final DirectClassFile directClassFile, final int n, final int n2, final ParseObserver parseObserver) {
        if (n2 != 4) {
            throwBadLength(4);
        }
        final ByteArray bytes = directClassFile.getBytes();
        final ConstantPool constantPool = directClassFile.getConstantPool();
        final CstType cstType = (CstType)constantPool.get(bytes.getUnsignedShort(n));
        final CstNat cstNat = (CstNat)constantPool.get0Ok(bytes.getUnsignedShort(n + 2));
        final AttEnclosingMethod attEnclosingMethod = new AttEnclosingMethod(cstType, cstNat);
        if (parseObserver != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("class: ");
            sb.append(cstType);
            parseObserver.parsed(bytes, n, 2, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("method: ");
            sb2.append(DirectClassFile.stringOrNone(cstNat));
            parseObserver.parsed(bytes, n + 2, 2, sb2.toString());
        }
        return attEnclosingMethod;
    }
    
    private Attribute exceptions(final DirectClassFile directClassFile, final int n, final int n2, final ParseObserver parseObserver) {
        if (n2 < 2) {
            return throwSeverelyTruncated();
        }
        final ByteArray bytes = directClassFile.getBytes();
        final int unsignedShort = bytes.getUnsignedShort(n);
        if (parseObserver != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("number_of_exceptions: ");
            sb.append(Hex.u2(unsignedShort));
            parseObserver.parsed(bytes, n, 2, sb.toString());
        }
        if (n2 - 2 != unsignedShort * 2) {
            throwBadLength(unsignedShort * 2 + 2);
        }
        return new AttExceptions(directClassFile.makeTypeList(n + 2, unsignedShort));
    }
    
    private Attribute innerClasses(final DirectClassFile directClassFile, int i, int n, final ParseObserver parseObserver) {
        if (n < 2) {
            return throwSeverelyTruncated();
        }
        final ByteArray bytes = directClassFile.getBytes();
        final ConstantPool constantPool = directClassFile.getConstantPool();
        final int unsignedShort = bytes.getUnsignedShort(i);
        if (parseObserver != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("number_of_classes: ");
            sb.append(Hex.u2(unsignedShort));
            parseObserver.parsed(bytes, i, 2, sb.toString());
        }
        final int n2 = i + 2;
        if (n - 2 != unsignedShort * 8) {
            throwBadLength(unsignedShort * 8 + 2);
        }
        final InnerClassList list = new InnerClassList(unsignedShort);
        i = 0;
        n = n2;
        while (i < unsignedShort) {
            final int unsignedShort2 = bytes.getUnsignedShort(n);
            final int unsignedShort3 = bytes.getUnsignedShort(n + 2);
            final int unsignedShort4 = bytes.getUnsignedShort(n + 4);
            final int unsignedShort5 = bytes.getUnsignedShort(n + 6);
            final CstType cstType = (CstType)constantPool.get(unsignedShort2);
            final CstType cstType2 = (CstType)constantPool.get0Ok(unsignedShort3);
            final CstString cstString = (CstString)constantPool.get0Ok(unsignedShort4);
            list.set(i, cstType, cstType2, cstString, unsignedShort5);
            if (parseObserver != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("inner_class: ");
                sb2.append(DirectClassFile.stringOrNone(cstType));
                parseObserver.parsed(bytes, n, 2, sb2.toString());
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("  outer_class: ");
                sb3.append(DirectClassFile.stringOrNone(cstType2));
                parseObserver.parsed(bytes, n + 2, 2, sb3.toString());
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("  name: ");
                sb4.append(DirectClassFile.stringOrNone(cstString));
                parseObserver.parsed(bytes, n + 4, 2, sb4.toString());
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("  access_flags: ");
                sb5.append(AccessFlags.innerClassString(unsignedShort5));
                parseObserver.parsed(bytes, n + 6, 2, sb5.toString());
            }
            n += 8;
            ++i;
        }
        list.setImmutable();
        return new AttInnerClasses(list);
    }
    
    private Attribute lineNumberTable(final DirectClassFile directClassFile, int i, int n, final ParseObserver parseObserver) {
        if (n < 2) {
            return throwSeverelyTruncated();
        }
        final ByteArray bytes = directClassFile.getBytes();
        final int unsignedShort = bytes.getUnsignedShort(i);
        if (parseObserver != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("line_number_table_length: ");
            sb.append(Hex.u2(unsignedShort));
            parseObserver.parsed(bytes, i, 2, sb.toString());
        }
        i += 2;
        if (n - 2 != unsignedShort * 4) {
            throwBadLength(unsignedShort * 4 + 2);
        }
        final LineNumberList list = new LineNumberList(unsignedShort);
        final int n2 = 0;
        n = i;
        int unsignedShort2;
        int unsignedShort3;
        StringBuilder sb2;
        for (i = n2; i < unsignedShort; ++i) {
            unsignedShort2 = bytes.getUnsignedShort(n);
            unsignedShort3 = bytes.getUnsignedShort(n + 2);
            list.set(i, unsignedShort2, unsignedShort3);
            if (parseObserver != null) {
                sb2 = new StringBuilder();
                sb2.append(Hex.u2(unsignedShort2));
                sb2.append(" ");
                sb2.append(unsignedShort3);
                parseObserver.parsed(bytes, n, 4, sb2.toString());
            }
            n += 4;
        }
        list.setImmutable();
        return new AttLineNumberTable(list);
    }
    
    private Attribute localVariableTable(final DirectClassFile directClassFile, final int n, final int n2, final ParseObserver parseObserver) {
        if (n2 < 2) {
            return throwSeverelyTruncated();
        }
        final ByteArray bytes = directClassFile.getBytes();
        final int unsignedShort = bytes.getUnsignedShort(n);
        if (parseObserver != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("local_variable_table_length: ");
            sb.append(Hex.u2(unsignedShort));
            parseObserver.parsed(bytes, n, 2, sb.toString());
        }
        return new AttLocalVariableTable(this.parseLocalVariables(bytes.slice(n + 2, n + n2), directClassFile.getConstantPool(), parseObserver, unsignedShort, false));
    }
    
    private Attribute localVariableTypeTable(final DirectClassFile directClassFile, final int n, final int n2, final ParseObserver parseObserver) {
        if (n2 < 2) {
            return throwSeverelyTruncated();
        }
        final ByteArray bytes = directClassFile.getBytes();
        final int unsignedShort = bytes.getUnsignedShort(n);
        if (parseObserver != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("local_variable_type_table_length: ");
            sb.append(Hex.u2(unsignedShort));
            parseObserver.parsed(bytes, n, 2, sb.toString());
        }
        return new AttLocalVariableTypeTable(this.parseLocalVariables(bytes.slice(n + 2, n + n2), directClassFile.getConstantPool(), parseObserver, unsignedShort, true));
    }
    
    private LocalVariableList parseLocalVariables(final ByteArray ex, final ConstantPool constantPool, final ParseObserver parseObserver, final int n, final boolean b) {
        if (((ByteArray)ex).size() != n * 10) {
            throwBadLength(n * 10 + 2);
        }
        final ByteArray.MyDataInputStream dataInputStream = ((ByteArray)ex).makeDataInputStream();
        final LocalVariableList list = new LocalVariableList(n);
        int i = 0;
    Label_0043:
        while (i < n) {
            while (true) {
                while (true) {
                    CstString cstString2 = null;
                    Label_0319: {
                        while (true) {
                            try {
                                final int unsignedShort = dataInputStream.readUnsignedShort();
                                final int unsignedShort2 = dataInputStream.readUnsignedShort();
                                final int unsignedShort3 = dataInputStream.readUnsignedShort();
                                final int unsignedShort4 = dataInputStream.readUnsignedShort();
                                final int unsignedShort5 = dataInputStream.readUnsignedShort();
                                final CstString cstString = (CstString)constantPool.get(unsignedShort3);
                                cstString2 = (CstString)constantPool.get(unsignedShort4);
                                final CstString cstString3 = null;
                                CstString cstString4 = null;
                                if (b) {
                                    cstString4 = cstString2;
                                    break Label_0316;
                                }
                                break Label_0319;
                                list.set(i, unsignedShort, unsignedShort2, cstString, cstString3, cstString4, unsignedShort5);
                                // iftrue(Label_0286:, parseObserver == null)
                                while (true) {
                                    Block_5: {
                                        break Block_5;
                                        ++i;
                                        continue Label_0043;
                                    }
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append(Hex.u2(unsignedShort));
                                    sb.append("..");
                                    sb.append(Hex.u2(unsignedShort + unsignedShort2));
                                    sb.append(" ");
                                    sb.append(Hex.u2(unsignedShort5));
                                    sb.append(" ");
                                    sb.append(cstString.toHuman());
                                    sb.append(" ");
                                    sb.append(cstString2.toHuman());
                                    final String string = sb.toString();
                                    try {
                                        parseObserver.parsed((ByteArray)ex, i * 10, 10, string);
                                    }
                                    catch (IOException ex) {
                                        break;
                                    }
                                    continue;
                                }
                            }
                            catch (IOException ex2) {}
                            break;
                            continue;
                        }
                    }
                    final CstString cstString3 = cstString2;
                    continue;
                }
            }
            throw new RuntimeException("shouldn't happen", ex);
        }
        list.setImmutable();
        return list;
    }
    
    private Attribute runtimeInvisibleAnnotations(final DirectClassFile directClassFile, final int n, final int n2, final ParseObserver parseObserver) {
        if (n2 < 2) {
            throwSeverelyTruncated();
        }
        return new AttRuntimeInvisibleAnnotations(new AnnotationParser(directClassFile, n, n2, parseObserver).parseAnnotationAttribute(AnnotationVisibility.BUILD), n2);
    }
    
    private Attribute runtimeInvisibleParameterAnnotations(final DirectClassFile directClassFile, final int n, final int n2, final ParseObserver parseObserver) {
        if (n2 < 2) {
            throwSeverelyTruncated();
        }
        return new AttRuntimeInvisibleParameterAnnotations(new AnnotationParser(directClassFile, n, n2, parseObserver).parseParameterAttribute(AnnotationVisibility.BUILD), n2);
    }
    
    private Attribute runtimeVisibleAnnotations(final DirectClassFile directClassFile, final int n, final int n2, final ParseObserver parseObserver) {
        if (n2 < 2) {
            throwSeverelyTruncated();
        }
        return new AttRuntimeVisibleAnnotations(new AnnotationParser(directClassFile, n, n2, parseObserver).parseAnnotationAttribute(AnnotationVisibility.RUNTIME), n2);
    }
    
    private Attribute runtimeVisibleParameterAnnotations(final DirectClassFile directClassFile, final int n, final int n2, final ParseObserver parseObserver) {
        if (n2 < 2) {
            throwSeverelyTruncated();
        }
        return new AttRuntimeVisibleParameterAnnotations(new AnnotationParser(directClassFile, n, n2, parseObserver).parseParameterAttribute(AnnotationVisibility.RUNTIME), n2);
    }
    
    private Attribute signature(final DirectClassFile directClassFile, final int n, final int n2, final ParseObserver parseObserver) {
        if (n2 != 2) {
            throwBadLength(2);
        }
        final ByteArray bytes = directClassFile.getBytes();
        final CstString cstString = (CstString)directClassFile.getConstantPool().get(bytes.getUnsignedShort(n));
        final AttSignature attSignature = new AttSignature(cstString);
        if (parseObserver != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("signature: ");
            sb.append(cstString);
            parseObserver.parsed(bytes, n, 2, sb.toString());
        }
        return attSignature;
    }
    
    private Attribute sourceFile(final DirectClassFile directClassFile, final int n, final int n2, final ParseObserver parseObserver) {
        if (n2 != 2) {
            throwBadLength(2);
        }
        final ByteArray bytes = directClassFile.getBytes();
        final CstString cstString = (CstString)directClassFile.getConstantPool().get(bytes.getUnsignedShort(n));
        final AttSourceFile attSourceFile = new AttSourceFile(cstString);
        if (parseObserver != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("source: ");
            sb.append(cstString);
            parseObserver.parsed(bytes, n, 2, sb.toString());
        }
        return attSourceFile;
    }
    
    private Attribute synthetic(final DirectClassFile directClassFile, final int n, final int n2, final ParseObserver parseObserver) {
        if (n2 != 0) {
            return throwBadLength(0);
        }
        return new AttSynthetic();
    }
    
    private static Attribute throwBadLength(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("bad attribute length; expected length ");
        sb.append(Hex.u4(n));
        throw new ParseException(sb.toString());
    }
    
    private static Attribute throwSeverelyTruncated() {
        throw new ParseException("severely truncated attribute");
    }
    
    private static Attribute throwTruncated() {
        throw new ParseException("truncated attribute");
    }
    
    @Override
    protected Attribute parse0(final DirectClassFile directClassFile, final int n, final String s, final int n2, final int n3, final ParseObserver parseObserver) {
        switch (n) {
            case 3: {
                if (s == "LineNumberTable") {
                    return this.lineNumberTable(directClassFile, n2, n3, parseObserver);
                }
                if (s == "LocalVariableTable") {
                    return this.localVariableTable(directClassFile, n2, n3, parseObserver);
                }
                if (s == "LocalVariableTypeTable") {
                    return this.localVariableTypeTable(directClassFile, n2, n3, parseObserver);
                }
                break;
            }
            case 2: {
                if (s == "AnnotationDefault") {
                    return this.annotationDefault(directClassFile, n2, n3, parseObserver);
                }
                if (s == "Code") {
                    return this.code(directClassFile, n2, n3, parseObserver);
                }
                if (s == "Deprecated") {
                    return this.deprecated(directClassFile, n2, n3, parseObserver);
                }
                if (s == "Exceptions") {
                    return this.exceptions(directClassFile, n2, n3, parseObserver);
                }
                if (s == "RuntimeInvisibleAnnotations") {
                    return this.runtimeInvisibleAnnotations(directClassFile, n2, n3, parseObserver);
                }
                if (s == "RuntimeVisibleAnnotations") {
                    return this.runtimeVisibleAnnotations(directClassFile, n2, n3, parseObserver);
                }
                if (s == "RuntimeInvisibleParameterAnnotations") {
                    return this.runtimeInvisibleParameterAnnotations(directClassFile, n2, n3, parseObserver);
                }
                if (s == "RuntimeVisibleParameterAnnotations") {
                    return this.runtimeVisibleParameterAnnotations(directClassFile, n2, n3, parseObserver);
                }
                if (s == "Signature") {
                    return this.signature(directClassFile, n2, n3, parseObserver);
                }
                if (s == "Synthetic") {
                    return this.synthetic(directClassFile, n2, n3, parseObserver);
                }
                break;
            }
            case 1: {
                if (s == "ConstantValue") {
                    return this.constantValue(directClassFile, n2, n3, parseObserver);
                }
                if (s == "Deprecated") {
                    return this.deprecated(directClassFile, n2, n3, parseObserver);
                }
                if (s == "RuntimeInvisibleAnnotations") {
                    return this.runtimeInvisibleAnnotations(directClassFile, n2, n3, parseObserver);
                }
                if (s == "RuntimeVisibleAnnotations") {
                    return this.runtimeVisibleAnnotations(directClassFile, n2, n3, parseObserver);
                }
                if (s == "Signature") {
                    return this.signature(directClassFile, n2, n3, parseObserver);
                }
                if (s == "Synthetic") {
                    return this.synthetic(directClassFile, n2, n3, parseObserver);
                }
                break;
            }
            case 0: {
                if (s == "Deprecated") {
                    return this.deprecated(directClassFile, n2, n3, parseObserver);
                }
                if (s == "EnclosingMethod") {
                    return this.enclosingMethod(directClassFile, n2, n3, parseObserver);
                }
                if (s == "InnerClasses") {
                    return this.innerClasses(directClassFile, n2, n3, parseObserver);
                }
                if (s == "RuntimeInvisibleAnnotations") {
                    return this.runtimeInvisibleAnnotations(directClassFile, n2, n3, parseObserver);
                }
                if (s == "RuntimeVisibleAnnotations") {
                    return this.runtimeVisibleAnnotations(directClassFile, n2, n3, parseObserver);
                }
                if (s == "Synthetic") {
                    return this.synthetic(directClassFile, n2, n3, parseObserver);
                }
                if (s == "Signature") {
                    return this.signature(directClassFile, n2, n3, parseObserver);
                }
                if (s == "SourceFile") {
                    return this.sourceFile(directClassFile, n2, n3, parseObserver);
                }
                break;
            }
        }
        return super.parse0(directClassFile, n, s, n2, n3, parseObserver);
    }
}
