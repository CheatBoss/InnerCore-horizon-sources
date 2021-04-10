package com.zhekasmirnov.apparatus.adapter.minecraft.version;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.log.*;
import org.json.*;
import com.zhekasmirnov.apparatus.util.*;
import java.util.function.*;
import java.util.*;
import com.zhekasmirnov.apparatus.cpp.*;

@SynthesizedClassMap({ -$$Lambda$VanillaIdConversionMap$RBg4s-KfKPo7590a-CeEywU4Z98.class })
public class VanillaIdConversionMap
{
    private static final VanillaIdConversionMap singleton;
    
    static {
        singleton = new VanillaIdConversionMap();
    }
    
    private VanillaIdConversionMap() {
    }
    
    public static VanillaIdConversionMap getSingleton() {
        return VanillaIdConversionMap.singleton;
    }
    
    private boolean tryLoadFromAsset(final String s, final Map<String, Map<String, Integer>> map, final boolean b) {
        try {
            this.loadJsonIntoMap(FileTools.getAssetAsJSON(s), map, b);
            final StringBuilder sb = new StringBuilder();
            sb.append("loaded ids from asset ");
            sb.append(s);
            ICLog.d("VanillaIdConversionMap", sb.toString());
            return true;
        }
        catch (JSONException | NullPointerException ex) {
            return false;
        }
    }
    
    public void loadJsonIntoMap(final JSONObject jsonObject, final Map<String, Map<String, Integer>> map, final boolean b) {
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String s = keys.next();
            final JSONObject optJSONObject = jsonObject.optJSONObject(s);
            if (optJSONObject != null) {
                final Map<String, Integer> map2 = Java8BackComp.computeIfAbsent(map, s, -$$Lambda$VanillaIdConversionMap$RBg4s-KfKPo7590a-CeEywU4Z98.INSTANCE);
                final Iterator keys2 = optJSONObject.keys();
                while (keys2.hasNext()) {
                    final String s2 = keys2.next();
                    final int optInt = optJSONObject.optInt(s2);
                    if (optInt != 0 && (b || !map2.containsKey(s2))) {
                        map2.put(s2, optInt);
                    }
                }
            }
        }
    }
    
    public Map<String, Map<String, Integer>> loadScopedIdMapFromAssets() {
        final HashMap<String, Map<String, Integer>> hashMap = new HashMap<String, Map<String, Integer>>();
        this.tryLoadFromAsset("innercore/id/numeric_ids.json", hashMap, false);
        int n = 0;
        while (true) {
            final StringBuilder sb = new StringBuilder();
            sb.append("innercore/id/numeric_ids_override_");
            sb.append(n);
            sb.append(".json");
            if (!this.tryLoadFromAsset(sb.toString(), hashMap, false)) {
                break;
            }
            ++n;
        }
        this.tryLoadFromAsset("innercore/numeric_ids.json", hashMap, false);
        return hashMap;
    }
    
    public void reloadFrom(final Map<String, Map<String, Integer>> map) {
    Label_0191_Outer:
        while (true) {
        Label_0206_Outer:
            while (true) {
            Label_0206:
                while (true) {
                    while (true) {
                        int n = 0;
                        Label_0220: {
                            synchronized (this) {
                                if (!MinecraftVersions.getCurrent().isVanillaIdRemappingRequired()) {
                                    ICLog.d("VanillaIdConversionMap", "vanilla id remapping is not required on this version of the game");
                                    return;
                                }
                                while (true) {
                                    for (Object iterator2 : map.entrySet()) {
                                        final String s = ((Map.Entry<String, Map<String, Map>>)iterator2).getKey();
                                        iterator2 = ((Map.Entry<String, Map<String, Map>>)iterator2).getValue().entrySet().iterator();
                                        if (((Iterator)iterator2).hasNext()) {
                                            final Map.Entry<String, Map> entry = ((Iterator<Map.Entry<String, Map>>)iterator2).next();
                                            final String s2 = entry.getKey();
                                            final int intValue = (int)entry.getValue();
                                            n = -1;
                                            final int hashCode = s.hashCode();
                                            if (hashCode != -1386164858) {
                                                if (hashCode != 100526016) {
                                                    break Label_0220;
                                                }
                                                if (s.equals("items")) {
                                                    n = 1;
                                                }
                                                break Label_0220;
                                            }
                                            else {
                                                if (s.equals("blocks")) {
                                                    n = 0;
                                                }
                                                break Label_0220;
                                            }
                                        }
                                    }
                                    return;
                                    String s2 = null;
                                    int intValue = 0;
                                    NativeVanillaIdConversionMap.addItemId(s2, intValue);
                                    break Label_0206;
                                    NativeVanillaIdConversionMap.addBlockId(s2, intValue);
                                    continue Label_0191_Outer;
                                }
                            }
                        }
                        switch (n) {
                            case 1: {
                                continue Label_0206_Outer;
                            }
                            case 0: {
                                continue;
                            }
                            default: {
                                continue Label_0206;
                            }
                        }
                        break;
                    }
                    break;
                }
                break;
            }
        }
    }
    
    public void reloadFromAssets() {
        this.reloadFrom(this.loadScopedIdMapFromAssets());
    }
}
