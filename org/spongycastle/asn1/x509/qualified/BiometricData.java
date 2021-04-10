package org.spongycastle.asn1.x509.qualified;

import org.spongycastle.asn1.x509.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class BiometricData extends ASN1Object
{
    private ASN1OctetString biometricDataHash;
    private AlgorithmIdentifier hashAlgorithm;
    private DERIA5String sourceDataUri;
    private TypeOfBiometricData typeOfBiometricData;
    
    private BiometricData(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.typeOfBiometricData = TypeOfBiometricData.getInstance(objects.nextElement());
        this.hashAlgorithm = AlgorithmIdentifier.getInstance(objects.nextElement());
        this.biometricDataHash = ASN1OctetString.getInstance(objects.nextElement());
        if (objects.hasMoreElements()) {
            this.sourceDataUri = DERIA5String.getInstance(objects.nextElement());
        }
    }
    
    public BiometricData(final TypeOfBiometricData typeOfBiometricData, final AlgorithmIdentifier hashAlgorithm, final ASN1OctetString biometricDataHash) {
        this.typeOfBiometricData = typeOfBiometricData;
        this.hashAlgorithm = hashAlgorithm;
        this.biometricDataHash = biometricDataHash;
        this.sourceDataUri = null;
    }
    
    public BiometricData(final TypeOfBiometricData typeOfBiometricData, final AlgorithmIdentifier hashAlgorithm, final ASN1OctetString biometricDataHash, final DERIA5String sourceDataUri) {
        this.typeOfBiometricData = typeOfBiometricData;
        this.hashAlgorithm = hashAlgorithm;
        this.biometricDataHash = biometricDataHash;
        this.sourceDataUri = sourceDataUri;
    }
    
    public static BiometricData getInstance(final Object o) {
        if (o instanceof BiometricData) {
            return (BiometricData)o;
        }
        if (o != null) {
            return new BiometricData(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1OctetString getBiometricDataHash() {
        return this.biometricDataHash;
    }
    
    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }
    
    public DERIA5String getSourceDataUri() {
        return this.sourceDataUri;
    }
    
    public TypeOfBiometricData getTypeOfBiometricData() {
        return this.typeOfBiometricData;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.typeOfBiometricData);
        asn1EncodableVector.add(this.hashAlgorithm);
        asn1EncodableVector.add(this.biometricDataHash);
        final DERIA5String sourceDataUri = this.sourceDataUri;
        if (sourceDataUri != null) {
            asn1EncodableVector.add(sourceDataUri);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
