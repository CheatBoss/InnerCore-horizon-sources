package org.spongycastle.jcajce.io;

import javax.crypto.*;
import java.io.*;

public final class MacOutputStream extends OutputStream
{
    private Mac mac;
    
    public MacOutputStream(final Mac mac) {
        this.mac = mac;
    }
    
    public byte[] getMac() {
        return this.mac.doFinal();
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
