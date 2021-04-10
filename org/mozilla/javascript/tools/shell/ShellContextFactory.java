package org.mozilla.javascript.tools.shell;

import org.mozilla.javascript.*;

public class ShellContextFactory extends ContextFactory
{
    private boolean allowReservedKeywords;
    private String characterEncoding;
    private ErrorReporter errorReporter;
    private boolean generatingDebug;
    private int languageVersion;
    private int optimizationLevel;
    private boolean strictMode;
    private boolean warningAsError;
    
    public ShellContextFactory() {
        this.languageVersion = 180;
        this.allowReservedKeywords = true;
    }
    
    public String getCharacterEncoding() {
        return this.characterEncoding;
    }
    
    @Override
    protected boolean hasFeature(final Context context, final int n) {
        if (n == 3) {
            return this.allowReservedKeywords;
        }
        switch (n) {
            default: {
                return super.hasFeature(context, n);
            }
            case 12: {
                return this.warningAsError;
            }
            case 10: {
                return this.generatingDebug;
            }
            case 8:
            case 9:
            case 11: {
                return this.strictMode;
            }
        }
    }
    
    @Override
    protected void onContextCreated(final Context context) {
        context.setLanguageVersion(this.languageVersion);
        context.setOptimizationLevel(this.optimizationLevel);
        if (this.errorReporter != null) {
            context.setErrorReporter(this.errorReporter);
        }
        context.setGeneratingDebug(this.generatingDebug);
        super.onContextCreated(context);
    }
    
    public void setAllowReservedKeywords(final boolean allowReservedKeywords) {
        this.allowReservedKeywords = allowReservedKeywords;
    }
    
    public void setCharacterEncoding(final String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }
    
    public void setErrorReporter(final ErrorReporter errorReporter) {
        if (errorReporter == null) {
            throw new IllegalArgumentException();
        }
        this.errorReporter = errorReporter;
    }
    
    public void setGeneratingDebug(final boolean generatingDebug) {
        this.generatingDebug = generatingDebug;
    }
    
    public void setLanguageVersion(final int languageVersion) {
        Context.checkLanguageVersion(languageVersion);
        this.checkNotSealed();
        this.languageVersion = languageVersion;
    }
    
    public void setOptimizationLevel(final int optimizationLevel) {
        Context.checkOptimizationLevel(optimizationLevel);
        this.checkNotSealed();
        this.optimizationLevel = optimizationLevel;
    }
    
    public void setStrictMode(final boolean strictMode) {
        this.checkNotSealed();
        this.strictMode = strictMode;
    }
    
    public void setWarningAsError(final boolean warningAsError) {
        this.checkNotSealed();
        this.warningAsError = warningAsError;
    }
}
