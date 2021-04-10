package com.bumptech.glide.load.model;

import com.bumptech.glide.load.*;
import android.os.*;
import java.io.*;

public class ImageVideoWrapperEncoder implements Encoder<ImageVideoWrapper>
{
    private final Encoder<ParcelFileDescriptor> fileDescriptorEncoder;
    private String id;
    private final Encoder<InputStream> streamEncoder;
    
    public ImageVideoWrapperEncoder(final Encoder<InputStream> streamEncoder, final Encoder<ParcelFileDescriptor> fileDescriptorEncoder) {
        this.streamEncoder = streamEncoder;
        this.fileDescriptorEncoder = fileDescriptorEncoder;
    }
    
    @Override
    public boolean encode(final ImageVideoWrapper imageVideoWrapper, final OutputStream outputStream) {
        if (imageVideoWrapper.getStream() != null) {
            return this.streamEncoder.encode(imageVideoWrapper.getStream(), outputStream);
        }
        return this.fileDescriptorEncoder.encode(imageVideoWrapper.getFileDescriptor(), outputStream);
    }
    
    @Override
    public String getId() {
        if (this.id == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.streamEncoder.getId());
            sb.append(this.fileDescriptorEncoder.getId());
            this.id = sb.toString();
        }
        return this.id;
    }
}
