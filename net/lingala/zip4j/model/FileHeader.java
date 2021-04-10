package net.lingala.zip4j.model;

import java.util.*;
import net.lingala.zip4j.progress.*;
import net.lingala.zip4j.exception.*;
import net.lingala.zip4j.util.*;
import net.lingala.zip4j.unzip.*;

public class FileHeader
{
    private AESExtraDataRecord aesExtraDataRecord;
    private long compressedSize;
    private int compressionMethod;
    private long crc32;
    private byte[] crcBuff;
    private boolean dataDescriptorExists;
    private int diskNumberStart;
    private int encryptionMethod;
    private byte[] externalFileAttr;
    private ArrayList extraDataRecords;
    private int extraFieldLength;
    private String fileComment;
    private int fileCommentLength;
    private String fileName;
    private int fileNameLength;
    private boolean fileNameUTF8Encoded;
    private byte[] generalPurposeFlag;
    private byte[] internalFileAttr;
    private boolean isDirectory;
    private boolean isEncrypted;
    private int lastModFileTime;
    private long offsetLocalHeader;
    private char[] password;
    private int signature;
    private long uncompressedSize;
    private int versionMadeBy;
    private int versionNeededToExtract;
    private Zip64ExtendedInfo zip64ExtendedInfo;
    
    public FileHeader() {
        this.encryptionMethod = -1;
        this.crc32 = 0L;
        this.uncompressedSize = 0L;
    }
    
    public void extractFile(final ZipModel zipModel, final String s, final UnzipParameters unzipParameters, final String s2, final ProgressMonitor progressMonitor, final boolean b) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("input zipModel is null");
        }
        if (!Zip4jUtil.checkOutputFolder(s)) {
            throw new ZipException("Invalid output path");
        }
        if (this == null) {
            throw new ZipException("invalid file header");
        }
        new Unzip(zipModel).extractFile(this, s, unzipParameters, s2, progressMonitor, b);
    }
    
    public void extractFile(final ZipModel zipModel, final String s, final UnzipParameters unzipParameters, final ProgressMonitor progressMonitor, final boolean b) throws ZipException {
        this.extractFile(zipModel, s, unzipParameters, null, progressMonitor, b);
    }
    
    public void extractFile(final ZipModel zipModel, final String s, final ProgressMonitor progressMonitor, final boolean b) throws ZipException {
        this.extractFile(zipModel, s, null, progressMonitor, b);
    }
    
    public AESExtraDataRecord getAesExtraDataRecord() {
        return this.aesExtraDataRecord;
    }
    
    public long getCompressedSize() {
        return this.compressedSize;
    }
    
    public int getCompressionMethod() {
        return this.compressionMethod;
    }
    
    public long getCrc32() {
        return this.crc32 & 0xFFFFFFFFL;
    }
    
    public byte[] getCrcBuff() {
        return this.crcBuff;
    }
    
    public int getDiskNumberStart() {
        return this.diskNumberStart;
    }
    
    public int getEncryptionMethod() {
        return this.encryptionMethod;
    }
    
    public byte[] getExternalFileAttr() {
        return this.externalFileAttr;
    }
    
    public ArrayList getExtraDataRecords() {
        return this.extraDataRecords;
    }
    
    public int getExtraFieldLength() {
        return this.extraFieldLength;
    }
    
    public String getFileComment() {
        return this.fileComment;
    }
    
    public int getFileCommentLength() {
        return this.fileCommentLength;
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    public int getFileNameLength() {
        return this.fileNameLength;
    }
    
    public byte[] getGeneralPurposeFlag() {
        return this.generalPurposeFlag;
    }
    
    public byte[] getInternalFileAttr() {
        return this.internalFileAttr;
    }
    
    public int getLastModFileTime() {
        return this.lastModFileTime;
    }
    
    public long getOffsetLocalHeader() {
        return this.offsetLocalHeader;
    }
    
    public char[] getPassword() {
        return this.password;
    }
    
    public int getSignature() {
        return this.signature;
    }
    
    public long getUncompressedSize() {
        return this.uncompressedSize;
    }
    
    public int getVersionMadeBy() {
        return this.versionMadeBy;
    }
    
    public int getVersionNeededToExtract() {
        return this.versionNeededToExtract;
    }
    
    public Zip64ExtendedInfo getZip64ExtendedInfo() {
        return this.zip64ExtendedInfo;
    }
    
    public boolean isDataDescriptorExists() {
        return this.dataDescriptorExists;
    }
    
    public boolean isDirectory() {
        return this.isDirectory;
    }
    
    public boolean isEncrypted() {
        return this.isEncrypted;
    }
    
    public boolean isFileNameUTF8Encoded() {
        return this.fileNameUTF8Encoded;
    }
    
    public void setAesExtraDataRecord(final AESExtraDataRecord aesExtraDataRecord) {
        this.aesExtraDataRecord = aesExtraDataRecord;
    }
    
    public void setCompressedSize(final long compressedSize) {
        this.compressedSize = compressedSize;
    }
    
    public void setCompressionMethod(final int compressionMethod) {
        this.compressionMethod = compressionMethod;
    }
    
    public void setCrc32(final long crc32) {
        this.crc32 = crc32;
    }
    
    public void setCrcBuff(final byte[] crcBuff) {
        this.crcBuff = crcBuff;
    }
    
    public void setDataDescriptorExists(final boolean dataDescriptorExists) {
        this.dataDescriptorExists = dataDescriptorExists;
    }
    
    public void setDirectory(final boolean isDirectory) {
        this.isDirectory = isDirectory;
    }
    
    public void setDiskNumberStart(final int diskNumberStart) {
        this.diskNumberStart = diskNumberStart;
    }
    
    public void setEncrypted(final boolean isEncrypted) {
        this.isEncrypted = isEncrypted;
    }
    
    public void setEncryptionMethod(final int encryptionMethod) {
        this.encryptionMethod = encryptionMethod;
    }
    
    public void setExternalFileAttr(final byte[] externalFileAttr) {
        this.externalFileAttr = externalFileAttr;
    }
    
    public void setExtraDataRecords(final ArrayList extraDataRecords) {
        this.extraDataRecords = extraDataRecords;
    }
    
    public void setExtraFieldLength(final int extraFieldLength) {
        this.extraFieldLength = extraFieldLength;
    }
    
    public void setFileComment(final String fileComment) {
        this.fileComment = fileComment;
    }
    
    public void setFileCommentLength(final int fileCommentLength) {
        this.fileCommentLength = fileCommentLength;
    }
    
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }
    
    public void setFileNameLength(final int fileNameLength) {
        this.fileNameLength = fileNameLength;
    }
    
    public void setFileNameUTF8Encoded(final boolean fileNameUTF8Encoded) {
        this.fileNameUTF8Encoded = fileNameUTF8Encoded;
    }
    
    public void setGeneralPurposeFlag(final byte[] generalPurposeFlag) {
        this.generalPurposeFlag = generalPurposeFlag;
    }
    
    public void setInternalFileAttr(final byte[] internalFileAttr) {
        this.internalFileAttr = internalFileAttr;
    }
    
    public void setLastModFileTime(final int lastModFileTime) {
        this.lastModFileTime = lastModFileTime;
    }
    
    public void setOffsetLocalHeader(final long offsetLocalHeader) {
        this.offsetLocalHeader = offsetLocalHeader;
    }
    
    public void setPassword(final char[] password) {
        this.password = password;
    }
    
    public void setSignature(final int signature) {
        this.signature = signature;
    }
    
    public void setUncompressedSize(final long uncompressedSize) {
        this.uncompressedSize = uncompressedSize;
    }
    
    public void setVersionMadeBy(final int versionMadeBy) {
        this.versionMadeBy = versionMadeBy;
    }
    
    public void setVersionNeededToExtract(final int versionNeededToExtract) {
        this.versionNeededToExtract = versionNeededToExtract;
    }
    
    public void setZip64ExtendedInfo(final Zip64ExtendedInfo zip64ExtendedInfo) {
        this.zip64ExtendedInfo = zip64ExtendedInfo;
    }
}
