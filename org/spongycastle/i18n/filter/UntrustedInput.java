package org.spongycastle.i18n.filter;

public class UntrustedInput
{
    protected Object input;
    
    public UntrustedInput(final Object input) {
        this.input = input;
    }
    
    public Object getInput() {
        return this.input;
    }
    
    public String getString() {
        return this.input.toString();
    }
    
    @Override
    public String toString() {
        return this.input.toString();
    }
}
