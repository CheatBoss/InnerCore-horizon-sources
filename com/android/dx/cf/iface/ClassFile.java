package com.android.dx.cf.iface;

import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;

public interface ClassFile extends HasAttribute
{
    int getAccessFlags();
    
    AttributeList getAttributes();
    
    ConstantPool getConstantPool();
    
    FieldList getFields();
    
    TypeList getInterfaces();
    
    int getMagic();
    
    int getMajorVersion();
    
    MethodList getMethods();
    
    int getMinorVersion();
    
    CstString getSourceFile();
    
    CstType getSuperclass();
    
    CstType getThisClass();
}
