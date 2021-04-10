package com.google.firebase.messaging;

import java.util.regex.*;

public class FirebaseMessaging
{
    private static final Pattern zzdm;
    
    static {
        zzdm = Pattern.compile("[a-zA-Z0-9-_.~%]{1,900}");
    }
}
