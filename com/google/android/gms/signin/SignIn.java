package com.google.android.gms.signin;

import com.google.android.gms.signin.internal.*;
import com.google.android.gms.common.api.*;
import android.os.*;

public final class SignIn
{
    public static final Api<SignInOptions> API;
    public static final Api.AbstractClientBuilder<SignInClientImpl, SignInOptions> CLIENT_BUILDER;
    public static final Api.ClientKey<SignInClientImpl> CLIENT_KEY;
    public static final Api<SignInOptionsInternal> INTERNAL_API;
    public static final Api.ClientKey<SignInClientImpl> INTERNAL_CLIENT_KEY;
    public static final Scope SCOPE_EMAIL;
    public static final Scope SCOPE_PROFILE;
    private static final Api.AbstractClientBuilder<SignInClientImpl, SignInOptionsInternal> zzacz;
    
    static {
        CLIENT_KEY = new Api.ClientKey();
        INTERNAL_CLIENT_KEY = new Api.ClientKey();
        CLIENT_BUILDER = new zza();
        zzacz = new zzb();
        SCOPE_PROFILE = new Scope("profile");
        SCOPE_EMAIL = new Scope("email");
        API = new Api<SignInOptions>("SignIn.API", (Api.AbstractClientBuilder<C, SignInOptions>)SignIn.CLIENT_BUILDER, (Api.ClientKey<C>)SignIn.CLIENT_KEY);
        INTERNAL_API = new Api<SignInOptionsInternal>("SignIn.INTERNAL_API", (Api.AbstractClientBuilder<C, SignInOptionsInternal>)SignIn.zzacz, (Api.ClientKey<C>)SignIn.INTERNAL_CLIENT_KEY);
    }
    
    public static class SignInOptionsInternal implements Api$ApiOptions$HasOptions
    {
        private final Bundle zzada;
        
        public Bundle getSignInOptionsBundle() {
            return this.zzada;
        }
    }
}
