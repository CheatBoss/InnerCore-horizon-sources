package bo.app;

import com.appboy.events.*;
import com.appboy.enums.*;
import com.appboy.support.*;
import android.content.*;
import com.appboy.models.cards.*;
import org.json.*;
import java.util.*;

public class dh implements dn<ContentCardsUpdatedEvent>
{
    private static final String a;
    private static final Set<String> b;
    private final SharedPreferences c;
    private final SharedPreferences d;
    private final c e;
    private final String f;
    private bl g;
    
    static {
        a = AppboyLogger.getAppboyLogTag(dh.class);
        (b = new HashSet<String>()).add(CardKey.VIEWED.getContentCardsKey());
        dh.b.add(CardKey.DISMISSED.getContentCardsKey());
    }
    
    public dh(final Context context, String cacheFileSuffix, final String s) {
        this.f = cacheFileSuffix;
        cacheFileSuffix = StringUtils.getCacheFileSuffix(context, cacheFileSuffix, s);
        final StringBuilder sb = new StringBuilder();
        sb.append("com.appboy.storage.content_cards_storage_provider.metadata");
        sb.append(cacheFileSuffix);
        this.d = context.getSharedPreferences(sb.toString(), 0);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("com.appboy.storage.content_cards_storage_provider.cards");
        sb2.append(cacheFileSuffix);
        this.c = context.getSharedPreferences(sb2.toString(), 0);
        this.e = new a();
    }
    
    static boolean a(final JSONObject jsonObject, final JSONObject jsonObject2) {
        if (jsonObject2 == null) {
            return true;
        }
        if (jsonObject == null) {
            return false;
        }
        final String contentCardsKey = CardKey.CREATED.getContentCardsKey();
        return jsonObject.has(contentCardsKey) && jsonObject2.has(contentCardsKey) && jsonObject.getLong(contentCardsKey) > jsonObject2.getLong(contentCardsKey);
    }
    
    static JSONObject b(final JSONObject jsonObject, final JSONObject jsonObject2) {
        if (jsonObject == null) {
            return jsonObject2;
        }
        final JSONObject jsonObject3 = new JSONObject();
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String s = keys.next();
            jsonObject3.put(s, jsonObject.get(s));
        }
        final Iterator keys2 = jsonObject2.keys();
        while (keys2.hasNext()) {
            final String s2 = keys2.next();
            if (dh.b.contains(s2)) {
                final boolean boolean1 = jsonObject.getBoolean(s2);
                final boolean boolean2 = jsonObject2.getBoolean(s2);
                jsonObject3.put(s2, boolean1 || boolean2);
            }
            else {
                jsonObject3.put(s2, jsonObject2.get(s2));
            }
        }
        return jsonObject3;
    }
    
    private void e() {
        final SharedPreferences$Editor edit = this.d.edit();
        edit.putLong("last_storage_update_timestamp", du.a());
        edit.apply();
    }
    
    private long f() {
        return this.d.getLong("last_storage_update_timestamp", 0L);
    }
    
    public ContentCardsUpdatedEvent a() {
        return this.a(true);
    }
    
    public ContentCardsUpdatedEvent a(final cn cn, String s) {
        String s2 = s;
        if (s == null) {
            AppboyLogger.d(dh.a, "Input user id was null. Defaulting to the empty user id");
            s2 = "";
        }
        if (!this.f.equals(s2)) {
            final String a = dh.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("The received cards are for user ");
            sb.append(s2);
            sb.append(" and the current user is ");
            sb.append(this.f);
            sb.append(" , the cards will be discarded and no changes will be made.");
            AppboyLogger.i(a, sb.toString());
            return null;
        }
        s = dh.a;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Updating offline Content Cards for user with id: ");
        sb2.append(s2);
        AppboyLogger.i(s, sb2.toString());
        this.a(cn);
        this.e();
        final HashSet<String> set = new HashSet<String>();
        final JSONArray d = cn.d();
        if (d != null && d.length() != 0) {
            final Set<String> d2 = this.d();
            for (int i = 0; i < d.length(); ++i) {
                final JSONObject jsonObject = d.getJSONObject(i);
                s = jsonObject.getString(CardKey.ID.getContentCardsKey());
                final JSONObject d3 = this.d(s);
                StringBuilder sb4 = null;
                Label_0305: {
                    if (!a(d3, jsonObject)) {
                        set.add(s);
                        if (jsonObject.has(CardKey.REMOVED.getContentCardsKey()) && jsonObject.getBoolean(CardKey.REMOVED.getContentCardsKey())) {
                            final String a2 = dh.a;
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("Server card is marked as removed. Removing from card storage with id: ");
                            sb3.append(s);
                            AppboyLogger.d(a2, sb3.toString());
                            this.f(s);
                        }
                        else {
                            if (d2.contains(s)) {
                                s = dh.a;
                                sb4 = new StringBuilder();
                                sb4.append("Server card was locally dismissed already. Not adding card to storage. Server card: ");
                                sb4.append(jsonObject);
                                break Label_0305;
                            }
                            if (!jsonObject.has(CardKey.DISMISSED.getContentCardsKey()) || !jsonObject.getBoolean(CardKey.DISMISSED.getContentCardsKey())) {
                                this.a(s, b(d3, jsonObject));
                                continue;
                            }
                            final String a3 = dh.a;
                            final StringBuilder sb5 = new StringBuilder();
                            sb5.append("Server card is marked as dismissed. Adding to dismissed cached and removing from card storage with id: ");
                            sb5.append(s);
                            AppboyLogger.d(a3, sb5.toString());
                            this.e(s);
                        }
                        this.a(s, null);
                        continue;
                    }
                    AppboyLogger.i(dh.a, "The server card received is older than the cached card. Discarding the server card.");
                    s = dh.a;
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("Server card json: ");
                    sb6.append(jsonObject.toString());
                    AppboyLogger.d(s, sb6.toString());
                    s = dh.a;
                    sb4 = new StringBuilder();
                    sb4.append("Cached card json: ");
                    sb4.append(jsonObject.toString());
                }
                AppboyLogger.d(s, sb4.toString());
            }
        }
        if (cn.c()) {
            this.a(set);
            this.b(set);
        }
        return this.a(false);
    }
    
    ContentCardsUpdatedEvent a(final boolean b) {
        final CardKey.Provider provider = new CardKey.Provider(true);
        final Map all = this.c.getAll();
        final JSONArray jsonArray = new JSONArray();
        final Iterator<String> iterator = all.values().iterator();
        while (iterator.hasNext()) {
            jsonArray.put((Object)iterator.next());
        }
        final List<Card> a = bz.a(jsonArray, provider, this.g, this, this.e);
        final Iterator<Card> iterator2 = a.iterator();
        while (iterator2.hasNext()) {
            final Card card = iterator2.next();
            if (card.isExpired()) {
                final String a2 = dh.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Deleting expired card from storage with id: ");
                sb.append(card.getId());
                AppboyLogger.d(a2, sb.toString());
                this.a(card.getId(), null);
                iterator2.remove();
            }
        }
        return new ContentCardsUpdatedEvent(a, this.f, this.f(), b);
    }
    
    public void a(final bl g) {
        this.g = g;
    }
    
    void a(final cn cn) {
        final SharedPreferences$Editor edit = this.d.edit();
        if (cn.b() != -1L) {
            edit.putLong("last_card_updated_at", cn.b());
        }
        if (cn.a() != -1L) {
            edit.putLong("last_full_sync_at", cn.a());
        }
        edit.apply();
    }
    
    @Override
    public void a(final String s) {
        this.a(s, CardKey.READ, true);
    }
    
    void a(final String s, final CardKey cardKey, final boolean b) {
        final JSONObject d = this.d(s);
        if (d == null) {
            final String a = dh.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Can't update card field. Json cannot be parsed from disk or is not present. Id: ");
            sb.append(s);
            AppboyLogger.d(a, sb.toString());
            return;
        }
        try {
            d.put(cardKey.getContentCardsKey(), b);
            this.a(s, d);
        }
        catch (JSONException ex) {
            final String a2 = dh.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to update card json field to ");
            sb2.append(b);
            sb2.append(" with key: ");
            sb2.append(cardKey);
            AppboyLogger.e(a2, sb2.toString(), (Throwable)ex);
        }
    }
    
    void a(final String s, final JSONObject jsonObject) {
        final SharedPreferences$Editor edit = this.c.edit();
        if (jsonObject != null) {
            edit.putString(s, jsonObject.toString());
        }
        else {
            edit.remove(s);
        }
        edit.apply();
    }
    
    void a(final Set<String> set) {
        final Set<String> d = this.d();
        d.retainAll(set);
        this.d.edit().putStringSet("dismissed", (Set)d).apply();
    }
    
    public long b() {
        return this.d.getLong("last_card_updated_at", 0L);
    }
    
    @Override
    public void b(final String s) {
        this.a(s, CardKey.VIEWED, true);
    }
    
    void b(final Set<String> set) {
        final Set<String> keySet = this.c.getAll().keySet();
        final SharedPreferences$Editor edit = this.c.edit();
        for (final String s : keySet) {
            if (!set.contains(s)) {
                final String a = dh.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Removing card from storage with id: ");
                sb.append(s);
                AppboyLogger.d(a, sb.toString());
                edit.remove(s);
            }
        }
        edit.apply();
    }
    
    public long c() {
        return this.d.getLong("last_full_sync_at", 0L);
    }
    
    @Override
    public void c(final String s) {
        this.e(s);
        this.a(s, null);
    }
    
    Set<String> d() {
        return new HashSet<String>(this.d.getStringSet("dismissed", (Set)new HashSet()));
    }
    
    JSONObject d(final String s) {
        final String string = this.c.getString(s, (String)null);
        if (string == null) {
            final String a = dh.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Card not present in storage for id: ");
            sb.append(s);
            AppboyLogger.d(a, sb.toString());
            return null;
        }
        try {
            return new JSONObject(string);
        }
        catch (JSONException ex) {
            final String a2 = dh.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to read card json from storage. Json: ");
            sb2.append(string);
            AppboyLogger.e(a2, sb2.toString(), (Throwable)ex);
            return null;
        }
    }
    
    void e(final String s) {
        final Set<String> d = this.d();
        d.add(s);
        this.d.edit().putStringSet("dismissed", (Set)d).apply();
    }
    
    void f(final String s) {
        final Set<String> d = this.d();
        d.remove(s);
        this.d.edit().putStringSet("dismissed", (Set)d).apply();
    }
}
