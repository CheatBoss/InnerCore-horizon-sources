package com.microsoft.aad.adal;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.io.*;
import android.content.*;
import android.os.*;

final class BrokerAccountServiceHandler
{
    private static final String BROKER_ACCOUNT_SERVICE_INTENT_FILTER = "com.microsoft.workaccount.BrokerAccount";
    private static final String TAG;
    private static ExecutorService sThreadExecutor;
    private ConcurrentMap<BrokerAccountServiceConnection, CallbackExecutor<BrokerAccountServiceConnection>> mPendingConnections;
    
    static {
        TAG = BrokerAccountServiceHandler.class.getSimpleName();
        BrokerAccountServiceHandler.sThreadExecutor = Executors.newCachedThreadPool();
    }
    
    private BrokerAccountServiceHandler() {
        this.mPendingConnections = new ConcurrentHashMap<BrokerAccountServiceConnection, CallbackExecutor<BrokerAccountServiceConnection>>();
    }
    
    private void bindToBrokerAccountService(final Context context, final Callback<BrokerAccountServiceConnection> callback, final BrokerEvent telemetryEvent) {
        final StringBuilder sb = new StringBuilder();
        sb.append(BrokerAccountServiceHandler.TAG);
        sb.append(":bindToBrokerAccountService");
        final String string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("uid: ");
        sb2.append(Process.myUid());
        Logger.v(string, "Binding to BrokerAccountService for caller uid. ", sb2.toString(), null);
        final Intent intentForBrokerAccountService = getIntentForBrokerAccountService(context);
        final BrokerAccountServiceConnection brokerAccountServiceConnection = new BrokerAccountServiceConnection();
        if (telemetryEvent != null) {
            brokerAccountServiceConnection.setTelemetryEvent(telemetryEvent);
            telemetryEvent.setBrokerAccountServerStartsBinding();
        }
        this.mPendingConnections.put(brokerAccountServiceConnection, new CallbackExecutor<BrokerAccountServiceConnection>(callback));
        final boolean bindService = context.bindService(intentForBrokerAccountService, (ServiceConnection)brokerAccountServiceConnection, 1);
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(BrokerAccountServiceHandler.TAG);
        sb3.append(":bindToBrokerAccountService");
        final String string2 = sb3.toString();
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("The status for brokerAccountService bindService call is: ");
        sb4.append((Object)bindService);
        Logger.v(string2, sb4.toString());
        if (telemetryEvent != null) {
            telemetryEvent.setBrokerAccountServiceBindingSucceed(bindService);
        }
        if (!bindService) {
            brokerAccountServiceConnection.unBindService(context);
            final StringBuilder sb5 = new StringBuilder();
            sb5.append(BrokerAccountServiceHandler.TAG);
            sb5.append(":bindToBrokerAccountService");
            Logger.e(sb5.toString(), "Failed to bind service to broker app. ", "'bindService returned false", ADALError.BROKER_BIND_SERVICE_FAILED);
            callback.onError(new AuthenticationException(ADALError.BROKER_BIND_SERVICE_FAILED));
        }
    }
    
    private UserInfo[] convertUserInfoBundleToArray(final Bundle bundle) {
        if (bundle == null) {
            Logger.v(BrokerAccountServiceHandler.TAG, "No user info returned from broker account service.");
            return new UserInfo[0];
        }
        final ArrayList<UserInfo> list = new ArrayList<UserInfo>();
        final Iterator<String> iterator = bundle.keySet().iterator();
        while (iterator.hasNext()) {
            final Bundle bundle2 = bundle.getBundle((String)iterator.next());
            list.add(new UserInfo(bundle2.getString("account.userinfo.userid"), bundle2.getString("account.userinfo.given.name"), bundle2.getString("account.userinfo.family.name"), bundle2.getString("account.userinfo.identity.provider"), bundle2.getString("account.userinfo.userid.displayable")));
        }
        return list.toArray(new UserInfo[list.size()]);
    }
    
    public static BrokerAccountServiceHandler getInstance() {
        return InstanceHolder.INSTANCE;
    }
    
    public static Intent getIntentForBrokerAccountService(final Context context) {
        final String currentActiveBrokerPackageName = new BrokerProxy(context).getCurrentActiveBrokerPackageName();
        if (currentActiveBrokerPackageName == null) {
            Logger.v(BrokerAccountServiceHandler.TAG, "No recognized broker is installed on the device.");
            return null;
        }
        final Intent intent = new Intent("com.microsoft.workaccount.BrokerAccount");
        intent.setPackage(currentActiveBrokerPackageName);
        intent.setClassName(currentActiveBrokerPackageName, "com.microsoft.aad.adal.BrokerAccountService");
        return intent;
    }
    
    private void performAsyncCallOnBound(final Context context, final Callback<BrokerAccountServiceConnection> callback, final BrokerEvent brokerEvent) {
        this.bindToBrokerAccountService(context, new Callback<BrokerAccountServiceConnection>() {
            @Override
            public void onError(final Throwable t) {
                callback.onError(t);
            }
            
            @Override
            public void onSuccess(final BrokerAccountServiceConnection brokerAccountServiceConnection) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    callback.onSuccess(brokerAccountServiceConnection);
                    brokerAccountServiceConnection.unBindService(context);
                    return;
                }
                BrokerAccountServiceHandler.sThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(brokerAccountServiceConnection);
                        brokerAccountServiceConnection.unBindService(context);
                    }
                });
            }
        }, brokerEvent);
    }
    
    private Map<String, String> prepareGetAuthTokenRequestData(final Context context, final Bundle bundle) {
        final Set keySet = bundle.keySet();
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        for (final String s : keySet) {
            String s2;
            if (!s.equals("com.microsoft.aad.adal:RequestId") && !s.equals("expiration.buffer")) {
                s2 = bundle.getString(s);
            }
            else {
                s2 = String.valueOf(bundle.getInt(s));
            }
            hashMap.put(s, s2);
        }
        hashMap.put("caller.info.package", context.getPackageName());
        return hashMap;
    }
    
    public Bundle getAuthToken(final Context context, final Bundle bundle, final BrokerEvent brokerEvent) throws AuthenticationException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AtomicReference<Bundle> atomicReference = new AtomicReference<Bundle>(null);
        final AtomicReference<InterruptedException> atomicReference2 = new AtomicReference<InterruptedException>(null);
        this.performAsyncCallOnBound(context, new Callback<BrokerAccountServiceConnection>() {
            @Override
            public void onError(final Throwable t) {
                atomicReference2.set(t);
                countDownLatch.countDown();
            }
            
            @Override
            public void onSuccess(final BrokerAccountServiceConnection brokerAccountServiceConnection) {
                final IBrokerAccountService brokerAccountServiceProvider = brokerAccountServiceConnection.getBrokerAccountServiceProvider();
                try {
                    atomicReference.set(brokerAccountServiceProvider.acquireTokenSilently(BrokerAccountServiceHandler.this.prepareGetAuthTokenRequestData(context, bundle)));
                }
                catch (RemoteException ex) {
                    atomicReference2.set(ex);
                }
                countDownLatch.countDown();
            }
        }, brokerEvent);
        try {
            countDownLatch.await();
        }
        catch (InterruptedException ex) {
            atomicReference2.set(ex);
        }
        final InterruptedException ex2 = atomicReference2.getAndSet(null);
        if (ex2 == null) {
            return atomicReference.getAndSet(null);
        }
        if (ex2 instanceof RemoteException) {
            final StringBuilder sb = new StringBuilder();
            sb.append(BrokerAccountServiceHandler.TAG);
            sb.append(":getAuthToken");
            Logger.e(sb.toString(), "Get error when trying to get token from broker. ", ex2.getMessage(), ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, ex2);
            throw new AuthenticationException(ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, ex2.getMessage(), ex2);
        }
        if (ex2 instanceof InterruptedException) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(BrokerAccountServiceHandler.TAG);
            sb2.append(":getAuthToken");
            Logger.e(sb2.toString(), "The broker account service binding call is interrupted. ", ex2.getMessage(), ADALError.BROKER_AUTHENTICATOR_EXCEPTION, ex2);
            throw new AuthenticationException(ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, ex2.getMessage(), ex2);
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(BrokerAccountServiceHandler.TAG);
        sb3.append(":getAuthToken");
        Logger.e(sb3.toString(), "Get error when trying to bind the broker account service.", ex2.getMessage(), ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, ex2);
        throw new AuthenticationException(ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, ex2.getMessage(), ex2);
    }
    
    UserInfo[] getBrokerUsers(final Context context) throws IOException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AtomicReference<Bundle> atomicReference = new AtomicReference<Bundle>(null);
        final AtomicReference<InterruptedException> atomicReference2 = new AtomicReference<InterruptedException>(null);
        this.performAsyncCallOnBound(context, new Callback<BrokerAccountServiceConnection>() {
            @Override
            public void onError(final Throwable t) {
                atomicReference2.set(t);
                countDownLatch.countDown();
            }
            
            @Override
            public void onSuccess(final BrokerAccountServiceConnection brokerAccountServiceConnection) {
                final IBrokerAccountService brokerAccountServiceProvider = brokerAccountServiceConnection.getBrokerAccountServiceProvider();
                try {
                    atomicReference.set(brokerAccountServiceProvider.getBrokerUsers());
                }
                catch (RemoteException ex) {
                    atomicReference2.set(ex);
                }
                countDownLatch.countDown();
            }
        }, null);
        try {
            countDownLatch.await();
        }
        catch (InterruptedException ex) {
            atomicReference2.set(ex);
        }
        final InterruptedException ex2 = atomicReference2.getAndSet(null);
        if (ex2 == null) {
            return this.convertUserInfoBundleToArray(atomicReference.getAndSet(null));
        }
        throw new IOException(ex2.getMessage(), ex2);
    }
    
    public Intent getIntentForInteractiveRequest(final Context context, final BrokerEvent brokerEvent) throws AuthenticationException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AtomicReference<Intent> atomicReference = new AtomicReference<Intent>(null);
        final AtomicReference<InterruptedException> atomicReference2 = new AtomicReference<InterruptedException>(null);
        this.performAsyncCallOnBound(context, new Callback<BrokerAccountServiceConnection>() {
            @Override
            public void onError(final Throwable t) {
                atomicReference2.set(t);
                countDownLatch.countDown();
            }
            
            @Override
            public void onSuccess(final BrokerAccountServiceConnection brokerAccountServiceConnection) {
                final IBrokerAccountService brokerAccountServiceProvider = brokerAccountServiceConnection.getBrokerAccountServiceProvider();
                try {
                    atomicReference.set(brokerAccountServiceProvider.getIntentForInteractiveRequest());
                }
                catch (RemoteException ex) {
                    atomicReference2.set(ex);
                }
                countDownLatch.countDown();
            }
        }, brokerEvent);
        try {
            countDownLatch.await();
        }
        catch (InterruptedException ex) {
            atomicReference2.set(ex);
        }
        final InterruptedException ex2 = atomicReference2.getAndSet(null);
        if (ex2 == null) {
            return atomicReference.getAndSet(null);
        }
        if (ex2 instanceof RemoteException) {
            Logger.e(BrokerAccountServiceHandler.TAG, "Get error when trying to get token from broker. ", ex2.getMessage(), ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, ex2);
            throw new AuthenticationException(ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, ex2.getMessage(), ex2);
        }
        if (ex2 instanceof InterruptedException) {
            Logger.e(BrokerAccountServiceHandler.TAG, "The broker account service binding call is interrupted. ", ex2.getMessage(), ADALError.BROKER_AUTHENTICATOR_EXCEPTION, ex2);
            throw new AuthenticationException(ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, ex2.getMessage(), ex2);
        }
        Logger.e(BrokerAccountServiceHandler.TAG, "Didn't receive the activity to launch from broker. ", ex2.getMessage(), ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, ex2);
        final ADALError broker_AUTHENTICATOR_NOT_RESPONDING = ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING;
        final StringBuilder sb = new StringBuilder();
        sb.append("Didn't receive the activity to launch from broker: ");
        sb.append(ex2.getMessage());
        throw new AuthenticationException(broker_AUTHENTICATOR_NOT_RESPONDING, sb.toString(), ex2);
    }
    
    public void removeAccounts(final Context context) {
        this.performAsyncCallOnBound(context, new Callback<BrokerAccountServiceConnection>() {
            @Override
            public void onError(final Throwable t) {
                final StringBuilder sb = new StringBuilder();
                sb.append(BrokerAccountServiceHandler.TAG);
                sb.append(":removeAccounts");
                Logger.e(sb.toString(), "Encounter exception when removing accounts from broker", t.getMessage(), null, t);
            }
            
            @Override
            public void onSuccess(final BrokerAccountServiceConnection brokerAccountServiceConnection) {
                try {
                    brokerAccountServiceConnection.getBrokerAccountServiceProvider().removeAccounts();
                }
                catch (RemoteException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(BrokerAccountServiceHandler.TAG);
                    sb.append(":removeAccounts");
                    Logger.e(sb.toString(), "Encounter exception when removing accounts from broker", ex.getMessage(), null, (Throwable)ex);
                }
            }
        }, null);
    }
    
    private class BrokerAccountServiceConnection implements ServiceConnection
    {
        private boolean mBound;
        private IBrokerAccountService mBrokerAccountService;
        private BrokerEvent mEvent;
        
        public IBrokerAccountService getBrokerAccountServiceProvider() {
            return this.mBrokerAccountService;
        }
        
        public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
            Logger.v(BrokerAccountServiceHandler.TAG, "Broker Account service is connected.");
            this.mBrokerAccountService = IBrokerAccountService.Stub.asInterface(binder);
            this.mBound = true;
            final BrokerEvent mEvent = this.mEvent;
            if (mEvent != null) {
                mEvent.setBrokerAccountServiceConnected();
            }
            final CallbackExecutor callbackExecutor = (CallbackExecutor)BrokerAccountServiceHandler.this.mPendingConnections.remove(this);
            if (callbackExecutor != null) {
                callbackExecutor.onSuccess(this);
                return;
            }
            Logger.v(BrokerAccountServiceHandler.TAG, "No callback is found.");
        }
        
        public void onServiceDisconnected(final ComponentName componentName) {
            Logger.v(BrokerAccountServiceHandler.TAG, "Broker Account service is disconnected.");
            this.mBound = false;
        }
        
        public void setTelemetryEvent(final BrokerEvent mEvent) {
            this.mEvent = mEvent;
        }
        
        public void unBindService(final Context context) {
            new Handler(Looper.getMainLooper()).post((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (!BrokerAccountServiceConnection.this.mBound) {
                        return;
                    }
                    try {
                        try {
                            context.unbindService((ServiceConnection)BrokerAccountServiceConnection.this);
                        }
                        finally {}
                    }
                    catch (IllegalArgumentException ex) {
                        Logger.e(BrokerAccountServiceHandler.TAG, "Unbind threw IllegalArgumentException", "", null, ex);
                    }
                    BrokerAccountServiceConnection.this.mBound = false;
                    return;
                    BrokerAccountServiceConnection.this.mBound = false;
                }
            });
        }
    }
    
    private static final class InstanceHolder
    {
        static final BrokerAccountServiceHandler INSTANCE;
        
        static {
            INSTANCE = new BrokerAccountServiceHandler(null);
        }
    }
}
