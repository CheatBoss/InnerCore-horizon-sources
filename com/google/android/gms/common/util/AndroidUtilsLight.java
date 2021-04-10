package com.google.android.gms.common.util;

import java.security.*;

public class AndroidUtilsLight
{
    public static MessageDigest getMessageDigest(final String s) {
        for (int i = 0; i < 2; ++i) {
            try {
                final MessageDigest instance = MessageDigest.getInstance(s);
                if (instance != null) {
                    return instance;
                }
            }
            catch (NoSuchAlgorithmException ex) {}
        }
        return null;
    }
}
