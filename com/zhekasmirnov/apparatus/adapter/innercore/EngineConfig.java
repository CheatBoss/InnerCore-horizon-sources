package com.zhekasmirnov.apparatus.adapter.innercore;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.innercore.api.*;

@SynthesizedClassMap({ -$$Lambda$EngineConfig$pQvs5NIc3cMFNVI00i5Gc67Gizg.class, -$$Lambda$EngineConfig$DK3bvXDgQWWHDrfJIYUC-FUtLGQ.class })
public class EngineConfig
{
    public static <T> T get(final String s, final Class<T> clazz, final PropertyValidator<T> propertyValidator) {
        final Object value = InnerCoreConfig.get(s);
        if (propertyValidator != null) {
            try {
                return propertyValidator.validate((T)value);
            }
            catch (ClassCastException ex) {
                T validate = null;
                if (propertyValidator != null) {
                    validate = propertyValidator.validate(null);
                }
                return validate;
            }
        }
        return (T)value;
    }
    
    public static boolean getBoolean(final String s, final boolean b) {
        return get(s, Boolean.class, new -$$Lambda$EngineConfig$DK3bvXDgQWWHDrfJIYUC-FUtLGQ(b));
    }
    
    public static double getDouble(final String s, final double n) {
        return getNumber(s, n).doubleValue();
    }
    
    public static float getFloat(final String s, final float n) {
        return getNumber(s, n).floatValue();
    }
    
    public static int getInt(final String s, final int n) {
        return getNumber(s, n).intValue();
    }
    
    public static long getLong(final String s, final long n) {
        return getNumber(s, n).longValue();
    }
    
    public static Number getNumber(final String s, final Number n) {
        return get(s, Number.class, new -$$Lambda$EngineConfig$pQvs5NIc3cMFNVI00i5Gc67Gizg(n));
    }
    
    public static boolean isDeveloperMode() {
        return InnerCoreConfig.getBool("developer_mode");
    }
    
    public interface PropertyValidator<T>
    {
        T validate(final T p0);
    }
}
