package org.mozilla.javascript;

public final class NativeContinuation extends IdScriptableObject implements Function
{
    private static final Object FTAG;
    private static final int Id_constructor = 1;
    private static final int MAX_PROTOTYPE_ID = 1;
    static final long serialVersionUID = 1794167133757605367L;
    private Object implementation;
    
    static {
        FTAG = "Continuation";
    }
    
    public static void init(final Context context, final Scriptable scriptable, final boolean b) {
        new NativeContinuation().exportAsJSClass(1, scriptable, b);
    }
    
    public static boolean isContinuationConstructor(final IdFunctionObject idFunctionObject) {
        return idFunctionObject.hasTag(NativeContinuation.FTAG) && idFunctionObject.methodId() == 1;
    }
    
    @Override
    public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        return Interpreter.restartContinuation(this, context, scriptable, array);
    }
    
    @Override
    public Scriptable construct(final Context context, final Scriptable scriptable, final Object[] array) {
        throw Context.reportRuntimeError("Direct call is not supported");
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(NativeContinuation.FTAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        if (methodId != 1) {
            throw new IllegalArgumentException(String.valueOf(methodId));
        }
        throw Context.reportRuntimeError("Direct call is not supported");
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        boolean b = false;
        String s2 = null;
        if (s.length() == 11) {
            s2 = "constructor";
            b = true;
        }
        int n = b ? 1 : 0;
        if (s2 != null) {
            n = (b ? 1 : 0);
            if (s2 != s) {
                n = (b ? 1 : 0);
                if (!s2.equals(s)) {
                    n = 0;
                }
            }
        }
        return n;
    }
    
    @Override
    public String getClassName() {
        return "Continuation";
    }
    
    public Object getImplementation() {
        return this.implementation;
    }
    
    public void initImplementation(final Object implementation) {
        this.implementation = implementation;
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        if (n != 1) {
            throw new IllegalArgumentException(String.valueOf(n));
        }
        this.initPrototypeMethod(NativeContinuation.FTAG, n, "constructor", 0);
    }
}
