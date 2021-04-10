package com.zhekasmirnov.innercore.modpack;

import java.io.*;
import java.util.*;
import com.zhekasmirnov.horizon.*;
import com.zhekasmirnov.innercore.api.log.*;
import android.app.*;
import android.content.*;

public class ModPackSelector
{
    private static final String PACK_SELECTED = "pack_selected";
    private static final String PREFERENCES_KEY;
    
    static {
        PREFERENCES_KEY = ModPackSelector.class.getName();
    }
    
    private static ModPack getPackByRoot(final String s) {
        for (final ModPack modPack : ModPackContext.getInstance().getStorage().getAllModPacks()) {
            if (modPack.getRootDirectory().equals(new File(s))) {
                return modPack;
            }
        }
        return null;
    }
    
    public static void restoreSelected() {
        final Activity topRunningActivity = HorizonApplication.getTopRunningActivity();
        final ModPackContext instance = ModPackContext.getInstance();
        final ModPackStorage storage = instance.getStorage();
        storage.rebuildModPackList();
        final String string = ((Context)topRunningActivity).getSharedPreferences(ModPackSelector.PREFERENCES_KEY, 0).getString("pack_selected", (String)null);
        if (string != null) {
            final ModPack packByRoot = getPackByRoot(string);
            if (packByRoot != null) {
                instance.setCurrentModPack(packByRoot);
            }
        }
        if (instance.getCurrentModPack() == null) {
            instance.setCurrentModPack(storage.getDefaultModPack());
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("selected modpack: ");
        sb.append(instance.getCurrentModPack().getRootDirectory());
        ICLog.d("ModPackSelector", sb.toString());
    }
    
    public static void setSelected(final ModPack currentModPack) {
        final Activity topRunningActivity = HorizonApplication.getTopRunningActivity();
        final ModPackContext instance = ModPackContext.getInstance();
        final SharedPreferences sharedPreferences = ((Context)topRunningActivity).getSharedPreferences(ModPackSelector.PREFERENCES_KEY, 0);
        instance.setCurrentModPack(currentModPack);
        sharedPreferences.edit().putString("pack_selected", currentModPack.getRootDirectory().getAbsolutePath()).apply();
    }
}
