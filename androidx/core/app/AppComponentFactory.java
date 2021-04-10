package androidx.core.app;

import androidx.annotation.*;
import java.lang.reflect.*;
import android.content.*;
import android.app.*;

@RequiresApi(28)
public class AppComponentFactory extends android.app.AppComponentFactory
{
    public final Activity instantiateActivity(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateActivityCompat(classLoader, s, intent));
    }
    
    @NonNull
    public Activity instantiateActivityCompat(@NonNull final ClassLoader classLoader, @NonNull final String s, @Nullable final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            return (Activity)Class.forName(s, false, classLoader).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (InvocationTargetException | NoSuchMethodException ex) {
            final Object o;
            throw new RuntimeException("Couldn't call constructor", (Throwable)o);
        }
    }
    
    public final Application instantiateApplication(final ClassLoader classLoader, final String s) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateApplicationCompat(classLoader, s));
    }
    
    @NonNull
    public Application instantiateApplicationCompat(@NonNull final ClassLoader classLoader, @NonNull final String s) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            return (Application)Class.forName(s, false, classLoader).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (InvocationTargetException | NoSuchMethodException ex) {
            final Object o;
            throw new RuntimeException("Couldn't call constructor", (Throwable)o);
        }
    }
    
    public final ContentProvider instantiateProvider(final ClassLoader classLoader, final String s) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateProviderCompat(classLoader, s));
    }
    
    @NonNull
    public ContentProvider instantiateProviderCompat(@NonNull final ClassLoader classLoader, @NonNull final String s) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            return (ContentProvider)Class.forName(s, false, classLoader).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (InvocationTargetException | NoSuchMethodException ex) {
            final Object o;
            throw new RuntimeException("Couldn't call constructor", (Throwable)o);
        }
    }
    
    public final BroadcastReceiver instantiateReceiver(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateReceiverCompat(classLoader, s, intent));
    }
    
    @NonNull
    public BroadcastReceiver instantiateReceiverCompat(@NonNull final ClassLoader classLoader, @NonNull final String s, @Nullable final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            return (BroadcastReceiver)Class.forName(s, false, classLoader).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (InvocationTargetException | NoSuchMethodException ex) {
            final Object o;
            throw new RuntimeException("Couldn't call constructor", (Throwable)o);
        }
    }
    
    public final Service instantiateService(final ClassLoader classLoader, final String s, final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return CoreComponentFactory.checkCompatWrapper(this.instantiateServiceCompat(classLoader, s, intent));
    }
    
    @NonNull
    public Service instantiateServiceCompat(@NonNull final ClassLoader classLoader, @NonNull final String s, @Nullable final Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        try {
            return (Service)Class.forName(s, false, classLoader).getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (InvocationTargetException | NoSuchMethodException ex) {
            final Object o;
            throw new RuntimeException("Couldn't call constructor", (Throwable)o);
        }
    }
}
