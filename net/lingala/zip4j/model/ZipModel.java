package net.lingala.zip4j.model;

import java.util.*;

public class ZipModel implements Cloneable
{
    private ArchiveExtraDataRecord archiveExtraDataRecord;
    private CentralDirectory centralDirectory;
    private List dataDescriptorList;
    private long end;
    private EndCentralDirRecord endCentralDirRecord;
    private String fileNameCharset;
    private boolean isNestedZipFile;
    private boolean isZip64Format;
    private List localFileHeaderList;
    private boolean splitArchive;
    private long splitLength;
    private long start;
    private Zip64EndCentralDirLocator zip64EndCentralDirLocator;
    private Zip64EndCentralDirRecord zip64EndCentralDirRecord;
    private String zipFile;
    
    public ZipModel() {
        this.splitLength = -1L;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public ArchiveExtraDataRecord getArchiveExtraDataRecord() {
        return this.archiveExtraDataRecord;
    }
    
    public CentralDirectory getCentralDirectory() {
        return this.centralDirectory;
    }
    
    public List getDataDescriptorList() {
        return this.dataDescriptorList;
    }
    
    public long getEnd() {
        return this.end;
    }
    
    public EndCentralDirRecord getEndCentralDirRecord() {
        return this.endCentralDirRecord;
    }
    
    public String getFileNameCharset() {
        return this.fileNameCharset;
    }
    
    public List getLocalFileHeaderList() {
        return this.localFileHeaderList;
    }
    
    public long getSplitLength() {
        return this.splitLength;
    }
    
    public long getStart() {
        return this.start;
    }
    
    public Zip64EndCentralDirLocator getZip64EndCentralDirLocator() {
        return this.zip64EndCentralDirLocator;
    }
    
    public Zip64EndCentralDirRecord getZip64EndCentralDirRecord() {
        return this.zip64EndCentralDirRecord;
    }
    
    public String getZipFile() {
        return this.zipFile;
    }
    
    public boolean isNestedZipFile() {
        return this.isNestedZipFile;
    }
    
    public boolean isSplitArchive() {
        return this.splitArchive;
    }
    
    public boolean isZip64Format() {
        return this.isZip64Format;
    }
    
    public void setArchiveExtraDataRecord(final ArchiveExtraDataRecord archiveExtraDataRecord) {
        this.archiveExtraDataRecord = archiveExtraDataRecord;
    }
    
    public void setCentralDirectory(final CentralDirectory centralDirectory) {
        this.centralDirectory = centralDirectory;
    }
    
    public void setDataDescriptorList(final List dataDescriptorList) {
        this.dataDescriptorList = dataDescriptorList;
    }
    
    public void setEnd(final long end) {
        this.end = end;
    }
    
    public void setEndCentralDirRecord(final EndCentralDirRecord endCentralDirRecord) {
        this.endCentralDirRecord = endCentralDirRecord;
    }
    
    public void setFileNameCharset(final String fileNameCharset) {
        this.fileNameCharset = fileNameCharset;
    }
    
    public void setLocalFileHeaderList(final List localFileHeaderList) {
        this.localFileHeaderList = localFileHeaderList;
    }
    
    public void setNestedZipFile(final boolean isNestedZipFile) {
        this.isNestedZipFile = isNestedZipFile;
    }
    
    public void setSplitArchive(final boolean splitArchive) {
        this.splitArchive = splitArchive;
    }
    
    public void setSplitLength(final long splitLength) {
        this.splitLength = splitLength;
    }
    
    public void setStart(final long start) {
        this.start = start;
    }
    
    public void setZip64EndCentralDirLocator(final Zip64EndCentralDirLocator zip64EndCentralDirLocator) {
        this.zip64EndCentralDirLocator = zip64EndCentralDirLocator;
    }
    
    public void setZip64EndCentralDirRecord(final Zip64EndCentralDirRecord zip64EndCentralDirRecord) {
        this.zip64EndCentralDirRecord = zip64EndCentralDirRecord;
    }
    
    public void setZip64Format(final boolean isZip64Format) {
        this.isZip64Format = isZip64Format;
    }
    
    public void setZipFile(final String zipFile) {
        this.zipFile = zipFile;
    }
}
