package org.mozilla.javascript;

class NativeScript extends BaseFunction
{
    private static final int Id_compile = 3;
    private static final int Id_constructor = 1;
    private static final int Id_exec = 4;
    private static final int Id_toString = 2;
    private static final int MAX_PROTOTYPE_ID = 4;
    private static final Object SCRIPT_TAG;
    static final long serialVersionUID = -6795101161980121700L;
    private Script script;
    
    static {
        SCRIPT_TAG = "Script";
    }
    
    private NativeScript(final Script script) {
        this.script = script;
    }
    
    private static Script compile(final Context context, final String s) {
        final int[] array = { 0 };
        String sourcePositionFromStack;
        if ((sourcePositionFromStack = Context.getSourcePositionFromStack(array)) == null) {
            sourcePositionFromStack = "<Script object>";
            array[0] = 1;
        }
        return context.compileString(s, null, DefaultErrorReporter.forEval(context.getErrorReporter()), sourcePositionFromStack, array[0], null);
    }
    
    static void init(final Scriptable scriptable, final boolean b) {
        new NativeScript(null).exportAsJSClass(4, scriptable, b);
    }
    
    private static NativeScript realThis(final Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        if (!(scriptable instanceof NativeScript)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        return (NativeScript)scriptable;
    }
    
    @Override
    public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (this.script != null) {
            return this.script.exec(context, scriptable);
        }
        return Undefined.instance;
    }
    
    @Override
    public Scriptable construct(final Context context, final Scriptable scriptable, final Object[] array) {
        throw Context.reportRuntimeError0("msg.script.is.not.constructor");
    }
    
    @Override
    String decompile(final int n, final int n2) {
        if (this.script instanceof NativeFunction) {
            return ((NativeFunction)this.script).decompile(n, n2);
        }
        return super.decompile(n, n2);
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(NativeScript.SCRIPT_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        switch (methodId) {
            default: {
                throw new IllegalArgumentException(String.valueOf(methodId));
            }
            case 4: {
                throw Context.reportRuntimeError1("msg.cant.call.indirect", "exec");
            }
            case 3: {
                final NativeScript realThis = realThis(scriptable2, idFunctionObject);
                realThis.script = compile(context, ScriptRuntime.toString(array, 0));
                return realThis;
            }
            case 2: {
                final Script script = realThis(scriptable2, idFunctionObject).script;
                if (script == null) {
                    return "";
                }
                return context.decompileScript(script, 0);
            }
            case 1: {
                String string;
                if (array.length == 0) {
                    string = "";
                }
                else {
                    string = ScriptRuntime.toString(array[0]);
                }
                final NativeScript nativeScript = new NativeScript(compile(context, string));
                ScriptRuntime.setObjectProtoAndParent(nativeScript, scriptable);
                return nativeScript;
            }
        }
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        int n = 0;
        String s2 = null;
        final int length = s.length();
        if (length != 4) {
            if (length != 11) {
                switch (length) {
                    case 8: {
                        s2 = "toString";
                        n = 2;
                        break;
                    }
                    case 7: {
                        s2 = "compile";
                        n = 3;
                        break;
                    }
                }
            }
            else {
                s2 = "constructor";
                n = 1;
            }
        }
        else {
            s2 = "exec";
            n = 4;
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
    public int getArity() {
        return 0;
    }
    
    @Override
    public String getClassName() {
        return "Script";
    }
    
    @Override
    public int getLength() {
        return 0;
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
                s = "exec";
                break;
            }
            case 3: {
                n2 = 1;
                s = "compile";
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
        this.initPrototypeMethod(NativeScript.SCRIPT_TAG, n, s, n2);
    }
}
