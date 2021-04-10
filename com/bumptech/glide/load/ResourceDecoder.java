package com.bumptech.glide.load;

import com.bumptech.glide.load.engine.*;
import java.io.*;

public interface ResourceDecoder<T, Z>
{
    Resource<Z> decode(final T p0, final int p1, final int p2) throws IOException;
    
    String getId();
}
