package net.lingala.zip4j.model;

public class Zip64ExtendedInfo
{
    private long compressedSize;
    private int diskNumberStart;
    private int header;
    private long offsetLocalHeader;
    private int size;
    private long unCompressedSize;
    
    public Zip64ExtendedInfo() {
        this.compressedSize = -1L;
        this.unCompressedSize = -1L;
        this.offsetLocalHeader = -1L;
        this.diskNumberStart = -1;
    }
    
    public long getCompressedSize() {
        return this.compressedSize;
    }
    
    public int getDiskNumberStart() {
        return this.diskNumberStart;
    }
    
    public int getHeader() {
        return this.header;
    }
    
    public long getOffsetLocalHeader() {
        return this.offsetLocalHeader;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public long getUnCompressedSize() {
        return this.unCompressedSize;
    }
    
    public void setCompressedSize(final long compressedSize) {
        this.compressedSize = compressedSize;
    }
    
    public void setDiskNumberStart(final int diskNumberStart) {
        this.diskNumberStart = diskNumberStart;
    }
    
    public void setHeader(final int header) {
        this.header = header;
    }
    
    public void setOffsetLocalHeader(final long offsetLocalHeader) {
        this.offsetLocalHeader = offsetLocalHeader;
    }
    
    public void setSize(final int size) {
        this.size = size;
    }
    
    public void setUnCompressedSize(final long unCompressedSize) {
        this.unCompressedSize = unCompressedSize;
    }
}
