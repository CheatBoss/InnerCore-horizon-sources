package com.microsoft.aad.adal;

import android.accounts.*;
import android.content.*;
import java.net.*;
import java.io.*;
import android.util.*;
import java.security.*;
import android.content.pm.*;

class PackageHelper
{
    private static final String TAG = "CallerInfo";
    private final AccountManager mAcctManager;
    private Context mContext;
    
    public PackageHelper(final Context mContext) {
        this.mContext = mContext;
        this.mAcctManager = AccountManager.get(mContext);
    }
    
    public static String getBrokerRedirectUrl(String format, final String s) {
        if (!StringExtensions.isNullOrBlank(format) && !StringExtensions.isNullOrBlank(s)) {
            try {
                format = String.format("%s://%s/%s", "msauth", URLEncoder.encode(format, "UTF_8"), URLEncoder.encode(s, "UTF_8"));
                return format;
            }
            catch (UnsupportedEncodingException ex) {
                Logger.e("CallerInfo", ADALError.ENCODING_IS_NOT_SUPPORTED.getDescription(), "", ADALError.ENCODING_IS_NOT_SUPPORTED, ex);
            }
        }
        return "";
    }
    
    public String getCurrentSignatureForPackage(String encodeToString) {
        ADALError adalError;
        try {
            final PackageInfo packageInfo = this.mContext.getPackageManager().getPackageInfo(encodeToString, 64);
            if (packageInfo != null && packageInfo.signatures != null && packageInfo.signatures.length > 0) {
                final Signature signature = packageInfo.signatures[0];
                final MessageDigest instance = MessageDigest.getInstance("SHA");
                instance.update(signature.toByteArray());
                encodeToString = Base64.encodeToString(instance.digest(), 2);
                return encodeToString;
            }
            return null;
        }
        catch (NoSuchAlgorithmException ex) {
            encodeToString = "Digest SHA algorithm does not exists. ";
            adalError = ADALError.DEVICE_NO_SUCH_ALGORITHM;
        }
        catch (PackageManager$NameNotFoundException ex2) {
            encodeToString = "Calling App's package does not exist in PackageManager. ";
            adalError = ADALError.APP_PACKAGE_NAME_NOT_FOUND;
        }
        Logger.e("CallerInfo", encodeToString, "", adalError);
        return null;
    }
    
    public int getUIDForPackage(final String s) {
        try {
            final ApplicationInfo applicationInfo = this.mContext.getPackageManager().getApplicationInfo(s, 0);
            if (applicationInfo != null) {
                return applicationInfo.uid;
            }
        }
        catch (PackageManager$NameNotFoundException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Package name: ");
            sb.append(s);
            Logger.e("CallerInfo", "Package is not found. ", sb.toString(), ADALError.PACKAGE_NAME_NOT_FOUND, (Throwable)ex);
        }
        return 0;
    }
}
