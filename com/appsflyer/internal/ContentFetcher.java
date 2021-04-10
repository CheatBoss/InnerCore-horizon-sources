package com.appsflyer.internal;

import android.content.*;
import java.util.concurrent.*;
import com.appsflyer.*;
import java.security.cert.*;
import java.security.*;
import android.content.pm.*;

public abstract class ContentFetcher<T>
{
    public final String authority;
    public final Context context;
    private final long \u0131;
    private FutureTask<T> \u01c3;
    private final String \u03b9;
    
    public ContentFetcher(final Context context, final String authority, final String \u03b9, final long \u0131) {
        this.\u01c3 = new FutureTask<T>(new Callable<T>() {
            @Override
            public final T call() {
                if (ContentFetcher.this.valid()) {
                    return ContentFetcher.this.query();
                }
                return null;
            }
        });
        this.context = context;
        this.authority = authority;
        this.\u03b9 = \u03b9;
        this.\u0131 = \u0131;
    }
    
    public T get() {
        try {
            return this.\u01c3.get(this.\u0131, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException | ExecutionException | TimeoutException ex2) {
            final Exception ex;
            this.onError(ex);
            return null;
        }
    }
    
    protected void onError(final Exception ex) {
        AFLogger.afErrorLog(this.getClass().getSimpleName(), ex);
    }
    
    protected abstract T query();
    
    public void start() {
        new Thread(this.\u01c3).start();
    }
    
    public boolean valid() {
        final boolean b = false;
        try {
            final ProviderInfo resolveContentProvider = this.context.getPackageManager().resolveContentProvider(this.authority, 128);
            boolean b2 = b;
            if (resolveContentProvider != null) {
                final boolean equalsIgnoreCase = AndroidUtils.signature(this.context.getPackageManager(), ((PackageItemInfo)resolveContentProvider).packageName).equalsIgnoreCase(this.\u03b9);
                b2 = b;
                if (equalsIgnoreCase) {
                    b2 = true;
                }
            }
            return b2;
        }
        catch (PackageManager$NameNotFoundException | CertificateException | NoSuchAlgorithmException ex2) {
            final Exception ex;
            this.onError(ex);
            return false;
        }
    }
}
