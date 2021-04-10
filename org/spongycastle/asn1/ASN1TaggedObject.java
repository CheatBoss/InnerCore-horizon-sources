package org.spongycastle.asn1;

import java.io.*;

public abstract class ASN1TaggedObject extends ASN1Primitive implements ASN1TaggedObjectParser
{
    boolean empty;
    boolean explicit;
    ASN1Encodable obj;
    int tagNo;
    
    public ASN1TaggedObject(final boolean explicit, final int tagNo, final ASN1Encodable asn1Encodable) {
        this.empty = false;
        this.explicit = true;
        this.obj = null;
        if (asn1Encodable instanceof ASN1Choice) {
            this.explicit = true;
        }
        else {
            this.explicit = explicit;
        }
        this.tagNo = tagNo;
        if (this.explicit) {
            this.obj = asn1Encodable;
            return;
        }
        final boolean b = asn1Encodable.toASN1Primitive() instanceof ASN1Set;
        this.obj = asn1Encodable;
    }
    
    public static ASN1TaggedObject getInstance(final Object o) {
        if (o != null && !(o instanceof ASN1TaggedObject)) {
            if (o instanceof byte[]) {
                try {
                    return getInstance(ASN1Primitive.fromByteArray((byte[])o));
                }
                catch (IOException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("failed to construct tagged object from byte[]: ");
                    sb.append(ex.getMessage());
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("unknown object in getInstance: ");
            sb2.append(o.getClass().getName());
            throw new IllegalArgumentException(sb2.toString());
        }
        return (ASN1TaggedObject)o;
    }
    
    public static ASN1TaggedObject getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        if (b) {
            return (ASN1TaggedObject)asn1TaggedObject.getObject();
        }
        throw new IllegalArgumentException("implicitly tagged tagged object");
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        if (!(asn1Primitive instanceof ASN1TaggedObject)) {
            return false;
        }
        final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Primitive;
        if (this.tagNo != asn1TaggedObject.tagNo || this.empty != asn1TaggedObject.empty) {
            return false;
        }
        if (this.explicit != asn1TaggedObject.explicit) {
            return false;
        }
        final ASN1Encodable obj = this.obj;
        if (obj == null) {
            if (asn1TaggedObject.obj != null) {
                return false;
            }
        }
        else if (!obj.toASN1Primitive().equals(asn1TaggedObject.obj.toASN1Primitive())) {
            return false;
        }
        return true;
    }
    
    @Override
    abstract void encode(final ASN1OutputStream p0) throws IOException;
    
    @Override
    public ASN1Primitive getLoadedObject() {
        return this.toASN1Primitive();
    }
    
    public ASN1Primitive getObject() {
        final ASN1Encodable obj = this.obj;
        if (obj != null) {
            return obj.toASN1Primitive();
        }
        return null;
    }
    
    @Override
    public ASN1Encodable getObjectParser(final int n, final boolean b) throws IOException {
        if (n == 4) {
            return ASN1OctetString.getInstance(this, b).parser();
        }
        if (n == 16) {
            return ASN1Sequence.getInstance(this, b).parser();
        }
        if (n == 17) {
            return ASN1Set.getInstance(this, b).parser();
        }
        if (b) {
            return this.getObject();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("implicit tagging not implemented for tag: ");
        sb.append(n);
        throw new ASN1Exception(sb.toString());
    }
    
    @Override
    public int getTagNo() {
        return this.tagNo;
    }
    
    @Override
    public int hashCode() {
        final int tagNo = this.tagNo;
        final ASN1Encodable obj = this.obj;
        int n = tagNo;
        if (obj != null) {
            n = (tagNo ^ obj.hashCode());
        }
        return n;
    }
    
    public boolean isEmpty() {
        return this.empty;
    }
    
    public boolean isExplicit() {
        return this.explicit;
    }
    
    @Override
    ASN1Primitive toDERObject() {
        return new DERTaggedObject(this.explicit, this.tagNo, this.obj);
    }
    
    @Override
    ASN1Primitive toDLObject() {
        return new DLTaggedObject(this.explicit, this.tagNo, this.obj);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(this.tagNo);
        sb.append("]");
        sb.append(this.obj);
        return sb.toString();
    }
}
