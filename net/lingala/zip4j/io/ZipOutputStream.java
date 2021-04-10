package net.lingala.zip4j.io;

import net.lingala.zip4j.model.*;
import java.io.*;

public class ZipOutputStream extends DeflaterOutputStream
{
    public ZipOutputStream(final OutputStream outputStream) {
        this(outputStream, null);
    }
    
    public ZipOutputStream(final OutputStream outputStream, final ZipModel zipModel) {
        super(outputStream, zipModel);
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
        this.crc.update(array, n, n2);
        this.updateTotalBytesRead(n2);
        super.write(array, n, n2);
    }
}
