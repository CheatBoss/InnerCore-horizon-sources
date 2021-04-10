package com.google.android.gms.common.api;

import android.content.*;
import android.accounts.*;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.*;
import com.google.android.gms.common.internal.*;
import java.util.*;
import android.os.*;

public final class Api<O extends ApiOptions>
{
    private final String mName;
    private final AbstractClientBuilder<?, O> zzby;
    private final zza<?, O> zzbz;
    private final ClientKey<?> zzca;
    private final zzb<?> zzcb;
    
    public <C extends Client> Api(final String mName, final AbstractClientBuilder<C, O> zzby, final ClientKey<C> zzca) {
        Preconditions.checkNotNull(zzby, "Cannot construct an Api with a null ClientBuilder");
        Preconditions.checkNotNull(zzca, "Cannot construct an Api with a null ClientKey");
        this.mName = mName;
        this.zzby = zzby;
        this.zzbz = null;
        this.zzca = zzca;
        this.zzcb = null;
    }
    
    public final String getName() {
        return this.mName;
    }
    
    public final AbstractClientBuilder<?, O> zzk() {
        Preconditions.checkState(this.zzby != null, "This API was constructed with a SimpleClientBuilder. Use getSimpleClientBuilder");
        return this.zzby;
    }
    
    public abstract static class AbstractClientBuilder<T extends Client, O> extends BaseClientBuilder<T, O>
    {
        public abstract T buildClient(final Context p0, final Looper p1, final ClientSettings p2, final O p3, final GoogleApiClient.ConnectionCallbacks p4, final GoogleApiClient.OnConnectionFailedListener p5);
    }
    
    public interface AnyClient
    {
    }
    
    public static class AnyClientKey<C extends AnyClient>
    {
    }
    
    public interface ApiOptions
    {
        public interface HasAccountOptions extends Api$ApiOptions$HasOptions, Api$ApiOptions$NotRequiredOptions
        {
            Account getAccount();
        }
        
        public interface HasGoogleSignInAccountOptions extends Api$ApiOptions$HasOptions
        {
            GoogleSignInAccount getGoogleSignInAccount();
        }
    }
    
    public static class BaseClientBuilder<T extends AnyClient, O>
    {
    }
    
    public interface Client extends AnyClient
    {
        void connect(final BaseGmsClient.ConnectionProgressReportCallbacks p0);
        
        void disconnect();
        
        Feature[] getAvailableFeatures();
        
        String getEndpointPackageName();
        
        int getMinApkVersion();
        
        void getRemoteService(final IAccountAccessor p0, final Set<Scope> p1);
        
        boolean isConnected();
        
        boolean isConnecting();
        
        void onUserSignOut(final BaseGmsClient.SignOutCallbacks p0);
        
        boolean requiresGooglePlayServices();
        
        boolean requiresSignIn();
    }
    
    public static final class ClientKey<C extends Client> extends AnyClientKey<C>
    {
    }
    
    public interface SimpleClient<T extends IInterface> extends AnyClient
    {
        T createServiceInterface(final IBinder p0);
        
        String getServiceDescriptor();
        
        String getStartServiceAction();
        
        void setState(final int p0, final T p1);
    }
    
    public static class zza<T extends SimpleClient, O> extends BaseClientBuilder<T, O>
    {
    }
    
    public static final class zzb<C extends SimpleClient> extends AnyClientKey<C>
    {
    }
}
