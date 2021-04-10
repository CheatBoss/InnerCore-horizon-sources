package com.google.android.gms.common;

import com.google.android.gms.common.internal.*;
import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;

public class Feature extends AbstractSafeParcelable
{
    public static final Parcelable$Creator<Feature> CREATOR;
    private final String name;
    @Deprecated
    private final int zzaq;
    private final long zzar;
    
    static {
        CREATOR = (Parcelable$Creator)new FeatureCreator();
    }
    
    public Feature(final String name, final int zzaq, final long zzar) {
        this.name = name;
        this.zzaq = zzaq;
        this.zzar = zzar;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof Feature) {
            final Feature feature = (Feature)o;
            if (((this.getName() != null && this.getName().equals(feature.getName())) || (this.getName() == null && feature.getName() == null)) && this.getVersion() == feature.getVersion()) {
                return true;
            }
        }
        return false;
    }
    
    public String getName() {
        return this.name;
    }
    
    public long getVersion() {
        long zzar;
        if ((zzar = this.zzar) == -1L) {
            zzar = this.zzaq;
        }
        return zzar;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.getName(), this.getVersion());
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("name", this.getName()).add("version", this.getVersion()).toString();
    }
    
    public void writeToParcel(final Parcel parcel, int beginObjectHeader) {
        beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 1, this.getName(), false);
        SafeParcelWriter.writeInt(parcel, 2, this.zzaq);
        SafeParcelWriter.writeLong(parcel, 3, this.getVersion());
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
