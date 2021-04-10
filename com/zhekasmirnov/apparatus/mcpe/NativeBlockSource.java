package com.zhekasmirnov.apparatus.mcpe;

import com.android.tools.r8.annotations.*;
import java.util.*;
import com.zhekasmirnov.apparatus.util.*;
import java.util.function.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.item.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.entity.*;
import com.zhekasmirnov.innercore.api.commontypes.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.block.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.api.*;

@SynthesizedClassMap({ -$$Lambda$NativeBlockSource$kwjIWCMaaUjr-McTeHMR8BzZsQU.class })
public class NativeBlockSource
{
    private static final Map<Integer, NativeBlockSource> defaultBlockSourceForDimensions;
    private boolean allowUpdate;
    private final boolean isFinalizable;
    private final long pointer;
    private int updateType;
    
    static {
        defaultBlockSourceForDimensions = new HashMap<Integer, NativeBlockSource>();
    }
    
    public NativeBlockSource(final int n) {
        this(n, true, false);
    }
    
    public NativeBlockSource(final int n, final boolean b, final boolean b2) {
        this(constructNew(n, b, b2), true);
    }
    
    public NativeBlockSource(final long pointer, final boolean isFinalizable) {
        this.allowUpdate = true;
        this.updateType = 3;
        if (pointer == 0L) {
            throw new IllegalArgumentException("cannot pass null pointer to NativeBlockSource constructor");
        }
        this.pointer = pointer;
        this.isFinalizable = isFinalizable;
    }
    
    private static native boolean canSeeSky(final long p0, final int p1, final int p2, final int p3);
    
    private static native long clip(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final int p7, final float[] p8);
    
    private static native long constructNew(final int p0, final boolean p1, final boolean p2);
    
    private static native void destroyBlock(final long p0, final int p1, final int p2, final int p3, final boolean p4, final int p5);
    
    private static native void explode(final long p0, final float p1, final float p2, final float p3, final float p4, final boolean p5);
    
    private static native long[] fetchEntitiesInAABB(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final int p7, final boolean p8);
    
    private static native int getBiome(final long p0, final int p1, final int p2, final int p3);
    
    private static native float getBiomeTemperatureAt(final long p0, final int p1, final int p2, final int p3);
    
    private static native int getBlockData(final long p0, final int p1, final int p2, final int p3);
    
    private static native long getBlockEntity(final long p0, final int p1, final int p2, final int p3);
    
    private static native int getBlockId(final long p0, final int p1, final int p2, final int p3);
    
    private static native long getBlockIdDataAndState(final long p0, final int p1, final int p2, final int p3);
    
    private static native int getBrightness(final long p0, final int p1, final int p2, final int p3);
    
    private static native int getChunkState(final long p0, final int p1, final int p2);
    
    public static NativeBlockSource getCurrentClientRegion() {
        final long nativeGetForClientSide = nativeGetForClientSide();
        if (nativeGetForClientSide != 0L) {
            return new NativeBlockSource(nativeGetForClientSide, false);
        }
        return null;
    }
    
    public static NativeBlockSource getCurrentWorldGenRegion() {
        final long nativeGetForCurrentThread = nativeGetForCurrentThread();
        if (nativeGetForCurrentThread != 0L) {
            return new NativeBlockSource(nativeGetForCurrentThread, false);
        }
        return null;
    }
    
    public static NativeBlockSource getDefaultForActor(final long n) {
        final int entityDimension = NativeAPI.getEntityDimension(n);
        if (!NativeStaticUtils.isExistingEntity(n)) {
            return null;
        }
        return getDefaultForDimension(entityDimension);
    }
    
    public static NativeBlockSource getDefaultForDimension(final int n) {
        final Map<Integer, NativeBlockSource> defaultBlockSourceForDimensions = NativeBlockSource.defaultBlockSourceForDimensions;
        // monitorenter(defaultBlockSourceForDimensions)
        while (true) {
            try {
                try {
                    // monitorexit(defaultBlockSourceForDimensions)
                    return Java8BackComp.computeIfAbsent(NativeBlockSource.defaultBlockSourceForDimensions, n, new -$$Lambda$NativeBlockSource$kwjIWCMaaUjr-McTeHMR8BzZsQU(n));
                }
                catch (IllegalArgumentException ex) {
                    // monitorexit(defaultBlockSourceForDimensions)
                    return null;
                }
                // monitorexit(defaultBlockSourceForDimensions)
                throw;
            }
            finally {
                continue;
            }
            break;
        }
    }
    
    private static native int getDimension(final long p0);
    
    public static NativeBlockSource getFromCallbackPointer(final long n) {
        return new NativeBlockSource(n, false);
    }
    
    private static native int getGrassColor(final long p0, final int p1, final int p2, final int p3);
    
    private static native boolean isChunkLoaded(final long p0, final int p1, final int p2);
    
    private static native void nativeFinalize(final long p0);
    
    private static native long nativeGetForClientSide();
    
    private static native long nativeGetForCurrentThread();
    
    public static void resetDefaultBlockSources() {
        ICLog.d("BlockSource", "resetting default block sources");
        synchronized (NativeBlockSource.defaultBlockSourceForDimensions) {
            NativeBlockSource.defaultBlockSourceForDimensions.clear();
        }
    }
    
    private static native void setBiome(final long p0, final int p1, final int p2, final int p3);
    
    private static native void setBlock(final long p0, final int p1, final int p2, final int p3, final int p4, final int p5, final boolean p6, final int p7);
    
    private static native void setBlockByRuntimeId(final long p0, final int p1, final int p2, final int p3, final int p4, final boolean p5, final int p6);
    
    private static native long spawnDroppedItem(final long p0, final float p1, final float p2, final float p3, final int p4, final int p5, final int p6, final long p7);
    
    private static native long spawnEntity(final long p0, final int p1, final float p2, final float p3, final float p4);
    
    private static native long spawnExpOrbs(final long p0, final float p1, final float p2, final float p3, final int p4);
    
    private static native long spawnNamespacedEntity(final long p0, final float p1, final float p2, final float p3, final String p4, final String p5, final String p6);
    
    public void breakBlock(final int n, final int n2, final int n3, final boolean b) {
        this.breakBlock(n, n2, n3, b, -1L, new ItemStack());
    }
    
    public void breakBlock(final int n, final int n2, final int n3, final boolean b, final long n4) {
        this.breakBlock(n, n2, n3, b, n4, StaticEntity.getCarriedItem(n4));
    }
    
    public void breakBlock(final int n, final int n2, final int n3, final boolean b, final long n4, final ItemStack itemStack) {
        final Coords coords = new Coords(n, n2, n3);
        final FullBlock fullBlock = new FullBlock(this, n, n2, n3);
        Callback.invokeAPICallback("BreakBlock", this, coords, fullBlock, b, n4, itemStack.asScriptable());
        if (!NativeAPI.isDefaultPrevented() && StaticEntity.exists(n4) && StaticEntity.getDimension(n4) == this.getDimension()) {
            Callback.invokeAPICallback("DestroyBlock", coords, fullBlock, n4);
        }
        this.destroyBlock(n, n2, n3, b);
    }
    
    public void breakBlock(final int n, final int n2, final int n3, final boolean b, final ItemStack itemStack) {
        this.breakBlock(n, n2, n3, b, -1L, itemStack);
    }
    
    public ScriptableObject breakBlockForJsResult(final int n, final int n2, final int n3) {
        return this.breakBlockForResult(n, n2, n3).asScriptable();
    }
    
    public ScriptableObject breakBlockForJsResult(final int n, final int n2, final int n3, final long n4) {
        return this.breakBlockForResult(n, n2, n3, n4).asScriptable();
    }
    
    public ScriptableObject breakBlockForJsResult(final int n, final int n2, final int n3, final long n4, final ScriptableObject scriptableObject) {
        return this.breakBlockForResult(n, n2, n3, n4, new ItemStack(scriptableObject)).asScriptable();
    }
    
    public ScriptableObject breakBlockForJsResult(final int n, final int n2, final int n3, final ScriptableObject scriptableObject) {
        return this.breakBlockForResult(n, n2, n3, new ItemStack(scriptableObject)).asScriptable();
    }
    
    public BlockBreakResult breakBlockForResult(final int n, final int n2, final int n3) {
        return this.breakBlockForResult(n, n2, n3, -1L, new ItemStack());
    }
    
    public BlockBreakResult breakBlockForResult(final int n, final int n2, final int n3, final long n4) {
        return this.breakBlockForResult(n, n2, n3, n4, StaticEntity.getCarriedItem(n4));
    }
    
    public BlockBreakResult breakBlockForResult(final int n, final int n2, final int n3, final long n4, final ItemStack itemStack) {
        NativeCallback.startOverrideBlockBreakResult();
        this.breakBlock(n, n2, n3, true, n4, itemStack);
        return NativeCallback.endOverrideBlockBreakResult();
    }
    
    public BlockBreakResult breakBlockForResult(final int n, final int n2, final int n3, final ItemStack itemStack) {
        return this.breakBlockForResult(n, n2, n3, -1L, itemStack);
    }
    
    public boolean canSeeSky(final int n, final int n2, final int n3) {
        return canSeeSky(this.pointer, n, n2, n3);
    }
    
    public long clip(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final int n7, final float[] array) {
        return clip(this.pointer, n, n2, n3, n4, n5, n6, n7, array);
    }
    
    public void destroyBlock(final int n, final int n2, final int n3) {
        this.destroyBlock(n, n2, n3, false);
    }
    
    public void destroyBlock(final int n, final int n2, final int n3, final boolean b) {
        destroyBlock(this.pointer, n, n2, n3, b, this.updateType);
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass() && this.pointer == ((NativeBlockSource)o).pointer);
    }
    
    public void explode(final float n, final float n2, final float n3, final float n4, final boolean b) {
        explode(this.pointer, n, n2, n3, n4, b);
    }
    
    public long[] fetchEntitiesInAABB(final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
        return this.fetchEntitiesInAABB(n, n2, n3, n4, n5, n6, 255, true);
    }
    
    public long[] fetchEntitiesInAABB(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final int n7) {
        return this.fetchEntitiesInAABB(n, n2, n3, n4, n5, n6, n7, false);
    }
    
    public long[] fetchEntitiesInAABB(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final int n7, final boolean b) {
        return fetchEntitiesInAABB(this.pointer, n, n2, n3, n4, n5, n6, n7, b);
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        final boolean isFinalizable = this.isFinalizable;
    }
    
    public int getBiome(final int n, final int n2) {
        return getBiome(this.pointer, n, 64, n2);
    }
    
    public float getBiomeTemperatureAt(final int n, final int n2, final int n3) {
        return getBiomeTemperatureAt(this.pointer, n, n2, n3);
    }
    
    public BlockState getBlock(final int n, final int n2, final int n3) {
        return new BlockState(getBlockIdDataAndState(this.pointer, n, n2, n3));
    }
    
    public int getBlockData(final int n, final int n2, final int n3) {
        return getBlockData(this.pointer, n, n2, n3);
    }
    
    public NativeTileEntity getBlockEntity(final int n, final int n2, final int n3) {
        final long blockEntity = getBlockEntity(this.pointer, n, n2, n3);
        if (blockEntity != 0L) {
            return new NativeTileEntity(blockEntity);
        }
        return null;
    }
    
    public int getBlockId(final int n, final int n2, final int n3) {
        return getBlockId(this.pointer, n, n2, n3);
    }
    
    public boolean getBlockUpdateAllowed() {
        return this.allowUpdate;
    }
    
    public int getBlockUpdateType() {
        return this.updateType;
    }
    
    public int getChunkState(final int n, final int n2) {
        return getChunkState(this.pointer, n, n2);
    }
    
    public int getChunkStateAt(final int n, final int n2) {
        return getChunkState(this.pointer, (int)Math.floor(n / 16.0), (int)Math.floor(n2 / 16.0));
    }
    
    public int getDimension() {
        return getDimension(this.pointer);
    }
    
    public int getGrassColor(final int n, final int n2) {
        return getGrassColor(this.pointer, n, 64, n2);
    }
    
    public int getLightLevel(final int n, final int n2, final int n3) {
        return getBrightness(this.pointer, n, n2, n3);
    }
    
    public long getPointer() {
        return this.pointer;
    }
    
    @Override
    public int hashCode() {
        return (int)(this.pointer ^ this.pointer >>> 32);
    }
    
    public boolean isChunkLoaded(final int n, final int n2) {
        return isChunkLoaded(this.pointer, n, n2);
    }
    
    public boolean isChunkLoadedAt(final int n, final int n2) {
        return isChunkLoaded(this.pointer, (int)Math.floor(n / 16.0), (int)Math.floor(n2 / 16.0));
    }
    
    public NativeArray listEntitiesInAABB(final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
        return this.listEntitiesInAABB(n, n2, n3, n4, n5, n6, 255, true);
    }
    
    public NativeArray listEntitiesInAABB(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final int n7) {
        return this.listEntitiesInAABB(n, n2, n3, n4, n5, n6, n7, false);
    }
    
    public NativeArray listEntitiesInAABB(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, int n7, final boolean b) {
        final long[] fetchEntitiesInAABB = fetchEntitiesInAABB(this.pointer, n, n2, n3, n4, n5, n6, n7, b);
        final Object[] array = new Object[fetchEntitiesInAABB.length];
        n7 = 0;
        for (int length = fetchEntitiesInAABB.length, i = 0; i < length; ++i, ++n7) {
            array[n7] = fetchEntitiesInAABB[i];
        }
        return ScriptableObjectHelper.createArray(array);
    }
    
    public void setBiome(final int n, final int n2, final int n3) {
        setBiome(this.pointer, n, n2, n3);
    }
    
    public void setBlock(final int n, final int n2, final int n3, final int n4) {
        this.setBlock(n, n2, n3, n4, 0);
    }
    
    public void setBlock(final int n, final int n2, final int n3, final int n4, final int n5) {
        setBlock(this.pointer, n, n2, n3, n4, n5, this.allowUpdate, this.updateType);
    }
    
    public void setBlock(final int n, final int n2, final int n3, final BlockState blockState) {
        if (blockState.runtimeId > 0) {
            setBlockByRuntimeId(this.pointer, n, n2, n3, blockState.runtimeId, this.allowUpdate, this.updateType);
            return;
        }
        setBlock(this.pointer, n, n2, n3, blockState.id, blockState.data, this.allowUpdate, this.updateType);
    }
    
    public void setBlockUpdateAllowed(final boolean allowUpdate) {
        this.allowUpdate = allowUpdate;
    }
    
    public void setBlockUpdateType(final int updateType) {
        this.updateType = updateType;
    }
    
    public long spawnDroppedItem(final float n, final float n2, final float n3, final int n4, final int n5, final int n6) {
        return this.spawnDroppedItem(n, n2, n3, n4, n5, n6, null);
    }
    
    public long spawnDroppedItem(final float n, final float n2, final float n3, final int n4, final int n5, final int n6, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        return spawnDroppedItem(this.pointer, n, n2, n3, n4, n5, n6, NativeItemInstanceExtra.getValueOrNullPtr(nativeItemInstanceExtra));
    }
    
    public long spawnEntity(final float n, final float n2, final float n3, final int n4) {
        return spawnEntity(this.pointer, n4, n, n2, n3);
    }
    
    public long spawnEntity(final float n, final float n2, final float n3, final String s) {
        if (s == null) {
            return -1L;
        }
        if (s.length() == 0) {
            return -1L;
        }
        final String[] split = s.split(":");
        if (split.length >= 3) {
            return this.spawnEntity(n, n2, n3, split[0], split[1], split[2]);
        }
        if (split.length == 2) {
            return this.spawnEntity(n, n2, n3, split[0], split[1], "");
        }
        if (split.length == 1) {
            return this.spawnEntity(n, n2, n3, "minecraft", split[0], "");
        }
        return -1L;
    }
    
    public long spawnEntity(final float n, final float n2, final float n3, final String s, final String s2, final String s3) {
        return spawnNamespacedEntity(this.pointer, n, n2, n3, s, s2, s3);
    }
    
    public void spawnExpOrbs(final float n, final float n2, final float n3, final int n4) {
        spawnExpOrbs(this.pointer, n, n2, n3, n4);
    }
}
