package net.lingala.zip4j.unzip;

import java.io.*;
import net.lingala.zip4j.model.*;
import net.lingala.zip4j.exception.*;
import net.lingala.zip4j.util.*;

public class UnzipUtil
{
    public static void applyFileAttributes(final FileHeader fileHeader, final File file) throws ZipException {
        applyFileAttributes(fileHeader, file, null);
    }
    
    public static void applyFileAttributes(final FileHeader fileHeader, final File file, final UnzipParameters unzipParameters) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("cannot set file properties: file header is null");
        }
        if (file == null) {
            throw new ZipException("cannot set file properties: output file is null");
        }
        if (!Zip4jUtil.checkFileExists(file)) {
            throw new ZipException("cannot set file properties: file doesnot exist");
        }
        if (unzipParameters == null || !unzipParameters.isIgnoreDateTimeAttributes()) {
            setFileLastModifiedTime(fileHeader, file);
        }
        if (unzipParameters == null) {
            setFileAttributes(fileHeader, file, true, true, true, true);
            return;
        }
        if (unzipParameters.isIgnoreAllFileAttributes()) {
            setFileAttributes(fileHeader, file, false, false, false, false);
            return;
        }
        setFileAttributes(fileHeader, file, unzipParameters.isIgnoreReadOnlyFileAttribute() ^ true, unzipParameters.isIgnoreHiddenFileAttribute() ^ true, unzipParameters.isIgnoreArchiveFileAttribute() ^ true, unzipParameters.isIgnoreSystemFileAttribute() ^ true);
    }
    
    private static void setFileAttributes(final FileHeader fileHeader, final File fileHidden, final boolean b, final boolean b2, final boolean b3, final boolean b4) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("invalid file header. cannot set file attributes");
        }
        final byte[] externalFileAttr = fileHeader.getExternalFileAttr();
        if (externalFileAttr == null) {
            return;
        }
        final byte b5 = externalFileAttr[0];
        Label_0241: {
            if (b5 != 18) {
                if (b5 != 38) {
                    Label_0205: {
                        if (b5 != 48) {
                            Label_0187: {
                                if (b5 != 50) {
                                    switch (b5) {
                                        default: {
                                            switch (b5) {
                                                default: {
                                                    return;
                                                }
                                                case 35: {
                                                    if (b3) {
                                                        Zip4jUtil.setFileArchive(fileHidden);
                                                    }
                                                    if (b) {
                                                        Zip4jUtil.setFileReadOnly(fileHidden);
                                                    }
                                                    if (b2) {
                                                        Zip4jUtil.setFileHidden(fileHidden);
                                                    }
                                                    return;
                                                }
                                                case 33: {
                                                    if (b3) {
                                                        Zip4jUtil.setFileArchive(fileHidden);
                                                    }
                                                    if (b) {
                                                        Zip4jUtil.setFileReadOnly(fileHidden);
                                                    }
                                                    return;
                                                }
                                                case 34: {
                                                    break Label_0187;
                                                }
                                                case 32: {
                                                    break Label_0205;
                                                }
                                            }
                                            break;
                                        }
                                        case 3: {
                                            if (b) {
                                                Zip4jUtil.setFileReadOnly(fileHidden);
                                            }
                                            if (b2) {
                                                Zip4jUtil.setFileHidden(fileHidden);
                                            }
                                            return;
                                        }
                                        case 1: {
                                            if (b) {
                                                Zip4jUtil.setFileReadOnly(fileHidden);
                                            }
                                            return;
                                        }
                                        case 2: {
                                            break Label_0241;
                                        }
                                    }
                                }
                            }
                            if (b3) {
                                Zip4jUtil.setFileArchive(fileHidden);
                            }
                            if (b2) {
                                Zip4jUtil.setFileHidden(fileHidden);
                            }
                            return;
                        }
                    }
                    if (b3) {
                        Zip4jUtil.setFileArchive(fileHidden);
                    }
                    return;
                }
                else {
                    if (b) {
                        Zip4jUtil.setFileReadOnly(fileHidden);
                    }
                    if (b2) {
                        Zip4jUtil.setFileHidden(fileHidden);
                    }
                    if (b4) {
                        Zip4jUtil.setFileSystemMode(fileHidden);
                    }
                    return;
                }
            }
        }
        if (b2) {
            Zip4jUtil.setFileHidden(fileHidden);
        }
    }
    
    private static void setFileLastModifiedTime(final FileHeader fileHeader, final File file) throws ZipException {
        if (fileHeader.getLastModFileTime() <= 0) {
            return;
        }
        if (file.exists()) {
            file.setLastModified(Zip4jUtil.dosToJavaTme(fileHeader.getLastModFileTime()));
        }
    }
}
