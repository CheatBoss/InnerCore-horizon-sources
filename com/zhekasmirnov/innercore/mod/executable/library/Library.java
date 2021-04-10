package com.zhekasmirnov.innercore.mod.executable.library;

import com.zhekasmirnov.innercore.mod.executable.*;
import com.zhekasmirnov.innercore.api.log.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.mod.util.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.*;

public class Library extends Executable
{
    private ArrayList<LibraryDependency> dependencies;
    private HashSet<String> exportNames;
    private ArrayList<LibraryExport> exports;
    private boolean isLoadingInProgress;
    private boolean isOldFormatted;
    private boolean isShared;
    private String libName;
    private LibraryState state;
    private int versionCode;
    
    public Library(final Context context, final Script script, final ScriptableObject scriptableObject, final CompilerConfig compilerConfig, final API api) {
        super(context, script, scriptableObject, compilerConfig, api);
        this.libName = null;
        this.versionCode = 0;
        this.isShared = false;
        this.dependencies = new ArrayList<LibraryDependency>();
        this.state = LibraryState.NONE;
        this.isOldFormatted = false;
        this.isLoadingInProgress = false;
        this.exports = new ArrayList<LibraryExport>();
        this.exportNames = new HashSet<String>();
    }
    
    public Library(final Context context, final ScriptableObject scriptableObject, final CompilerConfig compilerConfig, final API api) {
        super(context, scriptableObject, compilerConfig, api);
        this.libName = null;
        this.versionCode = 0;
        this.isShared = false;
        this.dependencies = new ArrayList<LibraryDependency>();
        this.state = LibraryState.NONE;
        this.isOldFormatted = false;
        this.isLoadingInProgress = false;
        this.exports = new ArrayList<LibraryExport>();
        this.exportNames = new HashSet<String>();
    }
    
    private void addExport(final LibraryExport libraryExport) {
        if (libraryExport.name != null && !libraryExport.name.equals("*")) {
            this.exports.add(libraryExport);
            this.exportNames.add(libraryExport.name);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid library export name: ");
        sb.append(libraryExport.name);
        throw new IllegalArgumentException(sb.toString());
    }
    
    private void headerCall(final String libName, final int versionCode, final String s, final boolean isShared, final ArrayList<LibraryDependency> dependencies) {
        if (libName == null) {
            throw new InvalidHeaderCall("Error in library initialization - name is not given");
        }
        final API instanceByName = API.getInstanceByName(s);
        if (instanceByName == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Error in library initialization - invalid API name: ");
            sb.append(s);
            throw new InvalidHeaderCall(sb.toString());
        }
        if (!this.isPrepared()) {
            this.libName = libName;
            this.versionCode = versionCode;
            this.isShared = isShared;
            this.apiInstance = instanceByName;
            this.dependencies = dependencies;
            this.prepare();
            throw new RunInterruptionException();
        }
    }
    
    private void onFatalException(final Throwable lastRunException) {
        this.setState(LibraryState.INVALID);
        this.lastRunException = lastRunException;
        final StringBuilder sb = new StringBuilder();
        sb.append("failed to run executable '");
        sb.append(this.name);
        sb.append("', some errors occurred:");
        ICLog.e("INNERCORE-EXEC", sb.toString(), lastRunException);
    }
    
    private void resolveAnnotations(final LibraryAnnotation.AnnotationSet set) {
        final LibraryAnnotation.AnnotationInstance find = set.find("$EXPORT");
        final LibraryAnnotation.AnnotationInstance find2 = set.find("$BACKCOMP");
        if (find != null) {
            final LibraryExport libraryExport = new LibraryExport(find.getParameter(0, (Class<? extends CharSequence>)CharSequence.class).toString(), set.getTarget());
            if (find2 != null) {
                libraryExport.setTargetVersion(find2.getParameter(0, (Class<? extends Number>)Number.class).intValue());
            }
            this.addExport(libraryExport);
        }
    }
    
    private void setState(final LibraryState state) {
        this.state = state;
    }
    
    public ArrayList<LibraryDependency> getDependencies() {
        return this.dependencies;
    }
    
    public LibraryExport getExportForDependency(final LibraryDependency libraryDependency, final String s) {
        LibraryExport libraryExport = null;
        for (final LibraryExport libraryExport2 : this.exports) {
            LibraryExport libraryExport3 = libraryExport;
            Label_0127: {
                if (s.equals(libraryExport2.name)) {
                    LibraryExport libraryExport4;
                    if ((libraryExport4 = libraryExport) == null) {
                        libraryExport4 = libraryExport2;
                    }
                    libraryExport3 = libraryExport4;
                    if (libraryDependency.hasTargetVersion()) {
                        libraryExport3 = libraryExport4;
                        if (libraryExport2.hasTargetVersion()) {
                            libraryExport3 = libraryExport4;
                            if (libraryExport2.getTargetVersion() >= libraryDependency.minVersion) {
                                if (libraryExport4.hasTargetVersion()) {
                                    libraryExport3 = libraryExport4;
                                    if (libraryExport4.getTargetVersion() <= libraryExport2.getTargetVersion()) {
                                        break Label_0127;
                                    }
                                }
                                libraryExport3 = libraryExport2;
                            }
                        }
                    }
                }
            }
            libraryExport = libraryExport3;
        }
        return libraryExport;
    }
    
    public HashSet<String> getExportNames() {
        return this.exportNames;
    }
    
    public String getLibName() {
        return this.libName;
    }
    
    public int getVersionCode() {
        return this.versionCode;
    }
    
    public void initialize() {
        this.setState(LibraryState.INITIALIZED);
        this.scriptScope.put("LIBRARY", (Scriptable)this.scriptScope, (Object)new ScriptableFunctionImpl() {
            public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
                int i = 0;
                final ScriptableObjectWrapper scriptableObjectWrapper = new ScriptableObjectWrapper((Scriptable)array[0]);
                final ArrayList<LibraryDependency> list = new ArrayList<LibraryDependency>();
                final ScriptableObjectWrapper scriptableWrapper = scriptableObjectWrapper.getScriptableWrapper("dependencies");
                if (scriptableWrapper != null) {
                    for (Object[] array2 = scriptableWrapper.asArray(); i < array2.length; ++i) {
                        final Object o = array2[i];
                        if (o instanceof String) {
                            final LibraryDependency libraryDependency = new LibraryDependency((String)o);
                            libraryDependency.setParentMod(Library.this.getParentMod());
                            list.add(libraryDependency);
                        }
                    }
                }
                Library.this.headerCall(scriptableObjectWrapper.getString("name"), scriptableObjectWrapper.getInt("version"), scriptableObjectWrapper.getString("api"), scriptableObjectWrapper.getBoolean("shared"), list);
                return null;
            }
        });
        try {
            this.runScript();
        }
        catch (Throwable t) {
            this.isOldFormatted = true;
            this.isShared = true;
            this.libName = this.compilerConfig.getName();
            this.versionCode = 0;
            if (this.libName.endsWith(".js")) {
                this.libName = this.libName.substring(0, this.libName.length() - 3);
            }
            this.prepare();
        }
        catch (InvalidHeaderCall invalidHeaderCall) {
            this.onFatalException(invalidHeaderCall);
        }
        catch (RunInterruptionException ex) {
            this.isOldFormatted = false;
        }
    }
    
    public boolean isInitialized() {
        return this.state != LibraryState.NONE;
    }
    
    public boolean isInvalid() {
        return this.state == LibraryState.INVALID;
    }
    
    public boolean isLoaded() {
        return this.state == LibraryState.LOADED;
    }
    
    public boolean isLoadingInProgress() {
        return this.isLoadingInProgress;
    }
    
    public boolean isPrepared() {
        return this.isInitialized() && !this.isInvalid() && this.state != LibraryState.INITIALIZED;
    }
    
    public boolean isShared() {
        return this.isShared;
    }
    
    public void load() {
        if (!this.isPrepared()) {
            this.onFatalException(new IllegalStateException("Trying to load library without calling prepare()"));
            return;
        }
        this.isLoadingInProgress = true;
        for (final LibraryDependency libraryDependency : this.dependencies) {
            if (LibraryRegistry.resolveDependencyAndLoadLib(libraryDependency) == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("failed to resolve dependency ");
                sb.append(libraryDependency);
                sb.append(" for library ");
                sb.append(this.libName);
                sb.append(", it may load incorrectly.");
                ICLog.i("ERROR", sb.toString());
            }
        }
        try {
            this.runScript();
            final Iterator<LibraryAnnotation.AnnotationSet> iterator2 = LibraryAnnotation.getAllAnnotations((Scriptable)this.scriptScope).iterator();
            while (iterator2.hasNext()) {
                this.resolveAnnotations(iterator2.next());
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("library loaded ");
            sb2.append(this.libName);
            sb2.append(":");
            sb2.append(this.versionCode);
            ICLog.d("LIBRARY", sb2.toString());
            this.setState(LibraryState.LOADED);
            this.isLoadingInProgress = false;
        }
        catch (Throwable t) {
            this.onFatalException(t);
            this.isLoadingInProgress = false;
        }
    }
    
    public void prepare() {
        if (!this.isInvalid()) {
            if (!this.isInitialized()) {
                return;
            }
            try {
                this.injectStaticAPIs();
                if (this.apiInstance != null) {
                    this.apiInstance.injectIntoScope(this.scriptScope);
                    this.apiInstance.prepareExecutable(this);
                }
                new LibraryAnnotation("$EXPORT", new Class[] { CharSequence.class }).injectMethod((Scriptable)this.scriptScope);
                new LibraryAnnotation("$BACKCOMP", new Class[] { Number.class }).injectMethod((Scriptable)this.scriptScope);
                final ScriptableFunctionImpl scriptableFunctionImpl = new ScriptableFunctionImpl() {
                    public Object call(Context context, final Scriptable scriptable, Scriptable scriptable2, final Object[] array) {
                        final Object o = array[0];
                        scriptable2 = (Scriptable)array[1];
                        final int n = -1;
                        context = (Context)o;
                        int intValue = n;
                        if (((String)o).contains(":")) {
                            final String[] split = ((String)o).split(":");
                            context = (Context)split[0];
                            try {
                                intValue = Integer.valueOf(split[1]);
                            }
                            catch (NumberFormatException ex) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("invalid formatted library export name ");
                                sb.append((String)context);
                                sb.append(" target version will be ignored");
                                ICLog.i("ERROR", sb.toString());
                                intValue = n;
                            }
                        }
                        final LibraryExport libraryExport = new LibraryExport((String)context, scriptable2);
                        libraryExport.setTargetVersion(intValue);
                        Library.this.addExport(libraryExport);
                        return null;
                    }
                };
                this.scriptScope.put("registerAPIUnit", (Scriptable)this.scriptScope, (Object)scriptableFunctionImpl);
                this.scriptScope.put("EXPORT", (Scriptable)this.scriptScope, (Object)scriptableFunctionImpl);
                this.setState(LibraryState.PREPARED);
            }
            catch (Throwable t) {
                this.onFatalException(t);
            }
        }
    }
    
    @Override
    public Object runForResult() {
        throw new UnsupportedOperationException("runForResult is not supported for library executables");
    }
    
    private static class InvalidHeaderCall extends RuntimeException
    {
        public InvalidHeaderCall(final String s) {
            super(s);
        }
    }
    
    private static class RunInterruptionException extends RuntimeException
    {
    }
}
