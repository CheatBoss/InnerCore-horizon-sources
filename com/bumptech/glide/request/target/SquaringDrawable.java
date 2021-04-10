package com.bumptech.glide.request.target;

import com.bumptech.glide.load.resource.drawable.*;
import android.content.res.*;
import android.annotation.*;
import android.graphics.drawable.*;
import android.graphics.*;

public class SquaringDrawable extends GlideDrawable
{
    private boolean mutated;
    private State state;
    private GlideDrawable wrapped;
    
    public SquaringDrawable(final GlideDrawable glideDrawable, final int n) {
        this(new State(glideDrawable.getConstantState(), n), glideDrawable, null);
    }
    
    SquaringDrawable(final State state, final GlideDrawable wrapped, final Resources resources) {
        this.state = state;
        if (wrapped != null) {
            this.wrapped = wrapped;
            return;
        }
        if (resources != null) {
            this.wrapped = (GlideDrawable)state.wrapped.newDrawable(resources);
            return;
        }
        this.wrapped = (GlideDrawable)state.wrapped.newDrawable();
    }
    
    public void clearColorFilter() {
        this.wrapped.clearColorFilter();
    }
    
    public void draw(final Canvas canvas) {
        this.wrapped.draw(canvas);
    }
    
    @TargetApi(19)
    public int getAlpha() {
        return this.wrapped.getAlpha();
    }
    
    @TargetApi(11)
    public Drawable$Callback getCallback() {
        return this.wrapped.getCallback();
    }
    
    public int getChangingConfigurations() {
        return this.wrapped.getChangingConfigurations();
    }
    
    public Drawable$ConstantState getConstantState() {
        return this.state;
    }
    
    public Drawable getCurrent() {
        return this.wrapped.getCurrent();
    }
    
    public int getIntrinsicHeight() {
        return this.state.side;
    }
    
    public int getIntrinsicWidth() {
        return this.state.side;
    }
    
    public int getMinimumHeight() {
        return this.wrapped.getMinimumHeight();
    }
    
    public int getMinimumWidth() {
        return this.wrapped.getMinimumWidth();
    }
    
    public int getOpacity() {
        return this.wrapped.getOpacity();
    }
    
    public boolean getPadding(final Rect rect) {
        return this.wrapped.getPadding(rect);
    }
    
    public void invalidateSelf() {
        super.invalidateSelf();
        this.wrapped.invalidateSelf();
    }
    
    @Override
    public boolean isAnimated() {
        return this.wrapped.isAnimated();
    }
    
    public boolean isRunning() {
        return this.wrapped.isRunning();
    }
    
    public Drawable mutate() {
        if (!this.mutated && super.mutate() == this) {
            this.wrapped = (GlideDrawable)this.wrapped.mutate();
            this.state = new State(this.state);
            this.mutated = true;
        }
        return this;
    }
    
    public void scheduleSelf(final Runnable runnable, final long n) {
        super.scheduleSelf(runnable, n);
        this.wrapped.scheduleSelf(runnable, n);
    }
    
    public void setAlpha(final int alpha) {
        this.wrapped.setAlpha(alpha);
    }
    
    public void setBounds(final int n, final int n2, final int n3, final int n4) {
        super.setBounds(n, n2, n3, n4);
        this.wrapped.setBounds(n, n2, n3, n4);
    }
    
    public void setBounds(final Rect rect) {
        super.setBounds(rect);
        this.wrapped.setBounds(rect);
    }
    
    public void setChangingConfigurations(final int changingConfigurations) {
        this.wrapped.setChangingConfigurations(changingConfigurations);
    }
    
    public void setColorFilter(final int n, final PorterDuff$Mode porterDuff$Mode) {
        this.wrapped.setColorFilter(n, porterDuff$Mode);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.wrapped.setColorFilter(colorFilter);
    }
    
    public void setDither(final boolean dither) {
        this.wrapped.setDither(dither);
    }
    
    public void setFilterBitmap(final boolean filterBitmap) {
        this.wrapped.setFilterBitmap(filterBitmap);
    }
    
    @Override
    public void setLoopCount(final int loopCount) {
        this.wrapped.setLoopCount(loopCount);
    }
    
    public boolean setVisible(final boolean b, final boolean b2) {
        return this.wrapped.setVisible(b, b2);
    }
    
    public void start() {
        this.wrapped.start();
    }
    
    public void stop() {
        this.wrapped.stop();
    }
    
    public void unscheduleSelf(final Runnable runnable) {
        super.unscheduleSelf(runnable);
        this.wrapped.unscheduleSelf(runnable);
    }
    
    static class State extends Drawable$ConstantState
    {
        private final int side;
        private final Drawable$ConstantState wrapped;
        
        State(final Drawable$ConstantState wrapped, final int side) {
            this.wrapped = wrapped;
            this.side = side;
        }
        
        State(final State state) {
            this(state.wrapped, state.side);
        }
        
        public int getChangingConfigurations() {
            return 0;
        }
        
        public Drawable newDrawable() {
            return this.newDrawable(null);
        }
        
        public Drawable newDrawable(final Resources resources) {
            return new SquaringDrawable(this, null, resources);
        }
    }
}
