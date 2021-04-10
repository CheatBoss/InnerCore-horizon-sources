package com.zhekasmirnov.innercore.api;

import com.zhekasmirnov.innercore.mod.build.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.log.*;
import org.json.*;
import java.io.*;

public class InnerCoreConfig
{
    public static Config config;
    
    static {
        InnerCoreConfig.config = new Config(getConfigFile());
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(FileTools.DIR_PACK);
            sb.append("assets/innercore/innercore_default_config.json");
            InnerCoreConfig.config.checkAndRestore(FileTools.readFileText(sb.toString()));
        }
        catch (JSONException | IOException ex) {
            final Throwable t;
            ICLog.e("CONFIG", "cannot load and validate default config.", t);
        }
    }
    
    public static int convertThreadPriority(final int n) {
        return 20 - Math.min(40, Math.max(1, n));
    }
    
    public static Object get(final String s) {
        return InnerCoreConfig.config.get(s);
    }
    
    public static boolean getBool(final String s) {
        final Object value = get(s);
        return value instanceof Boolean && (boolean)value;
    }
    
    public static File getConfigFile() {
        return new File(FileTools.DIR_WORK, "config.json");
    }
    
    public static int getInt(final String s) {
        return ((Number)InnerCoreConfig.config.get(s)).intValue();
    }
    
    public static int getInt(final String s, final int n) {
        try {
            return getInt(s);
        }
        catch (Exception ex) {
            return n;
        }
    }
    
    public static void set(final String s, final Object o) {
        InnerCoreConfig.config.set(s, o);
    }
}
