package com.microsoft.aad.adal;

import android.net.*;
import java.net.*;
import java.util.*;

final class Utility
{
    private Utility() {
    }
    
    static URL constructAuthorityUrl(final URL url, final String s) throws MalformedURLException {
        return new URL(new Uri$Builder().scheme(url.getProtocol()).authority(s).appendPath(url.getPath().replaceFirst("/", "")).build().toString());
    }
    
    static Date getImmutableDateObject(final Date date) {
        if (date != null) {
            return new Date(date.getTime());
        }
        return date;
    }
    
    static boolean isClaimsChallengePresent(final AuthenticationRequest authenticationRequest) {
        return StringExtensions.isNullOrBlank(authenticationRequest.getClaimsChallenge()) ^ true;
    }
}
