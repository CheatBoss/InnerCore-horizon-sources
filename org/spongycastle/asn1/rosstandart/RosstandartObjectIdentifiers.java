package org.spongycastle.asn1.rosstandart;

import org.spongycastle.asn1.*;

public interface RosstandartObjectIdentifiers
{
    public static final ASN1ObjectIdentifier id_tc26;
    public static final ASN1ObjectIdentifier id_tc26_agreement;
    public static final ASN1ObjectIdentifier id_tc26_agreement_gost_3410_12_256 = (id_tc26_agreement = RosstandartObjectIdentifiers.id_tc26.branch("1.6")).branch("1");
    public static final ASN1ObjectIdentifier id_tc26_agreement_gost_3410_12_512 = RosstandartObjectIdentifiers.id_tc26_agreement.branch("2");
    public static final ASN1ObjectIdentifier id_tc26_gost_28147_param_Z = RosstandartObjectIdentifiers.id_tc26.branch("2.5.1.1");
    public static final ASN1ObjectIdentifier id_tc26_gost_3410_12_256 = RosstandartObjectIdentifiers.id_tc26.branch("1.1.1");
    public static final ASN1ObjectIdentifier id_tc26_gost_3410_12_256_paramSetA = RosstandartObjectIdentifiers.id_tc26.branch("2.1.1.1");
    public static final ASN1ObjectIdentifier id_tc26_gost_3410_12_512 = RosstandartObjectIdentifiers.id_tc26.branch("1.1.2");
    public static final ASN1ObjectIdentifier id_tc26_gost_3410_12_512_paramSetA = RosstandartObjectIdentifiers.id_tc26.branch("2.1.2.1");
    public static final ASN1ObjectIdentifier id_tc26_gost_3410_12_512_paramSetB = RosstandartObjectIdentifiers.id_tc26.branch("2.1.2.2");
    public static final ASN1ObjectIdentifier id_tc26_gost_3410_12_512_paramSetC = RosstandartObjectIdentifiers.id_tc26.branch("2.1.2.3");
    public static final ASN1ObjectIdentifier id_tc26_gost_3411_12_256 = (id_tc26 = (rosstandart = new ASN1ObjectIdentifier("1.2.643.7")).branch("1")).branch("1.2.2");
    public static final ASN1ObjectIdentifier id_tc26_gost_3411_12_512 = RosstandartObjectIdentifiers.id_tc26.branch("1.2.3");
    public static final ASN1ObjectIdentifier id_tc26_hmac_gost_3411_12_256 = RosstandartObjectIdentifiers.id_tc26.branch("1.4.1");
    public static final ASN1ObjectIdentifier id_tc26_hmac_gost_3411_12_512 = RosstandartObjectIdentifiers.id_tc26.branch("1.4.2");
    public static final ASN1ObjectIdentifier id_tc26_signwithdigest_gost_3410_12_256 = RosstandartObjectIdentifiers.id_tc26.branch("1.3.2");
    public static final ASN1ObjectIdentifier id_tc26_signwithdigest_gost_3410_12_512 = RosstandartObjectIdentifiers.id_tc26.branch("1.3.3");
    public static final ASN1ObjectIdentifier rosstandart;
}
