package org.spongycastle.jce;

import org.spongycastle.asn1.x509.*;
import java.security.*;
import java.util.*;
import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.*;
import java.io.*;

public class X509Principal extends X509Name implements Principal
{
    public X509Principal(final String s) {
        super(s);
    }
    
    public X509Principal(final Hashtable hashtable) {
        super(hashtable);
    }
    
    public X509Principal(final Vector vector, final Hashtable hashtable) {
        super(vector, hashtable);
    }
    
    public X509Principal(final Vector vector, final Vector vector2) {
        super(vector, vector2);
    }
    
    public X509Principal(final X500Name x500Name) {
        super((ASN1Sequence)x500Name.toASN1Primitive());
    }
    
    public X509Principal(final X509Name x509Name) {
        super((ASN1Sequence)x509Name.toASN1Primitive());
    }
    
    public X509Principal(final boolean b, final String s) {
        super(b, s);
    }
    
    public X509Principal(final boolean b, final Hashtable hashtable, final String s) {
        super(b, hashtable, s);
    }
    
    public X509Principal(final byte[] array) throws IOException {
        super(readSequence(new ASN1InputStream(array)));
    }
    
    private static ASN1Sequence readSequence(final ASN1InputStream asn1InputStream) throws IOException {
        try {
            return ASN1Sequence.getInstance(asn1InputStream.readObject());
        }
        catch (IllegalArgumentException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("not an ASN.1 Sequence: ");
            sb.append(ex);
            throw new IOException(sb.toString());
        }
    }
    
    @Override
    public byte[] getEncoded() {
        try {
            return this.getEncoded("DER");
        }
        catch (IOException ex) {
            throw new RuntimeException(ex.toString());
        }
    }
    
    @Override
    public String getName() {
        return this.toString();
    }
}
