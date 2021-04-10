package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.load.model.*;
import com.bumptech.glide.gifdecoder.*;
import com.bumptech.glide.load.data.*;
import com.bumptech.glide.*;

class GifFrameModelLoader implements ModelLoader<GifDecoder, GifDecoder>
{
    @Override
    public DataFetcher<GifDecoder> getResourceFetcher(final GifDecoder gifDecoder, final int n, final int n2) {
        return new GifFrameDataFetcher(gifDecoder);
    }
    
    private static class GifFrameDataFetcher implements DataFetcher<GifDecoder>
    {
        private final GifDecoder decoder;
        
        public GifFrameDataFetcher(final GifDecoder decoder) {
            this.decoder = decoder;
        }
        
        @Override
        public void cancel() {
        }
        
        @Override
        public void cleanup() {
        }
        
        @Override
        public String getId() {
            return String.valueOf(this.decoder.getCurrentFrameIndex());
        }
        
        @Override
        public GifDecoder loadData(final Priority priority) {
            return this.decoder;
        }
    }
}
