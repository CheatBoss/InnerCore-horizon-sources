package org.mozilla.javascript.tools.shell;

import org.mozilla.javascript.*;
import java.util.*;

public class Environment extends ScriptableObject
{
    static final long serialVersionUID = -430727378460177065L;
    private Environment thePrototypeInstance;
    
    public Environment() {
        this.thePrototypeInstance = null;
        if (this.thePrototypeInstance == null) {
            this.thePrototypeInstance = this;
        }
    }
    
    public Environment(final ScriptableObject parentScope) {
        this.thePrototypeInstance = null;
        this.setParentScope(parentScope);
        final Object topLevelProp = ScriptRuntime.getTopLevelProp(parentScope, "Environment");
        if (topLevelProp != null && topLevelProp instanceof Scriptable) {
            final Scriptable scriptable = (Scriptable)topLevelProp;
            this.setPrototype((Scriptable)scriptable.get("prototype", scriptable));
        }
    }
    
    private Object[] collectIds() {
        return ((Map<Object, V>)System.getProperties()).keySet().toArray();
    }
    
    public static void defineClass(final ScriptableObject scriptableObject) {
        try {
            ScriptableObject.defineClass(scriptableObject, Environment.class);
        }
        catch (Exception ex) {
            throw new Error(ex.getMessage());
        }
    }
    
    @Override
    public Object get(String property, final Scriptable scriptable) {
        if (this == this.thePrototypeInstance) {
            return super.get(property, scriptable);
        }
        property = System.getProperty(property);
        if (property != null) {
            return ScriptRuntime.toObject(this.getParentScope(), property);
        }
        return Scriptable.NOT_FOUND;
    }
    
    @Override
    public Object[] getAllIds() {
        if (this == this.thePrototypeInstance) {
            return super.getAllIds();
        }
        return this.collectIds();
    }
    
    @Override
    public String getClassName() {
        return "Environment";
    }
    
    @Override
    public Object[] getIds() {
        if (this == this.thePrototypeInstance) {
            return super.getIds();
        }
        return this.collectIds();
    }
    
    @Override
    public boolean has(final String s, final Scriptable scriptable) {
        if (this == this.thePrototypeInstance) {
            return super.has(s, scriptable);
        }
        return System.getProperty(s) != null;
    }
    
    @Override
    public void put(final String s, final Scriptable scriptable, final Object o) {
        if (this == this.thePrototypeInstance) {
            super.put(s, scriptable, o);
            return;
        }
        ((Hashtable<String, String>)System.getProperties()).put(s, ScriptRuntime.toString(o));
    }
}
