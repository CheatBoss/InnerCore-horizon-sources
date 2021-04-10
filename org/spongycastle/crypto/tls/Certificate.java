package org.spongycastle.crypto.tls;

import java.util.*;
import java.io.*;

public class Certificate
{
    public static final Certificate EMPTY_CHAIN;
    protected org.spongycastle.asn1.x509.Certificate[] certificateList;
    
    static {
        EMPTY_CHAIN = new Certificate(new org.spongycastle.asn1.x509.Certificate[0]);
    }
    
    public Certificate(final org.spongycastle.asn1.x509.Certificate[] certificateList) {
        if (certificateList != null) {
            this.certificateList = certificateList;
            return;
        }
        throw new IllegalArgumentException("'certificateList' cannot be null");
    }
    
    public static Certificate parse(final InputStream inputStream) throws IOException {
        final int uint24 = TlsUtils.readUint24(inputStream);
        if (uint24 == 0) {
            return Certificate.EMPTY_CHAIN;
        }
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TlsUtils.readFully(uint24, inputStream));
        final Vector<org.spongycastle.asn1.x509.Certificate> vector = new Vector<org.spongycastle.asn1.x509.Certificate>();
        while (byteArrayInputStream.available() > 0) {
            vector.addElement(org.spongycastle.asn1.x509.Certificate.getInstance(TlsUtils.readASN1Object(TlsUtils.readOpaque24(byteArrayInputStream))));
        }
        final org.spongycastle.asn1.x509.Certificate[] array = new org.spongycastle.asn1.x509.Certificate[vector.size()];
        for (int i = 0; i < vector.size(); ++i) {
            array[i] = vector.elementAt(i);
        }
        return new Certificate(array);
    }
    
    protected org.spongycastle.asn1.x509.Certificate[] cloneCertificateList() {
        final org.spongycastle.asn1.x509.Certificate[] certificateList = this.certificateList;
        final int length = certificateList.length;
        final org.spongycastle.asn1.x509.Certificate[] array = new org.spongycastle.asn1.x509.Certificate[length];
        System.arraycopy(certificateList, 0, array, 0, length);
        return array;
    }
    
    public void encode(final OutputStream outputStream) throws IOException {
        final Vector<byte[]> vector = new Vector<byte[]>(this.certificateList.length);
        final int n = 0;
        int n2 = 0;
        int n3 = 0;
        while (true) {
            final org.spongycastle.asn1.x509.Certificate[] certificateList = this.certificateList;
            if (n2 >= certificateList.length) {
                break;
            }
            final byte[] encoded = certificateList[n2].getEncoded("DER");
            vector.addElement(encoded);
            n3 += encoded.length + 3;
            ++n2;
        }
        TlsUtils.checkUint24(n3);
        TlsUtils.writeUint24(n3, outputStream);
        for (int i = n; i < vector.size(); ++i) {
            TlsUtils.writeOpaque24(vector.elementAt(i), outputStream);
        }
    }
    
    public org.spongycastle.asn1.x509.Certificate getCertificateAt(final int n) {
        return this.certificateList[n];
    }
    
    public org.spongycastle.asn1.x509.Certificate[] getCertificateList() {
        return this.cloneCertificateList();
    }
    
    public int getLength() {
        return this.certificateList.length;
    }
    
    public boolean isEmpty() {
        return this.certificateList.length == 0;
    }
}
