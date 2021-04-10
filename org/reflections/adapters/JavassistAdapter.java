package org.reflections.adapters;

import com.google.common.collect.*;
import javassist.bytecode.annotation.*;
import com.google.common.base.*;
import org.reflections.vfs.*;
import org.reflections.util.*;
import org.reflections.*;
import java.io.*;
import javassist.bytecode.*;
import java.util.*;

public class JavassistAdapter implements MetadataAdapter<ClassFile, FieldInfo, MethodInfo>
{
    public static boolean includeInvisibleTag;
    
    static {
        JavassistAdapter.includeInvisibleTag = true;
    }
    
    private List<String> getAnnotationNames(final AnnotationsAttribute... array) {
        final ArrayList arrayList = Lists.newArrayList();
        if (array != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                final AnnotationsAttribute annotationsAttribute = array[i];
                if (annotationsAttribute != null) {
                    final Annotation[] annotations = annotationsAttribute.getAnnotations();
                    for (int length2 = annotations.length, j = 0; j < length2; ++j) {
                        arrayList.add(annotations[j].getTypeName());
                    }
                }
            }
        }
        return (List<String>)arrayList;
    }
    
    private List<String> getAnnotationNames(final Annotation[] array) {
        final ArrayList arrayList = Lists.newArrayList();
        for (int length = array.length, i = 0; i < length; ++i) {
            arrayList.add(array[i].getTypeName());
        }
        return (List<String>)arrayList;
    }
    
    private List<String> splitDescriptorToTypeNames(final String s) {
        final ArrayList arrayList = Lists.newArrayList();
        if (s != null && s.length() != 0) {
            final ArrayList arrayList2 = Lists.newArrayList();
            final Descriptor$Iterator descriptor$Iterator = new Descriptor$Iterator(s);
            while (descriptor$Iterator.hasNext()) {
                arrayList2.add(descriptor$Iterator.next());
            }
            arrayList2.add(s.length());
            for (int i = 0; i < arrayList2.size() - 1; ++i) {
                arrayList.add(Descriptor.toString(s.substring((int)arrayList2.get(i), (int)arrayList2.get(i + 1))));
            }
        }
        return (List<String>)arrayList;
    }
    
    @Override
    public boolean acceptsInput(final String s) {
        return s.endsWith(".class");
    }
    
    @Override
    public List<String> getClassAnnotationNames(final ClassFile classFile) {
        final AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute)classFile.getAttribute("RuntimeVisibleAnnotations");
        AnnotationsAttribute annotationsAttribute2;
        if (JavassistAdapter.includeInvisibleTag) {
            annotationsAttribute2 = (AnnotationsAttribute)classFile.getAttribute("RuntimeInvisibleAnnotations");
        }
        else {
            annotationsAttribute2 = null;
        }
        return this.getAnnotationNames(annotationsAttribute, annotationsAttribute2);
    }
    
    @Override
    public String getClassName(final ClassFile classFile) {
        return classFile.getName();
    }
    
    @Override
    public List<String> getFieldAnnotationNames(final FieldInfo fieldInfo) {
        final AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute)fieldInfo.getAttribute("RuntimeVisibleAnnotations");
        AnnotationsAttribute annotationsAttribute2;
        if (JavassistAdapter.includeInvisibleTag) {
            annotationsAttribute2 = (AnnotationsAttribute)fieldInfo.getAttribute("RuntimeInvisibleAnnotations");
        }
        else {
            annotationsAttribute2 = null;
        }
        return this.getAnnotationNames(annotationsAttribute, annotationsAttribute2);
    }
    
    @Override
    public String getFieldName(final FieldInfo fieldInfo) {
        return fieldInfo.getName();
    }
    
    @Override
    public List<FieldInfo> getFields(final ClassFile classFile) {
        return (List<FieldInfo>)classFile.getFields();
    }
    
    @Override
    public List<String> getInterfacesNames(final ClassFile classFile) {
        return Arrays.asList(classFile.getInterfaces());
    }
    
    @Override
    public List<String> getMethodAnnotationNames(final MethodInfo methodInfo) {
        final AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute)methodInfo.getAttribute("RuntimeVisibleAnnotations");
        AnnotationsAttribute annotationsAttribute2;
        if (JavassistAdapter.includeInvisibleTag) {
            annotationsAttribute2 = (AnnotationsAttribute)methodInfo.getAttribute("RuntimeInvisibleAnnotations");
        }
        else {
            annotationsAttribute2 = null;
        }
        return this.getAnnotationNames(annotationsAttribute, annotationsAttribute2);
    }
    
    @Override
    public String getMethodFullKey(final ClassFile classFile, final MethodInfo methodInfo) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClassName(classFile));
        sb.append(".");
        sb.append(this.getMethodKey(classFile, methodInfo));
        return sb.toString();
    }
    
    @Override
    public String getMethodKey(final ClassFile classFile, final MethodInfo methodInfo) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getMethodName(methodInfo));
        sb.append("(");
        sb.append(Joiner.on(", ").join((Iterable)this.getParameterNames(methodInfo)));
        sb.append(")");
        return sb.toString();
    }
    
    @Override
    public String getMethodModifier(final MethodInfo methodInfo) {
        final int accessFlags = methodInfo.getAccessFlags();
        if (AccessFlag.isPrivate(accessFlags)) {
            return "private";
        }
        if (AccessFlag.isProtected(accessFlags)) {
            return "protected";
        }
        if (this.isPublic(accessFlags)) {
            return "public";
        }
        return "";
    }
    
    @Override
    public String getMethodName(final MethodInfo methodInfo) {
        return methodInfo.getName();
    }
    
    @Override
    public List<MethodInfo> getMethods(final ClassFile classFile) {
        return (List<MethodInfo>)classFile.getMethods();
    }
    
    @Override
    public ClassFile getOfCreateClassObject(final Vfs.File file) {
        InputStream openInputStream = null;
        try {
            try {
                final InputStream inputStream2;
                final InputStream inputStream = inputStream2 = (openInputStream = file.openInputStream());
                final ClassFile classFile = new ClassFile(new DataInputStream(new BufferedInputStream(inputStream)));
                Utils.close(inputStream);
                return classFile;
            }
            finally {}
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("could not create class file from ");
            sb.append(file.getName());
            throw new ReflectionsException(sb.toString(), ex);
        }
        Utils.close(openInputStream);
    }
    
    @Override
    public List<String> getParameterAnnotationNames(final MethodInfo methodInfo, final int n) {
        final ArrayList arrayList = Lists.newArrayList();
        final ArrayList arrayList2 = Lists.newArrayList((Object[])new ParameterAnnotationsAttribute[] { (ParameterAnnotationsAttribute)methodInfo.getAttribute("RuntimeVisibleParameterAnnotations"), (ParameterAnnotationsAttribute)methodInfo.getAttribute("RuntimeInvisibleParameterAnnotations") });
        if (arrayList2 != null) {
            for (final ParameterAnnotationsAttribute parameterAnnotationsAttribute : arrayList2) {
                if (parameterAnnotationsAttribute != null) {
                    final Annotation[][] annotations = parameterAnnotationsAttribute.getAnnotations();
                    if (n >= annotations.length) {
                        continue;
                    }
                    arrayList.addAll(this.getAnnotationNames(annotations[n]));
                }
            }
        }
        return (List<String>)arrayList;
    }
    
    @Override
    public List<String> getParameterNames(final MethodInfo methodInfo) {
        final String descriptor = methodInfo.getDescriptor();
        return this.splitDescriptorToTypeNames(descriptor.substring(descriptor.indexOf("(") + 1, descriptor.lastIndexOf(")")));
    }
    
    @Override
    public String getReturnTypeName(final MethodInfo methodInfo) {
        final String descriptor = methodInfo.getDescriptor();
        return this.splitDescriptorToTypeNames(descriptor.substring(descriptor.lastIndexOf(")") + 1)).get(0);
    }
    
    @Override
    public String getSuperclassName(final ClassFile classFile) {
        return classFile.getSuperclass();
    }
    
    @Override
    public boolean isPublic(final Object o) {
        int n;
        if (o instanceof ClassFile) {
            n = ((ClassFile)o).getAccessFlags();
        }
        else if (o instanceof FieldInfo) {
            n = ((FieldInfo)o).getAccessFlags();
        }
        else {
            Integer value;
            if (o instanceof MethodInfo) {
                value = ((MethodInfo)o).getAccessFlags();
            }
            else {
                value = null;
            }
            n = value;
        }
        final Integer value2 = n;
        return value2 != null && AccessFlag.isPublic((int)value2);
    }
}
