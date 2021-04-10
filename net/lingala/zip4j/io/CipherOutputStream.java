package net.lingala.zip4j.io;

import java.util.zip.*;
import net.lingala.zip4j.exception.*;
import net.lingala.zip4j.util.*;
import java.io.*;
import net.lingala.zip4j.crypto.*;
import net.lingala.zip4j.model.*;
import java.util.*;
import net.lingala.zip4j.core.*;

public class CipherOutputStream extends BaseOutputStream
{
    private long bytesWrittenForThisFile;
    protected CRC32 crc;
    private IEncrypter encrypter;
    protected FileHeader fileHeader;
    protected LocalFileHeader localFileHeader;
    protected OutputStream outputStream;
    private byte[] pendingBuffer;
    private int pendingBufferLength;
    private File sourceFile;
    private long totalBytesRead;
    private long totalBytesWritten;
    protected ZipModel zipModel;
    protected ZipParameters zipParameters;
    
    public CipherOutputStream(final OutputStream outputStream, final ZipModel zipModel) {
        this.outputStream = outputStream;
        this.initZipModel(zipModel);
        this.crc = new CRC32();
        this.totalBytesWritten = 0L;
        this.bytesWrittenForThisFile = 0L;
        this.pendingBuffer = new byte[16];
        this.pendingBufferLength = 0;
        this.totalBytesRead = 0L;
    }
    
    private void createFileHeader() throws ZipException {
        (this.fileHeader = new FileHeader()).setSignature(33639248);
        this.fileHeader.setVersionMadeBy(20);
        this.fileHeader.setVersionNeededToExtract(20);
        if (this.zipParameters.isEncryptFiles() && this.zipParameters.getEncryptionMethod() == 99) {
            this.fileHeader.setCompressionMethod(99);
            this.fileHeader.setAesExtraDataRecord(this.generateAESExtraDataRecord(this.zipParameters));
        }
        else {
            this.fileHeader.setCompressionMethod(this.zipParameters.getCompressionMethod());
        }
        if (this.zipParameters.isEncryptFiles()) {
            this.fileHeader.setEncrypted(true);
            this.fileHeader.setEncryptionMethod(this.zipParameters.getEncryptionMethod());
        }
        String fileName;
        if (this.zipParameters.isSourceExternalStream()) {
            this.fileHeader.setLastModFileTime((int)Zip4jUtil.javaToDosTime(System.currentTimeMillis()));
            if (!Zip4jUtil.isStringNotNullAndNotEmpty(this.zipParameters.getFileNameInZip())) {
                throw new ZipException("fileNameInZip is null or empty");
            }
            fileName = this.zipParameters.getFileNameInZip();
        }
        else {
            this.fileHeader.setLastModFileTime((int)Zip4jUtil.javaToDosTime(Zip4jUtil.getLastModifiedFileTime(this.sourceFile, this.zipParameters.getTimeZone())));
            this.fileHeader.setUncompressedSize(this.sourceFile.length());
            fileName = Zip4jUtil.getRelativeFileName(this.sourceFile.getAbsolutePath(), this.zipParameters.getRootFolderInZip(), this.zipParameters.getDefaultFolderPath());
        }
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)) {
            throw new ZipException("fileName is null or empty. unable to create file header");
        }
        this.fileHeader.setFileName(fileName);
        if (Zip4jUtil.isStringNotNullAndNotEmpty(this.zipModel.getFileNameCharset())) {
            this.fileHeader.setFileNameLength(Zip4jUtil.getEncodedStringLength(fileName, this.zipModel.getFileNameCharset()));
        }
        else {
            this.fileHeader.setFileNameLength(Zip4jUtil.getEncodedStringLength(fileName));
        }
        if (this.outputStream instanceof SplitOutputStream) {
            this.fileHeader.setDiskNumberStart(((SplitOutputStream)this.outputStream).getCurrSplitFileCounter());
        }
        else {
            this.fileHeader.setDiskNumberStart(0);
        }
        int fileAttributes = 0;
        if (!this.zipParameters.isSourceExternalStream()) {
            fileAttributes = this.getFileAttributes(this.sourceFile);
        }
        this.fileHeader.setExternalFileAttr(new byte[] { (byte)fileAttributes, 0, 0, 0 });
        if (this.zipParameters.isSourceExternalStream()) {
            this.fileHeader.setDirectory(fileName.endsWith("/") || fileName.endsWith("\\"));
        }
        else {
            this.fileHeader.setDirectory(this.sourceFile.isDirectory());
        }
        if (this.fileHeader.isDirectory()) {
            this.fileHeader.setCompressedSize(0L);
            this.fileHeader.setUncompressedSize(0L);
        }
        else if (!this.zipParameters.isSourceExternalStream()) {
            final long fileLengh = Zip4jUtil.getFileLengh(this.sourceFile);
            if (this.zipParameters.getCompressionMethod() == 0) {
                if (this.zipParameters.getEncryptionMethod() == 0) {
                    this.fileHeader.setCompressedSize(fileLengh + 12L);
                }
                else if (this.zipParameters.getEncryptionMethod() == 99) {
                    final int aesKeyStrength = this.zipParameters.getAesKeyStrength();
                    int n;
                    if (aesKeyStrength != 1) {
                        if (aesKeyStrength != 3) {
                            throw new ZipException("invalid aes key strength, cannot determine key sizes");
                        }
                        n = 16;
                    }
                    else {
                        n = 8;
                    }
                    this.fileHeader.setCompressedSize(fileLengh + n + 10L + 2L);
                }
                else {
                    this.fileHeader.setCompressedSize(0L);
                }
            }
            else {
                this.fileHeader.setCompressedSize(0L);
            }
            this.fileHeader.setUncompressedSize(fileLengh);
        }
        if (this.zipParameters.isEncryptFiles() && this.zipParameters.getEncryptionMethod() == 0) {
            this.fileHeader.setCrc32(this.zipParameters.getSourceFileCRC());
        }
        final byte[] generalPurposeFlag = { Raw.bitArrayToByte(this.generateGeneralPurposeBitArray(this.fileHeader.isEncrypted(), this.zipParameters.getCompressionMethod())), 0 };
        final boolean stringNotNullAndNotEmpty = Zip4jUtil.isStringNotNullAndNotEmpty(this.zipModel.getFileNameCharset());
        if ((stringNotNullAndNotEmpty && this.zipModel.getFileNameCharset().equalsIgnoreCase("UTF8")) || (!stringNotNullAndNotEmpty && Zip4jUtil.detectCharSet(this.fileHeader.getFileName()).equals("UTF8"))) {
            generalPurposeFlag[1] = 8;
        }
        else {
            generalPurposeFlag[1] = 0;
        }
        this.fileHeader.setGeneralPurposeFlag(generalPurposeFlag);
    }
    
    private void createLocalFileHeader() throws ZipException {
        if (this.fileHeader == null) {
            throw new ZipException("file header is null, cannot create local file header");
        }
        (this.localFileHeader = new LocalFileHeader()).setSignature(67324752);
        this.localFileHeader.setVersionNeededToExtract(this.fileHeader.getVersionNeededToExtract());
        this.localFileHeader.setCompressionMethod(this.fileHeader.getCompressionMethod());
        this.localFileHeader.setLastModFileTime(this.fileHeader.getLastModFileTime());
        this.localFileHeader.setUncompressedSize(this.fileHeader.getUncompressedSize());
        this.localFileHeader.setFileNameLength(this.fileHeader.getFileNameLength());
        this.localFileHeader.setFileName(this.fileHeader.getFileName());
        this.localFileHeader.setEncrypted(this.fileHeader.isEncrypted());
        this.localFileHeader.setEncryptionMethod(this.fileHeader.getEncryptionMethod());
        this.localFileHeader.setAesExtraDataRecord(this.fileHeader.getAesExtraDataRecord());
        this.localFileHeader.setCrc32(this.fileHeader.getCrc32());
        this.localFileHeader.setCompressedSize(this.fileHeader.getCompressedSize());
        this.localFileHeader.setGeneralPurposeFlag(this.fileHeader.getGeneralPurposeFlag().clone());
    }
    
    private void encryptAndWrite(final byte[] array, final int n, final int n2) throws IOException {
        if (this.encrypter != null) {
            try {
                this.encrypter.encryptData(array, n, n2);
            }
            catch (ZipException ex) {
                throw new IOException(ex.getMessage());
            }
        }
        this.outputStream.write(array, n, n2);
        this.totalBytesWritten += n2;
        this.bytesWrittenForThisFile += n2;
    }
    
    private AESExtraDataRecord generateAESExtraDataRecord(final ZipParameters zipParameters) throws ZipException {
        if (zipParameters == null) {
            throw new ZipException("zip parameters are null, cannot generate AES Extra Data record");
        }
        final AESExtraDataRecord aesExtraDataRecord = new AESExtraDataRecord();
        aesExtraDataRecord.setSignature(39169L);
        aesExtraDataRecord.setDataSize(7);
        aesExtraDataRecord.setVendorID("AE");
        aesExtraDataRecord.setVersionNumber(2);
        if (zipParameters.getAesKeyStrength() == 1) {
            aesExtraDataRecord.setAesStrength(1);
        }
        else {
            if (zipParameters.getAesKeyStrength() != 3) {
                throw new ZipException("invalid AES key strength, cannot generate AES Extra data record");
            }
            aesExtraDataRecord.setAesStrength(3);
        }
        aesExtraDataRecord.setCompressionMethod(zipParameters.getCompressionMethod());
        return aesExtraDataRecord;
    }
    
    private int[] generateGeneralPurposeBitArray(final boolean b, final int n) {
        final int[] array = new int[8];
        if (b) {
            array[0] = 1;
        }
        else {
            array[0] = 0;
        }
        if (n != 8) {
            array[2] = (array[1] = 0);
        }
        array[3] = 1;
        return array;
    }
    
    private int getFileAttributes(final File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null, cannot get file attributes");
        }
        if (!file.exists()) {
            return 0;
        }
        if (file.isDirectory()) {
            if (file.isHidden()) {
                return 18;
            }
            return 16;
        }
        else {
            if (!file.canWrite() && file.isHidden()) {
                return 3;
            }
            if (!file.canWrite()) {
                return 1;
            }
            if (file.isHidden()) {
                return 2;
            }
            return 0;
        }
    }
    
    private void initEncrypter() throws ZipException {
        if (!this.zipParameters.isEncryptFiles()) {
            this.encrypter = null;
            return;
        }
        final int encryptionMethod = this.zipParameters.getEncryptionMethod();
        if (encryptionMethod == 0) {
            this.encrypter = new StandardEncrypter(this.zipParameters.getPassword(), (this.localFileHeader.getLastModFileTime() & 0xFFFF) << 16);
            return;
        }
        if (encryptionMethod != 99) {
            throw new ZipException("invalid encprytion method");
        }
        this.encrypter = new AESEncrpyter(this.zipParameters.getPassword(), this.zipParameters.getAesKeyStrength());
    }
    
    private void initZipModel(final ZipModel zipModel) {
        if (zipModel == null) {
            this.zipModel = new ZipModel();
        }
        else {
            this.zipModel = zipModel;
        }
        if (this.zipModel.getEndCentralDirRecord() == null) {
            this.zipModel.setEndCentralDirRecord(new EndCentralDirRecord());
        }
        if (this.zipModel.getCentralDirectory() == null) {
            this.zipModel.setCentralDirectory(new CentralDirectory());
        }
        if (this.zipModel.getCentralDirectory().getFileHeaders() == null) {
            this.zipModel.getCentralDirectory().setFileHeaders(new ArrayList());
        }
        if (this.zipModel.getLocalFileHeaderList() == null) {
            this.zipModel.setLocalFileHeaderList(new ArrayList());
        }
        if (this.outputStream instanceof SplitOutputStream && ((SplitOutputStream)this.outputStream).isSplitZipFile()) {
            this.zipModel.setSplitArchive(true);
            this.zipModel.setSplitLength(((SplitOutputStream)this.outputStream).getSplitLength());
        }
        this.zipModel.getEndCentralDirRecord().setSignature(101010256L);
    }
    
    @Override
    public void close() throws IOException {
        if (this.outputStream != null) {
            this.outputStream.close();
        }
    }
    
    public void closeEntry() throws IOException, ZipException {
        if (this.pendingBufferLength != 0) {
            this.encryptAndWrite(this.pendingBuffer, 0, this.pendingBufferLength);
            this.pendingBufferLength = 0;
        }
        if (this.zipParameters.isEncryptFiles() && this.zipParameters.getEncryptionMethod() == 99) {
            if (!(this.encrypter instanceof AESEncrpyter)) {
                throw new ZipException("invalid encrypter for AES encrypted file");
            }
            this.outputStream.write(((AESEncrpyter)this.encrypter).getFinalMac());
            this.bytesWrittenForThisFile += 10L;
            this.totalBytesWritten += 10L;
        }
        this.fileHeader.setCompressedSize(this.bytesWrittenForThisFile);
        this.localFileHeader.setCompressedSize(this.bytesWrittenForThisFile);
        if (this.zipParameters.isSourceExternalStream()) {
            this.fileHeader.setUncompressedSize(this.totalBytesRead);
            if (this.localFileHeader.getUncompressedSize() != this.totalBytesRead) {
                this.localFileHeader.setUncompressedSize(this.totalBytesRead);
            }
        }
        long value;
        final long n = value = this.crc.getValue();
        if (this.fileHeader.isEncrypted()) {
            value = n;
            if (this.fileHeader.getEncryptionMethod() == 99) {
                value = 0L;
            }
        }
        if (this.zipParameters.isEncryptFiles() && this.zipParameters.getEncryptionMethod() == 99) {
            this.fileHeader.setCrc32(0L);
            this.localFileHeader.setCrc32(0L);
        }
        else {
            this.fileHeader.setCrc32(value);
            this.localFileHeader.setCrc32(value);
        }
        this.zipModel.getLocalFileHeaderList().add(this.localFileHeader);
        this.zipModel.getCentralDirectory().getFileHeaders().add(this.fileHeader);
        this.totalBytesWritten += new HeaderWriter().writeExtendedLocalHeader(this.localFileHeader, this.outputStream);
        this.crc.reset();
        this.bytesWrittenForThisFile = 0L;
        this.encrypter = null;
        this.totalBytesRead = 0L;
    }
    
    public void decrementCompressedFileSize(final int n) {
        if (n <= 0) {
            return;
        }
        if (n <= this.bytesWrittenForThisFile) {
            this.bytesWrittenForThisFile -= n;
        }
    }
    
    public void finish() throws IOException, ZipException {
        this.zipModel.getEndCentralDirRecord().setOffsetOfStartOfCentralDir(this.totalBytesWritten);
        new HeaderWriter().finalizeZipFile(this.zipModel, this.outputStream);
    }
    
    public File getSourceFile() {
        return this.sourceFile;
    }
    
    public void putNextEntry(final File sourceFile, final ZipParameters zipParameters) throws ZipException {
        if (!zipParameters.isSourceExternalStream() && sourceFile == null) {
            throw new ZipException("input file is null");
        }
        if (!zipParameters.isSourceExternalStream() && !Zip4jUtil.checkFileExists(sourceFile)) {
            throw new ZipException("input file does not exist");
        }
        ZipParameters zipParameters2;
        if ((zipParameters2 = zipParameters) == null) {
            zipParameters2 = new ZipParameters();
        }
        try {
            this.sourceFile = sourceFile;
            this.zipParameters = (ZipParameters)zipParameters2.clone();
            if (!zipParameters2.isSourceExternalStream()) {
                if (this.sourceFile.isDirectory()) {
                    this.zipParameters.setEncryptFiles(false);
                    this.zipParameters.setEncryptionMethod(-1);
                    this.zipParameters.setCompressionMethod(0);
                }
            }
            else {
                if (!Zip4jUtil.isStringNotNullAndNotEmpty(this.zipParameters.getFileNameInZip())) {
                    throw new ZipException("file name is empty for external stream");
                }
                if (this.zipParameters.getFileNameInZip().endsWith("/") || this.zipParameters.getFileNameInZip().endsWith("\\")) {
                    this.zipParameters.setEncryptFiles(false);
                    this.zipParameters.setEncryptionMethod(-1);
                    this.zipParameters.setCompressionMethod(0);
                }
            }
            this.createFileHeader();
            this.createLocalFileHeader();
            if (this.zipModel.isSplitArchive() && (this.zipModel.getCentralDirectory() == null || this.zipModel.getCentralDirectory().getFileHeaders() == null || this.zipModel.getCentralDirectory().getFileHeaders().size() == 0)) {
                final byte[] array = new byte[4];
                Raw.writeIntLittleEndian(array, 0, 134695760);
                this.outputStream.write(array);
                this.totalBytesWritten += 4L;
            }
            if (this.outputStream instanceof SplitOutputStream) {
                if (this.totalBytesWritten == 4L) {
                    this.fileHeader.setOffsetLocalHeader(4L);
                }
                else {
                    this.fileHeader.setOffsetLocalHeader(((SplitOutputStream)this.outputStream).getFilePointer());
                }
            }
            else if (this.totalBytesWritten == 4L) {
                this.fileHeader.setOffsetLocalHeader(4L);
            }
            else {
                this.fileHeader.setOffsetLocalHeader(this.totalBytesWritten);
            }
            this.totalBytesWritten += new HeaderWriter().writeLocalFileHeader(this.zipModel, this.localFileHeader, this.outputStream);
            if (this.zipParameters.isEncryptFiles()) {
                this.initEncrypter();
                if (this.encrypter != null) {
                    if (zipParameters2.getEncryptionMethod() == 0) {
                        final byte[] headerBytes = ((StandardEncrypter)this.encrypter).getHeaderBytes();
                        this.outputStream.write(headerBytes);
                        this.totalBytesWritten += headerBytes.length;
                        this.bytesWrittenForThisFile += headerBytes.length;
                    }
                    else if (zipParameters2.getEncryptionMethod() == 99) {
                        final byte[] saltBytes = ((AESEncrpyter)this.encrypter).getSaltBytes();
                        final byte[] derivedPasswordVerifier = ((AESEncrpyter)this.encrypter).getDerivedPasswordVerifier();
                        this.outputStream.write(saltBytes);
                        this.outputStream.write(derivedPasswordVerifier);
                        this.totalBytesWritten += saltBytes.length + derivedPasswordVerifier.length;
                        this.bytesWrittenForThisFile += saltBytes.length + derivedPasswordVerifier.length;
                    }
                }
            }
            this.crc.reset();
        }
        catch (Exception ex) {
            throw new ZipException(ex);
        }
        catch (ZipException ex2) {
            throw ex2;
        }
        catch (CloneNotSupportedException ex3) {
            throw new ZipException(ex3);
        }
    }
    
    public void setSourceFile(final File sourceFile) {
        this.sourceFile = sourceFile;
    }
    
    protected void updateTotalBytesRead(final int n) {
        if (n > 0) {
            this.totalBytesRead += n;
        }
    }
    
    @Override
    public void write(final int n) throws IOException {
        this.write(new byte[] { (byte)n }, 0, 1);
    }
    
    @Override
    public void write(final byte[] array) throws IOException {
        if (array == null) {
            throw new NullPointerException();
        }
        if (array.length == 0) {
            return;
        }
        this.write(array, 0, array.length);
    }
    
    @Override
    public void write(final byte[] array, final int n, final int n2) throws IOException {
        if (n2 == 0) {
            return;
        }
        int n3 = n;
        int n4 = n2;
        if (this.zipParameters.isEncryptFiles()) {
            n3 = n;
            n4 = n2;
            if (this.zipParameters.getEncryptionMethod() == 99) {
                int n5 = n;
                int n6 = n2;
                if (this.pendingBufferLength != 0) {
                    if (n2 < 16 - this.pendingBufferLength) {
                        System.arraycopy(array, n, this.pendingBuffer, this.pendingBufferLength, n2);
                        this.pendingBufferLength += n2;
                        return;
                    }
                    System.arraycopy(array, n, this.pendingBuffer, this.pendingBufferLength, 16 - this.pendingBufferLength);
                    this.encryptAndWrite(this.pendingBuffer, 0, this.pendingBuffer.length);
                    n5 = 16 - this.pendingBufferLength;
                    n6 = n2 - n5;
                    this.pendingBufferLength = 0;
                }
                n3 = n5;
                if ((n4 = n6) != 0) {
                    n3 = n5;
                    n4 = n6;
                    if (n6 % 16 != 0) {
                        System.arraycopy(array, n6 + n5 - n6 % 16, this.pendingBuffer, 0, n6 % 16);
                        this.pendingBufferLength = n6 % 16;
                        n4 = n6 - this.pendingBufferLength;
                        n3 = n5;
                    }
                }
            }
        }
        if (n4 != 0) {
            this.encryptAndWrite(array, n3, n4);
        }
    }
}
