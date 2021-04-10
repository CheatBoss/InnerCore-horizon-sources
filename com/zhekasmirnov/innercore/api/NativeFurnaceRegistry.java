package com.zhekasmirnov.innercore.api;

public class NativeFurnaceRegistry
{
    public static native void nativeAddFuel(final int p0, final int p1, final int p2);
    
    public static native void nativeAddRecipe(final int p0, final int p1, final int p2, final int p3);
    
    public static native void nativeRemoveFuel(final int p0, final int p1);
    
    public static native void nativeRemoveRecipe(final int p0, final int p1);
}
