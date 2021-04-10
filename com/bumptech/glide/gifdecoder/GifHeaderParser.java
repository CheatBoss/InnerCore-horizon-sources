package com.bumptech.glide.gifdecoder;

import android.util.*;
import java.util.*;
import java.nio.*;

public class GifHeaderParser
{
    static final int DEFAULT_FRAME_DELAY = 10;
    private static final int MAX_BLOCK_SIZE = 256;
    static final int MIN_FRAME_DELAY = 3;
    public static final String TAG = "GifHeaderParser";
    private final byte[] block;
    private int blockSize;
    private GifHeader header;
    private ByteBuffer rawData;
    
    public GifHeaderParser() {
        this.block = new byte[256];
        this.blockSize = 0;
    }
    
    private boolean err() {
        return this.header.status != 0;
    }
    
    private int read() {
        try {
            return this.rawData.get() & 0xFF;
        }
        catch (Exception ex) {
            this.header.status = 1;
            return 0;
        }
    }
    
    private void readBitmap() {
        this.header.currentFrame.ix = this.readShort();
        this.header.currentFrame.iy = this.readShort();
        this.header.currentFrame.iw = this.readShort();
        this.header.currentFrame.ih = this.readShort();
        final int read = this.read();
        boolean interlace = false;
        final boolean b = (read & 0x80) != 0x0;
        final int n = (int)Math.pow(2.0, (read & 0x7) + 1);
        final GifFrame currentFrame = this.header.currentFrame;
        if ((read & 0x40) != 0x0) {
            interlace = true;
        }
        currentFrame.interlace = interlace;
        if (b) {
            this.header.currentFrame.lct = this.readColorTable(n);
        }
        else {
            this.header.currentFrame.lct = null;
        }
        this.header.currentFrame.bufferFrameStart = this.rawData.position();
        this.skipImageData();
        if (this.err()) {
            return;
        }
        final GifHeader header = this.header;
        ++header.frameCount;
        this.header.frames.add(this.header.currentFrame);
    }
    
    private int readBlock() {
        this.blockSize = this.read();
        int n = 0;
        final int n2 = 0;
        if (this.blockSize > 0) {
            int n3 = 0;
            n = n2;
            while (true) {
                int n4 = n3;
                try {
                    if (n < this.blockSize) {
                        n4 = n3;
                        n3 = (n4 = this.blockSize - n);
                        this.rawData.get(this.block, n, n3);
                        n += n3;
                        continue;
                    }
                    return n;
                }
                catch (Exception ex) {
                    if (Log.isLoggable("GifHeaderParser", 3)) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Error Reading Block n: ");
                        sb.append(n);
                        sb.append(" count: ");
                        sb.append(n4);
                        sb.append(" blockSize: ");
                        sb.append(this.blockSize);
                        Log.d("GifHeaderParser", sb.toString(), (Throwable)ex);
                    }
                    this.header.status = 1;
                }
                break;
            }
        }
        return n;
    }
    
    private int[] readColorTable(final int n) {
        final byte[] array = new byte[n * 3];
        try {
            this.rawData.get(array);
            final int[] array2 = new int[256];
            int i = 0;
            int n2 = 0;
            while (i < n) {
                final int n3 = n2 + 1;
                final byte b = array[n2];
                final int n4 = n3 + 1;
                array2[i] = (0xFF000000 | (b & 0xFF) << 16 | (array[n3] & 0xFF) << 8 | (array[n4] & 0xFF));
                n2 = n4 + 1;
                ++i;
            }
            return array2;
        }
        catch (BufferUnderflowException ex) {
            if (Log.isLoggable("GifHeaderParser", 3)) {
                Log.d("GifHeaderParser", "Format Error Reading Color Table", (Throwable)ex);
            }
            this.header.status = 1;
            return null;
        }
    }
    
    private void readContents() {
        int n = 0;
        while (n == 0 && !this.err()) {
            final int read = this.read();
            if (read != 33) {
                if (read != 44) {
                    if (read != 59) {
                        this.header.status = 1;
                    }
                    else {
                        n = 1;
                    }
                }
                else {
                    if (this.header.currentFrame == null) {
                        this.header.currentFrame = new GifFrame();
                    }
                    this.readBitmap();
                }
            }
            else {
                final int read2 = this.read();
                if (read2 != 1) {
                    if (read2 != 249) {
                        switch (read2) {
                            default: {
                                this.skip();
                                continue;
                            }
                            case 255: {
                                this.readBlock();
                                String string = "";
                                for (int i = 0; i < 11; ++i) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append(string);
                                    sb.append((char)this.block[i]);
                                    string = sb.toString();
                                }
                                if (string.equals("NETSCAPE2.0")) {
                                    this.readNetscapeExt();
                                }
                                else {
                                    this.skip();
                                }
                                continue;
                            }
                            case 254: {
                                this.skip();
                                continue;
                            }
                        }
                    }
                    else {
                        this.header.currentFrame = new GifFrame();
                        this.readGraphicControlExt();
                    }
                }
                else {
                    this.skip();
                }
            }
        }
    }
    
    private void readGraphicControlExt() {
        this.read();
        final int read = this.read();
        this.header.currentFrame.dispose = (read & 0x1C) >> 2;
        final int dispose = this.header.currentFrame.dispose;
        boolean transparency = true;
        if (dispose == 0) {
            this.header.currentFrame.dispose = 1;
        }
        final GifFrame currentFrame = this.header.currentFrame;
        if ((read & 0x1) == 0x0) {
            transparency = false;
        }
        currentFrame.transparency = transparency;
        int short1;
        if ((short1 = this.readShort()) < 3) {
            short1 = 10;
        }
        this.header.currentFrame.delay = short1 * 10;
        this.header.currentFrame.transIndex = this.read();
        this.read();
    }
    
    private void readHeader() {
        String string = "";
        for (int i = 0; i < 6; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append((char)this.read());
            string = sb.toString();
        }
        if (!string.startsWith("GIF")) {
            this.header.status = 1;
            return;
        }
        this.readLSD();
        if (this.header.gctFlag && !this.err()) {
            this.header.gct = this.readColorTable(this.header.gctSize);
            this.header.bgColor = this.header.gct[this.header.bgIndex];
        }
    }
    
    private void readLSD() {
        this.header.width = this.readShort();
        this.header.height = this.readShort();
        final int read = this.read();
        this.header.gctFlag = ((read & 0x80) != 0x0);
        this.header.gctSize = 2 << (read & 0x7);
        this.header.bgIndex = this.read();
        this.header.pixelAspect = this.read();
    }
    
    private void readNetscapeExt() {
        do {
            this.readBlock();
            if (this.block[0] == 1) {
                this.header.loopCount = ((this.block[2] & 0xFF) << 8 | (this.block[1] & 0xFF));
            }
        } while (this.blockSize > 0 && !this.err());
    }
    
    private int readShort() {
        return this.rawData.getShort();
    }
    
    private void reset() {
        this.rawData = null;
        Arrays.fill(this.block, (byte)0);
        this.header = new GifHeader();
        this.blockSize = 0;
    }
    
    private void skip() {
        int i;
        do {
            i = this.read();
            this.rawData.position(this.rawData.position() + i);
        } while (i > 0);
    }
    
    private void skipImageData() {
        this.read();
        this.skip();
    }
    
    public void clear() {
        this.rawData = null;
        this.header = null;
    }
    
    public GifHeader parseHeader() {
        if (this.rawData == null) {
            throw new IllegalStateException("You must call setData() before parseHeader()");
        }
        if (this.err()) {
            return this.header;
        }
        this.readHeader();
        if (!this.err()) {
            this.readContents();
            if (this.header.frameCount < 0) {
                this.header.status = 1;
            }
        }
        return this.header;
    }
    
    public GifHeaderParser setData(final byte[] array) {
        this.reset();
        if (array != null) {
            (this.rawData = ByteBuffer.wrap(array)).rewind();
            this.rawData.order(ByteOrder.LITTLE_ENDIAN);
            return this;
        }
        this.rawData = null;
        this.header.status = 2;
        return this;
    }
}
