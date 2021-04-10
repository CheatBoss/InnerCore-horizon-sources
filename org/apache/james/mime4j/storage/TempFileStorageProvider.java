package org.apache.james.mime4j.storage;

import java.util.*;
import java.io.*;

public class TempFileStorageProvider extends AbstractStorageProvider
{
    private static final String DEFAULT_PREFIX = "m4j";
    private final File directory;
    private final String prefix;
    private final String suffix;
    
    public TempFileStorageProvider() {
        this("m4j", null, null);
    }
    
    public TempFileStorageProvider(final File file) {
        this("m4j", null, file);
    }
    
    public TempFileStorageProvider(final String prefix, final String suffix, final File directory) {
        if (prefix == null || prefix.length() < 3) {
            throw new IllegalArgumentException("invalid prefix");
        }
        if (directory != null && !directory.isDirectory() && !directory.mkdirs()) {
            throw new IllegalArgumentException("invalid directory");
        }
        this.prefix = prefix;
        this.suffix = suffix;
        this.directory = directory;
    }
    
    @Override
    public StorageOutputStream createStorageOutputStream() throws IOException {
        final File tempFile = File.createTempFile(this.prefix, this.suffix, this.directory);
        tempFile.deleteOnExit();
        return new TempFileStorageOutputStream(tempFile);
    }
    
    private static final class TempFileStorage implements Storage
    {
        private static final Set<File> filesToDelete;
        private File file;
        
        static {
            filesToDelete = new HashSet<File>();
        }
        
        public TempFileStorage(final File file) {
            this.file = file;
        }
        
        @Override
        public void delete() {
            synchronized (TempFileStorage.filesToDelete) {
                if (this.file != null) {
                    TempFileStorage.filesToDelete.add(this.file);
                    this.file = null;
                }
                final Iterator<File> iterator = TempFileStorage.filesToDelete.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().delete()) {
                        iterator.remove();
                    }
                }
            }
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            if (this.file != null) {
                return new BufferedInputStream(new FileInputStream(this.file));
            }
            throw new IllegalStateException("storage has been deleted");
        }
    }
    
    private static final class TempFileStorageOutputStream extends StorageOutputStream
    {
        private File file;
        private OutputStream out;
        
        public TempFileStorageOutputStream(final File file) throws IOException {
            this.file = file;
            this.out = new FileOutputStream(file);
        }
        
        @Override
        public void close() throws IOException {
            super.close();
            this.out.close();
        }
        
        @Override
        protected Storage toStorage0() throws IOException {
            return new TempFileStorage(this.file);
        }
        
        @Override
        protected void write0(final byte[] array, final int n, final int n2) throws IOException {
            this.out.write(array, n, n2);
        }
    }
}
