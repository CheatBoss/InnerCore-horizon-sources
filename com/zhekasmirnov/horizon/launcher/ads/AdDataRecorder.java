package com.zhekasmirnov.horizon.launcher.ads;

import com.zhekasmirnov.horizon.util.*;
import org.json.*;
import java.util.*;

public class AdDataRecorder
{
    public final AdsManager manager;
    private final HashMap<String, ProviderData> recordedAdData;
    
    public AdDataRecorder(final AdsManager manager) {
        this.recordedAdData = new HashMap<String, ProviderData>();
        this.manager = manager;
    }
    
    public void readDataFromJson(final JSONObject json) {
        synchronized (this.recordedAdData) {
            this.recordedAdData.clear();
            boolean isDataCorrupt = false;
            final JSONArray allData = json.optJSONArray("data");
            if (allData != null) {
                for (final JSONObject providerJson : new JsonIterator<JSONObject>(allData)) {
                    final ProviderData providerData = new ProviderData();
                    providerData.domainJson = providerJson.optJSONObject("domain");
                    Label_0129: {
                        if (providerData.domainJson != null) {
                            Label_0134: {
                                try {
                                    providerData.domain = new AdDomain(this.manager, providerData.domainJson);
                                    break Label_0134;
                                }
                                catch (IllegalArgumentException e) {
                                    isDataCorrupt = true;
                                    break;
                                }
                                break Label_0129;
                            }
                            final JSONObject ratingJson = providerJson.optJSONObject("rating");
                            if (ratingJson != null) {
                                for (final String adType : new JsonIterator<String>(ratingJson)) {
                                    if (adType != null) {
                                        final Object rating = ratingJson.opt(adType);
                                        if (!(rating instanceof Number)) {
                                            continue;
                                        }
                                        providerData.adDataByType.put(adType, ((Number)rating).intValue());
                                    }
                                }
                                this.recordedAdData.put(providerData.domain.providerId, providerData);
                                continue;
                            }
                            isDataCorrupt = true;
                            break;
                        }
                    }
                    isDataCorrupt = true;
                    break;
                }
            }
            if (isDataCorrupt) {
                this.recordedAdData.clear();
            }
            final StringBuilder log = new StringBuilder();
            log.append("recorded data: \n");
            final Iterator<ProviderData> iterator3 = this.recordedAdData.values().iterator();
            while (iterator3.hasNext()) {
                final ProviderData providerData = iterator3.next();
                log.append("    domain ").append(providerData.domain.providerId).append("\n");
                for (final String adType2 : providerData.adDataByType.keySet()) {
                    log.append("        ").append(adType2).append(": ").append(providerData.adDataByType.get(adType2)).append("\n");
                }
            }
            System.out.println(log);
        }
    }
    
    public JSONObject writeDataToJson() {
        synchronized (this.recordedAdData) {
            try {
                final JSONObject json = new JSONObject();
                final JSONArray allData = new JSONArray();
                json.put("data", (Object)allData);
                for (final ProviderData providerData : this.recordedAdData.values()) {
                    final JSONObject providerJson = new JSONObject();
                    if (providerData.domain != null) {
                        providerJson.put("domain", (Object)providerData.domain.toJson());
                    }
                    else {
                        if (providerData.domainJson == null) {
                            continue;
                        }
                        providerJson.put("domain", (Object)providerData.domainJson);
                    }
                    final JSONObject rating = new JSONObject();
                    providerJson.put("rating", (Object)rating);
                    for (final String adType : providerData.adDataByType.keySet()) {
                        rating.put(adType, (double)providerData.adDataByType.get(adType));
                    }
                    allData.put((Object)providerJson);
                }
                return json;
            }
            catch (JSONException ignore) {
                return null;
            }
        }
    }
    
    private void recordRatingAdd(final AdDomain domain, final String adType, final int change) {
        synchronized (this.recordedAdData) {
            ProviderData providerData = this.recordedAdData.get(domain.providerId);
            if (providerData == null) {
                providerData = new ProviderData();
                providerData.domain = domain;
                this.recordedAdData.put(domain.providerId, providerData);
            }
            final Integer value = providerData.adDataByType.get(adType);
            if (value != null) {
                providerData.adDataByType.put(adType, value + change);
            }
            else {
                providerData.adDataByType.put(adType, change);
            }
        }
    }
    
    public void record(final AdDomain desired, final AdDomain actual, final String adType) {
        if (desired != null && !desired.providerId.equals(actual.providerId)) {
            this.recordRatingAdd(desired, adType, 1);
            this.recordRatingAdd(actual, adType, -1);
        }
    }
    
    public void recordInvalidRequest(final AdDomain domain, final String adType) {
        if (domain != null) {
            synchronized (this.recordedAdData) {
                final ProviderData providerData = this.recordedAdData.get(domain.providerId);
                if (providerData != null) {
                    providerData.adDataByType.remove(adType);
                }
            }
        }
    }
    
    public AdDomain createWinningDataDomainSource() {
        final AdGlobalConfiguration config = this.manager.configuration;
        final HashMap<String, Double> ratingMap = new HashMap<String, Double>();
        final HashMap<String, AdDomain> domainMap = new HashMap<String, AdDomain>();
        if (Math.random() < config.trustedAdDomainChance) {
            for (final String providerId : config.trustedAdDomains.keySet()) {
                final AdDomain domain = config.trustedAdDomains.get(providerId);
                if (domain != null) {
                    final ProviderData providerData = this.recordedAdData.get(domain.providerId);
                    double rating = 0.0;
                    if (providerData != null) {
                        for (final String adType : providerData.adDataByType.keySet()) {
                            final Integer data = providerData.adDataByType.get(adType);
                            if (data != null) {
                                rating += data * config.getAdTypeRatingWeight(adType);
                            }
                        }
                    }
                    ratingMap.put(domain.providerId, rating);
                    domainMap.put(domain.providerId, domain);
                }
            }
            if (ratingMap.size() == 1) {
                for (final String key : ratingMap.keySet()) {
                    ratingMap.put(key, config.lowerRatingThreshold + 1.0);
                }
            }
        }
        else {
            for (final String providerId : this.recordedAdData.keySet()) {
                double rating2 = 0.0;
                final ProviderData providerData2 = this.recordedAdData.get(providerId);
                if (providerData2 != null) {
                    if (providerData2.domain == null) {
                        try {
                            providerData2.domain = new AdDomain(this.manager, providerData2.domainJson);
                            domainMap.put(providerData2.domain.providerId, providerData2.domain);
                        }
                        catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                    for (final String adType2 : providerData2.adDataByType.keySet()) {
                        final Integer data2 = providerData2.adDataByType.get(adType2);
                        if (data2 != null) {
                            rating2 += data2 * config.getAdTypeRatingWeight(adType2);
                        }
                    }
                }
                ratingMap.put(providerId, rating2);
            }
        }
        Double minimalRating = null;
        for (final Double value : ratingMap.values()) {
            if (value != null && (minimalRating == null || minimalRating > value)) {
                minimalRating = value;
            }
        }
        if (minimalRating != null) {
            for (final String key2 : new ArrayList<String>(ratingMap.keySet())) {
                ratingMap.put(key2, ratingMap.get(key2) - minimalRating);
            }
        }
        final List<String> sortedByRating = new ArrayList<String>();
        for (final String key3 : ratingMap.keySet()) {
            if (ratingMap.get(key3) >= config.lowerRatingThreshold) {
                sortedByRating.add(key3);
            }
        }
        Collections.sort(sortedByRating, new Comparator<String>() {
            @Override
            public int compare(final String provider1, final String provider2) {
                return Double.compare(ratingMap.get(provider2), ratingMap.get(provider1));
            }
        });
        if (sortedByRating.size() == 0) {
            return this.manager.newHardcodedMainDomain();
        }
        for (int i = 0; i < sortedByRating.size(); ++i) {
            if (i != sortedByRating.size() - 1) {
                final AdDomain domain2 = domainMap.get(sortedByRating.get(i));
                final Double currentRating = ratingMap.get(sortedByRating.get(i));
                final Double nextRating = ratingMap.get(sortedByRating.get(i + 1));
                if (domain2 == null || currentRating == null || nextRating == null) {
                    break;
                }
                if (Math.random() > nextRating / currentRating * config.lowerRatingProbabilityFactor) {
                    try {
                        return new AdDomain(this.manager, domain2.toJson());
                    }
                    catch (IllegalArgumentException e2) {
                        break;
                    }
                }
            }
            else {
                final AdDomain domain2 = domainMap.get(sortedByRating.get(i));
                if (domain2 != null) {
                    try {
                        return new AdDomain(this.manager, domain2.toJson());
                    }
                    catch (IllegalArgumentException e3) {
                        break;
                    }
                }
            }
        }
        return this.manager.newHardcodedMainDomain();
    }
    
    private class ProviderData
    {
        private AdDomain domain;
        private JSONObject domainJson;
        private HashMap<String, Integer> adDataByType;
        
        private ProviderData() {
            this.adDataByType = new HashMap<String, Integer>();
        }
    }
}
