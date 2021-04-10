package net.lingala.zip4j.core;

import net.lingala.zip4j.progress.*;
import net.lingala.zip4j.exception.*;
import net.lingala.zip4j.zip.*;
import net.lingala.zip4j.unzip.*;
import net.lingala.zip4j.model.*;
import java.io.*;
import java.util.*;
import net.lingala.zip4j.io.*;
import net.lingala.zip4j.util.*;

public class ZipFile
{
    private String file;
    private String fileNameCharset;
    private boolean isEncrypted;
    private int mode;
    private ProgressMonitor progressMonitor;
    private boolean runInThread;
    private ZipModel zipModel;
    
    public ZipFile(final File file) throws ZipException {
        if (file == null) {
            throw new ZipException("Input zip file parameter is not null", 1);
        }
        this.file = file.getPath();
        this.mode = 2;
        this.progressMonitor = new ProgressMonitor();
        this.runInThread = false;
    }
    
    public ZipFile(final String s) throws ZipException {
        this(new File(s));
    }
    
    private void addFolder(final File file, final ZipParameters zipParameters, final boolean b) throws ZipException {
        this.checkZipModel();
        if (this.zipModel == null) {
            throw new ZipException("internal error: zip model is null");
        }
        if (b && this.zipModel.isSplitArchive()) {
            throw new ZipException("This is a split archive. Zip file format does not allow updating split/spanned files");
        }
        new ZipEngine(this.zipModel).addFolderToZip(file, zipParameters, this.progressMonitor, this.runInThread);
    }
    
    private void checkZipModel() throws ZipException {
        if (this.zipModel == null) {
            if (Zip4jUtil.checkFileExists(this.file)) {
                this.readZipInfo();
                return;
            }
            this.createNewZipModel();
        }
    }
    
    private void createNewZipModel() {
        (this.zipModel = new ZipModel()).setZipFile(this.file);
        this.zipModel.setFileNameCharset(this.fileNameCharset);
    }
    
    private void readZipInfo() throws ZipException {
        if (!Zip4jUtil.checkFileExists(this.file)) {
            throw new ZipException("zip file does not exist");
        }
        if (!Zip4jUtil.checkFileReadAccess(this.file)) {
            throw new ZipException("no read access for the input zip file");
        }
        if (this.mode != 2) {
            throw new ZipException("Invalid mode");
        }
        Object o = null;
        while (true) {
            try {
                try {
                    final RandomAccessFile randomAccessFile = (RandomAccessFile)(o = new RandomAccessFile(new File(this.file), "r"));
                    if (this.zipModel == null) {
                        o = randomAccessFile;
                        this.zipModel = new HeaderReader(randomAccessFile).readAllHeaders(this.fileNameCharset);
                        o = randomAccessFile;
                        if (this.zipModel != null) {
                            o = randomAccessFile;
                            this.zipModel.setZipFile(this.file);
                        }
                    }
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        }
                        catch (IOException o) {}
                    }
                    return;
                }
                finally {
                    if (o != null) {
                        final Object o2 = o;
                        ((RandomAccessFile)o2).close();
                    }
                }
            }
            catch (FileNotFoundException ex) {}
            try {
                final Object o2 = o;
                ((RandomAccessFile)o2).close();
                continue;
            }
            catch (IOException ex2) {}
            break;
        }
    }
    
    public void addFile(final File file, final ZipParameters zipParameters) throws ZipException {
        final ArrayList<File> list = new ArrayList<File>();
        list.add(file);
        this.addFiles(list, zipParameters);
    }
    
    public void addFiles(final ArrayList list, final ZipParameters zipParameters) throws ZipException {
        this.checkZipModel();
        if (this.zipModel == null) {
            throw new ZipException("internal error: zip model is null");
        }
        if (list == null) {
            throw new ZipException("input file ArrayList is null, cannot add files");
        }
        if (!Zip4jUtil.checkArrayListTypes(list, 1)) {
            throw new ZipException("One or more elements in the input ArrayList is not of type File");
        }
        if (zipParameters == null) {
            throw new ZipException("input parameters are null, cannot add files to zip");
        }
        if (this.progressMonitor.getState() == 1) {
            throw new ZipException("invalid operation - Zip4j is in busy state");
        }
        if (Zip4jUtil.checkFileExists(this.file) && this.zipModel.isSplitArchive()) {
            throw new ZipException("Zip file already exists. Zip file format does not allow updating split/spanned files");
        }
        new ZipEngine(this.zipModel).addFiles(list, zipParameters, this.progressMonitor, this.runInThread);
    }
    
    public void addFolder(final File file, final ZipParameters zipParameters) throws ZipException {
        if (file == null) {
            throw new ZipException("input path is null, cannot add folder to zip file");
        }
        if (zipParameters == null) {
            throw new ZipException("input parameters are null, cannot add folder to zip file");
        }
        this.addFolder(file, zipParameters, true);
    }
    
    public void addFolder(final String s, final ZipParameters zipParameters) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("input path is null or empty, cannot add folder to zip file");
        }
        this.addFolder(new File(s), zipParameters);
    }
    
    public void addStream(final InputStream inputStream, final ZipParameters zipParameters) throws ZipException {
        if (inputStream == null) {
            throw new ZipException("inputstream is null, cannot add file to zip");
        }
        if (zipParameters == null) {
            throw new ZipException("zip parameters are null");
        }
        this.setRunInThread(false);
        this.checkZipModel();
        if (this.zipModel == null) {
            throw new ZipException("internal error: zip model is null");
        }
        if (Zip4jUtil.checkFileExists(this.file) && this.zipModel.isSplitArchive()) {
            throw new ZipException("Zip file already exists. Zip file format does not allow updating split/spanned files");
        }
        new ZipEngine(this.zipModel).addStreamToZip(inputStream, zipParameters);
    }
    
    public void createZipFile(final File file, final ZipParameters zipParameters) throws ZipException {
        final ArrayList<File> list = new ArrayList<File>();
        list.add(file);
        this.createZipFile(list, zipParameters, false, -1L);
    }
    
    public void createZipFile(final File file, final ZipParameters zipParameters, final boolean b, final long n) throws ZipException {
        final ArrayList<File> list = new ArrayList<File>();
        list.add(file);
        this.createZipFile(list, zipParameters, b, n);
    }
    
    public void createZipFile(final ArrayList list, final ZipParameters zipParameters) throws ZipException {
        this.createZipFile(list, zipParameters, false, -1L);
    }
    
    public void createZipFile(final ArrayList list, final ZipParameters zipParameters, final boolean splitArchive, final long splitLength) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(this.file)) {
            throw new ZipException("zip file path is empty");
        }
        if (Zip4jUtil.checkFileExists(this.file)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("zip file: ");
            sb.append(this.file);
            sb.append(" already exists. To add files to existing zip file use addFile method");
            throw new ZipException(sb.toString());
        }
        if (list == null) {
            throw new ZipException("input file ArrayList is null, cannot create zip file");
        }
        if (!Zip4jUtil.checkArrayListTypes(list, 1)) {
            throw new ZipException("One or more elements in the input ArrayList is not of type File");
        }
        this.createNewZipModel();
        this.zipModel.setSplitArchive(splitArchive);
        this.zipModel.setSplitLength(splitLength);
        this.addFiles(list, zipParameters);
    }
    
    public void createZipFileFromFolder(final File file, final ZipParameters zipParameters, final boolean splitArchive, final long splitLength) throws ZipException {
        if (file == null) {
            throw new ZipException("folderToAdd is null, cannot create zip file from folder");
        }
        if (zipParameters == null) {
            throw new ZipException("input parameters are null, cannot create zip file from folder");
        }
        if (Zip4jUtil.checkFileExists(this.file)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("zip file: ");
            sb.append(this.file);
            sb.append(" already exists. To add files to existing zip file use addFolder method");
            throw new ZipException(sb.toString());
        }
        this.createNewZipModel();
        this.zipModel.setSplitArchive(splitArchive);
        if (splitArchive) {
            this.zipModel.setSplitLength(splitLength);
        }
        this.addFolder(file, zipParameters, false);
    }
    
    public void createZipFileFromFolder(final String s, final ZipParameters zipParameters, final boolean b, final long n) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("folderToAdd is empty or null, cannot create Zip File from folder");
        }
        this.createZipFileFromFolder(new File(s), zipParameters, b, n);
    }
    
    public void extractAll(final String s) throws ZipException {
        this.extractAll(s, null);
    }
    
    public void extractAll(final String s, final UnzipParameters unzipParameters) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("output path is null or invalid");
        }
        if (!Zip4jUtil.checkOutputFolder(s)) {
            throw new ZipException("invalid output path");
        }
        if (this.zipModel == null) {
            this.readZipInfo();
        }
        if (this.zipModel == null) {
            throw new ZipException("Internal error occurred when extracting zip file");
        }
        if (this.progressMonitor.getState() == 1) {
            throw new ZipException("invalid operation - Zip4j is in busy state");
        }
        new Unzip(this.zipModel).extractAll(unzipParameters, s, this.progressMonitor, this.runInThread);
    }
    
    public void extractFile(final String s, final String s2) throws ZipException {
        this.extractFile(s, s2, null);
    }
    
    public void extractFile(final String s, final String s2, final UnzipParameters unzipParameters) throws ZipException {
        this.extractFile(s, s2, unzipParameters, null);
    }
    
    public void extractFile(final String s, final String s2, final UnzipParameters unzipParameters, final String s3) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("file to extract is null or empty, cannot extract file");
        }
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(s2)) {
            throw new ZipException("destination string path is empty or null, cannot extract file");
        }
        this.readZipInfo();
        final FileHeader fileHeader = Zip4jUtil.getFileHeader(this.zipModel, s);
        if (fileHeader == null) {
            throw new ZipException("file header not found for given file name, cannot extract file");
        }
        if (this.progressMonitor.getState() == 1) {
            throw new ZipException("invalid operation - Zip4j is in busy state");
        }
        fileHeader.extractFile(this.zipModel, s2, unzipParameters, s3, this.progressMonitor, this.runInThread);
    }
    
    public void extractFile(final FileHeader fileHeader, final String s) throws ZipException {
        this.extractFile(fileHeader, s, null);
    }
    
    public void extractFile(final FileHeader fileHeader, final String s, final UnzipParameters unzipParameters) throws ZipException {
        this.extractFile(fileHeader, s, unzipParameters, null);
    }
    
    public void extractFile(final FileHeader fileHeader, final String s, final UnzipParameters unzipParameters, final String s2) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("input file header is null, cannot extract file");
        }
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("destination path is empty or null, cannot extract file");
        }
        this.readZipInfo();
        if (this.progressMonitor.getState() == 1) {
            throw new ZipException("invalid operation - Zip4j is in busy state");
        }
        fileHeader.extractFile(this.zipModel, s, unzipParameters, s2, this.progressMonitor, this.runInThread);
    }
    
    public String getComment() throws ZipException {
        return this.getComment(null);
    }
    
    public String getComment(final String s) throws ZipException {
        String charset_DEFAULT = s;
        if (s == null) {
            if (Zip4jUtil.isSupportedCharset("windows-1254")) {
                charset_DEFAULT = "windows-1254";
            }
            else {
                charset_DEFAULT = InternalZipConstants.CHARSET_DEFAULT;
            }
        }
        if (!Zip4jUtil.checkFileExists(this.file)) {
            throw new ZipException("zip file does not exist, cannot read comment");
        }
        this.checkZipModel();
        if (this.zipModel == null) {
            throw new ZipException("zip model is null, cannot read comment");
        }
        if (this.zipModel.getEndCentralDirRecord() == null) {
            throw new ZipException("end of central directory record is null, cannot read comment");
        }
        if (this.zipModel.getEndCentralDirRecord().getCommentBytes() != null) {
            if (this.zipModel.getEndCentralDirRecord().getCommentBytes().length > 0) {
                try {
                    return new String(this.zipModel.getEndCentralDirRecord().getCommentBytes(), charset_DEFAULT);
                }
                catch (UnsupportedEncodingException ex) {
                    throw new ZipException(ex);
                }
            }
        }
        return null;
    }
    
    public File getFile() {
        return new File(this.file);
    }
    
    public FileHeader getFileHeader(final String s) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("input file name is emtpy or null, cannot get FileHeader");
        }
        this.readZipInfo();
        if (this.zipModel != null && this.zipModel.getCentralDirectory() != null) {
            return Zip4jUtil.getFileHeader(this.zipModel, s);
        }
        return null;
    }
    
    public List getFileHeaders() throws ZipException {
        this.readZipInfo();
        if (this.zipModel != null && this.zipModel.getCentralDirectory() != null) {
            return this.zipModel.getCentralDirectory().getFileHeaders();
        }
        return null;
    }
    
    public ZipInputStream getInputStream(final FileHeader fileHeader) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("FileHeader is null, cannot get InputStream");
        }
        this.checkZipModel();
        if (this.zipModel == null) {
            throw new ZipException("zip model is null, cannot get inputstream");
        }
        return new Unzip(this.zipModel).getInputStream(fileHeader);
    }
    
    public ProgressMonitor getProgressMonitor() {
        return this.progressMonitor;
    }
    
    public ArrayList getSplitZipFiles() throws ZipException {
        this.checkZipModel();
        return Zip4jUtil.getSplitZipFiles(this.zipModel);
    }
    
    public boolean isEncrypted() throws ZipException {
        if (this.zipModel == null) {
            this.readZipInfo();
            if (this.zipModel == null) {
                throw new ZipException("Zip Model is null");
            }
        }
        if (this.zipModel.getCentralDirectory() != null && this.zipModel.getCentralDirectory().getFileHeaders() != null) {
            final ArrayList fileHeaders = this.zipModel.getCentralDirectory().getFileHeaders();
            for (int i = 0; i < fileHeaders.size(); ++i) {
                final FileHeader fileHeader = fileHeaders.get(i);
                if (fileHeader != null && fileHeader.isEncrypted()) {
                    this.isEncrypted = true;
                    break;
                }
            }
            return this.isEncrypted;
        }
        throw new ZipException("invalid zip file");
    }
    
    public boolean isRunInThread() {
        return this.runInThread;
    }
    
    public boolean isSplitArchive() throws ZipException {
        if (this.zipModel == null) {
            this.readZipInfo();
            if (this.zipModel == null) {
                throw new ZipException("Zip Model is null");
            }
        }
        return this.zipModel.isSplitArchive();
    }
    
    public boolean isValidZipFile() {
        try {
            this.readZipInfo();
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public void mergeSplitFiles(final File file) throws ZipException {
        if (file == null) {
            throw new ZipException("outputZipFile is null, cannot merge split files");
        }
        if (file.exists()) {
            throw new ZipException("output Zip File already exists");
        }
        this.checkZipModel();
        if (this.zipModel == null) {
            throw new ZipException("zip model is null, corrupt zip file?");
        }
        final ArchiveMaintainer archiveMaintainer = new ArchiveMaintainer();
        archiveMaintainer.initProgressMonitorForMergeOp(this.zipModel, this.progressMonitor);
        archiveMaintainer.mergeSplitZipFiles(this.zipModel, file, this.progressMonitor, this.runInThread);
    }
    
    public void removeFile(final String s) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(s)) {
            throw new ZipException("file name is empty or null, cannot remove file");
        }
        if (this.zipModel == null && Zip4jUtil.checkFileExists(this.file)) {
            this.readZipInfo();
        }
        if (this.zipModel.isSplitArchive()) {
            throw new ZipException("Zip file format does not allow updating split/spanned files");
        }
        final FileHeader fileHeader = Zip4jUtil.getFileHeader(this.zipModel, s);
        if (fileHeader == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("could not find file header for file: ");
            sb.append(s);
            throw new ZipException(sb.toString());
        }
        this.removeFile(fileHeader);
    }
    
    public void removeFile(final FileHeader fileHeader) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("file header is null, cannot remove file");
        }
        if (this.zipModel == null && Zip4jUtil.checkFileExists(this.file)) {
            this.readZipInfo();
        }
        if (this.zipModel.isSplitArchive()) {
            throw new ZipException("Zip file format does not allow updating split/spanned files");
        }
        final ArchiveMaintainer archiveMaintainer = new ArchiveMaintainer();
        archiveMaintainer.initProgressMonitorForRemoveOp(this.zipModel, fileHeader, this.progressMonitor);
        archiveMaintainer.removeZipFile(this.zipModel, fileHeader, this.progressMonitor, this.runInThread);
    }
    
    public void setComment(final String s) throws ZipException {
        if (s == null) {
            throw new ZipException("input comment is null, cannot update zip file");
        }
        if (!Zip4jUtil.checkFileExists(this.file)) {
            throw new ZipException("zip file does not exist, cannot set comment for zip file");
        }
        this.readZipInfo();
        if (this.zipModel == null) {
            throw new ZipException("zipModel is null, cannot update zip file");
        }
        if (this.zipModel.getEndCentralDirRecord() == null) {
            throw new ZipException("end of central directory is null, cannot set comment");
        }
        new ArchiveMaintainer().setComment(this.zipModel, s);
    }
    
    public void setFileNameCharset(final String fileNameCharset) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileNameCharset)) {
            throw new ZipException("null or empty charset name");
        }
        if (!Zip4jUtil.isSupportedCharset(fileNameCharset)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("unsupported charset: ");
            sb.append(fileNameCharset);
            throw new ZipException(sb.toString());
        }
        this.fileNameCharset = fileNameCharset;
    }
    
    public void setPassword(final String s) throws ZipException {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(s)) {
            throw new NullPointerException();
        }
        this.setPassword(s.toCharArray());
    }
    
    public void setPassword(final char[] password) throws ZipException {
        if (this.zipModel == null) {
            this.readZipInfo();
            if (this.zipModel == null) {
                throw new ZipException("Zip Model is null");
            }
        }
        if (this.zipModel.getCentralDirectory() != null && this.zipModel.getCentralDirectory().getFileHeaders() != null) {
            for (int i = 0; i < this.zipModel.getCentralDirectory().getFileHeaders().size(); ++i) {
                if (this.zipModel.getCentralDirectory().getFileHeaders().get(i) != null && ((FileHeader)this.zipModel.getCentralDirectory().getFileHeaders().get(i)).isEncrypted()) {
                    ((FileHeader)this.zipModel.getCentralDirectory().getFileHeaders().get(i)).setPassword(password);
                }
            }
            return;
        }
        throw new ZipException("invalid zip file");
    }
    
    public void setRunInThread(final boolean runInThread) {
        this.runInThread = runInThread;
    }
}
