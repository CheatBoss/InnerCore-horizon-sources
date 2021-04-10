package org.spongycastle.asn1.pkcs;

import org.spongycastle.asn1.x509.*;
import java.util.*;
import org.spongycastle.util.*;
import java.math.*;
import org.spongycastle.asn1.*;

public class PBKDF2Params extends ASN1Object
{
    private static final AlgorithmIdentifier algid_hmacWithSHA1;
    private final ASN1Integer iterationCount;
    private final ASN1Integer keyLength;
    private final ASN1OctetString octStr;
    private final AlgorithmIdentifier prf;
    
    static {
        algid_hmacWithSHA1 = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_hmacWithSHA1, DERNull.INSTANCE);
    }
    
    private PBKDF2Params(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.octStr = objects.nextElement();
        this.iterationCount = (ASN1Integer)objects.nextElement();
        if (!objects.hasMoreElements()) {
            this.keyLength = null;
            this.prf = null;
            return;
        }
        Object o = objects.nextElement();
        if (o instanceof ASN1Integer) {
            this.keyLength = ASN1Integer.getInstance(o);
            if (objects.hasMoreElements()) {
                o = objects.nextElement();
            }
            else {
                o = null;
            }
        }
        else {
            this.keyLength = null;
        }
        if (o != null) {
            this.prf = AlgorithmIdentifier.getInstance(o);
            return;
        }
        this.prf = null;
    }
    
    public PBKDF2Params(final byte[] array, final int n) {
        this(array, n, 0);
    }
    
    public PBKDF2Params(final byte[] array, final int n, final int n2) {
        this(array, n, n2, null);
    }
    
    public PBKDF2Params(final byte[] array, final int n, final int n2, final AlgorithmIdentifier prf) {
        this.octStr = new DEROctetString(Arrays.clone(array));
        this.iterationCount = new ASN1Integer(n);
        ASN1Integer keyLength;
        if (n2 > 0) {
            keyLength = new ASN1Integer(n2);
        }
        else {
            keyLength = null;
        }
        this.keyLength = keyLength;
        this.prf = prf;
    }
    
    public PBKDF2Params(final byte[] array, final int n, final AlgorithmIdentifier algorithmIdentifier) {
        this(array, n, 0, algorithmIdentifier);
    }
    
    public static PBKDF2Params getInstance(final Object o) {
        if (o instanceof PBKDF2Params) {
            return (PBKDF2Params)o;
        }
        if (o != null) {
            return new PBKDF2Params(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public BigInteger getIterationCount() {
        return this.iterationCount.getValue();
    }
    
    public BigInteger getKeyLength() {
        final ASN1Integer keyLength = this.keyLength;
        if (keyLength != null) {
            return keyLength.getValue();
        }
        return null;
    }
    
    public AlgorithmIdentifier getPrf() {
        final AlgorithmIdentifier prf = this.prf;
        if (prf != null) {
            return prf;
        }
        return PBKDF2Params.algid_hmacWithSHA1;
    }
    
    public byte[] getSalt() {
        return this.octStr.getOctets();
    }
    
    public boolean isDefaultPrf() {
        final AlgorithmIdentifier prf = this.prf;
        return prf == null || prf.equals(PBKDF2Params.algid_hmacWithSHA1);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.octStr);
        asn1EncodableVector.add(this.iterationCount);
        final ASN1Integer keyLength = this.keyLength;
        if (keyLength != null) {
            asn1EncodableVector.add(keyLength);
        }
        final AlgorithmIdentifier prf = this.prf;
        if (prf != null && !prf.equals(PBKDF2Params.algid_hmacWithSHA1)) {
            asn1EncodableVector.add(this.prf);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
