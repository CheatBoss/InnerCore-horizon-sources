package com.android.dx.cf.attrib;

import com.android.dx.rop.annotation.*;
import com.android.dx.util.*;

public abstract class BaseParameterAnnotations extends BaseAttribute
{
    private final int byteLength;
    private final AnnotationsList parameterAnnotations;
    
    public BaseParameterAnnotations(final String s, final AnnotationsList parameterAnnotations, final int byteLength) {
        super(s);
        try {
            if (parameterAnnotations.isMutable()) {
                throw new MutabilityException("parameterAnnotations.isMutable()");
            }
            this.parameterAnnotations = parameterAnnotations;
            this.byteLength = byteLength;
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("parameterAnnotations == null");
        }
    }
    
    @Override
    public final int byteLength() {
        return this.byteLength + 6;
    }
    
    public final AnnotationsList getParameterAnnotations() {
        return this.parameterAnnotations;
    }
}
