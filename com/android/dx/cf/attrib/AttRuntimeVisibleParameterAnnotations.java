package com.android.dx.cf.attrib;

import com.android.dx.rop.annotation.*;

public final class AttRuntimeVisibleParameterAnnotations extends BaseParameterAnnotations
{
    public static final String ATTRIBUTE_NAME = "RuntimeVisibleParameterAnnotations";
    
    public AttRuntimeVisibleParameterAnnotations(final AnnotationsList list, final int n) {
        super("RuntimeVisibleParameterAnnotations", list, n);
    }
}
