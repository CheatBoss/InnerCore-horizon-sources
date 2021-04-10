package com.microsoft.xbox.xle.app.adapter;

import com.microsoft.xbox.toolkit.ui.*;
import com.microsoft.xbox.xle.viewmodel.*;
import com.microsoft.xbox.toolkit.*;
import com.microsoft.xboxtcui.*;
import android.content.*;
import java.util.*;
import android.os.*;
import android.graphics.drawable.*;
import android.widget.*;
import android.view.*;
import com.microsoft.xbox.telemetry.helpers.*;

public class ReportUserScreenAdapter extends AdapterBase
{
    private XLEButton cancelButton;
    private EditText optionalText;
    private Spinner reasonSpinner;
    private ArrayAdapter<String> reasonSpinnerAdapter;
    private XLEButton submitButton;
    private CustomTypefaceTextView titleTextView;
    private ReportUserScreenViewModel viewModel;
    
    public ReportUserScreenAdapter(final ReportUserScreenViewModel viewModel) {
        super(viewModel);
        this.viewModel = viewModel;
        this.titleTextView = (CustomTypefaceTextView)this.findViewById(R$id.report_user_title);
        this.reasonSpinner = (Spinner)this.findViewById(R$id.report_user_reason);
        this.optionalText = (EditText)this.findViewById(R$id.report_user_text);
        this.cancelButton = (XLEButton)this.findViewById(R$id.report_user_cancel);
        this.submitButton = (XLEButton)this.findViewById(R$id.report_user_submit);
        XLEAssert.assertNotNull(this.titleTextView);
        XLEAssert.assertNotNull(this.reasonSpinner);
        XLEAssert.assertNotNull(this.optionalText);
        XLEAssert.assertNotNull(this.cancelButton);
        XLEAssert.assertNotNull(this.submitButton);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        (this.reasonSpinnerAdapter = (ArrayAdapter<String>)new ArrayAdapter((Context)XboxTcuiSdk.getActivity(), R$layout.report_spinner_item, (List)this.viewModel.getReasonTitles())).setDropDownViewResource(R$layout.spinner_item_dropdown);
        this.reasonSpinner.setAdapter((SpinnerAdapter)this.reasonSpinnerAdapter);
        if (Build$VERSION.SDK_INT >= 16) {
            this.reasonSpinner.setPopupBackgroundDrawable((Drawable)new ColorDrawable(this.viewModel.getPreferredColor()));
        }
        this.reasonSpinner.setOnItemSelectedListener((AdapterView$OnItemSelectedListener)new AdapterView$OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int reason, final long n) {
                ReportUserScreenAdapter.this.viewModel.setReason(reason);
            }
            
            public void onNothingSelected(final AdapterView<?> adapterView) {
            }
        });
        this.cancelButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                ReportUserScreenAdapter.this.viewModel.onBackButtonPressed();
            }
        });
        this.submitButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                String string;
                if (ReportUserScreenAdapter.this.viewModel.getReason() == null) {
                    string = "Unknown";
                }
                else {
                    string = ReportUserScreenAdapter.this.viewModel.getReason().toString();
                }
                UTCReportUser.trackReportDialogOK(string);
                ReportUserScreenAdapter.this.viewModel.submitReport(ReportUserScreenAdapter.this.optionalText.getText().toString());
            }
        });
    }
    
    @Override
    protected void updateViewOverride() {
        final CustomTypefaceTextView titleTextView = this.titleTextView;
        if (titleTextView != null) {
            titleTextView.setText((CharSequence)this.viewModel.getTitle());
        }
        final XLEButton submitButton = this.submitButton;
        if (submitButton != null) {
            submitButton.setEnabled(this.viewModel.validReasonSelected());
        }
    }
}
