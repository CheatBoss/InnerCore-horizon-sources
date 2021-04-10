package java.time.format;

import java.text.*;
import java.util.*;
import java.util.concurrent.*;

public final class DecimalStyle
{
    public static final DecimalStyle STANDARD;
    private static final ConcurrentMap<Locale, DecimalStyle> CACHE;
    private final char zeroDigit;
    private final char positiveSign;
    private final char negativeSign;
    private final char decimalSeparator;
    
    public static Set<Locale> getAvailableLocales() {
        final Locale[] availableLocales = DecimalFormatSymbols.getAvailableLocales();
        final HashSet<Object> set = new HashSet<Object>(availableLocales.length);
        Collections.addAll(set, availableLocales);
        return (Set<Locale>)set;
    }
    
    public static DecimalStyle ofDefaultLocale() {
        return of(Locale.getDefault(Locale.Category.FORMAT));
    }
    
    public static DecimalStyle of(final Locale locale) {
        Objects.requireNonNull(locale, "locale");
        DecimalStyle decimalStyle = DecimalStyle.CACHE.get(locale);
        if (decimalStyle == null) {
            DecimalStyle.CACHE.putIfAbsent(locale, create(locale));
            decimalStyle = DecimalStyle.CACHE.get(locale);
        }
        return decimalStyle;
    }
    
    private static DecimalStyle create(final Locale locale) {
        final DecimalFormatSymbols instance = DecimalFormatSymbols.getInstance(locale);
        final char zeroDigit = instance.getZeroDigit();
        final char c = '+';
        final char minusSign = instance.getMinusSign();
        final char decimalSeparator = instance.getDecimalSeparator();
        if (zeroDigit == '0' && minusSign == '-' && decimalSeparator == '.') {
            return DecimalStyle.STANDARD;
        }
        return new DecimalStyle(zeroDigit, c, minusSign, decimalSeparator);
    }
    
    private DecimalStyle(final char zeroDigit, final char positiveSign, final char negativeSign, final char decimalSeparator) {
        this.zeroDigit = zeroDigit;
        this.positiveSign = positiveSign;
        this.negativeSign = negativeSign;
        this.decimalSeparator = decimalSeparator;
    }
    
    public char getZeroDigit() {
        return this.zeroDigit;
    }
    
    public DecimalStyle withZeroDigit(final char c) {
        if (c == this.zeroDigit) {
            return this;
        }
        return new DecimalStyle(c, this.positiveSign, this.negativeSign, this.decimalSeparator);
    }
    
    public char getPositiveSign() {
        return this.positiveSign;
    }
    
    public DecimalStyle withPositiveSign(final char c) {
        if (c == this.positiveSign) {
            return this;
        }
        return new DecimalStyle(this.zeroDigit, c, this.negativeSign, this.decimalSeparator);
    }
    
    public char getNegativeSign() {
        return this.negativeSign;
    }
    
    public DecimalStyle withNegativeSign(final char c) {
        if (c == this.negativeSign) {
            return this;
        }
        return new DecimalStyle(this.zeroDigit, this.positiveSign, c, this.decimalSeparator);
    }
    
    public char getDecimalSeparator() {
        return this.decimalSeparator;
    }
    
    public DecimalStyle withDecimalSeparator(final char c) {
        if (c == this.decimalSeparator) {
            return this;
        }
        return new DecimalStyle(this.zeroDigit, this.positiveSign, this.negativeSign, c);
    }
    
    int convertToDigit(final char c) {
        final int n = c - this.zeroDigit;
        return (n >= 0 && n <= 9) ? n : -1;
    }
    
    String convertNumberToI18N(final String s) {
        if (this.zeroDigit == '0') {
            return s;
        }
        final int n = this.zeroDigit - '0';
        final char[] charArray = s.toCharArray();
        for (int i = 0; i < charArray.length; ++i) {
            charArray[i] += (char)n;
        }
        return new String(charArray);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof DecimalStyle) {
            final DecimalStyle decimalStyle = (DecimalStyle)o;
            return this.zeroDigit == decimalStyle.zeroDigit && this.positiveSign == decimalStyle.positiveSign && this.negativeSign == decimalStyle.negativeSign && this.decimalSeparator == decimalStyle.decimalSeparator;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.zeroDigit + this.positiveSign + this.negativeSign + this.decimalSeparator;
    }
    
    @Override
    public String toString() {
        return "DecimalStyle[" + this.zeroDigit + this.positiveSign + this.negativeSign + this.decimalSeparator + "]";
    }
    
    static {
        STANDARD = new DecimalStyle('0', '+', '-', '.');
        CACHE = new ConcurrentHashMap<Locale, DecimalStyle>(16, 0.75f, 2);
    }
}
