package com.android.dx.rop.annotation;

import com.android.dx.util.*;

public final class AnnotationsList extends FixedSizeList
{
    public static final AnnotationsList EMPTY;
    
    static {
        EMPTY = new AnnotationsList(0);
    }
    
    public AnnotationsList(final int n) {
        super(n);
    }
    
    public static AnnotationsList combine(final AnnotationsList list, final AnnotationsList list2) {
        final int size = list.size();
        if (size != list2.size()) {
            throw new IllegalArgumentException("list1.size() != list2.size()");
        }
        final AnnotationsList list3 = new AnnotationsList(size);
        for (int i = 0; i < size; ++i) {
            list3.set(i, Annotations.combine(list.get(i), list2.get(i)));
        }
        list3.setImmutable();
        return list3;
    }
    
    public Annotations get(final int n) {
        return (Annotations)this.get0(n);
    }
    
    public void set(final int n, final Annotations annotations) {
        annotations.throwIfMutable();
        this.set0(n, annotations);
    }
}
