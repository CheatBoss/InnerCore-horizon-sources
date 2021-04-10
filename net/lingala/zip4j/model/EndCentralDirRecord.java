package net.lingala.zip4j.model;

public class EndCentralDirRecord
{
    private String comment;
    private byte[] commentBytes;
    private int commentLength;
    private int noOfThisDisk;
    private int noOfThisDiskStartOfCentralDir;
    private long offsetOfStartOfCentralDir;
    private long signature;
    private int sizeOfCentralDir;
    private int totNoOfEntriesInCentralDir;
    private int totNoOfEntriesInCentralDirOnThisDisk;
    
    public String getComment() {
        return this.comment;
    }
    
    public byte[] getCommentBytes() {
        return this.commentBytes;
    }
    
    public int getCommentLength() {
        return this.commentLength;
    }
    
    public int getNoOfThisDisk() {
        return this.noOfThisDisk;
    }
    
    public int getNoOfThisDiskStartOfCentralDir() {
        return this.noOfThisDiskStartOfCentralDir;
    }
    
    public long getOffsetOfStartOfCentralDir() {
        return this.offsetOfStartOfCentralDir;
    }
    
    public long getSignature() {
        return this.signature;
    }
    
    public int getSizeOfCentralDir() {
        return this.sizeOfCentralDir;
    }
    
    public int getTotNoOfEntriesInCentralDir() {
        return this.totNoOfEntriesInCentralDir;
    }
    
    public int getTotNoOfEntriesInCentralDirOnThisDisk() {
        return this.totNoOfEntriesInCentralDirOnThisDisk;
    }
    
    public void setComment(final String comment) {
        this.comment = comment;
    }
    
    public void setCommentBytes(final byte[] commentBytes) {
        this.commentBytes = commentBytes;
    }
    
    public void setCommentLength(final int commentLength) {
        this.commentLength = commentLength;
    }
    
    public void setNoOfThisDisk(final int noOfThisDisk) {
        this.noOfThisDisk = noOfThisDisk;
    }
    
    public void setNoOfThisDiskStartOfCentralDir(final int noOfThisDiskStartOfCentralDir) {
        this.noOfThisDiskStartOfCentralDir = noOfThisDiskStartOfCentralDir;
    }
    
    public void setOffsetOfStartOfCentralDir(final long offsetOfStartOfCentralDir) {
        this.offsetOfStartOfCentralDir = offsetOfStartOfCentralDir;
    }
    
    public void setSignature(final long signature) {
        this.signature = signature;
    }
    
    public void setSizeOfCentralDir(final int sizeOfCentralDir) {
        this.sizeOfCentralDir = sizeOfCentralDir;
    }
    
    public void setTotNoOfEntriesInCentralDir(final int totNoOfEntriesInCentralDir) {
        this.totNoOfEntriesInCentralDir = totNoOfEntriesInCentralDir;
    }
    
    public void setTotNoOfEntriesInCentralDirOnThisDisk(final int totNoOfEntriesInCentralDirOnThisDisk) {
        this.totNoOfEntriesInCentralDirOnThisDisk = totNoOfEntriesInCentralDirOnThisDisk;
    }
}
