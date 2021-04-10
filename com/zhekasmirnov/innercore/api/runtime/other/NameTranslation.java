package com.zhekasmirnov.innercore.api.runtime.other;

import java.util.*;
import org.mozilla.javascript.*;
import java.nio.charset.*;
import com.zhekasmirnov.innercore.utils.*;
import java.io.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.log.*;

public class NameTranslation
{
    private static HashMap<String, HashMap<Integer, String>> allLanguageTranslations;
    private static HashMap<Integer, String> currentLanguageTranslations;
    private static HashMap<Integer, String> defaultLanguageTranslations;
    private static String language;
    private static final HashMap<Integer, String> namesToGenerateCache;
    
    static {
        NameTranslation.allLanguageTranslations = new HashMap<String, HashMap<Integer, String>>();
        NameTranslation.currentLanguageTranslations = new HashMap<Integer, String>();
        NameTranslation.defaultLanguageTranslations = new HashMap<Integer, String>();
        NameTranslation.language = "en";
        namesToGenerateCache = new HashMap<Integer, String>();
    }
    
    public static void addSingleTranslation(final String s, final String s2, final String s3) {
        getTranslationMap(toShortName(s)).put(s2.hashCode(), s3);
    }
    
    public static void addTranslation(final String s, final HashMap<String, String> hashMap) {
        for (final String s2 : hashMap.keySet()) {
            addSingleTranslation(s2, s, hashMap.get(s2));
        }
    }
    
    public static void addTranslation(final String s, final ScriptableObject scriptableObject) {
        final Object[] allIds = scriptableObject.getAllIds();
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        for (int length = allIds.length, i = 0; i < length; ++i) {
            final Object o = allIds[i];
            if (o instanceof String) {
                final String s2 = (String)o;
                final StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(scriptableObject.get(o));
                hashMap.put(s2, sb.toString());
            }
        }
        addTranslation(s, hashMap);
    }
    
    public static String fixUnicodeIfRequired(String string, final String s) {
        if (s.length() == 0) {
            return "error.blank_name";
        }
        if (isAscii(s)) {
            return s;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(string);
        sb.append(".name");
        string = sb.toString();
        addSingleTranslation("en", string, s);
        return string;
    }
    
    public static String getLanguage() {
        return NameTranslation.language;
    }
    
    private static HashMap<Integer, String> getTranslationMap(final String s) {
        if (NameTranslation.allLanguageTranslations.containsKey(s)) {
            return NameTranslation.allLanguageTranslations.get(s);
        }
        final HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
        NameTranslation.allLanguageTranslations.put(s, hashMap);
        return hashMap;
    }
    
    public static boolean isAscii(final String s) {
        return StandardCharsets.US_ASCII.newEncoder().canEncode(s);
    }
    
    public static void refresh() {
        final StringBuilder sb = new StringBuilder();
        sb.append(FileTools.DIR_MINECRAFT);
        sb.append("minecraftpe/");
        final File file = new File(sb.toString(), "options.txt");
        try {
            final String[] split = FileTools.readFileText(file.getAbsolutePath()).split("\n");
            final int length = split.length;
            int i = 0;
            while (i < length) {
                final String[] split2 = split[i].split(":");
                if (split2[0].equals("game_language")) {
                    if (split2.length == 2) {
                        setLanguage(split2[1]);
                        return;
                    }
                    refreshFromNative();
                    return;
                }
                else {
                    ++i;
                }
            }
        }
        catch (Exception ex) {
            refreshFromNative();
        }
        while (true) {
            while (true) {
                Label_0202: {
                    synchronized (NameTranslation.namesToGenerateCache) {
                        final Iterator<Integer> iterator = NameTranslation.namesToGenerateCache.keySet().iterator();
                        if (!iterator.hasNext()) {
                            return;
                        }
                        final Integer n = iterator.next();
                        final String s = NameTranslation.namesToGenerateCache.get(n);
                        if (s != null) {
                            NativeAPI.sendCachedItemNameOverride(n / 16, n % 16, translate(s));
                            break Label_0202;
                        }
                        break Label_0202;
                    }
                }
                continue;
            }
        }
    }
    
    private static void refreshFromNative() {
        ICLog.d("TRANSLATION", "failed to get language settings from file, trying native method");
        final String gameLanguage = NativeAPI.getGameLanguage();
        if (gameLanguage != null) {
            setLanguage(gameLanguage);
            return;
        }
        ICLog.d("TRANSLATION", "failed to get language settings");
        setLanguage("en_US");
    }
    
    public static void sendNameToGenerateCache(final int n, int min, final String s) {
        synchronized (NameTranslation.namesToGenerateCache) {
            min = Math.min(15, Math.max(0, min));
            NameTranslation.namesToGenerateCache.put(n * 16 + min, s);
        }
    }
    
    public static void setLanguage(final String s) {
        final String shortName = toShortName(s);
        NameTranslation.currentLanguageTranslations = getTranslationMap(shortName);
        NameTranslation.defaultLanguageTranslations = getTranslationMap("en");
        final StringBuilder sb = new StringBuilder();
        sb.append("set game language to ");
        sb.append(shortName);
        sb.append(" (full name is ");
        sb.append(s);
        sb.append(")");
        ICLog.d("TRANSLATION", sb.toString());
    }
    
    private static String toShortName(final String s) {
        final int index = s.indexOf(95);
        if (index == -1) {
            return s;
        }
        return s.substring(0, index);
    }
    
    public static String translate(final String s) {
        if (s == null) {
            return null;
        }
        final String s2 = NameTranslation.currentLanguageTranslations.get(s.hashCode());
        if (s2 != null) {
            return s2;
        }
        final String s3 = NameTranslation.defaultLanguageTranslations.get(s.hashCode());
        if (s3 != null) {
            return s3;
        }
        return s;
    }
}
