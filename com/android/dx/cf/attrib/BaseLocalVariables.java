package com.android.dx.cf.attrib;

import com.android.dx.cf.code.*;
import com.android.dx.util.*;

public abstract class BaseLocalVariables extends BaseAttribute
{
    private final LocalVariableList localVariables;
    
    public BaseLocalVariables(final String s, final LocalVariableList localVariables) {
        super(s);
        try {
            if (localVariables.isMutable()) {
                throw new MutabilityException("localVariables.isMutable()");
            }
            this.localVariables = localVariables;
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("localVariables == null");
        }
    }
    
    @Override
    public final int byteLength() {
        return this.localVariables.size() * 10 + 8;
    }
    
    public final LocalVariableList getLocalVariables() {
        return this.localVariables;
    }
}
