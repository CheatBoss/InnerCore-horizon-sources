package org.spongycastle.asn1.x509;

public class X509NameTokenizer
{
    private StringBuffer buf;
    private int index;
    private char separator;
    private String value;
    
    public X509NameTokenizer(final String s) {
        this(s, ',');
    }
    
    public X509NameTokenizer(final String value, final char separator) {
        this.buf = new StringBuffer();
        this.value = value;
        this.index = -1;
        this.separator = separator;
    }
    
    public boolean hasMoreTokens() {
        return this.index != this.value.length();
    }
    
    public String nextToken() {
        if (this.index == this.value.length()) {
            return null;
        }
        int i = this.index + 1;
        this.buf.setLength(0);
        int n = 0;
        int n2 = 0;
        while (i != this.value.length()) {
            final char char1 = this.value.charAt(i);
            Label_0153: {
                int n3;
                if (char1 == '\"') {
                    n3 = n;
                    if (n2 == 0) {
                        n3 = (n ^ 0x1);
                    }
                }
                else {
                    n3 = n;
                    if (n2 == 0) {
                        if (n != 0) {
                            n3 = n;
                        }
                        else {
                            if (char1 == '\\') {
                                this.buf.append(char1);
                                n2 = 1;
                                break Label_0153;
                            }
                            if (char1 == this.separator) {
                                break;
                            }
                            this.buf.append(char1);
                            break Label_0153;
                        }
                    }
                }
                this.buf.append(char1);
                n2 = 0;
                n = n3;
            }
            ++i;
        }
        this.index = i;
        return this.buf.toString();
    }
}
