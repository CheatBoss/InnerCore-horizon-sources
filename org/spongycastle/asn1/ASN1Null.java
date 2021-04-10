package org.spongycastle.asn1;

import java.io.*;

public abstract class ASN1Null extends ASN1Primitive
{
    public static ASN1Null getInstance(final Object o) {
        if (o instanceof ASN1Null) {
            return (ASN1Null)o;
        }
        if (o != null) {
            Label_0076: {
                try {
                    return getInstance(ASN1Primitive.fromByteArray((byte[])o));
                }
                catch (ClassCastException ex2) {}
                catch (IOException ex) {
                    break Label_0076;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("unknown object in getInstance(): ");
                sb.append(o.getClass().getName());
                throw new IllegalArgumentException(sb.toString());
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("failed to construct NULL from byte[]: ");
            final IOException ex;
            sb2.append(ex.getMessage());
            throw new IllegalArgumentException(sb2.toString());
        }
        return null;
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        return asn1Primitive instanceof ASN1Null;
    }
    
    @Override
    abstract void encode(final ASN1OutputStream p0) throws IOException;
    
    @Override
    public int hashCode() {
        return -1;
    }
    
    @Override
    public String toString() {
        return "NULL";
    }
}
