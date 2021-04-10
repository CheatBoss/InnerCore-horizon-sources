package com.zhekasmirnov.horizon.launcher.ads;

import android.app.*;
import com.zhekasmirnov.horizon.*;
import android.annotation.*;
import java.security.*;
import com.zhekasmirnov.horizon.runtime.task.*;
import android.util.*;
import org.json.*;
import android.content.*;
import java.util.*;
import com.google.android.gms.ads.*;

public class AdsManager
{
    public static final boolean isTestMode = false;
    private static AdsManager instance;
    private final AdDistributionModel distributionModel;
    private final AdDataRecorder adDataRecorder;
    public final AdGlobalConfiguration configuration;
    private AdDomain currentDomain;
    private boolean isAdMobInitialized;
    private String recordedDataLockUUID;
    
    public static AdsManager getInstance() {
        return AdsManager.instance;
    }
    
    private synchronized void initializeAdMobIfRequired() {
        if (this.currentDomain != null && !this.isAdMobInitialized) {
            final List<String> testDeviceIds = Arrays.asList("33BE2250B43518CCDA7DE426D04EE231");
            MobileAds.initialize((Context)this.getContext(), this.currentDomain.providerId);
            MobileAds.setAppMuted(!this.configuration.isAudioAllowed);
            this.isAdMobInitialized = true;
        }
    }
    
    public AdsManager() {
        this.currentDomain = null;
        this.isAdMobInitialized = false;
        this.recordedDataLockUUID = null;
        this.distributionModel = new AdDistributionModel(this);
        this.adDataRecorder = new AdDataRecorder(this);
        this.configuration = new AdGlobalConfiguration(this);
        this.distributionModel.addDistributionNode(this.distributionModel.getRootName(), 1.0, "horizon-dev").setDomain(this.newHardcodedMainDomain());
    }
    
    public Activity getContext() {
        final Activity result = HorizonApplication.getTopRunningActivity();
        return (result != null) ? result : HorizonApplication.getTopActivity();
    }
    
    public AdDomain newHardcodedMainDomain() {
        final AdDomain domainFromConfiguration = this.configuration.domainByDistributionNodeName.get("horizon-dev");
        if (domainFromConfiguration != null) {
            return domainFromConfiguration;
        }
        final AdDomain domain = new AdDomain(this, "ca-app-pub-3152642364854897~5577139781");
        final List<AdContainer> containers = new ArrayList<AdContainer>();
        containers.add(AdContainer.newContainer(this, "interstitial", "ca-app-pub-3152642364854897/5851444283"));
        containers.add(AdContainer.newContainer(this, "interstitial", "ca-app-pub-3152642364854897/4538362613"));
        containers.add(AdContainer.newContainer(this, "interstitial_video", "ca-app-pub-3152642364854897/7340991056"));
        containers.add(AdContainer.newContainer(this, "native", "ca-app-pub-3152642364854897/5696365763"));
        containers.add(AdContainer.newContainer(this, "native", "ca-app-pub-3152642364854897/3225280945"));
        containers.add(AdContainer.newContainer(this, "native", "ca-app-pub-3152642364854897/9599117602"));
        containers.add(AdContainer.newContainer(this, "native", "ca-app-pub-3152642364854897/6874264800"));
        containers.add(AdContainer.newContainer(this, "banner", "ca-app-pub-3152642364854897/9788202751"));
        containers.add(AdContainer.newContainer(this, "banner", "ca-app-pub-3152642364854897/2140264136"));
        containers.add(AdContainer.newContainer(this, "banner", "ca-app-pub-3152642364854897/8283549399"));
        containers.add(AdContainer.newContainer(this, "banner", "ca-app-pub-3152642364854897/9790689295"));
        domain.addContainers(containers);
        return domain;
    }
    
    @SuppressLint({ "HardwareIds" })
    public AdRequest buildAdRequest() {
        final AdRequest.Builder builder = new AdRequest.Builder();
        return builder.build();
    }
    
    private static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            final byte[] messageDigest = digest.digest();
            final StringBuilder hexString = new StringBuilder();
            for (final byte aMessageDigest : messageDigest) {
                String h;
                for (h = Integer.toHexString(0xFF & aMessageDigest); h.length() < 2; h = "0" + h) {}
                hexString.append(h);
            }
            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public Task getInitializationTask() {
        return new Task() {
            @Override
            public Object getLock() {
                return AdsManager.this;
            }
            
            @Override
            public void run() {
                AdsManager.this.runInitialization();
            }
        };
    }
    
    public void runInitialization() {
        this.configuration.readUrl("https://gitlab.com/zhekasmirnov/horizon-cloud-config/raw/master/ads.json");
        final Context context = (Context)this.getContext();
        if (context == null) {
            return;
        }
        final SharedPreferences sharedPreferences = context.getSharedPreferences("record", 0);
        final String data = sharedPreferences.getString("data", (String)null);
        JSONObject json = new JSONObject();
        if (data != null) {
            try {
                json = new JSONObject(data);
                this.recordedDataLockUUID = json.optString("lockUUID");
                if (this.configuration.dataLockUUID != null && !this.configuration.dataLockUUID.equals(this.recordedDataLockUUID)) {
                    Log.i("AdsManager", "recorded data lock changed, data will be erased");
                    json = new JSONObject();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.adDataRecorder.readDataFromJson(json);
        this.currentDomain = this.adDataRecorder.createWinningDataDomainSource();
        System.out.println("ad manager is now using domain: " + this.currentDomain.providerId);
    }
    
    public synchronized void flushRecordedData() {
        final JSONObject json = this.adDataRecorder.writeDataToJson();
        if (json != null) {
            try {
                json.put("lockUUID", (Object)((this.configuration.dataLockUUID != null) ? this.configuration.dataLockUUID : this.recordedDataLockUUID));
            }
            catch (JSONException ex) {}
            final SharedPreferences sharedPreferences = this.getContext().getSharedPreferences("record", 0);
            sharedPreferences.edit().putString("data", json.toString()).apply();
        }
    }
    
    public double getDesiredHorizonAdDensity() {
        return this.configuration.desiredHorizonAdDensity;
    }
    
    public double getDesiredAdDensity() {
        return this.configuration.desiredPackAdDensity;
    }
    
    public double getDesiredAdDuration() {
        return this.configuration.desiredPackAdDuration;
    }
    
    public double getDesiredAdditionalBannerChance() {
        return this.configuration.desiredAdditionalBannerChance;
    }
    
    public boolean runDesiredHorizonDensityRandom() {
        return Math.random() < this.configuration.desiredPackAdDensity;
    }
    
    public boolean runDesiredDensityRandom() {
        return Math.random() < this.configuration.desiredPackAdDensity;
    }
    
    public boolean runAdditionalBannerRandom() {
        return Math.random() < this.configuration.desiredAdditionalBannerChance;
    }
    
    public AdDistributionModel getDistributionModel() {
        return this.distributionModel;
    }
    
    public AdDistributionModel.Node addDistributionNode(final String parent, final String name, final double weight) {
        return this.distributionModel.addDistributionNode(parent, weight, name);
    }
    
    public AdDomain registerAdDomain(final AdDistributionModel.Node node, final String domainId, final HashMap<String, List<String>> adUnitIds) {
        final List<AdContainer> containers = new ArrayList<AdContainer>();
        for (final String adType : adUnitIds.keySet()) {
            final List<String> unitIds = adUnitIds.get(adType);
            if (unitIds != null) {
                for (final String adUnitId : unitIds) {
                    containers.add(AdContainer.newContainer(this, adType, adUnitId));
                }
            }
        }
        if (containers.size() > 0) {
            final AdDomain domain = new AdDomain(this, domainId);
            domain.addContainers(containers);
            node.setDomain(domain);
            return domain;
        }
        return null;
    }
    
    private void onRequestFulfilled(final AdListener listener, final AdContainer container, final String... distributionNodes) {
        final AdDomain desiredDomain = this.distributionModel.getRandomAdDomain(distributionNodes);
        this.adDataRecorder.record(desiredDomain, this.currentDomain, container.getAdType());
        this.flushRecordedData();
        this.getContext().runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                listener.onAdLoaded(container);
            }
        });
    }
    
    private void onRequestFailed(final String adType, final HashMap<Integer, Integer> failReasons, final String... distributionNodes) {
        for (final Integer reason : failReasons.keySet()) {
            if (reason != 2) {
                final AdDomain desired = this.distributionModel.getRandomAdDomain(distributionNodes);
                this.adDataRecorder.record(desired, this.currentDomain, adType);
                this.flushRecordedData();
                break;
            }
        }
    }
    
    public AdDomain.AsyncAdRequest loadAd(final String adType, final int timeout, final AdListener listener, final String... distributionNodes) {
        this.initializeAdMobIfRequired();
        if (this.currentDomain != null) {
            final AdDomain.AsyncAdRequest request = this.currentDomain.requireAsync(adType, timeout != 0, new AdDomain.AsyncAdRequestListener() {
                @Override
                public void onFulfilled(final AdContainer container) {
                    AdsManager.this.onRequestFulfilled(listener, container, distributionNodes);
                }
                
                @Override
                public void onFailed(final List<AdContainer> failedContainers, final HashMap<Integer, Integer> failReasons) {
                    AdsManager.this.onRequestFailed(adType, failReasons, new String[0]);
                }
            });
            if (timeout > 0) {
                request.setFancyTimeout(timeout);
            }
            return request;
        }
        return null;
    }
    
    public void loadConcurrentAd(final String[] adTypes, final int typeTimeout, final boolean fancy, final AdListener listener, final String... distributionNodes) {
        this.initializeAdMobIfRequired();
        if (this.currentDomain != null) {
            final List<AdDomain.AsyncAdRequest> requests = new ArrayList<AdDomain.AsyncAdRequest>();
            final HashMap<Integer, Integer> allFailReasons = new HashMap<Integer, Integer>();
            final ConcurrentAdStatus status = new ConcurrentAdStatus();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (final String type : adTypes) {
                        synchronized (requests) {
                            if (status.isFulfilled) {
                                return;
                            }
                            requests.add(AdsManager.this.currentDomain.requireAsync(type, fancy, new AdDomain.AsyncAdRequestListener() {
                                @Override
                                public void onFulfilled(final AdContainer container) {
                                    synchronized (requests) {
                                        if (status.isFulfilled) {
                                            return;
                                        }
                                        status.isFulfilled = true;
                                        for (final AdDomain.AsyncAdRequest request : requests) {
                                            request.close();
                                        }
                                    }
                                    AdsManager.this.onRequestFulfilled(listener, container, distributionNodes);
                                }
                                
                                @Override
                                public void onFailed(final List<AdContainer> failedContainers, final HashMap<Integer, Integer> failReasons) {
                                    final ConcurrentAdStatus val$status = status;
                                    ++val$status.failedCount;
                                    for (final Integer reason : failReasons.keySet()) {
                                        if (allFailReasons.containsKey(reason)) {
                                            allFailReasons.put(reason, failReasons.get(reason) + allFailReasons.get(reason));
                                        }
                                        else {
                                            allFailReasons.put(reason, failReasons.get(reason));
                                        }
                                    }
                                    if (status.failedCount == adTypes.length) {
                                        AdsManager.this.onRequestFailed(adTypes[0], allFailReasons, new String[0]);
                                    }
                                }
                            }));
                        }
                        try {
                            Thread.sleep(typeTimeout);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
    
    public void awaitAndLoadAds(final int count, final int maxTimeout, final String... types) {
        this.initializeAdMobIfRequired();
        if (this.currentDomain != null) {
            this.currentDomain.beginAdLoading(types);
            this.currentDomain.awaitLoadingOfAds(count, maxTimeout, new ArrayList<AdContainer>(), types);
            this.currentDomain.beginAdLoading(new String[0]);
        }
    }
    
    public void awaitAndLoadAds(final int count, final String... types) {
        this.awaitAndLoadAds(count, (int)(this.configuration.maxLoadWaitingTime * 1000.0), types);
    }
    
    public void closeInterstitialAds() {
        final Activity context = HorizonApplication.getTopActivity();
        if (context != null) {
            context.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    final List<Activity> activities = HorizonApplication.getActivityStack();
                    for (final Activity activity : activities) {
                        if (activity instanceof AdActivity) {
                            activity.finish();
                            return;
                        }
                    }
                    Log.d("AdsManager", "finishing ad activity failed");
                }
            });
        }
    }
    
    public void closeAllRequests() {
        if (this.currentDomain != null) {
            this.currentDomain.closeAllRequests();
        }
    }
    
    static {
        AdsManager.instance = new AdsManager();
    }
    
    private class ConcurrentAdStatus
    {
        boolean isFulfilled;
        int failedCount;
        
        private ConcurrentAdStatus() {
            this.isFulfilled = false;
            this.failedCount = 0;
        }
    }
    
    public interface AdListener
    {
        void onAdLoaded(final AdContainer p0);
    }
}
