package org.mineprogramming.horizon.innercore.inflater.drawable;

import android.content.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import org.json.*;
import android.graphics.drawable.*;
import java.util.*;

class InflatableStateListDrawable extends InflatableDrawable<StateListDrawable>
{
    private static Map<String, Integer> STATES;
    private StateListDrawable drawable;
    
    static {
        (InflatableStateListDrawable.STATES = new HashMap<String, Integer>()).put("state_empty", 16842921);
        InflatableStateListDrawable.STATES.put("state_activated", 16843518);
        InflatableStateListDrawable.STATES.put("state_active", 16842914);
        InflatableStateListDrawable.STATES.put("state_checkable", 16842911);
        InflatableStateListDrawable.STATES.put("state_checked", 16842912);
        InflatableStateListDrawable.STATES.put("state_enabled", 16842910);
        InflatableStateListDrawable.STATES.put("state_first", 16842916);
        InflatableStateListDrawable.STATES.put("state_focused", 16842908);
        InflatableStateListDrawable.STATES.put("state_focused", 16842918);
        InflatableStateListDrawable.STATES.put("state_focused", 16842917);
        InflatableStateListDrawable.STATES.put("state_pressed", 16842919);
        InflatableStateListDrawable.STATES.put("state_selected", 16842913);
        InflatableStateListDrawable.STATES.put("state_single", 16842915);
        InflatableStateListDrawable.STATES.put("state_single", 16842915);
        InflatableStateListDrawable.STATES.put("state_window_focused", 16842909);
        InflatableStateListDrawable.STATES.put("state_expanded", 16842920);
        InflatableStateListDrawable.STATES.put("state_hovered", 16843623);
    }
    
    public InflatableStateListDrawable(final Context context, final JSONObject jsonObject) throws JsonInflaterException {
        super(context, jsonObject);
        this.drawable = new StateListDrawable();
        final JSONArray optJSONArray = jsonObject.optJSONArray("items");
    Label_0212:
        for (int i = 0; i < optJSONArray.length(); ++i) {
            while (true) {
                final ArrayList<Integer> list = new ArrayList<Integer>();
                while (true) {
                    Label_0213: {
                        try {
                            final JSONObject jsonObject2 = optJSONArray.getJSONObject(i);
                            final Drawable inflateDrawable = DrawableInflater.inflateDrawable(context, jsonObject2);
                            final Iterator<String> iterator = InflatableStateListDrawable.STATES.keySet().iterator();
                            if (!iterator.hasNext()) {
                                final int[] array = new int[list.size()];
                                for (int j = 0; j < array.length; ++j) {
                                    array[j] = (int)list.get(j);
                                }
                                this.drawable.addState(array, inflateDrawable);
                                break;
                            }
                            final String s = iterator.next();
                            if (jsonObject2.optString(s).equals("true")) {
                                list.add(InflatableStateListDrawable.STATES.get(s));
                                break Label_0213;
                            }
                            break Label_0213;
                        }
                        catch (JSONException ex) {
                            throw new JsonInflaterException("Unable to inflate layer item", (Throwable)ex);
                        }
                        break Label_0212;
                    }
                    continue;
                }
            }
        }
    }
    
    @Override
    public StateListDrawable getDrawable() {
        return this.drawable;
    }
}
