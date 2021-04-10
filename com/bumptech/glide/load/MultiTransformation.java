package com.bumptech.glide.load;

import java.util.*;
import com.bumptech.glide.load.engine.*;

public class MultiTransformation<T> implements Transformation<T>
{
    private String id;
    private final Collection<? extends Transformation<T>> transformations;
    
    public MultiTransformation(final Collection<? extends Transformation<T>> transformations) {
        if (transformations.size() < 1) {
            throw new IllegalArgumentException("MultiTransformation must contain at least one Transformation");
        }
        this.transformations = transformations;
    }
    
    @SafeVarargs
    public MultiTransformation(final Transformation<T>... array) {
        if (array.length < 1) {
            throw new IllegalArgumentException("MultiTransformation must contain at least one Transformation");
        }
        this.transformations = Arrays.asList(array);
    }
    
    @Override
    public String getId() {
        if (this.id == null) {
            final StringBuilder sb = new StringBuilder();
            final Iterator<? extends Transformation<T>> iterator = this.transformations.iterator();
            while (iterator.hasNext()) {
                sb.append(((Transformation)iterator.next()).getId());
            }
            this.id = sb.toString();
        }
        return this.id;
    }
    
    @Override
    public Resource<T> transform(final Resource<T> resource, final int n, final int n2) {
        Resource<T> resource2 = resource;
        final Iterator<? extends Transformation<T>> iterator = this.transformations.iterator();
        while (iterator.hasNext()) {
            final Resource<T> transform = ((Transformation<T>)iterator.next()).transform(resource2, n, n2);
            if (resource2 != null && !resource2.equals(resource) && !resource2.equals(transform)) {
                resource2.recycle();
            }
            resource2 = transform;
        }
        return resource2;
    }
}
