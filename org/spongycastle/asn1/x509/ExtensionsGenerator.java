package org.spongycastle.asn1.x509;

import java.util.*;
import java.io.*;
import org.spongycastle.asn1.*;

public class ExtensionsGenerator
{
    private Vector extOrdering;
    private Hashtable extensions;
    
    public ExtensionsGenerator() {
        this.extensions = new Hashtable();
        this.extOrdering = new Vector();
    }
    
    public void addExtension(final ASN1ObjectIdentifier asn1ObjectIdentifier, final boolean b, final ASN1Encodable asn1Encodable) throws IOException {
        this.addExtension(asn1ObjectIdentifier, b, asn1Encodable.toASN1Primitive().getEncoded("DER"));
    }
    
    public void addExtension(final ASN1ObjectIdentifier asn1ObjectIdentifier, final boolean b, final byte[] array) {
        if (!this.extensions.containsKey(asn1ObjectIdentifier)) {
            this.extOrdering.addElement(asn1ObjectIdentifier);
            this.extensions.put(asn1ObjectIdentifier, new Extension(asn1ObjectIdentifier, b, new DEROctetString(array)));
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("extension ");
        sb.append(asn1ObjectIdentifier);
        sb.append(" already added");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public void addExtension(final Extension extension) {
        if (!this.extensions.containsKey(extension.getExtnId())) {
            this.extOrdering.addElement(extension.getExtnId());
            this.extensions.put(extension.getExtnId(), extension);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("extension ");
        sb.append(extension.getExtnId());
        sb.append(" already added");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public Extensions generate() {
        final Extension[] array = new Extension[this.extOrdering.size()];
        for (int i = 0; i != this.extOrdering.size(); ++i) {
            array[i] = (Extension)this.extensions.get(this.extOrdering.elementAt(i));
        }
        return new Extensions(array);
    }
    
    public boolean isEmpty() {
        return this.extOrdering.isEmpty();
    }
    
    public void reset() {
        this.extensions = new Hashtable();
        this.extOrdering = new Vector();
    }
}
