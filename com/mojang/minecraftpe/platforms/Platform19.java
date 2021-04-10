package com.mojang.minecraftpe.platforms;

import android.os.*;
import android.view.*;

public class Platform19 extends Platform9
{
    private Runnable decorViewSettings;
    private View decoreView;
    private Handler eventHandler;
    
    public Platform19(final boolean b) {
        if (b) {
            this.eventHandler = new Handler();
        }
    }
    
    @Override
    public void onAppStart(final View decoreView) {
        if (this.eventHandler == null) {
            return;
        }
        (this.decoreView = decoreView).setOnSystemUiVisibilityChangeListener((View$OnSystemUiVisibilityChangeListener)new View$OnSystemUiVisibilityChangeListener() {
            public void onSystemUiVisibilityChange(final int n) {
                Platform19.this.eventHandler.postDelayed(Platform19.this.decorViewSettings, 500L);
            }
        });
        final Runnable decorViewSettings = new Runnable() {
            @Override
            public void run() {
                Platform19.this.decoreView.setSystemUiVisibility(5894);
            }
        };
        this.decorViewSettings = decorViewSettings;
        this.eventHandler.post((Runnable)decorViewSettings);
    }
    
    @Override
    public void onViewFocusChanged(final boolean b) {
        final Handler eventHandler = this.eventHandler;
        if (eventHandler != null && b) {
            eventHandler.postDelayed(this.decorViewSettings, 500L);
        }
    }
    
    @Override
    public void onVolumePressed() {
    }
}
