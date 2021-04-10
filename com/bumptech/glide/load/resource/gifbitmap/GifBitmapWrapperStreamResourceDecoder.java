package com.bumptech.glide.load.resource.gifbitmap;

import com.bumptech.glide.load.*;
import com.bumptech.glide.load.model.*;
import com.bumptech.glide.load.engine.*;
import android.os.*;
import java.io.*;

public class GifBitmapWrapperStreamResourceDecoder implements ResourceDecoder<InputStream, GifBitmapWrapper>
{
    private final ResourceDecoder<ImageVideoWrapper, GifBitmapWrapper> gifBitmapDecoder;
    
    public GifBitmapWrapperStreamResourceDecoder(final ResourceDecoder<ImageVideoWrapper, GifBitmapWrapper> gifBitmapDecoder) {
        this.gifBitmapDecoder = gifBitmapDecoder;
    }
    
    @Override
    public Resource<GifBitmapWrapper> decode(final InputStream inputStream, final int n, final int n2) throws IOException {
        return this.gifBitmapDecoder.decode(new ImageVideoWrapper(inputStream, null), n, n2);
    }
    
    @Override
    public String getId() {
        return this.gifBitmapDecoder.getId();
    }
}
