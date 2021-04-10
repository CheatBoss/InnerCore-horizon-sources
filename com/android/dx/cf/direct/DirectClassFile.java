package com.android.dx.cf.direct;

import com.android.dx.util.*;
import com.android.dx.cf.cst.*;
import com.android.dx.rop.code.*;
import com.android.dx.rop.cst.*;
import com.android.dx.cf.attrib.*;
import com.android.dx.cf.iface.*;
import com.android.dx.rop.type.*;

public class DirectClassFile implements ClassFile
{
    private static final int CLASS_FILE_MAGIC = -889275714;
    private static final int CLASS_FILE_MAX_MAJOR_VERSION = 51;
    private static final int CLASS_FILE_MAX_MINOR_VERSION = 0;
    private static final int CLASS_FILE_MIN_MAJOR_VERSION = 45;
    private int accessFlags;
    private AttributeFactory attributeFactory;
    private StdAttributeList attributes;
    private final ByteArray bytes;
    private FieldList fields;
    private final String filePath;
    private TypeList interfaces;
    private MethodList methods;
    private ParseObserver observer;
    private StdConstantPool pool;
    private final boolean strictParse;
    private CstType superClass;
    private CstType thisClass;
    
    public DirectClassFile(final ByteArray bytes, final String filePath, final boolean strictParse) {
        if (bytes == null) {
            throw new NullPointerException("bytes == null");
        }
        if (filePath == null) {
            throw new NullPointerException("filePath == null");
        }
        this.filePath = filePath;
        this.bytes = bytes;
        this.strictParse = strictParse;
        this.accessFlags = -1;
    }
    
    public DirectClassFile(final byte[] array, final String s, final boolean b) {
        this(new ByteArray(array), s, b);
    }
    
    private boolean isGoodVersion(final int n, final int n2, final int n3) {
        if (n == -889275714 && n2 >= 0) {
            if (n3 == 51) {
                if (n2 <= 0) {
                    return true;
                }
            }
            else if (n3 < 51 && n3 >= 45) {
                return true;
            }
        }
        return false;
    }
    
    private void parse() {
        try {
            this.parse0();
        }
        catch (RuntimeException ex2) {
            final ParseException ex = new ParseException(ex2);
            final StringBuilder sb = new StringBuilder();
            sb.append("...while parsing ");
            sb.append(this.filePath);
            ex.addContext(sb.toString());
            throw ex;
        }
        catch (ParseException ex3) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("...while parsing ");
            sb2.append(this.filePath);
            ex3.addContext(sb2.toString());
            throw ex3;
        }
    }
    
    private void parse0() {
        if (this.bytes.size() < 10) {
            throw new ParseException("severely truncated class file");
        }
        if (this.observer != null) {
            this.observer.parsed(this.bytes, 0, 0, "begin classfile");
            final ParseObserver observer = this.observer;
            final ByteArray bytes = this.bytes;
            final StringBuilder sb = new StringBuilder();
            sb.append("magic: ");
            sb.append(Hex.u4(this.getMagic0()));
            observer.parsed(bytes, 0, 4, sb.toString());
            final ParseObserver observer2 = this.observer;
            final ByteArray bytes2 = this.bytes;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("minor_version: ");
            sb2.append(Hex.u2(this.getMinorVersion0()));
            observer2.parsed(bytes2, 4, 2, sb2.toString());
            final ParseObserver observer3 = this.observer;
            final ByteArray bytes3 = this.bytes;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("major_version: ");
            sb3.append(Hex.u2(this.getMajorVersion0()));
            observer3.parsed(bytes3, 6, 2, sb3.toString());
        }
        if (this.strictParse && !this.isGoodVersion(this.getMagic0(), this.getMinorVersion0(), this.getMajorVersion0())) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("bad class file magic (");
            sb4.append(Hex.u4(this.getMagic0()));
            sb4.append(") or version (");
            sb4.append(Hex.u2(this.getMajorVersion0()));
            sb4.append(".");
            sb4.append(Hex.u2(this.getMinorVersion0()));
            sb4.append(")");
            throw new ParseException(sb4.toString());
        }
        final ConstantPoolParser constantPoolParser = new ConstantPoolParser(this.bytes);
        constantPoolParser.setObserver(this.observer);
        (this.pool = constantPoolParser.getPool()).setImmutable();
        final int endOffset = constantPoolParser.getEndOffset();
        final int unsignedShort = this.bytes.getUnsignedShort(endOffset);
        this.thisClass = (CstType)this.pool.get(this.bytes.getUnsignedShort(endOffset + 2));
        this.superClass = (CstType)this.pool.get0Ok(this.bytes.getUnsignedShort(endOffset + 4));
        final int unsignedShort2 = this.bytes.getUnsignedShort(endOffset + 6);
        if (this.observer != null) {
            final ParseObserver observer4 = this.observer;
            final ByteArray bytes4 = this.bytes;
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("access_flags: ");
            sb5.append(AccessFlags.classString(unsignedShort));
            observer4.parsed(bytes4, endOffset, 2, sb5.toString());
            final ParseObserver observer5 = this.observer;
            final ByteArray bytes5 = this.bytes;
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("this_class: ");
            sb6.append(this.thisClass);
            observer5.parsed(bytes5, endOffset + 2, 2, sb6.toString());
            final ParseObserver observer6 = this.observer;
            final ByteArray bytes6 = this.bytes;
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("super_class: ");
            sb7.append(stringOrNone(this.superClass));
            observer6.parsed(bytes6, endOffset + 4, 2, sb7.toString());
            final ParseObserver observer7 = this.observer;
            final ByteArray bytes7 = this.bytes;
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("interfaces_count: ");
            sb8.append(Hex.u2(unsignedShort2));
            observer7.parsed(bytes7, endOffset + 6, 2, sb8.toString());
            if (unsignedShort2 != 0) {
                this.observer.parsed(this.bytes, endOffset + 8, 0, "interfaces:");
            }
        }
        final int n = endOffset + 8;
        this.interfaces = this.makeTypeList(n, unsignedShort2);
        if (this.strictParse) {
            final String className = this.thisClass.getClassType().getClassName();
            if (!this.filePath.endsWith(".class") || !this.filePath.startsWith(className) || this.filePath.length() != className.length() + 6) {
                final StringBuilder sb9 = new StringBuilder();
                sb9.append("class name (");
                sb9.append(className);
                sb9.append(") does not match path (");
                sb9.append(this.filePath);
                sb9.append(")");
                throw new ParseException(sb9.toString());
            }
        }
        this.accessFlags = unsignedShort;
        final FieldListParser fieldListParser = new FieldListParser(this, this.thisClass, n + unsignedShort2 * 2, this.attributeFactory);
        fieldListParser.setObserver(this.observer);
        this.fields = fieldListParser.getList();
        final MethodListParser methodListParser = new MethodListParser(this, this.thisClass, fieldListParser.getEndOffset(), this.attributeFactory);
        methodListParser.setObserver(this.observer);
        this.methods = methodListParser.getList();
        final AttributeListParser attributeListParser = new AttributeListParser(this, 0, methodListParser.getEndOffset(), this.attributeFactory);
        attributeListParser.setObserver(this.observer);
        (this.attributes = attributeListParser.getList()).setImmutable();
        final int endOffset2 = attributeListParser.getEndOffset();
        if (endOffset2 != this.bytes.size()) {
            final StringBuilder sb10 = new StringBuilder();
            sb10.append("extra bytes at end of class file, at offset ");
            sb10.append(Hex.u4(endOffset2));
            throw new ParseException(sb10.toString());
        }
        if (this.observer != null) {
            this.observer.parsed(this.bytes, endOffset2, 0, "end classfile");
        }
    }
    
    private void parseToEndIfNecessary() {
        if (this.attributes == null) {
            this.parse();
        }
    }
    
    private void parseToInterfacesIfNecessary() {
        if (this.accessFlags == -1) {
            this.parse();
        }
    }
    
    public static String stringOrNone(final Object o) {
        if (o == null) {
            return "(none)";
        }
        return o.toString();
    }
    
    @Override
    public int getAccessFlags() {
        this.parseToInterfacesIfNecessary();
        return this.accessFlags;
    }
    
    @Override
    public AttributeList getAttributes() {
        this.parseToEndIfNecessary();
        return this.attributes;
    }
    
    public ByteArray getBytes() {
        return this.bytes;
    }
    
    @Override
    public ConstantPool getConstantPool() {
        this.parseToInterfacesIfNecessary();
        return this.pool;
    }
    
    @Override
    public FieldList getFields() {
        this.parseToEndIfNecessary();
        return this.fields;
    }
    
    public String getFilePath() {
        return this.filePath;
    }
    
    @Override
    public TypeList getInterfaces() {
        this.parseToInterfacesIfNecessary();
        return this.interfaces;
    }
    
    @Override
    public int getMagic() {
        this.parseToInterfacesIfNecessary();
        return this.getMagic0();
    }
    
    public int getMagic0() {
        return this.bytes.getInt(0);
    }
    
    @Override
    public int getMajorVersion() {
        this.parseToInterfacesIfNecessary();
        return this.getMajorVersion0();
    }
    
    public int getMajorVersion0() {
        return this.bytes.getUnsignedShort(6);
    }
    
    @Override
    public MethodList getMethods() {
        this.parseToEndIfNecessary();
        return this.methods;
    }
    
    @Override
    public int getMinorVersion() {
        this.parseToInterfacesIfNecessary();
        return this.getMinorVersion0();
    }
    
    public int getMinorVersion0() {
        return this.bytes.getUnsignedShort(4);
    }
    
    @Override
    public CstString getSourceFile() {
        final Attribute first = this.getAttributes().findFirst("SourceFile");
        if (first instanceof AttSourceFile) {
            return ((AttSourceFile)first).getSourceFile();
        }
        return null;
    }
    
    @Override
    public CstType getSuperclass() {
        this.parseToInterfacesIfNecessary();
        return this.superClass;
    }
    
    @Override
    public CstType getThisClass() {
        this.parseToInterfacesIfNecessary();
        return this.thisClass;
    }
    
    public TypeList makeTypeList(final int n, final int n2) {
        if (n2 == 0) {
            return StdTypeList.EMPTY;
        }
        if (this.pool == null) {
            throw new IllegalStateException("pool not yet initialized");
        }
        return new DcfTypeList(this.bytes, n, n2, this.pool, this.observer);
    }
    
    public void setAttributeFactory(final AttributeFactory attributeFactory) {
        if (attributeFactory == null) {
            throw new NullPointerException("attributeFactory == null");
        }
        this.attributeFactory = attributeFactory;
    }
    
    public void setObserver(final ParseObserver observer) {
        this.observer = observer;
    }
    
    private static class DcfTypeList implements TypeList
    {
        private final ByteArray bytes;
        private final StdConstantPool pool;
        private final int size;
        
        public DcfTypeList(ByteArray slice, int i, final int size, final StdConstantPool pool, final ParseObserver parseObserver) {
            if (size < 0) {
                throw new IllegalArgumentException("size < 0");
            }
            slice = slice.slice(i, size * 2 + i);
            this.bytes = slice;
            this.size = size;
            this.pool = pool;
            i = 0;
            while (i < size) {
                final int n = i * 2;
                final int unsignedShort = slice.getUnsignedShort(n);
                try {
                    final CstType cstType = (CstType)pool.get(unsignedShort);
                    if (parseObserver != null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("  ");
                        sb.append(cstType);
                        parseObserver.parsed(slice, n, 2, sb.toString());
                    }
                    ++i;
                    continue;
                }
                catch (ClassCastException ex) {
                    throw new RuntimeException("bogus class cpi", ex);
                }
                break;
            }
        }
        
        @Override
        public Type getType(int unsignedShort) {
            unsignedShort = this.bytes.getUnsignedShort(unsignedShort * 2);
            return ((CstType)this.pool.get(unsignedShort)).getClassType();
        }
        
        @Override
        public int getWordCount() {
            return this.size;
        }
        
        @Override
        public boolean isMutable() {
            return false;
        }
        
        @Override
        public int size() {
            return this.size;
        }
        
        @Override
        public TypeList withAddedType(final Type type) {
            throw new UnsupportedOperationException("unsupported");
        }
    }
}
