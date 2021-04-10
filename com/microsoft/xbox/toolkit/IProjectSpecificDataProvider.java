package com.microsoft.xbox.toolkit;

public interface IProjectSpecificDataProvider
{
    boolean getAllowExplicitContent();
    
    String getAutoSuggestdDataSource();
    
    String getCombinedContentRating();
    
    String getConnectedLocale();
    
    String getConnectedLocale(final boolean p0);
    
    String getContentRestrictions();
    
    String getCurrentSandboxID();
    
    boolean getInitializeComplete();
    
    boolean getIsForXboxOne();
    
    boolean getIsFreeAccount();
    
    boolean getIsXboxMusicSupported();
    
    String getLegalLocale();
    
    String getMembershipLevel();
    
    String getPrivileges();
    
    String getRegion();
    
    String getSCDRpsTicket();
    
    String getVersionCheckUrl();
    
    int getVersionCode();
    
    String getWindowsLiveClientId();
    
    String getXuidString();
    
    boolean isDeviceLocaleKnown();
    
    void resetModels(final boolean p0);
    
    void setPrivileges(final String p0);
    
    void setSCDRpsTicket(final String p0);
    
    void setXuidString(final String p0);
}
