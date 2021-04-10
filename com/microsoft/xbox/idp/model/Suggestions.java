package com.microsoft.xbox.idp.model;

import android.os.*;
import java.util.*;

public class Suggestions
{
    public static class Request
    {
        public int Algorithm;
        public int Count;
        public String Locale;
        public String Seed;
    }
    
    public static class Response implements Parcelable
    {
        public static final Parcelable$Creator<Response> CREATOR;
        public ArrayList<String> Gamertags;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<Response>() {
                public Response createFromParcel(final Parcel parcel) {
                    return new Response(parcel);
                }
                
                public Response[] newArray(final int n) {
                    return new Response[n];
                }
            };
        }
        
        protected Response(final Parcel parcel) {
            this.Gamertags = (ArrayList<String>)parcel.createStringArrayList();
        }
        
        public int describeContents() {
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeStringList((List)this.Gamertags);
        }
    }
}
