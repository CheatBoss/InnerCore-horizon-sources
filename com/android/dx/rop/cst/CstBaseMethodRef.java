package com.android.dx.rop.cst;

import com.android.dx.rop.type.*;

public abstract class CstBaseMethodRef extends CstMemberRef
{
    private Prototype instancePrototype;
    private final Prototype prototype;
    
    CstBaseMethodRef(final CstType cstType, final CstNat cstNat) {
        super(cstType, cstNat);
        this.prototype = Prototype.intern(this.getNat().getDescriptor().getString());
        this.instancePrototype = null;
    }
    
    @Override
    protected final int compareTo0(final Constant constant) {
        final int compareTo0 = super.compareTo0(constant);
        if (compareTo0 != 0) {
            return compareTo0;
        }
        return this.prototype.compareTo(((CstBaseMethodRef)constant).prototype);
    }
    
    public final int getParameterWordCount(final boolean b) {
        return this.getPrototype(b).getParameterTypes().getWordCount();
    }
    
    public final Prototype getPrototype() {
        return this.prototype;
    }
    
    public final Prototype getPrototype(final boolean b) {
        if (b) {
            return this.prototype;
        }
        if (this.instancePrototype == null) {
            this.instancePrototype = this.prototype.withFirstParameter(this.getDefiningClass().getClassType());
        }
        return this.instancePrototype;
    }
    
    @Override
    public final Type getType() {
        return this.prototype.getReturnType();
    }
    
    public final boolean isClassInit() {
        return this.getNat().isClassInit();
    }
    
    public final boolean isInstanceInit() {
        return this.getNat().isInstanceInit();
    }
}
