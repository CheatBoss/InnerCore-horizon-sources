package org.spongycastle.asn1.icao;

import org.spongycastle.asn1.*;

public interface ICAOObjectIdentifiers
{
    public static final ASN1ObjectIdentifier id_icao;
    public static final ASN1ObjectIdentifier id_icao_aaProtocolObject = ICAOObjectIdentifiers.id_icao_mrtd_security.branch("5");
    public static final ASN1ObjectIdentifier id_icao_cscaMasterList = ICAOObjectIdentifiers.id_icao_mrtd_security.branch("2");
    public static final ASN1ObjectIdentifier id_icao_cscaMasterListSigningKey = ICAOObjectIdentifiers.id_icao_mrtd_security.branch("3");
    public static final ASN1ObjectIdentifier id_icao_documentTypeList = ICAOObjectIdentifiers.id_icao_mrtd_security.branch("4");
    public static final ASN1ObjectIdentifier id_icao_extensions;
    public static final ASN1ObjectIdentifier id_icao_extensions_namechangekeyrollover = (id_icao_extensions = ICAOObjectIdentifiers.id_icao_mrtd_security.branch("6")).branch("1");
    public static final ASN1ObjectIdentifier id_icao_ldsSecurityObject = (id_icao_mrtd_security = (id_icao_mrtd = (id_icao = new ASN1ObjectIdentifier("2.23.136")).branch("1")).branch("1")).branch("1");
    public static final ASN1ObjectIdentifier id_icao_mrtd;
    public static final ASN1ObjectIdentifier id_icao_mrtd_security;
}
