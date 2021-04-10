package org.spongycastle.asn1.eac;

import java.io.*;

public class CertificateHolderReference
{
    private static final String ReferenceEncoding = "ISO-8859-1";
    private String countryCode;
    private String holderMnemonic;
    private String sequenceNumber;
    
    public CertificateHolderReference(final String countryCode, final String holderMnemonic, final String sequenceNumber) {
        this.countryCode = countryCode;
        this.holderMnemonic = holderMnemonic;
        this.sequenceNumber = sequenceNumber;
    }
    
    CertificateHolderReference(final byte[] array) {
        try {
            final String s = new String(array, "ISO-8859-1");
            this.countryCode = s.substring(0, 2);
            this.holderMnemonic = s.substring(2, s.length() - 5);
            this.sequenceNumber = s.substring(s.length() - 5);
        }
        catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex.toString());
        }
    }
    
    public String getCountryCode() {
        return this.countryCode;
    }
    
    public byte[] getEncoded() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.countryCode);
        sb.append(this.holderMnemonic);
        sb.append(this.sequenceNumber);
        final String string = sb.toString();
        try {
            return string.getBytes("ISO-8859-1");
        }
        catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex.toString());
        }
    }
    
    public String getHolderMnemonic() {
        return this.holderMnemonic;
    }
    
    public String getSequenceNumber() {
        return this.sequenceNumber;
    }
}
