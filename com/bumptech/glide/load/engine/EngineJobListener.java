package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.*;

interface EngineJobListener
{
    void onEngineJobCancelled(final EngineJob p0, final Key p1);
    
    void onEngineJobComplete(final Key p0, final EngineResource<?> p1);
}
