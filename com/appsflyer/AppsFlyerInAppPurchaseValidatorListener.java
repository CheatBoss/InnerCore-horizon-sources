package com.appsflyer;

public interface AppsFlyerInAppPurchaseValidatorListener
{
    void onValidateInApp();
    
    void onValidateInAppFailure(final String p0);
}
