package com.microsoft.xbox.toolkit;

import android.content.*;
import java.util.*;
import android.media.*;
import com.microsoft.xboxtcui.*;

public class SoundManager
{
    private static final int MAX_STREAM_SIZE = 14;
    private static final int NO_LOOP = 0;
    private AudioManager audioManager;
    private Context context;
    private boolean isEnabled;
    private ArrayList<Integer> recentlyPlayedResourceIds;
    private HashMap<Integer, Integer> resourceSoundIdMap;
    private SoundPool soundPool;
    
    private SoundManager() {
        this.resourceSoundIdMap = new HashMap<Integer, Integer>();
        this.recentlyPlayedResourceIds = new ArrayList<Integer>();
        this.isEnabled = false;
        XLEAssert.assertTrue("You must access sound manager on UI thread.", Thread.currentThread() == ThreadManager.UIThread);
        this.context = XboxTcuiSdk.getApplicationContext();
        this.soundPool = new SoundPool(14, 3, 0);
        this.audioManager = (AudioManager)this.context.getSystemService("audio");
    }
    
    public static SoundManager getInstance() {
        return SoundManagerHolder.instance;
    }
    
    public void clearMostRecentlyPlayedResourceIds() {
    }
    
    public Integer[] getMostRecentlyPlayedResourceIds() {
        return new Integer[0];
    }
    
    public void loadSound(final int n) {
        if (!this.resourceSoundIdMap.containsKey(n)) {
            this.resourceSoundIdMap.put(n, this.soundPool.load(this.context, n, 1));
        }
    }
    
    public void playSound(int intValue) {
        if (!this.isEnabled) {
            return;
        }
        if (!this.resourceSoundIdMap.containsKey(intValue)) {
            final int load = this.soundPool.load(this.context, intValue, 1);
            this.resourceSoundIdMap.put(intValue, load);
            intValue = load;
        }
        else {
            intValue = this.resourceSoundIdMap.get(intValue);
        }
        final float n = this.audioManager.getStreamVolume(3) / (float)this.audioManager.getStreamMaxVolume(3);
        this.soundPool.play(intValue, n, n, 1, 0, 1.0f);
    }
    
    public void setEnabled(final boolean isEnabled) {
        if (this.isEnabled != isEnabled) {
            this.isEnabled = isEnabled;
        }
    }
    
    private static class SoundManagerHolder
    {
        public static final SoundManager instance;
        
        static {
            instance = new SoundManager(null);
        }
    }
}
