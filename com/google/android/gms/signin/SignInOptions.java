package com.google.android.gms.signin;

import com.google.android.gms.common.api.*;

public final class SignInOptions implements Api$ApiOptions$Optional
{
    public static final SignInOptions DEFAULT;
    private final boolean zzadb;
    private final boolean zzadc;
    private final Long zzadd;
    private final Long zzade;
    private final boolean zzt;
    private final boolean zzv;
    private final String zzw;
    private final String zzx;
    
    static {
        DEFAULT = new Builder().build();
    }
    
    private SignInOptions(final boolean zzadb, final boolean zzt, final String zzw, final boolean zzv, final String zzx, final boolean zzadc, final Long zzadd, final Long zzade) {
        this.zzadb = zzadb;
        this.zzt = zzt;
        this.zzw = zzw;
        this.zzv = zzv;
        this.zzadc = zzadc;
        this.zzx = zzx;
        this.zzadd = zzadd;
        this.zzade = zzade;
    }
    
    public final Long getAuthApiSignInModuleVersion() {
        return this.zzadd;
    }
    
    public final String getHostedDomain() {
        return this.zzx;
    }
    
    public final Long getRealClientLibraryVersion() {
        return this.zzade;
    }
    
    public final String getServerClientId() {
        return this.zzw;
    }
    
    public final boolean isForceCodeForRefreshToken() {
        return this.zzv;
    }
    
    public final boolean isIdTokenRequested() {
        return this.zzt;
    }
    
    public final boolean isOfflineAccessRequested() {
        return this.zzadb;
    }
    
    public final boolean waitForAccessTokenRefresh() {
        return this.zzadc;
    }
    
    public static final class Builder
    {
        private boolean zzadf;
        private boolean zzadg;
        private String zzadh;
        private boolean zzadi;
        private String zzadj;
        private boolean zzadk;
        private Long zzadl;
        private Long zzadm;
        
        public final SignInOptions build() {
            return new SignInOptions(this.zzadf, this.zzadg, this.zzadh, this.zzadi, this.zzadj, this.zzadk, this.zzadl, this.zzadm, null);
        }
    }
}
