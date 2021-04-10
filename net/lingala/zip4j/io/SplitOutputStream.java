package net.lingala.zip4j.io;

import net.lingala.zip4j.exception.*;
import net.lingala.zip4j.util.*;
import java.io.*;

public class SplitOutputStream extends OutputStream
{
    private long bytesWrittenForThisPart;
    private int currSplitFileCounter;
    private File outFile;
    private RandomAccessFile raf;
    private long splitLength;
    private File zipFile;
    
    public SplitOutputStream(final File file) throws FileNotFoundException, ZipException {
        this(file, -1L);
    }
    
    public SplitOutputStream(final File file, final long splitLength) throws FileNotFoundException, ZipException {
        if (splitLength >= 0L && splitLength < 65536L) {
            throw new ZipException("split length less than minimum allowed split length of 65536 Bytes");
        }
        this.raf = new RandomAccessFile(file, "rw");
        this.splitLength = splitLength;
        this.outFile = file;
        this.zipFile = file;
        this.currSplitFileCounter = 0;
        this.bytesWrittenForThisPart = 0L;
    }
    
    public SplitOutputStream(final String s) throws FileNotFoundException, ZipException {
        File file;
        if (Zip4jUtil.isStringNotNullAndNotEmpty(s)) {
            file = new File(s);
        }
        else {
            file = null;
        }
        this(file);
    }
    
    public SplitOutputStream(final String s, final long n) throws FileNotFoundException, ZipException {
        File file;
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(s)) {
            file = new File(s);
        }
        else {
            file = null;
        }
        this(file, n);
    }
    
    private boolean isHeaderData(final byte[] array) {
        if (array == null) {
            return false;
        }
        if (array.length < 4) {
            return false;
        }
        final int intLittleEndian = Raw.readIntLittleEndian(array, 0);
        final long[] allHeaderSignatures = Zip4jUtil.getAllHeaderSignatures();
        if (allHeaderSignatures != null && allHeaderSignatures.length > 0) {
            for (int i = 0; i < allHeaderSignatures.length; ++i) {
                if (allHeaderSignatures[i] != 134695760L && allHeaderSignatures[i] == intLittleEndian) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void startNextSplitFile() throws IOException {
        try {
            final String zipFileNameWithoutExt = Zip4jUtil.getZipFileNameWithoutExt(this.outFile.getName());
            final String absolutePath = this.zipFile.getAbsolutePath();
            File file;
            if (this.currSplitFileCounter < 9) {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.outFile.getParent());
                sb.append(System.getProperty("file.separator"));
                sb.append(zipFileNameWithoutExt);
                sb.append(".z0");
                sb.append(this.currSplitFileCounter + 1);
                file = new File(sb.toString());
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(this.outFile.getParent());
                sb2.append(System.getProperty("file.separator"));
                sb2.append(zipFileNameWithoutExt);
                sb2.append(".z");
                sb2.append(this.currSplitFileCounter + 1);
                file = new File(sb2.toString());
            }
            this.raf.close();
            if (file.exists()) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("split file: ");
                sb3.append(file.getName());
                sb3.append(" already exists in the current directory, cannot rename this file");
                throw new IOException(sb3.toString());
            }
            if (!this.zipFile.renameTo(file)) {
                throw new IOException("cannot rename newly created split file");
            }
            this.zipFile = new File(absolutePath);
            this.raf = new RandomAccessFile(this.zipFile, "rw");
            ++this.currSplitFileCounter;
        }
        catch (ZipException ex) {
            throw new IOException(ex.getMessage());
        }
    }
    
    public boolean checkBuffSizeAndStartNextSplitFile(final int n) throws ZipException {
        if (n < 0) {
            throw new ZipException("negative buffersize for checkBuffSizeAndStartNextSplitFile");
        }
        if (!this.isBuffSizeFitForCurrSplitFile(n)) {
            try {
                this.startNextSplitFile();
                this.bytesWrittenForThisPart = 0L;
                return true;
            }
            catch (IOException ex) {
                throw new ZipException(ex);
            }
        }
        return false;
    }
    
    @Override
    public void close() throws IOException {
        if (this.raf != null) {
            this.raf.close();
        }
    }
    
    @Override
    public void flush() throws IOException {
    }
    
    public int getCurrSplitFileCounter() {
        return this.currSplitFileCounter;
    }
    
    public long getFilePointer() throws IOException {
        return this.raf.getFilePointer();
    }
    
    public long getSplitLength() {
        return this.splitLength;
    }
    
    public boolean isBuffSizeFitForCurrSplitFile(final int n) throws ZipException {
        if (n < 0) {
            throw new ZipException("negative buffersize for isBuffSizeFitForCurrSplitFile");
        }
        return this.splitLength < 65536L || this.bytesWrittenForThisPart + n <= this.splitLength;
    }
    
    public boolean isSplitZipFile() {
        return this.splitLength != -1L;
    }
    
    public void seek(final long n) throws IOException {
        this.raf.seek(n);
    }
    
    @Override
    public void write(final int n) throws IOException {
        this.write(new byte[] { (byte)n }, 0, 1);
    }
    
    @Override
    public void write(final byte[] array) throws IOException {
        this.write(array, 0, array.length);
    }
    
    @Override
    public void write(final byte[] array, final int n, final int n2) throws IOException {
        if (n2 <= 0) {
            return;
        }
        if (this.splitLength == -1L) {
            this.raf.write(array, n, n2);
            this.bytesWrittenForThisPart += n2;
            return;
        }
        if (this.splitLength < 65536L) {
            throw new IOException("split length less than minimum allowed split length of 65536 Bytes");
        }
        if (this.bytesWrittenForThisPart >= this.splitLength) {
            this.startNextSplitFile();
            this.raf.write(array, n, n2);
            this.bytesWrittenForThisPart = n2;
            return;
        }
        if (this.bytesWrittenForThisPart + n2 <= this.splitLength) {
            this.raf.write(array, n, n2);
            this.bytesWrittenForThisPart += n2;
            return;
        }
        if (this.isHeaderData(array)) {
            this.startNextSplitFile();
            this.raf.write(array, n, n2);
            this.bytesWrittenForThisPart = n2;
            return;
        }
        this.raf.write(array, n, (int)(this.splitLength - this.bytesWrittenForThisPart));
        this.startNextSplitFile();
        this.raf.write(array, (int)(this.splitLength - this.bytesWrittenForThisPart) + n, (int)(n2 - (this.splitLength - this.bytesWrittenForThisPart)));
        this.bytesWrittenForThisPart = n2 - (this.splitLength - this.bytesWrittenForThisPart);
    }
}
