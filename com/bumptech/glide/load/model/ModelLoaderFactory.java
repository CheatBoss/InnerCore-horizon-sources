package com.bumptech.glide.load.model;

import android.content.*;

public interface ModelLoaderFactory<T, Y>
{
    ModelLoader<T, Y> build(final Context p0, final GenericLoaderFactory p1);
    
    void teardown();
}
