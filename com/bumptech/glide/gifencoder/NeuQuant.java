package com.bumptech.glide.gifencoder;

class NeuQuant
{
    protected static final int alphabiasshift = 10;
    protected static final int alpharadbias = 262144;
    protected static final int alpharadbshift = 18;
    protected static final int beta = 64;
    protected static final int betagamma = 65536;
    protected static final int betashift = 10;
    protected static final int gamma = 1024;
    protected static final int gammashift = 10;
    protected static final int initalpha = 1024;
    protected static final int initrad = 32;
    protected static final int initradius = 2048;
    protected static final int intbias = 65536;
    protected static final int intbiasshift = 16;
    protected static final int maxnetpos = 255;
    protected static final int minpicturebytes = 1509;
    protected static final int ncycles = 100;
    protected static final int netbiasshift = 4;
    protected static final int netsize = 256;
    protected static final int prime1 = 499;
    protected static final int prime2 = 491;
    protected static final int prime3 = 487;
    protected static final int prime4 = 503;
    protected static final int radbias = 256;
    protected static final int radbiasshift = 8;
    protected static final int radiusbias = 64;
    protected static final int radiusbiasshift = 6;
    protected static final int radiusdec = 30;
    protected int alphadec;
    protected int[] bias;
    protected int[] freq;
    protected int lengthcount;
    protected int[] netindex;
    protected int[][] network;
    protected int[] radpower;
    protected int samplefac;
    protected byte[] thepicture;
    
    public NeuQuant(final byte[] thepicture, int i, int samplefac) {
        this.netindex = new int[256];
        this.bias = new int[256];
        this.freq = new int[256];
        this.radpower = new int[32];
        this.thepicture = thepicture;
        this.lengthcount = i;
        this.samplefac = samplefac;
        this.network = new int[256][];
        int[] array;
        for (i = 0; i < 256; ++i) {
            this.network[i] = new int[4];
            array = this.network[i];
            samplefac = (i << 12) / 256;
            array[2] = samplefac;
            array[0] = (array[1] = samplefac);
            this.freq[i] = 256;
            this.bias[i] = 0;
        }
    }
    
    protected void alterneigh(int n, int n2, final int n3, final int n4, final int n5) {
        int n6;
        if ((n6 = n2 - n) < -1) {
            n6 = -1;
        }
        n += n2;
        int n7;
        if ((n7 = n) > 256) {
            n7 = 256;
        }
        final int n8 = n2 - 1;
        int n9 = n2 + 1;
        n = 1;
        int[] radpower;
        int n10;
        int n11;
        int[] array;
        int[] array2;
        for (n2 = n8; n9 < n7 || n2 > n6; n9 = n11) {
            radpower = this.radpower;
            n10 = n + 1;
            n = radpower[n];
            if ((n11 = n9) < n7) {
                array = this.network[n9];
                try {
                    array[0] -= (array[0] - n3) * n / 262144;
                    array[1] -= (array[1] - n4) * n / 262144;
                    array[2] -= (array[2] - n5) * n / 262144;
                }
                catch (Exception ex) {}
                n11 = n9 + 1;
            }
            if (n2 > n6) {
                array2 = this.network[n2];
                try {
                    array2[0] -= (array2[0] - n3) * n / 262144;
                    array2[1] -= (array2[1] - n4) * n / 262144;
                    array2[2] -= (array2[2] - n5) * n / 262144;
                }
                catch (Exception ex2) {}
                n = n10;
                --n2;
            }
            else {
                n = n10;
            }
        }
    }
    
    protected void altersingle(final int n, final int n2, final int n3, final int n4, final int n5) {
        final int[] array = this.network[n2];
        array[0] -= (array[0] - n3) * n / 1024;
        array[1] -= (array[1] - n4) * n / 1024;
        array[2] -= (array[2] - n5) * n / 1024;
    }
    
    public byte[] colorMap() {
        final byte[] array = new byte[768];
        final int[] array2 = new int[256];
        for (int i = 0; i < 256; ++i) {
            array2[this.network[i][3]] = i;
        }
        int n = 0;
        int n4;
        for (int j = 0; j < 256; ++j, n = n4 + 1) {
            final int n2 = array2[j];
            final int n3 = n + 1;
            array[n] = (byte)this.network[n2][0];
            n4 = n3 + 1;
            array[n3] = (byte)this.network[n2][1];
            array[n4] = (byte)this.network[n2][2];
        }
        return array;
    }
    
    protected int contest(final int n, final int n2, final int n3) {
        int n4 = Integer.MAX_VALUE;
        int n5 = -1;
        int n6 = -1;
        int n7 = n4;
        int n15;
        int n17;
        for (int i = 0; i < 256; ++i, n4 = n15, n7 = n17) {
            final int[] array = this.network[i];
            final int n8 = array[0] - n;
            int n9;
            if ((n9 = n8) < 0) {
                n9 = -n8;
            }
            final int n10 = array[1] - n2;
            int n11;
            if ((n11 = n10) < 0) {
                n11 = -n10;
            }
            final int n12 = array[2] - n3;
            int n13;
            if ((n13 = n12) < 0) {
                n13 = -n12;
            }
            final int n14 = n9 + n11 + n13;
            if (n14 < (n15 = n4)) {
                n15 = n14;
                n5 = i;
            }
            final int n16 = n14 - (this.bias[i] >> 12);
            if (n16 < (n17 = n7)) {
                n17 = n16;
                n6 = i;
            }
            final int n18 = this.freq[i] >> 10;
            final int[] freq = this.freq;
            freq[i] -= n18;
            final int[] bias = this.bias;
            bias[i] += n18 << 10;
        }
        final int[] freq2 = this.freq;
        freq2[n5] += 64;
        final int[] bias2 = this.bias;
        bias2[n5] -= 65536;
        return n6;
    }
    
    public void inxbuild() {
        int n = 0;
        int n2 = 0;
        int n11;
        int n12;
        for (int i = 0; i < 256; ++i, n2 = n11, n = n12) {
            final int[] array = this.network[i];
            final int n3 = i;
            int n4 = array[1];
            int j = i + 1;
            int n5 = n3;
            while (j < 256) {
                final int[] array2 = this.network[j];
                int n6;
                if (array2[1] < (n6 = n4)) {
                    n5 = j;
                    n6 = array2[1];
                }
                ++j;
                n4 = n6;
            }
            final int[] array3 = this.network[n5];
            if (i != n5) {
                final int n7 = array3[0];
                array3[0] = array[0];
                array[0] = n7;
                final int n8 = array3[1];
                array3[1] = array[1];
                array[1] = n8;
                final int n9 = array3[2];
                array3[2] = array[2];
                array[2] = n9;
                final int n10 = array3[3];
                array3[3] = array[3];
                array[3] = n10;
            }
            n11 = n2;
            n12 = n;
            if (n4 != n2) {
                this.netindex[n2] = n + i >> 1;
                for (int k = n2 + 1; k < n4; ++k) {
                    this.netindex[k] = i;
                }
                n12 = i;
                n11 = n4;
            }
        }
        this.netindex[n2] = n + 255 >> 1;
        for (int l = n2 + 1; l < 256; ++l) {
            this.netindex[l] = 255;
        }
    }
    
    public void learn() {
        final int lengthcount = this.lengthcount;
        int n = 1509;
        if (lengthcount < 1509) {
            this.samplefac = 1;
        }
        this.alphadec = (this.samplefac - 1) / 3 + 30;
        final byte[] thepicture = this.thepicture;
        final int lengthcount2 = this.lengthcount;
        final int n2 = this.lengthcount / (this.samplefac * 3);
        int n3 = n2 / 100;
        int n4;
        if ((n4 = 2048 >> 6) <= 1) {
            n4 = 0;
        }
        for (int i = 0; i < n4; ++i) {
            this.radpower[i] = (n4 * n4 - i * i) * 256 / (n4 * n4) * 1024;
        }
        Label_0194: {
            int n5;
            if (this.lengthcount < 1509) {
                n5 = 3;
            }
            else if (this.lengthcount % 499 != 0) {
                n5 = 1497;
            }
            else if (this.lengthcount % 491 != 0) {
                n5 = 1473;
            }
            else {
                if (this.lengthcount % 487 == 0) {
                    break Label_0194;
                }
                n5 = 1461;
            }
            n = n5;
        }
        int n6 = 0;
        int j = 0;
        int n7 = 1024;
        final int n8 = 2048;
        int n9 = n4;
        int n10 = n8;
        while (j < n2) {
            final int n11 = (thepicture[n6 + 0] & 0xFF) << 4;
            final int n12 = (thepicture[n6 + 1] & 0xFF) << 4;
            final int n13 = (thepicture[n6 + 2] & 0xFF) << 4;
            final int contest = this.contest(n11, n12, n13);
            this.altersingle(n7, contest, n11, n12, n13);
            if (n9 != 0) {
                this.alterneigh(n9, contest, n11, n12, n13);
            }
            final int n14 = n6 + n;
            int n15;
            if ((n15 = n14) >= lengthcount2) {
                n15 = n14 - this.lengthcount;
            }
            final int n16 = j + 1;
            int n17;
            if ((n17 = n3) == 0) {
                n17 = 1;
            }
            j = n16;
            n6 = n15;
            n3 = n17;
            if (n16 % n17 == 0) {
                final int n18 = n7 - n7 / this.alphadec;
                final int n19 = n10 - n10 / 30;
                int n20;
                if ((n20 = n19 >> 6) <= 1) {
                    n20 = 0;
                }
                final int n21 = n20;
                int n22 = 0;
                while (true) {
                    j = n16;
                    n6 = n15;
                    n7 = n18;
                    n9 = n21;
                    n10 = n19;
                    n3 = n17;
                    if (n22 >= n21) {
                        break;
                    }
                    this.radpower[n22] = (n21 * n21 - n22 * n22) * 256 / (n21 * n21) * n18;
                    ++n22;
                }
            }
        }
    }
    
    public int map(final int n, final int n2, final int n3) {
        int n4 = 1000;
        int n5 = -1;
        int n6 = this.netindex[n2];
        int n7 = n6 - 1;
        while (n6 < 256 || n7 >= 0) {
            int n8 = n4;
            int n9 = n5;
            int n10;
            if ((n10 = n6) < 256) {
                final int[] array = this.network[n6];
                final int n11 = array[1] - n2;
                if (n11 >= n4) {
                    n10 = 256;
                    n8 = n4;
                    n9 = n5;
                }
                else {
                    final int n12 = n6 + 1;
                    int n13;
                    if ((n13 = n11) < 0) {
                        n13 = -n11;
                    }
                    final int n14 = array[0] - n;
                    int n15;
                    if ((n15 = n14) < 0) {
                        n15 = -n14;
                    }
                    final int n16 = n13 + n15;
                    n8 = n4;
                    n9 = n5;
                    n10 = n12;
                    if (n16 < n4) {
                        final int n17 = array[2] - n3;
                        int n18;
                        if ((n18 = n17) < 0) {
                            n18 = -n17;
                        }
                        final int n19 = n16 + n18;
                        n8 = n4;
                        n9 = n5;
                        n10 = n12;
                        if (n19 < n4) {
                            n8 = n19;
                            n9 = array[3];
                            n10 = n12;
                        }
                    }
                }
            }
            n4 = n8;
            n5 = n9;
            n6 = n10;
            if (n7 >= 0) {
                final int[] array2 = this.network[n7];
                final int n20 = n2 - array2[1];
                if (n20 >= n8) {
                    n7 = -1;
                    n4 = n8;
                    n5 = n9;
                    n6 = n10;
                }
                else {
                    final int n21 = n7 - 1;
                    int n22;
                    if ((n22 = n20) < 0) {
                        n22 = -n20;
                    }
                    final int n23 = array2[0] - n;
                    int n24;
                    if ((n24 = n23) < 0) {
                        n24 = -n23;
                    }
                    final int n25 = n22 + n24;
                    n4 = n8;
                    n5 = n9;
                    n6 = n10;
                    n7 = n21;
                    if (n25 >= n8) {
                        continue;
                    }
                    final int n26 = array2[2] - n3;
                    int n27;
                    if ((n27 = n26) < 0) {
                        n27 = -n26;
                    }
                    final int n28 = n25 + n27;
                    n4 = n8;
                    n5 = n9;
                    n6 = n10;
                    n7 = n21;
                    if (n28 >= n8) {
                        continue;
                    }
                    n4 = n28;
                    n5 = array2[3];
                    n6 = n10;
                    n7 = n21;
                }
            }
        }
        return n5;
    }
    
    public byte[] process() {
        this.learn();
        this.unbiasnet();
        this.inxbuild();
        return this.colorMap();
    }
    
    public void unbiasnet() {
        for (int i = 0; i < 256; ++i) {
            final int[] array = this.network[i];
            array[0] >>= 4;
            final int[] array2 = this.network[i];
            array2[1] >>= 4;
            final int[] array3 = this.network[i];
            array3[2] >>= 4;
            this.network[i][3] = i;
        }
    }
}
