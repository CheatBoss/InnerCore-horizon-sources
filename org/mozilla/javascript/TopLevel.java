package org.mozilla.javascript;

import java.util.*;

public class TopLevel extends IdScriptableObject
{
    static final long serialVersionUID = -4648046356662472260L;
    private EnumMap<Builtins, BaseFunction> ctors;
    private EnumMap<NativeErrors, BaseFunction> errors;
    
    public static Function getBuiltinCtor(final Context context, final Scriptable scriptable, final Builtins builtins) {
        if (scriptable instanceof TopLevel) {
            final BaseFunction builtinCtor = ((TopLevel)scriptable).getBuiltinCtor(builtins);
            if (builtinCtor != null) {
                return builtinCtor;
            }
        }
        return ScriptRuntime.getExistingCtor(context, scriptable, builtins.name());
    }
    
    public static Scriptable getBuiltinPrototype(final Scriptable scriptable, final Builtins builtins) {
        if (scriptable instanceof TopLevel) {
            final Scriptable builtinPrototype = ((TopLevel)scriptable).getBuiltinPrototype(builtins);
            if (builtinPrototype != null) {
                return builtinPrototype;
            }
        }
        return ScriptableObject.getClassPrototype(scriptable, builtins.name());
    }
    
    static Function getNativeErrorCtor(final Context context, final Scriptable scriptable, final NativeErrors nativeErrors) {
        if (scriptable instanceof TopLevel) {
            final BaseFunction nativeErrorCtor = ((TopLevel)scriptable).getNativeErrorCtor(nativeErrors);
            if (nativeErrorCtor != null) {
                return nativeErrorCtor;
            }
        }
        return ScriptRuntime.getExistingCtor(context, scriptable, nativeErrors.name());
    }
    
    public void cacheBuiltins() {
        this.ctors = new EnumMap<Builtins, BaseFunction>(Builtins.class);
        final Builtins[] values = Builtins.values();
        final int length = values.length;
        final int n = 0;
        for (int i = 0; i < length; ++i) {
            final Builtins builtins = values[i];
            final Object property = ScriptableObject.getProperty(this, builtins.name());
            if (property instanceof BaseFunction) {
                this.ctors.put(builtins, (BaseFunction)property);
            }
        }
        this.errors = new EnumMap<NativeErrors, BaseFunction>(NativeErrors.class);
        final NativeErrors[] values2 = NativeErrors.values();
        for (int length2 = values2.length, j = n; j < length2; ++j) {
            final NativeErrors nativeErrors = values2[j];
            final Object property2 = ScriptableObject.getProperty(this, nativeErrors.name());
            if (property2 instanceof BaseFunction) {
                this.errors.put(nativeErrors, (BaseFunction)property2);
            }
        }
    }
    
    public BaseFunction getBuiltinCtor(final Builtins builtins) {
        if (this.ctors != null) {
            return this.ctors.get(builtins);
        }
        return null;
    }
    
    public Scriptable getBuiltinPrototype(final Builtins builtins) {
        final BaseFunction builtinCtor = this.getBuiltinCtor(builtins);
        Scriptable scriptable = null;
        Object prototypeProperty;
        if (builtinCtor != null) {
            prototypeProperty = builtinCtor.getPrototypeProperty();
        }
        else {
            prototypeProperty = null;
        }
        if (prototypeProperty instanceof Scriptable) {
            scriptable = (Scriptable)prototypeProperty;
        }
        return scriptable;
    }
    
    @Override
    public String getClassName() {
        return "global";
    }
    
    BaseFunction getNativeErrorCtor(final NativeErrors nativeErrors) {
        if (this.errors != null) {
            return this.errors.get(nativeErrors);
        }
        return null;
    }
    
    public enum Builtins
    {
        Array, 
        Boolean, 
        Error, 
        Function, 
        Number, 
        Object, 
        RegExp, 
        String;
    }
    
    enum NativeErrors
    {
        Error, 
        EvalError, 
        InternalError, 
        JavaException, 
        RangeError, 
        ReferenceError, 
        SyntaxError, 
        TypeError, 
        URIError;
    }
}
