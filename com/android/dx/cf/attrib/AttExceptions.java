package com.android.dx.cf.attrib;

import com.android.dx.rop.type.*;
import com.android.dx.util.*;

public final class AttExceptions extends BaseAttribute
{
    public static final String ATTRIBUTE_NAME = "Exceptions";
    private final TypeList exceptions;
    
    public AttExceptions(final TypeList exceptions) {
        super("Exceptions");
        try {
            if (exceptions.isMutable()) {
                throw new MutabilityException("exceptions.isMutable()");
            }
            this.exceptions = exceptions;
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("exceptions == null");
        }
    }
    
    @Override
    public int byteLength() {
        return this.exceptions.size() * 2 + 8;
    }
    
    public TypeList getExceptions() {
        return this.exceptions;
    }
}
