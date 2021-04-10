package androidx.core.content.pm;

import java.util.*;
import androidx.core.content.*;
import android.content.pm.*;
import android.text.*;
import androidx.annotation.*;
import android.content.*;
import android.os.*;

public class ShortcutManagerCompat
{
    @VisibleForTesting
    static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    public static final String EXTRA_SHORTCUT_ID = "android.intent.extra.shortcut.ID";
    @VisibleForTesting
    static final String INSTALL_SHORTCUT_PERMISSION = "com.android.launcher.permission.INSTALL_SHORTCUT";
    private static volatile ShortcutInfoCompatSaver<?> sShortcutInfoCompatSaver;
    
    static {
        ShortcutManagerCompat.sShortcutInfoCompatSaver = null;
    }
    
    private ShortcutManagerCompat() {
    }
    
    public static boolean addDynamicShortcuts(@NonNull final Context context, @NonNull final List<ShortcutInfoCompat> list) {
        if (Build$VERSION.SDK_INT >= 25) {
            final ArrayList<ShortcutInfo> list2 = new ArrayList<ShortcutInfo>();
            final Iterator<ShortcutInfoCompat> iterator = list.iterator();
            while (iterator.hasNext()) {
                list2.add(iterator.next().toShortcutInfo());
            }
            if (!((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).addDynamicShortcuts((List)list2)) {
                return false;
            }
        }
        getShortcutInfoSaverInstance(context).addShortcuts(list);
        return true;
    }
    
    @NonNull
    public static Intent createShortcutResultIntent(@NonNull final Context context, @NonNull final ShortcutInfoCompat shortcutInfoCompat) {
        Intent shortcutResultIntent = null;
        if (Build$VERSION.SDK_INT >= 26) {
            shortcutResultIntent = ((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).createShortcutResultIntent(shortcutInfoCompat.toShortcutInfo());
        }
        Intent intent;
        if ((intent = shortcutResultIntent) == null) {
            intent = new Intent();
        }
        return shortcutInfoCompat.addToIntent(intent);
    }
    
    @NonNull
    public static List<ShortcutInfoCompat> getDynamicShortcuts(@NonNull final Context context) {
        if (Build$VERSION.SDK_INT >= 25) {
            final List dynamicShortcuts = ((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).getDynamicShortcuts();
            final ArrayList list = new ArrayList<ShortcutInfoCompat>(dynamicShortcuts.size());
            final Iterator<ShortcutInfo> iterator = dynamicShortcuts.iterator();
            while (iterator.hasNext()) {
                list.add(new ShortcutInfoCompat.Builder(context, iterator.next()).build());
            }
            return (List<ShortcutInfoCompat>)list;
        }
        try {
            return (List<ShortcutInfoCompat>)getShortcutInfoSaverInstance(context).getShortcuts();
        }
        catch (Exception ex) {
            return new ArrayList<ShortcutInfoCompat>();
        }
    }
    
    public static int getMaxShortcutCountPerActivity(@NonNull final Context context) {
        if (Build$VERSION.SDK_INT >= 25) {
            return ((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).getMaxShortcutCountPerActivity();
        }
        return 0;
    }
    
    private static ShortcutInfoCompatSaver getShortcutInfoSaverInstance(final Context context) {
        if (ShortcutManagerCompat.sShortcutInfoCompatSaver == null) {
            if (Build$VERSION.SDK_INT >= 23) {
                try {
                    ShortcutManagerCompat.sShortcutInfoCompatSaver = (ShortcutInfoCompatSaver<?>)Class.forName("androidx.sharetarget.ShortcutInfoCompatSaverImpl", false, ShortcutManagerCompat.class.getClassLoader()).getMethod("getInstance", Context.class).invoke(null, context);
                }
                catch (Exception ex) {}
            }
            if (ShortcutManagerCompat.sShortcutInfoCompatSaver == null) {
                ShortcutManagerCompat.sShortcutInfoCompatSaver = new ShortcutInfoCompatSaver.NoopImpl();
            }
        }
        return ShortcutManagerCompat.sShortcutInfoCompatSaver;
    }
    
    public static boolean isRequestPinShortcutSupported(@NonNull final Context context) {
        if (Build$VERSION.SDK_INT >= 26) {
            return ((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).isRequestPinShortcutSupported();
        }
        if (ContextCompat.checkSelfPermission(context, "com.android.launcher.permission.INSTALL_SHORTCUT") != 0) {
            return false;
        }
        final Iterator<ResolveInfo> iterator = context.getPackageManager().queryBroadcastReceivers(new Intent("com.android.launcher.action.INSTALL_SHORTCUT"), 0).iterator();
        while (iterator.hasNext()) {
            final String permission = iterator.next().activityInfo.permission;
            if (!TextUtils.isEmpty((CharSequence)permission) && !"com.android.launcher.permission.INSTALL_SHORTCUT".equals(permission)) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    public static void removeAllDynamicShortcuts(@NonNull final Context context) {
        if (Build$VERSION.SDK_INT >= 25) {
            ((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).removeAllDynamicShortcuts();
        }
        getShortcutInfoSaverInstance(context).removeAllShortcuts();
    }
    
    public static boolean requestPinShortcut(@NonNull final Context context, @NonNull final ShortcutInfoCompat shortcutInfoCompat, @Nullable final IntentSender intentSender) {
        if (Build$VERSION.SDK_INT >= 26) {
            return ((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).requestPinShortcut(shortcutInfoCompat.toShortcutInfo(), intentSender);
        }
        if (!isRequestPinShortcutSupported(context)) {
            return false;
        }
        final Intent addToIntent = shortcutInfoCompat.addToIntent(new Intent("com.android.launcher.action.INSTALL_SHORTCUT"));
        if (intentSender == null) {
            context.sendBroadcast(addToIntent);
            return true;
        }
        context.sendOrderedBroadcast(addToIntent, (String)null, (BroadcastReceiver)new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                try {
                    intentSender.sendIntent(context, 0, (Intent)null, (IntentSender$OnFinished)null, (Handler)null);
                }
                catch (IntentSender$SendIntentException ex) {}
            }
        }, (Handler)null, -1, (String)null, (Bundle)null);
        return true;
    }
    
    public static boolean updateShortcuts(@NonNull final Context context, @NonNull final List<ShortcutInfoCompat> list) {
        if (Build$VERSION.SDK_INT >= 25) {
            final ArrayList<ShortcutInfo> list2 = new ArrayList<ShortcutInfo>();
            final Iterator<ShortcutInfoCompat> iterator = list.iterator();
            while (iterator.hasNext()) {
                list2.add(iterator.next().toShortcutInfo());
            }
            if (!((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).updateShortcuts((List)list2)) {
                return false;
            }
        }
        getShortcutInfoSaverInstance(context).addShortcuts(list);
        return true;
    }
    
    public void removeDynamicShortcuts(@NonNull final Context context, @NonNull final List<String> list) {
        if (Build$VERSION.SDK_INT >= 25) {
            ((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).removeDynamicShortcuts((List)list);
        }
        getShortcutInfoSaverInstance(context).removeShortcuts(list);
    }
}
