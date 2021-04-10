package org.mineprogramming.horizon.innercore.view;

import com.android.tools.r8.annotations.*;
import android.os.*;
import com.zhekasmirnov.horizon.launcher.ads.*;
import android.view.*;
import org.mineprogramming.horizon.innercore.util.*;
import android.content.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.app.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;

@SynthesizedClassMap({ -$$Lambda$ModsManagerBanner$j2DXDlHa3Lg83yWfPitkBCMAsGg.class })
public class ModsManagerBanner
{
    private static final ModsManagerBanner instance;
    private final double density;
    
    static {
        instance = new ModsManagerBanner();
    }
    
    private ModsManagerBanner() {
        this.density = AdsManager.getInstance().getDesiredAdDensity();
    }
    
    public static ModsManagerBanner getInstance() {
        return ModsManagerBanner.instance;
    }
    
    public void closeInterstitial() {
        new Handler(Looper.getMainLooper()).post((Runnable)-$$Lambda$ModsManagerBanner$j2DXDlHa3Lg83yWfPitkBCMAsGg.INSTANCE);
    }
    
    public boolean shouldDisplayInterstitial() {
        if (this.density >= 0.8) {
            return Math.random() > 0.5;
        }
        return this.density >= 0.7 && Math.random() > 0.75;
    }
    
    public boolean shouldDisplayNativeAfter(final int n) {
        final double density = this.density;
        final boolean b = false;
        final boolean b2 = false;
        final boolean b3 = false;
        boolean b4 = false;
        if (density < 0.5) {
            return false;
        }
        if (this.density >= 0.9) {
            if ((n - 3) % 4 == 0) {
                b4 = true;
            }
            return b4;
        }
        if (this.density >= 0.8) {
            boolean b5 = b;
            if ((n - 3) % 5 == 0) {
                b5 = true;
            }
            return b5;
        }
        if (this.density >= 0.7) {
            boolean b6 = b2;
            if ((n - 3) % 7 == 0) {
                b6 = true;
            }
            return b6;
        }
        boolean b7 = b3;
        if ((n - 3) % 10 == 0) {
            b7 = true;
        }
        return b7;
    }
    
    public void showInterstitial() {
        AdsManager.getInstance().loadConcurrentAd(new String[] { "interstitial_video", "interstitial" }, 10, false, (AdsManager$AdListener)new AdsManager$AdListener() {
            public void onAdLoaded(final AdContainer adContainer) {
                adContainer.inflate((ViewGroup)null);
                new Handler().postDelayed((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        AdsManager.getInstance().closeAllRequests();
                        AdsManager.getInstance().closeInterstitialAds();
                    }
                }, (long)(int)(AdsManager.getInstance().getDesiredAdDuration() * 1000.0));
            }
        }, new String[] { "horizon-dev", "pack-mod-browser" });
    }
    
    public void showInterstitialIfRequired() {
        if (this.shouldDisplayInterstitial()) {
            this.showInterstitial();
        }
    }
    
    public void showNative(final ViewGroup viewGroup) {
        AdsManager.getInstance().loadConcurrentAd(new String[] { "native" }, 200, false, (AdsManager$AdListener)new AdsManager$AdListener() {
            public void onAdLoaded(final AdContainer adContainer) {
                final Activity context = AdsManager.getInstance().getContext();
                try {
                    final InflatedView inflateLayout = JsonInflater.inflateLayout((Context)context, null, ResourceReader.readLayout((Context)context, "ad_item"));
                    inflateLayout.getViewByJsonId("image").setId(65540);
                    inflateLayout.getViewByJsonId("name").setId(65537);
                    inflateLayout.getViewByJsonId("description").setId(65536);
                    adContainer.inflate((ViewGroup)inflateLayout.getView());
                    viewGroup.addView(inflateLayout.getView());
                }
                catch (JSONException | JsonInflaterException ex) {
                    final Object o;
                    throw new RuntimeException((Throwable)o);
                }
            }
        }, new String[] { "horizon-dev", "pack-mod-browser" });
    }
}
