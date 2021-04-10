package com.appboy.lrucache;

import com.appboy.*;
import android.util.*;
import android.graphics.*;
import bo.app.*;
import android.content.*;
import java.io.*;
import com.appboy.support.*;
import android.net.*;
import com.appboy.enums.*;
import android.widget.*;
import android.os.*;

public class AppboyLruImageLoader implements IAppboyImageLoader
{
    private static final String a;
    private LruCache<String, Bitmap> b;
    private ba c;
    private final Object d;
    private boolean e;
    private boolean f;
    
    static {
        a = AppboyLogger.getAppboyLogTag(AppboyLruImageLoader.class);
    }
    
    public AppboyLruImageLoader(final Context context) {
        this.d = new Object();
        this.e = true;
        this.f = false;
        this.b = new LruCache<String, Bitmap>(AppboyImageUtils.getImageLoaderCacheSize()) {
            protected int a(final String s, final Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
        new a().execute((Object[])new File[] { a(context, "appboy.imageloader.lru.cache") });
    }
    
    static File a(final Context context, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(context.getCacheDir().getPath());
        sb.append(File.separator);
        sb.append(s);
        return new File(sb.toString());
    }
    
    private void b(final String s, final Bitmap bitmap) {
        this.b.put((Object)s, (Object)bitmap);
    }
    
    public static void deleteStoredData(final Context context) {
        final File file = new File(context.getCacheDir(), "appboy.imageloader.lru.cache");
        final String a = AppboyLruImageLoader.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Deleting lru image cache directory at: ");
        sb.append(file.getAbsolutePath());
        AppboyLogger.v(a, sb.toString());
        AppboyFileUtils.deleteFileOrDirectory(file);
    }
    
    Bitmap a(final Context context, final Uri uri, final AppboyViewBounds appboyViewBounds) {
        return AppboyImageUtils.getBitmap(context, uri, appboyViewBounds);
    }
    
    Bitmap a(final String s) {
        final Bitmap bitmap = (Bitmap)this.b.get((Object)s);
        if (bitmap != null) {
            final String a = AppboyLruImageLoader.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Got bitmap from mem cache for key ");
            sb.append(s);
            AppboyLogger.v(a, sb.toString());
            return bitmap;
        }
        final Bitmap c = this.c(s);
        if (c != null) {
            final String a2 = AppboyLruImageLoader.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Got bitmap from disk cache for key ");
            sb2.append(s);
            AppboyLogger.v(a2, sb2.toString());
            this.b(s, c);
            return c;
        }
        final String a3 = AppboyLruImageLoader.a;
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("No cache hit for bitmap: ");
        sb3.append(s);
        AppboyLogger.d(a3, sb3.toString());
        return null;
    }
    
    void a(final String s, final Bitmap bitmap) {
        if (this.b(s) == null) {
            final String a = AppboyLruImageLoader.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Adding bitmap to mem cache for key ");
            sb.append(s);
            AppboyLogger.d(a, sb.toString());
            this.b.put((Object)s, (Object)bitmap);
        }
        synchronized (this.d) {
            if (this.c != null && !this.c.b(s)) {
                final String a2 = AppboyLruImageLoader.a;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Adding bitmap to disk cache for key ");
                sb2.append(s);
                AppboyLogger.d(a2, sb2.toString());
                this.c.a(s, bitmap);
            }
        }
    }
    
    Bitmap b(final String s) {
        return (Bitmap)this.b.get((Object)s);
    }
    
    Bitmap c(final String s) {
        synchronized (this.d) {
            if (this.e) {
                return null;
            }
            if (this.c != null && this.c.b(s)) {
                return this.c.a(s);
            }
            return null;
        }
    }
    
    @Override
    public Bitmap getBitmapFromUrl(final Context context, final String s, final AppboyViewBounds appboyViewBounds) {
        final Bitmap a = this.a(s);
        if (a != null) {
            return a;
        }
        if (this.f) {
            AppboyLogger.d(AppboyLruImageLoader.a, "Cache is currently in offline mode. Not downloading bitmap.");
            return null;
        }
        final Bitmap a2 = this.a(context, Uri.parse(s), appboyViewBounds);
        if (a2 != null) {
            this.a(s, a2);
        }
        return a2;
    }
    
    @Override
    public void renderUrlIntoView(final Context context, final String s, final ImageView imageView, final AppboyViewBounds appboyViewBounds) {
        new b(context, imageView, appboyViewBounds, s).execute((Object[])new Void[0]);
    }
    
    @Override
    public void setOffline(final boolean f) {
        final String a = AppboyLruImageLoader.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Appboy image loader outbound network requests are now ");
        String s;
        if (f) {
            s = "disabled";
        }
        else {
            s = "enabled";
        }
        sb.append(s);
        AppboyLogger.i(a, sb.toString());
        this.f = f;
    }
    
    class a extends AsyncTask<File, Void, Void>
    {
        private a() {
        }
        
        protected Void a(final File... array) {
            final Object a = AppboyLruImageLoader.this.d;
            // monitorenter(a)
            final File file = array[0];
            try {
                try {
                    AppboyLogger.d(AppboyLruImageLoader.a, "Initializing disk cache");
                    AppboyLruImageLoader.this.c = new ba(file, 1, 1, 52428800L);
                }
                finally {
                    // monitorexit(a)
                    AppboyLruImageLoader.this.e = false;
                    AppboyLruImageLoader.this.d.notifyAll();
                    // monitorexit(a)
                    return null;
                }
            }
            catch (Exception ex) {}
        }
    }
    
    class b extends AsyncTask<Void, Void, Bitmap>
    {
        private final ImageView b;
        private final Context c;
        private final AppboyViewBounds d;
        private final String e;
        
        private b(final Context c, final ImageView b, final AppboyViewBounds d, final String e) {
            this.b = b;
            this.c = c;
            this.d = d;
            b.setTag((Object)(this.e = e));
        }
        
        protected Bitmap a(final Void... array) {
            return AppboyLruImageLoader.this.getBitmapFromUrl(this.c, this.e, this.d);
        }
        
        protected void a(final Bitmap imageBitmap) {
            final ImageView b = this.b;
            if (b != null && ((String)b.getTag()).equals(this.e)) {
                this.b.setImageBitmap(imageBitmap);
            }
        }
    }
}
