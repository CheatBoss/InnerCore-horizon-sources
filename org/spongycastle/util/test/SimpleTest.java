package org.spongycastle.util.test;

import java.io.*;
import org.spongycastle.util.*;

public abstract class SimpleTest implements Test
{
    protected static void runTest(final Test test) {
        runTest(test, System.out);
    }
    
    protected static void runTest(final Test test, final PrintStream printStream) {
        final TestResult perform = test.perform();
        printStream.println(perform.toString());
        if (perform.getException() != null) {
            perform.getException().printStackTrace(printStream);
        }
    }
    
    private TestResult success() {
        return SimpleTestResult.successful(this, "Okay");
    }
    
    protected boolean areEqual(final byte[] array, final byte[] array2) {
        return Arrays.areEqual(array, array2);
    }
    
    protected boolean areEqual(final byte[][] array, final byte[][] array2) {
        if (array == null && array2 == null) {
            return true;
        }
        if (array == null) {
            return false;
        }
        if (array2 == null) {
            return false;
        }
        if (array.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array.length; ++i) {
            if (!this.areEqual(array[i], array2[i])) {
                return false;
            }
        }
        return true;
    }
    
    protected void fail(final String s) {
        throw new TestFailedException(SimpleTestResult.failed(this, s));
    }
    
    protected void fail(final String s, final Object o, final Object o2) {
        throw new TestFailedException(SimpleTestResult.failed(this, s, o, o2));
    }
    
    protected void fail(final String s, final Throwable t) {
        throw new TestFailedException(SimpleTestResult.failed(this, s, t));
    }
    
    @Override
    public abstract String getName();
    
    protected void isEquals(final int n, final int n2) {
        if (n == n2) {
            return;
        }
        throw new TestFailedException(SimpleTestResult.failed(this, "no message"));
    }
    
    protected void isEquals(final long n, final long n2) {
        if (n == n2) {
            return;
        }
        throw new TestFailedException(SimpleTestResult.failed(this, "no message"));
    }
    
    protected void isEquals(final Object o, final Object o2) {
        if (o.equals(o2)) {
            return;
        }
        throw new TestFailedException(SimpleTestResult.failed(this, "no message"));
    }
    
    protected void isEquals(final String s, final long n, final long n2) {
        if (n == n2) {
            return;
        }
        throw new TestFailedException(SimpleTestResult.failed(this, s));
    }
    
    protected void isEquals(final String s, final Object o, final Object o2) {
        if (o == null && o2 == null) {
            return;
        }
        if (o == null) {
            throw new TestFailedException(SimpleTestResult.failed(this, s));
        }
        if (o2 == null) {
            throw new TestFailedException(SimpleTestResult.failed(this, s));
        }
        if (o.equals(o2)) {
            return;
        }
        throw new TestFailedException(SimpleTestResult.failed(this, s));
    }
    
    protected void isEquals(final String s, final boolean b, final boolean b2) {
        if (b == b2) {
            return;
        }
        throw new TestFailedException(SimpleTestResult.failed(this, s));
    }
    
    protected void isTrue(final String s, final boolean b) {
        if (b) {
            return;
        }
        throw new TestFailedException(SimpleTestResult.failed(this, s));
    }
    
    protected void isTrue(final boolean b) {
        if (b) {
            return;
        }
        throw new TestFailedException(SimpleTestResult.failed(this, "no message"));
    }
    
    @Override
    public TestResult perform() {
        try {
            this.performTest();
            return this.success();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Exception: ");
            sb.append(ex);
            return SimpleTestResult.failed(this, sb.toString(), ex);
        }
        catch (TestFailedException ex2) {
            return ex2.getResult();
        }
    }
    
    public abstract void performTest() throws Exception;
}
