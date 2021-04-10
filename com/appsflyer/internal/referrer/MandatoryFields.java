package com.appsflyer.internal.referrer;

import java.util.*;

public class MandatoryFields extends HashMap<String, Object>
{
    public MandatoryFields() {
        ((AbstractMap<String, String>)this).put("type", "store");
    }
}
