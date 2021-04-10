package net.lingala.zip4j.core;

import net.lingala.zip4j.exception.*;
import java.util.*;
import net.lingala.zip4j.io.*;
import net.lingala.zip4j.util.*;
import net.lingala.zip4j.model.*;
import java.io.*;

public class HeaderWriter
{
    private final int ZIP64_EXTRA_BUF;
    
    public HeaderWriter() {
        this.ZIP64_EXTRA_BUF = 50;
    }
    
    private byte[] byteArrayListToByteArray(final List list) throws ZipException {
        if (list == null) {
            throw new ZipException("input byte array list is null, cannot conver to byte array");
        }
        if (list.size() <= 0) {
            return null;
        }
        final byte[] array = new byte[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            array[i] = Byte.parseByte(list.get(i));
        }
        return array;
    }
    
    private void copyByteArrayToArrayList(final byte[] array, final List list) throws ZipException {
        if (list != null && array != null) {
            for (int i = 0; i < array.length; ++i) {
                list.add(Byte.toString(array[i]));
            }
            return;
        }
        throw new ZipException("one of the input parameters is null, cannot copy byte array to array list");
    }
    
    private int countNumberOfFileHeaderEntriesOnDisk(final ArrayList list, final int n) throws ZipException {
        if (list == null) {
            throw new ZipException("file headers are null, cannot calculate number of entries on this disk");
        }
        int n2 = 0;
        int n3;
        for (int i = 0; i < list.size(); ++i, n2 = n3) {
            n3 = n2;
            if (list.get(i).getDiskNumberStart() == n) {
                n3 = n2 + 1;
            }
        }
        return n2;
    }
    
    private void processHeaderData(final ZipModel zipModel, final OutputStream outputStream) throws ZipException {
        int currSplitFileCounter = 0;
        try {
            if (outputStream instanceof SplitOutputStream) {
                zipModel.getEndCentralDirRecord().setOffsetOfStartOfCentralDir(((SplitOutputStream)outputStream).getFilePointer());
                currSplitFileCounter = ((SplitOutputStream)outputStream).getCurrSplitFileCounter();
            }
            if (zipModel.isZip64Format()) {
                if (zipModel.getZip64EndCentralDirRecord() == null) {
                    zipModel.setZip64EndCentralDirRecord(new Zip64EndCentralDirRecord());
                }
                if (zipModel.getZip64EndCentralDirLocator() == null) {
                    zipModel.setZip64EndCentralDirLocator(new Zip64EndCentralDirLocator());
                }
                zipModel.getZip64EndCentralDirLocator().setNoOfDiskStartOfZip64EndOfCentralDirRec(currSplitFileCounter);
                zipModel.getZip64EndCentralDirLocator().setTotNumberOfDiscs(currSplitFileCounter + 1);
            }
            zipModel.getEndCentralDirRecord().setNoOfThisDisk(currSplitFileCounter);
            zipModel.getEndCentralDirRecord().setNoOfThisDiskStartOfCentralDir(currSplitFileCounter);
        }
        catch (IOException ex) {
            throw new ZipException(ex);
        }
    }
    
    private void updateCompressedSizeInLocalFileHeader(final SplitOutputStream splitOutputStream, final LocalFileHeader localFileHeader, long n, final long n2, final byte[] array, final boolean b) throws ZipException {
        if (splitOutputStream == null) {
            throw new ZipException("invalid output stream, cannot update compressed size for local file header");
        }
        try {
            if (!localFileHeader.isWriteComprSizeInZip64ExtraRecord()) {
                splitOutputStream.seek(n + n2);
                splitOutputStream.write(array);
                return;
            }
            if (array.length != 8) {
                throw new ZipException("attempting to write a non 8-byte compressed size block for a zip64 file");
            }
            final long n3 = n = n + n2 + 4L + 4L + 2L + 2L + localFileHeader.getFileNameLength() + 2L + 2L + 8L;
            if (n2 == 22L) {
                n = n3 + 8L;
            }
            splitOutputStream.seek(n);
            splitOutputStream.write(array);
        }
        catch (IOException ex) {
            throw new ZipException(ex);
        }
    }
    
    private int writeCentralDirectory(final ZipModel zipModel, final OutputStream outputStream, final List list) throws ZipException {
        if (zipModel == null || outputStream == null) {
            throw new ZipException("input parameters is null, cannot write central directory");
        }
        final CentralDirectory centralDirectory = zipModel.getCentralDirectory();
        int i = 0;
        if (centralDirectory == null || zipModel.getCentralDirectory().getFileHeaders() == null) {
            return 0;
        }
        if (zipModel.getCentralDirectory().getFileHeaders().size() <= 0) {
            return 0;
        }
        int n = 0;
        while (i < zipModel.getCentralDirectory().getFileHeaders().size()) {
            n += this.writeFileHeader(zipModel, zipModel.getCentralDirectory().getFileHeaders().get(i), outputStream, list);
            ++i;
        }
        return n;
    }
    
    private void writeEndOfCentralDirectoryRecord(final ZipModel ex, final OutputStream outputStream, int commentLength, final long n, final List list) throws ZipException {
        if (ex == null || outputStream == null) {
            throw new ZipException("zip model or output stream is null, cannot write end of central directory record");
        }
        while (true) {
            while (true) {
                Label_0354: {
                    while (true) {
                        int size = 0;
                        Label_0346: {
                            try {
                                final byte[] array = new byte[2];
                                final byte[] array2 = new byte[4];
                                final byte[] array3 = new byte[8];
                                Raw.writeIntLittleEndian(array2, 0, (int)((ZipModel)ex).getEndCentralDirRecord().getSignature());
                                this.copyByteArrayToArrayList(array2, list);
                                Raw.writeShortLittleEndian(array, 0, (short)((ZipModel)ex).getEndCentralDirRecord().getNoOfThisDisk());
                                this.copyByteArrayToArrayList(array, list);
                                Raw.writeShortLittleEndian(array, 0, (short)((ZipModel)ex).getEndCentralDirRecord().getNoOfThisDiskStartOfCentralDir());
                                this.copyByteArrayToArrayList(array, list);
                                if (((ZipModel)ex).getCentralDirectory() == null) {
                                    break Label_0354;
                                }
                                if (((ZipModel)ex).getCentralDirectory().getFileHeaders() == null) {
                                    break Label_0312;
                                }
                                size = ((ZipModel)ex).getCentralDirectory().getFileHeaders().size();
                                if (!((ZipModel)ex).isSplitArchive()) {
                                    break Label_0346;
                                }
                                final int countNumberOfFileHeaderEntriesOnDisk = this.countNumberOfFileHeaderEntriesOnDisk(((ZipModel)ex).getCentralDirectory().getFileHeaders(), ((ZipModel)ex).getEndCentralDirRecord().getNoOfThisDisk());
                                Raw.writeShortLittleEndian(array, 0, (short)countNumberOfFileHeaderEntriesOnDisk);
                                this.copyByteArrayToArrayList(array, list);
                                Raw.writeShortLittleEndian(array, 0, (short)size);
                                this.copyByteArrayToArrayList(array, list);
                                try {
                                    Raw.writeIntLittleEndian(array2, 0, commentLength);
                                    this.copyByteArrayToArrayList(array2, list);
                                    if (n > 4294967295L) {
                                        Raw.writeLongLittleEndian(array3, 0, 4294967295L);
                                        System.arraycopy(array3, 0, array2, 0, 4);
                                        this.copyByteArrayToArrayList(array2, list);
                                    }
                                    else {
                                        Raw.writeLongLittleEndian(array3, 0, n);
                                        System.arraycopy(array3, 0, array2, 0, 4);
                                        this.copyByteArrayToArrayList(array2, list);
                                    }
                                    commentLength = 0;
                                    if (((ZipModel)ex).getEndCentralDirRecord().getComment() != null) {
                                        commentLength = ((ZipModel)ex).getEndCentralDirRecord().getCommentLength();
                                    }
                                    Raw.writeShortLittleEndian(array, 0, (short)commentLength);
                                    this.copyByteArrayToArrayList(array, list);
                                    if (commentLength > 0) {
                                        this.copyByteArrayToArrayList(((ZipModel)ex).getEndCentralDirRecord().getCommentBytes(), list);
                                        return;
                                    }
                                    return;
                                    throw new ZipException("invalid central directory/file headers, cannot write end of central directory record");
                                }
                                catch (Exception ex) {}
                            }
                            catch (Exception ex2) {}
                            throw new ZipException(ex);
                        }
                        final int countNumberOfFileHeaderEntriesOnDisk = size;
                        continue;
                    }
                }
                continue;
            }
        }
    }
    
    private int writeFileHeader(final ZipModel ex, final FileHeader fileHeader, final OutputStream outputStream, final List list) throws ZipException {
        if (fileHeader != null && outputStream != null) {
        Label_0746_Outer:
            while (true) {
            Label_0669_Outer:
                while (true) {
                    while (true) {
                        byte[] array;
                        byte[] array2;
                        byte[] array3;
                        byte[] array5;
                        byte[] array4;
                        int n;
                        int n2;
                        boolean b = false;
                        byte[] array6;
                        boolean b2 = false;
                        int n4;
                        int n3;
                        int n5;
                        int n6 = 0;
                        int n7;
                        int n8;
                        int n9;
                        AESExtraDataRecord aesExtraDataRecord;
                        int n10;
                        byte[] bytes;
                        int n12;
                        int n11;
                        Block_14_Outer:Label_0634_Outer:
                        while (true) {
                            Label_0930: {
                                try {
                                    array = new byte[2];
                                    array2 = new byte[4];
                                    array3 = new byte[8];
                                    array4 = (array5 = new byte[2]);
                                    array5[1] = (array5[0] = 0);
                                    Raw.writeIntLittleEndian(array2, 0, fileHeader.getSignature());
                                    this.copyByteArrayToArrayList(array2, list);
                                    Raw.writeShortLittleEndian(array, 0, (short)fileHeader.getVersionMadeBy());
                                    this.copyByteArrayToArrayList(array, list);
                                    Raw.writeShortLittleEndian(array, 0, (short)fileHeader.getVersionNeededToExtract());
                                    this.copyByteArrayToArrayList(array, list);
                                    this.copyByteArrayToArrayList(fileHeader.getGeneralPurposeFlag(), list);
                                    Raw.writeShortLittleEndian(array, 0, (short)fileHeader.getCompressionMethod());
                                    this.copyByteArrayToArrayList(array, list);
                                    Raw.writeIntLittleEndian(array2, 0, fileHeader.getLastModFileTime());
                                    this.copyByteArrayToArrayList(array2, list);
                                    Raw.writeIntLittleEndian(array2, 0, (int)fileHeader.getCrc32());
                                    this.copyByteArrayToArrayList(array2, list);
                                    n = 0 + 4 + 2 + 2 + 2 + 2 + 4 + 4;
                                    if (fileHeader.getCompressedSize() < 4294967295L && fileHeader.getUncompressedSize() + 50L < 4294967295L) {
                                        Raw.writeLongLittleEndian(array3, 0, fileHeader.getCompressedSize());
                                        System.arraycopy(array3, 0, array2, 0, 4);
                                        this.copyByteArrayToArrayList(array2, list);
                                        Raw.writeLongLittleEndian(array3, 0, fileHeader.getUncompressedSize());
                                        System.arraycopy(array3, 0, array2, 0, 4);
                                        this.copyByteArrayToArrayList(array2, list);
                                        n2 = n + 4 + 4;
                                        b = false;
                                    }
                                    else {
                                        Raw.writeLongLittleEndian(array3, 0, 4294967295L);
                                        System.arraycopy(array3, 0, array2, 0, 4);
                                        this.copyByteArrayToArrayList(array2, list);
                                        this.copyByteArrayToArrayList(array2, list);
                                        n2 = n + 4 + 4;
                                        b = true;
                                    }
                                    Raw.writeShortLittleEndian(array, 0, (short)fileHeader.getFileNameLength());
                                    this.copyByteArrayToArrayList(array, list);
                                    array6 = new byte[4];
                                    if (fileHeader.getOffsetLocalHeader() > 4294967295L) {
                                        Raw.writeLongLittleEndian(array3, 0, 4294967295L);
                                        System.arraycopy(array3, 0, array6, 0, 4);
                                        b2 = true;
                                        break Label_0930;
                                    }
                                    Raw.writeLongLittleEndian(array3, 0, fileHeader.getOffsetLocalHeader());
                                    System.arraycopy(array3, 0, array6, 0, 4);
                                    b2 = false;
                                    break Label_0930;
                                    n3 = n4;
                                    // iftrue(Label_0424:, fileHeader.getAesExtraDataRecord() == null)
                                    // iftrue(Label_0589:, !Zip4jUtil.isStringNotNullAndNotEmpty(ex.getFileNameCharset()))
                                    // iftrue(Label_0631:, !b2)
                                    // iftrue(Label_0492:, fileHeader.getExternalFileAttr() == null)
                                    // iftrue(Label_0634:, b)
                                    while (true) {
                                        Block_15:Block_12_Outer:
                                        while (true) {
                                        Label_0618:
                                            while (true) {
                                                Label_0424: {
                                                    while (true) {
                                                        Block_13: {
                                                            Block_11: {
                                                                break Block_11;
                                                                while (true) {
                                                                    this.copyByteArrayToArrayList(array6, list);
                                                                    n5 = n2 + 2 + 2 + 2 + 2 + 2 + 4 + 4;
                                                                    break Block_13;
                                                                    Label_0492: {
                                                                        this.copyByteArrayToArrayList(new byte[] { 0, 0, 0, 0 }, list);
                                                                    }
                                                                    continue Block_14_Outer;
                                                                    break Block_15;
                                                                    this.copyByteArrayToArrayList(fileHeader.getExternalFileAttr(), list);
                                                                    continue Block_14_Outer;
                                                                }
                                                                try {
                                                                    ((ZipModel)ex).setZip64Format(true);
                                                                    Raw.writeShortLittleEndian(array, 0, (short)1);
                                                                    this.copyByteArrayToArrayList(array, list);
                                                                    n6 = 0;
                                                                    if (b) {
                                                                        n6 = 0 + 16;
                                                                    }
                                                                    break Block_14_Outer;
                                                                    // iftrue(Label_0780:, !b2)
                                                                    // iftrue(Label_1002:, !b)
                                                                Block_19:
                                                                    while (true) {
                                                                        Raw.writeLongLittleEndian(array3, 0, fileHeader.getUncompressedSize());
                                                                        this.copyByteArrayToArrayList(array3, list);
                                                                        Raw.writeLongLittleEndian(array3, 0, fileHeader.getCompressedSize());
                                                                        this.copyByteArrayToArrayList(array3, list);
                                                                        n7 = n7 + 8 + 8;
                                                                        n8 = n7;
                                                                        break Block_19;
                                                                        Raw.writeShortLittleEndian(array, 0, (short)n9);
                                                                        this.copyByteArrayToArrayList(array, list);
                                                                        n7 = n8 + 2 + 2;
                                                                        continue Label_0746_Outer;
                                                                    }
                                                                    Raw.writeLongLittleEndian(array3, 0, fileHeader.getOffsetLocalHeader());
                                                                    this.copyByteArrayToArrayList(array3, list);
                                                                    n8 = n7 + 8;
                                                                    break Label_0780;
                                                                    // iftrue(Label_0902:, fileHeader.getAesExtraDataRecord() == null)
                                                                    while (true) {
                                                                        aesExtraDataRecord = fileHeader.getAesExtraDataRecord();
                                                                        Raw.writeShortLittleEndian(array, 0, (short)aesExtraDataRecord.getSignature());
                                                                        this.copyByteArrayToArrayList(array, list);
                                                                        Raw.writeShortLittleEndian(array, 0, (short)aesExtraDataRecord.getDataSize());
                                                                        this.copyByteArrayToArrayList(array, list);
                                                                        Raw.writeShortLittleEndian(array, 0, (short)aesExtraDataRecord.getVersionNumber());
                                                                        this.copyByteArrayToArrayList(array, list);
                                                                        this.copyByteArrayToArrayList(aesExtraDataRecord.getVendorID().getBytes(), list);
                                                                        this.copyByteArrayToArrayList(new byte[] { (byte)aesExtraDataRecord.getAesStrength() }, list);
                                                                        Raw.writeShortLittleEndian(array, 0, (short)aesExtraDataRecord.getCompressionMethod());
                                                                        this.copyByteArrayToArrayList(array, list);
                                                                        n10 = n8 + 11;
                                                                        return n10;
                                                                        n10 = n8;
                                                                        continue;
                                                                    }
                                                                    Label_0902: {
                                                                        return n10;
                                                                    }
                                                                }
                                                                catch (Exception ex) {}
                                                            }
                                                            n3 = n4 + 11;
                                                            break Label_0424;
                                                        }
                                                        bytes = fileHeader.getFileName().getBytes(((ZipModel)ex).getFileNameCharset());
                                                        this.copyByteArrayToArrayList(bytes, list);
                                                        n8 = n5 + bytes.length;
                                                        break Label_0618;
                                                        Label_0631: {
                                                            continue;
                                                        }
                                                    }
                                                    Label_0589: {
                                                        this.copyByteArrayToArrayList(Zip4jUtil.convertCharset(fileHeader.getFileName()), list);
                                                    }
                                                    n8 = n5 + Zip4jUtil.getEncodedStringLength(fileHeader.getFileName());
                                                    break Label_0618;
                                                }
                                                Raw.writeShortLittleEndian(array, 0, (short)n3);
                                                this.copyByteArrayToArrayList(array, list);
                                                this.copyByteArrayToArrayList(array4, list);
                                                Raw.writeShortLittleEndian(array, 0, (short)fileHeader.getDiskNumberStart());
                                                this.copyByteArrayToArrayList(array, list);
                                                this.copyByteArrayToArrayList(array4, list);
                                                continue Label_0634_Outer;
                                            }
                                            continue Block_12_Outer;
                                        }
                                        continue Label_0746_Outer;
                                    }
                                }
                                catch (Exception ex2) {}
                                break;
                            }
                            n4 = 0;
                            if (!b && !b2) {
                                continue Label_0746_Outer;
                            }
                            n11 = (n12 = 0 + 4);
                            if (b) {
                                n12 = n11 + 16;
                            }
                            n4 = n12;
                            if (b2) {
                                n4 = n12 + 8;
                                continue Label_0746_Outer;
                            }
                            continue Label_0746_Outer;
                        }
                        n9 = n6;
                        if (b2) {
                            n9 = n6 + 8;
                            continue;
                        }
                        continue;
                    }
                    Label_1002: {
                        continue Label_0669_Outer;
                    }
                }
            }
            throw new ZipException(ex);
        }
        throw new ZipException("input parameters is null, cannot write local file header");
    }
    
    private void writeZip64EndOfCentralDirectoryLocator(final ZipModel zipModel, final OutputStream outputStream, final List list) throws ZipException {
        if (zipModel != null) {
            if (outputStream != null) {
                try {
                    final byte[] array = new byte[4];
                    final byte[] array2 = new byte[8];
                    Raw.writeIntLittleEndian(array, 0, 117853008);
                    this.copyByteArrayToArrayList(array, list);
                    Raw.writeIntLittleEndian(array, 0, zipModel.getZip64EndCentralDirLocator().getNoOfDiskStartOfZip64EndOfCentralDirRec());
                    this.copyByteArrayToArrayList(array, list);
                    Raw.writeLongLittleEndian(array2, 0, zipModel.getZip64EndCentralDirLocator().getOffsetZip64EndOfCentralDirRec());
                    this.copyByteArrayToArrayList(array2, list);
                    Raw.writeIntLittleEndian(array, 0, zipModel.getZip64EndCentralDirLocator().getTotNumberOfDiscs());
                    this.copyByteArrayToArrayList(array, list);
                    return;
                }
                catch (Exception ex) {
                    throw new ZipException(ex);
                }
                catch (ZipException ex2) {
                    throw ex2;
                }
            }
        }
        throw new ZipException("zip model or output stream is null, cannot write zip64 end of central directory locator");
    }
    
    private void writeZip64EndOfCentralDirectoryRecord(final ZipModel zipModel, final OutputStream outputStream, final int n, final long n2, final List list) throws ZipException {
        if (zipModel != null) {
            if (outputStream != null) {
                while (true) {
                    while (true) {
                        int size = 0;
                        Label_0394: {
                            try {
                                final byte[] array = new byte[2];
                                final byte[] array3;
                                final byte[] array2 = array3 = new byte[2];
                                array3[1] = (array3[0] = 0);
                                final byte[] array4 = new byte[4];
                                final byte[] array5 = new byte[8];
                                Raw.writeIntLittleEndian(array4, 0, 101075792);
                                this.copyByteArrayToArrayList(array4, list);
                                Raw.writeLongLittleEndian(array5, 0, 44L);
                                this.copyByteArrayToArrayList(array5, list);
                                if (zipModel.getCentralDirectory() != null && zipModel.getCentralDirectory().getFileHeaders() != null && zipModel.getCentralDirectory().getFileHeaders().size() > 0) {
                                    Raw.writeShortLittleEndian(array, 0, (short)zipModel.getCentralDirectory().getFileHeaders().get(0).getVersionMadeBy());
                                    this.copyByteArrayToArrayList(array, list);
                                    Raw.writeShortLittleEndian(array, 0, (short)zipModel.getCentralDirectory().getFileHeaders().get(0).getVersionNeededToExtract());
                                    this.copyByteArrayToArrayList(array, list);
                                }
                                else {
                                    this.copyByteArrayToArrayList(array2, list);
                                    this.copyByteArrayToArrayList(array2, list);
                                }
                                Raw.writeIntLittleEndian(array4, 0, zipModel.getEndCentralDirRecord().getNoOfThisDisk());
                                this.copyByteArrayToArrayList(array4, list);
                                Raw.writeIntLittleEndian(array4, 0, zipModel.getEndCentralDirRecord().getNoOfThisDiskStartOfCentralDir());
                                this.copyByteArrayToArrayList(array4, list);
                                final int n3 = 0;
                                if (zipModel.getCentralDirectory() == null || zipModel.getCentralDirectory().getFileHeaders() == null) {
                                    throw new ZipException("invalid central directory/file headers, cannot write end of central directory record");
                                }
                                size = zipModel.getCentralDirectory().getFileHeaders().size();
                                if (zipModel.isSplitArchive()) {
                                    this.countNumberOfFileHeaderEntriesOnDisk(zipModel.getCentralDirectory().getFileHeaders(), zipModel.getEndCentralDirRecord().getNoOfThisDisk());
                                    Raw.writeLongLittleEndian(array5, 0, n3);
                                    this.copyByteArrayToArrayList(array5, list);
                                    Raw.writeLongLittleEndian(array5, 0, size);
                                    this.copyByteArrayToArrayList(array5, list);
                                    Raw.writeLongLittleEndian(array5, 0, n);
                                    this.copyByteArrayToArrayList(array5, list);
                                    Raw.writeLongLittleEndian(array5, 0, n2);
                                    this.copyByteArrayToArrayList(array5, list);
                                    return;
                                }
                                break Label_0394;
                            }
                            catch (Exception ex) {
                                throw new ZipException(ex);
                            }
                            catch (ZipException ex2) {
                                throw ex2;
                            }
                            break;
                        }
                        final int n3 = size;
                        continue;
                    }
                }
            }
        }
        throw new ZipException("zip model or output stream is null, cannot write zip64 end of central directory record");
    }
    
    private void writeZipHeaderBytes(final ZipModel zipModel, final OutputStream outputStream, final byte[] array) throws ZipException {
        if (array == null) {
            throw new ZipException("invalid buff to write as zip headers");
        }
        try {
            if (outputStream instanceof SplitOutputStream && ((SplitOutputStream)outputStream).checkBuffSizeAndStartNextSplitFile(array.length)) {
                this.finalizeZipFile(zipModel, outputStream);
                return;
            }
            outputStream.write(array);
        }
        catch (IOException ex) {
            throw new ZipException(ex);
        }
    }
    
    public void finalizeZipFile(final ZipModel zipModel, final OutputStream outputStream) throws ZipException {
        if (zipModel != null) {
            if (outputStream != null) {
                try {
                    this.processHeaderData(zipModel, outputStream);
                    final long offsetOfStartOfCentralDir = zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir();
                    final ArrayList list = new ArrayList();
                    final int writeCentralDirectory = this.writeCentralDirectory(zipModel, outputStream, list);
                    if (zipModel.isZip64Format()) {
                        if (zipModel.getZip64EndCentralDirRecord() == null) {
                            zipModel.setZip64EndCentralDirRecord(new Zip64EndCentralDirRecord());
                        }
                        if (zipModel.getZip64EndCentralDirLocator() == null) {
                            zipModel.setZip64EndCentralDirLocator(new Zip64EndCentralDirLocator());
                        }
                        zipModel.getZip64EndCentralDirLocator().setOffsetZip64EndOfCentralDirRec(offsetOfStartOfCentralDir + writeCentralDirectory);
                        if (outputStream instanceof SplitOutputStream) {
                            zipModel.getZip64EndCentralDirLocator().setNoOfDiskStartOfZip64EndOfCentralDirRec(((SplitOutputStream)outputStream).getCurrSplitFileCounter());
                            zipModel.getZip64EndCentralDirLocator().setTotNumberOfDiscs(((SplitOutputStream)outputStream).getCurrSplitFileCounter() + 1);
                        }
                        else {
                            zipModel.getZip64EndCentralDirLocator().setNoOfDiskStartOfZip64EndOfCentralDirRec(0);
                            zipModel.getZip64EndCentralDirLocator().setTotNumberOfDiscs(1);
                        }
                        this.writeZip64EndOfCentralDirectoryRecord(zipModel, outputStream, writeCentralDirectory, offsetOfStartOfCentralDir, list);
                        this.writeZip64EndOfCentralDirectoryLocator(zipModel, outputStream, list);
                    }
                    this.writeEndOfCentralDirectoryRecord(zipModel, outputStream, writeCentralDirectory, offsetOfStartOfCentralDir, list);
                    this.writeZipHeaderBytes(zipModel, outputStream, this.byteArrayListToByteArray(list));
                    return;
                }
                catch (Exception ex) {
                    throw new ZipException(ex);
                }
                catch (ZipException ex2) {
                    throw ex2;
                }
            }
        }
        throw new ZipException("input parameters is null, cannot finalize zip file");
    }
    
    public void finalizeZipFileWithoutValidations(final ZipModel zipModel, final OutputStream outputStream) throws ZipException {
        if (zipModel != null) {
            if (outputStream != null) {
                try {
                    final ArrayList list = new ArrayList();
                    final long offsetOfStartOfCentralDir = zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir();
                    final int writeCentralDirectory = this.writeCentralDirectory(zipModel, outputStream, list);
                    if (zipModel.isZip64Format()) {
                        if (zipModel.getZip64EndCentralDirRecord() == null) {
                            zipModel.setZip64EndCentralDirRecord(new Zip64EndCentralDirRecord());
                        }
                        if (zipModel.getZip64EndCentralDirLocator() == null) {
                            zipModel.setZip64EndCentralDirLocator(new Zip64EndCentralDirLocator());
                        }
                        zipModel.getZip64EndCentralDirLocator().setOffsetZip64EndOfCentralDirRec(offsetOfStartOfCentralDir + writeCentralDirectory);
                        this.writeZip64EndOfCentralDirectoryRecord(zipModel, outputStream, writeCentralDirectory, offsetOfStartOfCentralDir, list);
                        this.writeZip64EndOfCentralDirectoryLocator(zipModel, outputStream, list);
                    }
                    this.writeEndOfCentralDirectoryRecord(zipModel, outputStream, writeCentralDirectory, offsetOfStartOfCentralDir, list);
                    this.writeZipHeaderBytes(zipModel, outputStream, this.byteArrayListToByteArray(list));
                    return;
                }
                catch (Exception ex) {
                    throw new ZipException(ex);
                }
                catch (ZipException ex2) {
                    throw ex2;
                }
            }
        }
        throw new ZipException("input parameters is null, cannot finalize zip file without validations");
    }
    
    public void updateLocalFileHeader(LocalFileHeader ex, final long n, final int n2, final ZipModel zipModel, final byte[] array, int n3, final SplitOutputStream splitOutputStream) throws ZipException {
        if (ex != null && n >= 0L && zipModel != null) {
        Label_0257_Outer:
            while (true) {
                final int n4 = 0;
            Label_0296_Outer:
                while (true) {
                Label_0296:
                    while (true) {
                        while (true) {
                        Label_0355:
                            while (true) {
                                Label_0344: {
                                    try {
                                        if (n3 == splitOutputStream.getCurrSplitFileCounter()) {
                                            break Label_0344;
                                        }
                                        final File file = new File(zipModel.getZipFile());
                                        final String parent = file.getParent();
                                        final String zipFileNameWithoutExt = Zip4jUtil.getZipFileNameWithoutExt(file.getName());
                                        final StringBuilder sb = new StringBuilder();
                                        sb.append(parent);
                                        sb.append(System.getProperty("file.separator"));
                                        final String string = sb.toString();
                                        String s;
                                        if (n3 < 9) {
                                            final StringBuilder sb2 = new StringBuilder();
                                            sb2.append(string);
                                            sb2.append(zipFileNameWithoutExt);
                                            sb2.append(".z0");
                                            sb2.append(n3 + 1);
                                            s = sb2.toString();
                                        }
                                        else {
                                            final StringBuilder sb3 = new StringBuilder();
                                            sb3.append(string);
                                            sb3.append(zipFileNameWithoutExt);
                                            sb3.append(".z");
                                            sb3.append(n3 + 1);
                                            s = sb3.toString();
                                        }
                                        final Object o = new SplitOutputStream(new File(s));
                                        n3 = 1;
                                        final long filePointer = ((SplitOutputStream)o).getFilePointer();
                                        if (o == null) {
                                            throw new ZipException("invalid output stream handler, cannot update local file header");
                                        }
                                        break Label_0355;
                                        while (true) {
                                            try {
                                                splitOutputStream.seek(filePointer);
                                                return;
                                            }
                                            catch (Exception ex) {}
                                            this.updateCompressedSizeInLocalFileHeader((SplitOutputStream)o, (LocalFileHeader)ex, n, n2, array, zipModel.isZip64Format());
                                            break Label_0296;
                                            ex = (Exception)o;
                                            ((SplitOutputStream)ex).seek(n + n2);
                                            try {
                                                ((SplitOutputStream)ex).write(array);
                                                if (n3 != 0) {
                                                    ((SplitOutputStream)o).close();
                                                    return;
                                                }
                                            }
                                            catch (Exception ex) {
                                                break;
                                            }
                                            continue Label_0257_Outer;
                                        }
                                    }
                                    catch (Exception ex2) {}
                                    break;
                                }
                                final Object o = splitOutputStream;
                                n3 = n4;
                                continue Label_0257_Outer;
                            }
                            if (n2 == 14) {
                                continue;
                            }
                            break;
                        }
                        if (n2 != 18 && n2 != 22) {
                            continue Label_0296;
                        }
                        break;
                    }
                    continue Label_0296_Outer;
                }
            }
            throw new ZipException(ex);
        }
        throw new ZipException("invalid input parameters, cannot update local file header");
    }
    
    public int writeExtendedLocalHeader(final LocalFileHeader localFileHeader, final OutputStream outputStream) throws ZipException, IOException {
        if (localFileHeader != null && outputStream != null) {
            final ArrayList list = new ArrayList();
            final byte[] array = new byte[4];
            Raw.writeIntLittleEndian(array, 0, 134695760);
            this.copyByteArrayToArrayList(array, list);
            Raw.writeIntLittleEndian(array, 0, (int)localFileHeader.getCrc32());
            this.copyByteArrayToArrayList(array, list);
            long compressedSize;
            if ((compressedSize = localFileHeader.getCompressedSize()) >= 2147483647L) {
                compressedSize = 2147483647L;
            }
            Raw.writeIntLittleEndian(array, 0, (int)compressedSize);
            this.copyByteArrayToArrayList(array, list);
            long uncompressedSize;
            if ((uncompressedSize = localFileHeader.getUncompressedSize()) >= 2147483647L) {
                uncompressedSize = 2147483647L;
            }
            Raw.writeIntLittleEndian(array, 0, (int)uncompressedSize);
            this.copyByteArrayToArrayList(array, list);
            final byte[] byteArrayListToByteArray = this.byteArrayListToByteArray(list);
            outputStream.write(byteArrayListToByteArray);
            return byteArrayListToByteArray.length;
        }
        throw new ZipException("input parameters is null, cannot write extended local header");
    }
    
    public int writeLocalFileHeader(ZipModel ex, final LocalFileHeader localFileHeader, final OutputStream outputStream) throws ZipException {
        if (localFileHeader == null) {
            throw new ZipException("input parameters are null, cannot write local file header");
        }
        while (true) {
            while (true) {
                Label_0695: {
                    try {
                        final ArrayList list = new ArrayList();
                        final byte[] array = new byte[2];
                        final byte[] array2 = new byte[4];
                        final byte[] array3 = new byte[8];
                        Raw.writeIntLittleEndian(array2, 0, localFileHeader.getSignature());
                        this.copyByteArrayToArrayList(array2, list);
                        Raw.writeShortLittleEndian(array, 0, (short)localFileHeader.getVersionNeededToExtract());
                        this.copyByteArrayToArrayList(array, list);
                        this.copyByteArrayToArrayList(localFileHeader.getGeneralPurposeFlag(), list);
                        Raw.writeShortLittleEndian(array, 0, (short)localFileHeader.getCompressionMethod());
                        this.copyByteArrayToArrayList(array, list);
                        Raw.writeIntLittleEndian(array2, 0, localFileHeader.getLastModFileTime());
                        this.copyByteArrayToArrayList(array2, list);
                        Raw.writeIntLittleEndian(array2, 0, (int)localFileHeader.getCrc32());
                        this.copyByteArrayToArrayList(array2, list);
                        boolean b = false;
                        if (localFileHeader.getUncompressedSize() + 50L >= 4294967295L) {
                            Raw.writeLongLittleEndian(array3, 0, 4294967295L);
                            System.arraycopy(array3, 0, array2, 0, 4);
                            this.copyByteArrayToArrayList(array2, list);
                            this.copyByteArrayToArrayList(array2, list);
                            ((ZipModel)ex).setZip64Format(true);
                            b = true;
                            localFileHeader.setWriteComprSizeInZip64ExtraRecord(true);
                        }
                        else {
                            Raw.writeLongLittleEndian(array3, 0, localFileHeader.getCompressedSize());
                            System.arraycopy(array3, 0, array2, 0, 4);
                            this.copyByteArrayToArrayList(array2, list);
                            Raw.writeLongLittleEndian(array3, 0, localFileHeader.getUncompressedSize());
                            System.arraycopy(array3, 0, array2, 0, 4);
                            this.copyByteArrayToArrayList(array2, list);
                            localFileHeader.setWriteComprSizeInZip64ExtraRecord(false);
                        }
                        Raw.writeShortLittleEndian(array, 0, (short)localFileHeader.getFileNameLength());
                        this.copyByteArrayToArrayList(array, list);
                        int n = 0;
                        if (b) {
                            n = 0 + 20;
                        }
                        int n2 = n;
                        if (localFileHeader.getAesExtraDataRecord() != null) {
                            n2 = n + 11;
                        }
                        Raw.writeShortLittleEndian(array, 0, (short)n2);
                        this.copyByteArrayToArrayList(array, list);
                        final int n3 = 0 + 4 + 2 + 2 + 2 + 4 + 4 + 8 + 2 + 2;
                        if (Zip4jUtil.isStringNotNullAndNotEmpty(((ZipModel)ex).getFileNameCharset())) {
                            ex = (Exception)(Object)localFileHeader.getFileName().getBytes(((ZipModel)ex).getFileNameCharset());
                            this.copyByteArrayToArrayList((byte[])(Object)ex, list);
                            final int n4 = n3 + ex.length;
                        }
                        else {
                            this.copyByteArrayToArrayList(Zip4jUtil.convertCharset(localFileHeader.getFileName()), list);
                            final int n5 = n3 + Zip4jUtil.getEncodedStringLength(localFileHeader.getFileName());
                        }
                        if (!b) {
                            break Label_0695;
                        }
                        Raw.writeShortLittleEndian(array, 0, (short)1);
                        this.copyByteArrayToArrayList(array, list);
                        Raw.writeShortLittleEndian(array, 0, (short)16);
                        this.copyByteArrayToArrayList(array, list);
                        Raw.writeLongLittleEndian(array3, 0, localFileHeader.getUncompressedSize());
                        this.copyByteArrayToArrayList(array3, list);
                        this.copyByteArrayToArrayList(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 }, list);
                        if (localFileHeader.getAesExtraDataRecord() != null) {
                            ex = (Exception)localFileHeader.getAesExtraDataRecord();
                            Raw.writeShortLittleEndian(array, 0, (short)((AESExtraDataRecord)ex).getSignature());
                            this.copyByteArrayToArrayList(array, list);
                            Raw.writeShortLittleEndian(array, 0, (short)((AESExtraDataRecord)ex).getDataSize());
                            this.copyByteArrayToArrayList(array, list);
                            Raw.writeShortLittleEndian(array, 0, (short)((AESExtraDataRecord)ex).getVersionNumber());
                            this.copyByteArrayToArrayList(array, list);
                            this.copyByteArrayToArrayList(((AESExtraDataRecord)ex).getVendorID().getBytes(), list);
                            this.copyByteArrayToArrayList(new byte[] { (byte)((AESExtraDataRecord)ex).getAesStrength() }, list);
                            Raw.writeShortLittleEndian(array, 0, (short)((AESExtraDataRecord)ex).getCompressionMethod());
                            this.copyByteArrayToArrayList(array, list);
                        }
                        ex = (Exception)(Object)this.byteArrayListToByteArray(list);
                        try {
                            outputStream.write((byte[])(Object)ex);
                            return ex.length;
                        }
                        catch (Exception ex) {}
                        catch (ZipException ex) {}
                    }
                    catch (Exception ex2) {}
                    catch (ZipException ex) {
                        throw ex;
                    }
                }
                continue;
            }
        }
    }
}
