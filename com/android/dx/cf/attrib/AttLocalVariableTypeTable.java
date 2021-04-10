package com.android.dx.cf.attrib;

import com.android.dx.cf.code.*;

public final class AttLocalVariableTypeTable extends BaseLocalVariables
{
    public static final String ATTRIBUTE_NAME = "LocalVariableTypeTable";
    
    public AttLocalVariableTypeTable(final LocalVariableList list) {
        super("LocalVariableTypeTable", list);
    }
}
