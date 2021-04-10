package com.appsflyer;

import java.util.*;

public interface ConversionDataListener
{
    void onConversionDataLoaded(final Map<String, Object> p0);
    
    void onConversionFailure(final String p0);
}
