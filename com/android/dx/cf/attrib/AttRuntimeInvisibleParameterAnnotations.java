package com.android.dx.cf.attrib;

import com.android.dx.rop.annotation.*;

public final class AttRuntimeInvisibleParameterAnnotations extends BaseParameterAnnotations
{
    public static final String ATTRIBUTE_NAME = "RuntimeInvisibleParameterAnnotations";
    
    public AttRuntimeInvisibleParameterAnnotations(final AnnotationsList list, final int n) {
        super("RuntimeInvisibleParameterAnnotations", list, n);
    }
}
