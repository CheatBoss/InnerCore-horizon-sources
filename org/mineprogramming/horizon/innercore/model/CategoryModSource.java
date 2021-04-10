package org.mineprogramming.horizon.innercore.model;

import org.mineprogramming.horizon.innercore.util.*;
import java.util.*;
import org.json.*;

public class CategoryModSource extends RemoteItemSource
{
    private final String GATEWAY_URL;
    private DownloadHandler downloadHandler;
    private String lastUrl;
    private int start;
    
    public CategoryModSource(final String s, final String s2) {
        this.start = 0;
        final StringBuilder sb = new StringBuilder();
        sb.append("https://icmods.mineprogramming.org/api/list?horizon&lang=");
        sb.append(s);
        sb.append("&count=20&sort=");
        sb.append(s2);
        sb.append("&start=");
        this.GATEWAY_URL = sb.toString();
        this.requestMore();
    }
    
    @Override
    void processData(final String s) throws JSONException {
        final JSONArray jsonArray = new JSONArray(s);
        final Iterator<JSONObject> jsonIterator = JSONUtils.getJsonIterator(jsonArray);
        while (jsonIterator.hasNext()) {
            this.addItem(new ModItem(jsonIterator.next()));
        }
        if (jsonArray.length() < 20) {
            this.notifyLoadLast();
        }
    }
    
    @Override
    public void requestMore() {
        this.downloadHandler = new DownloadHandler();
        final StringBuilder sb = new StringBuilder();
        sb.append(this.GATEWAY_URL);
        sb.append(this.start);
        this.lastUrl = sb.toString();
        this.downloadHandler.execute((Object[])new String[] { this.lastUrl });
        this.start += 20;
    }
    
    @Override
    public void retryLoad() {
        (this.downloadHandler = new DownloadHandler()).execute((Object[])new String[] { this.lastUrl });
    }
}
