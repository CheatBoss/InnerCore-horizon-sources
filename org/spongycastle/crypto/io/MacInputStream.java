package org.spongycastle.crypto.io;

import org.spongycastle.crypto.*;
import java.io.*;

public class MacInputStream extends FilterInputStream
{
    protected Mac mac;
    
    public MacInputStream(final InputStream inputStream, final Mac mac) {
        super(inputStream);
        this.mac = mac;
    }
    
    public Mac getMac() {
        return this.mac;
    }
    
    @Override
    public int read() throws IOException {
        final int read = this.in.read();
        if (read >= 0) {
            this.mac.update((byte)read);
        }
        return read;
    }
    
    @Override
    public int read(final byte[] array, final int n, int read) throws IOException {
        read = this.in.read(array, n, read);
        if (read >= 0) {
            this.mac.update(array, n, read);
        }
        return read;
    }
}
