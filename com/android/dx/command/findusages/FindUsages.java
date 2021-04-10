package com.android.dx.command.findusages;

import java.io.*;
import java.util.regex.*;
import com.android.dx.io.instructions.*;
import com.android.dx.io.*;
import java.util.*;
import com.android.dex.*;

public final class FindUsages
{
    private final CodeReader codeReader;
    private ClassDef currentClass;
    private ClassData.Method currentMethod;
    private final Dex dex;
    private final Set<Integer> fieldIds;
    private final Set<Integer> methodIds;
    private final PrintWriter out;
    
    public FindUsages(final Dex dex, final String s, final String s2, final PrintWriter out) {
        this.codeReader = new CodeReader();
        this.dex = dex;
        this.out = out;
        final HashSet<Integer> set = new HashSet<Integer>();
        final HashSet<Integer> set2 = new HashSet<Integer>();
        final Pattern compile = Pattern.compile(s);
        final Pattern compile2 = Pattern.compile(s2);
        final List<String> strings = dex.strings();
        for (int i = 0; i < strings.size(); ++i) {
            final String s3 = strings.get(i);
            if (compile.matcher(s3).matches()) {
                set.add(i);
            }
            if (compile2.matcher(s3).matches()) {
                set2.add(i);
            }
        }
        if (!set.isEmpty() && !set2.isEmpty()) {
            this.methodIds = new HashSet<Integer>();
            this.fieldIds = new HashSet<Integer>();
            final Iterator<Object> iterator = set.iterator();
            while (iterator.hasNext()) {
                final int binarySearch = Collections.binarySearch(dex.typeIds(), (int)iterator.next());
                if (binarySearch < 0) {
                    continue;
                }
                this.methodIds.addAll(this.getMethodIds(dex, set2, binarySearch));
                this.fieldIds.addAll(this.getFieldIds(dex, set2, binarySearch));
            }
            this.codeReader.setFieldVisitor((CodeReader.Visitor)new CodeReader.Visitor() {
                @Override
                public void visit(final DecodedInstruction[] array, final DecodedInstruction decodedInstruction) {
                    final int index = decodedInstruction.getIndex();
                    if (FindUsages.this.fieldIds.contains(index)) {
                        final PrintWriter val$out = out;
                        final StringBuilder sb = new StringBuilder();
                        sb.append(FindUsages.this.location());
                        sb.append(": field reference ");
                        sb.append(dex.fieldIds().get(index));
                        sb.append(" (");
                        sb.append(OpcodeInfo.getName(decodedInstruction.getOpcode()));
                        sb.append(")");
                        val$out.println(sb.toString());
                    }
                }
            });
            this.codeReader.setMethodVisitor((CodeReader.Visitor)new CodeReader.Visitor() {
                @Override
                public void visit(final DecodedInstruction[] array, final DecodedInstruction decodedInstruction) {
                    final int index = decodedInstruction.getIndex();
                    if (FindUsages.this.methodIds.contains(index)) {
                        final PrintWriter val$out = out;
                        final StringBuilder sb = new StringBuilder();
                        sb.append(FindUsages.this.location());
                        sb.append(": method reference ");
                        sb.append(dex.methodIds().get(index));
                        sb.append(" (");
                        sb.append(OpcodeInfo.getName(decodedInstruction.getOpcode()));
                        sb.append(")");
                        val$out.println(sb.toString());
                    }
                }
            });
            return;
        }
        this.fieldIds = null;
        this.methodIds = null;
    }
    
    private Set<Integer> findAssignableTypes(final Dex dex, int i) {
        final HashSet<Integer> set = new HashSet<Integer>();
        set.add(i);
        for (final ClassDef classDef : dex.classDefs()) {
            if (set.contains(classDef.getSupertypeIndex())) {
                set.add(classDef.getTypeIndex());
            }
            else {
                final short[] interfaces = classDef.getInterfaces();
                int length;
                for (length = interfaces.length, i = 0; i < length; ++i) {
                    if (set.contains((int)interfaces[i])) {
                        set.add(classDef.getTypeIndex());
                        break;
                    }
                }
            }
        }
        return set;
    }
    
    private Set<Integer> getFieldIds(final Dex dex, final Set<Integer> set, final int n) {
        final HashSet<Integer> set2 = new HashSet<Integer>();
        int n2 = 0;
        for (final FieldId fieldId : dex.fieldIds()) {
            if (set.contains(fieldId.getNameIndex()) && n == fieldId.getDeclaringClassIndex()) {
                set2.add(n2);
            }
            ++n2;
        }
        return set2;
    }
    
    private Set<Integer> getMethodIds(final Dex dex, final Set<Integer> set, int n) {
        final Set<Integer> assignableTypes = this.findAssignableTypes(dex, n);
        final HashSet<Integer> set2 = new HashSet<Integer>();
        n = 0;
        for (final MethodId methodId : dex.methodIds()) {
            if (set.contains(methodId.getNameIndex()) && assignableTypes.contains(methodId.getDeclaringClassIndex())) {
                set2.add(n);
            }
            ++n;
        }
        return set2;
    }
    
    private String location() {
        final String s = this.dex.typeNames().get(this.currentClass.getTypeIndex());
        if (this.currentMethod != null) {
            final MethodId methodId = this.dex.methodIds().get(this.currentMethod.getMethodIndex());
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(".");
            sb.append(this.dex.strings().get(methodId.getNameIndex()));
            return sb.toString();
        }
        return s;
    }
    
    public void findUsages() {
        if (this.fieldIds == null) {
            return;
        }
        if (this.methodIds == null) {
            return;
        }
        for (final ClassDef currentClass : this.dex.classDefs()) {
            this.currentClass = currentClass;
            this.currentMethod = null;
            if (currentClass.getClassDataOffset() == 0) {
                continue;
            }
            final ClassData classData = this.dex.readClassData(currentClass);
            final ClassData.Field[] allFields = classData.allFields();
            final int length = allFields.length;
            final int n = 0;
            for (int i = 0; i < length; ++i) {
                final int fieldIndex = allFields[i].getFieldIndex();
                if (this.fieldIds.contains(fieldIndex)) {
                    final PrintWriter out = this.out;
                    final StringBuilder sb = new StringBuilder();
                    sb.append(this.location());
                    sb.append(" field declared ");
                    sb.append(this.dex.fieldIds().get(fieldIndex));
                    out.println(sb.toString());
                }
            }
            final ClassData.Method[] allMethods = classData.allMethods();
            for (int length2 = allMethods.length, j = n; j < length2; ++j) {
                final ClassData.Method currentMethod = allMethods[j];
                this.currentMethod = currentMethod;
                final int methodIndex = currentMethod.getMethodIndex();
                if (this.methodIds.contains(methodIndex)) {
                    final PrintWriter out2 = this.out;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(this.location());
                    sb2.append(" method declared ");
                    sb2.append(this.dex.methodIds().get(methodIndex));
                    out2.println(sb2.toString());
                }
                if (currentMethod.getCodeOffset() != 0) {
                    this.codeReader.visitAll(this.dex.readCode(currentMethod).getInstructions());
                }
            }
        }
        this.currentClass = null;
        this.currentMethod = null;
    }
}
