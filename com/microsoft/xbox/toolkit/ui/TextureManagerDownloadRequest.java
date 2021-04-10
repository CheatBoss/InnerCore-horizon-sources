package com.microsoft.xbox.toolkit.ui;

import java.util.concurrent.atomic.*;
import java.io.*;

public class TextureManagerDownloadRequest implements Comparable<TextureManagerDownloadRequest>
{
    private static AtomicInteger nextIndex;
    public int index;
    public TextureManagerScaledNetworkBitmapRequest key;
    public InputStream stream;
    
    static {
        TextureManagerDownloadRequest.nextIndex = new AtomicInteger(0);
    }
    
    public TextureManagerDownloadRequest(final TextureManagerScaledNetworkBitmapRequest key) {
        this.key = key;
        this.index = TextureManagerDownloadRequest.nextIndex.incrementAndGet();
    }
    
    @Override
    public int compareTo(final TextureManagerDownloadRequest textureManagerDownloadRequest) {
        return this.index - textureManagerDownloadRequest.index;
    }
}
