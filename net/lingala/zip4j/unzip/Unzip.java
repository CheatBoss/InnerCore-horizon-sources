package net.lingala.zip4j.unzip;

import net.lingala.zip4j.exception.*;
import java.util.*;
import net.lingala.zip4j.progress.*;
import java.io.*;
import net.lingala.zip4j.util.*;
import net.lingala.zip4j.model.*;
import net.lingala.zip4j.io.*;

public class Unzip
{
    private ZipModel zipModel;
    
    public Unzip(final ZipModel zipModel) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("ZipModel is null");
        }
        this.zipModel = zipModel;
    }
    
    private long calculateTotalWork(final ArrayList list) throws ZipException {
        if (list == null) {
            throw new ZipException("fileHeaders is null, cannot calculate total work");
        }
        long n = 0L;
        for (int i = 0; i < list.size(); ++i) {
            final FileHeader fileHeader = list.get(i);
            if (fileHeader.getZip64ExtendedInfo() != null && fileHeader.getZip64ExtendedInfo().getUnCompressedSize() > 0L) {
                n += fileHeader.getZip64ExtendedInfo().getCompressedSize();
            }
            else {
                n += fileHeader.getCompressedSize();
            }
        }
        return n;
    }
    
    private void checkOutputDirectoryStructure(final FileHeader fileHeader, final String s, final String s2) throws ZipException {
        if (fileHeader != null) {
            if (Zip4jUtil.isStringNotNullAndNotEmpty(s)) {
                String fileName = fileHeader.getFileName();
                if (Zip4jUtil.isStringNotNullAndNotEmpty(s2)) {
                    fileName = s2;
                }
                if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)) {
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(fileName);
                final String string = sb.toString();
                try {
                    final File file = new File(new File(string).getParent());
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    return;
                }
                catch (Exception ex) {
                    throw new ZipException(ex);
                }
            }
        }
        throw new ZipException("Cannot check output directory structure...one of the parameters was null");
    }
    
    private void initExtractAll(final ArrayList list, final UnzipParameters unzipParameters, final ProgressMonitor progressMonitor, final String s) throws ZipException {
        for (int i = 0; i < list.size(); ++i) {
            this.initExtractFile(list.get(i), s, unzipParameters, null, progressMonitor);
            if (progressMonitor.isCancelAllTasks()) {
                progressMonitor.setResult(3);
                progressMonitor.setState(0);
                return;
            }
        }
    }
    
    private void initExtractFile(final FileHeader fileHeader, final String s, final UnzipParameters unzipParameters, final String s2, final ProgressMonitor progressMonitor) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("fileHeader is null");
        }
        try {
            progressMonitor.setFileName(fileHeader.getFileName());
            String string = s;
            if (!s.endsWith(InternalZipConstants.FILE_SEPARATOR)) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(InternalZipConstants.FILE_SEPARATOR);
                string = sb.toString();
            }
            if (fileHeader.isDirectory()) {
                try {
                    final String fileName = fileHeader.getFileName();
                    if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)) {
                        return;
                    }
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(string);
                    sb2.append(fileName);
                    final File file = new File(sb2.toString());
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    return;
                }
                catch (Exception ex) {
                    progressMonitor.endProgressMonitorError(ex);
                    throw new ZipException(ex);
                }
            }
            this.checkOutputDirectoryStructure(fileHeader, string, s2);
            final UnzipEngine unzipEngine = new UnzipEngine(this.zipModel, fileHeader);
            try {
                unzipEngine.unzipFile(progressMonitor, string, s2, unzipParameters);
            }
            catch (Exception ex2) {
                progressMonitor.endProgressMonitorError(ex2);
                throw new ZipException(ex2);
            }
        }
        catch (Exception ex3) {
            progressMonitor.endProgressMonitorError(ex3);
            throw new ZipException(ex3);
        }
        catch (ZipException ex4) {
            progressMonitor.endProgressMonitorError(ex4);
            throw ex4;
        }
    }
    
    public void extractAll(final UnzipParameters unzipParameters, final String s, final ProgressMonitor progressMonitor, final boolean b) throws ZipException {
        final CentralDirectory centralDirectory = this.zipModel.getCentralDirectory();
        if (centralDirectory == null || centralDirectory.getFileHeaders() == null) {
            throw new ZipException("invalid central directory in zipModel");
        }
        final ArrayList fileHeaders = centralDirectory.getFileHeaders();
        progressMonitor.setCurrentOperation(1);
        progressMonitor.setTotalWork(this.calculateTotalWork(fileHeaders));
        progressMonitor.setState(1);
        if (b) {
            new Thread("Zip4j") {
                @Override
                public void run() {
                    try {
                        Unzip.this.initExtractAll(fileHeaders, unzipParameters, progressMonitor, s);
                        progressMonitor.endProgressMonitorSuccess();
                    }
                    catch (ZipException ex) {}
                }
            }.start();
            return;
        }
        this.initExtractAll(fileHeaders, unzipParameters, progressMonitor, s);
    }
    
    public void extractFile(final FileHeader fileHeader, final String s, final UnzipParameters unzipParameters, final String s2, final ProgressMonitor progressMonitor, final boolean b) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("fileHeader is null");
        }
        progressMonitor.setCurrentOperation(1);
        progressMonitor.setTotalWork(fileHeader.getCompressedSize());
        progressMonitor.setState(1);
        progressMonitor.setPercentDone(0);
        progressMonitor.setFileName(fileHeader.getFileName());
        if (b) {
            new Thread("Zip4j") {
                @Override
                public void run() {
                    try {
                        Unzip.this.initExtractFile(fileHeader, s, unzipParameters, s2, progressMonitor);
                        progressMonitor.endProgressMonitorSuccess();
                    }
                    catch (ZipException ex) {}
                }
            }.start();
            return;
        }
        this.initExtractFile(fileHeader, s, unzipParameters, s2, progressMonitor);
        progressMonitor.endProgressMonitorSuccess();
    }
    
    public ZipInputStream getInputStream(final FileHeader fileHeader) throws ZipException {
        return new UnzipEngine(this.zipModel, fileHeader).getInputStream();
    }
}
