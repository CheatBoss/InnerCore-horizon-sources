package com.google.android.gms.common.internal;

import java.util.concurrent.atomic.*;
import com.google.android.gms.common.*;
import android.util.*;
import android.text.*;
import android.app.*;
import android.accounts.*;
import com.google.android.gms.common.api.*;
import java.util.*;
import android.content.*;
import android.os.*;

public abstract class BaseGmsClient<T extends IInterface>
{
    public static final String[] GOOGLE_PLUS_REQUIRED_FEATURES;
    private static final Feature[] zzqz;
    protected ConnectionProgressReportCallbacks mConnectionProgressReportCallbacks;
    private final Context mContext;
    protected AtomicInteger mDisconnectCount;
    final Handler mHandler;
    private final Object mLock;
    private final Looper zzcn;
    private final GoogleApiAvailabilityLight zzgk;
    private int zzra;
    private long zzrb;
    private long zzrc;
    private int zzrd;
    private long zzre;
    private GmsServiceEndpoint zzrf;
    private final GmsClientSupervisor zzrg;
    private final Object zzrh;
    private IGmsServiceBroker zzri;
    private T zzrj;
    private final ArrayList<CallbackProxy<?>> zzrk;
    private GmsServiceConnection zzrl;
    private int zzrm;
    private final BaseConnectionCallbacks zzrn;
    private final BaseOnConnectionFailedListener zzro;
    private final int zzrp;
    private final String zzrq;
    private ConnectionResult zzrr;
    private boolean zzrs;
    private volatile ConnectionInfo zzrt;
    
    static {
        zzqz = new Feature[0];
        GOOGLE_PLUS_REQUIRED_FEATURES = new String[] { "service_esmobile", "service_googleme" };
    }
    
    protected BaseGmsClient(final Context context, final Looper looper, final int n, final BaseConnectionCallbacks baseConnectionCallbacks, final BaseOnConnectionFailedListener baseOnConnectionFailedListener, final String s) {
        this(context, looper, GmsClientSupervisor.getInstance(context), GoogleApiAvailabilityLight.getInstance(), n, Preconditions.checkNotNull(baseConnectionCallbacks), Preconditions.checkNotNull(baseOnConnectionFailedListener), s);
    }
    
    protected BaseGmsClient(final Context context, final Looper looper, final GmsClientSupervisor gmsClientSupervisor, final GoogleApiAvailabilityLight googleApiAvailabilityLight, final int zzrp, final BaseConnectionCallbacks zzrn, final BaseOnConnectionFailedListener zzro, final String zzrq) {
        this.mLock = new Object();
        this.zzrh = new Object();
        this.zzrk = new ArrayList<CallbackProxy<?>>();
        this.zzrm = 1;
        this.zzrr = null;
        this.zzrs = false;
        this.zzrt = null;
        this.mDisconnectCount = new AtomicInteger(0);
        this.mContext = Preconditions.checkNotNull(context, "Context must not be null");
        this.zzcn = Preconditions.checkNotNull(looper, "Looper must not be null");
        this.zzrg = Preconditions.checkNotNull(gmsClientSupervisor, "Supervisor must not be null");
        this.zzgk = Preconditions.checkNotNull(googleApiAvailabilityLight, "API availability must not be null");
        this.mHandler = new zzb(looper);
        this.zzrp = zzrp;
        this.zzrn = zzrn;
        this.zzro = zzro;
        this.zzrq = zzrq;
    }
    
    private final void zza(final int zzrm, final T zzrj) {
        Preconditions.checkArgument(zzrm == 4 == (zzrj != null));
        synchronized (this.mLock) {
            this.onSetConnectState(this.zzrm = zzrm, this.zzrj = zzrj);
            if (zzrm != 1) {
                if (zzrm != 2 && zzrm != 3) {
                    if (zzrm == 4) {
                        this.onConnectedLocked(zzrj);
                    }
                }
                else {
                    if (this.zzrl != null && this.zzrf != null) {
                        final String zzcw = this.zzrf.zzcw();
                        final String packageName = this.zzrf.getPackageName();
                        final StringBuilder sb = new StringBuilder(String.valueOf(zzcw).length() + 70 + String.valueOf(packageName).length());
                        sb.append("Calling connect() while still connected, missing disconnect() for ");
                        sb.append(zzcw);
                        sb.append(" on ");
                        sb.append(packageName);
                        Log.e("GmsClient", sb.toString());
                        this.zzrg.unbindService(this.zzrf.zzcw(), this.zzrf.getPackageName(), this.zzrf.getBindFlags(), (ServiceConnection)this.zzrl, this.getRealClientName());
                        this.mDisconnectCount.incrementAndGet();
                    }
                    this.zzrl = new GmsServiceConnection(this.mDisconnectCount.get());
                    GmsServiceEndpoint zzrf;
                    if (this.zzrm == 3 && this.getLocalStartServiceAction() != null) {
                        zzrf = new GmsServiceEndpoint(this.getContext().getPackageName(), this.getLocalStartServiceAction(), true, this.getServiceBindFlags());
                    }
                    else {
                        zzrf = new GmsServiceEndpoint(this.getStartServicePackage(), this.getStartServiceAction(), false, this.getServiceBindFlags());
                    }
                    this.zzrf = zzrf;
                    if (!this.zzrg.bindService(zzrf.zzcw(), this.zzrf.getPackageName(), this.zzrf.getBindFlags(), (ServiceConnection)this.zzrl, this.getRealClientName())) {
                        final String zzcw2 = this.zzrf.zzcw();
                        final String packageName2 = this.zzrf.getPackageName();
                        final StringBuilder sb2 = new StringBuilder(String.valueOf(zzcw2).length() + 34 + String.valueOf(packageName2).length());
                        sb2.append("unable to connect to service: ");
                        sb2.append(zzcw2);
                        sb2.append(" on ");
                        sb2.append(packageName2);
                        Log.e("GmsClient", sb2.toString());
                        this.onPostServiceBindingHandler(16, null, this.mDisconnectCount.get());
                    }
                }
            }
            else if (this.zzrl != null) {
                this.zzrg.unbindService(this.getStartServiceAction(), this.getStartServicePackage(), this.getServiceBindFlags(), (ServiceConnection)this.zzrl, this.getRealClientName());
                this.zzrl = null;
            }
        }
    }
    
    static /* synthetic */ void zza(final BaseGmsClient baseGmsClient, final int n) {
        baseGmsClient.zzj(16);
    }
    
    static /* synthetic */ void zza(final BaseGmsClient baseGmsClient, final int n, final IInterface interface1) {
        baseGmsClient.zza(n, null);
    }
    
    private final void zza(final ConnectionInfo zzrt) {
        this.zzrt = zzrt;
    }
    
    private final boolean zza(final int n, final int n2, final T t) {
        synchronized (this.mLock) {
            if (this.zzrm != n) {
                return false;
            }
            this.zza(n2, t);
            return true;
        }
    }
    
    private final boolean zzcq() {
        while (true) {
            synchronized (this.mLock) {
                if (this.zzrm == 3) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private final boolean zzcr() {
        if (this.zzrs) {
            return false;
        }
        if (TextUtils.isEmpty((CharSequence)this.getServiceDescriptor())) {
            return false;
        }
        if (TextUtils.isEmpty((CharSequence)this.getLocalStartServiceAction())) {
            return false;
        }
        try {
            Class.forName(this.getServiceDescriptor());
            return true;
        }
        catch (ClassNotFoundException ex) {
            return false;
        }
    }
    
    private final void zzj(int n) {
        if (this.zzcq()) {
            n = 5;
            this.zzrs = true;
        }
        else {
            n = 4;
        }
        final Handler mHandler = this.mHandler;
        mHandler.sendMessage(mHandler.obtainMessage(n, this.mDisconnectCount.get(), 16));
    }
    
    public void checkAvailabilityAndConnect() {
        final int googlePlayServicesAvailable = this.zzgk.isGooglePlayServicesAvailable(this.mContext, this.getMinApkVersion());
        if (googlePlayServicesAvailable != 0) {
            this.zza(1, null);
            this.triggerNotAvailable((ConnectionProgressReportCallbacks)new LegacyClientCallbackAdapter(), googlePlayServicesAvailable, null);
            return;
        }
        this.connect((ConnectionProgressReportCallbacks)new LegacyClientCallbackAdapter());
    }
    
    protected final void checkConnected() {
        if (this.isConnected()) {
            return;
        }
        throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
    }
    
    public void connect(final ConnectionProgressReportCallbacks connectionProgressReportCallbacks) {
        this.mConnectionProgressReportCallbacks = Preconditions.checkNotNull(connectionProgressReportCallbacks, "Connection progress callbacks cannot be null.");
        this.zza(2, null);
    }
    
    protected abstract T createServiceInterface(final IBinder p0);
    
    public void disconnect() {
        this.mDisconnectCount.incrementAndGet();
        synchronized (this.zzrk) {
            for (int size = this.zzrk.size(), i = 0; i < size; ++i) {
                this.zzrk.get(i).removeListener();
            }
            this.zzrk.clear();
            // monitorexit(this.zzrk)
            final Object zzrh = this.zzrh;
            synchronized (this.zzrk) {
                this.zzri = null;
                // monitorexit(this.zzrk)
                this.zza(1, null);
            }
        }
    }
    
    public Account getAccount() {
        return null;
    }
    
    public final Account getAccountOrDefault() {
        if (this.getAccount() != null) {
            return this.getAccount();
        }
        return new Account("<<default account>>", "com.google");
    }
    
    public Feature[] getApiFeatures() {
        return BaseGmsClient.zzqz;
    }
    
    public final Feature[] getAvailableFeatures() {
        final ConnectionInfo zzrt = this.zzrt;
        if (zzrt == null) {
            return null;
        }
        return zzrt.getAvailableFeatures();
    }
    
    public Bundle getConnectionHint() {
        return null;
    }
    
    public final Context getContext() {
        return this.mContext;
    }
    
    public String getEndpointPackageName() {
        if (this.isConnected()) {
            final GmsServiceEndpoint zzrf = this.zzrf;
            if (zzrf != null) {
                return zzrf.getPackageName();
            }
        }
        throw new RuntimeException("Failed to connect when checking package");
    }
    
    protected Bundle getGetServiceRequestExtraArgs() {
        return new Bundle();
    }
    
    protected String getLocalStartServiceAction() {
        return null;
    }
    
    public int getMinApkVersion() {
        return GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    }
    
    protected final String getRealClientName() {
        String s;
        if ((s = this.zzrq) == null) {
            s = this.mContext.getClass().getName();
        }
        return s;
    }
    
    public void getRemoteService(final IAccountAccessor authenticatedAccount, final Set<Scope> scopes) {
        final GetServiceRequest setExtraArgs = new GetServiceRequest(this.zzrp).setCallingPackage(this.mContext.getPackageName()).setExtraArgs(this.getGetServiceRequestExtraArgs());
        if (scopes != null) {
            setExtraArgs.setScopes(scopes);
        }
        if (this.requiresSignIn()) {
            setExtraArgs.setClientRequestedAccount(this.getAccountOrDefault()).setAuthenticatedAccount(authenticatedAccount);
        }
        else if (this.requiresAccount()) {
            setExtraArgs.setClientRequestedAccount(this.getAccount());
        }
        setExtraArgs.setClientRequiredFeatures(this.getRequiredFeatures());
        setExtraArgs.setClientApiFeatures(this.getApiFeatures());
        try {
            synchronized (this.zzrh) {
                if (this.zzri != null) {
                    this.zzri.getService(new GmsCallbacks(this, this.mDisconnectCount.get()), setExtraArgs);
                }
                else {
                    Log.w("GmsClient", "mServiceBroker is null, client disconnected");
                }
            }
        }
        catch (RemoteException | RuntimeException ex3) {
            final Throwable t;
            Log.w("GmsClient", "IGmsServiceBroker.getService failed", t);
            this.onPostInitHandler(8, null, null, this.mDisconnectCount.get());
        }
        catch (SecurityException ex) {
            throw ex;
        }
        catch (DeadObjectException ex2) {
            Log.w("GmsClient", "IGmsServiceBroker.getService failed", (Throwable)ex2);
            this.triggerConnectionSuspended(1);
        }
    }
    
    public Feature[] getRequiredFeatures() {
        return BaseGmsClient.zzqz;
    }
    
    protected Set<Scope> getScopes() {
        return (Set<Scope>)Collections.EMPTY_SET;
    }
    
    public final T getService() throws DeadObjectException {
        while (true) {
            while (true) {
                synchronized (this.mLock) {
                    if (this.zzrm == 5) {
                        throw new DeadObjectException();
                    }
                    this.checkConnected();
                    if (this.zzrj != null) {
                        final boolean b = true;
                        Preconditions.checkState(b, "Client is connected but service is null");
                        return this.zzrj;
                    }
                }
                final boolean b = false;
                continue;
            }
        }
    }
    
    protected int getServiceBindFlags() {
        return 129;
    }
    
    protected abstract String getServiceDescriptor();
    
    protected abstract String getStartServiceAction();
    
    protected String getStartServicePackage() {
        return "com.google.android.gms";
    }
    
    public boolean isConnected() {
        while (true) {
            synchronized (this.mLock) {
                if (this.zzrm == 4) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean isConnecting() {
        while (true) {
            synchronized (this.mLock) {
                if (this.zzrm != 2 && this.zzrm != 3) {
                    return false;
                }
                return true;
            }
            return false;
            b = true;
            return b;
        }
    }
    
    protected void onConnectedLocked(final T t) {
        this.zzrc = System.currentTimeMillis();
    }
    
    protected void onConnectionFailed(final ConnectionResult connectionResult) {
        this.zzrd = connectionResult.getErrorCode();
        this.zzre = System.currentTimeMillis();
    }
    
    protected void onConnectionSuspended(final int zzra) {
        this.zzra = zzra;
        this.zzrb = System.currentTimeMillis();
    }
    
    protected void onPostInitHandler(final int n, final IBinder binder, final Bundle bundle, final int n2) {
        final Handler mHandler = this.mHandler;
        mHandler.sendMessage(mHandler.obtainMessage(1, n2, -1, (Object)new PostInitCallback(n, binder, bundle)));
    }
    
    protected void onPostServiceBindingHandler(final int n, final Bundle bundle, final int n2) {
        final Handler mHandler = this.mHandler;
        mHandler.sendMessage(mHandler.obtainMessage(7, n2, -1, (Object)new PostServiceBindingCallback(n, bundle)));
    }
    
    void onSetConnectState(final int n, final T t) {
    }
    
    public void onUserSignOut(final SignOutCallbacks signOutCallbacks) {
        signOutCallbacks.onSignOutComplete();
    }
    
    public boolean requiresAccount() {
        return false;
    }
    
    public boolean requiresGooglePlayServices() {
        return true;
    }
    
    public boolean requiresSignIn() {
        return false;
    }
    
    public void triggerConnectionSuspended(final int n) {
        final Handler mHandler = this.mHandler;
        mHandler.sendMessage(mHandler.obtainMessage(6, this.mDisconnectCount.get(), n));
    }
    
    protected void triggerNotAvailable(final ConnectionProgressReportCallbacks connectionProgressReportCallbacks, final int n, final PendingIntent pendingIntent) {
        this.mConnectionProgressReportCallbacks = Preconditions.checkNotNull(connectionProgressReportCallbacks, "Connection progress callbacks cannot be null.");
        final Handler mHandler = this.mHandler;
        mHandler.sendMessage(mHandler.obtainMessage(3, this.mDisconnectCount.get(), n, (Object)pendingIntent));
    }
    
    public interface BaseConnectionCallbacks
    {
        void onConnected(final Bundle p0);
        
        void onConnectionSuspended(final int p0);
    }
    
    public interface BaseOnConnectionFailedListener
    {
        void onConnectionFailed(final ConnectionResult p0);
    }
    
    protected abstract class CallbackProxy<TListener>
    {
        private TListener zzli;
        private boolean zzrv;
        
        public CallbackProxy(final TListener zzli) {
            this.zzli = zzli;
            this.zzrv = false;
        }
        
        public void deliverCallback() {
            synchronized (this) {
                final TListener zzli = this.zzli;
                if (this.zzrv) {
                    final String value = String.valueOf(this);
                    final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 47);
                    sb.append("Callback proxy ");
                    sb.append(value);
                    sb.append(" being reused. This is not safe.");
                    Log.w("GmsClient", sb.toString());
                }
                // monitorexit(this)
                Label_0092: {
                    if (zzli != null) {
                        try {
                            this.deliverCallback(zzli);
                            break Label_0092;
                        }
                        catch (RuntimeException ex) {
                            this.onDeliverCallbackFailed();
                            throw ex;
                        }
                    }
                    this.onDeliverCallbackFailed();
                }
                synchronized (this) {
                    this.zzrv = true;
                    // monitorexit(this)
                    this.unregister();
                }
            }
        }
        
        protected abstract void deliverCallback(final TListener p0);
        
        protected abstract void onDeliverCallbackFailed();
        
        public void removeListener() {
            synchronized (this) {
                this.zzli = null;
            }
        }
        
        public void unregister() {
            this.removeListener();
            synchronized (BaseGmsClient.this.zzrk) {
                BaseGmsClient.this.zzrk.remove(this);
            }
        }
    }
    
    public interface ConnectionProgressReportCallbacks
    {
        void onReportServiceBinding(final ConnectionResult p0);
    }
    
    public static final class GmsCallbacks extends Stub
    {
        private BaseGmsClient zzrw;
        private final int zzrx;
        
        public GmsCallbacks(final BaseGmsClient zzrw, final int zzrx) {
            this.zzrw = zzrw;
            this.zzrx = zzrx;
        }
        
        @Override
        public final void onAccountValidationComplete(final int n, final Bundle bundle) {
            Log.wtf("GmsClient", "received deprecated onAccountValidationComplete callback, ignoring", (Throwable)new Exception());
        }
        
        @Override
        public final void onPostInitComplete(final int n, final IBinder binder, final Bundle bundle) {
            Preconditions.checkNotNull(this.zzrw, "onPostInitComplete can be called only once per call to getRemoteService");
            this.zzrw.onPostInitHandler(n, binder, bundle, this.zzrx);
            this.zzrw = null;
        }
        
        @Override
        public final void onPostInitCompleteWithConnectionInfo(final int n, final IBinder binder, final ConnectionInfo connectionInfo) {
            Preconditions.checkNotNull(this.zzrw, "onPostInitCompleteWithConnectionInfo can be called only once per call togetRemoteService");
            Preconditions.checkNotNull(connectionInfo);
            this.zzrw.zza(connectionInfo);
            this.onPostInitComplete(n, binder, connectionInfo.getResolutionBundle());
        }
    }
    
    public final class GmsServiceConnection implements ServiceConnection
    {
        private final int zzrx;
        
        public GmsServiceConnection(final int zzrx) {
            this.zzrx = zzrx;
        }
        
        public final void onServiceConnected(final ComponentName componentName, final IBinder binder) {
            final BaseGmsClient zzru = BaseGmsClient.this;
            if (binder == null) {
                BaseGmsClient.zza(zzru, 16);
                return;
            }
            synchronized (zzru.zzrh) {
                BaseGmsClient.this.zzri = IGmsServiceBroker.Stub.asInterface(binder);
                // monitorexit(BaseGmsClient.zza(zzru))
                BaseGmsClient.this.onPostServiceBindingHandler(0, null, this.zzrx);
            }
        }
        
        public final void onServiceDisconnected(final ComponentName componentName) {
            synchronized (BaseGmsClient.this.zzrh) {
                BaseGmsClient.this.zzri = null;
                // monitorexit(BaseGmsClient.zza(this.zzru))
                BaseGmsClient.this.mHandler.sendMessage(BaseGmsClient.this.mHandler.obtainMessage(6, this.zzrx, 1));
            }
        }
    }
    
    protected class LegacyClientCallbackAdapter implements ConnectionProgressReportCallbacks
    {
        public LegacyClientCallbackAdapter() {
        }
        
        @Override
        public void onReportServiceBinding(final ConnectionResult connectionResult) {
            if (connectionResult.isSuccess()) {
                final BaseGmsClient zzru = BaseGmsClient.this;
                zzru.getRemoteService(null, zzru.getScopes());
                return;
            }
            if (BaseGmsClient.this.zzro != null) {
                BaseGmsClient.this.zzro.onConnectionFailed(connectionResult);
            }
        }
    }
    
    protected final class PostInitCallback extends zza
    {
        public final IBinder service;
        
        public PostInitCallback(final int n, final IBinder service, final Bundle bundle) {
            super(n, bundle);
            this.service = service;
        }
        
        @Override
        protected final void handleServiceFailure(final ConnectionResult connectionResult) {
            if (BaseGmsClient.this.zzro != null) {
                BaseGmsClient.this.zzro.onConnectionFailed(connectionResult);
            }
            BaseGmsClient.this.onConnectionFailed(connectionResult);
        }
        
        @Override
        protected final boolean handleServiceSuccess() {
            final boolean b = false;
            try {
                final String interfaceDescriptor = this.service.getInterfaceDescriptor();
                if (!BaseGmsClient.this.getServiceDescriptor().equals(interfaceDescriptor)) {
                    final String serviceDescriptor = BaseGmsClient.this.getServiceDescriptor();
                    final StringBuilder sb = new StringBuilder(String.valueOf(serviceDescriptor).length() + 34 + String.valueOf(interfaceDescriptor).length());
                    sb.append("service descriptor mismatch: ");
                    sb.append(serviceDescriptor);
                    sb.append(" vs. ");
                    sb.append(interfaceDescriptor);
                    Log.e("GmsClient", sb.toString());
                    return false;
                }
                final IInterface serviceInterface = BaseGmsClient.this.createServiceInterface(this.service);
                boolean b2 = b;
                if (serviceInterface != null) {
                    if (!BaseGmsClient.this.zza(2, 4, serviceInterface)) {
                        b2 = b;
                        if (!BaseGmsClient.this.zza(3, 4, serviceInterface)) {
                            return b2;
                        }
                    }
                    BaseGmsClient.this.zzrr = null;
                    final Bundle connectionHint = BaseGmsClient.this.getConnectionHint();
                    if (BaseGmsClient.this.zzrn != null) {
                        BaseGmsClient.this.zzrn.onConnected(connectionHint);
                    }
                    b2 = true;
                }
                return b2;
            }
            catch (RemoteException ex) {
                Log.w("GmsClient", "service probably died");
                return false;
            }
        }
    }
    
    protected final class PostServiceBindingCallback extends zza
    {
        public PostServiceBindingCallback(final int n, final Bundle bundle) {
            super(n, bundle);
        }
        
        @Override
        protected final void handleServiceFailure(final ConnectionResult connectionResult) {
            BaseGmsClient.this.mConnectionProgressReportCallbacks.onReportServiceBinding(connectionResult);
            BaseGmsClient.this.onConnectionFailed(connectionResult);
        }
        
        @Override
        protected final boolean handleServiceSuccess() {
            BaseGmsClient.this.mConnectionProgressReportCallbacks.onReportServiceBinding(ConnectionResult.RESULT_SUCCESS);
            return true;
        }
    }
    
    public interface SignOutCallbacks
    {
        void onSignOutComplete();
    }
    
    private abstract class zza extends CallbackProxy<Boolean>
    {
        public final Bundle resolution;
        public final int statusCode;
        
        protected zza(final int statusCode, final Bundle resolution) {
            super(true);
            this.statusCode = statusCode;
            this.resolution = resolution;
        }
        
        protected void deliverCallback(final Boolean b) {
            final PendingIntent pendingIntent = null;
            if (b == null) {
                BaseGmsClient.zza(BaseGmsClient.this, 1, null);
                return;
            }
            final int statusCode = this.statusCode;
            if (statusCode == 0) {
                if (!this.handleServiceSuccess()) {
                    BaseGmsClient.zza(BaseGmsClient.this, 1, null);
                    this.handleServiceFailure(new ConnectionResult(8, null));
                }
                return;
            }
            if (statusCode != 10) {
                BaseGmsClient.zza(BaseGmsClient.this, 1, null);
                final Bundle resolution = this.resolution;
                PendingIntent pendingIntent2 = pendingIntent;
                if (resolution != null) {
                    pendingIntent2 = (PendingIntent)resolution.getParcelable("pendingIntent");
                }
                this.handleServiceFailure(new ConnectionResult(this.statusCode, pendingIntent2));
                return;
            }
            BaseGmsClient.zza(BaseGmsClient.this, 1, null);
            throw new IllegalStateException("A fatal developer error has occurred. Check the logs for further information.");
        }
        
        protected abstract void handleServiceFailure(final ConnectionResult p0);
        
        protected abstract boolean handleServiceSuccess();
        
        @Override
        protected void onDeliverCallbackFailed() {
        }
    }
    
    final class zzb extends Handler
    {
        public zzb(final Looper looper) {
            super(looper);
        }
        
        private static void zza(final Message message) {
            final CallbackProxy callbackProxy = (CallbackProxy)message.obj;
            callbackProxy.onDeliverCallbackFailed();
            callbackProxy.unregister();
        }
        
        private static boolean zzb(final Message message) {
            return message.what == 2 || message.what == 1 || message.what == 7;
        }
        
        public final void handleMessage(final Message message) {
            if (BaseGmsClient.this.mDisconnectCount.get() != message.arg1) {
                if (zzb(message)) {
                    zza(message);
                }
                return;
            }
            if ((message.what == 1 || message.what == 7 || message.what == 4 || message.what == 5) && !BaseGmsClient.this.isConnecting()) {
                zza(message);
                return;
            }
            final int what = message.what;
            PendingIntent pendingIntent = null;
            if (what == 4) {
                BaseGmsClient.this.zzrr = new ConnectionResult(message.arg2);
                if (BaseGmsClient.this.zzcr() && !BaseGmsClient.this.zzrs) {
                    BaseGmsClient.zza(BaseGmsClient.this, 3, null);
                    return;
                }
                ConnectionResult zzd;
                if (BaseGmsClient.this.zzrr != null) {
                    zzd = BaseGmsClient.this.zzrr;
                }
                else {
                    zzd = new ConnectionResult(8);
                }
                BaseGmsClient.this.mConnectionProgressReportCallbacks.onReportServiceBinding(zzd);
                BaseGmsClient.this.onConnectionFailed(zzd);
            }
            else {
                if (message.what == 5) {
                    ConnectionResult zzd2;
                    if (BaseGmsClient.this.zzrr != null) {
                        zzd2 = BaseGmsClient.this.zzrr;
                    }
                    else {
                        zzd2 = new ConnectionResult(8);
                    }
                    BaseGmsClient.this.mConnectionProgressReportCallbacks.onReportServiceBinding(zzd2);
                    BaseGmsClient.this.onConnectionFailed(zzd2);
                    return;
                }
                if (message.what == 3) {
                    if (message.obj instanceof PendingIntent) {
                        pendingIntent = (PendingIntent)message.obj;
                    }
                    final ConnectionResult connectionResult = new ConnectionResult(message.arg2, pendingIntent);
                    BaseGmsClient.this.mConnectionProgressReportCallbacks.onReportServiceBinding(connectionResult);
                    BaseGmsClient.this.onConnectionFailed(connectionResult);
                    return;
                }
                if (message.what == 6) {
                    BaseGmsClient.zza(BaseGmsClient.this, 5, null);
                    if (BaseGmsClient.this.zzrn != null) {
                        BaseGmsClient.this.zzrn.onConnectionSuspended(message.arg2);
                    }
                    BaseGmsClient.this.onConnectionSuspended(message.arg2);
                    BaseGmsClient.this.zza(5, 1, null);
                    return;
                }
                if (message.what == 2 && !BaseGmsClient.this.isConnected()) {
                    zza(message);
                    return;
                }
                if (zzb(message)) {
                    ((CallbackProxy)message.obj).deliverCallback();
                    return;
                }
                final int what2 = message.what;
                final StringBuilder sb = new StringBuilder(45);
                sb.append("Don't know how to handle message: ");
                sb.append(what2);
                Log.wtf("GmsClient", sb.toString(), (Throwable)new Exception());
            }
        }
    }
}
