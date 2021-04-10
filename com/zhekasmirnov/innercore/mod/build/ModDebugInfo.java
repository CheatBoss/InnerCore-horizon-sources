package com.zhekasmirnov.innercore.mod.build;

import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;
import java.util.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import com.zhekasmirnov.innercore.api.log.*;

public class ModDebugInfo
{
    private HashMap<String, ExecutableStatus> statusMap;
    
    public ModDebugInfo() {
        this.statusMap = new HashMap<String, ExecutableStatus>();
    }
    
    public ScriptableObject getFormattedStatusMap() {
        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
        for (final String s : this.statusMap.keySet()) {
            final ExecutableStatus executableStatus = this.statusMap.get(s);
            final ScriptableObject empty2 = ScriptableObjectHelper.createEmpty();
            empty2.put("font", (Scriptable)empty2, (Object)executableStatus.getFont());
            empty2.put("status", (Scriptable)empty2, (Object)executableStatus.getStatus());
            empty2.put("report", (Scriptable)empty2, (Object)executableStatus.getReport());
            empty.put(s, (Scriptable)empty, (Object)empty2);
        }
        return empty;
    }
    
    public HashMap<String, ExecutableStatus> getStatusMap() {
        return this.statusMap;
    }
    
    public void putStatus(final String s, final ExecutableStatus executableStatus) {
        this.statusMap.put(s, executableStatus);
    }
    
    public void putStatus(final String s, final Executable executable) {
        this.statusMap.put(s, new ExecutableStatus(executable));
    }
    
    public void putStatus(final String s, final Throwable t) {
        this.statusMap.put(s, new ExecutableStatus(t));
    }
    
    public static class ExecutableStatus
    {
        private Throwable compileError;
        private Executable executable;
        private boolean isCompiled;
        
        public ExecutableStatus(final Executable executable) {
            this.isCompiled = false;
            this.isCompiled = true;
            this.executable = executable;
        }
        
        public ExecutableStatus(final Throwable compileError) {
            this.isCompiled = false;
            this.isCompiled = false;
            this.compileError = compileError;
        }
        
        public Throwable getError() {
            if (this.isCompiled) {
                return this.executable.getLastRunException();
            }
            return this.compileError;
        }
        
        public ScriptableObject getFont() {
            Font font;
            if (this.getError() != null) {
                font = new Font(-65536, 30.0f, 0.5f);
            }
            else {
                font = new Font(-16711936, 30.0f, 0.5f);
            }
            return font.asScriptable();
        }
        
        public String getReport() {
            final Throwable error = this.getError();
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getStatus());
            String string;
            if (error != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("\n");
                sb2.append(ICLog.getStackTrace(error));
                string = sb2.toString();
            }
            else {
                string = "";
            }
            sb.append(string);
            return sb.toString();
        }
        
        public String getStatus() {
            if (!this.isCompiled) {
                final StringBuilder sb = new StringBuilder();
                sb.append("compile error: ");
                sb.append(this.compileError);
                return sb.toString();
            }
            if (this.executable.getLastRunException() != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("run error: ");
                sb2.append(this.executable.getLastRunException());
                return sb2.toString();
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("ok");
            String s;
            if (this.executable.isLoadedFromDex) {
                s = " [bytecode]";
            }
            else {
                s = "";
            }
            sb3.append(s);
            return sb3.toString();
        }
    }
}
