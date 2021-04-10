package org.mozilla.javascript.ast;

import org.mozilla.javascript.*;
import java.util.*;

public class ErrorCollector implements IdeErrorReporter
{
    private List<ParseProblem> errors;
    
    public ErrorCollector() {
        this.errors = new ArrayList<ParseProblem>();
    }
    
    @Override
    public void error(final String s, final String s2, final int n, final int n2) {
        this.errors.add(new ParseProblem(ParseProblem.Type.Error, s, s2, n, n2));
    }
    
    @Override
    public void error(final String s, final String s2, final int n, final String s3, final int n2) {
        throw new UnsupportedOperationException();
    }
    
    public List<ParseProblem> getErrors() {
        return this.errors;
    }
    
    @Override
    public EvaluatorException runtimeError(final String s, final String s2, final int n, final String s3, final int n2) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.errors.size() * 100);
        final Iterator<ParseProblem> iterator = this.errors.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().toString());
            sb.append("\n");
        }
        return sb.toString();
    }
    
    @Override
    public void warning(final String s, final String s2, final int n, final int n2) {
        this.errors.add(new ParseProblem(ParseProblem.Type.Warning, s, s2, n, n2));
    }
    
    @Override
    public void warning(final String s, final String s2, final int n, final String s3, final int n2) {
        throw new UnsupportedOperationException();
    }
}
