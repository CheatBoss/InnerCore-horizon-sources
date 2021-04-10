package org.spongycastle.asn1.x9;

public abstract class X9ECParametersHolder
{
    private X9ECParameters params;
    
    protected abstract X9ECParameters createParameters();
    
    public X9ECParameters getParameters() {
        synchronized (this) {
            if (this.params == null) {
                this.params = this.createParameters();
            }
            return this.params;
        }
    }
}
