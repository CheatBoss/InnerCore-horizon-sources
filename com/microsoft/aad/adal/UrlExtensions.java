package com.microsoft.aad.adal;

import java.net.*;
import java.util.*;

final class UrlExtensions
{
    private UrlExtensions() {
    }
    
    public static boolean isADFSAuthority(final URL url) {
        final String path = url.getPath();
        return !StringExtensions.isNullOrBlank(path) && path.toLowerCase(Locale.ENGLISH).equals("/adfs");
    }
}
