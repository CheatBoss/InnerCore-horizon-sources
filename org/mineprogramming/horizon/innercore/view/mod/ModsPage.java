package org.mineprogramming.horizon.innercore.view.mod;

import com.android.tools.r8.annotations.*;
import org.mineprogramming.horizon.innercore.view.config.*;
import org.mineprogramming.horizon.innercore.view.item.*;
import org.mineprogramming.horizon.innercore.view.page.*;
import android.view.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import org.mineprogramming.horizon.innercore.util.*;
import android.widget.*;
import com.zhekasmirnov.innercore.modpack.*;
import android.text.*;
import android.support.v7.widget.*;
import android.os.*;
import org.json.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;
import org.mineprogramming.horizon.innercore.model.*;
import java.util.*;

@SynthesizedClassMap({ -$$Lambda$ModsPage$kv-25X2vMWPHRePacyFWzNUZJvc.class, -$$Lambda$ModsPage$lOUKNTg97R4hIszMFsfxyNwIakM.class })
public class ModsPage extends Page
{
    private Spinner category;
    private String customTitle;
    private boolean displayFilters;
    private final boolean isLocal;
    private String lastCategory;
    private RecyclerView modList;
    private final ModPack modPack;
    private ItemSource modSource;
    private ModSourceAdapter modSourceAdapter;
    private ViewGroup remoteFilters;
    private EditText search;
    
    public ModsPage(final PagesManager pagesManager, final String lastCategory, final ModPack modPack) {
        super(pagesManager);
        this.displayFilters = true;
        this.modPack = modPack;
        this.selectCategory(this.lastCategory = lastCategory);
        this.isLocal = false;
    }
    
    public ModsPage(final PagesManager pagesManager, final ItemSource modSource, final ModPack modPack) {
        super(pagesManager);
        boolean isLocal = true;
        this.displayFilters = true;
        this.modPack = modPack;
        this.modSource = modSource;
        this.modSourceAdapter = new ModSourceAdapter(this.context, modSource);
        if (modSource instanceof RemoteItemSource) {
            isLocal = false;
        }
        this.isLocal = isLocal;
    }
    
    private void selectCategory(final String lastCategory) {
        this.modSource = new CategoryModSource(this.language, lastCategory);
        this.modSourceAdapter = new ModSourceAdapter(this.context, this.modSource);
        if (this.modList != null) {
            this.modList.setAdapter((RecyclerView$Adapter)this.modSourceAdapter);
            this.setupAdapter();
        }
        this.lastCategory = lastCategory;
    }
    
    private void setupAdapter() {
        this.modSourceAdapter.setOnItemClickListener(new -$$Lambda$ModsPage$kv-25X2vMWPHRePacyFWzNUZJvc(this));
    }
    
    private void setupLocal() {
        this.modSourceAdapter.setOnItemSettingsClickListener(new -$$Lambda$ModsPage$lOUKNTg97R4hIszMFsfxyNwIakM(this));
    }
    
    private void setupRemote(final PageState pageState) {
        this.modSourceAdapter.setDisplaySettings(false);
        final ModCategoriesAdapter adapter = new ModCategoriesAdapter(this.context);
        this.category.setAdapter((SpinnerAdapter)adapter);
        final int optInt = pageState.optInt("category", 0);
        this.lastCategory = (String)adapter.getItem(optInt);
        this.category.setSelection(optInt);
        this.category.setOnItemSelectedListener((AdapterView$OnItemSelectedListener)new AdapterView$OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                final String s = (String)adapter.getItem(n);
                final StringBuilder sb = new StringBuilder();
                sb.append("Selecting category #");
                sb.append(n);
                sb.append(": ");
                sb.append(s);
                sb.append(", lastCategory=");
                sb.append(ModsPage.this.lastCategory);
                Logger.debug("MODS_MANAGER", sb.toString());
                if (!s.equals(ModsPage.this.lastCategory)) {
                    ModsPage.this.selectCategory(s);
                }
            }
            
            public void onNothingSelected(final AdapterView<?> adapterView) {
            }
        });
        this.search.addTextChangedListener((TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                if (charSequence.toString().isEmpty()) {
                    ModsPage.this.selectCategory(ModsPage.this.lastCategory);
                    return;
                }
                ModsPage.this.processSearch(charSequence.toString());
            }
        });
    }
    
    @Override
    public void display(final ViewGroup viewGroup, final PageState pageState) {
        try {
            final InflatedView inflateLayout = JsonInflater.inflateLayout(this.context, viewGroup, ResourceReader.readLayout(this.context, "page_mods"));
            this.remoteFilters = (ViewGroup)inflateLayout.getViewByJsonId("remote_filters");
            this.search = (EditText)inflateLayout.getViewByJsonId("search");
            this.category = (Spinner)inflateLayout.getViewByJsonId("category");
            this.modList = (RecyclerView)inflateLayout.getViewByJsonId("list");
            final TextView textView = inflateLayout.getViewByJsonId("title");
            if (this.customTitle != null) {
                textView.setText((CharSequence)this.customTitle);
            }
            else {
                String text = this.modPack.getManifest().getDisplayedName();
                if (this.modPack == ModPackContext.getInstance().getStorage().getDefaultModPack()) {
                    text = JsonInflater.getString(this.context, "default_pacK_title");
                }
                if (TextUtils.isEmpty((CharSequence)text)) {
                    text = "Untitled";
                }
                textView.setText((CharSequence)text);
            }
            this.modList.setLayoutManager((RecyclerView$LayoutManager)new LinearLayoutManager(this.context));
            this.modList.setAdapter((RecyclerView$Adapter)this.modSourceAdapter);
            this.setupAdapter();
            if (!this.displayFilters) {
                this.remoteFilters.setVisibility(8);
            }
            if (this.isLocal) {
                this.setupLocal();
            }
            else {
                this.setupRemote(pageState);
            }
            this.modSource.updateList();
            final Parcelable value = ((HashMap<K, Parcelable>)pageState).get("recyclerState");
            if (value != null) {
                this.modList.getLayoutManager().onRestoreInstanceState((Parcelable)value);
            }
        }
        catch (JSONException | JsonInflaterException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
    
    @Override
    public void onPause(final PageState pageState) {
        super.onPause(pageState);
        pageState.clear();
        ((HashMap<String, Integer>)pageState).put("category", this.category.getSelectedItemPosition());
        ((HashMap<String, Parcelable>)pageState).put("recyclerState", this.modList.getLayoutManager().onSaveInstanceState());
    }
    
    void processSearch(final String query) {
        if (this.modSource instanceof SearchModSource) {
            ((SearchModSource)this.modSource).setQuery(query);
            return;
        }
        this.modSource = new SearchModSource(this.language, "q", query);
        this.modSourceAdapter = new ModSourceAdapter(this.context, this.modSource);
        if (this.modList != null) {
            this.modList.setAdapter((RecyclerView$Adapter)this.modSourceAdapter);
            this.setupAdapter();
        }
    }
    
    public void setCustomTitle(final String customTitle) {
        this.customTitle = customTitle;
    }
    
    public void setDisplayFilters(final boolean displayFilters) {
        this.displayFilters = displayFilters;
    }
}
