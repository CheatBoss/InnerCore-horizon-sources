package com.bumptech.glide.gifencoder;

import java.io.*;

class LZWEncoder
{
    static final int BITS = 12;
    private static final int EOF = -1;
    static final int HSIZE = 5003;
    int ClearCode;
    int EOFCode;
    int a_count;
    byte[] accum;
    boolean clear_flg;
    int[] codetab;
    private int curPixel;
    int cur_accum;
    int cur_bits;
    int free_ent;
    int g_init_bits;
    int hsize;
    int[] htab;
    private int imgH;
    private int imgW;
    private int initCodeSize;
    int[] masks;
    int maxbits;
    int maxcode;
    int maxmaxcode;
    int n_bits;
    private byte[] pixAry;
    private int remaining;
    
    LZWEncoder(final int imgW, final int imgH, final byte[] pixAry, final int n) {
        this.maxbits = 12;
        this.maxmaxcode = 4096;
        this.htab = new int[5003];
        this.codetab = new int[5003];
        this.hsize = 5003;
        this.free_ent = 0;
        this.clear_flg = false;
        this.cur_accum = 0;
        this.cur_bits = 0;
        this.masks = new int[] { 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535 };
        this.accum = new byte[256];
        this.imgW = imgW;
        this.imgH = imgH;
        this.pixAry = pixAry;
        this.initCodeSize = Math.max(2, n);
    }
    
    private int nextPixel() {
        if (this.remaining == 0) {
            return -1;
        }
        --this.remaining;
        return this.pixAry[this.curPixel++] & 0xFF;
    }
    
    final int MAXCODE(final int n) {
        return (1 << n) - 1;
    }
    
    void char_out(final byte b, final OutputStream outputStream) throws IOException {
        this.accum[this.a_count++] = b;
        if (this.a_count >= 254) {
            this.flush_char(outputStream);
        }
    }
    
    void cl_block(final OutputStream outputStream) throws IOException {
        this.cl_hash(this.hsize);
        this.free_ent = this.ClearCode + 2;
        this.clear_flg = true;
        this.output(this.ClearCode, outputStream);
    }
    
    void cl_hash(final int n) {
        for (int i = 0; i < n; ++i) {
            this.htab[i] = -1;
        }
    }
    
    void compress(int i, final OutputStream outputStream) throws IOException {
        this.g_init_bits = i;
        this.clear_flg = false;
        this.n_bits = this.g_init_bits;
        this.maxcode = this.MAXCODE(this.n_bits);
        this.ClearCode = 1 << i - 1;
        this.EOFCode = this.ClearCode + 1;
        this.free_ent = this.ClearCode + 2;
        this.a_count = 0;
        final int nextPixel = this.nextPixel();
        int n = 0;
        for (i = this.hsize; i < 65536; i *= 2) {
            ++n;
        }
        final int hsize = this.hsize;
        this.cl_hash(hsize);
        this.output(this.ClearCode, outputStream);
        i = nextPixel;
    Label_0119:
        while (true) {
            final int nextPixel2 = this.nextPixel();
            if (nextPixel2 == -1) {
                break;
            }
            final int n2 = (nextPixel2 << this.maxbits) + i;
            final int n3 = nextPixel2 << 8 - n ^ i;
            if (this.htab[n3] == n2) {
                i = this.codetab[n3];
            }
            else {
                int n4 = n3;
                if (this.htab[n3] >= 0) {
                    int n5 = hsize - n3;
                    int n6;
                    if ((n6 = n3) == 0) {
                        n5 = 1;
                        n6 = n3;
                    }
                    int n7;
                    do {
                        final int n8 = n6 - n5;
                        final int n9 = n7 = n8;
                        if (n8 < 0) {
                            n7 = n9 + hsize;
                        }
                        if (this.htab[n7] == n2) {
                            i = this.codetab[n7];
                            continue Label_0119;
                        }
                        n6 = n7;
                    } while (this.htab[n7] >= 0);
                    n4 = n7;
                }
                this.output(i, outputStream);
                i = nextPixel2;
                if (this.free_ent < this.maxmaxcode) {
                    this.codetab[n4] = this.free_ent++;
                    this.htab[n4] = n2;
                }
                else {
                    this.cl_block(outputStream);
                }
            }
        }
        this.output(i, outputStream);
        this.output(this.EOFCode, outputStream);
    }
    
    void encode(final OutputStream outputStream) throws IOException {
        outputStream.write(this.initCodeSize);
        this.remaining = this.imgW * this.imgH;
        this.curPixel = 0;
        this.compress(this.initCodeSize + 1, outputStream);
        outputStream.write(0);
    }
    
    void flush_char(final OutputStream outputStream) throws IOException {
        if (this.a_count > 0) {
            outputStream.write(this.a_count);
            outputStream.write(this.accum, 0, this.a_count);
            this.a_count = 0;
        }
    }
    
    void output(final int cur_accum, final OutputStream outputStream) throws IOException {
        this.cur_accum &= this.masks[this.cur_bits];
        if (this.cur_bits > 0) {
            this.cur_accum |= cur_accum << this.cur_bits;
        }
        else {
            this.cur_accum = cur_accum;
        }
        this.cur_bits += this.n_bits;
        while (this.cur_bits >= 8) {
            this.char_out((byte)(this.cur_accum & 0xFF), outputStream);
            this.cur_accum >>= 8;
            this.cur_bits -= 8;
        }
        if (this.free_ent > this.maxcode || this.clear_flg) {
            if (this.clear_flg) {
                final int g_init_bits = this.g_init_bits;
                this.n_bits = g_init_bits;
                this.maxcode = this.MAXCODE(g_init_bits);
                this.clear_flg = false;
            }
            else {
                ++this.n_bits;
                if (this.n_bits == this.maxbits) {
                    this.maxcode = this.maxmaxcode;
                }
                else {
                    this.maxcode = this.MAXCODE(this.n_bits);
                }
            }
        }
        if (cur_accum == this.EOFCode) {
            while (this.cur_bits > 0) {
                this.char_out((byte)(this.cur_accum & 0xFF), outputStream);
                this.cur_accum >>= 8;
                this.cur_bits -= 8;
            }
            this.flush_char(outputStream);
        }
    }
}
