package org.spongycastle.pqc.crypto.xmss;

import java.util.*;

final class WOTSPlus
{
    private final KeyedHashFunctions khf;
    private final WOTSPlusParameters params;
    private byte[] publicSeed;
    private byte[] secretKeySeed;
    
    protected WOTSPlus(final WOTSPlusParameters params) {
        if (params != null) {
            this.params = params;
            final int digestSize = params.getDigestSize();
            this.khf = new KeyedHashFunctions(params.getDigest(), digestSize);
            this.secretKeySeed = new byte[digestSize];
            this.publicSeed = new byte[digestSize];
            return;
        }
        throw new NullPointerException("params == null");
    }
    
    private byte[] chain(byte[] chain, int i, final int n, final OTSHashAddress otsHashAddress) {
        final int digestSize = this.params.getDigestSize();
        if (chain == null) {
            throw new NullPointerException("startHash == null");
        }
        if (chain.length != digestSize) {
            final StringBuilder sb = new StringBuilder();
            sb.append("startHash needs to be ");
            sb.append(digestSize);
            sb.append("bytes");
            throw new IllegalArgumentException(sb.toString());
        }
        if (otsHashAddress == null) {
            throw new NullPointerException("otsHashAddress == null");
        }
        if (otsHashAddress.toByteArray() == null) {
            throw new NullPointerException("otsHashAddress byte array == null");
        }
        final int n2 = i + n;
        if (n2 > this.params.getWinternitzParameter() - 1) {
            throw new IllegalArgumentException("max chain length must not be greater than w");
        }
        if (n == 0) {
            return chain;
        }
        chain = this.chain(chain, i, n - 1, otsHashAddress);
        final OTSHashAddress.Builder withHashAddress = ((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)((XMSSAddress.Builder<OTSHashAddress.Builder>)new OTSHashAddress.Builder()).withLayerAddress(otsHashAddress.getLayerAddress())).withTreeAddress(otsHashAddress.getTreeAddress())).withOTSAddress(otsHashAddress.getOTSAddress()).withChainAddress(otsHashAddress.getChainAddress()).withHashAddress(n2 - 1);
        i = 0;
        final OTSHashAddress otsHashAddress2 = (OTSHashAddress)((XMSSAddress.Builder<OTSHashAddress.Builder>)withHashAddress).withKeyAndMask(0).build();
        final byte[] prf = this.khf.PRF(this.publicSeed, otsHashAddress2.toByteArray());
        final byte[] prf2 = this.khf.PRF(this.publicSeed, ((OTSHashAddress)((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)((XMSSAddress.Builder<OTSHashAddress.Builder>)new OTSHashAddress.Builder()).withLayerAddress(otsHashAddress2.getLayerAddress())).withTreeAddress(otsHashAddress2.getTreeAddress())).withOTSAddress(otsHashAddress2.getOTSAddress()).withChainAddress(otsHashAddress2.getChainAddress()).withHashAddress(otsHashAddress2.getHashAddress())).withKeyAndMask(1)).build()).toByteArray());
        final byte[] array = new byte[digestSize];
        while (i < digestSize) {
            array[i] = (byte)(chain[i] ^ prf2[i]);
            ++i;
        }
        return this.khf.F(prf, array);
    }
    
    private List<Integer> convertToBaseW(final byte[] array, final int n, final int n2) {
        if (array == null) {
            throw new NullPointerException("msg == null");
        }
        if (n != 4 && n != 16) {
            throw new IllegalArgumentException("w needs to be 4 or 16");
        }
        final int log2 = XMSSUtil.log2(n);
        if (n2 <= array.length * 8 / log2) {
            final ArrayList<Integer> list = new ArrayList<Integer>();
            for (int i = 0; i < array.length; ++i) {
                for (int j = 8 - log2; j >= 0; j -= log2) {
                    list.add(array[i] >> j & n - 1);
                    if (list.size() == n2) {
                        return list;
                    }
                }
            }
            return list;
        }
        throw new IllegalArgumentException("outLength too big");
    }
    
    private byte[] expandSecretKeySeed(final int n) {
        if (n >= 0 && n < this.params.getLen()) {
            return this.khf.PRF(this.secretKeySeed, XMSSUtil.toBytesBigEndian(n, 32));
        }
        throw new IllegalArgumentException("index out of bounds");
    }
    
    protected KeyedHashFunctions getKhf() {
        return this.khf;
    }
    
    protected WOTSPlusParameters getParams() {
        return this.params;
    }
    
    protected WOTSPlusPrivateKeyParameters getPrivateKey() {
        final int len = this.params.getLen();
        final byte[][] array = new byte[len][];
        for (int i = 0; i < len; ++i) {
            array[i] = this.expandSecretKeySeed(i);
        }
        return new WOTSPlusPrivateKeyParameters(this.params, array);
    }
    
    protected WOTSPlusPublicKeyParameters getPublicKey(OTSHashAddress otsHashAddress) {
        if (otsHashAddress != null) {
            final byte[][] array = new byte[this.params.getLen()][];
            for (int i = 0; i < this.params.getLen(); ++i) {
                otsHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)((XMSSAddress.Builder<OTSHashAddress.Builder>)new OTSHashAddress.Builder()).withLayerAddress(otsHashAddress.getLayerAddress())).withTreeAddress(otsHashAddress.getTreeAddress())).withOTSAddress(otsHashAddress.getOTSAddress()).withChainAddress(i).withHashAddress(otsHashAddress.getHashAddress()).withKeyAndMask(otsHashAddress.getKeyAndMask())).build();
                array[i] = this.chain(this.expandSecretKeySeed(i), 0, this.params.getWinternitzParameter() - 1, otsHashAddress);
            }
            return new WOTSPlusPublicKeyParameters(this.params, array);
        }
        throw new NullPointerException("otsHashAddress == null");
    }
    
    protected WOTSPlusPublicKeyParameters getPublicKeyFromSignature(final byte[] array, final WOTSPlusSignature wotsPlusSignature, OTSHashAddress otsHashAddress) {
        if (array == null) {
            throw new NullPointerException("messageDigest == null");
        }
        if (array.length != this.params.getDigestSize()) {
            throw new IllegalArgumentException("size of messageDigest needs to be equal to size of digest");
        }
        if (wotsPlusSignature == null) {
            throw new NullPointerException("signature == null");
        }
        if (otsHashAddress != null) {
            final List<Integer> convertToBaseW = this.convertToBaseW(array, this.params.getWinternitzParameter(), this.params.getLen1());
            final int n = 0;
            int i = 0;
            int n2 = 0;
            while (i < this.params.getLen1()) {
                n2 += this.params.getWinternitzParameter() - 1 - convertToBaseW.get(i);
                ++i;
            }
            final int len2 = this.params.getLen2();
            final int log2 = XMSSUtil.log2(this.params.getWinternitzParameter());
            final double n3 = this.params.getLen2() * XMSSUtil.log2(this.params.getWinternitzParameter());
            Double.isNaN(n3);
            convertToBaseW.addAll(this.convertToBaseW(XMSSUtil.toBytesBigEndian(n2 << 8 - len2 * log2 % 8, (int)Math.ceil(n3 / 8.0)), this.params.getWinternitzParameter(), this.params.getLen2()));
            final byte[][] array2 = new byte[this.params.getLen()][];
            for (int j = n; j < this.params.getLen(); ++j) {
                otsHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)((XMSSAddress.Builder<OTSHashAddress.Builder>)new OTSHashAddress.Builder()).withLayerAddress(otsHashAddress.getLayerAddress())).withTreeAddress(otsHashAddress.getTreeAddress())).withOTSAddress(otsHashAddress.getOTSAddress()).withChainAddress(j).withHashAddress(otsHashAddress.getHashAddress()).withKeyAndMask(otsHashAddress.getKeyAndMask())).build();
                array2[j] = this.chain(wotsPlusSignature.toByteArray()[j], convertToBaseW.get(j), this.params.getWinternitzParameter() - 1 - convertToBaseW.get(j), otsHashAddress);
            }
            return new WOTSPlusPublicKeyParameters(this.params, array2);
        }
        throw new NullPointerException("otsHashAddress == null");
    }
    
    protected byte[] getPublicSeed() {
        return XMSSUtil.cloneArray(this.publicSeed);
    }
    
    protected byte[] getSecretKeySeed() {
        return XMSSUtil.cloneArray(this.getSecretKeySeed());
    }
    
    protected byte[] getWOTSPlusSecretKey(final byte[] array, OTSHashAddress otsHashAddress) {
        otsHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)((XMSSAddress.Builder<OTSHashAddress.Builder>)new OTSHashAddress.Builder()).withLayerAddress(otsHashAddress.getLayerAddress())).withTreeAddress(otsHashAddress.getTreeAddress())).withOTSAddress(otsHashAddress.getOTSAddress()).build();
        return this.khf.PRF(array, otsHashAddress.toByteArray());
    }
    
    void importKeys(final byte[] secretKeySeed, final byte[] publicSeed) {
        if (secretKeySeed == null) {
            throw new NullPointerException("secretKeySeed == null");
        }
        if (secretKeySeed.length != this.params.getDigestSize()) {
            throw new IllegalArgumentException("size of secretKeySeed needs to be equal to size of digest");
        }
        if (publicSeed == null) {
            throw new NullPointerException("publicSeed == null");
        }
        if (publicSeed.length == this.params.getDigestSize()) {
            this.secretKeySeed = secretKeySeed;
            this.publicSeed = publicSeed;
            return;
        }
        throw new IllegalArgumentException("size of publicSeed needs to be equal to size of digest");
    }
    
    protected WOTSPlusSignature sign(final byte[] array, OTSHashAddress otsHashAddress) {
        if (array == null) {
            throw new NullPointerException("messageDigest == null");
        }
        if (array.length != this.params.getDigestSize()) {
            throw new IllegalArgumentException("size of messageDigest needs to be equal to size of digest");
        }
        if (otsHashAddress != null) {
            final List<Integer> convertToBaseW = this.convertToBaseW(array, this.params.getWinternitzParameter(), this.params.getLen1());
            int i = 0;
            int n = 0;
            while (i < this.params.getLen1()) {
                n += this.params.getWinternitzParameter() - 1 - convertToBaseW.get(i);
                ++i;
            }
            final int len2 = this.params.getLen2();
            final int log2 = XMSSUtil.log2(this.params.getWinternitzParameter());
            final double n2 = this.params.getLen2() * XMSSUtil.log2(this.params.getWinternitzParameter());
            Double.isNaN(n2);
            convertToBaseW.addAll(this.convertToBaseW(XMSSUtil.toBytesBigEndian(n << 8 - len2 * log2 % 8, (int)Math.ceil(n2 / 8.0)), this.params.getWinternitzParameter(), this.params.getLen2()));
            final byte[][] array2 = new byte[this.params.getLen()][];
            for (int j = 0; j < this.params.getLen(); ++j) {
                otsHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)((XMSSAddress.Builder<OTSHashAddress.Builder>)new OTSHashAddress.Builder()).withLayerAddress(otsHashAddress.getLayerAddress())).withTreeAddress(otsHashAddress.getTreeAddress())).withOTSAddress(otsHashAddress.getOTSAddress()).withChainAddress(j).withHashAddress(otsHashAddress.getHashAddress()).withKeyAndMask(otsHashAddress.getKeyAndMask())).build();
                array2[j] = this.chain(this.expandSecretKeySeed(j), 0, convertToBaseW.get(j), otsHashAddress);
            }
            return new WOTSPlusSignature(this.params, array2);
        }
        throw new NullPointerException("otsHashAddress == null");
    }
    
    protected boolean verifySignature(final byte[] array, final WOTSPlusSignature wotsPlusSignature, final OTSHashAddress otsHashAddress) {
        if (array == null) {
            throw new NullPointerException("messageDigest == null");
        }
        if (array.length != this.params.getDigestSize()) {
            throw new IllegalArgumentException("size of messageDigest needs to be equal to size of digest");
        }
        if (wotsPlusSignature == null) {
            throw new NullPointerException("signature == null");
        }
        if (otsHashAddress != null) {
            return XMSSUtil.areEqual(this.getPublicKeyFromSignature(array, wotsPlusSignature, otsHashAddress).toByteArray(), this.getPublicKey(otsHashAddress).toByteArray());
        }
        throw new NullPointerException("otsHashAddress == null");
    }
}
