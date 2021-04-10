package com.zhekasmirnov.innercore.modpack.installation;

import java.io.*;
import java.util.zip.*;

public class ZipFileExtractionTarget extends ModPackExtractionTarget implements Closeable
{
    private final File file;
    private final ZipOutputStream outputStream;
    
    public ZipFileExtractionTarget(final File file) throws FileNotFoundException {
        this.file = file;
        this.outputStream = new ZipOutputStream(new FileOutputStream(file));
    }
    
    @Override
    public void close() throws IOException {
        this.outputStream.close();
    }
    
    @Override
    public OutputStream write(final String s) throws IOException {
        this.outputStream.putNextEntry(new ZipEntry(s));
        return new OutputStream() {
            @Override
            public void close() throws IOException {
                ZipFileExtractionTarget.this.outputStream.closeEntry();
            }
            
            @Override
            public void write(final int n) throws IOException {
                ZipFileExtractionTarget.this.outputStream.write(n);
            }
            
            @Override
            public void write(final byte[] array) throws IOException {
                ZipFileExtractionTarget.this.outputStream.write(array);
            }
            
            @Override
            public void write(final byte[] array, final int n, final int n2) throws IOException {
                ZipFileExtractionTarget.this.outputStream.write(array, n, n2);
            }
        };
    }
}
