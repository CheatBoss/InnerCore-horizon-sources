package com.bumptech.glide.load.resource.bitmap;

import android.util.*;
import java.io.*;
import java.nio.*;

public class ImageHeaderParser
{
    private static final int[] BYTES_PER_FORMAT;
    private static final int EXIF_MAGIC_NUMBER = 65496;
    private static final int EXIF_SEGMENT_TYPE = 225;
    private static final int GIF_HEADER = 4671814;
    private static final int INTEL_TIFF_MAGIC_NUMBER = 18761;
    private static final String JPEG_EXIF_SEGMENT_PREAMBLE = "Exif\u0000\u0000";
    private static final byte[] JPEG_EXIF_SEGMENT_PREAMBLE_BYTES;
    private static final int MARKER_EOI = 217;
    private static final int MOTOROLA_TIFF_MAGIC_NUMBER = 19789;
    private static final int ORIENTATION_TAG_TYPE = 274;
    private static final int PNG_HEADER = -1991225785;
    private static final int SEGMENT_SOS = 218;
    private static final int SEGMENT_START_ID = 255;
    private static final String TAG = "ImageHeaderParser";
    private final StreamReader streamReader;
    
    static {
        BYTES_PER_FORMAT = new int[] { 0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8 };
        byte[] bytes = new byte[0];
        try {
            bytes = "Exif\u0000\u0000".getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException ex) {}
        JPEG_EXIF_SEGMENT_PREAMBLE_BYTES = bytes;
    }
    
    public ImageHeaderParser(final InputStream inputStream) {
        this.streamReader = new StreamReader(inputStream);
    }
    
    private static int calcTagOffset(final int n, final int n2) {
        return n + 2 + n2 * 12;
    }
    
    private byte[] getExifSegment() throws IOException {
        while (true) {
            final short uInt8 = this.streamReader.getUInt8();
            if (uInt8 != 255) {
                if (Log.isLoggable("ImageHeaderParser", 3)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown segmentId=");
                    sb.append(uInt8);
                    Log.d("ImageHeaderParser", sb.toString());
                }
                return null;
            }
            final short uInt9 = this.streamReader.getUInt8();
            if (uInt9 == 218) {
                return null;
            }
            if (uInt9 == 217) {
                if (Log.isLoggable("ImageHeaderParser", 3)) {
                    Log.d("ImageHeaderParser", "Found MARKER_EOI in exif segment");
                }
                return null;
            }
            final int n = this.streamReader.getUInt16() - 2;
            if (uInt9 != 225) {
                final long skip = this.streamReader.skip(n);
                if (skip != n) {
                    if (Log.isLoggable("ImageHeaderParser", 3)) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Unable to skip enough data, type: ");
                        sb2.append(uInt9);
                        sb2.append(", wanted to skip: ");
                        sb2.append(n);
                        sb2.append(", but actually skipped: ");
                        sb2.append(skip);
                        Log.d("ImageHeaderParser", sb2.toString());
                    }
                    return null;
                }
                continue;
            }
            else {
                final byte[] array = new byte[n];
                final int read = this.streamReader.read(array);
                if (read != n) {
                    if (Log.isLoggable("ImageHeaderParser", 3)) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Unable to read segment data, type: ");
                        sb3.append(uInt9);
                        sb3.append(", length: ");
                        sb3.append(n);
                        sb3.append(", actually read: ");
                        sb3.append(read);
                        Log.d("ImageHeaderParser", sb3.toString());
                    }
                    return null;
                }
                return array;
            }
        }
    }
    
    private static boolean handles(final int n) {
        return (n & 0xFFD8) == 0xFFD8 || n == 19789 || n == 18761;
    }
    
    private static int parseExifSegment(final RandomAccessReader randomAccessReader) {
        final int length = "Exif\u0000\u0000".length();
        final short int16 = randomAccessReader.getInt16(length);
        ByteOrder byteOrder;
        if (int16 == 19789) {
            byteOrder = ByteOrder.BIG_ENDIAN;
        }
        else if (int16 == 18761) {
            byteOrder = ByteOrder.LITTLE_ENDIAN;
        }
        else {
            if (Log.isLoggable("ImageHeaderParser", 3)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown endianness = ");
                sb.append(int16);
                Log.d("ImageHeaderParser", sb.toString());
            }
            byteOrder = ByteOrder.BIG_ENDIAN;
        }
        randomAccessReader.order(byteOrder);
        final int n = randomAccessReader.getInt32(length + 4) + length;
        final short int17 = randomAccessReader.getInt16(n);
        for (int i = 0; i < int17; ++i) {
            final int calcTagOffset = calcTagOffset(n, i);
            final short int18 = randomAccessReader.getInt16(calcTagOffset);
            if (int18 == 274) {
                final short int19 = randomAccessReader.getInt16(calcTagOffset + 2);
                if (int19 >= 1 && int19 <= 12) {
                    final int int20 = randomAccessReader.getInt32(calcTagOffset + 4);
                    if (int20 < 0) {
                        if (Log.isLoggable("ImageHeaderParser", 3)) {
                            Log.d("ImageHeaderParser", "Negative tiff component count");
                        }
                    }
                    else {
                        if (Log.isLoggable("ImageHeaderParser", 3)) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Got tagIndex=");
                            sb2.append(i);
                            sb2.append(" tagType=");
                            sb2.append(int18);
                            sb2.append(" formatCode=");
                            sb2.append(int19);
                            sb2.append(" componentCount=");
                            sb2.append(int20);
                            Log.d("ImageHeaderParser", sb2.toString());
                        }
                        final int n2 = ImageHeaderParser.BYTES_PER_FORMAT[int19] + int20;
                        if (n2 > 4) {
                            if (Log.isLoggable("ImageHeaderParser", 3)) {
                                final StringBuilder sb3 = new StringBuilder();
                                sb3.append("Got byte count > 4, not orientation, continuing, formatCode=");
                                sb3.append(int19);
                                Log.d("ImageHeaderParser", sb3.toString());
                            }
                        }
                        else {
                            final int n3 = calcTagOffset + 8;
                            if (n3 >= 0 && n3 <= randomAccessReader.length()) {
                                if (n2 >= 0 && n3 + n2 <= randomAccessReader.length()) {
                                    return randomAccessReader.getInt16(n3);
                                }
                                if (Log.isLoggable("ImageHeaderParser", 3)) {
                                    final StringBuilder sb4 = new StringBuilder();
                                    sb4.append("Illegal number of bytes for TI tag data tagType=");
                                    sb4.append(int18);
                                    Log.d("ImageHeaderParser", sb4.toString());
                                }
                            }
                            else if (Log.isLoggable("ImageHeaderParser", 3)) {
                                final StringBuilder sb5 = new StringBuilder();
                                sb5.append("Illegal tagValueOffset=");
                                sb5.append(n3);
                                sb5.append(" tagType=");
                                sb5.append(int18);
                                Log.d("ImageHeaderParser", sb5.toString());
                            }
                        }
                    }
                }
                else if (Log.isLoggable("ImageHeaderParser", 3)) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("Got invalid format code=");
                    sb6.append(int19);
                    Log.d("ImageHeaderParser", sb6.toString());
                }
            }
        }
        return -1;
    }
    
    public int getOrientation() throws IOException {
        if (!handles(this.streamReader.getUInt16())) {
            return -1;
        }
        final byte[] exifSegment = this.getExifSegment();
        int n = 0;
        boolean b2;
        final boolean b = b2 = (exifSegment != null && exifSegment.length > ImageHeaderParser.JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length);
        if (b) {
            while (true) {
                b2 = b;
                if (n >= ImageHeaderParser.JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length) {
                    break;
                }
                if (exifSegment[n] != ImageHeaderParser.JPEG_EXIF_SEGMENT_PREAMBLE_BYTES[n]) {
                    b2 = false;
                    break;
                }
                ++n;
            }
        }
        if (b2) {
            return parseExifSegment(new RandomAccessReader(exifSegment));
        }
        return -1;
    }
    
    public ImageType getType() throws IOException {
        final int uInt16 = this.streamReader.getUInt16();
        if (uInt16 == 65496) {
            return ImageType.JPEG;
        }
        final int n = (uInt16 << 16 & 0xFFFF0000) | (this.streamReader.getUInt16() & 0xFFFF);
        if (n == -1991225785) {
            this.streamReader.skip(21L);
            if (this.streamReader.getByte() >= 3) {
                return ImageType.PNG_A;
            }
            return ImageType.PNG;
        }
        else {
            if (n >> 8 == 4671814) {
                return ImageType.GIF;
            }
            return ImageType.UNKNOWN;
        }
    }
    
    public boolean hasAlpha() throws IOException {
        return this.getType().hasAlpha();
    }
    
    public enum ImageType
    {
        GIF(true), 
        JPEG(false), 
        PNG(false), 
        PNG_A(true), 
        UNKNOWN(false);
        
        private final boolean hasAlpha;
        
        private ImageType(final boolean hasAlpha) {
            this.hasAlpha = hasAlpha;
        }
        
        public boolean hasAlpha() {
            return this.hasAlpha;
        }
    }
    
    private static class RandomAccessReader
    {
        private final ByteBuffer data;
        
        public RandomAccessReader(final byte[] array) {
            (this.data = ByteBuffer.wrap(array)).order(ByteOrder.BIG_ENDIAN);
        }
        
        public short getInt16(final int n) {
            return this.data.getShort(n);
        }
        
        public int getInt32(final int n) {
            return this.data.getInt(n);
        }
        
        public int length() {
            return this.data.array().length;
        }
        
        public void order(final ByteOrder byteOrder) {
            this.data.order(byteOrder);
        }
    }
    
    private static class StreamReader
    {
        private final InputStream is;
        
        public StreamReader(final InputStream is) {
            this.is = is;
        }
        
        public int getByte() throws IOException {
            return this.is.read();
        }
        
        public int getUInt16() throws IOException {
            return (this.is.read() << 8 & 0xFF00) | (this.is.read() & 0xFF);
        }
        
        public short getUInt8() throws IOException {
            return (short)(this.is.read() & 0xFF);
        }
        
        public int read(final byte[] array) throws IOException {
            int i;
            int read;
            for (i = array.length; i > 0; i -= read) {
                read = this.is.read(array, array.length - i, i);
                if (read == -1) {
                    break;
                }
            }
            return array.length - i;
        }
        
        public long skip(final long n) throws IOException {
            if (n < 0L) {
                return 0L;
            }
            long n2 = n;
            while (n2 > 0L) {
                final long skip = this.is.skip(n2);
                if (skip > 0L) {
                    n2 -= skip;
                }
                else {
                    if (this.is.read() == -1) {
                        break;
                    }
                    --n2;
                }
            }
            return n - n2;
        }
    }
}
