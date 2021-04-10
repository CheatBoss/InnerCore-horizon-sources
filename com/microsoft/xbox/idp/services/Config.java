package com.microsoft.xbox.idp.services;

public class Config
{
    public static Endpoints.Type endpointType;
    
    static {
        Config.endpointType = Endpoints.Type.PROD;
    }
}
