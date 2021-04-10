package com.microsoft.aad.adal;

import java.io.*;
import android.net.*;
import java.util.*;
import android.os.*;

public class UserInfo implements Serializable
{
    private static final long serialVersionUID = 8790127561636702672L;
    private String mDisplayableId;
    private String mFamilyName;
    private String mGivenName;
    private String mIdentityProvider;
    private transient Uri mPasswordChangeUrl;
    private transient Date mPasswordExpiresOn;
    private String mUniqueId;
    
    public UserInfo() {
    }
    
    public UserInfo(final IdToken idToken) {
        this.mUniqueId = null;
        this.mDisplayableId = null;
        Label_0055: {
            String mUniqueId;
            if (!StringExtensions.isNullOrBlank(idToken.getObjectId())) {
                mUniqueId = idToken.getObjectId();
            }
            else {
                if (StringExtensions.isNullOrBlank(idToken.getSubject())) {
                    break Label_0055;
                }
                mUniqueId = idToken.getSubject();
            }
            this.mUniqueId = mUniqueId;
        }
        Label_0096: {
            String mDisplayableId;
            if (!StringExtensions.isNullOrBlank(idToken.getUpn())) {
                mDisplayableId = idToken.getUpn();
            }
            else {
                if (StringExtensions.isNullOrBlank(idToken.getEmail())) {
                    break Label_0096;
                }
                mDisplayableId = idToken.getEmail();
            }
            this.mDisplayableId = mDisplayableId;
        }
        this.mGivenName = idToken.getGivenName();
        this.mFamilyName = idToken.getFamilyName();
        this.mIdentityProvider = idToken.getIdentityProvider();
        if (idToken.getPasswordExpiration() > 0L) {
            final GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.add(13, (int)idToken.getPasswordExpiration());
            this.mPasswordExpiresOn = gregorianCalendar.getTime();
        }
        this.mPasswordChangeUrl = null;
        if (!StringExtensions.isNullOrBlank(idToken.getPasswordChangeUrl())) {
            this.mPasswordChangeUrl = Uri.parse(idToken.getPasswordChangeUrl());
        }
    }
    
    public UserInfo(final String mDisplayableId) {
        this.mDisplayableId = mDisplayableId;
    }
    
    public UserInfo(final String mUniqueId, final String mGivenName, final String mFamilyName, final String mIdentityProvider, final String mDisplayableId) {
        this.mUniqueId = mUniqueId;
        this.mGivenName = mGivenName;
        this.mFamilyName = mFamilyName;
        this.mIdentityProvider = mIdentityProvider;
        this.mDisplayableId = mDisplayableId;
    }
    
    static UserInfo getUserInfoFromBrokerResult(final Bundle bundle) {
        return new UserInfo(bundle.getString("account.userinfo.userid"), bundle.getString("account.userinfo.given.name"), bundle.getString("account.userinfo.family.name"), bundle.getString("account.userinfo.identity.provider"), bundle.getString("account.userinfo.userid.displayable"));
    }
    
    public String getDisplayableId() {
        return this.mDisplayableId;
    }
    
    public String getFamilyName() {
        return this.mFamilyName;
    }
    
    public String getGivenName() {
        return this.mGivenName;
    }
    
    public String getIdentityProvider() {
        return this.mIdentityProvider;
    }
    
    public Uri getPasswordChangeUrl() {
        return this.mPasswordChangeUrl;
    }
    
    public Date getPasswordExpiresOn() {
        return Utility.getImmutableDateObject(this.mPasswordExpiresOn);
    }
    
    public String getUserId() {
        return this.mUniqueId;
    }
    
    void setDisplayableId(final String mDisplayableId) {
        this.mDisplayableId = mDisplayableId;
    }
    
    void setUserId(final String mUniqueId) {
        this.mUniqueId = mUniqueId;
    }
}
