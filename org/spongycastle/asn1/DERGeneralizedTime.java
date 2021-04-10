package org.spongycastle.asn1;

import java.util.*;

public class DERGeneralizedTime extends ASN1GeneralizedTime
{
    public DERGeneralizedTime(final String s) {
        super(s);
    }
    
    public DERGeneralizedTime(final Date date) {
        super(date);
    }
    
    DERGeneralizedTime(final byte[] array) {
        super(array);
    }
}
