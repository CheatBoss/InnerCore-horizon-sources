package com.android.dx.command.annotool;

import com.android.dx.cf.attrib.*;
import java.util.*;
import java.lang.annotation.*;
import com.android.dx.rop.annotation.*;
import java.io.*;
import com.android.dx.util.*;
import com.android.dx.cf.direct.*;
import com.android.dx.cf.iface.*;

class AnnotationLister
{
    private static final String PACKAGE_INFO = "package-info";
    private final Main.Arguments args;
    HashSet<String> matchInnerClassesOf;
    HashSet<String> matchPackages;
    
    AnnotationLister(final Main.Arguments args) {
        this.matchInnerClassesOf = new HashSet<String>();
        this.matchPackages = new HashSet<String>();
        this.args = args;
    }
    
    private boolean isMatchingInnerClass(String substring) {
        int lastIndex;
        do {
            lastIndex = substring.lastIndexOf(36);
            if (lastIndex > 0) {
                continue;
            }
            return false;
        } while (!this.matchInnerClassesOf.contains(substring = substring.substring(0, lastIndex)));
        return true;
    }
    
    private boolean isMatchingPackage(String substring) {
        final int lastIndex = substring.lastIndexOf(47);
        if (lastIndex == -1) {
            substring = "";
        }
        else {
            substring = substring.substring(0, lastIndex);
        }
        return this.matchPackages.contains(substring);
    }
    
    private void printMatch(final DirectClassFile directClassFile) {
        final Iterator<Main.PrintType> iterator = this.args.printTypes.iterator();
        while (iterator.hasNext()) {
            switch (iterator.next()) {
                default: {
                    continue;
                }
                case METHOD: {
                    continue;
                }
                case INNERCLASS: {
                    this.matchInnerClassesOf.add(directClassFile.getThisClass().getClassType().getClassName());
                    continue;
                }
                case CLASS: {
                    System.out.println(directClassFile.getThisClass().getClassType().getClassName().replace('/', '.'));
                    continue;
                }
            }
        }
    }
    
    private void printMatchPackage(final String s) {
        final Iterator<Main.PrintType> iterator = this.args.printTypes.iterator();
        while (iterator.hasNext()) {
            switch (iterator.next()) {
                default: {
                    continue;
                }
                case PACKAGE: {
                    System.out.println(s.replace('/', '.'));
                    continue;
                }
                case CLASS:
                case INNERCLASS:
                case METHOD: {
                    this.matchPackages.add(s);
                    continue;
                }
            }
        }
    }
    
    private void visitClassAnnotation(final DirectClassFile directClassFile, final BaseAnnotations baseAnnotations) {
        if (!this.args.eTypes.contains(ElementType.TYPE)) {
            return;
        }
        final Iterator<Annotation> iterator = baseAnnotations.getAnnotations().getAnnotations().iterator();
        while (iterator.hasNext()) {
            if (this.args.aclass.equals(iterator.next().getType().getClassType().getClassName())) {
                this.printMatch(directClassFile);
            }
        }
    }
    
    private void visitPackageAnnotation(final DirectClassFile directClassFile, final BaseAnnotations baseAnnotations) {
        if (!this.args.eTypes.contains(ElementType.PACKAGE)) {
            return;
        }
        final String className = directClassFile.getThisClass().getClassType().getClassName();
        final int lastIndex = className.lastIndexOf(47);
        String substring;
        if (lastIndex == -1) {
            substring = "";
        }
        else {
            substring = className.substring(0, lastIndex);
        }
        final Iterator<Annotation> iterator = baseAnnotations.getAnnotations().getAnnotations().iterator();
        while (iterator.hasNext()) {
            if (this.args.aclass.equals(iterator.next().getType().getClassType().getClassName())) {
                this.printMatchPackage(substring);
            }
        }
    }
    
    void process() {
        final String[] files = this.args.files;
        for (int length = files.length, i = 0; i < length; ++i) {
            new ClassPathOpener(files[i], true, (ClassPathOpener.Consumer)new ClassPathOpener.Consumer() {
                @Override
                public void onException(final Exception ex) {
                    throw new RuntimeException(ex);
                }
                
                @Override
                public void onProcessArchiveStart(final File file) {
                }
                
                @Override
                public boolean processFileBytes(String className, final long n, final byte[] array) {
                    if (!className.endsWith(".class")) {
                        return true;
                    }
                    final DirectClassFile directClassFile = new DirectClassFile(new ByteArray(array), className, true);
                    directClassFile.setAttributeFactory(StdAttributeFactory.THE_ONE);
                    final AttributeList attributes = directClassFile.getAttributes();
                    className = directClassFile.getThisClass().getClassType().getClassName();
                    if (className.endsWith("package-info")) {
                        for (Attribute attribute = attributes.findFirst("RuntimeInvisibleAnnotations"); attribute != null; attribute = attributes.findNext(attribute)) {
                            AnnotationLister.this.visitPackageAnnotation(directClassFile, (BaseAnnotations)attribute);
                        }
                        for (Attribute attribute2 = attributes.findFirst("RuntimeVisibleAnnotations"); attribute2 != null; attribute2 = attributes.findNext(attribute2)) {
                            AnnotationLister.this.visitPackageAnnotation(directClassFile, (BaseAnnotations)attribute2);
                        }
                    }
                    else if (!AnnotationLister.this.isMatchingInnerClass(className) && !AnnotationLister.this.isMatchingPackage(className)) {
                        for (Attribute attribute3 = attributes.findFirst("RuntimeInvisibleAnnotations"); attribute3 != null; attribute3 = attributes.findNext(attribute3)) {
                            AnnotationLister.this.visitClassAnnotation(directClassFile, (BaseAnnotations)attribute3);
                        }
                        for (Attribute attribute4 = attributes.findFirst("RuntimeVisibleAnnotations"); attribute4 != null; attribute4 = attributes.findNext(attribute4)) {
                            AnnotationLister.this.visitClassAnnotation(directClassFile, (BaseAnnotations)attribute4);
                        }
                    }
                    else {
                        AnnotationLister.this.printMatch(directClassFile);
                    }
                    return true;
                }
            }).process();
        }
    }
}
