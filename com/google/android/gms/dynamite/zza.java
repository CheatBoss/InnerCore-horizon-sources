package com.google.android.gms.dynamite;

import android.content.*;

final class zza implements IVersions
{
    @Override
    public final int getLocalVersion(final Context context, final String s) {
        return DynamiteModule.getLocalVersion(context, s);
    }
    
    @Override
    public final int getRemoteVersion(final Context context, final String s, final boolean b) throws LoadingException {
        return DynamiteModule.getRemoteVersion(context, s, b);
    }
}
