package org.mozilla.javascript;

public final class NativeGenerator extends IdScriptableObject
{
    public static final int GENERATOR_CLOSE = 2;
    public static final int GENERATOR_SEND = 0;
    private static final Object GENERATOR_TAG;
    public static final int GENERATOR_THROW = 1;
    private static final int Id___iterator__ = 5;
    private static final int Id_close = 1;
    private static final int Id_next = 2;
    private static final int Id_send = 3;
    private static final int Id_throw = 4;
    private static final int MAX_PROTOTYPE_ID = 5;
    private static final long serialVersionUID = 1645892441041347273L;
    private boolean firstTime;
    private NativeFunction function;
    private int lineNumber;
    private String lineSource;
    private boolean locked;
    private Object savedState;
    
    static {
        GENERATOR_TAG = "Generator";
    }
    
    private NativeGenerator() {
        this.firstTime = true;
    }
    
    public NativeGenerator(Scriptable topLevelScope, final NativeFunction function, final Object savedState) {
        this.firstTime = true;
        this.function = function;
        this.savedState = savedState;
        topLevelScope = ScriptableObject.getTopLevelScope(topLevelScope);
        this.setParentScope(topLevelScope);
        this.setPrototype((Scriptable)ScriptableObject.getTopScopeValue(topLevelScope, NativeGenerator.GENERATOR_TAG));
    }
    
    static NativeGenerator init(final ScriptableObject parentScope, final boolean b) {
        final NativeGenerator nativeGenerator = new NativeGenerator();
        if (parentScope != null) {
            nativeGenerator.setParentScope(parentScope);
            nativeGenerator.setPrototype(ScriptableObject.getObjectPrototype(parentScope));
        }
        nativeGenerator.activatePrototypeMap(5);
        if (b) {
            nativeGenerator.sealObject();
        }
        if (parentScope != null) {
            parentScope.associateValue(NativeGenerator.GENERATOR_TAG, nativeGenerator);
        }
        return nativeGenerator;
    }
    
    private Object resume(final Context context, final Scriptable scriptable, final int n, Object stopIterationObject) {
        if (this.savedState == null) {
            if (n == 2) {
                return Undefined.instance;
            }
            if (n != 1) {
                stopIterationObject = NativeIterator.getStopIterationObject(scriptable);
            }
            throw new JavaScriptException(stopIterationObject, this.lineSource, this.lineNumber);
        }
        else {
            try {
                synchronized (this) {
                    if (this.locked) {
                        throw ScriptRuntime.typeError0("msg.already.exec.gen");
                    }
                    this.locked = true;
                    // monitorexit(this)
                    final Object resumeGenerator = this.function.resumeGenerator(context, scriptable, n, this.savedState, stopIterationObject);
                    synchronized (this) {
                        this.locked = false;
                        // monitorexit(this)
                        if (n == 2) {
                            this.savedState = null;
                        }
                        return resumeGenerator;
                    }
                }
            }
            catch (RhinoException ex) {}
            catch (GeneratorClosedException ex2) {}
            finally {
                synchronized (this) {
                    this.locked = false;
                    // monitorexit(this)
                    if (n == 2) {
                        this.savedState = null;
                    }
                }
                try {
                    this.locked = false;
                    // monitorexit(this)
                    if (n == 2) {
                        this.savedState = null;
                    }
                    return;
                }
                finally {
                }
                // monitorexit(this)
            }
        }
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(NativeGenerator.GENERATOR_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        if (!(scriptable2 instanceof NativeGenerator)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        final NativeGenerator nativeGenerator = (NativeGenerator)scriptable2;
        switch (methodId) {
            default: {
                throw new IllegalArgumentException(String.valueOf(methodId));
            }
            case 5: {
                return scriptable2;
            }
            case 4: {
                Object instance;
                if (array.length > 0) {
                    instance = array[0];
                }
                else {
                    instance = Undefined.instance;
                }
                return nativeGenerator.resume(context, scriptable, 1, instance);
            }
            case 3: {
                Object instance2;
                if (array.length > 0) {
                    instance2 = array[0];
                }
                else {
                    instance2 = Undefined.instance;
                }
                if (nativeGenerator.firstTime && !instance2.equals(Undefined.instance)) {
                    throw ScriptRuntime.typeError0("msg.send.newborn");
                }
                return nativeGenerator.resume(context, scriptable, 0, instance2);
            }
            case 2: {
                nativeGenerator.firstTime = false;
                return nativeGenerator.resume(context, scriptable, 0, Undefined.instance);
            }
            case 1: {
                return nativeGenerator.resume(context, scriptable, 2, new GeneratorClosedException());
            }
        }
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        int n = 0;
        String s2 = null;
        final int length = s.length();
        if (length == 4) {
            final char char1 = s.charAt(0);
            if (char1 == 'n') {
                s2 = "next";
                n = 2;
            }
            else if (char1 == 's') {
                s2 = "send";
                n = 3;
            }
        }
        else if (length == 5) {
            final char char2 = s.charAt(0);
            if (char2 == 'c') {
                s2 = "close";
                n = 1;
            }
            else if (char2 == 't') {
                s2 = "throw";
                n = 4;
            }
        }
        else if (length == 12) {
            s2 = "__iterator__";
            n = 5;
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
        return "Generator";
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        int n2 = 0;
        String s = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case 5: {
                n2 = 1;
                s = "__iterator__";
                break;
            }
            case 4: {
                n2 = 0;
                s = "throw";
                break;
            }
            case 3: {
                n2 = 0;
                s = "send";
                break;
            }
            case 2: {
                n2 = 1;
                s = "next";
                break;
            }
            case 1: {
                n2 = 1;
                s = "close";
                break;
            }
        }
        this.initPrototypeMethod(NativeGenerator.GENERATOR_TAG, n, s, n2);
    }
    
    private static class CloseGeneratorAction implements ContextAction
    {
        private NativeGenerator generator;
        
        CloseGeneratorAction(final NativeGenerator generator) {
            this.generator = generator;
        }
        
        @Override
        public Object run(final Context context) {
            return ScriptRuntime.doTopCall(new Callable() {
                @Override
                public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
                    return ((NativeGenerator)scriptable2).resume(context, scriptable, 2, new GeneratorClosedException());
                }
            }, context, ScriptableObject.getTopLevelScope(this.generator), this.generator, null);
        }
    }
    
    public static class GeneratorClosedException extends RuntimeException
    {
        private static final long serialVersionUID = 2561315658662379681L;
    }
}
