package com.microsoft.xbox.idp.services;

class EndpointsProd implements Endpoints
{
    @Override
    public String accounts() {
        return "https://accounts.xboxlive.com";
    }
    
    @Override
    public String privacy() {
        return "https://privacy.xboxlive.com";
    }
    
    @Override
    public String profile() {
        return "https://profile.xboxlive.com";
    }
    
    @Override
    public String userAccount() {
        return "https://accountstroubleshooter.xboxlive.com";
    }
    
    @Override
    public String userManagement() {
        return "https://user.mgt.xboxlive.com";
    }
}
