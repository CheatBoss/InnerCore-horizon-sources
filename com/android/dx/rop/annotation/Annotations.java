package com.android.dx.rop.annotation;

import com.android.dx.util.*;
import com.android.dx.rop.cst.*;
import java.util.*;

public final class Annotations extends MutabilityControl implements Comparable<Annotations>
{
    public static final Annotations EMPTY;
    private final TreeMap<CstType, Annotation> annotations;
    
    static {
        (EMPTY = new Annotations()).setImmutable();
    }
    
    public Annotations() {
        this.annotations = new TreeMap<CstType, Annotation>();
    }
    
    public static Annotations combine(final Annotations annotations, final Annotation annotation) {
        final Annotations annotations2 = new Annotations();
        annotations2.addAll(annotations);
        annotations2.add(annotation);
        annotations2.setImmutable();
        return annotations2;
    }
    
    public static Annotations combine(final Annotations annotations, final Annotations annotations2) {
        final Annotations annotations3 = new Annotations();
        annotations3.addAll(annotations);
        annotations3.addAll(annotations2);
        annotations3.setImmutable();
        return annotations3;
    }
    
    public void add(final Annotation annotation) {
        this.throwIfImmutable();
        if (annotation == null) {
            throw new NullPointerException("annotation == null");
        }
        final CstType type = annotation.getType();
        if (this.annotations.containsKey(type)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("duplicate type: ");
            sb.append(type.toHuman());
            throw new IllegalArgumentException(sb.toString());
        }
        this.annotations.put(type, annotation);
    }
    
    public void addAll(final Annotations annotations) {
        this.throwIfImmutable();
        if (annotations == null) {
            throw new NullPointerException("toAdd == null");
        }
        final Iterator<Annotation> iterator = annotations.annotations.values().iterator();
        while (iterator.hasNext()) {
            this.add(iterator.next());
        }
    }
    
    @Override
    public int compareTo(final Annotations annotations) {
        final Iterator<Annotation> iterator = this.annotations.values().iterator();
        final Iterator<Annotation> iterator2 = annotations.annotations.values().iterator();
        while (iterator.hasNext() && iterator2.hasNext()) {
            final int compareTo = iterator.next().compareTo(iterator2.next());
            if (compareTo != 0) {
                return compareTo;
            }
        }
        if (iterator.hasNext()) {
            return 1;
        }
        if (iterator2.hasNext()) {
            return -1;
        }
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof Annotations && this.annotations.equals(((Annotations)o).annotations);
    }
    
    public Collection<Annotation> getAnnotations() {
        return Collections.unmodifiableCollection((Collection<? extends Annotation>)this.annotations.values());
    }
    
    @Override
    public int hashCode() {
        return this.annotations.hashCode();
    }
    
    public int size() {
        return this.annotations.size();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        int n = 1;
        sb.append("annotations{");
        for (final Annotation annotation : this.annotations.values()) {
            if (n != 0) {
                n = 0;
            }
            else {
                sb.append(", ");
            }
            sb.append(annotation.toHuman());
        }
        sb.append("}");
        return sb.toString();
    }
}
