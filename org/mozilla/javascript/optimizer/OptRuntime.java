package org.mozilla.javascript.optimizer;

import org.mozilla.javascript.*;

public final class OptRuntime extends ScriptRuntime
{
    public static final Double minusOneObj;
    public static final Double oneObj;
    public static final Double zeroObj;
    
    static {
        zeroObj = new Double(0.0);
        oneObj = new Double(1.0);
        minusOneObj = new Double(-1.0);
    }
    
    public static Object add(final double n, final Object o) {
        Object defaultValue = o;
        if (o instanceof Scriptable) {
            defaultValue = ((Scriptable)o).getDefaultValue(null);
        }
        if (!(defaultValue instanceof CharSequence)) {
            return wrapDouble(ScriptRuntime.toNumber(defaultValue) + n);
        }
        return new ConsString(ScriptRuntime.toString(n), (CharSequence)defaultValue);
    }
    
    public static Object add(final Object o, final double n) {
        Object defaultValue = o;
        if (o instanceof Scriptable) {
            defaultValue = ((Scriptable)o).getDefaultValue(null);
        }
        if (!(defaultValue instanceof CharSequence)) {
            return wrapDouble(ScriptRuntime.toNumber(defaultValue) + n);
        }
        return new ConsString((CharSequence)defaultValue, ScriptRuntime.toString(n));
    }
    
    public static Object call0(final Callable callable, final Scriptable scriptable, final Context context, final Scriptable scriptable2) {
        return callable.call(context, scriptable2, scriptable, ScriptRuntime.emptyArgs);
    }
    
    public static Object call1(final Callable callable, final Scriptable scriptable, final Object o, final Context context, final Scriptable scriptable2) {
        return callable.call(context, scriptable2, scriptable, new Object[] { o });
    }
    
    public static Object call2(final Callable callable, final Scriptable scriptable, final Object o, final Object o2, final Context context, final Scriptable scriptable2) {
        return callable.call(context, scriptable2, scriptable, new Object[] { o, o2 });
    }
    
    public static Object callN(final Callable callable, final Scriptable scriptable, final Object[] array, final Context context, final Scriptable scriptable2) {
        return callable.call(context, scriptable2, scriptable, array);
    }
    
    public static Object callName(final Object[] array, final String s, final Context context, final Scriptable scriptable) {
        return ScriptRuntime.getNameFunctionAndThis(s, context, scriptable).call(context, scriptable, ScriptRuntime.lastStoredScriptable(context), array);
    }
    
    public static Object callName0(final String s, final Context context, final Scriptable scriptable) {
        return ScriptRuntime.getNameFunctionAndThis(s, context, scriptable).call(context, scriptable, ScriptRuntime.lastStoredScriptable(context), ScriptRuntime.emptyArgs);
    }
    
    public static Object callProp0(final Object o, final String s, final Context context, final Scriptable scriptable) {
        return ScriptRuntime.getPropFunctionAndThis(o, s, context, scriptable).call(context, scriptable, ScriptRuntime.lastStoredScriptable(context), ScriptRuntime.emptyArgs);
    }
    
    public static Object callSpecial(final Context context, final Callable callable, final Scriptable scriptable, final Object[] array, final Scriptable scriptable2, final Scriptable scriptable3, final int n, final String s, final int n2) {
        return ScriptRuntime.callSpecial(context, callable, scriptable, array, scriptable2, scriptable3, n, s, n2);
    }
    
    public static Scriptable createNativeGenerator(final NativeFunction nativeFunction, final Scriptable scriptable, final Scriptable scriptable2, final int n, final int n2) {
        return new NativeGenerator(scriptable, nativeFunction, new GeneratorState(scriptable2, n, n2));
    }
    
    private static int[] decodeIntArray(final String s, final int n) {
        if (n == 0) {
            if (s != null) {
                throw new IllegalArgumentException();
            }
            return null;
        }
        else {
            final int length = s.length();
            int i = 0;
            if (length != n * 2 + 1 && s.charAt(0) != '\u0001') {
                throw new IllegalArgumentException();
            }
            final int[] array = new int[n];
            while (i != n) {
                final int n2 = i * 2 + 1;
                array[i] = (s.charAt(n2) << 16 | s.charAt(n2 + 1));
                ++i;
            }
            return array;
        }
    }
    
    @Deprecated
    public static Object elemIncrDecr(final Object o, final double n, final Context context, final int n2) {
        return elemIncrDecr(o, n, context, ScriptRuntime.getTopCallScope(context), n2);
    }
    
    public static Object elemIncrDecr(final Object o, final double n, final Context context, final Scriptable scriptable, final int n2) {
        return ScriptRuntime.elemIncrDecr(o, new Double(n), context, scriptable, n2);
    }
    
    static String encodeIntArray(final int[] array) {
        if (array == null) {
            return null;
        }
        final int length = array.length;
        final char[] array2 = new char[length * 2 + 1];
        int i = 0;
        array2[0] = '\u0001';
        while (i != length) {
            final int n = array[i];
            final int n2 = i * 2 + 1;
            array2[n2] = (char)(n >>> 16);
            array2[n2 + 1] = (char)n;
            ++i;
        }
        return new String(array2);
    }
    
    public static Object[] getGeneratorLocalsState(final Object o) {
        final GeneratorState generatorState = (GeneratorState)o;
        if (generatorState.localsState == null) {
            generatorState.localsState = new Object[generatorState.maxLocals];
        }
        return generatorState.localsState;
    }
    
    public static Object[] getGeneratorStackState(final Object o) {
        final GeneratorState generatorState = (GeneratorState)o;
        if (generatorState.stackState == null) {
            generatorState.stackState = new Object[generatorState.maxStack];
        }
        return generatorState.stackState;
    }
    
    public static void initFunction(final NativeFunction nativeFunction, final int n, final Scriptable scriptable, final Context context) {
        ScriptRuntime.initFunction(context, scriptable, nativeFunction, n, false);
    }
    
    public static void main(final Script script, final String[] array) {
        ContextFactory.getGlobal().call(new ContextAction() {
            @Override
            public Object run(final Context context) {
                final ScriptableObject global = ScriptRuntime.getGlobal(context);
                final Object[] array = new Object[array.length];
                System.arraycopy(array, 0, array, 0, array.length);
                global.defineProperty("arguments", context.newArray(global, array), 2);
                script.exec(context, global);
                return null;
            }
        });
    }
    
    public static Scriptable newArrayLiteral(final Object[] array, final String s, final int n, final Context context, final Scriptable scriptable) {
        return ScriptRuntime.newArrayLiteral(array, decodeIntArray(s, n), context, scriptable);
    }
    
    public static Object newObjectSpecial(final Context context, final Object o, final Object[] array, final Scriptable scriptable, final Scriptable scriptable2, final int n) {
        return ScriptRuntime.newSpecial(context, o, array, scriptable, n);
    }
    
    public static Object[] padStart(final Object[] array, final int n) {
        final Object[] array2 = new Object[array.length + n];
        System.arraycopy(array, 0, array2, n, array.length);
        return array2;
    }
    
    public static void throwStopIteration(final Object o) {
        throw new JavaScriptException(NativeIterator.getStopIterationObject((Scriptable)o), "", 0);
    }
    
    public static Double wrapDouble(final double n) {
        if (n == 0.0) {
            if (1.0 / n > 0.0) {
                return OptRuntime.zeroObj;
            }
        }
        else {
            if (n == 1.0) {
                return OptRuntime.oneObj;
            }
            if (n == -1.0) {
                return OptRuntime.minusOneObj;
            }
            if (n != n) {
                return OptRuntime.NaNobj;
            }
        }
        return new Double(n);
    }
    
    public static class GeneratorState
    {
        static final String CLASS_NAME = "org/mozilla/javascript/optimizer/OptRuntime$GeneratorState";
        static final String resumptionPoint_NAME = "resumptionPoint";
        static final String resumptionPoint_TYPE = "I";
        static final String thisObj_NAME = "thisObj";
        static final String thisObj_TYPE = "Lorg/mozilla/javascript/Scriptable;";
        Object[] localsState;
        int maxLocals;
        int maxStack;
        public int resumptionPoint;
        Object[] stackState;
        public Scriptable thisObj;
        
        GeneratorState(final Scriptable thisObj, final int maxLocals, final int maxStack) {
            this.thisObj = thisObj;
            this.maxLocals = maxLocals;
            this.maxStack = maxStack;
        }
    }
}
