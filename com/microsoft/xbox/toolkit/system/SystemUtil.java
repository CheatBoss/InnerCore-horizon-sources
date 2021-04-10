package com.microsoft.xbox.toolkit.system;

import com.microsoft.xboxtcui.*;
import android.util.*;
import com.microsoft.xbox.toolkit.*;
import android.graphics.*;
import android.provider.*;
import android.view.*;
import java.net.*;
import java.util.*;
import android.os.*;

public class SystemUtil
{
    private static final int MAX_SD_SCREEN_PIXELS = 384000;
    
    public static int DIPtoPixels(final float n) {
        return (int)TypedValue.applyDimension(1, n, XboxTcuiSdk.getResources().getDisplayMetrics());
    }
    
    public static int SPtoPixels(final float n) {
        return (int)TypedValue.applyDimension(2, n, XboxTcuiSdk.getResources().getDisplayMetrics());
    }
    
    public static boolean TEST_randomFalseOutOf(final int n) {
        XLEAssert.assertTrue(false);
        return true;
    }
    
    public static void TEST_randomSleep(final int n) {
        XLEAssert.assertTrue(false);
    }
    
    public static int getColorDepth() {
        PixelFormat.getPixelFormatInfo(1, (PixelFormat)null);
        throw new NullPointerException();
    }
    
    public static String getDeviceId() {
        return Settings$Secure.getString(XboxTcuiSdk.getContentResolver(), "android_id");
    }
    
    public static String getDeviceModelName() {
        return Build.MODEL;
    }
    
    public static String getDeviceType() {
        XLEAssert.assertTrue(false);
        return "";
    }
    
    private static Display getDisplay() {
        return ((WindowManager)XboxTcuiSdk.getSystemService("window")).getDefaultDisplay();
    }
    
    public static String getMACAddress(String string) {
        try {
            for (final NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (string != null && !networkInterface.getName().equalsIgnoreCase(string)) {
                    continue;
                }
                final byte[] hardwareAddress = networkInterface.getHardwareAddress();
                if (hardwareAddress == null) {
                    return "";
                }
                final StringBuilder sb = new StringBuilder();
                for (int i = 0; i < hardwareAddress.length; ++i) {
                    sb.append(String.format("%02X:", hardwareAddress[i]));
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                string = sb.toString();
                return string;
            }
            return "";
        }
        catch (Exception ex) {
            return "";
        }
    }
    
    public static int getOrientation() {
        final int rotation = getRotation();
        if (rotation != 0 && rotation != 2) {
            return 2;
        }
        return 1;
    }
    
    public static int getRotation() {
        return getDisplay().getRotation();
    }
    
    public static int getScreenHeight() {
        return XboxTcuiSdk.getResources().getDisplayMetrics().heightPixels;
    }
    
    public static float getScreenHeightInches() {
        return getScreenHeight() / XboxTcuiSdk.getResources().getDisplayMetrics().ydpi;
    }
    
    public static int getScreenWidth() {
        return XboxTcuiSdk.getResources().getDisplayMetrics().widthPixels;
    }
    
    public static float getScreenWidthHeightAspectRatio() {
        final int screenWidth = getScreenWidth();
        final int screenHeight = getScreenHeight();
        if (screenWidth <= 0 || screenHeight <= 0) {
            return 0.0f;
        }
        if (screenWidth > screenHeight) {
            return screenWidth / (float)screenHeight;
        }
        return screenHeight / (float)screenWidth;
    }
    
    public static float getScreenWidthInches() {
        return getScreenWidth() / XboxTcuiSdk.getResources().getDisplayMetrics().xdpi;
    }
    
    public static int getSdkInt() {
        return Build$VERSION.SDK_INT;
    }
    
    public static float getYDPI() {
        return XboxTcuiSdk.getResources().getDisplayMetrics().ydpi;
    }
    
    public static boolean isHDScreen() {
        return getScreenHeight() * getScreenWidth() > 384000;
    }
    
    public static boolean isKindle() {
        final String manufacturer = Build.MANUFACTURER;
        return manufacturer != null && "AMAZON".compareToIgnoreCase(manufacturer) == 0;
    }
    
    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }
    
    public static boolean isSlate() {
        return Math.sqrt(Math.pow(getScreenWidthInches(), 2.0) + Math.pow(getScreenHeightInches(), 2.0)) > 6.0;
    }
}
