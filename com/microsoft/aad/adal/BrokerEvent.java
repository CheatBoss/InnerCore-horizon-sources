package com.microsoft.aad.adal;

import android.util.*;
import java.util.*;

final class BrokerEvent extends DefaultEvent
{
    BrokerEvent(final String s) {
        this.setProperty("Microsoft.ADAL.event_name", s);
    }
    
    @Override
    public void processEvent(final Map<String, String> map) {
        final List<Pair<String, String>> eventList = this.getEventList();
        map.put("Microsoft.ADAL.broker_app_used", Boolean.toString(true));
        for (final Pair<String, String> pair : eventList) {
            if (!((String)pair.first).equals("Microsoft.ADAL.event_name")) {
                map.put((String)pair.first, (String)pair.second);
            }
        }
    }
    
    void setBrokerAccountServerStartsBinding() {
        this.setProperty("Microsoft.ADAL.broker_account_service_starts_binding", Boolean.toString(true));
    }
    
    void setBrokerAccountServiceBindingSucceed(final boolean b) {
        this.setProperty("Microsoft.ADAL.broker_account_service_binding_succeed", Boolean.toString(b));
    }
    
    void setBrokerAccountServiceConnected() {
        this.setProperty("Microsoft.ADAL.broker_account_service_connected", Boolean.toString(true));
    }
    
    void setBrokerAppName(final String s) {
        this.setProperty("Microsoft.ADAL.broker_app", s);
    }
    
    void setBrokerAppVersion(final String s) {
        this.setProperty("Microsoft.ADAL.broker_version", s);
    }
    
    void setRefreshTokenAge(final String s) {
        if (!StringExtensions.isNullOrBlank(s)) {
            this.setProperty("Microsoft.ADAL.rt_age", s.trim());
        }
    }
    
    void setServerErrorCode(final String s) {
        if (!StringExtensions.isNullOrBlank(s) && !s.equals("0")) {
            this.setProperty("Microsoft.ADAL.server_error_code", s.trim());
        }
    }
    
    void setServerSubErrorCode(final String s) {
        if (!StringExtensions.isNullOrBlank(s) && !s.equals("0")) {
            this.setProperty("Microsoft.ADAL.server_sub_error_code", s.trim());
        }
    }
    
    void setSpeRing(final String s) {
        if (!StringExtensions.isNullOrBlank(s)) {
            this.setProperty("Microsoft.ADAL.spe_info", s.trim());
        }
    }
}
