package org.mineprogramming.horizon.innercore.util;

import java.util.*;
import org.json.*;

public class JSONUtils
{
    public static Iterator<JSONObject> getJsonIterator(final JSONArray jsonArray) {
        return new Iterator<JSONObject>() {
            private int i = 0;
            
            @Override
            public boolean hasNext() {
                return this.i < jsonArray.length();
            }
            
            @Override
            public JSONObject next() {
                return jsonArray.optJSONObject(this.i++);
            }
        };
    }
    
    public static Iterator<JSONObject> getJsonIterator(final JSONObject jsonObject) {
        return new Iterator<JSONObject>() {
            private int i = 0;
            final /* synthetic */ JSONArray val$names = jsonObject.names();
            
            @Override
            public boolean hasNext() {
                return this.i < this.val$names.length();
            }
            
            @Override
            public JSONObject next() {
                return jsonObject.optJSONObject(this.val$names.optString(this.i++));
            }
        };
    }
}
