package org.reflections;

import java.lang.annotation.*;
import com.google.common.base.*;
import com.google.common.collect.*;
import org.reflections.util.*;
import org.slf4j.*;
import java.lang.reflect.*;
import java.util.*;
import javax.annotation.*;
import java.util.regex.*;

public abstract class ReflectionUtils
{
    public static boolean includeObject;
    private static List<String> primitiveDescriptors;
    private static List<String> primitiveNames;
    private static List<Class> primitiveTypes;
    
    static {
        ReflectionUtils.includeObject = false;
    }
    
    private static Set<Class<? extends Annotation>> annotationTypes(final Iterable<Annotation> iterable) {
        final HashSet hashSet = Sets.newHashSet();
        final Iterator<Annotation> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            hashSet.add(iterator.next().annotationType());
        }
        return (Set<Class<? extends Annotation>>)hashSet;
    }
    
    private static Class<? extends Annotation>[] annotationTypes(final Annotation[] array) {
        final Class[] array2 = new Class[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = array[i].annotationType();
        }
        return (Class<? extends Annotation>[])array2;
    }
    
    private static boolean areAnnotationMembersMatching(final Annotation annotation, final Annotation annotation2) {
        if (annotation2 != null && annotation.annotationType() == annotation2.annotationType()) {
            final Method[] declaredMethods = annotation.annotationType().getDeclaredMethods();
            final int length = declaredMethods.length;
            int i = 0;
            while (i < length) {
                final Method method = declaredMethods[i];
                try {
                    if (!method.invoke(annotation, new Object[0]).equals(method.invoke(annotation2, new Object[0]))) {
                        return false;
                    }
                    ++i;
                    continue;
                }
                catch (Exception ex) {
                    throw new ReflectionsException(String.format("could not invoke method %s on annotation %s", method.getName(), annotation.annotationType()), ex);
                }
                break;
            }
            return true;
        }
        return false;
    }
    
    static <T> Set<T> filter(final Iterable<T> iterable, final Predicate<? super T>... array) {
        if (Utils.isEmpty(array)) {
            return (Set<T>)Sets.newHashSet((Iterable)iterable);
        }
        return (Set<T>)Sets.newHashSet(Iterables.filter((Iterable)iterable, Predicates.and((Predicate[])array)));
    }
    
    static <T> Set<T> filter(final T[] array, final Predicate<? super T>... array2) {
        if (Utils.isEmpty(array2)) {
            return (Set<T>)Sets.newHashSet((Object[])array);
        }
        return (Set<T>)Sets.newHashSet(Iterables.filter((Iterable)Arrays.asList(array), Predicates.and((Predicate[])array2)));
    }
    
    public static Class<?> forName(final String s, ClassLoader... classLoaders) {
        if (getPrimitiveNames().contains(s)) {
            return getPrimitiveTypes().get(getPrimitiveNames().indexOf(s));
        }
        String string2;
        if (s.contains("[")) {
            final int index = s.indexOf("[");
            final String substring = s.substring(0, index);
            final String replace = s.substring(index).replace("]", "");
            String string;
            if (getPrimitiveNames().contains(substring)) {
                string = getPrimitiveDescriptors().get(getPrimitiveNames().indexOf(substring));
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append("L");
                sb.append(substring);
                sb.append(";");
                string = sb.toString();
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(replace);
            sb2.append(string);
            string2 = sb2.toString();
        }
        else {
            string2 = s;
        }
        final ArrayList arrayList = Lists.newArrayList();
        classLoaders = ClasspathHelper.classLoaders(classLoaders);
        final int length = classLoaders.length;
        int i = 0;
        while (i < length) {
            final ClassLoader classLoader = classLoaders[i];
            if (string2.contains("[")) {
                try {
                    return Class.forName(string2, false, classLoader);
                }
                catch (Throwable t) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("could not get type for name ");
                    sb3.append(s);
                    arrayList.add(new ReflectionsException(sb3.toString(), t));
                }
            }
            try {
                return classLoader.loadClass(string2);
            }
            catch (Throwable t2) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("could not get type for name ");
                sb4.append(s);
                arrayList.add(new ReflectionsException(sb4.toString(), t2));
                ++i;
                continue;
            }
            break;
        }
        if (Reflections.log != null) {
            for (final ReflectionsException ex : arrayList) {
                final Logger log = Reflections.log;
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("could not get type for name ");
                sb5.append(s);
                sb5.append(" from any class loader");
                log.warn(sb5.toString(), (Throwable)ex);
            }
        }
        return null;
    }
    
    public static <T> List<Class<? extends T>> forNames(final Iterable<String> iterable, final ClassLoader... array) {
        final ArrayList<Class<?>> list = (ArrayList<Class<?>>)new ArrayList<Class<? extends T>>();
        final Iterator<String> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            final Class<?> forName = forName(iterator.next(), array);
            if (forName != null) {
                list.add(forName);
            }
        }
        return (List<Class<? extends T>>)list;
    }
    
    public static <T extends AnnotatedElement> Set<T> getAll(final Set<T> set, final Predicate<? super T>... array) {
        if (Utils.isEmpty(array)) {
            return set;
        }
        return (Set<T>)Sets.newHashSet(Iterables.filter((Iterable)set, Predicates.and((Predicate[])array)));
    }
    
    public static <T extends AnnotatedElement> Set<Annotation> getAllAnnotations(final T t, final Predicate<Annotation>... array) {
        final HashSet hashSet = Sets.newHashSet();
        if (t instanceof Class) {
            final Iterator<Class<?>> iterator = getAllSuperTypes((Class<?>)t, (Predicate<? super Class<?>>[])new Predicate[0]).iterator();
            while (iterator.hasNext()) {
                hashSet.addAll(getAnnotations(iterator.next(), array));
            }
        }
        else {
            hashSet.addAll(getAnnotations((AnnotatedElement)t, array));
        }
        return (Set<Annotation>)hashSet;
    }
    
    public static Set<Constructor> getAllConstructors(final Class<?> clazz, final Predicate<? super Constructor>... array) {
        final HashSet hashSet = Sets.newHashSet();
        final Iterator<Class<?>> iterator = getAllSuperTypes(clazz, (Predicate<? super Class<?>>[])new Predicate[0]).iterator();
        while (iterator.hasNext()) {
            hashSet.addAll(getConstructors(iterator.next(), array));
        }
        return (Set<Constructor>)hashSet;
    }
    
    public static Set<Field> getAllFields(final Class<?> clazz, final Predicate<? super Field>... array) {
        final HashSet hashSet = Sets.newHashSet();
        final Iterator<Class<?>> iterator = getAllSuperTypes(clazz, (Predicate<? super Class<?>>[])new Predicate[0]).iterator();
        while (iterator.hasNext()) {
            hashSet.addAll(getFields(iterator.next(), array));
        }
        return (Set<Field>)hashSet;
    }
    
    public static Set<Method> getAllMethods(final Class<?> clazz, final Predicate<? super Method>... array) {
        final HashSet hashSet = Sets.newHashSet();
        final Iterator<Class<?>> iterator = getAllSuperTypes(clazz, (Predicate<? super Class<?>>[])new Predicate[0]).iterator();
        while (iterator.hasNext()) {
            hashSet.addAll(getMethods(iterator.next(), array));
        }
        return (Set<Method>)hashSet;
    }
    
    public static Set<Class<?>> getAllSuperTypes(final Class<?> clazz, final Predicate<? super Class<?>>... array) {
        final LinkedHashSet linkedHashSet = Sets.newLinkedHashSet();
        if (clazz != null && (ReflectionUtils.includeObject || !clazz.equals(Object.class))) {
            linkedHashSet.add(clazz);
            linkedHashSet.addAll(getAllSuperTypes(clazz.getSuperclass(), (Predicate<? super Class<?>>[])new Predicate[0]));
            final Class[] interfaces = clazz.getInterfaces();
            for (int length = interfaces.length, i = 0; i < length; ++i) {
                linkedHashSet.addAll(getAllSuperTypes(interfaces[i], (Predicate<? super Class<?>>[])new Predicate[0]));
            }
        }
        return filter((Iterable<Class<?>>)linkedHashSet, array);
    }
    
    public static <T extends AnnotatedElement> Set<Annotation> getAnnotations(final T t, final Predicate<Annotation>... array) {
        return filter(t.getDeclaredAnnotations(), (com.google.common.base.Predicate<? super Annotation>[])array);
    }
    
    public static Set<Constructor> getConstructors(final Class<?> clazz, final Predicate<? super Constructor>... array) {
        return (Set<Constructor>)filter(clazz.getDeclaredConstructors(), (com.google.common.base.Predicate<? super Constructor<?>>[])array);
    }
    
    public static Set<Field> getFields(final Class<?> clazz, final Predicate<? super Field>... array) {
        return filter(clazz.getDeclaredFields(), array);
    }
    
    public static Set<Method> getMethods(final Class<?> clazz, final Predicate<? super Method>... array) {
        Method[] array2;
        if (clazz.isInterface()) {
            array2 = clazz.getMethods();
        }
        else {
            array2 = clazz.getDeclaredMethods();
        }
        return filter(array2, array);
    }
    
    private static List<String> getPrimitiveDescriptors() {
        initPrimitives();
        return ReflectionUtils.primitiveDescriptors;
    }
    
    private static List<String> getPrimitiveNames() {
        initPrimitives();
        return ReflectionUtils.primitiveNames;
    }
    
    private static List<Class> getPrimitiveTypes() {
        initPrimitives();
        return ReflectionUtils.primitiveTypes;
    }
    
    private static void initPrimitives() {
        if (ReflectionUtils.primitiveNames == null) {
            ReflectionUtils.primitiveNames = (List<String>)Lists.newArrayList((Object[])new String[] { "boolean", "char", "byte", "short", "int", "long", "float", "double", "void" });
            ReflectionUtils.primitiveTypes = (List<Class>)Lists.newArrayList((Object[])new Class[] { Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE });
            ReflectionUtils.primitiveDescriptors = (List<String>)Lists.newArrayList((Object[])new String[] { "Z", "C", "B", "S", "I", "J", "F", "D", "V" });
        }
    }
    
    private static Set<Annotation> parameterAnnotations(final Member member) {
        final HashSet hashSet = Sets.newHashSet();
        Annotation[][] array;
        if (member instanceof Method) {
            array = ((Method)member).getParameterAnnotations();
        }
        else if (member instanceof Constructor) {
            array = ((Constructor)member).getParameterAnnotations();
        }
        else {
            array = null;
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            Collections.addAll(hashSet, array[i]);
        }
        return (Set<Annotation>)hashSet;
    }
    
    private static Class[] parameterTypes(final Member member) {
        Class[] parameterTypes = null;
        if (member != null) {
            if (member.getClass() == Method.class) {
                return ((Method)member).getParameterTypes();
            }
            parameterTypes = parameterTypes;
            if (member.getClass() == Constructor.class) {
                parameterTypes = ((Constructor)member).getParameterTypes();
            }
        }
        return parameterTypes;
    }
    
    public static <T extends AnnotatedElement> Predicate<T> withAnnotation(final Class<? extends Annotation> clazz) {
        return (Predicate<T>)new Predicate<T>() {
            public boolean apply(@Nullable final T t) {
                return t != null && t.isAnnotationPresent(clazz);
            }
        };
    }
    
    public static <T extends AnnotatedElement> Predicate<T> withAnnotation(final Annotation annotation) {
        return (Predicate<T>)new Predicate<T>() {
            public boolean apply(@Nullable final T t) {
                return t != null && t.isAnnotationPresent(annotation.annotationType()) && areAnnotationMembersMatching(t.getAnnotation(annotation.annotationType()), annotation);
            }
        };
    }
    
    public static <T extends AnnotatedElement> Predicate<T> withAnnotations(final Class<? extends Annotation>... array) {
        return (Predicate<T>)new Predicate<T>() {
            public boolean apply(@Nullable final T t) {
                return t != null && Arrays.equals(array, annotationTypes(t.getAnnotations()));
            }
        };
    }
    
    public static <T extends AnnotatedElement> Predicate<T> withAnnotations(final Annotation... array) {
        return (Predicate<T>)new Predicate<T>() {
            public boolean apply(@Nullable final T t) {
                if (t != null) {
                    final Annotation[] annotations = t.getAnnotations();
                    if (annotations.length == array.length) {
                        for (int i = 0; i < annotations.length; ++i) {
                            if (!areAnnotationMembersMatching(annotations[i], array[i])) {
                                return false;
                            }
                        }
                    }
                }
                return true;
            }
        };
    }
    
    public static Predicate<Member> withAnyParameterAnnotation(final Class<? extends Annotation> clazz) {
        return (Predicate<Member>)new Predicate<Member>() {
            public boolean apply(@Nullable final Member member) {
                return member != null && Iterables.any((Iterable)annotationTypes(parameterAnnotations(member)), (Predicate)new Predicate<Class<? extends Annotation>>() {
                    public boolean apply(@Nullable final Class<? extends Annotation> clazz) {
                        return clazz.equals(clazz);
                    }
                });
            }
        };
    }
    
    public static Predicate<Member> withAnyParameterAnnotation(final Annotation annotation) {
        return (Predicate<Member>)new Predicate<Member>() {
            public boolean apply(@Nullable final Member member) {
                return member != null && Iterables.any((Iterable)parameterAnnotations(member), (Predicate)new Predicate<Annotation>() {
                    public boolean apply(@Nullable final Annotation annotation) {
                        return areAnnotationMembersMatching(annotation, annotation);
                    }
                });
            }
        };
    }
    
    public static Predicate<Class<?>> withClassModifier(final int n) {
        return (Predicate<Class<?>>)new Predicate<Class<?>>() {
            public boolean apply(@Nullable final Class<?> clazz) {
                return clazz != null && (clazz.getModifiers() & n) != 0x0;
            }
        };
    }
    
    public static <T extends Member> Predicate<T> withModifier(final int n) {
        return (Predicate<T>)new Predicate<T>() {
            public boolean apply(@Nullable final T t) {
                return t != null && (t.getModifiers() & n) != 0x0;
            }
        };
    }
    
    public static <T extends Member> Predicate<T> withName(final String s) {
        return (Predicate<T>)new Predicate<T>() {
            public boolean apply(@Nullable final T t) {
                return t != null && t.getName().equals(s);
            }
        };
    }
    
    public static Predicate<Member> withParameters(final Class<?>... array) {
        return (Predicate<Member>)new Predicate<Member>() {
            public boolean apply(@Nullable final Member member) {
                return Arrays.equals(parameterTypes(member), array);
            }
        };
    }
    
    public static Predicate<Member> withParametersAssignableTo(final Class... array) {
        return (Predicate<Member>)new Predicate<Member>() {
            public boolean apply(@Nullable final Member member) {
                if (member != null) {
                    final Class[] access$200 = parameterTypes(member);
                    if (access$200.length == array.length) {
                        for (int i = 0; i < access$200.length; ++i) {
                            if (!access$200[i].isAssignableFrom(array[i])) {
                                return false;
                            }
                            if (access$200[i] == Object.class && array[i] != Object.class) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        };
    }
    
    public static Predicate<Member> withParametersCount(final int n) {
        return (Predicate<Member>)new Predicate<Member>() {
            public boolean apply(@Nullable final Member member) {
                return member != null && parameterTypes(member).length == n;
            }
        };
    }
    
    public static <T extends AnnotatedElement> Predicate<T> withPattern(final String s) {
        return (Predicate<T>)new Predicate<T>() {
            public boolean apply(@Nullable final T t) {
                return Pattern.matches(s, t.toString());
            }
        };
    }
    
    public static <T extends Member> Predicate<T> withPrefix(final String s) {
        return (Predicate<T>)new Predicate<T>() {
            public boolean apply(@Nullable final T t) {
                return t != null && t.getName().startsWith(s);
            }
        };
    }
    
    public static <T> Predicate<Method> withReturnType(final Class<T> clazz) {
        return (Predicate<Method>)new Predicate<Method>() {
            public boolean apply(@Nullable final Method method) {
                return method != null && method.getReturnType().equals(clazz);
            }
        };
    }
    
    public static <T> Predicate<Method> withReturnTypeAssignableTo(final Class<T> clazz) {
        return (Predicate<Method>)new Predicate<Method>() {
            public boolean apply(@Nullable final Method method) {
                return method != null && clazz.isAssignableFrom(method.getReturnType());
            }
        };
    }
    
    public static <T> Predicate<Field> withType(final Class<T> clazz) {
        return (Predicate<Field>)new Predicate<Field>() {
            public boolean apply(@Nullable final Field field) {
                return field != null && field.getType().equals(clazz);
            }
        };
    }
    
    public static <T> Predicate<Field> withTypeAssignableTo(final Class<T> clazz) {
        return (Predicate<Field>)new Predicate<Field>() {
            public boolean apply(@Nullable final Field field) {
                return field != null && clazz.isAssignableFrom(field.getType());
            }
        };
    }
}
