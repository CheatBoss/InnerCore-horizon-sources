package com.mojang.minecraftpe;

import android.content.*;

public interface ActivityListener
{
    void onActivityResult(final int p0, final int p1, final Intent p2);
    
    void onDestroy();
    
    void onResume();
    
    void onStop();
}
