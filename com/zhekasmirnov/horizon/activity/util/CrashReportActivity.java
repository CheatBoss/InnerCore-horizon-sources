package com.zhekasmirnov.horizon.activity.util;

import android.support.v7.app.*;
import com.zhekasmirnov.horizon.util.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import java.io.*;
import android.os.*;
import android.widget.*;

public class CrashReportActivity extends AppCompatActivity
{
    public static String archiveCrashReport(final File file) {
        String crash = null;
        if (file.exists()) {
            try {
                crash = file + "\n\n\n" + FileUtils.readFileText(file).trim();
                if (crash.length() == 0) {
                    crash = "Empty crash dump";
                }
                if (!file.renameTo(new File(file.getParent(), "archived-crash-" + crash.hashCode() + ".txt"))) {
                    Logger.error("Failed to archive crash dump " + file);
                }
            }
            catch (IOException e) {
                Logger.error("Error occurred while reading crash dump: " + e);
            }
        }
        return crash;
    }
    
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(2131427356);
        final File file = new File(this.getIntent().getStringExtra("crash_path"));
        final String crash = archiveCrashReport(file);
        if (crash != null) {
            final TextView view = (TextView)this.findViewById(2131230784);
            view.setText((CharSequence)crash);
        }
    }
}
