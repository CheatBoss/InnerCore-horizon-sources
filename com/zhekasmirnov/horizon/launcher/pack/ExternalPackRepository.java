package com.zhekasmirnov.horizon.launcher.pack;

import java.net.*;
import com.zhekasmirnov.horizon.util.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import java.io.*;
import org.json.*;
import java.util.*;

public class ExternalPackRepository extends PackRepository
{
    private final String manifestUrl;
    private final List<String> uuids;
    private final List<String> suggestions;
    private final HashMap<String, IPackLocation> locationMap;
    
    public ExternalPackRepository(final String manifestUrl) {
        this.uuids = new ArrayList<String>();
        this.suggestions = new ArrayList<String>();
        this.locationMap = new HashMap<String, IPackLocation>();
        this.manifestUrl = manifestUrl;
    }
    
    @Override
    public synchronized void fetch() {
        try {
            final JSONObject packsJson = new JSONObject(FileUtils.convertStreamToString(new URL(this.manifestUrl).openStream()));
            final JSONArray packsList = packsJson.getJSONArray("packs");
            this.locationMap.clear();
            this.uuids.clear();
            if (packsList != null) {
                for (final JSONObject packJson : new JsonIterator<JSONObject>(packsList)) {
                    final String uuid = packJson.optString("uuid");
                    final String packageUrl = packJson.optString("package");
                    final String manifestUrl = packJson.optString("manifest");
                    final String graphicsUrl = packJson.optString("graphics");
                    final String changelogUrl = LocaleUtils.resolveLocaleJsonProperty(packJson, "changelog");
                    if (uuid != null && packageUrl != null && manifestUrl != null) {
                        final ExternalPackLocation location = new ExternalPackLocation(packageUrl, manifestUrl, graphicsUrl);
                        location.setUUID(uuid);
                        location.setChangelogUrl(changelogUrl);
                        this.locationMap.put(uuid, location);
                        this.uuids.add(uuid);
                        this.suggestions.add(uuid);
                    }
                    else {
                        Logger.error("failed to read pack description json: " + packJson);
                    }
                }
            }
            this.suggestions.clear();
            final JSONArray suggestedUUIDS = packsJson.optJSONArray("suggestions");
            if (suggestedUUIDS != null) {
                for (final String suggested : new JsonIterator<String>(suggestedUUIDS)) {
                    if (suggested != null) {
                        this.suggestions.add(suggested);
                    }
                }
            }
        }
        catch (IOException e) {
            Logger.error("failed to read external packs manifest from " + this.manifestUrl + " error: " + e);
        }
        catch (JSONException e2) {
            Logger.error("failed to read external packs manifest from " + this.manifestUrl + " error:  " + e2);
        }
    }
    
    @Override
    public List<String> getAllPacksUUIDs() {
        return this.uuids;
    }
    
    @Override
    public IPackLocation getLocationForUUID(final String uuid) {
        return this.locationMap.get(uuid);
    }
    
    @Override
    public List<String> getPackSuggestions() {
        return this.suggestions;
    }
}
