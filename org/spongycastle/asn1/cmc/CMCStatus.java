package org.spongycastle.asn1.cmc;

import java.util.*;
import org.spongycastle.asn1.*;

public class CMCStatus extends ASN1Object
{
    public static final CMCStatus confirmRequired;
    public static final CMCStatus failed;
    public static final CMCStatus noSupport;
    public static final CMCStatus partial;
    public static final CMCStatus pending;
    public static final CMCStatus popRequired;
    private static Map range;
    public static final CMCStatus success;
    private final ASN1Integer value;
    
    static {
        success = new CMCStatus(new ASN1Integer(0L));
        failed = new CMCStatus(new ASN1Integer(2L));
        pending = new CMCStatus(new ASN1Integer(3L));
        noSupport = new CMCStatus(new ASN1Integer(4L));
        confirmRequired = new CMCStatus(new ASN1Integer(5L));
        popRequired = new CMCStatus(new ASN1Integer(6L));
        partial = new CMCStatus(new ASN1Integer(7L));
        final Map map = CMCStatus.range = new HashMap();
        final CMCStatus success2 = CMCStatus.success;
        map.put(success2.value, success2);
        final Map range = CMCStatus.range;
        final CMCStatus failed2 = CMCStatus.failed;
        range.put(failed2.value, failed2);
        final Map range2 = CMCStatus.range;
        final CMCStatus pending2 = CMCStatus.pending;
        range2.put(pending2.value, pending2);
        final Map range3 = CMCStatus.range;
        final CMCStatus noSupport2 = CMCStatus.noSupport;
        range3.put(noSupport2.value, noSupport2);
        final Map range4 = CMCStatus.range;
        final CMCStatus confirmRequired2 = CMCStatus.confirmRequired;
        range4.put(confirmRequired2.value, confirmRequired2);
        final Map range5 = CMCStatus.range;
        final CMCStatus popRequired2 = CMCStatus.popRequired;
        range5.put(popRequired2.value, popRequired2);
        final Map range6 = CMCStatus.range;
        final CMCStatus partial2 = CMCStatus.partial;
        range6.put(partial2.value, partial2);
    }
    
    private CMCStatus(final ASN1Integer value) {
        this.value = value;
    }
    
    public static CMCStatus getInstance(final Object o) {
        if (o instanceof CMCStatus) {
            return (CMCStatus)o;
        }
        if (o == null) {
            return null;
        }
        final CMCStatus cmcStatus = CMCStatus.range.get(ASN1Integer.getInstance(o));
        if (cmcStatus != null) {
            return cmcStatus;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown object in getInstance(): ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.value;
    }
}
