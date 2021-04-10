package com.zhekasmirnov.apparatus.multiplayer.mod;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.multiplayer.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;
import com.zhekasmirnov.apparatus.modloader.*;
import java.util.*;
import org.json.*;

@SynthesizedClassMap({ -$$Lambda$MultiplayerModList$FdFqKkMm66LxXtLCnBaM-8Bz-M0.class, -$$Lambda$MultiplayerModList$gqEhIbU216MttcP8q-H84epi5go.class })
public class MultiplayerModList
{
    private static final MultiplayerModList singleton;
    private final List<ApparatusMod> modList;
    
    static {
        singleton = new MultiplayerModList();
        Network.getSingleton().addClientInitializationPacket("system.mod_list", (Network.ClientInitializationPacketSender)-$$Lambda$MultiplayerModList$gqEhIbU216MttcP8q-H84epi5go.INSTANCE, -$$Lambda$MultiplayerModList$FdFqKkMm66LxXtLCnBaM-8Bz-M0.INSTANCE);
    }
    
    private MultiplayerModList() {
        this.modList = new ArrayList<ApparatusMod>();
    }
    
    public static MultiplayerModList getSingleton() {
        return MultiplayerModList.singleton;
    }
    
    public static void loadClass() {
    }
    
    public void add(final ApparatusMod apparatusMod) {
        this.modList.add(apparatusMod);
    }
    
    public boolean checkMultiplayerAllowed() {
        boolean b = true;
        final StringBuilder sb = new StringBuilder();
        final Iterator<ApparatusMod> iterator = this.modList.iterator();
        while (iterator.hasNext()) {
            final ApparatusModInfo info = iterator.next().getInfo();
            if (!info.getBoolean("multiplayer_supported")) {
                sb.append(info.getString("displayed_name"));
                sb.append("\n");
                b = false;
            }
        }
        if (b) {
            return true;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(UserDialogLocalization.getLocalizedString("multiplayer_support_warning"));
        sb2.append((Object)sb);
        return true ^ UserDialog.awaitDecision("Warning", sb2.toString(), "LEAVE", "CONTINUE ANYWAY");
    }
    
    public void clear() {
        this.modList.clear();
    }
    
    public String compareToJson(final JSONObject jsonObject) {
        final JSONArray optJSONArray = jsonObject.optJSONArray("list");
        if (optJSONArray == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid mod list packet ");
            sb.append(jsonObject);
            return sb.toString();
        }
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        final HashMap<Object, String> hashMap2 = new HashMap<Object, String>();
        for (int i = 0; i < optJSONArray.length(); ++i) {
            final JSONObject optJSONObject = optJSONArray.optJSONObject(i);
            if (optJSONObject == null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("invalid mod list packet ");
                sb2.append(jsonObject);
                return sb2.toString();
            }
            hashMap.put(optJSONObject.optString("name"), optJSONObject.optString("version"));
        }
        final Iterator<ApparatusMod> iterator = this.modList.iterator();
        while (iterator.hasNext()) {
            final ApparatusModInfo info = iterator.next().getInfo();
            if (!info.getBoolean("client_only")) {
                hashMap2.put(info.getString("name"), info.getString("version"));
            }
        }
        boolean b = true;
        final StringBuilder sb3 = new StringBuilder();
        final StringBuilder sb4 = new StringBuilder();
        final StringBuilder sb5 = new StringBuilder();
        for (final Map.Entry<String, String> entry : hashMap.entrySet()) {
            final String s = entry.getKey();
            final String s2 = entry.getValue();
            final String s3 = hashMap2.get(s);
            if (s3 == null) {
                sb4.append(s);
                sb4.append(":");
                sb4.append(s2);
                sb4.append("\n");
                b = false;
            }
            else {
                if (s3.equals(s2)) {
                    continue;
                }
                sb5.append(s);
                sb5.append(":");
                sb5.append(s2);
                sb5.append(", server version: ");
                sb5.append(s3);
                sb5.append("\n");
                b = false;
            }
        }
        for (final Map.Entry<String, String> entry2 : hashMap2.entrySet()) {
            final String s4 = entry2.getKey();
            final String s5 = entry2.getValue();
            if (hashMap.get(s4) == null) {
                sb3.append(s4);
                sb3.append(":");
                sb3.append(s5);
                sb3.append("\n");
                b = false;
            }
        }
        if (!b) {
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("{{loc: multiplayer_mod_mismatch}}\n\n");
            String string;
            if (sb3.length() > 0) {
                final StringBuilder sb7 = new StringBuilder();
                sb7.append("{{loc: multiplayer_mod_missing}}:\n");
                sb7.append((Object)sb3);
                sb7.append("\n");
                string = sb7.toString();
            }
            else {
                string = "";
            }
            sb6.append(string);
            String string2;
            if (sb4.length() > 0) {
                final StringBuilder sb8 = new StringBuilder();
                sb8.append("{{loc: multiplayer_mod_excess}}\n");
                sb8.append((Object)sb4);
                sb8.append("\n");
                string2 = sb8.toString();
            }
            else {
                string2 = "";
            }
            sb6.append(string2);
            String string3;
            if (sb5.length() > 0) {
                final StringBuilder sb9 = new StringBuilder();
                sb9.append("{{loc: multiplayer_mod_different_version}}\n");
                sb9.append((Object)sb5);
                sb9.append("\n");
                string3 = sb9.toString();
            }
            else {
                string3 = "";
            }
            sb6.append(string3);
            return sb6.toString();
        }
        return null;
    }
    
    public JSONObject toJson() {
        final JSONObject jsonObject = new JSONObject();
        try {
            final JSONArray jsonArray = new JSONArray();
            jsonObject.put("list", (Object)jsonArray);
            final Iterator<ApparatusMod> iterator = this.modList.iterator();
            while (iterator.hasNext()) {
                final ApparatusModInfo info = iterator.next().getInfo();
                if (!info.getBoolean("client_only")) {
                    final JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("name", (Object)info.getString("name"));
                    jsonObject2.put("version", (Object)info.getString("version"));
                    jsonArray.put((Object)jsonObject2);
                }
            }
            return jsonObject;
        }
        catch (JSONException ex) {
            return jsonObject;
        }
    }
}
