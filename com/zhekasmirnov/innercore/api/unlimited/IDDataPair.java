package com.zhekasmirnov.innercore.api.unlimited;

public class IDDataPair
{
    public int data;
    public int id;
    
    public IDDataPair(final int id, final int data) {
        this.id = id;
        this.data = data;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof IDDataPair;
        final boolean b2 = false;
        if (b) {
            final IDDataPair idDataPair = (IDDataPair)o;
            boolean b3 = b2;
            if (this.id == idDataPair.id) {
                b3 = b2;
                if (this.data == idDataPair.data) {
                    b3 = true;
                }
            }
            return b3;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (this.id & 0xFFFF) | (this.data & 0xFF) << 16;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.id);
        sb.append(":");
        sb.append(this.data);
        return sb.toString();
    }
}
