package org.spongycastle.asn1.bc;

import org.spongycastle.asn1.*;
import java.io.*;

public class ObjectStoreIntegrityCheck extends ASN1Object implements ASN1Choice
{
    public static final int PBKD_MAC_CHECK = 0;
    private final ASN1Object integrityCheck;
    private final int type;
    
    private ObjectStoreIntegrityCheck(final ASN1Encodable asn1Encodable) {
        if (!(asn1Encodable instanceof ASN1Sequence) && !(asn1Encodable instanceof PbkdMacIntegrityCheck)) {
            throw new IllegalArgumentException("Unknown check object in integrity check.");
        }
        this.type = 0;
        this.integrityCheck = PbkdMacIntegrityCheck.getInstance(asn1Encodable);
    }
    
    public ObjectStoreIntegrityCheck(final PbkdMacIntegrityCheck pbkdMacIntegrityCheck) {
        this((ASN1Encodable)pbkdMacIntegrityCheck);
    }
    
    public static ObjectStoreIntegrityCheck getInstance(final Object o) {
        if (o instanceof ObjectStoreIntegrityCheck) {
            return (ObjectStoreIntegrityCheck)o;
        }
        if (o instanceof byte[]) {
            try {
                return new ObjectStoreIntegrityCheck(ASN1Primitive.fromByteArray((byte[])o));
            }
            catch (IOException ex) {
                throw new IllegalArgumentException("Unable to parse integrity check details.");
            }
        }
        if (o != null) {
            return new ObjectStoreIntegrityCheck((ASN1Encodable)o);
        }
        return null;
    }
    
    public ASN1Object getIntegrityCheck() {
        return this.integrityCheck;
    }
    
    public int getType() {
        return this.type;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.integrityCheck.toASN1Primitive();
    }
}
