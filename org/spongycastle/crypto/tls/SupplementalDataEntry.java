package org.spongycastle.crypto.tls;

public class SupplementalDataEntry
{
    protected byte[] data;
    protected int dataType;
    
    public SupplementalDataEntry(final int dataType, final byte[] data) {
        this.dataType = dataType;
        this.data = data;
    }
    
    public byte[] getData() {
        return this.data;
    }
    
    public int getDataType() {
        return this.dataType;
    }
}
