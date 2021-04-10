package com.android.dx.dex.file;

import com.android.dx.dex.code.*;
import java.io.*;
import java.util.*;
import com.android.dx.util.*;

public final class CatchStructs
{
    private static final int TRY_ITEM_WRITE_SIZE = 8;
    private final DalvCode code;
    private int encodedHandlerHeaderSize;
    private byte[] encodedHandlers;
    private TreeMap<CatchHandlerList, Integer> handlerOffsets;
    private CatchTable table;
    
    public CatchStructs(final DalvCode code) {
        this.code = code;
        this.table = null;
        this.encodedHandlers = null;
        this.encodedHandlerHeaderSize = 0;
        this.handlerOffsets = null;
    }
    
    private static void annotateAndConsumeHandlers(final CatchHandlerList list, final int n, final int n2, final String s, final PrintWriter printWriter, final AnnotatedOutput annotatedOutput) {
        final StringBuilder sb = new StringBuilder();
        sb.append(Hex.u2(n));
        sb.append(": ");
        final String human = list.toHuman(s, sb.toString());
        if (printWriter != null) {
            printWriter.println(human);
        }
        annotatedOutput.annotate(n2, human);
    }
    
    private void annotateEntries(final String s, final PrintWriter printWriter, final AnnotatedOutput annotatedOutput) {
        this.finishProcessingIfNecessary();
        final boolean b = annotatedOutput != null;
        int n;
        if (b) {
            n = 6;
        }
        else {
            n = 0;
        }
        int n2;
        if (b) {
            n2 = 2;
        }
        else {
            n2 = 0;
        }
        final int size = this.table.size();
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("  ");
        final String string = sb.toString();
        if (b) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append("tries:");
            annotatedOutput.annotate(0, sb2.toString());
        }
        else {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append("tries:");
            printWriter.println(sb3.toString());
        }
        for (int i = 0; i < size; ++i) {
            final CatchTable.Entry value = this.table.get(i);
            final CatchHandlerList handlers = value.getHandlers();
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(string);
            sb4.append("try ");
            sb4.append(Hex.u2or4(value.getStart()));
            sb4.append("..");
            sb4.append(Hex.u2or4(value.getEnd()));
            final String string2 = sb4.toString();
            final String human = handlers.toHuman(string, "");
            if (b) {
                annotatedOutput.annotate(n, string2);
                annotatedOutput.annotate(n2, human);
            }
            else {
                printWriter.println(string2);
                printWriter.println(human);
            }
        }
        if (!b) {
            return;
        }
        final StringBuilder sb5 = new StringBuilder();
        sb5.append(s);
        sb5.append("handlers:");
        annotatedOutput.annotate(0, sb5.toString());
        final int encodedHandlerHeaderSize = this.encodedHandlerHeaderSize;
        final StringBuilder sb6 = new StringBuilder();
        sb6.append(string);
        sb6.append("size: ");
        sb6.append(Hex.u2(this.handlerOffsets.size()));
        annotatedOutput.annotate(encodedHandlerHeaderSize, sb6.toString());
        final Iterator<Map.Entry<CatchHandlerList, Integer>> iterator = this.handlerOffsets.entrySet().iterator();
        int n3 = 0;
        CatchHandlerList list = null;
        while (iterator.hasNext()) {
            final Map.Entry<CatchHandlerList, Integer> entry = iterator.next();
            final CatchHandlerList list2 = entry.getKey();
            final int intValue = entry.getValue();
            if (list != null) {
                annotateAndConsumeHandlers(list, n3, intValue - n3, string, printWriter, annotatedOutput);
            }
            list = list2;
            n3 = intValue;
        }
        annotateAndConsumeHandlers(list, n3, this.encodedHandlers.length - n3, string, printWriter, annotatedOutput);
    }
    
    private void finishProcessingIfNecessary() {
        if (this.table == null) {
            this.table = this.code.getCatches();
        }
    }
    
    public void debugPrint(final PrintWriter printWriter, final String s) {
        this.annotateEntries(s, printWriter, null);
    }
    
    public void encode(final DexFile dexFile) {
        this.finishProcessingIfNecessary();
        final TypeIdsSection typeIds = dexFile.getTypeIds();
        final int size = this.table.size();
        this.handlerOffsets = new TreeMap<CatchHandlerList, Integer>();
        for (int i = 0; i < size; ++i) {
            this.handlerOffsets.put(this.table.get(i).getHandlers(), null);
        }
        if (this.handlerOffsets.size() > 65535) {
            throw new UnsupportedOperationException("too many catch handlers");
        }
        final ByteArrayAnnotatedOutput byteArrayAnnotatedOutput = new ByteArrayAnnotatedOutput();
        this.encodedHandlerHeaderSize = byteArrayAnnotatedOutput.writeUleb128(this.handlerOffsets.size());
        for (final Map.Entry<CatchHandlerList, Integer> entry : this.handlerOffsets.entrySet()) {
            final CatchHandlerList list = entry.getKey();
            int size2 = list.size();
            final boolean catchesAll = list.catchesAll();
            entry.setValue(byteArrayAnnotatedOutput.getCursor());
            if (catchesAll) {
                byteArrayAnnotatedOutput.writeSleb128(-(size2 - 1));
                --size2;
            }
            else {
                byteArrayAnnotatedOutput.writeSleb128(size2);
            }
            for (int j = 0; j < size2; ++j) {
                final CatchHandlerList.Entry value = list.get(j);
                byteArrayAnnotatedOutput.writeUleb128(typeIds.indexOf(value.getExceptionType()));
                byteArrayAnnotatedOutput.writeUleb128(value.getHandler());
            }
            if (catchesAll) {
                byteArrayAnnotatedOutput.writeUleb128(list.get(size2).getHandler());
            }
        }
        this.encodedHandlers = byteArrayAnnotatedOutput.toByteArray();
    }
    
    public int triesSize() {
        this.finishProcessingIfNecessary();
        return this.table.size();
    }
    
    public int writeSize() {
        return this.triesSize() * 8 + this.encodedHandlers.length;
    }
    
    public void writeTo(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        this.finishProcessingIfNecessary();
        if (annotatedOutput.annotates()) {
            this.annotateEntries("  ", null, annotatedOutput);
        }
        for (int size = this.table.size(), i = 0; i < size; ++i) {
            final CatchTable.Entry value = this.table.get(i);
            final int start = value.getStart();
            final int end = value.getEnd();
            final int n = end - start;
            if (n >= 65536) {
                final StringBuilder sb = new StringBuilder();
                sb.append("bogus exception range: ");
                sb.append(Hex.u4(start));
                sb.append("..");
                sb.append(Hex.u4(end));
                throw new UnsupportedOperationException(sb.toString());
            }
            annotatedOutput.writeInt(start);
            annotatedOutput.writeShort(n);
            annotatedOutput.writeShort(this.handlerOffsets.get(value.getHandlers()));
        }
        annotatedOutput.write(this.encodedHandlers);
    }
}
