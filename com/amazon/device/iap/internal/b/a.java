package com.amazon.device.iap.internal.b;

public class a extends RuntimeException
{
    private final String a;
    private final String b;
    
    public a(final String a, final String b, final Throwable t) {
        super(t);
        this.a = a;
        this.b = b;
    }
    
    public String a() {
        return this.a;
    }
}
