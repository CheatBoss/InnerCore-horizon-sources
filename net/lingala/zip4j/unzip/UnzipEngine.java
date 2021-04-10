package net.lingala.zip4j.unzip;

import java.util.zip.*;
import net.lingala.zip4j.exception.*;
import net.lingala.zip4j.util.*;
import java.io.*;
import net.lingala.zip4j.crypto.*;
import java.util.*;
import net.lingala.zip4j.io.*;
import net.lingala.zip4j.progress.*;
import net.lingala.zip4j.model.*;

public class UnzipEngine
{
    private CRC32 crc;
    private int currSplitFileCounter;
    private IDecrypter decrypter;
    private FileHeader fileHeader;
    private LocalFileHeader localFileHeader;
    private ZipModel zipModel;
    
    public UnzipEngine(final ZipModel zipModel, final FileHeader fileHeader) throws ZipException {
        this.currSplitFileCounter = 0;
        if (zipModel != null && fileHeader != null) {
            this.zipModel = zipModel;
            this.fileHeader = fileHeader;
            this.crc = new CRC32();
            return;
        }
        throw new ZipException("Invalid parameters passed to StoreUnzip. One or more of the parameters were null");
    }
    
    private int calculateAESSaltLength(final AESExtraDataRecord aesExtraDataRecord) throws ZipException {
        if (aesExtraDataRecord == null) {
            throw new ZipException("unable to determine salt length: AESExtraDataRecord is null");
        }
        switch (aesExtraDataRecord.getAesStrength()) {
            default: {
                throw new ZipException("unable to determine salt length: invalid aes key strength");
            }
            case 3: {
                return 16;
            }
            case 2: {
                return 12;
            }
            case 1: {
                return 8;
            }
        }
    }
    
    private boolean checkLocalHeader() throws ZipException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore          4
        //     3: aconst_null    
        //     4: astore_3       
        //     5: aload_0        
        //     6: invokespecial   net/lingala/zip4j/unzip/UnzipEngine.checkSplitFile:()Ljava/io/RandomAccessFile;
        //     9: astore          6
        //    11: aload           6
        //    13: astore          5
        //    15: aload           6
        //    17: ifnonnull       52
        //    20: aload           6
        //    22: astore_3       
        //    23: aload           6
        //    25: astore          4
        //    27: new             Ljava/io/RandomAccessFile;
        //    30: dup            
        //    31: new             Ljava/io/File;
        //    34: dup            
        //    35: aload_0        
        //    36: getfield        net/lingala/zip4j/unzip/UnzipEngine.zipModel:Lnet/lingala/zip4j/model/ZipModel;
        //    39: invokevirtual   net/lingala/zip4j/model/ZipModel.getZipFile:()Ljava/lang/String;
        //    42: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //    45: ldc             "r"
        //    47: invokespecial   java/io/RandomAccessFile.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    50: astore          5
        //    52: aload           5
        //    54: astore_3       
        //    55: aload           5
        //    57: astore          4
        //    59: aload_0        
        //    60: new             Lnet/lingala/zip4j/core/HeaderReader;
        //    63: dup            
        //    64: aload           5
        //    66: invokespecial   net/lingala/zip4j/core/HeaderReader.<init>:(Ljava/io/RandomAccessFile;)V
        //    69: aload_0        
        //    70: getfield        net/lingala/zip4j/unzip/UnzipEngine.fileHeader:Lnet/lingala/zip4j/model/FileHeader;
        //    73: invokevirtual   net/lingala/zip4j/core/HeaderReader.readLocalFileHeader:(Lnet/lingala/zip4j/model/FileHeader;)Lnet/lingala/zip4j/model/LocalFileHeader;
        //    76: putfield        net/lingala/zip4j/unzip/UnzipEngine.localFileHeader:Lnet/lingala/zip4j/model/LocalFileHeader;
        //    79: aload           5
        //    81: astore_3       
        //    82: aload           5
        //    84: astore          4
        //    86: aload_0        
        //    87: getfield        net/lingala/zip4j/unzip/UnzipEngine.localFileHeader:Lnet/lingala/zip4j/model/LocalFileHeader;
        //    90: ifnonnull       110
        //    93: aload           5
        //    95: astore_3       
        //    96: aload           5
        //    98: astore          4
        //   100: new             Lnet/lingala/zip4j/exception/ZipException;
        //   103: dup            
        //   104: ldc             "error reading local file header. Is this a valid zip file?"
        //   106: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/String;)V
        //   109: athrow         
        //   110: aload           5
        //   112: astore_3       
        //   113: aload           5
        //   115: astore          4
        //   117: aload_0        
        //   118: getfield        net/lingala/zip4j/unzip/UnzipEngine.localFileHeader:Lnet/lingala/zip4j/model/LocalFileHeader;
        //   121: invokevirtual   net/lingala/zip4j/model/LocalFileHeader.getCompressionMethod:()I
        //   124: istore_1       
        //   125: aload           5
        //   127: astore_3       
        //   128: aload           5
        //   130: astore          4
        //   132: aload_0        
        //   133: getfield        net/lingala/zip4j/unzip/UnzipEngine.fileHeader:Lnet/lingala/zip4j/model/FileHeader;
        //   136: invokevirtual   net/lingala/zip4j/model/FileHeader.getCompressionMethod:()I
        //   139: istore_2       
        //   140: iload_1        
        //   141: iload_2        
        //   142: if_icmpeq       165
        //   145: aload           5
        //   147: ifnull          163
        //   150: aload           5
        //   152: invokevirtual   java/io/RandomAccessFile.close:()V
        //   155: iconst_0       
        //   156: ireturn        
        //   157: astore_3       
        //   158: iconst_0       
        //   159: ireturn        
        //   160: astore_3       
        //   161: iconst_0       
        //   162: ireturn        
        //   163: iconst_0       
        //   164: ireturn        
        //   165: aload           5
        //   167: ifnull          183
        //   170: aload           5
        //   172: invokevirtual   java/io/RandomAccessFile.close:()V
        //   175: iconst_1       
        //   176: ireturn        
        //   177: astore_3       
        //   178: iconst_1       
        //   179: ireturn        
        //   180: astore_3       
        //   181: iconst_1       
        //   182: ireturn        
        //   183: iconst_1       
        //   184: ireturn        
        //   185: astore          4
        //   187: goto            205
        //   190: astore          5
        //   192: aload           4
        //   194: astore_3       
        //   195: new             Lnet/lingala/zip4j/exception/ZipException;
        //   198: dup            
        //   199: aload           5
        //   201: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/Throwable;)V
        //   204: athrow         
        //   205: aload_3        
        //   206: ifnull          224
        //   209: aload_3        
        //   210: invokevirtual   java/io/RandomAccessFile.close:()V
        //   213: goto            224
        //   216: astore_3       
        //   217: goto            224
        //   220: astore_3       
        //   221: goto            213
        //   224: aload           4
        //   226: athrow         
        //    Exceptions:
        //  throws net.lingala.zip4j.exception.ZipException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                           
        //  -----  -----  -----  -----  -------------------------------
        //  5      11     190    205    Ljava/io/FileNotFoundException;
        //  5      11     185    227    Any
        //  27     52     190    205    Ljava/io/FileNotFoundException;
        //  27     52     185    227    Any
        //  59     79     190    205    Ljava/io/FileNotFoundException;
        //  59     79     185    227    Any
        //  86     93     190    205    Ljava/io/FileNotFoundException;
        //  86     93     185    227    Any
        //  100    110    190    205    Ljava/io/FileNotFoundException;
        //  100    110    185    227    Any
        //  117    125    190    205    Ljava/io/FileNotFoundException;
        //  117    125    185    227    Any
        //  132    140    190    205    Ljava/io/FileNotFoundException;
        //  132    140    185    227    Any
        //  150    155    160    163    Ljava/io/IOException;
        //  150    155    157    160    Ljava/lang/Exception;
        //  170    175    180    183    Ljava/io/IOException;
        //  170    175    177    180    Ljava/lang/Exception;
        //  195    205    185    227    Any
        //  209    213    220    224    Ljava/io/IOException;
        //  209    213    216    220    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0213:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private RandomAccessFile checkSplitFile() throws ZipException {
        if (this.zipModel.isSplitArchive()) {
            final int diskNumberStart = this.fileHeader.getDiskNumberStart();
            this.currSplitFileCounter = diskNumberStart + 1;
            final String zipFile = this.zipModel.getZipFile();
            String s;
            if (diskNumberStart == this.zipModel.getEndCentralDirRecord().getNoOfThisDisk()) {
                s = this.zipModel.getZipFile();
            }
            else if (diskNumberStart >= 9) {
                final StringBuilder sb = new StringBuilder();
                sb.append(zipFile.substring(0, zipFile.lastIndexOf(".")));
                sb.append(".z");
                sb.append(diskNumberStart + 1);
                s = sb.toString();
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(zipFile.substring(0, zipFile.lastIndexOf(".")));
                sb2.append(".z0");
                sb2.append(diskNumberStart + 1);
                s = sb2.toString();
            }
            try {
                final RandomAccessFile randomAccessFile = new RandomAccessFile(s, "r");
                if (this.currSplitFileCounter == 1) {
                    final byte[] array = new byte[4];
                    randomAccessFile.read(array);
                    if (Raw.readIntLittleEndian(array, 0) != 134695760L) {
                        throw new ZipException("invalid first part split file signature");
                    }
                }
                return randomAccessFile;
            }
            catch (IOException ex) {
                throw new ZipException(ex);
            }
            catch (FileNotFoundException ex2) {
                throw new ZipException(ex2);
            }
        }
        return null;
    }
    
    private void closeStreams(final InputStream p0, final OutputStream p1) throws ZipException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifnull          79
        //     4: aload_1        
        //     5: invokevirtual   java/io/InputStream.close:()V
        //     8: goto            79
        //    11: astore_1       
        //    12: goto            54
        //    15: astore_1       
        //    16: aload_1        
        //    17: ifnull          68
        //    20: aload_1        
        //    21: invokevirtual   java/io/IOException.getMessage:()Ljava/lang/String;
        //    24: invokestatic    net/lingala/zip4j/util/Zip4jUtil.isStringNotNullAndNotEmpty:(Ljava/lang/String;)Z
        //    27: ifeq            68
        //    30: aload_1        
        //    31: invokevirtual   java/io/IOException.getMessage:()Ljava/lang/String;
        //    34: ldc             " - Wrong Password?"
        //    36: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;)I
        //    39: iflt            68
        //    42: new             Lnet/lingala/zip4j/exception/ZipException;
        //    45: dup            
        //    46: aload_1        
        //    47: invokevirtual   java/io/IOException.getMessage:()Ljava/lang/String;
        //    50: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/String;)V
        //    53: athrow         
        //    54: aload_2        
        //    55: ifnull          66
        //    58: aload_2        
        //    59: invokevirtual   java/io/OutputStream.close:()V
        //    62: goto            66
        //    65: astore_2       
        //    66: aload_1        
        //    67: athrow         
        //    68: aload_2        
        //    69: ifnull          92
        //    72: aload_2        
        //    73: invokevirtual   java/io/OutputStream.close:()V
        //    76: goto            87
        //    79: aload_2        
        //    80: ifnull          92
        //    83: aload_2        
        //    84: invokevirtual   java/io/OutputStream.close:()V
        //    87: goto            92
        //    90: astore_1       
        //    91: return         
        //    92: return         
        //    Exceptions:
        //  throws net.lingala.zip4j.exception.ZipException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  4      8      15     79     Ljava/io/IOException;
        //  4      8      11     68     Any
        //  20     54     11     68     Any
        //  58     62     65     66     Ljava/io/IOException;
        //  72     76     90     92     Ljava/io/IOException;
        //  83     87     90     92     Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 46, Size: 46
        //     at java.util.ArrayList.rangeCheck(Unknown Source)
        //     at java.util.ArrayList.get(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3569)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private RandomAccessFile createFileHandler(final String s) throws ZipException {
        if (this.zipModel != null) {
            if (Zip4jUtil.isStringNotNullAndNotEmpty(this.zipModel.getZipFile())) {
                try {
                    if (this.zipModel.isSplitArchive()) {
                        return this.checkSplitFile();
                    }
                    return new RandomAccessFile(new File(this.zipModel.getZipFile()), s);
                }
                catch (Exception ex) {
                    throw new ZipException(ex);
                }
                catch (FileNotFoundException ex2) {
                    throw new ZipException(ex2);
                }
            }
        }
        throw new ZipException("input parameter is null in getFilePointer");
    }
    
    private byte[] getAESPasswordVerifier(final RandomAccessFile randomAccessFile) throws ZipException {
        try {
            final byte[] array = new byte[2];
            randomAccessFile.read(array);
            return array;
        }
        catch (IOException ex) {
            throw new ZipException(ex);
        }
    }
    
    private byte[] getAESSalt(final RandomAccessFile randomAccessFile) throws ZipException {
        if (this.localFileHeader.getAesExtraDataRecord() == null) {
            return null;
        }
        try {
            final byte[] array = new byte[this.calculateAESSaltLength(this.localFileHeader.getAesExtraDataRecord())];
            randomAccessFile.seek(this.localFileHeader.getOffsetStartOfData());
            randomAccessFile.read(array);
            return array;
        }
        catch (IOException ex) {
            throw new ZipException(ex);
        }
    }
    
    private String getOutputFileNameWithPath(final String s, String fileName) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)) {
            fileName = this.fileHeader.getFileName();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(System.getProperty("file.separator"));
        sb.append(fileName);
        return sb.toString();
    }
    
    private FileOutputStream getOutputStream(final String s, final String s2) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("invalid output path");
        }
        try {
            final File file = new File(this.getOutputFileNameWithPath(s, s2));
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            return new FileOutputStream(file);
        }
        catch (FileNotFoundException ex) {
            throw new ZipException(ex);
        }
    }
    
    private byte[] getStandardDecrypterHeaderBytes(final RandomAccessFile randomAccessFile) throws ZipException {
        try {
            final byte[] array = new byte[12];
            randomAccessFile.seek(this.localFileHeader.getOffsetStartOfData());
            randomAccessFile.read(array, 0, 12);
            return array;
        }
        catch (Exception ex) {
            throw new ZipException(ex);
        }
        catch (IOException ex2) {
            throw new ZipException(ex2);
        }
    }
    
    private void init(final RandomAccessFile randomAccessFile) throws ZipException {
        if (this.localFileHeader == null) {
            throw new ZipException("local file header is null, cannot initialize input stream");
        }
        try {
            this.initDecrypter(randomAccessFile);
        }
        catch (Exception ex) {
            throw new ZipException(ex);
        }
        catch (ZipException ex2) {
            throw ex2;
        }
    }
    
    private void initDecrypter(final RandomAccessFile randomAccessFile) throws ZipException {
        if (this.localFileHeader == null) {
            throw new ZipException("local file header is null, cannot init decrypter");
        }
        if (!this.localFileHeader.isEncrypted()) {
            return;
        }
        if (this.localFileHeader.getEncryptionMethod() == 0) {
            this.decrypter = new StandardDecrypter(this.fileHeader, this.getStandardDecrypterHeaderBytes(randomAccessFile));
            return;
        }
        if (this.localFileHeader.getEncryptionMethod() == 99) {
            this.decrypter = new AESDecrypter(this.localFileHeader, this.getAESSalt(randomAccessFile), this.getAESPasswordVerifier(randomAccessFile));
            return;
        }
        throw new ZipException("unsupported encryption method");
    }
    
    public void checkCRC() throws ZipException {
        if (this.fileHeader != null) {
            if (this.fileHeader.getEncryptionMethod() == 99) {
                if (this.decrypter != null && this.decrypter instanceof AESDecrypter) {
                    final byte[] calculatedAuthenticationBytes = ((AESDecrypter)this.decrypter).getCalculatedAuthenticationBytes();
                    final byte[] storedMac = ((AESDecrypter)this.decrypter).getStoredMac();
                    final byte[] array = new byte[10];
                    if (array == null || storedMac == null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("CRC (MAC) check failed for ");
                        sb.append(this.fileHeader.getFileName());
                        throw new ZipException(sb.toString());
                    }
                    System.arraycopy(calculatedAuthenticationBytes, 0, array, 0, 10);
                    if (!Arrays.equals(array, storedMac)) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("invalid CRC (MAC) for file: ");
                        sb2.append(this.fileHeader.getFileName());
                        throw new ZipException(sb2.toString());
                    }
                }
            }
            else if ((this.crc.getValue() & 0xFFFFFFFFL) != this.fileHeader.getCrc32()) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("invalid CRC for file: ");
                sb3.append(this.fileHeader.getFileName());
                String s2;
                final String s = s2 = sb3.toString();
                if (this.localFileHeader.isEncrypted()) {
                    s2 = s;
                    if (this.localFileHeader.getEncryptionMethod() == 0) {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append(s);
                        sb4.append(" - Wrong Password?");
                        s2 = sb4.toString();
                    }
                }
                throw new ZipException(s2);
            }
        }
    }
    
    public IDecrypter getDecrypter() {
        return this.decrypter;
    }
    
    public FileHeader getFileHeader() {
        return this.fileHeader;
    }
    
    public ZipInputStream getInputStream() throws ZipException {
        if (this.fileHeader == null) {
            throw new ZipException("file header is null, cannot get inputstream");
        }
        RandomAccessFile fileHandler;
        while (true) {
            while (true) {
                Label_0452: {
                    try {
                        fileHandler = this.createFileHandler("r");
                        try {
                            if (!this.checkLocalHeader()) {
                                throw new ZipException("local header and file header do not match");
                            }
                            this.init(fileHandler);
                            long compressedSize = this.localFileHeader.getCompressedSize();
                            long offsetStartOfData = this.localFileHeader.getOffsetStartOfData();
                            if (!this.localFileHeader.isEncrypted()) {
                                break Label_0452;
                            }
                            if (this.localFileHeader.getEncryptionMethod() == 99) {
                                if (!(this.decrypter instanceof AESDecrypter)) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("invalid decryptor when trying to calculate compressed size for AES encrypted file: ");
                                    sb.append(this.fileHeader.getFileName());
                                    throw new ZipException(sb.toString());
                                }
                                compressedSize -= ((AESDecrypter)this.decrypter).getSaltLength() + ((AESDecrypter)this.decrypter).getPasswordVerifierLength() + 10;
                                offsetStartOfData += ((AESDecrypter)this.decrypter).getSaltLength() + ((AESDecrypter)this.decrypter).getPasswordVerifierLength();
                            }
                            else {
                                if (this.localFileHeader.getEncryptionMethod() != 0) {
                                    break Label_0452;
                                }
                                compressedSize -= 12L;
                                offsetStartOfData += 12L;
                            }
                            int n = this.fileHeader.getCompressionMethod();
                            if (this.fileHeader.getEncryptionMethod() == 99) {
                                if (this.fileHeader.getAesExtraDataRecord() == null) {
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append("AESExtraDataRecord does not exist for AES encrypted file: ");
                                    sb2.append(this.fileHeader.getFileName());
                                    throw new ZipException(sb2.toString());
                                }
                                n = this.fileHeader.getAesExtraDataRecord().getCompressionMethod();
                            }
                            fileHandler.seek(offsetStartOfData);
                            if (n == 0) {
                                return new ZipInputStream(new PartInputStream(fileHandler, offsetStartOfData, compressedSize, this));
                            }
                            if (n != 8) {
                                throw new ZipException("compression type not supported");
                            }
                            return new ZipInputStream(new InflaterInputStream(fileHandler, offsetStartOfData, compressedSize, this));
                        }
                        catch (Exception ex2) {}
                        catch (ZipException ex) {}
                    }
                    catch (Exception ex2) {
                        fileHandler = null;
                    }
                    catch (ZipException ex) {
                        fileHandler = null;
                    }
                    try {
                        fileHandler.close();
                        goto Label_0419;
                    }
                    catch (IOException ex3) {}
                    break;
                }
                continue;
            }
        }
        if (fileHandler != null) {
            try {
                fileHandler.close();
            }
            catch (IOException ex4) {}
        }
        throw;
    }
    
    public LocalFileHeader getLocalFileHeader() {
        return this.localFileHeader;
    }
    
    public ZipModel getZipModel() {
        return this.zipModel;
    }
    
    public RandomAccessFile startNextSplitFile() throws IOException, FileNotFoundException {
        final String zipFile = this.zipModel.getZipFile();
        String s;
        if (this.currSplitFileCounter == this.zipModel.getEndCentralDirRecord().getNoOfThisDisk()) {
            s = this.zipModel.getZipFile();
        }
        else if (this.currSplitFileCounter >= 9) {
            final StringBuilder sb = new StringBuilder();
            sb.append(zipFile.substring(0, zipFile.lastIndexOf(".")));
            sb.append(".z");
            sb.append(this.currSplitFileCounter + 1);
            s = sb.toString();
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(zipFile.substring(0, zipFile.lastIndexOf(".")));
            sb2.append(".z0");
            sb2.append(this.currSplitFileCounter + 1);
            s = sb2.toString();
        }
        ++this.currSplitFileCounter;
        try {
            if (!Zip4jUtil.checkFileExists(s)) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("zip split file does not exist: ");
                sb3.append(s);
                throw new IOException(sb3.toString());
            }
            return new RandomAccessFile(s, "r");
        }
        catch (ZipException ex) {
            throw new IOException(ex.getMessage());
        }
    }
    
    public void unzipFile(final ProgressMonitor progressMonitor, final String s, final String s2, final UnzipParameters unzipParameters) throws ZipException {
        if (this.zipModel == null || this.fileHeader == null || !Zip4jUtil.isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("Invalid parameters passed during unzipping file. One or more of the parameters were null");
        }
        final Object o = null;
        final Object o2 = null;
        final InputStream inputStream = null;
        final Object o3 = null;
        final Object o4 = null;
        final OutputStream outputStream = null;
        InputStream inputStream2 = inputStream;
        OutputStream outputStream2 = outputStream;
        try {
            try {
                final byte[] array = new byte[4096];
                inputStream2 = inputStream;
                outputStream2 = outputStream;
                final ZipInputStream zipInputStream = (ZipInputStream)(inputStream2 = this.getInputStream());
                outputStream2 = outputStream;
                final FileOutputStream outputStream3 = this.getOutputStream(s, s2);
                do {
                    inputStream2 = zipInputStream;
                    outputStream2 = outputStream3;
                    final int read = zipInputStream.read(array);
                    if (read == -1) {
                        inputStream2 = zipInputStream;
                        outputStream2 = outputStream3;
                        this.closeStreams(zipInputStream, outputStream3);
                        inputStream2 = zipInputStream;
                        outputStream2 = outputStream3;
                        UnzipUtil.applyFileAttributes(this.fileHeader, new File(this.getOutputFileNameWithPath(s, s2)), unzipParameters);
                        this.closeStreams(zipInputStream, outputStream3);
                        return;
                    }
                    inputStream2 = zipInputStream;
                    outputStream2 = outputStream3;
                    outputStream3.write(array, 0, read);
                    inputStream2 = zipInputStream;
                    outputStream2 = outputStream3;
                    progressMonitor.updateWorkCompleted(read);
                    inputStream2 = zipInputStream;
                    outputStream2 = outputStream3;
                    final FileOutputStream fileOutputStream = outputStream3;
                } while (!progressMonitor.isCancelAllTasks());
                inputStream2 = zipInputStream;
                outputStream2 = outputStream3;
                progressMonitor.setResult(3);
                inputStream2 = zipInputStream;
                outputStream2 = outputStream3;
                progressMonitor.setState(0);
                this.closeStreams(zipInputStream, outputStream3);
                return;
            }
            finally {}
        }
        catch (Exception ex) {
            throw new ZipException(ex);
        }
        catch (IOException ex2) {
            throw new ZipException(ex2);
        }
        this.closeStreams(inputStream2, outputStream2);
    }
    
    public void updateCRC(final int n) {
        this.crc.update(n);
    }
    
    public void updateCRC(final byte[] array, final int n, final int n2) {
        if (array != null) {
            this.crc.update(array, n, n2);
        }
    }
}
