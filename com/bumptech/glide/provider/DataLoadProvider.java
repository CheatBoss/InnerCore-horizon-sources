package com.bumptech.glide.provider;

import java.io.*;
import com.bumptech.glide.load.*;

public interface DataLoadProvider<T, Z>
{
    ResourceDecoder<File, Z> getCacheDecoder();
    
    ResourceEncoder<Z> getEncoder();
    
    ResourceDecoder<T, Z> getSourceDecoder();
    
    Encoder<T> getSourceEncoder();
}
