package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.resource.gif.*;
import com.bumptech.glide.load.engine.*;
import com.bumptech.glide.load.resource.bytes.*;

public class GifDrawableBytesTranscoder implements ResourceTranscoder<GifDrawable, byte[]>
{
    @Override
    public String getId() {
        return "GifDrawableBytesTranscoder.com.bumptech.glide.load.resource.transcode";
    }
    
    @Override
    public Resource<byte[]> transcode(final Resource<GifDrawable> resource) {
        return new BytesResource(resource.get().getData());
    }
}
