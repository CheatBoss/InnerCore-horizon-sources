package com.zhekasmirnov.apparatus.adapter.innercore.game.common;

public class Vector3
{
    public final float x;
    public final float y;
    public final float z;
    
    public Vector3(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3(final float[] array) {
        this(array[0], array[1], array[2]);
    }
    
    public float distance(final Vector3 vector3) {
        return (float)Math.sqrt(this.distanceSqr(vector3));
    }
    
    public float distanceSqr(final Vector3 vector3) {
        final float n = this.x - vector3.x;
        final float n2 = this.y - vector3.y;
        final float n3 = this.z - vector3.z;
        return n * n + n2 * n2 + n3 * n3;
    }
    
    public float length() {
        return (float)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }
    
    public float lengthSqr() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }
}
