package org.reflections.adapters;

import java.lang.annotation.*;
import org.reflections.util.*;
import com.google.common.collect.*;
import com.google.common.base.*;
import java.lang.reflect.*;
import java.util.*;
import org.reflections.vfs.*;
import javax.annotation.*;
import org.reflections.*;

public class JavaReflectionAdapter implements MetadataAdapter<Class, Field, Member>
{
    private List<String> getAnnotationNames(final Annotation[] array) {
        final ArrayList<String> list = new ArrayList<String>(array.length);
        for (int length = array.length, i = 0; i < length; ++i) {
            list.add(array[i].annotationType().getName());
        }
        return list;
    }
    
    public static String getName(final Class clazz) {
        if (clazz.isArray()) {
            Class componentType = clazz;
            int n = 0;
            try {
                while (componentType.isArray()) {
                    ++n;
                    componentType = componentType.getComponentType();
                }
                final StringBuilder sb = new StringBuilder();
                sb.append(componentType.getName());
                sb.append(Utils.repeat("[]", n));
                return sb.toString();
            }
            catch (Throwable t) {}
        }
        return clazz.getName();
    }
    
    @Override
    public boolean acceptsInput(final String s) {
        return s.endsWith(".class");
    }
    
    @Override
    public List<String> getClassAnnotationNames(final Class clazz) {
        return this.getAnnotationNames(clazz.getDeclaredAnnotations());
    }
    
    @Override
    public String getClassName(final Class clazz) {
        return clazz.getName();
    }
    
    @Override
    public List<String> getFieldAnnotationNames(final Field field) {
        return this.getAnnotationNames(field.getDeclaredAnnotations());
    }
    
    @Override
    public String getFieldName(final Field field) {
        return field.getName();
    }
    
    @Override
    public List<Field> getFields(final Class clazz) {
        return (List<Field>)Lists.newArrayList((Object[])clazz.getDeclaredFields());
    }
    
    @Override
    public List<String> getInterfacesNames(final Class clazz) {
        final Class[] interfaces = clazz.getInterfaces();
        final int n = 0;
        int length;
        if (interfaces != null) {
            length = interfaces.length;
        }
        else {
            length = 0;
        }
        final ArrayList list = new ArrayList<String>(length);
        if (interfaces != null) {
            for (int length2 = interfaces.length, i = n; i < length2; ++i) {
                list.add(interfaces[i].getName());
            }
        }
        return (List<String>)list;
    }
    
    @Override
    public List<String> getMethodAnnotationNames(final Member member) {
        Annotation[] array;
        if (member instanceof Method) {
            array = ((Method)member).getDeclaredAnnotations();
        }
        else if (member instanceof Constructor) {
            array = ((Constructor)member).getDeclaredAnnotations();
        }
        else {
            array = null;
        }
        return this.getAnnotationNames(array);
    }
    
    @Override
    public String getMethodFullKey(final Class clazz, final Member member) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClassName(clazz));
        sb.append(".");
        sb.append(this.getMethodKey(clazz, member));
        return sb.toString();
    }
    
    @Override
    public String getMethodKey(final Class clazz, final Member member) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getMethodName(member));
        sb.append("(");
        sb.append(Joiner.on(", ").join((Iterable)this.getParameterNames(member)));
        sb.append(")");
        return sb.toString();
    }
    
    @Override
    public String getMethodModifier(final Member member) {
        return Modifier.toString(member.getModifiers());
    }
    
    @Override
    public String getMethodName(final Member member) {
        if (member instanceof Method) {
            return member.getName();
        }
        if (member instanceof Constructor) {
            return "<init>";
        }
        return null;
    }
    
    @Override
    public List<Member> getMethods(final Class clazz) {
        final ArrayList arrayList = Lists.newArrayList();
        arrayList.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        arrayList.addAll(Arrays.asList(clazz.getDeclaredConstructors()));
        return (List<Member>)arrayList;
    }
    
    @Override
    public Class getOfCreateClassObject(final Vfs.File file) throws Exception {
        return this.getOfCreateClassObject(file, (ClassLoader[])null);
    }
    
    public Class getOfCreateClassObject(final Vfs.File file, @Nullable final ClassLoader... array) throws Exception {
        return ReflectionUtils.forName(file.getRelativePath().replace("/", ".").replace(".class", ""), array);
    }
    
    @Override
    public List<String> getParameterAnnotationNames(final Member member, final int n) {
        final boolean b = member instanceof Method;
        Annotation[] array = null;
        Annotation[][] array2;
        if (b) {
            array2 = ((Method)member).getParameterAnnotations();
        }
        else if (member instanceof Constructor) {
            array2 = ((Constructor)member).getParameterAnnotations();
        }
        else {
            array2 = null;
        }
        if (array2 != null) {
            array = array2[n];
        }
        return this.getAnnotationNames(array);
    }
    
    @Override
    public List<String> getParameterNames(final Member member) {
        final ArrayList arrayList = Lists.newArrayList();
        Class<?>[] array;
        if (member instanceof Method) {
            array = ((Method)member).getParameterTypes();
        }
        else if (member instanceof Constructor) {
            array = (Class<?>[])((Constructor)member).getParameterTypes();
        }
        else {
            array = null;
        }
        if (array != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                arrayList.add(getName(array[i]));
            }
        }
        return (List<String>)arrayList;
    }
    
    @Override
    public String getReturnTypeName(final Member member) {
        return ((Method)member).getReturnType().getName();
    }
    
    @Override
    public String getSuperclassName(Class superclass) {
        superclass = superclass.getSuperclass();
        if (superclass != null) {
            return superclass.getName();
        }
        return "";
    }
    
    @Override
    public boolean isPublic(final Object o) {
        int n;
        if (o instanceof Class) {
            n = ((Class)o).getModifiers();
        }
        else {
            Integer value;
            if (o instanceof Member) {
                value = ((Member)o).getModifiers();
            }
            else {
                value = null;
            }
            n = value;
        }
        final Integer value2 = n;
        return value2 != null && Modifier.isPublic(value2);
    }
}
