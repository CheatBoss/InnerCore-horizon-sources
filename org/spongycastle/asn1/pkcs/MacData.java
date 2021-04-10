package org.spongycastle.asn1.pkcs;

import java.math.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class MacData extends ASN1Object
{
    private static final BigInteger ONE;
    DigestInfo digInfo;
    BigInteger iterationCount;
    byte[] salt;
    
    static {
        ONE = BigInteger.valueOf(1L);
    }
    
    private MacData(final ASN1Sequence asn1Sequence) {
        this.digInfo = DigestInfo.getInstance(asn1Sequence.getObjectAt(0));
        this.salt = Arrays.clone(((ASN1OctetString)asn1Sequence.getObjectAt(1)).getOctets());
        BigInteger iterationCount;
        if (asn1Sequence.size() == 3) {
            iterationCount = ((ASN1Integer)asn1Sequence.getObjectAt(2)).getValue();
        }
        else {
            iterationCount = MacData.ONE;
        }
        this.iterationCount = iterationCount;
    }
    
    public MacData(final DigestInfo digInfo, final byte[] array, final int n) {
        this.digInfo = digInfo;
        this.salt = Arrays.clone(array);
        this.iterationCount = BigInteger.valueOf(n);
    }
    
    public static MacData getInstance(final Object o) {
        if (o instanceof MacData) {
            return (MacData)o;
        }
        if (o != null) {
            return new MacData(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public BigInteger getIterationCount() {
        return this.iterationCount;
    }
    
    public DigestInfo getMac() {
        return this.digInfo;
    }
    
    public byte[] getSalt() {
        return Arrays.clone(this.salt);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.digInfo);
        asn1EncodableVector.add(new DEROctetString(this.salt));
        if (!this.iterationCount.equals(MacData.ONE)) {
            asn1EncodableVector.add(new ASN1Integer(this.iterationCount));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
