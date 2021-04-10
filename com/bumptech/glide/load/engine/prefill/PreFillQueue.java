package com.bumptech.glide.load.engine.prefill;

import java.util.*;

final class PreFillQueue
{
    private final Map<PreFillType, Integer> bitmapsPerType;
    private int bitmapsRemaining;
    private int keyIndex;
    private final List<PreFillType> keyList;
    
    public PreFillQueue(final Map<PreFillType, Integer> bitmapsPerType) {
        this.bitmapsPerType = bitmapsPerType;
        this.keyList = new ArrayList<PreFillType>(bitmapsPerType.keySet());
        final Iterator<Integer> iterator = bitmapsPerType.values().iterator();
        while (iterator.hasNext()) {
            this.bitmapsRemaining += iterator.next();
        }
    }
    
    public int getSize() {
        return this.bitmapsRemaining;
    }
    
    public boolean isEmpty() {
        return this.bitmapsRemaining == 0;
    }
    
    public PreFillType remove() {
        final PreFillType preFillType = this.keyList.get(this.keyIndex);
        final Integer n = this.bitmapsPerType.get(preFillType);
        if (n == 1) {
            this.bitmapsPerType.remove(preFillType);
            this.keyList.remove(this.keyIndex);
        }
        else {
            this.bitmapsPerType.put(preFillType, n - 1);
        }
        --this.bitmapsRemaining;
        int keyIndex;
        if (this.keyList.isEmpty()) {
            keyIndex = 0;
        }
        else {
            keyIndex = (this.keyIndex + 1) % this.keyList.size();
        }
        this.keyIndex = keyIndex;
        return preFillType;
    }
}
