package org.spongycastle.asn1.x9;

import org.spongycastle.math.ec.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class X9ECPoint extends ASN1Object
{
    private ECCurve c;
    private final ASN1OctetString encoding;
    private ECPoint p;
    
    public X9ECPoint(final ECCurve ecCurve, final ASN1OctetString asn1OctetString) {
        this(ecCurve, asn1OctetString.getOctets());
    }
    
    public X9ECPoint(final ECCurve c, final byte[] array) {
        this.c = c;
        this.encoding = new DEROctetString(Arrays.clone(array));
    }
    
    public X9ECPoint(final ECPoint ecPoint) {
        this(ecPoint, false);
    }
    
    public X9ECPoint(final ECPoint ecPoint, final boolean b) {
        this.p = ecPoint.normalize();
        this.encoding = new DEROctetString(ecPoint.getEncoded(b));
    }
    
    public ECPoint getPoint() {
        synchronized (this) {
            if (this.p == null) {
                this.p = this.c.decodePoint(this.encoding.getOctets()).normalize();
            }
            return this.p;
        }
    }
    
    public byte[] getPointEncoding() {
        return Arrays.clone(this.encoding.getOctets());
    }
    
    public boolean isPointCompressed() {
        final byte[] octets = this.encoding.getOctets();
        boolean b2;
        final boolean b = b2 = false;
        if (octets != null) {
            b2 = b;
            if (octets.length > 0) {
                if (octets[0] != 2) {
                    b2 = b;
                    if (octets[0] != 3) {
                        return b2;
                    }
                }
                b2 = true;
            }
        }
        return b2;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.encoding;
    }
}
