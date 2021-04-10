package com.appboy.models;

public interface IInAppMessageHtml extends IInAppMessage
{
    String getAssetsZipRemoteUrl();
    
    String getLocalAssetsDirectoryUrl();
    
    boolean logButtonClick(final String p0);
    
    void setAssetsZipRemoteUrl(final String p0);
    
    void setLocalAssetsDirectoryUrl(final String p0);
}
