package com.zhekasmirnov.innercore.modpack.strategy.update;

import com.zhekasmirnov.horizon.util.*;
import org.json.*;
import com.zhekasmirnov.innercore.utils.*;
import java.io.*;

public class JsonMergeDirectoryUpdateStrategy extends DirectoryUpdateStrategy
{
    private JSONObject mergeJson(final JSONObject jsonObject, final JSONObject jsonObject2, final JSONObject jsonObject3) {
        return jsonObject3;
    }
    
    private JSONObject readJson(final File file, final JSONObject jsonObject) {
        try {
            return FileUtils.readJSON(file);
        }
        catch (IOException | JSONException ex) {
            return new JSONObject();
        }
    }
    
    @Override
    public void beginUpdate() throws IOException {
    }
    
    @Override
    public void finishUpdate() throws IOException {
    }
    
    @Override
    public void updateFile(String s, InputStream inputStream) throws IOException {
        final File location = this.getAssignedDirectory().getLocation();
        final File file = new File(location, s);
        file.getParentFile().mkdirs();
        final byte[] convertStreamToBytes = FileTools.convertStreamToBytes(inputStream);
        inputStream = null;
        final JSONObject json = this.readJson(file, null);
        if (json != null) {
            try {
                final JSONObject jsonObject = new JSONObject(new String(convertStreamToBytes));
                final File file2 = new File(new File(location, ".keep-unchanged"), s);
                file2.getParentFile().mkdirs();
                final JSONObject json2 = this.readJson(file2, null);
                if (json2 != null) {
                    FileUtils.writeJSON(file, this.mergeJson(json, json2, jsonObject));
                    FileUtils.writeJSON(file2, jsonObject);
                    return;
                }
            }
            catch (JSONException ex) {}
        }
        final FileOutputStream fileOutputStream = new FileOutputStream(file);
        s = (String)inputStream;
        while (true) {
            try {
                try {
                    fileOutputStream.write(convertStreamToBytes);
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    return;
                }
                finally {
                    if (fileOutputStream != null) {
                        if (s != null) {
                            final FileOutputStream fileOutputStream2 = fileOutputStream;
                            fileOutputStream2.close();
                        }
                        else {
                            fileOutputStream.close();
                        }
                    }
                }
            }
            catch (Throwable t) {}
            try {
                final FileOutputStream fileOutputStream2 = fileOutputStream;
                fileOutputStream2.close();
                continue;
            }
            catch (Throwable t2) {}
            break;
        }
    }
}
