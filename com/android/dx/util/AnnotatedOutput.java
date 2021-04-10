package com.android.dx.util;

public interface AnnotatedOutput extends Output
{
    void annotate(final int p0, final String p1);
    
    void annotate(final String p0);
    
    boolean annotates();
    
    void endAnnotation();
    
    int getAnnotationWidth();
    
    boolean isVerbose();
}
