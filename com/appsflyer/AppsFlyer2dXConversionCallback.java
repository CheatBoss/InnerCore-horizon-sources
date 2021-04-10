package com.appsflyer;

import org.json.*;
import java.util.*;

public class AppsFlyer2dXConversionCallback implements AppsFlyerConversionListener
{
    private void \u0399(final String s, final String s2) {
        Block_0: {
            break Block_0;
        Block_1:
            while (true) {
                int i = 0;
                do {
                    while (true) {
                        Label_0081: {
                            break Label_0081;
                            try {
                                final JSONObject jsonObject = new JSONObject();
                                jsonObject.put("status", (Object)"failure");
                                jsonObject.put("data", (Object)s2);
                                i = -1;
                                final int hashCode = s.hashCode();
                                if (hashCode != -1390007222) {
                                    if (hashCode != 1050716216) {
                                        break Label_0081;
                                    }
                                    if (s.equals("onInstallConversionFailure")) {
                                        i = 0;
                                    }
                                    break Label_0081;
                                }
                                else {
                                    if (s.equals("onAttributionFailure")) {
                                        i = 1;
                                    }
                                    break Label_0081;
                                }
                                this.onAttributionFailureNative(jsonObject);
                                return;
                                this.onInstallConversionFailureNative(jsonObject);
                                return;
                            }
                            catch (JSONException ex) {
                                ((Throwable)ex).printStackTrace();
                                return;
                            }
                        }
                        if (i != 0) {
                            continue Block_1;
                        }
                        continue;
                    }
                } while (i == 1);
                break;
            }
        }
    }
    
    @Override
    public void onAppOpenAttribution(final Map<String, String> map) {
        this.onAppOpenAttributionNative(map);
    }
    
    public native void onAppOpenAttributionNative(final Object p0);
    
    @Override
    public void onAttributionFailure(final String s) {
        this.\u0399("onInstallConversionFailure", s);
    }
    
    public native void onAttributionFailureNative(final Object p0);
    
    @Override
    public void onConversionDataFail(final String s) {
        this.\u0399("onAttributionFailure", s);
    }
    
    @Override
    public void onConversionDataSuccess(final Map<String, Object> map) {
        this.onInstallConversionDataLoadedNative(map);
    }
    
    public native void onInstallConversionDataLoadedNative(final Object p0);
    
    public native void onInstallConversionFailureNative(final Object p0);
}
