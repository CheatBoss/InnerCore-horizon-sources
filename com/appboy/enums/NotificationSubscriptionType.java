package com.appboy.enums;

import com.appboy.models.*;

public enum NotificationSubscriptionType implements IPutIntoJson<String>
{
    OPTED_IN, 
    SUBSCRIBED, 
    UNSUBSCRIBED;
    
    @Override
    public String forJsonPut() {
        final int n = NotificationSubscriptionType$1.a[this.ordinal()];
        if (n == 1) {
            return "unsubscribed";
        }
        if (n == 2) {
            return "subscribed";
        }
        if (n != 3) {
            return null;
        }
        return "opted_in";
    }
}
