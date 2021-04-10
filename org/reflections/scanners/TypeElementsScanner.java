package org.reflections.scanners;

import com.google.common.base.*;
import java.util.*;
import com.google.common.collect.*;

public class TypeElementsScanner extends AbstractScanner
{
    private boolean includeAnnotations;
    private boolean includeFields;
    private boolean includeMethods;
    private boolean publicOnly;
    
    public TypeElementsScanner() {
        this.includeFields = true;
        this.includeMethods = true;
        this.includeAnnotations = true;
        this.publicOnly = true;
    }
    
    public TypeElementsScanner includeAnnotations() {
        return this.includeAnnotations(true);
    }
    
    public TypeElementsScanner includeAnnotations(final boolean includeAnnotations) {
        this.includeAnnotations = includeAnnotations;
        return this;
    }
    
    public TypeElementsScanner includeFields() {
        return this.includeFields(true);
    }
    
    public TypeElementsScanner includeFields(final boolean includeFields) {
        this.includeFields = includeFields;
        return this;
    }
    
    public TypeElementsScanner includeMethods() {
        return this.includeMethods(true);
    }
    
    public TypeElementsScanner includeMethods(final boolean includeMethods) {
        this.includeMethods = includeMethods;
        return this;
    }
    
    public TypeElementsScanner publicOnly() {
        return this.publicOnly(true);
    }
    
    public TypeElementsScanner publicOnly(final boolean publicOnly) {
        this.publicOnly = publicOnly;
        return this;
    }
    
    @Override
    public void scan(final Object o) {
        final String className = this.getMetadataAdapter().getClassName(o);
        if (!this.acceptResult(className)) {
            return;
        }
        this.getStore().put((Object)className, (Object)"");
        if (this.includeFields) {
            final Iterator<Object> iterator = this.getMetadataAdapter().getFields(o).iterator();
            while (iterator.hasNext()) {
                this.getStore().put((Object)className, (Object)this.getMetadataAdapter().getFieldName(iterator.next()));
            }
        }
        if (this.includeMethods) {
            for (final Object next : this.getMetadataAdapter().getMethods(o)) {
                if (!this.publicOnly || this.getMetadataAdapter().isPublic(next)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(this.getMetadataAdapter().getMethodName(next));
                    sb.append("(");
                    sb.append(Joiner.on(", ").join((Iterable)this.getMetadataAdapter().getParameterNames(next)));
                    sb.append(")");
                    this.getStore().put((Object)className, (Object)sb.toString());
                }
            }
        }
        if (this.includeAnnotations) {
            for (final String next2 : this.getMetadataAdapter().getClassAnnotationNames(o)) {
                final Multimap<String, String> store = this.getStore();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("@");
                sb2.append((Object)next2);
                store.put((Object)className, (Object)sb2.toString());
            }
        }
    }
}
