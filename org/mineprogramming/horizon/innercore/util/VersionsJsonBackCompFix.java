package org.mineprogramming.horizon.innercore.util;

import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.horizon.util.*;
import org.mineprogramming.horizon.innercore.model.*;
import java.io.*;
import org.json.*;
import java.util.*;

public class VersionsJsonBackCompFix
{
    private final File defaultPackRoot;
    
    public VersionsJsonBackCompFix(final File defaultPackRoot) {
        this.defaultPackRoot = defaultPackRoot;
    }
    
    public void runFixIfRequired() {
        final File file = new File(this.defaultPackRoot, "versions.json");
        final StringBuilder sb = new StringBuilder();
        sb.append("runFixIfRequired ");
        sb.append(file.getAbsolutePath());
        ICLog.d("DEBUG", sb.toString());
        try {
            final JSONObject json = FileUtils.readJSON(file);
            if (json == null) {
                return;
            }
            final Iterator keys = json.keys();
            while (keys.hasNext()) {
                final String s = keys.next();
                try {
                    final int int1 = Integer.parseInt(s);
                    final JSONObject optJSONObject = json.optJSONObject(s);
                    if (optJSONObject == null) {
                        continue;
                    }
                    final String optString = optJSONObject.optString("directory");
                    final int optInt = optJSONObject.optInt("version");
                    if (optString == null || optString.length() <= 0) {
                        continue;
                    }
                    final File file2 = new File(this.defaultPackRoot, optString);
                    if (file2.isDirectory()) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("parsed mod external data from versions.json: dir=");
                        sb2.append(optString);
                        sb2.append(", id=");
                        sb2.append(int1);
                        sb2.append(", version=");
                        sb2.append(optInt);
                        ICLog.d("Versions-Json-Fix", sb2.toString());
                        new ModPreferences(file2).setIcmodsData(int1, optInt);
                    }
                    else {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("missing mod directory in versions.json: ");
                        sb3.append(optString);
                        ICLog.d("Versions-Json-Fix", sb3.toString());
                    }
                }
                catch (NumberFormatException ex) {}
            }
            file.delete();
        }
        catch (IOException | JSONException ex2) {}
    }
}
