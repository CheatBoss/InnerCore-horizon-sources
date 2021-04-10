package org.spongycastle.asn1.crmf;

import org.spongycastle.asn1.*;
import org.spongycastle.asn1.pkcs.*;

public interface CRMFObjectIdentifiers
{
    public static final ASN1ObjectIdentifier id_ct_encKeyWithID = PKCSObjectIdentifiers.id_ct.branch("21");
    public static final ASN1ObjectIdentifier id_pkip;
    public static final ASN1ObjectIdentifier id_pkix;
    public static final ASN1ObjectIdentifier id_regCtrl;
    public static final ASN1ObjectIdentifier id_regCtrl_authenticator = CRMFObjectIdentifiers.id_regCtrl.branch("2");
    public static final ASN1ObjectIdentifier id_regCtrl_pkiArchiveOptions = CRMFObjectIdentifiers.id_regCtrl.branch("4");
    public static final ASN1ObjectIdentifier id_regCtrl_pkiPublicationInfo = CRMFObjectIdentifiers.id_regCtrl.branch("3");
    public static final ASN1ObjectIdentifier id_regCtrl_regToken = (id_regCtrl = (id_pkip = (id_pkix = new ASN1ObjectIdentifier("1.3.6.1.5.5.7")).branch("5")).branch("1")).branch("1");
}
