package org.mozilla.javascript;

import java.lang.reflect.*;
import java.io.*;

final class NativeError extends IdScriptableObject
{
    private static final int ConstructorId_captureStackTrace = -1;
    public static final int DEFAULT_STACK_LIMIT = -1;
    private static final Method ERROR_DELEGATE_GET_STACK;
    private static final Method ERROR_DELEGATE_SET_STACK;
    private static final Object ERROR_TAG;
    private static final int Id_constructor = 1;
    private static final int Id_toSource = 3;
    private static final int Id_toString = 2;
    private static final int MAX_PROTOTYPE_ID = 3;
    private static final String STACK_HIDE_KEY = "_stackHide";
    static final long serialVersionUID = -5338413581437645187L;
    private RhinoException stackProvider;
    
    static {
        ERROR_TAG = "Error";
        try {
            ERROR_DELEGATE_GET_STACK = NativeError.class.getMethod("getStackDelegated", Scriptable.class);
            ERROR_DELEGATE_SET_STACK = NativeError.class.getMethod("setStackDelegated", Scriptable.class, Object.class);
        }
        catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private Object callPrepareStack(final Function function, final ScriptStackElement[] array) {
        final Context currentContext = Context.getCurrentContext();
        final Object[] array2 = new Object[array.length];
        for (int i = 0; i < array.length; ++i) {
            final NativeCallSite nativeCallSite = (NativeCallSite)currentContext.newObject(this, "CallSite");
            nativeCallSite.setElement(array[i]);
            array2[i] = nativeCallSite;
        }
        return function.call(currentContext, function, this, new Object[] { this, currentContext.newArray(this, array2) });
    }
    
    static void init(final Scriptable scriptable, final boolean b) {
        final NativeError nativeError = new NativeError();
        ScriptableObject.putProperty(nativeError, "name", "Error");
        ScriptableObject.putProperty(nativeError, "message", "");
        ScriptableObject.putProperty(nativeError, "fileName", "");
        ScriptableObject.putProperty(nativeError, "lineNumber", 0);
        nativeError.setAttributes("name", 2);
        nativeError.setAttributes("message", 2);
        nativeError.exportAsJSClass(3, scriptable, b);
        NativeCallSite.init(nativeError, b);
    }
    
    private static void js_captureStackTrace(final Context context, final Scriptable scriptable, final Object[] array) {
        final ScriptableObject scriptableObject = (ScriptableObject)ScriptRuntime.toObjectOrNull(context, array[0], scriptable);
        Scriptable scriptable2 = null;
        if (array.length > 1) {
            scriptable2 = ScriptRuntime.toObjectOrNull(context, array[1], scriptable);
        }
        final NativeError nativeError = (NativeError)context.newObject(scriptable, "Error");
        nativeError.setStackProvider(new EvaluatorException("[object Object]"));
        if (scriptable2 != null) {
            final Object value = scriptable2.get("name", scriptable2);
            if (value != null && !Undefined.instance.equals(value)) {
                nativeError.associateValue("_stackHide", Context.toString(value));
            }
        }
        scriptableObject.defineProperty("stack", nativeError, NativeError.ERROR_DELEGATE_GET_STACK, NativeError.ERROR_DELEGATE_SET_STACK, 0);
    }
    
    private static String js_toSource(final Context context, final Scriptable scriptable, final Scriptable scriptable2) {
        final Object property = ScriptableObject.getProperty(scriptable2, "name");
        final Object property2 = ScriptableObject.getProperty(scriptable2, "message");
        final Object property3 = ScriptableObject.getProperty(scriptable2, "fileName");
        final Object property4 = ScriptableObject.getProperty(scriptable2, "lineNumber");
        final StringBuilder sb = new StringBuilder();
        sb.append("(new ");
        Object instance = property;
        if (property == NativeError.NOT_FOUND) {
            instance = Undefined.instance;
        }
        sb.append(ScriptRuntime.toString(instance));
        sb.append("(");
        if (property2 != NativeError.NOT_FOUND || property3 != NativeError.NOT_FOUND || property4 != NativeError.NOT_FOUND) {
            Object o;
            if ((o = property2) == NativeError.NOT_FOUND) {
                o = "";
            }
            sb.append(ScriptRuntime.uneval(context, scriptable, o));
            if (property3 != NativeError.NOT_FOUND || property4 != NativeError.NOT_FOUND) {
                sb.append(", ");
                Object o2;
                if ((o2 = property3) == NativeError.NOT_FOUND) {
                    o2 = "";
                }
                sb.append(ScriptRuntime.uneval(context, scriptable, o2));
                if (property4 != NativeError.NOT_FOUND) {
                    final int int32 = ScriptRuntime.toInt32(property4);
                    if (int32 != 0) {
                        sb.append(", ");
                        sb.append(ScriptRuntime.toString(int32));
                    }
                }
            }
        }
        sb.append("))");
        return sb.toString();
    }
    
    private static Object js_toString(final Scriptable scriptable) {
        final Object property = ScriptableObject.getProperty(scriptable, "name");
        String string;
        if (property != NativeError.NOT_FOUND && property != Undefined.instance) {
            string = ScriptRuntime.toString(property);
        }
        else {
            string = "Error";
        }
        final Object property2 = ScriptableObject.getProperty(scriptable, "message");
        String string2;
        if (property2 != NativeError.NOT_FOUND && property2 != Undefined.instance) {
            string2 = ScriptRuntime.toString(property2);
        }
        else {
            string2 = "";
        }
        if (string.toString().length() == 0) {
            return string2;
        }
        if (string2.toString().length() == 0) {
            return string;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(string);
        sb.append(": ");
        sb.append(string2);
        return sb.toString();
    }
    
    static NativeError make(final Context context, final Scriptable parentScope, final IdFunctionObject idFunctionObject, final Object[] array) {
        final Scriptable prototype = (Scriptable)idFunctionObject.get("prototype", idFunctionObject);
        final NativeError nativeError = new NativeError();
        nativeError.setPrototype(prototype);
        nativeError.setParentScope(parentScope);
        final int length = array.length;
        if (length >= 1) {
            if (array[0] != Undefined.instance) {
                ScriptableObject.putProperty(nativeError, "message", ScriptRuntime.toString(array[0]));
            }
            if (length >= 2) {
                ScriptableObject.putProperty(nativeError, "fileName", array[1]);
                if (length >= 3) {
                    ScriptableObject.putProperty(nativeError, "lineNumber", ScriptRuntime.toInt32(array[2]));
                }
            }
        }
        return nativeError;
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(NativeError.ERROR_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        if (methodId == -1) {
            js_captureStackTrace(context, scriptable2, array);
            return Undefined.instance;
        }
        switch (methodId) {
            default: {
                throw new IllegalArgumentException(String.valueOf(methodId));
            }
            case 3: {
                return js_toSource(context, scriptable, scriptable2);
            }
            case 2: {
                return js_toString(scriptable2);
            }
            case 1: {
                return make(context, scriptable, idFunctionObject, array);
            }
        }
    }
    
    @Override
    protected void fillConstructorProperties(final IdFunctionObject idFunctionObject) {
        this.addIdFunctionProperty(idFunctionObject, NativeError.ERROR_TAG, -1, "captureStackTrace", 2);
        final ProtoProps protoProps = new ProtoProps();
        this.associateValue("_ErrorPrototypeProps", protoProps);
        idFunctionObject.defineProperty("stackTraceLimit", protoProps, ProtoProps.GET_STACK_LIMIT, ProtoProps.SET_STACK_LIMIT, 0);
        idFunctionObject.defineProperty("prepareStackTrace", protoProps, ProtoProps.GET_PREPARE_STACK, ProtoProps.SET_PREPARE_STACK, 0);
        super.fillConstructorProperties(idFunctionObject);
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        int n = 0;
        String s2 = null;
        final int length = s.length();
        if (length == 8) {
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
        return "Error";
    }
    
    public Object getStackDelegated(final Scriptable scriptable) {
        if (this.stackProvider == null) {
            return NativeError.NOT_FOUND;
        }
        int stackTraceLimit = -1;
        Function prepareStackTrace = null;
        final ProtoProps protoProps = (ProtoProps)((NativeError)this.getPrototype()).getAssociatedValue("_ErrorPrototypeProps");
        if (protoProps != null) {
            stackTraceLimit = protoProps.getStackTraceLimit();
            prepareStackTrace = protoProps.getPrepareStackTrace();
        }
        final ScriptStackElement[] scriptStack = this.stackProvider.getScriptStack(stackTraceLimit, (String)this.getAssociatedValue("_stackHide"));
        Object o;
        if (prepareStackTrace == null) {
            o = RhinoException.formatStackTrace(scriptStack, this.stackProvider.details());
        }
        else {
            o = this.callPrepareStack(prepareStackTrace, scriptStack);
        }
        this.setStackDelegated(scriptable, o);
        return o;
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        int n2 = 0;
        String s = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
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
        this.initPrototypeMethod(NativeError.ERROR_TAG, n, s, n2);
    }
    
    public void setStackDelegated(final Scriptable scriptable, final Object o) {
        scriptable.delete("stack");
        this.stackProvider = null;
        scriptable.put("stack", scriptable, o);
    }
    
    public void setStackProvider(final RhinoException stackProvider) {
        if (this.stackProvider == null) {
            this.stackProvider = stackProvider;
            this.defineProperty("stack", this, NativeError.ERROR_DELEGATE_GET_STACK, NativeError.ERROR_DELEGATE_SET_STACK, 2);
        }
    }
    
    @Override
    public String toString() {
        final Object js_toString = js_toString(this);
        if (js_toString instanceof String) {
            return (String)js_toString;
        }
        return super.toString();
    }
    
    private static final class ProtoProps implements Serializable
    {
        static final Method GET_PREPARE_STACK;
        static final Method GET_STACK_LIMIT;
        static final String KEY = "_ErrorPrototypeProps";
        static final Method SET_PREPARE_STACK;
        static final Method SET_STACK_LIMIT;
        private static final long serialVersionUID = 1907180507775337939L;
        private Function prepareStackTrace;
        private int stackTraceLimit;
        
        static {
            try {
                GET_STACK_LIMIT = ProtoProps.class.getMethod("getStackTraceLimit", Scriptable.class);
                SET_STACK_LIMIT = ProtoProps.class.getMethod("setStackTraceLimit", Scriptable.class, Object.class);
                GET_PREPARE_STACK = ProtoProps.class.getMethod("getPrepareStackTrace", Scriptable.class);
                SET_PREPARE_STACK = ProtoProps.class.getMethod("setPrepareStackTrace", Scriptable.class, Object.class);
            }
            catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
        }
        
        private ProtoProps() {
            this.stackTraceLimit = -1;
        }
        
        public Object getPrepareStackTrace(final Scriptable scriptable) {
            final Function prepareStackTrace = this.getPrepareStackTrace();
            if (prepareStackTrace == null) {
                return Undefined.instance;
            }
            return prepareStackTrace;
        }
        
        public Function getPrepareStackTrace() {
            return this.prepareStackTrace;
        }
        
        public int getStackTraceLimit() {
            return this.stackTraceLimit;
        }
        
        public Object getStackTraceLimit(final Scriptable scriptable) {
            if (this.stackTraceLimit >= 0) {
                return this.stackTraceLimit;
            }
            return Double.POSITIVE_INFINITY;
        }
        
        public void setPrepareStackTrace(final Scriptable scriptable, final Object o) {
            if (o != null && !Undefined.instance.equals(o)) {
                if (o instanceof Function) {
                    this.prepareStackTrace = (Function)o;
                }
            }
            else {
                this.prepareStackTrace = null;
            }
        }
        
        public void setStackTraceLimit(final Scriptable scriptable, final Object o) {
            final double number = Context.toNumber(o);
            if (!Double.isNaN(number) && !Double.isInfinite(number)) {
                this.stackTraceLimit = (int)number;
                return;
            }
            this.stackTraceLimit = -1;
        }
    }
}
