package com.android.dx.command.grep;

import com.android.dx.io.*;
import java.io.*;
import java.util.regex.*;
import com.android.dx.io.instructions.*;
import java.util.*;
import com.android.dex.*;
import com.android.dex.util.*;

public final class Grep
{
    private final CodeReader codeReader;
    private int count;
    private ClassDef currentClass;
    private ClassData.Method currentMethod;
    private final Dex dex;
    private final PrintWriter out;
    private final Set<Integer> stringIds;
    
    public Grep(final Dex dex, final Pattern pattern, final PrintWriter out) {
        this.codeReader = new CodeReader();
        this.count = 0;
        this.dex = dex;
        this.out = out;
        this.stringIds = this.getStringIds(dex, pattern);
        this.codeReader.setStringVisitor((CodeReader.Visitor)new CodeReader.Visitor() {
            @Override
            public void visit(final DecodedInstruction[] array, final DecodedInstruction decodedInstruction) {
                Grep.this.encounterString(decodedInstruction.getIndex());
            }
        });
    }
    
    private void encounterString(final int n) {
        if (this.stringIds.contains(n)) {
            final PrintWriter out = this.out;
            final StringBuilder sb = new StringBuilder();
            sb.append(this.location());
            sb.append(" ");
            sb.append(this.dex.strings().get(n));
            out.println(sb.toString());
            ++this.count;
        }
    }
    
    private Set<Integer> getStringIds(final Dex dex, final Pattern pattern) {
        final HashSet<Integer> set = new HashSet<Integer>();
        int n = 0;
        final Iterator<String> iterator = dex.strings().iterator();
        while (iterator.hasNext()) {
            if (pattern.matcher(iterator.next()).find()) {
                set.add(n);
            }
            ++n;
        }
        return set;
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
    
    private void readArray(final EncodedValueReader encodedValueReader) {
        for (int i = 0; i < encodedValueReader.readArray(); ++i) {
            final int peek = encodedValueReader.peek();
            if (peek != 23) {
                if (peek == 28) {
                    this.readArray(encodedValueReader);
                }
            }
            else {
                this.encounterString(encodedValueReader.readString());
            }
        }
    }
    
    public int grep() {
        for (final ClassDef currentClass : this.dex.classDefs()) {
            this.currentClass = currentClass;
            this.currentMethod = null;
            if (currentClass.getClassDataOffset() == 0) {
                continue;
            }
            final ClassData classData = this.dex.readClassData(currentClass);
            final int staticValuesOffset = currentClass.getStaticValuesOffset();
            if (staticValuesOffset != 0) {
                this.readArray(new EncodedValueReader(this.dex.open(staticValuesOffset)));
            }
            final ClassData.Method[] allMethods = classData.allMethods();
            for (int length = allMethods.length, i = 0; i < length; ++i) {
                final ClassData.Method currentMethod = allMethods[i];
                this.currentMethod = currentMethod;
                if (currentMethod.getCodeOffset() != 0) {
                    this.codeReader.visitAll(this.dex.readCode(currentMethod).getInstructions());
                }
            }
        }
        this.currentClass = null;
        this.currentMethod = null;
        return this.count;
    }
}
