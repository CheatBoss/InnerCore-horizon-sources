package net.lingala.zip4j.model;

public class ArchiveExtraDataRecord
{
    private String extraFieldData;
    private int extraFieldLength;
    private int signature;
    
    public String getExtraFieldData() {
        return this.extraFieldData;
    }
    
    public int getExtraFieldLength() {
        return this.extraFieldLength;
    }
    
    public int getSignature() {
        return this.signature;
    }
    
    public void setExtraFieldData(final String extraFieldData) {
        this.extraFieldData = extraFieldData;
    }
    
    public void setExtraFieldLength(final int extraFieldLength) {
        this.extraFieldLength = extraFieldLength;
    }
    
    public void setSignature(final int signature) {
        this.signature = signature;
    }
}
