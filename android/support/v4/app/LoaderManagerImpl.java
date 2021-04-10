package android.support.v4.app;

import android.os.*;
import android.util.*;
import java.io.*;
import android.support.v4.content.*;
import android.support.v4.util.*;
import java.lang.reflect.*;

class LoaderManagerImpl extends LoaderManager
{
    static boolean DEBUG = false;
    static final String TAG = "LoaderManager";
    boolean mCreatingLoader;
    private FragmentHostCallback mHost;
    final SparseArrayCompat<LoaderInfo> mInactiveLoaders;
    final SparseArrayCompat<LoaderInfo> mLoaders;
    boolean mRetaining;
    boolean mRetainingStarted;
    boolean mStarted;
    final String mWho;
    
    static {
        LoaderManagerImpl.DEBUG = false;
    }
    
    LoaderManagerImpl(final String mWho, final FragmentHostCallback mHost, final boolean mStarted) {
        this.mLoaders = new SparseArrayCompat<LoaderInfo>();
        this.mInactiveLoaders = new SparseArrayCompat<LoaderInfo>();
        this.mWho = mWho;
        this.mHost = mHost;
        this.mStarted = mStarted;
    }
    
    private LoaderInfo createAndInstallLoader(final int n, final Bundle bundle, final LoaderCallbacks<Object> loaderCallbacks) {
        try {
            this.mCreatingLoader = true;
            final LoaderInfo loader = this.createLoader(n, bundle, loaderCallbacks);
            this.installLoader(loader);
            return loader;
        }
        finally {
            this.mCreatingLoader = false;
        }
    }
    
    private LoaderInfo createLoader(final int n, final Bundle bundle, final LoaderCallbacks<Object> loaderCallbacks) {
        final LoaderInfo loaderInfo = new LoaderInfo(n, bundle, loaderCallbacks);
        loaderInfo.mLoader = loaderCallbacks.onCreateLoader(n, bundle);
        return loaderInfo;
    }
    
    @Override
    public void destroyLoader(int indexOfKey) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        if (LoaderManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("destroyLoader in ");
            sb.append(this);
            sb.append(" of ");
            sb.append(indexOfKey);
            Log.v("LoaderManager", sb.toString());
        }
        final int indexOfKey2 = this.mLoaders.indexOfKey(indexOfKey);
        if (indexOfKey2 >= 0) {
            final LoaderInfo loaderInfo = this.mLoaders.valueAt(indexOfKey2);
            this.mLoaders.removeAt(indexOfKey2);
            loaderInfo.destroy();
        }
        indexOfKey = this.mInactiveLoaders.indexOfKey(indexOfKey);
        if (indexOfKey >= 0) {
            final LoaderInfo loaderInfo2 = this.mInactiveLoaders.valueAt(indexOfKey);
            this.mInactiveLoaders.removeAt(indexOfKey);
            loaderInfo2.destroy();
        }
        if (this.mHost != null && !this.hasRunningLoaders()) {
            this.mHost.mFragmentManager.startPendingDeferredFragments();
        }
    }
    
    void doDestroy() {
        if (!this.mRetaining) {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Destroying Active in ");
                sb.append(this);
                Log.v("LoaderManager", sb.toString());
            }
            int size = this.mLoaders.size();
            while (true) {
                --size;
                if (size < 0) {
                    break;
                }
                this.mLoaders.valueAt(size).destroy();
            }
            this.mLoaders.clear();
        }
        if (LoaderManagerImpl.DEBUG) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Destroying Inactive in ");
            sb2.append(this);
            Log.v("LoaderManager", sb2.toString());
        }
        int size2 = this.mInactiveLoaders.size();
        while (true) {
            --size2;
            if (size2 < 0) {
                break;
            }
            this.mInactiveLoaders.valueAt(size2).destroy();
        }
        this.mInactiveLoaders.clear();
    }
    
    void doReportNextStart() {
        for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
            this.mLoaders.valueAt(i).mReportNextStart = true;
        }
    }
    
    void doReportStart() {
        int size = this.mLoaders.size();
        while (true) {
            --size;
            if (size < 0) {
                break;
            }
            this.mLoaders.valueAt(size).reportStart();
        }
    }
    
    void doRetain() {
        if (LoaderManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Retaining in ");
            sb.append(this);
            Log.v("LoaderManager", sb.toString());
        }
        if (!this.mStarted) {
            final RuntimeException ex = new RuntimeException("here");
            ex.fillInStackTrace();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Called doRetain when not started: ");
            sb2.append(this);
            Log.w("LoaderManager", sb2.toString(), (Throwable)ex);
            return;
        }
        this.mRetaining = true;
        this.mStarted = false;
        for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
            this.mLoaders.valueAt(i).retain();
        }
    }
    
    void doStart() {
        if (LoaderManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Starting in ");
            sb.append(this);
            Log.v("LoaderManager", sb.toString());
        }
        if (this.mStarted) {
            final RuntimeException ex = new RuntimeException("here");
            ex.fillInStackTrace();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Called doStart when already started: ");
            sb2.append(this);
            Log.w("LoaderManager", sb2.toString(), (Throwable)ex);
            return;
        }
        this.mStarted = true;
        for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
            this.mLoaders.valueAt(i).start();
        }
    }
    
    void doStop() {
        if (LoaderManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Stopping in ");
            sb.append(this);
            Log.v("LoaderManager", sb.toString());
        }
        if (!this.mStarted) {
            final RuntimeException ex = new RuntimeException("here");
            ex.fillInStackTrace();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Called doStop when not started: ");
            sb2.append(this);
            Log.w("LoaderManager", sb2.toString(), (Throwable)ex);
            return;
        }
        int size = this.mLoaders.size();
        while (true) {
            --size;
            if (size < 0) {
                break;
            }
            this.mLoaders.valueAt(size).stop();
        }
        this.mStarted = false;
    }
    
    @Override
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        final int size = this.mLoaders.size();
        final int n = 0;
        if (size > 0) {
            printWriter.print(s);
            printWriter.println("Active Loaders:");
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("    ");
            final String string = sb.toString();
            for (int i = 0; i < this.mLoaders.size(); ++i) {
                final LoaderInfo loaderInfo = this.mLoaders.valueAt(i);
                printWriter.print(s);
                printWriter.print("  #");
                printWriter.print(this.mLoaders.keyAt(i));
                printWriter.print(": ");
                printWriter.println(loaderInfo.toString());
                loaderInfo.dump(string, fileDescriptor, printWriter, array);
            }
        }
        if (this.mInactiveLoaders.size() > 0) {
            printWriter.print(s);
            printWriter.println("Inactive Loaders:");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append("    ");
            final String string2 = sb2.toString();
            for (int j = n; j < this.mInactiveLoaders.size(); ++j) {
                final LoaderInfo loaderInfo2 = this.mInactiveLoaders.valueAt(j);
                printWriter.print(s);
                printWriter.print("  #");
                printWriter.print(this.mInactiveLoaders.keyAt(j));
                printWriter.print(": ");
                printWriter.println(loaderInfo2.toString());
                loaderInfo2.dump(string2, fileDescriptor, printWriter, array);
            }
        }
    }
    
    void finishRetain() {
        if (this.mRetaining) {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Finished Retaining in ");
                sb.append(this);
                Log.v("LoaderManager", sb.toString());
            }
            this.mRetaining = false;
            int size = this.mLoaders.size();
            while (true) {
                --size;
                if (size < 0) {
                    break;
                }
                this.mLoaders.valueAt(size).finishRetain();
            }
        }
    }
    
    @Override
    public <D> Loader<D> getLoader(final int n) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        final LoaderInfo loaderInfo = this.mLoaders.get(n);
        if (loaderInfo == null) {
            return null;
        }
        if (loaderInfo.mPendingLoader != null) {
            return (Loader<D>)loaderInfo.mPendingLoader.mLoader;
        }
        return (Loader<D>)loaderInfo.mLoader;
    }
    
    @Override
    public boolean hasRunningLoaders() {
        final int size = this.mLoaders.size();
        int i = 0;
        boolean b = false;
        while (i < size) {
            final LoaderInfo loaderInfo = this.mLoaders.valueAt(i);
            b |= (loaderInfo.mStarted && !loaderInfo.mDeliveredData);
            ++i;
        }
        return b;
    }
    
    @Override
    public <D> Loader<D> initLoader(final int n, final Bundle bundle, final LoaderCallbacks<D> mCallbacks) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        final LoaderInfo loaderInfo = this.mLoaders.get(n);
        if (LoaderManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("initLoader in ");
            sb.append(this);
            sb.append(": args=");
            sb.append(bundle);
            Log.v("LoaderManager", sb.toString());
        }
        LoaderInfo andInstallLoader;
        if (loaderInfo == null) {
            final LoaderInfo loaderInfo2 = andInstallLoader = this.createAndInstallLoader(n, bundle, (LoaderCallbacks<Object>)mCallbacks);
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("  Created new loader ");
                sb2.append(loaderInfo2);
                Log.v("LoaderManager", sb2.toString());
                andInstallLoader = loaderInfo2;
            }
        }
        else {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("  Re-using existing loader ");
                sb3.append(loaderInfo);
                Log.v("LoaderManager", sb3.toString());
            }
            loaderInfo.mCallbacks = (LoaderCallbacks<Object>)mCallbacks;
            andInstallLoader = loaderInfo;
        }
        if (andInstallLoader.mHaveData && this.mStarted) {
            andInstallLoader.callOnLoadFinished(andInstallLoader.mLoader, andInstallLoader.mData);
        }
        return (Loader<D>)andInstallLoader.mLoader;
    }
    
    void installLoader(final LoaderInfo loaderInfo) {
        this.mLoaders.put(loaderInfo.mId, loaderInfo);
        if (this.mStarted) {
            loaderInfo.start();
        }
    }
    
    @Override
    public <D> Loader<D> restartLoader(final int n, final Bundle bundle, final LoaderCallbacks<D> loaderCallbacks) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        final LoaderInfo loaderInfo = this.mLoaders.get(n);
        if (LoaderManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("restartLoader in ");
            sb.append(this);
            sb.append(": args=");
            sb.append(bundle);
            Log.v("LoaderManager", sb.toString());
        }
        Label_0402: {
            if (loaderInfo != null) {
                final LoaderInfo loaderInfo2 = this.mInactiveLoaders.get(n);
                if (loaderInfo2 != null) {
                    if (loaderInfo.mHaveData) {
                        if (LoaderManagerImpl.DEBUG) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("  Removing last inactive loader: ");
                            sb2.append(loaderInfo);
                            Log.v("LoaderManager", sb2.toString());
                        }
                        loaderInfo2.mDeliveredData = false;
                        loaderInfo2.destroy();
                    }
                    else {
                        if (!loaderInfo.mStarted) {
                            if (LoaderManagerImpl.DEBUG) {
                                Log.v("LoaderManager", "  Current loader is stopped; replacing");
                            }
                            this.mLoaders.put(n, null);
                            loaderInfo.destroy();
                            break Label_0402;
                        }
                        if (LoaderManagerImpl.DEBUG) {
                            Log.v("LoaderManager", "  Current loader is running; attempting to cancel");
                        }
                        loaderInfo.cancel();
                        if (loaderInfo.mPendingLoader != null) {
                            if (LoaderManagerImpl.DEBUG) {
                                final StringBuilder sb3 = new StringBuilder();
                                sb3.append("  Removing pending loader: ");
                                sb3.append(loaderInfo.mPendingLoader);
                                Log.v("LoaderManager", sb3.toString());
                            }
                            loaderInfo.mPendingLoader.destroy();
                            loaderInfo.mPendingLoader = null;
                        }
                        if (LoaderManagerImpl.DEBUG) {
                            Log.v("LoaderManager", "  Enqueuing as new pending loader");
                        }
                        loaderInfo.mPendingLoader = this.createLoader(n, bundle, (LoaderCallbacks<Object>)loaderCallbacks);
                        final LoaderInfo loaderInfo3 = loaderInfo.mPendingLoader;
                        return (Loader<D>)loaderInfo3.mLoader;
                    }
                }
                else if (LoaderManagerImpl.DEBUG) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("  Making last loader inactive: ");
                    sb4.append(loaderInfo);
                    Log.v("LoaderManager", sb4.toString());
                }
                loaderInfo.mLoader.abandon();
                this.mInactiveLoaders.put(n, loaderInfo);
            }
            break Label_0402;
        }
        final LoaderInfo loaderInfo3 = this.createAndInstallLoader(n, bundle, (LoaderCallbacks<Object>)loaderCallbacks);
        return (Loader<D>)loaderInfo3.mLoader;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("LoaderManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        DebugUtils.buildShortClassTag(this.mHost, sb);
        sb.append("}}");
        return sb.toString();
    }
    
    void updateHostController(final FragmentHostCallback mHost) {
        this.mHost = mHost;
    }
    
    final class LoaderInfo implements OnLoadCompleteListener<Object>, OnLoadCanceledListener<Object>
    {
        final Bundle mArgs;
        LoaderCallbacks<Object> mCallbacks;
        Object mData;
        boolean mDeliveredData;
        boolean mDestroyed;
        boolean mHaveData;
        final int mId;
        boolean mListenerRegistered;
        Loader<Object> mLoader;
        LoaderInfo mPendingLoader;
        boolean mReportNextStart;
        boolean mRetaining;
        boolean mRetainingStarted;
        boolean mStarted;
        
        public LoaderInfo(final int mId, final Bundle mArgs, final LoaderCallbacks<Object> mCallbacks) {
            this.mId = mId;
            this.mArgs = mArgs;
            this.mCallbacks = mCallbacks;
        }
        
        void callOnLoadFinished(final Loader<Object> loader, final Object o) {
            if (this.mCallbacks != null) {
                String mNoTransactionsBecause = null;
                if (LoaderManagerImpl.this.mHost != null) {
                    mNoTransactionsBecause = LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause;
                    LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = "onLoadFinished";
                }
                try {
                    if (LoaderManagerImpl.DEBUG) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("  onLoadFinished in ");
                        sb.append(loader);
                        sb.append(": ");
                        sb.append(loader.dataToString(o));
                        Log.v("LoaderManager", sb.toString());
                    }
                    this.mCallbacks.onLoadFinished(loader, o);
                    if (LoaderManagerImpl.this.mHost != null) {
                        LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = mNoTransactionsBecause;
                    }
                    this.mDeliveredData = true;
                }
                finally {
                    if (LoaderManagerImpl.this.mHost != null) {
                        LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = mNoTransactionsBecause;
                    }
                }
            }
        }
        
        void cancel() {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("  Canceling: ");
                sb.append(this);
                Log.v("LoaderManager", sb.toString());
            }
            if (this.mStarted && this.mLoader != null && this.mListenerRegistered && !this.mLoader.cancelLoad()) {
                this.onLoadCanceled(this.mLoader);
            }
        }
        
        void destroy() {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("  Destroying: ");
                sb.append(this);
                Log.v("LoaderManager", sb.toString());
            }
            this.mDestroyed = true;
            final boolean mDeliveredData = this.mDeliveredData;
            this.mDeliveredData = false;
            if (this.mCallbacks != null && this.mLoader != null && this.mHaveData && mDeliveredData) {
                if (LoaderManagerImpl.DEBUG) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("  Reseting: ");
                    sb2.append(this);
                    Log.v("LoaderManager", sb2.toString());
                }
                String mNoTransactionsBecause;
                if (LoaderManagerImpl.this.mHost != null) {
                    mNoTransactionsBecause = LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause;
                    LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = "onLoaderReset";
                }
                else {
                    mNoTransactionsBecause = null;
                }
                try {
                    this.mCallbacks.onLoaderReset(this.mLoader);
                }
                finally {
                    if (LoaderManagerImpl.this.mHost != null) {
                        LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = mNoTransactionsBecause;
                    }
                }
            }
            this.mCallbacks = null;
            this.mData = null;
            this.mHaveData = false;
            if (this.mLoader != null) {
                if (this.mListenerRegistered) {
                    this.mListenerRegistered = false;
                    this.mLoader.unregisterListener((Loader.OnLoadCompleteListener<Object>)this);
                    this.mLoader.unregisterOnLoadCanceledListener((Loader.OnLoadCanceledListener<Object>)this);
                }
                this.mLoader.reset();
            }
            if (this.mPendingLoader != null) {
                this.mPendingLoader.destroy();
            }
        }
        
        public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
            printWriter.print(s);
            printWriter.print("mId=");
            printWriter.print(this.mId);
            printWriter.print(" mArgs=");
            printWriter.println(this.mArgs);
            printWriter.print(s);
            printWriter.print("mCallbacks=");
            printWriter.println(this.mCallbacks);
            printWriter.print(s);
            printWriter.print("mLoader=");
            printWriter.println(this.mLoader);
            if (this.mLoader != null) {
                final Loader<Object> mLoader = this.mLoader;
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append("  ");
                mLoader.dump(sb.toString(), fileDescriptor, printWriter, array);
            }
            if (this.mHaveData || this.mDeliveredData) {
                printWriter.print(s);
                printWriter.print("mHaveData=");
                printWriter.print(this.mHaveData);
                printWriter.print("  mDeliveredData=");
                printWriter.println(this.mDeliveredData);
                printWriter.print(s);
                printWriter.print("mData=");
                printWriter.println(this.mData);
            }
            printWriter.print(s);
            printWriter.print("mStarted=");
            printWriter.print(this.mStarted);
            printWriter.print(" mReportNextStart=");
            printWriter.print(this.mReportNextStart);
            printWriter.print(" mDestroyed=");
            printWriter.println(this.mDestroyed);
            printWriter.print(s);
            printWriter.print("mRetaining=");
            printWriter.print(this.mRetaining);
            printWriter.print(" mRetainingStarted=");
            printWriter.print(this.mRetainingStarted);
            printWriter.print(" mListenerRegistered=");
            printWriter.println(this.mListenerRegistered);
            if (this.mPendingLoader != null) {
                printWriter.print(s);
                printWriter.println("Pending Loader ");
                printWriter.print(this.mPendingLoader);
                printWriter.println(":");
                final LoaderInfo mPendingLoader = this.mPendingLoader;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s);
                sb2.append("  ");
                mPendingLoader.dump(sb2.toString(), fileDescriptor, printWriter, array);
            }
        }
        
        void finishRetain() {
            if (this.mRetaining) {
                if (LoaderManagerImpl.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("  Finished Retaining: ");
                    sb.append(this);
                    Log.v("LoaderManager", sb.toString());
                }
                this.mRetaining = false;
                if (this.mStarted != this.mRetainingStarted && !this.mStarted) {
                    this.stop();
                }
            }
            if (this.mStarted && this.mHaveData && !this.mReportNextStart) {
                this.callOnLoadFinished(this.mLoader, this.mData);
            }
        }
        
        @Override
        public void onLoadCanceled(final Loader<Object> loader) {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onLoadCanceled: ");
                sb.append(this);
                Log.v("LoaderManager", sb.toString());
            }
            if (this.mDestroyed) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v("LoaderManager", "  Ignoring load canceled -- destroyed");
                }
            }
            else if (LoaderManagerImpl.this.mLoaders.get(this.mId) != this) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v("LoaderManager", "  Ignoring load canceled -- not active");
                }
            }
            else {
                final LoaderInfo mPendingLoader = this.mPendingLoader;
                if (mPendingLoader != null) {
                    if (LoaderManagerImpl.DEBUG) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("  Switching to pending loader: ");
                        sb2.append(mPendingLoader);
                        Log.v("LoaderManager", sb2.toString());
                    }
                    this.mPendingLoader = null;
                    LoaderManagerImpl.this.mLoaders.put(this.mId, null);
                    this.destroy();
                    LoaderManagerImpl.this.installLoader(mPendingLoader);
                }
            }
        }
        
        @Override
        public void onLoadComplete(final Loader<Object> loader, final Object mData) {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onLoadComplete: ");
                sb.append(this);
                Log.v("LoaderManager", sb.toString());
            }
            if (this.mDestroyed) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v("LoaderManager", "  Ignoring load complete -- destroyed");
                }
            }
            else if (LoaderManagerImpl.this.mLoaders.get(this.mId) != this) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v("LoaderManager", "  Ignoring load complete -- not active");
                }
            }
            else {
                final LoaderInfo mPendingLoader = this.mPendingLoader;
                if (mPendingLoader != null) {
                    if (LoaderManagerImpl.DEBUG) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("  Switching to pending loader: ");
                        sb2.append(mPendingLoader);
                        Log.v("LoaderManager", sb2.toString());
                    }
                    this.mPendingLoader = null;
                    LoaderManagerImpl.this.mLoaders.put(this.mId, null);
                    this.destroy();
                    LoaderManagerImpl.this.installLoader(mPendingLoader);
                    return;
                }
                if (this.mData != mData || !this.mHaveData) {
                    this.mData = mData;
                    this.mHaveData = true;
                    if (this.mStarted) {
                        this.callOnLoadFinished(loader, mData);
                    }
                }
                final LoaderInfo loaderInfo = LoaderManagerImpl.this.mInactiveLoaders.get(this.mId);
                if (loaderInfo != null && loaderInfo != this) {
                    loaderInfo.mDeliveredData = false;
                    loaderInfo.destroy();
                    LoaderManagerImpl.this.mInactiveLoaders.remove(this.mId);
                }
                if (LoaderManagerImpl.this.mHost != null && !LoaderManagerImpl.this.hasRunningLoaders()) {
                    LoaderManagerImpl.this.mHost.mFragmentManager.startPendingDeferredFragments();
                }
            }
        }
        
        void reportStart() {
            if (this.mStarted && this.mReportNextStart) {
                this.mReportNextStart = false;
                if (this.mHaveData) {
                    this.callOnLoadFinished(this.mLoader, this.mData);
                }
            }
        }
        
        void retain() {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("  Retaining: ");
                sb.append(this);
                Log.v("LoaderManager", sb.toString());
            }
            this.mRetaining = true;
            this.mRetainingStarted = this.mStarted;
            this.mStarted = false;
            this.mCallbacks = null;
        }
        
        void start() {
            if (this.mRetaining && this.mRetainingStarted) {
                this.mStarted = true;
                return;
            }
            if (!this.mStarted) {
                this.mStarted = true;
                if (LoaderManagerImpl.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("  Starting: ");
                    sb.append(this);
                    Log.v("LoaderManager", sb.toString());
                }
                if (this.mLoader == null && this.mCallbacks != null) {
                    this.mLoader = this.mCallbacks.onCreateLoader(this.mId, this.mArgs);
                }
                if (this.mLoader != null) {
                    if (this.mLoader.getClass().isMemberClass() && !Modifier.isStatic(this.mLoader.getClass().getModifiers())) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Object returned from onCreateLoader must not be a non-static inner member class: ");
                        sb2.append(this.mLoader);
                        throw new IllegalArgumentException(sb2.toString());
                    }
                    if (!this.mListenerRegistered) {
                        this.mLoader.registerListener(this.mId, (Loader.OnLoadCompleteListener<Object>)this);
                        this.mLoader.registerOnLoadCanceledListener((Loader.OnLoadCanceledListener<Object>)this);
                        this.mListenerRegistered = true;
                    }
                    this.mLoader.startLoading();
                }
            }
        }
        
        void stop() {
            if (LoaderManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("  Stopping: ");
                sb.append(this);
                Log.v("LoaderManager", sb.toString());
            }
            this.mStarted = false;
            if (!this.mRetaining && this.mLoader != null && this.mListenerRegistered) {
                this.mListenerRegistered = false;
                this.mLoader.unregisterListener((Loader.OnLoadCompleteListener<Object>)this);
                this.mLoader.unregisterOnLoadCanceledListener((Loader.OnLoadCanceledListener<Object>)this);
                this.mLoader.stopLoading();
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(64);
            sb.append("LoaderInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" #");
            sb.append(this.mId);
            sb.append(" : ");
            DebugUtils.buildShortClassTag(this.mLoader, sb);
            sb.append("}}");
            return sb.toString();
        }
    }
}
