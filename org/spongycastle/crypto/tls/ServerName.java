package org.spongycastle.crypto.tls;

import java.io.*;

public class ServerName
{
    protected Object name;
    protected short nameType;
    
    public ServerName(final short nameType, final Object name) {
        if (isCorrectType(nameType, name)) {
            this.nameType = nameType;
            this.name = name;
            return;
        }
        throw new IllegalArgumentException("'name' is not an instance of the correct type");
    }
    
    protected static boolean isCorrectType(final short n, final Object o) {
        if (n == 0) {
            return o instanceof String;
        }
        throw new IllegalArgumentException("'nameType' is an unsupported NameType");
    }
    
    public static ServerName parse(final InputStream inputStream) throws IOException {
        final short uint8 = TlsUtils.readUint8(inputStream);
        if (uint8 != 0) {
            throw new TlsFatalAlert((short)50);
        }
        final byte[] opaque16 = TlsUtils.readOpaque16(inputStream);
        if (opaque16.length >= 1) {
            return new ServerName(uint8, new String(opaque16, "ASCII"));
        }
        throw new TlsFatalAlert((short)50);
    }
    
    public void encode(final OutputStream outputStream) throws IOException {
        TlsUtils.writeUint8(this.nameType, outputStream);
        if (this.nameType != 0) {
            throw new TlsFatalAlert((short)80);
        }
        final byte[] bytes = ((String)this.name).getBytes("ASCII");
        if (bytes.length >= 1) {
            TlsUtils.writeOpaque16(bytes, outputStream);
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public String getHostName() {
        if (isCorrectType((short)0, this.name)) {
            return (String)this.name;
        }
        throw new IllegalStateException("'name' is not a HostName string");
    }
    
    public Object getName() {
        return this.name;
    }
    
    public short getNameType() {
        return this.nameType;
    }
}
