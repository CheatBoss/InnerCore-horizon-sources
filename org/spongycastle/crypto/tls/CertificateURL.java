package org.spongycastle.crypto.tls;

import java.util.*;
import java.io.*;

public class CertificateURL
{
    protected short type;
    protected Vector urlAndHashList;
    
    public CertificateURL(final short type, final Vector urlAndHashList) {
        if (!CertChainType.isValid(type)) {
            throw new IllegalArgumentException("'type' is not a valid CertChainType value");
        }
        if (urlAndHashList != null && !urlAndHashList.isEmpty()) {
            this.type = type;
            this.urlAndHashList = urlAndHashList;
            return;
        }
        throw new IllegalArgumentException("'urlAndHashList' must have length > 0");
    }
    
    public static CertificateURL parse(final TlsContext tlsContext, final InputStream inputStream) throws IOException {
        final short uint8 = TlsUtils.readUint8(inputStream);
        if (!CertChainType.isValid(uint8)) {
            throw new TlsFatalAlert((short)50);
        }
        final int uint9 = TlsUtils.readUint16(inputStream);
        if (uint9 >= 1) {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TlsUtils.readFully(uint9, inputStream));
            final Vector<URLAndHash> vector = new Vector<URLAndHash>();
            while (byteArrayInputStream.available() > 0) {
                vector.addElement(URLAndHash.parse(tlsContext, byteArrayInputStream));
            }
            return new CertificateURL(uint8, vector);
        }
        throw new TlsFatalAlert((short)50);
    }
    
    public void encode(final OutputStream outputStream) throws IOException {
        TlsUtils.writeUint8(this.type, outputStream);
        final ListBuffer16 listBuffer16 = new ListBuffer16();
        for (int i = 0; i < this.urlAndHashList.size(); ++i) {
            ((URLAndHash)this.urlAndHashList.elementAt(i)).encode(listBuffer16);
        }
        listBuffer16.encodeTo(outputStream);
    }
    
    public short getType() {
        return this.type;
    }
    
    public Vector getURLAndHashList() {
        return this.urlAndHashList;
    }
    
    class ListBuffer16 extends ByteArrayOutputStream
    {
        ListBuffer16() throws IOException {
            TlsUtils.writeUint16(0, this);
        }
        
        void encodeTo(final OutputStream outputStream) throws IOException {
            final int n = this.count - 2;
            TlsUtils.checkUint16(n);
            TlsUtils.writeUint16(n, this.buf, 0);
            outputStream.write(this.buf, 0, this.count);
            this.buf = null;
        }
    }
}
