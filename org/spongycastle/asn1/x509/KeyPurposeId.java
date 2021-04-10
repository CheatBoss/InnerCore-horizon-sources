package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class KeyPurposeId extends ASN1Object
{
    public static final KeyPurposeId anyExtendedKeyUsage;
    private static final ASN1ObjectIdentifier id_kp;
    public static final KeyPurposeId id_kp_OCSPSigning;
    public static final KeyPurposeId id_kp_capwapAC;
    public static final KeyPurposeId id_kp_capwapWTP;
    public static final KeyPurposeId id_kp_clientAuth;
    public static final KeyPurposeId id_kp_codeSigning;
    public static final KeyPurposeId id_kp_dvcs;
    public static final KeyPurposeId id_kp_eapOverLAN;
    public static final KeyPurposeId id_kp_eapOverPPP;
    public static final KeyPurposeId id_kp_emailProtection;
    public static final KeyPurposeId id_kp_ipsecEndSystem;
    public static final KeyPurposeId id_kp_ipsecIKE;
    public static final KeyPurposeId id_kp_ipsecTunnel;
    public static final KeyPurposeId id_kp_ipsecUser;
    public static final KeyPurposeId id_kp_macAddress;
    public static final KeyPurposeId id_kp_msSGC;
    public static final KeyPurposeId id_kp_nsSGC;
    public static final KeyPurposeId id_kp_sbgpCertAAServerAuth;
    public static final KeyPurposeId id_kp_scvpClient;
    public static final KeyPurposeId id_kp_scvpServer;
    public static final KeyPurposeId id_kp_scvp_responder;
    public static final KeyPurposeId id_kp_serverAuth;
    public static final KeyPurposeId id_kp_smartcardlogon;
    public static final KeyPurposeId id_kp_timeStamping;
    private ASN1ObjectIdentifier id;
    
    static {
        id_kp = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.3");
        anyExtendedKeyUsage = new KeyPurposeId(Extension.extendedKeyUsage.branch("0"));
        id_kp_serverAuth = new KeyPurposeId(KeyPurposeId.id_kp.branch("1"));
        id_kp_clientAuth = new KeyPurposeId(KeyPurposeId.id_kp.branch("2"));
        id_kp_codeSigning = new KeyPurposeId(KeyPurposeId.id_kp.branch("3"));
        id_kp_emailProtection = new KeyPurposeId(KeyPurposeId.id_kp.branch("4"));
        id_kp_ipsecEndSystem = new KeyPurposeId(KeyPurposeId.id_kp.branch("5"));
        id_kp_ipsecTunnel = new KeyPurposeId(KeyPurposeId.id_kp.branch("6"));
        id_kp_ipsecUser = new KeyPurposeId(KeyPurposeId.id_kp.branch("7"));
        id_kp_timeStamping = new KeyPurposeId(KeyPurposeId.id_kp.branch("8"));
        id_kp_OCSPSigning = new KeyPurposeId(KeyPurposeId.id_kp.branch("9"));
        id_kp_dvcs = new KeyPurposeId(KeyPurposeId.id_kp.branch("10"));
        id_kp_sbgpCertAAServerAuth = new KeyPurposeId(KeyPurposeId.id_kp.branch("11"));
        id_kp_scvp_responder = new KeyPurposeId(KeyPurposeId.id_kp.branch("12"));
        id_kp_eapOverPPP = new KeyPurposeId(KeyPurposeId.id_kp.branch("13"));
        id_kp_eapOverLAN = new KeyPurposeId(KeyPurposeId.id_kp.branch("14"));
        id_kp_scvpServer = new KeyPurposeId(KeyPurposeId.id_kp.branch("15"));
        id_kp_scvpClient = new KeyPurposeId(KeyPurposeId.id_kp.branch("16"));
        id_kp_ipsecIKE = new KeyPurposeId(KeyPurposeId.id_kp.branch("17"));
        id_kp_capwapAC = new KeyPurposeId(KeyPurposeId.id_kp.branch("18"));
        id_kp_capwapWTP = new KeyPurposeId(KeyPurposeId.id_kp.branch("19"));
        id_kp_smartcardlogon = new KeyPurposeId(new ASN1ObjectIdentifier("1.3.6.1.4.1.311.20.2.2"));
        id_kp_macAddress = new KeyPurposeId(new ASN1ObjectIdentifier("1.3.6.1.1.1.1.22"));
        id_kp_msSGC = new KeyPurposeId(new ASN1ObjectIdentifier("1.3.6.1.4.1.311.10.3.3"));
        id_kp_nsSGC = new KeyPurposeId(new ASN1ObjectIdentifier("2.16.840.1.113730.4.1"));
    }
    
    public KeyPurposeId(final String s) {
        this(new ASN1ObjectIdentifier(s));
    }
    
    private KeyPurposeId(final ASN1ObjectIdentifier id) {
        this.id = id;
    }
    
    public static KeyPurposeId getInstance(final Object o) {
        if (o instanceof KeyPurposeId) {
            return (KeyPurposeId)o;
        }
        if (o != null) {
            return new KeyPurposeId(ASN1ObjectIdentifier.getInstance(o));
        }
        return null;
    }
    
    public String getId() {
        return this.id.getId();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.id;
    }
    
    public ASN1ObjectIdentifier toOID() {
        return this.id;
    }
    
    @Override
    public String toString() {
        return this.id.toString();
    }
}
