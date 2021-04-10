package com.bumptech.glide.load.model;

import com.bumptech.glide.load.data.*;

public interface ModelLoader<T, Y>
{
    DataFetcher<Y> getResourceFetcher(final T p0, final int p1, final int p2);
}
