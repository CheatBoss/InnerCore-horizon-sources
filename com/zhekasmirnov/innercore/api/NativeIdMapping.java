package com.zhekasmirnov.innercore.api;

public class NativeIdMapping
{
    public static native int getItemNumericId(final String p0);
    
    public static native String getItemStringId(final int p0);
    
    public static int getMaxData() {
        return 16;
    }
    
    public static void iterateMetadata(final int n, final Object o, final IIdIterator idIterator) {
        if (o instanceof Number && ((Number)o).intValue() != -1) {
            idIterator.onIdDataIterated(n, ((Number)o).intValue());
            return;
        }
        for (int i = 0; i < getMaxData(); ++i) {
            idIterator.onIdDataIterated(n, i);
        }
    }
    
    public interface IIdIterator
    {
        void onIdDataIterated(final int p0, final int p1);
    }
}
