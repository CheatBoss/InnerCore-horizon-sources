package org.mozilla.javascript;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;

public class NativeJavaObject implements Scriptable, Wrapper, Serializable
{
    private static final Object COERCED_INTERFACE_KEY;
    static final byte CONVERSION_NONE = 99;
    static final byte CONVERSION_NONTRIVIAL = 0;
    static final byte CONVERSION_TRIVIAL = 1;
    private static final int JSTYPE_BOOLEAN = 2;
    private static final int JSTYPE_JAVA_ARRAY = 7;
    private static final int JSTYPE_JAVA_CLASS = 5;
    private static final int JSTYPE_JAVA_OBJECT = 6;
    private static final int JSTYPE_NULL = 1;
    private static final int JSTYPE_NUMBER = 3;
    private static final int JSTYPE_OBJECT = 8;
    private static final int JSTYPE_STRING = 4;
    private static final int JSTYPE_UNDEFINED = 0;
    private static Method adapter_readAdapterObject;
    private static Method adapter_writeAdapterObject;
    static final long serialVersionUID = -6948590651130498591L;
    private transient Map<String, FieldAndMethods> fieldAndMethods;
    protected transient boolean isAdapter;
    protected transient Object javaObject;
    protected transient JavaMembers members;
    protected Scriptable parent;
    protected Scriptable prototype;
    protected transient Class<?> staticType;
    
    static {
        COERCED_INTERFACE_KEY = "Coerced Interface";
        final Class[] array = new Class[2];
        final Class<?> classOrNull = Kit.classOrNull("org.mozilla.javascript.JavaAdapter");
        if (classOrNull != null) {
            try {
                array[0] = ScriptRuntime.ObjectClass;
                array[1] = Kit.classOrNull("java.io.ObjectOutputStream");
                NativeJavaObject.adapter_writeAdapterObject = classOrNull.getMethod("writeAdapterObject", (Class<?>[])array);
                array[0] = ScriptRuntime.ScriptableClass;
                array[1] = Kit.classOrNull("java.io.ObjectInputStream");
                NativeJavaObject.adapter_readAdapterObject = classOrNull.getMethod("readAdapterObject", (Class<?>[])array);
            }
            catch (NoSuchMethodException ex) {
                NativeJavaObject.adapter_writeAdapterObject = null;
                NativeJavaObject.adapter_readAdapterObject = null;
            }
        }
    }
    
    public NativeJavaObject() {
    }
    
    public NativeJavaObject(final Scriptable scriptable, final Object o, final Class<?> clazz) {
        this(scriptable, o, clazz, false);
    }
    
    public NativeJavaObject(final Scriptable parent, final Object javaObject, final Class<?> staticType, final boolean isAdapter) {
        this.parent = parent;
        this.javaObject = javaObject;
        this.staticType = staticType;
        this.isAdapter = isAdapter;
        this.initMembers();
    }
    
    public static boolean canConvert(final Object o, final Class<?> clazz) {
        return getConversionWeight(o, clazz) < 99;
    }
    
    private static Object coerceToNumber(final Class<?> clazz, final Object o) {
        final Class<?> class1 = o.getClass();
        if (clazz != Character.TYPE && clazz != ScriptRuntime.CharacterClass) {
            if (clazz != ScriptRuntime.ObjectClass && clazz != ScriptRuntime.DoubleClass && clazz != Double.TYPE) {
                if (clazz != ScriptRuntime.FloatClass && clazz != Float.TYPE) {
                    if (clazz != ScriptRuntime.IntegerClass && clazz != Integer.TYPE) {
                        if (clazz != ScriptRuntime.LongClass && clazz != Long.TYPE) {
                            if (clazz != ScriptRuntime.ShortClass && clazz != Short.TYPE) {
                                if (clazz != ScriptRuntime.ByteClass && clazz != Byte.TYPE) {
                                    return new Double(toDouble(o));
                                }
                                if (class1 == ScriptRuntime.ByteClass) {
                                    return o;
                                }
                                return (byte)toInteger(o, ScriptRuntime.ByteClass, -128.0, 127.0);
                            }
                            else {
                                if (class1 == ScriptRuntime.ShortClass) {
                                    return o;
                                }
                                return (short)toInteger(o, ScriptRuntime.ShortClass, -32768.0, 32767.0);
                            }
                        }
                        else {
                            if (class1 == ScriptRuntime.LongClass) {
                                return o;
                            }
                            return toInteger(o, ScriptRuntime.LongClass, Double.longBitsToDouble(-4332462841530417152L), Double.longBitsToDouble(4890909195324358655L));
                        }
                    }
                    else {
                        if (class1 == ScriptRuntime.IntegerClass) {
                            return o;
                        }
                        return (int)toInteger(o, ScriptRuntime.IntegerClass, -2.147483648E9, 2.147483647E9);
                    }
                }
                else {
                    if (class1 == ScriptRuntime.FloatClass) {
                        return o;
                    }
                    final double double1 = toDouble(o);
                    if (!Double.isInfinite(double1) && !Double.isNaN(double1)) {
                        double n = 0.0;
                        if (double1 != 0.0) {
                            final double abs = Math.abs(double1);
                            if (abs < 1.401298464324817E-45) {
                                if (double1 <= 0.0) {
                                    n = 0.0;
                                }
                                return new Float(n);
                            }
                            if (abs > 3.4028234663852886E38) {
                                float n2;
                                if (double1 > 0.0) {
                                    n2 = Float.POSITIVE_INFINITY;
                                }
                                else {
                                    n2 = Float.NEGATIVE_INFINITY;
                                }
                                return new Float(n2);
                            }
                            return new Float((float)double1);
                        }
                    }
                    return new Float((float)double1);
                }
            }
            else {
                if (class1 == ScriptRuntime.DoubleClass) {
                    return o;
                }
                return new Double(toDouble(o));
            }
        }
        else {
            if (class1 == ScriptRuntime.CharacterClass) {
                return o;
            }
            return (char)toInteger(o, ScriptRuntime.CharacterClass, 0.0, 65535.0);
        }
    }
    
    @Deprecated
    public static Object coerceType(final Class<?> clazz, final Object o) {
        return coerceTypeImpl(clazz, o);
    }
    
    static Object coerceTypeImpl(final Class<?> clazz, Object unwrap) {
        if (unwrap != null && unwrap.getClass() == clazz) {
            return unwrap;
        }
        final int jsTypeCode = getJSTypeCode(unwrap);
        int n = 0;
        switch (jsTypeCode) {
            default: {
                return unwrap;
            }
            case 8: {
                if (clazz == ScriptRuntime.StringClass) {
                    return ScriptRuntime.toString(unwrap);
                }
                if (clazz.isPrimitive()) {
                    if (clazz == Boolean.TYPE) {
                        reportConversionError(unwrap, clazz);
                    }
                    return coerceToNumber(clazz, unwrap);
                }
                if (clazz.isInstance(unwrap)) {
                    return unwrap;
                }
                if (clazz == ScriptRuntime.DateClass && unwrap instanceof NativeDate) {
                    return new Date((long)((NativeDate)unwrap).getJSTimeValue());
                }
                if (clazz.isArray() && unwrap instanceof NativeArray) {
                    final NativeArray nativeArray = (NativeArray)unwrap;
                    final long length = nativeArray.getLength();
                    final Class<?> componentType = clazz.getComponentType();
                    final Object instance = Array.newInstance(componentType, (int)length);
                    while (n < length) {
                        try {
                            Array.set(instance, n, coerceTypeImpl(componentType, nativeArray.get(n, nativeArray)));
                        }
                        catch (EvaluatorException ex) {
                            reportConversionError(unwrap, clazz);
                        }
                        ++n;
                    }
                    return instance;
                }
                if (unwrap instanceof Wrapper) {
                    unwrap = ((Wrapper)unwrap).unwrap();
                    if (clazz.isInstance(unwrap)) {
                        return unwrap;
                    }
                    reportConversionError(unwrap, clazz);
                    return unwrap;
                }
                else {
                    if (clazz.isInterface() && (unwrap instanceof NativeObject || unwrap instanceof NativeFunction)) {
                        return createInterfaceAdapter(clazz, (ScriptableObject)unwrap);
                    }
                    reportConversionError(unwrap, clazz);
                    return unwrap;
                }
                break;
            }
            case 6:
            case 7: {
                Object unwrap2 = unwrap;
                if (unwrap instanceof Wrapper) {
                    unwrap2 = ((Wrapper)unwrap).unwrap();
                }
                if (clazz.isPrimitive()) {
                    if (clazz == Boolean.TYPE) {
                        reportConversionError(unwrap2, clazz);
                    }
                    return coerceToNumber(clazz, unwrap2);
                }
                if (clazz == ScriptRuntime.StringClass) {
                    return unwrap2.toString();
                }
                if (clazz.isInstance(unwrap2)) {
                    return unwrap2;
                }
                reportConversionError(unwrap2, clazz);
                return unwrap2;
            }
            case 5: {
                Object unwrap3 = unwrap;
                if (unwrap instanceof Wrapper) {
                    unwrap3 = ((Wrapper)unwrap).unwrap();
                }
                if (clazz == ScriptRuntime.ClassClass) {
                    return unwrap3;
                }
                if (clazz == ScriptRuntime.ObjectClass) {
                    return unwrap3;
                }
                if (clazz == ScriptRuntime.StringClass) {
                    return unwrap3.toString();
                }
                reportConversionError(unwrap3, clazz);
                return unwrap3;
            }
            case 4: {
                if (clazz == ScriptRuntime.StringClass || clazz.isInstance(unwrap)) {
                    return unwrap.toString();
                }
                if (clazz != Character.TYPE && clazz != ScriptRuntime.CharacterClass) {
                    if ((clazz.isPrimitive() && clazz != Boolean.TYPE) || ScriptRuntime.NumberClass.isAssignableFrom(clazz)) {
                        return coerceToNumber(clazz, unwrap);
                    }
                    reportConversionError(unwrap, clazz);
                    return unwrap;
                }
                else {
                    if (((CharSequence)unwrap).length() == 1) {
                        return ((CharSequence)unwrap).charAt(0);
                    }
                    return coerceToNumber(clazz, unwrap);
                }
                break;
            }
            case 3: {
                if (clazz == ScriptRuntime.StringClass) {
                    return ScriptRuntime.toString(unwrap);
                }
                if (clazz == ScriptRuntime.ObjectClass) {
                    return coerceToNumber(Double.TYPE, unwrap);
                }
                if ((clazz.isPrimitive() && clazz != Boolean.TYPE) || ScriptRuntime.NumberClass.isAssignableFrom(clazz)) {
                    return coerceToNumber(clazz, unwrap);
                }
                reportConversionError(unwrap, clazz);
                return unwrap;
            }
            case 2: {
                if (clazz == Boolean.TYPE || clazz == ScriptRuntime.BooleanClass) {
                    return unwrap;
                }
                if (clazz == ScriptRuntime.ObjectClass) {
                    return unwrap;
                }
                if (clazz == ScriptRuntime.StringClass) {
                    return unwrap.toString();
                }
                reportConversionError(unwrap, clazz);
                return unwrap;
            }
            case 1: {
                if (clazz.isPrimitive()) {
                    reportConversionError(unwrap, clazz);
                }
                return null;
            }
            case 0: {
                if (clazz != ScriptRuntime.StringClass && clazz != ScriptRuntime.ObjectClass) {
                    reportConversionError("undefined", clazz);
                    return unwrap;
                }
                return "undefined";
            }
        }
    }
    
    protected static Object createInterfaceAdapter(final Class<?> clazz, final ScriptableObject scriptableObject) {
        final Object hashKeyFromPair = Kit.makeHashKeyFromPair(NativeJavaObject.COERCED_INTERFACE_KEY, clazz);
        final Object associatedValue = scriptableObject.getAssociatedValue(hashKeyFromPair);
        if (associatedValue != null) {
            return associatedValue;
        }
        return scriptableObject.associateValue(hashKeyFromPair, InterfaceAdapter.create(Context.getContext(), clazz, scriptableObject));
    }
    
    static int getConversionWeight(final Object o, final Class<?> clazz) {
        final int jsTypeCode = getJSTypeCode(o);
        switch (jsTypeCode) {
            default: {
                return 99;
            }
            case 8: {
                if (clazz != ScriptRuntime.ObjectClass && clazz.isInstance(o)) {
                    return 1;
                }
                if (clazz.isArray()) {
                    if (o instanceof NativeArray) {
                        return 2;
                    }
                    break;
                }
                else {
                    if (clazz == ScriptRuntime.ObjectClass) {
                        return 3;
                    }
                    if (clazz == ScriptRuntime.StringClass) {
                        return 4;
                    }
                    if (clazz == ScriptRuntime.DateClass) {
                        if (o instanceof NativeDate) {
                            return 1;
                        }
                        break;
                    }
                    else if (clazz.isInterface()) {
                        if (o instanceof NativeObject) {
                            return 1;
                        }
                        if (o instanceof NativeFunction) {
                            return 1;
                        }
                        return 12;
                    }
                    else {
                        if (clazz.isPrimitive() && clazz != Boolean.TYPE) {
                            return getSizeRank(clazz) + 4;
                        }
                        break;
                    }
                }
                break;
            }
            case 6:
            case 7: {
                Object unwrap = o;
                if (o instanceof Wrapper) {
                    unwrap = ((Wrapper)o).unwrap();
                }
                if (clazz.isInstance(unwrap)) {
                    return 0;
                }
                if (clazz == ScriptRuntime.StringClass) {
                    return 2;
                }
                if (!clazz.isPrimitive() || clazz == Boolean.TYPE) {
                    break;
                }
                if (jsTypeCode == 7) {
                    return 99;
                }
                return getSizeRank(clazz) + 2;
            }
            case 5: {
                if (clazz == ScriptRuntime.ClassClass) {
                    return 1;
                }
                if (clazz == ScriptRuntime.ObjectClass) {
                    return 3;
                }
                if (clazz == ScriptRuntime.StringClass) {
                    return 4;
                }
                break;
            }
            case 4: {
                if (clazz == ScriptRuntime.StringClass) {
                    return 1;
                }
                if (clazz.isInstance(o)) {
                    return 2;
                }
                if (!clazz.isPrimitive()) {
                    break;
                }
                if (clazz == Character.TYPE) {
                    return 3;
                }
                if (clazz != Boolean.TYPE) {
                    return 4;
                }
                break;
            }
            case 3: {
                if (clazz.isPrimitive()) {
                    if (clazz == Double.TYPE) {
                        return 1;
                    }
                    if (clazz != Boolean.TYPE) {
                        return getSizeRank(clazz) + 1;
                    }
                    break;
                }
                else {
                    if (clazz == ScriptRuntime.StringClass) {
                        return 9;
                    }
                    if (clazz == ScriptRuntime.ObjectClass) {
                        return 10;
                    }
                    if (ScriptRuntime.NumberClass.isAssignableFrom(clazz)) {
                        return 2;
                    }
                    break;
                }
                break;
            }
            case 2: {
                if (clazz == Boolean.TYPE) {
                    return 1;
                }
                if (clazz == ScriptRuntime.BooleanClass) {
                    return 2;
                }
                if (clazz == ScriptRuntime.ObjectClass) {
                    return 3;
                }
                if (clazz == ScriptRuntime.StringClass) {
                    return 4;
                }
                break;
            }
            case 1: {
                if (!clazz.isPrimitive()) {
                    return 1;
                }
                break;
            }
            case 0: {
                if (clazz == ScriptRuntime.StringClass || clazz == ScriptRuntime.ObjectClass) {
                    return 1;
                }
                break;
            }
        }
        return 99;
    }
    
    private static int getJSTypeCode(final Object o) {
        if (o == null) {
            return 1;
        }
        if (o == Undefined.instance) {
            return 0;
        }
        if (o instanceof CharSequence) {
            return 4;
        }
        if (o instanceof Number) {
            return 3;
        }
        if (o instanceof Boolean) {
            return 2;
        }
        if (o instanceof Scriptable) {
            if (o instanceof NativeJavaClass) {
                return 5;
            }
            if (o instanceof NativeJavaArray) {
                return 7;
            }
            if (o instanceof Wrapper) {
                return 6;
            }
            return 8;
        }
        else {
            if (o instanceof Class) {
                return 5;
            }
            if (o.getClass().isArray()) {
                return 7;
            }
            return 6;
        }
    }
    
    static int getSizeRank(final Class<?> clazz) {
        if (clazz == Double.TYPE) {
            return 1;
        }
        if (clazz == Float.TYPE) {
            return 2;
        }
        if (clazz == Long.TYPE) {
            return 3;
        }
        if (clazz == Integer.TYPE) {
            return 4;
        }
        if (clazz == Short.TYPE) {
            return 5;
        }
        if (clazz == Character.TYPE) {
            return 6;
        }
        if (clazz == Byte.TYPE) {
            return 7;
        }
        if (clazz == Boolean.TYPE) {
            return 99;
        }
        return 8;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.isAdapter = objectInputStream.readBoolean();
        Label_0076: {
            if (this.isAdapter) {
                if (NativeJavaObject.adapter_readAdapterObject == null) {
                    throw new ClassNotFoundException();
                }
                try {
                    this.javaObject = NativeJavaObject.adapter_readAdapterObject.invoke(null, this, objectInputStream);
                    break Label_0076;
                }
                catch (Exception ex) {
                    throw new IOException();
                }
            }
            this.javaObject = objectInputStream.readObject();
        }
        final String s = (String)objectInputStream.readObject();
        if (s != null) {
            this.staticType = Class.forName(s);
        }
        else {
            this.staticType = null;
        }
        this.initMembers();
    }
    
    static void reportConversionError(final Object o, final Class<?> clazz) {
        throw Context.reportRuntimeError2("msg.conversion.not.allowed", String.valueOf(o), JavaMembers.javaSignature(clazz));
    }
    
    private static double toDouble(final Object o) {
        if (o instanceof Number) {
            return ((Number)o).doubleValue();
        }
        if (o instanceof String) {
            return ScriptRuntime.toNumber((String)o);
        }
        if (!(o instanceof Scriptable)) {
            Method method;
            try {
                method = o.getClass().getMethod("doubleValue", (Class<?>[])null);
            }
            catch (SecurityException ex) {
                method = null;
            }
            catch (NoSuchMethodException ex2) {
                method = null;
            }
            if (method != null) {
                try {
                    return ((Number)method.invoke(o, (Object[])null)).doubleValue();
                }
                catch (InvocationTargetException ex3) {
                    reportConversionError(o, Double.TYPE);
                }
                catch (IllegalAccessException ex4) {
                    reportConversionError(o, Double.TYPE);
                }
            }
            return ScriptRuntime.toNumber(o.toString());
        }
        if (o instanceof Wrapper) {
            return toDouble(((Wrapper)o).unwrap());
        }
        return ScriptRuntime.toNumber(o);
    }
    
    private static long toInteger(final Object o, final Class<?> clazz, final double n, final double n2) {
        final double double1 = toDouble(o);
        if (Double.isInfinite(double1) || Double.isNaN(double1)) {
            reportConversionError(ScriptRuntime.toString(o), clazz);
        }
        double n3;
        if (double1 > 0.0) {
            n3 = Math.floor(double1);
        }
        else {
            n3 = Math.ceil(double1);
        }
        if (n3 < n || n3 > n2) {
            reportConversionError(ScriptRuntime.toString(o), clazz);
        }
        return (long)n3;
    }
    
    @Deprecated
    public static Object wrap(final Scriptable scriptable, final Object o, final Class<?> clazz) {
        final Context context = Context.getContext();
        return context.getWrapFactory().wrap(context, scriptable, o, clazz);
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeBoolean(this.isAdapter);
        Label_0078: {
            if (this.isAdapter) {
                if (NativeJavaObject.adapter_writeAdapterObject == null) {
                    throw new IOException();
                }
                final Object javaObject = this.javaObject;
                try {
                    NativeJavaObject.adapter_writeAdapterObject.invoke(null, javaObject, objectOutputStream);
                    break Label_0078;
                }
                catch (Exception ex) {
                    throw new IOException();
                }
            }
            objectOutputStream.writeObject(this.javaObject);
        }
        if (this.staticType != null) {
            objectOutputStream.writeObject(this.staticType.getClass().getName());
            return;
        }
        objectOutputStream.writeObject(null);
    }
    
    @Override
    public void delete(final int n) {
    }
    
    @Override
    public void delete(final String s) {
    }
    
    @Override
    public Object get(final int n, final Scriptable scriptable) {
        throw this.members.reportMemberNotFound(Integer.toString(n));
    }
    
    @Override
    public Object get(final String s, final Scriptable scriptable) {
        if (this.fieldAndMethods != null) {
            final FieldAndMethods value = this.fieldAndMethods.get(s);
            if (value != null) {
                return value;
            }
        }
        return this.members.get(this, s, this.javaObject, false);
    }
    
    @Override
    public String getClassName() {
        return "JavaObject";
    }
    
    @Override
    public Object getDefaultValue(final Class<?> clazz) {
        Class<?> booleanClass = clazz;
        if (clazz == null) {
            booleanClass = clazz;
            if (this.javaObject instanceof Boolean) {
                booleanClass = ScriptRuntime.BooleanClass;
            }
        }
        Object o;
        if (booleanClass != null && booleanClass != ScriptRuntime.StringClass) {
            String s;
            if (booleanClass == ScriptRuntime.BooleanClass) {
                s = "booleanValue";
            }
            else {
                if (booleanClass != ScriptRuntime.NumberClass) {
                    throw Context.reportRuntimeError0("msg.default.value");
                }
                s = "doubleValue";
            }
            final Object value = this.get(s, this);
            if (value instanceof Function) {
                final Function function = (Function)value;
                o = function.call(Context.getContext(), function.getParentScope(), this, ScriptRuntime.emptyArgs);
            }
            else if (booleanClass == ScriptRuntime.NumberClass && this.javaObject instanceof Boolean) {
                double n;
                if (this.javaObject) {
                    n = 1.0;
                }
                else {
                    n = 0.0;
                }
                o = ScriptRuntime.wrapNumber(n);
            }
            else {
                o = this.javaObject.toString();
            }
        }
        else {
            o = this.javaObject.toString();
        }
        return o;
    }
    
    @Override
    public Object[] getIds() {
        return this.members.getIds(false);
    }
    
    @Override
    public Scriptable getParentScope() {
        return this.parent;
    }
    
    @Override
    public Scriptable getPrototype() {
        if (this.prototype == null && this.javaObject instanceof String) {
            return TopLevel.getBuiltinPrototype(ScriptableObject.getTopLevelScope(this.parent), TopLevel.Builtins.String);
        }
        return this.prototype;
    }
    
    @Override
    public boolean has(final int n, final Scriptable scriptable) {
        return false;
    }
    
    @Override
    public boolean has(final String s, final Scriptable scriptable) {
        return this.members.has(s, false);
    }
    
    @Override
    public boolean hasInstance(final Scriptable scriptable) {
        return false;
    }
    
    protected void initMembers() {
        Class<?> clazz;
        if (this.javaObject != null) {
            clazz = this.javaObject.getClass();
        }
        else {
            clazz = this.staticType;
        }
        this.members = JavaMembers.lookupClass(this.parent, clazz, this.staticType, this.isAdapter);
        this.fieldAndMethods = this.members.getFieldAndMethodsObjects(this, this.javaObject, false);
    }
    
    @Override
    public void put(final int n, final Scriptable scriptable, final Object o) {
        throw this.members.reportMemberNotFound(Integer.toString(n));
    }
    
    @Override
    public void put(final String s, final Scriptable scriptable, final Object o) {
        if (this.prototype != null && !this.members.has(s, false)) {
            this.prototype.put(s, this.prototype, o);
            return;
        }
        this.members.put(this, s, this.javaObject, o, false);
    }
    
    @Override
    public void setParentScope(final Scriptable parent) {
        this.parent = parent;
    }
    
    @Override
    public void setPrototype(final Scriptable prototype) {
        this.prototype = prototype;
    }
    
    @Override
    public Object unwrap() {
        return this.javaObject;
    }
}
