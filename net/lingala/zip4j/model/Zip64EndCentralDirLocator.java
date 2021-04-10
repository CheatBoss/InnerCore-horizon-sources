package net.lingala.zip4j.model;

public class Zip64EndCentralDirLocator
{
    private int noOfDiskStartOfZip64EndOfCentralDirRec;
    private long offsetZip64EndOfCentralDirRec;
    private long signature;
    private int totNumberOfDiscs;
    
    public int getNoOfDiskStartOfZip64EndOfCentralDirRec() {
        return this.noOfDiskStartOfZip64EndOfCentralDirRec;
    }
    
    public long getOffsetZip64EndOfCentralDirRec() {
        return this.offsetZip64EndOfCentralDirRec;
    }
    
    public long getSignature() {
        return this.signature;
    }
    
    public int getTotNumberOfDiscs() {
        return this.totNumberOfDiscs;
    }
    
    public void setNoOfDiskStartOfZip64EndOfCentralDirRec(final int noOfDiskStartOfZip64EndOfCentralDirRec) {
        this.noOfDiskStartOfZip64EndOfCentralDirRec = noOfDiskStartOfZip64EndOfCentralDirRec;
    }
    
    public void setOffsetZip64EndOfCentralDirRec(final long offsetZip64EndOfCentralDirRec) {
        this.offsetZip64EndOfCentralDirRec = offsetZip64EndOfCentralDirRec;
    }
    
    public void setSignature(final long signature) {
        this.signature = signature;
    }
    
    public void setTotNumberOfDiscs(final int totNumberOfDiscs) {
        this.totNumberOfDiscs = totNumberOfDiscs;
    }
}
