package org.spongycastle.asn1.eac;

import java.util.*;
import org.spongycastle.util.*;
import java.text.*;

public class PackedDate
{
    private byte[] time;
    
    public PackedDate(final String s) {
        this.time = this.convert(s);
    }
    
    public PackedDate(final Date date) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd'Z'");
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        this.time = this.convert(simpleDateFormat.format(date));
    }
    
    public PackedDate(final Date date, final Locale locale) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd'Z'", locale);
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        this.time = this.convert(simpleDateFormat.format(date));
    }
    
    PackedDate(final byte[] time) {
        this.time = time;
    }
    
    private byte[] convert(final String s) {
        final char[] charArray = s.toCharArray();
        final byte[] array = new byte[6];
        for (int i = 0; i != 6; ++i) {
            array[i] = (byte)(charArray[i] - '0');
        }
        return array;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof PackedDate && Arrays.areEqual(this.time, ((PackedDate)o).time);
    }
    
    public Date getDate() throws ParseException {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        final StringBuilder sb = new StringBuilder();
        sb.append("20");
        sb.append(this.toString());
        return simpleDateFormat.parse(sb.toString());
    }
    
    public byte[] getEncoding() {
        return Arrays.clone(this.time);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.time);
    }
    
    @Override
    public String toString() {
        final int length = this.time.length;
        final char[] array = new char[length];
        for (int i = 0; i != length; ++i) {
            array[i] = (char)((this.time[i] & 0xFF) + 48);
        }
        return new String(array);
    }
}
