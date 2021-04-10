package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.*;
import com.bumptech.glide.load.model.*;
import android.graphics.*;
import android.os.*;
import com.bumptech.glide.load.engine.*;
import android.util.*;
import java.io.*;

public class ImageVideoBitmapDecoder implements ResourceDecoder<ImageVideoWrapper, Bitmap>
{
    private static final String TAG = "ImageVideoDecoder";
    private final ResourceDecoder<ParcelFileDescriptor, Bitmap> fileDescriptorDecoder;
    private final ResourceDecoder<InputStream, Bitmap> streamDecoder;
    
    public ImageVideoBitmapDecoder(final ResourceDecoder<InputStream, Bitmap> streamDecoder, final ResourceDecoder<ParcelFileDescriptor, Bitmap> fileDescriptorDecoder) {
        this.streamDecoder = streamDecoder;
        this.fileDescriptorDecoder = fileDescriptorDecoder;
    }
    
    @Override
    public Resource<Bitmap> decode(final ImageVideoWrapper imageVideoWrapper, final int n, final int n2) throws IOException {
        final Resource<Bitmap> resource = null;
        final InputStream stream = imageVideoWrapper.getStream();
        Resource<Bitmap> decode = resource;
        if (stream != null) {
            try {
                decode = this.streamDecoder.decode(stream, n, n2);
            }
            catch (IOException ex) {
                decode = resource;
                if (Log.isLoggable("ImageVideoDecoder", 2)) {
                    Log.v("ImageVideoDecoder", "Failed to load image from stream, trying FileDescriptor", (Throwable)ex);
                    decode = resource;
                }
            }
        }
        Resource<Bitmap> decode2;
        if ((decode2 = decode) == null) {
            final ParcelFileDescriptor fileDescriptor = imageVideoWrapper.getFileDescriptor();
            decode2 = decode;
            if (fileDescriptor != null) {
                decode2 = this.fileDescriptorDecoder.decode(fileDescriptor, n, n2);
            }
        }
        return decode2;
    }
    
    @Override
    public String getId() {
        return "ImageVideoBitmapDecoder.com.bumptech.glide.load.resource.bitmap";
    }
}
