package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.*;
import java.io.*;

public class BodyPartReference extends ASN1Object implements ASN1Choice
{
    private final BodyPartID bodyPartID;
    private final BodyPartPath bodyPartPath;
    
    public BodyPartReference(final BodyPartID bodyPartID) {
        this.bodyPartID = bodyPartID;
        this.bodyPartPath = null;
    }
    
    public BodyPartReference(final BodyPartPath bodyPartPath) {
        this.bodyPartID = null;
        this.bodyPartPath = bodyPartPath;
    }
    
    public static BodyPartReference getInstance(final Object o) {
        if (o instanceof BodyPartReference) {
            return (BodyPartReference)o;
        }
        if (o != null) {
            if (o instanceof ASN1Encodable) {
                final ASN1Primitive asn1Primitive = ((ASN1Encodable)o).toASN1Primitive();
                if (asn1Primitive instanceof ASN1Integer) {
                    return new BodyPartReference(BodyPartID.getInstance(asn1Primitive));
                }
                if (asn1Primitive instanceof ASN1Sequence) {
                    return new BodyPartReference(BodyPartPath.getInstance(asn1Primitive));
                }
            }
            if (o instanceof byte[]) {
                try {
                    return getInstance(ASN1Primitive.fromByteArray((byte[])o));
                }
                catch (IOException ex) {
                    throw new IllegalArgumentException("unknown encoding in getInstance()");
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("unknown object in getInstance(): ");
            sb.append(o.getClass().getName());
            throw new IllegalArgumentException(sb.toString());
        }
        return null;
    }
    
    public BodyPartID getBodyPartID() {
        return this.bodyPartID;
    }
    
    public BodyPartPath getBodyPartPath() {
        return this.bodyPartPath;
    }
    
    public boolean isBodyPartID() {
        return this.bodyPartID != null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final BodyPartID bodyPartID = this.bodyPartID;
        if (bodyPartID != null) {
            return bodyPartID.toASN1Primitive();
        }
        return this.bodyPartPath.toASN1Primitive();
    }
}
