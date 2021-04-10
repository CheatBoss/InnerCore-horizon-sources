package com.microsoft.aad.adal;

import android.util.*;
import java.util.*;

final class UIEvent extends DefaultEvent
{
    UIEvent(final String s) {
        this.getEventList().add((Pair<String, String>)Pair.create((Object)"Microsoft.ADAL.event_name", (Object)s));
    }
    
    @Override
    public void processEvent(final Map<String, String> map) {
        final List<Pair<String, String>> eventList = this.getEventList();
        final String s = map.get("Microsoft.ADAL.ui_event_count");
        if (s == null) {
            map.put("Microsoft.ADAL.ui_event_count", "1");
        }
        else {
            map.put("Microsoft.ADAL.ui_event_count", Integer.toString(Integer.parseInt(s) + 1));
        }
        if (map.containsKey("Microsoft.ADAL.user_cancel")) {
            map.put("Microsoft.ADAL.user_cancel", "");
        }
        if (map.containsKey("Microsoft.ADAL.ntlm")) {
            map.put("Microsoft.ADAL.ntlm", "");
        }
        for (final Pair<String, String> pair : eventList) {
            final String s2 = (String)pair.first;
            if (s2.equals("Microsoft.ADAL.user_cancel") || s2.equals("Microsoft.ADAL.ntlm")) {
                map.put(s2, (String)pair.second);
            }
        }
    }
    
    void setNTLM(final boolean b) {
        this.setProperty("Microsoft.ADAL.ntlm", String.valueOf(b));
    }
    
    void setRedirectCount(final Integer n) {
        this.setProperty("Microsoft.ADAL.redirect_count", n.toString());
    }
    
    void setUserCancel() {
        this.setProperty("Microsoft.ADAL.user_cancel", "true");
    }
}
