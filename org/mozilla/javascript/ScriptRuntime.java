package org.mozilla.javascript;

import org.mozilla.javascript.xml.*;
import org.mozilla.javascript.v8dtoa.*;
import com.zhekasmirnov.innercore.utils.*;
import java.text.*;
import java.io.*;
import java.util.*;

public class ScriptRuntime
{
    public static final Class<?> BooleanClass;
    public static final Class<?> ByteClass;
    public static final Class<?> CharacterClass;
    public static final Class<?> ClassClass;
    public static final Class<?> ContextClass;
    public static final Class<?> ContextFactoryClass;
    private static final String DEFAULT_NS_TAG = "__default_namespace__";
    public static final Class<?> DateClass;
    public static final Class<?> DoubleClass;
    public static final int ENUMERATE_ARRAY = 2;
    public static final int ENUMERATE_ARRAY_NO_ITERATOR = 5;
    public static final int ENUMERATE_KEYS = 0;
    public static final int ENUMERATE_KEYS_NO_ITERATOR = 3;
    public static final int ENUMERATE_VALUES = 1;
    public static final int ENUMERATE_VALUES_NO_ITERATOR = 4;
    public static final Class<?> FloatClass;
    public static final Class<?> FunctionClass;
    public static final Class<?> IntegerClass;
    private static final Object LIBRARY_SCOPE_KEY;
    public static final Class<?> LongClass;
    public static final double NaN;
    public static final Double NaNobj;
    public static final Class<?> NumberClass;
    public static final Class<?> ObjectClass;
    public static Locale ROOT_LOCALE;
    public static final Class<Scriptable> ScriptableClass;
    public static final Class<?> ScriptableObjectClass;
    public static final Class<?> ShortClass;
    public static final Class<?> StringClass;
    public static final Object[] emptyArgs;
    public static final String[] emptyStrings;
    public static MessageProvider messageProvider;
    public static final double negativeZero;
    
    static {
        BooleanClass = Kit.classOrNull("java.lang.Boolean");
        ByteClass = Kit.classOrNull("java.lang.Byte");
        CharacterClass = Kit.classOrNull("java.lang.Character");
        ClassClass = Kit.classOrNull("java.lang.Class");
        DoubleClass = Kit.classOrNull("java.lang.Double");
        FloatClass = Kit.classOrNull("java.lang.Float");
        IntegerClass = Kit.classOrNull("java.lang.Integer");
        LongClass = Kit.classOrNull("java.lang.Long");
        NumberClass = Kit.classOrNull("java.lang.Number");
        ObjectClass = Kit.classOrNull("java.lang.Object");
        ShortClass = Kit.classOrNull("java.lang.Short");
        StringClass = Kit.classOrNull("java.lang.String");
        DateClass = Kit.classOrNull("java.util.Date");
        ContextClass = Kit.classOrNull("org.mozilla.javascript.Context");
        ContextFactoryClass = Kit.classOrNull("org.mozilla.javascript.ContextFactory");
        FunctionClass = Kit.classOrNull("org.mozilla.javascript.Function");
        ScriptableObjectClass = Kit.classOrNull("org.mozilla.javascript.ScriptableObject");
        ScriptableClass = Scriptable.class;
        ScriptRuntime.ROOT_LOCALE = new Locale("");
        LIBRARY_SCOPE_KEY = "LIBRARY_SCOPE";
        NaN = Double.longBitsToDouble(9221120237041090560L);
        negativeZero = Double.longBitsToDouble(Long.MIN_VALUE);
        NaNobj = new Double(ScriptRuntime.NaN);
        ScriptRuntime.messageProvider = (MessageProvider)new DefaultMessageProvider();
        emptyArgs = new Object[0];
        emptyStrings = new String[0];
    }
    
    protected ScriptRuntime() {
    }
    
    public static CharSequence add(final CharSequence charSequence, final Object o) {
        return new ConsString(charSequence, toCharSequence(o));
    }
    
    public static CharSequence add(final Object o, final CharSequence charSequence) {
        return new ConsString(toCharSequence(o), charSequence);
    }
    
    public static Object add(Object defaultValue, final Object o, final Context context) {
        if (defaultValue instanceof Number && o instanceof Number) {
            return wrapNumber(((Number)defaultValue).doubleValue() + ((Number)o).doubleValue());
        }
        if (defaultValue instanceof XMLObject) {
            final Object addValues = ((XMLObject)defaultValue).addValues(context, true, o);
            if (addValues != Scriptable.NOT_FOUND) {
                return addValues;
            }
        }
        if (o instanceof XMLObject) {
            final Object addValues2 = ((XMLObject)o).addValues(context, false, defaultValue);
            if (addValues2 != Scriptable.NOT_FOUND) {
                return addValues2;
            }
        }
        Object defaultValue2 = defaultValue;
        if (defaultValue instanceof Scriptable) {
            defaultValue2 = ((Scriptable)defaultValue).getDefaultValue(null);
        }
        defaultValue = o;
        if (o instanceof Scriptable) {
            defaultValue = ((Scriptable)o).getDefaultValue(null);
        }
        if (defaultValue2 instanceof CharSequence || defaultValue instanceof CharSequence) {
            return new ConsString(toCharSequence(defaultValue2), toCharSequence(defaultValue));
        }
        if (defaultValue2 instanceof Number && defaultValue instanceof Number) {
            return wrapNumber(((Number)defaultValue2).doubleValue() + ((Number)defaultValue).doubleValue());
        }
        return wrapNumber(toNumber(defaultValue2) + toNumber(defaultValue));
    }
    
    public static void addInstructionCount(final Context context, final int n) {
        context.instructionCount += n;
        if (context.instructionCount > context.instructionThreshold) {
            context.observeInstructionCount(context.instructionCount);
            context.instructionCount = 0;
        }
    }
    
    public static Object applyOrCall(final boolean b, final Context context, final Scriptable scriptable, Scriptable objectOrNull, final Object[] array) {
        final int length = array.length;
        final Callable callable = getCallable(objectOrNull);
        objectOrNull = null;
        if (length != 0) {
            objectOrNull = toObjectOrNull(context, array[0], scriptable);
        }
        Scriptable topCallScope;
        if ((topCallScope = objectOrNull) == null) {
            topCallScope = getTopCallScope(context);
        }
        Object[] array2;
        if (b) {
            if (length <= 1) {
                array2 = ScriptRuntime.emptyArgs;
            }
            else {
                array2 = getApplyArguments(context, array[1]);
            }
        }
        else if (length <= 1) {
            array2 = ScriptRuntime.emptyArgs;
        }
        else {
            array2 = new Object[length - 1];
            System.arraycopy(array, 1, array2, 0, length - 1);
        }
        return callable.call(context, scriptable, topCallScope, array2);
    }
    
    public static Scriptable bind(final Context context, Scriptable scriptable, final String s) {
        Scriptable scriptable2 = scriptable.getParentScope();
        XMLObject xmlObject5 = null;
        Label_0174: {
            if (scriptable2 != null) {
                final XMLObject xmlObject = null;
                Scriptable scriptable3 = scriptable;
                XMLObject xmlObject2 = xmlObject;
                Scriptable parentScope;
                Scriptable scriptable4;
                while (true) {
                    parentScope = scriptable2;
                    scriptable4 = scriptable3;
                    if (!(scriptable3 instanceof NativeWith)) {
                        break;
                    }
                    final Scriptable prototype = scriptable3.getPrototype();
                    if (prototype instanceof XMLObject) {
                        final XMLObject xmlObject3 = (XMLObject)prototype;
                        if (xmlObject3.has(context, s)) {
                            return xmlObject3;
                        }
                        XMLObject xmlObject4;
                        if ((xmlObject4 = xmlObject2) == null) {
                            xmlObject4 = xmlObject3;
                        }
                        xmlObject2 = xmlObject4;
                    }
                    else if (ScriptableObject.hasProperty(prototype, s)) {
                        return prototype;
                    }
                    scriptable3 = scriptable2;
                    scriptable2 = scriptable2.getParentScope();
                    if (scriptable2 == null) {
                        xmlObject5 = xmlObject2;
                        scriptable = scriptable3;
                        break Label_0174;
                    }
                }
                while (!ScriptableObject.hasProperty(scriptable4, s)) {
                    final Scriptable scriptable5 = parentScope;
                    final Scriptable scriptable6 = parentScope = parentScope.getParentScope();
                    scriptable4 = scriptable5;
                    if (scriptable6 == null) {
                        xmlObject5 = xmlObject2;
                        scriptable = scriptable5;
                        break Label_0174;
                    }
                }
                return scriptable4;
            }
            xmlObject5 = null;
        }
        Scriptable checkDynamicScope = scriptable;
        if (context.useDynamicScope) {
            checkDynamicScope = checkDynamicScope(context.topCallScope, scriptable);
        }
        if (ScriptableObject.hasProperty(checkDynamicScope, s)) {
            return checkDynamicScope;
        }
        return xmlObject5;
    }
    
    @Deprecated
    public static Object call(final Context context, final Object o, final Object o2, final Object[] array, final Scriptable scriptable) {
        if (!(o instanceof Function)) {
            throw notFunctionError(toString(o));
        }
        final Function function = (Function)o;
        final Scriptable objectOrNull = toObjectOrNull(context, o2, scriptable);
        if (objectOrNull == null) {
            throw undefCallError(objectOrNull, "function");
        }
        return function.call(context, scriptable, objectOrNull, array);
    }
    
    public static Ref callRef(final Callable callable, final Scriptable scriptable, final Object[] array, final Context context) {
        if (!(callable instanceof RefCallable)) {
            throw constructError("ReferenceError", getMessage1("msg.no.ref.from.function", toString(callable)));
        }
        final RefCallable refCallable = (RefCallable)callable;
        final Ref refCall = refCallable.refCall(context, scriptable, array);
        if (refCall == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(refCallable.getClass().getName());
            sb.append(".refCall() returned null");
            throw new IllegalStateException(sb.toString());
        }
        return refCall;
    }
    
    public static Object callSpecial(final Context context, final Callable callable, final Scriptable scriptable, final Object[] array, final Scriptable scriptable2, final Scriptable scriptable3, final int n, final String s, final int n2) {
        if (n == 1) {
            if (scriptable.getParentScope() == null && NativeGlobal.isEvalFunction(callable)) {
                return evalSpecial(context, scriptable2, scriptable3, array, s, n2);
            }
        }
        else {
            if (n != 2) {
                throw Kit.codeBug();
            }
            if (NativeWith.isWithFunction(callable)) {
                throw Context.reportRuntimeError1("msg.only.from.new", "With");
            }
        }
        return callable.call(context, scriptable2, scriptable, array);
    }
    
    static void checkDeprecated(final Context context, final String s) {
        final int languageVersion = context.getLanguageVersion();
        if (languageVersion >= 140 || languageVersion == 0) {
            final String message1 = getMessage1("msg.deprec.ctor", s);
            if (languageVersion != 0) {
                throw Context.reportRuntimeError(message1);
            }
            Context.reportWarning(message1);
        }
    }
    
    static Scriptable checkDynamicScope(final Scriptable scriptable, final Scriptable scriptable2) {
        if (scriptable == scriptable2) {
            return scriptable;
        }
        Scriptable scriptable3 = scriptable;
        Scriptable prototype;
        do {
            prototype = scriptable3.getPrototype();
            if (prototype == scriptable2) {
                return scriptable;
            }
        } while ((scriptable3 = prototype) != null);
        return scriptable2;
    }
    
    public static RegExpProxy checkRegExpProxy(final Context context) {
        final RegExpProxy regExpProxy = getRegExpProxy(context);
        if (regExpProxy == null) {
            throw Context.reportRuntimeError0("msg.no.regexp");
        }
        return regExpProxy;
    }
    
    public static boolean cmp_LE(Object defaultValue, final Object o) {
        final boolean b = defaultValue instanceof Number;
        final boolean b2 = false;
        boolean b3 = false;
        double n;
        double n2;
        if (b && o instanceof Number) {
            n = ((Number)defaultValue).doubleValue();
            n2 = ((Number)o).doubleValue();
        }
        else {
            Object defaultValue2 = defaultValue;
            if (defaultValue instanceof Scriptable) {
                defaultValue2 = ((Scriptable)defaultValue).getDefaultValue(ScriptRuntime.NumberClass);
            }
            defaultValue = o;
            if (o instanceof Scriptable) {
                defaultValue = ((Scriptable)o).getDefaultValue(ScriptRuntime.NumberClass);
            }
            if (defaultValue2 instanceof CharSequence && defaultValue instanceof CharSequence) {
                if (defaultValue2.toString().compareTo(defaultValue.toString()) <= 0) {
                    b3 = true;
                }
                return b3;
            }
            n = toNumber(defaultValue2);
            n2 = toNumber(defaultValue);
        }
        boolean b4 = b2;
        if (n <= n2) {
            b4 = true;
        }
        return b4;
    }
    
    public static boolean cmp_LT(Object defaultValue, final Object o) {
        final boolean b = defaultValue instanceof Number;
        final boolean b2 = false;
        boolean b3 = false;
        double n;
        double n2;
        if (b && o instanceof Number) {
            n = ((Number)defaultValue).doubleValue();
            n2 = ((Number)o).doubleValue();
        }
        else {
            Object defaultValue2 = defaultValue;
            if (defaultValue instanceof Scriptable) {
                defaultValue2 = ((Scriptable)defaultValue).getDefaultValue(ScriptRuntime.NumberClass);
            }
            defaultValue = o;
            if (o instanceof Scriptable) {
                defaultValue = ((Scriptable)o).getDefaultValue(ScriptRuntime.NumberClass);
            }
            if (defaultValue2 instanceof CharSequence && defaultValue instanceof CharSequence) {
                if (defaultValue2.toString().compareTo(defaultValue.toString()) < 0) {
                    b3 = true;
                }
                return b3;
            }
            n = toNumber(defaultValue2);
            n2 = toNumber(defaultValue);
        }
        boolean b4 = b2;
        if (n < n2) {
            b4 = true;
        }
        return b4;
    }
    
    public static EcmaError constructError(final String s, final String s2) {
        final int[] array = { 0 };
        return constructError(s, s2, Context.getSourcePositionFromStack(array), array[0], null, 0);
    }
    
    public static EcmaError constructError(final String s, final String s2, final int n) {
        final int[] array = { 0 };
        final String sourcePositionFromStack = Context.getSourcePositionFromStack(array);
        if (array[0] != 0) {
            array[0] += n;
        }
        return constructError(s, s2, sourcePositionFromStack, array[0], null, 0);
    }
    
    public static EcmaError constructError(final String s, final String s2, final String s3, final int n, final String s4, final int n2) {
        return new EcmaError(s, s2, s3, n, s4, n2);
    }
    
    public static Scriptable createFunctionActivation(final NativeFunction nativeFunction, final Scriptable scriptable, final Object[] array) {
        return new NativeCall(nativeFunction, scriptable, array);
    }
    
    private static XMLLib currentXMLLib(final Context context) {
        if (context.topCallScope == null) {
            throw new IllegalStateException();
        }
        XMLLib cachedXMLLib;
        if ((cachedXMLLib = context.cachedXMLLib) == null) {
            cachedXMLLib = XMLLib.extractFromScope(context.topCallScope);
            if (cachedXMLLib == null) {
                throw new IllegalStateException();
            }
            context.cachedXMLLib = cachedXMLLib;
        }
        return cachedXMLLib;
    }
    
    static String defaultObjectToSource(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        boolean b;
        boolean has;
        if (context.iterating == null) {
            b = true;
            has = false;
            context.iterating = new ObjToIntMap(31);
        }
        else {
            b = false;
            has = context.iterating.has(scriptable2);
        }
        final StringBuilder sb = new StringBuilder(128);
        if (b) {
            sb.append("(");
        }
        sb.append('{');
        if (!has) {
            try {
                context.iterating.intern(scriptable2);
                final Object[] ids = scriptable2.getIds();
                for (int i = 0; i < ids.length; ++i) {
                    final Object o = ids[i];
                    Object o2;
                    if (o instanceof Integer) {
                        final int intValue = (int)o;
                        o2 = scriptable2.get(intValue, scriptable2);
                        if (o2 == Scriptable.NOT_FOUND) {
                            continue;
                        }
                        if (i > 0) {
                            sb.append(", ");
                        }
                        sb.append(intValue);
                    }
                    else {
                        final String s = (String)o;
                        o2 = scriptable2.get(s, scriptable2);
                        if (o2 == Scriptable.NOT_FOUND) {
                            continue;
                        }
                        if (i > 0) {
                            sb.append(", ");
                        }
                        if (isValidIdentifierName(s)) {
                            sb.append(s);
                        }
                        else {
                            sb.append('\'');
                            sb.append(escapeString(s, '\''));
                            sb.append('\'');
                        }
                    }
                    sb.append(':');
                    sb.append(uneval(context, scriptable, o2));
                }
            }
            finally {
                if (b) {
                    context.iterating = null;
                }
            }
        }
        if (b) {
            context.iterating = null;
        }
        sb.append('}');
        if (b) {
            sb.append(')');
        }
        return sb.toString();
    }
    
    static String defaultObjectToString(final Scriptable scriptable) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[object ");
        sb.append(scriptable.getClassName());
        sb.append(']');
        return sb.toString();
    }
    
    @Deprecated
    public static Object delete(final Object o, final Object o2, final Context context) {
        return delete(o, o2, context, false);
    }
    
    public static Object delete(final Object o, final Object o2, final Context context, Scriptable objectOrNull, final boolean b) {
        objectOrNull = toObjectOrNull(context, o, objectOrNull);
        if (objectOrNull != null) {
            return wrapBoolean(deleteObjectElem(objectOrNull, o2, context));
        }
        if (b) {
            return Boolean.TRUE;
        }
        throw undefDeleteError(o, o2);
    }
    
    @Deprecated
    public static Object delete(final Object o, final Object o2, final Context context, final boolean b) {
        return delete(o, o2, context, getTopCallScope(context), b);
    }
    
    public static boolean deleteObjectElem(final Scriptable scriptable, final Object o, final Context context) {
        final String stringIdOrIndex = toStringIdOrIndex(context, o);
        if (stringIdOrIndex == null) {
            final int lastIndexResult = lastIndexResult(context);
            scriptable.delete(lastIndexResult);
            return scriptable.has(lastIndexResult, scriptable) ^ true;
        }
        scriptable.delete(stringIdOrIndex);
        return scriptable.has(stringIdOrIndex, scriptable) ^ true;
    }
    
    private static Object doScriptableIncrDecr(final Scriptable scriptable, final String s, final Scriptable scriptable2, Object wrapNumber, final int n) {
        final boolean b = (n & 0x2) != 0x0;
        double n2;
        if (wrapNumber instanceof Number) {
            n2 = ((Number)wrapNumber).doubleValue();
        }
        else {
            final double n3 = n2 = toNumber(wrapNumber);
            if (b) {
                wrapNumber = wrapNumber(n3);
                n2 = n3;
            }
        }
        double n4;
        if ((n & 0x1) == 0x0) {
            n4 = n2 + 1.0;
        }
        else {
            n4 = n2 - 1.0;
        }
        final Number wrapNumber2 = wrapNumber(n4);
        scriptable.put(s, scriptable2, wrapNumber2);
        if (b) {
            return wrapNumber;
        }
        return wrapNumber2;
    }
    
    public static Object doTopCall(final Callable callable, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (scriptable == null) {
            throw new IllegalArgumentException();
        }
        if (context.topCallScope != null) {
            throw new IllegalStateException();
        }
        context.topCallScope = ScriptableObject.getTopLevelScope(scriptable);
        context.useDynamicScope = context.hasFeature(7);
        final ContextFactory factory = context.getFactory();
        try {
            return factory.doTopCall(callable, context, scriptable, scriptable2, array);
        }
        finally {
            context.topCallScope = null;
            context.cachedXMLLib = null;
            if (context.currentActivationCall != null) {
                throw new IllegalStateException();
            }
        }
    }
    
    @Deprecated
    public static Object elemIncrDecr(final Object o, final Object o2, final Context context, final int n) {
        return elemIncrDecr(o, o2, context, getTopCallScope(context), n);
    }
    
    public static Object elemIncrDecr(final Object o, final Object o2, final Context context, final Scriptable scriptable, final int n) {
        Object o3 = getObjectElem(o, o2, context, scriptable);
        final boolean b = (n & 0x2) != 0x0;
        double n2;
        if (o3 instanceof Number) {
            n2 = ((Number)o3).doubleValue();
        }
        else {
            final double n3 = n2 = toNumber(o3);
            if (b) {
                o3 = wrapNumber(n3);
                n2 = n3;
            }
        }
        double n4;
        if ((n & 0x1) == 0x0) {
            n4 = n2 + 1.0;
        }
        else {
            n4 = n2 - 1.0;
        }
        final Number wrapNumber = wrapNumber(n4);
        setObjectElem(o, o2, wrapNumber, context, scriptable);
        if (b) {
            return o3;
        }
        return wrapNumber;
    }
    
    public static void enterActivationFunction(final Context context, final Scriptable scriptable) {
        if (context.topCallScope == null) {
            throw new IllegalStateException();
        }
        final NativeCall currentActivationCall = (NativeCall)scriptable;
        currentActivationCall.parentActivationCall = context.currentActivationCall;
        context.currentActivationCall = currentActivationCall;
    }
    
    public static Scriptable enterDotQuery(final Object o, final Scriptable scriptable) {
        if (!(o instanceof XMLObject)) {
            throw notXmlError(o);
        }
        return ((XMLObject)o).enterDotQuery(scriptable);
    }
    
    public static Scriptable enterWith(final Object o, final Context context, final Scriptable scriptable) {
        final Scriptable objectOrNull = toObjectOrNull(context, o, scriptable);
        if (objectOrNull == null) {
            throw typeError1("msg.undef.with", toString(o));
        }
        if (objectOrNull instanceof XMLObject) {
            return ((XMLObject)objectOrNull).enterWith(scriptable);
        }
        return new NativeWith(scriptable, objectOrNull);
    }
    
    private static void enumChangeObject(final IdEnumeration idEnumeration) {
        Object[] ids = null;
        while (idEnumeration.obj != null) {
            ids = idEnumeration.obj.getIds();
            if (ids.length != 0) {
                break;
            }
            idEnumeration.obj = idEnumeration.obj.getPrototype();
        }
        if (idEnumeration.obj != null && idEnumeration.ids != null) {
            final Object[] ids2 = idEnumeration.ids;
            final int length = ids2.length;
            if (idEnumeration.used == null) {
                idEnumeration.used = new ObjToIntMap(length);
            }
            for (int i = 0; i != length; ++i) {
                idEnumeration.used.intern(ids2[i]);
            }
        }
        idEnumeration.ids = ids;
        idEnumeration.index = 0;
    }
    
    public static Object enumId(Object enumValue, final Context context) {
        final IdEnumeration idEnumeration = (IdEnumeration)enumValue;
        if (idEnumeration.iterator != null) {
            return idEnumeration.currentId;
        }
        switch (idEnumeration.enumType) {
            default: {
                throw Kit.codeBug();
            }
            case 2:
            case 5: {
                final Object currentId = idEnumeration.currentId;
                enumValue = enumValue(enumValue, context);
                return context.newArray(ScriptableObject.getTopLevelScope(idEnumeration.obj), new Object[] { currentId, enumValue });
            }
            case 1:
            case 4: {
                return enumValue(enumValue, context);
            }
            case 0:
            case 3: {
                return idEnumeration.currentId;
            }
        }
    }
    
    @Deprecated
    public static Object enumInit(final Object o, final Context context, final int n) {
        return enumInit(o, context, getTopCallScope(context), n);
    }
    
    public static Object enumInit(final Object o, final Context context, Scriptable obj, final int enumType) {
        final IdEnumeration idEnumeration = new IdEnumeration();
        idEnumeration.obj = toObjectOrNull(context, o, obj);
        if (idEnumeration.obj == null) {
            return idEnumeration;
        }
        idEnumeration.enumType = enumType;
        idEnumeration.iterator = null;
        if (enumType != 3 && enumType != 4 && enumType != 5) {
            final Scriptable parentScope = idEnumeration.obj.getParentScope();
            obj = idEnumeration.obj;
            idEnumeration.iterator = toIterator(context, parentScope, obj, enumType == 0);
        }
        if (idEnumeration.iterator == null) {
            enumChangeObject(idEnumeration);
        }
        return idEnumeration;
    }
    
    @Deprecated
    public static Object enumInit(final Object o, final Context context, final boolean b) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    public static Boolean enumNext(Object property) {
        final IdEnumeration idEnumeration = (IdEnumeration)property;
        if (idEnumeration.iterator != null) {
            property = ScriptableObject.getProperty(idEnumeration.iterator, "next");
            if (!(property instanceof Callable)) {
                return Boolean.FALSE;
            }
            final Callable callable = (Callable)property;
            final Context context = Context.getContext();
            try {
                idEnumeration.currentId = callable.call(context, idEnumeration.iterator.getParentScope(), idEnumeration.iterator, ScriptRuntime.emptyArgs);
                return Boolean.TRUE;
            }
            catch (JavaScriptException ex) {
                if (ex.getValue() instanceof NativeIterator.StopIteration) {
                    return Boolean.FALSE;
                }
                throw ex;
            }
        }
        while (idEnumeration.obj != null) {
            if (idEnumeration.index == idEnumeration.ids.length) {
                idEnumeration.obj = idEnumeration.obj.getPrototype();
                enumChangeObject(idEnumeration);
            }
            else {
                property = idEnumeration.ids[idEnumeration.index++];
                if (idEnumeration.used != null && idEnumeration.used.has(property)) {
                    continue;
                }
                if (property instanceof String) {
                    final String currentId = (String)property;
                    if (!idEnumeration.obj.has(currentId, idEnumeration.obj)) {
                        continue;
                    }
                    idEnumeration.currentId = currentId;
                }
                else {
                    final int intValue = ((Number)property).intValue();
                    if (!idEnumeration.obj.has(intValue, idEnumeration.obj)) {
                        continue;
                    }
                    Serializable currentId2;
                    if (idEnumeration.enumNumbers) {
                        currentId2 = intValue;
                    }
                    else {
                        currentId2 = String.valueOf(intValue);
                    }
                    idEnumeration.currentId = currentId2;
                }
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
    
    public static Object enumValue(final Object o, final Context context) {
        final IdEnumeration idEnumeration = (IdEnumeration)o;
        final String stringIdOrIndex = toStringIdOrIndex(context, idEnumeration.currentId);
        if (stringIdOrIndex == null) {
            return idEnumeration.obj.get(lastIndexResult(context), idEnumeration.obj);
        }
        return idEnumeration.obj.get(stringIdOrIndex, idEnumeration.obj);
    }
    
    public static boolean eq(Object o, Object unwrap) {
        if (o != null && o != Undefined.instance) {
            if (o instanceof Number) {
                return eqNumber(((Number)o).doubleValue(), unwrap);
            }
            if (o == unwrap) {
                return true;
            }
            if (o instanceof CharSequence) {
                return eqString((CharSequence)o, unwrap);
            }
            final boolean b = o instanceof Boolean;
            double n = 0.0;
            if (b) {
                final boolean booleanValue = (boolean)o;
                if (unwrap instanceof Boolean) {
                    return booleanValue == (boolean)unwrap;
                }
                if (unwrap instanceof ScriptableObject) {
                    o = ((ScriptableObject)unwrap).equivalentValues(o);
                    if (o != Scriptable.NOT_FOUND) {
                        return (boolean)o;
                    }
                }
                if (booleanValue) {
                    n = 1.0;
                }
                return eqNumber(n, unwrap);
            }
            else {
                if (!(o instanceof Scriptable)) {
                    warnAboutNonJSObject(o);
                    return o == unwrap;
                }
                if (unwrap instanceof Scriptable) {
                    if (o instanceof ScriptableObject) {
                        final Object equivalentValues = ((ScriptableObject)o).equivalentValues(unwrap);
                        if (equivalentValues != Scriptable.NOT_FOUND) {
                            return (boolean)equivalentValues;
                        }
                    }
                    if (unwrap instanceof ScriptableObject) {
                        final Object equivalentValues2 = ((ScriptableObject)unwrap).equivalentValues(o);
                        if (equivalentValues2 != Scriptable.NOT_FOUND) {
                            return (boolean)equivalentValues2;
                        }
                    }
                    if (o instanceof Wrapper && unwrap instanceof Wrapper) {
                        o = ((Wrapper)o).unwrap();
                        unwrap = ((Wrapper)unwrap).unwrap();
                        return o == unwrap || (isPrimitive(o) && isPrimitive(unwrap) && eq(o, unwrap));
                    }
                    return false;
                }
                else {
                    if (unwrap instanceof Boolean) {
                        if (o instanceof ScriptableObject) {
                            final Object equivalentValues3 = ((ScriptableObject)o).equivalentValues(unwrap);
                            if (equivalentValues3 != Scriptable.NOT_FOUND) {
                                return (boolean)equivalentValues3;
                            }
                        }
                        if (unwrap) {
                            n = 1.0;
                        }
                        return eqNumber(n, o);
                    }
                    if (unwrap instanceof Number) {
                        return eqNumber(((Number)unwrap).doubleValue(), o);
                    }
                    return unwrap instanceof CharSequence && eqString((CharSequence)unwrap, o);
                }
            }
        }
        else {
            if (unwrap == null) {
                return true;
            }
            if (unwrap == Undefined.instance) {
                return true;
            }
            if (unwrap instanceof ScriptableObject) {
                o = ((ScriptableObject)unwrap).equivalentValues(o);
                if (o != Scriptable.NOT_FOUND) {
                    return (boolean)o;
                }
            }
            return false;
        }
    }
    
    static boolean eqNumber(final double n, Object primitive) {
        while (true) {
            final boolean b = false;
            final boolean b2 = false;
            boolean b3 = false;
            if (primitive == null) {
                return false;
            }
            if (primitive == Undefined.instance) {
                return false;
            }
            if (primitive instanceof Number) {
                if (n == ((Number)primitive).doubleValue()) {
                    b3 = true;
                }
                return b3;
            }
            if (primitive instanceof CharSequence) {
                boolean b4 = b;
                if (n == toNumber(primitive)) {
                    b4 = true;
                }
                return b4;
            }
            if (primitive instanceof Boolean) {
                double n2;
                if (primitive) {
                    n2 = 1.0;
                }
                else {
                    n2 = 0.0;
                }
                boolean b5 = b2;
                if (n == n2) {
                    b5 = true;
                }
                return b5;
            }
            if (!(primitive instanceof Scriptable)) {
                warnAboutNonJSObject(primitive);
                return false;
            }
            if (primitive instanceof ScriptableObject) {
                final Object equivalentValues = ((ScriptableObject)primitive).equivalentValues(wrapNumber(n));
                if (equivalentValues != Scriptable.NOT_FOUND) {
                    return (boolean)equivalentValues;
                }
            }
            primitive = toPrimitive(primitive);
        }
    }
    
    private static boolean eqString(final CharSequence charSequence, Object primitive) {
        while (true) {
            boolean b = false;
            final boolean b2 = false;
            final boolean b3 = false;
            if (primitive == null) {
                return false;
            }
            if (primitive == Undefined.instance) {
                return false;
            }
            if (primitive instanceof CharSequence) {
                final CharSequence charSequence2 = (CharSequence)primitive;
                boolean b4 = b3;
                if (charSequence.length() == charSequence2.length()) {
                    b4 = b3;
                    if (charSequence.toString().equals(charSequence2.toString())) {
                        b4 = true;
                    }
                }
                return b4;
            }
            if (primitive instanceof Number) {
                if (toNumber(charSequence.toString()) == ((Number)primitive).doubleValue()) {
                    b = true;
                }
                return b;
            }
            if (primitive instanceof Boolean) {
                final double number = toNumber(charSequence.toString());
                double n;
                if (primitive) {
                    n = 1.0;
                }
                else {
                    n = 0.0;
                }
                boolean b5 = b2;
                if (number == n) {
                    b5 = true;
                }
                return b5;
            }
            if (!(primitive instanceof Scriptable)) {
                warnAboutNonJSObject(primitive);
                return false;
            }
            if (primitive instanceof ScriptableObject) {
                final Object equivalentValues = ((ScriptableObject)primitive).equivalentValues(charSequence.toString());
                if (equivalentValues != Scriptable.NOT_FOUND) {
                    return (boolean)equivalentValues;
                }
            }
            primitive = toPrimitive(primitive);
        }
    }
    
    private static RuntimeException errorWithClassName(final String s, final Object o) {
        return Context.reportRuntimeError1(s, o.getClass().getName());
    }
    
    public static String escapeAttributeValue(final Object o, final Context context) {
        return currentXMLLib(context).escapeAttributeValue(o);
    }
    
    public static String escapeString(final String s) {
        return escapeString(s, '\"');
    }
    
    public static String escapeString(final String s, final char c) {
        if (c != '\"' && c != '\'') {
            Kit.codeBug();
        }
        StringBuilder sb = null;
        StringBuilder sb2;
        for (int i = 0, length = s.length(); i != length; ++i, sb = sb2) {
            final char char1 = s.charAt(i);
            if (' ' <= char1 && char1 <= '~' && char1 != c && char1 != '\\') {
                if ((sb2 = sb) != null) {
                    sb.append(char1);
                    sb2 = sb;
                }
            }
            else {
                StringBuilder sb3;
                if ((sb3 = sb) == null) {
                    sb3 = new StringBuilder(length + 3);
                    sb3.append(s);
                    sb3.setLength(i);
                }
                int n = -1;
                if (char1 != ' ') {
                    if (char1 != '\\') {
                        switch (char1) {
                            case '\r': {
                                n = 114;
                                break;
                            }
                            case '\f': {
                                n = 102;
                                break;
                            }
                            case '\u000b': {
                                n = 118;
                                break;
                            }
                            case '\n': {
                                n = 110;
                                break;
                            }
                            case '\t': {
                                n = 116;
                                break;
                            }
                            case '\b': {
                                n = 98;
                                break;
                            }
                        }
                    }
                    else {
                        n = 92;
                    }
                }
                else {
                    n = 32;
                }
                if (n >= 0) {
                    sb3.append('\\');
                    sb3.append((char)n);
                    sb2 = sb3;
                }
                else if (char1 == c) {
                    sb3.append('\\');
                    sb3.append(c);
                    sb2 = sb3;
                }
                else {
                    int n2;
                    if (char1 < '\u0100') {
                        sb3.append("\\x");
                        n2 = 2;
                    }
                    else {
                        sb3.append("\\u");
                        n2 = 4;
                    }
                    int n3 = (n2 - 1) * 4;
                    while (true) {
                        sb2 = sb3;
                        if (n3 < 0) {
                            break;
                        }
                        final int n4 = char1 >> n3 & 0xF;
                        int n5;
                        if (n4 < 10) {
                            n5 = n4 + 48;
                        }
                        else {
                            n5 = n4 + 87;
                        }
                        sb3.append((char)n5);
                        n3 -= 4;
                    }
                }
            }
        }
        if (sb == null) {
            return s;
        }
        return sb.toString();
    }
    
    public static String escapeTextValue(final Object o, final Context context) {
        return currentXMLLib(context).escapeTextValue(o);
    }
    
    public static Object evalSpecial(final Context context, final Scriptable scriptable, final Object o, final Object[] array, final String s, int n) {
        if (array.length < 1) {
            return Undefined.instance;
        }
        final Object o2 = array[0];
        if (!(o2 instanceof CharSequence)) {
            if (!context.hasFeature(11) && !context.hasFeature(9)) {
                Context.reportWarning(getMessage0("msg.eval.nonstring"));
                return o2;
            }
            throw Context.reportRuntimeError0("msg.eval.nonstring.strict");
        }
        else {
            String sourcePositionFromStack;
            if (s == null) {
                final int[] array2 = { 0 };
                sourcePositionFromStack = Context.getSourcePositionFromStack(array2);
                if (sourcePositionFromStack != null) {
                    n = array2[0];
                }
                else {
                    sourcePositionFromStack = "";
                }
            }
            else {
                sourcePositionFromStack = s;
            }
            final String urlForGeneratedScript = makeUrlForGeneratedScript(true, sourcePositionFromStack, n);
            final ErrorReporter forEval = DefaultErrorReporter.forEval(context.getErrorReporter());
            final Evaluator interpreter = Context.createInterpreter();
            if (interpreter == null) {
                throw new JavaScriptException("Interpreter not present", sourcePositionFromStack, n);
            }
            final Script compileString = context.compileString(o2.toString(), interpreter, forEval, urlForGeneratedScript, 1, null);
            interpreter.setEvalScriptFlag(compileString);
            return ((Callable)compileString).call(context, scriptable, (Scriptable)o, ScriptRuntime.emptyArgs);
        }
    }
    
    public static void exitActivationFunction(final Context context) {
        final NativeCall currentActivationCall = context.currentActivationCall;
        context.currentActivationCall = currentActivationCall.parentActivationCall;
        currentActivationCall.parentActivationCall = null;
    }
    
    static NativeCall findFunctionActivation(final Context context, final Function function) {
        for (NativeCall nativeCall = context.currentActivationCall; nativeCall != null; nativeCall = nativeCall.parentActivationCall) {
            if (nativeCall.function == function) {
                return nativeCall;
            }
        }
        return null;
    }
    
    static Object[] getApplyArguments(final Context context, final Object o) {
        if (o == null || o == Undefined.instance) {
            return ScriptRuntime.emptyArgs;
        }
        if (!(o instanceof NativeArray) && !(o instanceof Arguments)) {
            throw typeError0("msg.arg.isnt.array");
        }
        return context.getElements((Scriptable)o);
    }
    
    public static Object[] getArrayElements(final Scriptable scriptable) {
        final long lengthProperty = NativeArray.getLengthProperty(Context.getContext(), scriptable);
        if (lengthProperty > 2147483647L) {
            throw new IllegalArgumentException();
        }
        final int n = (int)lengthProperty;
        if (n == 0) {
            return ScriptRuntime.emptyArgs;
        }
        final Object[] array = new Object[n];
        for (int i = 0; i < n; ++i) {
            Object o = ScriptableObject.getProperty(scriptable, i);
            if (o == Scriptable.NOT_FOUND) {
                o = Undefined.instance;
            }
            array[i] = o;
        }
        return array;
    }
    
    static Callable getCallable(final Scriptable scriptable) {
        if (scriptable instanceof Callable) {
            return (Callable)scriptable;
        }
        final Object defaultValue = scriptable.getDefaultValue(ScriptRuntime.FunctionClass);
        if (!(defaultValue instanceof Callable)) {
            throw notFunctionError(defaultValue, scriptable);
        }
        return (Callable)defaultValue;
    }
    
    @Deprecated
    public static Callable getElemFunctionAndThis(final Object o, final Object o2, final Context context) {
        return getElemFunctionAndThis(o, o2, context, getTopCallScope(context));
    }
    
    public static Callable getElemFunctionAndThis(Object property, final Object o, final Context context, Scriptable objectOrNull) {
        final String stringIdOrIndex = toStringIdOrIndex(context, o);
        if (stringIdOrIndex != null) {
            return getPropFunctionAndThis(property, stringIdOrIndex, context, objectOrNull);
        }
        final int lastIndexResult = lastIndexResult(context);
        objectOrNull = toObjectOrNull(context, property, objectOrNull);
        if (objectOrNull == null) {
            throw undefCallError(property, String.valueOf(lastIndexResult));
        }
        property = ScriptableObject.getProperty(objectOrNull, lastIndexResult);
        if (!(property instanceof Callable)) {
            throw notFunctionError(property, o);
        }
        storeScriptable(context, objectOrNull);
        return (Callable)property;
    }
    
    static Function getExistingCtor(final Context context, final Scriptable scriptable, final String s) {
        final Object property = ScriptableObject.getProperty(scriptable, s);
        if (property instanceof Function) {
            return (Function)property;
        }
        if (property == Scriptable.NOT_FOUND) {
            throw Context.reportRuntimeError1("msg.ctor.not.found", s);
        }
        throw Context.reportRuntimeError1("msg.not.ctor", s);
    }
    
    public static ScriptableObject getGlobal(final Context context) {
        final Class<?> classOrNull = Kit.classOrNull("org.mozilla.javascript.tools.shell.Global");
        if (classOrNull != null) {
            try {
                return (ScriptableObject)classOrNull.getConstructor(ScriptRuntime.ContextClass).newInstance(context);
            }
            catch (Exception ex2) {}
            catch (RuntimeException ex) {
                throw ex;
            }
        }
        return new ImporterTopLevel(context);
    }
    
    static Object getIndexObject(final double n) {
        final int n2 = (int)n;
        if (n2 == n) {
            return n2;
        }
        return toString(n);
    }
    
    static Object getIndexObject(final String s) {
        final long indexFromString = indexFromString(s);
        if (indexFromString >= 0L) {
            return (int)indexFromString;
        }
        return s;
    }
    
    public static ScriptableObject getLibraryScopeOrNull(final Scriptable scriptable) {
        return (ScriptableObject)ScriptableObject.getTopScopeValue(scriptable, ScriptRuntime.LIBRARY_SCOPE_KEY);
    }
    
    public static String getMessage(final String s, final Object[] array) {
        try {
            return ScriptRuntime.messageProvider.getMessage(s, array);
        }
        catch (Throwable t) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(" ");
            sb.append(Arrays.toString(array));
            return sb.toString();
        }
    }
    
    public static String getMessage0(final String s) {
        return getMessage(s, null);
    }
    
    public static String getMessage1(final String s, final Object o) {
        return getMessage(s, new Object[] { o });
    }
    
    public static String getMessage2(final String s, final Object o, final Object o2) {
        return getMessage(s, new Object[] { o, o2 });
    }
    
    public static String getMessage3(final String s, final Object o, final Object o2, final Object o3) {
        return getMessage(s, new Object[] { o, o2, o3 });
    }
    
    public static String getMessage4(final String s, final Object o, final Object o2, final Object o3, final Object o4) {
        return getMessage(s, new Object[] { o, o2, o3, o4 });
    }
    
    public static Callable getNameFunctionAndThis(final String s, final Context context, final Scriptable scriptable) {
        final Scriptable parentScope = scriptable.getParentScope();
        if (parentScope != null) {
            return (Callable)nameOrFunction(context, scriptable, parentScope, s, true);
        }
        final Object topScopeName = topScopeName(context, scriptable, s);
        if (topScopeName instanceof Callable) {
            storeScriptable(context, scriptable);
            return (Callable)topScopeName;
        }
        if (topScopeName == Scriptable.NOT_FOUND) {
            throw notFoundError(scriptable, s);
        }
        throw notFunctionError(topScopeName, s);
    }
    
    @Deprecated
    public static Object getObjectElem(final Object o, final Object o2, final Context context) {
        return getObjectElem(o, o2, context, getTopCallScope(context));
    }
    
    public static Object getObjectElem(final Object o, final Object o2, final Context context, Scriptable objectOrNull) {
        objectOrNull = toObjectOrNull(context, o, objectOrNull);
        if (objectOrNull == null) {
            throw undefReadError(o, o2);
        }
        return getObjectElem(objectOrNull, o2, context);
    }
    
    public static Object getObjectElem(final Scriptable scriptable, Object instance, final Context context) {
        Object o;
        if (scriptable instanceof XMLObject) {
            o = ((XMLObject)scriptable).get(context, instance);
        }
        else {
            final String stringIdOrIndex = toStringIdOrIndex(context, instance);
            if (stringIdOrIndex == null) {
                o = ScriptableObject.getProperty(scriptable, lastIndexResult(context));
            }
            else {
                o = ScriptableObject.getProperty(scriptable, stringIdOrIndex);
            }
        }
        instance = o;
        if (o == Scriptable.NOT_FOUND) {
            instance = Undefined.instance;
        }
        return instance;
    }
    
    @Deprecated
    public static Object getObjectIndex(final Object o, final double n, final Context context) {
        return getObjectIndex(o, n, context, getTopCallScope(context));
    }
    
    public static Object getObjectIndex(final Object o, final double n, final Context context, Scriptable objectOrNull) {
        objectOrNull = toObjectOrNull(context, o, objectOrNull);
        if (objectOrNull == null) {
            throw undefReadError(o, toString(n));
        }
        final int n2 = (int)n;
        if (n2 == n) {
            return getObjectIndex(objectOrNull, n2, context);
        }
        return getObjectProp(objectOrNull, toString(n), context);
    }
    
    public static Object getObjectIndex(final Scriptable scriptable, final int n, final Context context) {
        Object o;
        if ((o = ScriptableObject.getProperty(scriptable, n)) == Scriptable.NOT_FOUND) {
            o = Undefined.instance;
        }
        return o;
    }
    
    @Deprecated
    public static Object getObjectProp(final Object o, final String s, final Context context) {
        return getObjectProp(o, s, context, getTopCallScope(context));
    }
    
    public static Object getObjectProp(final Object o, final String s, final Context context, Scriptable objectOrNull) {
        objectOrNull = toObjectOrNull(context, o, objectOrNull);
        if (objectOrNull == null) {
            throw undefReadError(o, s);
        }
        return getObjectProp(objectOrNull, s, context);
    }
    
    public static Object getObjectProp(final Scriptable scriptable, final String s, final Context context) {
        Object o;
        if ((o = ScriptableObject.getProperty(scriptable, s)) == Scriptable.NOT_FOUND) {
            if (context.hasFeature(11)) {
                Context.reportWarning(getMessage1("msg.ref.undefined.prop", s));
            }
            o = Undefined.instance;
        }
        return o;
    }
    
    @Deprecated
    public static Object getObjectPropNoWarn(final Object o, final String s, final Context context) {
        return getObjectPropNoWarn(o, s, context, getTopCallScope(context));
    }
    
    public static Object getObjectPropNoWarn(Object property, final String s, final Context context, final Scriptable scriptable) {
        final Scriptable objectOrNull = toObjectOrNull(context, property, scriptable);
        if (objectOrNull == null) {
            throw undefReadError(property, s);
        }
        property = ScriptableObject.getProperty(objectOrNull, s);
        if (property == Scriptable.NOT_FOUND) {
            return Undefined.instance;
        }
        return property;
    }
    
    @Deprecated
    public static Callable getPropFunctionAndThis(final Object o, final String s, final Context context) {
        return getPropFunctionAndThis(o, s, context, getTopCallScope(context));
    }
    
    public static Callable getPropFunctionAndThis(final Object o, final String s, final Context context, final Scriptable scriptable) {
        return getPropFunctionAndThisHelper(o, s, context, toObjectOrNull(context, o, scriptable));
    }
    
    private static Callable getPropFunctionAndThisHelper(Object property, final String s, final Context context, final Scriptable scriptable) {
        if (scriptable == null) {
            throw undefCallError(property, s);
        }
        final Object o = property = ScriptableObject.getProperty(scriptable, s);
        if (!(o instanceof Callable)) {
            final Object property2 = ScriptableObject.getProperty(scriptable, "__noSuchMethod__");
            property = o;
            if (property2 instanceof Callable) {
                property = new NoSuchMethodShim((Callable)property2, s);
            }
        }
        if (!(property instanceof Callable)) {
            throw notFunctionError(scriptable, property, s);
        }
        storeScriptable(context, scriptable);
        return (Callable)property;
    }
    
    public static RegExpProxy getRegExpProxy(final Context context) {
        return context.getRegExpProxy();
    }
    
    public static Scriptable getTopCallScope(final Context context) {
        if (context.topCallScope == null) {
            context.topCallScope = context.initStandardObjects();
            new ClassCache().associate((ScriptableObject)context.topCallScope);
        }
        return context.topCallScope;
    }
    
    public static Object getTopLevelProp(final Scriptable scriptable, final String s) {
        return ScriptableObject.getProperty(ScriptableObject.getTopLevelScope(scriptable), s);
    }
    
    static String[] getTopPackageNames() {
        if ("Dalvik".equals(System.getProperty("java.vm.name"))) {
            return new String[] { "java", "javax", "org", "com", "edu", "net", "android" };
        }
        return new String[] { "java", "javax", "org", "com", "edu", "net" };
    }
    
    public static Callable getValueFunctionAndThis(final Object o, final Context context) {
        if (!(o instanceof Callable)) {
            throw notFunctionError(o);
        }
        final Callable callable = (Callable)o;
        Scriptable parentScope = null;
        if (callable instanceof Scriptable) {
            parentScope = ((Scriptable)callable).getParentScope();
        }
        Scriptable topCallScope;
        if ((topCallScope = parentScope) == null) {
            if (context.topCallScope == null) {
                throw new IllegalStateException();
            }
            topCallScope = context.topCallScope;
        }
        Scriptable topLevelScope = topCallScope;
        if (topCallScope.getParentScope() != null) {
            if (topCallScope instanceof NativeWith) {
                topLevelScope = topCallScope;
            }
            else {
                topLevelScope = topCallScope;
                if (topCallScope instanceof NativeCall) {
                    topLevelScope = ScriptableObject.getTopLevelScope(topCallScope);
                }
            }
        }
        storeScriptable(context, topLevelScope);
        return callable;
    }
    
    public static boolean hasObjectElem(final Scriptable scriptable, final Object o, final Context context) {
        final String stringIdOrIndex = toStringIdOrIndex(context, o);
        if (stringIdOrIndex == null) {
            return ScriptableObject.hasProperty(scriptable, lastIndexResult(context));
        }
        return ScriptableObject.hasProperty(scriptable, stringIdOrIndex);
    }
    
    public static boolean hasTopCall(final Context context) {
        return context.topCallScope != null;
    }
    
    public static boolean in(final Object o, final Object o2, final Context context) {
        if (!(o2 instanceof Scriptable)) {
            throw typeError0("msg.in.not.object");
        }
        return hasObjectElem((Scriptable)o2, o, context);
    }
    
    public static long indexFromString(final String s) {
        final int length = s.length();
        if (length > 0) {
            final int n = 0;
            final boolean b = false;
            final char char1 = s.charAt(0);
            int n2 = n;
            boolean b2 = b;
            char char2;
            if ((char2 = char1) == '-') {
                n2 = n;
                b2 = b;
                char2 = char1;
                if (length > 1) {
                    char2 = s.charAt(1);
                    if (char2 == '0') {
                        return -1L;
                    }
                    n2 = 1;
                    b2 = true;
                }
            }
            final int n3 = char2 - '0';
            if (n3 >= 0 && n3 <= 9) {
                int n4;
                if (b2) {
                    n4 = 11;
                }
                else {
                    n4 = 10;
                }
                if (length <= n4) {
                    final int n5 = -n3;
                    int n6 = 0;
                    int n7 = 0;
                    int n8 = n2 + 1;
                    int n9 = n3;
                    int n10;
                    if ((n10 = n5) != 0) {
                        int n11 = n5;
                        n9 = n3;
                        int n12 = n8;
                        while (true) {
                            n8 = n12;
                            n10 = n11;
                            n6 = n7;
                            if (n12 == length) {
                                break;
                            }
                            final int n14;
                            final int n13 = n14 = s.charAt(n12) - 48;
                            n8 = n12;
                            n9 = n14;
                            n10 = n11;
                            n6 = n7;
                            if (n13 < 0) {
                                break;
                            }
                            n8 = n12;
                            n9 = n14;
                            n10 = n11;
                            n6 = n7;
                            if (n14 > 9) {
                                break;
                            }
                            n7 = n11;
                            n11 = n11 * 10 - n14;
                            ++n12;
                            n9 = n14;
                        }
                    }
                    if (n8 == length) {
                        if (n6 <= -214748364) {
                            if (n6 != -214748364) {
                                return -1L;
                            }
                            int n15;
                            if (b2) {
                                n15 = 8;
                            }
                            else {
                                n15 = 7;
                            }
                            if (n9 > n15) {
                                return -1L;
                            }
                        }
                        if (!b2) {
                            n10 = -n10;
                        }
                        return (long)n10 & 0xFFFFFFFFL;
                    }
                }
            }
        }
        return -1L;
    }
    
    public static void initFunction(final Context context, Scriptable parentScope, final NativeFunction nativeFunction, final int n, final boolean b) {
        if (n == 1) {
            final String functionName = nativeFunction.getFunctionName();
            if (functionName != null && functionName.length() != 0) {
                if (!b) {
                    ScriptableObject.defineProperty(parentScope, functionName, nativeFunction, 4);
                }
                else {
                    parentScope.put(functionName, parentScope, nativeFunction);
                }
            }
            return;
        }
        if (n == 3) {
            final String functionName2 = nativeFunction.getFunctionName();
            if (functionName2 != null && functionName2.length() != 0) {
                while (parentScope instanceof NativeWith) {
                    parentScope = parentScope.getParentScope();
                }
                parentScope.put(functionName2, parentScope, nativeFunction);
            }
            return;
        }
        throw Kit.codeBug();
    }
    
    public static ScriptableObject initSafeStandardObjects(final Context context, final ScriptableObject scriptableObject, final boolean b) {
        ScriptableObject scriptableObject2 = scriptableObject;
        if (scriptableObject == null) {
            scriptableObject2 = new NativeObject();
        }
        scriptableObject2.associateValue(ScriptRuntime.LIBRARY_SCOPE_KEY, scriptableObject2);
        new ClassCache().associate(scriptableObject2);
        BaseFunction.init(scriptableObject2, b);
        NativeObject.init(scriptableObject2, b);
        final Scriptable objectPrototype = ScriptableObject.getObjectPrototype(scriptableObject2);
        ScriptableObject.getClassPrototype(scriptableObject2, "Function").setPrototype(objectPrototype);
        if (scriptableObject2.getPrototype() == null) {
            scriptableObject2.setPrototype(objectPrototype);
        }
        NativeError.init(scriptableObject2, b);
        NativeGlobal.init(context, scriptableObject2, b);
        NativeArray.init(scriptableObject2, b);
        if (context.getOptimizationLevel() > 0) {
            NativeArray.setMaximumInitialCapacity(200000);
        }
        NativeString.init(scriptableObject2, b);
        NativeBoolean.init(scriptableObject2, b);
        NativeNumber.init(scriptableObject2, b);
        NativeDate.init(scriptableObject2, b);
        NativeMath.init(scriptableObject2, b);
        NativeJSON.init(scriptableObject2, b);
        NativeWith.init(scriptableObject2, b);
        NativeCall.init(scriptableObject2, b);
        NativeScript.init(scriptableObject2, b);
        NativeIterator.init(scriptableObject2, b);
        final boolean b2 = context.hasFeature(6) && context.getE4xImplementationFactory() != null;
        new LazilyLoadedCtor(scriptableObject2, "RegExp", "org.mozilla.javascript.regexp.NativeRegExp", b, true);
        new LazilyLoadedCtor(scriptableObject2, "Continuation", "org.mozilla.javascript.NativeContinuation", b, true);
        if (b2) {
            final String implementationClassName = context.getE4xImplementationFactory().getImplementationClassName();
            new LazilyLoadedCtor(scriptableObject2, "XML", implementationClassName, b, true);
            new LazilyLoadedCtor(scriptableObject2, "XMLList", implementationClassName, b, true);
            new LazilyLoadedCtor(scriptableObject2, "Namespace", implementationClassName, b, true);
            new LazilyLoadedCtor(scriptableObject2, "QName", implementationClassName, b, true);
        }
        if ((context.getLanguageVersion() >= 180 && context.hasFeature(14)) || context.getLanguageVersion() >= 200) {
            new LazilyLoadedCtor(scriptableObject2, "ArrayBuffer", "org.mozilla.javascript.typedarrays.NativeArrayBuffer", b, true);
            new LazilyLoadedCtor(scriptableObject2, "Int8Array", "org.mozilla.javascript.typedarrays.NativeInt8Array", b, true);
            new LazilyLoadedCtor(scriptableObject2, "Uint8Array", "org.mozilla.javascript.typedarrays.NativeUint8Array", b, true);
            new LazilyLoadedCtor(scriptableObject2, "Uint8ClampedArray", "org.mozilla.javascript.typedarrays.NativeUint8ClampedArray", b, true);
            new LazilyLoadedCtor(scriptableObject2, "Int16Array", "org.mozilla.javascript.typedarrays.NativeInt16Array", b, true);
            new LazilyLoadedCtor(scriptableObject2, "Uint16Array", "org.mozilla.javascript.typedarrays.NativeUint16Array", b, true);
            new LazilyLoadedCtor(scriptableObject2, "Int32Array", "org.mozilla.javascript.typedarrays.NativeInt32Array", b, true);
            new LazilyLoadedCtor(scriptableObject2, "Uint32Array", "org.mozilla.javascript.typedarrays.NativeUint32Array", b, true);
            new LazilyLoadedCtor(scriptableObject2, "Float32Array", "org.mozilla.javascript.typedarrays.NativeFloat32Array", b, true);
            new LazilyLoadedCtor(scriptableObject2, "Float64Array", "org.mozilla.javascript.typedarrays.NativeFloat64Array", b, true);
            new LazilyLoadedCtor(scriptableObject2, "DataView", "org.mozilla.javascript.typedarrays.NativeDataView", b, true);
        }
        if (scriptableObject2 instanceof TopLevel) {
            ((TopLevel)scriptableObject2).cacheBuiltins();
        }
        return scriptableObject2;
    }
    
    public static void initScript(final NativeFunction nativeFunction, Scriptable parentScope, final Context context, final Scriptable scriptable, final boolean b) {
        if (context.topCallScope == null) {
            throw new IllegalStateException();
        }
        int paramAndVarCount = nativeFunction.getParamAndVarCount();
        if (paramAndVarCount != 0) {
            for (parentScope = scriptable; parentScope instanceof NativeWith; parentScope = parentScope.getParentScope()) {}
            while (true) {
                final int n = paramAndVarCount - 1;
                if (paramAndVarCount == 0) {
                    break;
                }
                final String paramOrVarName = nativeFunction.getParamOrVarName(n);
                final boolean paramOrVarConst = nativeFunction.getParamOrVarConst(n);
                if (!ScriptableObject.hasProperty(scriptable, paramOrVarName)) {
                    if (paramOrVarConst) {
                        ScriptableObject.defineConstProperty(parentScope, paramOrVarName);
                    }
                    else if (!b) {
                        ScriptableObject.defineProperty(parentScope, paramOrVarName, Undefined.instance, 4);
                    }
                    else {
                        parentScope.put(paramOrVarName, parentScope, Undefined.instance);
                    }
                }
                else {
                    ScriptableObject.redefineProperty(scriptable, paramOrVarName, paramOrVarConst);
                }
                paramAndVarCount = n;
            }
        }
    }
    
    public static ScriptableObject initStandardObjects(final Context context, final ScriptableObject scriptableObject, final boolean b) {
        final ScriptableObject initSafeStandardObjects = initSafeStandardObjects(context, scriptableObject, b);
        new LazilyLoadedCtor(initSafeStandardObjects, "Packages", "org.mozilla.javascript.NativeJavaTopPackage", b, true);
        new LazilyLoadedCtor(initSafeStandardObjects, "getClass", "org.mozilla.javascript.NativeJavaTopPackage", b, true);
        new LazilyLoadedCtor(initSafeStandardObjects, "JavaAdapter", "org.mozilla.javascript.JavaAdapter", b, true);
        new LazilyLoadedCtor(initSafeStandardObjects, "JavaImporter", "org.mozilla.javascript.ImporterTopLevel", b, true);
        final String[] topPackageNames = getTopPackageNames();
        for (int length = topPackageNames.length, i = 0; i < length; ++i) {
            new LazilyLoadedCtor(initSafeStandardObjects, topPackageNames[i], "org.mozilla.javascript.NativeJavaTopPackage", b, true);
        }
        return initSafeStandardObjects;
    }
    
    public static boolean instanceOf(final Object o, final Object o2, final Context context) {
        if (!(o2 instanceof Scriptable)) {
            throw typeError0("msg.instanceof.not.object");
        }
        return o instanceof Scriptable && ((Scriptable)o2).hasInstance((Scriptable)o);
    }
    
    public static boolean isArrayObject(final Object o) {
        return o instanceof NativeArray || o instanceof Arguments;
    }
    
    static boolean isGeneratedScript(final String s) {
        return s.indexOf("(eval)") >= 0 || s.indexOf("(Function)") >= 0;
    }
    
    public static boolean isJSLineTerminator(final int n) {
        return (0xDFD0 & n) == 0x0 && (n == 10 || n == 13 || n == 8232 || n == 8233);
    }
    
    public static boolean isJSWhitespaceOrLineTerminator(final int n) {
        return isStrWhiteSpaceChar(n) || isJSLineTerminator(n);
    }
    
    public static boolean isPrimitive(final Object o) {
        return o == null || o == Undefined.instance || o instanceof Number || o instanceof String || o instanceof Boolean;
    }
    
    public static boolean isRhinoRuntimeType(final Class<?> clazz) {
        final boolean primitive = clazz.isPrimitive();
        boolean b = false;
        if (primitive) {
            if (clazz != Character.TYPE) {
                b = true;
            }
            return b;
        }
        return clazz == ScriptRuntime.StringClass || clazz == ScriptRuntime.BooleanClass || ScriptRuntime.NumberClass.isAssignableFrom(clazz) || ScriptRuntime.ScriptableClass.isAssignableFrom(clazz);
    }
    
    static boolean isSpecialProperty(final String s) {
        return s.equals("__proto__") || s.equals("__parent__");
    }
    
    static boolean isStrWhiteSpaceChar(final int n) {
        Label_0093: {
            if (n != 32 && n != 160 && n != 65279) {
                switch (n) {
                    default: {
                        switch (n) {
                            default: {
                                return Character.getType(n) == 12;
                            }
                            case 8232:
                            case 8233: {
                                break Label_0093;
                            }
                        }
                        break;
                    }
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13: {
                        break;
                    }
                }
            }
        }
        return true;
    }
    
    static boolean isValidIdentifierName(final String s) {
        final int length = s.length();
        if (length == 0) {
            return false;
        }
        if (!Character.isJavaIdentifierStart(s.charAt(0))) {
            return false;
        }
        for (int i = 1; i != length; ++i) {
            if (!Character.isJavaIdentifierPart(s.charAt(i))) {
                return false;
            }
        }
        return TokenStream.isKeyword(s) ^ true;
    }
    
    private static boolean isVisible(final Context context, final Object o) {
        final ClassShutter classShutter = context.getClassShutter();
        return classShutter == null || classShutter.visibleToScripts(o.getClass().getName());
    }
    
    public static boolean jsDelegatesTo(Scriptable scriptable, final Scriptable scriptable2) {
        for (scriptable = scriptable.getPrototype(); scriptable != null; scriptable = scriptable.getPrototype()) {
            if (scriptable.equals(scriptable2)) {
                return true;
            }
        }
        return false;
    }
    
    static int lastIndexResult(final Context context) {
        return context.scratchIndex;
    }
    
    public static Scriptable lastStoredScriptable(final Context context) {
        final Scriptable scratchScriptable = context.scratchScriptable;
        context.scratchScriptable = null;
        return scratchScriptable;
    }
    
    public static long lastUint32Result(final Context context) {
        final long scratchUint32 = context.scratchUint32;
        if (scratchUint32 >>> 32 != 0L) {
            throw new IllegalStateException();
        }
        return scratchUint32;
    }
    
    public static Scriptable leaveDotQuery(final Scriptable scriptable) {
        return ((NativeWith)scriptable).getParentScope();
    }
    
    public static Scriptable leaveWith(final Scriptable scriptable) {
        return ((NativeWith)scriptable).getParentScope();
    }
    
    static String makeUrlForGeneratedScript(final boolean b, final String s, final int n) {
        if (b) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append('#');
            sb.append(n);
            sb.append("(eval)");
            return sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append('#');
        sb2.append(n);
        sb2.append("(Function)");
        return sb2.toString();
    }
    
    public static Ref memberRef(final Object o, final Object o2, final Object o3, final Context context, final int n) {
        if (!(o instanceof XMLObject)) {
            throw notXmlError(o);
        }
        return ((XMLObject)o).memberRef(context, o2, o3, n);
    }
    
    public static Ref memberRef(final Object o, final Object o2, final Context context, final int n) {
        if (!(o instanceof XMLObject)) {
            throw notXmlError(o);
        }
        return ((XMLObject)o).memberRef(context, o2, n);
    }
    
    public static Object name(final Context context, final Scriptable scriptable, final String s) {
        final Scriptable parentScope = scriptable.getParentScope();
        if (parentScope != null) {
            return nameOrFunction(context, scriptable, parentScope, s, false);
        }
        final Object topScopeName = topScopeName(context, scriptable, s);
        if (topScopeName == Scriptable.NOT_FOUND) {
            throw notFoundError(scriptable, s);
        }
        return topScopeName;
    }
    
    @Deprecated
    public static Object nameIncrDecr(final Scriptable scriptable, final String s, final int n) {
        return nameIncrDecr(scriptable, s, Context.getContext(), n);
    }
    
    public static Object nameIncrDecr(Scriptable scriptable, final String s, final Context context, final int n) {
        Scriptable scriptable2 = scriptable;
        do {
            Label_0003: {
                scriptable = scriptable2;
            }
            if (context.useDynamicScope) {
                scriptable = scriptable2;
                if (scriptable2.getParentScope() == null) {
                    scriptable = checkDynamicScope(context.topCallScope, scriptable2);
                }
            }
            Scriptable prototype = scriptable;
            while (true) {
                while (!(prototype instanceof NativeWith) || !(prototype.getPrototype() instanceof XMLObject)) {
                    final Object value = prototype.get(s, scriptable);
                    if (value != Scriptable.NOT_FOUND) {
                        return doScriptableIncrDecr(prototype, s, scriptable, value, n);
                    }
                    if ((prototype = prototype.getPrototype()) == null) {
                        scriptable = scriptable.getParentScope();
                        continue Label_0003;
                    }
                }
                continue;
            }
        } while ((scriptable2 = scriptable) != null);
        throw notFoundError(scriptable, s);
    }
    
    private static Object nameOrFunction(final Context context, Scriptable topLevelScope, Scriptable scriptable, final String s, final boolean b) {
        final Scriptable scriptable2 = topLevelScope;
        final XMLObject xmlObject = null;
        Scriptable scriptable3 = scriptable;
        Scriptable scriptable4 = topLevelScope;
        XMLObject xmlObject2 = xmlObject;
    Label_0275:
        while (true) {
            Scriptable parentScope;
            XMLObject xmlObject5;
            do {
                if (scriptable4 instanceof NativeWith) {
                    final Scriptable prototype = scriptable4.getPrototype();
                    if (prototype instanceof XMLObject) {
                        final XMLObject xmlObject3 = (XMLObject)prototype;
                        if (xmlObject3.has(s, xmlObject3)) {
                            topLevelScope = xmlObject3;
                            final Object o = xmlObject3.get(s, xmlObject3);
                            break Label_0275;
                        }
                        XMLObject xmlObject4;
                        if ((xmlObject4 = xmlObject2) == null) {
                            xmlObject4 = xmlObject3;
                        }
                        xmlObject2 = xmlObject4;
                    }
                    else {
                        final Object o = ScriptableObject.getProperty(prototype, s);
                        if (o != Scriptable.NOT_FOUND) {
                            topLevelScope = prototype;
                            break Label_0275;
                        }
                    }
                    xmlObject5 = xmlObject2;
                }
                else if (scriptable4 instanceof NativeCall) {
                    final Object value = scriptable4.get(s, scriptable4);
                    xmlObject5 = xmlObject2;
                    if (value != Scriptable.NOT_FOUND) {
                        topLevelScope = scriptable2;
                        Object o = value;
                        if (b) {
                            topLevelScope = ScriptableObject.getTopLevelScope(scriptable3);
                            o = value;
                        }
                        break Label_0275;
                    }
                }
                else {
                    final Object o = ScriptableObject.getProperty(scriptable4, s);
                    xmlObject5 = xmlObject2;
                    if (o != Scriptable.NOT_FOUND) {
                        topLevelScope = scriptable4;
                        break Label_0275;
                    }
                }
                scriptable = scriptable3;
                parentScope = scriptable3.getParentScope();
                xmlObject2 = xmlObject5;
                scriptable4 = scriptable;
                continue;
                Object o = null;
                if (b) {
                    if (!(o instanceof Callable)) {
                        throw notFunctionError(o, s);
                    }
                    storeScriptable(context, topLevelScope);
                }
                return o;
            } while ((scriptable3 = parentScope) != null);
            Object o2;
            if ((o2 = topScopeName(context, scriptable, s)) == Scriptable.NOT_FOUND) {
                if (xmlObject5 == null || b) {
                    throw notFoundError(scriptable, s);
                }
                o2 = xmlObject5.get(s, xmlObject5);
            }
            final Scriptable scriptable5 = scriptable;
            Object o = o2;
            topLevelScope = scriptable5;
            continue Label_0275;
        }
    }
    
    public static Ref nameRef(final Object o, final Object o2, final Context context, final Scriptable scriptable, final int n) {
        return currentXMLLib(context).nameRef(context, o, o2, scriptable, n);
    }
    
    public static Ref nameRef(final Object o, final Context context, final Scriptable scriptable, final int n) {
        return currentXMLLib(context).nameRef(context, o, scriptable, n);
    }
    
    public static Scriptable newArrayLiteral(Object[] array, final int[] array2, final Context context, final Scriptable scriptable) {
        final int length = array.length;
        int length2 = 0;
        if (array2 != null) {
            length2 = array2.length;
        }
        final int n = length + length2;
        final int n2 = 0;
        int n3 = 0;
        if (n > 1 && length2 * 2 < n) {
            if (length2 != 0) {
                final Object[] array3 = new Object[n];
                int n4 = 0;
                for (int i = 0; i != n; ++i) {
                    if (n4 != length2 && array2[n4] == i) {
                        array3[i] = Scriptable.NOT_FOUND;
                        ++n4;
                    }
                    else {
                        array3[i] = array[n3];
                        ++n3;
                    }
                }
                array = array3;
            }
            return context.newArray(scriptable, array);
        }
        final Scriptable array4 = context.newArray(scriptable, n);
        int n5 = 0;
        int j = 0;
        int n6 = n2;
        while (j != n) {
            if (n5 != length2 && array2[n5] == j) {
                ++n5;
            }
            else {
                ScriptableObject.putProperty(array4, j, array[n6]);
                ++n6;
            }
            ++j;
        }
        return array4;
    }
    
    public static Scriptable newBuiltinObject(final Context context, final Scriptable scriptable, final TopLevel.Builtins builtins, final Object[] array) {
        final Scriptable topLevelScope = ScriptableObject.getTopLevelScope(scriptable);
        final Function builtinCtor = TopLevel.getBuiltinCtor(context, topLevelScope, builtins);
        Object[] emptyArgs = array;
        if (array == null) {
            emptyArgs = ScriptRuntime.emptyArgs;
        }
        return builtinCtor.construct(context, topLevelScope, emptyArgs);
    }
    
    public static Scriptable newCatchScope(final Throwable t, Scriptable scriptable, final String s, final Context context, final Scriptable scriptable2) {
        boolean b;
        if (t instanceof JavaScriptException) {
            b = false;
            scriptable = (Scriptable)((JavaScriptException)t).getValue();
        }
        else {
            b = true;
            if (scriptable != null) {
                scriptable = (Scriptable)((NativeObject)scriptable).getAssociatedValue(t);
                if (scriptable == null) {
                    Kit.codeBug();
                }
            }
            else {
                Throwable wrappedException = null;
                RhinoException stackProvider;
                TopLevel.NativeErrors nativeErrors;
                String s2;
                if (t instanceof EcmaError) {
                    final EcmaError ecmaError = (EcmaError)(stackProvider = (EcmaError)t);
                    nativeErrors = TopLevel.NativeErrors.valueOf(ecmaError.getName());
                    s2 = ecmaError.getErrorMessage();
                }
                else if (t instanceof WrappedException) {
                    wrappedException = ((WrappedException)(stackProvider = (WrappedException)t)).getWrappedException();
                    nativeErrors = TopLevel.NativeErrors.JavaException;
                    final StringBuilder sb = new StringBuilder();
                    sb.append(wrappedException.getClass().getName());
                    sb.append(": ");
                    sb.append(wrappedException.getMessage());
                    s2 = sb.toString();
                }
                else if (t instanceof EvaluatorException) {
                    final EvaluatorException ex = (EvaluatorException)(stackProvider = (EvaluatorException)t);
                    nativeErrors = TopLevel.NativeErrors.InternalError;
                    s2 = ex.getMessage();
                }
                else {
                    if (!context.hasFeature(13)) {
                        throw Kit.codeBug();
                    }
                    stackProvider = new WrappedException(t);
                    nativeErrors = TopLevel.NativeErrors.JavaException;
                    s2 = t.toString();
                }
                String sourceName;
                if ((sourceName = stackProvider.sourceName()) == null) {
                    sourceName = "";
                }
                final int lineNumber = stackProvider.lineNumber();
                Object[] array;
                if (lineNumber > 0) {
                    array = new Object[] { s2, sourceName, lineNumber };
                }
                else {
                    array = new Object[] { s2, sourceName };
                }
                final Scriptable nativeError = newNativeError(context, scriptable2, nativeErrors, array);
                if (nativeError instanceof NativeError) {
                    ((NativeError)nativeError).setStackProvider(stackProvider);
                }
                if (wrappedException != null && isVisible(context, wrappedException)) {
                    ScriptableObject.defineProperty(nativeError, "javaException", context.getWrapFactory().wrap(context, scriptable2, wrappedException, null), 7);
                }
                if (isVisible(context, stackProvider)) {
                    ScriptableObject.defineProperty(nativeError, "rhinoException", context.getWrapFactory().wrap(context, scriptable2, stackProvider, null), 7);
                }
                scriptable = nativeError;
            }
        }
        final NativeObject nativeObject = new NativeObject();
        nativeObject.defineProperty(s, scriptable, 4);
        if (isVisible(context, t)) {
            nativeObject.defineProperty("__exception__", Context.javaToJS(t, scriptable2), 6);
        }
        if (b) {
            nativeObject.associateValue(t, scriptable);
        }
        return nativeObject;
    }
    
    static Scriptable newNativeError(final Context context, final Scriptable scriptable, final TopLevel.NativeErrors nativeErrors, final Object[] array) {
        final Scriptable topLevelScope = ScriptableObject.getTopLevelScope(scriptable);
        final Function nativeErrorCtor = TopLevel.getNativeErrorCtor(context, topLevelScope, nativeErrors);
        Object[] emptyArgs = array;
        if (array == null) {
            emptyArgs = ScriptRuntime.emptyArgs;
        }
        return nativeErrorCtor.construct(context, topLevelScope, emptyArgs);
    }
    
    public static Scriptable newObject(final Object o, final Context context, final Scriptable scriptable, final Object[] array) {
        if (!(o instanceof Function)) {
            throw notFunctionError(o);
        }
        return ((Function)o).construct(context, scriptable, array);
    }
    
    public static Scriptable newObject(final Context context, final Scriptable scriptable, final String s, final Object[] array) {
        final Scriptable topLevelScope = ScriptableObject.getTopLevelScope(scriptable);
        final Function existingCtor = getExistingCtor(context, topLevelScope, s);
        Object[] emptyArgs = array;
        if (array == null) {
            emptyArgs = ScriptRuntime.emptyArgs;
        }
        return existingCtor.construct(context, topLevelScope, emptyArgs);
    }
    
    @Deprecated
    public static Scriptable newObjectLiteral(final Object[] array, final Object[] array2, final Context context, final Scriptable scriptable) {
        return newObjectLiteral(array, array2, null, context, scriptable);
    }
    
    public static Scriptable newObjectLiteral(final Object[] array, final Object[] array2, final int[] array3, final Context context, final Scriptable scriptable) {
        final Scriptable object = context.newObject(scriptable);
        for (int i = 0; i != array.length; ++i) {
            final Object o = array[i];
            int n;
            if (array3 == null) {
                n = 0;
            }
            else {
                n = array3[i];
            }
            final Object o2 = array2[i];
            if (o instanceof String) {
                if (n == 0) {
                    if (isSpecialProperty((String)o)) {
                        specialRef(object, (String)o, context, scriptable).set(context, scriptable, o2);
                    }
                    else {
                        object.put((String)o, object, o2);
                    }
                }
                else {
                    final ScriptableObject scriptableObject = (ScriptableObject)object;
                    final Callable callable = (Callable)o2;
                    boolean b = true;
                    if (n != 1) {
                        b = false;
                    }
                    scriptableObject.setGetterOrSetter((String)o, 0, callable, b);
                }
            }
            else {
                object.put((int)o, object, o2);
            }
        }
        return object;
    }
    
    public static Object newSpecial(final Context context, final Object o, final Object[] array, final Scriptable scriptable, final int n) {
        if (n == 1) {
            if (NativeGlobal.isEvalFunction(o)) {
                throw typeError1("msg.not.ctor", "eval");
            }
        }
        else {
            if (n != 2) {
                throw Kit.codeBug();
            }
            if (NativeWith.isWithFunction(o)) {
                return NativeWith.newWithSpecial(context, scriptable, array);
            }
        }
        return newObject(o, context, scriptable, array);
    }
    
    public static RuntimeException notFoundError(final Scriptable scriptable, final String s) {
        throw constructError("ReferenceError", getMessage1("msg.is.not.defined", s));
    }
    
    public static RuntimeException notFunctionError(final Object o) {
        return notFunctionError(o, o);
    }
    
    public static RuntimeException notFunctionError(final Object o, final Object o2) {
        String string;
        if (o2 == null) {
            string = "null";
        }
        else {
            string = o2.toString();
        }
        if (o == Scriptable.NOT_FOUND) {
            return typeError1("msg.function.not.found", string);
        }
        return typeError2("msg.isnt.function", string, typeof(o));
    }
    
    public static RuntimeException notFunctionError(final Object o, final Object o2, final String s) {
        String s3;
        final String s2 = s3 = toString(o);
        if (o instanceof NativeFunction) {
            final int index = s2.indexOf(123, s2.indexOf(41));
            s3 = s2;
            if (index > -1) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s2.substring(0, index + 1));
                sb.append("...}");
                s3 = sb.toString();
            }
        }
        if (o2 == Scriptable.NOT_FOUND) {
            return typeError2("msg.function.not.found.in", s, s3);
        }
        return typeError3("msg.isnt.function.in", s, s3, typeof(o2));
    }
    
    private static RuntimeException notXmlError(final Object o) {
        throw typeError1("msg.isnt.xml.object", toString(o));
    }
    
    public static String numberToString(final double n, final int n2) {
        if (n2 < 2 || n2 > 36) {
            throw Context.reportRuntimeError1("msg.bad.radix", Integer.toString(n2));
        }
        if (n != n) {
            return "NaN";
        }
        if (n == Double.POSITIVE_INFINITY) {
            return "Infinity";
        }
        if (n == Double.NEGATIVE_INFINITY) {
            return "-Infinity";
        }
        if (n == 0.0) {
            return "0";
        }
        if (n2 != 10) {
            return DToA.JS_dtobasestr(n2, n);
        }
        final String numberToString = FastDtoa.numberToString(n);
        if (numberToString != null) {
            return numberToString;
        }
        final StringBuilder sb = new StringBuilder();
        DToA.JS_dtostr(sb, 0, 0, n);
        return sb.toString();
    }
    
    public static Object[] padArguments(final Object[] array, final int n) {
        if (n < array.length) {
            return array;
        }
        final Object[] array2 = new Object[n];
        int n2 = 0;
        int i;
        while (true) {
            i = n2;
            if (n2 >= array.length) {
                break;
            }
            array2[n2] = array[n2];
            ++n2;
        }
        while (i < n) {
            array2[i] = Undefined.instance;
            ++i;
        }
        return array2;
    }
    
    @Deprecated
    public static Object propIncrDecr(final Object o, final String s, final Context context, final int n) {
        return propIncrDecr(o, s, context, getTopCallScope(context), n);
    }
    
    public static Object propIncrDecr(final Object o, final String s, final Context context, Scriptable prototype, final int n) {
        final Scriptable objectOrNull = toObjectOrNull(context, o, prototype);
        if (objectOrNull == null) {
            throw undefReadError(o, s);
        }
        Scriptable scriptable = objectOrNull;
        do {
            final Object value = scriptable.get(s, objectOrNull);
            if (value != Scriptable.NOT_FOUND) {
                return doScriptableIncrDecr(scriptable, s, objectOrNull, value, n);
            }
            prototype = scriptable.getPrototype();
        } while ((scriptable = prototype) != null);
        objectOrNull.put(s, objectOrNull, ScriptRuntime.NaNobj);
        return ScriptRuntime.NaNobj;
    }
    
    public static EcmaError rangeError(final String s) {
        return constructError("RangeError", s);
    }
    
    public static Object refDel(final Ref ref, final Context context) {
        return wrapBoolean(ref.delete(context));
    }
    
    public static Object refGet(final Ref ref, final Context context) {
        return ref.get(context);
    }
    
    @Deprecated
    public static Object refIncrDecr(final Ref ref, final Context context, final int n) {
        return refIncrDecr(ref, context, getTopCallScope(context), n);
    }
    
    public static Object refIncrDecr(final Ref ref, final Context context, final Scriptable scriptable, final int n) {
        Object o = ref.get(context);
        final boolean b = (n & 0x2) != 0x0;
        double n2;
        if (o instanceof Number) {
            n2 = ((Number)o).doubleValue();
        }
        else {
            final double n3 = n2 = toNumber(o);
            if (b) {
                o = wrapNumber(n3);
                n2 = n3;
            }
        }
        double n4;
        if ((n & 0x1) == 0x0) {
            n4 = n2 + 1.0;
        }
        else {
            n4 = n2 - 1.0;
        }
        final Number wrapNumber = wrapNumber(n4);
        ref.set(context, scriptable, wrapNumber);
        if (b) {
            return o;
        }
        return wrapNumber;
    }
    
    @Deprecated
    public static Object refSet(final Ref ref, final Object o, final Context context) {
        return refSet(ref, o, context, getTopCallScope(context));
    }
    
    public static Object refSet(final Ref ref, final Object o, final Context context, final Scriptable scriptable) {
        return ref.set(context, scriptable, o);
    }
    
    public static Scriptable requireObjectCoercible(Scriptable scriptable, final IdFunctionObject idFunctionObject) {
        if (scriptable.getParentScope() == null) {
            scriptable = null;
        }
        if (scriptable != null && scriptable != Undefined.instance) {
            return scriptable;
        }
        throw typeError2("msg.called.null.or.undefined", idFunctionObject.getTag(), idFunctionObject.getFunctionName());
    }
    
    public static Object searchDefaultNamespace(final Context context) {
        Scriptable scriptable;
        if ((scriptable = context.currentActivationCall) == null) {
            scriptable = getTopCallScope(context);
        }
        Object property;
        while (true) {
            final Scriptable parentScope = scriptable.getParentScope();
            if (parentScope == null) {
                if ((property = ScriptableObject.getProperty(scriptable, "__default_namespace__")) == Scriptable.NOT_FOUND) {
                    return null;
                }
                break;
            }
            else {
                final Object value = scriptable.get("__default_namespace__", scriptable);
                if (value != Scriptable.NOT_FOUND) {
                    property = value;
                    break;
                }
                scriptable = parentScope;
            }
        }
        return property;
    }
    
    public static void setBuiltinProtoAndParent(final ScriptableObject scriptableObject, Scriptable topLevelScope, final TopLevel.Builtins builtins) {
        topLevelScope = ScriptableObject.getTopLevelScope(topLevelScope);
        scriptableObject.setParentScope(topLevelScope);
        scriptableObject.setPrototype(TopLevel.getBuiltinPrototype(topLevelScope, builtins));
    }
    
    public static Object setConst(final Scriptable scriptable, final Object o, final Context context, final String s) {
        if (scriptable instanceof XMLObject) {
            scriptable.put(s, scriptable, o);
            return o;
        }
        ScriptableObject.putConstProperty(scriptable, s, o);
        return o;
    }
    
    public static Object setDefaultNamespace(Object defaultXmlNamespace, final Context context) {
        Scriptable scriptable;
        if ((scriptable = context.currentActivationCall) == null) {
            scriptable = getTopCallScope(context);
        }
        defaultXmlNamespace = currentXMLLib(context).toDefaultXmlNamespace(context, defaultXmlNamespace);
        if (!scriptable.has("__default_namespace__", scriptable)) {
            ScriptableObject.defineProperty(scriptable, "__default_namespace__", defaultXmlNamespace, 6);
        }
        else {
            scriptable.put("__default_namespace__", scriptable, defaultXmlNamespace);
        }
        return Undefined.instance;
    }
    
    public static void setEnumNumbers(final Object o, final boolean enumNumbers) {
        ((IdEnumeration)o).enumNumbers = enumNumbers;
    }
    
    public static void setFunctionProtoAndParent(final BaseFunction baseFunction, final Scriptable parentScope) {
        baseFunction.setParentScope(parentScope);
        baseFunction.setPrototype(ScriptableObject.getFunctionPrototype(parentScope));
    }
    
    public static Object setName(Scriptable scriptable, final Object o, final Context context, Scriptable scriptable2, final String s) {
        if (scriptable != null) {
            ScriptableObject.putProperty(scriptable, s, o);
            return o;
        }
        if (context.hasFeature(11) || context.hasFeature(8)) {
            Context.reportWarning(getMessage1("msg.assn.create.strict", s));
        }
        scriptable2 = (scriptable = ScriptableObject.getTopLevelScope(scriptable2));
        if (context.useDynamicScope) {
            scriptable = checkDynamicScope(context.topCallScope, scriptable2);
        }
        scriptable.put(s, scriptable, o);
        return o;
    }
    
    @Deprecated
    public static Object setObjectElem(final Object o, final Object o2, final Object o3, final Context context) {
        return setObjectElem(o, o2, o3, context, getTopCallScope(context));
    }
    
    public static Object setObjectElem(final Object o, final Object o2, final Object o3, final Context context, Scriptable objectOrNull) {
        objectOrNull = toObjectOrNull(context, o, objectOrNull);
        if (objectOrNull == null) {
            throw undefWriteError(o, o2, o3);
        }
        return setObjectElem(objectOrNull, o2, o3, context);
    }
    
    public static Object setObjectElem(final Scriptable scriptable, final Object o, final Object o2, final Context context) {
        if (scriptable instanceof XMLObject) {
            ((XMLObject)scriptable).put(context, o, o2);
            return o2;
        }
        final String stringIdOrIndex = toStringIdOrIndex(context, o);
        if (stringIdOrIndex == null) {
            ScriptableObject.putProperty(scriptable, lastIndexResult(context), o2);
            return o2;
        }
        ScriptableObject.putProperty(scriptable, stringIdOrIndex, o2);
        return o2;
    }
    
    @Deprecated
    public static Object setObjectIndex(final Object o, final double n, final Object o2, final Context context) {
        return setObjectIndex(o, n, o2, context, getTopCallScope(context));
    }
    
    public static Object setObjectIndex(final Object o, final double n, final Object o2, final Context context, Scriptable objectOrNull) {
        objectOrNull = toObjectOrNull(context, o, objectOrNull);
        if (objectOrNull == null) {
            throw undefWriteError(o, String.valueOf(n), o2);
        }
        final int n2 = (int)n;
        if (n2 == n) {
            return setObjectIndex(objectOrNull, n2, o2, context);
        }
        return setObjectProp(objectOrNull, toString(n), o2, context);
    }
    
    public static Object setObjectIndex(final Scriptable scriptable, final int n, final Object o, final Context context) {
        ScriptableObject.putProperty(scriptable, n, o);
        return o;
    }
    
    @Deprecated
    public static Object setObjectProp(final Object o, final String s, final Object o2, final Context context) {
        return setObjectProp(o, s, o2, context, getTopCallScope(context));
    }
    
    public static Object setObjectProp(final Object o, final String s, final Object o2, final Context context, Scriptable objectOrNull) {
        objectOrNull = toObjectOrNull(context, o, objectOrNull);
        if (objectOrNull == null) {
            throw undefWriteError(o, s, o2);
        }
        return setObjectProp(objectOrNull, s, o2, context);
    }
    
    public static Object setObjectProp(final Scriptable scriptable, final String s, final Object o, final Context context) {
        ScriptableObject.putProperty(scriptable, s, o);
        return o;
    }
    
    public static void setObjectProtoAndParent(final ScriptableObject scriptableObject, Scriptable topLevelScope) {
        topLevelScope = ScriptableObject.getTopLevelScope(topLevelScope);
        scriptableObject.setParentScope(topLevelScope);
        scriptableObject.setPrototype(ScriptableObject.getClassPrototype(topLevelScope, scriptableObject.getClassName()));
    }
    
    public static void setRegExpProxy(final Context context, final RegExpProxy regExpProxy) {
        if (regExpProxy == null) {
            throw new IllegalArgumentException();
        }
        context.regExpProxy = regExpProxy;
    }
    
    public static boolean shallowEq(final Object o, final Object o2) {
        if (o == o2) {
            if (!(o instanceof Number)) {
                return true;
            }
            final double doubleValue = ((Number)o).doubleValue();
            return doubleValue == doubleValue;
        }
        else {
            if (o == null) {
                return false;
            }
            if (o == Undefined.instance) {
                return false;
            }
            if (o instanceof Number) {
                if (o2 instanceof Number) {
                    return ((Number)o).doubleValue() == ((Number)o2).doubleValue();
                }
            }
            else if (o instanceof CharSequence) {
                if (o2 instanceof CharSequence) {
                    return o.toString().equals(o2.toString());
                }
            }
            else if (o instanceof Boolean) {
                if (o2 instanceof Boolean) {
                    return o.equals(o2);
                }
            }
            else {
                if (!(o instanceof Scriptable)) {
                    warnAboutNonJSObject(o);
                    return o == o2;
                }
                if (o instanceof Wrapper && o2 instanceof Wrapper) {
                    return ((Wrapper)o).unwrap() == ((Wrapper)o2).unwrap();
                }
            }
            return false;
        }
    }
    
    @Deprecated
    public static Ref specialRef(final Object o, final String s, final Context context) {
        return specialRef(o, s, context, getTopCallScope(context));
    }
    
    public static Ref specialRef(final Object o, final String s, final Context context, final Scriptable scriptable) {
        return SpecialRef.createSpecial(context, scriptable, o, s);
    }
    
    private static void storeIndexResult(final Context context, final int scratchIndex) {
        context.scratchIndex = scratchIndex;
    }
    
    private static void storeScriptable(final Context context, final Scriptable scratchScriptable) {
        if (context.scratchScriptable != null) {
            throw new IllegalStateException();
        }
        context.scratchScriptable = scratchScriptable;
    }
    
    public static void storeUint32Result(final Context context, final long scratchUint32) {
        if (scratchUint32 >>> 32 != 0L) {
            throw new IllegalArgumentException();
        }
        context.scratchUint32 = scratchUint32;
    }
    
    public static Object strictSetName(final Scriptable scriptable, final Object o, final Context context, final Scriptable scriptable2, final String s) {
        if (scriptable != null) {
            ScriptableObject.putProperty(scriptable, s, o);
            return o;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Assignment to undefined \"");
        sb.append(s);
        sb.append("\" in strict mode");
        throw constructError("ReferenceError", sb.toString());
    }
    
    static double stringToNumber(final String s, int char1, final int n) {
        char c = '9';
        char c2 = 'a';
        char c3 = 'A';
        final int length = s.length();
        if (n < 10) {
            c = (char)(n + 48 - 1);
        }
        if (n > 10) {
            c2 = (char)(n + 97 - 10);
            c3 = (char)(n + 65 - 10);
        }
        double n2 = 0.0;
        int i = char1;
        final char c4 = c;
        while (i < length) {
            final char char2 = s.charAt(i);
            int n3;
            if ('0' <= char2 && char2 <= c4) {
                n3 = char2 - '0';
            }
            else if ('a' <= char2 && char2 < c2) {
                n3 = char2 - 'a' + 10;
            }
            else {
                if ('A' > char2 || char2 >= c3) {
                    break;
                }
                n3 = char2 - 'A' + 10;
            }
            n2 = n * n2 + n3;
            ++i;
        }
        if (char1 == i) {
            return ScriptRuntime.NaN;
        }
        if (n2 >= 9.007199254740992E15) {
            if (n == 10) {
                try {
                    return Double.parseDouble(s.substring(char1, i));
                }
                catch (NumberFormatException ex) {
                    return ScriptRuntime.NaN;
                }
            }
            if (n == 2 || n == 4 || n == 8 || n == 16 || n == 32) {
                int n4 = 1;
                int n5 = 0;
                int n6 = 53;
                double n7 = 0.0;
                int n8 = 0;
                int n9 = 0;
                int n10 = 0;
                int n11 = char1;
                while (true) {
                    char1 = n4;
                    int n12 = n11;
                    if (n4 == 1) {
                        if (n11 == i) {
                            break;
                        }
                        char1 = s.charAt(n11);
                        if (48 <= char1 && char1 <= 57) {
                            char1 -= 48;
                        }
                        else if (97 <= char1 && char1 <= 122) {
                            char1 -= 87;
                        }
                        else {
                            char1 -= 55;
                        }
                        n12 = n11 + 1;
                        n9 = char1;
                        char1 = n;
                    }
                    final int n13 = char1 >> 1;
                    final boolean b = (n9 & n13) != 0x0;
                    char1 = n5;
                    int n14 = 0;
                    int n15 = 0;
                    double n16 = 0.0;
                    int n17 = 0;
                    double n18 = 0.0;
                    switch (n5) {
                        default: {
                            n14 = n10;
                            char1 = n5;
                            n15 = n6;
                            n16 = n7;
                            n17 = n8;
                            n18 = n2;
                            break;
                        }
                        case 3: {
                            char1 = n5;
                            if (b) {
                                char1 = 4;
                            }
                        }
                        case 4: {
                            n16 = n7 * 2.0;
                            n14 = n10;
                            n15 = n6;
                            n17 = n8;
                            n18 = n2;
                            break;
                        }
                        case 2: {
                            n16 = 2.0;
                            char1 = 3;
                            n14 = (b ? 1 : 0);
                            n15 = n6;
                            n17 = n8;
                            n18 = n2;
                            break;
                        }
                        case 1: {
                            double n20;
                            final double n19 = n20 = n2 * 2.0;
                            if (b) {
                                n20 = n19 + 1.0;
                            }
                            final int n21 = n6 - 1;
                            n14 = n10;
                            char1 = n5;
                            n15 = n21;
                            n16 = n7;
                            n17 = n8;
                            n18 = n20;
                            if (n21 == 0) {
                                char1 = 2;
                                n14 = n10;
                                n15 = n21;
                                n16 = n7;
                                n17 = (b ? 1 : 0);
                                n18 = n20;
                                break;
                            }
                            break;
                        }
                        case 0: {
                            n14 = n10;
                            char1 = n5;
                            n15 = n6;
                            n16 = n7;
                            n17 = n8;
                            n18 = n2;
                            if (b) {
                                n15 = n6 - 1;
                                n18 = 1.0;
                                char1 = 1;
                                n17 = n8;
                                n16 = n7;
                                n14 = n10;
                                break;
                            }
                            break;
                        }
                    }
                    n10 = n14;
                    n4 = n13;
                    n11 = n12;
                    n5 = char1;
                    n6 = n15;
                    n7 = n16;
                    n8 = n17;
                    n2 = n18;
                }
                double n24 = 0.0;
                switch (n5) {
                    default: {
                        return n2;
                    }
                    case 4: {
                        double n22 = n2;
                        if (n10 != 0) {
                            n22 = n2 + 1.0;
                        }
                        return n22 * n7;
                    }
                    case 3: {
                        double n23 = n2;
                        if ((n10 & n8) != 0x0) {
                            n23 = n2 + 1.0;
                        }
                        n24 = n23 * n7;
                        break;
                    }
                    case 1:
                    case 2: {
                        return n2;
                    }
                    case 0: {
                        n24 = 0.0;
                        break;
                    }
                }
                return n24;
            }
        }
        return n2;
    }
    
    public static long testUint32String(final String s) {
        final int length = s.length();
        long n = -1L;
        int i = 1;
        if (1 <= length && length <= 10) {
            final int n2 = s.charAt(0) - '0';
            if (n2 == 0) {
                if (length == 1) {
                    n = 0L;
                }
                return n;
            }
            if (1 <= n2 && n2 <= 9) {
                long n3 = n2;
                while (i != length) {
                    final int n4 = s.charAt(i) - '0';
                    if (n4 < 0) {
                        return -1L;
                    }
                    if (n4 > 9) {
                        return -1L;
                    }
                    n3 = 10L * n3 + n4;
                    ++i;
                }
                if (n3 >>> 32 == 0L) {
                    return n3;
                }
            }
        }
        return -1L;
    }
    
    public static JavaScriptException throwCustomError(final Context context, final Scriptable scriptable, final String s, final String s2) {
        final int[] array = { 0 };
        final String sourcePositionFromStack = Context.getSourcePositionFromStack(array);
        return new JavaScriptException(context.newObject(scriptable, s, new Object[] { s2, sourcePositionFromStack, array[0] }), sourcePositionFromStack, array[0]);
    }
    
    public static JavaScriptException throwError(final Context context, final Scriptable scriptable, final String s) {
        final int[] array = { 0 };
        final String sourcePositionFromStack = Context.getSourcePositionFromStack(array);
        return new JavaScriptException(newBuiltinObject(context, scriptable, TopLevel.Builtins.Error, new Object[] { s, sourcePositionFromStack, array[0] }), sourcePositionFromStack, array[0]);
    }
    
    public static boolean toBoolean(Object defaultValue) {
        while (!(defaultValue instanceof Boolean)) {
            final boolean b = false;
            boolean b2 = false;
            if (defaultValue == null) {
                return false;
            }
            if (defaultValue == Undefined.instance) {
                return false;
            }
            if (defaultValue instanceof CharSequence) {
                if (((CharSequence)defaultValue).length() != 0) {
                    b2 = true;
                }
                return b2;
            }
            if (defaultValue instanceof Number) {
                final double doubleValue = ((Number)defaultValue).doubleValue();
                boolean b3 = b;
                if (doubleValue == doubleValue) {
                    b3 = b;
                    if (doubleValue != 0.0) {
                        b3 = true;
                    }
                }
                return b3;
            }
            if (!(defaultValue instanceof Scriptable)) {
                warnAboutNonJSObject(defaultValue);
                return true;
            }
            if (defaultValue instanceof ScriptableObject && ((ScriptableObject)defaultValue).avoidObjectDetection()) {
                return false;
            }
            if (Context.getContext().isVersionECMA1()) {
                return true;
            }
            final Object o = defaultValue = ((Scriptable)defaultValue).getDefaultValue(ScriptRuntime.BooleanClass);
            if (o instanceof Scriptable) {
                throw errorWithClassName("msg.primitive.expected", o);
            }
        }
        return (boolean)defaultValue;
    }
    
    public static CharSequence toCharSequence(final Object o) {
        if (o instanceof NativeString) {
            return ((NativeString)o).toCharSequence();
        }
        if (o instanceof CharSequence) {
            return (CharSequence)o;
        }
        return toString(o);
    }
    
    public static int toInt32(final double n) {
        return DoubleConversion.doubleToInt32(n);
    }
    
    public static int toInt32(final Object o) {
        if (o instanceof Integer) {
            return (int)o;
        }
        return toInt32(toNumber(o));
    }
    
    public static int toInt32(final Object[] array, final int n) {
        if (n < array.length) {
            return toInt32(array[n]);
        }
        return 0;
    }
    
    public static double toInteger(final double n) {
        if (n != n) {
            return 0.0;
        }
        if (n == 0.0 || n == Double.POSITIVE_INFINITY) {
            return n;
        }
        if (n == Double.NEGATIVE_INFINITY) {
            return n;
        }
        if (n > 0.0) {
            return Math.floor(n);
        }
        return Math.ceil(n);
    }
    
    public static double toInteger(final Object o) {
        return toInteger(toNumber(o));
    }
    
    public static double toInteger(final Object[] array, final int n) {
        if (n < array.length) {
            return toInteger(array[n]);
        }
        return 0.0;
    }
    
    public static Scriptable toIterator(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final boolean b) {
        if (!ScriptableObject.hasProperty(scriptable2, "__iterator__")) {
            return null;
        }
        final Object property = ScriptableObject.getProperty(scriptable2, "__iterator__");
        if (!(property instanceof Callable)) {
            throw typeError0("msg.invalid.iterator");
        }
        final Callable callable = (Callable)property;
        Boolean b2;
        if (b) {
            b2 = Boolean.TRUE;
        }
        else {
            b2 = Boolean.FALSE;
        }
        final Object call = callable.call(context, scriptable, scriptable2, new Object[] { b2 });
        if (!(call instanceof Scriptable)) {
            throw typeError0("msg.iterator.primitive");
        }
        return (Scriptable)call;
    }
    
    public static double toNumber(Object defaultValue) {
        while (!(defaultValue instanceof Number)) {
            double n = 0.0;
            if (defaultValue == null) {
                return 0.0;
            }
            if (defaultValue == Undefined.instance) {
                return ScriptRuntime.NaN;
            }
            if (defaultValue instanceof String) {
                return toNumber((String)defaultValue);
            }
            if (defaultValue instanceof CharSequence) {
                return toNumber(defaultValue.toString());
            }
            if (defaultValue instanceof Boolean) {
                if (defaultValue) {
                    n = 1.0;
                }
                return n;
            }
            if (!(defaultValue instanceof Scriptable)) {
                warnAboutNonJSObject(defaultValue);
                return ScriptRuntime.NaN;
            }
            final Object o = defaultValue = ((Scriptable)defaultValue).getDefaultValue(ScriptRuntime.NumberClass);
            if (o instanceof Scriptable) {
                throw errorWithClassName("msg.primitive.expected", o);
            }
        }
        return ((Number)defaultValue).doubleValue();
    }
    
    public static double toNumber(String substring) {
        for (int length = substring.length(), i = 0; i != length; ++i) {
            final char char1 = substring.charAt(i);
            if (!isStrWhiteSpaceChar(char1)) {
                if (char1 == '0') {
                    if (i + 2 < length) {
                        final char char2 = substring.charAt(i + 1);
                        if (char2 == 'x' || char2 == 'X') {
                            return stringToNumber(substring, i + 2, 16);
                        }
                    }
                }
                else if ((char1 == '+' || char1 == '-') && i + 3 < length && substring.charAt(i + 1) == '0') {
                    final char char3 = substring.charAt(i + 2);
                    if (char3 == 'x' || char3 == 'X') {
                        final double stringToNumber = stringToNumber(substring, i + 3, 16);
                        if (char1 == '-') {
                            return -stringToNumber;
                        }
                        return stringToNumber;
                    }
                }
                int n = length - 1;
                char char4;
                while (true) {
                    char4 = substring.charAt(n);
                    if (!isStrWhiteSpaceChar(char4)) {
                        break;
                    }
                    --n;
                }
                if (char4 == 'y') {
                    int n2 = 0;
                    Label_0224: {
                        if (char1 != '+') {
                            n2 = i;
                            if (char1 != '-') {
                                break Label_0224;
                            }
                        }
                        n2 = i + 1;
                    }
                    if (n2 + 7 != n || !substring.regionMatches(n2, "Infinity", 0, 8)) {
                        return ScriptRuntime.NaN;
                    }
                    if (char1 == '-') {
                        return Double.NEGATIVE_INFINITY;
                    }
                    return Double.POSITIVE_INFINITY;
                }
                else {
                    substring = substring.substring(i, n + 1);
                    for (int j = substring.length() - 1; j >= 0; --j) {
                        final char char5 = substring.charAt(j);
                        if (('0' > char5 || char5 > '9') && char5 != '.' && char5 != 'e' && char5 != 'E' && char5 != '+' && char5 != '-') {
                            return ScriptRuntime.NaN;
                        }
                    }
                    try {
                        return Double.parseDouble(substring);
                    }
                    catch (NumberFormatException ex) {
                        return ScriptRuntime.NaN;
                    }
                }
            }
        }
        return 0.0;
    }
    
    public static double toNumber(final Object[] array, final int n) {
        if (n < array.length) {
            return toNumber(array[n]);
        }
        return ScriptRuntime.NaN;
    }
    
    public static Scriptable toObject(final Context context, final Scriptable scriptable, final Object o) {
        if (o instanceof Scriptable) {
            return (Scriptable)o;
        }
        if (o instanceof CharSequence) {
            final NativeString nativeString = new NativeString((CharSequence)o);
            setBuiltinProtoAndParent(nativeString, scriptable, TopLevel.Builtins.String);
            return nativeString;
        }
        if (o instanceof Number) {
            final NativeNumber nativeNumber = new NativeNumber(((Number)o).doubleValue());
            setBuiltinProtoAndParent(nativeNumber, scriptable, TopLevel.Builtins.Number);
            return nativeNumber;
        }
        if (o instanceof Boolean) {
            final NativeBoolean nativeBoolean = new NativeBoolean((boolean)o);
            setBuiltinProtoAndParent(nativeBoolean, scriptable, TopLevel.Builtins.Boolean);
            return nativeBoolean;
        }
        if (o == null) {
            throw typeError0("msg.null.to.object");
        }
        if (o == Undefined.instance) {
            throw typeError0("msg.undef.to.object");
        }
        final Object wrap = context.getWrapFactory().wrap(context, scriptable, o, null);
        if (wrap instanceof Scriptable) {
            return (Scriptable)wrap;
        }
        throw errorWithClassName("msg.invalid.type", o);
    }
    
    @Deprecated
    public static Scriptable toObject(final Context context, final Scriptable scriptable, final Object o, final Class<?> clazz) {
        return toObject(context, scriptable, o);
    }
    
    public static Scriptable toObject(final Scriptable scriptable, final Object o) {
        if (o instanceof Scriptable) {
            return (Scriptable)o;
        }
        return toObject(Context.getContext(), scriptable, o);
    }
    
    @Deprecated
    public static Scriptable toObject(final Scriptable scriptable, final Object o, final Class<?> clazz) {
        if (o instanceof Scriptable) {
            return (Scriptable)o;
        }
        return toObject(Context.getContext(), scriptable, o);
    }
    
    @Deprecated
    public static Scriptable toObjectOrNull(final Context context, final Object o) {
        if (o instanceof Scriptable) {
            return (Scriptable)o;
        }
        if (o != null && o != Undefined.instance) {
            return toObject(context, getTopCallScope(context), o);
        }
        return null;
    }
    
    public static Scriptable toObjectOrNull(final Context context, final Object o, final Scriptable scriptable) {
        if (o instanceof Scriptable) {
            return (Scriptable)o;
        }
        if (o != null && o != Undefined.instance) {
            return toObject(context, scriptable, o);
        }
        return null;
    }
    
    public static Object toPrimitive(final Object o) {
        return toPrimitive(o, null);
    }
    
    public static Object toPrimitive(Object defaultValue, final Class<?> clazz) {
        if (!(defaultValue instanceof Scriptable)) {
            return defaultValue;
        }
        defaultValue = ((Scriptable)defaultValue).getDefaultValue(clazz);
        if (defaultValue instanceof Scriptable) {
            throw typeError0("msg.bad.default.value");
        }
        return defaultValue;
    }
    
    public static String toString(final double n) {
        return numberToString(n, 10);
    }
    
    public static String toString(Object defaultValue) {
        while (defaultValue != null) {
            if (defaultValue == Undefined.instance) {
                return "undefined";
            }
            if (defaultValue instanceof String) {
                return (String)defaultValue;
            }
            if (defaultValue instanceof CharSequence) {
                return defaultValue.toString();
            }
            if (defaultValue instanceof Number) {
                return numberToString(((Number)defaultValue).doubleValue(), 10);
            }
            if (!(defaultValue instanceof Scriptable)) {
                return defaultValue.toString();
            }
            final Object o = defaultValue = ((Scriptable)defaultValue).getDefaultValue(ScriptRuntime.StringClass);
            if (o instanceof Scriptable) {
                throw errorWithClassName("msg.primitive.expected", o);
            }
        }
        return "null";
    }
    
    public static String toString(final Object[] array, final int n) {
        if (n < array.length) {
            return toString(array[n]);
        }
        return "undefined";
    }
    
    static String toStringIdOrIndex(final Context context, final Object o) {
        if (o instanceof Number) {
            final double doubleValue = ((Number)o).doubleValue();
            final int n = (int)doubleValue;
            if (n == doubleValue) {
                storeIndexResult(context, n);
                return null;
            }
            return toString(o);
        }
        else {
            String string;
            if (o instanceof String) {
                string = (String)o;
            }
            else {
                string = toString(o);
            }
            final long indexFromString = indexFromString(string);
            if (indexFromString >= 0L) {
                storeIndexResult(context, (int)indexFromString);
                return null;
            }
            return string;
        }
    }
    
    public static char toUint16(final Object o) {
        return (char)DoubleConversion.doubleToInt32(toNumber(o));
    }
    
    public static long toUint32(final double n) {
        return (long)DoubleConversion.doubleToInt32(n) & 0xFFFFFFFFL;
    }
    
    public static long toUint32(final Object o) {
        return toUint32(toNumber(o));
    }
    
    private static Object topScopeName(final Context context, final Scriptable scriptable, final String s) {
        Scriptable checkDynamicScope = scriptable;
        if (context.useDynamicScope) {
            checkDynamicScope = checkDynamicScope(context.topCallScope, scriptable);
        }
        return ScriptableObject.getProperty(checkDynamicScope, s);
    }
    
    public static EcmaError typeError(final String s) {
        return constructError("TypeError", s);
    }
    
    public static EcmaError typeError0(final String s) {
        return typeError(getMessage0(s));
    }
    
    public static EcmaError typeError1(final String s, final Object o) {
        return typeError(getMessage1(s, o));
    }
    
    public static EcmaError typeError2(final String s, final Object o, final Object o2) {
        return typeError(getMessage2(s, o, o2));
    }
    
    public static EcmaError typeError3(final String s, final String s2, final String s3, final String s4) {
        return typeError(getMessage3(s, s2, s3, s4));
    }
    
    @Deprecated
    public static BaseFunction typeErrorThrower() {
        return typeErrorThrower(Context.getCurrentContext());
    }
    
    public static BaseFunction typeErrorThrower(final Context context) {
        if (context.typeErrorThrower == null) {
            final BaseFunction typeErrorThrower = new BaseFunction() {
                static final long serialVersionUID = -5891740962154902286L;
                
                @Override
                public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
                    throw ScriptRuntime.typeError0("msg.op.not.allowed");
                }
                
                @Override
                public int getLength() {
                    return 0;
                }
            };
            setFunctionProtoAndParent(typeErrorThrower, context.topCallScope);
            typeErrorThrower.preventExtensions();
            context.typeErrorThrower = typeErrorThrower;
        }
        return context.typeErrorThrower;
    }
    
    public static String typeof(final Object o) {
        if (o == null) {
            return "object";
        }
        if (o == Undefined.instance) {
            return "undefined";
        }
        if (o instanceof ScriptableObject) {
            return ((ScriptableObject)o).getTypeOf();
        }
        if (o instanceof Scriptable) {
            if (o instanceof Callable) {
                return "function";
            }
            return "object";
        }
        else {
            if (o instanceof CharSequence) {
                return "string";
            }
            if (o instanceof Number) {
                return "number";
            }
            if (o instanceof Boolean) {
                return "boolean";
            }
            throw errorWithClassName("msg.invalid.type", o);
        }
    }
    
    public static String typeofName(Scriptable bind, final String s) {
        final Context context = Context.getContext();
        bind = bind(context, bind, s);
        if (bind == null) {
            return "undefined";
        }
        return typeof(getObjectProp(bind, s, context));
    }
    
    public static RuntimeException undefCallError(final Object o, final Object o2) {
        return typeError2("msg.undef.method.call", toString(o), toString(o2));
    }
    
    private static RuntimeException undefDeleteError(final Object o, final Object o2) {
        throw typeError2("msg.undef.prop.delete", toString(o), toString(o2));
    }
    
    public static RuntimeException undefReadError(final Object o, final Object o2) {
        return typeError2("msg.undef.prop.read", toString(o), toString(o2));
    }
    
    public static RuntimeException undefWriteError(final Object o, final Object o2, final Object o3) {
        return typeError3("msg.undef.prop.write", toString(o), toString(o2), toString(o3));
    }
    
    static String uneval(final Context context, final Scriptable scriptable, final Object o) {
        if (o == null) {
            return "null";
        }
        if (o == Undefined.instance) {
            return "undefined";
        }
        if (o instanceof CharSequence) {
            final String escapeString = escapeString(o.toString());
            final StringBuilder sb = new StringBuilder(escapeString.length() + 2);
            sb.append('\"');
            sb.append(escapeString);
            sb.append('\"');
            return sb.toString();
        }
        if (o instanceof Number) {
            final double doubleValue = ((Number)o).doubleValue();
            if (doubleValue == 0.0 && 1.0 / doubleValue < 0.0) {
                return "-0";
            }
            return toString(doubleValue);
        }
        else {
            if (o instanceof Boolean) {
                return toString(o);
            }
            if (o instanceof Scriptable) {
                final Scriptable scriptable2 = (Scriptable)o;
                if (ScriptableObject.hasProperty(scriptable2, "toSource")) {
                    final Object property = ScriptableObject.getProperty(scriptable2, "toSource");
                    if (property instanceof Function) {
                        return toString(((Function)property).call(context, scriptable, scriptable2, ScriptRuntime.emptyArgs));
                    }
                }
                return toString(o);
            }
            warnAboutNonJSObject(o);
            return o.toString();
        }
    }
    
    public static Object updateDotQuery(final boolean b, final Scriptable scriptable) {
        return ((NativeWith)scriptable).updateDotQuery(b);
    }
    
    private static void warnAboutNonJSObject(final Object o) {
        final StringBuilder sb = new StringBuilder();
        sb.append("RHINO USAGE WARNING: Missed Context.javaToJS() conversion:\nRhino runtime detected object ");
        sb.append(o);
        sb.append(" of class ");
        sb.append(o.getClass().getName());
        sb.append(" where it expected String, Number, Boolean or Scriptable instance. Please check your code for missing Context.javaToJS() call.");
        final String string = sb.toString();
        Context.reportWarning(string);
        System.err.println(string);
    }
    
    public static Boolean wrapBoolean(final boolean b) {
        if (b) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    public static Scriptable wrapException(final Throwable t, final Scriptable scriptable, final Context context) {
        Throwable wrappedException = null;
        EcmaError stackProvider;
        String name;
        String s;
        if (t instanceof EcmaError) {
            final EcmaError ecmaError = stackProvider = (EcmaError)t;
            name = ecmaError.getName();
            s = ecmaError.getErrorMessage();
        }
        else if (t instanceof WrappedException) {
            wrappedException = ((WrappedException)(stackProvider = (EcmaError)t)).getWrappedException();
            name = "JavaException";
            final StringBuilder sb = new StringBuilder();
            sb.append(wrappedException.getClass().getName());
            sb.append(": ");
            sb.append(wrappedException.getMessage());
            s = sb.toString();
        }
        else if (t instanceof EvaluatorException) {
            final EvaluatorException ex = (EvaluatorException)(stackProvider = (EcmaError)t);
            name = "InternalError";
            s = ex.getMessage();
        }
        else {
            if (!context.hasFeature(13)) {
                throw Kit.codeBug();
            }
            final WrappedException ex2 = new WrappedException(t);
            name = "JavaException";
            final String string = t.toString();
            stackProvider = (EcmaError)ex2;
            s = string;
        }
        String sourceName;
        if ((sourceName = stackProvider.sourceName()) == null) {
            sourceName = "";
        }
        final int lineNumber = stackProvider.lineNumber();
        Object[] array;
        if (lineNumber > 0) {
            array = new Object[] { s, sourceName, lineNumber };
        }
        else {
            array = new Object[] { s, sourceName };
        }
        final Scriptable object = context.newObject(scriptable, name, array);
        ScriptableObject.putProperty(object, "name", name);
        if (object instanceof NativeError) {
            ((NativeError)object).setStackProvider(stackProvider);
        }
        if (wrappedException != null && isVisible(context, wrappedException)) {
            ScriptableObject.defineProperty(object, "javaException", context.getWrapFactory().wrap(context, scriptable, wrappedException, null), 7);
        }
        if (isVisible(context, stackProvider)) {
            ScriptableObject.defineProperty(object, "rhinoException", context.getWrapFactory().wrap(context, scriptable, stackProvider, null), 7);
        }
        return object;
    }
    
    public static Integer wrapInt(final int n) {
        return n;
    }
    
    public static Number wrapNumber(final double n) {
        if (n != n) {
            return ScriptRuntime.NaNobj;
        }
        return new Double(n);
    }
    
    public static Scriptable wrapRegExp(final Context context, final Scriptable scriptable, final Object o) {
        return context.getRegExpProxy().wrapRegExp(context, scriptable, o);
    }
    
    private static class DefaultMessageProvider implements MessageProvider
    {
        private Properties properties;
        
        private DefaultMessageProvider() {
            this.properties = new Properties();
        }
        
        @Override
        public String getMessage(final String s, final Object[] array) {
            try {
                if (this.properties.isEmpty()) {
                    this.properties.load(FileTools.getAssetInputStream("rhino/Messages.properties"));
                }
                return new MessageFormat(this.properties.getProperty(s)).format(array);
            }
            catch (IOException ex) {
                throw new RuntimeException("Unable to load messages from assets; ", ex);
            }
            catch (MissingResourceException ex2) {
                final StringBuilder sb = new StringBuilder();
                sb.append("no message resource found for message property ");
                sb.append(s);
                throw new RuntimeException(sb.toString());
            }
        }
    }
    
    private static class IdEnumeration implements Serializable
    {
        private static final long serialVersionUID = 1L;
        Object currentId;
        boolean enumNumbers;
        int enumType;
        Object[] ids;
        int index;
        Scriptable iterator;
        Scriptable obj;
        ObjToIntMap used;
    }
    
    public interface MessageProvider
    {
        String getMessage(final String p0, final Object[] p1);
    }
    
    static class NoSuchMethodShim implements Callable
    {
        String methodName;
        Callable noSuchMethodMethod;
        
        NoSuchMethodShim(final Callable noSuchMethodMethod, final String methodName) {
            this.noSuchMethodMethod = noSuchMethodMethod;
            this.methodName = methodName;
        }
        
        @Override
        public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
            return this.noSuchMethodMethod.call(context, scriptable, scriptable2, new Object[] { this.methodName, ScriptRuntime.newArrayLiteral(array, null, context, scriptable) });
        }
    }
}
