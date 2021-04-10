package org.spongycastle.crypto.tls;

import java.util.*;
import org.spongycastle.asn1.x500.*;
import java.io.*;

public class CertificateRequest
{
    protected Vector certificateAuthorities;
    protected short[] certificateTypes;
    protected Vector supportedSignatureAlgorithms;
    
    public CertificateRequest(final short[] certificateTypes, final Vector supportedSignatureAlgorithms, final Vector certificateAuthorities) {
        this.certificateTypes = certificateTypes;
        this.supportedSignatureAlgorithms = supportedSignatureAlgorithms;
        this.certificateAuthorities = certificateAuthorities;
    }
    
    public static CertificateRequest parse(final TlsContext tlsContext, final InputStream inputStream) throws IOException {
        final short uint8 = TlsUtils.readUint8(inputStream);
        final short[] array = new short[uint8];
        for (int i = 0; i < uint8; ++i) {
            array[i] = TlsUtils.readUint8(inputStream);
        }
        Vector supportedSignatureAlgorithms = null;
        if (TlsUtils.isTLSv12(tlsContext)) {
            supportedSignatureAlgorithms = TlsUtils.parseSupportedSignatureAlgorithms(false, inputStream);
        }
        final Vector<X500Name> vector = new Vector<X500Name>();
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TlsUtils.readOpaque16(inputStream));
        while (byteArrayInputStream.available() > 0) {
            vector.addElement(X500Name.getInstance(TlsUtils.readDERObject(TlsUtils.readOpaque16(byteArrayInputStream))));
        }
        return new CertificateRequest(array, supportedSignatureAlgorithms, vector);
    }
    
    public void encode(final OutputStream outputStream) throws IOException {
        final short[] certificateTypes = this.certificateTypes;
        final int n = 0;
        if (certificateTypes != null && certificateTypes.length != 0) {
            TlsUtils.writeUint8ArrayWithUint8Length(certificateTypes, outputStream);
        }
        else {
            TlsUtils.writeUint8(0, outputStream);
        }
        final Vector supportedSignatureAlgorithms = this.supportedSignatureAlgorithms;
        if (supportedSignatureAlgorithms != null) {
            TlsUtils.encodeSupportedSignatureAlgorithms(supportedSignatureAlgorithms, false, outputStream);
        }
        final Vector certificateAuthorities = this.certificateAuthorities;
        if (certificateAuthorities != null && !certificateAuthorities.isEmpty()) {
            final Vector<byte[]> vector = new Vector<byte[]>(this.certificateAuthorities.size());
            int i = 0;
            int n2 = 0;
            while (i < this.certificateAuthorities.size()) {
                final byte[] encoded = this.certificateAuthorities.elementAt(i).getEncoded("DER");
                vector.addElement(encoded);
                n2 += encoded.length + 2;
                ++i;
            }
            TlsUtils.checkUint16(n2);
            TlsUtils.writeUint16(n2, outputStream);
            for (int j = n; j < vector.size(); ++j) {
                TlsUtils.writeOpaque16(vector.elementAt(j), outputStream);
            }
        }
        else {
            TlsUtils.writeUint16(0, outputStream);
        }
    }
    
    public Vector getCertificateAuthorities() {
        return this.certificateAuthorities;
    }
    
    public short[] getCertificateTypes() {
        return this.certificateTypes;
    }
    
    public Vector getSupportedSignatureAlgorithms() {
        return this.supportedSignatureAlgorithms;
    }
}
