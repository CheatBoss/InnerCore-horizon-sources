package org.mozilla.javascript;

import org.mozilla.javascript.debug.*;
import java.lang.annotation.*;
import org.mozilla.javascript.annotations.*;
import java.lang.reflect.*;
import java.io.*;
import java.util.*;

public abstract class ScriptableObject implements Scriptable, Serializable, DebuggableObject, ConstProperties
{
    public static final int CONST = 13;
    public static final int DONTENUM = 2;
    public static final int EMPTY = 0;
    private static final Method GET_ARRAY_LENGTH;
    private static final int INITIAL_SLOT_SIZE = 4;
    public static final int PERMANENT = 4;
    public static final int READONLY = 1;
    private static final int SLOT_CONVERT_ACCESSOR_TO_DATA = 5;
    private static final int SLOT_MODIFY = 2;
    private static final int SLOT_MODIFY_CONST = 3;
    private static final int SLOT_MODIFY_GETTER_SETTER = 4;
    private static final int SLOT_QUERY = 1;
    public static final int UNINITIALIZED_CONST = 8;
    static final long serialVersionUID = 2829861078851942586L;
    private volatile Map<Object, Object> associatedValues;
    private int count;
    private transient ExternalArrayData externalData;
    private transient Slot firstAdded;
    private boolean isExtensible;
    private transient Slot lastAdded;
    private Scriptable parentScopeObject;
    private Scriptable prototypeObject;
    private transient Slot[] slots;
    
    static {
        try {
            GET_ARRAY_LENGTH = ScriptableObject.class.getMethod("getExternalArrayLength", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public ScriptableObject() {
        this.isExtensible = true;
    }
    
    public ScriptableObject(final Scriptable parentScopeObject, final Scriptable prototypeObject) {
        this.isExtensible = true;
        if (parentScopeObject == null) {
            throw new IllegalArgumentException();
        }
        this.parentScopeObject = parentScopeObject;
        this.prototypeObject = prototypeObject;
    }
    
    private static void addKnownAbsentSlot(final Slot[] array, final Slot next, final int n) {
        if (array[n] == null) {
            array[n] = next;
            return;
        }
        Slot slot = array[n];
        Slot next3;
        for (Slot next2 = slot.next; next2 != null; next2 = next3) {
            next3 = next2.next;
            slot = next2;
        }
        slot.next = next;
    }
    
    static <T extends Scriptable> BaseFunction buildClassCtor(final Scriptable scriptable, final Class<T> clazz, final boolean b, final boolean b2) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        final Method[] methodList = FunctionObject.getMethodList(clazz);
        for (int i = 0; i < methodList.length; ++i) {
            final Method method = methodList[i];
            if (method.getName().equals("init")) {
                final Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 3 && parameterTypes[0] == ScriptRuntime.ContextClass && parameterTypes[1] == ScriptRuntime.ScriptableClass && parameterTypes[2] == Boolean.TYPE && Modifier.isStatic(method.getModifiers())) {
                    final Context context = Context.getContext();
                    Boolean b3;
                    if (b) {
                        b3 = Boolean.TRUE;
                    }
                    else {
                        b3 = Boolean.FALSE;
                    }
                    method.invoke(null, context, scriptable, b3);
                    return null;
                }
                if (parameterTypes.length == 1 && parameterTypes[0] == ScriptRuntime.ScriptableClass && Modifier.isStatic(method.getModifiers())) {
                    method.invoke(null, scriptable);
                    return null;
                }
            }
        }
        final Constructor<?>[] constructors = clazz.getConstructors();
        final Constructor<Scriptable> constructor = null;
        int n = 0;
        Constructor<?> constructor2;
        while (true) {
            constructor2 = constructor;
            if (n >= constructors.length) {
                break;
            }
            if (constructors[n].getParameterTypes().length == 0) {
                constructor2 = constructors[n];
                break;
            }
            ++n;
        }
        if (constructor2 == null) {
            throw Context.reportRuntimeError1("msg.zero.arg.ctor", clazz.getName());
        }
        final Scriptable scriptable2 = (Scriptable)constructor2.newInstance(ScriptRuntime.emptyArgs);
        final String className = scriptable2.getClassName();
        final Object property = getProperty(getTopLevelScope(scriptable), className);
        if (property instanceof BaseFunction) {
            final Object prototypeProperty = ((BaseFunction)property).getPrototypeProperty();
            if (prototypeProperty != null && clazz.equals(prototypeProperty.getClass())) {
                return (BaseFunction)property;
            }
        }
        Scriptable classPrototype;
        final Scriptable scriptable3 = classPrototype = null;
        if (b2) {
            final Class<?> superclass = clazz.getSuperclass();
            classPrototype = scriptable3;
            if (ScriptRuntime.ScriptableClass.isAssignableFrom(superclass)) {
                classPrototype = scriptable3;
                if (!Modifier.isAbstract(superclass.getModifiers())) {
                    final String defineClass = defineClass(scriptable, extendsScriptable(superclass), b, b2);
                    classPrototype = scriptable3;
                    if (defineClass != null) {
                        classPrototype = getClassPrototype(scriptable, defineClass);
                    }
                }
            }
        }
        Scriptable objectPrototype;
        if ((objectPrototype = classPrototype) == null) {
            objectPrototype = getObjectPrototype(scriptable);
        }
        scriptable2.setPrototype(objectPrototype);
        Member member;
        if ((member = findAnnotatedMember(methodList, JSConstructor.class)) == null) {
            member = findAnnotatedMember(constructors, JSConstructor.class);
        }
        Method singleMethod;
        if ((singleMethod = (Method)member) == null) {
            singleMethod = FunctionObject.findSingleMethod(methodList, "jsConstructor");
        }
        Method method2;
        if ((method2 = singleMethod) == null) {
            Executable executable;
            if (constructors.length == 1) {
                executable = constructors[0];
            }
            else {
                executable = singleMethod;
                if (constructors.length == 2) {
                    if (constructors[0].getParameterTypes().length == 0) {
                        executable = constructors[1];
                    }
                    else {
                        executable = singleMethod;
                        if (constructors[1].getParameterTypes().length == 0) {
                            executable = constructors[0];
                        }
                    }
                }
            }
            if ((method2 = (Method)executable) == null) {
                throw Context.reportRuntimeError1("msg.ctor.multiple.parms", clazz.getName());
            }
        }
        final FunctionObject functionObject = new FunctionObject(className, method2, scriptable);
        if (functionObject.isVarArgsMethod()) {
            throw Context.reportRuntimeError1("msg.varargs.ctor", method2.getName());
        }
        functionObject.initAsConstructor(scriptable, scriptable2);
        final HashSet<String> set = new HashSet<String>();
        final HashSet<String> set2 = new HashSet<String>();
        final Method[] array = methodList;
        final int length = array.length;
        final Method method3 = null;
        int j = 0;
        Method method4 = method3;
        while (j < length) {
            final Method method5 = array[j];
            Method method6 = null;
            Label_1243: {
                Label_0773: {
                    if (method5 != method2) {
                        final String name = method5.getName();
                        if (name.equals("finishInit")) {
                            final Class<?>[] parameterTypes2 = method5.getParameterTypes();
                            if (parameterTypes2.length == 3 && parameterTypes2[0] == ScriptRuntime.ScriptableClass && parameterTypes2[1] == FunctionObject.class && parameterTypes2[2] == ScriptRuntime.ScriptableClass && Modifier.isStatic(method5.getModifiers())) {
                                method4 = method5;
                                break Label_0773;
                            }
                        }
                        if (name.indexOf(36) == -1) {
                            if (!name.equals("jsConstructor")) {
                                Annotation annotation = null;
                                final String s = null;
                                if (method5.isAnnotationPresent(JSFunction.class)) {
                                    annotation = method5.getAnnotation(JSFunction.class);
                                }
                                else if (method5.isAnnotationPresent(JSStaticFunction.class)) {
                                    annotation = method5.getAnnotation(JSStaticFunction.class);
                                }
                                else if (method5.isAnnotationPresent(JSGetter.class)) {
                                    annotation = method5.getAnnotation(JSGetter.class);
                                }
                                else if (method5.isAnnotationPresent(JSSetter.class)) {
                                    break Label_0773;
                                }
                                String s2 = s;
                                if (annotation == null) {
                                    if (name.startsWith("jsFunction_")) {
                                        s2 = "jsFunction_";
                                    }
                                    else if (name.startsWith("jsStaticFunction_")) {
                                        s2 = "jsStaticFunction_";
                                    }
                                    else if (name.startsWith("jsGet_")) {
                                        s2 = "jsGet_";
                                    }
                                    else {
                                        s2 = s;
                                        if (annotation == null) {
                                            break Label_0773;
                                        }
                                    }
                                }
                                final boolean b4 = annotation instanceof JSStaticFunction || s2 == "jsStaticFunction_";
                                HashSet<String> set3;
                                if (b4) {
                                    set3 = set;
                                }
                                else {
                                    set3 = set2;
                                }
                                final String propertyName = getPropertyName(name, s2, annotation);
                                if (set3.contains(propertyName)) {
                                    throw Context.reportRuntimeError2("duplicate.defineClass.name", name, propertyName);
                                }
                                set3.add(propertyName);
                                if (!(annotation instanceof JSGetter) && s2 != "jsGet_") {
                                    if (b4 && !Modifier.isStatic(method5.getModifiers())) {
                                        throw Context.reportRuntimeError("jsStaticFunction must be used with static method.");
                                    }
                                    final FunctionObject functionObject2 = new FunctionObject(propertyName, method5, scriptable2);
                                    if (functionObject2.isVarArgsConstructor()) {
                                        throw Context.reportRuntimeError1("msg.varargs.fun", method2.getName());
                                    }
                                    Scriptable scriptable4;
                                    if (b4) {
                                        scriptable4 = functionObject;
                                    }
                                    else {
                                        scriptable4 = scriptable2;
                                    }
                                    defineProperty(scriptable4, propertyName, functionObject2, 2);
                                    method6 = method4;
                                    if (b) {
                                        functionObject2.sealObject();
                                        method6 = method4;
                                    }
                                    break Label_1243;
                                }
                                else {
                                    if (!(scriptable2 instanceof ScriptableObject)) {
                                        throw Context.reportRuntimeError2("msg.extend.scriptable", ((ScriptableObject)scriptable2).getClass().toString(), propertyName);
                                    }
                                    final Method setterMethod = findSetterMethod(methodList, propertyName, "jsSet_");
                                    ((ScriptableObject)scriptable2).defineProperty(propertyName, null, method5, setterMethod, ((setterMethod == null) ? 1 : 0) | 0x6);
                                    method6 = method4;
                                    break Label_1243;
                                }
                            }
                        }
                    }
                }
                method6 = method4;
            }
            ++j;
            method4 = method6;
        }
        if (method4 != null) {
            method4.invoke(null, scriptable, functionObject, scriptable2);
        }
        if (b) {
            functionObject.sealObject();
            if (scriptable2 instanceof ScriptableObject) {
                ((ScriptableObject)scriptable2).sealObject();
            }
        }
        return functionObject;
    }
    
    protected static ScriptableObject buildDataDescriptor(final Scriptable scriptable, final Object o, final int n) {
        final NativeObject nativeObject = new NativeObject();
        ScriptRuntime.setBuiltinProtoAndParent(nativeObject, scriptable, TopLevel.Builtins.Object);
        nativeObject.defineProperty("value", o, 0);
        final boolean b = true;
        nativeObject.defineProperty("writable", (n & 0x1) == 0x0, 0);
        nativeObject.defineProperty("enumerable", (n & 0x2) == 0x0, 0);
        nativeObject.defineProperty("configurable", (n & 0x4) == 0x0 && b, 0);
        return nativeObject;
    }
    
    public static Object callMethod(final Context context, final Scriptable scriptable, final String s, final Object[] array) {
        final Object property = getProperty(scriptable, s);
        if (!(property instanceof Function)) {
            throw ScriptRuntime.notFunctionError(scriptable, s);
        }
        final Function function = (Function)property;
        final Scriptable topLevelScope = getTopLevelScope(scriptable);
        if (context != null) {
            return function.call(context, topLevelScope, scriptable, array);
        }
        return Context.call(null, function, topLevelScope, scriptable, array);
    }
    
    public static Object callMethod(final Scriptable scriptable, final String s, final Object[] array) {
        return callMethod(null, scriptable, s, array);
    }
    
    private void checkNotSealed(String string, final int n) {
        if (!this.isSealed()) {
            return;
        }
        if (string == null) {
            string = Integer.toString(n);
        }
        throw Context.reportRuntimeError1("msg.modify.sealed", string);
    }
    
    static void checkValidAttributes(final int n) {
        if ((n & 0xFFFFFFF0) != 0x0) {
            throw new IllegalArgumentException(String.valueOf(n));
        }
    }
    
    private static void copyTable(final Slot[] array, final Slot[] array2, int n) {
        if (n == 0) {
            throw Kit.codeBug();
        }
        final int length = array2.length;
        int length2 = array.length;
    Block_4:
        while (true) {
            --length2;
            Slot next = array[length2];
            while (next != null) {
                final int slotIndex = getSlotIndex(length, next.indexOrHash);
                Serializable s;
                if (next.next == null) {
                    s = next;
                }
                else {
                    s = new RelinkedSlot(next);
                }
                addKnownAbsentSlot(array2, (Slot)s, slotIndex);
                next = next.next;
                --n;
                if (n == 0) {
                    break Block_4;
                }
            }
        }
    }
    
    private Slot createSlot(final String s, final int n, final int n2) {
        while (true) {
            Slot[] slots = null;
            Object next;
            int n3;
            Slot slot;
            Slot unwrapSlot;
            GetterSlot getterSlot;
            Slot slot2;
            Serializable lastAdded;
            Block_14_Outer:Label_0189_Outer:
            while (true) {
                Label_0444: {
                    while (true) {
                        Label_0441: {
                            synchronized (this) {
                                slots = this.slots;
                                if (this.count == 0) {
                                    next = new Slot[4];
                                    this.slots = (Slot[])next;
                                    n3 = getSlotIndex(((Slot)next).length, n);
                                }
                                else {
                                    n3 = getSlotIndex(((Slot)(Object)slots).length, n);
                                    for (next = (slot = slots[n3]); next != null; next = ((Slot)next).next) {
                                        if (((Slot)next).indexOrHash == n) {
                                            if (((Slot)next).name == s) {
                                                break;
                                            }
                                            if (s != null && s.equals(((Slot)next).name)) {
                                                break;
                                            }
                                        }
                                        slot = (Slot)next;
                                    }
                                    if (next != null) {
                                        unwrapSlot = unwrapSlot((Slot)next);
                                        if (n2 == 4 && !(unwrapSlot instanceof GetterSlot)) {
                                            getterSlot = new GetterSlot(s, n, unwrapSlot.getAttributes());
                                            break Label_0441;
                                        }
                                        if (n2 == 5 && unwrapSlot instanceof GetterSlot) {
                                            slot2 = new Slot(s, n, unwrapSlot.getAttributes());
                                            break Label_0441;
                                        }
                                        if (n2 == 3) {
                                            return null;
                                        }
                                        return unwrapSlot;
                                    }
                                    else {
                                        if ((this.count + 1) * 4 <= slots.length * 3) {
                                            break Label_0444;
                                        }
                                        next = new Slot[slots.length * 2];
                                        copyTable(this.slots, (Slot[])next, this.count);
                                        this.slots = (Slot[])next;
                                        n3 = getSlotIndex(((Slot)next).length, n);
                                    }
                                }
                                if (n2 == 4) {
                                    lastAdded = new GetterSlot(s, n, 0);
                                }
                                else {
                                    lastAdded = new Slot(s, n, 0);
                                }
                                if (n2 == 3) {
                                    ((Slot)lastAdded).setAttributes(13);
                                }
                                ++this.count;
                                if (this.lastAdded != null) {
                                    this.lastAdded.orderedNext = (Slot)lastAdded;
                                }
                                if (this.firstAdded == null) {
                                    this.firstAdded = (Slot)lastAdded;
                                }
                                addKnownAbsentSlot((Slot[])next, this.lastAdded = (Slot)lastAdded, n3);
                                return (Slot)lastAdded;
                                // iftrue(Label_0234:, this.firstAdded != null)
                                // iftrue(Label_0255:, slot != next)
                                while (true) {
                                Block_15_Outer:
                                    while (true) {
                                        ((Slot)next).markDeleted();
                                        return (Slot)s;
                                    Label_0234:
                                        while (true) {
                                            this.firstAdded = (Slot)s;
                                            break Label_0234;
                                            Label_0255: {
                                                slot.next = (Slot)s;
                                            }
                                            continue Block_15_Outer;
                                            this.lastAdded.orderedNext = (Slot)s;
                                            Label_0222:
                                            continue Block_14_Outer;
                                        }
                                        this.lastAdded = (Slot)s;
                                        slots[n3] = (Slot)s;
                                        continue Block_15_Outer;
                                    }
                                    ((Slot)s).value = unwrapSlot.value;
                                    ((Slot)s).next = ((Slot)next).next;
                                    continue Label_0189_Outer;
                                }
                            }
                            // iftrue(Label_0222:, this.lastAdded == null)
                        }
                        continue;
                    }
                }
                next = slots;
                continue;
            }
        }
    }
    
    public static <T extends Scriptable> String defineClass(final Scriptable scriptable, final Class<T> clazz, final boolean b, final boolean b2) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        final BaseFunction buildClassCtor = buildClassCtor(scriptable, clazz, b, b2);
        if (buildClassCtor == null) {
            return null;
        }
        final String className = buildClassCtor.getClassPrototype().getClassName();
        defineProperty(scriptable, className, buildClassCtor, 2);
        return className;
    }
    
    public static <T extends Scriptable> void defineClass(final Scriptable scriptable, final Class<T> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        defineClass(scriptable, clazz, false, false);
    }
    
    public static <T extends Scriptable> void defineClass(final Scriptable scriptable, final Class<T> clazz, final boolean b) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        defineClass(scriptable, clazz, b, false);
    }
    
    public static void defineConstProperty(final Scriptable scriptable, final String s) {
        if (scriptable instanceof ConstProperties) {
            ((ConstProperties)scriptable).defineConst(s, scriptable);
            return;
        }
        defineProperty(scriptable, s, Undefined.instance, 13);
    }
    
    public static void defineProperty(final Scriptable scriptable, final String s, final Object o, final int n) {
        if (!(scriptable instanceof ScriptableObject)) {
            scriptable.put(s, scriptable, o);
            return;
        }
        ((ScriptableObject)scriptable).defineProperty(s, o, n);
    }
    
    public static boolean deleteProperty(final Scriptable scriptable, final int n) {
        final Scriptable base = getBase(scriptable, n);
        if (base == null) {
            return true;
        }
        base.delete(n);
        return true ^ base.has(n, scriptable);
    }
    
    public static boolean deleteProperty(final Scriptable scriptable, final String s) {
        final Scriptable base = getBase(scriptable, s);
        if (base == null) {
            return true;
        }
        base.delete(s);
        return true ^ base.has(s, scriptable);
    }
    
    protected static Scriptable ensureScriptable(final Object o) {
        if (!(o instanceof Scriptable)) {
            throw ScriptRuntime.typeError1("msg.arg.not.object", ScriptRuntime.typeof(o));
        }
        return (Scriptable)o;
    }
    
    protected static ScriptableObject ensureScriptableObject(final Object o) {
        if (!(o instanceof ScriptableObject)) {
            throw ScriptRuntime.typeError1("msg.arg.not.object", ScriptRuntime.typeof(o));
        }
        return (ScriptableObject)o;
    }
    
    private static <T extends Scriptable> Class<T> extendsScriptable(final Class<?> clazz) {
        if (ScriptRuntime.ScriptableClass.isAssignableFrom(clazz)) {
            return (Class<T>)clazz;
        }
        return null;
    }
    
    private static Member findAnnotatedMember(final AccessibleObject[] array, final Class<? extends Annotation> clazz) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final AccessibleObject accessibleObject = array[i];
            if (accessibleObject.isAnnotationPresent(clazz)) {
                return (Member)accessibleObject;
            }
        }
        return null;
    }
    
    private Slot findAttributeSlot(String string, final int n, final int n2) {
        final Slot slot = this.getSlot(string, n, n2);
        if (slot == null) {
            if (string == null) {
                string = Integer.toString(n);
            }
            throw Context.reportRuntimeError1("msg.prop.not.found", string);
        }
        return slot;
    }
    
    private static Method findSetterMethod(final Method[] array, String string, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("set");
        final int n = 0;
        sb.append(Character.toUpperCase(string.charAt(0)));
        sb.append(string.substring(1));
        final String string2 = sb.toString();
        for (int length = array.length, i = 0; i < length; ++i) {
            final Method method = array[i];
            final JSSetter jsSetter = method.getAnnotation(JSSetter.class);
            if (jsSetter != null && (string.equals(jsSetter.value()) || ("".equals(jsSetter.value()) && string2.equals(method.getName())))) {
                return method;
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append(string);
        string = sb2.toString();
        for (int length2 = array.length, j = n; j < length2; ++j) {
            final Method method2 = array[j];
            if (string.equals(method2.getName())) {
                return method2;
            }
        }
        return null;
    }
    
    public static Scriptable getArrayPrototype(final Scriptable scriptable) {
        return TopLevel.getBuiltinPrototype(getTopLevelScope(scriptable), TopLevel.Builtins.Array);
    }
    
    private static Scriptable getBase(Scriptable scriptable, final int n) {
        while (!scriptable.has(n, scriptable)) {
            final Scriptable prototype = scriptable.getPrototype();
            if ((scriptable = prototype) == null) {
                return prototype;
            }
        }
        return scriptable;
    }
    
    private static Scriptable getBase(Scriptable scriptable, final String s) {
        while (!scriptable.has(s, scriptable)) {
            final Scriptable prototype = scriptable.getPrototype();
            if ((scriptable = prototype) == null) {
                return prototype;
            }
        }
        return scriptable;
    }
    
    public static Scriptable getClassPrototype(Scriptable scriptable, final String s) {
        final Object property = getProperty(getTopLevelScope(scriptable), s);
        Object o;
        if (property instanceof BaseFunction) {
            o = ((BaseFunction)property).getPrototypeProperty();
        }
        else {
            if (!(property instanceof Scriptable)) {
                return null;
            }
            scriptable = (BaseFunction)property;
            o = scriptable.get("prototype", scriptable);
        }
        if (o instanceof Scriptable) {
            return (Scriptable)o;
        }
        return null;
    }
    
    public static Object getDefaultValue(final Scriptable scriptable, final Class<?> clazz) {
        Context context = null;
        for (int i = 0; i < 2; ++i) {
            boolean b;
            if (clazz == ScriptRuntime.StringClass) {
                b = (i == 0);
            }
            else {
                b = (i == 1);
            }
            String s;
            Object[] emptyArgs;
            if (b) {
                s = "toString";
                emptyArgs = ScriptRuntime.emptyArgs;
            }
            else {
                String s2;
                if (clazz == null) {
                    s2 = "undefined";
                }
                else if (clazz == ScriptRuntime.StringClass) {
                    s2 = "string";
                }
                else if (clazz == ScriptRuntime.ScriptableClass) {
                    s2 = "object";
                }
                else if (clazz == ScriptRuntime.FunctionClass) {
                    s2 = "function";
                }
                else if (clazz != ScriptRuntime.BooleanClass && clazz != Boolean.TYPE) {
                    if (clazz != ScriptRuntime.NumberClass && clazz != ScriptRuntime.ByteClass && clazz != Byte.TYPE && clazz != ScriptRuntime.ShortClass && clazz != Short.TYPE && clazz != ScriptRuntime.IntegerClass && clazz != Integer.TYPE && clazz != ScriptRuntime.FloatClass && clazz != Float.TYPE && clazz != ScriptRuntime.DoubleClass && clazz != Double.TYPE) {
                        throw Context.reportRuntimeError1("msg.invalid.type", clazz.toString());
                    }
                    s2 = "number";
                }
                else {
                    s2 = "boolean";
                }
                emptyArgs = new Object[] { s2 };
                s = "valueOf";
            }
            final Object property = getProperty(scriptable, s);
            if (property instanceof Function) {
                final Function function = (Function)property;
                Context context2;
                if ((context2 = context) == null) {
                    context2 = Context.getContext();
                }
                final Object call = function.call(context2, function.getParentScope(), scriptable, emptyArgs);
                context = context2;
                if (call != null) {
                    if (!(call instanceof Scriptable)) {
                        return call;
                    }
                    if (clazz == ScriptRuntime.ScriptableClass) {
                        return call;
                    }
                    if (clazz == ScriptRuntime.FunctionClass) {
                        return call;
                    }
                    context = context2;
                    if (b) {
                        context = context2;
                        if (call instanceof Wrapper) {
                            final Object unwrap = ((Wrapper)call).unwrap();
                            context = context2;
                            if (unwrap instanceof String) {
                                return unwrap;
                            }
                        }
                    }
                }
            }
        }
        String name;
        if (clazz == null) {
            name = "undefined";
        }
        else {
            name = clazz.getName();
        }
        throw ScriptRuntime.typeError1("msg.default.value", name);
    }
    
    public static Scriptable getFunctionPrototype(final Scriptable scriptable) {
        return TopLevel.getBuiltinPrototype(getTopLevelScope(scriptable), TopLevel.Builtins.Function);
    }
    
    public static Scriptable getObjectPrototype(final Scriptable scriptable) {
        return TopLevel.getBuiltinPrototype(getTopLevelScope(scriptable), TopLevel.Builtins.Object);
    }
    
    public static Object getProperty(final Scriptable scriptable, final int n) {
        Scriptable prototype = scriptable;
        Object value;
        do {
            value = prototype.get(n, scriptable);
            if (value != Scriptable.NOT_FOUND) {
                return value;
            }
        } while ((prototype = prototype.getPrototype()) != null);
        return value;
    }
    
    public static Object getProperty(final Scriptable scriptable, final String s) {
        Scriptable prototype = scriptable;
        Object value;
        do {
            value = prototype.get(s, scriptable);
            if (value != Scriptable.NOT_FOUND) {
                return value;
            }
        } while ((prototype = prototype.getPrototype()) != null);
        return value;
    }
    
    public static Object[] getPropertyIds(final Scriptable scriptable) {
        if (scriptable == null) {
            return ScriptRuntime.emptyArgs;
        }
        final Object[] ids = scriptable.getIds();
        ObjToIntMap objToIntMap = null;
        Scriptable scriptable2 = scriptable;
        Object[] keys = ids;
        while (true) {
            final Scriptable prototype = scriptable2.getPrototype();
            if (prototype == null) {
                break;
            }
            final Object[] ids2 = prototype.getIds();
            if (ids2.length == 0) {
                scriptable2 = prototype;
            }
            else {
                final int n = 0;
                Object[] array = keys;
                ObjToIntMap objToIntMap2;
                if ((objToIntMap2 = objToIntMap) == null) {
                    if (keys.length == 0) {
                        keys = ids2;
                        scriptable2 = prototype;
                        continue;
                    }
                    objToIntMap2 = new ObjToIntMap(keys.length + ids2.length);
                    for (int i = 0; i != keys.length; ++i) {
                        objToIntMap2.intern(keys[i]);
                    }
                    array = null;
                }
                for (int j = n; j != ids2.length; ++j) {
                    objToIntMap2.intern(ids2[j]);
                }
                keys = array;
                objToIntMap = objToIntMap2;
                scriptable2 = prototype;
            }
        }
        if (objToIntMap != null) {
            keys = objToIntMap.getKeys();
        }
        return keys;
    }
    
    private static String getPropertyName(final String s, String s2, final Annotation annotation) {
        if (s2 != null) {
            return s.substring(s2.length());
        }
        s2 = null;
        Label_0191: {
            if (annotation instanceof JSGetter) {
                final String value = ((JSGetter)annotation).value();
                if (value != null) {
                    s2 = value;
                    if (value.length() != 0) {
                        break Label_0191;
                    }
                }
                s2 = value;
                if (s.length() > 3) {
                    s2 = value;
                    if (s.startsWith("get")) {
                        final String s3 = s2 = s.substring(3);
                        if (Character.isUpperCase(s3.charAt(0))) {
                            if (s3.length() == 1) {
                                s2 = s3.toLowerCase();
                            }
                            else {
                                s2 = s3;
                                if (!Character.isUpperCase(s3.charAt(1))) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append(Character.toLowerCase(s3.charAt(0)));
                                    sb.append(s3.substring(1));
                                    s2 = sb.toString();
                                }
                            }
                        }
                    }
                }
            }
            else if (annotation instanceof JSFunction) {
                s2 = ((JSFunction)annotation).value();
            }
            else if (annotation instanceof JSStaticFunction) {
                s2 = ((JSStaticFunction)annotation).value();
            }
        }
        if (s2 != null) {
            final String s4 = s2;
            if (s2.length() != 0) {
                return s4;
            }
        }
        return s;
    }
    
    private Slot getSlot(final String s, int hashCode, final int n) {
        final Slot[] slots = this.slots;
        if (slots == null && n == 1) {
            return null;
        }
        if (s != null) {
            hashCode = s.hashCode();
        }
        if (slots != null) {
            Slot next;
            for (next = slots[getSlotIndex(slots.length, hashCode)]; next != null; next = next.next) {
                final String name = next.name;
                if (hashCode == next.indexOrHash) {
                    if (name == s) {
                        break;
                    }
                    if (s != null && s.equals(name)) {
                        break;
                    }
                }
            }
            switch (n) {
                case 5: {
                    final Slot unwrapSlot = unwrapSlot(next);
                    if (!(unwrapSlot instanceof GetterSlot)) {
                        return unwrapSlot;
                    }
                    break;
                }
                case 4: {
                    final Slot unwrapSlot2 = unwrapSlot(next);
                    if (unwrapSlot2 instanceof GetterSlot) {
                        return unwrapSlot2;
                    }
                    break;
                }
                case 2:
                case 3: {
                    if (next != null) {
                        return next;
                    }
                    break;
                }
                case 1: {
                    return next;
                }
            }
        }
        return this.createSlot(s, hashCode, n);
    }
    
    private static int getSlotIndex(final int n, final int n2) {
        return n - 1 & n2;
    }
    
    public static Scriptable getTopLevelScope(Scriptable scriptable) {
        while (true) {
            final Scriptable parentScope = scriptable.getParentScope();
            if (parentScope == null) {
                break;
            }
            scriptable = parentScope;
        }
        return scriptable;
    }
    
    public static Object getTopScopeValue(Scriptable scriptable, final Object o) {
        scriptable = getTopLevelScope(scriptable);
        do {
            if (scriptable instanceof ScriptableObject) {
                final Object associatedValue = ((ScriptableObject)scriptable).getAssociatedValue(o);
                if (associatedValue != null) {
                    return associatedValue;
                }
                continue;
            }
        } while ((scriptable = scriptable.getPrototype()) != null);
        return null;
    }
    
    public static <T> T getTypedProperty(final Scriptable scriptable, final int n, final Class<T> clazz) {
        Object property;
        if ((property = getProperty(scriptable, n)) == Scriptable.NOT_FOUND) {
            property = null;
        }
        return clazz.cast(Context.jsToJava(property, clazz));
    }
    
    public static <T> T getTypedProperty(final Scriptable scriptable, final String s, final Class<T> clazz) {
        Object property;
        if ((property = getProperty(scriptable, s)) == Scriptable.NOT_FOUND) {
            property = null;
        }
        return clazz.cast(Context.jsToJava(property, clazz));
    }
    
    public static boolean hasProperty(final Scriptable scriptable, final int n) {
        return getBase(scriptable, n) != null;
    }
    
    public static boolean hasProperty(final Scriptable scriptable, final String s) {
        return getBase(scriptable, s) != null;
    }
    
    protected static boolean isFalse(final Object o) {
        return isTrue(o) ^ true;
    }
    
    protected static boolean isTrue(final Object o) {
        return o != ScriptableObject.NOT_FOUND && ScriptRuntime.toBoolean(o);
    }
    
    private boolean putConstImpl(final String s, int attributes, final Scriptable scriptable, final Object value, final int n) {
        Slot slot;
        if (this != scriptable) {
            if ((slot = this.getSlot(s, attributes, 1)) == null) {
                return false;
            }
        }
        else if (!this.isExtensible()) {
            slot = this.getSlot(s, attributes, 1);
            if (slot == null) {
                return true;
            }
        }
        else {
            this.checkNotSealed(s, attributes);
            final Slot unwrapSlot = unwrapSlot(this.getSlot(s, attributes, 3));
            attributes = unwrapSlot.getAttributes();
            if ((attributes & 0x1) == 0x0) {
                throw Context.reportRuntimeError1("msg.var.redecl", s);
            }
            if ((attributes & 0x8) != 0x0) {
                unwrapSlot.value = value;
                if (n != 8) {
                    unwrapSlot.setAttributes(attributes & 0xFFFFFFF7);
                }
            }
            return true;
        }
        return slot.setValue(value, this, scriptable);
    }
    
    public static void putConstProperty(final Scriptable scriptable, final String s, final Object o) {
        Scriptable base;
        if ((base = getBase(scriptable, s)) == null) {
            base = scriptable;
        }
        if (base instanceof ConstProperties) {
            ((ConstProperties)base).putConst(s, scriptable, o);
        }
    }
    
    private boolean putImpl(final String s, final int n, final Scriptable scriptable, final Object o) {
        Slot slot;
        if (this != scriptable) {
            if ((slot = this.getSlot(s, n, 1)) == null) {
                return false;
            }
        }
        else if (!this.isExtensible) {
            slot = this.getSlot(s, n, 1);
            if (slot == null) {
                return true;
            }
        }
        else {
            if (this.count < 0) {
                this.checkNotSealed(s, n);
            }
            slot = this.getSlot(s, n, 2);
        }
        return slot.setValue(o, this, scriptable);
    }
    
    public static void putProperty(final Scriptable scriptable, final int n, final Object o) {
        Scriptable base;
        if ((base = getBase(scriptable, n)) == null) {
            base = scriptable;
        }
        base.put(n, scriptable, o);
    }
    
    public static void putProperty(final Scriptable scriptable, final String s, final Object o) {
        Scriptable base;
        if ((base = getBase(scriptable, s)) == null) {
            base = scriptable;
        }
        base.put(s, scriptable, o);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        final int int1 = objectInputStream.readInt();
        if (int1 != 0) {
            int i = int1;
            if ((int1 - 1 & int1) != 0x0) {
                if (int1 > 1073741824) {
                    throw new RuntimeException("Property table overflow");
                }
                for (i = 4; i < int1; i <<= 1) {}
            }
            this.slots = new Slot[i];
            final int count = this.count;
            int n;
            if ((n = count) < 0) {
                n = ~count;
            }
            Slot lastAdded = null;
            for (int j = 0; j != n; ++j) {
                this.lastAdded = (Slot)objectInputStream.readObject();
                if (j == 0) {
                    this.firstAdded = this.lastAdded;
                }
                else {
                    lastAdded.orderedNext = this.lastAdded;
                }
                addKnownAbsentSlot(this.slots, this.lastAdded, getSlotIndex(i, this.lastAdded.indexOrHash));
                lastAdded = this.lastAdded;
            }
        }
    }
    
    public static void redefineProperty(Scriptable base, final String s, final boolean b) {
        base = getBase(base, s);
        if (base == null) {
            return;
        }
        if (base instanceof ConstProperties && ((ConstProperties)base).isConst(s)) {
            throw ScriptRuntime.typeError1("msg.const.redecl", s);
        }
        if (b) {
            throw ScriptRuntime.typeError1("msg.var.redecl", s);
        }
    }
    
    private void removeSlot(final String s, int hashCode) {
        // monitorenter(this)
        while (true) {
            Label_0246: {
                if (s == null) {
                    break Label_0246;
                }
                while (true) {
                    try {
                        hashCode = s.hashCode();
                        final Slot[] slots = this.slots;
                        if (this.count != 0) {
                            final int slotIndex = getSlotIndex(slots.length, hashCode);
                            Slot slot;
                            Slot next;
                            for (next = (slot = slots[slotIndex]); next != null; next = next.next) {
                                if (next.indexOrHash == hashCode) {
                                    if (next.name == s) {
                                        break;
                                    }
                                    if (s != null && s.equals(next.name)) {
                                        break;
                                    }
                                }
                                slot = next;
                            }
                            if (next != null && (next.getAttributes() & 0x4) == 0x0) {
                                --this.count;
                                if (slot == next) {
                                    slots[slotIndex] = next.next;
                                }
                                else {
                                    slot.next = next.next;
                                }
                                final Slot unwrapSlot = unwrapSlot(next);
                                Slot lastAdded;
                                if (unwrapSlot == this.firstAdded) {
                                    lastAdded = null;
                                    this.firstAdded = unwrapSlot.orderedNext;
                                }
                                else {
                                    for (lastAdded = this.firstAdded; lastAdded.orderedNext != unwrapSlot; lastAdded = lastAdded.orderedNext) {}
                                    lastAdded.orderedNext = unwrapSlot.orderedNext;
                                }
                                if (unwrapSlot == this.lastAdded) {
                                    this.lastAdded = lastAdded;
                                }
                                next.markDeleted();
                            }
                        }
                        // monitorexit(this)
                        return;
                        // monitorexit(this)
                        throw;
                    }
                    finally {
                        continue;
                    }
                    break;
                }
            }
            continue;
        }
    }
    
    private void setGetterOrSetter(final String s, final int n, final Callable callable, final boolean b, final boolean b2) {
        if (s != null && n != 0) {
            throw new IllegalArgumentException(s);
        }
        if (!b2) {
            this.checkNotSealed(s, n);
        }
        GetterSlot getterSlot;
        if (this.isExtensible()) {
            getterSlot = (GetterSlot)this.getSlot(s, n, 4);
        }
        else {
            final Slot unwrapSlot = unwrapSlot(this.getSlot(s, n, 1));
            if (!(unwrapSlot instanceof GetterSlot)) {
                return;
            }
            getterSlot = (GetterSlot)unwrapSlot;
        }
        if (!b2 && (((Slot)getterSlot).getAttributes() & 0x1) != 0x0) {
            throw Context.reportRuntimeError1("msg.modify.readonly", s);
        }
        if (b) {
            getterSlot.setter = callable;
        }
        else {
            getterSlot.getter = callable;
        }
        getterSlot.value = Undefined.instance;
    }
    
    private static Slot unwrapSlot(final Slot slot) {
        if (slot instanceof RelinkedSlot) {
            return ((RelinkedSlot)slot).slot;
        }
        return slot;
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        synchronized (this) {
            objectOutputStream.defaultWriteObject();
            int count;
            final int n = count = this.count;
            if (n < 0) {
                count = ~n;
            }
            if (count == 0) {
                objectOutputStream.writeInt(0);
            }
            else {
                objectOutputStream.writeInt(this.slots.length);
                Slot firstAdded;
                for (firstAdded = this.firstAdded; firstAdded != null && firstAdded.wasDeleted; firstAdded = firstAdded.orderedNext) {}
                this.firstAdded = firstAdded;
                Slot orderedNext;
                for (Slot slot = firstAdded; slot != null; slot = orderedNext) {
                    objectOutputStream.writeObject(slot);
                    for (orderedNext = slot.orderedNext; orderedNext != null && orderedNext.wasDeleted; orderedNext = orderedNext.orderedNext) {}
                    slot.orderedNext = orderedNext;
                }
            }
        }
    }
    
    void addLazilyInitializedValue(final String s, final int n, final LazilyLoadedCtor value, final int attributes) {
        if (s != null && n != 0) {
            throw new IllegalArgumentException(s);
        }
        this.checkNotSealed(s, n);
        final GetterSlot getterSlot = (GetterSlot)this.getSlot(s, n, 4);
        ((Slot)getterSlot).setAttributes(attributes);
        getterSlot.getter = null;
        getterSlot.setter = null;
        getterSlot.value = value;
    }
    
    protected int applyDescriptorToAttributeBitset(int n, final ScriptableObject scriptableObject) {
        final Object property = getProperty(scriptableObject, "enumerable");
        int n2 = n;
        if (property != ScriptableObject.NOT_FOUND) {
            if (ScriptRuntime.toBoolean(property)) {
                n &= 0xFFFFFFFD;
            }
            else {
                n |= 0x2;
            }
            n2 = n;
        }
        final Object property2 = getProperty(scriptableObject, "writable");
        n = n2;
        if (property2 != ScriptableObject.NOT_FOUND) {
            if (ScriptRuntime.toBoolean(property2)) {
                n = (n2 & 0xFFFFFFFE);
            }
            else {
                n = (n2 | 0x1);
            }
        }
        final Object property3 = getProperty(scriptableObject, "configurable");
        int n3 = n;
        if (property3 != ScriptableObject.NOT_FOUND) {
            if (ScriptRuntime.toBoolean(property3)) {
                n &= 0xFFFFFFFB;
            }
            else {
                n |= 0x4;
            }
            n3 = n;
        }
        return n3;
    }
    
    public final Object associateValue(Object initHash, final Object o) {
        // monitorenter(this)
        Label_0014: {
            if (o != null) {
                break Label_0014;
            }
            while (true) {
                try {
                    throw new IllegalArgumentException();
                    // iftrue(Label_0041:, associatedValues = this.associatedValues != null)
                    // monitorexit(this)
                    Block_3: {
                        break Block_3;
                        throw;
                    }
                    final Map<Object, Object> associatedValues = new HashMap<Object, Object>();
                    this.associatedValues = associatedValues;
                    Label_0041: {
                        initHash = Kit.initHash(associatedValues, initHash, o);
                    }
                    // monitorexit(this)
                    return initHash;
                }
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    public boolean avoidObjectDetection() {
        return false;
    }
    
    protected void checkPropertyChange(final String s, final ScriptableObject scriptableObject, final ScriptableObject scriptableObject2) {
        if (scriptableObject == null) {
            if (!this.isExtensible()) {
                throw ScriptRuntime.typeError0("msg.not.extensible");
            }
        }
        else if (isFalse(scriptableObject.get("configurable", scriptableObject))) {
            if (isTrue(getProperty(scriptableObject2, "configurable"))) {
                throw ScriptRuntime.typeError1("msg.change.configurable.false.to.true", s);
            }
            if (isTrue(scriptableObject.get("enumerable", scriptableObject)) != isTrue(getProperty(scriptableObject2, "enumerable"))) {
                throw ScriptRuntime.typeError1("msg.change.enumerable.with.configurable.false", s);
            }
            final boolean dataDescriptor = this.isDataDescriptor(scriptableObject2);
            final boolean accessorDescriptor = this.isAccessorDescriptor(scriptableObject2);
            if (!dataDescriptor && !accessorDescriptor) {
                return;
            }
            if (dataDescriptor && this.isDataDescriptor(scriptableObject)) {
                if (isFalse(scriptableObject.get("writable", scriptableObject))) {
                    if (isTrue(getProperty(scriptableObject2, "writable"))) {
                        throw ScriptRuntime.typeError1("msg.change.writable.false.to.true.with.configurable.false", s);
                    }
                    if (!this.sameValue(getProperty(scriptableObject2, "value"), scriptableObject.get("value", scriptableObject))) {
                        throw ScriptRuntime.typeError1("msg.change.value.with.writable.false", s);
                    }
                }
            }
            else if (accessorDescriptor && this.isAccessorDescriptor(scriptableObject)) {
                if (!this.sameValue(getProperty(scriptableObject2, "set"), scriptableObject.get("set", scriptableObject))) {
                    throw ScriptRuntime.typeError1("msg.change.setter.with.configurable.false", s);
                }
                if (!this.sameValue(getProperty(scriptableObject2, "get"), scriptableObject.get("get", scriptableObject))) {
                    throw ScriptRuntime.typeError1("msg.change.getter.with.configurable.false", s);
                }
            }
            else {
                if (this.isDataDescriptor(scriptableObject)) {
                    throw ScriptRuntime.typeError1("msg.change.property.data.to.accessor.with.configurable.false", s);
                }
                throw ScriptRuntime.typeError1("msg.change.property.accessor.to.data.with.configurable.false", s);
            }
        }
    }
    
    protected void checkPropertyDefinition(final ScriptableObject scriptableObject) {
        final Object property = getProperty(scriptableObject, "get");
        if (property != ScriptableObject.NOT_FOUND && property != Undefined.instance && !(property instanceof Callable)) {
            throw ScriptRuntime.notFunctionError(property);
        }
        final Object property2 = getProperty(scriptableObject, "set");
        if (property2 != ScriptableObject.NOT_FOUND && property2 != Undefined.instance && !(property2 instanceof Callable)) {
            throw ScriptRuntime.notFunctionError(property2);
        }
        if (this.isDataDescriptor(scriptableObject) && this.isAccessorDescriptor(scriptableObject)) {
            throw ScriptRuntime.typeError0("msg.both.data.and.accessor.desc");
        }
    }
    
    @Override
    public void defineConst(final String s, final Scriptable scriptable) {
        if (this.putConstImpl(s, 0, scriptable, Undefined.instance, 8)) {
            return;
        }
        if (scriptable == this) {
            throw Kit.codeBug();
        }
        if (scriptable instanceof ConstProperties) {
            ((ConstProperties)scriptable).defineConst(s, scriptable);
        }
    }
    
    public void defineFunctionProperties(final String[] array, final Class<?> clazz, final int n) {
        final Method[] methodList = FunctionObject.getMethodList(clazz);
        for (int i = 0; i < array.length; ++i) {
            final String s = array[i];
            final Method singleMethod = FunctionObject.findSingleMethod(methodList, s);
            if (singleMethod == null) {
                throw Context.reportRuntimeError2("msg.method.not.found", s, clazz.getName());
            }
            this.defineProperty(s, new FunctionObject(s, singleMethod, this), n);
        }
    }
    
    public void defineOwnProperties(final Context context, final ScriptableObject scriptableObject) {
        final Object[] ids = scriptableObject.getIds();
        final ScriptableObject[] array = new ScriptableObject[ids.length];
        for (int i = 0; i < ids.length; ++i) {
            final ScriptableObject ensureScriptableObject = ensureScriptableObject(ScriptRuntime.getObjectElem(scriptableObject, ids[i], context));
            this.checkPropertyDefinition(ensureScriptableObject);
            array[i] = ensureScriptableObject;
        }
        for (int j = 0; j < ids.length; ++j) {
            this.defineOwnProperty(context, ids[j], array[j]);
        }
    }
    
    public void defineOwnProperty(final Context context, final Object o, final ScriptableObject scriptableObject) {
        this.checkPropertyDefinition(scriptableObject);
        this.defineOwnProperty(context, o, scriptableObject, true);
    }
    
    protected void defineOwnProperty(final Context context, Object o, final ScriptableObject scriptableObject, final boolean b) {
        boolean b2 = true;
        final Slot slot = this.getSlot(context, o, 1);
        if (slot != null) {
            b2 = false;
        }
        if (b) {
            ScriptableObject propertyDescriptor;
            if (slot == null) {
                propertyDescriptor = null;
            }
            else {
                propertyDescriptor = slot.getPropertyDescriptor(context, this);
            }
            this.checkPropertyChange(ScriptRuntime.toString(o), propertyDescriptor, scriptableObject);
        }
        final boolean accessorDescriptor = this.isAccessorDescriptor(scriptableObject);
        Slot slot2;
        int n2;
        if (slot == null) {
            int n;
            if (accessorDescriptor) {
                n = 4;
            }
            else {
                n = 2;
            }
            slot2 = this.getSlot(context, o, n);
            n2 = this.applyDescriptorToAttributeBitset(7, scriptableObject);
        }
        else {
            n2 = this.applyDescriptorToAttributeBitset(slot.getAttributes(), scriptableObject);
            slot2 = slot;
        }
        final Slot unwrapSlot = unwrapSlot(slot2);
        if (accessorDescriptor) {
            Serializable slot3 = unwrapSlot;
            if (!(unwrapSlot instanceof GetterSlot)) {
                slot3 = this.getSlot(context, o, 4);
            }
            final GetterSlot getterSlot = (GetterSlot)slot3;
            o = getProperty(scriptableObject, "get");
            if (o != ScriptableObject.NOT_FOUND) {
                getterSlot.getter = o;
            }
            o = getProperty(scriptableObject, "set");
            if (o != ScriptableObject.NOT_FOUND) {
                getterSlot.setter = o;
            }
            getterSlot.value = Undefined.instance;
            ((Slot)getterSlot).setAttributes(n2);
            return;
        }
        Slot slot4 = unwrapSlot;
        if (unwrapSlot instanceof GetterSlot) {
            slot4 = unwrapSlot;
            if (this.isDataDescriptor(scriptableObject)) {
                slot4 = this.getSlot(context, o, 5);
            }
        }
        final Object property = getProperty(scriptableObject, "value");
        if (property != ScriptableObject.NOT_FOUND) {
            slot4.value = property;
        }
        else if (b2) {
            slot4.value = Undefined.instance;
        }
        slot4.setAttributes(n2);
    }
    
    public void defineProperty(final String s, final Class<?> clazz, int n) {
        final int length = s.length();
        if (length == 0) {
            throw new IllegalArgumentException();
        }
        final char[] array = new char[length + 3];
        s.getChars(0, length, array, 3);
        array[3] = Character.toUpperCase(array[3]);
        array[0] = 'g';
        array[1] = 'e';
        array[2] = 't';
        final String s2 = new String(array);
        array[0] = 's';
        final String s3 = new String(array);
        final Method[] methodList = FunctionObject.getMethodList(clazz);
        final Method singleMethod = FunctionObject.findSingleMethod(methodList, s2);
        Method singleMethod2 = FunctionObject.findSingleMethod(methodList, s3);
        if (singleMethod2 == null) {
            n |= 0x1;
        }
        if (singleMethod2 == null) {
            singleMethod2 = null;
        }
        this.defineProperty(s, null, singleMethod, singleMethod2, n);
    }
    
    public void defineProperty(final String s, final Object o, final int n) {
        this.checkNotSealed(s, 0);
        this.put(s, this, o);
        this.setAttributes(s, n);
    }
    
    public void defineProperty(final String s, final Object o, final Method method, final Method method2, final int attributes) {
        MemberBox getter = null;
        if (method != null) {
            getter = new MemberBox(method);
            boolean b;
            if (!Modifier.isStatic(method.getModifiers())) {
                b = (o != null);
                getter.delegateTo = o;
            }
            else {
                b = true;
                getter.delegateTo = Void.TYPE;
            }
            final String s2 = null;
            String s3 = null;
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 0) {
                s3 = s2;
                if (b) {
                    s3 = "msg.obj.getter.parms";
                }
            }
            else if (parameterTypes.length == 1) {
                final Class<?> clazz = parameterTypes[0];
                if (clazz != ScriptRuntime.ScriptableClass && clazz != ScriptRuntime.ScriptableObjectClass) {
                    s3 = "msg.bad.getter.parms";
                }
                else if (!b) {
                    s3 = "msg.bad.getter.parms";
                }
            }
            else {
                s3 = "msg.bad.getter.parms";
            }
            if (s3 != null) {
                throw Context.reportRuntimeError1(s3, method.toString());
            }
        }
        MemberBox setter = null;
        if (method2 != null) {
            if (method2.getReturnType() != Void.TYPE) {
                throw Context.reportRuntimeError1("msg.setter.return", method2.toString());
            }
            setter = new MemberBox(method2);
            boolean b2;
            if (!Modifier.isStatic(method2.getModifiers())) {
                b2 = (o != null);
                setter.delegateTo = o;
            }
            else {
                b2 = true;
                setter.delegateTo = Void.TYPE;
            }
            final String s4 = null;
            String s5 = null;
            final Class<?>[] parameterTypes2 = method2.getParameterTypes();
            if (parameterTypes2.length == 1) {
                s5 = s4;
                if (b2) {
                    s5 = "msg.setter2.expected";
                }
            }
            else if (parameterTypes2.length == 2) {
                final Class<?> clazz2 = parameterTypes2[0];
                if (clazz2 != ScriptRuntime.ScriptableClass && clazz2 != ScriptRuntime.ScriptableObjectClass) {
                    s5 = "msg.setter2.parms";
                }
                else if (!b2) {
                    s5 = "msg.setter1.parms";
                }
            }
            else {
                s5 = "msg.setter.parms";
            }
            if (s5 != null) {
                throw Context.reportRuntimeError1(s5, method2.toString());
            }
        }
        final GetterSlot getterSlot = (GetterSlot)this.getSlot(s, 0, 4);
        ((Slot)getterSlot).setAttributes(attributes);
        getterSlot.getter = getter;
        getterSlot.setter = setter;
    }
    
    @Override
    public void delete(final int n) {
        this.checkNotSealed(null, n);
        this.removeSlot(null, n);
    }
    
    @Override
    public void delete(final String s) {
        this.checkNotSealed(s, 0);
        this.removeSlot(s, 0);
    }
    
    protected Object equivalentValues(final Object o) {
        if (this == o) {
            return Boolean.TRUE;
        }
        return Scriptable.NOT_FOUND;
    }
    
    @Override
    public Object get(final int n, final Scriptable scriptable) {
        if (this.externalData != null) {
            if (n < this.externalData.getArrayLength()) {
                return this.externalData.getArrayElement(n);
            }
            return Scriptable.NOT_FOUND;
        }
        else {
            final Slot slot = this.getSlot(null, n, 1);
            if (slot == null) {
                return Scriptable.NOT_FOUND;
            }
            return slot.getValue(scriptable);
        }
    }
    
    public Object get(final Object o) {
        Object o2 = null;
        if (o instanceof String) {
            o2 = this.get((String)o, this);
        }
        else if (o instanceof Number) {
            o2 = this.get(((Number)o).intValue(), this);
        }
        if (o2 == Scriptable.NOT_FOUND || o2 == Undefined.instance) {
            return null;
        }
        if (o2 instanceof Wrapper) {
            return ((Wrapper)o2).unwrap();
        }
        return o2;
    }
    
    @Override
    public Object get(final String s, final Scriptable scriptable) {
        final Slot slot = this.getSlot(s, 0, 1);
        if (slot == null) {
            return Scriptable.NOT_FOUND;
        }
        return slot.getValue(scriptable);
    }
    
    @Override
    public Object[] getAllIds() {
        return this.getIds(true);
    }
    
    public final Object getAssociatedValue(final Object o) {
        final Map<Object, Object> associatedValues = this.associatedValues;
        if (associatedValues == null) {
            return null;
        }
        return associatedValues.get(o);
    }
    
    public int getAttributes(final int n) {
        return this.findAttributeSlot(null, n, 1).getAttributes();
    }
    
    @Deprecated
    public final int getAttributes(final int n, final Scriptable scriptable) {
        return this.getAttributes(n);
    }
    
    public int getAttributes(final String s) {
        return this.findAttributeSlot(s, 0, 1).getAttributes();
    }
    
    @Deprecated
    public final int getAttributes(final String s, final Scriptable scriptable) {
        return this.getAttributes(s);
    }
    
    @Override
    public abstract String getClassName();
    
    @Override
    public Object getDefaultValue(final Class<?> clazz) {
        return getDefaultValue(this, clazz);
    }
    
    public ExternalArrayData getExternalArrayData() {
        return this.externalData;
    }
    
    public Object getExternalArrayLength() {
        int arrayLength;
        if (this.externalData == null) {
            arrayLength = 0;
        }
        else {
            arrayLength = this.externalData.getArrayLength();
        }
        return arrayLength;
    }
    
    public Object getGetterOrSetter(final String s, final int n, final boolean b) {
        if (s != null && n != 0) {
            throw new IllegalArgumentException(s);
        }
        final Slot unwrapSlot = unwrapSlot(this.getSlot(s, n, 1));
        if (unwrapSlot == null) {
            return null;
        }
        if (!(unwrapSlot instanceof GetterSlot)) {
            return Undefined.instance;
        }
        final GetterSlot getterSlot = (GetterSlot)unwrapSlot;
        Object o;
        if (b) {
            o = getterSlot.setter;
        }
        else {
            o = getterSlot.getter;
        }
        if (o != null) {
            return o;
        }
        return Undefined.instance;
    }
    
    @Override
    public Object[] getIds() {
        return this.getIds(false);
    }
    
    Object[] getIds(final boolean b) {
        final Slot[] slots = this.slots;
        int arrayLength;
        if (this.externalData == null) {
            arrayLength = 0;
        }
        else {
            arrayLength = this.externalData.getArrayLength();
        }
        Object[] emptyArgs;
        if (arrayLength == 0) {
            emptyArgs = ScriptRuntime.emptyArgs;
        }
        else {
            final Object[] array = new Object[arrayLength];
            int n = 0;
            while (true) {
                emptyArgs = array;
                if (n >= arrayLength) {
                    break;
                }
                array[n] = n;
                ++n;
            }
        }
        if (slots == null) {
            return emptyArgs;
        }
        final int n2 = arrayLength;
        Slot slot = this.firstAdded;
        Object[] array2;
        int n3;
        Slot slot2;
        while (true) {
            array2 = emptyArgs;
            n3 = n2;
            slot2 = slot;
            if (slot == null) {
                break;
            }
            array2 = emptyArgs;
            n3 = n2;
            slot2 = slot;
            if (!slot.wasDeleted) {
                break;
            }
            slot = slot.orderedNext;
        }
        while (slot2 != null) {
            Object[] array3 = null;
            int n4 = 0;
            Label_0245: {
                if (!b) {
                    array3 = array2;
                    n4 = n3;
                    if ((slot2.getAttributes() & 0x2) != 0x0) {
                        break Label_0245;
                    }
                }
                Object[] array4 = array2;
                if (n3 == arrayLength) {
                    final Object[] array5 = array4 = new Object[slots.length + arrayLength];
                    if (array2 != null) {
                        System.arraycopy(array2, 0, array5, 0, arrayLength);
                        array4 = array5;
                    }
                }
                Serializable s;
                if (slot2.name != null) {
                    s = slot2.name;
                }
                else {
                    s = slot2.indexOrHash;
                }
                array4[n3] = s;
                n4 = n3 + 1;
                array3 = array4;
            }
            Slot slot3 = slot2.orderedNext;
            while (true) {
                array2 = array3;
                n3 = n4;
                slot2 = slot3;
                if (slot3 == null) {
                    break;
                }
                array2 = array3;
                n3 = n4;
                slot2 = slot3;
                if (!slot3.wasDeleted) {
                    break;
                }
                slot3 = slot3.orderedNext;
            }
        }
        if (n3 == array2.length + arrayLength) {
            return array2;
        }
        final Object[] array6 = new Object[n3];
        System.arraycopy(array2, 0, array6, 0, n3);
        return array6;
    }
    
    protected ScriptableObject getOwnPropertyDescriptor(final Context context, final Object o) {
        final Slot slot = this.getSlot(context, o, 1);
        if (slot == null) {
            return null;
        }
        Scriptable parentScope = this.getParentScope();
        if (parentScope == null) {
            parentScope = this;
        }
        return slot.getPropertyDescriptor(context, parentScope);
    }
    
    @Override
    public Scriptable getParentScope() {
        return this.parentScopeObject;
    }
    
    @Override
    public Scriptable getPrototype() {
        return this.prototypeObject;
    }
    
    protected Slot getSlot(final Context context, final Object o, final int n) {
        final String stringIdOrIndex = ScriptRuntime.toStringIdOrIndex(context, o);
        if (stringIdOrIndex == null) {
            return this.getSlot(null, ScriptRuntime.lastIndexResult(context), n);
        }
        return this.getSlot(stringIdOrIndex, 0, n);
    }
    
    public String getTypeOf() {
        if (this.avoidObjectDetection()) {
            return "undefined";
        }
        return "object";
    }
    
    @Override
    public boolean has(final int n, final Scriptable scriptable) {
        final ExternalArrayData externalData = this.externalData;
        final boolean b = false;
        boolean b2 = false;
        if (externalData != null) {
            if (n < this.externalData.getArrayLength()) {
                b2 = true;
            }
            return b2;
        }
        boolean b3 = b;
        if (this.getSlot(null, n, 1) != null) {
            b3 = true;
        }
        return b3;
    }
    
    @Override
    public boolean has(final String s, final Scriptable scriptable) {
        return this.getSlot(s, 0, 1) != null;
    }
    
    @Override
    public boolean hasInstance(final Scriptable scriptable) {
        return ScriptRuntime.jsDelegatesTo(scriptable, this);
    }
    
    protected boolean isAccessorDescriptor(final ScriptableObject scriptableObject) {
        return hasProperty(scriptableObject, "get") || hasProperty(scriptableObject, "set");
    }
    
    @Override
    public boolean isConst(final String s) {
        final Slot slot = this.getSlot(s, 0, 1);
        return slot != null && (slot.getAttributes() & 0x5) == 0x5;
    }
    
    protected boolean isDataDescriptor(final ScriptableObject scriptableObject) {
        return hasProperty(scriptableObject, "value") || hasProperty(scriptableObject, "writable");
    }
    
    public boolean isEmpty() {
        return this.count == 0 || this.count == -1;
    }
    
    public boolean isExtensible() {
        return this.isExtensible;
    }
    
    protected boolean isGenericDescriptor(final ScriptableObject scriptableObject) {
        return !this.isDataDescriptor(scriptableObject) && !this.isAccessorDescriptor(scriptableObject);
    }
    
    protected boolean isGetterOrSetter(final String s, final int n, final boolean b) {
        final Slot unwrapSlot = unwrapSlot(this.getSlot(s, n, 1));
        if (unwrapSlot instanceof GetterSlot) {
            if (b && ((GetterSlot)unwrapSlot).setter != null) {
                return true;
            }
            if (!b && ((GetterSlot)unwrapSlot).getter != null) {
                return true;
            }
        }
        return false;
    }
    
    public final boolean isSealed() {
        return this.count < 0;
    }
    
    public void preventExtensions() {
        this.isExtensible = false;
    }
    
    @Override
    public void put(final int n, final Scriptable scriptable, final Object o) {
        if (this.externalData != null) {
            if (n < this.externalData.getArrayLength()) {
                this.externalData.setArrayElement(n, o);
                return;
            }
            throw new JavaScriptException(ScriptRuntime.newNativeError(Context.getCurrentContext(), this, TopLevel.NativeErrors.RangeError, new Object[] { "External array index out of bounds " }), null, 0);
        }
        else {
            if (this.putImpl(null, n, scriptable, o)) {
                return;
            }
            if (scriptable == this) {
                throw Kit.codeBug();
            }
            scriptable.put(n, scriptable, o);
        }
    }
    
    @Override
    public void put(final String s, final Scriptable scriptable, final Object o) {
        if (this.putImpl(s, 0, scriptable, o)) {
            return;
        }
        if (scriptable == this) {
            throw Kit.codeBug();
        }
        scriptable.put(s, scriptable, o);
    }
    
    @Override
    public void putConst(final String s, final Scriptable scriptable, final Object o) {
        if (this.putConstImpl(s, 0, scriptable, o, 1)) {
            return;
        }
        if (scriptable == this) {
            throw Kit.codeBug();
        }
        if (scriptable instanceof ConstProperties) {
            ((ConstProperties)scriptable).putConst(s, scriptable, o);
            return;
        }
        scriptable.put(s, scriptable, o);
    }
    
    protected boolean sameValue(final Object o, final Object o2) {
        if (o == ScriptableObject.NOT_FOUND) {
            return true;
        }
        Object instance;
        if ((instance = o2) == ScriptableObject.NOT_FOUND) {
            instance = Undefined.instance;
        }
        if (instance instanceof Number && o instanceof Number) {
            final double doubleValue = ((Number)instance).doubleValue();
            final double doubleValue2 = ((Number)o).doubleValue();
            if (Double.isNaN(doubleValue) && Double.isNaN(doubleValue2)) {
                return true;
            }
            if (doubleValue == 0.0 && Double.doubleToLongBits(doubleValue) != Double.doubleToLongBits(doubleValue2)) {
                return false;
            }
        }
        return ScriptRuntime.shallowEq(instance, o);
    }
    
    public void sealObject() {
        synchronized (this) {
            if (this.count >= 0) {
                for (Slot slot = this.firstAdded; slot != null; slot = slot.orderedNext) {
                    final Object value = slot.value;
                    if (value instanceof LazilyLoadedCtor) {
                        final LazilyLoadedCtor lazilyLoadedCtor = (LazilyLoadedCtor)value;
                        try {
                            lazilyLoadedCtor.init();
                        }
                        finally {
                            slot.value = lazilyLoadedCtor.getValue();
                        }
                    }
                }
                this.count ^= -1;
            }
        }
    }
    
    public void setAttributes(final int n, final int attributes) {
        this.checkNotSealed(null, n);
        this.findAttributeSlot(null, n, 2).setAttributes(attributes);
    }
    
    @Deprecated
    public void setAttributes(final int n, final Scriptable scriptable, final int n2) {
        this.setAttributes(n, n2);
    }
    
    public void setAttributes(final String s, final int attributes) {
        this.checkNotSealed(s, 0);
        this.findAttributeSlot(s, 0, 2).setAttributes(attributes);
    }
    
    @Deprecated
    public final void setAttributes(final String s, final Scriptable scriptable, final int n) {
        this.setAttributes(s, n);
    }
    
    public void setExternalArrayData(final ExternalArrayData externalData) {
        this.externalData = externalData;
        if (externalData == null) {
            this.delete("length");
            return;
        }
        this.defineProperty("length", null, ScriptableObject.GET_ARRAY_LENGTH, null, 3);
    }
    
    public void setGetterOrSetter(final String s, final int n, final Callable callable, final boolean b) {
        this.setGetterOrSetter(s, n, callable, b, false);
    }
    
    @Override
    public void setParentScope(final Scriptable parentScopeObject) {
        this.parentScopeObject = parentScopeObject;
    }
    
    @Override
    public void setPrototype(final Scriptable prototypeObject) {
        this.prototypeObject = prototypeObject;
    }
    
    public int size() {
        if (this.count < 0) {
            return ~this.count;
        }
        return this.count;
    }
    
    private static final class GetterSlot extends Slot
    {
        static final long serialVersionUID = -4900574849788797588L;
        Object getter;
        Object setter;
        
        GetterSlot(final String s, final int n, final int n2) {
            super(s, n, n2);
        }
        
        @Override
        ScriptableObject getPropertyDescriptor(final Context context, final Scriptable scriptable) {
            final int attributes = ((Slot)this).getAttributes();
            final NativeObject nativeObject = new NativeObject();
            ScriptRuntime.setBuiltinProtoAndParent(nativeObject, scriptable, TopLevel.Builtins.Object);
            final boolean b = true;
            nativeObject.defineProperty("enumerable", (attributes & 0x2) == 0x0, 0);
            nativeObject.defineProperty("configurable", (attributes & 0x4) == 0x0 && b, 0);
            if (this.getter != null) {
                nativeObject.defineProperty("get", this.getter, 0);
            }
            if (this.setter != null) {
                nativeObject.defineProperty("set", this.setter, 0);
            }
            return nativeObject;
        }
        
        @Override
        Object getValue(Scriptable value) {
            if (this.getter != null) {
                if (this.getter instanceof MemberBox) {
                    final MemberBox memberBox = (MemberBox)this.getter;
                    Object[] emptyArgs;
                    if (memberBox.delegateTo == null) {
                        emptyArgs = ScriptRuntime.emptyArgs;
                    }
                    else {
                        final Object delegateTo = memberBox.delegateTo;
                        emptyArgs = new Object[] { value };
                        value = (Scriptable)delegateTo;
                    }
                    return memberBox.invoke(value, emptyArgs);
                }
                if (this.getter instanceof Function) {
                    final Function function = (Function)this.getter;
                    return function.call(Context.getContext(), function.getParentScope(), value, ScriptRuntime.emptyArgs);
                }
            }
            final Object value2 = this.value;
            if (value2 instanceof LazilyLoadedCtor) {
                value = (Scriptable)value2;
                try {
                    ((LazilyLoadedCtor)value).init();
                    value = (Scriptable)((LazilyLoadedCtor)value).getValue();
                    return this.value = value;
                }
                finally {
                    this.value = ((LazilyLoadedCtor)value).getValue();
                }
            }
            return value2;
        }
        
        @Override
        void markDeleted() {
            super.markDeleted();
            this.getter = null;
            this.setter = null;
        }
        
        @Override
        boolean setValue(Object delegateTo, final Scriptable scriptable, Scriptable scriptable2) {
            if (this.setter == null) {
                if (this.getter == null) {
                    return super.setValue(delegateTo, scriptable, scriptable2);
                }
                if (Context.getContext().hasFeature(11)) {
                    throw ScriptRuntime.typeError1("msg.set.prop.no.setter", this.name);
                }
                return true;
            }
            else {
                final Context context = Context.getContext();
                if (this.setter instanceof MemberBox) {
                    final MemberBox memberBox = (MemberBox)this.setter;
                    final Class<?>[] argTypes = memberBox.argTypes;
                    final Object convertArg = FunctionObject.convertArg(context, scriptable2, delegateTo, FunctionObject.getTypeTag(argTypes[argTypes.length - 1]));
                    Object[] array;
                    if (memberBox.delegateTo == null) {
                        array = new Object[] { convertArg };
                    }
                    else {
                        delegateTo = memberBox.delegateTo;
                        final Object[] array2 = { scriptable2, convertArg };
                        scriptable2 = (Scriptable)delegateTo;
                        array = array2;
                    }
                    memberBox.invoke(scriptable2, array);
                    return true;
                }
                if (this.setter instanceof Function) {
                    final Function function = (Function)this.setter;
                    function.call(context, function.getParentScope(), scriptable2, new Object[] { delegateTo });
                }
                return true;
            }
        }
    }
    
    private static class RelinkedSlot extends Slot
    {
        final Slot slot;
        
        RelinkedSlot(final Slot slot) {
            super(slot.name, slot.indexOrHash, slot.attributes);
            this.slot = unwrapSlot(slot);
        }
        
        private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.writeObject(this.slot);
        }
        
        @Override
        int getAttributes() {
            return this.slot.getAttributes();
        }
        
        @Override
        ScriptableObject getPropertyDescriptor(final Context context, final Scriptable scriptable) {
            return this.slot.getPropertyDescriptor(context, scriptable);
        }
        
        @Override
        Object getValue(final Scriptable scriptable) {
            return this.slot.getValue(scriptable);
        }
        
        @Override
        void markDeleted() {
            super.markDeleted();
            this.slot.markDeleted();
        }
        
        @Override
        void setAttributes(final int attributes) {
            this.slot.setAttributes(attributes);
        }
        
        @Override
        boolean setValue(final Object o, final Scriptable scriptable, final Scriptable scriptable2) {
            return this.slot.setValue(o, scriptable, scriptable2);
        }
    }
    
    private static class Slot implements Serializable
    {
        private static final long serialVersionUID = -6090581677123995491L;
        private volatile short attributes;
        int indexOrHash;
        String name;
        transient Slot next;
        transient volatile Slot orderedNext;
        volatile Object value;
        transient volatile boolean wasDeleted;
        
        Slot(final String name, final int indexOrHash, final int n) {
            this.name = name;
            this.indexOrHash = indexOrHash;
            this.attributes = (short)n;
        }
        
        private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            if (this.name != null) {
                this.indexOrHash = this.name.hashCode();
            }
        }
        
        int getAttributes() {
            return this.attributes;
        }
        
        ScriptableObject getPropertyDescriptor(final Context context, final Scriptable scriptable) {
            return ScriptableObject.buildDataDescriptor(scriptable, this.value, this.attributes);
        }
        
        Object getValue(final Scriptable scriptable) {
            return this.value;
        }
        
        void markDeleted() {
            this.wasDeleted = true;
            this.value = null;
            this.name = null;
        }
        
        void setAttributes(final int n) {
            synchronized (this) {
                ScriptableObject.checkValidAttributes(n);
                this.attributes = (short)n;
            }
        }
        
        boolean setValue(final Object value, final Scriptable scriptable, final Scriptable scriptable2) {
            if ((this.attributes & 0x1) != 0x0) {
                return true;
            }
            if (scriptable == scriptable2) {
                this.value = value;
                return true;
            }
            return false;
        }
    }
}
