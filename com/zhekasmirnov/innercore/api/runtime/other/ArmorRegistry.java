package com.zhekasmirnov.innercore.api.runtime.other;

import java.util.*;
import android.util.*;
import com.zhekasmirnov.innercore.api.commontypes.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import com.zhekasmirnov.innercore.api.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.log.*;

public class ArmorRegistry
{
    private static HashMap<Integer, ArmorInfo> armorInfoMap;
    private static NativeItemInstance[] armorSlots;
    
    static {
        ArmorRegistry.armorInfoMap = new HashMap<Integer, ArmorInfo>();
        ArmorRegistry.armorSlots = new NativeItemInstance[4];
    }
    
    public static void onHurt(final long n, int i, final int n2, final boolean b, final boolean b2) {
        refreshArmorSlots();
        final Pair pair = new Pair((Object)"attacker", (Object)n);
        final int n3 = 0;
        final ScriptableParams scriptableParams = new ScriptableParams((Pair<String, Object>[])new Pair[] { pair, new Pair((Object)"damage", (Object)i), new Pair((Object)"type", (Object)n2), new Pair((Object)"bool1", (Object)b), new Pair((Object)"bool2", (Object)b2) });
        NativeItemInstance nativeItemInstance;
        ArmorInfo armorInfo;
        ItemInstance itemInstance;
        for (i = n3; i < 4; ++i) {
            nativeItemInstance = ArmorRegistry.armorSlots[i];
            armorInfo = ArmorRegistry.armorInfoMap.get(nativeItemInstance.id);
            if (armorInfo != null && armorInfo.callback != null) {
                itemInstance = new ItemInstance(nativeItemInstance);
                if (armorInfo.callback.hurt(scriptableParams, itemInstance, i, armorInfo)) {
                    postArmorApply(i, nativeItemInstance, itemInstance);
                }
            }
        }
    }
    
    public static void onTick() {
        if (!NativeAPI.isValidEntity(NativeAPI.getPlayer())) {
            ICLog.d("ARMOR", "ticking with invalid player entity aborted");
            return;
        }
        refreshArmorSlots();
        for (int i = 0; i < 4; ++i) {
            final NativeItemInstance nativeItemInstance = ArmorRegistry.armorSlots[i];
            final ArmorInfo armorInfo = ArmorRegistry.armorInfoMap.get(nativeItemInstance.id);
            if (armorInfo != null && armorInfo.callback != null) {
                final ItemInstance itemInstance = new ItemInstance(nativeItemInstance);
                if (armorInfo.callback.tick(itemInstance, i, armorInfo)) {
                    postArmorApply(i, nativeItemInstance, itemInstance);
                }
            }
        }
    }
    
    private static void postArmorApply(final int n, final NativeItemInstance nativeItemInstance, final ItemInstance itemInstance) {
        MainThreadQueue.serverThread.enqueue(new Runnable() {
            @Override
            public void run() {
                final NativeItemInstance nativeItemInstance = new NativeItemInstance(NativeAPI.getPlayerArmor(n));
                if (nativeItemInstance.id == nativeItemInstance.id && nativeItemInstance.data == nativeItemInstance.data) {
                    NativeAPI.setPlayerArmor(n, itemInstance.getId(), 1, itemInstance.getData(), itemInstance.getExtraValue());
                }
                nativeItemInstance.destroy();
            }
        });
    }
    
    public static void preventArmorDamaging(final int n) {
        final NativeItem itemById = NativeItem.getItemById(n);
        if (itemById != null) {
            itemById.setArmorDamageable(false);
        }
    }
    
    private static void refreshArmorSlots() {
        for (int i = 0; i < 4; ++i) {
            if (ArmorRegistry.armorSlots[i] != null) {
                ArmorRegistry.armorSlots[i].destroy();
            }
            ArmorRegistry.armorSlots[i] = new NativeItemInstance(NativeAPI.getPlayerArmor(i));
        }
    }
    
    public static void registerArmor(final int n, final IArmorCallback armorCallback) {
        ArmorRegistry.armorInfoMap.put(n, new ArmorInfo(armorCallback, NativeItem.getMaxDamageForId(n, 0)));
        final NativeItem itemById = NativeItem.getItemById(n);
        if (itemById != null) {
            itemById.setArmorDamageable(armorCallback instanceof DefaultArmorCallback);
        }
    }
    
    public static void registerArmor(final int n, final ScriptableObject scriptableObject) {
        registerArmor(n, (IArmorCallback)new ScriptableArmorCallbacks(scriptableObject));
    }
    
    public static class ArmorInfo
    {
        public IArmorCallback callback;
        public int durability;
        
        public ArmorInfo(final int durability) {
            this.durability = durability;
        }
        
        public ArmorInfo(final IArmorCallback callback, final int durability) {
            this.callback = callback;
            this.durability = durability;
        }
    }
    
    public static class DefaultArmorCallback implements IArmorCallback
    {
        @Override
        public boolean hurt(final ScriptableParams scriptableParams, final ItemInstance itemInstance, final int n, final ArmorInfo armorInfo) {
            return false;
        }
        
        @Override
        public boolean tick(final ItemInstance itemInstance, final int n, final ArmorInfo armorInfo) {
            return false;
        }
    }
    
    interface IArmorCallback
    {
        boolean hurt(final ScriptableParams p0, final ItemInstance p1, final int p2, final ArmorInfo p3);
        
        boolean tick(final ItemInstance p0, final int p1, final ArmorInfo p2);
    }
    
    interface IJSArmorCallback
    {
        boolean hurt(final ScriptableParams p0, final ItemInstance p1, final int p2, final int p3);
        
        boolean tick(final ItemInstance p0, final int p1, final int p2);
    }
    
    public static class ScriptableArmorCallbacks implements IArmorCallback
    {
        private final IJSArmorCallback callbacks;
        
        public ScriptableArmorCallbacks(final ScriptableObject scriptableObject) {
            this.callbacks = (IJSArmorCallback)Context.jsToJava((Object)scriptableObject, (Class)IJSArmorCallback.class);
        }
        
        @Override
        public boolean hurt(final ScriptableParams scriptableParams, final ItemInstance itemInstance, final int n, final ArmorInfo armorInfo) {
            try {
                return this.callbacks.hurt(scriptableParams, itemInstance, n, armorInfo.durability);
            }
            catch (Throwable t) {
                ICLog.e("ARMOR", "error in armor tick", t);
                DialogHelper.reportNonFatalError("armor hurt func caused error", t);
                return false;
            }
        }
        
        @Override
        public boolean tick(final ItemInstance itemInstance, final int n, final ArmorInfo armorInfo) {
            try {
                return this.callbacks.tick(itemInstance, n, armorInfo.durability);
            }
            catch (Throwable t) {
                ICLog.e("ARMOR", "error in armor tick", t);
                final StringBuilder sb = new StringBuilder();
                sb.append("armor tick func caused ");
                sb.append(t);
                sb.append(" see log for details");
                PrintStacking.print(sb.toString());
                return false;
            }
        }
    }
}
