package org.spongycastle.pqc.crypto.gmss.util;

import org.spongycastle.crypto.*;

public class WinternitzOTSVerify
{
    private Digest messDigestOTS;
    private int w;
    
    public WinternitzOTSVerify(final Digest messDigestOTS, final int w) {
        this.w = w;
        this.messDigestOTS = messDigestOTS;
    }
    
    public byte[] Verify(byte[] array, final byte[] array2) {
        final int digestSize = this.messDigestOTS.getDigestSize();
        final byte[] array3 = new byte[digestSize];
        this.messDigestOTS.update(array, 0, array.length);
        final int digestSize2 = this.messDigestOTS.getDigestSize();
        final byte[] array4 = new byte[digestSize2];
        this.messDigestOTS.doFinal(array4, 0);
        final int n = digestSize << 3;
        final int w = this.w;
        final int n2 = (w - 1 + n) / w;
        final int log = this.getLog((n2 << w) + 1);
        final int w2 = this.w;
        int n3 = ((log + w2 - 1) / w2 + n2) * digestSize;
        if (n3 != array2.length) {
            return null;
        }
        final byte[] array5 = new byte[n3];
        int n14;
        if (8 % w2 == 0) {
            final int n4 = 8 / w2;
            final int n5 = (1 << w2) - 1;
            array = new byte[digestSize];
            int i = 0;
            int n6 = 0;
            int n7 = 0;
            while (i < digestSize2) {
                int j = 0;
                final int n8 = n3;
                while (j < n4) {
                    final int n9 = array4[i] & n5;
                    final int n10 = n7 + n9;
                    final int n11 = n6 * digestSize;
                    System.arraycopy(array2, n11, array, 0, digestSize);
                    int k = n9;
                    final byte b = (byte)n10;
                    while (k < n5) {
                        this.messDigestOTS.update(array, 0, array.length);
                        array = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(array, 0);
                        ++k;
                    }
                    System.arraycopy(array, 0, array5, n11, digestSize);
                    array4[i] >>>= (byte)this.w;
                    ++n6;
                    ++j;
                    n7 = b;
                }
                ++i;
                n3 = n8;
            }
            int n12 = (n2 << this.w) - n7;
            int n13 = 0;
            while (true) {
                n14 = n3;
                if (n13 >= log) {
                    break;
                }
                int l = n12 & n5;
                final int n15 = n6 * digestSize;
                System.arraycopy(array2, n15, array, 0, digestSize);
                while (l < n5) {
                    this.messDigestOTS.update(array, 0, array.length);
                    array = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(array, 0);
                    ++l;
                }
                System.arraycopy(array, 0, array5, n15, digestSize);
                final int w3 = this.w;
                n12 >>>= w3;
                ++n6;
                n13 += w3;
            }
        }
        else {
            final int n16 = n3;
            if (w2 < 8) {
                int n17 = digestSize / w2;
                final int n18 = (1 << w2) - 1;
                array = new byte[digestSize];
                int n19 = 0;
                int n20 = 0;
                int n21 = 0;
                int n22 = 0;
                while (n19 < n17) {
                    int n23;
                    long n24;
                    long n25;
                    for (n23 = 0, n24 = 0L; n23 < this.w; ++n23, n24 ^= n25) {
                        n25 = (array4[n20] & 0xFF) << (n23 << 3);
                        ++n20;
                    }
                    final int n26 = 0;
                    int n27 = n21;
                    final int n28 = n17;
                    int n31;
                    for (int n29 = n26; n29 < 8; ++n29, n27 = n31) {
                        final int n30 = (int)(n24 & (long)n18);
                        n31 = n27 + n30;
                        final int n32 = n22 * digestSize;
                        System.arraycopy(array2, n32, array, 0, digestSize);
                        for (int n33 = n30; n33 < n18; ++n33) {
                            this.messDigestOTS.update(array, 0, array.length);
                            array = new byte[this.messDigestOTS.getDigestSize()];
                            this.messDigestOTS.doFinal(array, 0);
                        }
                        System.arraycopy(array, 0, array5, n32, digestSize);
                        n24 >>>= this.w;
                        ++n22;
                    }
                    ++n19;
                    n17 = n28;
                    n21 = n27;
                }
                final int n34 = digestSize % this.w;
                final int n35 = 0;
                long n36 = 0L;
                int n37 = n20;
                long n39;
                for (int n38 = n35; n38 < n34; ++n38, n36 ^= n39) {
                    n39 = (array4[n37] & 0xFF) << (n38 << 3);
                    ++n37;
                }
                int n42;
                int w4;
                for (int n40 = 0; n40 < n34 << 3; n40 += w4, n21 = n42) {
                    final int n41 = (int)(n36 & (long)n18);
                    n42 = n21 + n41;
                    final int n43 = n22 * digestSize;
                    System.arraycopy(array2, n43, array, 0, digestSize);
                    for (int n44 = n41; n44 < n18; ++n44) {
                        this.messDigestOTS.update(array, 0, array.length);
                        array = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(array, 0);
                    }
                    System.arraycopy(array, 0, array5, n43, digestSize);
                    w4 = this.w;
                    n36 >>>= w4;
                    ++n22;
                }
                int n45 = (n2 << this.w) - n21;
                int n46 = 0;
                int n47 = n22;
                while (true) {
                    n14 = n16;
                    if (n46 >= log) {
                        break;
                    }
                    int n48 = n45 & n18;
                    final int n49 = n47 * digestSize;
                    System.arraycopy(array2, n49, array, 0, digestSize);
                    while (n48 < n18) {
                        this.messDigestOTS.update(array, 0, array.length);
                        array = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(array, 0);
                        ++n48;
                    }
                    System.arraycopy(array, 0, array5, n49, digestSize);
                    final int w5 = this.w;
                    n45 >>>= w5;
                    ++n47;
                    n46 += w5;
                }
            }
            else {
                n14 = n16;
                if (w2 < 57) {
                    final int n50 = n - w2;
                    final int n51 = (1 << w2) - 1;
                    array = new byte[digestSize];
                    int n52 = 0;
                    int n53 = 0;
                    int n54 = 0;
                    final int n55 = n2;
                    final int n56 = log;
                    while (n52 <= n50) {
                        int n57 = n52 >>> 3;
                        final int n58 = this.w + n52;
                        int n59 = 0;
                        long n60;
                        long n61;
                        for (n60 = 0L; n57 < n58 + 7 >>> 3; ++n57, n60 ^= n61) {
                            n61 = (array4[n57] & 0xFF) << (n59 << 3);
                            ++n59;
                        }
                        final long n62 = n51;
                        long n63 = n60 >>> n52 % 8 & n62;
                        n54 += (int)n63;
                        final int n64 = n53 * digestSize;
                        System.arraycopy(array2, n64, array, 0, digestSize);
                        while (n63 < n62) {
                            this.messDigestOTS.update(array, 0, array.length);
                            array = new byte[this.messDigestOTS.getDigestSize()];
                            this.messDigestOTS.doFinal(array, 0);
                            ++n63;
                        }
                        System.arraycopy(array, 0, array5, n64, digestSize);
                        ++n53;
                        n52 = n58;
                    }
                    int n65 = n52 >>> 3;
                    if (n65 < digestSize) {
                        int n66 = 0;
                        long n67;
                        long n68;
                        for (n67 = 0L; n65 < digestSize; ++n65, n67 ^= n68) {
                            n68 = (array4[n65] & 0xFF) << (n66 << 3);
                            ++n66;
                        }
                        final long n69 = n51;
                        long n70 = n67 >>> n52 % 8 & n69;
                        n54 += (int)n70;
                        final int n71 = n53 * digestSize;
                        System.arraycopy(array2, n71, array, 0, digestSize);
                        while (n70 < n69) {
                            this.messDigestOTS.update(array, 0, array.length);
                            array = new byte[this.messDigestOTS.getDigestSize()];
                            this.messDigestOTS.doFinal(array, 0);
                            ++n70;
                        }
                        System.arraycopy(array, 0, array5, n71, digestSize);
                        ++n53;
                    }
                    int n72 = (n55 << this.w) - n54;
                    int n73 = 0;
                    int n74 = n53;
                    while (true) {
                        n14 = n16;
                        if (n73 >= n56) {
                            break;
                        }
                        long n75 = n72 & n51;
                        final int n76 = n74 * digestSize;
                        System.arraycopy(array2, n76, array, 0, digestSize);
                        while (n75 < n51) {
                            this.messDigestOTS.update(array, 0, array.length);
                            array = new byte[this.messDigestOTS.getDigestSize()];
                            this.messDigestOTS.doFinal(array, 0);
                            ++n75;
                        }
                        System.arraycopy(array, 0, array5, n76, digestSize);
                        final int w6 = this.w;
                        n72 >>>= w6;
                        ++n74;
                        n73 += w6;
                    }
                }
            }
        }
        array = new byte[digestSize];
        this.messDigestOTS.update(array5, 0, n14);
        array = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(array, 0);
        return array;
    }
    
    public int getLog(final int n) {
        int n2 = 1;
        for (int i = 2; i < n; i <<= 1, ++n2) {}
        return n2;
    }
    
    public int getSignatureLength() {
        final int digestSize = this.messDigestOTS.getDigestSize();
        final int w = this.w;
        final int n = ((digestSize << 3) + (w - 1)) / w;
        final int log = this.getLog((n << w) + 1);
        final int w2 = this.w;
        return digestSize * (n + (log + w2 - 1) / w2);
    }
}
