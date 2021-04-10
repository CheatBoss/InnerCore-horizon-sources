package com.android.dx.rop.annotation;

import com.android.dx.util.*;
import com.android.dx.rop.cst.*;
import java.util.*;

public final class Annotation extends MutabilityControl implements Comparable<Annotation>, ToHuman
{
    private final TreeMap<CstString, NameValuePair> elements;
    private final CstType type;
    private final AnnotationVisibility visibility;
    
    public Annotation(final CstType type, final AnnotationVisibility visibility) {
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        if (visibility == null) {
            throw new NullPointerException("visibility == null");
        }
        this.type = type;
        this.visibility = visibility;
        this.elements = new TreeMap<CstString, NameValuePair>();
    }
    
    public void add(final NameValuePair nameValuePair) {
        this.throwIfImmutable();
        if (nameValuePair == null) {
            throw new NullPointerException("pair == null");
        }
        final CstString name = nameValuePair.getName();
        if (this.elements.get(name) != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("name already added: ");
            sb.append(name);
            throw new IllegalArgumentException(sb.toString());
        }
        this.elements.put(name, nameValuePair);
    }
    
    @Override
    public int compareTo(final Annotation annotation) {
        final int compareTo = this.type.compareTo((Constant)annotation.type);
        if (compareTo != 0) {
            return compareTo;
        }
        final int compareTo2 = this.visibility.compareTo(annotation.visibility);
        if (compareTo2 != 0) {
            return compareTo2;
        }
        final Iterator<NameValuePair> iterator = this.elements.values().iterator();
        final Iterator<NameValuePair> iterator2 = annotation.elements.values().iterator();
        while (iterator.hasNext() && iterator2.hasNext()) {
            final int compareTo3 = iterator.next().compareTo((NameValuePair)iterator2.next());
            if (compareTo3 != 0) {
                return compareTo3;
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
        if (!(o instanceof Annotation)) {
            return false;
        }
        final Annotation annotation = (Annotation)o;
        return this.type.equals(annotation.type) && this.visibility == annotation.visibility && this.elements.equals(annotation.elements);
    }
    
    public Collection<NameValuePair> getNameValuePairs() {
        return Collections.unmodifiableCollection((Collection<? extends NameValuePair>)this.elements.values());
    }
    
    public CstType getType() {
        return this.type;
    }
    
    public AnnotationVisibility getVisibility() {
        return this.visibility;
    }
    
    @Override
    public int hashCode() {
        return (this.type.hashCode() * 31 + this.elements.hashCode()) * 31 + this.visibility.hashCode();
    }
    
    public void put(final NameValuePair nameValuePair) {
        this.throwIfImmutable();
        if (nameValuePair == null) {
            throw new NullPointerException("pair == null");
        }
        this.elements.put(nameValuePair.getName(), nameValuePair);
    }
    
    @Override
    public String toHuman() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.visibility.toHuman());
        sb.append("-annotation ");
        sb.append(this.type.toHuman());
        sb.append(" {");
        int n = 1;
        for (final NameValuePair nameValuePair : this.elements.values()) {
            if (n != 0) {
                n = 0;
            }
            else {
                sb.append(", ");
            }
            sb.append(nameValuePair.getName().toHuman());
            sb.append(": ");
            sb.append(nameValuePair.getValue().toHuman());
        }
        sb.append("}");
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.toHuman();
    }
}
