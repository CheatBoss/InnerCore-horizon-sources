package com.appsflyer;

public class AFVersionDeclaration
{
    private static String googleSdkIdentifier;
    
    private AFVersionDeclaration() {
    }
    
    public static void init() {
        AFVersionDeclaration.googleSdkIdentifier = "!SDK-VERSION-STRING!:com.appsflyer:af-android-sdk:5.4.1";
    }
}
