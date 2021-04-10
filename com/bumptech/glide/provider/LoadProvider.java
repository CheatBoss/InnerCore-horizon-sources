package com.bumptech.glide.provider;

import com.bumptech.glide.load.model.*;
import com.bumptech.glide.load.resource.transcode.*;

public interface LoadProvider<A, T, Z, R> extends DataLoadProvider<T, Z>
{
    ModelLoader<A, T> getModelLoader();
    
    ResourceTranscoder<Z, R> getTranscoder();
}
