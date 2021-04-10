package com.microsoft.xbox.idp.util;

import android.os.*;
import android.app.*;

public class FragmentLoaderKey implements Parcelable
{
    public static final Parcelable$Creator<FragmentLoaderKey> CREATOR;
    private final String className;
    private final int loaderId;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<FragmentLoaderKey>() {
            public FragmentLoaderKey createFromParcel(final Parcel parcel) {
                return new FragmentLoaderKey(parcel);
            }
            
            public FragmentLoaderKey[] newArray(final int n) {
                return new FragmentLoaderKey[n];
            }
        };
    }
    
    protected FragmentLoaderKey(final Parcel parcel) {
        this.className = parcel.readString();
        this.loaderId = parcel.readInt();
    }
    
    public FragmentLoaderKey(final Class<? extends Fragment> clazz, final int loaderId) {
        this.className = clazz.getName();
        this.loaderId = loaderId;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final FragmentLoaderKey fragmentLoaderKey = (FragmentLoaderKey)o;
        return this.loaderId == fragmentLoaderKey.loaderId && this.className.equals(fragmentLoaderKey.className);
    }
    
    @Override
    public int hashCode() {
        return this.className.hashCode() * 31 + this.loaderId;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.className);
        parcel.writeInt(this.loaderId);
    }
}
