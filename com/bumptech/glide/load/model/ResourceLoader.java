package com.bumptech.glide.load.model;

import android.net.*;
import android.content.*;
import com.bumptech.glide.load.data.*;
import android.util.*;
import android.content.res.*;

public class ResourceLoader<T> implements ModelLoader<Integer, T>
{
    private static final String TAG = "ResourceLoader";
    private final Resources resources;
    private final ModelLoader<Uri, T> uriLoader;
    
    public ResourceLoader(final Context context, final ModelLoader<Uri, T> modelLoader) {
        this(context.getResources(), modelLoader);
    }
    
    public ResourceLoader(final Resources resources, final ModelLoader<Uri, T> uriLoader) {
        this.resources = resources;
        this.uriLoader = uriLoader;
    }
    
    @Override
    public DataFetcher<T> getResourceFetcher(final Integer n, final int n2, final int n3) {
        final Uri uri = null;
        Uri parse;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("android.resource://");
            sb.append(this.resources.getResourcePackageName((int)n));
            sb.append('/');
            sb.append(this.resources.getResourceTypeName((int)n));
            sb.append('/');
            sb.append(this.resources.getResourceEntryName((int)n));
            parse = Uri.parse(sb.toString());
        }
        catch (Resources$NotFoundException ex) {
            parse = uri;
            if (Log.isLoggable("ResourceLoader", 5)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Received invalid resource id: ");
                sb2.append(n);
                Log.w("ResourceLoader", sb2.toString(), (Throwable)ex);
                parse = uri;
            }
        }
        if (parse != null) {
            return this.uriLoader.getResourceFetcher(parse, n2, n3);
        }
        return null;
    }
}
