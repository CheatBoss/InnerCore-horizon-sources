package org.mozilla.javascript;

import java.util.stream.*;
import java.util.*;
import java.util.function.*;
import java.lang.reflect.*;

public class NativeArray extends IdScriptableObject implements List
{
    private static final Object ARRAY_TAG;
    private static final int ConstructorId_concat = -13;
    private static final int ConstructorId_every = -17;
    private static final int ConstructorId_filter = -18;
    private static final int ConstructorId_find = -22;
    private static final int ConstructorId_findIndex = -23;
    private static final int ConstructorId_forEach = -19;
    private static final int ConstructorId_indexOf = -15;
    private static final int ConstructorId_isArray = -26;
    private static final int ConstructorId_join = -5;
    private static final int ConstructorId_lastIndexOf = -16;
    private static final int ConstructorId_map = -20;
    private static final int ConstructorId_pop = -9;
    private static final int ConstructorId_push = -8;
    private static final int ConstructorId_reduce = -24;
    private static final int ConstructorId_reduceRight = -25;
    private static final int ConstructorId_reverse = -6;
    private static final int ConstructorId_shift = -10;
    private static final int ConstructorId_slice = -14;
    private static final int ConstructorId_some = -21;
    private static final int ConstructorId_sort = -7;
    private static final int ConstructorId_splice = -12;
    private static final int ConstructorId_unshift = -11;
    private static final int DEFAULT_INITIAL_CAPACITY = 10;
    private static final double GROW_FACTOR = 1.5;
    private static final int Id_concat = 13;
    private static final int Id_constructor = 1;
    private static final int Id_every = 17;
    private static final int Id_filter = 18;
    private static final int Id_find = 22;
    private static final int Id_findIndex = 23;
    private static final int Id_forEach = 19;
    private static final int Id_indexOf = 15;
    private static final int Id_join = 5;
    private static final int Id_lastIndexOf = 16;
    private static final int Id_length = 1;
    private static final int Id_map = 20;
    private static final int Id_pop = 9;
    private static final int Id_push = 8;
    private static final int Id_reduce = 24;
    private static final int Id_reduceRight = 25;
    private static final int Id_reverse = 6;
    private static final int Id_shift = 10;
    private static final int Id_slice = 14;
    private static final int Id_some = 21;
    private static final int Id_sort = 7;
    private static final int Id_splice = 12;
    private static final int Id_toLocaleString = 3;
    private static final int Id_toSource = 4;
    private static final int Id_toString = 2;
    private static final int Id_unshift = 11;
    private static final int MAX_INSTANCE_ID = 1;
    private static final int MAX_PRE_GROW_SIZE = 1431655764;
    private static final int MAX_PROTOTYPE_ID = 25;
    private static final Integer NEGATIVE_ONE;
    private static int maximumInitialCapacity = 0;
    static final long serialVersionUID = 7331366857676127338L;
    private Object[] dense;
    private boolean denseOnly;
    private long length;
    private int lengthAttr;
    
    static {
        ARRAY_TAG = "Array";
        NEGATIVE_ONE = -1;
        NativeArray.maximumInitialCapacity = 10000;
    }
    
    public NativeArray(final long length) {
        this.lengthAttr = 6;
        this.denseOnly = (length <= NativeArray.maximumInitialCapacity);
        if (this.denseOnly) {
            int n;
            if ((n = (int)length) < 10) {
                n = 10;
            }
            Arrays.fill(this.dense = new Object[n], Scriptable.NOT_FOUND);
        }
        this.length = length;
    }
    
    public NativeArray(final Object[] dense) {
        this.lengthAttr = 6;
        this.denseOnly = true;
        this.dense = dense;
        this.length = dense.length;
    }
    
    private ScriptableObject defaultIndexPropertyDescriptor(final Object o) {
        Scriptable parentScope;
        if ((parentScope = this.getParentScope()) == null) {
            parentScope = this;
        }
        final NativeObject nativeObject = new NativeObject();
        ScriptRuntime.setBuiltinProtoAndParent(nativeObject, parentScope, TopLevel.Builtins.Object);
        nativeObject.defineProperty("value", o, 0);
        nativeObject.defineProperty("writable", true, 0);
        nativeObject.defineProperty("enumerable", true, 0);
        nativeObject.defineProperty("configurable", true, 0);
        return nativeObject;
    }
    
    private static void defineElem(final Context context, final Scriptable scriptable, final long n, final Object o) {
        if (n > 2147483647L) {
            scriptable.put(Long.toString(n), scriptable, o);
            return;
        }
        scriptable.put((int)n, scriptable, o);
    }
    
    private static void deleteElem(final Scriptable scriptable, final long n) {
        final int n2 = (int)n;
        if (n2 == n) {
            scriptable.delete(n2);
            return;
        }
        scriptable.delete(Long.toString(n));
    }
    
    private boolean ensureCapacity(final int n) {
        if (n > this.dense.length) {
            if (n > 1431655764) {
                return this.denseOnly = false;
            }
            final Object[] dense = new Object[Math.max(n, (int)(this.dense.length * 1.5))];
            System.arraycopy(this.dense, 0, dense, 0, this.dense.length);
            Arrays.fill(dense, this.dense.length, dense.length, Scriptable.NOT_FOUND);
            this.dense = dense;
        }
        return true;
    }
    
    private static Object getElem(final Context context, final Scriptable scriptable, final long n) {
        final Object rawElem = getRawElem(scriptable, n);
        if (rawElem != Scriptable.NOT_FOUND) {
            return rawElem;
        }
        return Undefined.instance;
    }
    
    static long getLengthProperty(final Context context, final Scriptable scriptable) {
        if (scriptable instanceof NativeString) {
            return ((NativeString)scriptable).getLength();
        }
        if (scriptable instanceof NativeArray) {
            return ((NativeArray)scriptable).getLength();
        }
        final Object property = ScriptableObject.getProperty(scriptable, "length");
        if (property == Scriptable.NOT_FOUND) {
            return 0L;
        }
        return ScriptRuntime.toUint32(property);
    }
    
    static int getMaximumInitialCapacity() {
        return NativeArray.maximumInitialCapacity;
    }
    
    private static Object getRawElem(final Scriptable scriptable, final long n) {
        if (n > 2147483647L) {
            return ScriptableObject.getProperty(scriptable, Long.toString(n));
        }
        return ScriptableObject.getProperty(scriptable, (int)n);
    }
    
    static void init(final Scriptable scriptable, final boolean b) {
        new NativeArray(0L).exportAsJSClass(25, scriptable, b);
    }
    
    private static Object iterativeMethod(final Context context, final int n, Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        final long lengthProperty = getLengthProperty(context, scriptable2);
        Object instance;
        if (array.length > 0) {
            instance = array[0];
        }
        else {
            instance = Undefined.instance;
        }
        if (instance == null || !(instance instanceof Function)) {
            throw ScriptRuntime.notFunctionError(instance);
        }
        if ((n == 22 || n == 23) && !(instance instanceof NativeFunction)) {
            throw ScriptRuntime.notFunctionError(instance);
        }
        final Function function = (Function)instance;
        final Scriptable topLevelScope = ScriptableObject.getTopLevelScope(function);
        Scriptable object;
        if (array.length >= 2 && array[1] != null && array[1] != Undefined.instance) {
            object = ScriptRuntime.toObject(context, scriptable, array[1]);
        }
        else {
            object = topLevelScope;
        }
        if ((22 == n || 23 == n) && object == scriptable2) {
            throw ScriptRuntime.typeError("Array.prototype method called on null or undefined");
        }
        Scriptable array2 = null;
        if (n == 18 || n == 20) {
            int n2;
            if (n == 20) {
                n2 = (int)lengthProperty;
            }
            else {
                n2 = 0;
            }
            array2 = context.newArray(scriptable, n2);
        }
        long n3 = 0L;
        long n4 = 0L;
        while (true) {
            scriptable = scriptable2;
            if (n3 < lengthProperty) {
                final Object[] array3 = new Object[3];
                final Object rawElem = getRawElem(scriptable, n3);
                if (rawElem != Scriptable.NOT_FOUND) {
                    array3[0] = rawElem;
                    array3[1] = n3;
                    array3[2] = scriptable;
                    final Object call = function.call(context, topLevelScope, object, array3);
                    switch (n) {
                        case 23: {
                            if (ScriptRuntime.toBoolean(call)) {
                                return ScriptRuntime.wrapNumber((double)n3);
                            }
                            break;
                        }
                        case 22: {
                            if (ScriptRuntime.toBoolean(call)) {
                                return rawElem;
                            }
                            break;
                        }
                        case 21: {
                            if (ScriptRuntime.toBoolean(call)) {
                                return Boolean.TRUE;
                            }
                            break;
                        }
                        case 20: {
                            defineElem(context, array2, n3, call);
                        }
                        case 18: {
                            if (ScriptRuntime.toBoolean(call)) {
                                final long n5 = n4 + 1L;
                                defineElem(context, array2, n4, array3[0]);
                                n4 = n5;
                                break;
                            }
                            break;
                        }
                        case 17: {
                            if (!ScriptRuntime.toBoolean(call)) {
                                return Boolean.FALSE;
                            }
                            break;
                        }
                    }
                }
                ++n3;
            }
            else {
                switch (n) {
                    default: {
                        return Undefined.instance;
                    }
                    case 23: {
                        return ScriptRuntime.wrapNumber(-1.0);
                    }
                    case 21: {
                        return Boolean.FALSE;
                    }
                    case 18:
                    case 20: {
                        return array2;
                    }
                    case 17: {
                        return Boolean.TRUE;
                    }
                }
            }
        }
    }
    
    private static Object jsConstructor(final Context context, final Scriptable scriptable, final Object[] array) {
        if (array.length == 0) {
            return new NativeArray(0L);
        }
        if (context.getLanguageVersion() == 120) {
            return new NativeArray(array);
        }
        final Object o = array[0];
        if (array.length > 1 || !(o instanceof Number)) {
            return new NativeArray(array);
        }
        final long uint32 = ScriptRuntime.toUint32(o);
        if (uint32 != ((Number)o).doubleValue()) {
            throw ScriptRuntime.constructError("RangeError", ScriptRuntime.getMessage0("msg.arraylength.bad"));
        }
        return new NativeArray(uint32);
    }
    
    private static Scriptable js_concat(final Context context, Scriptable array, Scriptable scriptable, final Object[] array2) {
        array = context.newArray(ScriptableObject.getTopLevelScope(array), 0);
        if (scriptable instanceof NativeArray && array instanceof NativeArray) {
            final NativeArray nativeArray = (NativeArray)scriptable;
            final NativeArray nativeArray2 = (NativeArray)array;
            if (nativeArray.denseOnly && nativeArray2.denseOnly) {
                int n = (int)nativeArray.length;
                boolean denseOnly = true;
                int n3;
                for (int n2 = 0; n2 < array2.length && denseOnly; ++n2, n = n3) {
                    if (array2[n2] instanceof NativeArray) {
                        final NativeArray nativeArray3 = (NativeArray)array2[n2];
                        denseOnly = nativeArray3.denseOnly;
                        n3 = (int)(n + nativeArray3.length);
                    }
                    else {
                        n3 = n + 1;
                    }
                }
                if (denseOnly && nativeArray2.ensureCapacity(n)) {
                    System.arraycopy(nativeArray.dense, 0, nativeArray2.dense, 0, (int)nativeArray.length);
                    int n4 = (int)nativeArray.length;
                    for (int n5 = 0; n5 < array2.length && denseOnly; ++n5) {
                        if (array2[n5] instanceof NativeArray) {
                            final NativeArray nativeArray4 = (NativeArray)array2[n5];
                            System.arraycopy(nativeArray4.dense, 0, nativeArray2.dense, n4, (int)nativeArray4.length);
                            n4 += (int)nativeArray4.length;
                        }
                        else {
                            nativeArray2.dense[n4] = array2[n5];
                            ++n4;
                        }
                    }
                    nativeArray2.length = n;
                    return array;
                }
            }
        }
        int i = 0;
        final boolean js_isArray = js_isArray(scriptable);
        long n6 = 1L;
        long n7;
        if (js_isArray) {
            for (final long lengthProperty = getLengthProperty(context, scriptable), n7 = 0L; n7 < lengthProperty; ++n7) {
                final Object rawElem = getRawElem(scriptable, n7);
                if (rawElem != NativeArray.NOT_FOUND) {
                    defineElem(context, array, n7, rawElem);
                }
            }
        }
        else {
            defineElem(context, array, 0L, scriptable);
            n7 = 0L + 1L;
        }
        while (i < array2.length) {
            if (js_isArray(array2[i])) {
                scriptable = (Scriptable)array2[i];
                for (long lengthProperty2 = getLengthProperty(context, scriptable), n8 = 0L; n8 < lengthProperty2; ++n8) {
                    final Object rawElem2 = getRawElem(scriptable, n8);
                    if (rawElem2 != NativeArray.NOT_FOUND) {
                        defineElem(context, array, n7, rawElem2);
                    }
                    n6 = 1L;
                    ++n7;
                }
            }
            else {
                defineElem(context, array, n7, array2[i]);
                n7 += n6;
            }
            ++i;
        }
        setLengthProperty(context, array, n7);
        return array;
    }
    
    private static Object js_indexOf(final Context context, final Scriptable scriptable, final Object[] array) {
        Object instance;
        if (array.length > 0) {
            instance = array[0];
        }
        else {
            instance = Undefined.instance;
        }
        final long lengthProperty = getLengthProperty(context, scriptable);
        long n;
        if (array.length < 2) {
            n = 0L;
        }
        else {
            long n3;
            final long n2 = n3 = (long)ScriptRuntime.toInteger(array[1]);
            if (n2 < 0L) {
                n3 = n2 + lengthProperty;
                if (n3 < 0L) {
                    n3 = 0L;
                }
            }
            n = n3;
            if (n3 > lengthProperty - 1L) {
                return NativeArray.NEGATIVE_ONE;
            }
        }
        if (scriptable instanceof NativeArray) {
            final NativeArray nativeArray = (NativeArray)scriptable;
            if (nativeArray.denseOnly) {
                final Scriptable prototype = nativeArray.getPrototype();
                for (int n4 = (int)n; n4 < lengthProperty; ++n4) {
                    final Object o = nativeArray.dense[n4];
                    Object property;
                    if ((property = o) == NativeArray.NOT_FOUND) {
                        property = o;
                        if (prototype != null) {
                            property = ScriptableObject.getProperty(prototype, n4);
                        }
                    }
                    if (property != NativeArray.NOT_FOUND && ScriptRuntime.shallowEq(property, instance)) {
                        return n4;
                    }
                }
                return NativeArray.NEGATIVE_ONE;
            }
        }
        while (n < lengthProperty) {
            final Object rawElem = getRawElem(scriptable, n);
            if (rawElem != NativeArray.NOT_FOUND && ScriptRuntime.shallowEq(rawElem, instance)) {
                return n;
            }
            ++n;
        }
        return NativeArray.NEGATIVE_ONE;
    }
    
    private static boolean js_isArray(final Object o) {
        return o instanceof Scriptable && "Array".equals(((Scriptable)o).getClassName());
    }
    
    private static String js_join(final Context context, final Scriptable scriptable, final Object[] array) {
        final long lengthProperty = getLengthProperty(context, scriptable);
        final int n = (int)lengthProperty;
        if (lengthProperty != n) {
            throw Context.reportRuntimeError1("msg.arraylength.too.big", String.valueOf(lengthProperty));
        }
        final int length = array.length;
        final int n2 = 0;
        int i = 0;
        String string;
        if (length >= 1 && array[0] != Undefined.instance) {
            string = ScriptRuntime.toString(array[0]);
        }
        else {
            string = ",";
        }
        if (scriptable instanceof NativeArray) {
            final NativeArray nativeArray = (NativeArray)scriptable;
            if (nativeArray.denseOnly) {
                final StringBuilder sb = new StringBuilder();
                while (i < n) {
                    if (i != 0) {
                        sb.append(string);
                    }
                    if (i < nativeArray.dense.length) {
                        final Object o = nativeArray.dense[i];
                        if (o != null && o != Undefined.instance && o != Scriptable.NOT_FOUND) {
                            sb.append(ScriptRuntime.toString(o));
                        }
                    }
                    ++i;
                }
                return sb.toString();
            }
        }
        if (n == 0) {
            return "";
        }
        final String[] array2 = new String[n];
        int n3 = 0;
        int n4;
        for (int j = 0; j != n; ++j, n3 = n4) {
            final Object elem = getElem(context, scriptable, j);
            n4 = n3;
            if (elem != null) {
                n4 = n3;
                if (elem != Undefined.instance) {
                    final String string2 = ScriptRuntime.toString(elem);
                    n4 = n3 + string2.length();
                    array2[j] = string2;
                }
            }
        }
        final StringBuilder sb2 = new StringBuilder(n3 + (n - 1) * string.length());
        for (int k = n2; k != n; ++k) {
            if (k != 0) {
                sb2.append(string);
            }
            final String s = array2[k];
            if (s != null) {
                sb2.append(s);
            }
        }
        return sb2.toString();
    }
    
    private static Object js_lastIndexOf(final Context context, final Scriptable scriptable, final Object[] array) {
        Object instance;
        if (array.length > 0) {
            instance = array[0];
        }
        else {
            instance = Undefined.instance;
        }
        final long lengthProperty = getLengthProperty(context, scriptable);
        long n;
        if (array.length < 2) {
            n = lengthProperty - 1L;
        }
        else {
            final long n2 = (long)ScriptRuntime.toInteger(array[1]);
            long n3;
            if (n2 >= lengthProperty) {
                n3 = lengthProperty - 1L;
            }
            else {
                n3 = n2;
                if (n2 < 0L) {
                    n3 = n2 + lengthProperty;
                }
            }
            n = n3;
            if (n3 < 0L) {
                return NativeArray.NEGATIVE_ONE;
            }
        }
        if (scriptable instanceof NativeArray) {
            final NativeArray nativeArray = (NativeArray)scriptable;
            if (nativeArray.denseOnly) {
                final Scriptable prototype = nativeArray.getPrototype();
                for (int i = (int)n; i >= 0; --i) {
                    final Object o = nativeArray.dense[i];
                    Object property;
                    if ((property = o) == NativeArray.NOT_FOUND) {
                        property = o;
                        if (prototype != null) {
                            property = ScriptableObject.getProperty(prototype, i);
                        }
                    }
                    if (property != NativeArray.NOT_FOUND && ScriptRuntime.shallowEq(property, instance)) {
                        return i;
                    }
                }
                return NativeArray.NEGATIVE_ONE;
            }
        }
        while (n >= 0L) {
            final Object rawElem = getRawElem(scriptable, n);
            if (rawElem != NativeArray.NOT_FOUND && ScriptRuntime.shallowEq(rawElem, instance)) {
                return n;
            }
            --n;
        }
        return NativeArray.NEGATIVE_ONE;
    }
    
    private static Object js_pop(final Context context, final Scriptable scriptable, final Object[] array) {
        if (scriptable instanceof NativeArray) {
            final NativeArray nativeArray = (NativeArray)scriptable;
            if (nativeArray.denseOnly && nativeArray.length > 0L) {
                --nativeArray.length;
                final Object o = nativeArray.dense[(int)nativeArray.length];
                nativeArray.dense[(int)nativeArray.length] = NativeArray.NOT_FOUND;
                return o;
            }
        }
        long lengthProperty = getLengthProperty(context, scriptable);
        Object o2;
        if (lengthProperty > 0L) {
            --lengthProperty;
            o2 = getElem(context, scriptable, lengthProperty);
            deleteElem(scriptable, lengthProperty);
        }
        else {
            o2 = Undefined.instance;
        }
        setLengthProperty(context, scriptable, lengthProperty);
        return o2;
    }
    
    private static Object js_push(final Context context, final Scriptable scriptable, final Object[] array) {
        final boolean b = scriptable instanceof NativeArray;
        final int n = 0;
        int i = 0;
        if (b) {
            final NativeArray nativeArray = (NativeArray)scriptable;
            if (nativeArray.denseOnly && nativeArray.ensureCapacity((int)nativeArray.length + array.length)) {
                while (i < array.length) {
                    nativeArray.dense[(int)(nativeArray.length++)] = array[i];
                    ++i;
                }
                return ScriptRuntime.wrapNumber((double)nativeArray.length);
            }
        }
        final long lengthProperty = getLengthProperty(context, scriptable);
        for (int j = n; j < array.length; ++j) {
            setElem(context, scriptable, lengthProperty + j, array[j]);
        }
        final Object setLengthProperty = setLengthProperty(context, scriptable, lengthProperty + array.length);
        if (context.getLanguageVersion() != 120) {
            return setLengthProperty;
        }
        if (array.length == 0) {
            return Undefined.instance;
        }
        return array[array.length - 1];
    }
    
    private static Scriptable js_reverse(final Context context, final Scriptable scriptable, final Object[] array) {
        if (scriptable instanceof NativeArray) {
            final NativeArray nativeArray = (NativeArray)scriptable;
            if (nativeArray.denseOnly) {
                for (int i = 0, n = (int)nativeArray.length - 1; i < n; ++i, --n) {
                    final Object o = nativeArray.dense[i];
                    nativeArray.dense[i] = nativeArray.dense[n];
                    nativeArray.dense[n] = o;
                }
                return scriptable;
            }
        }
        final long lengthProperty = getLengthProperty(context, scriptable);
        for (long n2 = lengthProperty / 2L, n3 = 0L; n3 < n2; ++n3) {
            final long n4 = lengthProperty - n3 - 1L;
            final Object rawElem = getRawElem(scriptable, n3);
            setRawElem(context, scriptable, n3, getRawElem(scriptable, n4));
            setRawElem(context, scriptable, n4, rawElem);
        }
        return scriptable;
    }
    
    private static Object js_shift(final Context context, final Scriptable scriptable, final Object[] array) {
        if (scriptable instanceof NativeArray) {
            final NativeArray nativeArray = (NativeArray)scriptable;
            if (nativeArray.denseOnly && nativeArray.length > 0L) {
                --nativeArray.length;
                final Object o = nativeArray.dense[0];
                System.arraycopy(nativeArray.dense, 1, nativeArray.dense, 0, (int)nativeArray.length);
                nativeArray.dense[(int)nativeArray.length] = NativeArray.NOT_FOUND;
                if (o == NativeArray.NOT_FOUND) {
                    return Undefined.instance;
                }
                return o;
            }
        }
        long lengthProperty = getLengthProperty(context, scriptable);
        Object o2;
        if (lengthProperty > 0L) {
            final long n = lengthProperty - 1L;
            o2 = getElem(context, scriptable, 0L);
            if (n > 0L) {
                for (long n2 = 1L; n2 <= n; ++n2) {
                    setRawElem(context, scriptable, n2 - 1L, getRawElem(scriptable, n2));
                }
            }
            deleteElem(scriptable, n);
            lengthProperty = n;
        }
        else {
            o2 = Undefined.instance;
        }
        setLengthProperty(context, scriptable, lengthProperty);
        return o2;
    }
    
    private Scriptable js_slice(final Context context, final Scriptable scriptable, final Object[] array) {
        final Scriptable array2 = context.newArray(ScriptableObject.getTopLevelScope(this), 0);
        long n = getLengthProperty(context, scriptable);
        long sliceIndex;
        if (array.length == 0) {
            sliceIndex = 0L;
        }
        else {
            sliceIndex = toSliceIndex(ScriptRuntime.toInteger(array[0]), n);
            if (array.length != 1 && array[1] != Undefined.instance) {
                n = toSliceIndex(ScriptRuntime.toInteger(array[1]), n);
            }
        }
        for (long n2 = sliceIndex; n2 < n; ++n2) {
            final Object rawElem = getRawElem(scriptable, n2);
            if (rawElem != NativeArray.NOT_FOUND) {
                defineElem(context, array2, n2 - sliceIndex, rawElem);
            }
        }
        setLengthProperty(context, array2, Math.max(0L, n - sliceIndex));
        return array2;
    }
    
    private static Scriptable js_sort(final Context context, final Scriptable scriptable, final Scriptable scriptable2, Object[] array) {
        final int length = array.length;
        final int n = 0;
        Comparator<Object> comparator;
        if (length > 0 && Undefined.instance != array[0]) {
            comparator = new Comparator<Object>() {
                final /* synthetic */ Object[] val$cmpBuf = new Object[2];
                final /* synthetic */ Scriptable val$funThis = ScriptRuntime.lastStoredScriptable(context);
                final /* synthetic */ Callable val$jsCompareFunction = ScriptRuntime.getValueFunctionAndThis(array[0], context);
                
                @Override
                public int compare(final Object o, final Object o2) {
                    final Object not_FOUND = Scriptable.NOT_FOUND;
                    final boolean b = true;
                    int n = 1;
                    if (o == not_FOUND) {
                        if (o2 == Scriptable.NOT_FOUND) {
                            n = 0;
                        }
                        return n;
                    }
                    if (o2 == Scriptable.NOT_FOUND) {
                        return -1;
                    }
                    if (o == Undefined.instance) {
                        int n2 = b ? 1 : 0;
                        if (o2 == Undefined.instance) {
                            n2 = 0;
                        }
                        return n2;
                    }
                    if (o2 == Undefined.instance) {
                        return -1;
                    }
                    this.val$cmpBuf[0] = o;
                    this.val$cmpBuf[1] = o2;
                    final double number = ScriptRuntime.toNumber(this.val$jsCompareFunction.call(context, scriptable, this.val$funThis, this.val$cmpBuf));
                    if (number < 0.0) {
                        return -1;
                    }
                    if (number > 0.0) {
                        return 1;
                    }
                    return 0;
                }
                
                @Override
                public Comparator<Object> reversed() {
                    return (Comparator<Object>)Comparator-CC.$default$reversed((Comparator)this);
                }
                
                @Override
                public Comparator<Object> thenComparing(final Comparator<?> p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
                
                @Override
                public <U extends Comparable<? super U>> Comparator<Object> thenComparing(final java.util.function.Function<?, ? extends U> p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
                
                @Override
                public <U> Comparator<Object> thenComparing(final java.util.function.Function<?, ? extends U> p0, final Comparator<? super U> p1) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
                
                @Override
                public Comparator<Object> thenComparingDouble(final ToDoubleFunction<?> p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
                
                @Override
                public Comparator<Object> thenComparingInt(final ToIntFunction<?> p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
                
                @Override
                public Comparator<Object> thenComparingLong(final ToLongFunction<?> p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
            };
        }
        else {
            comparator = new Comparator<Object>() {
                @Override
                public int compare(final Object o, final Object o2) {
                    final Object not_FOUND = Scriptable.NOT_FOUND;
                    final boolean b = true;
                    int n = 1;
                    if (o == not_FOUND) {
                        if (o2 == Scriptable.NOT_FOUND) {
                            n = 0;
                        }
                        return n;
                    }
                    if (o2 == Scriptable.NOT_FOUND) {
                        return -1;
                    }
                    if (o == Undefined.instance) {
                        int n2 = b ? 1 : 0;
                        if (o2 == Undefined.instance) {
                            n2 = 0;
                        }
                        return n2;
                    }
                    if (o2 == Undefined.instance) {
                        return -1;
                    }
                    return ScriptRuntime.toString(o).compareTo(ScriptRuntime.toString(o2));
                }
                
                @Override
                public Comparator<Object> reversed() {
                    return (Comparator<Object>)Comparator-CC.$default$reversed((Comparator)this);
                }
                
                @Override
                public Comparator<Object> thenComparing(final Comparator<?> p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
                
                @Override
                public <U extends Comparable<? super U>> Comparator<Object> thenComparing(final java.util.function.Function<?, ? extends U> p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
                
                @Override
                public <U> Comparator<Object> thenComparing(final java.util.function.Function<?, ? extends U> p0, final Comparator<? super U> p1) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
                
                @Override
                public Comparator<Object> thenComparingDouble(final ToDoubleFunction<?> p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
                
                @Override
                public Comparator<Object> thenComparingInt(final ToIntFunction<?> p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
                
                @Override
                public Comparator<Object> thenComparingLong(final ToLongFunction<?> p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
            };
        }
        final long lengthProperty = getLengthProperty(context, scriptable2);
        final int n2 = (int)lengthProperty;
        if (lengthProperty != n2) {
            throw Context.reportRuntimeError1("msg.arraylength.too.big", String.valueOf(lengthProperty));
        }
        array = new Object[n2];
        for (int i = 0; i != n2; ++i) {
            array[i] = getRawElem(scriptable2, i);
        }
        Arrays.sort(array, comparator);
        for (int j = n; j < n2; ++j) {
            setRawElem(context, scriptable2, j, array[j]);
        }
        return scriptable2;
    }
    
    private static Object js_splice(final Context context, Scriptable o, final Scriptable scriptable, final Object[] array) {
        NativeArray nativeArray = null;
        boolean denseOnly = false;
        if (scriptable instanceof NativeArray) {
            nativeArray = (NativeArray)scriptable;
            denseOnly = nativeArray.denseOnly;
        }
        o = ScriptableObject.getTopLevelScope((Scriptable)o);
        final int length = array.length;
        if (length == 0) {
            return context.newArray((Scriptable)o, 0);
        }
        final long lengthProperty = getLengthProperty(context, scriptable);
        final long sliceIndex = toSliceIndex(ScriptRuntime.toInteger(array[0]), lengthProperty);
        int n = length - 1;
        long n2;
        if (array.length == 1) {
            n2 = lengthProperty - sliceIndex;
        }
        else {
            final double integer = ScriptRuntime.toInteger(array[1]);
            if (integer < 0.0) {
                n2 = 0L;
            }
            else if (integer > lengthProperty - sliceIndex) {
                n2 = lengthProperty - sliceIndex;
            }
            else {
                n2 = (long)integer;
            }
            --n;
        }
        final long n3 = sliceIndex + n2;
        if (n2 != 0L) {
            if (n2 == 1L && context.getLanguageVersion() == 120) {
                o = getElem(context, scriptable, sliceIndex);
            }
            else if (denseOnly) {
                final int n4 = (int)(n3 - sliceIndex);
                final Object[] array2 = new Object[n4];
                System.arraycopy(nativeArray.dense, (int)sliceIndex, array2, 0, n4);
                o = context.newArray((Scriptable)o, array2);
            }
            else {
                o = context.newArray((Scriptable)o, 0);
                for (long n5 = sliceIndex; n5 != n3; ++n5) {
                    final Object rawElem = getRawElem(scriptable, n5);
                    if (rawElem != NativeArray.NOT_FOUND) {
                        setElem(context, (Scriptable)o, n5 - sliceIndex, rawElem);
                    }
                }
                setLengthProperty(context, (Scriptable)o, n3 - sliceIndex);
            }
        }
        else if (context.getLanguageVersion() == 120) {
            o = Undefined.instance;
        }
        else {
            o = context.newArray((Scriptable)o, 0);
        }
        final long n6 = lengthProperty;
        final long n7 = n - n2;
        if (denseOnly && n6 + n7 < 2147483647L) {
            final int n8 = (int)(n6 + n7);
            final NativeArray nativeArray2 = nativeArray;
            if (nativeArray2.ensureCapacity(n8)) {
                System.arraycopy(nativeArray2.dense, (int)n3, nativeArray2.dense, (int)(sliceIndex + n), (int)(n6 - n3));
                if (n > 0) {
                    System.arraycopy(array, 2, nativeArray2.dense, (int)sliceIndex, n);
                }
                if (n7 < 0L) {
                    Arrays.fill(nativeArray2.dense, (int)(n6 + n7), (int)n6, NativeArray.NOT_FOUND);
                }
                nativeArray2.length = n6 + n7;
                return o;
            }
        }
        if (n7 > 0L) {
            for (long n9 = n6 - 1L; n9 >= n3; --n9) {
                setRawElem(context, scriptable, n9 + n7, getRawElem(scriptable, n9));
            }
        }
        else if (n7 < 0L) {
            for (long n10 = n3; n10 < n6; ++n10) {
                setRawElem(context, scriptable, n10 + n7, getRawElem(scriptable, n10));
            }
            for (long n11 = n6 + n7; n11 < n6; ++n11) {
                deleteElem(scriptable, n11);
            }
        }
        final int length2 = array.length;
        for (int i = 0; i < n; ++i) {
            setElem(context, scriptable, sliceIndex + i, array[i + (length2 - n)]);
        }
        setLengthProperty(context, scriptable, n6 + n7);
        return o;
    }
    
    private static Object js_unshift(final Context context, final Scriptable scriptable, final Object[] array) {
        final boolean b = scriptable instanceof NativeArray;
        int i = 0;
        final int n = 0;
        if (b) {
            final NativeArray nativeArray = (NativeArray)scriptable;
            if (nativeArray.denseOnly && nativeArray.ensureCapacity((int)nativeArray.length + array.length)) {
                System.arraycopy(nativeArray.dense, 0, nativeArray.dense, array.length, (int)nativeArray.length);
                for (int j = n; j < array.length; ++j) {
                    nativeArray.dense[j] = array[j];
                }
                nativeArray.length += array.length;
                return ScriptRuntime.wrapNumber((double)nativeArray.length);
            }
        }
        final long lengthProperty = getLengthProperty(context, scriptable);
        final int length = array.length;
        if (array.length > 0) {
            if (lengthProperty > 0L) {
                for (long n2 = lengthProperty - 1L; n2 >= 0L; --n2) {
                    setRawElem(context, scriptable, n2 + length, getRawElem(scriptable, n2));
                }
            }
            while (i < array.length) {
                setElem(context, scriptable, i, array[i]);
                ++i;
            }
        }
        return setLengthProperty(context, scriptable, lengthProperty + array.length);
    }
    
    private static Object reduceMethod(final Context context, int n, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        final long lengthProperty = getLengthProperty(context, scriptable2);
        Object instance;
        if (array.length > 0) {
            instance = array[0];
        }
        else {
            instance = Undefined.instance;
        }
        if (instance == null || !(instance instanceof Function)) {
            throw ScriptRuntime.notFunctionError(instance);
        }
        final Function function = (Function)instance;
        final Scriptable topLevelScope = ScriptableObject.getTopLevelScope(function);
        if (n == 24) {
            n = 1;
        }
        else {
            n = 0;
        }
        Object o;
        if (array.length > 1) {
            o = array[1];
        }
        else {
            o = Scriptable.NOT_FOUND;
        }
        for (long n2 = 0L; n2 < lengthProperty; ++n2) {
            long n3;
            if (n != 0) {
                n3 = n2;
            }
            else {
                n3 = lengthProperty - 1L - n2;
            }
            final Object rawElem = getRawElem(scriptable2, n3);
            if (rawElem != Scriptable.NOT_FOUND) {
                if (o == Scriptable.NOT_FOUND) {
                    o = rawElem;
                }
                else {
                    o = function.call(context, topLevelScope, topLevelScope, new Object[] { o, rawElem, n3, scriptable2 });
                }
            }
        }
        if (o == Scriptable.NOT_FOUND) {
            throw ScriptRuntime.typeError0("msg.empty.array.reduce");
        }
        return o;
    }
    
    private static void setElem(final Context context, final Scriptable scriptable, final long n, final Object o) {
        if (n > 2147483647L) {
            ScriptableObject.putProperty(scriptable, Long.toString(n), o);
            return;
        }
        ScriptableObject.putProperty(scriptable, (int)n, o);
    }
    
    private void setLength(final Object o) {
        if ((this.lengthAttr & 0x1) != 0x0) {
            return;
        }
        final double number = ScriptRuntime.toNumber(o);
        final long uint32 = ScriptRuntime.toUint32(number);
        if (uint32 != number) {
            throw ScriptRuntime.constructError("RangeError", ScriptRuntime.getMessage0("msg.arraylength.bad"));
        }
        final boolean denseOnly = this.denseOnly;
        int i = 0;
        if (denseOnly) {
            if (uint32 < this.length) {
                Arrays.fill(this.dense, (int)uint32, this.dense.length, NativeArray.NOT_FOUND);
                this.length = uint32;
                return;
            }
            if (uint32 < 1431655764L && uint32 < this.length * 1.5 && this.ensureCapacity((int)uint32)) {
                this.length = uint32;
                return;
            }
            this.denseOnly = false;
        }
        if (uint32 < this.length) {
            if (this.length - uint32 > 4096L) {
                for (Object[] ids = this.getIds(); i < ids.length; ++i) {
                    final Object o2 = ids[i];
                    if (o2 instanceof String) {
                        final String s = (String)o2;
                        if (toArrayIndex(s) >= uint32) {
                            this.delete(s);
                        }
                    }
                    else {
                        final int intValue = (int)o2;
                        if (intValue >= uint32) {
                            this.delete(intValue);
                        }
                    }
                }
            }
            else {
                for (long n = uint32; n < this.length; ++n) {
                    deleteElem(this, n);
                }
            }
        }
        this.length = uint32;
    }
    
    private static Object setLengthProperty(final Context context, final Scriptable scriptable, final long n) {
        final Number wrapNumber = ScriptRuntime.wrapNumber((double)n);
        ScriptableObject.putProperty(scriptable, "length", wrapNumber);
        return wrapNumber;
    }
    
    static void setMaximumInitialCapacity(final int maximumInitialCapacity) {
        NativeArray.maximumInitialCapacity = maximumInitialCapacity;
    }
    
    private static void setRawElem(final Context context, final Scriptable scriptable, final long n, final Object o) {
        if (o == NativeArray.NOT_FOUND) {
            deleteElem(scriptable, n);
            return;
        }
        setElem(context, scriptable, n, o);
    }
    
    private static long toArrayIndex(final double n) {
        if (n == n) {
            final long uint32 = ScriptRuntime.toUint32(n);
            if (uint32 == n && uint32 != 4294967295L) {
                return uint32;
            }
        }
        return -1L;
    }
    
    private static long toArrayIndex(final Object o) {
        if (o instanceof String) {
            return toArrayIndex((String)o);
        }
        if (o instanceof Number) {
            return toArrayIndex(((Number)o).doubleValue());
        }
        return -1L;
    }
    
    private static long toArrayIndex(final String s) {
        final long arrayIndex = toArrayIndex(ScriptRuntime.toNumber(s));
        if (Long.toString(arrayIndex).equals(s)) {
            return arrayIndex;
        }
        return -1L;
    }
    
    private static int toDenseIndex(final Object o) {
        final long arrayIndex = toArrayIndex(o);
        if (0L <= arrayIndex && arrayIndex < 2147483647L) {
            return (int)arrayIndex;
        }
        return -1;
    }
    
    private static long toSliceIndex(final double n, final long n2) {
        if (n < 0.0) {
            if (n2 + n < 0.0) {
                return 0L;
            }
            return (long)(n2 + n);
        }
        else {
            if (n > n2) {
                return n2;
            }
            return (long)n;
        }
    }
    
    private static String toStringHelper(final Context p0, final Scriptable p1, final Scriptable p2, final boolean p3, final boolean p4) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore          17
        //     3: aload_0        
        //     4: aload           17
        //     6: invokestatic    org/mozilla/javascript/NativeArray.getLengthProperty:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)J
        //     9: lstore          10
        //    11: new             Ljava/lang/StringBuilder;
        //    14: dup            
        //    15: sipush          256
        //    18: invokespecial   java/lang/StringBuilder.<init>:(I)V
        //    21: astore          19
        //    23: iload_3        
        //    24: ifeq            43
        //    27: aload           19
        //    29: bipush          91
        //    31: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //    34: pop            
        //    35: ldc_w           ", "
        //    38: astore          16
        //    40: goto            48
        //    43: ldc_w           ","
        //    46: astore          16
        //    48: iconst_0       
        //    49: istore          5
        //    51: iconst_0       
        //    52: istore          8
        //    54: lconst_0       
        //    55: lstore          12
        //    57: aload_0        
        //    58: getfield        org/mozilla/javascript/Context.iterating:Lorg/mozilla/javascript/ObjToIntMap;
        //    61: ifnonnull       86
        //    64: iconst_1       
        //    65: istore          6
        //    67: iconst_0       
        //    68: istore          9
        //    70: aload_0        
        //    71: new             Lorg/mozilla/javascript/ObjToIntMap;
        //    74: dup            
        //    75: bipush          31
        //    77: invokespecial   org/mozilla/javascript/ObjToIntMap.<init>:(I)V
        //    80: putfield        org/mozilla/javascript/Context.iterating:Lorg/mozilla/javascript/ObjToIntMap;
        //    83: goto            100
        //    86: iconst_0       
        //    87: istore          6
        //    89: aload_0        
        //    90: getfield        org/mozilla/javascript/Context.iterating:Lorg/mozilla/javascript/ObjToIntMap;
        //    93: aload           17
        //    95: invokevirtual   org/mozilla/javascript/ObjToIntMap.has:(Ljava/lang/Object;)Z
        //    98: istore          9
        //   100: lload           10
        //   102: lstore          14
        //   104: lload           12
        //   106: lstore          14
        //   108: iload           9
        //   110: ifne            408
        //   113: lload           10
        //   115: lstore          14
        //   117: aload_0        
        //   118: getfield        org/mozilla/javascript/Context.iterating:Lorg/mozilla/javascript/ObjToIntMap;
        //   121: aload           17
        //   123: iconst_0       
        //   124: invokevirtual   org/mozilla/javascript/ObjToIntMap.put:(Ljava/lang/Object;I)V
        //   127: iload_3        
        //   128: ifeq            470
        //   131: lload           10
        //   133: lstore          14
        //   135: aload_0        
        //   136: invokevirtual   org/mozilla/javascript/Context.getLanguageVersion:()I
        //   139: sipush          150
        //   142: if_icmpge       460
        //   145: goto            470
        //   148: lload           10
        //   150: lstore          14
        //   152: lload           12
        //   154: lstore          14
        //   156: lload           12
        //   158: lload           10
        //   160: lcmp           
        //   161: ifge            408
        //   164: lload           12
        //   166: lconst_0       
        //   167: lcmp           
        //   168: ifle            183
        //   171: lload           10
        //   173: lstore          14
        //   175: aload           19
        //   177: aload           16
        //   179: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   182: pop            
        //   183: lload           10
        //   185: lstore          14
        //   187: aload_2        
        //   188: lload           12
        //   190: invokestatic    org/mozilla/javascript/NativeArray.getRawElem:(Lorg/mozilla/javascript/Scriptable;J)Ljava/lang/Object;
        //   193: astore          18
        //   195: lload           10
        //   197: lstore          14
        //   199: getstatic       org/mozilla/javascript/NativeArray.NOT_FOUND:Ljava/lang/Object;
        //   202: astore          17
        //   204: aload           18
        //   206: aload           17
        //   208: if_acmpeq       383
        //   211: iload           7
        //   213: ifeq            236
        //   216: aload           18
        //   218: ifnull          483
        //   221: lload           10
        //   223: lstore          14
        //   225: aload           18
        //   227: getstatic       org/mozilla/javascript/Undefined.instance:Ljava/lang/Object;
        //   230: if_acmpne       236
        //   233: goto            483
        //   236: iconst_1       
        //   237: istore          5
        //   239: iload_3        
        //   240: ifeq            263
        //   243: lload           10
        //   245: lstore          14
        //   247: aload           19
        //   249: aload_0        
        //   250: aload_1        
        //   251: aload           18
        //   253: invokestatic    org/mozilla/javascript/ScriptRuntime.uneval:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/Object;)Ljava/lang/String;
        //   256: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   259: pop            
        //   260: goto            386
        //   263: lload           10
        //   265: lstore          14
        //   267: aload           18
        //   269: instanceof      Ljava/lang/String;
        //   272: ifeq            331
        //   275: lload           10
        //   277: lstore          14
        //   279: aload           18
        //   281: checkcast       Ljava/lang/String;
        //   284: astore          17
        //   286: iload_3        
        //   287: ifeq            320
        //   290: aload           19
        //   292: bipush          34
        //   294: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   297: pop            
        //   298: aload           19
        //   300: aload           17
        //   302: invokestatic    org/mozilla/javascript/ScriptRuntime.escapeString:(Ljava/lang/String;)Ljava/lang/String;
        //   305: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   308: pop            
        //   309: aload           19
        //   311: bipush          34
        //   313: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   316: pop            
        //   317: goto            486
        //   320: aload           19
        //   322: aload           17
        //   324: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   327: pop            
        //   328: goto            486
        //   331: aload           18
        //   333: astore          17
        //   335: iload           4
        //   337: ifeq            369
        //   340: aload           18
        //   342: ldc_w           "toLocaleString"
        //   345: aload_0        
        //   346: aload_1        
        //   347: invokestatic    org/mozilla/javascript/ScriptRuntime.getPropFunctionAndThis:(Ljava/lang/Object;Ljava/lang/String;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Callable;
        //   350: aload_0        
        //   351: aload_1        
        //   352: aload_0        
        //   353: invokestatic    org/mozilla/javascript/ScriptRuntime.lastStoredScriptable:(Lorg/mozilla/javascript/Context;)Lorg/mozilla/javascript/Scriptable;
        //   356: getstatic       org/mozilla/javascript/ScriptRuntime.emptyArgs:[Ljava/lang/Object;
        //   359: invokeinterface org/mozilla/javascript/Callable.call:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;
        //   364: astore          17
        //   366: goto            369
        //   369: aload           19
        //   371: aload           17
        //   373: invokestatic    org/mozilla/javascript/ScriptRuntime.toString:(Ljava/lang/Object;)Ljava/lang/String;
        //   376: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   379: pop            
        //   380: goto            386
        //   383: iconst_0       
        //   384: istore          5
        //   386: lload           12
        //   388: lconst_1       
        //   389: ladd           
        //   390: lstore          12
        //   392: goto            148
        //   395: astore_1       
        //   396: iload           6
        //   398: ifeq            406
        //   401: aload_0        
        //   402: aconst_null    
        //   403: putfield        org/mozilla/javascript/Context.iterating:Lorg/mozilla/javascript/ObjToIntMap;
        //   406: aload_1        
        //   407: athrow         
        //   408: iload           6
        //   410: ifeq            418
        //   413: aload_0        
        //   414: aconst_null    
        //   415: putfield        org/mozilla/javascript/Context.iterating:Lorg/mozilla/javascript/ObjToIntMap;
        //   418: iload_3        
        //   419: ifeq            454
        //   422: iload           5
        //   424: ifne            446
        //   427: lload           14
        //   429: lconst_0       
        //   430: lcmp           
        //   431: ifle            446
        //   434: aload           19
        //   436: ldc_w           ", ]"
        //   439: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   442: pop            
        //   443: goto            454
        //   446: aload           19
        //   448: bipush          93
        //   450: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   453: pop            
        //   454: aload           19
        //   456: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   459: areturn        
        //   460: iconst_0       
        //   461: istore          7
        //   463: goto            473
        //   466: astore_1       
        //   467: goto            396
        //   470: iconst_1       
        //   471: istore          7
        //   473: lconst_0       
        //   474: lstore          12
        //   476: iload           8
        //   478: istore          5
        //   480: goto            148
        //   483: goto            383
        //   486: goto            386
        //   489: astore_1       
        //   490: goto            396
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  117    127    395    396    Any
        //  135    145    466    470    Any
        //  175    183    466    470    Any
        //  187    195    395    396    Any
        //  199    204    395    396    Any
        //  225    233    466    470    Any
        //  247    260    466    470    Any
        //  267    275    395    396    Any
        //  279    286    395    396    Any
        //  290    317    489    493    Any
        //  320    328    489    493    Any
        //  340    366    489    493    Any
        //  369    380    489    493    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 236, Size: 236
        //     at java.util.ArrayList.rangeCheck(Unknown Source)
        //     at java.util.ArrayList.get(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public void add(final int n, final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(final int n, final Collection collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(final Collection collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.indexOf(o) > -1;
    }
    
    @Override
    public boolean containsAll(final Collection collection) {
        final Iterator<Object> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (!this.contains(iterator.next())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    protected void defineOwnProperty(final Context context, final Object o, final ScriptableObject scriptableObject, final boolean b) {
        if (this.dense != null) {
            final Object[] dense = this.dense;
            this.dense = null;
            int i = 0;
            this.denseOnly = false;
            while (i < dense.length) {
                if (dense[i] != NativeArray.NOT_FOUND) {
                    this.put(i, this, dense[i]);
                }
                ++i;
            }
        }
        final long arrayIndex = toArrayIndex(o);
        if (arrayIndex >= this.length) {
            this.length = arrayIndex + 1L;
        }
        super.defineOwnProperty(context, o, scriptableObject, b);
    }
    
    @Override
    public void delete(final int n) {
        if (this.dense != null && n >= 0 && n < this.dense.length && !this.isSealed() && (this.denseOnly || !this.isGetterOrSetter(null, n, true))) {
            this.dense[n] = NativeArray.NOT_FOUND;
            return;
        }
        super.delete(n);
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, Object[] array) {
        if (!idFunctionObject.hasTag(NativeArray.ARRAY_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        int methodId = idFunctionObject.methodId();
        Scriptable object = scriptable2;
        while (true) {
            boolean b = true;
            final boolean b2 = true;
            final int n = 0;
            switch (methodId) {
                default: {
                    switch (methodId) {
                        default: {
                            throw new IllegalArgumentException(String.valueOf(methodId));
                        }
                        case 24:
                        case 25: {
                            return reduceMethod(context, methodId, scriptable, object, array);
                        }
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23: {
                            return iterativeMethod(context, methodId, scriptable, object, array);
                        }
                        case 16: {
                            return js_lastIndexOf(context, object, array);
                        }
                        case 15: {
                            return js_indexOf(context, object, array);
                        }
                        case 14: {
                            return this.js_slice(context, object, array);
                        }
                        case 13: {
                            return js_concat(context, scriptable, object, array);
                        }
                        case 12: {
                            return js_splice(context, scriptable, object, array);
                        }
                        case 11: {
                            return js_unshift(context, object, array);
                        }
                        case 10: {
                            return js_shift(context, object, array);
                        }
                        case 9: {
                            return js_pop(context, object, array);
                        }
                        case 8: {
                            return js_push(context, object, array);
                        }
                        case 7: {
                            return js_sort(context, scriptable, object, array);
                        }
                        case 6: {
                            return js_reverse(context, object, array);
                        }
                        case 5: {
                            return js_join(context, object, array);
                        }
                        case 4: {
                            return toStringHelper(context, scriptable, object, true, false);
                        }
                        case 3: {
                            return toStringHelper(context, scriptable, object, false, true);
                        }
                        case 2: {
                            return toStringHelper(context, scriptable, object, context.hasFeature(4), false);
                        }
                        case 1: {
                            if (object != null || !b2) {
                                return idFunctionObject.construct(context, scriptable, array);
                            }
                            return jsConstructor(context, scriptable, array);
                        }
                    }
                    break;
                }
                case -25:
                case -24:
                case -23:
                case -22:
                case -21:
                case -20:
                case -19:
                case -18:
                case -17:
                case -16:
                case -15:
                case -14:
                case -13:
                case -12:
                case -11:
                case -10:
                case -9:
                case -8:
                case -7:
                case -6:
                case -5: {
                    Object[] array2 = array;
                    if (array.length > 0) {
                        object = ScriptRuntime.toObject(context, scriptable, array[0]);
                        array2 = new Object[array.length - 1];
                        for (int i = n; i < array2.length; ++i) {
                            array2[i] = array[i + 1];
                        }
                    }
                    methodId = -methodId;
                    array = array2;
                    continue;
                }
                case -26: {
                    if (array.length <= 0 || !js_isArray(array[0])) {
                        b = false;
                    }
                    return b;
                }
            }
        }
    }
    
    @Override
    protected void fillConstructorProperties(final IdFunctionObject idFunctionObject) {
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -5, "join", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -6, "reverse", 0);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -7, "sort", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -8, "push", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -9, "pop", 0);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -10, "shift", 0);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -11, "unshift", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -12, "splice", 2);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -13, "concat", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -14, "slice", 2);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -15, "indexOf", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -16, "lastIndexOf", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -17, "every", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -18, "filter", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -19, "forEach", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -20, "map", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -21, "some", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -22, "find", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -23, "findIndex", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -24, "reduce", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -25, "reduceRight", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeArray.ARRAY_TAG, -26, "isArray", 1);
        super.fillConstructorProperties(idFunctionObject);
    }
    
    @Override
    protected int findInstanceIdInfo(final String s) {
        if (s.equals("length")) {
            return IdScriptableObject.instanceIdInfo(this.lengthAttr, 1);
        }
        return super.findInstanceIdInfo(s);
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        final boolean b = false;
        final String s2 = null;
        final int length = s.length();
        int n = 0;
        String s3 = null;
        Label_0665: {
            if (length != 11) {
                if (length != 14) {
                    switch (length) {
                        default: {
                            n = (b ? 1 : 0);
                            s3 = s2;
                            break;
                        }
                        case 9: {
                            s3 = "findIndex";
                            n = 23;
                            break;
                        }
                        case 8: {
                            final char char1 = s.charAt(3);
                            if (char1 == 'o') {
                                s3 = "toSource";
                                n = 4;
                                break;
                            }
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (char1 == 't') {
                                s3 = "toString";
                                n = 2;
                                break;
                            }
                            break;
                        }
                        case 7: {
                            final char char2 = s.charAt(0);
                            if (char2 == 'f') {
                                s3 = "forEach";
                                n = 19;
                                break;
                            }
                            if (char2 == 'i') {
                                s3 = "indexOf";
                                n = 15;
                                break;
                            }
                            if (char2 == 'r') {
                                s3 = "reverse";
                                n = 6;
                                break;
                            }
                            if (char2 != 'u') {
                                n = (b ? 1 : 0);
                                s3 = s2;
                                break;
                            }
                            s3 = "unshift";
                            n = 11;
                            break;
                        }
                        case 6: {
                            final char char3 = s.charAt(0);
                            if (char3 == 'c') {
                                s3 = "concat";
                                n = 13;
                                break;
                            }
                            if (char3 == 'f') {
                                s3 = "filter";
                                n = 18;
                                break;
                            }
                            switch (char3) {
                                default: {
                                    n = (b ? 1 : 0);
                                    s3 = s2;
                                    break Label_0665;
                                }
                                case 115: {
                                    s3 = "splice";
                                    n = 12;
                                    break Label_0665;
                                }
                                case 114: {
                                    s3 = "reduce";
                                    n = 24;
                                    break Label_0665;
                                }
                            }
                            break;
                        }
                        case 5: {
                            final char char4 = s.charAt(1);
                            if (char4 == 'h') {
                                s3 = "shift";
                                n = 10;
                                break;
                            }
                            if (char4 == 'l') {
                                s3 = "slice";
                                n = 14;
                                break;
                            }
                            n = (b ? 1 : 0);
                            s3 = s2;
                            if (char4 == 'v') {
                                s3 = "every";
                                n = 17;
                                break;
                            }
                            break;
                        }
                        case 4: {
                            switch (s.charAt(2)) {
                                default: {
                                    n = (b ? 1 : 0);
                                    s3 = s2;
                                    break Label_0665;
                                }
                                case 's': {
                                    s3 = "push";
                                    n = 8;
                                    break Label_0665;
                                }
                                case 'r': {
                                    s3 = "sort";
                                    n = 7;
                                    break Label_0665;
                                }
                                case 'n': {
                                    s3 = "find";
                                    n = 22;
                                    break Label_0665;
                                }
                                case 'm': {
                                    s3 = "some";
                                    n = 21;
                                    break Label_0665;
                                }
                                case 'i': {
                                    s3 = "join";
                                    n = 5;
                                    break Label_0665;
                                }
                            }
                            break;
                        }
                        case 3: {
                            final char char5 = s.charAt(0);
                            if (char5 == 'm') {
                                n = (b ? 1 : 0);
                                s3 = s2;
                                if (s.charAt(2) != 'p') {
                                    break;
                                }
                                n = (b ? 1 : 0);
                                s3 = s2;
                                if (s.charAt(1) == 'a') {
                                    return 20;
                                }
                                break;
                            }
                            else {
                                n = (b ? 1 : 0);
                                s3 = s2;
                                if (char5 != 'p') {
                                    break;
                                }
                                n = (b ? 1 : 0);
                                s3 = s2;
                                if (s.charAt(2) != 'p') {
                                    break;
                                }
                                n = (b ? 1 : 0);
                                s3 = s2;
                                if (s.charAt(1) == 'o') {
                                    return 9;
                                }
                                break;
                            }
                            break;
                        }
                    }
                }
                else {
                    s3 = "toLocaleString";
                    n = 3;
                }
            }
            else {
                final char char6 = s.charAt(0);
                if (char6 == 'c') {
                    s3 = "constructor";
                    n = 1;
                }
                else if (char6 == 'l') {
                    s3 = "lastIndexOf";
                    n = 16;
                }
                else {
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char6 == 'r') {
                        s3 = "reduceRight";
                        n = 25;
                    }
                }
            }
        }
        int n2 = n;
        if (s3 != null) {
            n2 = n;
            if (s3 != s) {
                n2 = n;
                if (!s3.equals(s)) {
                    n2 = 0;
                }
            }
        }
        return n2;
    }
    
    @Override
    public void forEach(final Consumer<?> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public Object get(final int n) {
        return this.get((long)n);
    }
    
    @Override
    public Object get(final int n, final Scriptable scriptable) {
        if (!this.denseOnly && this.isGetterOrSetter(null, n, false)) {
            return super.get(n, scriptable);
        }
        if (this.dense != null && n >= 0 && n < this.dense.length) {
            return this.dense[n];
        }
        return super.get(n, scriptable);
    }
    
    public Object get(final long n) {
        if (n < 0L || n >= this.length) {
            throw new IndexOutOfBoundsException();
        }
        final Object rawElem = getRawElem(this, n);
        if (rawElem == Scriptable.NOT_FOUND || rawElem == Undefined.instance) {
            return null;
        }
        if (rawElem instanceof Wrapper) {
            return ((Wrapper)rawElem).unwrap();
        }
        return rawElem;
    }
    
    @Override
    public Object[] getAllIds() {
        final LinkedHashSet set = new LinkedHashSet((Collection<? extends E>)Arrays.asList(this.getIds()));
        set.addAll(Arrays.asList(super.getAllIds()));
        return set.toArray();
    }
    
    @Override
    public int getAttributes(final int n) {
        if (this.dense != null && n >= 0 && n < this.dense.length && this.dense[n] != NativeArray.NOT_FOUND) {
            return 0;
        }
        return super.getAttributes(n);
    }
    
    @Override
    public String getClassName() {
        return "Array";
    }
    
    @Override
    public Object getDefaultValue(final Class<?> clazz) {
        if (clazz == ScriptRuntime.NumberClass && Context.getContext().getLanguageVersion() == 120) {
            return this.length;
        }
        return super.getDefaultValue(clazz);
    }
    
    @Override
    public Object[] getIds() {
        final Object[] ids = super.getIds();
        if (this.dense == null) {
            return ids;
        }
        final int length = this.dense.length;
        final long length2 = this.length;
        int n = length;
        if (length > length2) {
            n = (int)length2;
        }
        if (n == 0) {
            return ids;
        }
        final int length3 = ids.length;
        final Object[] array = new Object[n + length3];
        int n2 = 0;
        int n3;
        for (int i = 0; i != n; ++i, n2 = n3) {
            n3 = n2;
            if (this.dense[i] != NativeArray.NOT_FOUND) {
                array[n2] = i;
                n3 = n2 + 1;
            }
        }
        Object[] array2 = array;
        if (n2 != n) {
            array2 = new Object[n2 + length3];
            System.arraycopy(array, 0, array2, 0, n2);
        }
        System.arraycopy(ids, 0, array2, n2, length3);
        return array2;
    }
    
    public Integer[] getIndexIds() {
        final Object[] ids = this.getIds();
        final ArrayList list = new ArrayList<Integer>(ids.length);
        for (int length = ids.length, i = 0; i < length; ++i) {
            final Object o = ids[i];
            final int int32 = ScriptRuntime.toInt32(o);
            if (int32 >= 0 && ScriptRuntime.toString(int32).equals(ScriptRuntime.toString(o))) {
                list.add(Integer.valueOf(int32));
            }
        }
        return list.toArray(new Integer[list.size()]);
    }
    
    @Override
    protected String getInstanceIdName(final int n) {
        if (n == 1) {
            return "length";
        }
        return super.getInstanceIdName(n);
    }
    
    @Override
    protected Object getInstanceIdValue(final int n) {
        if (n == 1) {
            return ScriptRuntime.wrapNumber((double)this.length);
        }
        return super.getInstanceIdValue(n);
    }
    
    public long getLength() {
        return this.length;
    }
    
    @Override
    protected int getMaxInstanceId() {
        return 1;
    }
    
    @Override
    protected ScriptableObject getOwnPropertyDescriptor(final Context context, final Object o) {
        if (this.dense != null) {
            final int denseIndex = toDenseIndex(o);
            if (denseIndex >= 0 && denseIndex < this.dense.length && this.dense[denseIndex] != NativeArray.NOT_FOUND) {
                return this.defaultIndexPropertyDescriptor(this.dense[denseIndex]);
            }
        }
        return super.getOwnPropertyDescriptor(context, o);
    }
    
    @Override
    public boolean has(final int n, final Scriptable scriptable) {
        final boolean denseOnly = this.denseOnly;
        boolean b = false;
        if (!denseOnly && this.isGetterOrSetter(null, n, false)) {
            return super.has(n, scriptable);
        }
        if (this.dense != null && n >= 0 && n < this.dense.length) {
            if (this.dense[n] != NativeArray.NOT_FOUND) {
                b = true;
            }
            return b;
        }
        return super.has(n, scriptable);
    }
    
    @Override
    public int indexOf(final Object o) {
        final long length = this.length;
        if (length > 2147483647L) {
            throw new IllegalStateException();
        }
        final int n = (int)length;
        final int n2 = 0;
        int i = 0;
        if (o == null) {
            while (i < n) {
                if (this.get(i) == null) {
                    return i;
                }
                ++i;
            }
        }
        else {
            for (int j = n2; j < n; ++j) {
                if (o.equals(this.get(j))) {
                    return j;
                }
            }
        }
        return -1;
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        int n2 = 0;
        String s = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case 25: {
                n2 = 1;
                s = "reduceRight";
                break;
            }
            case 24: {
                n2 = 1;
                s = "reduce";
                break;
            }
            case 23: {
                n2 = 1;
                s = "findIndex";
                break;
            }
            case 22: {
                n2 = 1;
                s = "find";
                break;
            }
            case 21: {
                n2 = 1;
                s = "some";
                break;
            }
            case 20: {
                n2 = 1;
                s = "map";
                break;
            }
            case 19: {
                n2 = 1;
                s = "forEach";
                break;
            }
            case 18: {
                n2 = 1;
                s = "filter";
                break;
            }
            case 17: {
                n2 = 1;
                s = "every";
                break;
            }
            case 16: {
                n2 = 1;
                s = "lastIndexOf";
                break;
            }
            case 15: {
                n2 = 1;
                s = "indexOf";
                break;
            }
            case 14: {
                n2 = 2;
                s = "slice";
                break;
            }
            case 13: {
                n2 = 1;
                s = "concat";
                break;
            }
            case 12: {
                n2 = 2;
                s = "splice";
                break;
            }
            case 11: {
                n2 = 1;
                s = "unshift";
                break;
            }
            case 10: {
                n2 = 0;
                s = "shift";
                break;
            }
            case 9: {
                n2 = 0;
                s = "pop";
                break;
            }
            case 8: {
                n2 = 1;
                s = "push";
                break;
            }
            case 7: {
                n2 = 1;
                s = "sort";
                break;
            }
            case 6: {
                n2 = 0;
                s = "reverse";
                break;
            }
            case 5: {
                n2 = 1;
                s = "join";
                break;
            }
            case 4: {
                n2 = 0;
                s = "toSource";
                break;
            }
            case 3: {
                n2 = 0;
                s = "toLocaleString";
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
        this.initPrototypeMethod(NativeArray.ARRAY_TAG, n, s, n2);
    }
    
    @Override
    public boolean isEmpty() {
        return this.length == 0L;
    }
    
    @Override
    public Iterator iterator() {
        return this.listIterator(0);
    }
    
    @Deprecated
    public long jsGet_length() {
        return this.getLength();
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        final long length = this.length;
        if (length > 2147483647L) {
            throw new IllegalStateException();
        }
        final int n = (int)length;
        if (o == null) {
            for (int i = n - 1; i >= 0; --i) {
                if (this.get(i) == null) {
                    return i;
                }
            }
        }
        else {
            for (int j = n - 1; j >= 0; --j) {
                if (o.equals(this.get(j))) {
                    return j;
                }
            }
        }
        return -1;
    }
    
    @Override
    public ListIterator listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public ListIterator listIterator(final int n) {
        final long length = this.length;
        if (length > 2147483647L) {
            throw new IllegalStateException();
        }
        final int n2 = (int)length;
        if (n >= 0 && n <= n2) {
            return new ListIterator() {
                int cursor = n;
                
                @Override
                public void add(final Object o) {
                    throw new UnsupportedOperationException();
                }
                
                @Override
                public void forEachRemaining(final Consumer<?> p0) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Could not show original bytecode, likely due to the same error.
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException
                    // 
                    throw new IllegalStateException("An error occurred while decompiling this method.");
                }
                
                @Override
                public boolean hasNext() {
                    return this.cursor < n2;
                }
                
                @Override
                public boolean hasPrevious() {
                    return this.cursor > 0;
                }
                
                @Override
                public Object next() {
                    if (this.cursor == n2) {
                        throw new NoSuchElementException();
                    }
                    return NativeArray.this.get(this.cursor++);
                }
                
                @Override
                public int nextIndex() {
                    return this.cursor;
                }
                
                @Override
                public Object previous() {
                    if (this.cursor == 0) {
                        throw new NoSuchElementException();
                    }
                    final NativeArray this$0 = NativeArray.this;
                    final int cursor = this.cursor - 1;
                    this.cursor = cursor;
                    return this$0.get(cursor);
                }
                
                @Override
                public int previousIndex() {
                    return this.cursor - 1;
                }
                
                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
                
                @Override
                public void set(final Object o) {
                    throw new UnsupportedOperationException();
                }
            };
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Index: ");
        sb.append(n);
        throw new IndexOutOfBoundsException(sb.toString());
    }
    
    @Override
    public Stream<Object> parallelStream() {
        return (Stream<Object>)Collection-CC.$default$parallelStream((Collection)this);
    }
    
    @Override
    public void put(final int n, final Scriptable scriptable, final Object o) {
        if (scriptable == this && !this.isSealed() && this.dense != null && n >= 0 && (this.denseOnly || !this.isGetterOrSetter(null, n, true))) {
            if (!this.isExtensible() && this.length <= n) {
                return;
            }
            if (n < this.dense.length) {
                this.dense[n] = o;
                if (this.length <= n) {
                    this.length = n + 1L;
                }
                return;
            }
            if (this.denseOnly && n < this.dense.length * 1.5 && this.ensureCapacity(n + 1)) {
                this.dense[n] = o;
                this.length = n + 1L;
                return;
            }
            this.denseOnly = false;
        }
        super.put(n, scriptable, o);
        if (scriptable == this && (0x1 & this.lengthAttr) == 0x0 && this.length <= n) {
            this.length = n + 1L;
        }
    }
    
    @Override
    public void put(final String s, final Scriptable scriptable, final Object o) {
        super.put(s, scriptable, o);
        if (scriptable == this) {
            final long arrayIndex = toArrayIndex(s);
            if (arrayIndex >= this.length) {
                this.length = arrayIndex + 1L;
                this.denseOnly = false;
            }
        }
    }
    
    @Override
    public Object remove(final int n) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean removeAll(final Collection collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean removeIf(final Predicate<?> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public void replaceAll(final UnaryOperator<Object> unaryOperator) {
        List-CC.$default$replaceAll((List)this, (UnaryOperator)unaryOperator);
    }
    
    @Override
    public boolean retainAll(final Collection collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object set(final int n, final Object o) {
        throw new UnsupportedOperationException();
    }
    
    void setDenseOnly(final boolean denseOnly) {
        if (denseOnly && !this.denseOnly) {
            throw new IllegalArgumentException();
        }
        this.denseOnly = denseOnly;
    }
    
    @Override
    protected void setInstanceIdAttributes(final int n, final int lengthAttr) {
        if (n == 1) {
            this.lengthAttr = lengthAttr;
        }
    }
    
    @Override
    protected void setInstanceIdValue(final int n, final Object length) {
        if (n == 1) {
            this.setLength(length);
            return;
        }
        super.setInstanceIdValue(n, length);
    }
    
    @Override
    public int size() {
        final long length = this.length;
        if (length > 2147483647L) {
            throw new IllegalStateException();
        }
        return (int)length;
    }
    
    @Override
    public void sort(final Comparator<?> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public Spliterator<Object> spliterator() {
        return (Spliterator<Object>)List-CC.$default$spliterator((List)this);
    }
    
    @Override
    public Stream<Object> stream() {
        return (Stream<Object>)Collection-CC.$default$stream((Collection)this);
    }
    
    @Override
    public List subList(final int n, final int n2) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object[] toArray() {
        return this.toArray(ScriptRuntime.emptyArgs);
    }
    
    public <T> T[] toArray(final IntFunction<T[]> intFunction) {
        return (T[])Collection-CC.$default$toArray((Collection)this, (IntFunction)intFunction);
    }
    
    @Override
    public Object[] toArray(Object[] array) {
        final long length = this.length;
        if (length > 2147483647L) {
            throw new IllegalStateException();
        }
        final int n = (int)length;
        if (array.length < n) {
            array = (Object[])Array.newInstance(array.getClass().getComponentType(), n);
        }
        for (int i = 0; i < n; ++i) {
            array[i] = this.get(i);
        }
        return array;
    }
}
