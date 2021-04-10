package org.spongycastle.asn1;

import java.text.*;
import java.util.*;
import org.spongycastle.util.*;
import java.io.*;

public class ASN1UTCTime extends ASN1Primitive
{
    private byte[] time;
    
    public ASN1UTCTime(final String s) {
        this.time = Strings.toByteArray(s);
        try {
            this.getDate();
        }
        catch (ParseException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid date string: ");
            sb.append(ex.getMessage());
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public ASN1UTCTime(final Date date) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss'Z'");
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        this.time = Strings.toByteArray(simpleDateFormat.format(date));
    }
    
    public ASN1UTCTime(final Date date, final Locale locale) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss'Z'", locale);
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        this.time = Strings.toByteArray(simpleDateFormat.format(date));
    }
    
    ASN1UTCTime(final byte[] time) {
        this.time = time;
    }
    
    public static ASN1UTCTime getInstance(final Object o) {
        if (o != null && !(o instanceof ASN1UTCTime)) {
            if (o instanceof byte[]) {
                try {
                    return (ASN1UTCTime)ASN1Primitive.fromByteArray((byte[])o);
                }
                catch (Exception ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("encoding error in getInstance: ");
                    sb.append(ex.toString());
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("illegal object in getInstance: ");
            sb2.append(o.getClass().getName());
            throw new IllegalArgumentException(sb2.toString());
        }
        return (ASN1UTCTime)o;
    }
    
    public static ASN1UTCTime getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof ASN1UTCTime)) {
            return new ASN1UTCTime(((ASN1OctetString)object).getOctets());
        }
        return getInstance(object);
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        return asn1Primitive instanceof ASN1UTCTime && Arrays.areEqual(this.time, ((ASN1UTCTime)asn1Primitive).time);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.write(23);
        final int length = this.time.length;
        asn1OutputStream.writeLength(length);
        for (int i = 0; i != length; ++i) {
            asn1OutputStream.write(this.time[i]);
        }
    }
    
    @Override
    int encodedLength() {
        final int length = this.time.length;
        return StreamUtil.calculateBodyLength(length) + 1 + length;
    }
    
    public Date getAdjustedDate() throws ParseException {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssz");
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        return simpleDateFormat.parse(this.getAdjustedTime());
    }
    
    public String getAdjustedTime() {
        final String time = this.getTime();
        StringBuilder sb;
        String s;
        if (time.charAt(0) < '5') {
            sb = new StringBuilder();
            s = "20";
        }
        else {
            sb = new StringBuilder();
            s = "19";
        }
        sb.append(s);
        sb.append(time);
        return sb.toString();
    }
    
    public Date getDate() throws ParseException {
        return new SimpleDateFormat("yyMMddHHmmssz").parse(this.getTime());
    }
    
    public String getTime() {
        final String fromByteArray = Strings.fromByteArray(this.time);
        StringBuilder sb2;
        String s2;
        if (fromByteArray.indexOf(45) < 0 && fromByteArray.indexOf(43) < 0) {
            if (fromByteArray.length() == 11) {
                final StringBuilder sb = new StringBuilder();
                sb.append(fromByteArray.substring(0, 10));
                final String s = "00GMT+00:00";
                sb2 = sb;
                s2 = s;
            }
            else {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(fromByteArray.substring(0, 12));
                s2 = "GMT+00:00";
                sb2 = sb3;
            }
        }
        else {
            int n;
            if ((n = fromByteArray.indexOf(45)) < 0) {
                n = fromByteArray.indexOf(43);
            }
            String string = fromByteArray;
            if (n == fromByteArray.length() - 3) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(fromByteArray);
                sb4.append("00");
                string = sb4.toString();
            }
            if (n == 10) {
                sb2 = new StringBuilder();
                sb2.append(string.substring(0, 10));
                sb2.append("00GMT");
                sb2.append(string.substring(10, 13));
                sb2.append(":");
                s2 = string.substring(13, 15);
            }
            else {
                sb2 = new StringBuilder();
                sb2.append(string.substring(0, 12));
                sb2.append("GMT");
                sb2.append(string.substring(12, 15));
                sb2.append(":");
                s2 = string.substring(15, 17);
            }
        }
        sb2.append(s2);
        return sb2.toString();
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.time);
    }
    
    @Override
    boolean isConstructed() {
        return false;
    }
    
    @Override
    public String toString() {
        return Strings.fromByteArray(this.time);
    }
}
