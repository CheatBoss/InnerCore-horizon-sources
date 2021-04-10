package org.mozilla.javascript;

public class BaseFunction extends IdScriptableObject implements Function
{
    private static final Object FUNCTION_TAG;
    private static final int Id_apply = 4;
    private static final int Id_arguments = 5;
    private static final int Id_arity = 2;
    private static final int Id_bind = 6;
    private static final int Id_call = 5;
    private static final int Id_constructor = 1;
    private static final int Id_length = 1;
    private static final int Id_name = 3;
    private static final int Id_prototype = 4;
    private static final int Id_toSource = 3;
    private static final int Id_toString = 2;
    private static final int MAX_INSTANCE_ID = 5;
    private static final int MAX_PROTOTYPE_ID = 6;
    static final long serialVersionUID = 5311394446546053859L;
    private int argumentsAttributes;
    private Object argumentsObj;
    private Object prototypeProperty;
    private int prototypePropertyAttributes;
    
    static {
        FUNCTION_TAG = "Function";
    }
    
    public BaseFunction() {
        this.argumentsObj = BaseFunction.NOT_FOUND;
        this.prototypePropertyAttributes = 6;
        this.argumentsAttributes = 6;
    }
    
    public BaseFunction(final Scriptable scriptable, final Scriptable scriptable2) {
        super(scriptable, scriptable2);
        this.argumentsObj = BaseFunction.NOT_FOUND;
        this.prototypePropertyAttributes = 6;
        this.argumentsAttributes = 6;
    }
    
    private Object getArguments() {
        Object o;
        if (this.defaultHas("arguments")) {
            o = this.defaultGet("arguments");
        }
        else {
            o = this.argumentsObj;
        }
        if (o != BaseFunction.NOT_FOUND) {
            return o;
        }
        final NativeCall functionActivation = ScriptRuntime.findFunctionActivation(Context.getContext(), this);
        if (functionActivation == null) {
            return null;
        }
        return functionActivation.get("arguments", functionActivation);
    }
    
    static void init(final Scriptable scriptable, final boolean b) {
        final BaseFunction baseFunction = new BaseFunction();
        baseFunction.prototypePropertyAttributes = 7;
        baseFunction.exportAsJSClass(6, scriptable, b);
    }
    
    static boolean isApply(final IdFunctionObject idFunctionObject) {
        return idFunctionObject.hasTag(BaseFunction.FUNCTION_TAG) && idFunctionObject.methodId() == 4;
    }
    
    static boolean isApplyOrCall(final IdFunctionObject idFunctionObject) {
        if (idFunctionObject.hasTag(BaseFunction.FUNCTION_TAG)) {
            switch (idFunctionObject.methodId()) {
                case 4:
                case 5: {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static Object jsConstructor(final Context context, Scriptable topLevelScope, final Object[] array) {
        final int length = array.length;
        final StringBuilder sb = new StringBuilder();
        sb.append("function ");
        if (context.getLanguageVersion() != 120) {
            sb.append("anonymous");
        }
        sb.append('(');
        for (int i = 0; i < length - 1; ++i) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(ScriptRuntime.toString(array[i]));
        }
        sb.append(") {");
        if (length != 0) {
            sb.append(ScriptRuntime.toString(array[length - 1]));
        }
        sb.append("\n}");
        final String string = sb.toString();
        final int[] array2 = { 0 };
        String sourcePositionFromStack;
        if ((sourcePositionFromStack = Context.getSourcePositionFromStack(array2)) == null) {
            sourcePositionFromStack = "<eval'ed string>";
            array2[0] = 1;
        }
        final String urlForGeneratedScript = ScriptRuntime.makeUrlForGeneratedScript(false, sourcePositionFromStack, array2[0]);
        topLevelScope = ScriptableObject.getTopLevelScope(topLevelScope);
        final ErrorReporter forEval = DefaultErrorReporter.forEval(context.getErrorReporter());
        final Evaluator interpreter = Context.createInterpreter();
        if (interpreter == null) {
            throw new JavaScriptException("Interpreter not present", sourcePositionFromStack, array2[0]);
        }
        return context.compileFunction(topLevelScope, string, interpreter, forEval, urlForGeneratedScript, 1, null);
    }
    
    private BaseFunction realFunction(Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        final Scriptable scriptable2 = scriptable = (Scriptable)scriptable.getDefaultValue(ScriptRuntime.FunctionClass);
        if (scriptable2 instanceof Delegator) {
            scriptable = ((Delegator)scriptable2).getDelegee();
        }
        if (scriptable instanceof BaseFunction) {
            return (BaseFunction)scriptable;
        }
        throw ScriptRuntime.typeError1("msg.incompat.call", idFunctionObject.getFunctionName());
    }
    
    private Object setupDefaultPrototype() {
        synchronized (this) {
            if (this.prototypeProperty != null) {
                return this.prototypeProperty;
            }
            final NativeObject prototypeProperty = new NativeObject();
            prototypeProperty.defineProperty("constructor", this, 2);
            this.prototypeProperty = prototypeProperty;
            final Scriptable objectPrototype = ScriptableObject.getObjectPrototype(this);
            if (objectPrototype != prototypeProperty) {
                prototypeProperty.setPrototype(objectPrototype);
            }
            return prototypeProperty;
        }
    }
    
    @Override
    public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        return Undefined.instance;
    }
    
    @Override
    public Scriptable construct(final Context context, Scriptable scriptable, final Object[] array) {
        final Scriptable object = this.createObject(context, scriptable);
        if (object != null) {
            final Object call = this.call(context, scriptable, object, array);
            Scriptable scriptable2 = object;
            if (call instanceof Scriptable) {
                scriptable2 = (Scriptable)call;
            }
            return scriptable2;
        }
        final Object call2 = this.call(context, scriptable, null, array);
        if (!(call2 instanceof Scriptable)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad implementaion of call as constructor, name=");
            sb.append(this.getFunctionName());
            sb.append(" in ");
            sb.append(this.getClass().getName());
            throw new IllegalStateException(sb.toString());
        }
        final Scriptable scriptable3 = (Scriptable)call2;
        if (scriptable3.getPrototype() == null) {
            scriptable = this.getClassPrototype();
            if (scriptable3 != scriptable) {
                scriptable3.setPrototype(scriptable);
            }
        }
        if (scriptable3.getParentScope() == null) {
            scriptable = this.getParentScope();
            if (scriptable3 != scriptable) {
                scriptable3.setParentScope(scriptable);
            }
        }
        return scriptable3;
    }
    
    public Scriptable createObject(final Context context, final Scriptable scriptable) {
        final NativeObject nativeObject = new NativeObject();
        nativeObject.setPrototype(this.getClassPrototype());
        nativeObject.setParentScope(this.getParentScope());
        return nativeObject;
    }
    
    String decompile(int n, final int n2) {
        final StringBuilder sb = new StringBuilder();
        if ((n2 & 0x1) != 0x0) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n == 0) {
            sb.append("function ");
            sb.append(this.getFunctionName());
            sb.append("() {\n\t");
        }
        sb.append("[native code, arity=");
        sb.append(this.getArity());
        sb.append("]\n");
        if (n == 0) {
            sb.append("}\n");
        }
        return sb.toString();
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(BaseFunction.FUNCTION_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        boolean b = true;
        switch (methodId) {
            default: {
                throw new IllegalArgumentException(String.valueOf(methodId));
            }
            case 6: {
                if (!(scriptable2 instanceof Callable)) {
                    throw ScriptRuntime.notFunctionError(scriptable2);
                }
                final Callable callable = (Callable)scriptable2;
                final int length = array.length;
                Object[] array2;
                Scriptable scriptable3;
                if (length > 0) {
                    final Scriptable objectOrNull = ScriptRuntime.toObjectOrNull(context, array[0], scriptable);
                    array2 = new Object[length - 1];
                    System.arraycopy(array, 1, array2, 0, length - 1);
                    scriptable3 = objectOrNull;
                }
                else {
                    final Object[] emptyArgs = ScriptRuntime.emptyArgs;
                    scriptable3 = null;
                    array2 = emptyArgs;
                }
                return new BoundFunction(context, scriptable, callable, scriptable3, array2);
            }
            case 4:
            case 5: {
                if (methodId != 4) {
                    b = false;
                }
                return ScriptRuntime.applyOrCall(b, context, scriptable, scriptable2, array);
            }
            case 3: {
                final BaseFunction realFunction = this.realFunction(scriptable2, idFunctionObject);
                int int32 = 0;
                int n = 2;
                if (array.length != 0) {
                    int32 = ScriptRuntime.toInt32(array[0]);
                    if (int32 >= 0) {
                        n = 0;
                    }
                    else {
                        int32 = 0;
                        n = n;
                    }
                }
                return realFunction.decompile(int32, n);
            }
            case 2: {
                return this.realFunction(scriptable2, idFunctionObject).decompile(ScriptRuntime.toInt32(array, 0), 0);
            }
            case 1: {
                return jsConstructor(context, scriptable, array);
            }
        }
    }
    
    @Override
    protected void fillConstructorProperties(final IdFunctionObject idFunctionObject) {
        idFunctionObject.setPrototype(this);
        super.fillConstructorProperties(idFunctionObject);
    }
    
    @Override
    protected int findInstanceIdInfo(final String s) {
        int n = 0;
        final int n2 = 0;
        final String s2 = null;
        String s3 = null;
        final int length = s.length();
        if (length != 9) {
            switch (length) {
                default: {
                    n = n2;
                    break;
                }
                case 6: {
                    s3 = "length";
                    n = 1;
                    break;
                }
                case 5: {
                    s3 = "arity";
                    n = 2;
                    break;
                }
                case 4: {
                    s3 = "name";
                    n = 3;
                    break;
                }
            }
        }
        else {
            final char char1 = s.charAt(0);
            if (char1 == 'a') {
                s3 = "arguments";
                n = 5;
            }
            else {
                s3 = s2;
                if (char1 == 'p') {
                    s3 = "prototype";
                    n = 4;
                }
            }
        }
        int n3 = n;
        if (s3 != null) {
            n3 = n;
            if (s3 != s) {
                n3 = n;
                if (!s3.equals(s)) {
                    n3 = 0;
                }
            }
        }
        if (n3 == 0) {
            return super.findInstanceIdInfo(s);
        }
        int n4 = 0;
        switch (n3) {
            default: {
                throw new IllegalStateException();
            }
            case 5: {
                n4 = this.argumentsAttributes;
                break;
            }
            case 4: {
                if (!this.hasPrototypeProperty()) {
                    return 0;
                }
                n4 = this.prototypePropertyAttributes;
                break;
            }
            case 1:
            case 2:
            case 3: {
                n4 = 7;
                break;
            }
        }
        return IdScriptableObject.instanceIdInfo(n4, n3);
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        int n = 0;
        String s2 = null;
        final int length = s.length();
        if (length != 8) {
            if (length != 11) {
                switch (length) {
                    case 5: {
                        s2 = "apply";
                        n = 4;
                        break;
                    }
                    case 4: {
                        final char char1 = s.charAt(0);
                        if (char1 == 'b') {
                            s2 = "bind";
                            n = 6;
                            break;
                        }
                        if (char1 == 'c') {
                            s2 = "call";
                            n = 5;
                            break;
                        }
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
            final char char2 = s.charAt(3);
            if (char2 == 'o') {
                s2 = "toSource";
                n = 3;
            }
            else if (char2 == 't') {
                s2 = "toString";
                n = 2;
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
    
    public int getArity() {
        return 0;
    }
    
    @Override
    public String getClassName() {
        return "Function";
    }
    
    protected Scriptable getClassPrototype() {
        final Object prototypeProperty = this.getPrototypeProperty();
        if (prototypeProperty instanceof Scriptable) {
            return (Scriptable)prototypeProperty;
        }
        return ScriptableObject.getObjectPrototype(this);
    }
    
    public String getFunctionName() {
        return "";
    }
    
    @Override
    protected String getInstanceIdName(final int n) {
        switch (n) {
            default: {
                return super.getInstanceIdName(n);
            }
            case 5: {
                return "arguments";
            }
            case 4: {
                return "prototype";
            }
            case 3: {
                return "name";
            }
            case 2: {
                return "arity";
            }
            case 1: {
                return "length";
            }
        }
    }
    
    @Override
    protected Object getInstanceIdValue(final int n) {
        switch (n) {
            default: {
                return super.getInstanceIdValue(n);
            }
            case 5: {
                return this.getArguments();
            }
            case 4: {
                return this.getPrototypeProperty();
            }
            case 3: {
                return this.getFunctionName();
            }
            case 2: {
                return ScriptRuntime.wrapInt(this.getArity());
            }
            case 1: {
                return ScriptRuntime.wrapInt(this.getLength());
            }
        }
    }
    
    public int getLength() {
        return 0;
    }
    
    @Override
    protected int getMaxInstanceId() {
        return 5;
    }
    
    protected Object getPrototypeProperty() {
        final Object prototypeProperty = this.prototypeProperty;
        if (prototypeProperty != null) {
            Object o;
            if ((o = prototypeProperty) == UniqueTag.NULL_VALUE) {
                o = null;
            }
            return o;
        }
        if (this instanceof NativeFunction) {
            return this.setupDefaultPrototype();
        }
        return Undefined.instance;
    }
    
    @Override
    public String getTypeOf() {
        if (this.avoidObjectDetection()) {
            return "undefined";
        }
        return "function";
    }
    
    @Override
    public boolean hasInstance(final Scriptable scriptable) {
        final Object property = ScriptableObject.getProperty(this, "prototype");
        if (property instanceof Scriptable) {
            return ScriptRuntime.jsDelegatesTo(scriptable, (Scriptable)property);
        }
        throw ScriptRuntime.typeError1("msg.instanceof.bad.prototype", this.getFunctionName());
    }
    
    protected boolean hasPrototypeProperty() {
        return this.prototypeProperty != null || this instanceof NativeFunction;
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        int n2 = 0;
        String s = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case 6: {
                n2 = 1;
                s = "bind";
                break;
            }
            case 5: {
                n2 = 1;
                s = "call";
                break;
            }
            case 4: {
                n2 = 2;
                s = "apply";
                break;
            }
            case 3: {
                n2 = 1;
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
        this.initPrototypeMethod(BaseFunction.FUNCTION_TAG, n, s, n2);
    }
    
    public void setImmunePrototypeProperty(Object null_VALUE) {
        if ((this.prototypePropertyAttributes & 0x1) != 0x0) {
            throw new IllegalStateException();
        }
        if (null_VALUE == null) {
            null_VALUE = UniqueTag.NULL_VALUE;
        }
        this.prototypeProperty = null_VALUE;
        this.prototypePropertyAttributes = 7;
    }
    
    @Override
    protected void setInstanceIdAttributes(final int n, final int n2) {
        switch (n) {
            default: {
                super.setInstanceIdAttributes(n, n2);
            }
            case 5: {
                this.argumentsAttributes = n2;
            }
            case 4: {
                this.prototypePropertyAttributes = n2;
            }
        }
    }
    
    @Override
    protected void setInstanceIdValue(final int n, Object null_VALUE) {
        switch (n) {
            default: {
                super.setInstanceIdValue(n, null_VALUE);
            }
            case 5: {
                if (null_VALUE == BaseFunction.NOT_FOUND) {
                    Kit.codeBug();
                }
                if (this.defaultHas("arguments")) {
                    this.defaultPut("arguments", null_VALUE);
                    return;
                }
                if ((this.argumentsAttributes & 0x1) == 0x0) {
                    this.argumentsObj = null_VALUE;
                }
            }
            case 4: {
                if ((this.prototypePropertyAttributes & 0x1) == 0x0) {
                    if (null_VALUE == null) {
                        null_VALUE = UniqueTag.NULL_VALUE;
                    }
                    this.prototypeProperty = null_VALUE;
                }
            }
            case 1:
            case 2:
            case 3: {}
        }
    }
}
