package com.android.dex;

import com.android.dex.util.*;

public final class EncodedValue implements Comparable<EncodedValue>
{
    private final byte[] data;
    
    public EncodedValue(final byte[] data) {
        this.data = data;
    }
    
    public ByteInput asByteInput() {
        return new ByteArrayByteInput(this.data);
    }
    
    @Override
    public int compareTo(final EncodedValue encodedValue) {
        for (int min = Math.min(this.data.length, encodedValue.data.length), i = 0; i < min; ++i) {
            if (this.data[i] != encodedValue.data[i]) {
                return (this.data[i] & 0xFF) - (encodedValue.data[i] & 0xFF);
            }
        }
        return this.data.length - encodedValue.data.length;
    }
    
    public byte[] getBytes() {
        return this.data;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(this.data[0] & 0xFF));
        sb.append("...(");
        sb.append(this.data.length);
        sb.append(")");
        return sb.toString();
    }
    
    public void writeTo(final Dex.Section section) {
        section.write(this.data);
    }
}
