package okhttp3.internal.io;

import java.io.*;

public interface FileSystem
{
    public static final FileSystem SYSTEM = new FileSystem() {
        @Override
        public void delete(final File file) throws IOException {
            if (file.delete()) {
                return;
            }
            if (!file.exists()) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("failed to delete ");
            sb.append(file);
            throw new IOException(sb.toString());
        }
        
        @Override
        public boolean exists(final File file) {
            return file.exists();
        }
        
        @Override
        public void rename(final File file, final File file2) throws IOException {
            this.delete(file2);
            if (file.renameTo(file2)) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("failed to rename ");
            sb.append(file);
            sb.append(" to ");
            sb.append(file2);
            throw new IOException(sb.toString());
        }
        
        @Override
        public long size(final File file) {
            return file.length();
        }
    };
    
    void delete(final File p0) throws IOException;
    
    boolean exists(final File p0);
    
    void rename(final File p0, final File p1) throws IOException;
    
    long size(final File p0);
}
