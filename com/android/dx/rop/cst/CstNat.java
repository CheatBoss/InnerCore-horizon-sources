package com.android.dx.rop.cst;

import com.android.dx.rop.type.*;

public final class CstNat extends Constant
{
    public static final CstNat PRIMITIVE_TYPE_NAT;
    private final CstString descriptor;
    private final CstString name;
    
    static {
        PRIMITIVE_TYPE_NAT = new CstNat(new CstString("TYPE"), new CstString("Ljava/lang/Class;"));
    }
    
    public CstNat(final CstString name, final CstString descriptor) {
        if (name == null) {
            throw new NullPointerException("name == null");
        }
        if (descriptor == null) {
            throw new NullPointerException("descriptor == null");
        }
        this.name = name;
        this.descriptor = descriptor;
    }
    
    @Override
    protected int compareTo0(final Constant constant) {
        final CstNat cstNat = (CstNat)constant;
        final int compareTo = this.name.compareTo((Constant)cstNat.name);
        if (compareTo != 0) {
            return compareTo;
        }
        return this.descriptor.compareTo((Constant)cstNat.descriptor);
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof CstNat;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final CstNat cstNat = (CstNat)o;
        boolean b3 = b2;
        if (this.name.equals(cstNat.name)) {
            b3 = b2;
            if (this.descriptor.equals(cstNat.descriptor)) {
                b3 = true;
            }
        }
        return b3;
    }
    
    public CstString getDescriptor() {
        return this.descriptor;
    }
    
    public Type getFieldType() {
        return Type.intern(this.descriptor.getString());
    }
    
    public CstString getName() {
        return this.name;
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode() * 31 ^ this.descriptor.hashCode();
    }
    
    @Override
    public boolean isCategory2() {
        return false;
    }
    
    public final boolean isClassInit() {
        return this.name.getString().equals("<clinit>");
    }
    
    public final boolean isInstanceInit() {
        return this.name.getString().equals("<init>");
    }
    
    @Override
    public String toHuman() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.name.toHuman());
        sb.append(':');
        sb.append(this.descriptor.toHuman());
        return sb.toString();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("nat{");
        sb.append(this.toHuman());
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public String typeName() {
        return "nat";
    }
}
