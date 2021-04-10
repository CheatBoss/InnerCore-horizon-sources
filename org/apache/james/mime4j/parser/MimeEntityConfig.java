package org.apache.james.mime4j.parser;

public final class MimeEntityConfig implements Cloneable
{
    private boolean countLineNumbers;
    private long maxContentLen;
    private int maxHeaderCount;
    private int maxLineLen;
    private boolean maximalBodyDescriptor;
    private boolean strictParsing;
    
    public MimeEntityConfig() {
        this.maximalBodyDescriptor = false;
        this.strictParsing = false;
        this.maxLineLen = 1000;
        this.maxHeaderCount = 1000;
        this.maxContentLen = -1L;
        this.countLineNumbers = false;
    }
    
    public MimeEntityConfig clone() {
        try {
            return (MimeEntityConfig)super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
    }
    
    public long getMaxContentLen() {
        return this.maxContentLen;
    }
    
    public int getMaxHeaderCount() {
        return this.maxHeaderCount;
    }
    
    public int getMaxLineLen() {
        return this.maxLineLen;
    }
    
    public boolean isCountLineNumbers() {
        return this.countLineNumbers;
    }
    
    public boolean isMaximalBodyDescriptor() {
        return this.maximalBodyDescriptor;
    }
    
    public boolean isStrictParsing() {
        return this.strictParsing;
    }
    
    public void setCountLineNumbers(final boolean countLineNumbers) {
        this.countLineNumbers = countLineNumbers;
    }
    
    public void setMaxContentLen(final long maxContentLen) {
        this.maxContentLen = maxContentLen;
    }
    
    public void setMaxHeaderCount(final int maxHeaderCount) {
        this.maxHeaderCount = maxHeaderCount;
    }
    
    public void setMaxLineLen(final int maxLineLen) {
        this.maxLineLen = maxLineLen;
    }
    
    public void setMaximalBodyDescriptor(final boolean maximalBodyDescriptor) {
        this.maximalBodyDescriptor = maximalBodyDescriptor;
    }
    
    public void setStrictParsing(final boolean strictParsing) {
        this.strictParsing = strictParsing;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[max body descriptor: ");
        sb.append(this.maximalBodyDescriptor);
        sb.append(", strict parsing: ");
        sb.append(this.strictParsing);
        sb.append(", max line length: ");
        sb.append(this.maxLineLen);
        sb.append(", max header count: ");
        sb.append(this.maxHeaderCount);
        sb.append(", max content length: ");
        sb.append(this.maxContentLen);
        sb.append(", count line numbers: ");
        sb.append(this.countLineNumbers);
        sb.append("]");
        return sb.toString();
    }
}
