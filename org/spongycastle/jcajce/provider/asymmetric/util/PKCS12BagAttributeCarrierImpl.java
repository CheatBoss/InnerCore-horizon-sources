package org.spongycastle.jcajce.provider.asymmetric.util;

import org.spongycastle.jce.interfaces.*;
import java.util.*;
import org.spongycastle.asn1.*;
import java.io.*;

public class PKCS12BagAttributeCarrierImpl implements PKCS12BagAttributeCarrier
{
    private Hashtable pkcs12Attributes;
    private Vector pkcs12Ordering;
    
    public PKCS12BagAttributeCarrierImpl() {
        this(new Hashtable(), new Vector());
    }
    
    PKCS12BagAttributeCarrierImpl(final Hashtable pkcs12Attributes, final Vector pkcs12Ordering) {
        this.pkcs12Attributes = pkcs12Attributes;
        this.pkcs12Ordering = pkcs12Ordering;
    }
    
    Hashtable getAttributes() {
        return this.pkcs12Attributes;
    }
    
    @Override
    public ASN1Encodable getBagAttribute(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        return this.pkcs12Attributes.get(asn1ObjectIdentifier);
    }
    
    @Override
    public Enumeration getBagAttributeKeys() {
        return this.pkcs12Ordering.elements();
    }
    
    Vector getOrdering() {
        return this.pkcs12Ordering;
    }
    
    public void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        final Object object = objectInputStream.readObject();
        if (object instanceof Hashtable) {
            this.pkcs12Attributes = (Hashtable)object;
            this.pkcs12Ordering = (Vector)objectInputStream.readObject();
            return;
        }
        final ASN1InputStream asn1InputStream = new ASN1InputStream((byte[])object);
        while (true) {
            final ASN1ObjectIdentifier asn1ObjectIdentifier = (ASN1ObjectIdentifier)asn1InputStream.readObject();
            if (asn1ObjectIdentifier == null) {
                break;
            }
            this.setBagAttribute(asn1ObjectIdentifier, asn1InputStream.readObject());
        }
    }
    
    @Override
    public void setBagAttribute(final ASN1ObjectIdentifier asn1ObjectIdentifier, final ASN1Encodable asn1Encodable) {
        if (this.pkcs12Attributes.containsKey(asn1ObjectIdentifier)) {
            this.pkcs12Attributes.put(asn1ObjectIdentifier, asn1Encodable);
            return;
        }
        this.pkcs12Attributes.put(asn1ObjectIdentifier, asn1Encodable);
        this.pkcs12Ordering.addElement(asn1ObjectIdentifier);
    }
    
    int size() {
        return this.pkcs12Ordering.size();
    }
    
    public void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        Object byteArray;
        if (this.pkcs12Ordering.size() == 0) {
            objectOutputStream.writeObject(new Hashtable());
            byteArray = new Vector();
        }
        else {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final ASN1OutputStream asn1OutputStream = new ASN1OutputStream(byteArrayOutputStream);
            final Enumeration bagAttributeKeys = this.getBagAttributeKeys();
            while (bagAttributeKeys.hasMoreElements()) {
                final ASN1ObjectIdentifier asn1ObjectIdentifier = bagAttributeKeys.nextElement();
                asn1OutputStream.writeObject(asn1ObjectIdentifier);
                asn1OutputStream.writeObject((ASN1Encodable)this.pkcs12Attributes.get(asn1ObjectIdentifier));
            }
            byteArray = byteArrayOutputStream.toByteArray();
        }
        objectOutputStream.writeObject(byteArray);
    }
}
