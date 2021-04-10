package com.zhekasmirnov.innercore.core;

import java.util.*;
import com.zhekasmirnov.innercore.utils.*;
import java.io.*;

public class AssetOverrideManager
{
    private static HashMap<String, String> pathOverrides;
    
    static {
        AssetOverrideManager.pathOverrides = new HashMap<String, String>();
    }
    
    public static void addOverride(final String s, final String s2) {
        AssetOverrideManager.pathOverrides.put(s, s2);
    }
    
    public static String getFileOverride(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(FileTools.DIR_MINECRAFT);
        sb.append("innercore/assets");
        String s2;
        if (s.startsWith("/")) {
            s2 = "";
        }
        else {
            s2 = "/";
        }
        sb.append(s2);
        sb.append(s);
        final String string = sb.toString();
        if (new File(string).exists()) {
            return string;
        }
        return s;
    }
    
    public static String getPathOverride(final String s) {
        if (AssetOverrideManager.pathOverrides.containsKey(s)) {
            return getFileOverride(AssetOverrideManager.pathOverrides.get(s));
        }
        return getFileOverride(s);
    }
}
