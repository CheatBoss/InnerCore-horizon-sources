package org.mozilla.javascript;

public class NativeCallSite extends IdScriptableObject
{
    private static final String CALLSITE_TAG = "CallSite";
    private static final int Id_constructor = 1;
    private static final int Id_getColumnNumber = 9;
    private static final int Id_getEvalOrigin = 10;
    private static final int Id_getFileName = 7;
    private static final int Id_getFunction = 4;
    private static final int Id_getFunctionName = 5;
    private static final int Id_getLineNumber = 8;
    private static final int Id_getMethodName = 6;
    private static final int Id_getThis = 2;
    private static final int Id_getTypeName = 3;
    private static final int Id_isConstructor = 14;
    private static final int Id_isEval = 12;
    private static final int Id_isNative = 13;
    private static final int Id_isToplevel = 11;
    private static final int Id_toString = 15;
    private static final int MAX_PROTOTYPE_ID = 15;
    private ScriptStackElement element;
    
    private NativeCallSite() {
    }
    
    private Object getFalse() {
        return Boolean.FALSE;
    }
    
    private Object getFileName(Scriptable prototype) {
        while (prototype != null && !(prototype instanceof NativeCallSite)) {
            prototype = prototype.getPrototype();
        }
        if (prototype == null) {
            return NativeCallSite.NOT_FOUND;
        }
        final NativeCallSite nativeCallSite = (NativeCallSite)prototype;
        if (nativeCallSite.element == null) {
            return null;
        }
        return nativeCallSite.element.fileName;
    }
    
    private Object getFunctionName(Scriptable prototype) {
        while (prototype != null && !(prototype instanceof NativeCallSite)) {
            prototype = prototype.getPrototype();
        }
        if (prototype == null) {
            return NativeCallSite.NOT_FOUND;
        }
        final NativeCallSite nativeCallSite = (NativeCallSite)prototype;
        if (nativeCallSite.element == null) {
            return null;
        }
        return nativeCallSite.element.functionName;
    }
    
    private Object getLineNumber(Scriptable prototype) {
        while (prototype != null && !(prototype instanceof NativeCallSite)) {
            prototype = prototype.getPrototype();
        }
        if (prototype == null) {
            return NativeCallSite.NOT_FOUND;
        }
        final NativeCallSite nativeCallSite = (NativeCallSite)prototype;
        if (nativeCallSite.element != null && nativeCallSite.element.lineNumber >= 0) {
            return nativeCallSite.element.lineNumber;
        }
        return Undefined.instance;
    }
    
    private Object getNull() {
        return null;
    }
    
    private Object getUndefined() {
        return Undefined.instance;
    }
    
    static void init(final Scriptable scriptable, final boolean b) {
        new NativeCallSite().exportAsJSClass(15, scriptable, b);
    }
    
    private Object js_toString(Scriptable prototype) {
        while (prototype != null && !(prototype instanceof NativeCallSite)) {
            prototype = prototype.getPrototype();
        }
        if (prototype == null) {
            return NativeCallSite.NOT_FOUND;
        }
        final NativeCallSite nativeCallSite = (NativeCallSite)prototype;
        final StringBuilder sb = new StringBuilder();
        nativeCallSite.element.renderJavaStyle(sb);
        return sb.toString();
    }
    
    static NativeCallSite make(final Scriptable parentScope, Scriptable prototype) {
        final NativeCallSite nativeCallSite = new NativeCallSite();
        prototype = (Scriptable)prototype.get("prototype", prototype);
        nativeCallSite.setParentScope(parentScope);
        nativeCallSite.setPrototype(prototype);
        return nativeCallSite;
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag("CallSite")) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        switch (methodId) {
            default: {
                throw new IllegalArgumentException(String.valueOf(methodId));
            }
            case 15: {
                return this.js_toString(scriptable2);
            }
            case 10:
            case 12:
            case 14: {
                return this.getFalse();
            }
            case 8: {
                return this.getLineNumber(scriptable2);
            }
            case 7: {
                return this.getFileName(scriptable2);
            }
            case 6: {
                return this.getNull();
            }
            case 5: {
                return this.getFunctionName(scriptable2);
            }
            case 2:
            case 3:
            case 4:
            case 9: {
                return this.getUndefined();
            }
            case 1: {
                return make(scriptable, idFunctionObject);
            }
        }
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        int n = 0;
        String s2 = null;
        Label_0332: {
            switch (s.length()) {
                case 15: {
                    final char char1 = s.charAt(3);
                    if (char1 == 'C') {
                        s2 = "getColumnNumber";
                        n = 9;
                        break;
                    }
                    if (char1 == 'F') {
                        s2 = "getFunctionName";
                        n = 5;
                        break;
                    }
                    break;
                }
                case 13: {
                    final char char2 = s.charAt(3);
                    if (char2 == 'E') {
                        s2 = "getEvalOrigin";
                        n = 10;
                        break;
                    }
                    if (char2 == 'o') {
                        s2 = "isConstructor";
                        n = 14;
                        break;
                    }
                    switch (char2) {
                        default: {
                            break Label_0332;
                        }
                        case 77: {
                            s2 = "getMethodName";
                            n = 6;
                            break Label_0332;
                        }
                        case 76: {
                            s2 = "getLineNumber";
                            n = 8;
                            break Label_0332;
                        }
                    }
                    break;
                }
                case 11: {
                    final char char3 = s.charAt(4);
                    if (char3 == 'i') {
                        s2 = "getFileName";
                        n = 7;
                        break;
                    }
                    if (char3 == 'y') {
                        s2 = "getTypeName";
                        n = 3;
                        break;
                    }
                    switch (char3) {
                        default: {
                            break Label_0332;
                        }
                        case 117: {
                            s2 = "getFunction";
                            n = 4;
                            break Label_0332;
                        }
                        case 116: {
                            s2 = "constructor";
                            n = 1;
                            break Label_0332;
                        }
                    }
                    break;
                }
                case 10: {
                    s2 = "isToplevel";
                    n = 11;
                    break;
                }
                case 8: {
                    final char char4 = s.charAt(0);
                    if (char4 == 'i') {
                        s2 = "isNative";
                        n = 13;
                        break;
                    }
                    if (char4 == 't') {
                        s2 = "toString";
                        n = 15;
                        break;
                    }
                    break;
                }
                case 7: {
                    s2 = "getThis";
                    n = 2;
                    break;
                }
                case 6: {
                    s2 = "isEval";
                    n = 12;
                    break;
                }
            }
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
        return "CallSite";
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        String s = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case 15: {
                s = "toString";
                break;
            }
            case 14: {
                s = "isConstructor";
                break;
            }
            case 13: {
                s = "isNative";
                break;
            }
            case 12: {
                s = "isEval";
                break;
            }
            case 11: {
                s = "isToplevel";
                break;
            }
            case 10: {
                s = "getEvalOrigin";
                break;
            }
            case 9: {
                s = "getColumnNumber";
                break;
            }
            case 8: {
                s = "getLineNumber";
                break;
            }
            case 7: {
                s = "getFileName";
                break;
            }
            case 6: {
                s = "getMethodName";
                break;
            }
            case 5: {
                s = "getFunctionName";
                break;
            }
            case 4: {
                s = "getFunction";
                break;
            }
            case 3: {
                s = "getTypeName";
                break;
            }
            case 2: {
                s = "getThis";
                break;
            }
            case 1: {
                s = "constructor";
                break;
            }
        }
        this.initPrototypeMethod("CallSite", n, s, 0);
    }
    
    void setElement(final ScriptStackElement element) {
        this.element = element;
    }
    
    @Override
    public String toString() {
        if (this.element == null) {
            return "";
        }
        return this.element.toString();
    }
}
