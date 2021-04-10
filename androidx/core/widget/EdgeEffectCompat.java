package androidx.core.widget;

import android.widget.*;
import android.content.*;
import androidx.annotation.*;
import android.os.*;
import android.graphics.*;

public final class EdgeEffectCompat
{
    private EdgeEffect mEdgeEffect;
    
    @Deprecated
    public EdgeEffectCompat(final Context context) {
        this.mEdgeEffect = new EdgeEffect(context);
    }
    
    public static void onPull(@NonNull final EdgeEffect edgeEffect, final float n, final float n2) {
        if (Build$VERSION.SDK_INT >= 21) {
            edgeEffect.onPull(n, n2);
            return;
        }
        edgeEffect.onPull(n);
    }
    
    @Deprecated
    public boolean draw(final Canvas canvas) {
        return this.mEdgeEffect.draw(canvas);
    }
    
    @Deprecated
    public void finish() {
        this.mEdgeEffect.finish();
    }
    
    @Deprecated
    public boolean isFinished() {
        return this.mEdgeEffect.isFinished();
    }
    
    @Deprecated
    public boolean onAbsorb(final int n) {
        this.mEdgeEffect.onAbsorb(n);
        return true;
    }
    
    @Deprecated
    public boolean onPull(final float n) {
        this.mEdgeEffect.onPull(n);
        return true;
    }
    
    @Deprecated
    public boolean onPull(final float n, final float n2) {
        onPull(this.mEdgeEffect, n, n2);
        return true;
    }
    
    @Deprecated
    public boolean onRelease() {
        this.mEdgeEffect.onRelease();
        return this.mEdgeEffect.isFinished();
    }
    
    @Deprecated
    public void setSize(final int n, final int n2) {
        this.mEdgeEffect.setSize(n, n2);
    }
}
