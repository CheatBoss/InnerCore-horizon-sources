package org.spongycastle.i18n.filter;

public class TrustedInput
{
    protected Object input;
    
    public TrustedInput(final Object input) {
        this.input = input;
    }
    
    public Object getInput() {
        return this.input;
    }
    
    @Override
    public String toString() {
        return this.input.toString();
    }
}
