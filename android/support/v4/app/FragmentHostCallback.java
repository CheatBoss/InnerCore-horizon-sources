package android.support.v4.app;

import android.app.*;
import android.support.v4.util.*;
import java.io.*;
import android.view.*;
import android.support.annotation.*;
import android.content.*;
import android.os.*;

public abstract class FragmentHostCallback<E> extends FragmentContainer
{
    private final Activity mActivity;
    private SimpleArrayMap<String, LoaderManager> mAllLoaderManagers;
    private boolean mCheckedForLoaderManager;
    final Context mContext;
    final FragmentManagerImpl mFragmentManager;
    private final Handler mHandler;
    private LoaderManagerImpl mLoaderManager;
    private boolean mLoadersStarted;
    private boolean mRetainLoaders;
    final int mWindowAnimations;
    
    FragmentHostCallback(final Activity mActivity, final Context mContext, final Handler mHandler, final int mWindowAnimations) {
        this.mFragmentManager = new FragmentManagerImpl();
        this.mActivity = mActivity;
        this.mContext = mContext;
        this.mHandler = mHandler;
        this.mWindowAnimations = mWindowAnimations;
    }
    
    public FragmentHostCallback(final Context context, final Handler handler, final int n) {
        this(null, context, handler, n);
    }
    
    FragmentHostCallback(final FragmentActivity fragmentActivity) {
        this(fragmentActivity, (Context)fragmentActivity, fragmentActivity.mHandler, 0);
    }
    
    void doLoaderDestroy() {
        if (this.mLoaderManager == null) {
            return;
        }
        this.mLoaderManager.doDestroy();
    }
    
    void doLoaderRetain() {
        if (this.mLoaderManager == null) {
            return;
        }
        this.mLoaderManager.doRetain();
    }
    
    void doLoaderStart() {
        if (this.mLoadersStarted) {
            return;
        }
        this.mLoadersStarted = true;
        Label_0072: {
            if (this.mLoaderManager == null) {
                if (this.mCheckedForLoaderManager) {
                    break Label_0072;
                }
                this.mLoaderManager = this.getLoaderManager("(root)", this.mLoadersStarted, false);
                if (this.mLoaderManager == null || this.mLoaderManager.mStarted) {
                    break Label_0072;
                }
            }
            this.mLoaderManager.doStart();
        }
        this.mCheckedForLoaderManager = true;
    }
    
    void doLoaderStop(final boolean mRetainLoaders) {
        this.mRetainLoaders = mRetainLoaders;
        if (this.mLoaderManager == null) {
            return;
        }
        if (this.mLoadersStarted) {
            this.mLoadersStarted = false;
            if (mRetainLoaders) {
                this.mLoaderManager.doRetain();
                return;
            }
            this.mLoaderManager.doStop();
        }
    }
    
    void dumpLoaders(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        printWriter.print(s);
        printWriter.print("mLoadersStarted=");
        printWriter.println(this.mLoadersStarted);
        if (this.mLoaderManager != null) {
            printWriter.print(s);
            printWriter.print("Loader Manager ");
            printWriter.print(Integer.toHexString(System.identityHashCode(this.mLoaderManager)));
            printWriter.println(":");
            final LoaderManagerImpl mLoaderManager = this.mLoaderManager;
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("  ");
            mLoaderManager.dump(sb.toString(), fileDescriptor, printWriter, array);
        }
    }
    
    Activity getActivity() {
        return this.mActivity;
    }
    
    Context getContext() {
        return this.mContext;
    }
    
    FragmentManagerImpl getFragmentManagerImpl() {
        return this.mFragmentManager;
    }
    
    Handler getHandler() {
        return this.mHandler;
    }
    
    LoaderManagerImpl getLoaderManager(final String s, final boolean b, final boolean b2) {
        if (this.mAllLoaderManagers == null) {
            this.mAllLoaderManagers = new SimpleArrayMap<String, LoaderManager>();
        }
        LoaderManagerImpl loaderManagerImpl = this.mAllLoaderManagers.get(s);
        if (loaderManagerImpl == null) {
            if (b2) {
                loaderManagerImpl = new LoaderManagerImpl(s, this, b);
                this.mAllLoaderManagers.put(s, loaderManagerImpl);
            }
            return loaderManagerImpl;
        }
        loaderManagerImpl.updateHostController(this);
        return loaderManagerImpl;
    }
    
    LoaderManagerImpl getLoaderManagerImpl() {
        if (this.mLoaderManager != null) {
            return this.mLoaderManager;
        }
        this.mCheckedForLoaderManager = true;
        return this.mLoaderManager = this.getLoaderManager("(root)", this.mLoadersStarted, true);
    }
    
    boolean getRetainLoaders() {
        return this.mRetainLoaders;
    }
    
    void inactivateFragment(final String s) {
        if (this.mAllLoaderManagers != null) {
            final LoaderManagerImpl loaderManagerImpl = this.mAllLoaderManagers.get(s);
            if (loaderManagerImpl != null && !loaderManagerImpl.mRetaining) {
                loaderManagerImpl.doDestroy();
                this.mAllLoaderManagers.remove(s);
            }
        }
    }
    
    void onAttachFragment(final Fragment fragment) {
    }
    
    public void onDump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
    }
    
    @Nullable
    @Override
    public View onFindViewById(final int n) {
        return null;
    }
    
    @Nullable
    public abstract E onGetHost();
    
    public LayoutInflater onGetLayoutInflater() {
        return (LayoutInflater)this.mContext.getSystemService("layout_inflater");
    }
    
    public int onGetWindowAnimations() {
        return this.mWindowAnimations;
    }
    
    @Override
    public boolean onHasView() {
        return true;
    }
    
    public boolean onHasWindowAnimations() {
        return true;
    }
    
    public void onRequestPermissionsFromFragment(@NonNull final Fragment fragment, @NonNull final String[] array, final int n) {
    }
    
    public boolean onShouldSaveFragmentState(final Fragment fragment) {
        return true;
    }
    
    public boolean onShouldShowRequestPermissionRationale(@NonNull final String s) {
        return false;
    }
    
    public void onStartActivityFromFragment(final Fragment fragment, final Intent intent, final int n) {
        this.onStartActivityFromFragment(fragment, intent, n, null);
    }
    
    public void onStartActivityFromFragment(final Fragment fragment, final Intent intent, final int n, @Nullable final Bundle bundle) {
        if (n != -1) {
            throw new IllegalStateException("Starting activity with a requestCode requires a FragmentActivity host");
        }
        this.mContext.startActivity(intent);
    }
    
    public void onSupportInvalidateOptionsMenu() {
    }
    
    void reportLoaderStart() {
        if (this.mAllLoaderManagers != null) {
            final int size = this.mAllLoaderManagers.size();
            final LoaderManagerImpl[] array = new LoaderManagerImpl[size];
            for (int i = size - 1; i >= 0; --i) {
                array[i] = (LoaderManagerImpl)this.mAllLoaderManagers.valueAt(i);
            }
            for (int j = 0; j < size; ++j) {
                final LoaderManagerImpl loaderManagerImpl = array[j];
                loaderManagerImpl.finishRetain();
                loaderManagerImpl.doReportStart();
            }
        }
    }
    
    void restoreLoaderNonConfig(final SimpleArrayMap<String, LoaderManager> mAllLoaderManagers) {
        this.mAllLoaderManagers = mAllLoaderManagers;
    }
    
    SimpleArrayMap<String, LoaderManager> retainLoaderNonConfig() {
        final SimpleArrayMap<String, LoaderManager> mAllLoaderManagers = this.mAllLoaderManagers;
        int n = 0;
        final boolean b = false;
        if (mAllLoaderManagers != null) {
            final int size = this.mAllLoaderManagers.size();
            final LoaderManagerImpl[] array = new LoaderManagerImpl[size];
            for (int i = size - 1; i >= 0; --i) {
                array[i] = (LoaderManagerImpl)this.mAllLoaderManagers.valueAt(i);
            }
            int n2 = 0;
            int n3 = b ? 1 : 0;
            while (true) {
                n = n3;
                if (n2 >= size) {
                    break;
                }
                final LoaderManagerImpl loaderManagerImpl = array[n2];
                if (loaderManagerImpl.mRetaining) {
                    n3 = 1;
                }
                else {
                    loaderManagerImpl.doDestroy();
                    this.mAllLoaderManagers.remove(loaderManagerImpl.mWho);
                }
                ++n2;
            }
        }
        if (n != 0) {
            return this.mAllLoaderManagers;
        }
        return null;
    }
}
