package com.zhekasmirnov.horizon.launcher.ads;

import java.util.*;
import java.net.*;
import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import org.json.*;

public class AdGlobalConfiguration
{
    public final AdsManager manager;
    public String dataLockUUID;
    public boolean isAudioAllowed;
    public double maxLoadWaitingTime;
    public HashMap<String, Integer> maxLoadingAttemptsByType;
    public double lowerRatingThreshold;
    public double lowerRatingProbabilityFactor;
    public double desiredAdditionalBannerChance;
    public double desiredPackAdDensity;
    public double desiredHorizonAdDensity;
    public double desiredPackAdDuration;
    public final HashMap<String, Double> adTypeRatingWeights;
    public double trustedAdDomainChance;
    public final HashMap<String, AdDomain> trustedAdDomains;
    public final HashMap<String, AdDomain> domainByDistributionNodeName;
    public final HashMap<String, Double> weightByDistributionNodeName;
    private static final int READ_TIME_OUT = 7500;
    
    public AdGlobalConfiguration(final AdsManager manager) {
        this.dataLockUUID = null;
        this.isAudioAllowed = false;
        this.maxLoadWaitingTime = 1.25;
        this.maxLoadingAttemptsByType = new HashMap<String, Integer>();
        this.lowerRatingThreshold = 20.0;
        this.lowerRatingProbabilityFactor = 0.0;
        this.desiredAdditionalBannerChance = 0.0;
        this.desiredPackAdDensity = 1.0;
        this.desiredHorizonAdDensity = 1.0;
        this.desiredPackAdDuration = 10.0;
        this.adTypeRatingWeights = new HashMap<String, Double>();
        this.trustedAdDomainChance = 1.0;
        this.trustedAdDomains = new HashMap<String, AdDomain>();
        this.domainByDistributionNodeName = new HashMap<String, AdDomain>();
        this.weightByDistributionNodeName = new HashMap<String, Double>();
        this.manager = manager;
    }
    
    public void readJson(JSONObject json) {
        if (json == null) {
            json = new JSONObject();
        }
        System.out.println("reading ads config json: " + json);
        final StringBuilder log = new StringBuilder();
        this.dataLockUUID = json.optString("dataLockUUID", (String)null);
        log.append("dataLockUUID: ").append(this.dataLockUUID).append("\n");
        this.isAudioAllowed = json.optBoolean("isAudioAllowed", false);
        log.append("isAudioAllowed: ").append(this.isAudioAllowed).append("\n");
        this.maxLoadWaitingTime = json.optDouble("maxLoadWaitingTime", 1.25);
        if (this.maxLoadWaitingTime < 0.75) {
            this.maxLoadWaitingTime = 1.25;
        }
        log.append("maxLoadWaitingTime: ").append(this.maxLoadWaitingTime).append("\n");
        this.lowerRatingThreshold = json.optDouble("lowerRatingThreshold", 20.0);
        if (this.lowerRatingThreshold < 1.0) {
            this.lowerRatingThreshold = 20.0;
        }
        log.append("lowerRatingThreshold: ").append(this.lowerRatingThreshold).append("\n");
        this.lowerRatingProbabilityFactor = json.optDouble("lowerRatingProbabilityFactor", 0.0);
        if (this.lowerRatingProbabilityFactor > 0.75) {
            this.lowerRatingProbabilityFactor = 0.0;
        }
        log.append("lowerRatingProbabilityFactor: ").append(this.lowerRatingProbabilityFactor).append("\n");
        this.trustedAdDomainChance = json.optDouble("trustedAdDomainChance", 1.0);
        if (this.trustedAdDomainChance < 0.05 || this.trustedAdDomainChance > 1.0) {
            this.trustedAdDomainChance = 1.0;
        }
        log.append("trustedAdDomainChance: ").append(this.trustedAdDomainChance).append("\n");
        this.desiredAdditionalBannerChance = json.optDouble("desiredAdditionalBannerChance", 1.0);
        if (this.desiredAdditionalBannerChance > 0.8) {
            this.desiredAdditionalBannerChance = 0.0;
        }
        log.append("desiredAdditionalBannerChance: ").append(this.desiredAdditionalBannerChance).append("\n");
        this.desiredHorizonAdDensity = json.optDouble("desiredHorizonAdDensity", 1.0);
        if (this.desiredHorizonAdDensity < 0.0) {
            this.desiredHorizonAdDensity = 1.0;
        }
        log.append("desiredHorizonAdDensity: ").append(this.desiredHorizonAdDensity).append("\n");
        this.desiredPackAdDensity = json.optDouble("desiredPackAdDensity", 1.0);
        if (this.desiredPackAdDensity < 0.0) {
            this.desiredPackAdDensity = 1.0;
        }
        log.append("desiredPackAdDensity: ").append(this.desiredPackAdDensity).append("\n");
        this.desiredPackAdDuration = json.optDouble("desiredPackAdDuration", 10.0);
        if (this.desiredPackAdDuration < 4.0) {
            this.desiredPackAdDuration = 10.0;
        }
        log.append("desiredPackAdDuration: ").append(this.desiredPackAdDuration).append("\n");
        this.adTypeRatingWeights.clear();
        this.adTypeRatingWeights.put("native", 1.0);
        this.adTypeRatingWeights.put("banner", 1.0);
        this.adTypeRatingWeights.put("interstitial", 5.0);
        this.adTypeRatingWeights.put("interstitial_video", 10.0);
        final JSONObject weights = json.optJSONObject("weights");
        if (weights != null) {
            for (final String adType : new JsonIterator<String>(weights)) {
                final double weight = weights.optDouble(adType, -1.0);
                if (weight > -1.0) {
                    this.adTypeRatingWeights.put(adType, weight);
                }
            }
        }
        log.append("weights: \n");
        for (final String adType : this.adTypeRatingWeights.keySet()) {
            log.append("    ").append(adType).append(": ").append(this.adTypeRatingWeights.get(adType)).append("\n");
        }
        this.maxLoadingAttemptsByType.clear();
        final JSONObject attempts = json.optJSONObject("attempts");
        if (attempts != null) {
            for (final String adType2 : new JsonIterator<String>(attempts)) {
                final int value = attempts.optInt(adType2, 0);
                if (value > 0) {
                    this.maxLoadingAttemptsByType.put(adType2, value);
                }
            }
        }
        log.append("attempts: \n");
        for (final String adType2 : this.maxLoadingAttemptsByType.keySet()) {
            log.append("    ").append(adType2).append(": ").append(this.maxLoadingAttemptsByType.get(adType2)).append("\n");
        }
        this.weightByDistributionNodeName.clear();
        final JSONObject distributionModelWeights = json.optJSONObject("distributionModelWeights");
        if (distributionModelWeights != null) {
            for (final String name : new JsonIterator<String>(distributionModelWeights)) {
                final double weight2 = distributionModelWeights.optDouble(name, -1.0);
                if (weight2 >= 0.0) {
                    this.weightByDistributionNodeName.put(name, weight2);
                }
            }
        }
        log.append("distribution node weights: \n");
        for (final String name : this.weightByDistributionNodeName.keySet()) {
            log.append("    ").append(name).append(": ").append(this.weightByDistributionNodeName.get(name)).append("\n");
        }
        this.domainByDistributionNodeName.clear();
        final JSONObject distributionModelDomains = json.optJSONObject("distributionModelDomains");
        if (distributionModelDomains != null) {
            for (final String name2 : new JsonIterator<String>(distributionModelDomains)) {
                final JSONObject domainJson = distributionModelDomains.optJSONObject(name2);
                if (domainJson != null) {
                    try {
                        final AdDomain domain = new AdDomain(this.manager, domainJson);
                        this.domainByDistributionNodeName.put(name2, domain);
                    }
                    catch (IllegalArgumentException ex) {}
                }
            }
        }
        log.append("distribution node domains: \n");
        for (final String name2 : this.domainByDistributionNodeName.keySet()) {
            final AdDomain domain2 = this.domainByDistributionNodeName.get(name2);
            if (domain2 != null) {
                log.append("    ").append(name2).append(": domain provider: ").append(domain2.providerId).append(" , containers: \n");
                for (final AdContainer container : domain2.getContainers()) {
                    log.append("        ").append(container.getAdType()).append(": ").append(container.adUnitId).append("\n");
                }
            }
        }
        this.trustedAdDomains.clear();
        final JSONArray trustedDomains = json.optJSONArray("trusted");
        if (trustedDomains != null) {
            for (final JSONObject trustedDomainJson : new JsonIterator<JSONObject>(trustedDomains)) {
                if (trustedDomainJson != null) {
                    try {
                        final AdDomain domain = new AdDomain(this.manager, trustedDomainJson);
                        this.trustedAdDomains.put(domain.providerId, domain);
                    }
                    catch (IllegalArgumentException ex2) {}
                }
            }
        }
        final AdDomain hardcoded = this.manager.newHardcodedMainDomain();
        if (this.trustedAdDomains.get(hardcoded.providerId) == null) {
            this.trustedAdDomains.put(hardcoded.providerId, hardcoded);
        }
        log.append("trusted domains: \n");
        final Iterator<AdDomain> iterator11 = this.trustedAdDomains.values().iterator();
        while (iterator11.hasNext()) {
            final AdDomain domain = iterator11.next();
            log.append("    domain provider: ").append(domain.providerId).append(" , containers: \n");
            for (final AdContainer container2 : domain.getContainers()) {
                log.append("        ").append(container2.getAdType()).append(": ").append(container2.adUnitId).append("\n");
            }
        }
        System.out.println("read ads config: \n" + (Object)log);
    }
    
    public void readUrl(final String url) {
        if (url != null) {
            try {
                final PendingInterrupt pendingInterrupt = new PendingInterrupt(7500);
                this.readJson(new JSONObject(FileUtils.convertStreamToString(new URL(url).openStream())));
                pendingInterrupt.cancel();
            }
            catch (IOException e) {
                e.printStackTrace();
                this.readJson(null);
            }
            catch (JSONException e2) {
                e2.printStackTrace();
                this.readJson(null);
            }
        }
        else {
            this.readJson(null);
        }
    }
    
    public double getAdTypeRatingWeight(final String type) {
        final Double value = this.adTypeRatingWeights.get(type);
        return (value != null) ? value : 1.0;
    }
    
    public int getAdTypeMaxAttempts(final String type) {
        final Integer value = this.maxLoadingAttemptsByType.get(type);
        return (value != null) ? value : 2;
    }
    
    private class PendingInterrupt extends Thread
    {
        boolean isCancelled;
        int timeout;
        Thread current;
        Thread thread;
        
        public PendingInterrupt(final int timeout) {
            this.isCancelled = false;
            this.current = Thread.currentThread();
            this.thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(PendingInterrupt.this.timeout);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!PendingInterrupt.this.isCancelled) {
                        PendingInterrupt.this.current.interrupt();
                    }
                }
            });
            this.timeout = timeout;
            this.thread.start();
        }
        
        public void cancel() {
            this.isCancelled = true;
        }
    }
}
