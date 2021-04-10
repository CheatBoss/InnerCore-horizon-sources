package net.lingala.zip4j.model;

public class ExtraDataRecord
{
    private byte[] data;
    private long header;
    private int sizeOfData;
    
    public byte[] getData() {
        return this.data;
    }
    
    public long getHeader() {
        return this.header;
    }
    
    public int getSizeOfData() {
        return this.sizeOfData;
    }
    
    public void setData(final byte[] data) {
        this.data = data;
    }
    
    public void setHeader(final long header) {
        this.header = header;
    }
    
    public void setSizeOfData(final int sizeOfData) {
        this.sizeOfData = sizeOfData;
    }
}
