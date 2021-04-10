package com.appsflyer.internal;

public final class w
{
    private boolean \u0399;
    public String \u03b9;
    
    w(final String \u03b9, final boolean \u03b92) {
        this.\u03b9 = \u03b9;
        this.\u0399 = \u03b92;
    }
    
    @Override
    public final String toString() {
        return String.format("%s,%s", this.\u03b9, this.\u0399);
    }
    
    public final boolean \u0269() {
        return this.\u0399;
    }
    
    enum e
    {
        \u0131(1), 
        \u03b9(0);
        
        private int \u0399;
        
        private e(final int \u03b9) {
            this.\u0399 = \u03b9;
        }
        
        @Override
        public final String toString() {
            return String.valueOf(this.\u0399);
        }
    }
}
