package com.android.dx.cf.direct;

import com.android.dx.rop.code.*;
import com.android.dx.rop.cst.*;
import com.android.dx.cf.iface.*;

final class FieldListParser extends MemberListParser
{
    private final StdFieldList fields;
    
    public FieldListParser(final DirectClassFile directClassFile, final CstType cstType, final int n, final AttributeFactory attributeFactory) {
        super(directClassFile, cstType, n, attributeFactory);
        this.fields = new StdFieldList(this.getCount());
    }
    
    @Override
    protected int getAttributeContext() {
        return 1;
    }
    
    public StdFieldList getList() {
        this.parseIfNecessary();
        return this.fields;
    }
    
    @Override
    protected String humanAccessFlags(final int n) {
        return AccessFlags.fieldString(n);
    }
    
    @Override
    protected String humanName() {
        return "field";
    }
    
    @Override
    protected Member set(final int n, final int n2, final CstNat cstNat, final AttributeList list) {
        final StdField stdField = new StdField(this.getDefiner(), n2, cstNat, list);
        this.fields.set(n, stdField);
        return stdField;
    }
}
