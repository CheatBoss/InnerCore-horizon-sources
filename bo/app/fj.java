package bo.app;

import com.appboy.models.outgoing.*;

public class fj extends fs
{
    private String a;
    
    public fj(final String a, final AppboyProperties appboyProperties, final ca ca) {
        super(appboyProperties, ca);
        this.a = a;
    }
    
    public String a() {
        return this.a;
    }
    
    @Override
    public String b() {
        return "custom_event";
    }
}
