package net.lingala.zip4j.model;

public class DigitalSignature
{
    private int headerSignature;
    private String signatureData;
    private int sizeOfData;
    
    public int getHeaderSignature() {
        return this.headerSignature;
    }
    
    public String getSignatureData() {
        return this.signatureData;
    }
    
    public int getSizeOfData() {
        return this.sizeOfData;
    }
    
    public void setHeaderSignature(final int headerSignature) {
        this.headerSignature = headerSignature;
    }
    
    public void setSignatureData(final String signatureData) {
        this.signatureData = signatureData;
    }
    
    public void setSizeOfData(final int sizeOfData) {
        this.sizeOfData = sizeOfData;
    }
}
