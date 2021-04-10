package com.microsoft.xbox.idp.ui;

import com.microsoft.xbox.idp.compat.*;
import com.microsoft.xbox.idp.model.*;
import com.microsoft.xbox.idp.toolkit.*;
import android.widget.*;
import android.os.*;
import android.util.*;
import com.google.gson.*;
import com.microsoft.xbox.idp.services.*;
import com.microsoft.xbox.idp.util.*;
import android.content.*;
import android.text.*;
import android.graphics.*;
import android.app.*;
import android.view.*;
import com.microsoft.xboxtcui.*;

public class HeaderFragment extends BaseFragment implements View$OnClickListener
{
    private static final int LOADER_GET_PROFILE = 1;
    private static final int LOADER_USER_IMAGE_URL = 2;
    private static final Callbacks NO_OP_CALLBACKS;
    private static final String TAG;
    private Callbacks callbacks;
    private final LoaderManager$LoaderCallbacks<BitmapLoader.Result> imageCallbacks;
    private UserAccount userAccount;
    LoaderManager$LoaderCallbacks<ObjectLoader.Result<UserAccount>> userAccountCallbacks;
    private TextView userEmail;
    private ImageView userImageView;
    private TextView userName;
    
    static {
        TAG = HeaderFragment.class.getSimpleName();
        NO_OP_CALLBACKS = (Callbacks)new Callbacks() {
            @Override
            public void onClickCloseHeader() {
            }
        };
    }
    
    public HeaderFragment() {
        this.callbacks = HeaderFragment.NO_OP_CALLBACKS;
        this.userAccountCallbacks = (LoaderManager$LoaderCallbacks<ObjectLoader.Result<UserAccount>>)new LoaderManager$LoaderCallbacks<ObjectLoader.Result<UserAccount>>() {
            public Loader<ObjectLoader.Result<UserAccount>> onCreateLoader(final int n, final Bundle bundle) {
                Log.d(HeaderFragment.TAG, "Creating LOADER_GET_PROFILE");
                return (Loader<ObjectLoader.Result<UserAccount>>)new ObjectLoader((Context)HeaderFragment.this.getActivity(), CacheUtil.getObjectLoaderCache(), new FragmentLoaderKey(HeaderFragment.class, 1), (Class<Object>)UserAccount.class, UserAccount.registerAdapters(new GsonBuilder()).create(), HttpUtil.appendCommonParameters(new HttpCall("GET", EndpointsFactory.get().accounts(), "/users/current/profile"), "4"));
            }
            
            public void onLoadFinished(final Loader<ObjectLoader.Result<UserAccount>> loader, final ObjectLoader.Result<UserAccount> result) {
                Log.d(HeaderFragment.TAG, "LOADER_GET_PROFILE finished");
                if (result.hasData()) {
                    HeaderFragment.this.userAccount = result.getData();
                    HeaderFragment.this.userEmail.setText((CharSequence)HeaderFragment.this.userAccount.email);
                    if (TextUtils.isEmpty((CharSequence)HeaderFragment.this.userAccount.firstName) && TextUtils.isEmpty((CharSequence)HeaderFragment.this.userAccount.lastName)) {
                        HeaderFragment.this.userName.setVisibility(8);
                    }
                    else {
                        HeaderFragment.this.userName.setVisibility(0);
                        HeaderFragment.this.userName.setText((CharSequence)HeaderFragment.this.getString(R$string.xbid_first_and_last_name_android, new Object[] { HeaderFragment.this.userAccount.firstName, HeaderFragment.this.userAccount.lastName }));
                    }
                    HeaderFragment.this.getLoaderManager().initLoader(2, (Bundle)null, HeaderFragment.this.imageCallbacks);
                    return;
                }
                Log.e(HeaderFragment.TAG, "Error getting UserAccount");
            }
            
            public void onLoaderReset(final Loader<ObjectLoader.Result<UserAccount>> loader) {
            }
        };
        this.imageCallbacks = (LoaderManager$LoaderCallbacks<BitmapLoader.Result>)new LoaderManager$LoaderCallbacks<BitmapLoader.Result>() {
            public Loader<BitmapLoader.Result> onCreateLoader(final int n, final Bundle bundle) {
                Log.d(HeaderFragment.TAG, "Creating LOADER_USER_IMAGE_URL");
                final String access$000 = HeaderFragment.TAG;
                final StringBuilder sb = new StringBuilder();
                sb.append("url: ");
                sb.append(HeaderFragment.this.userAccount.imageUrl);
                Log.d(access$000, sb.toString());
                return new BitmapLoader((Context)HeaderFragment.this.getActivity(), CacheUtil.getBitmapCache(), HeaderFragment.this.userAccount.imageUrl, HeaderFragment.this.userAccount.imageUrl);
            }
            
            public void onLoadFinished(final Loader<BitmapLoader.Result> loader, final BitmapLoader.Result result) {
                Log.d(HeaderFragment.TAG, "LOADER_USER_IMAGE_URL finished");
                if (result.hasData()) {
                    HeaderFragment.this.userImageView.setVisibility(0);
                    HeaderFragment.this.userImageView.setImageBitmap((Bitmap)result.getData());
                    return;
                }
                if (result.hasException()) {
                    HeaderFragment.this.userImageView.setVisibility(8);
                    final String access$000 = HeaderFragment.TAG;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Failed to load user image with message: ");
                    sb.append(result.getException().getMessage());
                    Log.w(access$000, sb.toString());
                }
            }
            
            public void onLoaderReset(final Loader<BitmapLoader.Result> loader) {
                HeaderFragment.this.userImageView.setImageBitmap((Bitmap)null);
            }
        };
    }
    
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        this.callbacks = (Callbacks)activity;
    }
    
    public void onClick(final View view) {
        if (view.getId() == R$id.xbid_close) {
            this.callbacks.onClickCloseHeader();
        }
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(R$layout.xbid_fragment_header, viewGroup, false);
    }
    
    public void onDetach() {
        super.onDetach();
        this.callbacks = HeaderFragment.NO_OP_CALLBACKS;
    }
    
    public void onResume() {
        super.onResume();
        final Bundle arguments = this.getArguments();
        if (arguments != null) {
            this.getLoaderManager().initLoader(1, arguments, (LoaderManager$LoaderCallbacks)this.userAccountCallbacks);
            return;
        }
        Log.e(HeaderFragment.TAG, "No arguments provided");
    }
    
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        view.findViewById(R$id.xbid_close).setOnClickListener((View$OnClickListener)this);
        this.userImageView = (ImageView)view.findViewById(R$id.xbid_user_image);
        this.userName = (TextView)view.findViewById(R$id.xbid_user_name);
        this.userEmail = (TextView)view.findViewById(R$id.xbid_user_email);
    }
    
    public interface Callbacks
    {
        void onClickCloseHeader();
    }
}
