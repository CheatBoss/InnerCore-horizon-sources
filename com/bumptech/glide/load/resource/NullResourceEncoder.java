package com.bumptech.glide.load.resource;

import com.bumptech.glide.load.*;
import com.bumptech.glide.load.engine.*;
import java.io.*;

public class NullResourceEncoder<T> implements ResourceEncoder<T>
{
    private static final NullResourceEncoder<?> NULL_ENCODER;
    
    static {
        NULL_ENCODER = new NullResourceEncoder<Object>();
    }
    
    public static <T> NullResourceEncoder<T> get() {
        return (NullResourceEncoder<T>)NullResourceEncoder.NULL_ENCODER;
    }
    
    @Override
    public boolean encode(final Resource<T> resource, final OutputStream outputStream) {
        return false;
    }
    
    @Override
    public String getId() {
        return "";
    }
}
