package org.mozilla.javascript;

import java.lang.reflect.*;
import java.util.*;

class JavaMembers
{
    private Class<?> cl;
    NativeJavaMethod ctors;
    private Map<String, FieldAndMethods> fieldAndMethods;
    private Map<String, Object> members;
    private Map<String, FieldAndMethods> staticFieldAndMethods;
    private Map<String, Object> staticMembers;
    
    JavaMembers(final Scriptable scriptable, final Class<?> clazz) {
        this(scriptable, clazz, false);
    }
    
    JavaMembers(final Scriptable scriptable, final Class<?> cl, final boolean b) {
        try {
            final Context enterContext = ContextFactory.getGlobal().enterContext();
            final ClassShutter classShutter = enterContext.getClassShutter();
            if (classShutter != null && !classShutter.visibleToScripts(cl.getName())) {
                throw Context.reportRuntimeError1("msg.access.prohibited", cl.getName());
            }
            this.members = new HashMap<String, Object>();
            this.staticMembers = new HashMap<String, Object>();
            this.cl = cl;
            this.reflect(scriptable, b, enterContext.hasFeature(13));
        }
        finally {
            Context.exit();
        }
    }
    
    private static void discoverAccessibleMethods(final Class<?> clazz, final Map<MethodSignature, Method> map, final boolean b, final boolean b2) {
        final boolean public1 = Modifier.isPublic(clazz.getModifiers());
        final int n = 0;
        Class<?> clazz2 = null;
        Label_0383: {
            if (!public1) {
                clazz2 = clazz;
                if (!b2) {
                    break Label_0383;
                }
            }
            Class<?> superclass = clazz;
            Label_0344: {
                if (!b) {
                    if (b2) {
                        superclass = clazz;
                    }
                    else {
                        clazz2 = clazz;
                        try {
                            final Method[] methods = clazz.getMethods();
                            clazz2 = clazz;
                            for (int length = methods.length, i = 0; i < length; ++i) {
                                final Method method = methods[i];
                                clazz2 = clazz;
                                final MethodSignature methodSignature = new MethodSignature(method);
                                clazz2 = clazz;
                                if (!map.containsKey(methodSignature)) {
                                    clazz2 = clazz;
                                    map.put(methodSignature, method);
                                }
                            }
                            return;
                        }
                        catch (SecurityException ex) {
                            break Label_0344;
                        }
                    }
                }
                while (superclass != null) {
                    while (true) {
                        while (true) {
                            int n2 = 0;
                            Label_0442: {
                                try {
                                    final Method[] declaredMethods = superclass.getDeclaredMethods();
                                    final int length2 = declaredMethods.length;
                                    n2 = 0;
                                    if (n2 >= length2) {
                                        superclass = superclass.getSuperclass();
                                        break;
                                    }
                                    final Method method2 = declaredMethods[n2];
                                    final int modifiers = method2.getModifiers();
                                    if (!Modifier.isPublic(modifiers) && !Modifier.isProtected(modifiers) && !b2) {
                                        break Label_0442;
                                    }
                                    final MethodSignature methodSignature2 = new MethodSignature(method2);
                                    if (!map.containsKey(methodSignature2)) {
                                        if (b2 && !method2.isAccessible()) {
                                            method2.setAccessible(true);
                                        }
                                        map.put(methodSignature2, method2);
                                    }
                                    break Label_0442;
                                }
                                catch (SecurityException ex2) {
                                    final Method[] methods2 = superclass.getMethods();
                                    for (int length3 = methods2.length, j = 0; j < length3; ++j) {
                                        final Method method3 = methods2[j];
                                        final MethodSignature methodSignature3 = new MethodSignature(method3);
                                        if (!map.containsKey(methodSignature3)) {
                                            map.put(methodSignature3, method3);
                                        }
                                    }
                                    return;
                                }
                                break Label_0344;
                            }
                            ++n2;
                            continue;
                        }
                    }
                }
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not discover accessible methods of class ");
            sb.append(clazz2.getName());
            sb.append(" due to lack of privileges, attemping superclasses/interfaces.");
            Context.reportWarning(sb.toString());
        }
        final Class<?>[] interfaces = clazz2.getInterfaces();
        for (int length4 = interfaces.length, k = n; k < length4; ++k) {
            discoverAccessibleMethods(interfaces[k], map, b, b2);
        }
        final Class<?> superclass2 = clazz2.getSuperclass();
        if (superclass2 != null) {
            discoverAccessibleMethods(superclass2, map, b, b2);
        }
    }
    
    private static Method[] discoverAccessibleMethods(final Class<?> clazz, final boolean b, final boolean b2) {
        final HashMap<MethodSignature, Object> hashMap = new HashMap<MethodSignature, Object>();
        discoverAccessibleMethods(clazz, (Map<MethodSignature, Method>)hashMap, b, b2);
        return hashMap.values().toArray(new Method[hashMap.size()]);
    }
    
    private static MemberBox extractGetMethod(final MemberBox[] array, final boolean b) {
        final int length = array.length;
        int i = 0;
        while (i < length) {
            final MemberBox memberBox = array[i];
            if (memberBox.argTypes.length == 0 && (!b || memberBox.isStatic())) {
                if (memberBox.method().getReturnType() != Void.TYPE) {
                    return memberBox;
                }
                break;
            }
            else {
                ++i;
            }
        }
        return null;
    }
    
    private static MemberBox extractSetMethod(final Class<?> clazz, final MemberBox[] array, final boolean b) {
        for (int i = 1; i <= 2; ++i) {
            for (int length = array.length, j = 0; j < length; ++j) {
                final MemberBox memberBox = array[j];
                if (!b || memberBox.isStatic()) {
                    final Class<?>[] argTypes = memberBox.argTypes;
                    if (argTypes.length == 1) {
                        if (i == 1) {
                            if (argTypes[0] == clazz) {
                                return memberBox;
                            }
                        }
                        else {
                            if (i != 2) {
                                Kit.codeBug();
                            }
                            if (argTypes[0].isAssignableFrom(clazz)) {
                                return memberBox;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private static MemberBox extractSetMethod(final MemberBox[] array, final boolean b) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final MemberBox memberBox = array[i];
            if ((!b || memberBox.isStatic()) && memberBox.method().getReturnType() == Void.TYPE && memberBox.argTypes.length == 1) {
                return memberBox;
            }
        }
        return null;
    }
    
    private MemberBox findExplicitFunction(final String s, final boolean b) {
        final int index = s.indexOf(40);
        if (index < 0) {
            return null;
        }
        Map<String, Object> map;
        if (b) {
            map = this.staticMembers;
        }
        else {
            map = this.members;
        }
        final MemberBox[] array = null;
        MemberBox[] array2;
        if (b && index == 0) {
            array2 = this.ctors.methods;
        }
        else {
            final String substring = s.substring(0, index);
            Object o;
            final NativeJavaMethod nativeJavaMethod = (NativeJavaMethod)(o = map.get(substring));
            if (!b && (o = nativeJavaMethod) == null) {
                o = this.staticMembers.get(substring);
            }
            array2 = array;
            if (o instanceof NativeJavaMethod) {
                array2 = ((NativeJavaMethod)o).methods;
            }
        }
        if (array2 != null) {
            for (int length = array2.length, i = 0; i < length; ++i) {
                final MemberBox memberBox = array2[i];
                final String liveConnectSignature = liveConnectSignature(memberBox.argTypes);
                if (liveConnectSignature.length() + index == s.length() && s.regionMatches(index, liveConnectSignature, 0, liveConnectSignature.length())) {
                    return memberBox;
                }
            }
        }
        return null;
    }
    
    private MemberBox findGetter(final boolean b, final Map<String, Object> map, String concat, final String s) {
        concat = concat.concat(s);
        if (map.containsKey(concat)) {
            final NativeJavaMethod value = map.get(concat);
            if (value instanceof NativeJavaMethod) {
                return extractGetMethod(value.methods, b);
            }
        }
        return null;
    }
    
    private Constructor<?>[] getAccessibleConstructors(final boolean b) {
        if (b && this.cl != ScriptRuntime.ClassClass) {
            try {
                final Constructor<?>[] declaredConstructors = this.cl.getDeclaredConstructors();
                AccessibleObject.setAccessible(declaredConstructors, true);
                return declaredConstructors;
            }
            catch (SecurityException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Could not access constructor  of class ");
                sb.append(this.cl.getName());
                sb.append(" due to lack of privileges.");
                Context.reportWarning(sb.toString());
            }
        }
        return this.cl.getConstructors();
    }
    
    private Field[] getAccessibleFields(final boolean b, final boolean b2) {
        if (!b2) {
            if (!b) {
                return this.cl.getFields();
            }
        }
        while (true) {
            while (true) {
                int n = 0;
                Label_0153: {
                    try {
                        final ArrayList<Field> list = new ArrayList<Field>();
                        Class<?> clazz = this.cl;
                        while (clazz != null) {
                            final Field[] declaredFields = clazz.getDeclaredFields();
                            final int length = declaredFields.length;
                            n = 0;
                            if (n < length) {
                                final Field field = declaredFields[n];
                                final int modifiers = field.getModifiers();
                                if (b2 || Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
                                    if (!field.isAccessible()) {
                                        field.setAccessible(true);
                                    }
                                    list.add(field);
                                }
                                break Label_0153;
                            }
                            else {
                                clazz = clazz.getSuperclass();
                            }
                        }
                        return list.toArray(new Field[list.size()]);
                    }
                    catch (SecurityException ex) {}
                    break;
                }
                ++n;
                continue;
            }
        }
        return this.cl.getFields();
    }
    
    private Object getExplicitFunction(final Scriptable scriptable, final String s, Object value, final boolean b) {
        Map<String, Object> map;
        if (b) {
            map = this.staticMembers;
        }
        else {
            map = this.members;
        }
        value = null;
        final MemberBox explicitFunction = this.findExplicitFunction(s, b);
        if (explicitFunction != null) {
            final Scriptable functionPrototype = ScriptableObject.getFunctionPrototype(scriptable);
            if (explicitFunction.isCtor()) {
                final NativeJavaConstructor nativeJavaConstructor = new NativeJavaConstructor(explicitFunction);
                nativeJavaConstructor.setPrototype(functionPrototype);
                map.put(s, nativeJavaConstructor);
                return nativeJavaConstructor;
            }
            final Object o = value = map.get(explicitFunction.getName());
            if (o instanceof NativeJavaMethod) {
                value = o;
                if (((NativeJavaMethod)o).methods.length > 1) {
                    value = new NativeJavaMethod(explicitFunction, s);
                    ((ScriptableObject)value).setPrototype(functionPrototype);
                    map.put(s, value);
                }
            }
        }
        return value;
    }
    
    static String javaSignature(Class<?> clazz) {
        if (!clazz.isArray()) {
            return clazz.getName();
        }
        int n = 0;
        Class componentType;
        int i;
        do {
            i = n + 1;
            componentType = clazz.getComponentType();
            n = i;
            clazz = componentType;
        } while (componentType.isArray());
        final String name = componentType.getName();
        if (i == 1) {
            return name.concat("[]");
        }
        final StringBuilder sb = new StringBuilder(name.length() + "[]".length() * i);
        sb.append(name);
        while (i != 0) {
            --i;
            sb.append("[]");
        }
        return sb.toString();
    }
    
    static String liveConnectSignature(final Class<?>[] array) {
        final int length = array.length;
        if (length == 0) {
            return "()";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (int i = 0; i != length; ++i) {
            if (i != 0) {
                sb.append(',');
            }
            sb.append(javaSignature(array[i]));
        }
        sb.append(')');
        return sb.toString();
    }
    
    static JavaMembers lookupClass(Scriptable scriptable, final Class<?> clazz, final Class<?> scriptable2, final boolean b) {
        final ClassCache value = ClassCache.get(scriptable);
        final Map<Class<?>, JavaMembers> classCacheMap = value.getClassCacheMap();
        scriptable = scriptable2;
        Object o = clazz;
        JavaMembers javaMembers;
        while (true) {
            javaMembers = classCacheMap.get(o);
            if (javaMembers != null) {
                break;
            }
            try {
                final JavaMembers javaMembers2 = new JavaMembers(value.getAssociatedScope(), (Class<?>)o, b);
                if (value.isCachingEnabled()) {
                    classCacheMap.put((Class<?>)o, javaMembers2);
                    if (o != clazz) {
                        classCacheMap.put(clazz, javaMembers2);
                    }
                }
                return javaMembers2;
            }
            catch (SecurityException ex) {
                Scriptable scriptable3;
                Object o2;
                if (scriptable != null && ((Class)scriptable).isInterface()) {
                    scriptable3 = null;
                    o2 = scriptable;
                }
                else {
                    if ((o2 = ((Class<?>)o).getSuperclass()) == null) {
                        if (!((Class)o).isInterface()) {
                            throw ex;
                        }
                        o2 = ScriptRuntime.ObjectClass;
                    }
                    scriptable3 = scriptable;
                }
                scriptable = scriptable3;
                o = o2;
            }
        }
        if (o != clazz) {
            classCacheMap.put(clazz, javaMembers);
        }
        return javaMembers;
    }
    
    private void reflect(final Scriptable scriptable, final boolean b, final boolean b2) {
        final Method[] discoverAccessibleMethods = discoverAccessibleMethods(this.cl, b, b2);
        for (int length = discoverAccessibleMethods.length, i = 0; i < length; ++i) {
            final Method method = discoverAccessibleMethods[i];
            Map<String, Object> map;
            if (Modifier.isStatic(method.getModifiers())) {
                map = this.staticMembers;
            }
            else {
                map = this.members;
            }
            final String name = method.getName();
            final Method override = MembersPatch.override(this.cl, method);
            final ObjArray value = map.get(name);
            if (value == null) {
                map.put(name, override);
            }
            else {
                ObjArray objArray;
                if (value instanceof ObjArray) {
                    objArray = value;
                }
                else {
                    if (!(value instanceof Method)) {
                        Kit.codeBug();
                    }
                    final ObjArray objArray2 = new ObjArray();
                    objArray2.add(value);
                    map.put(name, objArray2);
                    objArray = objArray2;
                }
                objArray.add(override);
            }
        }
        for (int j = 0; j != 2; ++j) {
            Map<String, Object> map2;
            if (j == 0) {
                map2 = this.staticMembers;
            }
            else {
                map2 = this.members;
            }
            for (final Map.Entry<String, NativeJavaMethod> entry : map2.entrySet()) {
                final NativeJavaMethod value2 = entry.getValue();
                MemberBox[] array;
                if (value2 instanceof Method) {
                    array = new MemberBox[] { new MemberBox((Method)value2) };
                }
                else {
                    final ObjArray objArray3 = (ObjArray)value2;
                    final int size = objArray3.size();
                    if (size < 2) {
                        Kit.codeBug();
                    }
                    final MemberBox[] array2 = new MemberBox[size];
                    for (int k = 0; k != size; ++k) {
                        array2[k] = new MemberBox((Method)objArray3.get(k));
                    }
                    array = array2;
                }
                final NativeJavaMethod nativeJavaMethod = new NativeJavaMethod(array);
                if (scriptable != null) {
                    ScriptRuntime.setFunctionProtoAndParent(nativeJavaMethod, scriptable);
                }
                map2.put(entry.getKey(), nativeJavaMethod);
            }
        }
        final Field[] accessibleFields = this.getAccessibleFields(b, b2);
        for (int length2 = accessibleFields.length, l = 0; l < length2; ++l) {
            while (true) {
                final Field field = accessibleFields[l];
                final String name2 = field.getName();
                final int modifiers = field.getModifiers();
                while (true) {
                    Label_1428: {
                        try {
                            final boolean static1 = Modifier.isStatic(modifiers);
                            Map<String, Object> map3;
                            if (static1) {
                                map3 = this.staticMembers;
                            }
                            else {
                                map3 = this.members;
                            }
                            final Object value3 = map3.get(name2);
                            if (value3 == null) {
                                map3.put(name2, field);
                            }
                            else if (value3 instanceof NativeJavaMethod) {
                                final FieldAndMethods fieldAndMethods = new FieldAndMethods(scriptable, ((NativeJavaMethod)value3).methods, field);
                                Map<String, FieldAndMethods> map4;
                                if (static1) {
                                    map4 = this.staticFieldAndMethods;
                                }
                                else {
                                    map4 = this.fieldAndMethods;
                                }
                                Map<String, FieldAndMethods> map5 = map4;
                                if (map4 == null) {
                                    map5 = new HashMap<String, FieldAndMethods>();
                                    if (static1) {
                                        this.staticFieldAndMethods = map5;
                                    }
                                    else {
                                        this.fieldAndMethods = map5;
                                    }
                                }
                                map5.put(name2, fieldAndMethods);
                                map3.put(name2, fieldAndMethods);
                            }
                            else if (value3 instanceof Field) {
                                if (((Field)value3).getDeclaringClass().isAssignableFrom(field.getDeclaringClass())) {
                                    map3.put(name2, field);
                                    break Label_1428;
                                }
                                break Label_1428;
                            }
                            else {
                                Kit.codeBug();
                            }
                        }
                        catch (SecurityException ex) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Could not access field ");
                            sb.append(name2);
                            sb.append(" of class ");
                            sb.append(this.cl.getName());
                            sb.append(" due to lack of privileges.");
                            Context.reportWarning(sb.toString());
                        }
                        break;
                    }
                    continue;
                }
            }
        }
        for (int n = 0; n != 2; ++n) {
            final boolean b3 = n == 0;
            Map<String, Object> map6;
            if (b3) {
                map6 = this.staticMembers;
            }
            else {
                map6 = this.members;
            }
            final HashMap<String, Member> hashMap = new HashMap<String, Member>();
            for (final String s : map6.keySet()) {
                final boolean startsWith = s.startsWith("get");
                final boolean startsWith2 = s.startsWith("set");
                final boolean startsWith3 = s.startsWith("is");
                if (!startsWith && !startsWith3 && !startsWith2) {
                    continue;
                }
                int n2;
                if (startsWith3) {
                    n2 = 2;
                }
                else {
                    n2 = 3;
                }
                final String substring = s.substring(n2);
                if (substring.length() == 0) {
                    continue;
                }
                String s2 = substring;
                final char char1 = substring.charAt(0);
                if (Character.isUpperCase(char1)) {
                    if (substring.length() == 1) {
                        s2 = substring.toLowerCase();
                    }
                    else if (!Character.isUpperCase(substring.charAt(1))) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(Character.toLowerCase(char1));
                        sb2.append(substring.substring(1));
                        s2 = sb2.toString();
                    }
                }
                if (hashMap.containsKey(s2)) {
                    continue;
                }
                final NativeJavaMethod value4 = map6.get(s2);
                if (value4 != null) {
                    if (!b2 || !(value4 instanceof Member)) {
                        continue;
                    }
                    if (!Modifier.isPrivate(((Member)value4).getModifiers())) {
                        continue;
                    }
                }
                MemberBox memberBox;
                if ((memberBox = this.findGetter(b3, map6, "get", substring)) == null) {
                    memberBox = this.findGetter(b3, map6, "is", substring);
                }
                NativeJavaMethod nativeJavaMethod2 = null;
                final String concat = "set".concat(substring);
                MemberBox memberBox3 = null;
                Label_1266: {
                    if (map6.containsKey(concat)) {
                        final NativeJavaMethod value5 = map6.get(concat);
                        if (value5 instanceof NativeJavaMethod) {
                            final NativeJavaMethod nativeJavaMethod3 = value5;
                            MemberBox memberBox2;
                            if (memberBox != null) {
                                memberBox2 = extractSetMethod(memberBox.method().getReturnType(), nativeJavaMethod3.methods, b3);
                            }
                            else {
                                memberBox2 = extractSetMethod(nativeJavaMethod3.methods, b3);
                            }
                            memberBox3 = memberBox2;
                            if (nativeJavaMethod3.methods.length > 1) {
                                nativeJavaMethod2 = nativeJavaMethod3;
                                memberBox3 = memberBox2;
                            }
                            break Label_1266;
                        }
                    }
                    memberBox3 = null;
                }
                hashMap.put(s2, (Member)new BeanProperty(memberBox, memberBox3, nativeJavaMethod2));
            }
            for (final String s3 : hashMap.keySet()) {
                map6.put(s3, hashMap.get(s3));
            }
        }
        final Constructor<?>[] accessibleConstructors = this.getAccessibleConstructors(b2);
        final MemberBox[] array3 = new MemberBox[accessibleConstructors.length];
        for (int n3 = 0; n3 != accessibleConstructors.length; ++n3) {
            array3[n3] = new MemberBox(accessibleConstructors[n3]);
        }
        this.ctors = new NativeJavaMethod(array3, this.cl.getSimpleName());
    }
    
    Object get(Scriptable topLevelScope, final String s, Object o, final boolean b) {
        Map<String, Object> map;
        if (b) {
            map = this.staticMembers;
        }
        else {
            map = this.members;
        }
        Object o2 = map.get(s);
        if (!b && (o2 = o2) == null) {
            o2 = this.staticMembers.get(s);
        }
        Object explicitFunction;
        if ((explicitFunction = o2) == null && (explicitFunction = this.getExplicitFunction(topLevelScope, s, o, b)) == null) {
            return Scriptable.NOT_FOUND;
        }
        if (explicitFunction instanceof Scriptable) {
            return explicitFunction;
        }
        while (true) {
            final Context context = Context.getContext();
            while (true) {
                Label_0212: {
                    try {
                        Class<?> clazz;
                        if (explicitFunction instanceof BeanProperty) {
                            final BeanProperty beanProperty = (BeanProperty)explicitFunction;
                            if (beanProperty.getter == null) {
                                return Scriptable.NOT_FOUND;
                            }
                            o = beanProperty.getter.invoke(o, Context.emptyArgs);
                            clazz = beanProperty.getter.method().getReturnType();
                        }
                        else {
                            final Field field = (Field)explicitFunction;
                            if (!b) {
                                break Label_0212;
                            }
                            final Object o3 = null;
                            o = field.get(o3);
                            clazz = field.getType();
                        }
                        topLevelScope = ScriptableObject.getTopLevelScope(topLevelScope);
                        return context.getWrapFactory().wrap(context, topLevelScope, o, clazz);
                    }
                    catch (Exception ex) {
                        throw Context.throwAsScriptRuntimeEx(ex);
                    }
                }
                final Object o3 = o;
                continue;
            }
        }
    }
    
    Map<String, FieldAndMethods> getFieldAndMethodsObjects(final Scriptable scriptable, final Object javaObject, final boolean b) {
        Map<String, FieldAndMethods> map;
        if (b) {
            map = this.staticFieldAndMethods;
        }
        else {
            map = this.fieldAndMethods;
        }
        if (map == null) {
            return null;
        }
        final HashMap hashMap = new HashMap<String, FieldAndMethods>(map.size());
        for (final FieldAndMethods fieldAndMethods : map.values()) {
            final FieldAndMethods fieldAndMethods2 = new FieldAndMethods(scriptable, fieldAndMethods.methods, fieldAndMethods.field);
            fieldAndMethods2.javaObject = javaObject;
            hashMap.put(fieldAndMethods.field.getName(), fieldAndMethods2);
        }
        return (Map<String, FieldAndMethods>)hashMap;
    }
    
    Object[] getIds(final boolean b) {
        Map<String, Object> map;
        if (b) {
            map = this.staticMembers;
        }
        else {
            map = this.members;
        }
        return map.keySet().toArray(new Object[map.size()]);
    }
    
    boolean has(final String s, final boolean b) {
        Map<String, Object> map;
        if (b) {
            map = this.staticMembers;
        }
        else {
            map = this.members;
        }
        return map.get(s) != null || this.findExplicitFunction(s, b) != null;
    }
    
    void put(Scriptable scriptable, final String s, final Object o, final Object o2, final boolean b) {
        Map<String, Object> map;
        if (b) {
            map = this.staticMembers;
        }
        else {
            map = this.members;
        }
        Object o4;
        final Object o3 = o4 = map.get(s);
        if (!b && (o4 = o3) == null) {
            o4 = this.staticMembers.get(s);
        }
        if (o4 == null) {
            throw this.reportMemberNotFound(s);
        }
        Object field = o4;
        if (o4 instanceof FieldAndMethods) {
            field = map.get(s).field;
        }
        if (field instanceof BeanProperty) {
            final BeanProperty beanProperty = (BeanProperty)field;
            if (beanProperty.setter == null) {
                throw this.reportMemberNotFound(s);
            }
            if (beanProperty.setters != null && o2 != null) {
                beanProperty.setters.call(Context.getContext(), ScriptableObject.getTopLevelScope(scriptable), scriptable, new Object[] { o2 });
                return;
            }
            final Object jsToJava = Context.jsToJava(o2, beanProperty.setter.argTypes[0]);
            try {
                beanProperty.setter.invoke(o, new Object[] { jsToJava });
                return;
            }
            catch (Exception ex) {
                throw Context.throwAsScriptRuntimeEx(ex);
            }
        }
        if (!(field instanceof Field)) {
            String s2;
            if (field == null) {
                s2 = "msg.java.internal.private";
            }
            else {
                s2 = "msg.java.method.assign";
            }
            throw Context.reportRuntimeError1(s2, s);
        }
        scriptable = (Scriptable)field;
        final Object jsToJava2 = Context.jsToJava(o2, ((Field)scriptable).getType());
        try {
            ((Field)scriptable).set(o, jsToJava2);
        }
        catch (IllegalArgumentException ex3) {
            throw Context.reportRuntimeError3("msg.java.internal.field.type", o2.getClass().getName(), scriptable, o.getClass().getName());
        }
        catch (IllegalAccessException ex2) {
            if ((((Field)scriptable).getModifiers() & 0x10) != 0x0) {
                return;
            }
            throw Context.throwAsScriptRuntimeEx(ex2);
        }
    }
    
    RuntimeException reportMemberNotFound(final String s) {
        return Context.reportRuntimeError2("msg.java.member.not.found", this.cl.getName(), s);
    }
    
    private static final class MethodSignature
    {
        private final Class<?>[] args;
        private final String name;
        
        private MethodSignature(final String name, final Class<?>[] args) {
            this.name = name;
            this.args = args;
        }
        
        MethodSignature(final Method method) {
            this(method.getName(), method.getParameterTypes());
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = o instanceof MethodSignature;
            final boolean b2 = false;
            if (b) {
                final MethodSignature methodSignature = (MethodSignature)o;
                boolean b3 = b2;
                if (methodSignature.name.equals(this.name)) {
                    b3 = b2;
                    if (Arrays.equals(this.args, methodSignature.args)) {
                        b3 = true;
                    }
                }
                return b3;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.name.hashCode() ^ this.args.length;
        }
    }
}
