package org.mozilla.javascript;

final class NativeBoolean extends IdScriptableObject
{
    private static final Object BOOLEAN_TAG;
    private static final int Id_constructor = 1;
    private static final int Id_toSource = 3;
    private static final int Id_toString = 2;
    private static final int Id_valueOf = 4;
    private static final int MAX_PROTOTYPE_ID = 4;
    static final long serialVersionUID = -3716996899943880933L;
    private boolean booleanValue;
    
    static {
        BOOLEAN_TAG = "Boolean";
    }
    
    NativeBoolean(final boolean booleanValue) {
        this.booleanValue = booleanValue;
    }
    
    static void init(final Scriptable scriptable, final boolean b) {
        new NativeBoolean(false).exportAsJSClass(4, scriptable, b);
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(NativeBoolean.BOOLEAN_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        boolean boolean1 = true;
        if (methodId == 1) {
            if (array.length == 0) {
                boolean1 = false;
            }
            else if (!(array[0] instanceof ScriptableObject) || !((ScriptableObject)array[0]).avoidObjectDetection()) {
                boolean1 = ScriptRuntime.toBoolean(array[0]);
            }
            if (scriptable2 == null) {
                return new NativeBoolean(boolean1);
            }
            return ScriptRuntime.wrapBoolean(boolean1);
        }
        else {
            if (!(scriptable2 instanceof NativeBoolean)) {
                throw IdScriptableObject.incompatibleCallError(idFunctionObject);
            }
            final boolean booleanValue = ((NativeBoolean)scriptable2).booleanValue;
            switch (methodId) {
                default: {
                    throw new IllegalArgumentException(String.valueOf(methodId));
                }
                case 4: {
                    return ScriptRuntime.wrapBoolean(booleanValue);
                }
                case 3: {
                    if (booleanValue) {
                        return "(new Boolean(true))";
                    }
                    return "(new Boolean(false))";
                }
                case 2: {
                    if (booleanValue) {
                        return "true";
                    }
                    return "false";
                }
            }
        }
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        int n = 0;
        String s2 = null;
        final int length = s.length();
        if (length == 7) {
            s2 = "valueOf";
            n = 4;
        }
        else if (length == 8) {
            final char char1 = s.charAt(3);
            if (char1 == 'o') {
                s2 = "toSource";
                n = 3;
            }
            else if (char1 == 't') {
                s2 = "toString";
                n = 2;
            }
        }
        else if (length == 11) {
            s2 = "constructor";
            n = 1;
        }
        int n2 = n;
        if (s2 != null) {
            n2 = n;
            if (s2 != s) {
                n2 = n;
                if (!s2.equals(s)) {
                    n2 = 0;
                }
            }
        }
        return n2;
    }
    
    @Override
    public String getClassName() {
        return "Boolean";
    }
    
    @Override
    public Object getDefaultValue(final Class<?> clazz) {
        if (clazz == ScriptRuntime.BooleanClass) {
            return ScriptRuntime.wrapBoolean(this.booleanValue);
        }
        return super.getDefaultValue(clazz);
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        int n2 = 0;
        String s = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case 4: {
                n2 = 0;
                s = "valueOf";
                break;
            }
            case 3: {
                n2 = 0;
                s = "toSource";
                break;
            }
            case 2: {
                n2 = 0;
                s = "toString";
                break;
            }
            case 1: {
                n2 = 1;
                s = "constructor";
                break;
            }
        }
        this.initPrototypeMethod(NativeBoolean.BOOLEAN_TAG, n, s, n2);
    }
}
