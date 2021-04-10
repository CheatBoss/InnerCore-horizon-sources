package org.spongycastle.crypto.tls;

import java.util.*;
import org.spongycastle.util.*;
import java.io.*;
import org.spongycastle.util.io.*;

public class ServerNameList
{
    protected Vector serverNameList;
    
    public ServerNameList(final Vector serverNameList) {
        if (serverNameList != null) {
            this.serverNameList = serverNameList;
            return;
        }
        throw new IllegalArgumentException("'serverNameList' must not be null");
    }
    
    private static short[] checkNameType(final short[] array, final short n) {
        if (NameType.isValid(n) && !Arrays.contains(array, n)) {
            return Arrays.append(array, n);
        }
        return null;
    }
    
    public static ServerNameList parse(final InputStream inputStream) throws IOException {
        final int uint16 = TlsUtils.readUint16(inputStream);
        if (uint16 >= 1) {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TlsUtils.readFully(uint16, inputStream));
            short[] checkNameType = new short[0];
            final Vector<ServerName> vector = new Vector<ServerName>();
            while (byteArrayInputStream.available() > 0) {
                final ServerName parse = ServerName.parse(byteArrayInputStream);
                checkNameType = checkNameType(checkNameType, parse.getNameType());
                if (checkNameType == null) {
                    throw new TlsFatalAlert((short)47);
                }
                vector.addElement(parse);
            }
            return new ServerNameList(vector);
        }
        throw new TlsFatalAlert((short)50);
    }
    
    public void encode(final OutputStream outputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        short[] checkNameType = new short[0];
        while (i < this.serverNameList.size()) {
            final ServerName serverName = this.serverNameList.elementAt(i);
            checkNameType = checkNameType(checkNameType, serverName.getNameType());
            if (checkNameType == null) {
                throw new TlsFatalAlert((short)80);
            }
            serverName.encode(byteArrayOutputStream);
            ++i;
        }
        TlsUtils.checkUint16(byteArrayOutputStream.size());
        TlsUtils.writeUint16(byteArrayOutputStream.size(), outputStream);
        Streams.writeBufTo(byteArrayOutputStream, outputStream);
    }
    
    public Vector getServerNameList() {
        return this.serverNameList;
    }
}
