package bo.app;

import com.appboy.models.outgoing.*;

public abstract class fs extends fr implements fl
{
    private AppboyProperties a;
    
    protected fs(final AppboyProperties a, final ca ca) {
        super(ca);
        this.a = a;
    }
    
    @Override
    public AppboyProperties f() {
        return this.a;
    }
}
