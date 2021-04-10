package bo.app;

import com.appboy.configuration.*;
import android.content.*;

public class p extends CachedConfigurationProvider
{
    public p(final Context context) {
        super(context);
    }
    
    public long a() {
        return this.getIntValue("com_appboy_data_flush_interval_bad_network", 60) * 1000;
    }
    
    public long b() {
        return this.getIntValue("com_appboy_data_flush_interval_good_network", 30) * 1000;
    }
    
    public long c() {
        return this.getIntValue("com_appboy_data_flush_interval_great_network", 10) * 1000;
    }
}
