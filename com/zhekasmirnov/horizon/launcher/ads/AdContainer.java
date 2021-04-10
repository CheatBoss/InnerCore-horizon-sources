package com.zhekasmirnov.horizon.launcher.ads;

import android.view.*;
import android.util.*;
import android.app.*;
import android.content.*;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.formats.*;
import android.widget.*;

public abstract class AdContainer
{
    public static final String TYPE_BANNER = "banner";
    public static final String TYPE_INTERSTITIAL = "interstitial";
    public static final String TYPE_INTERSTITIAL_VIDEO = "interstitial_video";
    public static final String TYPE_NATIVE = "native";
    private static final int ID_OFFSET = 65535;
    public static final int ID_AD_VIEW = 65535;
    public static final int ID_BODY = 65536;
    public static final int ID_HEADING = 65537;
    public static final int ID_MEDIA = 65538;
    public static final int ID_IMAGE = 65539;
    public static final int ID_ICON = 65540;
    public static final int ID_PRICING = 65541;
    public static final int ID_ADVERTISER = 65542;
    public static final int ID_AD_CHOICES = 65543;
    public static final int ID_STORE = 65544;
    public static final int ID_STAR_RATING = 65545;
    public static final int ID_CALL_TO_ACTION = 65546;
    public static final int ID_CLICK_CONFIRMING = 65547;
    public final AdsManager manager;
    public final String adUnitId;
    private int failReason;
    public int useCount;
    public int remainingLoadingAttempts;
    private State state;
    
    public AdContainer(final AdsManager manager, final String adUnitId) {
        this.failReason = -1;
        this.useCount = 0;
        this.remainingLoadingAttempts = 1;
        this.state = State.INITIALIZED;
        this.adUnitId = adUnitId;
        this.manager = manager;
    }
    
    public abstract String getAdType();
    
    public abstract View inflate(final ViewGroup p0);
    
    protected abstract void loadAd(final AdListener p0, final AdRequest p1);
    
    public State getState() {
        return this.state;
    }
    
    public int getFailReason() {
        return this.failReason;
    }
    
    private void load0(final Listener listener) {
        final Activity context = this.manager.getContext();
        if (context == null) {
            return;
        }
        context.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                AdContainer.this.loadAd(new AdListener() {
                    public void onAdLoaded() {
                        AdContainer.this.state = State.LOADED;
                        AdContainer.this.failReason = -1;
                        if (listener != null) {
                            listener.onLoaded(AdContainer.this);
                        }
                    }
                    
                    public void onAdFailedToLoad(final int reason) {
                        final AdContainer this$0 = AdContainer.this;
                        final int remainingLoadingAttempts = this$0.remainingLoadingAttempts - 1;
                        this$0.remainingLoadingAttempts = remainingLoadingAttempts;
                        if (remainingLoadingAttempts > 0) {
                            AdContainer.this.state = State.LOADING;
                            AdContainer.this.load0(listener);
                        }
                        else {
                            AdContainer.this.state = State.FAILED;
                            AdContainer.this.failReason = reason;
                            if (listener != null) {
                                listener.onFailedToLoad(AdContainer.this, reason);
                            }
                        }
                        Log.d("AdContainer", AdContainer.this.getAdType() + " ad failed to load, reason: " + reason + ", attempts left: " + AdContainer.this.remainingLoadingAttempts);
                    }
                    
                    public void onAdClosed() {
                    }
                    
                    public void onAdOpened() {
                    }
                    
                    public void onAdImpression() {
                    }
                }, AdContainer.this.manager.buildAdRequest());
            }
        });
    }
    
    public void load(final Listener listener) {
        this.state = State.LOADING;
        this.remainingLoadingAttempts = this.manager.configuration.getAdTypeMaxAttempts(this.getAdType());
        this.load0(listener);
    }
    
    public static AdContainer newContainer(final AdsManager manager, final String type, final String adUnitId) {
        if (manager.getContext() == null) {
            return new Stub(manager, adUnitId, type);
        }
        switch (type) {
            case "banner": {
                return new Banner(manager, adUnitId);
            }
            case "interstitial": {
                return new Interstitial(manager, adUnitId);
            }
            case "interstitial_video": {
                return new InterstitialVideo(manager, adUnitId);
            }
            case "native": {
                return new Native(manager, adUnitId);
            }
            default: {
                throw new IllegalArgumentException("invalid ad container type: " + type);
            }
        }
    }
    
    public enum State
    {
        INITIALIZED, 
        LOADING, 
        LOADED, 
        FAILED;
    }
    
    public static class Stub extends AdContainer
    {
        private final String type;
        
        public Stub(final AdsManager manager, final String adUnitId, final String type) {
            super(manager, adUnitId);
            this.type = type;
            System.out.println("Stub ad container was created because context is null: " + type + " " + adUnitId);
        }
        
        @Override
        public String getAdType() {
            return this.type;
        }
        
        @Override
        public View inflate(final ViewGroup parent) {
            return null;
        }
        
        @Override
        protected void loadAd(final AdListener listener, final AdRequest request) {
        }
    }
    
    public static class Banner extends AdContainer
    {
        public Banner(final AdsManager manager, final String adUnitId) {
            super(manager, adUnitId);
        }
        
        @Override
        public String getAdType() {
            return "banner";
        }
        
        @Override
        public View inflate(final ViewGroup parent) {
            return null;
        }
        
        @Override
        protected void loadAd(final AdListener listener, final AdRequest request) {
        }
    }
    
    public static class Interstitial extends AdContainer
    {
        public final InterstitialAd adInstance;
        
        public Interstitial(final AdsManager manager, final String adUnitId) {
            super(manager, adUnitId);
            (this.adInstance = new InterstitialAd((Context)manager.getContext())).setAdUnitId(adUnitId);
        }
        
        @Override
        public String getAdType() {
            return "interstitial";
        }
        
        @Override
        public View inflate(final ViewGroup parent) {
            this.adInstance.show();
            return null;
        }
        
        @Override
        protected void loadAd(final AdListener listener, final AdRequest request) {
            this.adInstance.setAdListener(listener);
            this.adInstance.loadAd(request);
        }
    }
    
    public static class InterstitialVideo extends Interstitial
    {
        public InterstitialVideo(final AdsManager manager, final String adUnitId) {
            super(manager, adUnitId);
        }
        
        @Override
        public String getAdType() {
            return "interstitial_video";
        }
    }
    
    public static class Native extends AdContainer
    {
        private UnifiedNativeAd nativeAd;
        private AdListener wrappedListener;
        private AdLoader loader;
        
        public Native(final AdsManager manager, final String adUnitId) {
            super(manager, adUnitId);
            this.nativeAd = null;
            this.wrappedListener = new AdListener();
            this.loader = new AdLoader.Builder((Context)manager.getContext(), adUnitId).forUnifiedNativeAd((UnifiedNativeAd.OnUnifiedNativeAdLoadedListener)new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                public void onUnifiedNativeAdLoaded(final UnifiedNativeAd unifiedNativeAd) {
                    Native.this.nativeAd = unifiedNativeAd;
                    Native.this.wrappedListener.onAdLoaded();
                }
            }).withAdListener((AdListener)new AdListener() {
                public void onAdOpened() {
                    Native.this.wrappedListener.onAdOpened();
                }
                
                public void onAdFailedToLoad(final int i) {
                    Native.this.wrappedListener.onAdFailedToLoad(i);
                }
                
                public void onAdClicked() {
                    Native.this.wrappedListener.onAdClicked();
                }
                
                public void onAdClosed() {
                    Native.this.wrappedListener.onAdClosed();
                }
                
                public void onAdImpression() {
                    Native.this.wrappedListener.onAdImpression();
                }
                
                public void onAdLeftApplication() {
                    Native.this.wrappedListener.onAdLeftApplication();
                }
            }).build();
        }
        
        @Override
        public String getAdType() {
            return "native";
        }
        
        @Override
        public View inflate(final ViewGroup parent) {
            if (this.nativeAd == null) {
                return null;
            }
            UnifiedNativeAdView adView = (UnifiedNativeAdView)parent.findViewById(65535);
            if (adView == null) {
                adView = (UnifiedNativeAdView)parent;
            }
            final TextView headline = (TextView)parent.findViewById(65537);
            if (headline != null) {
                adView.setHeadlineView((View)headline);
                headline.setText((CharSequence)this.nativeAd.getHeadline());
            }
            final View bodyView = parent.findViewById(65536);
            if (bodyView != null) {
                adView.setBodyView(bodyView);
                if (this.nativeAd.getBody() == null) {
                    adView.getBodyView().setVisibility(4);
                }
                else {
                    adView.getBodyView().setVisibility(0);
                    ((TextView)adView.getBodyView()).setText((CharSequence)this.nativeAd.getBody());
                }
            }
            final MediaView mediaView = (MediaView)parent.findViewById(65538);
            if (mediaView != null) {
                mediaView.setOnHierarchyChangeListener((ViewGroup.OnHierarchyChangeListener)new ViewGroup.OnHierarchyChangeListener() {
                    public void onChildViewAdded(final View parent, final View child) {
                        if (child instanceof ImageView) {
                            final ImageView imageView = (ImageView)child;
                            imageView.setAdjustViewBounds(true);
                        }
                    }
                    
                    public void onChildViewRemoved(final View parent, final View child) {
                    }
                });
                adView.setMediaView(mediaView);
            }
            final View imageView = parent.findViewById(65539);
            if (imageView != null) {
                adView.setImageView(imageView);
            }
            final View iconView = parent.findViewById(65540);
            if (iconView != null) {
                adView.setIconView(iconView);
                if (this.nativeAd.getIcon() == null) {
                    adView.getIconView().setVisibility(8);
                }
                else {
                    ((ImageView)adView.getIconView()).setImageDrawable(this.nativeAd.getIcon().getDrawable());
                    adView.getIconView().setVisibility(0);
                }
            }
            final View pricingView = parent.findViewById(65541);
            if (pricingView != null) {
                adView.setPriceView(pricingView);
                if (this.nativeAd.getPrice() == null) {
                    adView.getPriceView().setVisibility(4);
                }
                else {
                    adView.getPriceView().setVisibility(0);
                    ((TextView)adView.getPriceView()).setText((CharSequence)this.nativeAd.getPrice());
                }
            }
            final View advertiserView = parent.findViewById(65542);
            if (advertiserView != null) {
                adView.setAdvertiserView(advertiserView);
                if (this.nativeAd.getAdvertiser() == null) {
                    adView.getAdvertiserView().setVisibility(4);
                }
                else {
                    ((TextView)adView.getAdvertiserView()).setText((CharSequence)this.nativeAd.getAdvertiser());
                    adView.getAdvertiserView().setVisibility(0);
                }
            }
            final AdChoicesView adChoicesView = (AdChoicesView)parent.findViewById(65543);
            if (adChoicesView != null) {
                adView.setAdChoicesView(adChoicesView);
            }
            final View storeView = parent.findViewById(65544);
            if (storeView != null) {
                adView.setStoreView(storeView);
                if (this.nativeAd.getStore() == null) {
                    adView.getStoreView().setVisibility(4);
                }
                else {
                    adView.getStoreView().setVisibility(0);
                    ((TextView)adView.getStoreView()).setText((CharSequence)this.nativeAd.getStore());
                }
            }
            final View starRatingView = parent.findViewById(65545);
            if (starRatingView != null) {
                adView.setStarRatingView(starRatingView);
                if (this.nativeAd.getStarRating() == null) {
                    adView.getStarRatingView().setVisibility(4);
                }
                else {
                    ((RatingBar)adView.getStarRatingView()).setRating(this.nativeAd.getStarRating().floatValue());
                    adView.getStarRatingView().setVisibility(0);
                }
            }
            final View callToActionView = parent.findViewById(65546);
            if (callToActionView != null) {
                adView.setCallToActionView(callToActionView);
                if (this.nativeAd.getCallToAction() == null) {
                    adView.getCallToActionView().setVisibility(4);
                }
                else {
                    adView.getCallToActionView().setVisibility(0);
                    ((Button)adView.getCallToActionView()).setText((CharSequence)this.nativeAd.getCallToAction());
                }
            }
            final View clickConfirmationView = parent.findViewById(65546);
            if (clickConfirmationView != null) {
                adView.setClickConfirmingView(clickConfirmationView);
            }
            adView.setNativeAd(this.nativeAd);
            return (View)parent;
        }
        
        @Override
        protected void loadAd(final AdListener listener, final AdRequest request) {
            this.wrappedListener = listener;
            this.loader.loadAd(request);
        }
    }
    
    public interface Listener
    {
        void onLoaded(final AdContainer p0);
        
        void onFailedToLoad(final AdContainer p0, final int p1);
    }
}
