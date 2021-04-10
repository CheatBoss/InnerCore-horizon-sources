package com.bumptech.glide.request;

import com.bumptech.glide.load.engine.*;

public interface ResourceCallback
{
    void onException(final Exception p0);
    
    void onResourceReady(final Resource<?> p0);
}
