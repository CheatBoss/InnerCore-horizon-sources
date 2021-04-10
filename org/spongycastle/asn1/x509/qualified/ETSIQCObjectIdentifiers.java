package org.spongycastle.asn1.x509.qualified;

import org.spongycastle.asn1.*;

public interface ETSIQCObjectIdentifiers
{
    public static final ASN1ObjectIdentifier id_etsi_qcs_LimiteValue = new ASN1ObjectIdentifier("0.4.0.1862.1.2");
    public static final ASN1ObjectIdentifier id_etsi_qcs_QcCompliance = new ASN1ObjectIdentifier("0.4.0.1862.1.1");
    public static final ASN1ObjectIdentifier id_etsi_qcs_QcPds = new ASN1ObjectIdentifier("0.4.0.1862.1.5");
    public static final ASN1ObjectIdentifier id_etsi_qcs_QcSSCD = new ASN1ObjectIdentifier("0.4.0.1862.1.4");
    public static final ASN1ObjectIdentifier id_etsi_qcs_QcType;
    public static final ASN1ObjectIdentifier id_etsi_qcs_RetentionPeriod = new ASN1ObjectIdentifier("0.4.0.1862.1.3");
    public static final ASN1ObjectIdentifier id_etsi_qct_eseal = ETSIQCObjectIdentifiers.id_etsi_qcs_QcType.branch("2");
    public static final ASN1ObjectIdentifier id_etsi_qct_esign = (id_etsi_qcs_QcType = new ASN1ObjectIdentifier("0.4.0.1862.1.6")).branch("1");
    public static final ASN1ObjectIdentifier id_etsi_qct_web = ETSIQCObjectIdentifiers.id_etsi_qcs_QcType.branch("3");
}
