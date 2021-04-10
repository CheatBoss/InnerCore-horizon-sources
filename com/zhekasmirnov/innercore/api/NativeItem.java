package com.zhekasmirnov.innercore.api;

import java.util.*;
import org.mozilla.javascript.*;
import org.mozilla.javascript.annotations.*;
import com.zhekasmirnov.innercore.mod.resource.*;
import com.zhekasmirnov.innercore.api.runtime.other.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.innercore.api.commontypes.*;
import com.zhekasmirnov.innercore.api.runtime.*;

public class NativeItem
{
    public static final Object DYNAMIC_ICON_LOCK;
    public static final Object DYNAMIC_NAME_LOCK;
    public static final int MAX_ITEM_ID = 32768;
    private static boolean isInnerCoreUIOverride;
    private static NativeItem[] itemById;
    private static HashSet<Integer> itemIdsWithDynamicIcon;
    private static String lastIconOverridePath;
    public int id;
    public final String nameId;
    public final String nameToDisplay;
    private long pointer;
    
    static {
        NativeItem.itemById = new NativeItem[32768];
        DYNAMIC_ICON_LOCK = new Object();
        DYNAMIC_NAME_LOCK = new Object();
        NativeItem.itemIdsWithDynamicIcon = new HashSet<Integer>();
        NativeItem.isInnerCoreUIOverride = false;
        NativeItem.lastIconOverridePath = null;
    }
    
    protected NativeItem(final int id, final long pointer, final String nameId, final String nameToDisplay) {
        this.id = id;
        this.pointer = pointer;
        this.nameId = nameId;
        this.nameToDisplay = nameToDisplay;
        NativeItem.itemById[id] = this;
        NameTranslation.sendNameToGenerateCache(id, 0, nameToDisplay);
    }
    
    @JSStaticFunction
    public static void addCreativeGroup(final String s, final String s2, final NativeArray nativeArray) {
        for (int n = 0; n < nativeArray.getLength(); ++n) {
            addToCreativeGroup(s, s2, ((Number)nativeArray.get(n)).intValue());
        }
    }
    
    public static native void addRepairItemId(final long p0, final int p1);
    
    @JSStaticFunction
    public static void addToCreative(final int n, final int n2, final int n3, final Object o) {
        addToCreativeInternal(n, n2, n3, NativeItemInstanceExtra.unwrapValue(o));
    }
    
    @JSStaticFunction
    public static native void addToCreativeGroup(final String p0, final String p1, final int p2);
    
    public static native void addToCreativeInternal(final int p0, final int p1, final int p2, final long p3);
    
    public static native long constuctArmorItem(final int p0, final String p1, final String p2, final String p3, final int p4, final String p5, final int p6, final int p7, final int p8);
    
    public static native long constuctItem(final int p0, final String p1, final String p2, final String p3, final int p4);
    
    public static native long constuctThrowableItem(final int p0, final String p1, final String p2, final String p3, final int p4);
    
    @JSStaticFunction
    public static NativeItem createArmorItem(final int n, String s, final String s2, final String s3, int n2, final String s4, final int n3, final int n4, final int n5) {
        final String convertNameId = NativeAPI.convertNameId(s);
        if (!ResourcePackManager.isValidItemTexture(s3, n2)) {
            s = "missing_icon";
            n2 = 0;
        }
        else {
            s = s3;
        }
        registerIcon(n, s, n2);
        final StringBuilder sb = new StringBuilder();
        sb.append("item_");
        sb.append(convertNameId);
        final NativeItem nativeItem = new NativeItem(n, constuctArmorItem(n, convertNameId, NameTranslation.fixUnicodeIfRequired(sb.toString(), s2), s, n2, s4, n3, n4, n5), convertNameId, s2);
        ArmorRegistry.registerArmor(n, (ArmorRegistry.IArmorCallback)new ArmorRegistry.DefaultArmorCallback());
        return nativeItem;
    }
    
    @JSStaticFunction
    public static NativeItem createItem(final int n, String s, final String s2, final String s3, final int n2) {
        final String convertNameId = NativeAPI.convertNameId(s);
        s = s3;
        int n3 = n2;
        if (!ResourcePackManager.isValidItemTexture(s3, n2)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s3);
            sb.append(" ");
            sb.append(n2);
            sb.append(ResourcePackManager.isValidItemTexture("missing_icon", n));
            Logger.debug("isValidItemTexture", sb.toString());
            s = "missing_icon";
            n3 = 0;
        }
        registerIcon(n, s, n3);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("item_");
        sb2.append(convertNameId);
        return new NativeItem(n, constuctItem(n, convertNameId, NameTranslation.fixUnicodeIfRequired(sb2.toString(), s2), s, n3), convertNameId, s2);
    }
    
    @JSStaticFunction
    public static NativeItem createThrowableItem(final int n, String s, final String s2, final String s3, final int n2) {
        final String convertNameId = NativeAPI.convertNameId(s);
        s = s3;
        int n3 = n2;
        if (!ResourcePackManager.isValidItemTexture(s3, n2)) {
            s = "missing_icon";
            n3 = 0;
        }
        registerIcon(n, s, n3);
        final StringBuilder sb = new StringBuilder();
        sb.append("item_");
        sb.append(convertNameId);
        return new NativeItem(n, constuctThrowableItem(n, convertNameId, NameTranslation.fixUnicodeIfRequired(sb.toString(), s2), s, n3), convertNameId, s2);
    }
    
    public static String getDynamicItemIconOverride(final int n, final int n2, final int n3, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        synchronized (NativeItem.class) {
            if (isDynamicIconItem(n)) {
                synchronized (NativeItem.DYNAMIC_ICON_LOCK) {
                    NativeItem.isInnerCoreUIOverride = true;
                    NativeItem.lastIconOverridePath = null;
                    Callback.invokeAPICallback("ItemIconOverride", new ItemInstance(n, n2, n3, nativeItemInstanceExtra), true);
                    NativeItem.isInnerCoreUIOverride = false;
                    return NativeItem.lastIconOverridePath;
                }
            }
            return null;
        }
    }
    
    @JSStaticFunction
    public static NativeItem getItemById(final int n) {
        if (n >= 0 && n < 32768) {
            return NativeItem.itemById[n];
        }
        return null;
    }
    
    public static String getLastIconOverridePath() {
        return NativeItem.lastIconOverridePath;
    }
    
    public static native int getMaxDamageForId(final int p0, final int p1);
    
    public static native int getMaxStackForId(final int p0, final int p1);
    
    public static String getNameForId(final int n, final int n2) {
        return getNameForId(n, n2, 0L);
    }
    
    public static native String getNameForId(final int p0, final int p1, final long p2);
    
    public static boolean isDynamicIconItem(final int n) {
        return NativeItem.itemIdsWithDynamicIcon.contains(n);
    }
    
    @JSStaticFunction
    public static boolean isGlintItemInstance(final int n, final int n2, final Object o) {
        return NativeAPI.isGlintItemInstance(n, n2, NativeItemInstanceExtra.unwrapValue(o));
    }
    
    @JSStaticFunction
    public static boolean isValid(int n) {
        if (n == 95) {
            return true;
        }
        final String nameForId = getNameForId(n, 0);
        if (nameForId.isEmpty()) {
            return false;
        }
        if (nameForId.startsWith("item.tile.")) {
            try {
                n = Integer.valueOf(nameForId.split("\\.")[2]);
                return n >= 256;
            }
            catch (Exception ex) {
                return true;
            }
        }
        if (nameForId.startsWith("tile.")) {
            try {
                n = Integer.valueOf(nameForId.split("\\.")[1]);
                return n >= 256;
            }
            catch (Exception ex2) {
                return true;
            }
        }
        return true;
    }
    
    public static void overrideItemIcon(final String s, final int n) {
        synchronized (NativeItem.DYNAMIC_ICON_LOCK) {
            if (!NativeItem.isInnerCoreUIOverride) {
                NativeAPI.overrideItemIcon(s, n);
            }
            NativeItem.lastIconOverridePath = ResourcePackManager.getItemTextureName(s, n);
            if (NativeItem.lastIconOverridePath != null && !NativeItem.lastIconOverridePath.endsWith(".png")) {
                final StringBuilder sb = new StringBuilder();
                sb.append(NativeItem.lastIconOverridePath);
                sb.append(".png");
                NativeItem.lastIconOverridePath = sb.toString();
            }
        }
    }
    
    private static void registerIcon(final int n, String itemTextureName, final int n2) {
        itemTextureName = ResourcePackManager.getItemTextureName(itemTextureName, n2);
        if (itemTextureName != null) {
            NativeAPI.addTextureToLoad(itemTextureName);
            NativeItemModel.getFor(n, 0).setItemTexturePath(itemTextureName);
        }
    }
    
    public static native void setAllowedInOffhand(final long p0, final boolean p1);
    
    public static native void setArmorDamageable(final long p0, final boolean p1);
    
    @JSStaticFunction
    public static void setCategoryForId(final int n, final int n2) {
        setCreativeCategoryForId(n, n2);
    }
    
    public static native void setCreativeCategory(final long p0, final int p1);
    
    public static native void setCreativeCategoryForId(final int p0, final int p1);
    
    public static native void setEnchantability(final long p0, final int p1, final int p2);
    
    public static native void setGlint(final long p0, final boolean p1);
    
    public static native void setHandEquipped(final long p0, final boolean p1);
    
    public static void setItemRequiresIconOverride(final int n, final boolean b) {
        if (b) {
            NativeItem.itemIdsWithDynamicIcon.add(n);
        }
        else {
            NativeItem.itemIdsWithDynamicIcon.remove(n);
        }
        NativeAPI.setItemRequiresIconOverride(n, b);
    }
    
    public static native void setLiquidClip(final long p0, final boolean p1);
    
    public static native void setMaxDamage(final long p0, final int p1);
    
    public static native void setMaxStackSize(final long p0, final int p1);
    
    public static native void setMaxUseDuration(final long p0, final int p1);
    
    public static native void setProperties(final long p0, final String p1);
    
    public static native void setStackedByData(final long p0, final boolean p1);
    
    public static native void setUseAnimation(final long p0, final int p1);
    
    public void addRepairItem(final int n) {
        addRepairItemId(this.pointer, n);
    }
    
    public void addRepairItems(final int... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            addRepairItemId(this.pointer, array[i]);
        }
    }
    
    public void setAllowedInOffhand(final boolean b) {
        setAllowedInOffhand(this.pointer, b);
    }
    
    public void setArmorDamageable(final boolean b) {
        setArmorDamageable(this.pointer, b);
    }
    
    public void setCreativeCategory(final int n) {
        setCreativeCategory(this.pointer, n);
    }
    
    public void setEnchantType(final int n) {
        this.setEnchantType(n, 1);
    }
    
    public void setEnchantType(final int n, final int n2) {
        setEnchantability(this.pointer, n, n2);
    }
    
    public void setEnchantability(final int n, final int n2) {
        setEnchantability(this.pointer, n, n2);
    }
    
    public void setGlint(final boolean b) {
        setGlint(this.pointer, b);
    }
    
    public void setHandEquipped(final boolean b) {
        setHandEquipped(this.pointer, b);
    }
    
    public void setLiquidClip(final boolean b) {
        setLiquidClip(this.pointer, b);
    }
    
    public void setMaxDamage(final int n) {
        setMaxDamage(this.pointer, n);
    }
    
    public void setMaxStackSize(final int n) {
        setMaxStackSize(this.pointer, n);
    }
    
    public void setMaxUseDuration(final int n) {
        setMaxUseDuration(this.pointer, n);
    }
    
    public void setProperties(final String s) {
        setProperties(this.pointer, s);
    }
    
    public void setStackedByData(final boolean b) {
        setStackedByData(this.pointer, b);
    }
    
    public void setUseAnimation(final int n) {
        setUseAnimation(this.pointer, n);
    }
}
