package com.bumptech.glide.load.resource.file;

import com.bumptech.glide.provider.*;
import com.bumptech.glide.load.model.*;
import com.bumptech.glide.load.*;
import com.bumptech.glide.load.resource.*;
import com.bumptech.glide.load.engine.*;
import java.io.*;

public class StreamFileDataLoadProvider implements DataLoadProvider<InputStream, File>
{
    private static final ErrorSourceDecoder ERROR_DECODER;
    private final ResourceDecoder<File, File> cacheDecoder;
    private final Encoder<InputStream> encoder;
    
    static {
        ERROR_DECODER = new ErrorSourceDecoder();
    }
    
    public StreamFileDataLoadProvider() {
        this.cacheDecoder = new FileDecoder();
        this.encoder = new StreamEncoder();
    }
    
    @Override
    public ResourceDecoder<File, File> getCacheDecoder() {
        return this.cacheDecoder;
    }
    
    @Override
    public ResourceEncoder<File> getEncoder() {
        return (ResourceEncoder<File>)NullResourceEncoder.get();
    }
    
    @Override
    public ResourceDecoder<InputStream, File> getSourceDecoder() {
        return StreamFileDataLoadProvider.ERROR_DECODER;
    }
    
    @Override
    public Encoder<InputStream> getSourceEncoder() {
        return this.encoder;
    }
    
    private static class ErrorSourceDecoder implements ResourceDecoder<InputStream, File>
    {
        @Override
        public Resource<File> decode(final InputStream inputStream, final int n, final int n2) {
            throw new Error("You cannot decode a File from an InputStream by default, try either #diskCacheStratey(DiskCacheStrategy.SOURCE) to avoid this call or #decoder(ResourceDecoder) to replace this Decoder");
        }
        
        @Override
        public String getId() {
            return "";
        }
    }
}
