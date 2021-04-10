package net.lingala.zip4j.zip;

import net.lingala.zip4j.exception.*;
import java.util.*;
import net.lingala.zip4j.progress.*;
import net.lingala.zip4j.model.*;
import net.lingala.zip4j.util.*;
import java.io.*;
import net.lingala.zip4j.io.*;

public class ZipEngine
{
    private ZipModel zipModel;
    
    public ZipEngine(final ZipModel zipModel) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null in ZipEngine constructor");
        }
        this.zipModel = zipModel;
    }
    
    private long calculateTotalWork(final ArrayList list, final ZipParameters zipParameters) throws ZipException {
        if (list == null) {
            throw new ZipException("file list is null, cannot calculate total work");
        }
        long n = 0L;
        long n2;
        for (int i = 0; i < list.size(); ++i, n = n2) {
            n2 = n;
            if (list.get(i) instanceof File) {
                n2 = n;
                if (list.get(i).exists()) {
                    if (zipParameters.isEncryptFiles() && zipParameters.getEncryptionMethod() == 0) {
                        n2 = n + Zip4jUtil.getFileLengh(list.get(i)) * 2L;
                    }
                    else {
                        n2 = n + Zip4jUtil.getFileLengh(list.get(i));
                    }
                    if (this.zipModel.getCentralDirectory() != null && this.zipModel.getCentralDirectory().getFileHeaders() != null && this.zipModel.getCentralDirectory().getFileHeaders().size() > 0) {
                        final FileHeader fileHeader = Zip4jUtil.getFileHeader(this.zipModel, Zip4jUtil.getRelativeFileName(list.get(i).getAbsolutePath(), zipParameters.getRootFolderInZip(), zipParameters.getDefaultFolderPath()));
                        if (fileHeader != null) {
                            n2 += Zip4jUtil.getFileLengh(new File(this.zipModel.getZipFile())) - fileHeader.getCompressedSize();
                        }
                    }
                }
            }
        }
        return n;
    }
    
    private void checkParameters(final ZipParameters zipParameters) throws ZipException {
        if (zipParameters == null) {
            throw new ZipException("cannot validate zip parameters");
        }
        if (zipParameters.getCompressionMethod() != 0 && zipParameters.getCompressionMethod() != 8) {
            throw new ZipException("unsupported compression type");
        }
        if (zipParameters.getCompressionMethod() == 8 && zipParameters.getCompressionLevel() < 0 && zipParameters.getCompressionLevel() > 9) {
            throw new ZipException("invalid compression level. compression level dor deflate should be in the range of 0-9");
        }
        if (zipParameters.isEncryptFiles()) {
            if (zipParameters.getEncryptionMethod() != 0 && zipParameters.getEncryptionMethod() != 99) {
                throw new ZipException("unsupported encryption method");
            }
            if (zipParameters.getPassword() == null || zipParameters.getPassword().length <= 0) {
                throw new ZipException("input password is empty or null");
            }
        }
        else {
            zipParameters.setAesKeyStrength(-1);
            zipParameters.setEncryptionMethod(-1);
        }
    }
    
    private EndCentralDirRecord createEndOfCentralDirectoryRecord() {
        final EndCentralDirRecord endCentralDirRecord = new EndCentralDirRecord();
        endCentralDirRecord.setSignature(101010256L);
        endCentralDirRecord.setNoOfThisDisk(0);
        endCentralDirRecord.setTotNoOfEntriesInCentralDir(0);
        endCentralDirRecord.setTotNoOfEntriesInCentralDirOnThisDisk(0);
        endCentralDirRecord.setOffsetOfStartOfCentralDir(0L);
        return endCentralDirRecord;
    }
    
    private void initAddFiles(ArrayList list, ZipParameters zipParameters, final ProgressMonitor progressMonitor) throws ZipException {
        Label_0954: {
            if (list == null || zipParameters == null) {
                break Label_0954;
            }
            if (list.size() <= 0) {
                throw new ZipException("no files to add");
            }
            if (this.zipModel.getEndCentralDirRecord() == null) {
                this.zipModel.setEndCentralDirRecord(this.createEndOfCentralDirectoryRecord());
            }
            Object o = null;
            byte[] array = null;
            Object o2;
            final Throwable t = (Throwable)(o2 = null);
            Object o3 = o;
            Object o4 = array;
        Label_0898_Outer:
            while (true) {
                Label_0965: {
                    while (true) {
                        try {
                            this.checkParameters(zipParameters);
                            o2 = t;
                            o3 = o;
                            o4 = array;
                            this.removeFilesIfExists(list, zipParameters, progressMonitor);
                            o2 = t;
                            o3 = o;
                            o4 = array;
                            final boolean checkFileExists = Zip4jUtil.checkFileExists(this.zipModel.getZipFile());
                            o2 = t;
                            o3 = o;
                            o4 = array;
                            final SplitOutputStream splitOutputStream = new SplitOutputStream(new File(this.zipModel.getZipFile()), this.zipModel.getSplitLength());
                            o2 = t;
                            o3 = o;
                            o4 = array;
                            final Object o5 = new ZipOutputStream(splitOutputStream, this.zipModel);
                            if (checkFileExists) {
                                o2 = o5;
                                o3 = o5;
                                o4 = o5;
                                if (this.zipModel.getEndCentralDirRecord() == null) {
                                    o2 = o5;
                                    o3 = o5;
                                    o4 = o5;
                                    throw new ZipException("invalid end of central directory record");
                                }
                                o2 = o5;
                                o3 = o5;
                                o4 = o5;
                                splitOutputStream.seek(this.zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir());
                            }
                            o2 = o5;
                            o3 = o5;
                            o4 = o5;
                            array = new byte[4096];
                            o2 = null;
                            final int n = 0;
                            o3 = o2;
                            o4 = o2;
                            o = o2;
                            try {
                                if (n >= list.size()) {
                                    o3 = o2;
                                    o4 = o2;
                                    o = o2;
                                    ((ZipOutputStream)o5).finish();
                                    o3 = o2;
                                    o4 = o2;
                                    o = o2;
                                    progressMonitor.endProgressMonitorSuccess();
                                    if (o2 != null) {
                                        try {
                                            ((InputStream)o2).close();
                                        }
                                        catch (IOException ex) {}
                                    }
                                    if (o5 != null) {
                                        try {
                                            ((ZipOutputStream)o5).close();
                                        }
                                        catch (IOException ex2) {}
                                    }
                                    return;
                                }
                                o3 = o2;
                                o4 = o2;
                                o = o2;
                                final ZipParameters zipParameters2 = (ZipParameters)zipParameters.clone();
                                o3 = o2;
                                o4 = o2;
                                o = o2;
                                progressMonitor.setFileName(list.get(n).getAbsolutePath());
                                o3 = o2;
                                o4 = o2;
                                o = o2;
                                if (!list.get(n).isDirectory()) {
                                    o3 = o2;
                                    o4 = o2;
                                    o = o2;
                                    if (zipParameters2.isEncryptFiles()) {
                                        o3 = o2;
                                        o4 = o2;
                                        o = o2;
                                        if (zipParameters2.getEncryptionMethod() == 0) {
                                            o3 = o2;
                                            o4 = o2;
                                            o = o2;
                                            progressMonitor.setCurrentOperation(3);
                                            o3 = o2;
                                            o4 = o2;
                                            o = o2;
                                            zipParameters2.setSourceFileCRC((int)CRCUtil.computeFileCRC(list.get(n).getAbsolutePath(), progressMonitor));
                                            o3 = o2;
                                            o4 = o2;
                                            o = o2;
                                            progressMonitor.setCurrentOperation(0);
                                        }
                                    }
                                    o3 = o2;
                                    o4 = o2;
                                    o = o2;
                                    if (Zip4jUtil.getFileLengh(list.get(n)) == 0L) {
                                        o3 = o2;
                                        o4 = o2;
                                        o = o2;
                                        zipParameters2.setCompressionMethod(0);
                                    }
                                }
                                o3 = o2;
                                o4 = o2;
                                o = o2;
                                ((DeflaterOutputStream)o5).putNextEntry(list.get(n), zipParameters2);
                                o3 = o2;
                                o4 = o2;
                                o = o2;
                                if (list.get(n).isDirectory()) {
                                    o3 = o2;
                                    o4 = o2;
                                    o = o2;
                                    ((ZipOutputStream)o5).closeEntry();
                                    o3 = o2;
                                    break Label_0965;
                                }
                                o3 = o2;
                                o4 = o2;
                                o = o2;
                                o2 = new FileInputStream(list.get(n));
                                while (true) {
                                    o3 = o2;
                                    o4 = o2;
                                    o = o2;
                                    final int read = ((InputStream)o2).read(array);
                                    if (read == -1) {
                                        break;
                                    }
                                    o3 = o2;
                                    o4 = o2;
                                    o = o2;
                                    ((ZipOutputStream)o5).write(array, 0, read);
                                    o3 = o2;
                                    o4 = o2;
                                    o = o2;
                                    progressMonitor.updateWorkCompleted(read);
                                }
                                o3 = o2;
                                o4 = o2;
                                o = o2;
                                ((ZipOutputStream)o5).closeEntry();
                                if ((o3 = o2) != null) {
                                    o3 = o2;
                                    o4 = o2;
                                    o = o2;
                                    ((InputStream)o2).close();
                                    o3 = o2;
                                }
                                break Label_0965;
                            }
                            catch (Exception o2) {}
                            catch (ZipException o2) {}
                        }
                        catch (Exception o2) {
                            o4 = null;
                            final Object o5 = o3;
                            zipParameters = (ZipParameters)o5;
                            list = (ArrayList<File>)o4;
                            try {
                                progressMonitor.endProgressMonitorError((Throwable)o2);
                                zipParameters = (ZipParameters)o5;
                                list = (ArrayList<File>)o4;
                                throw new ZipException((Throwable)o2);
                                zipParameters = (ZipParameters)o5;
                                list = (ArrayList<File>)o;
                                progressMonitor.endProgressMonitorError((Throwable)o2);
                                zipParameters = (ZipParameters)o5;
                                list = (ArrayList<File>)o;
                                throw o2;
                            }
                            finally {
                                o3 = list;
                                goto Label_0858;
                            }
                            if (o3 != null) {
                                try {
                                    ((InputStream)o3).close();
                                }
                                catch (IOException ex3) {}
                            }
                            if (zipParameters != null) {
                                try {
                                    ((CipherOutputStream)zipParameters).close();
                                }
                                catch (IOException ex4) {}
                            }
                            throw progressMonitor;
                            final int n = n + 1;
                            o2 = o3;
                            continue Label_0898_Outer;
                            throw new ZipException("one of the input parameters is null when adding files");
                        }
                        catch (ZipException o2) {
                            continue;
                        }
                        finally {
                            o3 = null;
                            final Object o5 = o2;
                        }
                        break;
                    }
                }
                break;
            }
        }
    }
    
    private RandomAccessFile prepareFileOutputStream() throws ZipException {
        final String zipFile = this.zipModel.getZipFile();
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(zipFile)) {
            throw new ZipException("invalid output path");
        }
        try {
            final File file = new File(zipFile);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            return new RandomAccessFile(file, "rw");
        }
        catch (FileNotFoundException ex) {
            throw new ZipException(ex);
        }
    }
    
    private void removeFilesIfExists(final ArrayList p0, final ZipParameters p1, final ProgressMonitor p2) throws ZipException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        net/lingala/zip4j/zip/ZipEngine.zipModel:Lnet/lingala/zip4j/model/ZipModel;
        //     4: ifnull          506
        //     7: aload_0        
        //     8: getfield        net/lingala/zip4j/zip/ZipEngine.zipModel:Lnet/lingala/zip4j/model/ZipModel;
        //    11: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //    14: ifnull          506
        //    17: aload_0        
        //    18: getfield        net/lingala/zip4j/zip/ZipEngine.zipModel:Lnet/lingala/zip4j/model/ZipModel;
        //    21: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //    24: invokevirtual   net/lingala/zip4j/model/CentralDirectory.getFileHeaders:()Ljava/util/ArrayList;
        //    27: ifnull          506
        //    30: aload_0        
        //    31: getfield        net/lingala/zip4j/zip/ZipEngine.zipModel:Lnet/lingala/zip4j/model/ZipModel;
        //    34: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //    37: invokevirtual   net/lingala/zip4j/model/CentralDirectory.getFileHeaders:()Ljava/util/ArrayList;
        //    40: invokevirtual   java/util/ArrayList.size:()I
        //    43: ifgt            49
        //    46: goto            506
        //    49: aconst_null    
        //    50: astore          8
        //    52: iconst_0       
        //    53: istore          4
        //    55: aload_1        
        //    56: invokevirtual   java/util/ArrayList.size:()I
        //    59: istore          5
        //    61: iload           4
        //    63: iload           5
        //    65: if_icmpge       450
        //    68: aload           8
        //    70: astore          11
        //    72: aload           8
        //    74: astore          9
        //    76: aload_1        
        //    77: iload           4
        //    79: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //    82: checkcast       Ljava/io/File;
        //    85: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //    88: aload_2        
        //    89: invokevirtual   net/lingala/zip4j/model/ZipParameters.getRootFolderInZip:()Ljava/lang/String;
        //    92: aload_2        
        //    93: invokevirtual   net/lingala/zip4j/model/ZipParameters.getDefaultFolderPath:()Ljava/lang/String;
        //    96: invokestatic    net/lingala/zip4j/util/Zip4jUtil.getRelativeFileName:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //    99: astore          10
        //   101: aload           8
        //   103: astore          11
        //   105: aload           8
        //   107: astore          9
        //   109: aload_0        
        //   110: getfield        net/lingala/zip4j/zip/ZipEngine.zipModel:Lnet/lingala/zip4j/model/ZipModel;
        //   113: aload           10
        //   115: invokestatic    net/lingala/zip4j/util/Zip4jUtil.getFileHeader:(Lnet/lingala/zip4j/model/ZipModel;Ljava/lang/String;)Lnet/lingala/zip4j/model/FileHeader;
        //   118: astore          12
        //   120: aload           8
        //   122: astore          9
        //   124: aload           12
        //   126: ifnull          429
        //   129: aload           8
        //   131: astore          10
        //   133: aload           8
        //   135: ifnull          154
        //   138: aload           8
        //   140: astore          11
        //   142: aload           8
        //   144: astore          9
        //   146: aload           8
        //   148: invokevirtual   java/io/RandomAccessFile.close:()V
        //   151: aconst_null    
        //   152: astore          10
        //   154: aload           10
        //   156: astore          11
        //   158: aload           10
        //   160: astore          9
        //   162: new             Lnet/lingala/zip4j/util/ArchiveMaintainer;
        //   165: dup            
        //   166: invokespecial   net/lingala/zip4j/util/ArchiveMaintainer.<init>:()V
        //   169: astore          8
        //   171: aload           10
        //   173: astore          11
        //   175: aload           10
        //   177: astore          9
        //   179: aload_3        
        //   180: iconst_2       
        //   181: invokevirtual   net/lingala/zip4j/progress/ProgressMonitor.setCurrentOperation:(I)V
        //   184: aload           10
        //   186: astore          11
        //   188: aload           10
        //   190: astore          9
        //   192: aload           8
        //   194: aload_0        
        //   195: getfield        net/lingala/zip4j/zip/ZipEngine.zipModel:Lnet/lingala/zip4j/model/ZipModel;
        //   198: aload           12
        //   200: aload_3        
        //   201: invokevirtual   net/lingala/zip4j/util/ArchiveMaintainer.initRemoveZipFile:(Lnet/lingala/zip4j/model/ZipModel;Lnet/lingala/zip4j/model/FileHeader;Lnet/lingala/zip4j/progress/ProgressMonitor;)Ljava/util/HashMap;
        //   204: astore          12
        //   206: aload           10
        //   208: astore          11
        //   210: aload           10
        //   212: astore          9
        //   214: aload_3        
        //   215: invokevirtual   net/lingala/zip4j/progress/ProgressMonitor.isCancelAllTasks:()Z
        //   218: ifeq            260
        //   221: aload           10
        //   223: astore          11
        //   225: aload           10
        //   227: astore          9
        //   229: aload_3        
        //   230: iconst_3       
        //   231: invokevirtual   net/lingala/zip4j/progress/ProgressMonitor.setResult:(I)V
        //   234: aload           10
        //   236: astore          11
        //   238: aload           10
        //   240: astore          9
        //   242: aload_3        
        //   243: iconst_0       
        //   244: invokevirtual   net/lingala/zip4j/progress/ProgressMonitor.setState:(I)V
        //   247: aload           10
        //   249: ifnull          259
        //   252: aload           10
        //   254: invokevirtual   java/io/RandomAccessFile.close:()V
        //   257: return         
        //   258: astore_1       
        //   259: return         
        //   260: aload           10
        //   262: astore          11
        //   264: aload           10
        //   266: astore          9
        //   268: aload_3        
        //   269: iconst_0       
        //   270: invokevirtual   net/lingala/zip4j/progress/ProgressMonitor.setCurrentOperation:(I)V
        //   273: aload           10
        //   275: astore          9
        //   277: aload           10
        //   279: ifnonnull       429
        //   282: aload           10
        //   284: astore          11
        //   286: aload           10
        //   288: astore          9
        //   290: aload_0        
        //   291: invokespecial   net/lingala/zip4j/zip/ZipEngine.prepareFileOutputStream:()Ljava/io/RandomAccessFile;
        //   294: astore          8
        //   296: aload           8
        //   298: astore          9
        //   300: aload           12
        //   302: ifnull          429
        //   305: aload           8
        //   307: astore          11
        //   309: aload           8
        //   311: astore          9
        //   313: aload           12
        //   315: ldc_w           "offsetCentralDir"
        //   318: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   321: astore          10
        //   323: aload           8
        //   325: astore          9
        //   327: aload           10
        //   329: ifnull          429
        //   332: aload           8
        //   334: astore          11
        //   336: aload           8
        //   338: astore          9
        //   340: aload           12
        //   342: ldc_w           "offsetCentralDir"
        //   345: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   348: checkcast       Ljava/lang/String;
        //   351: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //   354: lstore          6
        //   356: aload           8
        //   358: astore          9
        //   360: lload           6
        //   362: lconst_0       
        //   363: lcmp           
        //   364: iflt            429
        //   367: aload           8
        //   369: astore          11
        //   371: aload           8
        //   373: astore          9
        //   375: aload           8
        //   377: lload           6
        //   379: invokevirtual   java/io/RandomAccessFile.seek:(J)V
        //   382: aload           8
        //   384: astore          9
        //   386: goto            429
        //   389: astore_1       
        //   390: aload           8
        //   392: astore          11
        //   394: aload           8
        //   396: astore          9
        //   398: new             Lnet/lingala/zip4j/exception/ZipException;
        //   401: dup            
        //   402: ldc_w           "Error while parsing offset central directory. Cannot update already existing file header"
        //   405: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/String;)V
        //   408: athrow         
        //   409: astore_1       
        //   410: aload           8
        //   412: astore          11
        //   414: aload           8
        //   416: astore          9
        //   418: new             Lnet/lingala/zip4j/exception/ZipException;
        //   421: dup            
        //   422: ldc_w           "NumberFormatException while parsing offset central directory. Cannot update already existing file header"
        //   425: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/String;)V
        //   428: athrow         
        //   429: iload           4
        //   431: iconst_1       
        //   432: iadd           
        //   433: istore          4
        //   435: aload           9
        //   437: astore          8
        //   439: goto            55
        //   442: astore_1       
        //   443: aload           11
        //   445: astore          8
        //   447: goto            469
        //   450: aload           8
        //   452: ifnull          463
        //   455: aload           8
        //   457: invokevirtual   java/io/RandomAccessFile.close:()V
        //   460: return         
        //   461: astore_1       
        //   462: return         
        //   463: return         
        //   464: astore_1       
        //   465: goto            490
        //   468: astore_1       
        //   469: aload           8
        //   471: astore          9
        //   473: new             Lnet/lingala/zip4j/exception/ZipException;
        //   476: dup            
        //   477: aload_1        
        //   478: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/Throwable;)V
        //   481: athrow         
        //   482: astore_1       
        //   483: aload           9
        //   485: astore          8
        //   487: goto            465
        //   490: aload           8
        //   492: ifnull          504
        //   495: aload           8
        //   497: invokevirtual   java/io/RandomAccessFile.close:()V
        //   500: goto            504
        //   503: astore_2       
        //   504: aload_1        
        //   505: athrow         
        //   506: return         
        //    Exceptions:
        //  throws net.lingala.zip4j.exception.ZipException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  55     61     468    469    Ljava/io/IOException;
        //  55     61     464    465    Any
        //  76     101    442    450    Ljava/io/IOException;
        //  76     101    482    490    Any
        //  109    120    442    450    Ljava/io/IOException;
        //  109    120    482    490    Any
        //  146    151    442    450    Ljava/io/IOException;
        //  146    151    482    490    Any
        //  162    171    442    450    Ljava/io/IOException;
        //  162    171    482    490    Any
        //  179    184    442    450    Ljava/io/IOException;
        //  179    184    482    490    Any
        //  192    206    442    450    Ljava/io/IOException;
        //  192    206    482    490    Any
        //  214    221    442    450    Ljava/io/IOException;
        //  214    221    482    490    Any
        //  229    234    442    450    Ljava/io/IOException;
        //  229    234    482    490    Any
        //  242    247    442    450    Ljava/io/IOException;
        //  242    247    482    490    Any
        //  252    257    258    259    Ljava/io/IOException;
        //  268    273    442    450    Ljava/io/IOException;
        //  268    273    482    490    Any
        //  290    296    442    450    Ljava/io/IOException;
        //  290    296    482    490    Any
        //  313    323    442    450    Ljava/io/IOException;
        //  313    323    482    490    Any
        //  340    356    409    429    Ljava/lang/NumberFormatException;
        //  340    356    389    409    Ljava/lang/Exception;
        //  340    356    442    450    Ljava/io/IOException;
        //  340    356    482    490    Any
        //  375    382    442    450    Ljava/io/IOException;
        //  375    382    482    490    Any
        //  398    409    442    450    Ljava/io/IOException;
        //  398    409    482    490    Any
        //  418    429    442    450    Ljava/io/IOException;
        //  418    429    482    490    Any
        //  455    460    461    463    Ljava/io/IOException;
        //  473    482    482    490    Any
        //  495    500    503    504    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0154:
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
    
    public void addFiles(final ArrayList list, final ZipParameters zipParameters, final ProgressMonitor progressMonitor, final boolean b) throws ZipException {
        if (list == null || zipParameters == null) {
            throw new ZipException("one of the input parameters is null when adding files");
        }
        if (list.size() <= 0) {
            throw new ZipException("no files to add");
        }
        progressMonitor.setTotalWork(this.calculateTotalWork(list, zipParameters));
        progressMonitor.setCurrentOperation(0);
        progressMonitor.setState(1);
        progressMonitor.setResult(1);
        if (b) {
            new Thread("Zip4j") {
                @Override
                public void run() {
                    try {
                        ZipEngine.this.initAddFiles(list, zipParameters, progressMonitor);
                    }
                    catch (ZipException ex) {}
                }
            }.start();
            return;
        }
        this.initAddFiles(list, zipParameters, progressMonitor);
    }
    
    public void addFolderToZip(final File file, final ZipParameters zipParameters, final ProgressMonitor progressMonitor, final boolean b) throws ZipException {
        if (file == null || zipParameters == null) {
            throw new ZipException("one of the input parameters is null, cannot add folder to zip");
        }
        if (!Zip4jUtil.checkFileExists(file.getAbsolutePath())) {
            throw new ZipException("input folder does not exist");
        }
        if (!file.isDirectory()) {
            throw new ZipException("input file is not a folder, user addFileToZip method to add files");
        }
        if (!Zip4jUtil.checkFileReadAccess(file.getAbsolutePath())) {
            final StringBuilder sb = new StringBuilder();
            sb.append("cannot read folder: ");
            sb.append(file.getAbsolutePath());
            throw new ZipException(sb.toString());
        }
        String defaultFolderPath;
        if (zipParameters.isIncludeRootFolder()) {
            if (file.getAbsolutePath() != null) {
                if (file.getAbsoluteFile().getParentFile() != null) {
                    defaultFolderPath = file.getAbsoluteFile().getParentFile().getAbsolutePath();
                }
                else {
                    defaultFolderPath = "";
                }
            }
            else if (file.getParentFile() != null) {
                defaultFolderPath = file.getParentFile().getAbsolutePath();
            }
            else {
                defaultFolderPath = "";
            }
        }
        else {
            defaultFolderPath = file.getAbsolutePath();
        }
        zipParameters.setDefaultFolderPath(defaultFolderPath);
        ArrayList<File> filesInDirectoryRec;
        final ArrayList list = filesInDirectoryRec = (ArrayList<File>)Zip4jUtil.getFilesInDirectoryRec(file, zipParameters.isReadHiddenFiles());
        if (zipParameters.isIncludeRootFolder()) {
            if ((filesInDirectoryRec = (ArrayList<File>)list) == null) {
                filesInDirectoryRec = new ArrayList<File>();
            }
            filesInDirectoryRec.add(file);
        }
        this.addFiles(filesInDirectoryRec, zipParameters, progressMonitor, b);
    }
    
    public void addStreamToZip(final InputStream inputStream, final ZipParameters zipParameters) throws ZipException {
        if (inputStream != null) {
            if (zipParameters != null) {
                ZipOutputStream zipOutputStream2;
                final ZipOutputStream zipOutputStream = zipOutputStream2 = null;
                while (true) {
                    try {
                        try {
                            this.checkParameters(zipParameters);
                            zipOutputStream2 = zipOutputStream;
                            final boolean checkFileExists = Zip4jUtil.checkFileExists(this.zipModel.getZipFile());
                            zipOutputStream2 = zipOutputStream;
                            final SplitOutputStream splitOutputStream = new SplitOutputStream(new File(this.zipModel.getZipFile()), this.zipModel.getSplitLength());
                            zipOutputStream2 = zipOutputStream;
                            final ZipOutputStream zipOutputStream3 = new ZipOutputStream(splitOutputStream, this.zipModel);
                            if (checkFileExists) {
                                zipOutputStream2 = zipOutputStream3;
                                if (this.zipModel.getEndCentralDirRecord() == null) {
                                    zipOutputStream2 = zipOutputStream3;
                                    throw new ZipException("invalid end of central directory record");
                                }
                                zipOutputStream2 = zipOutputStream3;
                                splitOutputStream.seek(this.zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir());
                            }
                            zipOutputStream2 = zipOutputStream3;
                            final byte[] array = new byte[4096];
                            zipOutputStream2 = zipOutputStream3;
                            zipOutputStream3.putNextEntry(null, zipParameters);
                            zipOutputStream2 = zipOutputStream3;
                            if (!zipParameters.getFileNameInZip().endsWith("/")) {
                                zipOutputStream2 = zipOutputStream3;
                                if (!zipParameters.getFileNameInZip().endsWith("\\")) {
                                    while (true) {
                                        zipOutputStream2 = zipOutputStream3;
                                        final int read = inputStream.read(array);
                                        if (read == -1) {
                                            break;
                                        }
                                        zipOutputStream2 = zipOutputStream3;
                                        zipOutputStream3.write(array, 0, read);
                                    }
                                }
                            }
                            zipOutputStream2 = zipOutputStream3;
                            zipOutputStream3.closeEntry();
                            zipOutputStream2 = zipOutputStream3;
                            zipOutputStream3.finish();
                            if (zipOutputStream3 != null) {
                                try {
                                    zipOutputStream3.close();
                                }
                                catch (IOException ex) {}
                            }
                            return;
                        }
                        finally {
                            if (zipOutputStream2 != null) {
                                final ZipOutputStream zipOutputStream4 = zipOutputStream2;
                                zipOutputStream4.close();
                            }
                        }
                    }
                    catch (Exception ex2) {}
                    catch (ZipException ex3) {}
                    try {
                        final ZipOutputStream zipOutputStream4 = zipOutputStream2;
                        zipOutputStream4.close();
                        continue;
                    }
                    catch (IOException ex4) {}
                    break;
                }
            }
        }
        throw new ZipException("one of the input parameters is null, cannot add stream to zip");
    }
}
