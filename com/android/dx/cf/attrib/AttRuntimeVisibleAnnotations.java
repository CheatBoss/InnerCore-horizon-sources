package com.android.dx.cf.attrib;

import com.android.dx.rop.annotation.*;

public final class AttRuntimeVisibleAnnotations extends BaseAnnotations
{
    public static final String ATTRIBUTE_NAME = "RuntimeVisibleAnnotations";
    
    public AttRuntimeVisibleAnnotations(final Annotations annotations, final int n) {
        super("RuntimeVisibleAnnotations", annotations, n);
    }
}
