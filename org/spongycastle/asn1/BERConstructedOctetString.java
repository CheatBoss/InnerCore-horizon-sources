package org.spongycastle.asn1;

import java.util.*;
import java.io.*;

public class BERConstructedOctetString extends BEROctetString
{
    private static final int MAX_LENGTH = 1000;
    private Vector octs;
    
    public BERConstructedOctetString(final Vector octs) {
        super(toBytes(octs));
        this.octs = octs;
    }
    
    public BERConstructedOctetString(final ASN1Encodable asn1Encodable) {
        this(asn1Encodable.toASN1Primitive());
    }
    
    public BERConstructedOctetString(final ASN1Primitive asn1Primitive) {
        super(toByteArray(asn1Primitive));
    }
    
    public BERConstructedOctetString(final byte[] array) {
        super(array);
    }
    
    public static BEROctetString fromSequence(final ASN1Sequence asn1Sequence) {
        final Vector<Object> vector = new Vector<Object>();
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            vector.addElement(objects.nextElement());
        }
        return new BERConstructedOctetString(vector);
    }
    
    private Vector generateOcts() {
        final Vector<DEROctetString> vector = new Vector<DEROctetString>();
        int n;
        for (int i = 0; i < this.string.length; i = n) {
            n = i + 1000;
            int length;
            if (n > this.string.length) {
                length = this.string.length;
            }
            else {
                length = n;
            }
            final int n2 = length - i;
            final byte[] array = new byte[n2];
            System.arraycopy(this.string, i, array, 0, n2);
            vector.addElement(new DEROctetString(array));
        }
        return vector;
    }
    
    private static byte[] toByteArray(final ASN1Primitive asn1Primitive) {
        try {
            return asn1Primitive.getEncoded();
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Unable to encode object");
        }
    }
    
    private static byte[] toBytes(final Vector vector) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        while (i != vector.size()) {
            try {
                byteArrayOutputStream.write(vector.elementAt(i).getOctets());
                ++i;
                continue;
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("exception converting octets ");
                sb.append(ex.toString());
                throw new IllegalArgumentException(sb.toString());
            }
            catch (ClassCastException ex2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(vector.elementAt(i).getClass().getName());
                sb2.append(" found in input should only contain DEROctetString");
                throw new IllegalArgumentException(sb2.toString());
            }
            break;
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    @Override
    public Enumeration getObjects() {
        final Vector octs = this.octs;
        if (octs == null) {
            return this.generateOcts().elements();
        }
        return octs.elements();
    }
    
    @Override
    public byte[] getOctets() {
        return this.string;
    }
}
