package net.lingala.zip4j.util;

import net.lingala.zip4j.progress.*;
import net.lingala.zip4j.exception.*;
import java.util.zip.*;
import java.io.*;

public class CRCUtil
{
    private static final int BUF_SIZE = 16384;
    
    public static long computeFileCRC(final String s) throws ZipException {
        return computeFileCRC(s, null);
    }
    
    public static long computeFileCRC(final String s, final ProgressMonitor progressMonitor) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("input file is null or empty, cannot calculate CRC for the file");
        }
        FileInputStream fileInputStream2;
        final FileInputStream fileInputStream = fileInputStream2 = null;
        while (true) {
            try {
                try {
                    Zip4jUtil.checkFileReadAccess(s);
                    fileInputStream2 = fileInputStream;
                    final FileInputStream fileInputStream3 = fileInputStream2 = new FileInputStream(new File(s));
                    final byte[] array = new byte[16384];
                    fileInputStream2 = fileInputStream3;
                    final CRC32 crc32 = new CRC32();
                    while (true) {
                        fileInputStream2 = fileInputStream3;
                        final int read = fileInputStream3.read(array);
                        if (read == -1) {
                            fileInputStream2 = fileInputStream3;
                            final long value = crc32.getValue();
                            if (fileInputStream3 != null) {
                                try {
                                    fileInputStream3.close();
                                    return value;
                                }
                                catch (IOException ex) {
                                    throw new ZipException("error while closing the file after calculating crc");
                                }
                            }
                            return value;
                        }
                        fileInputStream2 = fileInputStream3;
                        crc32.update(array, 0, read);
                        if (progressMonitor == null) {
                            continue;
                        }
                        fileInputStream2 = fileInputStream3;
                        progressMonitor.updateWorkCompleted(read);
                        fileInputStream2 = fileInputStream3;
                        if (progressMonitor.isCancelAllTasks()) {
                            fileInputStream2 = fileInputStream3;
                            progressMonitor.setResult(3);
                            fileInputStream2 = fileInputStream3;
                            progressMonitor.setState(0);
                            if (fileInputStream3 != null) {
                                try {
                                    fileInputStream3.close();
                                    return 0L;
                                }
                                catch (IOException ex2) {
                                    throw new ZipException("error while closing the file after calculating crc");
                                }
                            }
                            return 0L;
                        }
                    }
                }
                finally {
                    if (fileInputStream2 != null) {
                        final FileInputStream fileInputStream4 = fileInputStream2;
                        fileInputStream4.close();
                    }
                }
            }
            catch (Exception ex3) {}
            catch (IOException ex4) {}
            try {
                final FileInputStream fileInputStream4 = fileInputStream2;
                fileInputStream4.close();
                continue;
            }
            catch (IOException ex5) {}
            break;
        }
    }
}
