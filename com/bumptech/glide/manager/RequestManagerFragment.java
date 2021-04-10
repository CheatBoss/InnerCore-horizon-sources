package com.bumptech.glide.manager;

import com.bumptech.glide.*;
import android.annotation.*;
import android.os.*;
import java.util.*;
import android.app.*;

@TargetApi(11)
public class RequestManagerFragment extends Fragment
{
    private final HashSet<RequestManagerFragment> childRequestManagerFragments;
    private final ActivityFragmentLifecycle lifecycle;
    private RequestManager requestManager;
    private final RequestManagerTreeNode requestManagerTreeNode;
    private RequestManagerFragment rootRequestManagerFragment;
    
    public RequestManagerFragment() {
        this(new ActivityFragmentLifecycle());
    }
    
    @SuppressLint({ "ValidFragment" })
    RequestManagerFragment(final ActivityFragmentLifecycle lifecycle) {
        this.requestManagerTreeNode = new FragmentRequestManagerTreeNode();
        this.childRequestManagerFragments = new HashSet<RequestManagerFragment>();
        this.lifecycle = lifecycle;
    }
    
    private void addChildRequestManagerFragment(final RequestManagerFragment requestManagerFragment) {
        this.childRequestManagerFragments.add(requestManagerFragment);
    }
    
    @TargetApi(17)
    private boolean isDescendant(Fragment parentFragment) {
        final Fragment parentFragment2 = this.getParentFragment();
        while (parentFragment.getParentFragment() != null) {
            if (parentFragment.getParentFragment() == parentFragment2) {
                return true;
            }
            parentFragment = parentFragment.getParentFragment();
        }
        return false;
    }
    
    private void removeChildRequestManagerFragment(final RequestManagerFragment requestManagerFragment) {
        this.childRequestManagerFragments.remove(requestManagerFragment);
    }
    
    @TargetApi(17)
    public Set<RequestManagerFragment> getDescendantRequestManagerFragments() {
        if (this.rootRequestManagerFragment == this) {
            return Collections.unmodifiableSet((Set<? extends RequestManagerFragment>)this.childRequestManagerFragments);
        }
        if (this.rootRequestManagerFragment != null && Build$VERSION.SDK_INT >= 17) {
            final HashSet<RequestManagerFragment> set = new HashSet<RequestManagerFragment>();
            for (final RequestManagerFragment requestManagerFragment : this.rootRequestManagerFragment.getDescendantRequestManagerFragments()) {
                if (this.isDescendant(requestManagerFragment.getParentFragment())) {
                    set.add(requestManagerFragment);
                }
            }
            return (Set<RequestManagerFragment>)Collections.unmodifiableSet((Set<?>)set);
        }
        return Collections.emptySet();
    }
    
    ActivityFragmentLifecycle getLifecycle() {
        return this.lifecycle;
    }
    
    public RequestManager getRequestManager() {
        return this.requestManager;
    }
    
    public RequestManagerTreeNode getRequestManagerTreeNode() {
        return this.requestManagerTreeNode;
    }
    
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        this.rootRequestManagerFragment = RequestManagerRetriever.get().getRequestManagerFragment(this.getActivity().getFragmentManager());
        if (this.rootRequestManagerFragment != this) {
            this.rootRequestManagerFragment.addChildRequestManagerFragment(this);
        }
    }
    
    public void onDestroy() {
        super.onDestroy();
        this.lifecycle.onDestroy();
    }
    
    public void onDetach() {
        super.onDetach();
        if (this.rootRequestManagerFragment != null) {
            this.rootRequestManagerFragment.removeChildRequestManagerFragment(this);
            this.rootRequestManagerFragment = null;
        }
    }
    
    public void onLowMemory() {
        if (this.requestManager != null) {
            this.requestManager.onLowMemory();
        }
    }
    
    public void onStart() {
        super.onStart();
        this.lifecycle.onStart();
    }
    
    public void onStop() {
        super.onStop();
        this.lifecycle.onStop();
    }
    
    public void onTrimMemory(final int n) {
        if (this.requestManager != null) {
            this.requestManager.onTrimMemory(n);
        }
    }
    
    public void setRequestManager(final RequestManager requestManager) {
        this.requestManager = requestManager;
    }
    
    private class FragmentRequestManagerTreeNode implements RequestManagerTreeNode
    {
        @Override
        public Set<RequestManager> getDescendants() {
            final Set<RequestManagerFragment> descendantRequestManagerFragments = RequestManagerFragment.this.getDescendantRequestManagerFragments();
            final HashSet set = new HashSet<RequestManager>(descendantRequestManagerFragments.size());
            for (final RequestManagerFragment requestManagerFragment : descendantRequestManagerFragments) {
                if (requestManagerFragment.getRequestManager() != null) {
                    set.add(requestManagerFragment.getRequestManager());
                }
            }
            return (Set<RequestManager>)set;
        }
    }
}
