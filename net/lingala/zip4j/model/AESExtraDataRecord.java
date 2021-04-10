package net.lingala.zip4j.model;

public class AESExtraDataRecord
{
    private int aesStrength;
    private int compressionMethod;
    private int dataSize;
    private long signature;
    private String vendorID;
    private int versionNumber;
    
    public AESExtraDataRecord() {
        this.signature = -1L;
        this.dataSize = -1;
        this.versionNumber = -1;
        this.vendorID = null;
        this.aesStrength = -1;
        this.compressionMethod = -1;
    }
    
    public int getAesStrength() {
        return this.aesStrength;
    }
    
    public int getCompressionMethod() {
        return this.compressionMethod;
    }
    
    public int getDataSize() {
        return this.dataSize;
    }
    
    public long getSignature() {
        return this.signature;
    }
    
    public String getVendorID() {
        return this.vendorID;
    }
    
    public int getVersionNumber() {
        return this.versionNumber;
    }
    
    public void setAesStrength(final int aesStrength) {
        this.aesStrength = aesStrength;
    }
    
    public void setCompressionMethod(final int compressionMethod) {
        this.compressionMethod = compressionMethod;
    }
    
    public void setDataSize(final int dataSize) {
        this.dataSize = dataSize;
    }
    
    public void setSignature(final long signature) {
        this.signature = signature;
    }
    
    public void setVendorID(final String vendorID) {
        this.vendorID = vendorID;
    }
    
    public void setVersionNumber(final int versionNumber) {
        this.versionNumber = versionNumber;
    }
}
