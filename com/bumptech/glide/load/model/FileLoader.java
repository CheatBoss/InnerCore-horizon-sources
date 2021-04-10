package com.bumptech.glide.load.model;

import java.io.*;
import android.net.*;
import com.bumptech.glide.load.data.*;

public class FileLoader<T> implements ModelLoader<File, T>
{
    private final ModelLoader<Uri, T> uriLoader;
    
    public FileLoader(final ModelLoader<Uri, T> uriLoader) {
        this.uriLoader = uriLoader;
    }
    
    @Override
    public DataFetcher<T> getResourceFetcher(final File file, final int n, final int n2) {
        return this.uriLoader.getResourceFetcher(Uri.fromFile(file), n, n2);
    }
}
