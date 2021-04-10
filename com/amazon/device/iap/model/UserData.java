package com.amazon.device.iap.model;

import android.os.*;
import com.amazon.device.iap.internal.model.*;
import org.json.*;

public final class UserData implements Parcelable
{
    public static final Parcelable$Creator<UserData> CREATOR;
    private static final String MARKETPLACE = "marketplace";
    private static final String USER_ID = "userId";
    private final String marketplace;
    private final String userId;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UserData>() {
            public UserData createFromParcel(final Parcel parcel) {
                return new UserData(parcel, null);
            }
            
            public UserData[] newArray(final int n) {
                return new UserData[n];
            }
        };
    }
    
    private UserData(final Parcel parcel) {
        this.userId = parcel.readString();
        this.marketplace = parcel.readString();
    }
    
    public UserData(final UserDataBuilder userDataBuilder) {
        this.userId = userDataBuilder.getUserId();
        this.marketplace = userDataBuilder.getMarketplace();
    }
    
    public int describeContents() {
        return 0;
    }
    
    public String getMarketplace() {
        return this.marketplace;
    }
    
    public String getUserId() {
        return this.userId;
    }
    
    public JSONObject toJSON() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", (Object)this.userId);
            jsonObject.put("marketplace", (Object)this.marketplace);
            return jsonObject;
        }
        catch (JSONException ex) {
            return jsonObject;
        }
    }
    
    @Override
    public String toString() {
        try {
            return this.toJSON().toString(4);
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeStringArray(new String[] { this.userId, this.marketplace });
    }
}
