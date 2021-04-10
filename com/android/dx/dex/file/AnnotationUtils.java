package com.android.dx.dex.file;

import com.android.dx.rop.annotation.*;
import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;
import java.util.*;

public final class AnnotationUtils
{
    private static final CstString ACCESS_FLAGS_STRING;
    private static final CstType ANNOTATION_DEFAULT_TYPE;
    private static final CstType ENCLOSING_CLASS_TYPE;
    private static final CstType ENCLOSING_METHOD_TYPE;
    private static final CstType INNER_CLASS_TYPE;
    private static final CstType MEMBER_CLASSES_TYPE;
    private static final CstString NAME_STRING;
    private static final CstType SIGNATURE_TYPE;
    private static final CstType THROWS_TYPE;
    private static final CstString VALUE_STRING;
    
    static {
        ANNOTATION_DEFAULT_TYPE = CstType.intern(Type.intern("Ldalvik/annotation/AnnotationDefault;"));
        ENCLOSING_CLASS_TYPE = CstType.intern(Type.intern("Ldalvik/annotation/EnclosingClass;"));
        ENCLOSING_METHOD_TYPE = CstType.intern(Type.intern("Ldalvik/annotation/EnclosingMethod;"));
        INNER_CLASS_TYPE = CstType.intern(Type.intern("Ldalvik/annotation/InnerClass;"));
        MEMBER_CLASSES_TYPE = CstType.intern(Type.intern("Ldalvik/annotation/MemberClasses;"));
        SIGNATURE_TYPE = CstType.intern(Type.intern("Ldalvik/annotation/Signature;"));
        THROWS_TYPE = CstType.intern(Type.intern("Ldalvik/annotation/Throws;"));
        ACCESS_FLAGS_STRING = new CstString("accessFlags");
        NAME_STRING = new CstString("name");
        VALUE_STRING = new CstString("value");
    }
    
    private AnnotationUtils() {
    }
    
    public static Annotation makeAnnotationDefault(final Annotation annotation) {
        final Annotation annotation2 = new Annotation(AnnotationUtils.ANNOTATION_DEFAULT_TYPE, AnnotationVisibility.SYSTEM);
        annotation2.put(new NameValuePair(AnnotationUtils.VALUE_STRING, new CstAnnotation(annotation)));
        annotation2.setImmutable();
        return annotation2;
    }
    
    private static CstArray makeCstArray(final TypeList list) {
        final int size = list.size();
        final CstArray.List list2 = new CstArray.List(size);
        for (int i = 0; i < size; ++i) {
            list2.set(i, CstType.intern(list.getType(i)));
        }
        list2.setImmutable();
        return new CstArray(list2);
    }
    
    public static Annotation makeEnclosingClass(final CstType cstType) {
        final Annotation annotation = new Annotation(AnnotationUtils.ENCLOSING_CLASS_TYPE, AnnotationVisibility.SYSTEM);
        annotation.put(new NameValuePair(AnnotationUtils.VALUE_STRING, cstType));
        annotation.setImmutable();
        return annotation;
    }
    
    public static Annotation makeEnclosingMethod(final CstMethodRef cstMethodRef) {
        final Annotation annotation = new Annotation(AnnotationUtils.ENCLOSING_METHOD_TYPE, AnnotationVisibility.SYSTEM);
        annotation.put(new NameValuePair(AnnotationUtils.VALUE_STRING, cstMethodRef));
        annotation.setImmutable();
        return annotation;
    }
    
    public static Annotation makeInnerClass(CstString the_ONE, final int n) {
        final Annotation annotation = new Annotation(AnnotationUtils.INNER_CLASS_TYPE, AnnotationVisibility.SYSTEM);
        if (the_ONE == null) {
            the_ONE = (CstString)CstKnownNull.THE_ONE;
        }
        annotation.put(new NameValuePair(AnnotationUtils.NAME_STRING, the_ONE));
        annotation.put(new NameValuePair(AnnotationUtils.ACCESS_FLAGS_STRING, CstInteger.make(n)));
        annotation.setImmutable();
        return annotation;
    }
    
    public static Annotation makeMemberClasses(final TypeList list) {
        final CstArray cstArray = makeCstArray(list);
        final Annotation annotation = new Annotation(AnnotationUtils.MEMBER_CLASSES_TYPE, AnnotationVisibility.SYSTEM);
        annotation.put(new NameValuePair(AnnotationUtils.VALUE_STRING, cstArray));
        annotation.setImmutable();
        return annotation;
    }
    
    public static Annotation makeSignature(final CstString cstString) {
        final Annotation annotation = new Annotation(AnnotationUtils.SIGNATURE_TYPE, AnnotationVisibility.SYSTEM);
        final String string = cstString.getString();
        final int length = string.length();
        final ArrayList<String> list = new ArrayList<String>(20);
        final int n = 0;
        int n5;
        for (int i = 0; i < length; i = n5) {
            final char char1 = string.charAt(i);
            int n3;
            final int n2 = n3 = i + 1;
            if (char1 == 'L') {
                int n4 = n2;
                while (true) {
                    n5 = n4;
                    if (n4 >= length) {
                        break;
                    }
                    final char char2 = string.charAt(n4);
                    if (char2 == ';') {
                        n5 = n4 + 1;
                        break;
                    }
                    if (char2 == '<') {
                        n5 = n4;
                        break;
                    }
                    ++n4;
                }
            }
            else {
                while ((n5 = n3) < length) {
                    if (string.charAt(n3) == 'L') {
                        n5 = n3;
                        break;
                    }
                    ++n3;
                }
            }
            list.add(string.substring(i, n5));
        }
        final int size = list.size();
        final CstArray.List list2 = new CstArray.List(size);
        for (int j = n; j < size; ++j) {
            list2.set(j, new CstString(list.get(j)));
        }
        list2.setImmutable();
        annotation.put(new NameValuePair(AnnotationUtils.VALUE_STRING, new CstArray(list2)));
        annotation.setImmutable();
        return annotation;
    }
    
    public static Annotation makeThrows(final TypeList list) {
        final CstArray cstArray = makeCstArray(list);
        final Annotation annotation = new Annotation(AnnotationUtils.THROWS_TYPE, AnnotationVisibility.SYSTEM);
        annotation.put(new NameValuePair(AnnotationUtils.VALUE_STRING, cstArray));
        annotation.setImmutable();
        return annotation;
    }
}
