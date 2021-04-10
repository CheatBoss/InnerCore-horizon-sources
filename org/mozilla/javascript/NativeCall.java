package org.mozilla.javascript;

public final class NativeCall extends IdScriptableObject
{
    private static final Object CALL_TAG;
    private static final int Id_constructor = 1;
    private static final int MAX_PROTOTYPE_ID = 1;
    static final long serialVersionUID = -7471457301304454454L;
    NativeFunction function;
    Object[] originalArgs;
    transient NativeCall parentActivationCall;
    
    static {
        CALL_TAG = "Call";
    }
    
    NativeCall() {
    }
    
    NativeCall(final NativeFunction function, final Scriptable parentScope, final Object[] array) {
        this.function = function;
        this.setParentScope(parentScope);
        Object[] emptyArgs;
        if (array == null) {
            emptyArgs = ScriptRuntime.emptyArgs;
        }
        else {
            emptyArgs = array;
        }
        this.originalArgs = emptyArgs;
        final int paramAndVarCount = function.getParamAndVarCount();
        final int paramCount = function.getParamCount();
        if (paramAndVarCount != 0) {
            for (int i = 0; i < paramCount; ++i) {
                final String paramOrVarName = function.getParamOrVarName(i);
                Object instance;
                if (i < array.length) {
                    instance = array[i];
                }
                else {
                    instance = Undefined.instance;
                }
                this.defineProperty(paramOrVarName, instance, 4);
            }
        }
        if (!super.has("arguments", this)) {
            this.defineProperty("arguments", new Arguments(this), 4);
        }
        if (paramAndVarCount != 0) {
            for (int j = paramCount; j < paramAndVarCount; ++j) {
                final String paramOrVarName2 = function.getParamOrVarName(j);
                if (!super.has(paramOrVarName2, this)) {
                    if (function.getParamOrVarConst(j)) {
                        this.defineProperty(paramOrVarName2, Undefined.instance, 13);
                    }
                    else {
                        this.defineProperty(paramOrVarName2, Undefined.instance, 4);
                    }
                }
            }
        }
    }
    
    static void init(final Scriptable scriptable, final boolean b) {
        new NativeCall().exportAsJSClass(1, scriptable, b);
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(NativeCall.CALL_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        if (methodId != 1) {
            throw new IllegalArgumentException(String.valueOf(methodId));
        }
        if (scriptable2 != null) {
            throw Context.reportRuntimeError1("msg.only.from.new", "Call");
        }
        ScriptRuntime.checkDeprecated(context, "Call");
        final NativeCall nativeCall = new NativeCall();
        nativeCall.setPrototype(ScriptableObject.getObjectPrototype(scriptable));
        return nativeCall;
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    @Override
    public String getClassName() {
        return "Call";
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        if (n == 1) {
            this.initPrototypeMethod(NativeCall.CALL_TAG, n, "constructor", 1);
            return;
        }
        throw new IllegalArgumentException(String.valueOf(n));
    }
}
