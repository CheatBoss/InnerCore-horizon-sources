package com.android.installreferrer.api;

import android.content.*;
import android.os.*;
import java.lang.annotation.*;

public abstract class InstallReferrerClient
{
    public static Builder newBuilder(final Context context) {
        return new Builder(context);
    }
    
    public abstract void endConnection();
    
    public abstract ReferrerDetails getInstallReferrer() throws RemoteException;
    
    public abstract boolean isReady();
    
    public abstract void startConnection(final InstallReferrerStateListener p0);
    
    public static final class Builder
    {
        private final Context mContext;
        
        private Builder(final Context mContext) {
            this.mContext = mContext;
        }
        
        public InstallReferrerClient build() {
            if (this.mContext != null) {
                return new InstallReferrerClientImpl(this.mContext);
            }
            throw new IllegalArgumentException("Please provide a valid Context.");
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface InstallReferrerResponse {
        public static final int DEVELOPER_ERROR = 3;
        public static final int FEATURE_NOT_SUPPORTED = 2;
        public static final int OK = 0;
        public static final int SERVICE_DISCONNECTED = -1;
        public static final int SERVICE_UNAVAILABLE = 1;
    }
}
