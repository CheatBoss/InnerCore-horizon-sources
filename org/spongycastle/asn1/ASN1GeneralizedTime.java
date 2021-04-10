package org.spongycastle.asn1;

import java.text.*;
import java.util.*;
import org.spongycastle.util.*;
import java.io.*;

public class ASN1GeneralizedTime extends ASN1Primitive
{
    private byte[] time;
    
    public ASN1GeneralizedTime(final String s) {
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
    
    public ASN1GeneralizedTime(final Date date) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        this.time = Strings.toByteArray(simpleDateFormat.format(date));
    }
    
    public ASN1GeneralizedTime(final Date date, final Locale locale) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'", locale);
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        this.time = Strings.toByteArray(simpleDateFormat.format(date));
    }
    
    ASN1GeneralizedTime(final byte[] time) {
        this.time = time;
    }
    
    private String calculateGMTOffset() {
        final TimeZone default1 = TimeZone.getDefault();
        int rawOffset = default1.getRawOffset();
        String s;
        if (rawOffset < 0) {
            rawOffset = -rawOffset;
            s = "-";
        }
        else {
            s = "+";
        }
        final int n = rawOffset / 3600000;
        final int n2 = (rawOffset - n * 60 * 60 * 1000) / 60000;
        int n3 = n;
        try {
            if (default1.useDaylightTime()) {
                n3 = n;
                if (default1.inDaylightTime(this.getDate())) {
                    int n4;
                    if (s.equals("+")) {
                        n4 = 1;
                    }
                    else {
                        n4 = -1;
                    }
                    n3 = n + n4;
                }
            }
        }
        catch (ParseException ex) {
            n3 = n;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("GMT");
        sb.append(s);
        sb.append(this.convert(n3));
        sb.append(":");
        sb.append(this.convert(n2));
        return sb.toString();
    }
    
    private String convert(final int n) {
        if (n < 10) {
            final StringBuilder sb = new StringBuilder();
            sb.append("0");
            sb.append(n);
            return sb.toString();
        }
        return Integer.toString(n);
    }
    
    public static ASN1GeneralizedTime getInstance(final Object o) {
        if (o != null && !(o instanceof ASN1GeneralizedTime)) {
            if (o instanceof byte[]) {
                try {
                    return (ASN1GeneralizedTime)ASN1Primitive.fromByteArray((byte[])o);
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
        return (ASN1GeneralizedTime)o;
    }
    
    public static ASN1GeneralizedTime getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof ASN1GeneralizedTime)) {
            return new ASN1GeneralizedTime(((ASN1OctetString)object).getOctets());
        }
        return getInstance(object);
    }
    
    private boolean hasFractionalSeconds() {
        int n = 0;
        while (true) {
            final byte[] time = this.time;
            if (n == time.length) {
                return false;
            }
            if (time[n] == 46 && n == 14) {
                return true;
            }
            ++n;
        }
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        return asn1Primitive instanceof ASN1GeneralizedTime && Arrays.areEqual(this.time, ((ASN1GeneralizedTime)asn1Primitive).time);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.writeEncoded(24, this.time);
    }
    
    @Override
    int encodedLength() {
        final int length = this.time.length;
        return StreamUtil.calculateBodyLength(length) + 1 + length;
    }
    
    public Date getDate() throws ParseException {
        String s = Strings.fromByteArray(this.time);
        SimpleDateFormat simpleDateFormat;
        SimpleTimeZone timeZone;
        if (s.endsWith("Z")) {
            if (this.hasFractionalSeconds()) {
                simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS'Z'");
            }
            else {
                simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
            }
            timeZone = new SimpleTimeZone(0, "Z");
        }
        else if (s.indexOf(45) <= 0 && s.indexOf(43) <= 0) {
            if (this.hasFractionalSeconds()) {
                simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
            }
            else {
                simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            }
            timeZone = new SimpleTimeZone(0, TimeZone.getDefault().getID());
        }
        else {
            s = this.getTime();
            if (this.hasFractionalSeconds()) {
                simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSSz");
            }
            else {
                simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssz");
            }
            timeZone = new SimpleTimeZone(0, "Z");
        }
        simpleDateFormat.setTimeZone(timeZone);
        String string = s;
        if (this.hasFractionalSeconds()) {
            String substring;
            int i;
            char char1;
            for (substring = s.substring(14), i = 1; i < substring.length(); ++i) {
                char1 = substring.charAt(i);
                if ('0' > char1) {
                    break;
                }
                if (char1 > '9') {
                    break;
                }
            }
            final int n = i - 1;
            String s2;
            StringBuilder sb2;
            if (n > 3) {
                final StringBuilder sb = new StringBuilder();
                sb.append(substring.substring(0, 4));
                sb.append(substring.substring(i));
                s2 = sb.toString();
                sb2 = new StringBuilder();
            }
            else if (n == 1) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(substring.substring(0, i));
                sb3.append("00");
                sb3.append(substring.substring(i));
                s2 = sb3.toString();
                sb2 = new StringBuilder();
            }
            else {
                string = s;
                if (n != 2) {
                    return simpleDateFormat.parse(string);
                }
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(substring.substring(0, i));
                sb4.append("0");
                sb4.append(substring.substring(i));
                s2 = sb4.toString();
                sb2 = new StringBuilder();
            }
            sb2.append(s.substring(0, 14));
            sb2.append(s2);
            string = sb2.toString();
        }
        return simpleDateFormat.parse(string);
    }
    
    public String getTime() {
        final String fromByteArray = Strings.fromByteArray(this.time);
        if (fromByteArray.charAt(fromByteArray.length() - 1) == 'Z') {
            final StringBuilder sb = new StringBuilder();
            sb.append(fromByteArray.substring(0, fromByteArray.length() - 1));
            sb.append("GMT+00:00");
            return sb.toString();
        }
        final int n = fromByteArray.length() - 5;
        final char char1 = fromByteArray.charAt(n);
        if (char1 == '-' || char1 == '+') {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(fromByteArray.substring(0, n));
            sb2.append("GMT");
            final int n2 = n + 3;
            sb2.append(fromByteArray.substring(n, n2));
            sb2.append(":");
            sb2.append(fromByteArray.substring(n2));
            return sb2.toString();
        }
        final int n3 = fromByteArray.length() - 3;
        final char char2 = fromByteArray.charAt(n3);
        if (char2 != '-' && char2 != '+') {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(fromByteArray);
            sb3.append(this.calculateGMTOffset());
            return sb3.toString();
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(fromByteArray.substring(0, n3));
        sb4.append("GMT");
        sb4.append(fromByteArray.substring(n3));
        sb4.append(":00");
        return sb4.toString();
    }
    
    public String getTimeString() {
        return Strings.fromByteArray(this.time);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.time);
    }
    
    @Override
    boolean isConstructed() {
        return false;
    }
}
