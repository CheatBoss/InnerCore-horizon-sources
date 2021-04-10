package com.zhekasmirnov.apparatus.adapter.innercore.game.entity;

import com.zhekasmirnov.apparatus.adapter.innercore.game.item.*;
import com.zhekasmirnov.innercore.api.nbt.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.common.*;
import com.zhekasmirnov.innercore.api.*;

public class StaticEntity
{
    public static boolean exists(final long n) {
        return NativeAPI.isValidEntity(n);
    }
    
    public static ItemStack getCarriedItem(final long n) {
        return new ItemStack(new NativeItemInstance(NativeAPI.getEntityCarriedItem(n)));
    }
    
    public static int getDimension(final long n) {
        return NativeAPI.getEntityDimension(n);
    }
    
    public static ItemStack getDroppedItem(final long n) {
        return new ItemStack(new NativeItemInstance(NativeAPI.getItemFromDrop(n)));
    }
    
    public static int getExperienceOrbValue(long entityCompoundTag) {
        entityCompoundTag = NativeAPI.getEntityCompoundTag(entityCompoundTag);
        if (entityCompoundTag != 0L) {
            return new NativeCompoundTag(entityCompoundTag).getInt("experience value");
        }
        return 0;
    }
    
    public static Vector3 getPosition(final long n) {
        final float[] array = new float[3];
        NativeAPI.getPosition(n, array);
        return new Vector3(array);
    }
    
    public static int getType(final long n) {
        return NativeAPI.getEntityType(n);
    }
    
    public static Vector3 getVelocity(final long n) {
        final float[] array = new float[3];
        NativeAPI.getVelocity(n, array);
        return new Vector3(array);
    }
    
    public static EntityActor newDroppedItem(final float n, final float n2, final float n3, final int n4, final int n5, final int n6, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        return new EntityActor(NativeAPI.spawnDroppedItem(n, n2, n3, n4, n5, n6, NativeItemInstanceExtra.getValueOrNullPtr(nativeItemInstanceExtra)));
    }
}
