package com.android.dx.rop.cst;

public abstract class CstMemberRef extends TypedConstant
{
    private final CstType definingClass;
    private final CstNat nat;
    
    CstMemberRef(final CstType definingClass, final CstNat nat) {
        if (definingClass == null) {
            throw new NullPointerException("definingClass == null");
        }
        if (nat == null) {
            throw new NullPointerException("nat == null");
        }
        this.definingClass = definingClass;
        this.nat = nat;
    }
    
    @Override
    protected int compareTo0(final Constant constant) {
        final CstMemberRef cstMemberRef = (CstMemberRef)constant;
        final int compareTo = this.definingClass.compareTo((Constant)cstMemberRef.definingClass);
        if (compareTo != 0) {
            return compareTo;
        }
        return this.nat.getName().compareTo((Constant)cstMemberRef.nat.getName());
    }
    
    @Override
    public final boolean equals(final Object o) {
        final boolean b = false;
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final CstMemberRef cstMemberRef = (CstMemberRef)o;
        boolean b2 = b;
        if (this.definingClass.equals(cstMemberRef.definingClass)) {
            b2 = b;
            if (this.nat.equals(cstMemberRef.nat)) {
                b2 = true;
            }
        }
        return b2;
    }
    
    public final CstType getDefiningClass() {
        return this.definingClass;
    }
    
    public final CstNat getNat() {
        return this.nat;
    }
    
    @Override
    public final int hashCode() {
        return this.definingClass.hashCode() * 31 ^ this.nat.hashCode();
    }
    
    @Override
    public final boolean isCategory2() {
        return false;
    }
    
    @Override
    public final String toHuman() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.definingClass.toHuman());
        sb.append('.');
        sb.append(this.nat.toHuman());
        return sb.toString();
    }
    
    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.typeName());
        sb.append('{');
        sb.append(this.toHuman());
        sb.append('}');
        return sb.toString();
    }
}
