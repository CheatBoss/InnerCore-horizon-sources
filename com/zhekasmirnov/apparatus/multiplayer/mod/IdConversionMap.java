package com.zhekasmirnov.apparatus.multiplayer.mod;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.multiplayer.*;
import com.zhekasmirnov.apparatus.multiplayer.client.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;
import org.json.*;
import com.zhekasmirnov.apparatus.util.*;
import java.util.*;
import com.zhekasmirnov.apparatus.cpp.*;

@SynthesizedClassMap({ -$$Lambda$IdConversionMap$In_m1qbDnWPqWezNfBdFo4aFzyI.class, -$$Lambda$IdConversionMap$4KqMILyHO5uVSgxbCkFsMmAB83g.class })
public class IdConversionMap
{
    private static final IdConversionMap singleton;
    private final Map<Integer, Integer> fromLocalToServerMap;
    private final Map<Integer, Integer> fromServerToLocalMap;
    private final Map<String, Integer> localIdMap;
    
    static {
        Network.getSingleton().addServerInitializationPacket("system.id_map", (Network.ServerInitializationPacketSender)-$$Lambda$IdConversionMap$4KqMILyHO5uVSgxbCkFsMmAB83g.INSTANCE, (ModdedClient.TypedOnPacketReceivedListener<Object>)-$$Lambda$IdConversionMap$In_m1qbDnWPqWezNfBdFo4aFzyI.INSTANCE);
        singleton = new IdConversionMap();
    }
    
    public IdConversionMap() {
        this.localIdMap = new HashMap<String, Integer>();
        this.fromLocalToServerMap = new HashMap<Integer, Integer>();
        this.fromServerToLocalMap = new HashMap<Integer, Integer>();
    }
    
    public static IdConversionMap getSingleton() {
        return IdConversionMap.singleton;
    }
    
    public static void loadClass() {
    }
    
    private JSONObject localMapAsJson() {
        return new JSONObject((Map)this.localIdMap);
    }
    
    public static int localToServer(final int n) {
        return Java8BackComp.getOrDefault(getSingleton().fromLocalToServerMap, n, n);
    }
    
    public static int serverToLocal(final int n) {
        return Java8BackComp.getOrDefault(getSingleton().fromServerToLocalMap, n, n);
    }
    
    private void updateConversionMap(final JSONObject jsonObject) {
        this.fromLocalToServerMap.clear();
        this.fromServerToLocalMap.clear();
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String s = keys.next();
            final int optInt = jsonObject.optInt(s);
            if (optInt != 0 && this.localIdMap.containsKey(s)) {
                final int intValue = this.localIdMap.get(s);
                this.fromServerToLocalMap.put(optInt, intValue);
                this.fromLocalToServerMap.put(intValue, optInt);
            }
        }
        this.rebuildNativeConversionMap();
    }
    
    public void clearLocalIdMap() {
        this.localIdMap.clear();
    }
    
    public void rebuildNativeConversionMap() {
        NativeIdConversionMap.clearAll();
        for (final Map.Entry<Integer, Integer> entry : this.fromLocalToServerMap.entrySet()) {
            NativeIdConversionMap.mapConversion(entry.getKey(), entry.getValue());
        }
    }
    
    public void registerId(final String s, final int n) {
        this.localIdMap.put(s, n);
    }
    
    public void registerIdsFromMap(final String s, final Map<String, Integer> map) {
        for (final Map.Entry<String, Integer> entry : map.entrySet()) {
            final StringBuilder sb = new StringBuilder();
            String string;
            if (s != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s);
                sb2.append(":");
                string = sb2.toString();
            }
            else {
                string = "";
            }
            sb.append(string);
            sb.append(entry.getKey());
            this.registerId(sb.toString(), entry.getValue());
        }
    }
    
    public void unregisterId(final String s) {
        this.localIdMap.remove(s);
    }
}
