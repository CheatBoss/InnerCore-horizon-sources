package androidx.core.app;

import android.app.*;
import androidx.core.view.*;
import androidx.collection.*;
import android.view.*;
import android.os.*;
import android.annotation.*;
import androidx.lifecycle.*;
import androidx.annotation.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
public class ComponentActivity extends Activity implements LifecycleOwner, Component
{
    private SimpleArrayMap<Class<? extends ExtraData>, ExtraData> mExtraDataMap;
    private LifecycleRegistry mLifecycleRegistry;
    
    public ComponentActivity() {
        this.mExtraDataMap = (SimpleArrayMap<Class<? extends ExtraData>, ExtraData>)new SimpleArrayMap();
        this.mLifecycleRegistry = new LifecycleRegistry((LifecycleOwner)this);
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        final View decorView = this.getWindow().getDecorView();
        return (decorView != null && KeyEventDispatcher.dispatchBeforeHierarchy(decorView, keyEvent)) || KeyEventDispatcher.dispatchKeyEvent((KeyEventDispatcher.Component)this, decorView, (Window$Callback)this, keyEvent);
    }
    
    public boolean dispatchKeyShortcutEvent(final KeyEvent keyEvent) {
        final View decorView = this.getWindow().getDecorView();
        return (decorView != null && KeyEventDispatcher.dispatchBeforeHierarchy(decorView, keyEvent)) || super.dispatchKeyShortcutEvent(keyEvent);
    }
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public <T extends ExtraData> T getExtraData(final Class<T> clazz) {
        return (T)this.mExtraDataMap.get((Object)clazz);
    }
    
    @NonNull
    public Lifecycle getLifecycle() {
        return (Lifecycle)this.mLifecycleRegistry;
    }
    
    @SuppressLint({ "RestrictedApi" })
    protected void onCreate(@Nullable final Bundle bundle) {
        super.onCreate(bundle);
        ReportFragment.injectIfNeededIn((Activity)this);
    }
    
    @CallSuper
    protected void onSaveInstanceState(@NonNull final Bundle bundle) {
        this.mLifecycleRegistry.markState(Lifecycle$State.CREATED);
        super.onSaveInstanceState(bundle);
    }
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public void putExtraData(final ExtraData extraData) {
        this.mExtraDataMap.put((Object)extraData.getClass(), (Object)extraData);
    }
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public boolean superDispatchKeyEvent(final KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent);
    }
    
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static class ExtraData
    {
    }
}
