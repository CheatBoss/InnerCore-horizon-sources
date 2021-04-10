package org.mineprogramming.horizon.innercore.model;

import org.mineprogramming.horizon.innercore.util.*;
import java.util.*;
import org.json.*;
import java.nio.charset.*;
import java.net.*;

public class SearchModSource extends RemoteItemSource
{
    public static final String SEARCH_MODE_AUTHOR = "author";
    public static final String SEARCH_MODE_QUERY = "q";
    public static final String SEARCH_MODE_TAG = "tag";
    private final String GATEWAY_URL;
    private DownloadHandler downloadHandler;
    private String query;
    
    public SearchModSource(final String s, final String s2, final String query) {
        final StringBuilder sb = new StringBuilder();
        sb.append("https://icmods.mineprogramming.org/api/search.php?horizon&lang=");
        sb.append(s);
        sb.append("&");
        sb.append(s2);
        sb.append("=");
        this.GATEWAY_URL = sb.toString();
        this.setQuery(query);
    }
    
    @Override
    void processData(final String s) throws JSONException {
        Iterator<JSONObject> iterator;
        if (s.trim().startsWith("{")) {
            iterator = JSONUtils.getJsonIterator(new JSONObject(s));
        }
        else {
            iterator = JSONUtils.getJsonIterator(new JSONArray(s));
        }
        while (iterator.hasNext()) {
            this.addItem(new ModItem(iterator.next()));
        }
        this.notifyLoadLast();
    }
    
    @Override
    public void retryLoad() {
        this.setQuery(this.query);
    }
    
    public void setQuery(String query) {
        if (this.downloadHandler != null) {
            this.downloadHandler.cancel(true);
        }
        this.downloadHandler = new DownloadHandler();
        this.clearItems();
        this.query = query;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.GATEWAY_URL);
            sb.append(URLEncoder.encode(query, StandardCharsets.UTF_8.toString()));
            query = sb.toString();
        }
        catch (Exception ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(this.GATEWAY_URL);
            sb2.append(URLEncoder.encode(query));
            query = sb2.toString();
        }
        this.downloadHandler.execute((Object[])new String[] { query });
    }
}
