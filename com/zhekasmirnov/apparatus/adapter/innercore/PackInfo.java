package com.zhekasmirnov.apparatus.adapter.innercore;

import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import org.json.*;

public class PackInfo
{
    private static boolean isInitialized;
    private static int packBuildVersionCode;
    private static String packBuildVersionName;
    private static String packName;
    private static File packRootDirectory;
    
    static {
        PackInfo.isInitialized = false;
        PackInfo.packName = "Inner Core";
        PackInfo.packBuildVersionName = "unknown version";
        PackInfo.packBuildVersionCode = 0;
    }
    
    public static String getNetworkPackIdentifier() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getPackName());
        sb.append("#");
        sb.append(getPackVersionName());
        return sb.toString();
    }
    
    public static String getPackName() {
        initializeIfRequired();
        return PackInfo.packName;
    }
    
    public static int getPackVersionCode() {
        initializeIfRequired();
        return PackInfo.packBuildVersionCode;
    }
    
    public static String getPackVersionName() {
        initializeIfRequired();
        return PackInfo.packBuildVersionName;
    }
    
    private static void initializeIfRequired() {
        synchronized (PackInfo.class) {
            if (!PackInfo.isInitialized) {
                PackInfo.packRootDirectory = new File(FileTools.DIR_PACK);
                try {
                    final JSONObject json = FileUtils.readJSON(new File(PackInfo.packRootDirectory, "manifest.json"));
                    final StringBuilder sb = new StringBuilder();
                    sb.append(json.optString("pack", PackInfo.packName));
                    sb.append("");
                    PackInfo.packName = sb.toString();
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(json.optString("packVersion", PackInfo.packBuildVersionName));
                    sb2.append("");
                    PackInfo.packBuildVersionName = sb2.toString();
                    PackInfo.packBuildVersionCode = json.optInt("packVersionCode", PackInfo.packBuildVersionCode);
                }
                catch (IOException | JSONException ex) {
                    final Throwable t;
                    t.printStackTrace();
                }
                PackInfo.isInitialized = true;
            }
        }
    }
}
