package com.amazon.device.iap.internal.b;

public class d extends RuntimeException
{
    private final String a;
    private final String b;
    private final String c;
    
    public d(final String a, final String b, final String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    public String a() {
        return this.a;
    }
}
