package org.mozilla.javascript;

public interface Scriptable
{
    public static final Object NOT_FOUND = UniqueTag.NOT_FOUND;
    
    void delete(final int p0);
    
    void delete(final String p0);
    
    Object get(final int p0, final Scriptable p1);
    
    Object get(final String p0, final Scriptable p1);
    
    String getClassName();
    
    Object getDefaultValue(final Class<?> p0);
    
    Object[] getIds();
    
    Scriptable getParentScope();
    
    Scriptable getPrototype();
    
    boolean has(final int p0, final Scriptable p1);
    
    boolean has(final String p0, final Scriptable p1);
    
    boolean hasInstance(final Scriptable p0);
    
    void put(final int p0, final Scriptable p1, final Object p2);
    
    void put(final String p0, final Scriptable p1, final Object p2);
    
    void setParentScope(final Scriptable p0);
    
    void setPrototype(final Scriptable p0);
}
