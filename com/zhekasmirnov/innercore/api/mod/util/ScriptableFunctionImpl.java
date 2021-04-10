package com.zhekasmirnov.innercore.api.mod.util;

import org.mozilla.javascript.*;

public abstract class ScriptableFunctionImpl implements Function
{
    public Scriptable construct(final Context context, final Scriptable scriptable, final Object[] array) {
        return null;
    }
    
    public void delete(final int n) {
    }
    
    public void delete(final String s) {
    }
    
    public Object get(final int n, final Scriptable scriptable) {
        return null;
    }
    
    public Object get(final String s, final Scriptable scriptable) {
        return null;
    }
    
    public String getClassName() {
        return null;
    }
    
    public Object getDefaultValue(final Class<?> clazz) {
        return null;
    }
    
    public Object[] getIds() {
        return new Object[0];
    }
    
    public Scriptable getParentScope() {
        return null;
    }
    
    public Scriptable getPrototype() {
        return null;
    }
    
    public boolean has(final int n, final Scriptable scriptable) {
        return false;
    }
    
    public boolean has(final String s, final Scriptable scriptable) {
        return false;
    }
    
    public boolean hasInstance(final Scriptable scriptable) {
        return false;
    }
    
    public void put(final int n, final Scriptable scriptable, final Object o) {
    }
    
    public void put(final String s, final Scriptable scriptable, final Object o) {
    }
    
    public void setParentScope(final Scriptable scriptable) {
    }
    
    public void setPrototype(final Scriptable scriptable) {
    }
}
