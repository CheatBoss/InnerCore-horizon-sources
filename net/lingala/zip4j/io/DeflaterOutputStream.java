package net.lingala.zip4j.io;

import java.util.zip.*;
import net.lingala.zip4j.exception.*;
import java.io.*;
import net.lingala.zip4j.model.*;

public class DeflaterOutputStream extends CipherOutputStream
{
    private byte[] buff;
    protected Deflater deflater;
    private boolean firstBytesRead;
    
    public DeflaterOutputStream(final OutputStream outputStream, final ZipModel zipModel) {
        super(outputStream, zipModel);
        this.deflater = new Deflater();
        this.buff = new byte[4096];
        this.firstBytesRead = false;
    }
    
    private void deflate() throws IOException {
        final int deflate = this.deflater.deflate(this.buff, 0, this.buff.length);
        if (deflate > 0) {
            int n = deflate;
            if (this.deflater.finished()) {
                if (deflate == 4) {
                    return;
                }
                if (deflate < 4) {
                    this.decrementCompressedFileSize(4 - deflate);
                    return;
                }
                n = deflate - 4;
            }
            if (!this.firstBytesRead) {
                super.write(this.buff, 2, n - 2);
                this.firstBytesRead = true;
                return;
            }
            super.write(this.buff, 0, n);
        }
    }
    
    @Override
    public void closeEntry() throws IOException, ZipException {
        if (this.zipParameters.getCompressionMethod() == 8) {
            if (!this.deflater.finished()) {
                this.deflater.finish();
                while (!this.deflater.finished()) {
                    this.deflate();
                }
            }
            this.firstBytesRead = false;
        }
        super.closeEntry();
    }
    
    @Override
    public void finish() throws IOException, ZipException {
        super.finish();
    }
    
    @Override
    public void putNextEntry(final File file, final ZipParameters zipParameters) throws ZipException {
        super.putNextEntry(file, zipParameters);
        if (zipParameters.getCompressionMethod() == 8) {
            this.deflater.reset();
            if ((zipParameters.getCompressionLevel() < 0 || zipParameters.getCompressionLevel() > 9) && zipParameters.getCompressionLevel() != -1) {
                throw new ZipException("invalid compression level for deflater. compression level should be in the range of 0-9");
            }
            this.deflater.setLevel(zipParameters.getCompressionLevel());
        }
    }
    
    @Override
    public void write(final int n) throws IOException {
        this.write(new byte[] { (byte)n }, 0, 1);
    }
    
    @Override
    public void write(final byte[] array) throws IOException {
        this.write(array, 0, array.length);
    }
    
    @Override
    public void write(final byte[] array, final int n, final int n2) throws IOException {
        if (this.zipParameters.getCompressionMethod() != 8) {
            super.write(array, n, n2);
            return;
        }
        this.deflater.setInput(array, n, n2);
        while (!this.deflater.needsInput()) {
            this.deflate();
        }
    }
}
