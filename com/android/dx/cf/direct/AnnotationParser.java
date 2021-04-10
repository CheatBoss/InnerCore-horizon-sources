package com.android.dx.cf.direct;

import com.android.dx.rop.type.*;
import java.io.*;
import com.android.dx.util.*;
import com.android.dx.rop.annotation.*;
import com.android.dx.cf.iface.*;
import com.android.dx.rop.cst.*;

public final class AnnotationParser
{
    private final ByteArray bytes;
    private final DirectClassFile cf;
    private final ByteArray.MyDataInputStream input;
    private final ParseObserver observer;
    private int parseCursor;
    private final ConstantPool pool;
    
    public AnnotationParser(final DirectClassFile cf, final int n, final int n2, final ParseObserver observer) {
        if (cf == null) {
            throw new NullPointerException("cf == null");
        }
        this.cf = cf;
        this.pool = cf.getConstantPool();
        this.observer = observer;
        this.bytes = cf.getBytes().slice(n, n + n2);
        this.input = this.bytes.makeDataInputStream();
        this.parseCursor = 0;
    }
    
    private void changeIndent(final int n) {
        this.observer.changeIndent(n);
    }
    
    private Annotation parseAnnotation(final AnnotationVisibility annotationVisibility) throws IOException {
        this.requireLength(4);
        final int unsignedShort = this.input.readUnsignedShort();
        final int unsignedShort2 = this.input.readUnsignedShort();
        final CstType cstType = new CstType(Type.intern(((CstString)this.pool.get(unsignedShort)).getString()));
        if (this.observer != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("type: ");
            sb.append(cstType.toHuman());
            this.parsed(2, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("num_elements: ");
            sb2.append(unsignedShort2);
            this.parsed(2, sb2.toString());
        }
        final Annotation annotation = new Annotation(cstType, annotationVisibility);
        for (int i = 0; i < unsignedShort2; ++i) {
            if (this.observer != null) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("elements[");
                sb3.append(i);
                sb3.append("]:");
                this.parsed(0, sb3.toString());
                this.changeIndent(1);
            }
            annotation.add(this.parseElement());
            if (this.observer != null) {
                this.changeIndent(-1);
            }
        }
        annotation.setImmutable();
        return annotation;
    }
    
    private Annotations parseAnnotations(final AnnotationVisibility annotationVisibility) throws IOException {
        final int unsignedShort = this.input.readUnsignedShort();
        if (this.observer != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("num_annotations: ");
            sb.append(Hex.u2(unsignedShort));
            this.parsed(2, sb.toString());
        }
        final Annotations annotations = new Annotations();
        for (int i = 0; i < unsignedShort; ++i) {
            if (this.observer != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("annotations[");
                sb2.append(i);
                sb2.append("]:");
                this.parsed(0, sb2.toString());
                this.changeIndent(1);
            }
            annotations.add(this.parseAnnotation(annotationVisibility));
            if (this.observer != null) {
                this.observer.changeIndent(-1);
            }
        }
        annotations.setImmutable();
        return annotations;
    }
    
    private AnnotationsList parseAnnotationsList(final AnnotationVisibility annotationVisibility) throws IOException {
        final int unsignedByte = this.input.readUnsignedByte();
        if (this.observer != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("num_parameters: ");
            sb.append(Hex.u1(unsignedByte));
            this.parsed(1, sb.toString());
        }
        final AnnotationsList list = new AnnotationsList(unsignedByte);
        for (int i = 0; i < unsignedByte; ++i) {
            if (this.observer != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("parameter_annotations[");
                sb2.append(i);
                sb2.append("]:");
                this.parsed(0, sb2.toString());
                this.changeIndent(1);
            }
            list.set(i, this.parseAnnotations(annotationVisibility));
            if (this.observer != null) {
                this.observer.changeIndent(-1);
            }
        }
        list.setImmutable();
        return list;
    }
    
    private Constant parseConstant() throws IOException {
        final Constant value = this.pool.get(this.input.readUnsignedShort());
        if (this.observer != null) {
            String s;
            if (value instanceof CstString) {
                s = ((CstString)value).toQuoted();
            }
            else {
                s = value.toHuman();
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("constant_value: ");
            sb.append(s);
            this.parsed(2, sb.toString());
        }
        return value;
    }
    
    private NameValuePair parseElement() throws IOException {
        this.requireLength(5);
        final CstString cstString = (CstString)this.pool.get(this.input.readUnsignedShort());
        if (this.observer != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("element_name: ");
            sb.append(cstString.toHuman());
            this.parsed(2, sb.toString());
            this.parsed(0, "value: ");
            this.changeIndent(1);
        }
        final Constant value = this.parseValue();
        if (this.observer != null) {
            this.changeIndent(-1);
        }
        return new NameValuePair(cstString, value);
    }
    
    private Constant parseValue() throws IOException {
        final int unsignedByte = this.input.readUnsignedByte();
        if (this.observer != null) {
            final CstString cstString = new CstString(Character.toString((char)unsignedByte));
            final StringBuilder sb = new StringBuilder();
            sb.append("tag: ");
            sb.append(cstString.toQuoted());
            this.parsed(1, sb.toString());
        }
        switch (unsignedByte) {
            default: {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("unknown annotation tag: ");
                sb2.append(Hex.u1(unsignedByte));
                throw new ParseException(sb2.toString());
            }
            case 115: {
                return this.parseConstant();
            }
            case 101: {
                this.requireLength(4);
                final int unsignedShort = this.input.readUnsignedShort();
                final int unsignedShort2 = this.input.readUnsignedShort();
                final CstString cstString2 = (CstString)this.pool.get(unsignedShort);
                final CstString cstString3 = (CstString)this.pool.get(unsignedShort2);
                if (this.observer != null) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("type_name: ");
                    sb3.append(cstString2.toHuman());
                    this.parsed(2, sb3.toString());
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("const_name: ");
                    sb4.append(cstString3.toHuman());
                    this.parsed(2, sb4.toString());
                }
                return new CstEnumRef(new CstNat(cstString3, cstString2));
            }
            case 99: {
                final Type internReturnType = Type.internReturnType(((CstString)this.pool.get(this.input.readUnsignedShort())).getString());
                if (this.observer != null) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("class_info: ");
                    sb5.append(internReturnType.toHuman());
                    this.parsed(2, sb5.toString());
                }
                return new CstType(internReturnType);
            }
            case 91: {
                this.requireLength(2);
                final int unsignedShort3 = this.input.readUnsignedShort();
                final CstArray.List list = new CstArray.List(unsignedShort3);
                if (this.observer != null) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("num_values: ");
                    sb6.append(unsignedShort3);
                    this.parsed(2, sb6.toString());
                    this.changeIndent(1);
                }
                for (int i = 0; i < unsignedShort3; ++i) {
                    if (this.observer != null) {
                        this.changeIndent(-1);
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append("element_value[");
                        sb7.append(i);
                        sb7.append("]:");
                        this.parsed(0, sb7.toString());
                        this.changeIndent(1);
                    }
                    list.set(i, this.parseValue());
                }
                if (this.observer != null) {
                    this.changeIndent(-1);
                }
                list.setImmutable();
                return new CstArray(list);
            }
            case 90: {
                return CstBoolean.make(((CstInteger)this.parseConstant()).getValue());
            }
            case 83: {
                return CstShort.make(((CstInteger)this.parseConstant()).getValue());
            }
            case 74: {
                return this.parseConstant();
            }
            case 73: {
                return this.parseConstant();
            }
            case 70: {
                return this.parseConstant();
            }
            case 68: {
                return this.parseConstant();
            }
            case 67: {
                final CstInteger cstInteger = (CstInteger)this.parseConstant();
                cstInteger.getValue();
                return CstChar.make(cstInteger.getValue());
            }
            case 66: {
                return CstByte.make(((CstInteger)this.parseConstant()).getValue());
            }
            case 64: {
                return new CstAnnotation(this.parseAnnotation(AnnotationVisibility.EMBEDDED));
            }
        }
    }
    
    private void parsed(final int n, final String s) {
        this.observer.parsed(this.bytes, this.parseCursor, n, s);
        this.parseCursor += n;
    }
    
    private void requireLength(final int n) throws IOException {
        if (this.input.available() < n) {
            throw new ParseException("truncated annotation attribute");
        }
    }
    
    public Annotations parseAnnotationAttribute(final AnnotationVisibility annotationVisibility) {
        try {
            final Annotations annotations = this.parseAnnotations(annotationVisibility);
            if (this.input.available() != 0) {
                throw new ParseException("extra data in attribute");
            }
            return annotations;
        }
        catch (IOException ex) {
            throw new RuntimeException("shouldn't happen", ex);
        }
    }
    
    public AnnotationsList parseParameterAttribute(final AnnotationVisibility annotationVisibility) {
        try {
            final AnnotationsList annotationsList = this.parseAnnotationsList(annotationVisibility);
            if (this.input.available() != 0) {
                throw new ParseException("extra data in attribute");
            }
            return annotationsList;
        }
        catch (IOException ex) {
            throw new RuntimeException("shouldn't happen", ex);
        }
    }
    
    public Constant parseValueAttribute() {
        try {
            final Constant value = this.parseValue();
            if (this.input.available() != 0) {
                throw new ParseException("extra data in attribute");
            }
            return value;
        }
        catch (IOException ex) {
            throw new RuntimeException("shouldn't happen", ex);
        }
    }
}
