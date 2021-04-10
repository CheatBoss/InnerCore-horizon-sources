package org.mozilla.javascript;

import java.util.*;
import org.mozilla.javascript.ast.*;

public class CompilerEnvirons
{
    Set<String> activationNames;
    private boolean allowMemberExprAsFunctionName;
    private boolean allowSharpComments;
    private ErrorReporter errorReporter;
    private boolean generateDebugInfo;
    private boolean generateObserverCount;
    private boolean generatingSource;
    private boolean ideMode;
    private int languageVersion;
    private int optimizationLevel;
    private boolean recordingComments;
    private boolean recordingLocalJsDocComments;
    private boolean recoverFromErrors;
    private boolean reservedKeywordAsIdentifier;
    private boolean strictMode;
    private boolean warnTrailingComma;
    private boolean warningAsError;
    private boolean xmlAvailable;
    
    public CompilerEnvirons() {
        this.errorReporter = DefaultErrorReporter.instance;
        this.languageVersion = 0;
        this.generateDebugInfo = true;
        this.reservedKeywordAsIdentifier = true;
        this.allowMemberExprAsFunctionName = false;
        this.xmlAvailable = true;
        this.optimizationLevel = 0;
        this.generatingSource = true;
        this.strictMode = false;
        this.warningAsError = false;
        this.generateObserverCount = false;
        this.allowSharpComments = false;
    }
    
    public static CompilerEnvirons ideEnvirons() {
        final CompilerEnvirons compilerEnvirons = new CompilerEnvirons();
        compilerEnvirons.setRecoverFromErrors(true);
        compilerEnvirons.setRecordingComments(true);
        compilerEnvirons.setStrictMode(true);
        compilerEnvirons.setWarnTrailingComma(true);
        compilerEnvirons.setLanguageVersion(170);
        compilerEnvirons.setReservedKeywordAsIdentifier(true);
        compilerEnvirons.setIdeMode(true);
        compilerEnvirons.setErrorReporter(new ErrorCollector());
        return compilerEnvirons;
    }
    
    public Set<String> getActivationNames() {
        return this.activationNames;
    }
    
    public boolean getAllowSharpComments() {
        return this.allowSharpComments;
    }
    
    public final ErrorReporter getErrorReporter() {
        return this.errorReporter;
    }
    
    public final int getLanguageVersion() {
        return this.languageVersion;
    }
    
    public final int getOptimizationLevel() {
        return this.optimizationLevel;
    }
    
    public boolean getWarnTrailingComma() {
        return this.warnTrailingComma;
    }
    
    public void initFromContext(final Context context) {
        this.setErrorReporter(context.getErrorReporter());
        this.languageVersion = context.getLanguageVersion();
        this.generateDebugInfo = (!context.isGeneratingDebugChanged() || context.isGeneratingDebug());
        this.reservedKeywordAsIdentifier = context.hasFeature(3);
        this.allowMemberExprAsFunctionName = context.hasFeature(2);
        this.strictMode = context.hasFeature(11);
        this.warningAsError = context.hasFeature(12);
        this.xmlAvailable = context.hasFeature(6);
        this.optimizationLevel = context.getOptimizationLevel();
        this.generatingSource = context.isGeneratingSource();
        this.activationNames = context.activationNames;
        this.generateObserverCount = context.generateObserverCount;
    }
    
    public final boolean isAllowMemberExprAsFunctionName() {
        return this.allowMemberExprAsFunctionName;
    }
    
    public final boolean isGenerateDebugInfo() {
        return this.generateDebugInfo;
    }
    
    public boolean isGenerateObserverCount() {
        return this.generateObserverCount;
    }
    
    public final boolean isGeneratingSource() {
        return this.generatingSource;
    }
    
    public boolean isIdeMode() {
        return this.ideMode;
    }
    
    public boolean isRecordingComments() {
        return this.recordingComments;
    }
    
    public boolean isRecordingLocalJsDocComments() {
        return this.recordingLocalJsDocComments;
    }
    
    public final boolean isReservedKeywordAsIdentifier() {
        return this.reservedKeywordAsIdentifier;
    }
    
    public final boolean isStrictMode() {
        return this.strictMode;
    }
    
    public final boolean isXmlAvailable() {
        return this.xmlAvailable;
    }
    
    public boolean recoverFromErrors() {
        return this.recoverFromErrors;
    }
    
    public final boolean reportWarningAsError() {
        return this.warningAsError;
    }
    
    public void setActivationNames(final Set<String> activationNames) {
        this.activationNames = activationNames;
    }
    
    public void setAllowMemberExprAsFunctionName(final boolean allowMemberExprAsFunctionName) {
        this.allowMemberExprAsFunctionName = allowMemberExprAsFunctionName;
    }
    
    public void setAllowSharpComments(final boolean allowSharpComments) {
        this.allowSharpComments = allowSharpComments;
    }
    
    public void setErrorReporter(final ErrorReporter errorReporter) {
        if (errorReporter == null) {
            throw new IllegalArgumentException();
        }
        this.errorReporter = errorReporter;
    }
    
    public void setGenerateDebugInfo(final boolean generateDebugInfo) {
        this.generateDebugInfo = generateDebugInfo;
    }
    
    public void setGenerateObserverCount(final boolean generateObserverCount) {
        this.generateObserverCount = generateObserverCount;
    }
    
    public void setGeneratingSource(final boolean generatingSource) {
        this.generatingSource = generatingSource;
    }
    
    public void setIdeMode(final boolean ideMode) {
        this.ideMode = ideMode;
    }
    
    public void setLanguageVersion(final int languageVersion) {
        Context.checkLanguageVersion(languageVersion);
        this.languageVersion = languageVersion;
    }
    
    public void setOptimizationLevel(final int optimizationLevel) {
        Context.checkOptimizationLevel(optimizationLevel);
        this.optimizationLevel = optimizationLevel;
    }
    
    public void setRecordingComments(final boolean recordingComments) {
        this.recordingComments = recordingComments;
    }
    
    public void setRecordingLocalJsDocComments(final boolean recordingLocalJsDocComments) {
        this.recordingLocalJsDocComments = recordingLocalJsDocComments;
    }
    
    public void setRecoverFromErrors(final boolean recoverFromErrors) {
        this.recoverFromErrors = recoverFromErrors;
    }
    
    public void setReservedKeywordAsIdentifier(final boolean reservedKeywordAsIdentifier) {
        this.reservedKeywordAsIdentifier = reservedKeywordAsIdentifier;
    }
    
    public void setStrictMode(final boolean strictMode) {
        this.strictMode = strictMode;
    }
    
    public void setWarnTrailingComma(final boolean warnTrailingComma) {
        this.warnTrailingComma = warnTrailingComma;
    }
    
    public void setXmlAvailable(final boolean xmlAvailable) {
        this.xmlAvailable = xmlAvailable;
    }
}
