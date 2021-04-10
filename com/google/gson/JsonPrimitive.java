package com.google.gson;

import java.math.*;
import com.google.gson.internal.*;

public final class JsonPrimitive extends JsonElement
{
    private static final Class<?>[] PRIMITIVE_TYPES;
    private Object value;
    
    static {
        PRIMITIVE_TYPES = new Class[] { Integer.TYPE, Long.TYPE, Short.TYPE, Float.TYPE, Double.TYPE, Byte.TYPE, Boolean.TYPE, Character.TYPE, Integer.class, Long.class, Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class };
    }
    
    public JsonPrimitive(final Boolean value) {
        this.setValue(value);
    }
    
    public JsonPrimitive(final Number value) {
        this.setValue(value);
    }
    
    public JsonPrimitive(final String value) {
        this.setValue(value);
    }
    
    private static boolean isIntegral(final JsonPrimitive jsonPrimitive) {
        final Object value = jsonPrimitive.value;
        if (value instanceof Number) {
            final Number n = (Number)value;
            if (n instanceof BigInteger || n instanceof Long || n instanceof Integer || n instanceof Short || n instanceof Byte) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean isPrimitiveOrString(final Object o) {
        if (o instanceof String) {
            return true;
        }
        final Class<?> class1 = o.getClass();
        final Class<?>[] primitive_TYPES = JsonPrimitive.PRIMITIVE_TYPES;
        for (int length = primitive_TYPES.length, i = 0; i < length; ++i) {
            if (primitive_TYPES[i].isAssignableFrom(class1)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final JsonPrimitive jsonPrimitive = (JsonPrimitive)o;
        if (this.value == null) {
            return jsonPrimitive.value == null;
        }
        if (isIntegral(this) && isIntegral(jsonPrimitive)) {
            return this.getAsNumber().longValue() == jsonPrimitive.getAsNumber().longValue();
        }
        if (this.value instanceof Number && jsonPrimitive.value instanceof Number) {
            final double doubleValue = this.getAsNumber().doubleValue();
            final double doubleValue2 = jsonPrimitive.getAsNumber().doubleValue();
            if (doubleValue != doubleValue2) {
                if (Double.isNaN(doubleValue) && Double.isNaN(doubleValue2)) {
                    return true;
                }
                b = false;
            }
            return b;
        }
        return this.value.equals(jsonPrimitive.value);
    }
    
    @Override
    public boolean getAsBoolean() {
        if (this.isBoolean()) {
            return this.getAsBooleanWrapper();
        }
        return Boolean.parseBoolean(this.getAsString());
    }
    
    @Override
    Boolean getAsBooleanWrapper() {
        return (Boolean)this.value;
    }
    
    @Override
    public Number getAsNumber() {
        final Object value = this.value;
        if (value instanceof String) {
            return new LazilyParsedNumber((String)this.value);
        }
        return (Number)value;
    }
    
    @Override
    public String getAsString() {
        if (this.isNumber()) {
            return this.getAsNumber().toString();
        }
        if (this.isBoolean()) {
            return this.getAsBooleanWrapper().toString();
        }
        return (String)this.value;
    }
    
    @Override
    public int hashCode() {
        if (this.value == null) {
            return 31;
        }
        long n;
        if (isIntegral(this)) {
            n = this.getAsNumber().longValue();
        }
        else {
            final Object value = this.value;
            if (!(value instanceof Number)) {
                return value.hashCode();
            }
            n = Double.doubleToLongBits(this.getAsNumber().doubleValue());
        }
        return (int)(n >>> 32 ^ n);
    }
    
    public boolean isBoolean() {
        return this.value instanceof Boolean;
    }
    
    public boolean isNumber() {
        return this.value instanceof Number;
    }
    
    public boolean isString() {
        return this.value instanceof String;
    }
    
    void setValue(Object value) {
        if (value instanceof Character) {
            value = String.valueOf((char)value);
        }
        else {
            $Gson$Preconditions.checkArgument(value instanceof Number || isPrimitiveOrString(value));
        }
        this.value = value;
    }
}
