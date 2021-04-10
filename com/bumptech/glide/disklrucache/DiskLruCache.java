package com.bumptech.glide.disklrucache;

import java.util.concurrent.*;
import java.io.*;
import java.util.*;

public final class DiskLruCache implements Closeable
{
    static final long ANY_SEQUENCE_NUMBER = -1L;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final String MAGIC = "libcore.io.DiskLruCache";
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    static final String VERSION_1 = "1";
    private final int appVersion;
    private final Callable<Void> cleanupCallable;
    private final File directory;
    final ThreadPoolExecutor executorService;
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    private Writer journalWriter;
    private final LinkedHashMap<String, Entry> lruEntries;
    private long maxSize;
    private long nextSequenceNumber;
    private int redundantOpCount;
    private long size;
    private final int valueCount;
    
    private DiskLruCache(final File directory, final int appVersion, final int valueCount, final long maxSize) {
        this.size = 0L;
        this.lruEntries = new LinkedHashMap<String, Entry>(0, 0.75f, true);
        this.nextSequenceNumber = 0L;
        this.executorService = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        this.cleanupCallable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                synchronized (DiskLruCache.this) {
                    if (DiskLruCache.this.journalWriter == null) {
                        return null;
                    }
                    DiskLruCache.this.trimToSize();
                    if (DiskLruCache.this.journalRebuildRequired()) {
                        DiskLruCache.this.rebuildJournal();
                        DiskLruCache.this.redundantOpCount = 0;
                    }
                    return null;
                }
            }
        };
        this.directory = directory;
        this.appVersion = appVersion;
        this.journalFile = new File(directory, "journal");
        this.journalFileTmp = new File(directory, "journal.tmp");
        this.journalFileBackup = new File(directory, "journal.bkp");
        this.valueCount = valueCount;
        this.maxSize = maxSize;
    }
    
    private void checkNotClosed() {
        if (this.journalWriter == null) {
            throw new IllegalStateException("cache is closed");
        }
    }
    
    private void completeEdit(final Editor editor, final boolean b) throws IOException {
        while (true) {
            while (true) {
                int i;
                synchronized (this) {
                    final Entry access$1400 = editor.entry;
                    if (access$1400.currentEditor != editor) {
                        throw new IllegalStateException();
                    }
                    final int n = 0;
                    if (b && !access$1400.readable) {
                        for (i = 0; i < this.valueCount; ++i) {
                            if (!editor.written[i]) {
                                editor.abort();
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Newly created entry didn't create value for index ");
                                sb.append(i);
                                throw new IllegalStateException(sb.toString());
                            }
                            if (!access$1400.getDirtyFile(i).exists()) {
                                editor.abort();
                                return;
                            }
                        }
                    }
                    i = n;
                    if (i >= this.valueCount) {
                        ++this.redundantOpCount;
                        access$1400.currentEditor = null;
                        if (access$1400.readable | b) {
                            access$1400.readable = true;
                            this.journalWriter.append((CharSequence)"CLEAN");
                            this.journalWriter.append(' ');
                            this.journalWriter.append((CharSequence)access$1400.key);
                            this.journalWriter.append((CharSequence)access$1400.getLengths());
                            this.journalWriter.append('\n');
                            if (b) {
                                access$1400.sequenceNumber = this.nextSequenceNumber++;
                            }
                        }
                        else {
                            this.lruEntries.remove(access$1400.key);
                            this.journalWriter.append((CharSequence)"REMOVE");
                            this.journalWriter.append(' ');
                            this.journalWriter.append((CharSequence)access$1400.key);
                            this.journalWriter.append('\n');
                        }
                        this.journalWriter.flush();
                        if (this.size > this.maxSize || this.journalRebuildRequired()) {
                            this.executorService.submit(this.cleanupCallable);
                        }
                        return;
                    }
                    final File dirtyFile = access$1400.getDirtyFile(i);
                    if (b) {
                        if (dirtyFile.exists()) {
                            final File cleanFile = access$1400.getCleanFile(i);
                            dirtyFile.renameTo(cleanFile);
                            final long n2 = access$1400.lengths[i];
                            final long length = cleanFile.length();
                            access$1400.lengths[i] = length;
                            this.size = this.size - n2 + length;
                        }
                    }
                    else {
                        deleteIfExists(dirtyFile);
                    }
                }
                ++i;
                continue;
            }
        }
    }
    
    private static void deleteIfExists(final File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }
    
    private Editor edit(final String s, final long n) throws IOException {
        synchronized (this) {
            this.checkNotClosed();
            Entry entry = this.lruEntries.get(s);
            if (n != -1L && (entry == null || entry.sequenceNumber != n)) {
                return null;
            }
            if (entry == null) {
                entry = new Entry(s);
                this.lruEntries.put(s, entry);
            }
            else if (entry.currentEditor != null) {
                return null;
            }
            final Editor editor = new Editor(entry);
            entry.currentEditor = editor;
            this.journalWriter.append((CharSequence)"DIRTY");
            this.journalWriter.append(' ');
            this.journalWriter.append((CharSequence)s);
            this.journalWriter.append('\n');
            this.journalWriter.flush();
            return editor;
        }
    }
    
    private static String inputStreamToString(final InputStream inputStream) throws IOException {
        return Util.readFully(new InputStreamReader(inputStream, Util.UTF_8));
    }
    
    private boolean journalRebuildRequired() {
        return this.redundantOpCount >= 2000 && this.redundantOpCount >= this.lruEntries.size();
    }
    
    public static DiskLruCache open(final File file, final int n, final int n2, final long n3) throws IOException {
        if (n3 <= 0L) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (n2 <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        }
        final File file2 = new File(file, "journal.bkp");
        if (file2.exists()) {
            final File file3 = new File(file, "journal");
            if (file3.exists()) {
                file2.delete();
            }
            else {
                renameTo(file2, file3, false);
            }
        }
        final DiskLruCache diskLruCache = new DiskLruCache(file, n, n2, n3);
        if (diskLruCache.journalFile.exists()) {
            try {
                diskLruCache.readJournal();
                diskLruCache.processJournal();
                return diskLruCache;
            }
            catch (IOException ex) {
                final PrintStream out = System.out;
                final StringBuilder sb = new StringBuilder();
                sb.append("DiskLruCache ");
                sb.append(file);
                sb.append(" is corrupt: ");
                sb.append(ex.getMessage());
                sb.append(", removing");
                out.println(sb.toString());
                diskLruCache.delete();
            }
        }
        file.mkdirs();
        final DiskLruCache diskLruCache2 = new DiskLruCache(file, n, n2, n3);
        diskLruCache2.rebuildJournal();
        return diskLruCache2;
    }
    
    private void processJournal() throws IOException {
        deleteIfExists(this.journalFileTmp);
        final Iterator<Entry> iterator = this.lruEntries.values().iterator();
        while (iterator.hasNext()) {
            final Entry entry = iterator.next();
            final Editor access$700 = entry.currentEditor;
            final int n = 0;
            int i = 0;
            if (access$700 == null) {
                while (i < this.valueCount) {
                    this.size += entry.lengths[i];
                    ++i;
                }
            }
            else {
                entry.currentEditor = null;
                for (int j = n; j < this.valueCount; ++j) {
                    deleteIfExists(entry.getCleanFile(j));
                    deleteIfExists(entry.getDirtyFile(j));
                }
                iterator.remove();
            }
        }
    }
    
    private void readJournal() throws IOException {
        final StrictLineReader strictLineReader = new StrictLineReader(new FileInputStream(this.journalFile), Util.US_ASCII);
        try {
            final String line = strictLineReader.readLine();
            final String line2 = strictLineReader.readLine();
            final String line3 = strictLineReader.readLine();
            final String line4 = strictLineReader.readLine();
            final String line5 = strictLineReader.readLine();
            if ("libcore.io.DiskLruCache".equals(line) && "1".equals(line2) && Integer.toString(this.appVersion).equals(line3) && Integer.toString(this.valueCount).equals(line4)) {
                if ("".equals(line5)) {
                    int n = 0;
                    try {
                        while (true) {
                            this.readJournalLine(strictLineReader.readLine());
                            ++n;
                        }
                    }
                    catch (EOFException ex) {
                        this.redundantOpCount = n - this.lruEntries.size();
                        if (strictLineReader.hasUnterminatedLine()) {
                            this.rebuildJournal();
                        }
                        else {
                            this.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFile, true), Util.US_ASCII));
                        }
                        return;
                    }
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("unexpected journal header: [");
            sb.append(line);
            sb.append(", ");
            sb.append(line2);
            sb.append(", ");
            sb.append(line4);
            sb.append(", ");
            sb.append(line5);
            sb.append("]");
            throw new IOException(sb.toString());
        }
        finally {
            Util.closeQuietly(strictLineReader);
        }
    }
    
    private void readJournalLine(final String s) throws IOException {
        final int index = s.indexOf(32);
        if (index == -1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("unexpected journal line: ");
            sb.append(s);
            throw new IOException(sb.toString());
        }
        final int n = index + 1;
        final int index2 = s.indexOf(32, n);
        String s3;
        if (index2 == -1) {
            final String s2 = s3 = s.substring(n);
            if (index == "REMOVE".length()) {
                s3 = s2;
                if (s.startsWith("REMOVE")) {
                    this.lruEntries.remove(s2);
                    return;
                }
            }
        }
        else {
            s3 = s.substring(n, index2);
        }
        Entry entry;
        if ((entry = this.lruEntries.get(s3)) == null) {
            entry = new Entry(s3);
            this.lruEntries.put(s3, entry);
        }
        if (index2 != -1 && index == "CLEAN".length() && s.startsWith("CLEAN")) {
            final String[] split = s.substring(index2 + 1).split(" ");
            entry.readable = true;
            entry.currentEditor = null;
            entry.setLengths(split);
            return;
        }
        if (index2 == -1 && index == "DIRTY".length() && s.startsWith("DIRTY")) {
            entry.currentEditor = new Editor(entry);
            return;
        }
        if (index2 == -1 && index == "READ".length() && s.startsWith("READ")) {
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("unexpected journal line: ");
        sb2.append(s);
        throw new IOException(sb2.toString());
    }
    
    private void rebuildJournal() throws IOException {
        synchronized (this) {
            if (this.journalWriter != null) {
                this.journalWriter.close();
            }
            final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFileTmp), Util.US_ASCII));
            try {
                bufferedWriter.write("libcore.io.DiskLruCache");
                bufferedWriter.write("\n");
                bufferedWriter.write("1");
                bufferedWriter.write("\n");
                bufferedWriter.write(Integer.toString(this.appVersion));
                bufferedWriter.write("\n");
                bufferedWriter.write(Integer.toString(this.valueCount));
                bufferedWriter.write("\n");
                bufferedWriter.write("\n");
                for (final Entry entry : this.lruEntries.values()) {
                    if (entry.currentEditor != null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("DIRTY ");
                        sb.append(entry.key);
                        sb.append('\n');
                        bufferedWriter.write(sb.toString());
                    }
                    else {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("CLEAN ");
                        sb2.append(entry.key);
                        sb2.append(entry.getLengths());
                        sb2.append('\n');
                        bufferedWriter.write(sb2.toString());
                    }
                }
                bufferedWriter.close();
                if (this.journalFile.exists()) {
                    renameTo(this.journalFile, this.journalFileBackup, true);
                }
                renameTo(this.journalFileTmp, this.journalFile, false);
                this.journalFileBackup.delete();
                this.journalWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.journalFile, true), Util.US_ASCII));
            }
            finally {
                bufferedWriter.close();
            }
        }
    }
    
    private static void renameTo(final File file, final File file2, final boolean b) throws IOException {
        if (b) {
            deleteIfExists(file2);
        }
        if (!file.renameTo(file2)) {
            throw new IOException();
        }
    }
    
    private void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            this.remove(this.lruEntries.entrySet().iterator().next().getKey());
        }
    }
    
    @Override
    public void close() throws IOException {
        synchronized (this) {
            if (this.journalWriter == null) {
                return;
            }
            for (final Entry entry : new ArrayList<Entry>(this.lruEntries.values())) {
                if (entry.currentEditor != null) {
                    entry.currentEditor.abort();
                }
            }
            this.trimToSize();
            this.journalWriter.close();
            this.journalWriter = null;
        }
    }
    
    public void delete() throws IOException {
        this.close();
        Util.deleteContents(this.directory);
    }
    
    public Editor edit(final String s) throws IOException {
        return this.edit(s, -1L);
    }
    
    public void flush() throws IOException {
        synchronized (this) {
            this.checkNotClosed();
            this.trimToSize();
            this.journalWriter.flush();
        }
    }
    
    public Value get(final String s) throws IOException {
        synchronized (this) {
            this.checkNotClosed();
            final Entry entry = this.lruEntries.get(s);
            if (entry == null) {
                return null;
            }
            if (!entry.readable) {
                return null;
            }
            final File[] cleanFiles = entry.cleanFiles;
            for (int length = cleanFiles.length, i = 0; i < length; ++i) {
                if (!cleanFiles[i].exists()) {
                    return null;
                }
            }
            ++this.redundantOpCount;
            this.journalWriter.append((CharSequence)"READ");
            this.journalWriter.append(' ');
            this.journalWriter.append((CharSequence)s);
            this.journalWriter.append('\n');
            if (this.journalRebuildRequired()) {
                this.executorService.submit(this.cleanupCallable);
            }
            return new Value(s, entry.sequenceNumber, entry.cleanFiles, entry.lengths);
        }
    }
    
    public File getDirectory() {
        return this.directory;
    }
    
    public long getMaxSize() {
        synchronized (this) {
            return this.maxSize;
        }
    }
    
    public boolean isClosed() {
        synchronized (this) {
            return this.journalWriter == null;
        }
    }
    
    public boolean remove(final String s) throws IOException {
    Label_0037_Outer:
        while (true) {
            while (true) {
                Label_0221: {
                    synchronized (this) {
                        this.checkNotClosed();
                        final Entry entry = this.lruEntries.get(s);
                        int n = 0;
                        if (entry != null && entry.currentEditor == null) {
                            break Label_0221;
                        }
                        return false;
                        Label_0208: {
                            return true;
                        }
                        // iftrue(Label_0131:, n >= this.valueCount)
                        // iftrue(Label_0208:, !this.journalRebuildRequired())
                        // iftrue(Label_0100:, !cleanFile.exists() || cleanFile.delete())
                        Block_8: {
                            while (true) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("failed to delete ");
                                final File cleanFile;
                                sb.append(cleanFile);
                                throw new IOException(sb.toString());
                                Block_5: {
                                    break Block_5;
                                    Label_0131:
                                    ++this.redundantOpCount;
                                    this.journalWriter.append((CharSequence)"REMOVE");
                                    this.journalWriter.append(' ');
                                    final CharSequence charSequence;
                                    this.journalWriter.append(charSequence);
                                    this.journalWriter.append('\n');
                                    this.lruEntries.remove(charSequence);
                                    break Block_8;
                                }
                                cleanFile = entry.getCleanFile(n);
                                continue Label_0037_Outer;
                            }
                            Label_0100:
                            this.size -= entry.lengths[n];
                            entry.lengths[n] = 0L;
                            ++n;
                            continue;
                        }
                        this.executorService.submit(this.cleanupCallable);
                        return true;
                    }
                }
                continue;
            }
        }
    }
    
    public void setMaxSize(final long maxSize) {
        synchronized (this) {
            this.maxSize = maxSize;
            this.executorService.submit(this.cleanupCallable);
        }
    }
    
    public long size() {
        synchronized (this) {
            return this.size;
        }
    }
    
    public final class Editor
    {
        private boolean committed;
        private final Entry entry;
        private final boolean[] written;
        
        private Editor(final Entry entry) {
            this.entry = entry;
            boolean[] written;
            if (entry.readable) {
                written = null;
            }
            else {
                written = new boolean[DiskLruCache.this.valueCount];
            }
            this.written = written;
        }
        
        private InputStream newInputStream(final int n) throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                if (!this.entry.readable) {
                    return null;
                }
                try {
                    return new FileInputStream(this.entry.getCleanFile(n));
                }
                catch (FileNotFoundException ex) {
                    return null;
                }
            }
        }
        
        public void abort() throws IOException {
            DiskLruCache.this.completeEdit(this, false);
        }
        
        public void abortUnlessCommitted() {
            if (!this.committed) {
                try {
                    this.abort();
                }
                catch (IOException ex) {}
            }
        }
        
        public void commit() throws IOException {
            DiskLruCache.this.completeEdit(this, true);
            this.committed = true;
        }
        
        public File getFile(final int n) throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                if (!this.entry.readable) {
                    this.written[n] = true;
                }
                final File dirtyFile = this.entry.getDirtyFile(n);
                if (!DiskLruCache.this.directory.exists()) {
                    DiskLruCache.this.directory.mkdirs();
                }
                return dirtyFile;
            }
        }
        
        public String getString(final int n) throws IOException {
            final InputStream inputStream = this.newInputStream(n);
            if (inputStream != null) {
                return inputStreamToString(inputStream);
            }
            return null;
        }
        
        public void set(final int n, final String s) throws IOException {
            Closeable closeable = null;
            try {
                ((Writer)(closeable = new OutputStreamWriter(new FileOutputStream(this.getFile(n)), Util.UTF_8))).write(s);
            }
            finally {
                Util.closeQuietly(closeable);
            }
        }
    }
    
    private final class Entry
    {
        File[] cleanFiles;
        private Editor currentEditor;
        File[] dirtyFiles;
        private final String key;
        private final long[] lengths;
        private boolean readable;
        private long sequenceNumber;
        
        private Entry(final String key) {
            this.key = key;
            this.lengths = new long[DiskLruCache.this.valueCount];
            this.cleanFiles = new File[DiskLruCache.this.valueCount];
            this.dirtyFiles = new File[DiskLruCache.this.valueCount];
            final StringBuilder append = new StringBuilder(key).append('.');
            final int length = append.length();
            for (int i = 0; i < DiskLruCache.this.valueCount; ++i) {
                append.append(i);
                this.cleanFiles[i] = new File(DiskLruCache.this.directory, append.toString());
                append.append(".tmp");
                this.dirtyFiles[i] = new File(DiskLruCache.this.directory, append.toString());
                append.setLength(length);
            }
        }
        
        private IOException invalidLengths(final String[] array) throws IOException {
            final StringBuilder sb = new StringBuilder();
            sb.append("unexpected journal line: ");
            sb.append(Arrays.toString(array));
            throw new IOException(sb.toString());
        }
        
        private void setLengths(final String[] array) throws IOException {
            if (array.length != DiskLruCache.this.valueCount) {
                throw this.invalidLengths(array);
            }
            int i = 0;
            try {
                while (i < array.length) {
                    this.lengths[i] = Long.parseLong(array[i]);
                    ++i;
                }
            }
            catch (NumberFormatException ex) {
                throw this.invalidLengths(array);
            }
        }
        
        public File getCleanFile(final int n) {
            return this.cleanFiles[n];
        }
        
        public File getDirtyFile(final int n) {
            return this.dirtyFiles[n];
        }
        
        public String getLengths() throws IOException {
            final StringBuilder sb = new StringBuilder();
            final long[] lengths = this.lengths;
            for (int length = lengths.length, i = 0; i < length; ++i) {
                final long n = lengths[i];
                sb.append(' ');
                sb.append(n);
            }
            return sb.toString();
        }
    }
    
    public final class Value
    {
        private final File[] files;
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;
        
        private Value(final String key, final long sequenceNumber, final File[] files, final long[] lengths) {
            this.key = key;
            this.sequenceNumber = sequenceNumber;
            this.files = files;
            this.lengths = lengths;
        }
        
        public Editor edit() throws IOException {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }
        
        public File getFile(final int n) {
            return this.files[n];
        }
        
        public long getLength(final int n) {
            return this.lengths[n];
        }
        
        public String getString(final int n) throws IOException {
            return inputStreamToString(new FileInputStream(this.files[n]));
        }
    }
}
