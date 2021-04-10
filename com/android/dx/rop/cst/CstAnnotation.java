package com.android.dx.rop.cst;

import com.android.dx.rop.annotation.*;

public final class CstAnnotation extends Constant
{
    private final Annotation annotation;
    
    public CstAnnotation(final Annotation annotation) {
        if (annotation == null) {
            throw new NullPointerException("annotation == null");
        }
        annotation.throwIfMutable();
        this.annotation = annotation;
    }
    
    @Override
    protected int compareTo0(final Constant constant) {
        return this.annotation.compareTo(((CstAnnotation)constant).annotation);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof CstAnnotation && this.annotation.equals(((CstAnnotation)o).annotation);
    }
    
    public Annotation getAnnotation() {
        return this.annotation;
    }
    
    @Override
    public int hashCode() {
        return this.annotation.hashCode();
    }
    
    @Override
    public boolean isCategory2() {
        return false;
    }
    
    @Override
    public String toHuman() {
        return this.annotation.toString();
    }
    
    @Override
    public String toString() {
        return this.annotation.toString();
    }
    
    @Override
    public String typeName() {
        return "annotation";
    }
}
