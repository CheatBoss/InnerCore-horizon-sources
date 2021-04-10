package com.bumptech.glide.load.model;

import android.os.*;
import java.io.*;

public class ImageVideoWrapper
{
    private final ParcelFileDescriptor fileDescriptor;
    private final InputStream streamData;
    
    public ImageVideoWrapper(final InputStream streamData, final ParcelFileDescriptor fileDescriptor) {
        this.streamData = streamData;
        this.fileDescriptor = fileDescriptor;
    }
    
    public ParcelFileDescriptor getFileDescriptor() {
        return this.fileDescriptor;
    }
    
    public InputStream getStream() {
        return this.streamData;
    }
}
