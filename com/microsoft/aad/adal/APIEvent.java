package com.microsoft.aad.adal;

import android.content.*;
import android.util.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.security.*;

final class APIEvent extends DefaultEvent
{
    private static final String TAG;
    private final String mEventName;
    
    static {
        TAG = DefaultEvent.class.getSimpleName();
    }
    
    APIEvent(final String mEventName) {
        this.setProperty("Microsoft.ADAL.event_name", mEventName);
        this.mEventName = mEventName;
    }
    
    APIEvent(final String s, final Context context, final String s2) {
        this(s);
        this.setDefaults(context, s2);
    }
    
    String getEventName() {
        return this.mEventName;
    }
    
    @Override
    public void processEvent(final Map<String, String> map) {
        super.processEvent(map);
        for (final Pair<String, String> pair : this.getEventList()) {
            final String s = (String)pair.first;
            if (s.equals("Microsoft.ADAL.authority_type") || s.equals("Microsoft.ADAL.is_deprecated") || s.equals("Microsoft.ADAL.authority_validation_status") || s.equals("Microsoft.ADAL.extended_expires_on_setting") || s.equals("Microsoft.ADAL.prompt_behavior") || s.equals("Microsoft.ADAL.is_successful") || s.equals("Microsoft.ADAL.idp") || s.equals("Microsoft.ADAL.tenant_id") || s.equals("Microsoft.ADAL.user_id") || s.equals("Microsoft.ADAL.login_hint") || s.equals("Microsoft.ADAL.response_time") || s.equals("Microsoft.ADAL.correlation_id") || s.equals("Microsoft.ADAL.request_id") || s.equals("Microsoft.ADAL.api_id") || s.equals("Microsoft.ADAL.api_error_code") || s.equals("Microsoft.ADAL.server_error_code") || s.equals("Microsoft.ADAL.server_sub_error_code") || s.equals("Microsoft.ADAL.rt_age") || s.equals("Microsoft.ADAL.spe_info")) {
                map.put(s, (String)pair.second);
            }
        }
    }
    
    void setAPIId(final String s) {
        this.setProperty("Microsoft.ADAL.api_id", s);
    }
    
    void setAuthority(String authorityType) {
        if (StringExtensions.isNullOrBlank(authorityType)) {
            return;
        }
        this.setProperty("Microsoft.ADAL.authority", authorityType);
        final URL url = StringExtensions.getUrl(authorityType);
        if (url == null) {
            return;
        }
        if (UrlExtensions.isADFSAuthority(url)) {
            authorityType = "adfs";
        }
        else {
            authorityType = "aad";
        }
        this.setAuthorityType(authorityType);
    }
    
    void setAuthorityType(final String s) {
        this.setProperty("Microsoft.ADAL.authority_type", s);
    }
    
    void setExtendedExpiresOnSetting(final boolean b) {
        this.setProperty("Microsoft.ADAL.extended_expires_on_setting", String.valueOf(b));
    }
    
    void setIdToken(final String s) {
        if (StringExtensions.isNullOrBlank(s)) {
            return;
        }
        try {
            final IdToken idToken = new IdToken(s);
            final UserInfo userInfo = new UserInfo(idToken);
            this.setProperty("Microsoft.ADAL.idp", idToken.getIdentityProvider());
            try {
                this.setProperty("Microsoft.ADAL.tenant_id", StringExtensions.createHash(idToken.getTenantId()));
                this.setProperty("Microsoft.ADAL.user_id", StringExtensions.createHash(userInfo.getDisplayableId()));
            }
            catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append(APIEvent.TAG);
                sb.append(":setIdToken");
                Logger.i(sb.toString(), "Skipping TENANT_ID and USER_ID", "");
            }
        }
        catch (AuthenticationException ex2) {}
    }
    
    void setIsDeprecated(final boolean b) {
        this.setProperty("Microsoft.ADAL.is_deprecated", String.valueOf(b));
    }
    
    void setLoginHint(final String s) {
        try {
            this.setProperty("Microsoft.ADAL.login_hint", StringExtensions.createHash(s));
        }
        catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append(APIEvent.TAG);
            sb.append(":setLoginHint");
            Logger.i(sb.toString(), "Skipping telemetry for LOGIN_HINT", "");
        }
    }
    
    void setPromptBehavior(final String s) {
        this.setProperty("Microsoft.ADAL.prompt_behavior", s);
    }
    
    void setRefreshTokenAge(final String s) {
        if (!StringExtensions.isNullOrBlank(s)) {
            this.setProperty("Microsoft.ADAL.rt_age", s.trim());
        }
    }
    
    void setServerErrorCode(final String s) {
        if (s != null && !s.equals("0")) {
            this.setProperty("Microsoft.ADAL.server_error_code", s.trim());
        }
    }
    
    void setServerSubErrorCode(final String s) {
        if (s != null && !s.equals("0")) {
            this.setProperty("Microsoft.ADAL.server_sub_error_code", s.trim());
        }
    }
    
    void setSpeRing(final String s) {
        if (!StringExtensions.isNullOrBlank(s)) {
            this.setProperty("Microsoft.ADAL.spe_info", s.trim());
        }
    }
    
    void setValidationStatus(final String s) {
        this.setProperty("Microsoft.ADAL.authority_validation_status", s);
    }
    
    void setWasApiCallSuccessful(final boolean b, final Exception ex) {
        this.setProperty("Microsoft.ADAL.is_successful", String.valueOf(b));
        if (ex != null && ex instanceof AuthenticationException) {
            this.setProperty("Microsoft.ADAL.api_error_code", ((AuthenticationException)ex).getCode().toString());
        }
    }
    
    void stopTelemetryAndFlush() {
        Telemetry.getInstance().stopEvent(this.getTelemetryRequestId(), this, this.getEventName());
        Telemetry.getInstance().flush(this.getTelemetryRequestId());
    }
}
