package com.bumptech.glide.load;

import com.bumptech.glide.load.engine.*;

public interface Transformation<T>
{
    String getId();
    
    Resource<T> transform(final Resource<T> p0, final int p1, final int p2);
}
