package com.appboy.ui.actions;

import android.net.*;
import android.os.*;
import com.appboy.enums.*;
import com.appboy.support.*;

public class ActionFactory
{
    public static UriAction createUriActionFromUri(final Uri uri, final Bundle bundle, final boolean b, final Channel channel) {
        if (uri != null) {
            return new UriAction(uri, bundle, b, channel);
        }
        return null;
    }
    
    public static UriAction createUriActionFromUrlString(final String s, final Bundle bundle, final boolean b, final Channel channel) {
        if (!StringUtils.isNullOrBlank(s)) {
            return createUriActionFromUri(Uri.parse(s), bundle, b, channel);
        }
        return null;
    }
}
