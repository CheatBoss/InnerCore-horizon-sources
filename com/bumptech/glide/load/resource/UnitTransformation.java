package com.bumptech.glide.load.resource;

import com.bumptech.glide.load.*;
import com.bumptech.glide.load.engine.*;

public class UnitTransformation<T> implements Transformation<T>
{
    private static final Transformation<?> TRANSFORMATION;
    
    static {
        TRANSFORMATION = new UnitTransformation<Object>();
    }
    
    public static <T> UnitTransformation<T> get() {
        return (UnitTransformation<T>)(UnitTransformation)UnitTransformation.TRANSFORMATION;
    }
    
    @Override
    public String getId() {
        return "";
    }
    
    @Override
    public Resource<T> transform(final Resource<T> resource, final int n, final int n2) {
        return resource;
    }
}
