package com.google.android.gms.common.internal;

import android.content.*;
import android.view.*;
import com.google.android.gms.dynamic.*;
import com.google.android.gms.common.api.*;
import android.os.*;

public final class SignInButtonCreator extends RemoteCreator<ISignInButtonCreator>
{
    private static final SignInButtonCreator zzuz;
    
    static {
        zzuz = new SignInButtonCreator();
    }
    
    private SignInButtonCreator() {
        super("com.google.android.gms.common.ui.SignInButtonCreatorImpl");
    }
    
    public static View createView(final Context context, final int n, final int n2) throws RemoteCreatorException {
        return SignInButtonCreator.zzuz.zza(context, n, n2);
    }
    
    private final View zza(final Context context, final int n, final int n2) throws RemoteCreatorException {
        try {
            return ObjectWrapper.unwrap(this.getRemoteCreatorInstance(context).newSignInButtonFromConfig(ObjectWrapper.wrap(context), new SignInButtonConfig(n, n2, null)));
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder(64);
            sb.append("Could not get button with size ");
            sb.append(n);
            sb.append(" and color ");
            sb.append(n2);
            throw new RemoteCreatorException(sb.toString(), ex);
        }
    }
    
    public final ISignInButtonCreator getRemoteCreator(final IBinder binder) {
        return ISignInButtonCreator.Stub.asInterface(binder);
    }
}
