package com.bumptech.glide.load.data;

import android.net.*;
import android.content.*;
import java.io.*;

public class StreamLocalUriFetcher extends LocalUriFetcher<InputStream>
{
    public StreamLocalUriFetcher(final Context context, final Uri uri) {
        super(context, uri);
    }
    
    @Override
    protected void close(final InputStream inputStream) throws IOException {
        inputStream.close();
    }
    
    @Override
    protected InputStream loadResource(final Uri uri, final ContentResolver contentResolver) throws FileNotFoundException {
        return contentResolver.openInputStream(uri);
    }
}
