package org.spongycastle.asn1.cmc;

import java.math.*;
import org.spongycastle.asn1.*;

public class BodyPartID extends ASN1Object
{
    public static final long bodyIdMax = 4294967295L;
    private final long id;
    
    public BodyPartID(final long id) {
        if (id >= 0L && id <= 4294967295L) {
            this.id = id;
            return;
        }
        throw new IllegalArgumentException("id out of range");
    }
    
    private BodyPartID(final ASN1Integer asn1Integer) {
        this(convert(asn1Integer.getValue()));
    }
    
    private static long convert(final BigInteger bigInteger) {
        if (bigInteger.bitLength() <= 32) {
            return bigInteger.longValue();
        }
        throw new IllegalArgumentException("id out of range");
    }
    
    public static BodyPartID getInstance(final Object o) {
        if (o instanceof BodyPartID) {
            return (BodyPartID)o;
        }
        if (o != null) {
            return new BodyPartID(ASN1Integer.getInstance(o));
        }
        return null;
    }
    
    public long getID() {
        return this.id;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new ASN1Integer(this.id);
    }
}
