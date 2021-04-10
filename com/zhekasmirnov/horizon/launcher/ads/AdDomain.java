package com.zhekasmirnov.horizon.launcher.ads;

import com.zhekasmirnov.horizon.util.*;
import org.json.*;
import android.util.*;
import java.util.*;

public class AdDomain
{
    public final String providerId;
    public final AdsManager manager;
    private final List<AdContainer> containers;
    private final List<AsyncAdRequest> asyncAdRequests;
    
    public AdDomain(final AdsManager manager, final String providerId) {
        this.containers = new ArrayList<AdContainer>();
        this.asyncAdRequests = new ArrayList<AsyncAdRequest>();
        this.manager = manager;
        this.providerId = providerId;
    }
    
    public AdDomain(final AdsManager adsManager, final JSONObject jsonObject) {
        this.containers = new ArrayList<AdContainer>();
        this.asyncAdRequests = new ArrayList<AsyncAdRequest>();
        this.manager = adsManager;
        this.providerId = jsonObject.optString("provider");
        if (this.providerId == null || this.providerId.length() == 0) {
            throw new IllegalArgumentException();
        }
        final JSONObject adUnits = jsonObject.optJSONObject("units");
        if (adUnits != null) {
            final List<AdContainer> containers = new ArrayList<AdContainer>();
            for (final String adType : new JsonIterator<String>(adUnits)) {
                final JSONArray adUnitsOfType = adUnits.optJSONArray(adType);
                if (adUnitsOfType != null) {
                    for (final String adUnit : new JsonIterator<String>(adUnitsOfType)) {
                        if (adUnit != null) {
                            containers.add(AdContainer.newContainer(adsManager, adType, adUnit));
                        }
                    }
                }
            }
            this.addContainers(containers);
        }
    }
    
    public JSONObject toJson() {
        try {
            final JSONObject json = new JSONObject();
            json.put("provider", (Object)this.providerId);
            final JSONObject units = new JSONObject();
            json.put("units", (Object)units);
            synchronized (this.containers) {
                for (final AdContainer container : this.containers) {
                    final String type = container.getAdType();
                    final String unit = container.adUnitId;
                    JSONArray unitsByType = units.optJSONArray(type);
                    if (unitsByType == null) {
                        unitsByType = new JSONArray();
                        units.put(type, (Object)unitsByType);
                    }
                    unitsByType.put((Object)unit);
                }
            }
            return json;
        }
        catch (JSONException ignore) {
            return null;
        }
    }
    
    public void addContainers(final Collection<AdContainer> add) {
        synchronized (this.containers) {
            this.containers.addAll(add);
        }
    }
    
    public List<AdContainer> getContainers() {
        return this.containers;
    }
    
    public void beginAdLoading(final String... types) {
        synchronized (this.containers) {
            for (final AdContainer container : this.containers) {
                boolean isSameType = true;
                if (types != null && types.length != 0) {
                    isSameType = false;
                    final String type = container.getAdType();
                    for (final String t : types) {
                        if (t.equals(type)) {
                            isSameType = true;
                            break;
                        }
                    }
                }
                if (isSameType && container.getState() != AdContainer.State.LOADING && container.getState() != AdContainer.State.LOADED) {
                    container.load(new AdContainer.Listener() {
                        @Override
                        public void onLoaded(final AdContainer container) {
                            AdDomain.this.onAdContainerLoaded(container);
                        }
                        
                        @Override
                        public void onFailedToLoad(final AdContainer container, final int reason) {
                            AdDomain.this.onAdContainerFailedToLoad(container, reason);
                        }
                    });
                }
            }
        }
    }
    
    public List<AdContainer> getAllContainersWithStatesAndTypes(final AdContainer.State[] states, final String[] types) {
        synchronized (this.containers) {
            final List<AdContainer> result = new ArrayList<AdContainer>();
            for (final AdContainer container : this.containers) {
                boolean add = true;
                if (types != null && types.length != 0) {
                    add = false;
                    final String type = container.getAdType();
                    for (final String t : types) {
                        if (t.equals(type)) {
                            add = true;
                            break;
                        }
                    }
                }
                if (add && states != null && states.length != 0) {
                    add = false;
                    final AdContainer.State state = container.getState();
                    for (final AdContainer.State s : states) {
                        if (s.equals(state)) {
                            add = true;
                            break;
                        }
                    }
                }
                if (add) {
                    result.add(container);
                }
            }
            return result;
        }
    }
    
    public int getLoadedCount(final String... types) {
        return this.getAllContainersWithStatesAndTypes(new AdContainer.State[] { AdContainer.State.LOADED }, types).size();
    }
    
    public int getLoadingCount(final String... types) {
        return this.getAllContainersWithStatesAndTypes(new AdContainer.State[] { AdContainer.State.LOADING }, types).size();
    }
    
    private static String listToString(final String[] list) {
        final StringBuilder builder = new StringBuilder();
        for (final String str : list) {
            builder.append(str).append(" ");
        }
        return builder.toString();
    }
    
    public boolean awaitLoadingOfAds(final int count, final int maxWaitTime, final List<AdContainer> result, final String... types) {
        final long started = System.currentTimeMillis();
        List<AdContainer> loaded;
        while ((loaded = this.getAllContainersWithStatesAndTypes(new AdContainer.State[] { AdContainer.State.LOADED }, types)).size() < count) {
            try {
                Thread.sleep(64L);
                if (System.currentTimeMillis() - started > maxWaitTime) {
                    Log.i("AdLoader", "ad loading waiting time exceeded limit of " + maxWaitTime + "ms (awaiting " + count + " of " + listToString(types) + ")");
                    if (result != null) {
                        result.addAll(loaded);
                    }
                    return false;
                }
                continue;
            }
            catch (InterruptedException e) {
                Log.i("AdLoader", "ad loading interrupted with exception (awaiting " + count + " of " + listToString(types) + ")");
                e.printStackTrace();
                if (result != null) {
                    result.addAll(loaded);
                }
                return false;
            }
            break;
        }
        if (result != null) {
            result.addAll(loaded);
        }
        return true;
    }
    
    public AdContainer require(final String type) {
        final List<AdContainer> containers = this.getAllContainersWithStatesAndTypes(new AdContainer.State[] { AdContainer.State.LOADED }, new String[] { type });
        return (containers.size() > 0) ? containers.get(0) : null;
    }
    
    private void onAdContainerLoaded(final AdContainer container) {
        for (final AsyncAdRequest request : new ArrayList<AsyncAdRequest>(this.asyncAdRequests)) {
            this.tryToFulfillRequest(request);
        }
    }
    
    private void onAdContainerFailedToLoad(final AdContainer container, final int reason) {
        for (final AsyncAdRequest request : new ArrayList<AsyncAdRequest>(this.asyncAdRequests)) {
            this.tryToFulfillRequest(request);
        }
    }
    
    private void tryToFulfillRequest(final AsyncAdRequest request) {
        synchronized (this.containers) {
            final List<AdContainer> containers = this.getAllContainersWithStatesAndTypes(new AdContainer.State[] { AdContainer.State.LOADED, AdContainer.State.LOADING }, new String[] { request.adType });
            AdContainer bestContainer = null;
            AdContainer bestLoadedContainer = null;
            for (final AdContainer container : containers) {
                if (bestContainer == null || bestContainer.useCount > container.useCount) {
                    bestContainer = container;
                }
                if (container.getState() == AdContainer.State.LOADED && (bestLoadedContainer == null || bestLoadedContainer.useCount > container.useCount)) {
                    bestLoadedContainer = container;
                }
            }
            if (request.isFancy) {
                if (bestContainer != null && bestContainer.getState() == AdContainer.State.LOADED) {
                    request.onFulfilled(bestContainer);
                }
            }
            else if (bestLoadedContainer != null) {
                request.onFulfilled(bestLoadedContainer);
            }
            if (containers.size() == 0) {
                final List<AdContainer> allContainers = this.getAllContainersWithStatesAndTypes(null, new String[] { request.adType });
                final List<AdContainer> failedContainers = new ArrayList<AdContainer>();
                final HashMap<Integer, Integer> failReasons = new HashMap<Integer, Integer>();
                for (final AdContainer container2 : allContainers) {
                    if (container2.getState() != AdContainer.State.FAILED) {
                        return;
                    }
                    failedContainers.add(container2);
                    final Integer fails = failReasons.get(container2.getFailReason());
                    if (fails != null) {
                        failReasons.put(container2.getFailReason(), fails + 1);
                    }
                    else {
                        failReasons.put(container2.getFailReason(), 1);
                    }
                }
                if (failedContainers.size() > 0) {
                    request.onFailed(failedContainers, failReasons);
                }
                else if (allContainers.size() == 0) {
                    request.onFailed(failedContainers, failReasons);
                }
            }
        }
    }
    
    public void useContainer(final AdContainer container) {
        if (!this.containers.contains(container)) {
            throw new IllegalArgumentException("passed container from another domain");
        }
        ++container.useCount;
    }
    
    public AsyncAdRequest requireAsync(final String type, final boolean fancy, final AsyncAdRequestListener listener) {
        this.beginAdLoading(type);
        final AsyncAdRequest request = new AsyncAdRequest(type, fancy, listener);
        this.tryToFulfillRequest(request);
        return request;
    }
    
    public void closeAllRequests() {
        synchronized (this.asyncAdRequests) {
            for (final AsyncAdRequest request : new ArrayList<AsyncAdRequest>(this.asyncAdRequests)) {
                request.close();
            }
        }
    }
    
    public static class AsyncAdRequestListener
    {
        public void onFulfilled(final AdContainer container) {
        }
        
        public void onClosed() {
        }
        
        public void onFailed(final List<AdContainer> failedContainers, final HashMap<Integer, Integer> failReasons) {
        }
    }
    
    public class AsyncAdRequest
    {
        public final String adType;
        private AsyncAdRequestListener listener;
        private int timeout;
        private boolean isActive;
        private boolean isFancy;
        
        public AsyncAdRequest(final String adType, final boolean isFancy, final AsyncAdRequestListener listener) {
            this.timeout = -1;
            this.isActive = true;
            this.adType = adType;
            this.isFancy = isFancy;
            this.listener = listener;
            synchronized (AdDomain.this.asyncAdRequests) {
                AdDomain.this.asyncAdRequests.add(this);
            }
        }
        
        private void onFulfilled(final AdContainer container) {
            if (this.isActive) {
                this.isActive = false;
                if (this.listener != null) {
                    AdDomain.this.useContainer(container);
                    this.listener.onFulfilled(container);
                }
            }
            synchronized (AdDomain.this.asyncAdRequests) {
                AdDomain.this.asyncAdRequests.remove(this);
            }
        }
        
        private void onClosed() {
            this.isActive = false;
            if (this.listener != null) {
                this.listener.onClosed();
            }
            synchronized (AdDomain.this.asyncAdRequests) {
                AdDomain.this.asyncAdRequests.remove(this);
            }
        }
        
        private void onFailed(final List<AdContainer> failedContainers, final HashMap<Integer, Integer> failReasons) {
            this.isActive = false;
            if (this.listener != null) {
                this.listener.onFailed(failedContainers, failReasons);
            }
            synchronized (AdDomain.this.asyncAdRequests) {
                AdDomain.this.asyncAdRequests.remove(this);
            }
        }
        
        public boolean isActive() {
            return this.isActive;
        }
        
        public boolean isFancy() {
            return this.isFancy;
        }
        
        public synchronized void close() {
            if (this.isActive) {
                this.isActive = false;
                this.onClosed();
            }
        }
        
        public synchronized AsyncAdRequest setFancyTimeout(final int time) {
            if (this.timeout != -1) {
                throw new IllegalStateException();
            }
            this.timeout = time;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(AsyncAdRequest.this.timeout);
                    }
                    catch (InterruptedException ex) {}
                    AsyncAdRequest.this.isFancy = false;
                    if (AsyncAdRequest.this.isActive) {
                        AdDomain.this.beginAdLoading(AsyncAdRequest.this.adType);
                        AdDomain.this.tryToFulfillRequest(AsyncAdRequest.this);
                    }
                }
            }).start();
            return this;
        }
    }
}
