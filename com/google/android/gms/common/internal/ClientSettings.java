package com.google.android.gms.common.internal;

import android.view.*;
import android.accounts.*;
import com.google.android.gms.common.api.*;
import com.google.android.gms.signin.*;
import java.util.*;
import javax.annotation.*;
import android.support.v4.util.*;

public final class ClientSettings
{
    private final Set<Scope> zzcv;
    private final int zzcx;
    private final View zzcy;
    private final String zzcz;
    private final String zzda;
    private final Set<Scope> zzrz;
    private final Account zzs;
    private final Map<Api<?>, OptionalApiSettings> zzsa;
    private final SignInOptions zzsb;
    private Integer zzsc;
    
    public ClientSettings(final Account zzs, final Set<Scope> set, final Map<Api<?>, OptionalApiSettings> map, final int zzcx, final View zzcy, final String zzcz, final String zzda, final SignInOptions zzsb) {
        this.zzs = zzs;
        Set<Scope> zzcv;
        if (set == null) {
            zzcv = (Set<Scope>)Collections.EMPTY_SET;
        }
        else {
            zzcv = Collections.unmodifiableSet((Set<? extends Scope>)set);
        }
        this.zzcv = zzcv;
        Map<Api<?>, OptionalApiSettings> empty_MAP = map;
        if (map == null) {
            empty_MAP = (Map<Api<?>, OptionalApiSettings>)Collections.EMPTY_MAP;
        }
        this.zzsa = empty_MAP;
        this.zzcy = zzcy;
        this.zzcx = zzcx;
        this.zzcz = zzcz;
        this.zzda = zzda;
        this.zzsb = zzsb;
        final HashSet<Scope> set2 = new HashSet<Scope>(this.zzcv);
        final Iterator<OptionalApiSettings> iterator = this.zzsa.values().iterator();
        while (iterator.hasNext()) {
            set2.addAll((Collection<?>)iterator.next().mScopes);
        }
        this.zzrz = (Set<Scope>)Collections.unmodifiableSet((Set<?>)set2);
    }
    
    @Nullable
    public final Account getAccount() {
        return this.zzs;
    }
    
    public final Account getAccountOrDefault() {
        final Account zzs = this.zzs;
        if (zzs != null) {
            return zzs;
        }
        return new Account("<<default account>>", "com.google");
    }
    
    public final Set<Scope> getAllRequestedScopes() {
        return this.zzrz;
    }
    
    @Nullable
    public final Integer getClientSessionId() {
        return this.zzsc;
    }
    
    @Nullable
    public final String getRealClientClassName() {
        return this.zzda;
    }
    
    @Nullable
    public final String getRealClientPackageName() {
        return this.zzcz;
    }
    
    public final Set<Scope> getRequiredScopes() {
        return this.zzcv;
    }
    
    @Nullable
    public final SignInOptions getSignInOptions() {
        return this.zzsb;
    }
    
    public final void setClientSessionId(final Integer zzsc) {
        this.zzsc = zzsc;
    }
    
    public static final class Builder
    {
        private int zzcx;
        private View zzcy;
        private String zzcz;
        private String zzda;
        private Account zzs;
        private Map<Api<?>, OptionalApiSettings> zzsa;
        private SignInOptions zzsb;
        private ArraySet<Scope> zzsd;
        
        public Builder() {
            this.zzcx = 0;
            this.zzsb = SignInOptions.DEFAULT;
        }
        
        public final Builder addAllRequiredScopes(final Collection<Scope> collection) {
            if (this.zzsd == null) {
                this.zzsd = (ArraySet<Scope>)new ArraySet();
            }
            this.zzsd.addAll((Collection)collection);
            return this;
        }
        
        public final ClientSettings build() {
            return new ClientSettings(this.zzs, (Set<Scope>)this.zzsd, this.zzsa, this.zzcx, this.zzcy, this.zzcz, this.zzda, this.zzsb);
        }
        
        public final Builder setAccount(final Account zzs) {
            this.zzs = zzs;
            return this;
        }
        
        public final Builder setRealClientClassName(final String zzda) {
            this.zzda = zzda;
            return this;
        }
        
        public final Builder setRealClientPackageName(final String zzcz) {
            this.zzcz = zzcz;
            return this;
        }
    }
    
    public static final class OptionalApiSettings
    {
        public final Set<Scope> mScopes;
    }
}
