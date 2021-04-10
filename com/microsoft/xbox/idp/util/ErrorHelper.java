package com.microsoft.xbox.idp.util;

import android.os.*;
import android.net.*;
import android.util.*;
import com.microsoft.xbox.idp.ui.*;
import android.content.*;
import android.app.*;

public final class ErrorHelper implements Parcelable
{
    public static final Parcelable$Creator<ErrorHelper> CREATOR;
    public static final String KEY_RESULT_KEY = "KEY_RESULT_KEY";
    public static final int LOADER_NONE = -1;
    public static final int RC_ERROR_SCREEN = 63;
    private static final String TAG;
    private ActivityContext activityContext;
    public Bundle loaderArgs;
    public int loaderId;
    
    static {
        TAG = ErrorHelper.class.getSimpleName();
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<ErrorHelper>() {
            public ErrorHelper createFromParcel(final Parcel parcel) {
                return new ErrorHelper(parcel);
            }
            
            public ErrorHelper[] newArray(final int n) {
                return new ErrorHelper[n];
            }
        };
    }
    
    public ErrorHelper() {
        this.loaderId = -1;
        this.loaderArgs = null;
    }
    
    protected ErrorHelper(final Parcel parcel) {
        this.loaderId = parcel.readInt();
        this.loaderArgs = parcel.readBundle();
    }
    
    private boolean isConnected() {
        final NetworkInfo activeNetworkInfo = ((ConnectivityManager)this.activityContext.getActivity().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
    public void deleteLoader() {
        if (this.loaderId != -1) {
            this.activityContext.getLoaderManager().destroyLoader(this.loaderId);
            final Bundle loaderArgs = this.loaderArgs;
            Object value;
            if (loaderArgs == null) {
                value = null;
            }
            else {
                value = loaderArgs.get("KEY_RESULT_KEY");
            }
            if (value != null) {
                this.activityContext.getLoaderInfo(this.loaderId).clearCache(value);
            }
            this.loaderId = -1;
            this.loaderArgs = null;
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    public ActivityResult getActivityResult(final int n, final int n2, final Intent intent) {
        if (n != 63) {
            return null;
        }
        boolean b = true;
        if (n2 != 1) {
            b = false;
        }
        return new ActivityResult(b);
    }
    
    public <D> boolean initLoader(final int n, final Bundle bundle) {
        return this.initLoader(n, bundle, true);
    }
    
    public <D> boolean initLoader(final int loaderId, final Bundle loaderArgs, final boolean b) {
        Log.d(ErrorHelper.TAG, "initLoader");
        if (loaderId == -1) {
            Log.e(ErrorHelper.TAG, "LOADER_NONE");
            return false;
        }
        this.loaderId = loaderId;
        this.loaderArgs = loaderArgs;
        final LoaderManager loaderManager = this.activityContext.getLoaderManager();
        final LoaderInfo loaderInfo = this.activityContext.getLoaderInfo(this.loaderId);
        final Bundle loaderArgs2 = this.loaderArgs;
        Object value;
        if (loaderArgs2 == null) {
            value = null;
        }
        else {
            value = loaderArgs2.get("KEY_RESULT_KEY");
        }
        if ((value == null || !loaderInfo.hasCachedData(value)) && loaderManager.getLoader(loaderId) == null && b && !this.isConnected()) {
            Log.e(ErrorHelper.TAG, "Starting error activity: OFFLINE");
            this.startErrorActivity(ErrorActivity.ErrorScreen.OFFLINE);
            return false;
        }
        final String tag = ErrorHelper.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("initializing loader #");
        sb.append(this.loaderId);
        Log.d(tag, sb.toString());
        loaderManager.initLoader(loaderId, loaderArgs, (LoaderManager$LoaderCallbacks)loaderInfo.getLoaderCallbacks());
        return true;
    }
    
    public <D> boolean restartLoader() {
        if (this.loaderId != -1) {
            if (this.isConnected()) {
                final LoaderManager loaderManager = this.activityContext.getLoaderManager();
                final int loaderId = this.loaderId;
                loaderManager.restartLoader(loaderId, this.loaderArgs, (LoaderManager$LoaderCallbacks)this.activityContext.getLoaderInfo(loaderId).getLoaderCallbacks());
                return true;
            }
            this.startErrorActivity(ErrorActivity.ErrorScreen.OFFLINE);
        }
        return false;
    }
    
    public <D> boolean restartLoader(int loaderId, final Bundle loaderArgs) {
        if (loaderId != -1) {
            this.loaderId = loaderId;
            this.loaderArgs = loaderArgs;
            if (this.isConnected()) {
                final LoaderManager loaderManager = this.activityContext.getLoaderManager();
                loaderId = this.loaderId;
                loaderManager.restartLoader(loaderId, this.loaderArgs, (LoaderManager$LoaderCallbacks)this.activityContext.getLoaderInfo(loaderId).getLoaderCallbacks());
                return true;
            }
            this.startErrorActivity(ErrorActivity.ErrorScreen.OFFLINE);
        }
        return false;
    }
    
    public void setActivityContext(final ActivityContext activityContext) {
        this.activityContext = activityContext;
    }
    
    public void startErrorActivity(final ErrorActivity.ErrorScreen errorScreen) {
        final Intent intent = new Intent((Context)this.activityContext.getActivity(), (Class)ErrorActivity.class);
        intent.putExtra("ARG_ERROR_TYPE", errorScreen.type.getId());
        this.activityContext.startActivityForResult(intent, 63);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.loaderId);
        parcel.writeBundle(this.loaderArgs);
    }
    
    public interface ActivityContext
    {
        Activity getActivity();
        
        LoaderInfo getLoaderInfo(final int p0);
        
        LoaderManager getLoaderManager();
        
        void startActivityForResult(final Intent p0, final int p1);
    }
    
    public static class ActivityResult
    {
        private final boolean tryAgain;
        
        public ActivityResult(final boolean tryAgain) {
            this.tryAgain = tryAgain;
        }
        
        public boolean isTryAgain() {
            return this.tryAgain;
        }
    }
    
    public interface LoaderInfo
    {
        void clearCache(final Object p0);
        
        LoaderManager$LoaderCallbacks<?> getLoaderCallbacks();
        
        boolean hasCachedData(final Object p0);
    }
}
