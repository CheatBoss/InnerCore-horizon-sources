package com.microsoft.xbox.idp.model;

import android.net.*;

public class Const
{
    public static final Uri URL_ENFORCEMENT_XBOX_COM;
    public static final Uri URL_XBOX_COM;
    
    static {
        URL_XBOX_COM = Uri.parse("http://www.xbox.com");
        URL_ENFORCEMENT_XBOX_COM = Uri.parse("http://enforcement.xbox.com");
    }
}
