package org.spongycastle.asn1;

import java.util.*;

public class DERUTCTime extends ASN1UTCTime
{
    public DERUTCTime(final String s) {
        super(s);
    }
    
    public DERUTCTime(final Date date) {
        super(date);
    }
    
    DERUTCTime(final byte[] array) {
        super(array);
    }
}
