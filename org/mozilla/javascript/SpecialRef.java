package org.mozilla.javascript;

class SpecialRef extends Ref
{
    private static final int SPECIAL_NONE = 0;
    private static final int SPECIAL_PARENT = 2;
    private static final int SPECIAL_PROTO = 1;
    static final long serialVersionUID = -7521596632456797847L;
    private String name;
    private Scriptable target;
    private int type;
    
    private SpecialRef(final Scriptable target, final int type, final String name) {
        this.target = target;
        this.type = type;
        this.name = name;
    }
    
    static Ref createSpecial(final Context context, Scriptable objectOrNull, final Object o, final String s) {
        objectOrNull = ScriptRuntime.toObjectOrNull(context, o, objectOrNull);
        if (objectOrNull == null) {
            throw ScriptRuntime.undefReadError(o, s);
        }
        int n;
        if (s.equals("__proto__")) {
            n = 1;
        }
        else {
            if (!s.equals("__parent__")) {
                throw new IllegalArgumentException(s);
            }
            n = 2;
        }
        if (!context.hasFeature(5)) {
            n = 0;
        }
        return new SpecialRef(objectOrNull, n, s);
    }
    
    @Override
    public boolean delete(final Context context) {
        return this.type == 0 && ScriptRuntime.deleteObjectElem(this.target, this.name, context);
    }
    
    @Override
    public Object get(final Context context) {
        switch (this.type) {
            default: {
                throw Kit.codeBug();
            }
            case 2: {
                return this.target.getParentScope();
            }
            case 1: {
                return this.target.getPrototype();
            }
            case 0: {
                return ScriptRuntime.getObjectProp(this.target, this.name, context);
            }
        }
    }
    
    @Override
    public boolean has(final Context context) {
        return this.type != 0 || ScriptRuntime.hasObjectElem(this.target, this.name, context);
    }
    
    @Deprecated
    @Override
    public Object set(final Context context, final Object o) {
        throw new IllegalStateException();
    }
    
    @Override
    public Object set(final Context context, Scriptable scriptable, final Object o) {
        switch (this.type) {
            default: {
                throw Kit.codeBug();
            }
            case 1:
            case 2: {
                final Scriptable objectOrNull = ScriptRuntime.toObjectOrNull(context, o, scriptable);
                Label_0098: {
                    if (objectOrNull != null) {
                        scriptable = objectOrNull;
                        while (scriptable != this.target) {
                            Scriptable scriptable2;
                            if (this.type == 1) {
                                scriptable2 = scriptable.getPrototype();
                            }
                            else {
                                scriptable2 = scriptable.getParentScope();
                            }
                            scriptable = scriptable2;
                            if (scriptable2 == null) {
                                break Label_0098;
                            }
                        }
                        throw Context.reportRuntimeError1("msg.cyclic.value", this.name);
                    }
                }
                if (this.type == 1) {
                    this.target.setPrototype(objectOrNull);
                    return objectOrNull;
                }
                this.target.setParentScope(objectOrNull);
                return objectOrNull;
            }
            case 0: {
                return ScriptRuntime.setObjectProp(this.target, this.name, o, context);
            }
        }
    }
}
