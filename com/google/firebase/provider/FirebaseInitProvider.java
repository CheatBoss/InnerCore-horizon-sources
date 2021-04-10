package com.google.firebase.provider;

import android.content.pm.*;
import com.google.android.gms.common.internal.*;
import android.net.*;
import android.content.*;
import com.google.firebase.*;
import android.util.*;
import android.database.*;

public class FirebaseInitProvider extends ContentProvider
{
    public void attachInfo(final Context context, final ProviderInfo providerInfo) {
        Preconditions.checkNotNull(providerInfo, "FirebaseInitProvider ProviderInfo cannot be null.");
        if (!"com.google.firebase.firebaseinitprovider".equals(providerInfo.authority)) {
            super.attachInfo(context, providerInfo);
            return;
        }
        throw new IllegalStateException("Incorrect provider authority in manifest. Most likely due to a missing applicationId variable in application's build.gradle.");
    }
    
    public int delete(final Uri uri, final String s, final String[] array) {
        return 0;
    }
    
    public String getType(final Uri uri) {
        return null;
    }
    
    public Uri insert(final Uri uri, final ContentValues contentValues) {
        return null;
    }
    
    public boolean onCreate() {
        String s;
        if (FirebaseApp.initializeApp(this.getContext()) == null) {
            s = "FirebaseApp initialization unsuccessful";
        }
        else {
            s = "FirebaseApp initialization successful";
        }
        Log.i("FirebaseInitProvider", s);
        return false;
    }
    
    public Cursor query(final Uri uri, final String[] array, final String s, final String[] array2, final String s2) {
        return null;
    }
    
    public int update(final Uri uri, final ContentValues contentValues, final String s, final String[] array) {
        return 0;
    }
}
