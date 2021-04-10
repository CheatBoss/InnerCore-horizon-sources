package net.lingala.zip4j.util;

import net.lingala.zip4j.progress.*;
import net.lingala.zip4j.exception.*;
import net.lingala.zip4j.core.*;
import net.lingala.zip4j.model.*;
import java.util.*;
import java.io.*;
import net.lingala.zip4j.io.*;

public class ArchiveMaintainer
{
    private long calculateTotalWorkForMergeOp(final ZipModel zipModel) throws ZipException {
        long n = 0L;
        if (zipModel.isSplitArchive()) {
            final int noOfThisDisk = zipModel.getEndCentralDirRecord().getNoOfThisDisk();
            final String zipFile = zipModel.getZipFile();
            n = 0L;
            long fileLengh;
            for (int i = 0; i <= noOfThisDisk; ++i, n += fileLengh) {
                String s;
                if (zipModel.getEndCentralDirRecord().getNoOfThisDisk() == 0) {
                    s = zipModel.getZipFile();
                }
                else if (9 >= 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(zipFile.substring(0, zipFile.lastIndexOf(".")));
                    sb.append(".z");
                    sb.append(0 + 1);
                    s = sb.toString();
                }
                else {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(zipFile.substring(0, zipFile.lastIndexOf(".")));
                    sb2.append(".z0");
                    sb2.append(0 + 1);
                    s = sb2.toString();
                }
                fileLengh = Zip4jUtil.getFileLengh(new File(s));
            }
        }
        return n;
    }
    
    private long calculateTotalWorkForRemoveOp(final ZipModel zipModel, final FileHeader fileHeader) throws ZipException {
        return Zip4jUtil.getFileLengh(new File(zipModel.getZipFile())) - fileHeader.getCompressedSize();
    }
    
    private void copyFile(final RandomAccessFile randomAccessFile, final OutputStream outputStream, long n, final long n2, final ProgressMonitor progressMonitor) throws ZipException {
        if (randomAccessFile != null) {
            if (outputStream != null) {
                if (n < 0L) {
                    throw new ZipException("starting offset is negative, cannot copy file");
                }
                if (n2 < 0L) {
                    throw new ZipException("end offset is negative, cannot copy file");
                }
                if (n > n2) {
                    throw new ZipException("start offset is greater than end offset, cannot copy file");
                }
                if (n == n2) {
                    return;
                }
                if (progressMonitor.isCancelAllTasks()) {
                    progressMonitor.setResult(3);
                    progressMonitor.setState(0);
                    return;
                }
                Label_0090: {
                    break Label_0090;
                    long n3 = 0L;
                    do {
                    Block_13_Outer:
                        while (true) {
                            break Label_0185;
                            int read = 0;
                            Label_0248: {
                                try {
                                    randomAccessFile.seek(n);
                                    n3 = n2 - n;
                                    byte[] array;
                                    if (n2 - n < 4096L) {
                                        final long n4 = 0L;
                                        array = new byte[(int)(n2 - n)];
                                        n = n4;
                                    }
                                    else {
                                        n = 0L;
                                        array = new byte[4096];
                                    }
                                    while (true) {
                                        while (true) {
                                            read = randomAccessFile.read(array);
                                            if (read == -1) {
                                                return;
                                            }
                                            outputStream.write(array, 0, read);
                                            progressMonitor.updateWorkCompleted(read);
                                            if (progressMonitor.isCancelAllTasks()) {
                                                progressMonitor.setResult(3);
                                                return;
                                            }
                                            break Label_0248;
                                            final byte[] array2 = new byte[(int)(n3 - n)];
                                            Label_0210: {
                                                array = array2;
                                            }
                                            continue Block_13_Outer;
                                        }
                                        final byte[] array2 = array;
                                        continue;
                                    }
                                }
                                // iftrue(Label_0210:, n + (long)array.length <= n3)
                                catch (Exception ex) {
                                    throw new ZipException(ex);
                                }
                                catch (IOException ex2) {
                                    throw new ZipException(ex2);
                                }
                                throw new ZipException("input or output stream is null, cannot copy file");
                            }
                            n += read;
                            continue;
                        }
                    } while (n != n3);
                }
                return;
            }
        }
        throw new ZipException("input or output stream is null, cannot copy file");
    }
    
    private RandomAccessFile createFileHandler(final ZipModel zipModel, final String s) throws ZipException {
        if (zipModel != null) {
            if (Zip4jUtil.isStringNotNullAndNotEmpty(zipModel.getZipFile())) {
                try {
                    return new RandomAccessFile(new File(zipModel.getZipFile()), s);
                }
                catch (FileNotFoundException ex) {
                    throw new ZipException(ex);
                }
            }
        }
        throw new ZipException("input parameter is null in getFilePointer, cannot create file handler to remove file");
    }
    
    private RandomAccessFile createSplitZipFileHandler(final ZipModel zipModel, final int n) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null, cannot create split file handler");
        }
        if (n < 0) {
            throw new ZipException("invlaid part number, cannot create split file handler");
        }
        try {
            final String zipFile = zipModel.getZipFile();
            String s;
            if (n == zipModel.getEndCentralDirRecord().getNoOfThisDisk()) {
                s = zipModel.getZipFile();
            }
            else if (n >= 9) {
                final StringBuilder sb = new StringBuilder();
                sb.append(zipFile.substring(0, zipFile.lastIndexOf(".")));
                sb.append(".z");
                sb.append(n + 1);
                s = sb.toString();
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(zipFile.substring(0, zipFile.lastIndexOf(".")));
                sb2.append(".z0");
                sb2.append(n + 1);
                s = sb2.toString();
            }
            final File file = new File(s);
            if (!Zip4jUtil.checkFileExists(file)) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("split file does not exist: ");
                sb3.append(s);
                throw new ZipException(sb3.toString());
            }
            return new RandomAccessFile(file, "r");
        }
        catch (Exception ex) {
            throw new ZipException(ex);
        }
        catch (FileNotFoundException ex2) {
            throw new ZipException(ex2);
        }
    }
    
    private void initMergeSplitZipFile(ZipModel ex, File splitZipFileHandler, ProgressMonitor progressMonitor) throws ZipException {
        if (ex == null) {
            final ZipException ex2 = new ZipException("one of the input parameters is null, cannot merge split zip file");
            progressMonitor.endProgressMonitorError(ex2);
            throw ex2;
        }
        if (!((ZipModel)ex).isSplitArchive()) {
            final ZipException ex3 = new ZipException("archive not a split zip file");
            progressMonitor.endProgressMonitorError(ex3);
            throw ex3;
        }
        final Long n = null;
        Object prepareOutputStreamForMerge = null;
        Object o = null;
        Object o2 = null;
        ArrayList<Long> list = null;
        final ArrayList<Long> list2 = new ArrayList<Long>();
        long offsetOfStartOfCentralDir = 0L;
        boolean b = false;
        while (true) {
            Label_0876: {
                try {
                    final int noOfThisDisk = ((ZipModel)ex).getEndCentralDirRecord().getNoOfThisDisk();
                    if (noOfThisDisk <= 0) {
                        try {
                            throw new ZipException("corrupt zip model, archive not a split zip file");
                        }
                        catch (Exception ex4) {}
                        catch (IOException ex5) {}
                    }
                    prepareOutputStreamForMerge = this.prepareOutputStreamForMerge((File)splitZipFileHandler);
                    offsetOfStartOfCentralDir = 0L;
                    int n2 = 0;
                    list = list2;
                    o = n;
                    if (n2 <= noOfThisDisk) {
                        try {
                            splitZipFileHandler = (Exception)this.createSplitZipFileHandler((ZipModel)ex, n2);
                            try {
                                o2 = new Long(((RandomAccessFile)splitZipFileHandler).length());
                                int intLittleEndian = 0;
                            Label_0296_Outer:
                                while (true) {
                                    while (true) {
                                        Label_0318: {
                                            Label_0315: {
                                                if (n2 != 0) {
                                                    break Label_0315;
                                                }
                                                try {
                                                    if (((ZipModel)ex).getCentralDirectory() == null || ((ZipModel)ex).getCentralDirectory().getFileHeaders() == null || ((ZipModel)ex).getCentralDirectory().getFileHeaders().size() <= 0) {
                                                        break Label_0315;
                                                    }
                                                    o = new byte[4];
                                                    ((RandomAccessFile)splitZipFileHandler).seek(0L);
                                                    ((RandomAccessFile)splitZipFileHandler).read((byte[])o);
                                                    intLittleEndian = Raw.readIntLittleEndian((byte[])o, 0);
                                                    if (intLittleEndian == 134695760L) {
                                                        intLittleEndian = 4;
                                                        b = true;
                                                        break Label_0318;
                                                    }
                                                    break Label_0315;
                                                }
                                                catch (Exception ex6) {
                                                    o = splitZipFileHandler;
                                                    o2 = prepareOutputStreamForMerge;
                                                }
                                                catch (IOException ex7) {}
                                                o = splitZipFileHandler;
                                                o2 = prepareOutputStreamForMerge;
                                            }
                                            intLittleEndian = 0;
                                        }
                                        if (n2 == noOfThisDisk) {
                                            try {
                                                o2 = new Long(((ZipModel)ex).getEndCentralDirRecord().getOffsetOfStartOfCentralDir());
                                            }
                                            catch (Exception ex8) {
                                                continue Label_0296_Outer;
                                            }
                                            catch (IOException ex9) {
                                                continue;
                                            }
                                            finally {
                                                goto Label_0269;
                                            }
                                        }
                                        break;
                                    }
                                    break;
                                }
                                final long n3 = intLittleEndian;
                                try {
                                    final long longValue = (long)o2;
                                    o = prepareOutputStreamForMerge;
                                    try {
                                        this.copyFile((RandomAccessFile)splitZipFileHandler, (OutputStream)prepareOutputStreamForMerge, n3, longValue, progressMonitor);
                                        offsetOfStartOfCentralDir += (long)o2 - intLittleEndian;
                                        try {
                                            Label_0477: {
                                                if (!progressMonitor.isCancelAllTasks()) {
                                                    break Label_0477;
                                                }
                                                try {
                                                    progressMonitor.setResult(3);
                                                    progressMonitor.setState(0);
                                                    if (o != null) {
                                                        try {
                                                            ((OutputStream)o).close();
                                                        }
                                                        catch (IOException ex10) {}
                                                    }
                                                    if (splitZipFileHandler != null) {
                                                        try {
                                                            ((RandomAccessFile)splitZipFileHandler).close();
                                                        }
                                                        catch (IOException ex11) {}
                                                    }
                                                    return;
                                                }
                                                catch (Exception ex12) {}
                                                catch (IOException ex13) {}
                                                try {
                                                    try {
                                                        list.add((Long)o2);
                                                        try {
                                                            ((RandomAccessFile)splitZipFileHandler).close();
                                                        }
                                                        catch (IOException ex14) {}
                                                        prepareOutputStreamForMerge = o;
                                                        ++n2;
                                                    }
                                                    catch (Exception ex15) {}
                                                }
                                                catch (IOException ex) {}
                                            }
                                        }
                                        catch (Exception ex) {}
                                        catch (IOException ex) {}
                                    }
                                    catch (Exception ex16) {}
                                    catch (IOException ex17) {}
                                }
                                catch (Exception o) {
                                    prepareOutputStreamForMerge = o;
                                }
                                catch (IOException o) {
                                    prepareOutputStreamForMerge = o;
                                }
                            }
                            catch (Exception o) {
                                prepareOutputStreamForMerge = o;
                            }
                            catch (IOException o) {
                                prepareOutputStreamForMerge = o;
                            }
                            finally {
                                progressMonitor = (ProgressMonitor)prepareOutputStreamForMerge;
                            }
                        }
                        catch (Exception ex18) {}
                        catch (IOException ex19) {}
                        finally {
                            progressMonitor = (ProgressMonitor)prepareOutputStreamForMerge;
                            break Label_0919;
                        }
                    }
                    o2 = prepareOutputStreamForMerge;
                    try {
                        ex = (IOException)((ZipModel)ex).clone();
                        ((ZipModel)ex).getEndCentralDirRecord().setOffsetOfStartOfCentralDir(offsetOfStartOfCentralDir);
                        try {
                            this.updateSplitZipModel((ZipModel)ex, list, b);
                            new HeaderWriter().finalizeZipFileWithoutValidations((ZipModel)ex, (OutputStream)o2);
                            progressMonitor.endProgressMonitorSuccess();
                            if (o2 != null) {
                                try {
                                    ((OutputStream)o2).close();
                                }
                                catch (IOException ex20) {}
                            }
                            if (o != null) {
                                try {
                                    ((RandomAccessFile)o).close();
                                }
                                catch (IOException ex21) {}
                            }
                            return;
                        }
                        catch (Exception ex) {}
                        catch (IOException ex) {}
                    }
                    catch (Exception ex22) {
                        prepareOutputStreamForMerge = o;
                        break Label_0876;
                    }
                    catch (IOException ex23) {}
                    prepareOutputStreamForMerge = o;
                    break Label_0876;
                }
                catch (Exception splitZipFileHandler) {
                    ex = null;
                    o = prepareOutputStreamForMerge;
                    o2 = ex;
                    try {
                        progressMonitor.endProgressMonitorError(splitZipFileHandler);
                        o = prepareOutputStreamForMerge;
                        o2 = ex;
                        throw new ZipException(splitZipFileHandler);
                        o = prepareOutputStreamForMerge;
                        o2 = ex;
                        progressMonitor.endProgressMonitorError(splitZipFileHandler);
                        o = prepareOutputStreamForMerge;
                        o2 = ex;
                        throw new ZipException(splitZipFileHandler);
                    }
                    finally {
                        progressMonitor = (ProgressMonitor)o2;
                        splitZipFileHandler = (Exception)o;
                    }
                    if (progressMonitor != null) {
                        try {
                            ((OutputStream)progressMonitor).close();
                        }
                        catch (IOException ex24) {}
                    }
                    if (splitZipFileHandler != null) {
                        try {
                            ((RandomAccessFile)splitZipFileHandler).close();
                        }
                        catch (IOException ex25) {}
                    }
                    throw ex;
                }
                catch (IOException ex26) {}
                finally {
                    progressMonitor = null;
                    splitZipFileHandler = (Exception)o2;
                    continue;
                }
            }
            break;
        }
    }
    
    private OutputStream prepareOutputStreamForMerge(final File file) throws ZipException {
        if (file == null) {
            throw new ZipException("outFile is null, cannot create outputstream");
        }
        try {
            return new FileOutputStream(file);
        }
        catch (Exception ex) {
            throw new ZipException(ex);
        }
        catch (FileNotFoundException ex2) {
            throw new ZipException(ex2);
        }
    }
    
    private void restoreFileName(final File file, final String s) throws ZipException {
        if (!file.delete()) {
            throw new ZipException("cannot delete old zip file");
        }
        if (!new File(s).renameTo(file)) {
            throw new ZipException("cannot rename modified zip file");
        }
    }
    
    private void updateSplitEndCentralDirectory(final ZipModel zipModel) throws ZipException {
        while (true) {
            if (zipModel == null) {
                while (true) {
                    try {
                        throw new ZipException("zip model is null - cannot update end of central directory for split zip model");
                        // iftrue(Label_0033:, zipModel.getCentralDirectory() != null)
                        throw new ZipException("corrupt zip model - getCentralDirectory, cannot update split zip model");
                        Label_0033: {
                            zipModel.getEndCentralDirRecord().setNoOfThisDisk(0);
                        }
                        zipModel.getEndCentralDirRecord().setNoOfThisDiskStartOfCentralDir(0);
                        zipModel.getEndCentralDirRecord().setTotNoOfEntriesInCentralDir(zipModel.getCentralDirectory().getFileHeaders().size());
                        zipModel.getEndCentralDirRecord().setTotNoOfEntriesInCentralDirOnThisDisk(zipModel.getCentralDirectory().getFileHeaders().size());
                        return;
                        throw;
                    }
                    catch (Exception ex3) {}
                    catch (ZipException ex2) {}
                    final ZipException ex2;
                    final ZipException ex = ex2;
                    continue;
                }
            }
            continue;
        }
    }
    
    private void updateSplitFileHeader(final ZipModel p0, final ArrayList p1, final boolean p2) throws ZipException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //     4: ifnonnull       18
        //     7: new             Lnet/lingala/zip4j/exception/ZipException;
        //    10: dup            
        //    11: ldc_w           "corrupt zip model - getCentralDirectory, cannot update split zip model"
        //    14: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/String;)V
        //    17: athrow         
        //    18: aload_1        
        //    19: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //    22: invokevirtual   net/lingala/zip4j/model/CentralDirectory.getFileHeaders:()Ljava/util/ArrayList;
        //    25: invokevirtual   java/util/ArrayList.size:()I
        //    28: istore          7
        //    30: iconst_0       
        //    31: istore          4
        //    33: iload_3        
        //    34: ifeq            185
        //    37: iconst_4       
        //    38: istore          4
        //    40: goto            185
        //    43: aload_1        
        //    44: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //    47: invokevirtual   net/lingala/zip4j/model/CentralDirectory.getFileHeaders:()Ljava/util/ArrayList;
        //    50: iload           5
        //    52: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //    55: checkcast       Lnet/lingala/zip4j/model/FileHeader;
        //    58: invokevirtual   net/lingala/zip4j/model/FileHeader.getDiskNumberStart:()I
        //    61: istore          8
        //    63: iload           6
        //    65: iload           8
        //    67: if_icmpge       100
        //    70: aload_2        
        //    71: iload           6
        //    73: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //    76: checkcast       Ljava/lang/Long;
        //    79: invokevirtual   java/lang/Long.longValue:()J
        //    82: lstore          11
        //    84: iload           6
        //    86: iconst_1       
        //    87: iadd           
        //    88: istore          6
        //    90: lload           9
        //    92: lload           11
        //    94: ladd           
        //    95: lstore          9
        //    97: goto            43
        //   100: aload_1        
        //   101: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //   104: invokevirtual   net/lingala/zip4j/model/CentralDirectory.getFileHeaders:()Ljava/util/ArrayList;
        //   107: iload           5
        //   109: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //   112: checkcast       Lnet/lingala/zip4j/model/FileHeader;
        //   115: aload_1        
        //   116: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //   119: invokevirtual   net/lingala/zip4j/model/CentralDirectory.getFileHeaders:()Ljava/util/ArrayList;
        //   122: iload           5
        //   124: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //   127: checkcast       Lnet/lingala/zip4j/model/FileHeader;
        //   130: invokevirtual   net/lingala/zip4j/model/FileHeader.getOffsetLocalHeader:()J
        //   133: lload           9
        //   135: ladd           
        //   136: iload           4
        //   138: i2l            
        //   139: lsub           
        //   140: invokevirtual   net/lingala/zip4j/model/FileHeader.setOffsetLocalHeader:(J)V
        //   143: aload_1        
        //   144: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //   147: invokevirtual   net/lingala/zip4j/model/CentralDirectory.getFileHeaders:()Ljava/util/ArrayList;
        //   150: iload           5
        //   152: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //   155: checkcast       Lnet/lingala/zip4j/model/FileHeader;
        //   158: iconst_0       
        //   159: invokevirtual   net/lingala/zip4j/model/FileHeader.setDiskNumberStart:(I)V
        //   162: iload           5
        //   164: iconst_1       
        //   165: iadd           
        //   166: istore          5
        //   168: goto            188
        //   171: return         
        //   172: astore_1       
        //   173: new             Lnet/lingala/zip4j/exception/ZipException;
        //   176: dup            
        //   177: aload_1        
        //   178: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/Throwable;)V
        //   181: athrow         
        //   182: astore_1       
        //   183: aload_1        
        //   184: athrow         
        //   185: iconst_0       
        //   186: istore          5
        //   188: iload           5
        //   190: iload           7
        //   192: if_icmpge       171
        //   195: lconst_0       
        //   196: lstore          9
        //   198: iconst_0       
        //   199: istore          6
        //   201: goto            43
        //   204: astore_1       
        //   205: goto            173
        //   208: astore_1       
        //   209: goto            183
        //    Exceptions:
        //  throws net.lingala.zip4j.exception.ZipException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                      
        //  -----  -----  -----  -----  ------------------------------------------
        //  0      18     182    185    Lnet/lingala/zip4j/exception/ZipException;
        //  0      18     172    173    Ljava/lang/Exception;
        //  18     30     182    185    Lnet/lingala/zip4j/exception/ZipException;
        //  18     30     172    173    Ljava/lang/Exception;
        //  43     63     182    185    Lnet/lingala/zip4j/exception/ZipException;
        //  43     63     172    173    Ljava/lang/Exception;
        //  70     84     208    185    Lnet/lingala/zip4j/exception/ZipException;
        //  70     84     204    208    Ljava/lang/Exception;
        //  100    162    208    185    Lnet/lingala/zip4j/exception/ZipException;
        //  100    162    204    208    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0100:
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
    
    private void updateSplitZip64EndCentralDirLocator(final ZipModel zipModel, final ArrayList list) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null, cannot update split Zip64 end of central directory locator");
        }
        if (zipModel.getZip64EndCentralDirLocator() == null) {
            return;
        }
        final Zip64EndCentralDirLocator zip64EndCentralDirLocator = zipModel.getZip64EndCentralDirLocator();
        int i = 0;
        zip64EndCentralDirLocator.setNoOfDiskStartOfZip64EndOfCentralDirRec(0);
        long n;
        long longValue;
        for (n = 0L; i < list.size(); ++i, n += longValue) {
            longValue = list.get(i);
        }
        zipModel.getZip64EndCentralDirLocator().setOffsetZip64EndOfCentralDirRec(zipModel.getZip64EndCentralDirLocator().getOffsetZip64EndOfCentralDirRec() + n);
        zipModel.getZip64EndCentralDirLocator().setTotNumberOfDiscs(1);
    }
    
    private void updateSplitZip64EndCentralDirRec(final ZipModel zipModel, final ArrayList list) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null, cannot update split Zip64 end of central directory record");
        }
        if (zipModel.getZip64EndCentralDirRecord() == null) {
            return;
        }
        final Zip64EndCentralDirRecord zip64EndCentralDirRecord = zipModel.getZip64EndCentralDirRecord();
        int i = 0;
        zip64EndCentralDirRecord.setNoOfThisDisk(0);
        zipModel.getZip64EndCentralDirRecord().setNoOfThisDiskStartOfCentralDir(0);
        zipModel.getZip64EndCentralDirRecord().setTotNoOfEntriesInCentralDirOnThisDisk(zipModel.getEndCentralDirRecord().getTotNoOfEntriesInCentralDir());
        long n;
        long longValue;
        for (n = 0L; i < list.size(); ++i, n += longValue) {
            longValue = list.get(i);
        }
        zipModel.getZip64EndCentralDirRecord().setOffsetStartCenDirWRTStartDiskNo(zipModel.getZip64EndCentralDirRecord().getOffsetStartCenDirWRTStartDiskNo() + n);
    }
    
    private void updateSplitZipModel(final ZipModel zipModel, final ArrayList list, final boolean b) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null, cannot update split zip model");
        }
        zipModel.setSplitArchive(false);
        this.updateSplitFileHeader(zipModel, list, b);
        this.updateSplitEndCentralDirectory(zipModel);
        if (zipModel.isZip64Format()) {
            this.updateSplitZip64EndCentralDirLocator(zipModel, list);
            this.updateSplitZip64EndCentralDirRec(zipModel, list);
        }
    }
    
    public void initProgressMonitorForMergeOp(final ZipModel zipModel, final ProgressMonitor progressMonitor) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("zip model is null, cannot calculate total work for merge op");
        }
        progressMonitor.setCurrentOperation(4);
        progressMonitor.setFileName(zipModel.getZipFile());
        progressMonitor.setTotalWork(this.calculateTotalWorkForMergeOp(zipModel));
        progressMonitor.setState(1);
    }
    
    public void initProgressMonitorForRemoveOp(final ZipModel zipModel, final FileHeader fileHeader, final ProgressMonitor progressMonitor) throws ZipException {
        if (zipModel != null && fileHeader != null && progressMonitor != null) {
            progressMonitor.setCurrentOperation(2);
            progressMonitor.setFileName(fileHeader.getFileName());
            progressMonitor.setTotalWork(this.calculateTotalWorkForRemoveOp(zipModel, fileHeader));
            progressMonitor.setState(1);
            return;
        }
        throw new ZipException("one of the input parameters is null, cannot calculate total work");
    }
    
    public HashMap initRemoveZipFile(final ZipModel p0, final FileHeader p1, final ProgressMonitor p2) throws ZipException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifnull          2535
        //     4: aload_1        
        //     5: ifnonnull       11
        //     8: goto            2535
        //    11: aconst_null    
        //    12: astore          31
        //    14: aconst_null    
        //    15: astore          32
        //    17: aconst_null    
        //    18: astore          30
        //    20: iconst_0       
        //    21: istore          9
        //    23: iconst_0       
        //    24: istore          10
        //    26: iconst_0       
        //    27: istore          16
        //    29: iconst_0       
        //    30: istore          15
        //    32: iconst_0       
        //    33: istore          11
        //    35: iconst_0       
        //    36: istore          6
        //    38: iconst_0       
        //    39: istore          12
        //    41: iconst_0       
        //    42: istore          4
        //    44: iconst_0       
        //    45: istore          5
        //    47: iconst_0       
        //    48: istore          13
        //    50: iconst_0       
        //    51: istore          14
        //    53: iconst_0       
        //    54: istore          8
        //    56: aconst_null    
        //    57: astore          34
        //    59: aconst_null    
        //    60: astore          35
        //    62: aconst_null    
        //    63: astore          26
        //    65: new             Ljava/util/HashMap;
        //    68: dup            
        //    69: invokespecial   java/util/HashMap.<init>:()V
        //    72: astore          33
        //    74: aload_1        
        //    75: aload_2        
        //    76: invokestatic    net/lingala/zip4j/util/Zip4jUtil.getIndexOfFileHeader:(Lnet/lingala/zip4j/model/ZipModel;Lnet/lingala/zip4j/model/FileHeader;)I
        //    79: istore          7
        //    81: iload           7
        //    83: ifge            152
        //    86: new             Lnet/lingala/zip4j/exception/ZipException;
        //    89: dup            
        //    90: ldc_w           "file header not found in zip model, cannot remove file"
        //    93: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/String;)V
        //    96: athrow         
        //    97: astore_1       
        //    98: aconst_null    
        //    99: astore          26
        //   101: aconst_null    
        //   102: astore_2       
        //   103: aconst_null    
        //   104: astore_3       
        //   105: aload           32
        //   107: astore          28
        //   109: iload           14
        //   111: istore          4
        //   113: goto            2471
        //   116: astore_2       
        //   117: aconst_null    
        //   118: astore          27
        //   120: aconst_null    
        //   121: astore_1       
        //   122: aconst_null    
        //   123: astore          26
        //   125: aload           30
        //   127: astore          28
        //   129: goto            2355
        //   132: astore_2       
        //   133: aconst_null    
        //   134: astore          27
        //   136: aconst_null    
        //   137: astore_1       
        //   138: aconst_null    
        //   139: astore          26
        //   141: aload           31
        //   143: astore          28
        //   145: iload           5
        //   147: istore          4
        //   149: goto            2407
        //   152: aload_1        
        //   153: invokevirtual   net/lingala/zip4j/model/ZipModel.isSplitArchive:()Z
        //   156: istore          17
        //   158: iload           17
        //   160: ifeq            174
        //   163: new             Lnet/lingala/zip4j/exception/ZipException;
        //   166: dup            
        //   167: ldc_w           "This is a split archive. Zip file format does not allow updating split/spanned files"
        //   170: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/String;)V
        //   173: athrow         
        //   174: invokestatic    java/lang/System.currentTimeMillis:()J
        //   177: lstore          18
        //   179: new             Ljava/lang/StringBuilder;
        //   182: dup            
        //   183: invokespecial   java/lang/StringBuilder.<init>:()V
        //   186: astore          36
        //   188: aload           36
        //   190: aload_1        
        //   191: invokevirtual   net/lingala/zip4j/model/ZipModel.getZipFile:()Ljava/lang/String;
        //   194: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   197: pop            
        //   198: aload           26
        //   200: astore          27
        //   202: aload           34
        //   204: astore          28
        //   206: aload           35
        //   208: astore          29
        //   210: aload           36
        //   212: lload           18
        //   214: ldc2_w          1000
        //   217: lrem           
        //   218: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   221: pop            
        //   222: aload           26
        //   224: astore          27
        //   226: aload           34
        //   228: astore          28
        //   230: aload           35
        //   232: astore          29
        //   234: aload           36
        //   236: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   239: astore          26
        //   241: aload           26
        //   243: astore          27
        //   245: aload           26
        //   247: astore          28
        //   249: aload           26
        //   251: astore          29
        //   253: new             Ljava/io/File;
        //   256: dup            
        //   257: aload           26
        //   259: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   262: astore          34
        //   264: aload           34
        //   266: astore          27
        //   268: aload           27
        //   270: invokevirtual   java/io/File.exists:()Z
        //   273: istore          17
        //   275: iload           17
        //   277: ifeq            471
        //   280: aload           26
        //   282: astore          27
        //   284: aload           26
        //   286: astore          28
        //   288: aload           26
        //   290: astore          29
        //   292: invokestatic    java/lang/System.currentTimeMillis:()J
        //   295: lstore          18
        //   297: aload           26
        //   299: astore          27
        //   301: aload           26
        //   303: astore          28
        //   305: aload           26
        //   307: astore          29
        //   309: new             Ljava/lang/StringBuilder;
        //   312: dup            
        //   313: invokespecial   java/lang/StringBuilder.<init>:()V
        //   316: astore          34
        //   318: aload           26
        //   320: astore          27
        //   322: aload           26
        //   324: astore          28
        //   326: aload           26
        //   328: astore          29
        //   330: aload           34
        //   332: aload_1        
        //   333: invokevirtual   net/lingala/zip4j/model/ZipModel.getZipFile:()Ljava/lang/String;
        //   336: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   339: pop            
        //   340: aload           26
        //   342: astore          27
        //   344: aload           26
        //   346: astore          28
        //   348: aload           26
        //   350: astore          29
        //   352: aload           34
        //   354: lload           18
        //   356: ldc2_w          1000
        //   359: lrem           
        //   360: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   363: pop            
        //   364: aload           26
        //   366: astore          27
        //   368: aload           26
        //   370: astore          28
        //   372: aload           26
        //   374: astore          29
        //   376: aload           34
        //   378: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   381: astore          26
        //   383: aload           26
        //   385: astore          27
        //   387: aload           26
        //   389: astore          28
        //   391: aload           26
        //   393: astore          29
        //   395: new             Ljava/io/File;
        //   398: dup            
        //   399: aload           26
        //   401: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   404: astore          34
        //   406: aload           34
        //   408: astore          27
        //   410: goto            268
        //   413: astore_1       
        //   414: aconst_null    
        //   415: astore_2       
        //   416: aconst_null    
        //   417: astore          26
        //   419: aload           27
        //   421: astore_3       
        //   422: aload           32
        //   424: astore          28
        //   426: iload           14
        //   428: istore          4
        //   430: goto            2471
        //   433: astore_2       
        //   434: aconst_null    
        //   435: astore_1       
        //   436: aconst_null    
        //   437: astore          27
        //   439: aload           28
        //   441: astore          26
        //   443: aload           30
        //   445: astore          28
        //   447: goto            2355
        //   450: astore_2       
        //   451: aconst_null    
        //   452: astore_1       
        //   453: aconst_null    
        //   454: astore          27
        //   456: aload           29
        //   458: astore          26
        //   460: aload           31
        //   462: astore          28
        //   464: iload           5
        //   466: istore          4
        //   468: goto            2407
        //   471: new             Lnet/lingala/zip4j/io/SplitOutputStream;
        //   474: dup            
        //   475: new             Ljava/io/File;
        //   478: dup            
        //   479: aload           26
        //   481: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   484: invokespecial   net/lingala/zip4j/io/SplitOutputStream.<init>:(Ljava/io/File;)V
        //   487: astore          27
        //   489: new             Ljava/io/File;
        //   492: dup            
        //   493: aload_1        
        //   494: invokevirtual   net/lingala/zip4j/model/ZipModel.getZipFile:()Ljava/lang/String;
        //   497: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   500: astore          28
        //   502: aload_0        
        //   503: aload_1        
        //   504: ldc             "r"
        //   506: invokespecial   net/lingala/zip4j/util/ArchiveMaintainer.createFileHandler:(Lnet/lingala/zip4j/model/ZipModel;Ljava/lang/String;)Ljava/io/RandomAccessFile;
        //   509: astore          29
        //   511: new             Lnet/lingala/zip4j/core/HeaderReader;
        //   514: dup            
        //   515: aload           29
        //   517: invokespecial   net/lingala/zip4j/core/HeaderReader.<init>:(Ljava/io/RandomAccessFile;)V
        //   520: aload_2        
        //   521: invokevirtual   net/lingala/zip4j/core/HeaderReader.readLocalFileHeader:(Lnet/lingala/zip4j/model/FileHeader;)Lnet/lingala/zip4j/model/LocalFileHeader;
        //   524: astore          30
        //   526: aload           30
        //   528: ifnonnull       610
        //   531: new             Lnet/lingala/zip4j/exception/ZipException;
        //   534: dup            
        //   535: ldc_w           "invalid local file header, cannot remove file from archive"
        //   538: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/String;)V
        //   541: athrow         
        //   542: astore_1       
        //   543: aload           26
        //   545: astore_3       
        //   546: aload           28
        //   548: astore          26
        //   550: aload           27
        //   552: astore_2       
        //   553: aload           29
        //   555: astore          28
        //   557: iload           14
        //   559: istore          4
        //   561: goto            2471
        //   564: astore_2       
        //   565: aload           29
        //   567: astore_1       
        //   568: aload           28
        //   570: astore          29
        //   572: aload_1        
        //   573: astore          28
        //   575: aload           27
        //   577: astore_1       
        //   578: aload           29
        //   580: astore          27
        //   582: goto            2355
        //   585: astore_2       
        //   586: aload           29
        //   588: astore_1       
        //   589: aload           28
        //   591: astore          29
        //   593: aload_1        
        //   594: astore          28
        //   596: aload           27
        //   598: astore_1       
        //   599: iload           5
        //   601: istore          4
        //   603: aload           29
        //   605: astore          27
        //   607: goto            2407
        //   610: aload_2        
        //   611: invokevirtual   net/lingala/zip4j/model/FileHeader.getOffsetLocalHeader:()J
        //   614: lstore          18
        //   616: aload_2        
        //   617: invokevirtual   net/lingala/zip4j/model/FileHeader.getZip64ExtendedInfo:()Lnet/lingala/zip4j/model/Zip64ExtendedInfo;
        //   620: astore          30
        //   622: aload           30
        //   624: ifnull          657
        //   627: lload           18
        //   629: lstore          20
        //   631: aload_2        
        //   632: invokevirtual   net/lingala/zip4j/model/FileHeader.getZip64ExtendedInfo:()Lnet/lingala/zip4j/model/Zip64ExtendedInfo;
        //   635: invokevirtual   net/lingala/zip4j/model/Zip64ExtendedInfo.getOffsetLocalHeader:()J
        //   638: ldc2_w          -1
        //   641: lcmp           
        //   642: ifeq            661
        //   645: aload_2        
        //   646: invokevirtual   net/lingala/zip4j/model/FileHeader.getZip64ExtendedInfo:()Lnet/lingala/zip4j/model/Zip64ExtendedInfo;
        //   649: invokevirtual   net/lingala/zip4j/model/Zip64ExtendedInfo.getOffsetLocalHeader:()J
        //   652: lstore          20
        //   654: goto            661
        //   657: lload           18
        //   659: lstore          20
        //   661: ldc2_w          -1
        //   664: lstore          18
        //   666: aload_1        
        //   667: invokevirtual   net/lingala/zip4j/model/ZipModel.getEndCentralDirRecord:()Lnet/lingala/zip4j/model/EndCentralDirRecord;
        //   670: invokevirtual   net/lingala/zip4j/model/EndCentralDirRecord.getOffsetOfStartOfCentralDir:()J
        //   673: lstore          22
        //   675: aload_1        
        //   676: invokevirtual   net/lingala/zip4j/model/ZipModel.isZip64Format:()Z
        //   679: istore          17
        //   681: iload           17
        //   683: ifeq            705
        //   686: aload_1        
        //   687: invokevirtual   net/lingala/zip4j/model/ZipModel.getZip64EndCentralDirRecord:()Lnet/lingala/zip4j/model/Zip64EndCentralDirRecord;
        //   690: ifnull          705
        //   693: aload_1        
        //   694: invokevirtual   net/lingala/zip4j/model/ZipModel.getZip64EndCentralDirRecord:()Lnet/lingala/zip4j/model/Zip64EndCentralDirRecord;
        //   697: invokevirtual   net/lingala/zip4j/model/Zip64EndCentralDirRecord.getOffsetStartCenDirWRTStartDiskNo:()J
        //   700: lstore          22
        //   702: goto            705
        //   705: aload_1        
        //   706: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //   709: invokevirtual   net/lingala/zip4j/model/CentralDirectory.getFileHeaders:()Ljava/util/ArrayList;
        //   712: astore          31
        //   714: iload           7
        //   716: aload           31
        //   718: invokevirtual   java/util/ArrayList.size:()I
        //   721: iconst_1       
        //   722: isub           
        //   723: if_icmpne       735
        //   726: lload           22
        //   728: lconst_1       
        //   729: lsub           
        //   730: lstore          18
        //   732: goto            804
        //   735: aload           31
        //   737: iload           7
        //   739: iconst_1       
        //   740: iadd           
        //   741: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //   744: checkcast       Lnet/lingala/zip4j/model/FileHeader;
        //   747: astore_2       
        //   748: aload_2        
        //   749: ifnull          804
        //   752: aload_2        
        //   753: invokevirtual   net/lingala/zip4j/model/FileHeader.getOffsetLocalHeader:()J
        //   756: lconst_1       
        //   757: lsub           
        //   758: lstore          24
        //   760: lload           24
        //   762: lstore          18
        //   764: aload_2        
        //   765: invokevirtual   net/lingala/zip4j/model/FileHeader.getZip64ExtendedInfo:()Lnet/lingala/zip4j/model/Zip64ExtendedInfo;
        //   768: ifnull          804
        //   771: lload           24
        //   773: lstore          18
        //   775: aload_2        
        //   776: invokevirtual   net/lingala/zip4j/model/FileHeader.getZip64ExtendedInfo:()Lnet/lingala/zip4j/model/Zip64ExtendedInfo;
        //   779: invokevirtual   net/lingala/zip4j/model/Zip64ExtendedInfo.getOffsetLocalHeader:()J
        //   782: ldc2_w          -1
        //   785: lcmp           
        //   786: ifeq            804
        //   789: aload_2        
        //   790: invokevirtual   net/lingala/zip4j/model/FileHeader.getZip64ExtendedInfo:()Lnet/lingala/zip4j/model/Zip64ExtendedInfo;
        //   793: invokevirtual   net/lingala/zip4j/model/Zip64ExtendedInfo.getOffsetLocalHeader:()J
        //   796: lstore          18
        //   798: lload           18
        //   800: lconst_1       
        //   801: lsub           
        //   802: lstore          18
        //   804: lload           20
        //   806: lconst_0       
        //   807: lcmp           
        //   808: iflt            1867
        //   811: lload           18
        //   813: lconst_0       
        //   814: lcmp           
        //   815: ifge            821
        //   818: goto            1867
        //   821: iload           7
        //   823: ifne            929
        //   826: aload_1        
        //   827: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //   830: invokevirtual   net/lingala/zip4j/model/CentralDirectory.getFileHeaders:()Ljava/util/ArrayList;
        //   833: invokevirtual   java/util/ArrayList.size:()I
        //   836: istore          6
        //   838: iload           6
        //   840: iconst_1       
        //   841: if_icmple       862
        //   844: aload_0        
        //   845: aload           29
        //   847: aload           27
        //   849: lload           18
        //   851: lconst_1       
        //   852: ladd           
        //   853: lload           22
        //   855: aload_3        
        //   856: invokespecial   net/lingala/zip4j/util/ArchiveMaintainer.copyFile:(Ljava/io/RandomAccessFile;Ljava/io/OutputStream;JJLnet/lingala/zip4j/progress/ProgressMonitor;)V
        //   859: goto            1059
        //   862: goto            1059
        //   865: astore_1       
        //   866: aload           26
        //   868: astore_3       
        //   869: aload           28
        //   871: astore          26
        //   873: aload           29
        //   875: astore          28
        //   877: aload           27
        //   879: astore_2       
        //   880: iload           14
        //   882: istore          4
        //   884: goto            2471
        //   887: astore_2       
        //   888: aload           28
        //   890: astore          30
        //   892: aload           29
        //   894: astore          28
        //   896: aload           27
        //   898: astore_1       
        //   899: aload           30
        //   901: astore          27
        //   903: goto            2355
        //   906: astore_2       
        //   907: aload           28
        //   909: astore          30
        //   911: aload           29
        //   913: astore          28
        //   915: aload           27
        //   917: astore_1       
        //   918: iload           5
        //   920: istore          4
        //   922: aload           30
        //   924: astore          27
        //   926: goto            2407
        //   929: aload           29
        //   931: astore_2       
        //   932: aload           27
        //   934: astore          30
        //   936: aload           31
        //   938: invokevirtual   java/util/ArrayList.size:()I
        //   941: istore          6
        //   943: iload           7
        //   945: iload           6
        //   947: iconst_1       
        //   948: isub           
        //   949: if_icmpne       1034
        //   952: aload_0        
        //   953: aload_2        
        //   954: aload           30
        //   956: lconst_0       
        //   957: lload           20
        //   959: aload_3        
        //   960: invokespecial   net/lingala/zip4j/util/ArchiveMaintainer.copyFile:(Ljava/io/RandomAccessFile;Ljava/io/OutputStream;JJLnet/lingala/zip4j/progress/ProgressMonitor;)V
        //   963: goto            1059
        //   966: aload           28
        //   968: astore          30
        //   970: aload           26
        //   972: astore_3       
        //   973: astore_1       
        //   974: aload           29
        //   976: astore          28
        //   978: aload           27
        //   980: astore_2       
        //   981: iload           14
        //   983: istore          4
        //   985: aload           30
        //   987: astore          26
        //   989: goto            2471
        //   992: aload           28
        //   994: astore          30
        //   996: astore_2       
        //   997: aload           29
        //   999: astore          28
        //  1001: aload           27
        //  1003: astore_1       
        //  1004: aload           30
        //  1006: astore          27
        //  1008: goto            2355
        //  1011: aload           28
        //  1013: astore          30
        //  1015: astore_2       
        //  1016: aload           29
        //  1018: astore          28
        //  1020: aload           27
        //  1022: astore_1       
        //  1023: iload           5
        //  1025: istore          4
        //  1027: aload           30
        //  1029: astore          27
        //  1031: goto            2407
        //  1034: aload_0        
        //  1035: aload_2        
        //  1036: aload           30
        //  1038: lconst_0       
        //  1039: lload           20
        //  1041: aload_3        
        //  1042: invokespecial   net/lingala/zip4j/util/ArchiveMaintainer.copyFile:(Ljava/io/RandomAccessFile;Ljava/io/OutputStream;JJLnet/lingala/zip4j/progress/ProgressMonitor;)V
        //  1045: aload_0        
        //  1046: aload_2        
        //  1047: aload           30
        //  1049: lload           18
        //  1051: lconst_1       
        //  1052: ladd           
        //  1053: lload           22
        //  1055: aload_3        
        //  1056: invokespecial   net/lingala/zip4j/util/ArchiveMaintainer.copyFile:(Ljava/io/RandomAccessFile;Ljava/io/OutputStream;JJLnet/lingala/zip4j/progress/ProgressMonitor;)V
        //  1059: aload           27
        //  1061: astore          32
        //  1063: aload           29
        //  1065: astore_2       
        //  1066: aload           33
        //  1068: astore          36
        //  1070: aload           28
        //  1072: astore          30
        //  1074: aload           26
        //  1076: astore          31
        //  1078: aload_3        
        //  1079: invokevirtual   net/lingala/zip4j/progress/ProgressMonitor.isCancelAllTasks:()Z
        //  1082: istore          17
        //  1084: iload           17
        //  1086: ifeq            1237
        //  1089: aload_3        
        //  1090: iconst_3       
        //  1091: invokevirtual   net/lingala/zip4j/progress/ProgressMonitor.setResult:(I)V
        //  1094: aload_3        
        //  1095: iconst_0       
        //  1096: invokevirtual   net/lingala/zip4j/progress/ProgressMonitor.setState:(I)V
        //  1099: aload_2        
        //  1100: ifnull          1114
        //  1103: aload_2        
        //  1104: invokevirtual   java/io/RandomAccessFile.close:()V
        //  1107: goto            1114
        //  1110: astore_1       
        //  1111: goto            1128
        //  1114: aload           32
        //  1116: ifnull          1139
        //  1119: aload           32
        //  1121: invokevirtual   java/io/OutputStream.close:()V
        //  1124: goto            1139
        //  1127: astore_1       
        //  1128: new             Lnet/lingala/zip4j/exception/ZipException;
        //  1131: dup            
        //  1132: ldc_w           "cannot close input stream or output stream when trying to delete a file from zip file"
        //  1135: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/String;)V
        //  1138: athrow         
        //  1139: iconst_0       
        //  1140: ifeq            1153
        //  1143: aload_0        
        //  1144: aload           30
        //  1146: aload           31
        //  1148: invokespecial   net/lingala/zip4j/util/ArchiveMaintainer.restoreFileName:(Ljava/io/File;Ljava/lang/String;)V
        //  1151: aconst_null    
        //  1152: areturn        
        //  1153: new             Ljava/io/File;
        //  1156: dup            
        //  1157: aload           31
        //  1159: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //  1162: invokevirtual   java/io/File.delete:()Z
        //  1165: pop            
        //  1166: aconst_null    
        //  1167: areturn        
        //  1168: astore_1       
        //  1169: aload_2        
        //  1170: astore          28
        //  1172: aload           32
        //  1174: astore_2       
        //  1175: aload           31
        //  1177: astore_3       
        //  1178: iload           14
        //  1180: istore          4
        //  1182: aload           30
        //  1184: astore          26
        //  1186: goto            2471
        //  1189: astore          26
        //  1191: aload           32
        //  1193: astore_1       
        //  1194: aload_2        
        //  1195: astore          28
        //  1197: aload           26
        //  1199: astore_2       
        //  1200: aload           31
        //  1202: astore          26
        //  1204: aload           30
        //  1206: astore          27
        //  1208: goto            2355
        //  1211: astore          26
        //  1213: aload           32
        //  1215: astore_1       
        //  1216: aload_2        
        //  1217: astore          28
        //  1219: aload           26
        //  1221: astore_2       
        //  1222: aload           31
        //  1224: astore          26
        //  1226: iload           5
        //  1228: istore          4
        //  1230: aload           30
        //  1232: astore          27
        //  1234: goto            2407
        //  1237: aload           32
        //  1239: astore          27
        //  1241: iload           8
        //  1243: istore          4
        //  1245: iload           9
        //  1247: istore          5
        //  1249: iload           10
        //  1251: istore          6
        //  1253: aload_1        
        //  1254: invokevirtual   net/lingala/zip4j/model/ZipModel.getEndCentralDirRecord:()Lnet/lingala/zip4j/model/EndCentralDirRecord;
        //  1257: aload           27
        //  1259: checkcast       Lnet/lingala/zip4j/io/SplitOutputStream;
        //  1262: invokevirtual   net/lingala/zip4j/io/SplitOutputStream.getFilePointer:()J
        //  1265: invokevirtual   net/lingala/zip4j/model/EndCentralDirRecord.setOffsetOfStartOfCentralDir:(J)V
        //  1268: iload           8
        //  1270: istore          4
        //  1272: iload           9
        //  1274: istore          5
        //  1276: iload           10
        //  1278: istore          6
        //  1280: aload_1        
        //  1281: invokevirtual   net/lingala/zip4j/model/ZipModel.getEndCentralDirRecord:()Lnet/lingala/zip4j/model/EndCentralDirRecord;
        //  1284: aload_1        
        //  1285: invokevirtual   net/lingala/zip4j/model/ZipModel.getEndCentralDirRecord:()Lnet/lingala/zip4j/model/EndCentralDirRecord;
        //  1288: invokevirtual   net/lingala/zip4j/model/EndCentralDirRecord.getTotNoOfEntriesInCentralDir:()I
        //  1291: iconst_1       
        //  1292: isub           
        //  1293: invokevirtual   net/lingala/zip4j/model/EndCentralDirRecord.setTotNoOfEntriesInCentralDir:(I)V
        //  1296: iload           8
        //  1298: istore          4
        //  1300: iload           9
        //  1302: istore          5
        //  1304: iload           10
        //  1306: istore          6
        //  1308: aload_1        
        //  1309: invokevirtual   net/lingala/zip4j/model/ZipModel.getEndCentralDirRecord:()Lnet/lingala/zip4j/model/EndCentralDirRecord;
        //  1312: aload_1        
        //  1313: invokevirtual   net/lingala/zip4j/model/ZipModel.getEndCentralDirRecord:()Lnet/lingala/zip4j/model/EndCentralDirRecord;
        //  1316: invokevirtual   net/lingala/zip4j/model/EndCentralDirRecord.getTotNoOfEntriesInCentralDirOnThisDisk:()I
        //  1319: iconst_1       
        //  1320: isub           
        //  1321: invokevirtual   net/lingala/zip4j/model/EndCentralDirRecord.setTotNoOfEntriesInCentralDirOnThisDisk:(I)V
        //  1324: iload           8
        //  1326: istore          4
        //  1328: iload           9
        //  1330: istore          5
        //  1332: iload           10
        //  1334: istore          6
        //  1336: aload_1        
        //  1337: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //  1340: invokevirtual   net/lingala/zip4j/model/CentralDirectory.getFileHeaders:()Ljava/util/ArrayList;
        //  1343: iload           7
        //  1345: invokevirtual   java/util/ArrayList.remove:(I)Ljava/lang/Object;
        //  1348: pop            
        //  1349: iload           8
        //  1351: istore          4
        //  1353: iload           9
        //  1355: istore          5
        //  1357: iload           10
        //  1359: istore          6
        //  1361: aload_1        
        //  1362: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //  1365: invokevirtual   net/lingala/zip4j/model/CentralDirectory.getFileHeaders:()Ljava/util/ArrayList;
        //  1368: invokevirtual   java/util/ArrayList.size:()I
        //  1371: istore          14
        //  1373: iload           7
        //  1375: iload           14
        //  1377: if_icmpge       1570
        //  1380: aload_1        
        //  1381: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //  1384: invokevirtual   net/lingala/zip4j/model/CentralDirectory.getFileHeaders:()Ljava/util/ArrayList;
        //  1387: iload           7
        //  1389: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  1392: checkcast       Lnet/lingala/zip4j/model/FileHeader;
        //  1395: invokevirtual   net/lingala/zip4j/model/FileHeader.getOffsetLocalHeader:()J
        //  1398: lstore          24
        //  1400: lload           24
        //  1402: lstore          22
        //  1404: aload_1        
        //  1405: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //  1408: invokevirtual   net/lingala/zip4j/model/CentralDirectory.getFileHeaders:()Ljava/util/ArrayList;
        //  1411: iload           7
        //  1413: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  1416: checkcast       Lnet/lingala/zip4j/model/FileHeader;
        //  1419: invokevirtual   net/lingala/zip4j/model/FileHeader.getZip64ExtendedInfo:()Lnet/lingala/zip4j/model/Zip64ExtendedInfo;
        //  1422: ifnull          1480
        //  1425: lload           24
        //  1427: lstore          22
        //  1429: aload_1        
        //  1430: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //  1433: invokevirtual   net/lingala/zip4j/model/CentralDirectory.getFileHeaders:()Ljava/util/ArrayList;
        //  1436: iload           7
        //  1438: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  1441: checkcast       Lnet/lingala/zip4j/model/FileHeader;
        //  1444: invokevirtual   net/lingala/zip4j/model/FileHeader.getZip64ExtendedInfo:()Lnet/lingala/zip4j/model/Zip64ExtendedInfo;
        //  1447: invokevirtual   net/lingala/zip4j/model/Zip64ExtendedInfo.getOffsetLocalHeader:()J
        //  1450: ldc2_w          -1
        //  1453: lcmp           
        //  1454: ifeq            1480
        //  1457: aload_1        
        //  1458: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //  1461: invokevirtual   net/lingala/zip4j/model/CentralDirectory.getFileHeaders:()Ljava/util/ArrayList;
        //  1464: iload           7
        //  1466: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  1469: checkcast       Lnet/lingala/zip4j/model/FileHeader;
        //  1472: invokevirtual   net/lingala/zip4j/model/FileHeader.getZip64ExtendedInfo:()Lnet/lingala/zip4j/model/Zip64ExtendedInfo;
        //  1475: invokevirtual   net/lingala/zip4j/model/Zip64ExtendedInfo.getOffsetLocalHeader:()J
        //  1478: lstore          22
        //  1480: aload_1        
        //  1481: invokevirtual   net/lingala/zip4j/model/ZipModel.getCentralDirectory:()Lnet/lingala/zip4j/model/CentralDirectory;
        //  1484: invokevirtual   net/lingala/zip4j/model/CentralDirectory.getFileHeaders:()Ljava/util/ArrayList;
        //  1487: iload           7
        //  1489: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //  1492: checkcast       Lnet/lingala/zip4j/model/FileHeader;
        //  1495: lload           22
        //  1497: lload           18
        //  1499: lload           20
        //  1501: lsub           
        //  1502: lsub           
        //  1503: lconst_1       
        //  1504: lsub           
        //  1505: invokevirtual   net/lingala/zip4j/model/FileHeader.setOffsetLocalHeader:(J)V
        //  1508: iload           7
        //  1510: iconst_1       
        //  1511: iadd           
        //  1512: istore          7
        //  1514: goto            1349
        //  1517: astore_1       
        //  1518: aload_2        
        //  1519: astore          28
        //  1521: aload           27
        //  1523: astore_2       
        //  1524: aload           31
        //  1526: astore_3       
        //  1527: iload           13
        //  1529: istore          4
        //  1531: aload           30
        //  1533: astore          26
        //  1535: goto            2471
        //  1538: astore_1       
        //  1539: aload_2        
        //  1540: astore          29
        //  1542: aload_1        
        //  1543: astore_2       
        //  1544: aload           27
        //  1546: astore_1       
        //  1547: iload           11
        //  1549: istore          4
        //  1551: goto            1940
        //  1554: astore_1       
        //  1555: aload_2        
        //  1556: astore          29
        //  1558: aload_1        
        //  1559: astore_2       
        //  1560: aload           27
        //  1562: astore_1       
        //  1563: iload           12
        //  1565: istore          4
        //  1567: goto            1959
        //  1570: iload           8
        //  1572: istore          4
        //  1574: iload           9
        //  1576: istore          5
        //  1578: iload           10
        //  1580: istore          6
        //  1582: new             Lnet/lingala/zip4j/core/HeaderWriter;
        //  1585: dup            
        //  1586: invokespecial   net/lingala/zip4j/core/HeaderWriter.<init>:()V
        //  1589: aload_1        
        //  1590: aload           27
        //  1592: invokevirtual   net/lingala/zip4j/core/HeaderWriter.finalizeZipFile:(Lnet/lingala/zip4j/model/ZipModel;Ljava/io/OutputStream;)V
        //  1595: iconst_1       
        //  1596: istore          5
        //  1598: iconst_1       
        //  1599: istore          6
        //  1601: iconst_1       
        //  1602: istore          9
        //  1604: iconst_1       
        //  1605: istore          8
        //  1607: iconst_1       
        //  1608: istore          7
        //  1610: iconst_1       
        //  1611: istore          4
        //  1613: aload_1        
        //  1614: invokevirtual   net/lingala/zip4j/model/ZipModel.getEndCentralDirRecord:()Lnet/lingala/zip4j/model/EndCentralDirRecord;
        //  1617: invokevirtual   net/lingala/zip4j/model/EndCentralDirRecord.getOffsetOfStartOfCentralDir:()J
        //  1620: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //  1623: astore          37
        //  1625: aload_2        
        //  1626: astore_1       
        //  1627: aload           27
        //  1629: astore          32
        //  1631: iload           9
        //  1633: istore          4
        //  1635: aload_2        
        //  1636: astore          34
        //  1638: aload           27
        //  1640: astore          33
        //  1642: iload           8
        //  1644: istore          5
        //  1646: aload_2        
        //  1647: astore          29
        //  1649: aload           27
        //  1651: astore          35
        //  1653: iload           7
        //  1655: istore          6
        //  1657: aload           36
        //  1659: ldc_w           "offsetCentralDir"
        //  1662: aload           37
        //  1664: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1667: pop            
        //  1668: aload_2        
        //  1669: ifnull          1679
        //  1672: aload_2        
        //  1673: invokevirtual   java/io/RandomAccessFile.close:()V
        //  1676: goto            1679
        //  1679: aload           27
        //  1681: ifnull          1703
        //  1684: aload           27
        //  1686: invokevirtual   java/io/OutputStream.close:()V
        //  1689: goto            1703
        //  1692: new             Lnet/lingala/zip4j/exception/ZipException;
        //  1695: dup            
        //  1696: ldc_w           "cannot close input stream or output stream when trying to delete a file from zip file"
        //  1699: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/String;)V
        //  1702: athrow         
        //  1703: iconst_1       
        //  1704: ifeq            1718
        //  1707: aload_0        
        //  1708: aload           30
        //  1710: aload           31
        //  1712: invokespecial   net/lingala/zip4j/util/ArchiveMaintainer.restoreFileName:(Ljava/io/File;Ljava/lang/String;)V
        //  1715: aload           36
        //  1717: areturn        
        //  1718: new             Ljava/io/File;
        //  1721: dup            
        //  1722: aload           31
        //  1724: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //  1727: invokevirtual   java/io/File.delete:()Z
        //  1730: pop            
        //  1731: aload           36
        //  1733: areturn        
        //  1734: astore_1       
        //  1735: aload_2        
        //  1736: astore          28
        //  1738: aload           27
        //  1740: astore_2       
        //  1741: aload           31
        //  1743: astore_3       
        //  1744: aload           30
        //  1746: astore          26
        //  1748: goto            2471
        //  1751: astore_1       
        //  1752: aload_2        
        //  1753: astore          28
        //  1755: aload_1        
        //  1756: astore_2       
        //  1757: aload           27
        //  1759: astore_1       
        //  1760: aload           31
        //  1762: astore          26
        //  1764: iload           5
        //  1766: istore          4
        //  1768: aload           30
        //  1770: astore          27
        //  1772: goto            2355
        //  1775: astore_1       
        //  1776: aload_2        
        //  1777: astore          28
        //  1779: aload_1        
        //  1780: astore_2       
        //  1781: aload           27
        //  1783: astore_1       
        //  1784: aload           31
        //  1786: astore          26
        //  1788: iload           6
        //  1790: istore          4
        //  1792: aload           30
        //  1794: astore          27
        //  1796: goto            2407
        //  1799: aload           28
        //  1801: astore          30
        //  1803: aload           26
        //  1805: astore_3       
        //  1806: astore_1       
        //  1807: aload           27
        //  1809: astore_2       
        //  1810: aload           29
        //  1812: astore          28
        //  1814: iload           14
        //  1816: istore          4
        //  1818: aload           30
        //  1820: astore          26
        //  1822: goto            2471
        //  1825: aload           28
        //  1827: astore          30
        //  1829: astore_2       
        //  1830: aload           27
        //  1832: astore_1       
        //  1833: aload           29
        //  1835: astore          28
        //  1837: aload           30
        //  1839: astore          27
        //  1841: goto            2355
        //  1844: aload           28
        //  1846: astore          30
        //  1848: astore_2       
        //  1849: aload           27
        //  1851: astore_1       
        //  1852: aload           29
        //  1854: astore          28
        //  1856: iload           5
        //  1858: istore          4
        //  1860: aload           30
        //  1862: astore          27
        //  1864: goto            2407
        //  1867: aload           29
        //  1869: astore_1       
        //  1870: aload           27
        //  1872: astore          32
        //  1874: iload           16
        //  1876: istore          4
        //  1878: aload           29
        //  1880: astore          34
        //  1882: aload           27
        //  1884: astore          33
        //  1886: iload           15
        //  1888: istore          5
        //  1890: aload           27
        //  1892: astore          35
        //  1894: new             Lnet/lingala/zip4j/exception/ZipException;
        //  1897: dup            
        //  1898: ldc_w           "invalid offset for start and end of local file, cannot remove file"
        //  1901: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/String;)V
        //  1904: athrow         
        //  1905: aload           28
        //  1907: astore          27
        //  1909: aload           26
        //  1911: astore_3       
        //  1912: astore_2       
        //  1913: aload_1        
        //  1914: astore          28
        //  1916: aload_2        
        //  1917: astore_1       
        //  1918: aload           32
        //  1920: astore_2       
        //  1921: aload           27
        //  1923: astore          26
        //  1925: goto            2471
        //  1928: astore_2       
        //  1929: aload           34
        //  1931: astore          29
        //  1933: iload           5
        //  1935: istore          4
        //  1937: aload           33
        //  1939: astore_1       
        //  1940: aload           28
        //  1942: astore          27
        //  1944: aload           29
        //  1946: astore          28
        //  1948: goto            2355
        //  1951: astore_2       
        //  1952: iload           6
        //  1954: istore          4
        //  1956: aload           35
        //  1958: astore_1       
        //  1959: aload           28
        //  1961: astore          27
        //  1963: aload           29
        //  1965: astore          28
        //  1967: goto            2407
        //  1970: astore_1       
        //  1971: aload           26
        //  1973: astore_3       
        //  1974: aload           28
        //  1976: astore          26
        //  1978: aload           27
        //  1980: astore_2       
        //  1981: aload           29
        //  1983: astore          28
        //  1985: iload           14
        //  1987: istore          4
        //  1989: goto            2471
        //  1992: astore_2       
        //  1993: aload           28
        //  1995: astore          30
        //  1997: aload           27
        //  1999: astore_1       
        //  2000: aload           29
        //  2002: astore          28
        //  2004: aload           30
        //  2006: astore          27
        //  2008: goto            2355
        //  2011: astore_2       
        //  2012: aload           28
        //  2014: astore          30
        //  2016: aload           27
        //  2018: astore_1       
        //  2019: aload           29
        //  2021: astore          28
        //  2023: iload           5
        //  2025: istore          4
        //  2027: aload           30
        //  2029: astore          27
        //  2031: goto            2407
        //  2034: astore_1       
        //  2035: aload           26
        //  2037: astore_3       
        //  2038: aload           28
        //  2040: astore          26
        //  2042: aload           27
        //  2044: astore_2       
        //  2045: aload           32
        //  2047: astore          28
        //  2049: iload           14
        //  2051: istore          4
        //  2053: goto            2471
        //  2056: astore_2       
        //  2057: aload           28
        //  2059: astore          29
        //  2061: aload           27
        //  2063: astore_1       
        //  2064: aload           30
        //  2066: astore          28
        //  2068: aload           29
        //  2070: astore          27
        //  2072: goto            2355
        //  2075: astore_2       
        //  2076: aload           28
        //  2078: astore          29
        //  2080: aload           27
        //  2082: astore_1       
        //  2083: aload           31
        //  2085: astore          28
        //  2087: iload           5
        //  2089: istore          4
        //  2091: aload           29
        //  2093: astore          27
        //  2095: goto            2407
        //  2098: astore_1       
        //  2099: aload           27
        //  2101: astore_2       
        //  2102: aload           26
        //  2104: astore_3       
        //  2105: aconst_null    
        //  2106: astore          26
        //  2108: aload           32
        //  2110: astore          28
        //  2112: iload           14
        //  2114: istore          4
        //  2116: goto            2471
        //  2119: astore_2       
        //  2120: aload           27
        //  2122: astore_1       
        //  2123: aconst_null    
        //  2124: astore          27
        //  2126: aload           30
        //  2128: astore          28
        //  2130: goto            2355
        //  2133: astore_2       
        //  2134: aload           27
        //  2136: astore_1       
        //  2137: aconst_null    
        //  2138: astore          27
        //  2140: aload           31
        //  2142: astore          28
        //  2144: iload           5
        //  2146: istore          4
        //  2148: goto            2407
        //  2151: astore_1       
        //  2152: new             Lnet/lingala/zip4j/exception/ZipException;
        //  2155: dup            
        //  2156: aload_1        
        //  2157: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/Throwable;)V
        //  2160: athrow         
        //  2161: astore_1       
        //  2162: aconst_null    
        //  2163: astore_2       
        //  2164: aconst_null    
        //  2165: astore          27
        //  2167: aload           32
        //  2169: astore          28
        //  2171: aload           26
        //  2173: astore_3       
        //  2174: iload           14
        //  2176: istore          4
        //  2178: aload           27
        //  2180: astore          26
        //  2182: goto            2471
        //  2185: astore_2       
        //  2186: aconst_null    
        //  2187: astore_1       
        //  2188: aconst_null    
        //  2189: astore          27
        //  2191: aload           30
        //  2193: astore          28
        //  2195: goto            2355
        //  2198: astore_2       
        //  2199: aconst_null    
        //  2200: astore_1       
        //  2201: aconst_null    
        //  2202: astore          27
        //  2204: aload           31
        //  2206: astore          28
        //  2208: iload           5
        //  2210: istore          4
        //  2212: goto            2407
        //  2215: astore_1       
        //  2216: aload           26
        //  2218: astore_3       
        //  2219: aconst_null    
        //  2220: astore_2       
        //  2221: aconst_null    
        //  2222: astore          26
        //  2224: aload           32
        //  2226: astore          28
        //  2228: iload           14
        //  2230: istore          4
        //  2232: goto            2471
        //  2235: astore_2       
        //  2236: aconst_null    
        //  2237: astore_1       
        //  2238: aconst_null    
        //  2239: astore          27
        //  2241: aload           30
        //  2243: astore          28
        //  2245: goto            2355
        //  2248: astore_2       
        //  2249: aconst_null    
        //  2250: astore_1       
        //  2251: aconst_null    
        //  2252: astore          27
        //  2254: aload           31
        //  2256: astore          28
        //  2258: iload           5
        //  2260: istore          4
        //  2262: goto            2407
        //  2265: astore_1       
        //  2266: aload           27
        //  2268: astore_3       
        //  2269: aconst_null    
        //  2270: astore_2       
        //  2271: aconst_null    
        //  2272: astore          26
        //  2274: aload           32
        //  2276: astore          28
        //  2278: iload           14
        //  2280: istore          4
        //  2282: goto            2471
        //  2285: astore_2       
        //  2286: aload           28
        //  2288: astore          26
        //  2290: aconst_null    
        //  2291: astore_1       
        //  2292: aconst_null    
        //  2293: astore          27
        //  2295: aload           30
        //  2297: astore          28
        //  2299: goto            2355
        //  2302: astore_2       
        //  2303: aload           29
        //  2305: astore          26
        //  2307: aconst_null    
        //  2308: astore_1       
        //  2309: aconst_null    
        //  2310: astore          27
        //  2312: aload           31
        //  2314: astore          28
        //  2316: iload           5
        //  2318: istore          4
        //  2320: goto            2407
        //  2323: astore_1       
        //  2324: aconst_null    
        //  2325: astore_3       
        //  2326: aconst_null    
        //  2327: astore_2       
        //  2328: aconst_null    
        //  2329: astore          26
        //  2331: aload           32
        //  2333: astore          28
        //  2335: iload           14
        //  2337: istore          4
        //  2339: goto            2471
        //  2342: astore_2       
        //  2343: aconst_null    
        //  2344: astore          26
        //  2346: aconst_null    
        //  2347: astore_1       
        //  2348: aconst_null    
        //  2349: astore          27
        //  2351: aload           30
        //  2353: astore          28
        //  2355: aload           28
        //  2357: astore          29
        //  2359: aload_1        
        //  2360: astore          30
        //  2362: aload           26
        //  2364: astore          31
        //  2366: iload           4
        //  2368: istore          5
        //  2370: aload           27
        //  2372: astore          32
        //  2374: aload_3        
        //  2375: aload_2        
        //  2376: invokevirtual   net/lingala/zip4j/progress/ProgressMonitor.endProgressMonitorError:(Ljava/lang/Throwable;)V
        //  2379: aload           28
        //  2381: astore          29
        //  2383: aload_1        
        //  2384: astore          30
        //  2386: aload           26
        //  2388: astore          31
        //  2390: iload           4
        //  2392: istore          5
        //  2394: aload           27
        //  2396: astore          32
        //  2398: new             Lnet/lingala/zip4j/exception/ZipException;
        //  2401: dup            
        //  2402: aload_2        
        //  2403: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/Throwable;)V
        //  2406: athrow         
        //  2407: aload           28
        //  2409: astore          29
        //  2411: aload_1        
        //  2412: astore          30
        //  2414: aload           26
        //  2416: astore          31
        //  2418: iload           4
        //  2420: istore          5
        //  2422: aload           27
        //  2424: astore          32
        //  2426: aload_3        
        //  2427: aload_2        
        //  2428: invokevirtual   net/lingala/zip4j/progress/ProgressMonitor.endProgressMonitorError:(Ljava/lang/Throwable;)V
        //  2431: aload           28
        //  2433: astore          29
        //  2435: aload_1        
        //  2436: astore          30
        //  2438: aload           26
        //  2440: astore          31
        //  2442: iload           4
        //  2444: istore          5
        //  2446: aload           27
        //  2448: astore          32
        //  2450: aload_2        
        //  2451: athrow         
        //  2452: astore_1       
        //  2453: aload           32
        //  2455: astore          26
        //  2457: iload           5
        //  2459: istore          4
        //  2461: aload           31
        //  2463: astore_3       
        //  2464: aload           30
        //  2466: astore_2       
        //  2467: aload           29
        //  2469: astore          28
        //  2471: aload           28
        //  2473: ifnull          2484
        //  2476: aload           28
        //  2478: invokevirtual   java/io/RandomAccessFile.close:()V
        //  2481: goto            2484
        //  2484: aload_2        
        //  2485: ifnull          2506
        //  2488: aload_2        
        //  2489: invokevirtual   java/io/OutputStream.close:()V
        //  2492: goto            2506
        //  2495: new             Lnet/lingala/zip4j/exception/ZipException;
        //  2498: dup            
        //  2499: ldc_w           "cannot close input stream or output stream when trying to delete a file from zip file"
        //  2502: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/String;)V
        //  2505: athrow         
        //  2506: iload           4
        //  2508: ifeq            2521
        //  2511: aload_0        
        //  2512: aload           26
        //  2514: aload_3        
        //  2515: invokespecial   net/lingala/zip4j/util/ArchiveMaintainer.restoreFileName:(Ljava/io/File;Ljava/lang/String;)V
        //  2518: goto            2533
        //  2521: new             Ljava/io/File;
        //  2524: dup            
        //  2525: aload_3        
        //  2526: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //  2529: invokevirtual   java/io/File.delete:()Z
        //  2532: pop            
        //  2533: aload_1        
        //  2534: athrow         
        //  2535: new             Lnet/lingala/zip4j/exception/ZipException;
        //  2538: dup            
        //  2539: ldc_w           "input parameters is null in maintain zip file, cannot remove file from archive"
        //  2542: invokespecial   net/lingala/zip4j/exception/ZipException.<init>:(Ljava/lang/String;)V
        //  2545: athrow         
        //  2546: astore_1       
        //  2547: goto            1692
        //  2550: astore_2       
        //  2551: aconst_null    
        //  2552: astore          26
        //  2554: aconst_null    
        //  2555: astore_1       
        //  2556: aconst_null    
        //  2557: astore          27
        //  2559: aload           31
        //  2561: astore          28
        //  2563: iload           5
        //  2565: istore          4
        //  2567: goto            2407
        //  2570: astore_1       
        //  2571: goto            2495
        //    Exceptions:
        //  throws net.lingala.zip4j.exception.ZipException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                      
        //  -----  -----  -----  -----  ------------------------------------------
        //  74     81     2550   2570   Lnet/lingala/zip4j/exception/ZipException;
        //  74     81     2342   2355   Ljava/lang/Exception;
        //  74     81     2323   2342   Any
        //  86     97     132    152    Lnet/lingala/zip4j/exception/ZipException;
        //  86     97     116    132    Ljava/lang/Exception;
        //  86     97     97     116    Any
        //  152    158    2550   2570   Lnet/lingala/zip4j/exception/ZipException;
        //  152    158    2342   2355   Ljava/lang/Exception;
        //  152    158    2323   2342   Any
        //  163    174    132    152    Lnet/lingala/zip4j/exception/ZipException;
        //  163    174    116    132    Ljava/lang/Exception;
        //  163    174    97     116    Any
        //  174    198    2550   2570   Lnet/lingala/zip4j/exception/ZipException;
        //  174    198    2342   2355   Ljava/lang/Exception;
        //  174    198    2323   2342   Any
        //  210    222    2302   2323   Lnet/lingala/zip4j/exception/ZipException;
        //  210    222    2285   2302   Ljava/lang/Exception;
        //  210    222    2265   2285   Any
        //  234    241    2302   2323   Lnet/lingala/zip4j/exception/ZipException;
        //  234    241    2285   2302   Ljava/lang/Exception;
        //  234    241    2265   2285   Any
        //  253    264    2302   2323   Lnet/lingala/zip4j/exception/ZipException;
        //  253    264    2285   2302   Ljava/lang/Exception;
        //  253    264    2265   2285   Any
        //  268    275    2248   2265   Lnet/lingala/zip4j/exception/ZipException;
        //  268    275    2235   2248   Ljava/lang/Exception;
        //  268    275    2215   2235   Any
        //  292    297    450    471    Lnet/lingala/zip4j/exception/ZipException;
        //  292    297    433    450    Ljava/lang/Exception;
        //  292    297    413    433    Any
        //  309    318    450    471    Lnet/lingala/zip4j/exception/ZipException;
        //  309    318    433    450    Ljava/lang/Exception;
        //  309    318    413    433    Any
        //  330    340    450    471    Lnet/lingala/zip4j/exception/ZipException;
        //  330    340    433    450    Ljava/lang/Exception;
        //  330    340    413    433    Any
        //  352    364    450    471    Lnet/lingala/zip4j/exception/ZipException;
        //  352    364    433    450    Ljava/lang/Exception;
        //  352    364    413    433    Any
        //  376    383    450    471    Lnet/lingala/zip4j/exception/ZipException;
        //  376    383    433    450    Ljava/lang/Exception;
        //  376    383    413    433    Any
        //  395    406    450    471    Lnet/lingala/zip4j/exception/ZipException;
        //  395    406    433    450    Ljava/lang/Exception;
        //  395    406    413    433    Any
        //  471    489    2151   2215   Ljava/io/FileNotFoundException;
        //  471    489    2248   2265   Lnet/lingala/zip4j/exception/ZipException;
        //  471    489    2235   2248   Ljava/lang/Exception;
        //  471    489    2215   2235   Any
        //  489    502    2133   2151   Lnet/lingala/zip4j/exception/ZipException;
        //  489    502    2119   2133   Ljava/lang/Exception;
        //  489    502    2098   2119   Any
        //  502    511    2075   2098   Lnet/lingala/zip4j/exception/ZipException;
        //  502    511    2056   2075   Ljava/lang/Exception;
        //  502    511    2034   2056   Any
        //  511    526    2011   2034   Lnet/lingala/zip4j/exception/ZipException;
        //  511    526    1992   2011   Ljava/lang/Exception;
        //  511    526    1970   1992   Any
        //  531    542    585    610    Lnet/lingala/zip4j/exception/ZipException;
        //  531    542    564    585    Ljava/lang/Exception;
        //  531    542    542    564    Any
        //  610    622    2011   2034   Lnet/lingala/zip4j/exception/ZipException;
        //  610    622    1992   2011   Ljava/lang/Exception;
        //  610    622    1970   1992   Any
        //  631    654    585    610    Lnet/lingala/zip4j/exception/ZipException;
        //  631    654    564    585    Ljava/lang/Exception;
        //  631    654    542    564    Any
        //  666    681    2011   2034   Lnet/lingala/zip4j/exception/ZipException;
        //  666    681    1992   2011   Ljava/lang/Exception;
        //  666    681    1970   1992   Any
        //  686    702    585    610    Lnet/lingala/zip4j/exception/ZipException;
        //  686    702    564    585    Ljava/lang/Exception;
        //  686    702    542    564    Any
        //  705    726    2011   2034   Lnet/lingala/zip4j/exception/ZipException;
        //  705    726    1992   2011   Ljava/lang/Exception;
        //  705    726    1970   1992   Any
        //  735    748    2011   2034   Lnet/lingala/zip4j/exception/ZipException;
        //  735    748    1992   2011   Ljava/lang/Exception;
        //  735    748    1970   1992   Any
        //  752    760    585    610    Lnet/lingala/zip4j/exception/ZipException;
        //  752    760    564    585    Ljava/lang/Exception;
        //  752    760    542    564    Any
        //  764    771    585    610    Lnet/lingala/zip4j/exception/ZipException;
        //  764    771    564    585    Ljava/lang/Exception;
        //  764    771    542    564    Any
        //  775    798    585    610    Lnet/lingala/zip4j/exception/ZipException;
        //  775    798    564    585    Ljava/lang/Exception;
        //  775    798    542    564    Any
        //  826    838    906    929    Lnet/lingala/zip4j/exception/ZipException;
        //  826    838    887    906    Ljava/lang/Exception;
        //  826    838    865    887    Any
        //  844    859    1011   1034   Lnet/lingala/zip4j/exception/ZipException;
        //  844    859    992    1011   Ljava/lang/Exception;
        //  844    859    966    992    Any
        //  936    943    1844   1867   Lnet/lingala/zip4j/exception/ZipException;
        //  936    943    1825   1844   Ljava/lang/Exception;
        //  936    943    1799   1825   Any
        //  952    963    1011   1034   Lnet/lingala/zip4j/exception/ZipException;
        //  952    963    992    1011   Ljava/lang/Exception;
        //  952    963    966    992    Any
        //  1034   1059   1844   1867   Lnet/lingala/zip4j/exception/ZipException;
        //  1034   1059   1825   1844   Ljava/lang/Exception;
        //  1034   1059   1799   1825   Any
        //  1078   1084   1844   1867   Lnet/lingala/zip4j/exception/ZipException;
        //  1078   1084   1825   1844   Ljava/lang/Exception;
        //  1078   1084   1799   1825   Any
        //  1089   1099   1211   1237   Lnet/lingala/zip4j/exception/ZipException;
        //  1089   1099   1189   1211   Ljava/lang/Exception;
        //  1089   1099   1168   1189   Any
        //  1103   1107   1110   1114   Ljava/io/IOException;
        //  1119   1124   1127   1128   Ljava/io/IOException;
        //  1253   1268   1775   1799   Lnet/lingala/zip4j/exception/ZipException;
        //  1253   1268   1751   1775   Ljava/lang/Exception;
        //  1253   1268   1734   1751   Any
        //  1280   1296   1775   1799   Lnet/lingala/zip4j/exception/ZipException;
        //  1280   1296   1751   1775   Ljava/lang/Exception;
        //  1280   1296   1734   1751   Any
        //  1308   1324   1775   1799   Lnet/lingala/zip4j/exception/ZipException;
        //  1308   1324   1751   1775   Ljava/lang/Exception;
        //  1308   1324   1734   1751   Any
        //  1336   1349   1775   1799   Lnet/lingala/zip4j/exception/ZipException;
        //  1336   1349   1751   1775   Ljava/lang/Exception;
        //  1336   1349   1734   1751   Any
        //  1361   1373   1775   1799   Lnet/lingala/zip4j/exception/ZipException;
        //  1361   1373   1751   1775   Ljava/lang/Exception;
        //  1361   1373   1734   1751   Any
        //  1380   1400   1554   1570   Lnet/lingala/zip4j/exception/ZipException;
        //  1380   1400   1538   1554   Ljava/lang/Exception;
        //  1380   1400   1517   1538   Any
        //  1404   1425   1554   1570   Lnet/lingala/zip4j/exception/ZipException;
        //  1404   1425   1538   1554   Ljava/lang/Exception;
        //  1404   1425   1517   1538   Any
        //  1429   1480   1554   1570   Lnet/lingala/zip4j/exception/ZipException;
        //  1429   1480   1538   1554   Ljava/lang/Exception;
        //  1429   1480   1517   1538   Any
        //  1480   1508   1554   1570   Lnet/lingala/zip4j/exception/ZipException;
        //  1480   1508   1538   1554   Ljava/lang/Exception;
        //  1480   1508   1517   1538   Any
        //  1582   1595   1775   1799   Lnet/lingala/zip4j/exception/ZipException;
        //  1582   1595   1751   1775   Ljava/lang/Exception;
        //  1582   1595   1734   1751   Any
        //  1613   1625   1775   1799   Lnet/lingala/zip4j/exception/ZipException;
        //  1613   1625   1751   1775   Ljava/lang/Exception;
        //  1613   1625   1734   1751   Any
        //  1657   1668   1951   1959   Lnet/lingala/zip4j/exception/ZipException;
        //  1657   1668   1928   1940   Ljava/lang/Exception;
        //  1657   1668   1905   1928   Any
        //  1672   1676   2546   1703   Ljava/io/IOException;
        //  1684   1689   2546   1703   Ljava/io/IOException;
        //  1894   1905   1951   1959   Lnet/lingala/zip4j/exception/ZipException;
        //  1894   1905   1928   1940   Ljava/lang/Exception;
        //  1894   1905   1905   1928   Any
        //  2152   2161   2198   2215   Lnet/lingala/zip4j/exception/ZipException;
        //  2152   2161   2185   2198   Ljava/lang/Exception;
        //  2152   2161   2161   2185   Any
        //  2374   2379   2452   2471   Any
        //  2398   2407   2452   2471   Any
        //  2426   2431   2452   2471   Any
        //  2450   2452   2452   2471   Any
        //  2476   2481   2570   2506   Ljava/io/IOException;
        //  2488   2492   2570   2506   Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 1347, Size: 1347
        //     at java.util.ArrayList.rangeCheck(Unknown Source)
        //     at java.util.ArrayList.get(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
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
    
    public void mergeSplitZipFiles(final ZipModel zipModel, final File file, final ProgressMonitor progressMonitor, final boolean b) throws ZipException {
        if (b) {
            new Thread("Zip4j") {
                @Override
                public void run() {
                    try {
                        ArchiveMaintainer.this.initMergeSplitZipFile(zipModel, file, progressMonitor);
                    }
                    catch (ZipException ex) {}
                }
            }.start();
            return;
        }
        this.initMergeSplitZipFile(zipModel, file, progressMonitor);
    }
    
    public HashMap removeZipFile(final ZipModel zipModel, final FileHeader fileHeader, final ProgressMonitor progressMonitor, final boolean b) throws ZipException {
        if (b) {
            new Thread("Zip4j") {
                @Override
                public void run() {
                    try {
                        ArchiveMaintainer.this.initRemoveZipFile(zipModel, fileHeader, progressMonitor);
                        progressMonitor.endProgressMonitorSuccess();
                    }
                    catch (ZipException ex) {}
                }
            }.start();
            return null;
        }
        final HashMap initRemoveZipFile = this.initRemoveZipFile(zipModel, fileHeader, progressMonitor);
        progressMonitor.endProgressMonitorSuccess();
        return initRemoveZipFile;
    }
    
    public void setComment(final ZipModel zipModel, String s) throws ZipException {
        if (s == null) {
            throw new ZipException("comment is null, cannot update Zip file with comment");
        }
        if (zipModel == null) {
            throw new ZipException("zipModel is null, cannot update Zip file with comment");
        }
        String comment = s;
        byte[] commentBytes = s.getBytes();
        int commentLength = s.length();
        if (Zip4jUtil.isSupportedCharset("windows-1254")) {
            try {
                comment = new String(s.getBytes("windows-1254"), "windows-1254");
                commentBytes = comment.getBytes("windows-1254");
                commentLength = comment.length();
            }
            catch (UnsupportedEncodingException ex) {
                comment = s;
                commentBytes = s.getBytes();
                commentLength = s.length();
            }
        }
        if (commentLength > 65535) {
            throw new ZipException("comment length exceeds maximum length");
        }
        zipModel.getEndCentralDirRecord().setComment(comment);
        zipModel.getEndCentralDirRecord().setCommentBytes(commentBytes);
        zipModel.getEndCentralDirRecord().setCommentLength(commentLength);
        final String s2 = s = null;
        while (true) {
            try {
                try {
                    final HeaderWriter headerWriter = new HeaderWriter();
                    s = s2;
                    final Object o = s = (String)new SplitOutputStream(zipModel.getZipFile());
                    if (zipModel.isZip64Format()) {
                        s = (String)o;
                        ((SplitOutputStream)o).seek(zipModel.getZip64EndCentralDirRecord().getOffsetStartCenDirWRTStartDiskNo());
                    }
                    else {
                        s = (String)o;
                        ((SplitOutputStream)o).seek(zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir());
                    }
                    s = (String)o;
                    headerWriter.finalizeZipFileWithoutValidations(zipModel, (OutputStream)o);
                    if (o != null) {
                        try {
                            ((SplitOutputStream)o).close();
                        }
                        catch (IOException ex2) {}
                    }
                    return;
                }
                finally {
                    if (s != null) {
                        final String s3 = s;
                        ((SplitOutputStream)s3).close();
                    }
                }
            }
            catch (IOException ex3) {}
            catch (FileNotFoundException ex4) {}
            try {
                final String s3 = s;
                ((SplitOutputStream)s3).close();
                continue;
            }
            catch (IOException ex5) {}
            break;
        }
    }
}
