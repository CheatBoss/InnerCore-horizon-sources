package net.lingala.zip4j.core;

import net.lingala.zip4j.exception.*;
import java.util.*;
import net.lingala.zip4j.util.*;
import java.io.*;
import net.lingala.zip4j.model.*;

public class HeaderReader
{
    private RandomAccessFile zip4jRaf;
    private ZipModel zipModel;
    
    public HeaderReader(final RandomAccessFile zip4jRaf) {
        this.zip4jRaf = null;
        this.zip4jRaf = zip4jRaf;
    }
    
    private byte[] getLongByteFromIntByte(final byte[] array) throws ZipException {
        if (array == null) {
            throw new ZipException("input parameter is null, cannot expand to 8 bytes");
        }
        if (array.length != 4) {
            throw new ZipException("invalid byte length, cannot expand to 8 bytes");
        }
        return new byte[] { array[0], array[1], array[2], array[3], 0, 0, 0, 0 };
    }
    
    private AESExtraDataRecord readAESExtraDataRecord(final ArrayList list) throws ZipException {
        if (list == null) {
            return null;
        }
        for (int i = 0; i < list.size(); ++i) {
            final ExtraDataRecord extraDataRecord = list.get(i);
            if (extraDataRecord != null) {
                if (extraDataRecord.getHeader() == 39169L) {
                    if (extraDataRecord.getData() == null) {
                        throw new ZipException("corrput AES extra data records");
                    }
                    final AESExtraDataRecord aesExtraDataRecord = new AESExtraDataRecord();
                    aesExtraDataRecord.setSignature(39169L);
                    aesExtraDataRecord.setDataSize(extraDataRecord.getSizeOfData());
                    final byte[] data = extraDataRecord.getData();
                    aesExtraDataRecord.setVersionNumber(Raw.readShortLittleEndian(data, 0));
                    final byte[] array = new byte[2];
                    System.arraycopy(data, 2, array, 0, 2);
                    aesExtraDataRecord.setVendorID(new String(array));
                    aesExtraDataRecord.setAesStrength(data[4] & 0xFF);
                    aesExtraDataRecord.setCompressionMethod(Raw.readShortLittleEndian(data, 5));
                    return aesExtraDataRecord;
                }
            }
        }
        return null;
    }
    
    private void readAndSaveAESExtraDataRecord(final FileHeader fileHeader) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("file header is null in reading Zip64 Extended Info");
        }
        if (fileHeader.getExtraDataRecords() == null) {
            return;
        }
        if (fileHeader.getExtraDataRecords().size() <= 0) {
            return;
        }
        final AESExtraDataRecord aesExtraDataRecord = this.readAESExtraDataRecord(fileHeader.getExtraDataRecords());
        if (aesExtraDataRecord != null) {
            fileHeader.setAesExtraDataRecord(aesExtraDataRecord);
            fileHeader.setEncryptionMethod(99);
        }
    }
    
    private void readAndSaveAESExtraDataRecord(final LocalFileHeader localFileHeader) throws ZipException {
        if (localFileHeader == null) {
            throw new ZipException("file header is null in reading Zip64 Extended Info");
        }
        if (localFileHeader.getExtraDataRecords() == null) {
            return;
        }
        if (localFileHeader.getExtraDataRecords().size() <= 0) {
            return;
        }
        final AESExtraDataRecord aesExtraDataRecord = this.readAESExtraDataRecord(localFileHeader.getExtraDataRecords());
        if (aesExtraDataRecord != null) {
            localFileHeader.setAesExtraDataRecord(aesExtraDataRecord);
            localFileHeader.setEncryptionMethod(99);
        }
    }
    
    private void readAndSaveExtraDataRecord(final FileHeader fileHeader) throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("invalid file handler when trying to read extra data record");
        }
        if (fileHeader == null) {
            throw new ZipException("file header is null");
        }
        final int extraFieldLength = fileHeader.getExtraFieldLength();
        if (extraFieldLength <= 0) {
            return;
        }
        fileHeader.setExtraDataRecords(this.readExtraDataRecords(extraFieldLength));
    }
    
    private void readAndSaveExtraDataRecord(final LocalFileHeader localFileHeader) throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("invalid file handler when trying to read extra data record");
        }
        if (localFileHeader == null) {
            throw new ZipException("file header is null");
        }
        final int extraFieldLength = localFileHeader.getExtraFieldLength();
        if (extraFieldLength <= 0) {
            return;
        }
        localFileHeader.setExtraDataRecords(this.readExtraDataRecords(extraFieldLength));
    }
    
    private void readAndSaveZip64ExtendedInfo(final FileHeader fileHeader) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("file header is null in reading Zip64 Extended Info");
        }
        if (fileHeader.getExtraDataRecords() == null) {
            return;
        }
        if (fileHeader.getExtraDataRecords().size() <= 0) {
            return;
        }
        final Zip64ExtendedInfo zip64ExtendedInfo = this.readZip64ExtendedInfo(fileHeader.getExtraDataRecords(), fileHeader.getUncompressedSize(), fileHeader.getCompressedSize(), fileHeader.getOffsetLocalHeader(), fileHeader.getDiskNumberStart());
        if (zip64ExtendedInfo != null) {
            fileHeader.setZip64ExtendedInfo(zip64ExtendedInfo);
            if (zip64ExtendedInfo.getUnCompressedSize() != -1L) {
                fileHeader.setUncompressedSize(zip64ExtendedInfo.getUnCompressedSize());
            }
            if (zip64ExtendedInfo.getCompressedSize() != -1L) {
                fileHeader.setCompressedSize(zip64ExtendedInfo.getCompressedSize());
            }
            if (zip64ExtendedInfo.getOffsetLocalHeader() != -1L) {
                fileHeader.setOffsetLocalHeader(zip64ExtendedInfo.getOffsetLocalHeader());
            }
            if (zip64ExtendedInfo.getDiskNumberStart() != -1) {
                fileHeader.setDiskNumberStart(zip64ExtendedInfo.getDiskNumberStart());
            }
        }
    }
    
    private void readAndSaveZip64ExtendedInfo(final LocalFileHeader localFileHeader) throws ZipException {
        if (localFileHeader == null) {
            throw new ZipException("file header is null in reading Zip64 Extended Info");
        }
        if (localFileHeader.getExtraDataRecords() == null) {
            return;
        }
        if (localFileHeader.getExtraDataRecords().size() <= 0) {
            return;
        }
        final Zip64ExtendedInfo zip64ExtendedInfo = this.readZip64ExtendedInfo(localFileHeader.getExtraDataRecords(), localFileHeader.getUncompressedSize(), localFileHeader.getCompressedSize(), -1L, -1);
        if (zip64ExtendedInfo != null) {
            localFileHeader.setZip64ExtendedInfo(zip64ExtendedInfo);
            if (zip64ExtendedInfo.getUnCompressedSize() != -1L) {
                localFileHeader.setUncompressedSize(zip64ExtendedInfo.getUnCompressedSize());
            }
            if (zip64ExtendedInfo.getCompressedSize() != -1L) {
                localFileHeader.setCompressedSize(zip64ExtendedInfo.getCompressedSize());
            }
        }
    }
    
    private CentralDirectory readCentralDirectory() throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("random access file was null", 3);
        }
        if (this.zipModel.getEndCentralDirRecord() == null) {
            throw new ZipException("EndCentralRecord was null, maybe a corrupt zip file");
        }
        CentralDirectory centralDirectory;
        ArrayList<FileHeader> fileHeaders;
        EndCentralDirRecord endCentralDirRecord;
        long n;
        int totNoOfEntriesInCentralDir;
        byte[] array;
        byte[] array2;
        byte[] array3;
        int i;
        FileHeader fileHeader;
        int intLittleEndian;
        StringBuilder sb;
        boolean fileNameUTF8Encoded;
        byte b;
        boolean dataDescriptorExists;
        int shortLittleEndian;
        int shortLittleEndian2;
        byte[] array4;
        String fileName;
        StringBuilder sb2;
        StringBuilder sb3;
        byte[] array5;
        DigitalSignature digitalSignature;
        int intLittleEndian2;
        int shortLittleEndian3;
        byte[] array6;
        boolean directory;
        Label_0358_Outer:Label_0857_Outer:Label_0889_Outer:
        while (true) {
            while (true) {
                Label_1124: {
                Label_1118:
                    while (true) {
                    Label_1115:
                        while (true) {
                        Label_1109:
                            while (true) {
                                Label_1103: {
                                    try {
                                        centralDirectory = new CentralDirectory();
                                        fileHeaders = new ArrayList<FileHeader>();
                                        endCentralDirRecord = this.zipModel.getEndCentralDirRecord();
                                        n = endCentralDirRecord.getOffsetOfStartOfCentralDir();
                                        totNoOfEntriesInCentralDir = endCentralDirRecord.getTotNoOfEntriesInCentralDir();
                                        if (this.zipModel.isZip64Format()) {
                                            n = this.zipModel.getZip64EndCentralDirRecord().getOffsetStartCenDirWRTStartDiskNo();
                                            totNoOfEntriesInCentralDir = (int)this.zipModel.getZip64EndCentralDirRecord().getTotNoOfEntriesInCentralDir();
                                        }
                                        this.zip4jRaf.seek(n);
                                        array = new byte[4];
                                        array2 = new byte[2];
                                        array3 = new byte[8];
                                        i = 0;
                                        while (true) {
                                            while (i < totNoOfEntriesInCentralDir) {
                                                fileHeader = new FileHeader();
                                                this.readIntoBuff(this.zip4jRaf, array);
                                                intLittleEndian = Raw.readIntLittleEndian(array, 0);
                                                if (intLittleEndian != 33639248L) {
                                                    sb = new StringBuilder();
                                                    sb.append("Expected central directory entry not found (#");
                                                    sb.append(i + 1);
                                                    sb.append(")");
                                                    throw new ZipException(sb.toString());
                                                }
                                                fileHeader.setSignature(intLittleEndian);
                                                this.readIntoBuff(this.zip4jRaf, array2);
                                                fileHeader.setVersionMadeBy(Raw.readShortLittleEndian(array2, 0));
                                                this.readIntoBuff(this.zip4jRaf, array2);
                                                fileHeader.setVersionNeededToExtract(Raw.readShortLittleEndian(array2, 0));
                                                this.readIntoBuff(this.zip4jRaf, array2);
                                                if ((Raw.readShortLittleEndian(array2, 0) & 0x800) == 0x0) {
                                                    break Label_1103;
                                                }
                                                fileNameUTF8Encoded = true;
                                                fileHeader.setFileNameUTF8Encoded(fileNameUTF8Encoded);
                                                b = array2[0];
                                                if ((b & 0x1) != 0x0) {
                                                    fileHeader.setEncrypted(true);
                                                }
                                                fileHeader.setGeneralPurposeFlag(array2.clone());
                                                if (b >> 3 != 1) {
                                                    break Label_1109;
                                                }
                                                dataDescriptorExists = true;
                                                fileHeader.setDataDescriptorExists(dataDescriptorExists);
                                                this.readIntoBuff(this.zip4jRaf, array2);
                                                fileHeader.setCompressionMethod(Raw.readShortLittleEndian(array2, 0));
                                                this.readIntoBuff(this.zip4jRaf, array);
                                                fileHeader.setLastModFileTime(Raw.readIntLittleEndian(array, 0));
                                                this.readIntoBuff(this.zip4jRaf, array);
                                                fileHeader.setCrc32(Raw.readIntLittleEndian(array, 0));
                                                fileHeader.setCrcBuff(array.clone());
                                                this.readIntoBuff(this.zip4jRaf, array);
                                                fileHeader.setCompressedSize(Raw.readLongLittleEndian(this.getLongByteFromIntByte(array), 0));
                                                this.readIntoBuff(this.zip4jRaf, array);
                                                fileHeader.setUncompressedSize(Raw.readLongLittleEndian(this.getLongByteFromIntByte(array), 0));
                                                this.readIntoBuff(this.zip4jRaf, array2);
                                                shortLittleEndian = Raw.readShortLittleEndian(array2, 0);
                                                fileHeader.setFileNameLength(shortLittleEndian);
                                                this.readIntoBuff(this.zip4jRaf, array2);
                                                fileHeader.setExtraFieldLength(Raw.readShortLittleEndian(array2, 0));
                                                this.readIntoBuff(this.zip4jRaf, array2);
                                                shortLittleEndian2 = Raw.readShortLittleEndian(array2, 0);
                                                fileHeader.setFileComment(new String(array2));
                                                this.readIntoBuff(this.zip4jRaf, array2);
                                                fileHeader.setDiskNumberStart(Raw.readShortLittleEndian(array2, 0));
                                                this.readIntoBuff(this.zip4jRaf, array2);
                                                fileHeader.setInternalFileAttr(array2.clone());
                                                this.readIntoBuff(this.zip4jRaf, array);
                                                fileHeader.setExternalFileAttr(array.clone());
                                                this.readIntoBuff(this.zip4jRaf, array);
                                                fileHeader.setOffsetLocalHeader(Raw.readLongLittleEndian(this.getLongByteFromIntByte(array), 0) & 0xFFFFFFFFL);
                                                if (shortLittleEndian > 0) {
                                                    array4 = new byte[shortLittleEndian];
                                                    this.readIntoBuff(this.zip4jRaf, array4);
                                                    if (Zip4jUtil.isStringNotNullAndNotEmpty(this.zipModel.getFileNameCharset())) {
                                                        fileName = new String(array4, this.zipModel.getFileNameCharset());
                                                    }
                                                    else {
                                                        fileName = Zip4jUtil.decodeFileName(array4, fileHeader.isFileNameUTF8Encoded());
                                                    }
                                                    if (fileName == null) {
                                                        throw new ZipException("fileName is null when reading central directory");
                                                    }
                                                    sb2 = new StringBuilder();
                                                    sb2.append(":");
                                                    sb2.append(System.getProperty("file.separator"));
                                                    if (fileName.indexOf(sb2.toString()) < 0) {
                                                        break Label_1115;
                                                    }
                                                    sb3 = new StringBuilder();
                                                    sb3.append(":");
                                                    sb3.append(System.getProperty("file.separator"));
                                                    fileName = fileName.substring(fileName.indexOf(sb3.toString()) + 2);
                                                    fileHeader.setFileName(fileName);
                                                    if (!fileName.endsWith("/") && !fileName.endsWith("\\")) {
                                                        break Label_1118;
                                                    }
                                                    break Label_1124;
                                                }
                                                else {
                                                    fileHeader.setFileName(null);
                                                    this.readAndSaveExtraDataRecord(fileHeader);
                                                    this.readAndSaveZip64ExtendedInfo(fileHeader);
                                                    this.readAndSaveAESExtraDataRecord(fileHeader);
                                                    if (shortLittleEndian2 > 0) {
                                                        array5 = new byte[shortLittleEndian2];
                                                        this.readIntoBuff(this.zip4jRaf, array5);
                                                        fileHeader.setFileComment(new String(array5));
                                                    }
                                                    fileHeaders.add(fileHeader);
                                                    ++i;
                                                }
                                            }
                                            centralDirectory.setFileHeaders(fileHeaders);
                                            digitalSignature = new DigitalSignature();
                                            this.readIntoBuff(this.zip4jRaf, array);
                                            intLittleEndian2 = Raw.readIntLittleEndian(array, 0);
                                            if (intLittleEndian2 != 84233040L) {
                                                return centralDirectory;
                                            }
                                            digitalSignature.setHeaderSignature(intLittleEndian2);
                                            this.readIntoBuff(this.zip4jRaf, array2);
                                            shortLittleEndian3 = Raw.readShortLittleEndian(array2, 0);
                                            digitalSignature.setSizeOfData(shortLittleEndian3);
                                            if (shortLittleEndian3 > 0) {
                                                array6 = new byte[shortLittleEndian3];
                                                this.readIntoBuff(this.zip4jRaf, array6);
                                                digitalSignature.setSignatureData(new String(array6));
                                            }
                                            return centralDirectory;
                                            fileHeader.setDirectory(directory);
                                            continue Label_0889_Outer;
                                        }
                                    }
                                    catch (IOException ex) {
                                        throw new ZipException(ex);
                                    }
                                }
                                fileNameUTF8Encoded = false;
                                continue Label_0358_Outer;
                            }
                            dataDescriptorExists = false;
                            continue Label_0857_Outer;
                        }
                        continue Label_0889_Outer;
                    }
                    directory = false;
                    continue;
                }
                directory = true;
                continue;
            }
        }
    }
    
    private EndCentralDirRecord readEndOfCentralDirectoryRecord() throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("random access file was null", 3);
        }
        while (true) {
            while (true) {
                long n;
                try {
                    final byte[] array = new byte[4];
                    n = this.zip4jRaf.length() - 22L;
                    final EndCentralDirRecord endCentralDirRecord = new EndCentralDirRecord();
                    int n2 = 0;
                    this.zip4jRaf.seek(n);
                    ++n2;
                    if (Raw.readLeInt(this.zip4jRaf, array) == 101010256L || n2 > 3000) {
                        if (Raw.readIntLittleEndian(array, 0) != 101010256L) {
                            throw new ZipException("zip headers not found. probably not a zip file");
                        }
                        final byte[] array2 = new byte[4];
                        final byte[] array3 = new byte[2];
                        endCentralDirRecord.setSignature(101010256L);
                        this.readIntoBuff(this.zip4jRaf, array3);
                        endCentralDirRecord.setNoOfThisDisk(Raw.readShortLittleEndian(array3, 0));
                        this.readIntoBuff(this.zip4jRaf, array3);
                        endCentralDirRecord.setNoOfThisDiskStartOfCentralDir(Raw.readShortLittleEndian(array3, 0));
                        this.readIntoBuff(this.zip4jRaf, array3);
                        endCentralDirRecord.setTotNoOfEntriesInCentralDirOnThisDisk(Raw.readShortLittleEndian(array3, 0));
                        this.readIntoBuff(this.zip4jRaf, array3);
                        endCentralDirRecord.setTotNoOfEntriesInCentralDir(Raw.readShortLittleEndian(array3, 0));
                        this.readIntoBuff(this.zip4jRaf, array2);
                        endCentralDirRecord.setSizeOfCentralDir(Raw.readIntLittleEndian(array2, 0));
                        this.readIntoBuff(this.zip4jRaf, array2);
                        endCentralDirRecord.setOffsetOfStartOfCentralDir(Raw.readLongLittleEndian(this.getLongByteFromIntByte(array2), 0));
                        this.readIntoBuff(this.zip4jRaf, array3);
                        final int shortLittleEndian = Raw.readShortLittleEndian(array3, 0);
                        endCentralDirRecord.setCommentLength(shortLittleEndian);
                        if (shortLittleEndian > 0) {
                            final byte[] commentBytes = new byte[shortLittleEndian];
                            this.readIntoBuff(this.zip4jRaf, commentBytes);
                            endCentralDirRecord.setComment(new String(commentBytes));
                            endCentralDirRecord.setCommentBytes(commentBytes);
                        }
                        else {
                            endCentralDirRecord.setComment(null);
                        }
                        if (endCentralDirRecord.getNoOfThisDisk() > 0) {
                            this.zipModel.setSplitArchive(true);
                            return endCentralDirRecord;
                        }
                        this.zipModel.setSplitArchive(false);
                        return endCentralDirRecord;
                    }
                }
                catch (IOException ex) {
                    throw new ZipException("Probably not a zip file or a corrupted zip file", ex, 4);
                }
                --n;
                continue;
            }
        }
    }
    
    private ArrayList readExtraDataRecords(int size) throws ZipException {
        if (size <= 0) {
            return null;
        }
        try {
            final byte[] array = new byte[size];
            this.zip4jRaf.read(array);
            int i = 0;
            final ArrayList<ExtraDataRecord> list = new ArrayList<ExtraDataRecord>();
            while (i < size) {
                final ExtraDataRecord extraDataRecord = new ExtraDataRecord();
                extraDataRecord.setHeader(Raw.readShortLittleEndian(array, i));
                final int n = i + 2;
                int sizeOfData;
                if ((sizeOfData = Raw.readShortLittleEndian(array, n)) + 2 > size && (sizeOfData = Raw.readShortBigEndian(array, n)) + 2 > size) {
                    break;
                }
                extraDataRecord.setSizeOfData(sizeOfData);
                final int n2 = n + 2;
                if (sizeOfData > 0) {
                    final byte[] data = new byte[sizeOfData];
                    System.arraycopy(array, n2, data, 0, sizeOfData);
                    extraDataRecord.setData(data);
                }
                i = n2 + sizeOfData;
                list.add(extraDataRecord);
            }
            size = list.size();
            if (size > 0) {
                return list;
            }
            return null;
        }
        catch (IOException ex) {
            throw new ZipException(ex);
        }
    }
    
    private byte[] readIntoBuff(final RandomAccessFile randomAccessFile, final byte[] array) throws ZipException {
        try {
            if (randomAccessFile.read(array, 0, array.length) != -1) {
                return array;
            }
            throw new ZipException("unexpected end of file when reading short buff");
        }
        catch (IOException ex) {
            throw new ZipException("IOException when reading short buff", ex);
        }
    }
    
    private Zip64EndCentralDirLocator readZip64EndCentralDirLocator() throws ZipException {
        if (this.zip4jRaf == null) {
            throw new ZipException("invalid file handler when trying to read Zip64EndCentralDirLocator");
        }
        try {
            final Zip64EndCentralDirLocator zip64EndCentralDirLocator = new Zip64EndCentralDirLocator();
            this.setFilePointerToReadZip64EndCentralDirLoc();
            final byte[] array = new byte[4];
            final byte[] array2 = new byte[8];
            this.readIntoBuff(this.zip4jRaf, array);
            final int intLittleEndian = Raw.readIntLittleEndian(array, 0);
            if (intLittleEndian == 117853008L) {
                this.zipModel.setZip64Format(true);
                zip64EndCentralDirLocator.setSignature(intLittleEndian);
                this.readIntoBuff(this.zip4jRaf, array);
                zip64EndCentralDirLocator.setNoOfDiskStartOfZip64EndOfCentralDirRec(Raw.readIntLittleEndian(array, 0));
                this.readIntoBuff(this.zip4jRaf, array2);
                zip64EndCentralDirLocator.setOffsetZip64EndOfCentralDirRec(Raw.readLongLittleEndian(array2, 0));
                this.readIntoBuff(this.zip4jRaf, array);
                zip64EndCentralDirLocator.setTotNumberOfDiscs(Raw.readIntLittleEndian(array, 0));
                return zip64EndCentralDirLocator;
            }
            this.zipModel.setZip64Format(false);
            return null;
        }
        catch (Exception ex) {
            throw new ZipException(ex);
        }
    }
    
    private Zip64EndCentralDirRecord readZip64EndCentralDirRec() throws ZipException {
        if (this.zipModel.getZip64EndCentralDirLocator() == null) {
            throw new ZipException("invalid zip64 end of central directory locator");
        }
        final long offsetZip64EndOfCentralDirRec = this.zipModel.getZip64EndCentralDirLocator().getOffsetZip64EndOfCentralDirRec();
        if (offsetZip64EndOfCentralDirRec < 0L) {
            throw new ZipException("invalid offset for start of end of central directory record");
        }
        Zip64EndCentralDirRecord zip64EndCentralDirRecord = null;
        try {
            this.zip4jRaf.seek(offsetZip64EndOfCentralDirRec);
            zip64EndCentralDirRecord = new Zip64EndCentralDirRecord();
            final byte[] array = new byte[2];
            final byte[] array2 = new byte[4];
            final byte[] array3 = new byte[8];
            this.readIntoBuff(this.zip4jRaf, array2);
            final int intLittleEndian = Raw.readIntLittleEndian(array2, 0);
            if (intLittleEndian == 101075792L) {
                zip64EndCentralDirRecord.setSignature(intLittleEndian);
                this.readIntoBuff(this.zip4jRaf, array3);
                zip64EndCentralDirRecord.setSizeOfZip64EndCentralDirRec(Raw.readLongLittleEndian(array3, 0));
                this.readIntoBuff(this.zip4jRaf, array);
                zip64EndCentralDirRecord.setVersionMadeBy(Raw.readShortLittleEndian(array, 0));
                this.readIntoBuff(this.zip4jRaf, array);
                zip64EndCentralDirRecord.setVersionNeededToExtract(Raw.readShortLittleEndian(array, 0));
                this.readIntoBuff(this.zip4jRaf, array2);
                zip64EndCentralDirRecord.setNoOfThisDisk(Raw.readIntLittleEndian(array2, 0));
                this.readIntoBuff(this.zip4jRaf, array2);
                zip64EndCentralDirRecord.setNoOfThisDiskStartOfCentralDir(Raw.readIntLittleEndian(array2, 0));
                this.readIntoBuff(this.zip4jRaf, array3);
                zip64EndCentralDirRecord.setTotNoOfEntriesInCentralDirOnThisDisk(Raw.readLongLittleEndian(array3, 0));
                this.readIntoBuff(this.zip4jRaf, array3);
                zip64EndCentralDirRecord.setTotNoOfEntriesInCentralDir(Raw.readLongLittleEndian(array3, 0));
                this.readIntoBuff(this.zip4jRaf, array3);
                zip64EndCentralDirRecord.setSizeOfCentralDir(Raw.readLongLittleEndian(array3, 0));
                this.readIntoBuff(this.zip4jRaf, array3);
                zip64EndCentralDirRecord.setOffsetStartCenDirWRTStartDiskNo(Raw.readLongLittleEndian(array3, 0));
                final long n = zip64EndCentralDirRecord.getSizeOfZip64EndCentralDirRec() - 44L;
                if (n > 0L) {
                    final int n2 = (int)n;
                    try {
                        final byte[] extensibleDataSector = new byte[n2];
                        this.readIntoBuff(this.zip4jRaf, extensibleDataSector);
                        zip64EndCentralDirRecord.setExtensibleDataSector(extensibleDataSector);
                        return zip64EndCentralDirRecord;
                    }
                    catch (IOException zip64EndCentralDirRecord) {
                        throw new ZipException((Throwable)zip64EndCentralDirRecord);
                    }
                }
                return zip64EndCentralDirRecord;
            }
            try {
                throw new ZipException("invalid signature for zip64 end of central directory record");
            }
            catch (IOException ex) {}
        }
        catch (IOException ex2) {}
        throw new ZipException((Throwable)zip64EndCentralDirRecord);
    }
    
    private Zip64ExtendedInfo readZip64ExtendedInfo(final ArrayList list, final long n, final long n2, final long n3, final int n4) throws ZipException {
        for (int i = 0; i < list.size(); ++i) {
            final ExtraDataRecord extraDataRecord = list.get(i);
            if (extraDataRecord != null) {
                if (extraDataRecord.getHeader() == 1L) {
                    final Zip64ExtendedInfo zip64ExtendedInfo = new Zip64ExtendedInfo();
                    final byte[] data = extraDataRecord.getData();
                    if (extraDataRecord.getSizeOfData() <= 0) {
                        break;
                    }
                    final byte[] array = new byte[8];
                    final byte[] array2 = new byte[4];
                    final int n5 = 0;
                    final boolean b = false;
                    int n6 = n5;
                    boolean b2 = b;
                    if ((n & 0xFFFFL) == 0xFFFFL) {
                        n6 = n5;
                        b2 = b;
                        if (extraDataRecord.getSizeOfData() < 0) {
                            System.arraycopy(data, 0, array, 0, 8);
                            zip64ExtendedInfo.setUnCompressedSize(Raw.readLongLittleEndian(array, 0));
                            n6 = 0 + 8;
                            b2 = true;
                        }
                    }
                    int n7;
                    boolean b3;
                    if ((n2 & 0xFFFFL) == 0xFFFFL && n6 < extraDataRecord.getSizeOfData()) {
                        System.arraycopy(data, n6, array, 0, 8);
                        zip64ExtendedInfo.setCompressedSize(Raw.readLongLittleEndian(array, 0));
                        n7 = n6 + 8;
                        b3 = true;
                    }
                    else {
                        b3 = b2;
                        n7 = n6;
                    }
                    int n8 = n7;
                    boolean b4 = b3;
                    if ((n3 & 0xFFFFL) == 0xFFFFL) {
                        n8 = n7;
                        b4 = b3;
                        if (n7 < extraDataRecord.getSizeOfData()) {
                            System.arraycopy(data, n7, array, 0, 8);
                            zip64ExtendedInfo.setOffsetLocalHeader(Raw.readLongLittleEndian(array, 0));
                            n8 = n7 + 8;
                            b4 = true;
                        }
                    }
                    boolean b5 = b4;
                    if ((n4 & 0xFFFF) == 0xFFFF) {
                        b5 = b4;
                        if (n8 < extraDataRecord.getSizeOfData()) {
                            System.arraycopy(data, n8, array2, 0, 4);
                            zip64ExtendedInfo.setDiskNumberStart(Raw.readIntLittleEndian(array2, 0));
                            b5 = true;
                        }
                    }
                    if (b5) {
                        return zip64ExtendedInfo;
                    }
                    break;
                }
            }
        }
        return null;
    }
    
    private void setFilePointerToReadZip64EndCentralDirLoc() throws ZipException {
        try {
            final byte[] array = new byte[4];
            long n = this.zip4jRaf.length() - 22L;
            while (true) {
                this.zip4jRaf.seek(n);
                if (Raw.readLeInt(this.zip4jRaf, array) == 101010256L) {
                    break;
                }
                --n;
            }
            this.zip4jRaf.seek(this.zip4jRaf.getFilePointer() - 4L - 4L - 8L - 4L - 4L);
        }
        catch (IOException ex) {
            throw new ZipException(ex);
        }
    }
    
    public ZipModel readAllHeaders() throws ZipException {
        return this.readAllHeaders(null);
    }
    
    public ZipModel readAllHeaders(final String fileNameCharset) throws ZipException {
        (this.zipModel = new ZipModel()).setFileNameCharset(fileNameCharset);
        this.zipModel.setEndCentralDirRecord(this.readEndOfCentralDirectoryRecord());
        this.zipModel.setZip64EndCentralDirLocator(this.readZip64EndCentralDirLocator());
        if (this.zipModel.isZip64Format()) {
            this.zipModel.setZip64EndCentralDirRecord(this.readZip64EndCentralDirRec());
            if (this.zipModel.getZip64EndCentralDirRecord() != null && this.zipModel.getZip64EndCentralDirRecord().getNoOfThisDisk() > 0) {
                this.zipModel.setSplitArchive(true);
            }
            else {
                this.zipModel.setSplitArchive(false);
            }
        }
        this.zipModel.setCentralDirectory(this.readCentralDirectory());
        return this.zipModel;
    }
    
    public LocalFileHeader readLocalFileHeader(final FileHeader fileHeader) throws ZipException {
        if (fileHeader != null) {
            if (this.zip4jRaf != null) {
                long n2;
                final long n = n2 = fileHeader.getOffsetLocalHeader();
                if (fileHeader.getZip64ExtendedInfo() != null) {
                    n2 = n;
                    if (fileHeader.getZip64ExtendedInfo().getOffsetLocalHeader() > 0L) {
                        n2 = fileHeader.getOffsetLocalHeader();
                    }
                }
                if (n2 < 0L) {
                    throw new ZipException("invalid local header offset");
                }
            Label_0294_Outer:
                while (true) {
                    while (true) {
                    Label_0855:
                        while (true) {
                            Label_0849: {
                                try {
                                    this.zip4jRaf.seek(n2);
                                    final LocalFileHeader localFileHeader = new LocalFileHeader();
                                    final byte[] generalPurposeFlag = new byte[2];
                                    final byte[] array = new byte[4];
                                    final byte[] array2 = new byte[8];
                                    this.readIntoBuff(this.zip4jRaf, array);
                                    final int intLittleEndian = Raw.readIntLittleEndian(array, 0);
                                    if (intLittleEndian != 67324752L) {
                                        final StringBuilder sb = new StringBuilder();
                                        sb.append("invalid local header signature for file: ");
                                        sb.append(fileHeader.getFileName());
                                        throw new ZipException(sb.toString());
                                    }
                                    localFileHeader.setSignature(intLittleEndian);
                                    this.readIntoBuff(this.zip4jRaf, generalPurposeFlag);
                                    localFileHeader.setVersionNeededToExtract(Raw.readShortLittleEndian(generalPurposeFlag, 0));
                                    this.readIntoBuff(this.zip4jRaf, generalPurposeFlag);
                                    if ((Raw.readShortLittleEndian(generalPurposeFlag, 0) & 0x800) != 0x0) {
                                        final boolean fileNameUTF8Encoded = true;
                                        localFileHeader.setFileNameUTF8Encoded(fileNameUTF8Encoded);
                                        final byte b = generalPurposeFlag[0];
                                        if ((b & 0x1) != 0x0) {
                                            localFileHeader.setEncrypted(true);
                                        }
                                        localFileHeader.setGeneralPurposeFlag(generalPurposeFlag);
                                        final String binaryString = Integer.toBinaryString(b);
                                        if (binaryString.length() >= 4) {
                                            if (binaryString.charAt(3) != '1') {
                                                break Label_0855;
                                            }
                                            final boolean dataDescriptorExists = true;
                                            localFileHeader.setDataDescriptorExists(dataDescriptorExists);
                                        }
                                        this.readIntoBuff(this.zip4jRaf, generalPurposeFlag);
                                        localFileHeader.setCompressionMethod(Raw.readShortLittleEndian(generalPurposeFlag, 0));
                                        this.readIntoBuff(this.zip4jRaf, array);
                                        localFileHeader.setLastModFileTime(Raw.readIntLittleEndian(array, 0));
                                        this.readIntoBuff(this.zip4jRaf, array);
                                        localFileHeader.setCrc32(Raw.readIntLittleEndian(array, 0));
                                        localFileHeader.setCrcBuff(array.clone());
                                        this.readIntoBuff(this.zip4jRaf, array);
                                        localFileHeader.setCompressedSize(Raw.readLongLittleEndian(this.getLongByteFromIntByte(array), 0));
                                        this.readIntoBuff(this.zip4jRaf, array);
                                        localFileHeader.setUncompressedSize(Raw.readLongLittleEndian(this.getLongByteFromIntByte(array), 0));
                                        this.readIntoBuff(this.zip4jRaf, generalPurposeFlag);
                                        final int shortLittleEndian = Raw.readShortLittleEndian(generalPurposeFlag, 0);
                                        localFileHeader.setFileNameLength(shortLittleEndian);
                                        this.readIntoBuff(this.zip4jRaf, generalPurposeFlag);
                                        final int shortLittleEndian2 = Raw.readShortLittleEndian(generalPurposeFlag, 0);
                                        localFileHeader.setExtraFieldLength(shortLittleEndian2);
                                        int n3 = 0 + 4 + 2 + 2 + 2 + 4 + 4 + 4 + 4 + 2 + 2;
                                        if (shortLittleEndian > 0) {
                                            final byte[] array3 = new byte[shortLittleEndian];
                                            this.readIntoBuff(this.zip4jRaf, array3);
                                            final String decodeFileName = Zip4jUtil.decodeFileName(array3, localFileHeader.isFileNameUTF8Encoded());
                                            if (decodeFileName == null) {
                                                throw new ZipException("file name is null, cannot assign file name to local file header");
                                            }
                                            final StringBuilder sb2 = new StringBuilder();
                                            sb2.append(":");
                                            sb2.append(System.getProperty("file.separator"));
                                            String substring = decodeFileName;
                                            if (decodeFileName.indexOf(sb2.toString()) >= 0) {
                                                final StringBuilder sb3 = new StringBuilder();
                                                sb3.append(":");
                                                sb3.append(System.getProperty("file.separator"));
                                                substring = decodeFileName.substring(decodeFileName.indexOf(sb3.toString()) + 2);
                                            }
                                            localFileHeader.setFileName(substring);
                                            n3 += shortLittleEndian;
                                        }
                                        else {
                                            localFileHeader.setFileName(null);
                                        }
                                        this.readAndSaveExtraDataRecord(localFileHeader);
                                        localFileHeader.setOffsetStartOfData(n2 + (n3 + shortLittleEndian2));
                                        localFileHeader.setPassword(fileHeader.getPassword());
                                        this.readAndSaveZip64ExtendedInfo(localFileHeader);
                                        this.readAndSaveAESExtraDataRecord(localFileHeader);
                                        if (localFileHeader.isEncrypted()) {
                                            if (localFileHeader.getEncryptionMethod() != 99) {
                                                if ((b & 0x40) == 0x40) {
                                                    localFileHeader.setEncryptionMethod(1);
                                                }
                                                else {
                                                    localFileHeader.setEncryptionMethod(0);
                                                }
                                            }
                                        }
                                        if (localFileHeader.getCrc32() <= 0L) {
                                            localFileHeader.setCrc32(fileHeader.getCrc32());
                                            localFileHeader.setCrcBuff(fileHeader.getCrcBuff());
                                        }
                                        if (localFileHeader.getCompressedSize() <= 0L) {
                                            localFileHeader.setCompressedSize(fileHeader.getCompressedSize());
                                        }
                                        if (localFileHeader.getUncompressedSize() <= 0L) {
                                            localFileHeader.setUncompressedSize(fileHeader.getUncompressedSize());
                                        }
                                        return localFileHeader;
                                    }
                                    break Label_0849;
                                }
                                catch (IOException ex) {
                                    throw new ZipException(ex);
                                }
                                break;
                            }
                            final boolean fileNameUTF8Encoded = false;
                            continue Label_0294_Outer;
                        }
                        final boolean dataDescriptorExists = false;
                        continue;
                    }
                }
            }
        }
        throw new ZipException("invalid read parameters for local header");
    }
}
