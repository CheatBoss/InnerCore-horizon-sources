package com.zhekasmirnov.innercore.api;

import org.mozilla.javascript.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.runtime.other.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import com.zhekasmirnov.apparatus.mcpe.*;

public class NativeBlock
{
    private static HashMap<Integer, Function> animateCallbackForId;
    private static HashMap<Integer, Float> blockDestroyTimes;
    private static HashMap<Integer, Float> realBlockDestroyTimes;
    private static HashMap<Integer, Function> tickingCallbackForId;
    private String basicName;
    private int id;
    private long pointer;
    private ArrayList<Long> variantPtrs;
    
    static {
        NativeBlock.blockDestroyTimes = new HashMap<Integer, Float>();
        NativeBlock.realBlockDestroyTimes = new HashMap<Integer, Float>();
        NativeBlock.tickingCallbackForId = new HashMap<Integer, Function>();
        NativeBlock.animateCallbackForId = new HashMap<Integer, Function>();
    }
    
    protected NativeBlock(final long pointer, final int id, final String basicName) {
        this.basicName = "Unknown Block";
        this.variantPtrs = new ArrayList<Long>();
        this.pointer = pointer;
        this.id = id;
        this.basicName = basicName;
    }
    
    public static native long addVariant(final long p0, final String p1, final String[] p2, final int[] p3);
    
    public static native long constructBlock(final int p0, final String p1, final String p2, final int p3);
    
    public static NativeBlock createBlock(final int n, final String s, String fixUnicodeIfRequired, final int n2) {
        final StringBuilder sb = new StringBuilder();
        sb.append("block.");
        sb.append(s);
        fixUnicodeIfRequired = NameTranslation.fixUnicodeIfRequired(sb.toString(), fixUnicodeIfRequired);
        return new NativeBlock(constructBlock(n, s, fixUnicodeIfRequired, n2), n, fixUnicodeIfRequired);
    }
    
    public static native float getDestroyTime(final int p0);
    
    public static float getDestroyTimeForId(final int n) {
        if (NativeBlock.blockDestroyTimes.containsKey(n)) {
            return NativeBlock.blockDestroyTimes.get(n);
        }
        if (NativeBlock.realBlockDestroyTimes.containsKey(n)) {
            return NativeBlock.realBlockDestroyTimes.get(n);
        }
        return getDestroyTime(n);
    }
    
    public static native float getExplosionResistance(final int p0);
    
    public static native float getFriction(final int p0);
    
    public static native int getLightLevel(final int p0);
    
    public static native int getLightOpacity(final int p0);
    
    public static native int getMapColor(final int p0);
    
    public static native int getMaterial(final int p0);
    
    public static native int getRenderLayer(final int p0);
    
    public static native int getRenderType(final int p0);
    
    public static native float getTranslucency(final int p0);
    
    public static native boolean isSolid(final int p0);
    
    public static void onAnimateTickCallback(final int n, final int n2, final int n3, final int n4, final int n5) {
        final Function function = NativeBlock.animateCallbackForId.get(n4);
        if (function != null) {
            function.call(Compiler.assureContextForCurrentThread(), function.getParentScope(), function.getParentScope(), new Object[] { n, n2, n3, n4, n5 });
        }
    }
    
    public static void onBlockDestroyStarted(int tile, final int n, final int n2, final int n3) {
        tile = NativeAPI.getTile(tile, n, n2);
        if (NativeBlock.blockDestroyTimes.containsKey(tile)) {
            setTempDestroyTimeForId(tile, NativeBlock.blockDestroyTimes.get(tile));
            return;
        }
        if (NativeBlock.realBlockDestroyTimes.containsKey(tile)) {
            setDestroyTime(tile, NativeBlock.realBlockDestroyTimes.get(tile));
        }
    }
    
    public static void onRandomTickCallback(final int n, final int n2, final int n3, final int n4, final int n5, final NativeBlockSource nativeBlockSource) {
        final Function function = NativeBlock.tickingCallbackForId.get(n4);
        if (function != null) {
            function.call(Compiler.assureContextForCurrentThread(), function.getParentScope(), function.getParentScope(), new Object[] { n, n2, n3, n4, n5, nativeBlockSource });
        }
    }
    
    public static void setAnimateTickCallback(final int n, final Function function) {
        NativeIdMapping.iterateMetadata(n, -1, (NativeIdMapping.IIdIterator)new NativeIdMapping.IIdIterator() {
            @Override
            public void onIdDataIterated(final int n, final int n2) {
                NativeBlock.setAnimatedTile(n, n2, function != null);
            }
        });
        if (function != null) {
            NativeBlock.animateCallbackForId.put(n, function);
            return;
        }
        if (NativeBlock.animateCallbackForId.containsKey(n)) {
            NativeBlock.animateCallbackForId.remove(n);
        }
    }
    
    public static native void setAnimatedTile(final int p0, final int p1, final boolean p2);
    
    public static native void setBlockColorSource(final int p0, final int p1);
    
    public static native void setDestroyTime(final int p0, final float p1);
    
    public static void setDestroyTimeForId(final int n, final float n2) {
        NativeBlock.blockDestroyTimes.put(n, n2);
    }
    
    public static native void setExplosionResistance(final int p0, final float p1);
    
    public static native void setFriction(final int p0, final float p1);
    
    public static native void setLightLevel(final int p0, final int p1);
    
    public static native void setLightOpacity(final int p0, final int p1);
    
    public static native void setMapColor(final int p0, final int p1);
    
    public static native void setMaterial(final int p0, final int p1);
    
    public static native void setMaterialBase(final int p0, final int p1);
    
    public static void setRandomTickCallback(final int n, final Function function) {
        NativeIdMapping.iterateMetadata(n, -1, (NativeIdMapping.IIdIterator)new NativeIdMapping.IIdIterator() {
            @Override
            public void onIdDataIterated(final int n, final int n2) {
                NativeBlock.setTickingTile(n, n2, function != null);
            }
        });
        if (function != null) {
            NativeBlock.tickingCallbackForId.put(n, function);
            return;
        }
        if (NativeBlock.tickingCallbackForId.containsKey(n)) {
            NativeBlock.tickingCallbackForId.remove(n);
        }
    }
    
    public static native void setReceivingEntityInsideEvent(final int p0, final boolean p1);
    
    public static native void setReceivingEntityStepOnEvent(final int p0, final boolean p1);
    
    public static native void setReceivingNeighbourChangeEvent(final int p0, final boolean p1);
    
    public static native void setRedstoneConnectorNative(final int p0, final int p1, final boolean p2);
    
    public static native void setRedstoneEmitterNative(final int p0, final int p1, final boolean p2);
    
    public static native void setRedstoneTileNative(final int p0, final int p1, final boolean p2);
    
    public static native void setRenderAllFaces(final int p0, final boolean p1);
    
    public static native void setRenderLayer(final int p0, final int p1);
    
    public static native void setRenderType(final int p0, final int p1);
    
    public static native void setShape(final int p0, final int p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7);
    
    public static native void setSolid(final int p0, final boolean p1);
    
    public static native void setSoundType(final int p0, final String p1);
    
    public static void setTempDestroyTimeForId(final int n, final float n2) {
        final float destroyTimeForId = getDestroyTimeForId(n);
        setDestroyTime(n, n2);
        NativeBlock.realBlockDestroyTimes.put(n, destroyTimeForId);
    }
    
    public static native void setTickingTile(final int p0, final int p1, final boolean p2);
    
    public static native void setTranslucency(final int p0, final float p1);
    
    public void addVariant(final String s, final String[] array, final int[] array2) {
        this.variantPtrs.add(addVariant(this.pointer, s, array, array2));
    }
    
    public void addVariant(final String[] array, final int[] array2) {
        this.variantPtrs.add(addVariant(this.pointer, this.basicName, array, array2));
    }
    
    public int getId() {
        return this.id;
    }
}
