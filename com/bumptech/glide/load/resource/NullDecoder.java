package com.bumptech.glide.load.resource;

import com.bumptech.glide.load.*;
import com.bumptech.glide.load.engine.*;

public class NullDecoder<T, Z> implements ResourceDecoder<T, Z>
{
    private static final NullDecoder<?, ?> NULL_DECODER;
    
    static {
        NULL_DECODER = new NullDecoder<Object, Object>();
    }
    
    public static <T, Z> NullDecoder<T, Z> get() {
        return (NullDecoder<T, Z>)NullDecoder.NULL_DECODER;
    }
    
    @Override
    public Resource<Z> decode(final T t, final int n, final int n2) {
        return null;
    }
    
    @Override
    public String getId() {
        return "";
    }
}
