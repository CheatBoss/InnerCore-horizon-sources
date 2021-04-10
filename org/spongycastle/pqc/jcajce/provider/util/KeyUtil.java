package org.spongycastle.pqc.jcajce.provider.util;

import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;

public class KeyUtil
{
    public static byte[] getEncodedPrivateKeyInfo(final PrivateKeyInfo privateKeyInfo) {
        try {
            return privateKeyInfo.getEncoded("DER");
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static byte[] getEncodedPrivateKeyInfo(final AlgorithmIdentifier algorithmIdentifier, final ASN1Encodable asn1Encodable) {
        try {
            return getEncodedPrivateKeyInfo(new PrivateKeyInfo(algorithmIdentifier, asn1Encodable.toASN1Primitive()));
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static byte[] getEncodedSubjectPublicKeyInfo(final AlgorithmIdentifier algorithmIdentifier, final ASN1Encodable asn1Encodable) {
        try {
            return getEncodedSubjectPublicKeyInfo(new SubjectPublicKeyInfo(algorithmIdentifier, asn1Encodable));
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static byte[] getEncodedSubjectPublicKeyInfo(final AlgorithmIdentifier algorithmIdentifier, final byte[] array) {
        try {
            return getEncodedSubjectPublicKeyInfo(new SubjectPublicKeyInfo(algorithmIdentifier, array));
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static byte[] getEncodedSubjectPublicKeyInfo(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        try {
            return subjectPublicKeyInfo.getEncoded("DER");
        }
        catch (Exception ex) {
            return null;
        }
    }
}
