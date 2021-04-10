package com.android.multidex;

import com.android.dx.cf.direct.*;
import com.android.dx.rop.cst.*;
import com.android.dx.rop.type.*;
import java.util.zip.*;
import java.io.*;
import java.util.*;

public class ClassReferenceListBuilder
{
    private static final String CLASS_EXTENSION = ".class";
    private final Set<String> classNames;
    private final Path path;
    
    public ClassReferenceListBuilder(final Path path) {
        this.classNames = new HashSet<String>();
        this.path = path;
    }
    
    private void addClassWithHierachy(final String s) {
        if (this.classNames.contains(s)) {
            return;
        }
        try {
            final Path path = this.path;
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(".class");
            final DirectClassFile class1 = path.getClass(sb.toString());
            this.classNames.add(s);
            final CstType superclass = class1.getSuperclass();
            if (superclass != null) {
                this.addClassWithHierachy(superclass.getClassType().getClassName());
            }
            final TypeList interfaces = class1.getInterfaces();
            for (int size = interfaces.size(), i = 0; i < size; ++i) {
                this.addClassWithHierachy(interfaces.getType(i).getClassName());
            }
        }
        catch (FileNotFoundException ex) {}
    }
    
    private void addDependencies(final ConstantPool constantPool) {
        final Constant[] entries = constantPool.getEntries();
        for (int length = entries.length, i = 0; i < length; ++i) {
            final Constant constant = entries[i];
            if (constant instanceof CstType) {
                this.checkDescriptor(((CstType)constant).getClassType());
            }
            else if (constant instanceof CstFieldRef) {
                this.checkDescriptor(((CstFieldRef)constant).getType());
            }
            else if (constant instanceof CstMethodRef) {
                final Prototype prototype = ((CstMethodRef)constant).getPrototype();
                this.checkDescriptor(prototype.getReturnType());
                final StdTypeList parameterTypes = prototype.getParameterTypes();
                for (int j = 0; j < parameterTypes.size(); ++j) {
                    this.checkDescriptor(parameterTypes.get(j));
                }
            }
        }
    }
    
    private void checkDescriptor(final Type type) {
        final String descriptor = type.getDescriptor();
        if (descriptor.endsWith(";")) {
            final int lastIndex = descriptor.lastIndexOf(91);
            if (lastIndex < 0) {
                this.addClassWithHierachy(descriptor.substring(1, descriptor.length() - 1));
                return;
            }
            this.addClassWithHierachy(descriptor.substring(lastIndex + 2, descriptor.length() - 1));
        }
    }
    
    @Deprecated
    public static void main(final String[] array) {
        MainDexListBuilder.main(array);
    }
    
    public void addRoots(ZipFile name) throws IOException {
        final Enumeration<? extends ZipEntry> entries = name.entries();
        while (entries.hasMoreElements()) {
            final String name2 = ((ZipEntry)entries.nextElement()).getName();
            if (name2.endsWith(".class")) {
                this.classNames.add(name2.substring(0, name2.length() - ".class".length()));
            }
        }
        final Enumeration<? extends ZipEntry> entries2 = name.entries();
        while (entries2.hasMoreElements()) {
            name = (ZipFile)((ZipEntry)entries2.nextElement()).getName();
            if (((String)name).endsWith(".class")) {
                try {
                    this.addDependencies(this.path.getClass((String)name).getConstantPool());
                }
                catch (FileNotFoundException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Class ");
                    sb.append((String)name);
                    sb.append(" is missing form original class path ");
                    sb.append(this.path);
                    throw new IOException(sb.toString(), ex);
                }
            }
        }
    }
    
    Set<String> getClassNames() {
        return this.classNames;
    }
}
