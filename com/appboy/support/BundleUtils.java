package com.appboy.support;

import android.os.*;
import java.util.*;

public class BundleUtils
{
    public static Bundle mapToBundle(final Map<String, String> map) {
        final Bundle bundle = new Bundle();
        if (map != null) {
            for (final Map.Entry<String, String> entry : map.entrySet()) {
                bundle.putString((String)entry.getKey(), (String)entry.getValue());
            }
        }
        return bundle;
    }
}
