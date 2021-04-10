package org.mozilla.javascript.regexp;

public class SubString
{
    public static final SubString emptySubString;
    int index;
    int length;
    String str;
    
    static {
        emptySubString = new SubString();
    }
    
    public SubString() {
    }
    
    public SubString(final String str) {
        this.str = str;
        this.index = 0;
        this.length = str.length();
    }
    
    public SubString(final String str, final int index, final int length) {
        this.str = str;
        this.index = index;
        this.length = length;
    }
    
    @Override
    public String toString() {
        if (this.str == null) {
            return "";
        }
        return this.str.substring(this.index, this.index + this.length);
    }
}
