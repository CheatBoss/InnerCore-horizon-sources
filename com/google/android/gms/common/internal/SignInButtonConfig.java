package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.*;
import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;

public class SignInButtonConfig extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<SignInButtonConfig> CREATOR;
    private final int zzal;
    @Deprecated
    private final Scope[] zzqw;
    private final int zzux;
    private final int zzuy;
    
    static {
        CREATOR = (Parcelable$Creator)new SignInButtonConfigCreator();
    }
    
    SignInButtonConfig(final int zzal, final int zzux, final int zzuy, final Scope[] zzqw) {
        this.zzal = zzal;
        this.zzux = zzux;
        this.zzuy = zzuy;
        this.zzqw = zzqw;
    }
    
    public SignInButtonConfig(final int n, final int n2, final Scope[] array) {
        this(1, n, n2, null);
    }
    
    public int getButtonSize() {
        return this.zzux;
    }
    
    public int getColorScheme() {
        return this.zzuy;
    }
    
    @Deprecated
    public Scope[] getScopes() {
        return this.zzqw;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        SafeParcelWriter.writeInt(parcel, 2, this.getButtonSize());
        SafeParcelWriter.writeInt(parcel, 3, this.getColorScheme());
        SafeParcelWriter.writeTypedArray(parcel, 4, this.getScopes(), n, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
