package org.spongycastle.util.io;

import java.io.*;

public class TeeOutputStream extends OutputStream
{
    private OutputStream output1;
    private OutputStream output2;
    
    public TeeOutputStream(final OutputStream output1, final OutputStream output2) {
        this.output1 = output1;
        this.output2 = output2;
    }
    
    @Override
    public void close() throws IOException {
        this.output1.close();
        this.output2.close();
    }
    
    @Override
    public void flush() throws IOException {
        this.output1.flush();
        this.output2.flush();
    }
    
    @Override
    public void write(final int n) throws IOException {
        this.output1.write(n);
        this.output2.write(n);
    }
    
    @Override
    public void write(final byte[] array) throws IOException {
        this.output1.write(array);
        this.output2.write(array);
    }
    
    @Override
    public void write(final byte[] array, final int n, final int n2) throws IOException {
        this.output1.write(array, n, n2);
        this.output2.write(array, n, n2);
    }
}
