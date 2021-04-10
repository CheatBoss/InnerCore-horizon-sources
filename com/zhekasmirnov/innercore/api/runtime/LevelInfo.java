package com.zhekasmirnov.innercore.api.runtime;

import java.io.*;
import com.zhekasmirnov.innercore.utils.*;

public class LevelInfo
{
    public static final int STATE_IN_WORLD = 2;
    public static final int STATE_LOADING = 1;
    public static final int STATE_OFFLINE = 0;
    public static String levelDir;
    public static String levelName;
    public static int state;
    public static String worldsPathOverride;
    
    static {
        LevelInfo.worldsPathOverride = null;
        LevelInfo.state = 0;
    }
    
    public static String getAbsoluteDir() {
        if (LevelInfo.levelDir == null) {
            return null;
        }
        if (!isOnline()) {
            return null;
        }
        if (LevelInfo.worldsPathOverride != null) {
            String s2;
            final String s = s2 = new File(LevelInfo.worldsPathOverride, LevelInfo.levelDir).getAbsolutePath();
            if (!s.endsWith("/")) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append("/");
                s2 = sb.toString();
            }
            return s2;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(FileTools.DIR_ROOT);
        sb2.append("games/horizon/minecraftWorlds/");
        sb2.append(LevelInfo.levelDir);
        sb2.append("/");
        return sb2.toString();
    }
    
    public static String getLevelDir() {
        if (isOnline()) {
            return LevelInfo.levelDir;
        }
        return null;
    }
    
    public static String getLevelName() {
        if (isOnline()) {
            return LevelInfo.levelName;
        }
        return null;
    }
    
    public static int getState() {
        return LevelInfo.state;
    }
    
    public static boolean isLoaded() {
        return getState() == 2;
    }
    
    public static boolean isOnline() {
        return getState() != 0;
    }
    
    public static void onEnter(final String levelName, final String levelDir) {
        LevelInfo.state = 1;
        LevelInfo.levelName = levelName;
        LevelInfo.levelDir = levelDir;
    }
    
    public static void onLeft() {
        LevelInfo.state = 0;
    }
    
    public static void onLoaded() {
        LevelInfo.state = 2;
    }
}
