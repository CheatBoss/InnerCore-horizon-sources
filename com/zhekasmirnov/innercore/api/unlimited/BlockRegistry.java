package com.zhekasmirnov.innercore.api.unlimited;

import java.util.*;
import com.zhekasmirnov.innercore.api.log.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.modpack.*;
import com.zhekasmirnov.innercore.api.runtime.*;

public class BlockRegistry
{
    public static final String LOGGER_TAG = "INNERCORE-BLOCKS";
    private static HashMap<IDDataPair, BlockVariant> blockVariantMap;
    private static FileLoader loader;
    
    static {
        BlockRegistry.blockVariantMap = new HashMap<IDDataPair, BlockVariant>();
    }
    
    public static void createBlock(final int n, final String s, final ScriptableObject scriptableObject) {
        createBlock(n, s, scriptableObject, SpecialType.DEFAULT);
    }
    
    public static void createBlock(final int n, final String s, final ScriptableObject scriptableObject, final SpecialType specialType) {
        if (!IDRegistry.getNameByID(n).equals(s)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("numeric uid ");
            sb.append(n);
            sb.append(IDRegistry.getNameByID(n));
            sb.append(" doesn't match string id ");
            sb.append(s);
            throw new IllegalArgumentException(sb.toString());
        }
        if (IDRegistry.isVanilla(n)) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("cannot create block with vanilla id ");
            sb2.append(n);
            ICLog.e("INNERCORE-BLOCKS", sb2.toString(), new RuntimeException());
            return;
        }
        final String convertNameId = NativeAPI.convertNameId(s);
        int i = 0;
        final NativeBlock block = NativeBlock.createBlock(n, convertNameId, "blank", 0);
        int n2 = 0;
        final Object[] allIds = scriptableObject.getAllIds();
        if (allIds.length == 0) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("no variants found in variant array while creating block ");
            sb3.append(s);
            sb3.append(", variants must be formatted as [{name: 'name', textures:[['name', index], ...], inCreative: true/false}, ...]");
            throw new IllegalArgumentException(sb3.toString());
        }
        while (i < allIds.length) {
            final Object o = allIds[i];
            Object o2 = null;
            if (o instanceof Integer) {
                o2 = scriptableObject.get((int)o, (Scriptable)scriptableObject);
            }
            if (o instanceof String) {
                o2 = scriptableObject.get((String)o, (Scriptable)scriptableObject);
            }
            int n3 = n2;
            if (o2 != null) {
                n3 = n2;
                if (o2 instanceof ScriptableObject) {
                    n3 = n2 + 1;
                    final BlockVariant blockVariant = new BlockVariant(n, n2, (ScriptableObject)o2);
                    if (blockVariant.name != null) {
                        block.addVariant(blockVariant.name, blockVariant.textures, blockVariant.textureIds);
                    }
                    else {
                        block.addVariant(blockVariant.textures, blockVariant.textureIds);
                    }
                    if (blockVariant.inCreative) {
                        NativeItem.addToCreative(n, 1, n3 - 1, null);
                    }
                    BlockRegistry.blockVariantMap.put(new IDDataPair(n, blockVariant.data), blockVariant);
                    final NativeItemModel for1 = NativeItemModel.getFor(n, blockVariant.data);
                    for1.updateForBlockVariant(blockVariant);
                    if (for1.getCacheKey() == null) {
                        for1.setCacheKey("modded");
                    }
                    for1.isLazyLoading = blockVariant.isTechnical;
                }
            }
            ++i;
            n2 = n3;
        }
        specialType.setupBlock(n);
    }
    
    public static BlockVariant getBlockVariant(final int n, final int n2) {
        return BlockRegistry.blockVariantMap.get(new IDDataPair(n, n2));
    }
    
    public static void onInit() {
        ICLog.d("INNERCORE-BLOCKS", "reading saved mappings...");
        BlockRegistry.loader = new FileLoader(ModPackContext.getInstance().getCurrentModPack().getRequestHandler(ModPackDirectory.DirectoryType.CONFIG).get("innercore", "ids.json"));
    }
    
    public static void onModsLoaded() {
        Callback.invokeAPICallback("PreBlocksDefined", new Object[0]);
        BlockRegistry.loader.save();
        Callback.invokeAPICallback("BlocksDefined", new Object[0]);
        ICLog.d("INNERCORE-BLOCKS", "complete");
    }
    
    public static void setShape(final int n, final int n2, final float n3, final float n4, final float n5, final float n6, final float n7, final float n8) {
        final BlockShape shape = new BlockShape(n3, n4, n5, n6, n7, n8);
        shape.setToBlock(n, n2);
        final BlockVariant blockVariant = getBlockVariant(n, n2);
        if (blockVariant != null) {
            blockVariant.shape = shape;
            NativeItemModel.getFor(n, n2).updateForBlockVariant(blockVariant);
        }
    }
}
