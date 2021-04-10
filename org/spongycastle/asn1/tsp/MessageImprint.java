package org.spongycastle.asn1.tsp;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class MessageImprint extends ASN1Object
{
    AlgorithmIdentifier hashAlgorithm;
    byte[] hashedMessage;
    
    private MessageImprint(final ASN1Sequence asn1Sequence) {
        this.hashAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.hashedMessage = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(1)).getOctets();
    }
    
    public MessageImprint(final AlgorithmIdentifier hashAlgorithm, final byte[] array) {
        this.hashAlgorithm = hashAlgorithm;
        this.hashedMessage = Arrays.clone(array);
    }
    
    public static MessageImprint getInstance(final Object o) {
        if (o instanceof MessageImprint) {
            return (MessageImprint)o;
        }
        if (o != null) {
            return new MessageImprint(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }
    
    public byte[] getHashedMessage() {
        return Arrays.clone(this.hashedMessage);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.hashAlgorithm);
        asn1EncodableVector.add(new DEROctetString(this.hashedMessage));
        return new DERSequence(asn1EncodableVector);
    }
}
