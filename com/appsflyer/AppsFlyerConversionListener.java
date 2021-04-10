package com.appsflyer;

import java.util.*;

public interface AppsFlyerConversionListener
{
    void onAppOpenAttribution(final Map<String, String> p0);
    
    void onAttributionFailure(final String p0);
    
    void onConversionDataFail(final String p0);
    
    void onConversionDataSuccess(final Map<String, Object> p0);
}
