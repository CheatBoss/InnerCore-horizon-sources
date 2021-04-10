package org.spongycastle.asn1.cmc;

import java.util.*;
import org.spongycastle.asn1.*;

public class CMCFailInfo extends ASN1Object
{
    public static final CMCFailInfo authDataFail;
    public static final CMCFailInfo badAlg;
    public static final CMCFailInfo badCertId;
    public static final CMCFailInfo badIdentity;
    public static final CMCFailInfo badMessageCheck;
    public static final CMCFailInfo badRequest;
    public static final CMCFailInfo badTime;
    public static final CMCFailInfo internalCAError;
    public static final CMCFailInfo mustArchiveKeys;
    public static final CMCFailInfo noKeyReuse;
    public static final CMCFailInfo popFailed;
    public static final CMCFailInfo popRequired;
    private static Map range;
    public static final CMCFailInfo tryLater;
    public static final CMCFailInfo unsupportedExt;
    private final ASN1Integer value;
    
    static {
        badAlg = new CMCFailInfo(new ASN1Integer(0L));
        badMessageCheck = new CMCFailInfo(new ASN1Integer(1L));
        badRequest = new CMCFailInfo(new ASN1Integer(2L));
        badTime = new CMCFailInfo(new ASN1Integer(3L));
        badCertId = new CMCFailInfo(new ASN1Integer(4L));
        unsupportedExt = new CMCFailInfo(new ASN1Integer(5L));
        mustArchiveKeys = new CMCFailInfo(new ASN1Integer(6L));
        badIdentity = new CMCFailInfo(new ASN1Integer(7L));
        popRequired = new CMCFailInfo(new ASN1Integer(8L));
        popFailed = new CMCFailInfo(new ASN1Integer(9L));
        noKeyReuse = new CMCFailInfo(new ASN1Integer(10L));
        internalCAError = new CMCFailInfo(new ASN1Integer(11L));
        tryLater = new CMCFailInfo(new ASN1Integer(12L));
        authDataFail = new CMCFailInfo(new ASN1Integer(13L));
        final Map map = CMCFailInfo.range = new HashMap();
        final CMCFailInfo badAlg2 = CMCFailInfo.badAlg;
        map.put(badAlg2.value, badAlg2);
        final Map range = CMCFailInfo.range;
        final CMCFailInfo badMessageCheck2 = CMCFailInfo.badMessageCheck;
        range.put(badMessageCheck2.value, badMessageCheck2);
        final Map range2 = CMCFailInfo.range;
        final CMCFailInfo badRequest2 = CMCFailInfo.badRequest;
        range2.put(badRequest2.value, badRequest2);
        final Map range3 = CMCFailInfo.range;
        final CMCFailInfo badTime2 = CMCFailInfo.badTime;
        range3.put(badTime2.value, badTime2);
        final Map range4 = CMCFailInfo.range;
        final CMCFailInfo badCertId2 = CMCFailInfo.badCertId;
        range4.put(badCertId2.value, badCertId2);
        final Map range5 = CMCFailInfo.range;
        final CMCFailInfo popRequired2 = CMCFailInfo.popRequired;
        range5.put(popRequired2.value, popRequired2);
        final Map range6 = CMCFailInfo.range;
        final CMCFailInfo unsupportedExt2 = CMCFailInfo.unsupportedExt;
        range6.put(unsupportedExt2.value, unsupportedExt2);
        final Map range7 = CMCFailInfo.range;
        final CMCFailInfo mustArchiveKeys2 = CMCFailInfo.mustArchiveKeys;
        range7.put(mustArchiveKeys2.value, mustArchiveKeys2);
        final Map range8 = CMCFailInfo.range;
        final CMCFailInfo badIdentity2 = CMCFailInfo.badIdentity;
        range8.put(badIdentity2.value, badIdentity2);
        final Map range9 = CMCFailInfo.range;
        final CMCFailInfo popRequired3 = CMCFailInfo.popRequired;
        range9.put(popRequired3.value, popRequired3);
        final Map range10 = CMCFailInfo.range;
        final CMCFailInfo popFailed2 = CMCFailInfo.popFailed;
        range10.put(popFailed2.value, popFailed2);
        final Map range11 = CMCFailInfo.range;
        final CMCFailInfo badCertId3 = CMCFailInfo.badCertId;
        range11.put(badCertId3.value, badCertId3);
        final Map range12 = CMCFailInfo.range;
        final CMCFailInfo popRequired4 = CMCFailInfo.popRequired;
        range12.put(popRequired4.value, popRequired4);
        final Map range13 = CMCFailInfo.range;
        final CMCFailInfo noKeyReuse2 = CMCFailInfo.noKeyReuse;
        range13.put(noKeyReuse2.value, noKeyReuse2);
        final Map range14 = CMCFailInfo.range;
        final CMCFailInfo internalCAError2 = CMCFailInfo.internalCAError;
        range14.put(internalCAError2.value, internalCAError2);
        final Map range15 = CMCFailInfo.range;
        final CMCFailInfo tryLater2 = CMCFailInfo.tryLater;
        range15.put(tryLater2.value, tryLater2);
        final Map range16 = CMCFailInfo.range;
        final CMCFailInfo authDataFail2 = CMCFailInfo.authDataFail;
        range16.put(authDataFail2.value, authDataFail2);
    }
    
    private CMCFailInfo(final ASN1Integer value) {
        this.value = value;
    }
    
    public static CMCFailInfo getInstance(final Object o) {
        if (o instanceof CMCFailInfo) {
            return (CMCFailInfo)o;
        }
        if (o == null) {
            return null;
        }
        final CMCFailInfo cmcFailInfo = CMCFailInfo.range.get(ASN1Integer.getInstance(o));
        if (cmcFailInfo != null) {
            return cmcFailInfo;
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
