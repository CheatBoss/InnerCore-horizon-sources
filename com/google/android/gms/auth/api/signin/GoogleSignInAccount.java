package com.google.android.gms.auth.api.signin;

import android.net.*;
import com.google.android.gms.common.api.*;
import com.google.android.gms.common.util.*;
import com.google.android.gms.common.internal.*;
import java.util.*;
import android.text.*;
import org.json.*;
import android.accounts.*;
import com.google.android.gms.common.internal.safeparcel.*;
import android.os.*;

public class GoogleSignInAccount extends AbstractSafeParcelable implements ReflectedParcelable
{
    public static final Parcelable$Creator<GoogleSignInAccount> CREATOR;
    public static Clock sClock;
    private final int versionCode;
    private String zze;
    private String zzf;
    private String zzg;
    private String zzh;
    private Uri zzi;
    private String zzj;
    private long zzk;
    private String zzl;
    private List<Scope> zzm;
    private String zzn;
    private String zzo;
    private Set<Scope> zzp;
    
    static {
        CREATOR = (Parcelable$Creator)new GoogleSignInAccountCreator();
        GoogleSignInAccount.sClock = DefaultClock.getInstance();
    }
    
    GoogleSignInAccount(final int versionCode, final String zze, final String zzf, final String zzg, final String zzh, final Uri zzi, final String zzj, final long zzk, final String zzl, final List<Scope> zzm, final String zzn, final String zzo) {
        this.zzp = new HashSet<Scope>();
        this.versionCode = versionCode;
        this.zze = zze;
        this.zzf = zzf;
        this.zzg = zzg;
        this.zzh = zzh;
        this.zzi = zzi;
        this.zzj = zzj;
        this.zzk = zzk;
        this.zzl = zzl;
        this.zzm = zzm;
        this.zzn = zzn;
        this.zzo = zzo;
    }
    
    public static GoogleSignInAccount create(final String s, final String s2, final String s3, final String s4, final String s5, final String s6, final Uri uri, Long value, final String s7, final Set<Scope> set) {
        if (value == null) {
            value = GoogleSignInAccount.sClock.currentTimeMillis() / 1000L;
        }
        return new GoogleSignInAccount(3, s, s2, s3, s4, uri, null, value, Preconditions.checkNotEmpty(s7), new ArrayList<Scope>(Preconditions.checkNotNull(set)), s5, s6);
    }
    
    public static GoogleSignInAccount fromJsonString(String optString) throws JSONException {
        if (TextUtils.isEmpty((CharSequence)optString)) {
            return null;
        }
        final JSONObject jsonObject = new JSONObject(optString);
        optString = jsonObject.optString("photoUrl", (String)null);
        Uri parse;
        if (!TextUtils.isEmpty((CharSequence)optString)) {
            parse = Uri.parse(optString);
        }
        else {
            parse = null;
        }
        final long long1 = Long.parseLong(jsonObject.getString("expirationTime"));
        final HashSet<Scope> set = new HashSet<Scope>();
        final JSONArray jsonArray = jsonObject.getJSONArray("grantedScopes");
        for (int length = jsonArray.length(), i = 0; i < length; ++i) {
            set.add(new Scope(jsonArray.getString(i)));
        }
        return create(jsonObject.optString("id"), jsonObject.optString("tokenId", (String)null), jsonObject.optString("email", (String)null), jsonObject.optString("displayName", (String)null), jsonObject.optString("givenName", (String)null), jsonObject.optString("familyName", (String)null), parse, long1, jsonObject.getString("obfuscatedIdentifier"), set).setServerAuthCode(jsonObject.optString("serverAuthCode", (String)null));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GoogleSignInAccount)) {
            return false;
        }
        final GoogleSignInAccount googleSignInAccount = (GoogleSignInAccount)o;
        return googleSignInAccount.getObfuscatedIdentifier().equals(this.getObfuscatedIdentifier()) && googleSignInAccount.getRequestedScopes().equals(this.getRequestedScopes());
    }
    
    public Account getAccount() {
        if (this.zzg == null) {
            return null;
        }
        return new Account(this.zzg, "com.google");
    }
    
    public String getDisplayName() {
        return this.zzh;
    }
    
    public String getEmail() {
        return this.zzg;
    }
    
    public long getExpirationTimeSecs() {
        return this.zzk;
    }
    
    public String getFamilyName() {
        return this.zzo;
    }
    
    public String getGivenName() {
        return this.zzn;
    }
    
    public String getId() {
        return this.zze;
    }
    
    public String getIdToken() {
        return this.zzf;
    }
    
    public String getObfuscatedIdentifier() {
        return this.zzl;
    }
    
    public Uri getPhotoUrl() {
        return this.zzi;
    }
    
    public Set<Scope> getRequestedScopes() {
        final HashSet<Object> set = (HashSet<Object>)new HashSet<Scope>(this.zzm);
        set.addAll(this.zzp);
        return (Set<Scope>)set;
    }
    
    public String getServerAuthCode() {
        return this.zzj;
    }
    
    @Override
    public int hashCode() {
        return (this.getObfuscatedIdentifier().hashCode() + 527) * 31 + this.getRequestedScopes().hashCode();
    }
    
    public GoogleSignInAccount setServerAuthCode(final String zzj) {
        this.zzj = zzj;
        return this;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.versionCode);
        SafeParcelWriter.writeString(parcel, 2, this.getId(), false);
        SafeParcelWriter.writeString(parcel, 3, this.getIdToken(), false);
        SafeParcelWriter.writeString(parcel, 4, this.getEmail(), false);
        SafeParcelWriter.writeString(parcel, 5, this.getDisplayName(), false);
        SafeParcelWriter.writeParcelable(parcel, 6, (Parcelable)this.getPhotoUrl(), n, false);
        SafeParcelWriter.writeString(parcel, 7, this.getServerAuthCode(), false);
        SafeParcelWriter.writeLong(parcel, 8, this.getExpirationTimeSecs());
        SafeParcelWriter.writeString(parcel, 9, this.getObfuscatedIdentifier(), false);
        SafeParcelWriter.writeTypedList(parcel, 10, this.zzm, false);
        SafeParcelWriter.writeString(parcel, 11, this.getGivenName(), false);
        SafeParcelWriter.writeString(parcel, 12, this.getFamilyName(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
