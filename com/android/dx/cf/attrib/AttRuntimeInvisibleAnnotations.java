package com.android.dx.cf.attrib;

import com.android.dx.rop.annotation.*;

public final class AttRuntimeInvisibleAnnotations extends BaseAnnotations
{
    public static final String ATTRIBUTE_NAME = "RuntimeInvisibleAnnotations";
    
    public AttRuntimeInvisibleAnnotations(final Annotations annotations, final int n) {
        super("RuntimeInvisibleAnnotations", annotations, n);
    }
}
