package com.zhekasmirnov.innercore.mod.build;

import com.zhekasmirnov.innercore.utils.*;
import java.io.*;
import org.json.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.log.*;

public class CompiledSources
{
    private File dir;
    private boolean isValid;
    private JSONObject sourceList;
    private File sourceListFile;
    
    public CompiledSources(final File dir) {
        this.isValid = true;
        this.sourceList = new JSONObject();
        this.dir = dir;
        if (!dir.exists()) {
            this.invalidate();
            return;
        }
        this.sourceListFile = new File(dir, "sources.json");
        if (!this.sourceListFile.exists()) {
            this.invalidate();
            return;
        }
        try {
            this.sourceList = FileTools.readJSON(this.sourceListFile.getAbsolutePath());
            this.validateJson();
            this.validateFiles();
        }
        catch (IOException | JSONException ex) {
            final Throwable t;
            t.printStackTrace();
            this.invalidate();
        }
    }
    
    private void invalidate() {
        this.isValid = false;
        this.validateJson();
        this.validateFiles();
    }
    
    private void validateFiles() {
        if (this.dir != null && !this.dir.isDirectory()) {
            this.dir.delete();
        }
        if (this.dir != null && !this.dir.exists()) {
            this.dir.mkdirs();
        }
        if (this.sourceListFile != null && !this.sourceListFile.exists()) {
            this.saveSourceList();
        }
    }
    
    private void validateJson() {
        if (this.sourceList == null) {
            this.sourceList = new JSONObject();
        }
    }
    
    public void addCompiledSource(final String s, final File file, final String s2) {
        JSONObject optJSONObject;
        if ((optJSONObject = this.sourceList.optJSONObject(s)) == null) {
            optJSONObject = new JSONObject();
        }
        try {
            JSONArray optJSONArray;
            if ((optJSONArray = optJSONObject.optJSONArray("links")) == null) {
                optJSONArray = new JSONArray();
                optJSONObject.put("links", (Object)optJSONArray);
            }
            optJSONArray.put(optJSONArray.length(), (Object)file.getName());
            optJSONObject.put("class_name", (Object)s2);
            this.sourceList.put(s, (Object)optJSONObject);
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        this.saveSourceList();
    }
    
    public Executable getCompiledExecutableFor(final String s, final CompilerConfig compilerConfig) throws IOException {
        final File[] compiledSourceFiles = this.getCompiledSourceFilesFor(s);
        if (compiledSourceFiles != null) {
            return Compiler.loadDexList(compiledSourceFiles, compilerConfig);
        }
        return null;
    }
    
    public File[] getCompiledSourceFilesFor(String optString) {
        final JSONObject optJSONObject = this.sourceList.optJSONObject(optString);
        if (optJSONObject == null) {
            return null;
        }
        final JSONArray optJSONArray = optJSONObject.optJSONArray("links");
        int i = 0;
        if (optJSONArray != null) {
            final ArrayList<File> list = new ArrayList<File>();
            while (i < optJSONArray.length()) {
                final String optString2 = optJSONArray.optString(i);
                if (optString2 != null) {
                    final File file = new File(this.dir, optString2);
                    if (file.exists()) {
                        list.add(file);
                    }
                    else {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("compiled dex file ");
                        sb.append(optString2);
                        sb.append(" related to source ");
                        sb.append(optString);
                        sb.append(" has incorrect formatted path");
                        ICLog.d("WARNING", sb.toString());
                    }
                }
                else {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("compiled dex file at index ");
                    sb2.append(i);
                    sb2.append(" related to source ");
                    sb2.append(optString);
                    sb2.append(" has incorrect formatted path");
                    ICLog.d("WARNING", sb2.toString());
                }
                ++i;
            }
            final File[] array = new File[list.size()];
            list.toArray(array);
            return array;
        }
        optString = optJSONObject.optString("path");
        if (optString != null) {
            return new File[] { new File(this.dir, optString) };
        }
        return null;
    }
    
    public File getTargetCompilationFile(final String s) {
        return new File(this.dir, s);
    }
    
    public void reset() {
        this.validateJson();
        this.validateFiles();
        final File[] listFiles = this.dir.listFiles();
        for (int length = listFiles.length, i = 0; i < length; ++i) {
            listFiles[i].delete();
        }
        this.sourceList = null;
        this.validateJson();
        this.validateFiles();
    }
    
    public void saveSourceList() {
        try {
            FileTools.writeJSON(this.sourceListFile.getAbsolutePath(), this.sourceList);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
