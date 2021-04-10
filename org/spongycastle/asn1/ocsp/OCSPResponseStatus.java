package org.spongycastle.asn1.ocsp;

import java.math.*;
import org.spongycastle.asn1.*;

public class OCSPResponseStatus extends ASN1Object
{
    public static final int INTERNAL_ERROR = 2;
    public static final int MALFORMED_REQUEST = 1;
    public static final int SIG_REQUIRED = 5;
    public static final int SUCCESSFUL = 0;
    public static final int TRY_LATER = 3;
    public static final int UNAUTHORIZED = 6;
    private ASN1Enumerated value;
    
    public OCSPResponseStatus(final int n) {
        this(new ASN1Enumerated(n));
    }
    
    private OCSPResponseStatus(final ASN1Enumerated value) {
        this.value = value;
    }
    
    public static OCSPResponseStatus getInstance(final Object o) {
        if (o instanceof OCSPResponseStatus) {
            return (OCSPResponseStatus)o;
        }
        if (o != null) {
            return new OCSPResponseStatus(ASN1Enumerated.getInstance(o));
        }
        return null;
    }
    
    public BigInteger getValue() {
        return this.value.getValue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.value;
    }
}
