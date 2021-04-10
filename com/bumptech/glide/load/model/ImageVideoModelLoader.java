package com.bumptech.glide.load.model;

import android.os.*;
import java.io.*;
import com.bumptech.glide.load.data.*;
import com.bumptech.glide.*;
import android.util.*;

public class ImageVideoModelLoader<A> implements ModelLoader<A, ImageVideoWrapper>
{
    private static final String TAG = "IVML";
    private final ModelLoader<A, ParcelFileDescriptor> fileDescriptorLoader;
    private final ModelLoader<A, InputStream> streamLoader;
    
    public ImageVideoModelLoader(final ModelLoader<A, InputStream> streamLoader, final ModelLoader<A, ParcelFileDescriptor> fileDescriptorLoader) {
        if (streamLoader == null && fileDescriptorLoader == null) {
            throw new NullPointerException("At least one of streamLoader and fileDescriptorLoader must be non null");
        }
        this.streamLoader = streamLoader;
        this.fileDescriptorLoader = fileDescriptorLoader;
    }
    
    @Override
    public DataFetcher<ImageVideoWrapper> getResourceFetcher(final A a, final int n, final int n2) {
        DataFetcher<InputStream> resourceFetcher = null;
        if (this.streamLoader != null) {
            resourceFetcher = this.streamLoader.getResourceFetcher(a, n, n2);
        }
        DataFetcher<ParcelFileDescriptor> resourceFetcher2 = null;
        if (this.fileDescriptorLoader != null) {
            resourceFetcher2 = this.fileDescriptorLoader.getResourceFetcher(a, n, n2);
        }
        if (resourceFetcher == null && resourceFetcher2 == null) {
            return null;
        }
        return new ImageVideoFetcher(resourceFetcher, resourceFetcher2);
    }
    
    static class ImageVideoFetcher implements DataFetcher<ImageVideoWrapper>
    {
        private final DataFetcher<ParcelFileDescriptor> fileDescriptorFetcher;
        private final DataFetcher<InputStream> streamFetcher;
        
        public ImageVideoFetcher(final DataFetcher<InputStream> streamFetcher, final DataFetcher<ParcelFileDescriptor> fileDescriptorFetcher) {
            this.streamFetcher = streamFetcher;
            this.fileDescriptorFetcher = fileDescriptorFetcher;
        }
        
        @Override
        public void cancel() {
            if (this.streamFetcher != null) {
                this.streamFetcher.cancel();
            }
            if (this.fileDescriptorFetcher != null) {
                this.fileDescriptorFetcher.cancel();
            }
        }
        
        @Override
        public void cleanup() {
            if (this.streamFetcher != null) {
                this.streamFetcher.cleanup();
            }
            if (this.fileDescriptorFetcher != null) {
                this.fileDescriptorFetcher.cleanup();
            }
        }
        
        @Override
        public String getId() {
            if (this.streamFetcher != null) {
                return this.streamFetcher.getId();
            }
            return this.fileDescriptorFetcher.getId();
        }
        
        @Override
        public ImageVideoWrapper loadData(final Priority priority) throws Exception {
            InputStream inputStream2;
            final InputStream inputStream = inputStream2 = null;
            if (this.streamFetcher != null) {
                try {
                    inputStream2 = this.streamFetcher.loadData(priority);
                }
                catch (Exception ex) {
                    if (Log.isLoggable("IVML", 2)) {
                        Log.v("IVML", "Exception fetching input stream, trying ParcelFileDescriptor", (Throwable)ex);
                    }
                    inputStream2 = inputStream;
                    if (this.fileDescriptorFetcher == null) {
                        throw ex;
                    }
                }
            }
            ParcelFileDescriptor parcelFileDescriptor = null;
            if (this.fileDescriptorFetcher != null) {
                try {
                    parcelFileDescriptor = this.fileDescriptorFetcher.loadData(priority);
                }
                catch (Exception ex2) {
                    if (Log.isLoggable("IVML", 2)) {
                        Log.v("IVML", "Exception fetching ParcelFileDescriptor", (Throwable)ex2);
                    }
                    parcelFileDescriptor = parcelFileDescriptor;
                    if (inputStream2 == null) {
                        throw ex2;
                    }
                }
            }
            return new ImageVideoWrapper(inputStream2, parcelFileDescriptor);
        }
    }
}
