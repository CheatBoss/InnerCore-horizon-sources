package net.lingala.zip4j.util;

import net.lingala.zip4j.exception.*;
import net.lingala.zip4j.model.*;
import java.util.*;
import java.io.*;
import java.nio.*;

public class Zip4jUtil
{
    public static boolean checkArrayListTypes(final ArrayList list, int n) throws ZipException {
        if (list == null) {
            throw new ZipException("input arraylist is null, cannot check types");
        }
        if (list.size() <= 0) {
            return true;
        }
        final int n2 = 0;
        final int n3 = 0;
        final int n4 = 0;
        Label_0131: {
            switch (n) {
                default: {
                    n = n3;
                    break;
                }
                case 2: {
                    int n5 = 0;
                    while (true) {
                        n = n4;
                        if (n5 >= list.size()) {
                            break;
                        }
                        if (!(list.get(n5) instanceof String)) {
                            n = 1;
                            break;
                        }
                        ++n5;
                    }
                    break;
                }
                case 1: {
                    int n6 = 0;
                    while (true) {
                        n = n2;
                        if (n6 >= list.size()) {
                            break Label_0131;
                        }
                        if (!(list.get(n6) instanceof File)) {
                            n = 1;
                            break Label_0131;
                        }
                        ++n6;
                    }
                    break;
                }
            }
        }
        return n == 0;
    }
    
    public static boolean checkFileExists(final File file) throws ZipException {
        if (file == null) {
            throw new ZipException("cannot check if file exists: input file is null");
        }
        return file.exists();
    }
    
    public static boolean checkFileExists(final String s) throws ZipException {
        if (!isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("path is null");
        }
        return checkFileExists(new File(s));
    }
    
    public static boolean checkFileReadAccess(final String s) throws ZipException {
        if (!isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("path is null");
        }
        if (!checkFileExists(s)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("file does not exist: ");
            sb.append(s);
            throw new ZipException(sb.toString());
        }
        try {
            return new File(s).canRead();
        }
        catch (Exception ex) {
            throw new ZipException("cannot read zip file");
        }
    }
    
    public static boolean checkFileWriteAccess(final String s) throws ZipException {
        if (!isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("path is null");
        }
        if (!checkFileExists(s)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("file does not exist: ");
            sb.append(s);
            throw new ZipException(sb.toString());
        }
        try {
            return new File(s).canWrite();
        }
        catch (Exception ex) {
            throw new ZipException("cannot read zip file");
        }
    }
    
    public static boolean checkOutputFolder(final String s) throws ZipException {
        if (!isStringNotNullAndNotEmpty(s)) {
            throw new ZipException(new NullPointerException("output path is null"));
        }
        final File file = new File(s);
        if (file.exists()) {
            if (!file.isDirectory()) {
                throw new ZipException("output folder is not valid");
            }
            if (!file.canWrite()) {
                throw new ZipException("no write access to output folder");
            }
            return true;
        }
        try {
            file.mkdirs();
            if (!file.isDirectory()) {
                throw new ZipException("output folder is not valid");
            }
            if (!file.canWrite()) {
                throw new ZipException("no write access to destination folder");
            }
            return true;
        }
        catch (Exception ex) {
            throw new ZipException("Cannot create destination folder");
        }
    }
    
    public static byte[] convertCharset(final String s) throws ZipException {
        try {
            final String detectCharSet = detectCharSet(s);
            if (detectCharSet.equals("Cp850")) {
                return s.getBytes("Cp850");
            }
            if (detectCharSet.equals("UTF8")) {
                return s.getBytes("UTF8");
            }
            return s.getBytes();
        }
        catch (Exception ex) {
            throw new ZipException(ex);
        }
        catch (UnsupportedEncodingException ex2) {
            return s.getBytes();
        }
    }
    
    public static String decodeFileName(final byte[] array, final boolean b) {
        if (b) {
            try {
                return new String(array, "UTF8");
            }
            catch (UnsupportedEncodingException ex) {
                return new String(array);
            }
        }
        return getCp850EncodedString(array);
    }
    
    public static String detectCharSet(String charset_DEFAULT) throws ZipException {
        if (charset_DEFAULT == null) {
            throw new ZipException("input string is null, cannot detect charset");
        }
        try {
            if (charset_DEFAULT.equals(new String(charset_DEFAULT.getBytes("Cp850"), "Cp850"))) {
                return "Cp850";
            }
            if (charset_DEFAULT.equals(new String(charset_DEFAULT.getBytes("UTF8"), "UTF8"))) {
                return "UTF8";
            }
            charset_DEFAULT = InternalZipConstants.CHARSET_DEFAULT;
            return charset_DEFAULT;
        }
        catch (Exception ex) {
            return InternalZipConstants.CHARSET_DEFAULT;
        }
        catch (UnsupportedEncodingException ex2) {
            return InternalZipConstants.CHARSET_DEFAULT;
        }
    }
    
    public static long dosToJavaTme(final int n) {
        final Calendar instance = Calendar.getInstance();
        instance.set((n >> 25 & 0x7F) + 1980, (n >> 21 & 0xF) - 1, n >> 16 & 0x1F, n >> 11 & 0x1F, n >> 5 & 0x3F, (n & 0x1F) * 2);
        instance.set(14, 0);
        return instance.getTime().getTime();
    }
    
    public static String getAbsoluteFilePath(final String s) throws ZipException {
        if (!isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("filePath is null or empty, cannot get absolute file path");
        }
        return new File(s).getAbsolutePath();
    }
    
    public static long[] getAllHeaderSignatures() {
        return new long[] { 67324752L, 134695760L, 33639248L, 101010256L, 84233040L, 134630224L, 134695760L, 117853008L, 101075792L, 1L, 39169L };
    }
    
    public static String getCp850EncodedString(final byte[] array) {
        try {
            return new String(array, "Cp850");
        }
        catch (UnsupportedEncodingException ex) {
            return new String(array);
        }
    }
    
    public static int getEncodedStringLength(final String s) throws ZipException {
        if (!isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("input string is null, cannot calculate encoded String length");
        }
        return getEncodedStringLength(s, detectCharSet(s));
    }
    
    public static int getEncodedStringLength(String s, final String s2) throws ZipException {
        if (!isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("input string is null, cannot calculate encoded String length");
        }
        if (!isStringNotNullAndNotEmpty(s2)) {
            throw new ZipException("encoding is not defined, cannot calculate string length");
        }
        try {
            if (s2.equals("Cp850")) {
                s = (String)ByteBuffer.wrap(s.getBytes("Cp850"));
            }
            else if (s2.equals("UTF8")) {
                s = (String)ByteBuffer.wrap(s.getBytes("UTF8"));
            }
            else {
                s = (String)ByteBuffer.wrap(s.getBytes(s2));
            }
        }
        catch (Exception ex) {
            throw new ZipException(ex);
        }
        catch (UnsupportedEncodingException ex2) {
            s = (String)ByteBuffer.wrap(s.getBytes());
        }
        return ((Buffer)s).limit();
    }
    
    public static FileHeader getFileHeader(final ZipModel zipModel, final String s) throws ZipException {
        if (zipModel == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("zip model is null, cannot determine file header for fileName: ");
            sb.append(s);
            throw new ZipException(sb.toString());
        }
        if (!isStringNotNullAndNotEmpty(s)) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("file name is null, cannot determine file header for fileName: ");
            sb2.append(s);
            throw new ZipException(sb2.toString());
        }
        FileHeader fileHeader;
        if ((fileHeader = getFileHeaderWithExactMatch(zipModel, s)) == null) {
            final String replaceAll = s.replaceAll("\\\\", "/");
            if ((fileHeader = getFileHeaderWithExactMatch(zipModel, replaceAll)) == null) {
                fileHeader = getFileHeaderWithExactMatch(zipModel, replaceAll.replaceAll("/", "\\\\"));
            }
        }
        return fileHeader;
    }
    
    public static FileHeader getFileHeaderWithExactMatch(final ZipModel zipModel, final String s) throws ZipException {
        if (zipModel == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("zip model is null, cannot determine file header with exact match for fileName: ");
            sb.append(s);
            throw new ZipException(sb.toString());
        }
        if (!isStringNotNullAndNotEmpty(s)) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("file name is null, cannot determine file header with exact match for fileName: ");
            sb2.append(s);
            throw new ZipException(sb2.toString());
        }
        if (zipModel.getCentralDirectory() == null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("central directory is null, cannot determine file header with exact match for fileName: ");
            sb3.append(s);
            throw new ZipException(sb3.toString());
        }
        if (zipModel.getCentralDirectory().getFileHeaders() == null) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("file Headers are null, cannot determine file header with exact match for fileName: ");
            sb4.append(s);
            throw new ZipException(sb4.toString());
        }
        if (zipModel.getCentralDirectory().getFileHeaders().size() <= 0) {
            return null;
        }
        final ArrayList fileHeaders = zipModel.getCentralDirectory().getFileHeaders();
        for (int i = 0; i < fileHeaders.size(); ++i) {
            final FileHeader fileHeader = fileHeaders.get(i);
            final String fileName = fileHeader.getFileName();
            if (isStringNotNullAndNotEmpty(fileName)) {
                if (s.equalsIgnoreCase(fileName)) {
                    return fileHeader;
                }
            }
        }
        return null;
    }
    
    public static long getFileLengh(final File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null, cannot calculate file length");
        }
        if (file.isDirectory()) {
            return -1L;
        }
        return file.length();
    }
    
    public static long getFileLengh(final String s) throws ZipException {
        if (!isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("invalid file name");
        }
        return getFileLengh(new File(s));
    }
    
    public static String getFileNameFromFilePath(final File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null, cannot get file name");
        }
        if (file.isDirectory()) {
            return null;
        }
        return file.getName();
    }
    
    public static ArrayList getFilesInDirectoryRec(File file, final boolean b) throws ZipException {
        if (file == null) {
            throw new ZipException("input path is null, cannot read files in the directory");
        }
        final ArrayList<File> list = new ArrayList<File>();
        final List<File> list2 = Arrays.asList(file.listFiles());
        if (!file.canRead()) {
            return list;
        }
        for (int i = 0; i < list2.size(); ++i) {
            file = list2.get(i);
            if (file.isHidden() && !b) {
                return list;
            }
            list.add(file);
            if (file.isDirectory()) {
                list.addAll(getFilesInDirectoryRec(file, b));
            }
        }
        return list;
    }
    
    public static int getIndexOfFileHeader(final ZipModel zipModel, final FileHeader fileHeader) throws ZipException {
        if (zipModel == null || fileHeader == null) {
            throw new ZipException("input parameters is null, cannot determine index of file header");
        }
        if (zipModel.getCentralDirectory() == null) {
            throw new ZipException("central directory is null, ccannot determine index of file header");
        }
        if (zipModel.getCentralDirectory().getFileHeaders() == null) {
            throw new ZipException("file Headers are null, cannot determine index of file header");
        }
        if (zipModel.getCentralDirectory().getFileHeaders().size() <= 0) {
            return -1;
        }
        final String fileName = fileHeader.getFileName();
        if (!isStringNotNullAndNotEmpty(fileName)) {
            throw new ZipException("file name in file header is empty or null, cannot determine index of file header");
        }
        final ArrayList fileHeaders = zipModel.getCentralDirectory().getFileHeaders();
        for (int i = 0; i < fileHeaders.size(); ++i) {
            final String fileName2 = fileHeaders.get(i).getFileName();
            if (isStringNotNullAndNotEmpty(fileName2)) {
                if (fileName.equalsIgnoreCase(fileName2)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public static long getLastModifiedFileTime(final File file, final TimeZone timeZone) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null, cannot read last modified file time");
        }
        if (!file.exists()) {
            throw new ZipException("input file does not exist, cannot read last modified file time");
        }
        return file.lastModified();
    }
    
    public static String getRelativeFileName(String s, final String s2, String s3) throws ZipException {
        if (!isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("input file path/name is empty, cannot calculate relative file name");
        }
        if (isStringNotNullAndNotEmpty(s3)) {
            final String s4 = s3 = new File(s3).getPath();
            if (!s4.endsWith(InternalZipConstants.FILE_SEPARATOR)) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s4);
                sb.append(InternalZipConstants.FILE_SEPARATOR);
                s3 = sb.toString();
            }
            final String s5 = s3 = s.substring(s3.length());
            if (s5.startsWith(System.getProperty("file.separator"))) {
                s3 = s5.substring(1);
            }
            final File file = new File(s);
            if (file.isDirectory()) {
                s = s3.replaceAll("\\\\", "/");
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s);
                sb2.append("/");
                s = sb2.toString();
            }
            else {
                s3 = s3.substring(0, s3.lastIndexOf(file.getName())).replaceAll("\\\\", "/");
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(s3);
                sb3.append(file.getName());
                s = sb3.toString();
            }
        }
        else {
            final File file2 = new File(s);
            if (file2.isDirectory()) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(file2.getName());
                sb4.append("/");
                s = sb4.toString();
            }
            else {
                s = getFileNameFromFilePath(new File(s));
            }
        }
        s3 = s;
        if (isStringNotNullAndNotEmpty(s2)) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(s2);
            sb5.append(s);
            s3 = sb5.toString();
        }
        if (!isStringNotNullAndNotEmpty(s3)) {
            throw new ZipException("Error determining file name");
        }
        return s3;
    }
    
    public static ArrayList getSplitZipFiles(final ZipModel zipModel) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("cannot get split zip files: zipmodel is null");
        }
        if (zipModel.getEndCentralDirRecord() == null) {
            return null;
        }
        final ArrayList<String> list = new ArrayList<String>();
        final String zipFile = zipModel.getZipFile();
        final String name = new File(zipFile).getName();
        if (!isStringNotNullAndNotEmpty(zipFile)) {
            throw new ZipException("cannot get split zip files: zipfile is null");
        }
        if (!zipModel.isSplitArchive()) {
            list.add(zipFile);
            return list;
        }
        final int noOfThisDisk = zipModel.getEndCentralDirRecord().getNoOfThisDisk();
        if (noOfThisDisk == 0) {
            list.add(zipFile);
            return list;
        }
        for (int i = 0; i <= noOfThisDisk; ++i) {
            if (i == noOfThisDisk) {
                list.add(zipModel.getZipFile());
            }
            else {
                String s = ".z0";
                if (i > 9) {
                    s = ".z";
                }
                String s2;
                if (name.indexOf(".") >= 0) {
                    s2 = zipFile.substring(0, zipFile.lastIndexOf("."));
                }
                else {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(zipFile);
                    sb.append(s);
                    sb.append(i + 1);
                    s2 = sb.toString();
                }
                list.add(s2);
            }
        }
        return list;
    }
    
    public static String getZipFileNameWithoutExt(String substring) throws ZipException {
        if (!isStringNotNullAndNotEmpty(substring)) {
            throw new ZipException("zip file name is empty or null, cannot determine zip file name");
        }
        String substring2 = substring;
        if (substring.indexOf(System.getProperty("file.separator")) >= 0) {
            substring2 = substring.substring(substring.lastIndexOf(System.getProperty("file.separator")));
        }
        substring = substring2;
        if (substring2.indexOf(".") > 0) {
            substring = substring2.substring(0, substring2.lastIndexOf("."));
        }
        return substring;
    }
    
    public static boolean isStringNotNullAndNotEmpty(final String s) {
        return s != null && s.trim().length() > 0;
    }
    
    public static boolean isSupportedCharset(final String s) throws ZipException {
        if (!isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("charset is null or empty, cannot check if it is supported");
        }
        try {
            new String("a".getBytes(), s);
            return true;
        }
        catch (Exception ex) {
            throw new ZipException(ex);
        }
        catch (UnsupportedEncodingException ex2) {
            return false;
        }
    }
    
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
    }
    
    public static long javaToDosTime(final long timeInMillis) {
        final Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(timeInMillis);
        final int value = instance.get(1);
        if (value < 1980) {
            return 2162688L;
        }
        return instance.get(13) >> 1 | (value - 1980 << 25 | instance.get(2) + 1 << 21 | instance.get(5) << 16 | instance.get(11) << 11 | instance.get(12) << 5);
    }
    
    public static void setFileArchive(final File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null. cannot set archive file attribute");
        }
        if (!isWindows()) {
            return;
        }
        if (file.exists()) {
            try {
                if (file.isDirectory()) {
                    final Runtime runtime = Runtime.getRuntime();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("attrib +A \"");
                    sb.append(file.getAbsolutePath());
                    sb.append("\"");
                    runtime.exec(sb.toString());
                }
                else {
                    final Runtime runtime2 = Runtime.getRuntime();
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("attrib +A \"");
                    sb2.append(file.getAbsolutePath());
                    sb2.append("\"");
                    runtime2.exec(sb2.toString());
                }
            }
            catch (IOException ex) {}
        }
    }
    
    public static void setFileHidden(final File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null. cannot set hidden file attribute");
        }
        if (!isWindows()) {
            return;
        }
        if (file.exists()) {
            try {
                final Runtime runtime = Runtime.getRuntime();
                final StringBuilder sb = new StringBuilder();
                sb.append("attrib +H \"");
                sb.append(file.getAbsolutePath());
                sb.append("\"");
                runtime.exec(sb.toString());
            }
            catch (IOException ex) {}
        }
    }
    
    public static void setFileReadOnly(final File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null. cannot set read only file attribute");
        }
        if (file.exists()) {
            file.setReadOnly();
        }
    }
    
    public static void setFileSystemMode(final File file) throws ZipException {
        if (file == null) {
            throw new ZipException("input file is null. cannot set archive file attribute");
        }
        if (!isWindows()) {
            return;
        }
        if (file.exists()) {
            try {
                final Runtime runtime = Runtime.getRuntime();
                final StringBuilder sb = new StringBuilder();
                sb.append("attrib +S \"");
                sb.append(file.getAbsolutePath());
                sb.append("\"");
                runtime.exec(sb.toString());
            }
            catch (IOException ex) {}
        }
    }
}
