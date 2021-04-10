package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.*;

public interface CMCObjectIdentifiers
{
    public static final ASN1ObjectIdentifier id_cct = CMCObjectIdentifiers.id_pkix.branch("12");
    public static final ASN1ObjectIdentifier id_cct_PKIData = CMCObjectIdentifiers.id_cct.branch("2");
    public static final ASN1ObjectIdentifier id_cct_PKIResponse = CMCObjectIdentifiers.id_cct.branch("3");
    public static final ASN1ObjectIdentifier id_cmc = (id_pkix = new ASN1ObjectIdentifier("1.3.6.1.5.5.7")).branch("7");
    public static final ASN1ObjectIdentifier id_cmc_addExtensions = CMCObjectIdentifiers.id_cmc.branch("8");
    public static final ASN1ObjectIdentifier id_cmc_authData = CMCObjectIdentifiers.id_cmc.branch("27");
    public static final ASN1ObjectIdentifier id_cmc_batchRequests = CMCObjectIdentifiers.id_cmc.branch("28");
    public static final ASN1ObjectIdentifier id_cmc_batchResponses = CMCObjectIdentifiers.id_cmc.branch("29");
    public static final ASN1ObjectIdentifier id_cmc_confirmCertAcceptance = CMCObjectIdentifiers.id_cmc.branch("24");
    public static final ASN1ObjectIdentifier id_cmc_controlProcessed = CMCObjectIdentifiers.id_cmc.branch("32");
    public static final ASN1ObjectIdentifier id_cmc_dataReturn = CMCObjectIdentifiers.id_cmc.branch("4");
    public static final ASN1ObjectIdentifier id_cmc_decryptedPOP = CMCObjectIdentifiers.id_cmc.branch("10");
    public static final ASN1ObjectIdentifier id_cmc_encryptedPOP = CMCObjectIdentifiers.id_cmc.branch("9");
    public static final ASN1ObjectIdentifier id_cmc_getCRL = CMCObjectIdentifiers.id_cmc.branch("16");
    public static final ASN1ObjectIdentifier id_cmc_getCert = CMCObjectIdentifiers.id_cmc.branch("15");
    public static final ASN1ObjectIdentifier id_cmc_identification = CMCObjectIdentifiers.id_cmc.branch("2");
    public static final ASN1ObjectIdentifier id_cmc_identityProof = CMCObjectIdentifiers.id_cmc.branch("3");
    public static final ASN1ObjectIdentifier id_cmc_identityProofV2 = CMCObjectIdentifiers.id_cmc.branch("34");
    public static final ASN1ObjectIdentifier id_cmc_lraPOPWitness = CMCObjectIdentifiers.id_cmc.branch("11");
    public static final ASN1ObjectIdentifier id_cmc_modCertTemplate = CMCObjectIdentifiers.id_cmc.branch("31");
    public static final ASN1ObjectIdentifier id_cmc_popLinkRandom = CMCObjectIdentifiers.id_cmc.branch("22");
    public static final ASN1ObjectIdentifier id_cmc_popLinkWitness = CMCObjectIdentifiers.id_cmc.branch("23");
    public static final ASN1ObjectIdentifier id_cmc_popLinkWitnessV2 = CMCObjectIdentifiers.id_cmc.branch("33");
    public static final ASN1ObjectIdentifier id_cmc_publishCert = CMCObjectIdentifiers.id_cmc.branch("30");
    public static final ASN1ObjectIdentifier id_cmc_queryPending = CMCObjectIdentifiers.id_cmc.branch("21");
    public static final ASN1ObjectIdentifier id_cmc_recipientNonce = CMCObjectIdentifiers.id_cmc.branch("7");
    public static final ASN1ObjectIdentifier id_cmc_regInfo = CMCObjectIdentifiers.id_cmc.branch("18");
    public static final ASN1ObjectIdentifier id_cmc_responseInfo = CMCObjectIdentifiers.id_cmc.branch("19");
    public static final ASN1ObjectIdentifier id_cmc_revokeRequest = CMCObjectIdentifiers.id_cmc.branch("17");
    public static final ASN1ObjectIdentifier id_cmc_senderNonce = CMCObjectIdentifiers.id_cmc.branch("6");
    public static final ASN1ObjectIdentifier id_cmc_statusInfo = CMCObjectIdentifiers.id_cmc.branch("1");
    public static final ASN1ObjectIdentifier id_cmc_statusInfoV2 = CMCObjectIdentifiers.id_cmc.branch("25");
    public static final ASN1ObjectIdentifier id_cmc_transactionId = CMCObjectIdentifiers.id_cmc.branch("5");
    public static final ASN1ObjectIdentifier id_cmc_trustedAnchors = CMCObjectIdentifiers.id_cmc.branch("26");
    public static final ASN1ObjectIdentifier id_pkix;
}
