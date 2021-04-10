package org.mineprogramming.horizon.innercore.model;

import com.android.tools.r8.annotations.*;
import android.text.*;
import android.os.*;
import org.mineprogramming.horizon.innercore.util.*;
import org.json.*;
import java.util.*;
import com.zhekasmirnov.innercore.modpack.*;

@SynthesizedClassMap({ -$$Lambda$UpdateSource$4tQOf9HW3FlfrnEDfv7biVWOPvo.class })
public class UpdateSource
{
    private static UpdateSource instance;
    private Map<Integer, Integer> currentVersions;
    private List<UpdatesListener> listeners;
    private List<Item> modItems;
    private List<Item> packItems;
    private boolean ready;
    private final String urlBase;
    
    private UpdateSource(final String s) {
        this.listeners = new ArrayList<UpdatesListener>();
        this.ready = false;
        final StringBuilder sb = new StringBuilder();
        sb.append("https://icmods.mineprogramming.org/api/list?horizon&lang=");
        sb.append(s);
        sb.append("&ids=");
        this.urlBase = sb.toString();
    }
    
    private String getIds() {
        return TextUtils.join((CharSequence)",", (Iterable)this.currentVersions.keySet());
    }
    
    public static UpdateSource getInstance(final String s) {
        if (UpdateSource.instance == null) {
            UpdateSource.instance = new UpdateSource(s);
        }
        return UpdateSource.instance;
    }
    
    private void loadRemote() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.urlBase);
        sb.append(this.getIds());
        new AsyncTask<String, Void, String>() {
            protected String doInBackground(final String... array) {
                try {
                    return DownloadHelper.downloadString(array[0]);
                }
                catch (Exception ex) {
                    return null;
                }
            }
            
            protected void onPostExecute(final String s) {
                super.onPostExecute((Object)s);
                if (s == null) {
                    UpdateSource.this.onDownloadFailed();
                    return;
                }
                try {
                    UpdateSource.this.onRemoteLoaded(new JSONArray(s));
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                    UpdateSource.this.onDownloadFailed();
                }
            }
        }.execute((Object[])new String[] { sb.toString() });
    }
    
    private void onDownloadFailed() {
        new Handler().postDelayed((Runnable)new -$$Lambda$UpdateSource$4tQOf9HW3FlfrnEDfv7biVWOPvo(this), 5000L);
    }
    
    private void onRemoteLoaded(final JSONArray jsonArray) {
        final Iterator<JSONObject> jsonIterator = JSONUtils.getJsonIterator(jsonArray);
        this.packItems = new ArrayList<Item>();
        this.modItems = new ArrayList<Item>();
        while (jsonIterator.hasNext()) {
            final JSONObject jsonObject = jsonIterator.next();
            if (this.currentVersions.get(jsonObject.optInt("id")) < jsonObject.optInt("version")) {
                if (jsonObject.optInt("pack") == 1) {
                    this.packItems.add(new ModPackItem(jsonObject));
                }
                else {
                    this.modItems.add(new ModItem(jsonObject));
                }
            }
        }
        this.ready = true;
        this.notifyListeners();
    }
    
    public void addUpdateListener(final UpdatesListener updatesListener) {
        this.listeners.add(updatesListener);
    }
    
    public int getItemsCount() {
        return this.packItems.size() + this.modItems.size();
    }
    
    public Item getModItem(final int n) {
        return this.modItems.get(n);
    }
    
    public int getModItemsCount() {
        return this.modItems.size();
    }
    
    public Item getPackItem(final int n) {
        return this.packItems.get(n);
    }
    
    public int getPackItemsCount() {
        return this.packItems.size();
    }
    
    public boolean isReady() {
        return this.ready;
    }
    
    public boolean needsUpdate(final int n) {
        final Iterator<Item> iterator = this.modItems.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId() == n) {
                return true;
            }
        }
        final Iterator<Item> iterator2 = this.packItems.iterator();
        while (iterator2.hasNext()) {
            if (iterator2.next().getId() == n) {
                return true;
            }
        }
        return false;
    }
    
    public void notifyListeners() {
        final int size = this.packItems.size();
        final int size2 = this.modItems.size();
        final Iterator<UpdatesListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onUpdatesCountChanged(size + size2);
        }
    }
    
    public void onUpdateOrDelete(final int n) {
        if (!this.isReady()) {
            return;
        }
        final Iterator<Item> iterator = this.modItems.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId() == n) {
                iterator.remove();
            }
        }
        final Iterator<Item> iterator2 = this.packItems.iterator();
        while (iterator2.hasNext()) {
            if (iterator2.next().getId() == n) {
                iterator2.remove();
            }
        }
        this.notifyListeners();
    }
    
    public void removeUpdateListener(final UpdatesListener updatesListener) {
        this.listeners.remove(updatesListener);
    }
    
    public void requestUpdates(final UpdatesListener updatesListener) {
        if (updatesListener != null) {
            this.listeners.add(updatesListener);
        }
        final ModPackContext instance = ModPackContext.getInstance();
        final List allModPacks = instance.getStorage().getAllModPacks();
        this.currentVersions = new HashMap<Integer, Integer>();
        final Iterator<ModPack> iterator = allModPacks.iterator();
        while (iterator.hasNext()) {
            final ModPackPreferences preferences = iterator.next().getPreferences();
            if (!preferences.getBoolean("modified", true)) {
                final int int1 = preferences.getInt("icmods_id", 0);
                final int int2 = preferences.getInt("icmods_version", 0);
                if (int1 == 0) {
                    continue;
                }
                this.currentVersions.put(int1, int2);
            }
        }
        if (instance.getCurrentModPack().getPreferences().getBoolean("modified", true)) {
            this.currentVersions.putAll(ModTracker.getCurrent().getVersions());
        }
        this.loadRemote();
    }
    
    public interface UpdatesListener
    {
        void onUpdatesCountChanged(final int p0);
    }
}
