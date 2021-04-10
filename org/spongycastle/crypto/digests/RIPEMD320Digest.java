package org.spongycastle.crypto.digests;

import org.spongycastle.util.*;

public class RIPEMD320Digest extends GeneralDigest
{
    private static final int DIGEST_LENGTH = 40;
    private int H0;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int H5;
    private int H6;
    private int H7;
    private int H8;
    private int H9;
    private int[] X;
    private int xOff;
    
    public RIPEMD320Digest() {
        this.X = new int[16];
        this.reset();
    }
    
    public RIPEMD320Digest(final RIPEMD320Digest ripemd320Digest) {
        super(ripemd320Digest);
        this.X = new int[16];
        this.doCopy(ripemd320Digest);
    }
    
    private int RL(final int n, final int n2) {
        return n << n2 | n >>> 32 - n2;
    }
    
    private void doCopy(final RIPEMD320Digest ripemd320Digest) {
        super.copyIn(ripemd320Digest);
        this.H0 = ripemd320Digest.H0;
        this.H1 = ripemd320Digest.H1;
        this.H2 = ripemd320Digest.H2;
        this.H3 = ripemd320Digest.H3;
        this.H4 = ripemd320Digest.H4;
        this.H5 = ripemd320Digest.H5;
        this.H6 = ripemd320Digest.H6;
        this.H7 = ripemd320Digest.H7;
        this.H8 = ripemd320Digest.H8;
        this.H9 = ripemd320Digest.H9;
        final int[] x = ripemd320Digest.X;
        System.arraycopy(x, 0, this.X, 0, x.length);
        this.xOff = ripemd320Digest.xOff;
    }
    
    private int f1(final int n, final int n2, final int n3) {
        return n ^ n2 ^ n3;
    }
    
    private int f2(final int n, final int n2, final int n3) {
        return (n & n2) | (n3 & ~n);
    }
    
    private int f3(final int n, final int n2, final int n3) {
        return (n | ~n2) ^ n3;
    }
    
    private int f4(final int n, final int n2, final int n3) {
        return (n & n3) | (n2 & ~n3);
    }
    
    private int f5(final int n, final int n2, final int n3) {
        return n ^ (n2 | ~n3);
    }
    
    private void unpackWord(final int n, final byte[] array, final int n2) {
        array[n2] = (byte)n;
        array[n2 + 1] = (byte)(n >>> 8);
        array[n2 + 2] = (byte)(n >>> 16);
        array[n2 + 3] = (byte)(n >>> 24);
    }
    
    @Override
    public Memoable copy() {
        return new RIPEMD320Digest(this);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        this.finish();
        this.unpackWord(this.H0, array, n);
        this.unpackWord(this.H1, array, n + 4);
        this.unpackWord(this.H2, array, n + 8);
        this.unpackWord(this.H3, array, n + 12);
        this.unpackWord(this.H4, array, n + 16);
        this.unpackWord(this.H5, array, n + 20);
        this.unpackWord(this.H6, array, n + 24);
        this.unpackWord(this.H7, array, n + 28);
        this.unpackWord(this.H8, array, n + 32);
        this.unpackWord(this.H9, array, n + 36);
        this.reset();
        return 40;
    }
    
    @Override
    public String getAlgorithmName() {
        return "RIPEMD320";
    }
    
    @Override
    public int getDigestSize() {
        return 40;
    }
    
    @Override
    protected void processBlock() {
        final int h0 = this.H0;
        final int h2 = this.H1;
        final int h3 = this.H2;
        final int h4 = this.H3;
        final int h5 = this.H4;
        final int h6 = this.H5;
        final int h7 = this.H6;
        final int h8 = this.H7;
        final int h9 = this.H8;
        final int h10 = this.H9;
        final int n = this.RL(h0 + this.f1(h2, h3, h4) + this.X[0], 11) + h5;
        final int rl = this.RL(h3, 10);
        final int n2 = this.RL(h5 + this.f1(n, h2, rl) + this.X[1], 14) + h4;
        final int rl2 = this.RL(h2, 10);
        final int n3 = this.RL(h4 + this.f1(n2, n, rl2) + this.X[2], 15) + rl;
        final int rl3 = this.RL(n, 10);
        final int n4 = this.RL(rl + this.f1(n3, n2, rl3) + this.X[3], 12) + rl2;
        final int rl4 = this.RL(n2, 10);
        final int n5 = this.RL(rl2 + this.f1(n4, n3, rl4) + this.X[4], 5) + rl3;
        final int rl5 = this.RL(n3, 10);
        final int n6 = this.RL(rl3 + this.f1(n5, n4, rl5) + this.X[5], 8) + rl4;
        final int rl6 = this.RL(n4, 10);
        final int n7 = this.RL(rl4 + this.f1(n6, n5, rl6) + this.X[6], 7) + rl5;
        final int rl7 = this.RL(n5, 10);
        final int n8 = this.RL(rl5 + this.f1(n7, n6, rl7) + this.X[7], 9) + rl6;
        final int rl8 = this.RL(n6, 10);
        final int n9 = this.RL(rl6 + this.f1(n8, n7, rl8) + this.X[8], 11) + rl7;
        final int rl9 = this.RL(n7, 10);
        final int n10 = this.RL(rl7 + this.f1(n9, n8, rl9) + this.X[9], 13) + rl8;
        final int rl10 = this.RL(n8, 10);
        final int n11 = this.RL(rl8 + this.f1(n10, n9, rl10) + this.X[10], 14) + rl9;
        final int rl11 = this.RL(n9, 10);
        final int n12 = this.RL(rl9 + this.f1(n11, n10, rl11) + this.X[11], 15) + rl10;
        final int rl12 = this.RL(n10, 10);
        final int n13 = this.RL(rl10 + this.f1(n12, n11, rl12) + this.X[12], 6) + rl11;
        final int rl13 = this.RL(n11, 10);
        final int n14 = this.RL(rl11 + this.f1(n13, n12, rl13) + this.X[13], 7) + rl12;
        final int rl14 = this.RL(n12, 10);
        final int n15 = this.RL(rl12 + this.f1(n14, n13, rl14) + this.X[14], 9) + rl13;
        final int rl15 = this.RL(n13, 10);
        final int n16 = this.RL(rl13 + this.f1(n15, n14, rl15) + this.X[15], 8) + rl14;
        final int rl16 = this.RL(n14, 10);
        final int n17 = this.RL(h6 + this.f5(h7, h8, h9) + this.X[5] + 1352829926, 8) + h10;
        final int rl17 = this.RL(h8, 10);
        final int n18 = this.RL(h10 + this.f5(n17, h7, rl17) + this.X[14] + 1352829926, 9) + h9;
        final int rl18 = this.RL(h7, 10);
        final int n19 = this.RL(h9 + this.f5(n18, n17, rl18) + this.X[7] + 1352829926, 9) + rl17;
        final int rl19 = this.RL(n17, 10);
        final int n20 = this.RL(rl17 + this.f5(n19, n18, rl19) + this.X[0] + 1352829926, 11) + rl18;
        final int rl20 = this.RL(n18, 10);
        final int n21 = this.RL(rl18 + this.f5(n20, n19, rl20) + this.X[9] + 1352829926, 13) + rl19;
        final int rl21 = this.RL(n19, 10);
        final int n22 = this.RL(rl19 + this.f5(n21, n20, rl21) + this.X[2] + 1352829926, 15) + rl20;
        final int rl22 = this.RL(n20, 10);
        final int n23 = this.RL(rl20 + this.f5(n22, n21, rl22) + this.X[11] + 1352829926, 15) + rl21;
        final int rl23 = this.RL(n21, 10);
        final int n24 = this.RL(rl21 + this.f5(n23, n22, rl23) + this.X[4] + 1352829926, 5) + rl22;
        final int rl24 = this.RL(n22, 10);
        final int n25 = this.RL(rl22 + this.f5(n24, n23, rl24) + this.X[13] + 1352829926, 7) + rl23;
        final int rl25 = this.RL(n23, 10);
        final int n26 = this.RL(rl23 + this.f5(n25, n24, rl25) + this.X[6] + 1352829926, 7) + rl24;
        final int rl26 = this.RL(n24, 10);
        final int n27 = this.RL(rl24 + this.f5(n26, n25, rl26) + this.X[15] + 1352829926, 8) + rl25;
        final int rl27 = this.RL(n25, 10);
        final int n28 = this.RL(rl25 + this.f5(n27, n26, rl27) + this.X[8] + 1352829926, 11) + rl26;
        final int rl28 = this.RL(n26, 10);
        final int n29 = this.RL(rl26 + this.f5(n28, n27, rl28) + this.X[1] + 1352829926, 14) + rl27;
        final int rl29 = this.RL(n27, 10);
        final int n30 = this.RL(rl27 + this.f5(n29, n28, rl29) + this.X[10] + 1352829926, 14) + rl28;
        final int rl30 = this.RL(n28, 10);
        final int n31 = this.RL(rl28 + this.f5(n30, n29, rl30) + this.X[3] + 1352829926, 12) + rl29;
        final int rl31 = this.RL(n29, 10);
        final int n32 = this.RL(rl29 + this.f5(n31, n30, rl31) + this.X[12] + 1352829926, 6) + rl30;
        final int rl32 = this.RL(n30, 10);
        final int n33 = this.RL(rl14 + this.f2(n32, n15, rl16) + this.X[7] + 1518500249, 7) + rl15;
        final int rl33 = this.RL(n15, 10);
        final int n34 = this.RL(rl15 + this.f2(n33, n32, rl33) + this.X[4] + 1518500249, 6) + rl16;
        final int rl34 = this.RL(n32, 10);
        final int n35 = this.RL(rl16 + this.f2(n34, n33, rl34) + this.X[13] + 1518500249, 8) + rl33;
        final int rl35 = this.RL(n33, 10);
        final int n36 = this.RL(rl33 + this.f2(n35, n34, rl35) + this.X[1] + 1518500249, 13) + rl34;
        final int rl36 = this.RL(n34, 10);
        final int n37 = this.RL(rl34 + this.f2(n36, n35, rl36) + this.X[10] + 1518500249, 11) + rl35;
        final int rl37 = this.RL(n35, 10);
        final int n38 = this.RL(rl35 + this.f2(n37, n36, rl37) + this.X[6] + 1518500249, 9) + rl36;
        final int rl38 = this.RL(n36, 10);
        final int n39 = this.RL(rl36 + this.f2(n38, n37, rl38) + this.X[15] + 1518500249, 7) + rl37;
        final int rl39 = this.RL(n37, 10);
        final int n40 = this.RL(rl37 + this.f2(n39, n38, rl39) + this.X[3] + 1518500249, 15) + rl38;
        final int rl40 = this.RL(n38, 10);
        final int n41 = this.RL(rl38 + this.f2(n40, n39, rl40) + this.X[12] + 1518500249, 7) + rl39;
        final int rl41 = this.RL(n39, 10);
        final int n42 = this.RL(rl39 + this.f2(n41, n40, rl41) + this.X[0] + 1518500249, 12) + rl40;
        final int rl42 = this.RL(n40, 10);
        final int n43 = this.RL(rl40 + this.f2(n42, n41, rl42) + this.X[9] + 1518500249, 15) + rl41;
        final int rl43 = this.RL(n41, 10);
        final int n44 = this.RL(rl41 + this.f2(n43, n42, rl43) + this.X[5] + 1518500249, 9) + rl42;
        final int rl44 = this.RL(n42, 10);
        final int n45 = this.RL(rl42 + this.f2(n44, n43, rl44) + this.X[2] + 1518500249, 11) + rl43;
        final int rl45 = this.RL(n43, 10);
        final int n46 = this.RL(rl43 + this.f2(n45, n44, rl45) + this.X[14] + 1518500249, 7) + rl44;
        final int rl46 = this.RL(n44, 10);
        final int n47 = this.RL(rl44 + this.f2(n46, n45, rl46) + this.X[11] + 1518500249, 13) + rl45;
        final int rl47 = this.RL(n45, 10);
        final int n48 = this.RL(rl45 + this.f2(n47, n46, rl47) + this.X[8] + 1518500249, 12) + rl46;
        final int rl48 = this.RL(n46, 10);
        final int n49 = this.RL(rl30 + this.f4(n16, n31, rl32) + this.X[6] + 1548603684, 9) + rl31;
        final int rl49 = this.RL(n31, 10);
        final int n50 = this.RL(rl31 + this.f4(n49, n16, rl49) + this.X[11] + 1548603684, 13) + rl32;
        final int rl50 = this.RL(n16, 10);
        final int n51 = this.RL(rl32 + this.f4(n50, n49, rl50) + this.X[3] + 1548603684, 15) + rl49;
        final int rl51 = this.RL(n49, 10);
        final int n52 = this.RL(rl49 + this.f4(n51, n50, rl51) + this.X[7] + 1548603684, 7) + rl50;
        final int rl52 = this.RL(n50, 10);
        final int n53 = this.RL(rl50 + this.f4(n52, n51, rl52) + this.X[0] + 1548603684, 12) + rl51;
        final int rl53 = this.RL(n51, 10);
        final int n54 = this.RL(rl51 + this.f4(n53, n52, rl53) + this.X[13] + 1548603684, 8) + rl52;
        final int rl54 = this.RL(n52, 10);
        final int n55 = this.RL(rl52 + this.f4(n54, n53, rl54) + this.X[5] + 1548603684, 9) + rl53;
        final int rl55 = this.RL(n53, 10);
        final int n56 = this.RL(rl53 + this.f4(n55, n54, rl55) + this.X[10] + 1548603684, 11) + rl54;
        final int rl56 = this.RL(n54, 10);
        final int n57 = this.RL(rl54 + this.f4(n56, n55, rl56) + this.X[14] + 1548603684, 7) + rl55;
        final int rl57 = this.RL(n55, 10);
        final int n58 = this.RL(rl55 + this.f4(n57, n56, rl57) + this.X[15] + 1548603684, 7) + rl56;
        final int rl58 = this.RL(n56, 10);
        final int n59 = this.RL(rl56 + this.f4(n58, n57, rl58) + this.X[8] + 1548603684, 12) + rl57;
        final int rl59 = this.RL(n57, 10);
        final int n60 = this.RL(rl57 + this.f4(n59, n58, rl59) + this.X[12] + 1548603684, 7) + rl58;
        final int rl60 = this.RL(n58, 10);
        final int n61 = this.RL(rl58 + this.f4(n60, n59, rl60) + this.X[4] + 1548603684, 6) + rl59;
        final int rl61 = this.RL(n59, 10);
        final int n62 = this.RL(rl59 + this.f4(n61, n60, rl61) + this.X[9] + 1548603684, 15) + rl60;
        final int rl62 = this.RL(n60, 10);
        final int n63 = this.RL(rl60 + this.f4(n62, n61, rl62) + this.X[1] + 1548603684, 13) + rl61;
        final int rl63 = this.RL(n61, 10);
        final int n64 = this.RL(rl61 + this.f4(n63, n62, rl63) + this.X[2] + 1548603684, 11) + rl62;
        final int rl64 = this.RL(n62, 10);
        final int n65 = this.RL(rl46 + this.f3(n48, n47, rl64) + this.X[3] + 1859775393, 11) + rl47;
        final int rl65 = this.RL(n47, 10);
        final int n66 = this.RL(rl47 + this.f3(n65, n48, rl65) + this.X[10] + 1859775393, 13) + rl64;
        final int rl66 = this.RL(n48, 10);
        final int n67 = this.RL(rl64 + this.f3(n66, n65, rl66) + this.X[14] + 1859775393, 6) + rl65;
        final int rl67 = this.RL(n65, 10);
        final int n68 = this.RL(rl65 + this.f3(n67, n66, rl67) + this.X[4] + 1859775393, 7) + rl66;
        final int rl68 = this.RL(n66, 10);
        final int n69 = this.RL(rl66 + this.f3(n68, n67, rl68) + this.X[9] + 1859775393, 14) + rl67;
        final int rl69 = this.RL(n67, 10);
        final int n70 = this.RL(rl67 + this.f3(n69, n68, rl69) + this.X[15] + 1859775393, 9) + rl68;
        final int rl70 = this.RL(n68, 10);
        final int n71 = this.RL(rl68 + this.f3(n70, n69, rl70) + this.X[8] + 1859775393, 13) + rl69;
        final int rl71 = this.RL(n69, 10);
        final int n72 = this.RL(rl69 + this.f3(n71, n70, rl71) + this.X[1] + 1859775393, 15) + rl70;
        final int rl72 = this.RL(n70, 10);
        final int n73 = this.RL(rl70 + this.f3(n72, n71, rl72) + this.X[2] + 1859775393, 14) + rl71;
        final int rl73 = this.RL(n71, 10);
        final int n74 = this.RL(rl71 + this.f3(n73, n72, rl73) + this.X[7] + 1859775393, 8) + rl72;
        final int rl74 = this.RL(n72, 10);
        final int n75 = this.RL(rl72 + this.f3(n74, n73, rl74) + this.X[0] + 1859775393, 13) + rl73;
        final int rl75 = this.RL(n73, 10);
        final int n76 = this.RL(rl73 + this.f3(n75, n74, rl75) + this.X[6] + 1859775393, 6) + rl74;
        final int rl76 = this.RL(n74, 10);
        final int n77 = this.RL(rl74 + this.f3(n76, n75, rl76) + this.X[13] + 1859775393, 5) + rl75;
        final int rl77 = this.RL(n75, 10);
        final int n78 = this.RL(rl75 + this.f3(n77, n76, rl77) + this.X[11] + 1859775393, 12) + rl76;
        final int rl78 = this.RL(n76, 10);
        final int n79 = this.RL(rl76 + this.f3(n78, n77, rl78) + this.X[5] + 1859775393, 7) + rl77;
        final int rl79 = this.RL(n77, 10);
        final int n80 = this.RL(rl77 + this.f3(n79, n78, rl79) + this.X[12] + 1859775393, 5) + rl78;
        final int rl80 = this.RL(n78, 10);
        final int n81 = this.RL(rl62 + this.f3(n64, n63, rl48) + this.X[15] + 1836072691, 9) + rl63;
        final int rl81 = this.RL(n63, 10);
        final int n82 = this.RL(rl63 + this.f3(n81, n64, rl81) + this.X[5] + 1836072691, 7) + rl48;
        final int rl82 = this.RL(n64, 10);
        final int n83 = this.RL(rl48 + this.f3(n82, n81, rl82) + this.X[1] + 1836072691, 15) + rl81;
        final int rl83 = this.RL(n81, 10);
        final int n84 = this.RL(rl81 + this.f3(n83, n82, rl83) + this.X[3] + 1836072691, 11) + rl82;
        final int rl84 = this.RL(n82, 10);
        final int n85 = this.RL(rl82 + this.f3(n84, n83, rl84) + this.X[7] + 1836072691, 8) + rl83;
        final int rl85 = this.RL(n83, 10);
        final int n86 = this.RL(rl83 + this.f3(n85, n84, rl85) + this.X[14] + 1836072691, 6) + rl84;
        final int rl86 = this.RL(n84, 10);
        final int n87 = this.RL(rl84 + this.f3(n86, n85, rl86) + this.X[6] + 1836072691, 6) + rl85;
        final int rl87 = this.RL(n85, 10);
        final int n88 = this.RL(rl85 + this.f3(n87, n86, rl87) + this.X[9] + 1836072691, 14) + rl86;
        final int rl88 = this.RL(n86, 10);
        final int n89 = this.RL(rl86 + this.f3(n88, n87, rl88) + this.X[11] + 1836072691, 12) + rl87;
        final int rl89 = this.RL(n87, 10);
        final int n90 = this.RL(rl87 + this.f3(n89, n88, rl89) + this.X[8] + 1836072691, 13) + rl88;
        final int rl90 = this.RL(n88, 10);
        final int n91 = this.RL(rl88 + this.f3(n90, n89, rl90) + this.X[12] + 1836072691, 5) + rl89;
        final int rl91 = this.RL(n89, 10);
        final int n92 = this.RL(rl89 + this.f3(n91, n90, rl91) + this.X[2] + 1836072691, 14) + rl90;
        final int rl92 = this.RL(n90, 10);
        final int n93 = this.RL(rl90 + this.f3(n92, n91, rl92) + this.X[10] + 1836072691, 13) + rl91;
        final int rl93 = this.RL(n91, 10);
        final int n94 = this.RL(rl91 + this.f3(n93, n92, rl93) + this.X[0] + 1836072691, 13) + rl92;
        final int rl94 = this.RL(n92, 10);
        final int n95 = this.RL(rl92 + this.f3(n94, n93, rl94) + this.X[4] + 1836072691, 7) + rl93;
        final int rl95 = this.RL(n93, 10);
        final int n96 = this.RL(rl93 + this.f3(n95, n94, rl95) + this.X[13] + 1836072691, 5) + rl94;
        final int rl96 = this.RL(n94, 10);
        final int n97 = this.RL(rl94 + this.f4(n80, n79, rl80) + this.X[1] - 1894007588, 11) + rl79;
        final int rl97 = this.RL(n79, 10);
        final int n98 = this.RL(rl79 + this.f4(n97, n80, rl97) + this.X[9] - 1894007588, 12) + rl80;
        final int rl98 = this.RL(n80, 10);
        final int n99 = this.RL(rl80 + this.f4(n98, n97, rl98) + this.X[11] - 1894007588, 14) + rl97;
        final int rl99 = this.RL(n97, 10);
        final int n100 = this.RL(rl97 + this.f4(n99, n98, rl99) + this.X[10] - 1894007588, 15) + rl98;
        final int rl100 = this.RL(n98, 10);
        final int n101 = this.RL(rl98 + this.f4(n100, n99, rl100) + this.X[0] - 1894007588, 14) + rl99;
        final int rl101 = this.RL(n99, 10);
        final int n102 = this.RL(rl99 + this.f4(n101, n100, rl101) + this.X[8] - 1894007588, 15) + rl100;
        final int rl102 = this.RL(n100, 10);
        final int n103 = this.RL(rl100 + this.f4(n102, n101, rl102) + this.X[12] - 1894007588, 9) + rl101;
        final int rl103 = this.RL(n101, 10);
        final int n104 = this.RL(rl101 + this.f4(n103, n102, rl103) + this.X[4] - 1894007588, 8) + rl102;
        final int rl104 = this.RL(n102, 10);
        final int n105 = this.RL(rl102 + this.f4(n104, n103, rl104) + this.X[13] - 1894007588, 9) + rl103;
        final int rl105 = this.RL(n103, 10);
        final int n106 = this.RL(rl103 + this.f4(n105, n104, rl105) + this.X[3] - 1894007588, 14) + rl104;
        final int rl106 = this.RL(n104, 10);
        final int n107 = this.RL(rl104 + this.f4(n106, n105, rl106) + this.X[7] - 1894007588, 5) + rl105;
        final int rl107 = this.RL(n105, 10);
        final int n108 = this.RL(rl105 + this.f4(n107, n106, rl107) + this.X[15] - 1894007588, 6) + rl106;
        final int rl108 = this.RL(n106, 10);
        final int n109 = this.RL(rl106 + this.f4(n108, n107, rl108) + this.X[14] - 1894007588, 8) + rl107;
        final int rl109 = this.RL(n107, 10);
        final int n110 = this.RL(rl107 + this.f4(n109, n108, rl109) + this.X[5] - 1894007588, 6) + rl108;
        final int rl110 = this.RL(n108, 10);
        final int n111 = this.RL(rl108 + this.f4(n110, n109, rl110) + this.X[6] - 1894007588, 5) + rl109;
        final int rl111 = this.RL(n109, 10);
        final int n112 = this.RL(rl109 + this.f4(n111, n110, rl111) + this.X[2] - 1894007588, 12) + rl110;
        final int rl112 = this.RL(n110, 10);
        final int n113 = this.RL(rl78 + this.f2(n96, n95, rl96) + this.X[8] + 2053994217, 15) + rl95;
        final int rl113 = this.RL(n95, 10);
        final int n114 = this.RL(rl95 + this.f2(n113, n96, rl113) + this.X[6] + 2053994217, 5) + rl96;
        final int rl114 = this.RL(n96, 10);
        final int n115 = this.RL(rl96 + this.f2(n114, n113, rl114) + this.X[4] + 2053994217, 8) + rl113;
        final int rl115 = this.RL(n113, 10);
        final int n116 = this.RL(rl113 + this.f2(n115, n114, rl115) + this.X[1] + 2053994217, 11) + rl114;
        final int rl116 = this.RL(n114, 10);
        final int n117 = this.RL(rl114 + this.f2(n116, n115, rl116) + this.X[3] + 2053994217, 14) + rl115;
        final int rl117 = this.RL(n115, 10);
        final int n118 = this.RL(rl115 + this.f2(n117, n116, rl117) + this.X[11] + 2053994217, 14) + rl116;
        final int rl118 = this.RL(n116, 10);
        final int n119 = this.RL(rl116 + this.f2(n118, n117, rl118) + this.X[15] + 2053994217, 6) + rl117;
        final int rl119 = this.RL(n117, 10);
        final int n120 = this.RL(rl117 + this.f2(n119, n118, rl119) + this.X[0] + 2053994217, 14) + rl118;
        final int rl120 = this.RL(n118, 10);
        final int n121 = this.RL(rl118 + this.f2(n120, n119, rl120) + this.X[5] + 2053994217, 6) + rl119;
        final int rl121 = this.RL(n119, 10);
        final int n122 = this.RL(rl119 + this.f2(n121, n120, rl121) + this.X[12] + 2053994217, 9) + rl120;
        final int rl122 = this.RL(n120, 10);
        final int n123 = this.RL(rl120 + this.f2(n122, n121, rl122) + this.X[2] + 2053994217, 12) + rl121;
        final int rl123 = this.RL(n121, 10);
        final int n124 = this.RL(rl121 + this.f2(n123, n122, rl123) + this.X[13] + 2053994217, 9) + rl122;
        final int rl124 = this.RL(n122, 10);
        final int n125 = this.RL(rl122 + this.f2(n124, n123, rl124) + this.X[9] + 2053994217, 12) + rl123;
        final int rl125 = this.RL(n123, 10);
        final int n126 = this.RL(rl123 + this.f2(n125, n124, rl125) + this.X[7] + 2053994217, 5) + rl124;
        final int rl126 = this.RL(n124, 10);
        final int n127 = this.RL(rl124 + this.f2(n126, n125, rl126) + this.X[10] + 2053994217, 15) + rl125;
        final int rl127 = this.RL(n125, 10);
        final int n128 = this.RL(rl125 + this.f2(n127, n126, rl127) + this.X[14] + 2053994217, 8) + rl126;
        final int rl128 = this.RL(n126, 10);
        final int n129 = this.RL(rl110 + this.f5(n112, n127, rl112) + this.X[4] - 1454113458, 9) + rl111;
        final int rl129 = this.RL(n127, 10);
        final int n130 = this.RL(rl111 + this.f5(n129, n112, rl129) + this.X[0] - 1454113458, 15) + rl112;
        final int rl130 = this.RL(n112, 10);
        final int n131 = this.RL(rl112 + this.f5(n130, n129, rl130) + this.X[5] - 1454113458, 5) + rl129;
        final int rl131 = this.RL(n129, 10);
        final int n132 = this.RL(rl129 + this.f5(n131, n130, rl131) + this.X[9] - 1454113458, 11) + rl130;
        final int rl132 = this.RL(n130, 10);
        final int n133 = this.RL(rl130 + this.f5(n132, n131, rl132) + this.X[7] - 1454113458, 6) + rl131;
        final int rl133 = this.RL(n131, 10);
        final int n134 = this.RL(rl131 + this.f5(n133, n132, rl133) + this.X[12] - 1454113458, 8) + rl132;
        final int rl134 = this.RL(n132, 10);
        final int n135 = this.RL(rl132 + this.f5(n134, n133, rl134) + this.X[2] - 1454113458, 13) + rl133;
        final int rl135 = this.RL(n133, 10);
        final int n136 = this.RL(rl133 + this.f5(n135, n134, rl135) + this.X[10] - 1454113458, 12) + rl134;
        final int rl136 = this.RL(n134, 10);
        final int n137 = this.RL(rl134 + this.f5(n136, n135, rl136) + this.X[14] - 1454113458, 5) + rl135;
        final int rl137 = this.RL(n135, 10);
        final int n138 = this.RL(rl135 + this.f5(n137, n136, rl137) + this.X[1] - 1454113458, 12) + rl136;
        final int rl138 = this.RL(n136, 10);
        final int n139 = this.RL(rl136 + this.f5(n138, n137, rl138) + this.X[3] - 1454113458, 13) + rl137;
        final int rl139 = this.RL(n137, 10);
        final int n140 = this.RL(rl137 + this.f5(n139, n138, rl139) + this.X[8] - 1454113458, 14) + rl138;
        final int rl140 = this.RL(n138, 10);
        final int n141 = this.RL(rl138 + this.f5(n140, n139, rl140) + this.X[11] - 1454113458, 11) + rl139;
        final int rl141 = this.RL(n139, 10);
        final int n142 = this.RL(rl139 + this.f5(n141, n140, rl141) + this.X[6] - 1454113458, 8) + rl140;
        final int rl142 = this.RL(n140, 10);
        final int n143 = this.RL(rl140 + this.f5(n142, n141, rl142) + this.X[15] - 1454113458, 5) + rl141;
        final int rl143 = this.RL(n141, 10);
        final int rl144 = this.RL(rl141 + this.f5(n143, n142, rl143) + this.X[13] - 1454113458, 6);
        final int rl145 = this.RL(n142, 10);
        final int n144 = this.RL(rl126 + this.f1(n128, n111, rl128) + this.X[12], 8) + rl127;
        final int rl146 = this.RL(n111, 10);
        final int n145 = this.RL(rl127 + this.f1(n144, n128, rl146) + this.X[15], 5) + rl128;
        final int rl147 = this.RL(n128, 10);
        final int n146 = this.RL(rl128 + this.f1(n145, n144, rl147) + this.X[10], 12) + rl146;
        final int rl148 = this.RL(n144, 10);
        final int n147 = this.RL(rl146 + this.f1(n146, n145, rl148) + this.X[4], 9) + rl147;
        final int rl149 = this.RL(n145, 10);
        final int n148 = this.RL(rl147 + this.f1(n147, n146, rl149) + this.X[1], 12) + rl148;
        final int rl150 = this.RL(n146, 10);
        final int n149 = this.RL(rl148 + this.f1(n148, n147, rl150) + this.X[5], 5) + rl149;
        final int rl151 = this.RL(n147, 10);
        final int n150 = this.RL(rl149 + this.f1(n149, n148, rl151) + this.X[8], 14) + rl150;
        final int rl152 = this.RL(n148, 10);
        final int n151 = this.RL(rl150 + this.f1(n150, n149, rl152) + this.X[7], 6) + rl151;
        final int rl153 = this.RL(n149, 10);
        final int n152 = this.RL(rl151 + this.f1(n151, n150, rl153) + this.X[6], 8) + rl152;
        final int rl154 = this.RL(n150, 10);
        final int n153 = this.RL(rl152 + this.f1(n152, n151, rl154) + this.X[2], 13) + rl153;
        final int rl155 = this.RL(n151, 10);
        final int n154 = this.RL(rl153 + this.f1(n153, n152, rl155) + this.X[13], 6) + rl154;
        final int rl156 = this.RL(n152, 10);
        final int n155 = this.RL(rl154 + this.f1(n154, n153, rl156) + this.X[14], 5) + rl155;
        final int rl157 = this.RL(n153, 10);
        final int n156 = this.RL(rl155 + this.f1(n155, n154, rl157) + this.X[0], 15) + rl156;
        final int rl158 = this.RL(n154, 10);
        final int n157 = this.RL(rl156 + this.f1(n156, n155, rl158) + this.X[3], 13) + rl157;
        final int rl159 = this.RL(n155, 10);
        final int n158 = this.RL(rl157 + this.f1(n157, n156, rl159) + this.X[9], 11) + rl158;
        final int rl160 = this.RL(n156, 10);
        final int rl161 = this.RL(rl158 + this.f1(n158, n157, rl160) + this.X[11], 11);
        final int rl162 = this.RL(n157, 10);
        this.H0 += rl142;
        this.H1 += rl144 + rl142;
        this.H2 += n143;
        this.H3 += rl145;
        this.H4 += rl160;
        this.H5 += rl159;
        this.H6 += rl161 + rl159;
        this.H7 += n158;
        this.H8 += rl162;
        this.H9 += rl143;
        int n159 = 0;
        this.xOff = 0;
        while (true) {
            final int[] x = this.X;
            if (n159 == x.length) {
                break;
            }
            x[n159] = 0;
            ++n159;
        }
    }
    
    @Override
    protected void processLength(final long n) {
        if (this.xOff > 14) {
            this.processBlock();
        }
        final int[] x = this.X;
        x[14] = (int)(n & -1L);
        x[15] = (int)(n >>> 32);
    }
    
    @Override
    protected void processWord(final byte[] array, final int n) {
        final int[] x = this.X;
        final int xOff = this.xOff;
        final int xOff2 = xOff + 1;
        this.xOff = xOff2;
        x[xOff] = ((array[n + 3] & 0xFF) << 24 | ((array[n] & 0xFF) | (array[n + 1] & 0xFF) << 8 | (array[n + 2] & 0xFF) << 16));
        if (xOff2 == 16) {
            this.processBlock();
        }
    }
    
    @Override
    public void reset() {
        super.reset();
        this.H0 = 1732584193;
        this.H1 = -271733879;
        this.H2 = -1732584194;
        this.H3 = 271733878;
        this.H4 = -1009589776;
        this.H5 = 1985229328;
        this.H6 = -19088744;
        this.H7 = -1985229329;
        this.H8 = 19088743;
        this.H9 = 1009589775;
        this.xOff = 0;
        int n = 0;
        while (true) {
            final int[] x = this.X;
            if (n == x.length) {
                break;
            }
            x[n] = 0;
            ++n;
        }
    }
    
    @Override
    public void reset(final Memoable memoable) {
        this.doCopy((RIPEMD320Digest)memoable);
    }
}
