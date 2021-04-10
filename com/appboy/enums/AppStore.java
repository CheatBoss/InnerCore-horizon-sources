package com.appboy.enums;

import com.appboy.models.*;
import java.util.*;

public enum AppStore implements IPutIntoJson<String>
{
    GOOGLE_PLAY_STORE, 
    KINDLE_STORE;
    
    public static String serverStringToEnumString(final String s) {
        return s.replace(" ", "_").toUpperCase(Locale.US);
    }
    
    @Override
    public String forJsonPut() {
        final int n = AppStore$1.a[this.ordinal()];
        if (n == 1) {
            return "Google Play Store";
        }
        if (n != 2) {
            return null;
        }
        return "Kindle Store";
    }
}
