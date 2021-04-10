package org.mineprogramming.horizon.innercore.model;

import org.json.*;
import android.os.*;
import org.mineprogramming.horizon.innercore.util.*;

public abstract class RemoteModSource extends ModSource
{
    abstract void processData(final String p0) throws JSONException;
    
    protected class DownloadHandler extends AsyncTask<String, Void, String>
    {
        protected String doInBackground(final String... array) {
            try {
                return DownloadHelper.downloadString(array[0]);
            }
            catch (Exception ex) {
                return null;
            }
        }
        
        protected void onPostExecute(final String s) {
            super.onPostExecute((Object)s);
            if (s == null) {
                RemoteModSource.this.notifyLoadFailed();
                return;
            }
            try {
                RemoteModSource.this.processData(s);
            }
            catch (Exception ex) {
                ex.printStackTrace();
                RemoteModSource.this.notifyLoadFailed();
            }
        }
        
        protected void onPreExecute() {
            super.onPreExecute();
            RemoteModSource.this.notifyLoadInProgress();
        }
    }
}
