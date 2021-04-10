package com.microsoft.xbox.idp.services;

public interface Endpoints
{
    String accounts();
    
    String privacy();
    
    String profile();
    
    String userAccount();
    
    String userManagement();
    
    public enum Type
    {
        DNET, 
        PROD;
    }
}
