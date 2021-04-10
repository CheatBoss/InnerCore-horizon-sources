package com.android.dx.dex.cf;

import com.android.dx.cf.direct.*;
import com.android.dx.util.*;
import com.android.dx.rop.code.*;
import java.io.*;
import com.android.dx.dex.file.*;
import com.android.dx.rop.annotation.*;
import com.android.dx.cf.iface.*;
import com.android.dx.rop.cst.*;
import java.util.*;
import com.android.dx.rop.type.*;
import com.android.dx.cf.attrib.*;

class AttributeTranslator
{
    private AttributeTranslator() {
    }
    
    public static Annotations getAnnotations(final AttributeList list) {
        final Annotations annotations0 = getAnnotations0(list);
        final Annotation signature = getSignature(list);
        Annotations combine = annotations0;
        if (signature != null) {
            combine = Annotations.combine(annotations0, signature);
        }
        return combine;
    }
    
    private static Annotations getAnnotations0(final AttributeList list) {
        final AttRuntimeVisibleAnnotations attRuntimeVisibleAnnotations = (AttRuntimeVisibleAnnotations)list.findFirst("RuntimeVisibleAnnotations");
        final AttRuntimeInvisibleAnnotations attRuntimeInvisibleAnnotations = (AttRuntimeInvisibleAnnotations)list.findFirst("RuntimeInvisibleAnnotations");
        if (attRuntimeVisibleAnnotations == null) {
            if (attRuntimeInvisibleAnnotations == null) {
                return Annotations.EMPTY;
            }
            return attRuntimeInvisibleAnnotations.getAnnotations();
        }
        else {
            if (attRuntimeInvisibleAnnotations == null) {
                return attRuntimeVisibleAnnotations.getAnnotations();
            }
            return Annotations.combine(attRuntimeVisibleAnnotations.getAnnotations(), attRuntimeInvisibleAnnotations.getAnnotations());
        }
    }
    
    public static Annotations getClassAnnotations(final DirectClassFile directClassFile, final CfOptions cfOptions) {
        final CstType thisClass = directClassFile.getThisClass();
        final AttributeList attributes = directClassFile.getAttributes();
        final Annotations annotations = getAnnotations(attributes);
        final Annotation translateEnclosingMethod = translateEnclosingMethod(attributes);
        boolean b;
        if (translateEnclosingMethod == null) {
            b = true;
        }
        else {
            b = false;
        }
        Annotations combine;
        try {
            final Annotations translateInnerClasses = translateInnerClasses(thisClass, attributes, b);
            combine = annotations;
            if (translateInnerClasses != null) {
                combine = Annotations.combine(annotations, translateInnerClasses);
            }
        }
        catch (Warning warning) {
            final PrintStream warn = cfOptions.warn;
            final StringBuilder sb = new StringBuilder();
            sb.append("warning: ");
            sb.append(warning.getMessage());
            warn.println(sb.toString());
            combine = annotations;
        }
        Annotations combine2 = combine;
        if (translateEnclosingMethod != null) {
            combine2 = Annotations.combine(combine, translateEnclosingMethod);
        }
        Annotations combine3 = combine2;
        if (AccessFlags.isAnnotation(directClassFile.getAccessFlags())) {
            final Annotation translateAnnotationDefaults = translateAnnotationDefaults(directClassFile);
            combine3 = combine2;
            if (translateAnnotationDefaults != null) {
                combine3 = Annotations.combine(combine2, translateAnnotationDefaults);
            }
        }
        return combine3;
    }
    
    public static TypeList getExceptions(final Method method) {
        final AttExceptions attExceptions = (AttExceptions)method.getAttributes().findFirst("Exceptions");
        if (attExceptions == null) {
            return StdTypeList.EMPTY;
        }
        return attExceptions.getExceptions();
    }
    
    public static Annotations getMethodAnnotations(final Method method) {
        final Annotations annotations = getAnnotations(method.getAttributes());
        final TypeList exceptions = getExceptions(method);
        Annotations combine = annotations;
        if (exceptions.size() != 0) {
            combine = Annotations.combine(annotations, AnnotationUtils.makeThrows(exceptions));
        }
        return combine;
    }
    
    public static AnnotationsList getParameterAnnotations(final Method method) {
        final AttributeList attributes = method.getAttributes();
        final AttRuntimeVisibleParameterAnnotations attRuntimeVisibleParameterAnnotations = (AttRuntimeVisibleParameterAnnotations)attributes.findFirst("RuntimeVisibleParameterAnnotations");
        final AttRuntimeInvisibleParameterAnnotations attRuntimeInvisibleParameterAnnotations = (AttRuntimeInvisibleParameterAnnotations)attributes.findFirst("RuntimeInvisibleParameterAnnotations");
        if (attRuntimeVisibleParameterAnnotations == null) {
            if (attRuntimeInvisibleParameterAnnotations == null) {
                return AnnotationsList.EMPTY;
            }
            return attRuntimeInvisibleParameterAnnotations.getParameterAnnotations();
        }
        else {
            if (attRuntimeInvisibleParameterAnnotations == null) {
                return attRuntimeVisibleParameterAnnotations.getParameterAnnotations();
            }
            return AnnotationsList.combine(attRuntimeVisibleParameterAnnotations.getParameterAnnotations(), attRuntimeInvisibleParameterAnnotations.getParameterAnnotations());
        }
    }
    
    private static Annotation getSignature(final AttributeList list) {
        final AttSignature attSignature = (AttSignature)list.findFirst("Signature");
        if (attSignature == null) {
            return null;
        }
        return AnnotationUtils.makeSignature(attSignature.getSignature());
    }
    
    private static Annotation translateAnnotationDefaults(final DirectClassFile directClassFile) {
        final CstType thisClass = directClassFile.getThisClass();
        final MethodList methods = directClassFile.getMethods();
        final int size = methods.size();
        final Annotation annotation = new Annotation(thisClass, AnnotationVisibility.EMBEDDED);
        boolean b = false;
        for (int i = 0; i < size; ++i) {
            final Method value = methods.get(i);
            final AttAnnotationDefault attAnnotationDefault = (AttAnnotationDefault)value.getAttributes().findFirst("AnnotationDefault");
            if (attAnnotationDefault != null) {
                annotation.add(new NameValuePair(value.getNat().getName(), attAnnotationDefault.getValue()));
                b = true;
            }
        }
        if (!b) {
            return null;
        }
        annotation.setImmutable();
        return AnnotationUtils.makeAnnotationDefault(annotation);
    }
    
    private static Annotation translateEnclosingMethod(final AttributeList list) {
        final AttEnclosingMethod attEnclosingMethod = (AttEnclosingMethod)list.findFirst("EnclosingMethod");
        if (attEnclosingMethod == null) {
            return null;
        }
        final CstType enclosingClass = attEnclosingMethod.getEnclosingClass();
        final CstNat method = attEnclosingMethod.getMethod();
        if (method == null) {
            return AnnotationUtils.makeEnclosingClass(enclosingClass);
        }
        return AnnotationUtils.makeEnclosingMethod(new CstMethodRef(enclosingClass, method));
    }
    
    private static Annotations translateInnerClasses(final CstType cstType, final AttributeList list, final boolean b) {
        final AttInnerClasses attInnerClasses = (AttInnerClasses)list.findFirst("InnerClasses");
        if (attInnerClasses == null) {
            return null;
        }
        final InnerClassList innerClasses = attInnerClasses.getInnerClasses();
        final int size = innerClasses.size();
        final ArrayList<Type> list2 = new ArrayList<Type>();
        final int n = 0;
        Object o = null;
        InnerClassList.Item item;
        for (int i = 0; i < size; ++i, o = item) {
            final InnerClassList.Item value = innerClasses.get(i);
            final CstType innerClass = value.getInnerClass();
            if (innerClass.equals(cstType)) {
                item = value;
            }
            else {
                item = (InnerClassList.Item)o;
                if (cstType.equals(value.getOuterClass())) {
                    list2.add(innerClass.getClassType());
                    item = (InnerClassList.Item)o;
                }
            }
        }
        final int size2 = list2.size();
        if (o == null && size2 == 0) {
            return null;
        }
        final Annotations annotations = new Annotations();
        if (o != null) {
            annotations.add(AnnotationUtils.makeInnerClass(((InnerClassList.Item)o).getInnerName(), ((InnerClassList.Item)o).getAccessFlags()));
            if (b) {
                if (((InnerClassList.Item)o).getOuterClass() == null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Ignoring InnerClasses attribute for an anonymous inner class\n(");
                    sb.append(cstType.toHuman());
                    sb.append(") that doesn't come with an\n");
                    sb.append("associated EnclosingMethod attribute. ");
                    sb.append("This class was probably produced by a\n");
                    sb.append("compiler that did not target the modern ");
                    sb.append(".class file format. The recommended\n");
                    sb.append("solution is to recompile the class from ");
                    sb.append("source, using an up-to-date compiler\n");
                    sb.append("and without specifying any \"-target\" type ");
                    sb.append("options. The consequence of ignoring\n");
                    sb.append("this warning is that reflective operations ");
                    sb.append("on this class will incorrectly\n");
                    sb.append("indicate that it is *not* an inner class.");
                    throw new Warning(sb.toString());
                }
                annotations.add(AnnotationUtils.makeEnclosingClass(((InnerClassList.Item)o).getOuterClass()));
            }
        }
        if (size2 != 0) {
            final StdTypeList list3 = new StdTypeList(size2);
            for (int j = n; j < size2; ++j) {
                list3.set(j, list2.get(j));
            }
            list3.setImmutable();
            annotations.add(AnnotationUtils.makeMemberClasses(list3));
        }
        annotations.setImmutable();
        return annotations;
    }
}
