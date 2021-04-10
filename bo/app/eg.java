package bo.app;

import android.content.*;

public final class eg
{
    public static boolean a(final Context context, final Class<?> clazz) {
        return context.getPackageManager().queryIntentServices(new Intent().setClass(context, (Class)clazz), 65536).size() > 0;
    }
}
