package org.mozilla.javascript;

public class Delegator implements Function
{
    protected Scriptable obj;
    
    public Delegator() {
        this.obj = null;
    }
    
    public Delegator(final Scriptable obj) {
        this.obj = null;
        this.obj = obj;
    }
    
    @Override
    public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        return ((Function)this.obj).call(context, scriptable, scriptable2, array);
    }
    
    @Override
    public Scriptable construct(final Context context, final Scriptable scriptable, final Object[] array) {
        if (this.obj == null) {
            final Delegator instance = this.newInstance();
            Scriptable object;
            if (array.length == 0) {
                object = new NativeObject();
            }
            else {
                object = ScriptRuntime.toObject(context, scriptable, array[0]);
            }
            instance.setDelegee(object);
            return instance;
        }
        return ((Function)this.obj).construct(context, scriptable, array);
    }
    
    @Override
    public void delete(final int n) {
        this.obj.delete(n);
    }
    
    @Override
    public void delete(final String s) {
        this.obj.delete(s);
    }
    
    @Override
    public Object get(final int n, final Scriptable scriptable) {
        return this.obj.get(n, scriptable);
    }
    
    @Override
    public Object get(final String s, final Scriptable scriptable) {
        return this.obj.get(s, scriptable);
    }
    
    @Override
    public String getClassName() {
        return this.obj.getClassName();
    }
    
    @Override
    public Object getDefaultValue(final Class<?> clazz) {
        if (clazz != null && clazz != ScriptRuntime.ScriptableClass && clazz != ScriptRuntime.FunctionClass) {
            return this.obj.getDefaultValue(clazz);
        }
        return this;
    }
    
    public Scriptable getDelegee() {
        return this.obj;
    }
    
    @Override
    public Object[] getIds() {
        return this.obj.getIds();
    }
    
    @Override
    public Scriptable getParentScope() {
        return this.obj.getParentScope();
    }
    
    @Override
    public Scriptable getPrototype() {
        return this.obj.getPrototype();
    }
    
    @Override
    public boolean has(final int n, final Scriptable scriptable) {
        return this.obj.has(n, scriptable);
    }
    
    @Override
    public boolean has(final String s, final Scriptable scriptable) {
        return this.obj.has(s, scriptable);
    }
    
    @Override
    public boolean hasInstance(final Scriptable scriptable) {
        return this.obj.hasInstance(scriptable);
    }
    
    protected Delegator newInstance() {
        try {
            return (Delegator)this.getClass().newInstance();
        }
        catch (Exception ex) {
            throw Context.throwAsScriptRuntimeEx(ex);
        }
    }
    
    @Override
    public void put(final int n, final Scriptable scriptable, final Object o) {
        this.obj.put(n, scriptable, o);
    }
    
    @Override
    public void put(final String s, final Scriptable scriptable, final Object o) {
        this.obj.put(s, scriptable, o);
    }
    
    public void setDelegee(final Scriptable obj) {
        this.obj = obj;
    }
    
    @Override
    public void setParentScope(final Scriptable parentScope) {
        this.obj.setParentScope(parentScope);
    }
    
    @Override
    public void setPrototype(final Scriptable prototype) {
        this.obj.setPrototype(prototype);
    }
}
