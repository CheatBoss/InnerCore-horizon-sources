package com.android.dx.cf.iface;

import com.android.dx.rop.cst.*;

public interface Member extends HasAttribute
{
    int getAccessFlags();
    
    AttributeList getAttributes();
    
    CstType getDefiningClass();
    
    CstString getDescriptor();
    
    CstString getName();
    
    CstNat getNat();
}
