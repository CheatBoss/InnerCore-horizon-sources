package com.zhekasmirnov.horizon.util;

import android.content.res.*;
import org.json.*;
import java.util.*;

public class LocaleUtils
{
    public static final String LANG_EN = "en";
    public static final String LANG_RU = "ru";
    private static String defaultLanguage;
    
    public static Locale getLocale(final Resources resources) {
        return (resources != null) ? resources.getConfiguration().locale : Resources.getSystem().getConfiguration().locale;
    }
    
    public static void updateDefaultLanguage(final Resources resources) {
        final Locale locale = getLocale(resources);
        if (locale != null) {
            LocaleUtils.defaultLanguage = locale.getLanguage();
        }
    }
    
    public static String getLanguage(final Resources resources, final String defaultValue) {
        if (resources != null) {
            final Locale locale = getLocale(resources);
            return (locale != null) ? locale.getLanguage() : defaultValue;
        }
        return (LocaleUtils.defaultLanguage != null) ? LocaleUtils.defaultLanguage : defaultValue;
    }
    
    public static String getLanguage(final Resources resources) {
        return getLanguage(resources, "en");
    }
    
    public static String resolveLocaleJsonProperty(final Resources resources, final JSONObject json, final String propertyName) {
        final Object property = json.opt(propertyName);
        if (property instanceof JSONObject) {
            final String language = getLanguage(resources);
            final JSONObject localeJson = (JSONObject)property;
            String value = localeJson.optString(language);
            if (value == null) {
                value = localeJson.optString("en");
                if (value == null) {
                    for (final String key : new JsonIterator<String>(localeJson)) {
                        value = localeJson.optString(key);
                        if (value != null) {
                            return value;
                        }
                    }
                }
            }
            return value;
        }
        return json.optString(propertyName);
    }
    
    public static String resolveLocaleJsonProperty(final JSONObject json, final String propertyName) {
        return resolveLocaleJsonProperty(null, json, propertyName);
    }
    
    static {
        LocaleUtils.defaultLanguage = null;
    }
}
