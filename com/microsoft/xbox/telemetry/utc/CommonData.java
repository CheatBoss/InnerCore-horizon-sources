package com.microsoft.xbox.telemetry.utc;

import android.view.accessibility.*;
import android.accessibilityservice.*;
import com.microsoft.xbox.telemetry.helpers.*;
import android.content.*;
import java.util.*;
import android.os.*;
import android.net.*;
import com.microsoft.xbox.idp.interop.*;
import com.google.gson.*;

public class CommonData
{
    private static final String DEFAULTSANDBOX = "RETAIL";
    private static final String DEFAULTSERVICES = "none";
    private static final String EVENTVERSION = "1.1";
    private static final String UNKNOWNAPP = "UNKNOWN";
    private static final String UNKNOWNUSER = "0";
    private static UUID applicationSession;
    private static NetworkType netType;
    private static String staticAccessibilityInfo;
    private static String staticAppName;
    private static String staticDeviceModel;
    private static String staticOSLocale;
    private String accessibilityInfo;
    public HashMap<String, Object> additionalInfo;
    public String appName;
    public String appSessionId;
    public String clientLanguage;
    public String deviceModel;
    public String eventVersion;
    public int network;
    public String sandboxId;
    public String titleDeviceId;
    public String titleSessionId;
    public String userId;
    public String xsapiVersion;
    
    static {
        CommonData.netType = getNetworkConnection();
        CommonData.staticDeviceModel = getDeviceModel();
        CommonData.staticAppName = getAppName();
        CommonData.staticOSLocale = getDeviceLocale();
        CommonData.staticAccessibilityInfo = getAccessibilityInfo();
        CommonData.applicationSession = UUID.randomUUID();
    }
    
    public CommonData(final int n) {
        this.deviceModel = CommonData.staticDeviceModel;
        this.xsapiVersion = "1.0";
        this.appName = CommonData.staticAppName;
        this.clientLanguage = CommonData.staticOSLocale;
        this.network = CommonData.netType.getValue();
        this.sandboxId = getSandboxId();
        this.appSessionId = getApplicationSession();
        this.userId = "0";
        this.titleDeviceId = get_title_telemetry_device_id();
        this.titleSessionId = get_title_telemetry_session_id();
        this.additionalInfo = new HashMap<String, Object>();
        this.accessibilityInfo = CommonData.staticAccessibilityInfo;
        this.eventVersion = String.format("%s.%s", "1.1", n);
    }
    
    private static String getAccessibilityInfo() {
        try {
            final Context applicationContext = Interop.getApplicationContext();
            if (applicationContext != null) {
                final AccessibilityManager accessibilityManager = (AccessibilityManager)applicationContext.getSystemService("accessibility");
                final HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();
                hashMap.put("isenabled", accessibilityManager.isEnabled());
                final Iterator iterator = accessibilityManager.getEnabledAccessibilityServiceList(-1).iterator();
                String s = "none";
                while (iterator.hasNext()) {
                    final AccessibilityServiceInfo accessibilityServiceInfo = iterator.next();
                    if (s.equals("none")) {
                        s = accessibilityServiceInfo.getId();
                    }
                    else {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(s);
                        sb.append(String.format(";%s", accessibilityServiceInfo.getId()));
                        s = sb.toString();
                    }
                }
                hashMap.put("enabledservices", (Boolean)s);
                return new GsonBuilder().serializeNulls().create().toJson((Object)hashMap);
            }
        }
        catch (Exception ex) {
            UTCLog.log(ex.getMessage(), new Object[0]);
        }
        return "";
    }
    
    private static String getAppName() {
        try {
            final Context applicationContext = Interop.getApplicationContext();
            if (applicationContext != null) {
                return applicationContext.getApplicationInfo().packageName;
            }
        }
        catch (Exception ex) {
            UTCLog.log(ex.getMessage(), new Object[0]);
        }
        return "UNKNOWN";
    }
    
    public static String getApplicationSession() {
        return CommonData.applicationSession.toString();
    }
    
    private static String getDeviceLocale() {
        try {
            final Locale default1 = Locale.getDefault();
            return String.format("%s-%s", default1.getLanguage(), default1.getCountry());
        }
        catch (Exception ex) {
            UTCLog.log(ex.getMessage(), new Object[0]);
            return null;
        }
    }
    
    private static String getDeviceModel() {
        final String model = Build.MODEL;
        if (model != null && !model.isEmpty()) {
            return model.replace("|", "");
        }
        return "UNKNOWN";
    }
    
    private static NetworkType getNetworkConnection() {
        if (CommonData.netType == NetworkType.UNKNOWN && Interop.getApplicationContext() != null) {
            try {
                final NetworkInfo activeNetworkInfo = ((ConnectivityManager)Interop.getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
                if (activeNetworkInfo == null) {
                    return CommonData.netType;
                }
                if (activeNetworkInfo.getState() == NetworkInfo$State.CONNECTED) {
                    final int type = activeNetworkInfo.getType();
                    while (true) {
                        Label_0100: {
                            if (type == 0) {
                                break Label_0100;
                            }
                            NetworkType netType;
                            if (type != 1) {
                                if (type == 6) {
                                    break Label_0100;
                                }
                                if (type != 9) {
                                    netType = NetworkType.UNKNOWN;
                                }
                                else {
                                    netType = NetworkType.WIRED;
                                }
                            }
                            else {
                                netType = NetworkType.WIFI;
                            }
                            CommonData.netType = netType;
                            return CommonData.netType;
                        }
                        NetworkType netType = NetworkType.CELLULAR;
                        continue;
                    }
                }
            }
            catch (Exception ex) {
                UTCLog.log(ex.getMessage(), new Object[0]);
                CommonData.netType = NetworkType.UNKNOWN;
            }
        }
        return CommonData.netType;
    }
    
    private static String getSandboxId() {
        try {
            return new XboxLiveAppConfig().getSandbox();
        }
        catch (Exception ex) {
            UTCLog.log(ex.getMessage(), new Object[0]);
            return "RETAIL";
        }
    }
    
    private static native String get_title_telemetry_device_id();
    
    private static native String get_title_telemetry_session_id();
    
    public String GetAdditionalInfoString() {
        try {
            return new GsonBuilder().serializeNulls().create().toJson((Object)this.additionalInfo);
        }
        catch (JsonIOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Error in json serialization");
            sb.append(ex.toString());
            UTCLog.log("UTCJsonSerializer", sb.toString());
            return null;
        }
    }
    
    public String ToJson() {
        try {
            return new GsonBuilder().serializeNulls().create().toJson((Object)this);
        }
        catch (JsonIOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Error in json serialization");
            sb.append(ex.toString());
            UTCLog.log("UTCJsonSerializer", sb.toString());
            return "";
        }
    }
    
    private enum NetworkType
    {
        CELLULAR(2), 
        UNKNOWN(0), 
        WIFI(1), 
        WIRED(3);
        
        private int value;
        
        private NetworkType(final int value) {
            this.value = 0;
            this.setValue(value);
        }
        
        public int getValue() {
            return this.value;
        }
        
        public void setValue(final int value) {
            this.value = value;
        }
    }
}
