package org.mozilla.javascript;

import java.util.function.*;
import java.util.*;

public class NativeObject extends IdScriptableObject implements Map
{
    private static final int ConstructorId_create = -9;
    private static final int ConstructorId_defineProperties = -8;
    private static final int ConstructorId_defineProperty = -5;
    private static final int ConstructorId_freeze = -13;
    private static final int ConstructorId_getOwnPropertyDescriptor = -4;
    private static final int ConstructorId_getOwnPropertyNames = -3;
    private static final int ConstructorId_getPrototypeOf = -1;
    private static final int ConstructorId_isExtensible = -6;
    private static final int ConstructorId_isFrozen = -11;
    private static final int ConstructorId_isSealed = -10;
    private static final int ConstructorId_keys = -2;
    private static final int ConstructorId_preventExtensions = -7;
    private static final int ConstructorId_seal = -12;
    private static final int Id___defineGetter__ = 9;
    private static final int Id___defineSetter__ = 10;
    private static final int Id___lookupGetter__ = 11;
    private static final int Id___lookupSetter__ = 12;
    private static final int Id_constructor = 1;
    private static final int Id_hasOwnProperty = 5;
    private static final int Id_isPrototypeOf = 7;
    private static final int Id_propertyIsEnumerable = 6;
    private static final int Id_toLocaleString = 3;
    private static final int Id_toSource = 8;
    private static final int Id_toString = 2;
    private static final int Id_valueOf = 4;
    private static final int MAX_PROTOTYPE_ID = 12;
    private static final Object OBJECT_TAG;
    static final long serialVersionUID = -6345305608474346996L;
    
    static {
        OBJECT_TAG = "Object";
    }
    
    static void init(final Scriptable scriptable, final boolean b) {
        new NativeObject().exportAsJSClass(12, scriptable, b);
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object compute(final Object o, final BiFunction biFunction) {
        return Map-CC.$default$compute((Map)this, o, biFunction);
    }
    
    @Override
    public Object computeIfAbsent(final Object o, final Function function) {
        return Map-CC.$default$computeIfAbsent((Map)this, o, function);
    }
    
    @Override
    public Object computeIfPresent(final Object o, final BiFunction biFunction) {
        return Map-CC.$default$computeIfPresent((Map)this, o, biFunction);
    }
    
    @Override
    public boolean containsKey(final Object o) {
        if (o instanceof String) {
            return this.has((String)o, this);
        }
        return o instanceof Number && this.has(((Number)o).intValue(), this);
    }
    
    @Override
    public boolean containsValue(final Object o) {
        for (final Object next : this.values()) {
            if (o != next && (o == null || !o.equals(next))) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public Set<Entry<Object, Object>> entrySet() {
        return new EntrySet();
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(NativeObject.OBJECT_TAG)) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        final boolean b = true;
        final boolean b2 = true;
        final boolean b3 = true;
        boolean b4 = true;
        final int n = 0;
        int i = 0;
        final int n2 = 0;
        final int n3 = 0;
        switch (methodId) {
            default: {
                throw new IllegalArgumentException(String.valueOf(methodId));
            }
            case 11:
            case 12: {
                if (array.length < 1 || !(scriptable2 instanceof ScriptableObject)) {
                    return Undefined.instance;
                }
                ScriptableObject scriptableObject = (ScriptableObject)scriptable2;
                final String stringIdOrIndex = ScriptRuntime.toStringIdOrIndex(context, array[0]);
                int lastIndexResult;
                if (stringIdOrIndex != null) {
                    lastIndexResult = 0;
                }
                else {
                    lastIndexResult = ScriptRuntime.lastIndexResult(context);
                }
                if (methodId != 12) {
                    b4 = false;
                }
                Object getterOrSetter;
                while (true) {
                    getterOrSetter = scriptableObject.getGetterOrSetter(stringIdOrIndex, lastIndexResult, b4);
                    if (getterOrSetter != null) {
                        break;
                    }
                    final Scriptable prototype = scriptableObject.getPrototype();
                    if (prototype == null) {
                        break;
                    }
                    if (!(prototype instanceof ScriptableObject)) {
                        break;
                    }
                    scriptableObject = (ScriptableObject)prototype;
                }
                if (getterOrSetter != null) {
                    return getterOrSetter;
                }
                return Undefined.instance;
            }
            case 9:
            case 10: {
                if (array.length < 2 || !(array[1] instanceof Callable)) {
                    Object instance;
                    if (array.length >= 2) {
                        instance = array[1];
                    }
                    else {
                        instance = Undefined.instance;
                    }
                    throw ScriptRuntime.notFunctionError(instance);
                }
                if (!(scriptable2 instanceof ScriptableObject)) {
                    throw Context.reportRuntimeError2("msg.extend.scriptable", scriptable2.getClass().getName(), String.valueOf(array[0]));
                }
                final ScriptableObject scriptableObject2 = (ScriptableObject)scriptable2;
                final String stringIdOrIndex2 = ScriptRuntime.toStringIdOrIndex(context, array[0]);
                int lastIndexResult2;
                if (stringIdOrIndex2 != null) {
                    lastIndexResult2 = 0;
                }
                else {
                    lastIndexResult2 = ScriptRuntime.lastIndexResult(context);
                }
                scriptableObject2.setGetterOrSetter(stringIdOrIndex2, lastIndexResult2, (Callable)array[1], methodId == 10 && b);
                if (scriptableObject2 instanceof NativeArray) {
                    ((NativeArray)scriptableObject2).setDenseOnly(false);
                }
                return Undefined.instance;
            }
            case 8: {
                return ScriptRuntime.defaultObjectToSource(context, scriptable, scriptable2, array);
            }
            case 7: {
                boolean b6;
                final boolean b5 = b6 = false;
                if (array.length != 0) {
                    b6 = b5;
                    if (array[0] instanceof Scriptable) {
                        Scriptable scriptable3 = (Scriptable)array[0];
                        Scriptable prototype2;
                        do {
                            prototype2 = scriptable3.getPrototype();
                            if (prototype2 == scriptable2) {
                                b6 = true;
                                return ScriptRuntime.wrapBoolean(b6);
                            }
                        } while ((scriptable3 = prototype2) != null);
                        b6 = b5;
                    }
                }
                return ScriptRuntime.wrapBoolean(b6);
            }
            case 6: {
                Object instance2;
                if (array.length < 1) {
                    instance2 = Undefined.instance;
                }
                else {
                    instance2 = array[0];
                }
                final String stringIdOrIndex3 = ScriptRuntime.toStringIdOrIndex(context, instance2);
                boolean b8;
                if (stringIdOrIndex3 == null) {
                    final int lastIndexResult3 = ScriptRuntime.lastIndexResult(context);
                    final boolean b7 = b8 = scriptable2.has(lastIndexResult3, scriptable2);
                    if (b7) {
                        b8 = b7;
                        if (scriptable2 instanceof ScriptableObject) {
                            b8 = ((((ScriptableObject)scriptable2).getAttributes(lastIndexResult3) & 0x2) == 0x0 && b2);
                        }
                    }
                }
                else {
                    final boolean b9 = b8 = scriptable2.has(stringIdOrIndex3, scriptable2);
                    if (b9) {
                        b8 = b9;
                        if (scriptable2 instanceof ScriptableObject) {
                            b8 = ((((ScriptableObject)scriptable2).getAttributes(stringIdOrIndex3) & 0x2) == 0x0 && b3);
                        }
                    }
                }
                return ScriptRuntime.wrapBoolean(b8);
            }
            case 5: {
                Object instance3;
                if (array.length < 1) {
                    instance3 = Undefined.instance;
                }
                else {
                    instance3 = array[0];
                }
                final String stringIdOrIndex4 = ScriptRuntime.toStringIdOrIndex(context, instance3);
                boolean b10;
                if (stringIdOrIndex4 == null) {
                    b10 = scriptable2.has(ScriptRuntime.lastIndexResult(context), scriptable2);
                }
                else {
                    b10 = scriptable2.has(stringIdOrIndex4, scriptable2);
                }
                return ScriptRuntime.wrapBoolean(b10);
            }
            case 4: {
                return scriptable2;
            }
            case 3: {
                final Object property = ScriptableObject.getProperty(scriptable2, "toString");
                if (!(property instanceof Callable)) {
                    throw ScriptRuntime.notFunctionError(property);
                }
                return ((Callable)property).call(context, scriptable, scriptable2, ScriptRuntime.emptyArgs);
            }
            case 2: {
                if (context.hasFeature(4)) {
                    final String defaultObjectToSource = ScriptRuntime.defaultObjectToSource(context, scriptable, scriptable2, array);
                    final int length = defaultObjectToSource.length();
                    String substring = defaultObjectToSource;
                    if (length != 0) {
                        substring = defaultObjectToSource;
                        if (defaultObjectToSource.charAt(0) == '(') {
                            substring = defaultObjectToSource;
                            if (defaultObjectToSource.charAt(length - 1) == ')') {
                                substring = defaultObjectToSource.substring(1, length - 1);
                            }
                        }
                    }
                    return substring;
                }
                return ScriptRuntime.defaultObjectToString(scriptable2);
            }
            case 1: {
                if (scriptable2 != null) {
                    return idFunctionObject.construct(context, scriptable, array);
                }
                if (array.length != 0 && array[0] != null && array[0] != Undefined.instance) {
                    return ScriptRuntime.toObject(context, scriptable, array[0]);
                }
                return new NativeObject();
            }
            case -1: {
                Object instance4;
                if (array.length < 1) {
                    instance4 = Undefined.instance;
                }
                else {
                    instance4 = array[0];
                }
                return ScriptableObject.ensureScriptable(instance4).getPrototype();
            }
            case -2: {
                Object instance5;
                if (array.length < 1) {
                    instance5 = Undefined.instance;
                }
                else {
                    instance5 = array[0];
                }
                final Object[] ids = ScriptableObject.ensureScriptable(instance5).getIds();
                for (int j = n3; j < ids.length; ++j) {
                    ids[j] = ScriptRuntime.toString(ids[j]);
                }
                return context.newArray(scriptable, ids);
            }
            case -3: {
                Object instance6;
                if (array.length < 1) {
                    instance6 = Undefined.instance;
                }
                else {
                    instance6 = array[0];
                }
                final Object[] allIds = ScriptableObject.ensureScriptableObject(instance6).getAllIds();
                for (int k = n; k < allIds.length; ++k) {
                    allIds[k] = ScriptRuntime.toString(allIds[k]);
                }
                return context.newArray(scriptable, allIds);
            }
            case -4: {
                Object instance7;
                if (array.length < 1) {
                    instance7 = Undefined.instance;
                }
                else {
                    instance7 = array[0];
                }
                final ScriptableObject ensureScriptableObject = ScriptableObject.ensureScriptableObject(instance7);
                Object instance8;
                if (array.length < 2) {
                    instance8 = Undefined.instance;
                }
                else {
                    instance8 = array[1];
                }
                final ScriptableObject ownPropertyDescriptor = ensureScriptableObject.getOwnPropertyDescriptor(context, ScriptRuntime.toString(instance8));
                if (ownPropertyDescriptor == null) {
                    return Undefined.instance;
                }
                return ownPropertyDescriptor;
            }
            case -5: {
                Object instance9;
                if (array.length < 1) {
                    instance9 = Undefined.instance;
                }
                else {
                    instance9 = array[0];
                }
                final ScriptableObject ensureScriptableObject2 = ScriptableObject.ensureScriptableObject(instance9);
                Object instance10;
                if (array.length < 2) {
                    instance10 = Undefined.instance;
                }
                else {
                    instance10 = array[1];
                }
                Object instance11;
                if (array.length < 3) {
                    instance11 = Undefined.instance;
                }
                else {
                    instance11 = array[2];
                }
                ensureScriptableObject2.defineOwnProperty(context, instance10, ScriptableObject.ensureScriptableObject(instance11));
                return ensureScriptableObject2;
            }
            case -6: {
                Object instance12;
                if (array.length < 1) {
                    instance12 = Undefined.instance;
                }
                else {
                    instance12 = array[0];
                }
                return ScriptableObject.ensureScriptableObject(instance12).isExtensible();
            }
            case -7: {
                Object instance13;
                if (array.length < 1) {
                    instance13 = Undefined.instance;
                }
                else {
                    instance13 = array[0];
                }
                final ScriptableObject ensureScriptableObject3 = ScriptableObject.ensureScriptableObject(instance13);
                ensureScriptableObject3.preventExtensions();
                return ensureScriptableObject3;
            }
            case -8: {
                Object instance14;
                if (array.length < 1) {
                    instance14 = Undefined.instance;
                }
                else {
                    instance14 = array[0];
                }
                final ScriptableObject ensureScriptableObject4 = ScriptableObject.ensureScriptableObject(instance14);
                Object instance15;
                if (array.length < 2) {
                    instance15 = Undefined.instance;
                }
                else {
                    instance15 = array[1];
                }
                ensureScriptableObject4.defineOwnProperties(context, ScriptableObject.ensureScriptableObject(Context.toObject(instance15, this.getParentScope())));
                return ensureScriptableObject4;
            }
            case -9: {
                Object instance16;
                if (array.length < 1) {
                    instance16 = Undefined.instance;
                }
                else {
                    instance16 = array[0];
                }
                Scriptable ensureScriptable;
                if (instance16 == null) {
                    ensureScriptable = null;
                }
                else {
                    ensureScriptable = ScriptableObject.ensureScriptable(instance16);
                }
                final NativeObject nativeObject = new NativeObject();
                nativeObject.setParentScope(this.getParentScope());
                nativeObject.setPrototype(ensureScriptable);
                if (array.length > 1 && array[1] != Undefined.instance) {
                    nativeObject.defineOwnProperties(context, ScriptableObject.ensureScriptableObject(Context.toObject(array[1], this.getParentScope())));
                }
                return nativeObject;
            }
            case -10: {
                Object instance17;
                if (array.length < 1) {
                    instance17 = Undefined.instance;
                }
                else {
                    instance17 = array[0];
                }
                final ScriptableObject ensureScriptableObject5 = ScriptableObject.ensureScriptableObject(instance17);
                if (ensureScriptableObject5.isExtensible()) {
                    return Boolean.FALSE;
                }
                for (Object[] allIds2 = ensureScriptableObject5.getAllIds(); i < allIds2.length; ++i) {
                    if (Boolean.TRUE.equals(ensureScriptableObject5.getOwnPropertyDescriptor(context, allIds2[i]).get("configurable"))) {
                        return Boolean.FALSE;
                    }
                }
                return Boolean.TRUE;
            }
            case -11: {
                Object instance18;
                if (array.length < 1) {
                    instance18 = Undefined.instance;
                }
                else {
                    instance18 = array[0];
                }
                final ScriptableObject ensureScriptableObject6 = ScriptableObject.ensureScriptableObject(instance18);
                if (ensureScriptableObject6.isExtensible()) {
                    return Boolean.FALSE;
                }
                final Object[] allIds3 = ensureScriptableObject6.getAllIds();
                for (int length2 = allIds3.length, l = n2; l < length2; ++l) {
                    final ScriptableObject ownPropertyDescriptor2 = ensureScriptableObject6.getOwnPropertyDescriptor(context, allIds3[l]);
                    if (Boolean.TRUE.equals(ownPropertyDescriptor2.get("configurable"))) {
                        return Boolean.FALSE;
                    }
                    if (this.isDataDescriptor(ownPropertyDescriptor2) && Boolean.TRUE.equals(ownPropertyDescriptor2.get("writable"))) {
                        return Boolean.FALSE;
                    }
                }
                return Boolean.TRUE;
            }
            case -12: {
                Object instance19;
                if (array.length < 1) {
                    instance19 = Undefined.instance;
                }
                else {
                    instance19 = array[0];
                }
                final ScriptableObject ensureScriptableObject7 = ScriptableObject.ensureScriptableObject(instance19);
                final Object[] allIds4 = ensureScriptableObject7.getAllIds();
                for (int length3 = allIds4.length, n4 = 0; n4 < length3; ++n4) {
                    final Object o = allIds4[n4];
                    final ScriptableObject ownPropertyDescriptor3 = ensureScriptableObject7.getOwnPropertyDescriptor(context, o);
                    if (Boolean.TRUE.equals(ownPropertyDescriptor3.get("configurable"))) {
                        ownPropertyDescriptor3.put("configurable", ownPropertyDescriptor3, Boolean.FALSE);
                        ensureScriptableObject7.defineOwnProperty(context, o, ownPropertyDescriptor3, false);
                    }
                }
                ensureScriptableObject7.preventExtensions();
                return ensureScriptableObject7;
            }
            case -13: {
                Object instance20;
                if (array.length < 1) {
                    instance20 = Undefined.instance;
                }
                else {
                    instance20 = array[0];
                }
                final ScriptableObject ensureScriptableObject8 = ScriptableObject.ensureScriptableObject(instance20);
                final Object[] allIds5 = ensureScriptableObject8.getAllIds();
                for (int length4 = allIds5.length, n5 = 0; n5 < length4; ++n5) {
                    final Object o2 = allIds5[n5];
                    final ScriptableObject ownPropertyDescriptor4 = ensureScriptableObject8.getOwnPropertyDescriptor(context, o2);
                    if (this.isDataDescriptor(ownPropertyDescriptor4) && Boolean.TRUE.equals(ownPropertyDescriptor4.get("writable"))) {
                        ownPropertyDescriptor4.put("writable", ownPropertyDescriptor4, Boolean.FALSE);
                    }
                    if (Boolean.TRUE.equals(ownPropertyDescriptor4.get("configurable"))) {
                        ownPropertyDescriptor4.put("configurable", ownPropertyDescriptor4, Boolean.FALSE);
                    }
                    ensureScriptableObject8.defineOwnProperty(context, o2, ownPropertyDescriptor4, false);
                }
                ensureScriptableObject8.preventExtensions();
                return ensureScriptableObject8;
            }
        }
    }
    
    @Override
    protected void fillConstructorProperties(final IdFunctionObject idFunctionObject) {
        this.addIdFunctionProperty(idFunctionObject, NativeObject.OBJECT_TAG, -1, "getPrototypeOf", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeObject.OBJECT_TAG, -2, "keys", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeObject.OBJECT_TAG, -3, "getOwnPropertyNames", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeObject.OBJECT_TAG, -4, "getOwnPropertyDescriptor", 2);
        this.addIdFunctionProperty(idFunctionObject, NativeObject.OBJECT_TAG, -5, "defineProperty", 3);
        this.addIdFunctionProperty(idFunctionObject, NativeObject.OBJECT_TAG, -6, "isExtensible", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeObject.OBJECT_TAG, -7, "preventExtensions", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeObject.OBJECT_TAG, -8, "defineProperties", 2);
        this.addIdFunctionProperty(idFunctionObject, NativeObject.OBJECT_TAG, -9, "create", 2);
        this.addIdFunctionProperty(idFunctionObject, NativeObject.OBJECT_TAG, -10, "isSealed", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeObject.OBJECT_TAG, -11, "isFrozen", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeObject.OBJECT_TAG, -12, "seal", 1);
        this.addIdFunctionProperty(idFunctionObject, NativeObject.OBJECT_TAG, -13, "freeze", 1);
        super.fillConstructorProperties(idFunctionObject);
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        final boolean b = false;
        final String s2 = null;
        int n = 0;
        String s3 = null;
        switch (s.length()) {
            default: {
                n = (b ? 1 : 0);
                s3 = s2;
                break;
            }
            case 20: {
                s3 = "propertyIsEnumerable";
                n = 6;
                break;
            }
            case 16: {
                final char char1 = s.charAt(2);
                if (char1 == 'd') {
                    final char char2 = s.charAt(8);
                    if (char2 == 'G') {
                        s3 = "__defineGetter__";
                        n = 9;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char2 == 'S') {
                        s3 = "__defineSetter__";
                        n = 10;
                        break;
                    }
                    break;
                }
                else {
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char1 != 'l') {
                        break;
                    }
                    final char char3 = s.charAt(8);
                    if (char3 == 'G') {
                        s3 = "__lookupGetter__";
                        n = 11;
                        break;
                    }
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (char3 == 'S') {
                        s3 = "__lookupSetter__";
                        n = 12;
                        break;
                    }
                    break;
                }
                break;
            }
            case 14: {
                final char char4 = s.charAt(0);
                if (char4 == 'h') {
                    s3 = "hasOwnProperty";
                    n = 5;
                    break;
                }
                n = (b ? 1 : 0);
                s3 = s2;
                if (char4 == 't') {
                    s3 = "toLocaleString";
                    n = 3;
                    break;
                }
                break;
            }
            case 13: {
                s3 = "isPrototypeOf";
                n = 7;
                break;
            }
            case 11: {
                s3 = "constructor";
                n = 1;
                break;
            }
            case 8: {
                final char char5 = s.charAt(3);
                if (char5 == 'o') {
                    s3 = "toSource";
                    n = 8;
                    break;
                }
                n = (b ? 1 : 0);
                s3 = s2;
                if (char5 == 't') {
                    s3 = "toString";
                    n = 2;
                    break;
                }
                break;
            }
            case 7: {
                s3 = "valueOf";
                n = 4;
                break;
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
    public void forEach(final BiConsumer<?, ?> p0) {
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
    public String getClassName() {
        return "Object";
    }
    
    @Override
    public Object getOrDefault(final Object o, final Object o2) {
        return Map-CC.$default$getOrDefault((Map)this, o, o2);
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        int n2 = 0;
        String s = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case 12: {
                n2 = 1;
                s = "__lookupSetter__";
                break;
            }
            case 11: {
                n2 = 1;
                s = "__lookupGetter__";
                break;
            }
            case 10: {
                n2 = 2;
                s = "__defineSetter__";
                break;
            }
            case 9: {
                n2 = 2;
                s = "__defineGetter__";
                break;
            }
            case 8: {
                n2 = 0;
                s = "toSource";
                break;
            }
            case 7: {
                n2 = 1;
                s = "isPrototypeOf";
                break;
            }
            case 6: {
                n2 = 1;
                s = "propertyIsEnumerable";
                break;
            }
            case 5: {
                n2 = 1;
                s = "hasOwnProperty";
                break;
            }
            case 4: {
                n2 = 0;
                s = "valueOf";
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
        this.initPrototypeMethod(NativeObject.OBJECT_TAG, n, s, n2);
    }
    
    @Override
    public Set<Object> keySet() {
        return new KeySet();
    }
    
    @Override
    public Object merge(final Object o, final Object o2, final BiFunction biFunction) {
        return Map-CC.$default$merge((Map)this, o, o2, biFunction);
    }
    
    @Override
    public Object put(final Object o, final Object o2) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void putAll(final Map map) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object putIfAbsent(final Object o, final Object o2) {
        return Map-CC.$default$putIfAbsent((Map)this, o, o2);
    }
    
    @Override
    public Object remove(final Object o) {
        final Object value = this.get(o);
        if (o instanceof String) {
            this.delete((String)o);
            return value;
        }
        if (o instanceof Number) {
            this.delete(((Number)o).intValue());
        }
        return value;
    }
    
    @Override
    public boolean remove(final Object o, final Object o2) {
        return Map-CC.$default$remove((Map)this, o, o2);
    }
    
    @Override
    public Object replace(final Object o, final Object o2) {
        return Map-CC.$default$replace((Map)this, o, o2);
    }
    
    @Override
    public boolean replace(final Object o, final Object o2, final Object o3) {
        return Map-CC.$default$replace((Map)this, o, o2, o3);
    }
    
    @Override
    public void replaceAll(final BiFunction<?, ?, ?> p0) {
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
    public String toString() {
        return ScriptRuntime.defaultObjectToString(this);
    }
    
    @Override
    public Collection<Object> values() {
        return new ValueCollection();
    }
    
    class EntrySet extends AbstractSet<Entry<Object, Object>>
    {
        @Override
        public Iterator<Entry<Object, Object>> iterator() {
            return new Iterator<Entry<Object, Object>>() {
                Object[] ids = NativeObject.this.getIds();
                int index = 0;
                Object key = null;
                
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
                    return this.index < this.ids.length;
                }
                
                @Override
                public Entry<Object, Object> next() {
                    final Object key = this.ids[this.index++];
                    this.key = key;
                    return new Entry<Object, Object>() {
                        final /* synthetic */ Object val$value = NativeObject.this.get(NativeObject$EntrySet$1.this.key);
                        
                        @Override
                        public boolean equals(final Object o) {
                            final boolean b = o instanceof Entry;
                            final boolean b2 = false;
                            if (!b) {
                                return false;
                            }
                            final Entry entry = (Entry)o;
                            if (key == null) {
                                final boolean b3 = b2;
                                if (entry.getKey() != null) {
                                    return b3;
                                }
                            }
                            else {
                                final boolean b3 = b2;
                                if (!key.equals(entry.getKey())) {
                                    return b3;
                                }
                            }
                            if (this.val$value == null) {
                                final boolean b3 = b2;
                                if (entry.getValue() != null) {
                                    return b3;
                                }
                            }
                            else {
                                final boolean b3 = b2;
                                if (!this.val$value.equals(entry.getValue())) {
                                    return b3;
                                }
                            }
                            return true;
                        }
                        
                        @Override
                        public Object getKey() {
                            return key;
                        }
                        
                        @Override
                        public Object getValue() {
                            return this.val$value;
                        }
                        
                        @Override
                        public int hashCode() {
                            final Object val$ekey = key;
                            int hashCode = 0;
                            int hashCode2;
                            if (val$ekey == null) {
                                hashCode2 = 0;
                            }
                            else {
                                hashCode2 = key.hashCode();
                            }
                            if (this.val$value != null) {
                                hashCode = this.val$value.hashCode();
                            }
                            return hashCode2 ^ hashCode;
                        }
                        
                        @Override
                        public Object setValue(final Object o) {
                            throw new UnsupportedOperationException();
                        }
                        
                        @Override
                        public String toString() {
                            final StringBuilder sb = new StringBuilder();
                            sb.append(key);
                            sb.append("=");
                            sb.append(this.val$value);
                            return sb.toString();
                        }
                    };
                }
                
                @Override
                public void remove() {
                    if (this.key == null) {
                        throw new IllegalStateException();
                    }
                    NativeObject.this.remove(this.key);
                    this.key = null;
                }
            };
        }
        
        @Override
        public int size() {
            return NativeObject.this.size();
        }
    }
    
    class KeySet extends AbstractSet<Object>
    {
        @Override
        public boolean contains(final Object o) {
            return NativeObject.this.containsKey(o);
        }
        
        @Override
        public Iterator<Object> iterator() {
            return new Iterator<Object>() {
                Object[] ids = NativeObject.this.getIds();
                int index = 0;
                Object key;
                
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
                    return this.index < this.ids.length;
                }
                
                @Override
                public Object next() {
                    try {
                        return this.key = this.ids[this.index++];
                    }
                    catch (ArrayIndexOutOfBoundsException ex) {
                        this.key = null;
                        throw new NoSuchElementException();
                    }
                }
                
                @Override
                public void remove() {
                    if (this.key == null) {
                        throw new IllegalStateException();
                    }
                    NativeObject.this.remove(this.key);
                    this.key = null;
                }
            };
        }
        
        @Override
        public int size() {
            return NativeObject.this.size();
        }
    }
    
    class ValueCollection extends AbstractCollection<Object>
    {
        @Override
        public Iterator<Object> iterator() {
            return new Iterator<Object>() {
                Object[] ids = NativeObject.this.getIds();
                int index = 0;
                Object key;
                
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
                    return this.index < this.ids.length;
                }
                
                @Override
                public Object next() {
                    final NativeObject this$0 = NativeObject.this;
                    final Object key = this.ids[this.index++];
                    this.key = key;
                    return this$0.get(key);
                }
                
                @Override
                public void remove() {
                    if (this.key == null) {
                        throw new IllegalStateException();
                    }
                    NativeObject.this.remove(this.key);
                    this.key = null;
                }
            };
        }
        
        @Override
        public int size() {
            return NativeObject.this.size();
        }
    }
}
