package com.android.dx.io.instructions;

public abstract class BaseCodeCursor implements CodeCursor
{
    private final AddressMap baseAddressMap;
    private int cursor;
    
    public BaseCodeCursor() {
        this.baseAddressMap = new AddressMap();
        this.cursor = 0;
    }
    
    protected final void advance(final int n) {
        this.cursor += n;
    }
    
    @Override
    public final int baseAddressForCursor() {
        final int value = this.baseAddressMap.get(this.cursor);
        if (value >= 0) {
            return value;
        }
        return this.cursor;
    }
    
    @Override
    public final int cursor() {
        return this.cursor;
    }
    
    @Override
    public final void setBaseAddress(final int n, final int n2) {
        this.baseAddressMap.put(n, n2);
    }
}
