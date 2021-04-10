package android.support.v4.app;

import java.lang.reflect.*;
import android.content.*;
import android.support.v4.view.*;
import java.io.*;
import java.util.*;
import android.view.animation.*;
import android.view.*;
import android.util.*;
import android.content.res.*;
import android.os.*;
import android.support.v4.util.*;
import android.graphics.*;
import android.support.annotation.*;

final class FragmentManagerImpl extends FragmentManager implements LayoutInflaterFactory
{
    static final Interpolator ACCELERATE_CUBIC;
    static final Interpolator ACCELERATE_QUINT;
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final Interpolator DECELERATE_CUBIC;
    static final Interpolator DECELERATE_QUINT;
    static final boolean HONEYCOMB;
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    static Field sAnimationListenerField;
    ArrayList<Fragment> mActive;
    ArrayList<Fragment> mAdded;
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<Integer> mAvailIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    FragmentController mController;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState;
    boolean mDestroyed;
    Runnable mExecCommit;
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback mHost;
    boolean mNeedMenuInvalidate;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList<Runnable> mPendingActions;
    SparseArray<Parcelable> mStateArray;
    Bundle mStateBundle;
    boolean mStateSaved;
    Runnable[] mTmpActions;
    
    static {
        boolean honeycomb = false;
        FragmentManagerImpl.DEBUG = false;
        if (Build$VERSION.SDK_INT >= 11) {
            honeycomb = true;
        }
        HONEYCOMB = honeycomb;
        FragmentManagerImpl.sAnimationListenerField = null;
        DECELERATE_QUINT = (Interpolator)new DecelerateInterpolator(2.5f);
        DECELERATE_CUBIC = (Interpolator)new DecelerateInterpolator(1.5f);
        ACCELERATE_QUINT = (Interpolator)new AccelerateInterpolator(2.5f);
        ACCELERATE_CUBIC = (Interpolator)new AccelerateInterpolator(1.5f);
    }
    
    FragmentManagerImpl() {
        this.mCurState = 0;
        this.mStateBundle = null;
        this.mStateArray = null;
        this.mExecCommit = new Runnable() {
            @Override
            public void run() {
                FragmentManagerImpl.this.execPendingActions();
            }
        };
    }
    
    private void checkStateLoss() {
        if (this.mStateSaved) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
        if (this.mNoTransactionsBecause != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Can not perform this action inside of ");
            sb.append(this.mNoTransactionsBecause);
            throw new IllegalStateException(sb.toString());
        }
    }
    
    static Animation makeFadeAnimation(final Context context, final float n, final float n2) {
        final AlphaAnimation alphaAnimation = new AlphaAnimation(n, n2);
        alphaAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_CUBIC);
        alphaAnimation.setDuration(220L);
        return (Animation)alphaAnimation;
    }
    
    static Animation makeOpenCloseAnimation(final Context context, final float n, final float n2, final float n3, final float n4) {
        final AnimationSet set = new AnimationSet(false);
        final ScaleAnimation scaleAnimation = new ScaleAnimation(n, n2, n, n2, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_QUINT);
        scaleAnimation.setDuration(220L);
        set.addAnimation((Animation)scaleAnimation);
        final AlphaAnimation alphaAnimation = new AlphaAnimation(n3, n4);
        alphaAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_CUBIC);
        alphaAnimation.setDuration(220L);
        set.addAnimation((Animation)alphaAnimation);
        return (Animation)set;
    }
    
    static boolean modifiesAlpha(final Animation animation) {
        if (!(animation instanceof AlphaAnimation)) {
            if (animation instanceof AnimationSet) {
                final List animations = ((AnimationSet)animation).getAnimations();
                for (int i = 0; i < animations.size(); ++i) {
                    if (animations.get(i) instanceof AlphaAnimation) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }
    
    public static int reverseTransit(final int n) {
        if (n == 4097) {
            return 8194;
        }
        if (n == 4099) {
            return 4099;
        }
        if (n != 8194) {
            return 0;
        }
        return 4097;
    }
    
    private void setHWLayerAnimListenerIfAlpha(final View view, final Animation animation) {
        if (view != null) {
            if (animation == null) {
                return;
            }
            if (shouldRunOnHWLayer(view, animation)) {
                final Animation$AnimationListener animation$AnimationListener = null;
                Animation$AnimationListener animation$AnimationListener2 = null;
                Label_0084: {
                    String s;
                    try {
                        if (FragmentManagerImpl.sAnimationListenerField == null) {
                            (FragmentManagerImpl.sAnimationListenerField = Animation.class.getDeclaredField("mListener")).setAccessible(true);
                        }
                        animation$AnimationListener2 = (Animation$AnimationListener)FragmentManagerImpl.sAnimationListenerField.get(animation);
                        break Label_0084;
                    }
                    catch (IllegalAccessException ex) {
                        s = "Cannot access Animation's mListener field";
                    }
                    catch (NoSuchFieldException ex) {
                        s = "No field with the name mListener is found in Animation class";
                    }
                    final IllegalAccessException ex;
                    Log.e("FragmentManager", s, (Throwable)ex);
                    animation$AnimationListener2 = animation$AnimationListener;
                }
                animation.setAnimationListener((Animation$AnimationListener)new AnimateOnHWLayerIfNeededListener(view, animation, animation$AnimationListener2));
            }
        }
    }
    
    static boolean shouldRunOnHWLayer(final View view, final Animation animation) {
        return Build$VERSION.SDK_INT >= 19 && ViewCompat.getLayerType(view) == 0 && ViewCompat.hasOverlappingRendering(view) && modifiesAlpha(animation);
    }
    
    private void throwException(final RuntimeException ex) {
        Log.e("FragmentManager", ex.getMessage());
        Log.e("FragmentManager", "Activity state:");
        final PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
        if (this.mHost != null) {
            try {
                this.mHost.onDump("  ", null, printWriter, new String[0]);
            }
            catch (Exception ex2) {
                Log.e("FragmentManager", "Failed dumping state", (Throwable)ex2);
            }
        }
        else {
            this.dump("  ", null, printWriter, new String[0]);
        }
        throw ex;
    }
    
    public static int transitToStyleIndex(final int n, final boolean b) {
        if (n != 4097) {
            if (n != 4099) {
                if (n != 8194) {
                    return -1;
                }
                if (b) {
                    return 3;
                }
                return 4;
            }
            else {
                if (b) {
                    return 5;
                }
                return 6;
            }
        }
        else {
            if (b) {
                return 1;
            }
            return 2;
        }
    }
    
    void addBackStackState(final BackStackRecord backStackRecord) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList<BackStackRecord>();
        }
        this.mBackStack.add(backStackRecord);
        this.reportBackStackChanged();
    }
    
    public void addFragment(final Fragment fragment, final boolean b) {
        if (this.mAdded == null) {
            this.mAdded = new ArrayList<Fragment>();
        }
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("add: ");
            sb.append(fragment);
            Log.v("FragmentManager", sb.toString());
        }
        this.makeActive(fragment);
        if (!fragment.mDetached) {
            if (this.mAdded.contains(fragment)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Fragment already added: ");
                sb2.append(fragment);
                throw new IllegalStateException(sb2.toString());
            }
            this.mAdded.add(fragment);
            fragment.mAdded = true;
            fragment.mRemoving = false;
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            if (b) {
                this.moveToState(fragment);
            }
        }
    }
    
    @Override
    public void addOnBackStackChangedListener(final OnBackStackChangedListener onBackStackChangedListener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList<OnBackStackChangedListener>();
        }
        this.mBackStackChangeListeners.add(onBackStackChangedListener);
    }
    
    public int allocBackStackIndex(final BackStackRecord backStackRecord) {
        synchronized (this) {
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                final int intValue = this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1);
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Adding back stack index ");
                    sb.append(intValue);
                    sb.append(" with ");
                    sb.append(backStackRecord);
                    Log.v("FragmentManager", sb.toString());
                }
                this.mBackStackIndices.set(intValue, backStackRecord);
                return intValue;
            }
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<BackStackRecord>();
            }
            final int size = this.mBackStackIndices.size();
            if (FragmentManagerImpl.DEBUG) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Setting back stack index ");
                sb2.append(size);
                sb2.append(" to ");
                sb2.append(backStackRecord);
                Log.v("FragmentManager", sb2.toString());
            }
            this.mBackStackIndices.add(backStackRecord);
            return size;
        }
    }
    
    public void attachController(final FragmentHostCallback mHost, final FragmentContainer mContainer, final Fragment mParent) {
        if (this.mHost != null) {
            throw new IllegalStateException("Already attached");
        }
        this.mHost = mHost;
        this.mContainer = mContainer;
        this.mParent = mParent;
    }
    
    public void attachFragment(final Fragment fragment, final int n, final int n2) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("attach: ");
            sb.append(fragment);
            Log.v("FragmentManager", sb.toString());
        }
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (!fragment.mAdded) {
                if (this.mAdded == null) {
                    this.mAdded = new ArrayList<Fragment>();
                }
                if (this.mAdded.contains(fragment)) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Fragment already added: ");
                    sb2.append(fragment);
                    throw new IllegalStateException(sb2.toString());
                }
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("add from attach: ");
                    sb3.append(fragment);
                    Log.v("FragmentManager", sb3.toString());
                }
                this.mAdded.add(fragment);
                fragment.mAdded = true;
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                this.moveToState(fragment, this.mCurState, n, n2, false);
            }
        }
    }
    
    @Override
    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }
    
    public void detachFragment(final Fragment fragment, final int n, final int n2) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("detach: ");
            sb.append(fragment);
            Log.v("FragmentManager", sb.toString());
        }
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                if (this.mAdded != null) {
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("remove from detach: ");
                        sb2.append(fragment);
                        Log.v("FragmentManager", sb2.toString());
                    }
                    this.mAdded.remove(fragment);
                }
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                this.moveToState(fragment, 1, n, n2, fragment.mAdded = false);
            }
        }
    }
    
    public void dispatchActivityCreated() {
        this.moveToState(2, this.mStateSaved = false);
    }
    
    public void dispatchConfigurationChanged(final Configuration configuration) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); ++i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null) {
                    fragment.performConfigurationChanged(configuration);
                }
            }
        }
    }
    
    public boolean dispatchContextItemSelected(final MenuItem menuItem) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); ++i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null && fragment.performContextItemSelected(menuItem)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void dispatchCreate() {
        this.moveToState(1, this.mStateSaved = false);
    }
    
    public boolean dispatchCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        final ArrayList<Fragment> mAdded = this.mAdded;
        ArrayList<Fragment> mCreatedMenus = null;
        ArrayList<Fragment> list = null;
        final int n = 0;
        boolean b2;
        if (mAdded != null) {
            boolean b = false;
            int n2 = 0;
            while (true) {
                b2 = b;
                mCreatedMenus = list;
                if (n2 >= this.mAdded.size()) {
                    break;
                }
                final Fragment fragment = this.mAdded.get(n2);
                boolean b3 = b;
                ArrayList<Fragment> list2 = list;
                if (fragment != null) {
                    b3 = b;
                    list2 = list;
                    if (fragment.performCreateOptionsMenu(menu, menuInflater)) {
                        if ((list2 = list) == null) {
                            list2 = new ArrayList<Fragment>();
                        }
                        list2.add(fragment);
                        b3 = true;
                    }
                }
                ++n2;
                b = b3;
                list = list2;
            }
        }
        else {
            b2 = false;
        }
        if (this.mCreatedMenus != null) {
            for (int i = n; i < this.mCreatedMenus.size(); ++i) {
                final Fragment fragment2 = this.mCreatedMenus.get(i);
                if (mCreatedMenus == null || !mCreatedMenus.contains(fragment2)) {
                    fragment2.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = mCreatedMenus;
        return b2;
    }
    
    public void dispatchDestroy() {
        this.mDestroyed = true;
        this.execPendingActions();
        this.moveToState(0, false);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
    }
    
    public void dispatchDestroyView() {
        this.moveToState(1, false);
    }
    
    public void dispatchLowMemory() {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); ++i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null) {
                    fragment.performLowMemory();
                }
            }
        }
    }
    
    public boolean dispatchOptionsItemSelected(final MenuItem menuItem) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); ++i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null && fragment.performOptionsItemSelected(menuItem)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void dispatchOptionsMenuClosed(final Menu menu) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); ++i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null) {
                    fragment.performOptionsMenuClosed(menu);
                }
            }
        }
    }
    
    public void dispatchPause() {
        this.moveToState(4, false);
    }
    
    public boolean dispatchPrepareOptionsMenu(final Menu menu) {
        final ArrayList<Fragment> mAdded = this.mAdded;
        boolean b = false;
        boolean b2 = false;
        if (mAdded != null) {
            int n = 0;
            while (true) {
                b = b2;
                if (n >= this.mAdded.size()) {
                    break;
                }
                final Fragment fragment = this.mAdded.get(n);
                boolean b3 = b2;
                if (fragment != null) {
                    b3 = b2;
                    if (fragment.performPrepareOptionsMenu(menu)) {
                        b3 = true;
                    }
                }
                ++n;
                b2 = b3;
            }
        }
        return b;
    }
    
    public void dispatchReallyStop() {
        this.moveToState(2, false);
    }
    
    public void dispatchResume() {
        this.moveToState(5, this.mStateSaved = false);
    }
    
    public void dispatchStart() {
        this.moveToState(4, this.mStateSaved = false);
    }
    
    public void dispatchStop() {
        this.mStateSaved = true;
        this.moveToState(3, false);
    }
    
    @Override
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("    ");
        final String string = sb.toString();
        final ArrayList<Fragment> mActive = this.mActive;
        final int n = 0;
        if (mActive != null) {
            final int size = this.mActive.size();
            if (size > 0) {
                printWriter.print(s);
                printWriter.print("Active Fragments in ");
                printWriter.print(Integer.toHexString(System.identityHashCode(this)));
                printWriter.println(":");
                for (int i = 0; i < size; ++i) {
                    final Fragment fragment = this.mActive.get(i);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(i);
                    printWriter.print(": ");
                    printWriter.println(fragment);
                    if (fragment != null) {
                        fragment.dump(string, fileDescriptor, printWriter, array);
                    }
                }
            }
        }
        if (this.mAdded != null) {
            final int size2 = this.mAdded.size();
            if (size2 > 0) {
                printWriter.print(s);
                printWriter.println("Added Fragments:");
                for (int j = 0; j < size2; ++j) {
                    final Fragment fragment2 = this.mAdded.get(j);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(j);
                    printWriter.print(": ");
                    printWriter.println(fragment2.toString());
                }
            }
        }
        if (this.mCreatedMenus != null) {
            final int size3 = this.mCreatedMenus.size();
            if (size3 > 0) {
                printWriter.print(s);
                printWriter.println("Fragments Created Menus:");
                for (int k = 0; k < size3; ++k) {
                    final Fragment fragment3 = this.mCreatedMenus.get(k);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(k);
                    printWriter.print(": ");
                    printWriter.println(fragment3.toString());
                }
            }
        }
        if (this.mBackStack != null) {
            final int size4 = this.mBackStack.size();
            if (size4 > 0) {
                printWriter.print(s);
                printWriter.println("Back Stack:");
                for (int l = 0; l < size4; ++l) {
                    final BackStackRecord backStackRecord = this.mBackStack.get(l);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(l);
                    printWriter.print(": ");
                    printWriter.println(backStackRecord.toString());
                    backStackRecord.dump(string, fileDescriptor, printWriter, array);
                }
            }
        }
        synchronized (this) {
            if (this.mBackStackIndices != null) {
                final int size5 = this.mBackStackIndices.size();
                if (size5 > 0) {
                    printWriter.print(s);
                    printWriter.println("Back Stack Indices:");
                    for (int n2 = 0; n2 < size5; ++n2) {
                        final BackStackRecord backStackRecord2 = this.mBackStackIndices.get(n2);
                        printWriter.print(s);
                        printWriter.print("  #");
                        printWriter.print(n2);
                        printWriter.print(": ");
                        printWriter.println(backStackRecord2);
                    }
                }
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                printWriter.print(s);
                printWriter.print("mAvailBackStackIndices: ");
                printWriter.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
            // monitorexit(this)
            if (this.mPendingActions != null) {
                final int size6 = this.mPendingActions.size();
                if (size6 > 0) {
                    printWriter.print(s);
                    printWriter.println("Pending Actions:");
                    for (int n3 = n; n3 < size6; ++n3) {
                        final Runnable runnable = this.mPendingActions.get(n3);
                        printWriter.print(s);
                        printWriter.print("  #");
                        printWriter.print(n3);
                        printWriter.print(": ");
                        printWriter.println(runnable);
                    }
                }
            }
            printWriter.print(s);
            printWriter.println("FragmentManager misc state:");
            printWriter.print(s);
            printWriter.print("  mHost=");
            printWriter.println(this.mHost);
            printWriter.print(s);
            printWriter.print("  mContainer=");
            printWriter.println(this.mContainer);
            if (this.mParent != null) {
                printWriter.print(s);
                printWriter.print("  mParent=");
                printWriter.println(this.mParent);
            }
            printWriter.print(s);
            printWriter.print("  mCurState=");
            printWriter.print(this.mCurState);
            printWriter.print(" mStateSaved=");
            printWriter.print(this.mStateSaved);
            printWriter.print(" mDestroyed=");
            printWriter.println(this.mDestroyed);
            if (this.mNeedMenuInvalidate) {
                printWriter.print(s);
                printWriter.print("  mNeedMenuInvalidate=");
                printWriter.println(this.mNeedMenuInvalidate);
            }
            if (this.mNoTransactionsBecause != null) {
                printWriter.print(s);
                printWriter.print("  mNoTransactionsBecause=");
                printWriter.println(this.mNoTransactionsBecause);
            }
            if (this.mAvailIndices != null && this.mAvailIndices.size() > 0) {
                printWriter.print(s);
                printWriter.print("  mAvailIndices: ");
                printWriter.println(Arrays.toString(this.mAvailIndices.toArray()));
            }
        }
    }
    
    public void enqueueAction(final Runnable runnable, final boolean b) {
        if (!b) {
            this.checkStateLoss();
        }
        synchronized (this) {
            if (!this.mDestroyed && this.mHost != null) {
                if (this.mPendingActions == null) {
                    this.mPendingActions = new ArrayList<Runnable>();
                }
                this.mPendingActions.add(runnable);
                if (this.mPendingActions.size() == 1) {
                    this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                    this.mHost.getHandler().post(this.mExecCommit);
                }
                return;
            }
            throw new IllegalStateException("Activity has been destroyed");
        }
    }
    
    public boolean execPendingActions() {
        if (this.mExecutingActions) {
            throw new IllegalStateException("Recursive entry to executePendingTransactions");
        }
        if (Looper.myLooper() != this.mHost.getHandler().getLooper()) {
            throw new IllegalStateException("Must be called from main thread of process");
        }
        boolean b = false;
        while (true) {
            synchronized (this) {
                if (this.mPendingActions == null || this.mPendingActions.size() == 0) {
                    // monitorexit(this)
                    if (this.mHavePendingDeferredStart) {
                        int i = 0;
                        boolean b2 = false;
                        while (i < this.mActive.size()) {
                            final Fragment fragment = this.mActive.get(i);
                            boolean b3 = b2;
                            if (fragment != null) {
                                b3 = b2;
                                if (fragment.mLoaderManager != null) {
                                    b3 = (b2 | fragment.mLoaderManager.hasRunningLoaders());
                                }
                            }
                            ++i;
                            b2 = b3;
                        }
                        if (!b2) {
                            this.mHavePendingDeferredStart = false;
                            this.startPendingDeferredFragments();
                        }
                    }
                    return b;
                }
                final int size = this.mPendingActions.size();
                if (this.mTmpActions == null || this.mTmpActions.length < size) {
                    this.mTmpActions = new Runnable[size];
                }
                this.mPendingActions.toArray(this.mTmpActions);
                this.mPendingActions.clear();
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                // monitorexit(this)
                this.mExecutingActions = true;
                for (int j = 0; j < size; ++j) {
                    this.mTmpActions[j].run();
                    this.mTmpActions[j] = null;
                }
                this.mExecutingActions = false;
                b = true;
            }
        }
    }
    
    @Override
    public boolean executePendingTransactions() {
        return this.execPendingActions();
    }
    
    @Override
    public Fragment findFragmentById(final int n) {
        if (this.mAdded != null) {
            int size = this.mAdded.size();
            while (true) {
                --size;
                if (size < 0) {
                    break;
                }
                final Fragment fragment = this.mAdded.get(size);
                if (fragment != null && fragment.mFragmentId == n) {
                    return fragment;
                }
            }
        }
        if (this.mActive != null) {
            int size2 = this.mActive.size();
            while (true) {
                --size2;
                if (size2 < 0) {
                    break;
                }
                final Fragment fragment2 = this.mActive.get(size2);
                if (fragment2 != null && fragment2.mFragmentId == n) {
                    return fragment2;
                }
            }
        }
        return null;
    }
    
    @Override
    public Fragment findFragmentByTag(final String s) {
        if (this.mAdded != null && s != null) {
            int size = this.mAdded.size();
            while (true) {
                --size;
                if (size < 0) {
                    break;
                }
                final Fragment fragment = this.mAdded.get(size);
                if (fragment != null && s.equals(fragment.mTag)) {
                    return fragment;
                }
            }
        }
        if (this.mActive != null && s != null) {
            int size2 = this.mActive.size();
            while (true) {
                --size2;
                if (size2 < 0) {
                    break;
                }
                final Fragment fragment2 = this.mActive.get(size2);
                if (fragment2 != null && s.equals(fragment2.mTag)) {
                    return fragment2;
                }
            }
        }
        return null;
    }
    
    public Fragment findFragmentByWho(final String s) {
        if (this.mActive != null && s != null) {
            int size = this.mActive.size();
            while (true) {
                final int n = size - 1;
                if (n < 0) {
                    break;
                }
                final Fragment fragment = this.mActive.get(n);
                size = n;
                if (fragment == null) {
                    continue;
                }
                final Fragment fragmentByWho = fragment.findFragmentByWho(s);
                size = n;
                if (fragmentByWho != null) {
                    return fragmentByWho;
                }
            }
        }
        return null;
    }
    
    public void freeBackStackIndex(final int n) {
        synchronized (this) {
            this.mBackStackIndices.set(n, null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList<Integer>();
            }
            if (FragmentManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Freeing back stack index ");
                sb.append(n);
                Log.v("FragmentManager", sb.toString());
            }
            this.mAvailBackStackIndices.add(n);
        }
    }
    
    @Override
    public BackStackEntry getBackStackEntryAt(final int n) {
        return this.mBackStack.get(n);
    }
    
    @Override
    public int getBackStackEntryCount() {
        if (this.mBackStack != null) {
            return this.mBackStack.size();
        }
        return 0;
    }
    
    @Override
    public Fragment getFragment(final Bundle bundle, final String s) {
        final int int1 = bundle.getInt(s, -1);
        if (int1 == -1) {
            return null;
        }
        if (int1 >= this.mActive.size()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment no longer exists for key ");
            sb.append(s);
            sb.append(": index ");
            sb.append(int1);
            this.throwException(new IllegalStateException(sb.toString()));
        }
        final Fragment fragment = this.mActive.get(int1);
        if (fragment == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Fragment no longer exists for key ");
            sb2.append(s);
            sb2.append(": index ");
            sb2.append(int1);
            this.throwException(new IllegalStateException(sb2.toString()));
            return fragment;
        }
        return fragment;
    }
    
    @Override
    public List<Fragment> getFragments() {
        return this.mActive;
    }
    
    LayoutInflaterFactory getLayoutInflaterFactory() {
        return this;
    }
    
    public void hideFragment(final Fragment fragment, final int n, final int n2) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("hide: ");
            sb.append(fragment);
            Log.v("FragmentManager", sb.toString());
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            if (fragment.mView != null) {
                final Animation loadAnimation = this.loadAnimation(fragment, n, false, n2);
                if (loadAnimation != null) {
                    this.setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                    fragment.mView.startAnimation(loadAnimation);
                }
                fragment.mView.setVisibility(8);
            }
            if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.onHiddenChanged(true);
        }
    }
    
    @Override
    public boolean isDestroyed() {
        return this.mDestroyed;
    }
    
    Animation loadAnimation(final Fragment fragment, int n, final boolean b, final int n2) {
        final Animation onCreateAnimation = fragment.onCreateAnimation(n, b, fragment.mNextAnim);
        if (onCreateAnimation != null) {
            return onCreateAnimation;
        }
        if (fragment.mNextAnim != 0) {
            final Animation loadAnimation = AnimationUtils.loadAnimation(this.mHost.getContext(), fragment.mNextAnim);
            if (loadAnimation != null) {
                return loadAnimation;
            }
        }
        if (n == 0) {
            return null;
        }
        n = transitToStyleIndex(n, b);
        if (n < 0) {
            return null;
        }
        switch (n) {
            default: {
                n = n2;
                if (n2 == 0) {
                    n = n2;
                    if (this.mHost.onHasWindowAnimations()) {
                        n = this.mHost.onGetWindowAnimations();
                    }
                }
                if (n == 0) {}
                return null;
            }
            case 6: {
                return makeFadeAnimation(this.mHost.getContext(), 1.0f, 0.0f);
            }
            case 5: {
                return makeFadeAnimation(this.mHost.getContext(), 0.0f, 1.0f);
            }
            case 4: {
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 1.075f, 1.0f, 0.0f);
            }
            case 3: {
                return makeOpenCloseAnimation(this.mHost.getContext(), 0.975f, 1.0f, 0.0f, 1.0f);
            }
            case 2: {
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 0.975f, 1.0f, 0.0f);
            }
            case 1: {
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.125f, 1.0f, 0.0f, 1.0f);
            }
        }
    }
    
    void makeActive(final Fragment fragment) {
        if (fragment.mIndex >= 0) {
            return;
        }
        if (this.mAvailIndices != null && this.mAvailIndices.size() > 0) {
            fragment.setIndex(this.mAvailIndices.remove(this.mAvailIndices.size() - 1), this.mParent);
            this.mActive.set(fragment.mIndex, fragment);
        }
        else {
            if (this.mActive == null) {
                this.mActive = new ArrayList<Fragment>();
            }
            fragment.setIndex(this.mActive.size(), this.mParent);
            this.mActive.add(fragment);
        }
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Allocated fragment index ");
            sb.append(fragment);
            Log.v("FragmentManager", sb.toString());
        }
    }
    
    void makeInactive(final Fragment fragment) {
        if (fragment.mIndex < 0) {
            return;
        }
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Freeing fragment index ");
            sb.append(fragment);
            Log.v("FragmentManager", sb.toString());
        }
        this.mActive.set(fragment.mIndex, null);
        if (this.mAvailIndices == null) {
            this.mAvailIndices = new ArrayList<Integer>();
        }
        this.mAvailIndices.add(fragment.mIndex);
        this.mHost.inactivateFragment(fragment.mWho);
        fragment.initState();
    }
    
    void moveToState(final int mCurState, final int n, final int n2, final boolean b) {
        if (this.mHost == null && mCurState != 0) {
            throw new IllegalStateException("No host");
        }
        if (!b && this.mCurState == mCurState) {
            return;
        }
        this.mCurState = mCurState;
        if (this.mActive != null) {
            int i = 0;
            boolean b2 = false;
            while (i < this.mActive.size()) {
                final Fragment fragment = this.mActive.get(i);
                boolean b3 = b2;
                if (fragment != null) {
                    this.moveToState(fragment, mCurState, n, n2, false);
                    b3 = b2;
                    if (fragment.mLoaderManager != null) {
                        b3 = (b2 | fragment.mLoaderManager.hasRunningLoaders());
                    }
                }
                ++i;
                b2 = b3;
            }
            if (!b2) {
                this.startPendingDeferredFragments();
            }
            if (this.mNeedMenuInvalidate && this.mHost != null && this.mCurState == 5) {
                this.mHost.onSupportInvalidateOptionsMenu();
                this.mNeedMenuInvalidate = false;
            }
        }
    }
    
    void moveToState(final int n, final boolean b) {
        this.moveToState(n, 0, 0, b);
    }
    
    void moveToState(final Fragment fragment) {
        this.moveToState(fragment, this.mCurState, 0, 0, false);
    }
    
    void moveToState(final Fragment fragment, int mState, final int n, final int n2, final boolean b) {
        final boolean mAdded = fragment.mAdded;
        final int n3 = 1;
        if (!mAdded || fragment.mDetached) {
            if ((mState = mState) > 1) {
                mState = 1;
            }
        }
        int mState2 = mState;
        if (fragment.mRemoving && (mState2 = mState) > fragment.mState) {
            mState2 = fragment.mState;
        }
        if (fragment.mDeferStart && fragment.mState < 4 && mState2 > 3) {
            mState = 3;
        }
        else {
            mState = mState2;
        }
        if (fragment.mState < mState) {
            if (fragment.mFromLayout && !fragment.mInLayout) {
                return;
            }
            if (fragment.mAnimatingAway != null) {
                fragment.mAnimatingAway = null;
                this.moveToState(fragment, fragment.mStateAfterAnimating, 0, 0, true);
            }
            int n4 = mState;
            int n5 = mState;
            int n6 = mState;
            Label_0631: {
                switch (fragment.mState) {
                    case 1: {
                        if ((n6 = n5) > 1) {
                            if (FragmentManagerImpl.DEBUG) {
                                final StringBuilder sb4 = new StringBuilder();
                                sb4.append("moveto ACTIVITY_CREATED: ");
                                sb4.append(fragment);
                                Log.v("FragmentManager", sb4.toString());
                            }
                            if (!fragment.mFromLayout) {
                                ViewGroup mContainer;
                                if (fragment.mContainerId != 0) {
                                    final ViewGroup viewGroup = (ViewGroup)this.mContainer.onFindViewById(fragment.mContainerId);
                                    if ((mContainer = viewGroup) == null) {
                                        mContainer = viewGroup;
                                        if (!fragment.mRestored) {
                                            final StringBuilder sb5 = new StringBuilder();
                                            sb5.append("No view found for id 0x");
                                            sb5.append(Integer.toHexString(fragment.mContainerId));
                                            sb5.append(" (");
                                            sb5.append(fragment.getResources().getResourceName(fragment.mContainerId));
                                            sb5.append(") for fragment ");
                                            sb5.append(fragment);
                                            this.throwException(new IllegalArgumentException(sb5.toString()));
                                            mContainer = viewGroup;
                                        }
                                    }
                                }
                                else {
                                    mContainer = null;
                                }
                                fragment.mContainer = mContainer;
                                fragment.mView = fragment.performCreateView(fragment.getLayoutInflater(fragment.mSavedFragmentState), mContainer, fragment.mSavedFragmentState);
                                if (fragment.mView != null) {
                                    fragment.mInnerView = fragment.mView;
                                    if (Build$VERSION.SDK_INT >= 11) {
                                        ViewCompat.setSaveFromParentEnabled(fragment.mView, false);
                                    }
                                    else {
                                        fragment.mView = (View)NoSaveStateFrameLayout.wrap(fragment.mView);
                                    }
                                    if (mContainer != null) {
                                        final Animation loadAnimation = this.loadAnimation(fragment, n, true, n2);
                                        if (loadAnimation != null) {
                                            this.setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                                            fragment.mView.startAnimation(loadAnimation);
                                        }
                                        mContainer.addView(fragment.mView);
                                    }
                                    if (fragment.mHidden) {
                                        fragment.mView.setVisibility(8);
                                    }
                                    fragment.onViewCreated(fragment.mView, fragment.mSavedFragmentState);
                                }
                                else {
                                    fragment.mInnerView = null;
                                }
                            }
                            fragment.performActivityCreated(fragment.mSavedFragmentState);
                            if (fragment.mView != null) {
                                fragment.restoreViewState(fragment.mSavedFragmentState);
                            }
                            fragment.mSavedFragmentState = null;
                            n6 = n5;
                        }
                    }
                    case 2:
                    case 3: {
                        if ((n4 = n6) > 3) {
                            if (FragmentManagerImpl.DEBUG) {
                                final StringBuilder sb6 = new StringBuilder();
                                sb6.append("moveto STARTED: ");
                                sb6.append(fragment);
                                Log.v("FragmentManager", sb6.toString());
                            }
                            fragment.performStart();
                            n4 = n6;
                        }
                    }
                    case 4: {
                        if ((mState = n4) > 4) {
                            if (FragmentManagerImpl.DEBUG) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("moveto RESUMED: ");
                                sb.append(fragment);
                                Log.v("FragmentManager", sb.toString());
                            }
                            fragment.performResume();
                            fragment.mSavedFragmentState = null;
                            fragment.mSavedViewState = null;
                            mState = n4;
                            break;
                        }
                        break;
                    }
                    case 0: {
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("moveto CREATED: ");
                            sb2.append(fragment);
                            Log.v("FragmentManager", sb2.toString());
                        }
                        int n7 = mState;
                        if (fragment.mSavedFragmentState != null) {
                            fragment.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                            fragment.mSavedViewState = (SparseArray<Parcelable>)fragment.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                            fragment.mTarget = this.getFragment(fragment.mSavedFragmentState, "android:target_state");
                            if (fragment.mTarget != null) {
                                fragment.mTargetRequestCode = fragment.mSavedFragmentState.getInt("android:target_req_state", 0);
                            }
                            fragment.mUserVisibleHint = fragment.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
                            n7 = mState;
                            if (!fragment.mUserVisibleHint) {
                                fragment.mDeferStart = true;
                                if ((n7 = mState) > 3) {
                                    n7 = 3;
                                }
                            }
                        }
                        fragment.mHost = this.mHost;
                        fragment.mParentFragment = this.mParent;
                        FragmentManagerImpl mFragmentManager;
                        if (this.mParent != null) {
                            mFragmentManager = this.mParent.mChildFragmentManager;
                        }
                        else {
                            mFragmentManager = this.mHost.getFragmentManagerImpl();
                        }
                        fragment.mFragmentManager = mFragmentManager;
                        fragment.mCalled = false;
                        fragment.onAttach(this.mHost.getContext());
                        if (!fragment.mCalled) {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("Fragment ");
                            sb3.append(fragment);
                            sb3.append(" did not call through to super.onAttach()");
                            throw new SuperNotCalledException(sb3.toString());
                        }
                        if (fragment.mParentFragment == null) {
                            this.mHost.onAttachFragment(fragment);
                        }
                        if (!fragment.mRetaining) {
                            fragment.performCreate(fragment.mSavedFragmentState);
                        }
                        fragment.mRetaining = false;
                        n5 = n7;
                        if (!fragment.mFromLayout) {
                            break Label_0631;
                        }
                        fragment.mView = fragment.performCreateView(fragment.getLayoutInflater(fragment.mSavedFragmentState), null, fragment.mSavedFragmentState);
                        if (fragment.mView != null) {
                            fragment.mInnerView = fragment.mView;
                            if (Build$VERSION.SDK_INT >= 11) {
                                ViewCompat.setSaveFromParentEnabled(fragment.mView, false);
                            }
                            else {
                                fragment.mView = (View)NoSaveStateFrameLayout.wrap(fragment.mView);
                            }
                            if (fragment.mHidden) {
                                fragment.mView.setVisibility(8);
                            }
                            fragment.onViewCreated(fragment.mView, fragment.mSavedFragmentState);
                            n5 = n7;
                            break Label_0631;
                        }
                        fragment.mInnerView = null;
                        n5 = n7;
                        break Label_0631;
                    }
                }
            }
        }
        else if (fragment.mState > mState) {
            switch (fragment.mState) {
                case 5: {
                    if (mState < 5) {
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb7 = new StringBuilder();
                            sb7.append("movefrom RESUMED: ");
                            sb7.append(fragment);
                            Log.v("FragmentManager", sb7.toString());
                        }
                        fragment.performPause();
                    }
                }
                case 4: {
                    if (mState < 4) {
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb8 = new StringBuilder();
                            sb8.append("movefrom STARTED: ");
                            sb8.append(fragment);
                            Log.v("FragmentManager", sb8.toString());
                        }
                        fragment.performStop();
                    }
                }
                case 3: {
                    if (mState < 3) {
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb9 = new StringBuilder();
                            sb9.append("movefrom STOPPED: ");
                            sb9.append(fragment);
                            Log.v("FragmentManager", sb9.toString());
                        }
                        fragment.performReallyStop();
                    }
                }
                case 2: {
                    if (mState < 2) {
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb10 = new StringBuilder();
                            sb10.append("movefrom ACTIVITY_CREATED: ");
                            sb10.append(fragment);
                            Log.v("FragmentManager", sb10.toString());
                        }
                        if (fragment.mView != null && this.mHost.onShouldSaveFragmentState(fragment) && fragment.mSavedViewState == null) {
                            this.saveFragmentViewState(fragment);
                        }
                        fragment.performDestroyView();
                        if (fragment.mView != null && fragment.mContainer != null) {
                            Animation loadAnimation2;
                            if (this.mCurState > 0 && !this.mDestroyed) {
                                loadAnimation2 = this.loadAnimation(fragment, n, false, n2);
                            }
                            else {
                                loadAnimation2 = null;
                            }
                            if (loadAnimation2 != null) {
                                fragment.mAnimatingAway = fragment.mView;
                                fragment.mStateAfterAnimating = mState;
                                loadAnimation2.setAnimationListener((Animation$AnimationListener)new AnimateOnHWLayerIfNeededListener(fragment.mView, loadAnimation2) {
                                    @Override
                                    public void onAnimationEnd(final Animation animation) {
                                        super.onAnimationEnd(animation);
                                        if (fragment.mAnimatingAway != null) {
                                            fragment.mAnimatingAway = null;
                                            FragmentManagerImpl.this.moveToState(fragment, fragment.mStateAfterAnimating, 0, 0, false);
                                        }
                                    }
                                });
                                fragment.mView.startAnimation(loadAnimation2);
                            }
                            fragment.mContainer.removeView(fragment.mView);
                        }
                        fragment.mContainer = null;
                        fragment.mView = null;
                        fragment.mInnerView = null;
                    }
                }
                case 1: {
                    if (mState >= 1) {
                        break;
                    }
                    if (this.mDestroyed && fragment.mAnimatingAway != null) {
                        final View mAnimatingAway = fragment.mAnimatingAway;
                        fragment.mAnimatingAway = null;
                        mAnimatingAway.clearAnimation();
                    }
                    if (fragment.mAnimatingAway != null) {
                        fragment.mStateAfterAnimating = mState;
                        mState = n3;
                        break;
                    }
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb11 = new StringBuilder();
                        sb11.append("movefrom CREATED: ");
                        sb11.append(fragment);
                        Log.v("FragmentManager", sb11.toString());
                    }
                    if (!fragment.mRetaining) {
                        fragment.performDestroy();
                    }
                    else {
                        fragment.mState = 0;
                    }
                    fragment.mCalled = false;
                    fragment.onDetach();
                    if (!fragment.mCalled) {
                        final StringBuilder sb12 = new StringBuilder();
                        sb12.append("Fragment ");
                        sb12.append(fragment);
                        sb12.append(" did not call through to super.onDetach()");
                        throw new SuperNotCalledException(sb12.toString());
                    }
                    if (b) {
                        break;
                    }
                    if (!fragment.mRetaining) {
                        this.makeInactive(fragment);
                        break;
                    }
                    fragment.mHost = null;
                    fragment.mParentFragment = null;
                    fragment.mFragmentManager = null;
                    fragment.mChildFragmentManager = null;
                    break;
                }
            }
        }
        if (fragment.mState != mState) {
            final StringBuilder sb13 = new StringBuilder();
            sb13.append("moveToState: Fragment state for ");
            sb13.append(fragment);
            sb13.append(" not updated inline; ");
            sb13.append("expected state ");
            sb13.append(mState);
            sb13.append(" found ");
            sb13.append(fragment.mState);
            Log.w("FragmentManager", sb13.toString());
            fragment.mState = mState;
        }
    }
    
    public void noteStateNotSaved() {
        this.mStateSaved = false;
    }
    
    @Override
    public View onCreateView(final View view, final String s, final Context context, final AttributeSet set) {
        final boolean equals = "fragment".equals(s);
        Fragment fragmentById = null;
        if (!equals) {
            return null;
        }
        final String attributeValue = set.getAttributeValue((String)null, "class");
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, FragmentTag.Fragment);
        int id = 0;
        String string;
        if ((string = attributeValue) == null) {
            string = obtainStyledAttributes.getString(0);
        }
        final int resourceId = obtainStyledAttributes.getResourceId(1, -1);
        final String string2 = obtainStyledAttributes.getString(2);
        obtainStyledAttributes.recycle();
        if (!Fragment.isSupportFragmentClass(this.mHost.getContext(), string)) {
            return null;
        }
        if (view != null) {
            id = view.getId();
        }
        if (id == -1 && resourceId == -1 && string2 == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(set.getPositionDescription());
            sb.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
            sb.append(string);
            throw new IllegalArgumentException(sb.toString());
        }
        if (resourceId != -1) {
            fragmentById = this.findFragmentById(resourceId);
        }
        Fragment fragmentByTag;
        if ((fragmentByTag = fragmentById) == null) {
            fragmentByTag = fragmentById;
            if (string2 != null) {
                fragmentByTag = this.findFragmentByTag(string2);
            }
        }
        Fragment fragmentById2;
        if ((fragmentById2 = fragmentByTag) == null) {
            fragmentById2 = fragmentByTag;
            if (id != -1) {
                fragmentById2 = this.findFragmentById(id);
            }
        }
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("onCreateView: id=0x");
            sb2.append(Integer.toHexString(resourceId));
            sb2.append(" fname=");
            sb2.append(string);
            sb2.append(" existing=");
            sb2.append(fragmentById2);
            Log.v("FragmentManager", sb2.toString());
        }
        Fragment instantiate;
        if (fragmentById2 == null) {
            instantiate = Fragment.instantiate(context, string);
            instantiate.mFromLayout = true;
            int mFragmentId;
            if (resourceId != 0) {
                mFragmentId = resourceId;
            }
            else {
                mFragmentId = id;
            }
            instantiate.mFragmentId = mFragmentId;
            instantiate.mContainerId = id;
            instantiate.mTag = string2;
            instantiate.mInLayout = true;
            instantiate.mFragmentManager = this;
            instantiate.mHost = this.mHost;
            instantiate.onInflate(this.mHost.getContext(), set, instantiate.mSavedFragmentState);
            this.addFragment(instantiate, true);
        }
        else {
            if (fragmentById2.mInLayout) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(set.getPositionDescription());
                sb3.append(": Duplicate id 0x");
                sb3.append(Integer.toHexString(resourceId));
                sb3.append(", tag ");
                sb3.append(string2);
                sb3.append(", or parent id 0x");
                sb3.append(Integer.toHexString(id));
                sb3.append(" with another fragment for ");
                sb3.append(string);
                throw new IllegalArgumentException(sb3.toString());
            }
            fragmentById2.mInLayout = true;
            fragmentById2.mHost = this.mHost;
            if (!fragmentById2.mRetaining) {
                fragmentById2.onInflate(this.mHost.getContext(), set, fragmentById2.mSavedFragmentState);
            }
            instantiate = fragmentById2;
        }
        if (this.mCurState < 1 && instantiate.mFromLayout) {
            this.moveToState(instantiate, 1, 0, 0, false);
        }
        else {
            this.moveToState(instantiate);
        }
        if (instantiate.mView == null) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Fragment ");
            sb4.append(string);
            sb4.append(" did not create a view.");
            throw new IllegalStateException(sb4.toString());
        }
        if (resourceId != 0) {
            instantiate.mView.setId(resourceId);
        }
        if (instantiate.mView.getTag() == null) {
            instantiate.mView.setTag((Object)string2);
        }
        return instantiate.mView;
    }
    
    public void performPendingDeferredStart(final Fragment fragment) {
        if (fragment.mDeferStart) {
            if (this.mExecutingActions) {
                this.mHavePendingDeferredStart = true;
                return;
            }
            fragment.mDeferStart = false;
            this.moveToState(fragment, this.mCurState, 0, 0, false);
        }
    }
    
    @Override
    public void popBackStack() {
        this.enqueueAction(new Runnable() {
            @Override
            public void run() {
                FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mHost.getHandler(), null, -1, 0);
            }
        }, false);
    }
    
    @Override
    public void popBackStack(final int n, final int n2) {
        if (n < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad id: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        this.enqueueAction(new Runnable() {
            @Override
            public void run() {
                FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mHost.getHandler(), null, n, n2);
            }
        }, false);
    }
    
    @Override
    public void popBackStack(final String s, final int n) {
        this.enqueueAction(new Runnable() {
            @Override
            public void run() {
                FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mHost.getHandler(), s, -1, n);
            }
        }, false);
    }
    
    @Override
    public boolean popBackStackImmediate() {
        this.checkStateLoss();
        this.executePendingTransactions();
        return this.popBackStackState(this.mHost.getHandler(), null, -1, 0);
    }
    
    @Override
    public boolean popBackStackImmediate(final int n, final int n2) {
        this.checkStateLoss();
        this.executePendingTransactions();
        if (n < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad id: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        return this.popBackStackState(this.mHost.getHandler(), null, n, n2);
    }
    
    @Override
    public boolean popBackStackImmediate(final String s, final int n) {
        this.checkStateLoss();
        this.executePendingTransactions();
        return this.popBackStackState(this.mHost.getHandler(), s, -1, n);
    }
    
    boolean popBackStackState(final Handler handler, final String s, int i, int n) {
        if (this.mBackStack == null) {
            return false;
        }
        if (s == null && i < 0 && (n & 0x1) == 0x0) {
            i = this.mBackStack.size() - 1;
            if (i < 0) {
                return false;
            }
            final BackStackRecord backStackRecord = this.mBackStack.remove(i);
            final SparseArray sparseArray = new SparseArray();
            final SparseArray sparseArray2 = new SparseArray();
            backStackRecord.calculateBackFragments((SparseArray<Fragment>)sparseArray, (SparseArray<Fragment>)sparseArray2);
            backStackRecord.popFromBackStack(true, null, (SparseArray<Fragment>)sparseArray, (SparseArray<Fragment>)sparseArray2);
        }
        else {
            int n2 = -1;
            if (s != null || i >= 0) {
                int j;
                for (j = this.mBackStack.size() - 1; j >= 0; --j) {
                    final BackStackRecord backStackRecord2 = this.mBackStack.get(j);
                    if (s != null && s.equals(backStackRecord2.getName())) {
                        break;
                    }
                    if (i >= 0 && i == backStackRecord2.mIndex) {
                        break;
                    }
                }
                if (j < 0) {
                    return false;
                }
                n2 = j;
                if ((n & 0x1) != 0x0) {
                    while (true) {
                        n = j - 1;
                        if ((n2 = n) < 0) {
                            break;
                        }
                        final BackStackRecord backStackRecord3 = this.mBackStack.get(n);
                        if (s != null) {
                            j = n;
                            if (s.equals(backStackRecord3.getName())) {
                                continue;
                            }
                        }
                        n2 = n;
                        if (i < 0) {
                            break;
                        }
                        n2 = n;
                        if (i != backStackRecord3.mIndex) {
                            break;
                        }
                        j = n;
                    }
                }
            }
            if (n2 == this.mBackStack.size() - 1) {
                return false;
            }
            final ArrayList<BackStackRecord> list = new ArrayList<BackStackRecord>();
            for (i = this.mBackStack.size() - 1; i > n2; --i) {
                list.add(this.mBackStack.remove(i));
            }
            n = list.size() - 1;
            final SparseArray sparseArray3 = new SparseArray();
            final SparseArray sparseArray4 = new SparseArray();
            for (i = 0; i <= n; ++i) {
                list.get(i).calculateBackFragments((SparseArray<Fragment>)sparseArray3, (SparseArray<Fragment>)sparseArray4);
            }
            BackStackRecord.TransitionState popFromBackStack = null;
            StringBuilder sb;
            BackStackRecord backStackRecord4;
            boolean b;
            for (i = 0; i <= n; ++i) {
                if (FragmentManagerImpl.DEBUG) {
                    sb = new StringBuilder();
                    sb.append("Popping back stack state: ");
                    sb.append(list.get(i));
                    Log.v("FragmentManager", sb.toString());
                }
                backStackRecord4 = list.get(i);
                if (i == n) {
                    b = true;
                }
                else {
                    b = false;
                }
                popFromBackStack = backStackRecord4.popFromBackStack(b, popFromBackStack, (SparseArray<Fragment>)sparseArray3, (SparseArray<Fragment>)sparseArray4);
            }
        }
        this.reportBackStackChanged();
        return true;
    }
    
    @Override
    public void putFragment(final Bundle bundle, final String s, final Fragment fragment) {
        if (fragment.mIndex < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(fragment);
            sb.append(" is not currently in the FragmentManager");
            this.throwException(new IllegalStateException(sb.toString()));
        }
        bundle.putInt(s, fragment.mIndex);
    }
    
    public void removeFragment(final Fragment fragment, final int n, final int n2) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("remove: ");
            sb.append(fragment);
            sb.append(" nesting=");
            sb.append(fragment.mBackStackNesting);
            Log.v("FragmentManager", sb.toString());
        }
        final boolean b = fragment.isInBackStack() ^ true;
        if (!fragment.mDetached || b) {
            if (this.mAdded != null) {
                this.mAdded.remove(fragment);
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
            fragment.mRemoving = true;
            int n3;
            if (b) {
                n3 = 0;
            }
            else {
                n3 = 1;
            }
            this.moveToState(fragment, n3, n, n2, false);
        }
    }
    
    @Override
    public void removeOnBackStackChangedListener(final OnBackStackChangedListener onBackStackChangedListener) {
        if (this.mBackStackChangeListeners != null) {
            this.mBackStackChangeListeners.remove(onBackStackChangedListener);
        }
    }
    
    void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); ++i) {
                this.mBackStackChangeListeners.get(i).onBackStackChanged();
            }
        }
    }
    
    void restoreAllState(final Parcelable parcelable, final List<Fragment> list) {
        if (parcelable == null) {
            return;
        }
        final FragmentManagerState fragmentManagerState = (FragmentManagerState)parcelable;
        if (fragmentManagerState.mActive != null) {
            if (list != null) {
                for (int i = 0; i < list.size(); ++i) {
                    final Fragment mInstance = list.get(i);
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("restoreAllState: re-attaching retained ");
                        sb.append(mInstance);
                        Log.v("FragmentManager", sb.toString());
                    }
                    final FragmentState fragmentState = fragmentManagerState.mActive[mInstance.mIndex];
                    fragmentState.mInstance = mInstance;
                    mInstance.mSavedViewState = null;
                    mInstance.mBackStackNesting = 0;
                    mInstance.mInLayout = false;
                    mInstance.mAdded = false;
                    mInstance.mTarget = null;
                    if (fragmentState.mSavedFragmentState != null) {
                        fragmentState.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                        mInstance.mSavedViewState = (SparseArray<Parcelable>)fragmentState.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                        mInstance.mSavedFragmentState = fragmentState.mSavedFragmentState;
                    }
                }
            }
            this.mActive = new ArrayList<Fragment>(fragmentManagerState.mActive.length);
            if (this.mAvailIndices != null) {
                this.mAvailIndices.clear();
            }
            for (int j = 0; j < fragmentManagerState.mActive.length; ++j) {
                final FragmentState fragmentState2 = fragmentManagerState.mActive[j];
                if (fragmentState2 != null) {
                    final Fragment instantiate = fragmentState2.instantiate(this.mHost, this.mParent);
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("restoreAllState: active #");
                        sb2.append(j);
                        sb2.append(": ");
                        sb2.append(instantiate);
                        Log.v("FragmentManager", sb2.toString());
                    }
                    this.mActive.add(instantiate);
                    fragmentState2.mInstance = null;
                }
                else {
                    this.mActive.add(null);
                    if (this.mAvailIndices == null) {
                        this.mAvailIndices = new ArrayList<Integer>();
                    }
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("restoreAllState: avail #");
                        sb3.append(j);
                        Log.v("FragmentManager", sb3.toString());
                    }
                    this.mAvailIndices.add(j);
                }
            }
            if (list != null) {
                for (int k = 0; k < list.size(); ++k) {
                    final Fragment fragment = list.get(k);
                    if (fragment.mTargetIndex >= 0) {
                        if (fragment.mTargetIndex < this.mActive.size()) {
                            fragment.mTarget = this.mActive.get(fragment.mTargetIndex);
                        }
                        else {
                            final StringBuilder sb4 = new StringBuilder();
                            sb4.append("Re-attaching retained fragment ");
                            sb4.append(fragment);
                            sb4.append(" target no longer exists: ");
                            sb4.append(fragment.mTargetIndex);
                            Log.w("FragmentManager", sb4.toString());
                            fragment.mTarget = null;
                        }
                    }
                }
            }
            if (fragmentManagerState.mAdded != null) {
                this.mAdded = new ArrayList<Fragment>(fragmentManagerState.mAdded.length);
                for (int l = 0; l < fragmentManagerState.mAdded.length; ++l) {
                    final Fragment fragment2 = this.mActive.get(fragmentManagerState.mAdded[l]);
                    if (fragment2 == null) {
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append("No instantiated fragment for index #");
                        sb5.append(fragmentManagerState.mAdded[l]);
                        this.throwException(new IllegalStateException(sb5.toString()));
                    }
                    fragment2.mAdded = true;
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append("restoreAllState: added #");
                        sb6.append(l);
                        sb6.append(": ");
                        sb6.append(fragment2);
                        Log.v("FragmentManager", sb6.toString());
                    }
                    if (this.mAdded.contains(fragment2)) {
                        throw new IllegalStateException("Already added!");
                    }
                    this.mAdded.add(fragment2);
                }
            }
            else {
                this.mAdded = null;
            }
            if (fragmentManagerState.mBackStack != null) {
                this.mBackStack = new ArrayList<BackStackRecord>(fragmentManagerState.mBackStack.length);
                for (int n = 0; n < fragmentManagerState.mBackStack.length; ++n) {
                    final BackStackRecord instantiate2 = fragmentManagerState.mBackStack[n].instantiate(this);
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append("restoreAllState: back stack #");
                        sb7.append(n);
                        sb7.append(" (index ");
                        sb7.append(instantiate2.mIndex);
                        sb7.append("): ");
                        sb7.append(instantiate2);
                        Log.v("FragmentManager", sb7.toString());
                        instantiate2.dump("  ", new PrintWriter(new LogWriter("FragmentManager")), false);
                    }
                    this.mBackStack.add(instantiate2);
                    if (instantiate2.mIndex >= 0) {
                        this.setBackStackIndex(instantiate2.mIndex, instantiate2);
                    }
                }
            }
            else {
                this.mBackStack = null;
            }
        }
    }
    
    ArrayList<Fragment> retainNonConfig() {
        final ArrayList<Fragment> mActive = this.mActive;
        ArrayList<Fragment> list = null;
        ArrayList<Fragment> list2 = null;
        if (mActive != null) {
            int n = 0;
            while (true) {
                list = list2;
                if (n >= this.mActive.size()) {
                    break;
                }
                final Fragment fragment = this.mActive.get(n);
                ArrayList<Fragment> list3 = list2;
                if (fragment != null) {
                    list3 = list2;
                    if (fragment.mRetainInstance) {
                        ArrayList<Fragment> list4;
                        if ((list4 = list2) == null) {
                            list4 = new ArrayList<Fragment>();
                        }
                        list4.add(fragment);
                        fragment.mRetaining = true;
                        int mIndex;
                        if (fragment.mTarget != null) {
                            mIndex = fragment.mTarget.mIndex;
                        }
                        else {
                            mIndex = -1;
                        }
                        fragment.mTargetIndex = mIndex;
                        list3 = list4;
                        if (FragmentManagerImpl.DEBUG) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("retainNonConfig: keeping retained ");
                            sb.append(fragment);
                            Log.v("FragmentManager", sb.toString());
                            list3 = list4;
                        }
                    }
                }
                ++n;
                list2 = list3;
            }
        }
        return list;
    }
    
    Parcelable saveAllState() {
        this.execPendingActions();
        if (FragmentManagerImpl.HONEYCOMB) {
            this.mStateSaved = true;
        }
        final ArrayList<Fragment> mActive = this.mActive;
        final BackStackState[] array = null;
        if (mActive != null) {
            if (this.mActive.size() <= 0) {
                return null;
            }
            final int size = this.mActive.size();
            final FragmentState[] mActive2 = new FragmentState[size];
            final int n = 0;
            int i = 0;
            boolean b = false;
            while (i < size) {
                final Fragment fragment = this.mActive.get(i);
                if (fragment != null) {
                    if (fragment.mIndex < 0) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Failure saving state: active ");
                        sb.append(fragment);
                        sb.append(" has cleared index: ");
                        sb.append(fragment.mIndex);
                        this.throwException(new IllegalStateException(sb.toString()));
                    }
                    final FragmentState fragmentState = new FragmentState(fragment);
                    mActive2[i] = fragmentState;
                    if (fragment.mState > 0 && fragmentState.mSavedFragmentState == null) {
                        fragmentState.mSavedFragmentState = this.saveFragmentBasicState(fragment);
                        if (fragment.mTarget != null) {
                            if (fragment.mTarget.mIndex < 0) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("Failure saving state: ");
                                sb2.append(fragment);
                                sb2.append(" has target not in fragment manager: ");
                                sb2.append(fragment.mTarget);
                                this.throwException(new IllegalStateException(sb2.toString()));
                            }
                            if (fragmentState.mSavedFragmentState == null) {
                                fragmentState.mSavedFragmentState = new Bundle();
                            }
                            this.putFragment(fragmentState.mSavedFragmentState, "android:target_state", fragment.mTarget);
                            if (fragment.mTargetRequestCode != 0) {
                                fragmentState.mSavedFragmentState.putInt("android:target_req_state", fragment.mTargetRequestCode);
                            }
                        }
                    }
                    else {
                        fragmentState.mSavedFragmentState = fragment.mSavedFragmentState;
                    }
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Saved state of ");
                        sb3.append(fragment);
                        sb3.append(": ");
                        sb3.append(fragmentState.mSavedFragmentState);
                        Log.v("FragmentManager", sb3.toString());
                    }
                    b = true;
                }
                ++i;
            }
            if (b) {
                int[] mAdded = null;
                Label_0650: {
                    if (this.mAdded != null) {
                        final int size2 = this.mAdded.size();
                        if (size2 > 0) {
                            final int[] array2 = new int[size2];
                            int n2 = 0;
                            while (true) {
                                mAdded = array2;
                                if (n2 >= size2) {
                                    break Label_0650;
                                }
                                array2[n2] = this.mAdded.get(n2).mIndex;
                                if (array2[n2] < 0) {
                                    final StringBuilder sb4 = new StringBuilder();
                                    sb4.append("Failure saving state: active ");
                                    sb4.append(this.mAdded.get(n2));
                                    sb4.append(" has cleared index: ");
                                    sb4.append(array2[n2]);
                                    this.throwException(new IllegalStateException(sb4.toString()));
                                }
                                if (FragmentManagerImpl.DEBUG) {
                                    final StringBuilder sb5 = new StringBuilder();
                                    sb5.append("saveAllState: adding fragment #");
                                    sb5.append(n2);
                                    sb5.append(": ");
                                    sb5.append(this.mAdded.get(n2));
                                    Log.v("FragmentManager", sb5.toString());
                                }
                                ++n2;
                            }
                        }
                    }
                    mAdded = null;
                }
                BackStackState[] mBackStack = array;
                if (this.mBackStack != null) {
                    final int size3 = this.mBackStack.size();
                    mBackStack = array;
                    if (size3 > 0) {
                        final BackStackState[] array3 = new BackStackState[size3];
                        int n3 = n;
                        while (true) {
                            mBackStack = array3;
                            if (n3 >= size3) {
                                break;
                            }
                            array3[n3] = new BackStackState(this.mBackStack.get(n3));
                            if (FragmentManagerImpl.DEBUG) {
                                final StringBuilder sb6 = new StringBuilder();
                                sb6.append("saveAllState: adding back stack #");
                                sb6.append(n3);
                                sb6.append(": ");
                                sb6.append(this.mBackStack.get(n3));
                                Log.v("FragmentManager", sb6.toString());
                            }
                            ++n3;
                        }
                    }
                }
                final FragmentManagerState fragmentManagerState = new FragmentManagerState();
                fragmentManagerState.mActive = mActive2;
                fragmentManagerState.mAdded = mAdded;
                fragmentManagerState.mBackStack = mBackStack;
                return (Parcelable)fragmentManagerState;
            }
            if (FragmentManagerImpl.DEBUG) {
                Log.v("FragmentManager", "saveAllState: no fragments!");
                return null;
            }
        }
        return null;
    }
    
    Bundle saveFragmentBasicState(final Fragment fragment) {
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        fragment.performSaveInstanceState(this.mStateBundle);
        Bundle mStateBundle;
        if (!this.mStateBundle.isEmpty()) {
            mStateBundle = this.mStateBundle;
            this.mStateBundle = null;
        }
        else {
            mStateBundle = null;
        }
        if (fragment.mView != null) {
            this.saveFragmentViewState(fragment);
        }
        Bundle bundle = mStateBundle;
        if (fragment.mSavedViewState != null) {
            if ((bundle = mStateBundle) == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray("android:view_state", (SparseArray)fragment.mSavedViewState);
        }
        Bundle bundle2 = bundle;
        if (!fragment.mUserVisibleHint) {
            if ((bundle2 = bundle) == null) {
                bundle2 = new Bundle();
            }
            bundle2.putBoolean("android:user_visible_hint", fragment.mUserVisibleHint);
        }
        return bundle2;
    }
    
    @Override
    public Fragment.SavedState saveFragmentInstanceState(final Fragment fragment) {
        if (fragment.mIndex < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Fragment ");
            sb.append(fragment);
            sb.append(" is not currently in the FragmentManager");
            this.throwException(new IllegalStateException(sb.toString()));
        }
        if (fragment.mState > 0) {
            final Bundle saveFragmentBasicState = this.saveFragmentBasicState(fragment);
            if (saveFragmentBasicState != null) {
                return new Fragment.SavedState(saveFragmentBasicState);
            }
        }
        return null;
    }
    
    void saveFragmentViewState(final Fragment fragment) {
        if (fragment.mInnerView == null) {
            return;
        }
        if (this.mStateArray == null) {
            this.mStateArray = (SparseArray<Parcelable>)new SparseArray();
        }
        else {
            this.mStateArray.clear();
        }
        fragment.mInnerView.saveHierarchyState((SparseArray)this.mStateArray);
        if (this.mStateArray.size() > 0) {
            fragment.mSavedViewState = this.mStateArray;
            this.mStateArray = null;
        }
    }
    
    public void setBackStackIndex(final int n, final BackStackRecord backStackRecord) {
        synchronized (this) {
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<BackStackRecord>();
            }
            int i;
            if (n < (i = this.mBackStackIndices.size())) {
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Setting back stack index ");
                    sb.append(n);
                    sb.append(" to ");
                    sb.append(backStackRecord);
                    Log.v("FragmentManager", sb.toString());
                }
                this.mBackStackIndices.set(n, backStackRecord);
            }
            else {
                while (i < n) {
                    this.mBackStackIndices.add(null);
                    if (this.mAvailBackStackIndices == null) {
                        this.mAvailBackStackIndices = new ArrayList<Integer>();
                    }
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Adding available back stack index ");
                        sb2.append(i);
                        Log.v("FragmentManager", sb2.toString());
                    }
                    this.mAvailBackStackIndices.add(i);
                    ++i;
                }
                if (FragmentManagerImpl.DEBUG) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Adding back stack index ");
                    sb3.append(n);
                    sb3.append(" with ");
                    sb3.append(backStackRecord);
                    Log.v("FragmentManager", sb3.toString());
                }
                this.mBackStackIndices.add(backStackRecord);
            }
        }
    }
    
    public void showFragment(final Fragment fragment, final int n, final int n2) {
        if (FragmentManagerImpl.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("show: ");
            sb.append(fragment);
            Log.v("FragmentManager", sb.toString());
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            if (fragment.mView != null) {
                final Animation loadAnimation = this.loadAnimation(fragment, n, true, n2);
                if (loadAnimation != null) {
                    this.setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                    fragment.mView.startAnimation(loadAnimation);
                }
                fragment.mView.setVisibility(0);
            }
            if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.onHiddenChanged(false);
        }
    }
    
    void startPendingDeferredFragments() {
        if (this.mActive == null) {
            return;
        }
        for (int i = 0; i < this.mActive.size(); ++i) {
            final Fragment fragment = this.mActive.get(i);
            if (fragment != null) {
                this.performPendingDeferredStart(fragment);
            }
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        Object o;
        if (this.mParent != null) {
            o = this.mParent;
        }
        else {
            o = this.mHost;
        }
        DebugUtils.buildShortClassTag(o, sb);
        sb.append("}}");
        return sb.toString();
    }
    
    static class AnimateOnHWLayerIfNeededListener implements Animation$AnimationListener
    {
        private Animation$AnimationListener mOrignalListener;
        private boolean mShouldRunOnHWLayer;
        private View mView;
        
        public AnimateOnHWLayerIfNeededListener(final View mView, final Animation animation) {
            this.mOrignalListener = null;
            this.mShouldRunOnHWLayer = false;
            this.mView = null;
            if (mView != null) {
                if (animation == null) {
                    return;
                }
                this.mView = mView;
            }
        }
        
        public AnimateOnHWLayerIfNeededListener(final View mView, final Animation animation, final Animation$AnimationListener mOrignalListener) {
            this.mOrignalListener = null;
            this.mShouldRunOnHWLayer = false;
            this.mView = null;
            if (mView != null) {
                if (animation == null) {
                    return;
                }
                this.mOrignalListener = mOrignalListener;
                this.mView = mView;
            }
        }
        
        @CallSuper
        public void onAnimationEnd(final Animation animation) {
            if (this.mView != null && this.mShouldRunOnHWLayer) {
                this.mView.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        ViewCompat.setLayerType(AnimateOnHWLayerIfNeededListener.this.mView, 0, null);
                    }
                });
            }
            if (this.mOrignalListener != null) {
                this.mOrignalListener.onAnimationEnd(animation);
            }
        }
        
        public void onAnimationRepeat(final Animation animation) {
            if (this.mOrignalListener != null) {
                this.mOrignalListener.onAnimationRepeat(animation);
            }
        }
        
        @CallSuper
        public void onAnimationStart(final Animation animation) {
            if (this.mView != null) {
                this.mShouldRunOnHWLayer = FragmentManagerImpl.shouldRunOnHWLayer(this.mView, animation);
                if (this.mShouldRunOnHWLayer) {
                    this.mView.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            ViewCompat.setLayerType(AnimateOnHWLayerIfNeededListener.this.mView, 2, null);
                        }
                    });
                }
            }
            if (this.mOrignalListener != null) {
                this.mOrignalListener.onAnimationStart(animation);
            }
        }
    }
    
    static class FragmentTag
    {
        public static final int[] Fragment;
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;
        
        static {
            Fragment = new int[] { 16842755, 16842960, 16842961 };
        }
    }
}
