package com.bumptech.glide.load.data;

import com.bumptech.glide.*;

public interface DataFetcher<T>
{
    void cancel();
    
    void cleanup();
    
    String getId();
    
    T loadData(final Priority p0) throws Exception;
}
