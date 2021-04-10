package org.spongycastle.crypto.util;

import org.spongycastle.asn1.*;
import org.spongycastle.util.*;

public final class DERMacData
{
    private final byte[] macData;
    
    private DERMacData(final byte[] macData) {
        this.macData = macData;
    }
    
    public byte[] getMacData() {
        return Arrays.clone(this.macData);
    }
    
    public static final class Builder
    {
        private ASN1OctetString ephemDataU;
        private ASN1OctetString ephemDataV;
        private ASN1OctetString idU;
        private ASN1OctetString idV;
        private byte[] text;
        private final Type type;
        
        public Builder(final Type type, final byte[] array, final byte[] array2, final byte[] array3, final byte[] array4) {
            this.type = type;
            this.idU = DerUtil.getOctetString(array);
            this.idV = DerUtil.getOctetString(array2);
            this.ephemDataU = DerUtil.getOctetString(array3);
            this.ephemDataV = DerUtil.getOctetString(array4);
        }
        
        private byte[] concatenate(final byte[] array, final byte[] array2, final byte[] array3, final byte[] array4, final byte[] array5, final byte[] array6) {
            return Arrays.concatenate(Arrays.concatenate(array, array2, array3), Arrays.concatenate(array4, array5, array6));
        }
        
        public DERMacData build() {
            final int n = DERMacData$1.$SwitchMap$org$spongycastle$crypto$util$DERMacData$Type[this.type.ordinal()];
            if (n == 1 || n == 2) {
                return new DERMacData(this.concatenate(this.type.getHeader(), DerUtil.toByteArray(this.idU), DerUtil.toByteArray(this.idV), DerUtil.toByteArray(this.ephemDataU), DerUtil.toByteArray(this.ephemDataV), this.text), null);
            }
            if (n != 3 && n != 4) {
                throw new IllegalStateException("Unknown type encountered in build");
            }
            return new DERMacData(this.concatenate(this.type.getHeader(), DerUtil.toByteArray(this.idV), DerUtil.toByteArray(this.idU), DerUtil.toByteArray(this.ephemDataV), DerUtil.toByteArray(this.ephemDataU), this.text), null);
        }
        
        public Builder withText(final byte[] array) {
            this.text = DerUtil.toByteArray(new DERTaggedObject(false, 0, DerUtil.getOctetString(array)));
            return this;
        }
    }
    
    public enum Type
    {
        BILATERALU("KC_2_U"), 
        BILATERALV("KC_2_V"), 
        UNILATERALU("KC_1_U"), 
        UNILATERALV("KC_1_V");
        
        private final String enc;
        
        private Type(final String enc) {
            this.enc = enc;
        }
        
        public byte[] getHeader() {
            return Strings.toByteArray(this.enc);
        }
    }
}
