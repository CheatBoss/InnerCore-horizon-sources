package com.android.dx.io;

import java.util.*;
import java.io.*;
import com.android.dex.*;

public final class DexIndexPrinter
{
    private final Dex dex;
    private final TableOfContents tableOfContents;
    
    public DexIndexPrinter(final File file) throws IOException {
        this.dex = new Dex(file);
        this.tableOfContents = this.dex.getTableOfContents();
    }
    
    public static void main(final String[] array) throws IOException {
        final DexIndexPrinter dexIndexPrinter = new DexIndexPrinter(new File(array[0]));
        dexIndexPrinter.printMap();
        dexIndexPrinter.printStrings();
        dexIndexPrinter.printTypeIds();
        dexIndexPrinter.printProtoIds();
        dexIndexPrinter.printFieldIds();
        dexIndexPrinter.printMethodIds();
        dexIndexPrinter.printTypeLists();
        dexIndexPrinter.printClassDefs();
    }
    
    private void printClassDefs() {
        int n = 0;
        for (final ClassDef classDef : this.dex.classDefs()) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("class def ");
            sb.append(n);
            sb.append(": ");
            sb.append(classDef);
            out.println(sb.toString());
            ++n;
        }
    }
    
    private void printFieldIds() throws IOException {
        int n = 0;
        for (final FieldId fieldId : this.dex.fieldIds()) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("field ");
            sb.append(n);
            sb.append(": ");
            sb.append(fieldId);
            out.println(sb.toString());
            ++n;
        }
    }
    
    private void printMap() {
        final TableOfContents.Section[] sections = this.tableOfContents.sections;
        for (int length = sections.length, i = 0; i < length; ++i) {
            final TableOfContents.Section section = sections[i];
            if (section.off != -1) {
                final PrintStream out = System.out;
                final StringBuilder sb = new StringBuilder();
                sb.append("section ");
                sb.append(Integer.toHexString(section.type));
                sb.append(" off=");
                sb.append(Integer.toHexString(section.off));
                sb.append(" size=");
                sb.append(Integer.toHexString(section.size));
                sb.append(" byteCount=");
                sb.append(Integer.toHexString(section.byteCount));
                out.println(sb.toString());
            }
        }
    }
    
    private void printMethodIds() throws IOException {
        int n = 0;
        for (final MethodId methodId : this.dex.methodIds()) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("methodId ");
            sb.append(n);
            sb.append(": ");
            sb.append(methodId);
            out.println(sb.toString());
            ++n;
        }
    }
    
    private void printProtoIds() throws IOException {
        int n = 0;
        for (final ProtoId protoId : this.dex.protoIds()) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("proto ");
            sb.append(n);
            sb.append(": ");
            sb.append(protoId);
            out.println(sb.toString());
            ++n;
        }
    }
    
    private void printStrings() throws IOException {
        int n = 0;
        for (final String s : this.dex.strings()) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("string ");
            sb.append(n);
            sb.append(": ");
            sb.append(s);
            out.println(sb.toString());
            ++n;
        }
    }
    
    private void printTypeIds() throws IOException {
        int n = 0;
        for (final Integer n2 : this.dex.typeIds()) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("type ");
            sb.append(n);
            sb.append(": ");
            sb.append(this.dex.strings().get(n2));
            out.println(sb.toString());
            ++n;
        }
    }
    
    private void printTypeLists() throws IOException {
        if (this.tableOfContents.typeLists.off == -1) {
            System.out.println("No type lists");
            return;
        }
        final Dex.Section open = this.dex.open(this.tableOfContents.typeLists.off);
        for (int i = 0; i < this.tableOfContents.typeLists.size; ++i) {
            final int int1 = open.readInt();
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("Type list i=");
            sb.append(i);
            sb.append(", size=");
            sb.append(int1);
            sb.append(", elements=");
            out.print(sb.toString());
            for (int j = 0; j < int1; ++j) {
                final PrintStream out2 = System.out;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(" ");
                sb2.append(this.dex.typeNames().get(open.readShort()));
                out2.print(sb2.toString());
            }
            if (int1 % 2 == 1) {
                open.readShort();
            }
            System.out.println();
        }
    }
}
