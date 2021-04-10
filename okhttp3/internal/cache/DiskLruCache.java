package okhttp3.internal.cache;

import java.util.regex.*;
import java.util.concurrent.*;
import okhttp3.internal.io.*;
import okio.*;
import java.util.*;
import java.io.*;

public final class DiskLruCache implements Closeable, Flushable
{
    static final Pattern LEGAL_KEY_PATTERN;
    private final Runnable cleanupRunnable;
    boolean closed;
    private final Executor executor;
    final FileSystem fileSystem;
    boolean initialized;
    BufferedSink journalWriter;
    final LinkedHashMap<String, Entry> lruEntries;
    private long maxSize;
    boolean mostRecentTrimFailed;
    private long nextSequenceNumber;
    int redundantOpCount;
    private long size;
    final int valueCount;
    
    static {
        LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,120}");
    }
    
    private void checkNotClosed() {
        synchronized (this) {
            if (!this.isClosed()) {
                return;
            }
            throw new IllegalStateException("cache is closed");
        }
    }
    
    @Override
    public void close() throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: monitorenter   
        //     2: aload_0        
        //     3: getfield        okhttp3/internal/cache/DiskLruCache.initialized:Z
        //     6: ifeq            105
        //     9: aload_0        
        //    10: getfield        okhttp3/internal/cache/DiskLruCache.closed:Z
        //    13: ifeq            19
        //    16: goto            105
        //    19: aload_0        
        //    20: getfield        okhttp3/internal/cache/DiskLruCache.lruEntries:Ljava/util/LinkedHashMap;
        //    23: invokevirtual   java/util/LinkedHashMap.values:()Ljava/util/Collection;
        //    26: aload_0        
        //    27: getfield        okhttp3/internal/cache/DiskLruCache.lruEntries:Ljava/util/LinkedHashMap;
        //    30: invokevirtual   java/util/LinkedHashMap.size:()I
        //    33: anewarray       Lokhttp3/internal/cache/DiskLruCache$Entry;
        //    36: invokeinterface java/util/Collection.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //    41: checkcast       [Lokhttp3/internal/cache/DiskLruCache$Entry;
        //    44: astore_3       
        //    45: aload_3        
        //    46: arraylength    
        //    47: istore_2       
        //    48: iconst_0       
        //    49: istore_1       
        //    50: iload_1        
        //    51: iload_2        
        //    52: if_icmpge       79
        //    55: aload_3        
        //    56: iload_1        
        //    57: aaload         
        //    58: astore          4
        //    60: aload           4
        //    62: getfield        okhttp3/internal/cache/DiskLruCache$Entry.currentEditor:Lokhttp3/internal/cache/DiskLruCache$Editor;
        //    65: ifnull          118
        //    68: aload           4
        //    70: getfield        okhttp3/internal/cache/DiskLruCache$Entry.currentEditor:Lokhttp3/internal/cache/DiskLruCache$Editor;
        //    73: invokevirtual   okhttp3/internal/cache/DiskLruCache$Editor.abort:()V
        //    76: goto            118
        //    79: aload_0        
        //    80: invokevirtual   okhttp3/internal/cache/DiskLruCache.trimToSize:()V
        //    83: aload_0        
        //    84: getfield        okhttp3/internal/cache/DiskLruCache.journalWriter:Lokio/BufferedSink;
        //    87: invokeinterface okio/BufferedSink.close:()V
        //    92: aload_0        
        //    93: aconst_null    
        //    94: putfield        okhttp3/internal/cache/DiskLruCache.journalWriter:Lokio/BufferedSink;
        //    97: aload_0        
        //    98: iconst_1       
        //    99: putfield        okhttp3/internal/cache/DiskLruCache.closed:Z
        //   102: aload_0        
        //   103: monitorexit    
        //   104: return         
        //   105: aload_0        
        //   106: iconst_1       
        //   107: putfield        okhttp3/internal/cache/DiskLruCache.closed:Z
        //   110: aload_0        
        //   111: monitorexit    
        //   112: return         
        //   113: astore_3       
        //   114: aload_0        
        //   115: monitorexit    
        //   116: aload_3        
        //   117: athrow         
        //   118: iload_1        
        //   119: iconst_1       
        //   120: iadd           
        //   121: istore_1       
        //   122: goto            50
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  2      16     113    118    Any
        //  19     48     113    118    Any
        //  60     76     113    118    Any
        //  79     102    113    118    Any
        //  105    110    113    118    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    void completeEdit(final Editor editor, final boolean b) throws IOException {
        while (true) {
            while (true) {
                int n2;
                synchronized (this) {
                    final Entry entry = editor.entry;
                    if (entry.currentEditor != editor) {
                        throw new IllegalStateException();
                    }
                    final int n = n2 = 0;
                    if (b) {
                        n2 = n;
                        if (!entry.readable) {
                            int n3 = 0;
                            while (true) {
                                n2 = n;
                                if (n3 >= this.valueCount) {
                                    break;
                                }
                                if (!editor.written[n3]) {
                                    editor.abort();
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("Newly created entry didn't create value for index ");
                                    sb.append(n3);
                                    throw new IllegalStateException(sb.toString());
                                }
                                if (!this.fileSystem.exists(entry.dirtyFiles[n3])) {
                                    editor.abort();
                                    return;
                                }
                                ++n3;
                            }
                        }
                    }
                    if (n2 >= this.valueCount) {
                        ++this.redundantOpCount;
                        entry.currentEditor = null;
                        if (entry.readable | b) {
                            entry.readable = true;
                            this.journalWriter.writeUtf8("CLEAN").writeByte(32);
                            this.journalWriter.writeUtf8(entry.key);
                            entry.writeLengths(this.journalWriter);
                            this.journalWriter.writeByte(10);
                            if (b) {
                                entry.sequenceNumber = this.nextSequenceNumber++;
                            }
                        }
                        else {
                            this.lruEntries.remove(entry.key);
                            this.journalWriter.writeUtf8("REMOVE").writeByte(32);
                            this.journalWriter.writeUtf8(entry.key);
                            this.journalWriter.writeByte(10);
                        }
                        this.journalWriter.flush();
                        if (this.size > this.maxSize || this.journalRebuildRequired()) {
                            this.executor.execute(this.cleanupRunnable);
                        }
                        return;
                    }
                    final File file = entry.dirtyFiles[n2];
                    if (b) {
                        if (this.fileSystem.exists(file)) {
                            final File file2 = entry.cleanFiles[n2];
                            this.fileSystem.rename(file, file2);
                            final long n4 = entry.lengths[n2];
                            final long size = this.fileSystem.size(file2);
                            entry.lengths[n2] = size;
                            this.size = this.size - n4 + size;
                        }
                    }
                    else {
                        this.fileSystem.delete(file);
                    }
                }
                ++n2;
                continue;
            }
        }
    }
    
    @Override
    public void flush() throws IOException {
        synchronized (this) {
            if (!this.initialized) {
                return;
            }
            this.checkNotClosed();
            this.trimToSize();
            this.journalWriter.flush();
        }
    }
    
    public boolean isClosed() {
        synchronized (this) {
            return this.closed;
        }
    }
    
    boolean journalRebuildRequired() {
        final int redundantOpCount = this.redundantOpCount;
        return redundantOpCount >= 2000 && redundantOpCount >= this.lruEntries.size();
    }
    
    boolean removeEntry(final Entry entry) throws IOException {
        if (entry.currentEditor != null) {
            entry.currentEditor.detach();
        }
        for (int i = 0; i < this.valueCount; ++i) {
            this.fileSystem.delete(entry.cleanFiles[i]);
            this.size -= entry.lengths[i];
            entry.lengths[i] = 0L;
        }
        ++this.redundantOpCount;
        this.journalWriter.writeUtf8("REMOVE").writeByte(32).writeUtf8(entry.key).writeByte(10);
        this.lruEntries.remove(entry.key);
        if (this.journalRebuildRequired()) {
            this.executor.execute(this.cleanupRunnable);
        }
        return true;
    }
    
    void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            this.removeEntry(this.lruEntries.values().iterator().next());
        }
        this.mostRecentTrimFailed = false;
    }
    
    public final class Editor
    {
        private boolean done;
        final Entry entry;
        final /* synthetic */ DiskLruCache this$0;
        final boolean[] written;
        
        public void abort() throws IOException {
            synchronized (this.this$0) {
                if (!this.done) {
                    if (this.entry.currentEditor == this) {
                        this.this$0.completeEdit(this, false);
                    }
                    this.done = true;
                    return;
                }
                throw new IllegalStateException();
            }
        }
        
        void detach() {
            if (this.entry.currentEditor == this) {
                for (int i = 0; i < this.this$0.valueCount; ++i) {
                    try {
                        this.this$0.fileSystem.delete(this.entry.dirtyFiles[i]);
                    }
                    catch (IOException ex) {}
                }
                this.entry.currentEditor = null;
            }
        }
    }
    
    private final class Entry
    {
        final File[] cleanFiles;
        Editor currentEditor;
        final File[] dirtyFiles;
        final String key;
        final long[] lengths;
        boolean readable;
        long sequenceNumber;
        
        void writeLengths(final BufferedSink bufferedSink) throws IOException {
            final long[] lengths = this.lengths;
            for (int length = lengths.length, i = 0; i < length; ++i) {
                bufferedSink.writeByte(32).writeDecimalLong(lengths[i]);
            }
        }
    }
}
