package net.lingala.zip4j.io;

import net.lingala.zip4j.unzip.*;
import java.io.*;
import java.util.zip.*;

public class InflaterInputStream extends PartInputStream
{
    private byte[] buff;
    private long bytesWritten;
    private Inflater inflater;
    private byte[] oneByteBuff;
    private long uncompressedSize;
    private UnzipEngine unzipEngine;
    
    public InflaterInputStream(final RandomAccessFile randomAccessFile, final long n, final long n2, final UnzipEngine unzipEngine) {
        super(randomAccessFile, n, n2, unzipEngine);
        this.oneByteBuff = new byte[1];
        this.inflater = new Inflater(true);
        this.buff = new byte[4096];
        this.unzipEngine = unzipEngine;
        this.bytesWritten = 0L;
        this.uncompressedSize = unzipEngine.getFileHeader().getUncompressedSize();
    }
    
    private void fill() throws IOException {
        final int read = super.read(this.buff, 0, this.buff.length);
        if (read == -1) {
            throw new EOFException("Unexpected end of ZLIB input stream");
        }
        this.inflater.setInput(this.buff, 0, read);
    }
    
    @Override
    public int available() {
        return (this.inflater.finished() ^ true) ? 1 : 0;
    }
    
    @Override
    public void close() throws IOException {
        this.inflater.end();
        super.close();
    }
    
    @Override
    public UnzipEngine getUnzipEngine() {
        return super.getUnzipEngine();
    }
    
    @Override
    public int read() throws IOException {
        if (this.read(this.oneByteBuff, 0, 1) == -1) {
            return -1;
        }
        return this.oneByteBuff[0] & 0xFF;
    }
    
    @Override
    public int read(final byte[] array) throws IOException {
        if (array == null) {
            throw new NullPointerException("input buffer is null");
        }
        return this.read(array, 0, array.length);
    }
    
    @Override
    public int read(final byte[] array, final int n, final int n2) throws IOException {
        if (array == null) {
            throw new NullPointerException("input buffer is null");
        }
        if (n >= 0 && n2 >= 0) {
            if (n2 <= array.length - n) {
                if (n2 == 0) {
                    return 0;
                }
                try {
                    if (this.bytesWritten >= this.uncompressedSize) {
                        return -1;
                    }
                    while (true) {
                        final int inflate = this.inflater.inflate(array, n, n2);
                        if (inflate != 0) {
                            this.bytesWritten += inflate;
                            return inflate;
                        }
                        if (this.inflater.finished()) {
                            return -1;
                        }
                        if (this.inflater.needsDictionary()) {
                            return -1;
                        }
                        if (!this.inflater.needsInput()) {
                            continue;
                        }
                        this.fill();
                    }
                }
                catch (DataFormatException ex) {
                    String message = "Invalid ZLIB data format";
                    if (ex.getMessage() != null) {
                        message = ex.getMessage();
                    }
                    String string = message;
                    if (this.unzipEngine != null) {
                        string = message;
                        if (this.unzipEngine.getLocalFileHeader().isEncrypted()) {
                            string = message;
                            if (this.unzipEngine.getLocalFileHeader().getEncryptionMethod() == 0) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append(message);
                                sb.append(" - Wrong Password?");
                                string = sb.toString();
                            }
                        }
                    }
                    throw new IOException(string);
                }
                throw new IndexOutOfBoundsException();
            }
        }
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public void seek(final long n) throws IOException {
        super.seek(n);
    }
    
    @Override
    public long skip(final long n) throws IOException {
        if (n < 0L) {
            throw new IllegalArgumentException("negative skip length");
        }
        final int n2 = (int)Math.min(n, 2147483647L);
        int i = 0;
        final byte[] array = new byte[512];
        while (i < n2) {
            int length;
            if ((length = n2 - i) > array.length) {
                length = array.length;
            }
            final int read = this.read(array, 0, length);
            if (read == -1) {
                break;
            }
            i += read;
        }
        return i;
    }
}
