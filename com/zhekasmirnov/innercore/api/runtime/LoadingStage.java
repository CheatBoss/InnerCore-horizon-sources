package com.zhekasmirnov.innercore.api.runtime;

import java.util.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.horizon.runtime.logger.*;

public class LoadingStage
{
    public static final int STAGE_COMPLETE = 7;
    public static final int STAGE_COUNT = 8;
    public static final int STAGE_FINAL_LOADING = 6;
    public static final int STAGE_IDLE = 0;
    public static final int STAGE_MCPE_INITIALIZING = 5;
    public static final int STAGE_MCPE_STARTING = 4;
    public static final int STAGE_MODS_PRELOAD = 3;
    public static final int STAGE_RESOURCES_LOADING = 2;
    public static final int STAGE_START = 1;
    private static int stage;
    private static final long startupTime;
    private static HashMap<Integer, Long> timeMap;
    
    static {
        LoadingStage.stage = 0;
        LoadingStage.timeMap = new HashMap<Integer, Long>();
        startupTime = System.currentTimeMillis();
    }
    
    public static int getStage() {
        return LoadingStage.stage;
    }
    
    public static long getStageBeginTime(final int n) {
        if (LoadingStage.timeMap.containsKey(n)) {
            return LoadingStage.timeMap.get(n);
        }
        return -1L;
    }
    
    public static boolean isInOrPassed(final int n) {
        return LoadingStage.stage >= n;
    }
    
    public static boolean isPassed(final int n) {
        return LoadingStage.stage > n;
    }
    
    public static void outputTimeMap() {
        ICLog.d("PROFILING", "showing startup time map");
        for (int i = 0; i < 8; ++i) {
            final long stageBeginTime = getStageBeginTime(i);
            if (stageBeginTime != -1L) {
                final StringBuilder sb = new StringBuilder();
                sb.append("stage ");
                sb.append(stageToString(i));
                sb.append(" started at ");
                sb.append((stageBeginTime - LoadingStage.startupTime) * 0.001);
                sb.append("s");
                Logger.debug("PROFILING", sb.toString());
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("stage ");
                sb2.append(stageToString(i));
                sb2.append(" was ignored");
                Logger.debug("PROFILING", sb2.toString());
            }
        }
    }
    
    public static void setStage(final int stage) {
        LoadingStage.stage = stage;
        LoadingStage.timeMap.put(stage, System.currentTimeMillis());
        final StringBuilder sb = new StringBuilder();
        sb.append("switched into new loading stage: stage=");
        sb.append(stageToString(stage));
        ICLog.i("PROFILING", sb.toString());
    }
    
    public static String stageToString(final int n) {
        switch (n) {
            default: {
                return "STAGE_UNKNOWN";
            }
            case 7: {
                return "STAGE_COMPLETE";
            }
            case 6: {
                return "STAGE_FINAL_LOADING";
            }
            case 5: {
                return "STAGE_MCPE_INITIALIZING";
            }
            case 4: {
                return "STAGE_MCPE_STARTING";
            }
            case 3: {
                return "STAGE_MODS_PRELOAD";
            }
            case 2: {
                return "STAGE_RESOURCES_LOADING";
            }
            case 1: {
                return "STAGE_START";
            }
            case 0: {
                return "STAGE_IDLE";
            }
        }
    }
}
