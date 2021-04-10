package org.mozilla.javascript;

import java.io.*;

public class NativeWith implements Scriptable, IdFunctionCall, Serializable
{
    private static final Object FTAG;
    private static final int Id_constructor = 1;
    private static final long serialVersionUID = 1L;
    protected Scriptable parent;
    protected Scriptable prototype;
    
    static {
        FTAG = "With";
    }
    
    private NativeWith() {
    }
    
    protected NativeWith(final Scriptable parent, final Scriptable prototype) {
        this.parent = parent;
        this.prototype = prototype;
    }
    
    static void init(final Scriptable parentScope, final boolean b) {
        final NativeWith nativeWith = new NativeWith();
        nativeWith.setParentScope(parentScope);
        nativeWith.setPrototype(ScriptableObject.getObjectPrototype(parentScope));
        final IdFunctionObject idFunctionObject = new IdFunctionObject(nativeWith, NativeWith.FTAG, 1, "With", 0, parentScope);
        idFunctionObject.markAsConstructor(nativeWith);
        if (b) {
            idFunctionObject.sealObject();
        }
        idFunctionObject.exportAsScopeProperty();
    }
    
    static boolean isWithFunction(final Object o) {
        final boolean b = o instanceof IdFunctionObject;
        final boolean b2 = false;
        if (b) {
            final IdFunctionObject idFunctionObject = (IdFunctionObject)o;
            boolean b3 = b2;
            if (idFunctionObject.hasTag(NativeWith.FTAG)) {
                b3 = b2;
                if (idFunctionObject.methodId() == 1) {
                    b3 = true;
                }
            }
            return b3;
        }
        return false;
    }
    
    static Object newWithSpecial(final Context context, Scriptable topLevelScope, final Object[] array) {
        ScriptRuntime.checkDeprecated(context, "With");
        topLevelScope = ScriptableObject.getTopLevelScope(topLevelScope);
        final NativeWith nativeWith = new NativeWith();
        Scriptable prototype;
        if (array.length == 0) {
            prototype = ScriptableObject.getObjectPrototype(topLevelScope);
        }
        else {
            prototype = ScriptRuntime.toObject(context, topLevelScope, array[0]);
        }
        nativeWith.setPrototype(prototype);
        nativeWith.setParentScope(topLevelScope);
        return nativeWith;
    }
    
    @Override
    public void delete(final int n) {
        this.prototype.delete(n);
    }
    
    @Override
    public void delete(final String s) {
        this.prototype.delete(s);
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (idFunctionObject.hasTag(NativeWith.FTAG) && idFunctionObject.methodId() == 1) {
            throw Context.reportRuntimeError1("msg.cant.call.indirect", "With");
        }
        throw idFunctionObject.unknown();
    }
    
    @Override
    public Object get(final int n, final Scriptable scriptable) {
        Scriptable prototype = scriptable;
        if (scriptable == this) {
            prototype = this.prototype;
        }
        return this.prototype.get(n, prototype);
    }
    
    @Override
    public Object get(final String s, final Scriptable scriptable) {
        Scriptable prototype = scriptable;
        if (scriptable == this) {
            prototype = this.prototype;
        }
        return this.prototype.get(s, prototype);
    }
    
    @Override
    public String getClassName() {
        return "With";
    }
    
    @Override
    public Object getDefaultValue(final Class<?> clazz) {
        return this.prototype.getDefaultValue(clazz);
    }
    
    @Override
    public Object[] getIds() {
        return this.prototype.getIds();
    }
    
    @Override
    public Scriptable getParentScope() {
        return this.parent;
    }
    
    @Override
    public Scriptable getPrototype() {
        return this.prototype;
    }
    
    @Override
    public boolean has(final int n, final Scriptable scriptable) {
        return this.prototype.has(n, this.prototype);
    }
    
    @Override
    public boolean has(final String s, final Scriptable scriptable) {
        return this.prototype.has(s, this.prototype);
    }
    
    @Override
    public boolean hasInstance(final Scriptable scriptable) {
        return this.prototype.hasInstance(scriptable);
    }
    
    @Override
    public void put(final int n, final Scriptable scriptable, final Object o) {
        Scriptable prototype = scriptable;
        if (scriptable == this) {
            prototype = this.prototype;
        }
        this.prototype.put(n, prototype, o);
    }
    
    @Override
    public void put(final String s, final Scriptable scriptable, final Object o) {
        Scriptable prototype = scriptable;
        if (scriptable == this) {
            prototype = this.prototype;
        }
        this.prototype.put(s, prototype, o);
    }
    
    @Override
    public void setParentScope(final Scriptable parent) {
        this.parent = parent;
    }
    
    @Override
    public void setPrototype(final Scriptable prototype) {
        this.prototype = prototype;
    }
    
    protected Object updateDotQuery(final boolean b) {
        throw new IllegalStateException();
    }
}
