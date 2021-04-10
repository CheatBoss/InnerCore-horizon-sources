package com.microsoft.aad.adal;

import android.util.*;
import java.util.*;

final class CacheEvent extends DefaultEvent
{
    private final String mEventName;
    
    CacheEvent(final String mEventName) {
        this.setProperty("Microsoft.ADAL.event_name", this.mEventName = mEventName);
    }
    
    @Override
    public void processEvent(final Map<String, String> map) {
        if (this.mEventName != "Microsoft.ADAL.token_cache_lookup") {
            return;
        }
        final List<Pair<String, String>> eventList = this.getEventList();
        final String s = map.get("Microsoft.ADAL.cache_event_count");
        if (s == null) {
            map.put("Microsoft.ADAL.cache_event_count", "1");
        }
        else {
            map.put("Microsoft.ADAL.cache_event_count", Integer.toString(Integer.parseInt(s) + 1));
        }
        map.put("Microsoft.ADAL.is_frt", "");
        map.put("Microsoft.ADAL.is_mrrt", "");
        map.put("Microsoft.ADAL.is_rt", "");
        if (map.containsKey("Microsoft.ADAL.spe_info")) {
            map.remove("Microsoft.ADAL.spe_info");
        }
        for (final Pair<String, String> pair : eventList) {
            final String s2 = (String)pair.first;
            if (s2.equals("Microsoft.ADAL.is_frt") || s2.equals("Microsoft.ADAL.is_rt") || s2.equals("Microsoft.ADAL.is_mrrt") || s2.equals("Microsoft.ADAL.spe_info")) {
                map.put(s2, (String)pair.second);
            }
        }
    }
    
    void setSpeRing(final String s) {
        if (!StringExtensions.isNullOrBlank(s)) {
            this.setProperty("Microsoft.ADAL.spe_info", s.trim());
        }
    }
    
    void setTokenType(final String s) {
        this.getEventList().add((Pair<String, String>)Pair.create((Object)"Microsoft.ADAL.token_type", (Object)s));
    }
    
    void setTokenTypeFRT(final boolean b) {
        this.setProperty("Microsoft.ADAL.is_frt", String.valueOf(b));
    }
    
    void setTokenTypeMRRT(final boolean b) {
        this.setProperty("Microsoft.ADAL.is_mrrt", String.valueOf(b));
    }
    
    void setTokenTypeRT(final boolean b) {
        this.setProperty("Microsoft.ADAL.is_rt", String.valueOf(b));
    }
}
