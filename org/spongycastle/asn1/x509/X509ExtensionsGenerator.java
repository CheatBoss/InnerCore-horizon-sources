package org.spongycastle.asn1.x509;

import java.util.*;
import java.io.*;
import org.spongycastle.asn1.*;

public class X509ExtensionsGenerator
{
    private Vector extOrdering;
    private Hashtable extensions;
    
    public X509ExtensionsGenerator() {
        this.extensions = new Hashtable();
        this.extOrdering = new Vector();
    }
    
    public void addExtension(final ASN1ObjectIdentifier asn1ObjectIdentifier, final boolean b, final ASN1Encodable asn1Encodable) {
        try {
            this.addExtension(asn1ObjectIdentifier, b, asn1Encodable.toASN1Primitive().getEncoded("DER"));
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("error encoding value: ");
            sb.append(ex);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public void addExtension(final ASN1ObjectIdentifier asn1ObjectIdentifier, final boolean b, final byte[] array) {
        if (!this.extensions.containsKey(asn1ObjectIdentifier)) {
            this.extOrdering.addElement(asn1ObjectIdentifier);
            this.extensions.put(asn1ObjectIdentifier, new X509Extension(b, new DEROctetString(array)));
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("extension ");
        sb.append(asn1ObjectIdentifier);
        sb.append(" already added");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public X509Extensions generate() {
        return new X509Extensions(this.extOrdering, this.extensions);
    }
    
    public boolean isEmpty() {
        return this.extOrdering.isEmpty();
    }
    
    public void reset() {
        this.extensions = new Hashtable();
        this.extOrdering = new Vector();
    }
}
