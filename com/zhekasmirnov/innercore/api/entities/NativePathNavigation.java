package com.zhekasmirnov.innercore.api.entities;

import java.util.*;

public class NativePathNavigation
{
    private static HashMap<Long, NativePathNavigation> activeNavigationMap;
    private final long entity;
    private boolean isValid;
    private final long pointer;
    NavigationResultFunction resultFunction;
    
    static {
        NativePathNavigation.activeNavigationMap = new HashMap<Long, NativePathNavigation>();
    }
    
    private NativePathNavigation(final long entity) {
        this.isValid = true;
        this.entity = entity;
        this.pointer = nativeConstruct(entity);
        if (this.pointer == 0L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid mob was passed to NativePathNavigation constructor: ");
            sb.append(entity);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public static void cleanup() {
        final Iterator<NativePathNavigation> iterator = NativePathNavigation.activeNavigationMap.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().isValid = false;
        }
        NativePathNavigation.activeNavigationMap.clear();
        nativeCleanup();
    }
    
    public static NativePathNavigation getNavigation(final long n) {
        final NativePathNavigation nativePathNavigation = NativePathNavigation.activeNavigationMap.get(n);
        if (nativePathNavigation != null) {
            return nativePathNavigation;
        }
        final NativePathNavigation nativePathNavigation2 = new NativePathNavigation(n);
        NativePathNavigation.activeNavigationMap.put(n, nativePathNavigation2);
        return nativePathNavigation2;
    }
    
    private static native void nativeCleanup();
    
    private static native long nativeConstruct(final long p0);
    
    public static void onNavigationResult(final long n, final int n2) {
        final NativePathNavigation navigation = getNavigation(n);
        if (navigation.resultFunction != null) {
            navigation.resultFunction.onNavigationResult(navigation, n2);
        }
    }
    
    public native boolean canOpenDoors();
    
    public native boolean canPassDoors();
    
    public native boolean getAvoidDamageBlocks();
    
    public native boolean getAvoidPortals();
    
    public native boolean getAvoidWater();
    
    public native boolean getCanBreach();
    
    public native boolean getCanFloat();
    
    public native boolean getCanJump();
    
    public native boolean getCanSink();
    
    public long getEntity() {
        return this.entity;
    }
    
    public native float getMaxNavigationDistance();
    
    public native float getSpeed();
    
    public native boolean isAmphibious();
    
    public native boolean isDone();
    
    public native boolean isRiverFollowing();
    
    public native NativePathNavigation moveToCoords(final float p0, final float p1, final float p2, final float p3);
    
    public native NativePathNavigation moveToEntity(final long p0, final float p1);
    
    public native NativePathNavigation setAvoidDamageBlocks(final boolean p0);
    
    public native NativePathNavigation setAvoidPortals(final boolean p0);
    
    public native NativePathNavigation setAvoidSun(final boolean p0);
    
    public native NativePathNavigation setAvoidWater(final boolean p0);
    
    public native NativePathNavigation setCanBreach(final boolean p0);
    
    public native NativePathNavigation setCanFloat(final boolean p0);
    
    public native NativePathNavigation setCanJump(final boolean p0);
    
    public native NativePathNavigation setCanOpenDoors(final boolean p0);
    
    public native NativePathNavigation setCanPassDoors(final boolean p0);
    
    public native NativePathNavigation setCanSink(final boolean p0);
    
    public native NativePathNavigation setEndPathRadius(final float p0);
    
    public native NativePathNavigation setIsAmphibious(final boolean p0);
    
    public native NativePathNavigation setIsRiverFollowing(final boolean p0);
    
    public native NativePathNavigation setMaxNavigationDistance(final float p0);
    
    public NativePathNavigation setResultFunction(final NavigationResultFunction resultFunction) {
        if (this.resultFunction != null) {
            this.resultFunction.onNavigationResult(this, 5);
        }
        this.resultFunction = resultFunction;
        return this;
    }
    
    public native NativePathNavigation setSpeed(final float p0);
    
    public native NativePathNavigation setType(final int p0);
    
    public native NativePathNavigation stop();
    
    interface NavigationResultFunction
    {
        void onNavigationResult(final NativePathNavigation p0, final int p1);
    }
}
