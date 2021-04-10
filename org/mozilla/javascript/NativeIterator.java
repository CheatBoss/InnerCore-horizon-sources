package org.mozilla.javascript;

import java.util.*;

public final class NativeIterator extends IdScriptableObject
{
    public static final String ITERATOR_PROPERTY_NAME = "__iterator__";
    private static final Object ITERATOR_TAG;
    private static final int Id___iterator__ = 3;
    private static final int Id_constructor = 1;
    private static final int Id_next = 2;
    private static final int MAX_PROTOTYPE_ID = 3;
    private static final String STOP_ITERATION = "StopIteration";
    private static final long serialVersionUID = -4136968203581667681L;
    private Object objectIterator;
    
    static {
        ITERATOR_TAG = "Iterator";
    }
    
    private NativeIterator() {
    }
    
    private NativeIterator(final Object objectIterator) {
        this.objectIterator = objectIterator;
    }
    
    public static Object getStopIterationObject(final Scriptable scriptable) {
        return ScriptableObject.getTopScopeValue(ScriptableObject.getTopLevelScope(scriptable), NativeIterator.ITERATOR_TAG);
    }
    
    static void init(final ScriptableObject parentScope, final boolean b) {
        new NativeIterator().exportAsJSClass(3, parentScope, b);
        NativeGenerator.init(parentScope, b);
        final StopIteration stopIteration = new StopIteration();
        stopIteration.setPrototype(ScriptableObject.getObjectPrototype(parentScope));
        stopIteration.setParentScope(parentScope);
        if (b) {
            stopIteration.sealObject();
        }
        ScriptableObject.defineProperty(parentScope, "StopIteration", stopIteration, 2);
        parentScope.associateValue(NativeIterator.ITERATOR_TAG, stopIteration);
    }
    
    private static Object jsConstructor(final Context context, Scriptable topLevelScope, Scriptable iterator, final Object[] array) {
        final int length = array.length;
        final boolean b = false;
        if (length != 0 && array[0] != null && array[0] != Undefined.instance) {
            final Scriptable object = ScriptRuntime.toObject(context, topLevelScope, array[0]);
            boolean b2 = b;
            if (array.length > 1) {
                b2 = b;
                if (ScriptRuntime.toBoolean(array[1])) {
                    b2 = true;
                }
            }
            if (iterator != null) {
                final Iterator<?> javaIterator = VMBridge.instance.getJavaIterator(context, topLevelScope, object);
                if (javaIterator != null) {
                    topLevelScope = ScriptableObject.getTopLevelScope(topLevelScope);
                    return context.getWrapFactory().wrap(context, topLevelScope, new WrappedJavaIterator(javaIterator, topLevelScope), WrappedJavaIterator.class);
                }
                iterator = ScriptRuntime.toIterator(context, topLevelScope, object, b2);
                if (iterator != null) {
                    return iterator;
                }
            }
            int n;
            if (b2) {
                n = 3;
            }
            else {
                n = 5;
            }
            final Object enumInit = ScriptRuntime.enumInit(object, context, topLevelScope, n);
            ScriptRuntime.setEnumNumbers(enumInit, true);
            final NativeIterator nativeIterator = new NativeIterator(enumInit);
            nativeIterator.setPrototype(ScriptableObject.getClassPrototype(topLevelScope, nativeIterator.getClassName()));
            nativeIterator.setParentScope(topLevelScope);
            return nativeIterator;
        }
        Object instance;
        if (array.length == 0) {
            instance = Undefined.instance;
        }
        else {
            instance = array[0];
        }
        throw ScriptRuntime.typeError1("msg.no.properties", ScriptRuntime.toString(instance));
    }
    
    private Object next(final Context context, final Scriptable scriptable) {
        if (!ScriptRuntime.enumNext(this.objectIterator)) {
            throw new JavaScriptException(getStopIterationObject(scriptable), null, 0);
        }
        return ScriptRuntime.enumId(this.objectIterator, context);
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(NativeIterator.ITERATOR_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        if (methodId == 1) {
            return jsConstructor(context, scriptable, scriptable2, array);
        }
        if (!(scriptable2 instanceof NativeIterator)) {
            throw IdScriptableObject.incompatibleCallError(idFunctionObject);
        }
        final NativeIterator nativeIterator = (NativeIterator)scriptable2;
        switch (methodId) {
            default: {
                throw new IllegalArgumentException(String.valueOf(methodId));
            }
            case 3: {
                return scriptable2;
            }
            case 2: {
                return nativeIterator.next(context, scriptable);
            }
        }
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        int n = 0;
        String s2 = null;
        final int length = s.length();
        if (length == 4) {
            s2 = "next";
            n = 2;
        }
        else if (length == 11) {
            s2 = "constructor";
            n = 1;
        }
        else if (length == 12) {
            s2 = "__iterator__";
            n = 3;
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
        return "Iterator";
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
                n2 = 1;
                s = "__iterator__";
                break;
            }
            case 2: {
                n2 = 0;
                s = "next";
                break;
            }
            case 1: {
                n2 = 2;
                s = "constructor";
                break;
            }
        }
        this.initPrototypeMethod(NativeIterator.ITERATOR_TAG, n, s, n2);
    }
    
    static class StopIteration extends NativeObject
    {
        private static final long serialVersionUID = 2485151085722377663L;
        
        @Override
        public String getClassName() {
            return "StopIteration";
        }
        
        @Override
        public boolean hasInstance(final Scriptable scriptable) {
            return scriptable instanceof StopIteration;
        }
    }
    
    public static class WrappedJavaIterator
    {
        private Iterator<?> iterator;
        private Scriptable scope;
        
        WrappedJavaIterator(final Iterator<?> iterator, final Scriptable scope) {
            this.iterator = iterator;
            this.scope = scope;
        }
        
        public Object __iterator__(final boolean b) {
            return this;
        }
        
        public Object next() {
            if (!this.iterator.hasNext()) {
                throw new JavaScriptException(NativeIterator.getStopIterationObject(this.scope), null, 0);
            }
            return this.iterator.next();
        }
    }
}
