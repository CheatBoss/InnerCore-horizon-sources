package org.spongycastle.asn1;

import java.util.*;
import java.io.*;

public class BEROctetString extends ASN1OctetString
{
    private static final int MAX_LENGTH = 1000;
    private ASN1OctetString[] octs;
    
    public BEROctetString(final byte[] array) {
        super(array);
    }
    
    public BEROctetString(final ASN1OctetString[] octs) {
        super(toBytes(octs));
        this.octs = octs;
    }
    
    static BEROctetString fromSequence(final ASN1Sequence asn1Sequence) {
        final ASN1OctetString[] array = new ASN1OctetString[asn1Sequence.size()];
        final Enumeration objects = asn1Sequence.getObjects();
        int n = 0;
        while (objects.hasMoreElements()) {
            array[n] = objects.nextElement();
            ++n;
        }
        return new BEROctetString(array);
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
    
    private static byte[] toBytes(final ASN1OctetString[] array) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 0;
        while (i != array.length) {
            try {
                byteArrayOutputStream.write(((DEROctetString)array[i]).getOctets());
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
                sb2.append(array[i].getClass().getName());
                sb2.append(" found in input should only contain DEROctetString");
                throw new IllegalArgumentException(sb2.toString());
            }
            break;
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    public void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.write(36);
        asn1OutputStream.write(128);
        final Enumeration objects = this.getObjects();
        while (objects.hasMoreElements()) {
            asn1OutputStream.writeObject(objects.nextElement());
        }
        asn1OutputStream.write(0);
        asn1OutputStream.write(0);
    }
    
    @Override
    int encodedLength() throws IOException {
        final Enumeration objects = this.getObjects();
        int n = 0;
        while (objects.hasMoreElements()) {
            n += objects.nextElement().toASN1Primitive().encodedLength();
        }
        return n + 2 + 2;
    }
    
    public Enumeration getObjects() {
        if (this.octs == null) {
            return this.generateOcts().elements();
        }
        return new Enumeration() {
            int counter = 0;
            
            @Override
            public boolean hasMoreElements() {
                return this.counter < BEROctetString.this.octs.length;
            }
            
            @Override
            public Object nextElement() {
                return BEROctetString.this.octs[this.counter++];
            }
        };
    }
    
    @Override
    public byte[] getOctets() {
        return this.string;
    }
    
    @Override
    boolean isConstructed() {
        return true;
    }
}
