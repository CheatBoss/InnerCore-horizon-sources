package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.engine.*;

public interface ResourceTranscoder<Z, R>
{
    String getId();
    
    Resource<R> transcode(final Resource<Z> p0);
}
