package com.bumptech.glide.gifdecoder;

import android.graphics.*;
import android.util.*;
import android.os.*;
import android.annotation.*;
import java.io.*;
import java.nio.*;
import java.util.*;

public class GifDecoder
{
    private static final Bitmap$Config BITMAP_CONFIG;
    private static final int DISPOSAL_BACKGROUND = 2;
    private static final int DISPOSAL_NONE = 1;
    private static final int DISPOSAL_PREVIOUS = 3;
    private static final int DISPOSAL_UNSPECIFIED = 0;
    private static final int INITIAL_FRAME_POINTER = -1;
    private static final int MAX_STACK_SIZE = 4096;
    private static final int NULL_CODE = -1;
    public static final int STATUS_FORMAT_ERROR = 1;
    public static final int STATUS_OK = 0;
    public static final int STATUS_OPEN_ERROR = 2;
    public static final int STATUS_PARTIAL_DECODE = 3;
    private static final String TAG;
    private int[] act;
    private BitmapProvider bitmapProvider;
    private final byte[] block;
    private byte[] data;
    private int framePointer;
    private GifHeader header;
    private byte[] mainPixels;
    private int[] mainScratch;
    private GifHeaderParser parser;
    private byte[] pixelStack;
    private short[] prefix;
    private Bitmap previousImage;
    private ByteBuffer rawData;
    private boolean savePrevious;
    private int status;
    private byte[] suffix;
    
    static {
        TAG = GifDecoder.class.getSimpleName();
        BITMAP_CONFIG = Bitmap$Config.ARGB_8888;
    }
    
    public GifDecoder(final BitmapProvider bitmapProvider) {
        this.block = new byte[256];
        this.bitmapProvider = bitmapProvider;
        this.header = new GifHeader();
    }
    
    private void decodeBitmapData(final GifFrame gifFrame) {
        if (gifFrame != null) {
            this.rawData.position(gifFrame.bufferFrameStart);
        }
        int n;
        int n2;
        if (gifFrame == null) {
            n = this.header.width;
            n2 = this.header.height;
        }
        else {
            n = gifFrame.iw;
            n2 = gifFrame.ih;
        }
        final int n3 = n * n2;
        if (this.mainPixels == null || this.mainPixels.length < n3) {
            this.mainPixels = new byte[n3];
        }
        if (this.prefix == null) {
            this.prefix = new short[4096];
        }
        if (this.suffix == null) {
            this.suffix = new byte[4096];
        }
        if (this.pixelStack == null) {
            this.pixelStack = new byte[4097];
        }
        final int read = this.read();
        int n4 = 1;
        final int n5 = 1 << read;
        int n6 = read + 1;
        int n7 = (1 << n6) - 1;
        for (int i = 0; i < n5; ++i) {
            this.prefix[i] = 0;
            this.suffix[i] = (byte)i;
        }
        int n8 = 0;
        int j = 0;
        int n9 = 0;
        int n10 = 0;
        int n11 = 0;
        int n12 = 0;
        int k = 0;
        int n13 = -1;
        int n14 = n5 + 2;
        int l = 0;
    Label_0220:
        while (l < n3) {
            final int n15 = 3;
            int n16 = n8;
            int block;
            if ((block = n10) == 0) {
                block = this.readBlock();
                if (block <= 0) {
                    this.status = 3;
                    break;
                }
                n16 = 0;
            }
            n12 += (this.block[n16] & 0xFF) << n11;
            final int n17 = n16 + n4;
            final int n18 = block - 1;
            final int n19 = n9;
            final int n20 = j;
            final int n21 = l;
            final int n22 = n11 + 8;
            int n23 = n19;
            int n24 = n20;
            int n25 = n21;
            int n26 = n13;
            int status = n15;
            int n27 = n7;
            int n28 = n22;
            while (n28 >= n6) {
                final int n29 = n12 & n27;
                n12 >>= n6;
                n28 -= n6;
                if (n29 != n5) {
                    if (n29 > n14) {
                        this.status = status;
                    }
                    else if (n29 != n5 + 1) {
                        if (n26 == -1) {
                            this.pixelStack[k] = this.suffix[n29];
                            n26 = n29;
                            n23 = n29;
                            ++k;
                            n4 = 1;
                            status = 3;
                            continue;
                        }
                        int n31;
                        int n32;
                        if (n29 >= n14) {
                            final byte[] pixelStack = this.pixelStack;
                            final int n30 = k + 1;
                            pixelStack[k] = (byte)n23;
                            n31 = n26;
                            n32 = n30;
                        }
                        else {
                            n31 = n29;
                            n32 = k;
                        }
                        while (n31 >= n5) {
                            this.pixelStack[n32] = this.suffix[n31];
                            n31 = this.prefix[n31];
                            ++n32;
                        }
                        final int n33 = this.suffix[n31] & 0xFF;
                        this.pixelStack[n32] = (byte)n33;
                        int n34 = n14;
                        int n35 = n6;
                        int n36 = n27;
                        if (n14 < 4096) {
                            this.prefix[n14] = (short)n26;
                            this.suffix[n14] = (byte)n33;
                            final int n37 = n14 + 1;
                            if ((n37 & n27) == 0x0) {
                                n34 = n37;
                                n35 = n6;
                                n36 = n27;
                                if (n37 < 4096) {
                                    n35 = n6 + 1;
                                    n36 = n27 + n37;
                                    n34 = n37;
                                }
                            }
                            else {
                                n36 = n27;
                                n35 = n6;
                                n34 = n37;
                            }
                        }
                        n26 = n29;
                        for (k = n32 + 1; k > 0; --k, this.mainPixels[n24] = this.pixelStack[k], ++n25, ++n24) {}
                        n23 = n33;
                        n4 = 1;
                        final int n38 = 3;
                        n14 = n34;
                        n6 = n35;
                        n27 = n36;
                        status = n38;
                        continue;
                    }
                    final int n39 = n28;
                    final int n40 = n25;
                    final int n41 = n24;
                    final int n42 = n23;
                    l = n40;
                    n7 = n27;
                    n13 = n26;
                    n8 = n17;
                    j = n41;
                    n9 = n42;
                    n10 = n18;
                    n11 = n39;
                    continue Label_0220;
                }
                n6 = read + 1;
                n27 = (n4 << n6) - 1;
                n14 = n5 + 2;
                n26 = -1;
            }
            final int n43 = n28;
            final int n44 = n25;
            final int n45 = n24;
            final int n46 = n23;
            l = n44;
            n7 = n27;
            n13 = n26;
            n8 = n17;
            j = n45;
            n9 = n46;
            n10 = n18;
            n11 = n43;
        }
        while (j < n3) {
            this.mainPixels[j] = 0;
            ++j;
        }
    }
    
    private GifHeaderParser getHeaderParser() {
        if (this.parser == null) {
            this.parser = new GifHeaderParser();
        }
        return this.parser;
    }
    
    private Bitmap getNextBitmap() {
        Bitmap alpha;
        if ((alpha = this.bitmapProvider.obtain(this.header.width, this.header.height, GifDecoder.BITMAP_CONFIG)) == null) {
            alpha = Bitmap.createBitmap(this.header.width, this.header.height, GifDecoder.BITMAP_CONFIG);
        }
        setAlpha(alpha);
        return alpha;
    }
    
    private int read() {
        try {
            return this.rawData.get() & 0xFF;
        }
        catch (Exception ex) {
            this.status = 1;
            return 0;
        }
    }
    
    private int readBlock() {
        final int read = this.read();
        int i = 0;
        final int n = 0;
        if (read > 0) {
            i = n;
            while (i < read) {
                final int n2 = read - i;
                try {
                    this.rawData.get(this.block, i, n2);
                    i += n2;
                    continue;
                }
                catch (Exception ex) {
                    Log.w(GifDecoder.TAG, "Error Reading Block", (Throwable)ex);
                    this.status = 1;
                    return i;
                }
                break;
            }
        }
        return i;
    }
    
    @TargetApi(12)
    private static void setAlpha(final Bitmap bitmap) {
        if (Build$VERSION.SDK_INT >= 12) {
            bitmap.setHasAlpha(true);
        }
    }
    
    private Bitmap setPixels(final GifFrame gifFrame, final GifFrame gifFrame2) {
        final int width = this.header.width;
        final int height = this.header.height;
        final int[] mainScratch = this.mainScratch;
        if (gifFrame2 != null && gifFrame2.dispose > 0) {
            if (gifFrame2.dispose == 2) {
                int bgColor = 0;
                if (!gifFrame.transparency) {
                    bgColor = this.header.bgColor;
                }
                Arrays.fill(mainScratch, bgColor);
            }
            else if (gifFrame2.dispose == 3 && this.previousImage != null) {
                this.previousImage.getPixels(mainScratch, 0, width, 0, 0, width, height);
            }
        }
        this.decodeBitmapData(gifFrame);
        int i = 0;
        int n = 1;
        int n2 = 8;
        int n3 = 0;
        while (i < gifFrame.ih) {
            int n4 = i;
            int n5 = n3;
            int n6 = n;
            int n7 = n2;
            if (gifFrame.interlace) {
                int n8 = n3;
                n6 = n;
                n7 = n2;
                if (n3 >= gifFrame.ih) {
                    n6 = n + 1;
                    switch (n6) {
                        default: {
                            n8 = n3;
                            n7 = n2;
                            break;
                        }
                        case 4: {
                            n8 = 1;
                            n7 = 2;
                            break;
                        }
                        case 3: {
                            n8 = 2;
                            n7 = 4;
                            break;
                        }
                        case 2: {
                            n8 = 4;
                            n7 = n2;
                            break;
                        }
                    }
                }
                final int n9 = n8;
                n5 = n8 + n7;
                n4 = n9;
            }
            final int n10 = n4 + gifFrame.iy;
            if (n10 < this.header.height) {
                final int n11 = this.header.width * n10;
                int j = gifFrame.ix + n11;
                int n12;
                if (this.header.width + n11 < (n12 = gifFrame.iw + j)) {
                    n12 = n11 + this.header.width;
                }
                for (int n13 = gifFrame.iw * i; j < n12; ++j, ++n13) {
                    final int n14 = this.act[this.mainPixels[n13] & 0xFF];
                    if (n14 != 0) {
                        mainScratch[j] = n14;
                    }
                }
            }
            ++i;
            n3 = n5;
            n = n6;
            n2 = n7;
        }
        if (this.savePrevious && (gifFrame.dispose == 0 || gifFrame.dispose == 1)) {
            if (this.previousImage == null) {
                this.previousImage = this.getNextBitmap();
            }
            this.previousImage.setPixels(mainScratch, 0, width, 0, 0, width, height);
        }
        final Bitmap nextBitmap = this.getNextBitmap();
        nextBitmap.setPixels(mainScratch, 0, width, 0, 0, width, height);
        return nextBitmap;
    }
    
    public void advance() {
        this.framePointer = (this.framePointer + 1) % this.header.frameCount;
    }
    
    public void clear() {
        this.header = null;
        this.data = null;
        this.mainPixels = null;
        this.mainScratch = null;
        if (this.previousImage != null) {
            this.bitmapProvider.release(this.previousImage);
        }
        this.previousImage = null;
    }
    
    public int getCurrentFrameIndex() {
        return this.framePointer;
    }
    
    public byte[] getData() {
        return this.data;
    }
    
    public int getDelay(final int n) {
        int delay = -1;
        if (n >= 0) {
            delay = delay;
            if (n < this.header.frameCount) {
                delay = this.header.frames.get(n).delay;
            }
        }
        return delay;
    }
    
    public int getFrameCount() {
        return this.header.frameCount;
    }
    
    public int getHeight() {
        return this.header.height;
    }
    
    public int getLoopCount() {
        return this.header.loopCount;
    }
    
    public int getNextDelay() {
        if (this.header.frameCount > 0 && this.framePointer >= 0) {
            return this.getDelay(this.framePointer);
        }
        return -1;
    }
    
    public Bitmap getNextFrame() {
        synchronized (this) {
            if (this.header.frameCount <= 0 || this.framePointer < 0) {
                if (Log.isLoggable(GifDecoder.TAG, 3)) {
                    final String tag = GifDecoder.TAG;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("unable to decode frame, frameCount=");
                    sb.append(this.header.frameCount);
                    sb.append(" framePointer=");
                    sb.append(this.framePointer);
                    Log.d(tag, sb.toString());
                }
                this.status = 1;
            }
            if (this.status == 1 || this.status == 2) {
                if (Log.isLoggable(GifDecoder.TAG, 3)) {
                    final String tag2 = GifDecoder.TAG;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Unable to decode frame, status=");
                    sb2.append(this.status);
                    Log.d(tag2, sb2.toString());
                }
                return null;
            }
            this.status = 0;
            final GifFrame gifFrame = this.header.frames.get(this.framePointer);
            GifFrame gifFrame2 = null;
            final int n = this.framePointer - 1;
            if (n >= 0) {
                gifFrame2 = this.header.frames.get(n);
            }
            if (gifFrame.lct == null) {
                this.act = this.header.gct;
            }
            else {
                this.act = gifFrame.lct;
                if (this.header.bgIndex == gifFrame.transIndex) {
                    this.header.bgColor = 0;
                }
            }
            int n2 = 0;
            if (gifFrame.transparency) {
                n2 = this.act[gifFrame.transIndex];
                this.act[gifFrame.transIndex] = 0;
            }
            if (this.act == null) {
                if (Log.isLoggable(GifDecoder.TAG, 3)) {
                    Log.d(GifDecoder.TAG, "No Valid Color Table");
                }
                this.status = 1;
                return null;
            }
            final Bitmap setPixels = this.setPixels(gifFrame, gifFrame2);
            if (gifFrame.transparency) {
                this.act[gifFrame.transIndex] = n2;
            }
            return setPixels;
        }
    }
    
    public int getStatus() {
        return this.status;
    }
    
    public int getWidth() {
        return this.header.width;
    }
    
    public int read(final InputStream inputStream, int read) {
        if (inputStream != null) {
            if (read > 0) {
                read += 4096;
            }
            else {
                read = 16384;
            }
            try {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(read);
                final byte[] array = new byte[16384];
                while (true) {
                    read = inputStream.read(array, 0, array.length);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(array, 0, read);
                }
                byteArrayOutputStream.flush();
                this.read(byteArrayOutputStream.toByteArray());
            }
            catch (IOException ex) {
                Log.w(GifDecoder.TAG, "Error reading data from stream", (Throwable)ex);
            }
        }
        else {
            this.status = 2;
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            }
            catch (IOException ex2) {
                Log.w(GifDecoder.TAG, "Error closing stream", (Throwable)ex2);
            }
        }
        return this.status;
    }
    
    public int read(final byte[] array) {
        this.data = array;
        this.header = this.getHeaderParser().setData(array).parseHeader();
        if (array != null) {
            (this.rawData = ByteBuffer.wrap(array)).rewind();
            this.rawData.order(ByteOrder.LITTLE_ENDIAN);
            this.mainPixels = new byte[this.header.width * this.header.height];
            this.mainScratch = new int[this.header.width * this.header.height];
            this.savePrevious = false;
            final Iterator<GifFrame> iterator = this.header.frames.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().dispose == 3) {
                    this.savePrevious = true;
                    break;
                }
            }
        }
        return this.status;
    }
    
    public void resetFrameIndex() {
        this.framePointer = -1;
    }
    
    public void setData(final GifHeader header, final byte[] data) {
        this.header = header;
        this.data = data;
        this.status = 0;
        this.framePointer = -1;
        (this.rawData = ByteBuffer.wrap(data)).rewind();
        this.rawData.order(ByteOrder.LITTLE_ENDIAN);
        this.savePrevious = false;
        final Iterator<GifFrame> iterator = header.frames.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().dispose == 3) {
                this.savePrevious = true;
                break;
            }
        }
        this.mainPixels = new byte[header.width * header.height];
        this.mainScratch = new int[header.width * header.height];
    }
    
    public interface BitmapProvider
    {
        Bitmap obtain(final int p0, final int p1, final Bitmap$Config p2);
        
        void release(final Bitmap p0);
    }
}
