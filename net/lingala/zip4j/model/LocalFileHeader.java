package net.lingala.zip4j.model;

import java.util.*;

public class LocalFileHeader
{
    private AESExtraDataRecord aesExtraDataRecord;
    private long compressedSize;
    private int compressionMethod;
    private long crc32;
    private byte[] crcBuff;
    private boolean dataDescriptorExists;
    private int encryptionMethod;
    private ArrayList extraDataRecords;
    private byte[] extraField;
    private int extraFieldLength;
    private String fileName;
    private int fileNameLength;
    private boolean fileNameUTF8Encoded;
    private byte[] generalPurposeFlag;
    private boolean isEncrypted;
    private int lastModFileTime;
    private long offsetStartOfData;
    private char[] password;
    private int signature;
    private long uncompressedSize;
    private int versionNeededToExtract;
    private boolean writeComprSizeInZip64ExtraRecord;
    private Zip64ExtendedInfo zip64ExtendedInfo;
    
    public LocalFileHeader() {
        this.encryptionMethod = -1;
        this.writeComprSizeInZip64ExtraRecord = false;
        this.crc32 = 0L;
        this.uncompressedSize = 0L;
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
        return this.crc32;
    }
    
    public byte[] getCrcBuff() {
        return this.crcBuff;
    }
    
    public int getEncryptionMethod() {
        return this.encryptionMethod;
    }
    
    public ArrayList getExtraDataRecords() {
        return this.extraDataRecords;
    }
    
    public byte[] getExtraField() {
        return this.extraField;
    }
    
    public int getExtraFieldLength() {
        return this.extraFieldLength;
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
    
    public int getLastModFileTime() {
        return this.lastModFileTime;
    }
    
    public long getOffsetStartOfData() {
        return this.offsetStartOfData;
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
    
    public int getVersionNeededToExtract() {
        return this.versionNeededToExtract;
    }
    
    public Zip64ExtendedInfo getZip64ExtendedInfo() {
        return this.zip64ExtendedInfo;
    }
    
    public boolean isDataDescriptorExists() {
        return this.dataDescriptorExists;
    }
    
    public boolean isEncrypted() {
        return this.isEncrypted;
    }
    
    public boolean isFileNameUTF8Encoded() {
        return this.fileNameUTF8Encoded;
    }
    
    public boolean isWriteComprSizeInZip64ExtraRecord() {
        return this.writeComprSizeInZip64ExtraRecord;
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
    
    public void setEncrypted(final boolean isEncrypted) {
        this.isEncrypted = isEncrypted;
    }
    
    public void setEncryptionMethod(final int encryptionMethod) {
        this.encryptionMethod = encryptionMethod;
    }
    
    public void setExtraDataRecords(final ArrayList extraDataRecords) {
        this.extraDataRecords = extraDataRecords;
    }
    
    public void setExtraField(final byte[] extraField) {
        this.extraField = extraField;
    }
    
    public void setExtraFieldLength(final int extraFieldLength) {
        this.extraFieldLength = extraFieldLength;
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
    
    public void setLastModFileTime(final int lastModFileTime) {
        this.lastModFileTime = lastModFileTime;
    }
    
    public void setOffsetStartOfData(final long offsetStartOfData) {
        this.offsetStartOfData = offsetStartOfData;
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
    
    public void setVersionNeededToExtract(final int versionNeededToExtract) {
        this.versionNeededToExtract = versionNeededToExtract;
    }
    
    public void setWriteComprSizeInZip64ExtraRecord(final boolean writeComprSizeInZip64ExtraRecord) {
        this.writeComprSizeInZip64ExtraRecord = writeComprSizeInZip64ExtraRecord;
    }
    
    public void setZip64ExtendedInfo(final Zip64ExtendedInfo zip64ExtendedInfo) {
        this.zip64ExtendedInfo = zip64ExtendedInfo;
    }
}
