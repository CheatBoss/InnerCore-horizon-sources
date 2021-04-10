package com.android.dx.cf.direct;

import com.android.dx.rop.code.*;
import com.android.dx.rop.cst.*;
import com.android.dx.cf.iface.*;

final class MethodListParser extends MemberListParser
{
    private final StdMethodList methods;
    
    public MethodListParser(final DirectClassFile directClassFile, final CstType cstType, final int n, final AttributeFactory attributeFactory) {
        super(directClassFile, cstType, n, attributeFactory);
        this.methods = new StdMethodList(this.getCount());
    }
    
    @Override
    protected int getAttributeContext() {
        return 2;
    }
    
    public StdMethodList getList() {
        this.parseIfNecessary();
        return this.methods;
    }
    
    @Override
    protected String humanAccessFlags(final int n) {
        return AccessFlags.methodString(n);
    }
    
    @Override
    protected String humanName() {
        return "method";
    }
    
    @Override
    protected Member set(final int n, final int n2, final CstNat cstNat, final AttributeList list) {
        final StdMethod stdMethod = new StdMethod(this.getDefiner(), n2, cstNat, list);
        this.methods.set(n, stdMethod);
        return stdMethod;
    }
}
