package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.engine.*;

public class UnitTranscoder<Z> implements ResourceTranscoder<Z, Z>
{
    private static final UnitTranscoder<?> UNIT_TRANSCODER;
    
    static {
        UNIT_TRANSCODER = new UnitTranscoder<Object>();
    }
    
    public static <Z> ResourceTranscoder<Z, Z> get() {
        return (ResourceTranscoder<Z, Z>)UnitTranscoder.UNIT_TRANSCODER;
    }
    
    @Override
    public String getId() {
        return "";
    }
    
    @Override
    public Resource<Z> transcode(final Resource<Z> resource) {
        return resource;
    }
}
