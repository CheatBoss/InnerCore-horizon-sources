package com.android.dx.dex.file;

import java.util.*;
import com.android.dx.util.*;

public final class Statistics
{
    private final HashMap<String, Data> dataMap;
    
    public Statistics() {
        this.dataMap = new HashMap<String, Data>(50);
    }
    
    public void add(final Item item) {
        final String typeName = item.typeName();
        final Data data = this.dataMap.get(typeName);
        if (data == null) {
            this.dataMap.put(typeName, new Data(item, typeName));
            return;
        }
        data.add(item);
    }
    
    public void addAll(final Section section) {
        final Iterator<? extends Item> iterator = section.items().iterator();
        while (iterator.hasNext()) {
            this.add((Item)iterator.next());
        }
    }
    
    public String toHuman() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Statistics:\n");
        final TreeMap<String, Data> treeMap = new TreeMap<String, Data>();
        for (final Data data : this.dataMap.values()) {
            treeMap.put(data.name, data);
        }
        final Iterator<Data> iterator2 = treeMap.values().iterator();
        while (iterator2.hasNext()) {
            sb.append(iterator2.next().toHuman());
        }
        return sb.toString();
    }
    
    public final void writeAnnotation(final AnnotatedOutput annotatedOutput) {
        if (this.dataMap.size() == 0) {
            return;
        }
        annotatedOutput.annotate(0, "\nstatistics:\n");
        final TreeMap<String, Data> treeMap = new TreeMap<String, Data>();
        for (final Data data : this.dataMap.values()) {
            treeMap.put(data.name, data);
        }
        final Iterator<Data> iterator2 = treeMap.values().iterator();
        while (iterator2.hasNext()) {
            iterator2.next().writeAnnotation(annotatedOutput);
        }
    }
    
    private static class Data
    {
        private int count;
        private int largestSize;
        private final String name;
        private int smallestSize;
        private int totalSize;
        
        public Data(final Item item, final String name) {
            final int writeSize = item.writeSize();
            this.name = name;
            this.count = 1;
            this.totalSize = writeSize;
            this.largestSize = writeSize;
            this.smallestSize = writeSize;
        }
        
        public void add(final Item item) {
            final int writeSize = item.writeSize();
            ++this.count;
            this.totalSize += writeSize;
            if (writeSize > this.largestSize) {
                this.largestSize = writeSize;
            }
            if (writeSize < this.smallestSize) {
                this.smallestSize = writeSize;
            }
        }
        
        public String toHuman() {
            final StringBuilder sb = new StringBuilder();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  ");
            sb2.append(this.name);
            sb2.append(": ");
            sb2.append(this.count);
            sb2.append(" item");
            String s;
            if (this.count == 1) {
                s = "";
            }
            else {
                s = "s";
            }
            sb2.append(s);
            sb2.append("; ");
            sb2.append(this.totalSize);
            sb2.append(" bytes total\n");
            sb.append(sb2.toString());
            if (this.smallestSize == this.largestSize) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("    ");
                sb3.append(this.smallestSize);
                sb3.append(" bytes/item\n");
                sb.append(sb3.toString());
            }
            else {
                final int n = this.totalSize / this.count;
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("    ");
                sb4.append(this.smallestSize);
                sb4.append("..");
                sb4.append(this.largestSize);
                sb4.append(" bytes/item; average ");
                sb4.append(n);
                sb4.append("\n");
                sb.append(sb4.toString());
            }
            return sb.toString();
        }
        
        public void writeAnnotation(final AnnotatedOutput annotatedOutput) {
            annotatedOutput.annotate(this.toHuman());
        }
    }
}
