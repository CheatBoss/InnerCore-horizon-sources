package org.spongycastle.pqc.crypto.xmss;

import java.io.*;
import java.util.*;
import org.spongycastle.util.*;

public class BDSStateMap implements Serializable
{
    private final Map<Integer, BDS> bdsState;
    
    BDSStateMap() {
        this.bdsState = new TreeMap<Integer, BDS>();
    }
    
    BDSStateMap(final BDSStateMap bdsStateMap, final XMSSMTParameters xmssmtParameters, final long n, final byte[] array, final byte[] array2) {
        this.bdsState = new TreeMap<Integer, BDS>();
        for (final Integer n2 : bdsStateMap.bdsState.keySet()) {
            this.bdsState.put(n2, bdsStateMap.bdsState.get(n2));
        }
        this.updateState(xmssmtParameters, n, array, array2);
    }
    
    BDSStateMap(final XMSSMTParameters xmssmtParameters, final long n, final byte[] array, final byte[] array2) {
        this.bdsState = new TreeMap<Integer, BDS>();
        for (long n2 = 0L; n2 < n; ++n2) {
            this.updateState(xmssmtParameters, n2, array, array2);
        }
    }
    
    private void updateState(final XMSSMTParameters xmssmtParameters, final long n, final byte[] array, final byte[] array2) {
        final XMSSParameters xmssParameters = xmssmtParameters.getXMSSParameters();
        final int height = xmssParameters.getHeight();
        final long treeIndex = XMSSUtil.getTreeIndex(n, height);
        final int leafIndex = XMSSUtil.getLeafIndex(n, height);
        final OTSHashAddress otsHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)new OTSHashAddress.Builder()).withTreeAddress(treeIndex)).withOTSAddress(leafIndex).build();
        final boolean b = true;
        final int n2 = (1 << height) - 1;
        long treeIndex2 = treeIndex;
        int i = b ? 1 : 0;
        if (leafIndex < n2) {
            if (this.get(0) == null || leafIndex == 0) {
                this.put(0, new BDS(xmssParameters, array, array2, otsHashAddress));
            }
            this.update(0, array, array2, otsHashAddress);
            i = (b ? 1 : 0);
            treeIndex2 = treeIndex;
        }
        while (i < xmssmtParameters.getLayers()) {
            final int leafIndex2 = XMSSUtil.getLeafIndex(treeIndex2, height);
            treeIndex2 = XMSSUtil.getTreeIndex(treeIndex2, height);
            final OTSHashAddress otsHashAddress2 = (OTSHashAddress)((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)((XMSSAddress.Builder<OTSHashAddress.Builder>)new OTSHashAddress.Builder()).withLayerAddress(i)).withTreeAddress(treeIndex2)).withOTSAddress(leafIndex2).build();
            if (leafIndex2 < n2 && XMSSUtil.isNewAuthenticationPathNeeded(n, height, i)) {
                if (this.get(i) == null) {
                    this.put(i, new BDS(xmssmtParameters.getXMSSParameters(), array, array2, otsHashAddress2));
                }
                this.update(i, array, array2, otsHashAddress2);
            }
            ++i;
        }
    }
    
    public BDS get(final int n) {
        return this.bdsState.get(Integers.valueOf(n));
    }
    
    public boolean isEmpty() {
        return this.bdsState.isEmpty();
    }
    
    public void put(final int n, final BDS bds) {
        this.bdsState.put(Integers.valueOf(n), bds);
    }
    
    void setXMSS(final XMSSParameters xmss) {
        final Iterator<Integer> iterator = this.bdsState.keySet().iterator();
        while (iterator.hasNext()) {
            final BDS bds = this.bdsState.get(iterator.next());
            bds.setXMSS(xmss);
            bds.validate();
        }
    }
    
    public BDS update(final int n, final byte[] array, final byte[] array2, final OTSHashAddress otsHashAddress) {
        return this.bdsState.put(Integers.valueOf(n), this.bdsState.get(Integers.valueOf(n)).getNextState(array, array2, otsHashAddress));
    }
}
