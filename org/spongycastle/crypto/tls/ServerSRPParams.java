package org.spongycastle.crypto.tls;

import java.math.*;
import org.spongycastle.util.*;
import java.io.*;

public class ServerSRPParams
{
    protected BigInteger B;
    protected BigInteger N;
    protected BigInteger g;
    protected byte[] s;
    
    public ServerSRPParams(final BigInteger n, final BigInteger g, final byte[] array, final BigInteger b) {
        this.N = n;
        this.g = g;
        this.s = Arrays.clone(array);
        this.B = b;
    }
    
    public static ServerSRPParams parse(final InputStream inputStream) throws IOException {
        return new ServerSRPParams(TlsSRPUtils.readSRPParameter(inputStream), TlsSRPUtils.readSRPParameter(inputStream), TlsUtils.readOpaque8(inputStream), TlsSRPUtils.readSRPParameter(inputStream));
    }
    
    public void encode(final OutputStream outputStream) throws IOException {
        TlsSRPUtils.writeSRPParameter(this.N, outputStream);
        TlsSRPUtils.writeSRPParameter(this.g, outputStream);
        TlsUtils.writeOpaque8(this.s, outputStream);
        TlsSRPUtils.writeSRPParameter(this.B, outputStream);
    }
    
    public BigInteger getB() {
        return this.B;
    }
    
    public BigInteger getG() {
        return this.g;
    }
    
    public BigInteger getN() {
        return this.N;
    }
    
    public byte[] getS() {
        return this.s;
    }
}
