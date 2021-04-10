package com.android.dx.cf.iface;

import com.android.dx.rop.cst.*;

public abstract class StdMember implements Member
{
    private final int accessFlags;
    private final AttributeList attributes;
    private final CstType definingClass;
    private final CstNat nat;
    
    public StdMember(final CstType definingClass, final int accessFlags, final CstNat nat, final AttributeList attributes) {
        if (definingClass == null) {
            throw new NullPointerException("definingClass == null");
        }
        if (nat == null) {
            throw new NullPointerException("nat == null");
        }
        if (attributes == null) {
            throw new NullPointerException("attributes == null");
        }
        this.definingClass = definingClass;
        this.accessFlags = accessFlags;
        this.nat = nat;
        this.attributes = attributes;
    }
    
    @Override
    public final int getAccessFlags() {
        return this.accessFlags;
    }
    
    @Override
    public final AttributeList getAttributes() {
        return this.attributes;
    }
    
    @Override
    public final CstType getDefiningClass() {
        return this.definingClass;
    }
    
    @Override
    public final CstString getDescriptor() {
        return this.nat.getDescriptor();
    }
    
    @Override
    public final CstString getName() {
        return this.nat.getName();
    }
    
    @Override
    public final CstNat getNat() {
        return this.nat;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(100);
        sb.append(this.getClass().getName());
        sb.append('{');
        sb.append(this.nat.toHuman());
        sb.append('}');
        return sb.toString();
    }
}
