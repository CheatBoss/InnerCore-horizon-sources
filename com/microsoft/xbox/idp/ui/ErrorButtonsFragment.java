package com.microsoft.xbox.idp.ui;

import com.microsoft.xbox.idp.compat.*;
import android.app.*;
import android.view.*;
import android.os.*;
import com.microsoft.xboxtcui.*;
import android.widget.*;

public class ErrorButtonsFragment extends BaseFragment implements View$OnClickListener
{
    public static final String ARG_LEFT_ERROR_BUTTON_STRING_ID = "ARG_LEFT_ERROR_BUTTON_STRING_ID";
    private static final Callbacks NO_OP_CALLBACKS;
    private Callbacks callbacks;
    
    static {
        NO_OP_CALLBACKS = (Callbacks)new Callbacks() {
            @Override
            public void onClickedLeftButton() {
            }
            
            @Override
            public void onClickedRightButton() {
            }
        };
    }
    
    public ErrorButtonsFragment() {
        this.callbacks = ErrorButtonsFragment.NO_OP_CALLBACKS;
    }
    
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        this.callbacks = (Callbacks)activity;
    }
    
    public void onClick(final View view) {
        final int id = view.getId();
        if (id == R$id.xbid_error_left_button) {
            this.callbacks.onClickedLeftButton();
            return;
        }
        if (id == R$id.xbid_error_right_button) {
            this.callbacks.onClickedRightButton();
        }
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(R$layout.xbid_fragment_error_buttons, viewGroup, false);
    }
    
    public void onDetach() {
        super.onDetach();
        this.callbacks = ErrorButtonsFragment.NO_OP_CALLBACKS;
    }
    
    public void onViewCreated(final View view, final Bundle bundle) {
        super.onViewCreated(view, bundle);
        final Button button = (Button)view.findViewById(R$id.xbid_error_left_button);
        button.setOnClickListener((View$OnClickListener)this);
        view.findViewById(R$id.xbid_error_right_button).setOnClickListener((View$OnClickListener)this);
        final Bundle arguments = this.getArguments();
        if (arguments != null && arguments.containsKey("ARG_LEFT_ERROR_BUTTON_STRING_ID")) {
            button.setText(arguments.getInt("ARG_LEFT_ERROR_BUTTON_STRING_ID"));
        }
    }
    
    public interface Callbacks
    {
        void onClickedLeftButton();
        
        void onClickedRightButton();
    }
}
