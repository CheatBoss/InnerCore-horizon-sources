package com.bumptech.glide.load.model;

import android.net.*;
import java.io.*;
import com.bumptech.glide.load.data.*;
import android.text.*;

public class StringLoader<T> implements ModelLoader<String, T>
{
    private final ModelLoader<Uri, T> uriLoader;
    
    public StringLoader(final ModelLoader<Uri, T> uriLoader) {
        this.uriLoader = uriLoader;
    }
    
    private static Uri toFileUri(final String s) {
        return Uri.fromFile(new File(s));
    }
    
    @Override
    public DataFetcher<T> getResourceFetcher(final String s, final int n, final int n2) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return null;
        }
        Uri uri;
        if (s.startsWith("/")) {
            uri = toFileUri(s);
        }
        else if ((uri = Uri.parse(s)).getScheme() == null) {
            uri = toFileUri(s);
        }
        return this.uriLoader.getResourceFetcher(uri, n, n2);
    }
}
