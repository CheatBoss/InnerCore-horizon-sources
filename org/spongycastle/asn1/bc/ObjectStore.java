package org.spongycastle.asn1.bc;

import org.spongycastle.asn1.*;

public class ObjectStore extends ASN1Object
{
    private final ObjectStoreIntegrityCheck integrityCheck;
    private final ASN1Encodable storeData;
    
    private ObjectStore(final ASN1Sequence asn1Sequence) {
        ASN1Encodable storeData = asn1Sequence.getObjectAt(0);
        if (!(storeData instanceof EncryptedObjectStoreData)) {
            if (!(storeData instanceof ObjectStoreData)) {
                final ASN1Sequence instance = ASN1Sequence.getInstance(storeData);
                if (instance.size() == 2) {
                    storeData = EncryptedObjectStoreData.getInstance(instance);
                }
                else {
                    storeData = ObjectStoreData.getInstance(instance);
                }
            }
        }
        this.storeData = storeData;
        this.integrityCheck = ObjectStoreIntegrityCheck.getInstance(asn1Sequence.getObjectAt(1));
    }
    
    public ObjectStore(final EncryptedObjectStoreData storeData, final ObjectStoreIntegrityCheck integrityCheck) {
        this.storeData = storeData;
        this.integrityCheck = integrityCheck;
    }
    
    public ObjectStore(final ObjectStoreData storeData, final ObjectStoreIntegrityCheck integrityCheck) {
        this.storeData = storeData;
        this.integrityCheck = integrityCheck;
    }
    
    public static ObjectStore getInstance(final Object o) {
        if (o instanceof ObjectStore) {
            return (ObjectStore)o;
        }
        if (o != null) {
            return new ObjectStore(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ObjectStoreIntegrityCheck getIntegrityCheck() {
        return this.integrityCheck;
    }
    
    public ASN1Encodable getStoreData() {
        return this.storeData;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.storeData);
        asn1EncodableVector.add(this.integrityCheck);
        return new DERSequence(asn1EncodableVector);
    }
}
