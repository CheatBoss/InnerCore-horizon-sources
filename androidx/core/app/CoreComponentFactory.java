package androidx.core.app;

import androidx.annotation.*;
import android.content.*;
import android.app.*;

@RequiresApi(api = 28)
@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
public class CoreComponentFactory extends AppComponentFactory
{
    static <T> T checkCompatWrapper(final T t) {
        if (t instanceof CompatWrapped) {
            final Object wrapper = ((CompatWrapped)t).getWrapper();
            if (wrapper != null) {
                return (T)wrapper;
            }
        }
        return t;
    }
    
    public Activity instantiateActivity(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return checkCompatWrapper(super.instantiateActivity(classLoader, s, intent));
    }
    
    public Application instantiateApplication(final ClassLoader classLoader, final String s) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return checkCompatWrapper(super.instantiateApplication(classLoader, s));
    }
    
    public ContentProvider instantiateProvider(final ClassLoader classLoader, final String s) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return checkCompatWrapper(super.instantiateProvider(classLoader, s));
    }
    
    public BroadcastReceiver instantiateReceiver(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return checkCompatWrapper(super.instantiateReceiver(classLoader, s, intent));
    }
    
    public Service instantiateService(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return checkCompatWrapper(super.instantiateService(classLoader, s, intent));
    }
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public interface CompatWrapped
    {
        Object getWrapper();
    }
}
