package org.spongycastle.asn1.x509;

import java.util.*;
import org.spongycastle.asn1.*;
import java.text.*;

public class Time extends ASN1Object implements ASN1Choice
{
    ASN1Primitive time;
    
    public Time(final Date date) {
        final SimpleTimeZone timeZone = new SimpleTimeZone(0, "Z");
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        simpleDateFormat.setTimeZone(timeZone);
        final StringBuilder sb = new StringBuilder();
        sb.append(simpleDateFormat.format(date));
        sb.append("Z");
        final String string = sb.toString();
        final int int1 = Integer.parseInt(string.substring(0, 4));
        ASN1Primitive time;
        if (int1 >= 1950 && int1 <= 2049) {
            time = new DERUTCTime(string.substring(2));
        }
        else {
            time = new DERGeneralizedTime(string);
        }
        this.time = time;
    }
    
    public Time(final Date date, final Locale locale) {
        final SimpleTimeZone timeZone = new SimpleTimeZone(0, "Z");
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", locale);
        simpleDateFormat.setTimeZone(timeZone);
        final StringBuilder sb = new StringBuilder();
        sb.append(simpleDateFormat.format(date));
        sb.append("Z");
        final String string = sb.toString();
        final int int1 = Integer.parseInt(string.substring(0, 4));
        ASN1Primitive time;
        if (int1 >= 1950 && int1 <= 2049) {
            time = new DERUTCTime(string.substring(2));
        }
        else {
            time = new DERGeneralizedTime(string);
        }
        this.time = time;
    }
    
    public Time(final ASN1Primitive time) {
        if (!(time instanceof ASN1UTCTime) && !(time instanceof ASN1GeneralizedTime)) {
            throw new IllegalArgumentException("unknown object passed to Time");
        }
        this.time = time;
    }
    
    public static Time getInstance(final Object o) {
        if (o == null || o instanceof Time) {
            return (Time)o;
        }
        if (o instanceof ASN1UTCTime) {
            return new Time((ASN1Primitive)o);
        }
        if (o instanceof ASN1GeneralizedTime) {
            return new Time((ASN1Primitive)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown object in factory: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static Time getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public Date getDate() {
        try {
            if (this.time instanceof ASN1UTCTime) {
                return ((ASN1UTCTime)this.time).getAdjustedDate();
            }
            return ((ASN1GeneralizedTime)this.time).getDate();
        }
        catch (ParseException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid date string: ");
            sb.append(ex.getMessage());
            throw new IllegalStateException(sb.toString());
        }
    }
    
    public String getTime() {
        final ASN1Primitive time = this.time;
        if (time instanceof ASN1UTCTime) {
            return ((ASN1UTCTime)time).getAdjustedTime();
        }
        return ((ASN1GeneralizedTime)time).getTime();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.time;
    }
    
    @Override
    public String toString() {
        return this.getTime();
    }
}
