package org.spongycastle.pqc.crypto.xmss;

class XMSSNodeUtil
{
    static XMSSNode lTree(final WOTSPlus wotsPlus, final WOTSPlusPublicKeyParameters wotsPlusPublicKeyParameters, final LTreeAddress lTreeAddress) {
        if (wotsPlusPublicKeyParameters == null) {
            throw new NullPointerException("publicKey == null");
        }
        if (lTreeAddress != null) {
            final int len = wotsPlus.getParams().getLen();
            final byte[][] byteArray = wotsPlusPublicKeyParameters.toByteArray();
            final XMSSNode[] array = new XMSSNode[byteArray.length];
            for (int i = 0; i < byteArray.length; ++i) {
                array[i] = new XMSSNode(0, byteArray[i]);
            }
            LTreeAddress.Builder withTreeIndex = ((LTreeAddress.Builder)((XMSSAddress.Builder<LTreeAddress.Builder>)((XMSSAddress.Builder<LTreeAddress.Builder>)new LTreeAddress.Builder()).withLayerAddress(lTreeAddress.getLayerAddress())).withTreeAddress(lTreeAddress.getTreeAddress())).withLTreeAddress(lTreeAddress.getLTreeAddress()).withTreeHeight(0).withTreeIndex(lTreeAddress.getTreeIndex());
            int n = lTreeAddress.getKeyAndMask();
            int n2 = len;
            while (true) {
                LTreeAddress lTreeAddress2 = (LTreeAddress)((XMSSAddress.Builder<LTreeAddress.Builder>)withTreeIndex).withKeyAndMask(n).build();
                if (n2 <= 1) {
                    break;
                }
                int n3 = 0;
                double n4;
                while (true) {
                    n4 = n2 / 2;
                    if (n3 >= (int)Math.floor(n4)) {
                        break;
                    }
                    lTreeAddress2 = (LTreeAddress)((LTreeAddress.Builder)((LTreeAddress.Builder)((XMSSAddress.Builder<LTreeAddress.Builder>)((XMSSAddress.Builder<LTreeAddress.Builder>)new LTreeAddress.Builder()).withLayerAddress(lTreeAddress2.getLayerAddress())).withTreeAddress(lTreeAddress2.getTreeAddress())).withLTreeAddress(lTreeAddress2.getLTreeAddress()).withTreeHeight(lTreeAddress2.getTreeHeight()).withTreeIndex(n3).withKeyAndMask(lTreeAddress2.getKeyAndMask())).build();
                    final int n5 = n3 * 2;
                    array[n3] = randomizeHash(wotsPlus, array[n5], array[n5 + 1], lTreeAddress2);
                    ++n3;
                }
                if (n2 % 2 == 1) {
                    array[(int)Math.floor(n4)] = array[n2 - 1];
                }
                final double n6 = n2;
                Double.isNaN(n6);
                n2 = (int)Math.ceil(n6 / 2.0);
                final LTreeAddress.Builder withTreeIndex2 = ((LTreeAddress.Builder)((XMSSAddress.Builder<LTreeAddress.Builder>)((XMSSAddress.Builder<LTreeAddress.Builder>)new LTreeAddress.Builder()).withLayerAddress(lTreeAddress2.getLayerAddress())).withTreeAddress(lTreeAddress2.getTreeAddress())).withLTreeAddress(lTreeAddress2.getLTreeAddress()).withTreeHeight(lTreeAddress2.getTreeHeight() + 1).withTreeIndex(lTreeAddress2.getTreeIndex());
                n = lTreeAddress2.getKeyAndMask();
                withTreeIndex = withTreeIndex2;
            }
            return array[0];
        }
        throw new NullPointerException("address == null");
    }
    
    static XMSSNode randomizeHash(final WOTSPlus wotsPlus, final XMSSNode xmssNode, final XMSSNode xmssNode2, XMSSAddress xmssAddress) {
        if (xmssNode == null) {
            throw new NullPointerException("left == null");
        }
        if (xmssNode2 == null) {
            throw new NullPointerException("right == null");
        }
        if (xmssNode.getHeight() != xmssNode2.getHeight()) {
            throw new IllegalStateException("height of both nodes must be equal");
        }
        if (xmssAddress != null) {
            final byte[] publicSeed = wotsPlus.getPublicSeed();
            final boolean b = xmssAddress instanceof LTreeAddress;
            final int n = 0;
            XMSSAddress xmssAddress2;
            if (b) {
                final LTreeAddress lTreeAddress = (LTreeAddress)xmssAddress;
                xmssAddress2 = ((LTreeAddress.Builder)((XMSSAddress.Builder<LTreeAddress.Builder>)((LTreeAddress.Builder)((XMSSAddress.Builder<LTreeAddress.Builder>)((XMSSAddress.Builder<LTreeAddress.Builder>)new LTreeAddress.Builder()).withLayerAddress(lTreeAddress.getLayerAddress())).withTreeAddress(lTreeAddress.getTreeAddress())).withLTreeAddress(lTreeAddress.getLTreeAddress()).withTreeHeight(lTreeAddress.getTreeHeight()).withTreeIndex(lTreeAddress.getTreeIndex())).withKeyAndMask(0)).build();
            }
            else {
                xmssAddress2 = xmssAddress;
                if (xmssAddress instanceof HashTreeAddress) {
                    final HashTreeAddress hashTreeAddress = (HashTreeAddress)xmssAddress;
                    xmssAddress2 = ((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress())).withTreeHeight(hashTreeAddress.getTreeHeight()).withTreeIndex(hashTreeAddress.getTreeIndex()).withKeyAndMask(0)).build();
                }
            }
            final byte[] prf = wotsPlus.getKhf().PRF(publicSeed, xmssAddress2.toByteArray());
            if (xmssAddress2 instanceof LTreeAddress) {
                final LTreeAddress lTreeAddress2 = (LTreeAddress)xmssAddress2;
                xmssAddress = ((LTreeAddress.Builder)((LTreeAddress.Builder)((LTreeAddress.Builder)new LTreeAddress.Builder().withLayerAddress(lTreeAddress2.getLayerAddress())).withTreeAddress(lTreeAddress2.getTreeAddress())).withLTreeAddress(lTreeAddress2.getLTreeAddress()).withTreeHeight(lTreeAddress2.getTreeHeight()).withTreeIndex(lTreeAddress2.getTreeIndex()).withKeyAndMask(1)).build();
            }
            else {
                xmssAddress = xmssAddress2;
                if (xmssAddress2 instanceof HashTreeAddress) {
                    final HashTreeAddress hashTreeAddress2 = (HashTreeAddress)xmssAddress2;
                    xmssAddress = ((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress2.getLayerAddress())).withTreeAddress(hashTreeAddress2.getTreeAddress())).withTreeHeight(hashTreeAddress2.getTreeHeight()).withTreeIndex(hashTreeAddress2.getTreeIndex()).withKeyAndMask(1)).build();
                }
            }
            final byte[] prf2 = wotsPlus.getKhf().PRF(publicSeed, xmssAddress.toByteArray());
            XMSSAddress xmssAddress3;
            if (xmssAddress instanceof LTreeAddress) {
                final LTreeAddress lTreeAddress3 = (LTreeAddress)xmssAddress;
                xmssAddress3 = ((LTreeAddress.Builder)((XMSSAddress.Builder<LTreeAddress.Builder>)((LTreeAddress.Builder)((XMSSAddress.Builder<LTreeAddress.Builder>)((XMSSAddress.Builder<LTreeAddress.Builder>)new LTreeAddress.Builder()).withLayerAddress(lTreeAddress3.getLayerAddress())).withTreeAddress(lTreeAddress3.getTreeAddress())).withLTreeAddress(lTreeAddress3.getLTreeAddress()).withTreeHeight(lTreeAddress3.getTreeHeight()).withTreeIndex(lTreeAddress3.getTreeIndex())).withKeyAndMask(2)).build();
            }
            else {
                xmssAddress3 = xmssAddress;
                if (xmssAddress instanceof HashTreeAddress) {
                    final HashTreeAddress hashTreeAddress3 = (HashTreeAddress)xmssAddress;
                    xmssAddress3 = ((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress3.getLayerAddress())).withTreeAddress(hashTreeAddress3.getTreeAddress())).withTreeHeight(hashTreeAddress3.getTreeHeight()).withTreeIndex(hashTreeAddress3.getTreeIndex()).withKeyAndMask(2)).build();
                }
            }
            final byte[] prf3 = wotsPlus.getKhf().PRF(publicSeed, xmssAddress3.toByteArray());
            final int digestSize = wotsPlus.getParams().getDigestSize();
            final byte[] array = new byte[digestSize * 2];
            int n2 = 0;
            int i;
            while (true) {
                i = n;
                if (n2 >= digestSize) {
                    break;
                }
                array[n2] = (byte)(xmssNode.getValue()[n2] ^ prf2[n2]);
                ++n2;
            }
            while (i < digestSize) {
                array[i + digestSize] = (byte)(xmssNode2.getValue()[i] ^ prf3[i]);
                ++i;
            }
            return new XMSSNode(xmssNode.getHeight(), wotsPlus.getKhf().H(prf, array));
        }
        throw new NullPointerException("address == null");
    }
}
