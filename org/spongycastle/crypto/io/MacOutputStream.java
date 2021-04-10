package org.spongycastle.crypto.io;

import org.spongycastle.crypto.*;
import java.io.*;

public class MacOutputStream extends OutputStream
{
    protected Mac mac;
    
    public MacOutputStream(final Mac mac) {
        this.mac = mac;
    }
    
    public byte[] getMac() {
        final byte[] array = new byte[this.mac.getMacSize()];
        this.mac.doFinal(array, 0);
        return array;
    }
    
    @Override
    public void write(final int n) throws IOException {
        this.mac.update((byte)n);
    }
    
    @Override
    public void write(final byte[] array, final int n, final int n2) throws IOException {
        this.mac.update(array, n, n2);
    }
}
