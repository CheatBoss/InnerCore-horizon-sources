package android.support.v4.app;

import android.os.*;
import android.text.*;
import android.util.*;
import java.util.*;

final class BackStackState implements Parcelable
{
    public static final Parcelable$Creator<BackStackState> CREATOR;
    final int mBreadCrumbShortTitleRes;
    final CharSequence mBreadCrumbShortTitleText;
    final int mBreadCrumbTitleRes;
    final CharSequence mBreadCrumbTitleText;
    final int mIndex;
    final String mName;
    final int[] mOps;
    final ArrayList<String> mSharedElementSourceNames;
    final ArrayList<String> mSharedElementTargetNames;
    final int mTransition;
    final int mTransitionStyle;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<BackStackState>() {
            public BackStackState createFromParcel(final Parcel parcel) {
                return new BackStackState(parcel);
            }
            
            public BackStackState[] newArray(final int n) {
                return new BackStackState[n];
            }
        };
    }
    
    public BackStackState(final Parcel parcel) {
        this.mOps = parcel.createIntArray();
        this.mTransition = parcel.readInt();
        this.mTransitionStyle = parcel.readInt();
        this.mName = parcel.readString();
        this.mIndex = parcel.readInt();
        this.mBreadCrumbTitleRes = parcel.readInt();
        this.mBreadCrumbTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mBreadCrumbShortTitleRes = parcel.readInt();
        this.mBreadCrumbShortTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mSharedElementSourceNames = (ArrayList<String>)parcel.createStringArrayList();
        this.mSharedElementTargetNames = (ArrayList<String>)parcel.createStringArrayList();
    }
    
    public BackStackState(final BackStackRecord backStackRecord) {
        BackStackRecord.Op op = backStackRecord.mHead;
        int n = 0;
        while (op != null) {
            int n2 = n;
            if (op.removed != null) {
                n2 = n + op.removed.size();
            }
            op = op.next;
            n = n2;
        }
        this.mOps = new int[backStackRecord.mNumOp * 7 + n];
        if (!backStackRecord.mAddToBackStack) {
            throw new IllegalStateException("Not on back stack");
        }
        BackStackRecord.Op op2 = backStackRecord.mHead;
        int n3 = 0;
        while (op2 != null) {
            final int[] mOps = this.mOps;
            final int n4 = n3 + 1;
            mOps[n3] = op2.cmd;
            final int[] mOps2 = this.mOps;
            final int n5 = n4 + 1;
            int mIndex;
            if (op2.fragment != null) {
                mIndex = op2.fragment.mIndex;
            }
            else {
                mIndex = -1;
            }
            mOps2[n4] = mIndex;
            final int[] mOps3 = this.mOps;
            final int n6 = n5 + 1;
            mOps3[n5] = op2.enterAnim;
            final int[] mOps4 = this.mOps;
            final int n7 = n6 + 1;
            mOps4[n6] = op2.exitAnim;
            final int[] mOps5 = this.mOps;
            final int n8 = n7 + 1;
            mOps5[n7] = op2.popEnterAnim;
            final int[] mOps6 = this.mOps;
            final int n9 = n8 + 1;
            mOps6[n8] = op2.popExitAnim;
            int n12;
            if (op2.removed != null) {
                final int size = op2.removed.size();
                this.mOps[n9] = size;
                int n10 = n9 + 1;
                int n11 = 0;
                while (true) {
                    n12 = n10;
                    if (n11 >= size) {
                        break;
                    }
                    this.mOps[n10] = op2.removed.get(n11).mIndex;
                    ++n11;
                    ++n10;
                }
            }
            else {
                final int[] mOps7 = this.mOps;
                n12 = n9 + 1;
                mOps7[n9] = 0;
            }
            n3 = n12;
            op2 = op2.next;
        }
        this.mTransition = backStackRecord.mTransition;
        this.mTransitionStyle = backStackRecord.mTransitionStyle;
        this.mName = backStackRecord.mName;
        this.mIndex = backStackRecord.mIndex;
        this.mBreadCrumbTitleRes = backStackRecord.mBreadCrumbTitleRes;
        this.mBreadCrumbTitleText = backStackRecord.mBreadCrumbTitleText;
        this.mBreadCrumbShortTitleRes = backStackRecord.mBreadCrumbShortTitleRes;
        this.mBreadCrumbShortTitleText = backStackRecord.mBreadCrumbShortTitleText;
        this.mSharedElementSourceNames = backStackRecord.mSharedElementSourceNames;
        this.mSharedElementTargetNames = backStackRecord.mSharedElementTargetNames;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public BackStackRecord instantiate(final FragmentManagerImpl fragmentManagerImpl) {
        final BackStackRecord backStackRecord = new BackStackRecord(fragmentManagerImpl);
        int i = 0;
        int n = 0;
        while (i < this.mOps.length) {
            final BackStackRecord.Op op = new BackStackRecord.Op();
            final int[] mOps = this.mOps;
            final int n2 = i + 1;
            op.cmd = mOps[i];
            if (FragmentManagerImpl.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Instantiate ");
                sb.append(backStackRecord);
                sb.append(" op #");
                sb.append(n);
                sb.append(" base fragment #");
                sb.append(this.mOps[n2]);
                Log.v("FragmentManager", sb.toString());
            }
            final int[] mOps2 = this.mOps;
            final int n3 = n2 + 1;
            final int n4 = mOps2[n2];
            Fragment fragment;
            if (n4 >= 0) {
                fragment = fragmentManagerImpl.mActive.get(n4);
            }
            else {
                fragment = null;
            }
            op.fragment = fragment;
            final int[] mOps3 = this.mOps;
            final int n5 = n3 + 1;
            op.enterAnim = mOps3[n3];
            final int[] mOps4 = this.mOps;
            final int n6 = n5 + 1;
            op.exitAnim = mOps4[n5];
            final int[] mOps5 = this.mOps;
            final int n7 = n6 + 1;
            op.popEnterAnim = mOps5[n6];
            final int[] mOps6 = this.mOps;
            final int n8 = n7 + 1;
            op.popExitAnim = mOps6[n7];
            final int[] mOps7 = this.mOps;
            int n9 = n8 + 1;
            final int n10 = mOps7[n8];
            int n11 = n9;
            if (n10 > 0) {
                op.removed = new ArrayList<Fragment>(n10);
                int n12 = 0;
                while (true) {
                    n11 = n9;
                    if (n12 >= n10) {
                        break;
                    }
                    if (FragmentManagerImpl.DEBUG) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Instantiate ");
                        sb2.append(backStackRecord);
                        sb2.append(" set remove fragment #");
                        sb2.append(this.mOps[n9]);
                        Log.v("FragmentManager", sb2.toString());
                    }
                    op.removed.add(fragmentManagerImpl.mActive.get(this.mOps[n9]));
                    ++n12;
                    ++n9;
                }
            }
            i = n11;
            backStackRecord.addOp(op);
            ++n;
        }
        backStackRecord.mTransition = this.mTransition;
        backStackRecord.mTransitionStyle = this.mTransitionStyle;
        backStackRecord.mName = this.mName;
        backStackRecord.mIndex = this.mIndex;
        backStackRecord.mAddToBackStack = true;
        backStackRecord.mBreadCrumbTitleRes = this.mBreadCrumbTitleRes;
        backStackRecord.mBreadCrumbTitleText = this.mBreadCrumbTitleText;
        backStackRecord.mBreadCrumbShortTitleRes = this.mBreadCrumbShortTitleRes;
        backStackRecord.mBreadCrumbShortTitleText = this.mBreadCrumbShortTitleText;
        backStackRecord.mSharedElementSourceNames = this.mSharedElementSourceNames;
        backStackRecord.mSharedElementTargetNames = this.mSharedElementTargetNames;
        backStackRecord.bumpBackStackNesting(1);
        return backStackRecord;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeIntArray(this.mOps);
        parcel.writeInt(this.mTransition);
        parcel.writeInt(this.mTransitionStyle);
        parcel.writeString(this.mName);
        parcel.writeInt(this.mIndex);
        parcel.writeInt(this.mBreadCrumbTitleRes);
        TextUtils.writeToParcel(this.mBreadCrumbTitleText, parcel, 0);
        parcel.writeInt(this.mBreadCrumbShortTitleRes);
        TextUtils.writeToParcel(this.mBreadCrumbShortTitleText, parcel, 0);
        parcel.writeStringList((List)this.mSharedElementSourceNames);
        parcel.writeStringList((List)this.mSharedElementTargetNames);
    }
}
