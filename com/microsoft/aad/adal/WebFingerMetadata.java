package com.microsoft.aad.adal;

import java.util.*;
import com.google.gson.annotations.*;

final class WebFingerMetadata
{
    @SerializedName("links")
    private List<Link> mLinks;
    @SerializedName("subject")
    private String mSubject;
    
    List<Link> getLinks() {
        return this.mLinks;
    }
    
    String getSubject() {
        return this.mSubject;
    }
    
    void setLinks(final List<Link> mLinks) {
        this.mLinks = mLinks;
    }
    
    void setSubject(final String mSubject) {
        this.mSubject = mSubject;
    }
}
