package androidx.core.view;

import java.lang.reflect.*;
import android.app.*;
import android.content.*;
import androidx.annotation.*;
import android.view.*;
import android.os.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
public class KeyEventDispatcher
{
    private static boolean sActionBarFieldsFetched;
    private static Method sActionBarOnMenuKeyMethod;
    private static boolean sDialogFieldsFetched;
    private static Field sDialogKeyListenerField;
    
    static {
        KeyEventDispatcher.sActionBarFieldsFetched = false;
        KeyEventDispatcher.sActionBarOnMenuKeyMethod = null;
        KeyEventDispatcher.sDialogFieldsFetched = false;
        KeyEventDispatcher.sDialogKeyListenerField = null;
    }
    
    private KeyEventDispatcher() {
    }
    
    private static boolean actionBarOnMenuKeyEventPre28(final ActionBar actionBar, final KeyEvent keyEvent) {
        if (!KeyEventDispatcher.sActionBarFieldsFetched) {
            try {
                KeyEventDispatcher.sActionBarOnMenuKeyMethod = actionBar.getClass().getMethod("onMenuKeyEvent", KeyEvent.class);
            }
            catch (NoSuchMethodException ex) {}
            KeyEventDispatcher.sActionBarFieldsFetched = true;
        }
        if (KeyEventDispatcher.sActionBarOnMenuKeyMethod != null) {
            try {
                return (boolean)KeyEventDispatcher.sActionBarOnMenuKeyMethod.invoke(actionBar, keyEvent);
            }
            catch (InvocationTargetException ex2) {
                return false;
            }
            catch (IllegalAccessException ex3) {}
        }
        return false;
    }
    
    private static boolean activitySuperDispatchKeyEventPre28(final Activity activity, final KeyEvent keyEvent) {
        activity.onUserInteraction();
        final Window window = activity.getWindow();
        if (window.hasFeature(8)) {
            final ActionBar actionBar = activity.getActionBar();
            if (keyEvent.getKeyCode() == 82 && actionBar != null && actionBarOnMenuKeyEventPre28(actionBar, keyEvent)) {
                return true;
            }
        }
        if (window.superDispatchKeyEvent(keyEvent)) {
            return true;
        }
        final View decorView = window.getDecorView();
        if (ViewCompat.dispatchUnhandledKeyEventBeforeCallback(decorView, keyEvent)) {
            return true;
        }
        KeyEvent$DispatcherState keyDispatcherState;
        if (decorView != null) {
            keyDispatcherState = decorView.getKeyDispatcherState();
        }
        else {
            keyDispatcherState = null;
        }
        return keyEvent.dispatch((KeyEvent$Callback)activity, keyDispatcherState, (Object)activity);
    }
    
    private static boolean dialogSuperDispatchKeyEventPre28(final Dialog dialog, final KeyEvent keyEvent) {
        final DialogInterface$OnKeyListener dialogKeyListenerPre28 = getDialogKeyListenerPre28(dialog);
        if (dialogKeyListenerPre28 != null && dialogKeyListenerPre28.onKey((DialogInterface)dialog, keyEvent.getKeyCode(), keyEvent)) {
            return true;
        }
        final Window window = dialog.getWindow();
        if (window.superDispatchKeyEvent(keyEvent)) {
            return true;
        }
        final View decorView = window.getDecorView();
        if (ViewCompat.dispatchUnhandledKeyEventBeforeCallback(decorView, keyEvent)) {
            return true;
        }
        KeyEvent$DispatcherState keyDispatcherState;
        if (decorView != null) {
            keyDispatcherState = decorView.getKeyDispatcherState();
        }
        else {
            keyDispatcherState = null;
        }
        return keyEvent.dispatch((KeyEvent$Callback)dialog, keyDispatcherState, (Object)dialog);
    }
    
    public static boolean dispatchBeforeHierarchy(@NonNull final View view, @NonNull final KeyEvent keyEvent) {
        return ViewCompat.dispatchUnhandledKeyEventBeforeHierarchy(view, keyEvent);
    }
    
    public static boolean dispatchKeyEvent(@NonNull final Component component, @Nullable final View view, @Nullable final Window$Callback window$Callback, @NonNull final KeyEvent keyEvent) {
        boolean b = false;
        if (component == null) {
            return false;
        }
        if (Build$VERSION.SDK_INT >= 28) {
            return component.superDispatchKeyEvent(keyEvent);
        }
        if (window$Callback instanceof Activity) {
            return activitySuperDispatchKeyEventPre28((Activity)window$Callback, keyEvent);
        }
        if (window$Callback instanceof Dialog) {
            return dialogSuperDispatchKeyEventPre28((Dialog)window$Callback, keyEvent);
        }
        if ((view != null && ViewCompat.dispatchUnhandledKeyEventBeforeCallback(view, keyEvent)) || component.superDispatchKeyEvent(keyEvent)) {
            b = true;
        }
        return b;
    }
    
    private static DialogInterface$OnKeyListener getDialogKeyListenerPre28(final Dialog dialog) {
        if (!KeyEventDispatcher.sDialogFieldsFetched) {
            try {
                (KeyEventDispatcher.sDialogKeyListenerField = Dialog.class.getDeclaredField("mOnKeyListener")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {}
            KeyEventDispatcher.sDialogFieldsFetched = true;
        }
        if (KeyEventDispatcher.sDialogKeyListenerField != null) {
            try {
                return (DialogInterface$OnKeyListener)KeyEventDispatcher.sDialogKeyListenerField.get(dialog);
            }
            catch (IllegalAccessException ex2) {}
        }
        return null;
    }
    
    public interface Component
    {
        boolean superDispatchKeyEvent(final KeyEvent p0);
    }
}
