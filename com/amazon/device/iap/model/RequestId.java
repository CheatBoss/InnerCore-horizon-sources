package com.amazon.device.iap.model;

import android.os.*;
import java.util.*;
import com.amazon.device.iap.internal.util.*;
import org.json.*;

public final class RequestId implements Parcelable
{
    public static final Parcelable$Creator<RequestId> CREATOR;
    private static final String ENCODED_ID = "encodedId";
    private final String encodedId;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<RequestId>() {
            public RequestId createFromParcel(final Parcel parcel) {
                return new RequestId(parcel, null);
            }
            
            public RequestId[] newArray(final int n) {
                return new RequestId[n];
            }
        };
    }
    
    public RequestId() {
        this.encodedId = UUID.randomUUID().toString();
    }
    
    private RequestId(final Parcel parcel) {
        this.encodedId = parcel.readString();
    }
    
    private RequestId(final String encodedId) {
        d.a((Object)encodedId, "encodedId");
        this.encodedId = encodedId;
    }
    
    public static RequestId fromString(final String s) {
        return new RequestId(s);
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o != null && this.getClass() == o.getClass() && this.encodedId.equals(((RequestId)o).encodedId);
    }
    
    @Override
    public int hashCode() {
        final String encodedId = this.encodedId;
        int hashCode;
        if (encodedId == null) {
            hashCode = 0;
        }
        else {
            hashCode = encodedId.hashCode();
        }
        return hashCode + 31;
    }
    
    public JSONObject toJSON() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("encodedId", (Object)this.encodedId);
            return jsonObject;
        }
        catch (JSONException ex) {
            return jsonObject;
        }
    }
    
    @Override
    public String toString() {
        return this.encodedId;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.encodedId);
    }
}
