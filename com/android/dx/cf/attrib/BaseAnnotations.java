package com.android.dx.cf.attrib;

import com.android.dx.rop.annotation.*;
import com.android.dx.util.*;

public abstract class BaseAnnotations extends BaseAttribute
{
    private final Annotations annotations;
    private final int byteLength;
    
    public BaseAnnotations(final String s, final Annotations annotations, final int byteLength) {
        super(s);
        try {
            if (annotations.isMutable()) {
                throw new MutabilityException("annotations.isMutable()");
            }
            this.annotations = annotations;
            this.byteLength = byteLength;
        }
        catch (NullPointerException ex) {
            throw new NullPointerException("annotations == null");
        }
    }
    
    @Override
    public final int byteLength() {
        return this.byteLength + 6;
    }
    
    public final Annotations getAnnotations() {
        return this.annotations;
    }
}
