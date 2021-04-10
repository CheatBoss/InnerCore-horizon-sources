package com.zhekasmirnov.innercore.api.entities;

public class NativeAttributeInstance
{
    private final String attribute;
    private final long entity;
    
    public NativeAttributeInstance(final long entity, final String attribute) {
        this.entity = entity;
        this.attribute = attribute;
    }
    
    private static native float getEntityAttributeDefaultValue(final long p0, final String p1);
    
    private static native float getEntityAttributeMaxValue(final long p0, final String p1);
    
    private static native float getEntityAttributeMinValue(final long p0, final String p1);
    
    private static native float getEntityAttributeValue(final long p0, final String p1);
    
    private static native void setEntityAttributeDefaultValue(final long p0, final String p1, final float p2);
    
    private static native void setEntityAttributeMaxValue(final long p0, final String p1, final float p2);
    
    private static native void setEntityAttributeMinValue(final long p0, final String p1, final float p2);
    
    private static native void setEntityAttributeValue(final long p0, final String p1, final float p2);
    
    public float getDefaultValue() {
        return getEntityAttributeDefaultValue(this.entity, this.attribute);
    }
    
    public float getMaxValue() {
        return getEntityAttributeMaxValue(this.entity, this.attribute);
    }
    
    public float getMinValue() {
        return getEntityAttributeMinValue(this.entity, this.attribute);
    }
    
    public float getValue() {
        return getEntityAttributeValue(this.entity, this.attribute);
    }
    
    public void setDefaultValue(final float n) {
        setEntityAttributeDefaultValue(this.entity, this.attribute, n);
    }
    
    public void setMaxValue(final float n) {
        setEntityAttributeMaxValue(this.entity, this.attribute, n);
    }
    
    public void setMinValue(final float n) {
        setEntityAttributeMinValue(this.entity, this.attribute, n);
    }
    
    public void setValue(final float n) {
        setEntityAttributeDefaultValue(this.entity, this.attribute, n);
    }
}
