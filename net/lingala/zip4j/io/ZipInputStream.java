package net.lingala.zip4j.io;

import java.io.*;
import net.lingala.zip4j.exception.*;

public class ZipInputStream extends InputStream
{
    private BaseInputStream is;
    
    public ZipInputStream(final BaseInputStream is) {
        this.is = is;
    }
    
    @Override
    public int available() throws IOException {
        return this.is.available();
    }
    
    @Override
    public void close() throws IOException {
        this.close(false);
    }
    
    public void close(final boolean b) throws IOException {
        try {
            this.is.close();
            if (!b && this.is.getUnzipEngine() != null) {
                this.is.getUnzipEngine().checkCRC();
            }
        }
        catch (ZipException ex) {
            throw new IOException(ex.getMessage());
        }
    }
    
    @Override
    public int read() throws IOException {
        final int read = this.is.read();
        if (read != -1) {
            this.is.getUnzipEngine().updateCRC(read);
        }
        return read;
    }
    
    @Override
    public int read(final byte[] array) throws IOException {
        return this.read(array, 0, array.length);
    }
    
    @Override
    public int read(final byte[] array, final int n, int read) throws IOException {
        read = this.is.read(array, n, read);
        if (read > 0 && this.is.getUnzipEngine() != null) {
            this.is.getUnzipEngine().updateCRC(array, n, read);
        }
        return read;
    }
    
    @Override
    public long skip(final long n) throws IOException {
        return this.is.skip(n);
    }
}
