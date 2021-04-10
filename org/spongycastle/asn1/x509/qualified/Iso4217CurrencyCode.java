package org.spongycastle.asn1.x509.qualified;

import org.spongycastle.asn1.*;

public class Iso4217CurrencyCode extends ASN1Object implements ASN1Choice
{
    final int ALPHABETIC_MAXSIZE;
    final int NUMERIC_MAXSIZE;
    final int NUMERIC_MINSIZE;
    int numeric;
    ASN1Encodable obj;
    
    public Iso4217CurrencyCode(final int n) {
        this.ALPHABETIC_MAXSIZE = 3;
        this.NUMERIC_MINSIZE = 1;
        this.NUMERIC_MAXSIZE = 999;
        if (n <= 999 && n >= 1) {
            this.obj = new ASN1Integer(n);
            return;
        }
        throw new IllegalArgumentException("wrong size in numeric code : not in (1..999)");
    }
    
    public Iso4217CurrencyCode(final String s) {
        this.ALPHABETIC_MAXSIZE = 3;
        this.NUMERIC_MINSIZE = 1;
        this.NUMERIC_MAXSIZE = 999;
        if (s.length() <= 3) {
            this.obj = new DERPrintableString(s);
            return;
        }
        throw new IllegalArgumentException("wrong size in alphabetic code : max size is 3");
    }
    
    public static Iso4217CurrencyCode getInstance(final Object o) {
        if (o == null || o instanceof Iso4217CurrencyCode) {
            return (Iso4217CurrencyCode)o;
        }
        if (o instanceof ASN1Integer) {
            return new Iso4217CurrencyCode(ASN1Integer.getInstance(o).getValue().intValue());
        }
        if (o instanceof DERPrintableString) {
            return new Iso4217CurrencyCode(DERPrintableString.getInstance(o).getString());
        }
        throw new IllegalArgumentException("unknown object in getInstance");
    }
    
    public String getAlphabetic() {
        return ((DERPrintableString)this.obj).getString();
    }
    
    public int getNumeric() {
        return ((ASN1Integer)this.obj).getValue().intValue();
    }
    
    public boolean isAlphabetic() {
        return this.obj instanceof DERPrintableString;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.obj.toASN1Primitive();
    }
}
