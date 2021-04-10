package com.bumptech.glide.gifencoder;

import android.graphics.*;
import android.util.*;
import java.io.*;

public class AnimatedGifEncoder
{
    private static final double MIN_TRANSPARENT_PERCENTAGE = 4.0;
    private static final String TAG = "AnimatedGifEncoder";
    private boolean closeStream;
    private int colorDepth;
    private byte[] colorTab;
    private int delay;
    private int dispose;
    private boolean firstFrame;
    private boolean hasTransparentPixels;
    private int height;
    private Bitmap image;
    private byte[] indexedPixels;
    private OutputStream out;
    private int palSize;
    private byte[] pixels;
    private int repeat;
    private int sample;
    private boolean sizeSet;
    private boolean started;
    private int transIndex;
    private Integer transparent;
    private boolean[] usedEntry;
    private int width;
    
    public AnimatedGifEncoder() {
        this.transparent = null;
        this.repeat = -1;
        this.delay = 0;
        this.started = false;
        this.usedEntry = new boolean[256];
        this.palSize = 7;
        this.dispose = -1;
        this.closeStream = false;
        this.firstFrame = true;
        this.sizeSet = false;
        this.sample = 10;
    }
    
    private void analyzePixels() {
        final int length = this.pixels.length;
        final int n = length / 3;
        this.indexedPixels = new byte[n];
        final NeuQuant neuQuant = new NeuQuant(this.pixels, length, this.sample);
        this.colorTab = neuQuant.process();
        for (int i = 0; i < this.colorTab.length; i += 3) {
            final byte b = this.colorTab[i];
            this.colorTab[i] = this.colorTab[i + 2];
            this.colorTab[i + 2] = b;
            this.usedEntry[i / 3] = false;
        }
        int n2 = 0;
        int n4;
        for (int j = 0; j < n; ++j, n2 = n4 + 1) {
            final byte[] pixels = this.pixels;
            final int n3 = n2 + 1;
            final byte b2 = pixels[n2];
            final byte[] pixels2 = this.pixels;
            n4 = n3 + 1;
            final int map = neuQuant.map(b2 & 0xFF, pixels2[n3] & 0xFF, this.pixels[n4] & 0xFF);
            this.usedEntry[map] = true;
            this.indexedPixels[j] = (byte)map;
        }
        this.pixels = null;
        this.colorDepth = 8;
        this.palSize = 7;
        if (this.transparent != null) {
            this.transIndex = this.findClosest(this.transparent);
            return;
        }
        if (this.hasTransparentPixels) {
            this.transIndex = this.findClosest(0);
        }
    }
    
    private int findClosest(int n) {
        if (this.colorTab == null) {
            return -1;
        }
        final int red = Color.red(n);
        final int green = Color.green(n);
        final int blue = Color.blue(n);
        int n2 = 0;
        n = 16777216;
        int n12;
        for (int length = this.colorTab.length, i = 0; i < length; i = n12) {
            final byte[] colorTab = this.colorTab;
            final int n3 = i + 1;
            final int n4 = red - (colorTab[i] & 0xFF);
            final byte[] colorTab2 = this.colorTab;
            final int n5 = n3 + 1;
            final int n6 = green - (colorTab2[n3] & 0xFF);
            final int n7 = blue - (this.colorTab[n5] & 0xFF);
            final int n8 = n4 * n4 + n6 * n6 + n7 * n7;
            final int n9 = n5 / 3;
            int n10 = n2;
            int n11 = n;
            if (this.usedEntry[n9]) {
                n10 = n2;
                if (n8 < (n11 = n)) {
                    n11 = n8;
                    n10 = n9;
                }
            }
            n12 = n5 + 1;
            n2 = n10;
            n = n11;
        }
        return n2;
    }
    
    private void getImagePixels() {
        final int width = this.image.getWidth();
        final int height = this.image.getHeight();
        if (width != this.width || height != this.height) {
            final Bitmap bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap$Config.ARGB_8888);
            new Canvas(bitmap).drawBitmap(bitmap, 0.0f, 0.0f, (Paint)null);
            this.image = bitmap;
        }
        final int[] array = new int[width * height];
        final Bitmap image = this.image;
        boolean hasTransparentPixels = false;
        image.getPixels(array, 0, width, 0, 0, width, height);
        this.pixels = new byte[array.length * 3];
        this.hasTransparentPixels = false;
        int n = 0;
        final int length = array.length;
        int n2 = 0;
        int n4;
        int n6;
        for (int i = 0; i < length; ++i, n2 = n6 + 1, n = n4) {
            final int n3 = array[i];
            n4 = n;
            if (n3 == 0) {
                n4 = n + 1;
            }
            final byte[] pixels = this.pixels;
            final int n5 = n2 + 1;
            pixels[n2] = (byte)(n3 & 0xFF);
            final byte[] pixels2 = this.pixels;
            n6 = n5 + 1;
            pixels2[n5] = (byte)(n3 >> 8 & 0xFF);
            this.pixels[n6] = (byte)(n3 >> 16 & 0xFF);
        }
        final double n7 = n * 100 / (double)array.length;
        if (n7 > 4.0) {
            hasTransparentPixels = true;
        }
        this.hasTransparentPixels = hasTransparentPixels;
        if (Log.isLoggable("AnimatedGifEncoder", 3)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("got pixels for frame with ");
            sb.append(n7);
            sb.append("% transparent pixels");
            Log.d("AnimatedGifEncoder", sb.toString());
        }
    }
    
    private void writeGraphicCtrlExt() throws IOException {
        this.out.write(33);
        this.out.write(249);
        this.out.write(4);
        boolean b;
        int n;
        if (this.transparent == null && !this.hasTransparentPixels) {
            b = false;
            n = 0;
        }
        else {
            b = true;
            n = 2;
        }
        if (this.dispose >= 0) {
            n = (this.dispose & 0x7);
        }
        this.out.write(n << 2 | 0x0 | 0x0 | (b ? 1 : 0));
        this.writeShort(this.delay);
        this.out.write(this.transIndex);
        this.out.write(0);
    }
    
    private void writeImageDesc() throws IOException {
        this.out.write(44);
        this.writeShort(0);
        this.writeShort(0);
        this.writeShort(this.width);
        this.writeShort(this.height);
        if (this.firstFrame) {
            this.out.write(0);
            return;
        }
        this.out.write(this.palSize | 0x80);
    }
    
    private void writeLSD() throws IOException {
        this.writeShort(this.width);
        this.writeShort(this.height);
        this.out.write(this.palSize | 0xF0);
        this.out.write(0);
        this.out.write(0);
    }
    
    private void writeNetscapeExt() throws IOException {
        this.out.write(33);
        this.out.write(255);
        this.out.write(11);
        this.writeString("NETSCAPE2.0");
        this.out.write(3);
        this.out.write(1);
        this.writeShort(this.repeat);
        this.out.write(0);
    }
    
    private void writePalette() throws IOException {
        this.out.write(this.colorTab, 0, this.colorTab.length);
        for (int length = this.colorTab.length, i = 0; i < 768 - length; ++i) {
            this.out.write(0);
        }
    }
    
    private void writePixels() throws IOException {
        new LZWEncoder(this.width, this.height, this.indexedPixels, this.colorDepth).encode(this.out);
    }
    
    private void writeShort(final int n) throws IOException {
        this.out.write(n & 0xFF);
        this.out.write(n >> 8 & 0xFF);
    }
    
    private void writeString(final String s) throws IOException {
        for (int i = 0; i < s.length(); ++i) {
            this.out.write((byte)s.charAt(i));
        }
    }
    
    public boolean addFrame(final Bitmap image) {
        if (image != null) {
            if (!this.started) {
                return false;
            }
            try {
                if (!this.sizeSet) {
                    this.setSize(image.getWidth(), image.getHeight());
                }
                this.image = image;
                this.getImagePixels();
                this.analyzePixels();
                if (this.firstFrame) {
                    this.writeLSD();
                    this.writePalette();
                    if (this.repeat >= 0) {
                        this.writeNetscapeExt();
                    }
                }
                this.writeGraphicCtrlExt();
                this.writeImageDesc();
                if (!this.firstFrame) {
                    this.writePalette();
                }
                this.writePixels();
                this.firstFrame = false;
                return true;
            }
            catch (IOException ex) {
                return false;
            }
        }
        return false;
    }
    
    public boolean finish() {
        if (!this.started) {
            return false;
        }
        boolean b = true;
        this.started = false;
        try {
            this.out.write(59);
            this.out.flush();
            if (this.closeStream) {
                this.out.close();
            }
        }
        catch (IOException ex) {
            b = false;
        }
        this.transIndex = 0;
        this.out = null;
        this.image = null;
        this.pixels = null;
        this.indexedPixels = null;
        this.colorTab = null;
        this.closeStream = false;
        this.firstFrame = true;
        return b;
    }
    
    public void setDelay(final int n) {
        this.delay = Math.round(n / 10.0f);
    }
    
    public void setDispose(final int dispose) {
        if (dispose >= 0) {
            this.dispose = dispose;
        }
    }
    
    public void setFrameRate(final float n) {
        if (n != 0.0f) {
            this.delay = Math.round(100.0f / n);
        }
    }
    
    public void setQuality(final int n) {
        int sample = n;
        if (n < 1) {
            sample = 1;
        }
        this.sample = sample;
    }
    
    public void setRepeat(final int repeat) {
        if (repeat >= 0) {
            this.repeat = repeat;
        }
    }
    
    public void setSize(final int width, final int height) {
        if (this.started && !this.firstFrame) {
            return;
        }
        this.width = width;
        this.height = height;
        if (this.width < 1) {
            this.width = 320;
        }
        if (this.height < 1) {
            this.height = 240;
        }
        this.sizeSet = true;
    }
    
    public void setTransparent(final int n) {
        this.transparent = n;
    }
    
    public boolean start(final OutputStream out) {
        if (out == null) {
            return false;
        }
        boolean started = true;
        this.closeStream = false;
        this.out = out;
        try {
            this.writeString("GIF89a");
        }
        catch (IOException ex) {
            started = false;
        }
        return this.started = started;
    }
    
    public boolean start(final String s) {
        boolean start;
        try {
            this.out = new BufferedOutputStream(new FileOutputStream(s));
            start = this.start(this.out);
            this.closeStream = true;
        }
        catch (IOException ex) {
            start = false;
        }
        return this.started = start;
    }
}
