package org.spongycastle.asn1.crmf;

import org.spongycastle.asn1.*;

public class SubsequentMessage extends ASN1Integer
{
    public static final SubsequentMessage challengeResp;
    public static final SubsequentMessage encrCert;
    
    static {
        encrCert = new SubsequentMessage(0);
        challengeResp = new SubsequentMessage(1);
    }
    
    private SubsequentMessage(final int n) {
        super(n);
    }
    
    public static SubsequentMessage valueOf(final int n) {
        if (n == 0) {
            return SubsequentMessage.encrCert;
        }
        if (n == 1) {
            return SubsequentMessage.challengeResp;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown value: ");
        sb.append(n);
        throw new IllegalArgumentException(sb.toString());
    }
}
