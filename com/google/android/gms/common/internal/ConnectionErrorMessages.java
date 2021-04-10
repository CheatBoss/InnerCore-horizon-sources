package com.google.android.gms.common.internal;

import android.support.v4.util.*;
import android.content.*;
import com.google.android.gms.common.wrappers.*;
import android.text.*;
import android.content.pm.*;
import com.google.android.gms.base.*;
import android.content.res.*;
import com.google.android.gms.common.util.*;
import android.util.*;
import com.google.android.gms.common.*;

public final class ConnectionErrorMessages
{
    private static final SimpleArrayMap<String, String> zzse;
    
    static {
        zzse = new SimpleArrayMap<String, String>();
    }
    
    public static String getAppName(final Context context) {
        final String packageName = context.getPackageName();
        try {
            return Wrappers.packageManager(context).getApplicationLabel(packageName).toString();
        }
        catch (PackageManager$NameNotFoundException | NullPointerException ex) {
            final String name = context.getApplicationInfo().name;
            if (TextUtils.isEmpty((CharSequence)name)) {
                return packageName;
            }
            return name;
        }
    }
    
    public static String getDefaultNotificationChannelName(final Context context) {
        return context.getResources().getString(R$string.common_google_play_services_notification_channel_name);
    }
    
    public static String getErrorDialogButtonMessage(final Context context, int n) {
        final Resources resources = context.getResources();
        if (n != 1) {
            if (n != 2) {
                if (n != 3) {
                    n = 17039370;
                }
                else {
                    n = R$string.common_google_play_services_enable_button;
                }
            }
            else {
                n = R$string.common_google_play_services_update_button;
            }
        }
        else {
            n = R$string.common_google_play_services_install_button;
        }
        return resources.getString(n);
    }
    
    public static String getErrorMessage(final Context context, final int n) {
        final Resources resources = context.getResources();
        final String appName = getAppName(context);
        if (n == 1) {
            return resources.getString(R$string.common_google_play_services_install_text, new Object[] { appName });
        }
        if (n != 2) {
            if (n == 3) {
                return resources.getString(R$string.common_google_play_services_enable_text, new Object[] { appName });
            }
            if (n == 5) {
                return zza(context, "common_google_play_services_invalid_account_text", appName);
            }
            if (n == 7) {
                return zza(context, "common_google_play_services_network_error_text", appName);
            }
            if (n == 9) {
                return resources.getString(R$string.common_google_play_services_unsupported_text, new Object[] { appName });
            }
            if (n == 20) {
                return zza(context, "common_google_play_services_restricted_profile_text", appName);
            }
            switch (n) {
                default: {
                    return resources.getString(com.google.android.gms.common.R$string.common_google_play_services_unknown_issue, new Object[] { appName });
                }
                case 18: {
                    return resources.getString(R$string.common_google_play_services_updating_text, new Object[] { appName });
                }
                case 17: {
                    return zza(context, "common_google_play_services_sign_in_failed_text", appName);
                }
                case 16: {
                    return zza(context, "common_google_play_services_api_unavailable_text", appName);
                }
            }
        }
        else {
            if (DeviceProperties.isWearableWithoutPlayStore(context)) {
                return resources.getString(R$string.common_google_play_services_wear_update_text);
            }
            return resources.getString(R$string.common_google_play_services_update_text, new Object[] { appName });
        }
    }
    
    public static String getErrorNotificationMessage(final Context context, final int n) {
        if (n == 6) {
            return zza(context, "common_google_play_services_resolution_required_text", getAppName(context));
        }
        return getErrorMessage(context, n);
    }
    
    public static String getErrorNotificationTitle(final Context context, final int n) {
        String s;
        if (n == 6) {
            s = zzb(context, "common_google_play_services_resolution_required_title");
        }
        else {
            s = getErrorTitle(context, n);
        }
        String string = s;
        if (s == null) {
            string = context.getResources().getString(R$string.common_google_play_services_notification_ticker);
        }
        return string;
    }
    
    public static String getErrorTitle(final Context context, final int n) {
        final Resources resources = context.getResources();
        if (n != 20) {
            String string = null;
            Label_0173: {
                switch (n) {
                    default: {
                        switch (n) {
                            default: {
                                final StringBuilder sb = new StringBuilder(33);
                                sb.append("Unexpected error code ");
                                sb.append(n);
                                string = sb.toString();
                                break Label_0173;
                            }
                            case 17: {
                                Log.e("GoogleApiAvailability", "The specified account could not be signed in.");
                                return zzb(context, "common_google_play_services_sign_in_failed_title");
                            }
                            case 16: {
                                string = "One of the API components you attempted to connect to is not available.";
                                break Label_0173;
                            }
                            case 18: {
                                return null;
                            }
                        }
                        break;
                    }
                    case 11: {
                        string = "The application is not licensed to the user.";
                        break;
                    }
                    case 10: {
                        string = "Developer error occurred. Please see logs for detailed information";
                        break;
                    }
                    case 9: {
                        string = "Google Play services is invalid. Cannot recover.";
                        break;
                    }
                    case 8: {
                        string = "Internal error occurred. Please see logs for detailed information";
                        break;
                    }
                    case 7: {
                        Log.e("GoogleApiAvailability", "Network error occurred. Please retry request later.");
                        return zzb(context, "common_google_play_services_network_error_title");
                    }
                    case 5: {
                        Log.e("GoogleApiAvailability", "An invalid account was specified when connecting. Please provide a valid account.");
                        return zzb(context, "common_google_play_services_invalid_account_title");
                    }
                    case 4:
                    case 6: {
                        return null;
                    }
                    case 3: {
                        return resources.getString(R$string.common_google_play_services_enable_title);
                    }
                    case 2: {
                        return resources.getString(R$string.common_google_play_services_update_title);
                    }
                    case 1: {
                        return resources.getString(R$string.common_google_play_services_install_title);
                    }
                }
            }
            Log.e("GoogleApiAvailability", string);
            return null;
        }
        Log.e("GoogleApiAvailability", "The current user profile is restricted and could not use authenticated features.");
        return zzb(context, "common_google_play_services_restricted_profile_title");
    }
    
    private static String zza(final Context context, String s, final String s2) {
        final Resources resources = context.getResources();
        String s3;
        s = (s3 = zzb(context, s));
        if (s == null) {
            s3 = resources.getString(com.google.android.gms.common.R$string.common_google_play_services_unknown_issue);
        }
        return String.format(resources.getConfiguration().locale, s3, s2);
    }
    
    private static String zzb(final Context context, final String s) {
        synchronized (ConnectionErrorMessages.zzse) {
            final String s2 = ConnectionErrorMessages.zzse.get(s);
            if (s2 != null) {
                return s2;
            }
            final Resources remoteResource = GooglePlayServicesUtil.getRemoteResource(context);
            if (remoteResource == null) {
                return null;
            }
            final int identifier = remoteResource.getIdentifier(s, "string", "com.google.android.gms");
            if (identifier == 0) {
                final String value = String.valueOf(s);
                String concat;
                if (value.length() != 0) {
                    concat = "Missing resource: ".concat(value);
                }
                else {
                    concat = new String("Missing resource: ");
                }
                Log.w("GoogleApiAvailability", concat);
                return null;
            }
            final String string = remoteResource.getString(identifier);
            if (TextUtils.isEmpty((CharSequence)string)) {
                final String value2 = String.valueOf(s);
                String concat2;
                if (value2.length() != 0) {
                    concat2 = "Got empty resource: ".concat(value2);
                }
                else {
                    concat2 = new String("Got empty resource: ");
                }
                Log.w("GoogleApiAvailability", concat2);
                return null;
            }
            ConnectionErrorMessages.zzse.put(s, string);
            return string;
        }
    }
}
