package com.google.firebase;

import com.google.android.gms.common.util.*;
import android.content.*;
import android.text.*;
import com.google.android.gms.common.internal.*;

public final class FirebaseOptions
{
    private final String zza;
    private final String zzb;
    private final String zzc;
    private final String zzd;
    private final String zze;
    private final String zzf;
    private final String zzg;
    
    private FirebaseOptions(final String zzb, final String zza, final String zzc, final String zzd, final String zze, final String zzf, final String zzg) {
        Preconditions.checkState(Strings.isEmptyOrWhitespace(zzb) ^ true, "ApplicationId must be set.");
        this.zzb = zzb;
        this.zza = zza;
        this.zzc = zzc;
        this.zzd = zzd;
        this.zze = zze;
        this.zzf = zzf;
        this.zzg = zzg;
    }
    
    public static FirebaseOptions fromResource(final Context context) {
        final StringResourceValueReader stringResourceValueReader = new StringResourceValueReader(context);
        final String string = stringResourceValueReader.getString("google_app_id");
        if (TextUtils.isEmpty((CharSequence)string)) {
            return null;
        }
        return new FirebaseOptions(string, stringResourceValueReader.getString("google_api_key"), stringResourceValueReader.getString("firebase_database_url"), stringResourceValueReader.getString("ga_trackingId"), stringResourceValueReader.getString("gcm_defaultSenderId"), stringResourceValueReader.getString("google_storage_bucket"), stringResourceValueReader.getString("project_id"));
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (!(o instanceof FirebaseOptions)) {
            return false;
        }
        final FirebaseOptions firebaseOptions = (FirebaseOptions)o;
        return Objects.equal(this.zzb, firebaseOptions.zzb) && Objects.equal(this.zza, firebaseOptions.zza) && Objects.equal(this.zzc, firebaseOptions.zzc) && Objects.equal(this.zzd, firebaseOptions.zzd) && Objects.equal(this.zze, firebaseOptions.zze) && Objects.equal(this.zzf, firebaseOptions.zzf) && Objects.equal(this.zzg, firebaseOptions.zzg);
    }
    
    public final String getApplicationId() {
        return this.zzb;
    }
    
    public final String getGcmSenderId() {
        return this.zze;
    }
    
    @Override
    public final int hashCode() {
        return Objects.hashCode(this.zzb, this.zza, this.zzc, this.zzd, this.zze, this.zzf, this.zzg);
    }
    
    @Override
    public final String toString() {
        return Objects.toStringHelper(this).add("applicationId", this.zzb).add("apiKey", this.zza).add("databaseUrl", this.zzc).add("gcmSenderId", this.zze).add("storageBucket", this.zzf).add("projectId", this.zzg).toString();
    }
}
