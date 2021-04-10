package net.lingala.zip4j.model;

public class Zip64EndCentralDirRecord
{
    private byte[] extensibleDataSector;
    private int noOfThisDisk;
    private int noOfThisDiskStartOfCentralDir;
    private long offsetStartCenDirWRTStartDiskNo;
    private long signature;
    private long sizeOfCentralDir;
    private long sizeOfZip64EndCentralDirRec;
    private long totNoOfEntriesInCentralDir;
    private long totNoOfEntriesInCentralDirOnThisDisk;
    private int versionMadeBy;
    private int versionNeededToExtract;
    
    public byte[] getExtensibleDataSector() {
        return this.extensibleDataSector;
    }
    
    public int getNoOfThisDisk() {
        return this.noOfThisDisk;
    }
    
    public int getNoOfThisDiskStartOfCentralDir() {
        return this.noOfThisDiskStartOfCentralDir;
    }
    
    public long getOffsetStartCenDirWRTStartDiskNo() {
        return this.offsetStartCenDirWRTStartDiskNo;
    }
    
    public long getSignature() {
        return this.signature;
    }
    
    public long getSizeOfCentralDir() {
        return this.sizeOfCentralDir;
    }
    
    public long getSizeOfZip64EndCentralDirRec() {
        return this.sizeOfZip64EndCentralDirRec;
    }
    
    public long getTotNoOfEntriesInCentralDir() {
        return this.totNoOfEntriesInCentralDir;
    }
    
    public long getTotNoOfEntriesInCentralDirOnThisDisk() {
        return this.totNoOfEntriesInCentralDirOnThisDisk;
    }
    
    public int getVersionMadeBy() {
        return this.versionMadeBy;
    }
    
    public int getVersionNeededToExtract() {
        return this.versionNeededToExtract;
    }
    
    public void setExtensibleDataSector(final byte[] extensibleDataSector) {
        this.extensibleDataSector = extensibleDataSector;
    }
    
    public void setNoOfThisDisk(final int noOfThisDisk) {
        this.noOfThisDisk = noOfThisDisk;
    }
    
    public void setNoOfThisDiskStartOfCentralDir(final int noOfThisDiskStartOfCentralDir) {
        this.noOfThisDiskStartOfCentralDir = noOfThisDiskStartOfCentralDir;
    }
    
    public void setOffsetStartCenDirWRTStartDiskNo(final long offsetStartCenDirWRTStartDiskNo) {
        this.offsetStartCenDirWRTStartDiskNo = offsetStartCenDirWRTStartDiskNo;
    }
    
    public void setSignature(final long signature) {
        this.signature = signature;
    }
    
    public void setSizeOfCentralDir(final long sizeOfCentralDir) {
        this.sizeOfCentralDir = sizeOfCentralDir;
    }
    
    public void setSizeOfZip64EndCentralDirRec(final long sizeOfZip64EndCentralDirRec) {
        this.sizeOfZip64EndCentralDirRec = sizeOfZip64EndCentralDirRec;
    }
    
    public void setTotNoOfEntriesInCentralDir(final long totNoOfEntriesInCentralDir) {
        this.totNoOfEntriesInCentralDir = totNoOfEntriesInCentralDir;
    }
    
    public void setTotNoOfEntriesInCentralDirOnThisDisk(final long totNoOfEntriesInCentralDirOnThisDisk) {
        this.totNoOfEntriesInCentralDirOnThisDisk = totNoOfEntriesInCentralDirOnThisDisk;
    }
    
    public void setVersionMadeBy(final int versionMadeBy) {
        this.versionMadeBy = versionMadeBy;
    }
    
    public void setVersionNeededToExtract(final int versionNeededToExtract) {
        this.versionNeededToExtract = versionNeededToExtract;
    }
}
