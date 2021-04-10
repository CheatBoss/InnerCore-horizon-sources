package org.spongycastle.asn1;

public class OIDTokenizer
{
    private int index;
    private String oid;
    
    public OIDTokenizer(final String oid) {
        this.oid = oid;
        this.index = 0;
    }
    
    public boolean hasMoreTokens() {
        return this.index != -1;
    }
    
    public String nextToken() {
        final int index = this.index;
        if (index == -1) {
            return null;
        }
        final int index2 = this.oid.indexOf(46, index);
        if (index2 == -1) {
            final String substring = this.oid.substring(this.index);
            this.index = -1;
            return substring;
        }
        final String substring2 = this.oid.substring(this.index, index2);
        this.index = index2 + 1;
        return substring2;
    }
}
