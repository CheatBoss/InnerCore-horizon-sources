package com.microsoft.aad.adal;

import com.google.gson.annotations.*;

final class Link
{
    @SerializedName("href")
    private String mHref;
    @SerializedName("rel")
    private String mRel;
    
    String getHref() {
        return this.mHref;
    }
    
    String getRel() {
        return this.mRel;
    }
    
    void setHref(final String mHref) {
        this.mHref = mHref;
    }
    
    void setRel(final String mRel) {
        this.mRel = mRel;
    }
}
