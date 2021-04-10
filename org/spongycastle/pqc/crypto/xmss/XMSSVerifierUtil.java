package org.spongycastle.pqc.crypto.xmss;

class XMSSVerifierUtil
{
    static XMSSNode getRootNodeFromSignature(final WOTSPlus wotsPlus, final int n, final byte[] array, final XMSSReducedSignature xmssReducedSignature, final OTSHashAddress otsHashAddress, final int n2) {
        if (array.length != wotsPlus.getParams().getDigestSize()) {
            throw new IllegalArgumentException("size of messageDigest needs to be equal to size of digest");
        }
        if (xmssReducedSignature == null) {
            throw new NullPointerException("signature == null");
        }
        if (otsHashAddress != null) {
            final LTreeAddress lTreeAddress = (LTreeAddress)((LTreeAddress.Builder)((XMSSAddress.Builder<LTreeAddress.Builder>)((XMSSAddress.Builder<LTreeAddress.Builder>)new LTreeAddress.Builder()).withLayerAddress(otsHashAddress.getLayerAddress())).withTreeAddress(otsHashAddress.getTreeAddress())).withLTreeAddress(otsHashAddress.getOTSAddress()).build();
            final HashTreeAddress hashTreeAddress = (HashTreeAddress)((HashTreeAddress.Builder)((XMSSAddress.Builder<HashTreeAddress.Builder>)((XMSSAddress.Builder<HashTreeAddress.Builder>)new HashTreeAddress.Builder()).withLayerAddress(otsHashAddress.getLayerAddress())).withTreeAddress(otsHashAddress.getTreeAddress())).withTreeIndex(otsHashAddress.getOTSAddress()).build();
            final XMSSNode[] array2 = { XMSSNodeUtil.lTree(wotsPlus, wotsPlus.getPublicKeyFromSignature(array, xmssReducedSignature.getWOTSPlusSignature(), otsHashAddress), lTreeAddress), null };
            int i = 0;
            HashTreeAddress hashTreeAddress2 = hashTreeAddress;
            while (i < n) {
                final HashTreeAddress hashTreeAddress3 = (HashTreeAddress)((HashTreeAddress.Builder)((XMSSAddress.Builder<HashTreeAddress.Builder>)((HashTreeAddress.Builder)((XMSSAddress.Builder<HashTreeAddress.Builder>)((XMSSAddress.Builder<HashTreeAddress.Builder>)new HashTreeAddress.Builder()).withLayerAddress(hashTreeAddress2.getLayerAddress())).withTreeAddress(hashTreeAddress2.getTreeAddress())).withTreeHeight(i).withTreeIndex(hashTreeAddress2.getTreeIndex())).withKeyAndMask(hashTreeAddress2.getKeyAndMask())).build();
                if (Math.floor(n2 / (1 << i)) % 2.0 == 0.0) {
                    hashTreeAddress2 = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress3.getLayerAddress())).withTreeAddress(hashTreeAddress3.getTreeAddress())).withTreeHeight(hashTreeAddress3.getTreeHeight()).withTreeIndex(hashTreeAddress3.getTreeIndex() / 2).withKeyAndMask(hashTreeAddress3.getKeyAndMask())).build();
                    array2[1] = XMSSNodeUtil.randomizeHash(wotsPlus, array2[0], xmssReducedSignature.getAuthPath().get(i), hashTreeAddress2);
                    array2[1] = new XMSSNode(array2[1].getHeight() + 1, array2[1].getValue());
                }
                else {
                    hashTreeAddress2 = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress3.getLayerAddress())).withTreeAddress(hashTreeAddress3.getTreeAddress())).withTreeHeight(hashTreeAddress3.getTreeHeight()).withTreeIndex((hashTreeAddress3.getTreeIndex() - 1) / 2).withKeyAndMask(hashTreeAddress3.getKeyAndMask())).build();
                    array2[1] = XMSSNodeUtil.randomizeHash(wotsPlus, xmssReducedSignature.getAuthPath().get(i), array2[0], hashTreeAddress2);
                    array2[1] = new XMSSNode(array2[1].getHeight() + 1, array2[1].getValue());
                }
                array2[0] = array2[1];
                ++i;
            }
            return array2[0];
        }
        throw new NullPointerException("otsHashAddress == null");
    }
}
