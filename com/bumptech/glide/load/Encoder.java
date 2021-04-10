package com.bumptech.glide.load;

import java.io.*;

public interface Encoder<T>
{
    boolean encode(final T p0, final OutputStream p1);
    
    String getId();
}
