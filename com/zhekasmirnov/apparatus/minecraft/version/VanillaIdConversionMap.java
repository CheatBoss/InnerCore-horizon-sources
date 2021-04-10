package com.zhekasmirnov.apparatus.minecraft.version;

import com.android.tools.r8.annotations.*;
import java.util.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.log.*;
import org.json.*;
import com.zhekasmirnov.apparatus.util.*;
import java.util.function.*;
import com.zhekasmirnov.apparatus.cpp.*;

@SynthesizedClassMap({ -$$Lambda$VanillaIdConversionMap$-nVjPDSo0oHtc8aR6oRcKKe7Bf8.class })
public class VanillaIdConversionMap
{
    private static final VanillaIdConversionMap singleton;
    
    static {
        singleton = new VanillaIdConversionMap();
    }
    
    private VanillaIdConversionMap() {
    }
    
    private void fixMissingItemIds(final Map<String, Map<String, Integer>> map) {
        final Map<String, Integer> map2 = map.get("blocks");
        final Map<String, Integer> map3 = map.get("items");
        if (map2 == null) {
            return;
        }
        if (map3 == null) {
            return;
        }
        for (final Map.Entry<String, Integer> entry : map2.entrySet()) {
            int intValue = entry.getValue();
            final String s = entry.getKey();
            final Integer n = map3.get(s);
            if (intValue > 255) {
                intValue = 255 - intValue;
            }
            if ((n == null || n != intValue) && !map3.containsValue(intValue) && !map3.containsKey(s)) {
                map3.put(s, intValue);
            }
        }
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
                final Map<String, Integer> map2 = Java8BackComp.computeIfAbsent(map, s, -$$Lambda$VanillaIdConversionMap$-nVjPDSo0oHtc8aR6oRcKKe7Bf8.INSTANCE);
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
        this.fixMissingItemIds(hashMap);
        return hashMap;
    }
    
    public void reloadFrom(final Map<String, Map<String, Integer>> map) {
    Label_0202_Outer:
        while (true) {
        Label_0208_Outer:
            while (true) {
            Label_0193_Outer:
                while (true) {
                    while (true) {
                        int n = 0;
                        Label_0222: {
                            synchronized (this) {
                                if (!MinecraftVersions.getCurrent().isFeatureSupported("vanilla_id_mapping")) {
                                    ICLog.d("VanillaIdConversionMap", "vanilla id remapping is not required on this version of the game");
                                    return;
                                }
                                String s2;
                                int intValue;
                                while (true) {
                                    for (Object iterator2 : map.entrySet()) {
                                        final String s = ((Map.Entry<String, Map<String, Map>>)iterator2).getKey();
                                        iterator2 = ((Map.Entry<String, Map<String, Map>>)iterator2).getValue().entrySet().iterator();
                                        if (((Iterator)iterator2).hasNext()) {
                                            final Map.Entry<String, Map> entry = ((Iterator<Map.Entry<String, Map>>)iterator2).next();
                                            s2 = entry.getKey();
                                            intValue = (int)entry.getValue();
                                            n = -1;
                                            final int hashCode = s.hashCode();
                                            if (hashCode != -1386164858) {
                                                if (hashCode != 100526016) {
                                                    break Label_0222;
                                                }
                                                if (s.equals("items")) {
                                                    n = 1;
                                                }
                                                break Label_0222;
                                            }
                                            else {
                                                if (s.equals("blocks")) {
                                                    n = 0;
                                                }
                                                break Label_0222;
                                            }
                                        }
                                    }
                                    return;
                                    NativeVanillaIdConversionMap.addBlockId(s2, intValue);
                                    continue Label_0202_Outer;
                                }
                                NativeVanillaIdConversionMap.addItemId(s2, intValue);
                                continue Label_0193_Outer;
                            }
                        }
                        switch (n) {
                            case 1: {
                                continue;
                            }
                            case 0: {
                                continue Label_0208_Outer;
                            }
                            default: {
                                continue Label_0193_Outer;
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
