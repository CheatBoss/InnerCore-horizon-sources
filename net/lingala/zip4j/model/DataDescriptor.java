package net.lingala.zip4j.model;

public class DataDescriptor
{
    private int compressedSize;
    private String crc32;
    private int uncompressedSize;
    
    public int getCompressedSize() {
        return this.compressedSize;
    }
    
    public String getCrc32() {
        return this.crc32;
    }
    
    public int getUncompressedSize() {
        return this.uncompressedSize;
    }
    
    public void setCompressedSize(final int compressedSize) {
        this.compressedSize = compressedSize;
    }
    
    public void setCrc32(final String crc32) {
        this.crc32 = crc32;
    }
    
    public void setUncompressedSize(final int uncompressedSize) {
        this.uncompressedSize = uncompressedSize;
    }
}
