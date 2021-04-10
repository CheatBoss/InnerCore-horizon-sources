package org.spongycastle.crypto.util;

import java.io.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class DEROtherInfo
{
    private final DERSequence sequence;
    
    private DEROtherInfo(final DERSequence sequence) {
        this.sequence = sequence;
    }
    
    public byte[] getEncoded() throws IOException {
        return this.sequence.getEncoded();
    }
    
    public static final class Builder
    {
        private final AlgorithmIdentifier algorithmID;
        private final ASN1OctetString partyUVInfo;
        private final ASN1OctetString partyVInfo;
        private ASN1TaggedObject suppPrivInfo;
        private ASN1TaggedObject suppPubInfo;
        
        public Builder(final AlgorithmIdentifier algorithmID, final byte[] array, final byte[] array2) {
            this.algorithmID = algorithmID;
            this.partyUVInfo = DerUtil.getOctetString(array);
            this.partyVInfo = DerUtil.getOctetString(array2);
        }
        
        public DEROtherInfo build() {
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            asn1EncodableVector.add(this.algorithmID);
            asn1EncodableVector.add(this.partyUVInfo);
            asn1EncodableVector.add(this.partyVInfo);
            final ASN1TaggedObject suppPubInfo = this.suppPubInfo;
            if (suppPubInfo != null) {
                asn1EncodableVector.add(suppPubInfo);
            }
            final ASN1TaggedObject suppPrivInfo = this.suppPrivInfo;
            if (suppPrivInfo != null) {
                asn1EncodableVector.add(suppPrivInfo);
            }
            return new DEROtherInfo(new DERSequence(asn1EncodableVector), null);
        }
        
        public Builder withSuppPrivInfo(final byte[] array) {
            this.suppPrivInfo = new DERTaggedObject(false, 1, DerUtil.getOctetString(array));
            return this;
        }
        
        public Builder withSuppPubInfo(final byte[] array) {
            this.suppPubInfo = new DERTaggedObject(false, 0, DerUtil.getOctetString(array));
            return this;
        }
    }
}
