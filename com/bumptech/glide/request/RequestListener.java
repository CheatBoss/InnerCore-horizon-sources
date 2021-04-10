package com.bumptech.glide.request;

import com.bumptech.glide.request.target.*;

public interface RequestListener<T, R>
{
    boolean onException(final Exception p0, final T p1, final Target<R> p2, final boolean p3);
    
    boolean onResourceReady(final R p0, final T p1, final Target<R> p2, final boolean p3, final boolean p4);
}
