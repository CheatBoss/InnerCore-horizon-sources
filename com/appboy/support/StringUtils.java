package com.appboy.support;

import android.content.*;
import java.security.*;
import android.content.res.*;
import org.json.*;
import java.util.*;

public final class StringUtils
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(StringUtils.class);
    }
    
    private static String a(final String s, final String s2) {
        if (isNullOrBlank(s2)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(".");
            sb.append(s);
            return sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(".");
        sb2.append(s);
        sb2.append(".");
        sb2.append(s2);
        return sb2.toString();
    }
    
    public static String checkNotNullOrEmpty(final String s) {
        if (s == null) {
            throw new NullPointerException("Provided String must be non-null.");
        }
        if (s.length() != 0) {
            return s;
        }
        throw new IllegalArgumentException("Provided String must be non-empty.");
    }
    
    public static int countOccurrences(final String s, final String s2) {
        return s.split(s2, -1).length - 1;
    }
    
    public static String emptyToNull(final String s) {
        String s2 = s;
        if (s.trim().equals("")) {
            s2 = null;
        }
        return s2;
    }
    
    public static String getCacheFileSuffix(final Context context, final String s) {
        return getCacheFileSuffix(context, s, null);
    }
    
    public static String getCacheFileSuffix(final Context context, String md5Hash, final String s) {
        String s2 = md5Hash;
        if (md5Hash == null) {
            s2 = "null";
        }
        if (s2.equals("null")) {
            return a("37a6259cc0c1dae299a7866489dff0bd", s);
        }
        final SharedPreferences sharedPreferences = context.getSharedPreferences("com.appboy.support.stringutils.cachefilesuffix", 0);
        final String string = sharedPreferences.getString("user_id_key", (String)null);
        if (string != null && string.equals(s2)) {
            final String string2 = sharedPreferences.getString("user_id_hash_value", (String)null);
            if (!isNullOrEmpty(string2)) {
                return a(string2, s);
            }
            AppboyLogger.d(StringUtils.a, "The saved user id hash was null or empty.");
        }
        final String a = StringUtils.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Generating MD5 for user id: ");
        sb.append(s2);
        sb.append(" apiKey: ");
        sb.append(s);
        AppboyLogger.d(a, sb.toString());
        md5Hash = getMd5Hash(s2);
        String string3;
        if ((string3 = md5Hash) == null) {
            string3 = Integer.toString(s2.hashCode());
        }
        final SharedPreferences$Editor edit = sharedPreferences.edit();
        edit.putString("user_id_key", s2);
        edit.putString("user_id_hash_value", string3);
        edit.apply();
        return a(string3, s);
    }
    
    public static String getMd5Hash(String string) {
        try {
            final byte[] digest = MessageDigest.getInstance("MD5").digest(string.getBytes());
            final StringBuilder sb = new StringBuilder();
            for (int length = digest.length, i = 0; i < length; ++i) {
                sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1, 3));
            }
            string = sb.toString();
            return string;
        }
        catch (Exception ex) {
            AppboyLogger.e(StringUtils.a, "Failed to calculate MD5 hash", ex);
            return null;
        }
    }
    
    public static String getOptionalStringResource(final Resources resources, final int n, final String s) {
        try {
            return resources.getString(n);
        }
        catch (Resources$NotFoundException ex) {
            return s;
        }
    }
    
    public static boolean isNullOrBlank(final String s) {
        return s == null || s.trim().length() == 0;
    }
    
    public static boolean isNullOrEmpty(final String s) {
        return s == null || s.length() == 0;
    }
    
    public static String join(final Collection<String> collection, final String s) {
        if (collection == null) {
            return "";
        }
        return join(collection.toArray(new String[collection.size()]), s);
    }
    
    public static String join(final String[] array, final String s) {
        if (array != null && s != null) {
            final StringBuilder sb = new StringBuilder();
            for (int length = array.length, i = 0; i < length; ++i) {
                final String s2 = array[i];
                if (s2 != null) {
                    sb.append(s2);
                    sb.append(s);
                }
            }
            String s4;
            final String s3 = s4 = sb.toString();
            if (s3.endsWith(s)) {
                s4 = s3.substring(0, s3.length() - s.length());
            }
            return s4;
        }
        return "";
    }
    
    public static HashSet<String> jsonArrayToHashSet(final JSONArray jsonArray) {
        final HashSet<String> set = new HashSet<String>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); ++i) {
                set.add(jsonArray.get(i).toString());
            }
        }
        return set;
    }
    
    public static HashSet<String> stringArrayToHashSet(final String[] array) {
        return new HashSet<String>(Arrays.asList(array));
    }
    
    public static String stringArrayToJsonString(final String[] array) {
        final JSONArray jsonArray = new JSONArray();
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return "[]";
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            jsonArray.put((Object)array[i]);
        }
        return jsonArray.toString();
    }
}
