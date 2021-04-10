package org.mozilla.javascript.tools.debugger;

import org.mozilla.javascript.tools.shell.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import org.mozilla.javascript.*;

public class Main
{
    private SwingGui debugGui;
    private Dim dim;
    
    public Main(final String s) {
        this.dim = new Dim();
        this.debugGui = new SwingGui(this.dim, s);
    }
    
    public static void main(final String[] array) {
        final Main main = new Main("Rhino JavaScript Debugger");
        main.doBreak();
        main.setExitAction(new IProxy(1));
        System.setIn(main.getIn());
        System.setOut(main.getOut());
        System.setErr(main.getErr());
        final Global global = org.mozilla.javascript.tools.shell.Main.getGlobal();
        global.setIn(main.getIn());
        global.setOut(main.getOut());
        global.setErr(main.getErr());
        main.attachTo(org.mozilla.javascript.tools.shell.Main.shellContextFactory);
        main.setScope(global);
        main.pack();
        main.setSize(600, 460);
        main.setVisible(true);
        org.mozilla.javascript.tools.shell.Main.exec(array);
    }
    
    public static Main mainEmbedded(final String s) {
        final ContextFactory global = ContextFactory.getGlobal();
        final Global global2 = new Global();
        global2.init(global);
        return mainEmbedded(global, global2, s);
    }
    
    public static Main mainEmbedded(final ContextFactory contextFactory, final Scriptable scriptable, final String s) {
        return mainEmbeddedImpl(contextFactory, scriptable, s);
    }
    
    public static Main mainEmbedded(final ContextFactory contextFactory, final ScopeProvider scopeProvider, final String s) {
        return mainEmbeddedImpl(contextFactory, scopeProvider, s);
    }
    
    private static Main mainEmbeddedImpl(final ContextFactory contextFactory, final Object o, final String s) {
        String s2 = s;
        if (s == null) {
            s2 = "Rhino JavaScript Debugger (embedded usage)";
        }
        final Main main = new Main(s2);
        main.doBreak();
        main.setExitAction(new IProxy(1));
        main.attachTo(contextFactory);
        if (o instanceof ScopeProvider) {
            main.setScopeProvider((ScopeProvider)o);
        }
        else {
            final Scriptable scope = (Scriptable)o;
            if (scope instanceof Global) {
                final Global global = (Global)scope;
                global.setIn(main.getIn());
                global.setOut(main.getOut());
                global.setErr(main.getErr());
            }
            main.setScope(scope);
        }
        main.pack();
        main.setSize(600, 460);
        main.setVisible(true);
        return main;
    }
    
    public void attachTo(final ContextFactory contextFactory) {
        this.dim.attachTo(contextFactory);
    }
    
    public void clearAllBreakpoints() {
        this.dim.clearAllBreakpoints();
    }
    
    @Deprecated
    public void contextCreated(final Context context) {
        throw new IllegalStateException();
    }
    
    @Deprecated
    public void contextEntered(final Context context) {
        throw new IllegalStateException();
    }
    
    @Deprecated
    public void contextExited(final Context context) {
        throw new IllegalStateException();
    }
    
    @Deprecated
    public void contextReleased(final Context context) {
        throw new IllegalStateException();
    }
    
    public void detach() {
        this.dim.detach();
    }
    
    public void dispose() {
        this.clearAllBreakpoints();
        this.dim.go();
        this.debugGui.dispose();
        this.dim = null;
    }
    
    public void doBreak() {
        this.dim.setBreak();
    }
    
    public JFrame getDebugFrame() {
        return this.debugGui;
    }
    
    public PrintStream getErr() {
        return this.debugGui.getConsole().getErr();
    }
    
    public InputStream getIn() {
        return this.debugGui.getConsole().getIn();
    }
    
    public PrintStream getOut() {
        return this.debugGui.getConsole().getOut();
    }
    
    public void go() {
        this.dim.go();
    }
    
    public boolean isVisible() {
        return this.debugGui.isVisible();
    }
    
    public void pack() {
        this.debugGui.pack();
    }
    
    public void setBreakOnEnter(final boolean b) {
        this.dim.setBreakOnEnter(b);
        this.debugGui.getMenubar().getBreakOnEnter().setSelected(b);
    }
    
    public void setBreakOnExceptions(final boolean b) {
        this.dim.setBreakOnExceptions(b);
        this.debugGui.getMenubar().getBreakOnExceptions().setSelected(b);
    }
    
    public void setBreakOnReturn(final boolean b) {
        this.dim.setBreakOnReturn(b);
        this.debugGui.getMenubar().getBreakOnReturn().setSelected(b);
    }
    
    public void setExitAction(final Runnable exitAction) {
        this.debugGui.setExitAction(exitAction);
    }
    
    @Deprecated
    public void setOptimizationLevel(final int n) {
    }
    
    public void setScope(final Scriptable scriptable) {
        this.setScopeProvider(IProxy.newScopeProvider(scriptable));
    }
    
    public void setScopeProvider(final ScopeProvider scopeProvider) {
        this.dim.setScopeProvider(scopeProvider);
    }
    
    public void setSize(final int n, final int n2) {
        this.debugGui.setSize(n, n2);
    }
    
    @Deprecated
    public void setSize(final Dimension dimension) {
        this.debugGui.setSize(dimension.width, dimension.height);
    }
    
    public void setSourceProvider(final SourceProvider sourceProvider) {
        this.dim.setSourceProvider(sourceProvider);
    }
    
    public void setVisible(final boolean visible) {
        this.debugGui.setVisible(visible);
    }
    
    private static class IProxy implements Runnable, ScopeProvider
    {
        public static final int EXIT_ACTION = 1;
        public static final int SCOPE_PROVIDER = 2;
        private Scriptable scope;
        private final int type;
        
        public IProxy(final int type) {
            this.type = type;
        }
        
        public static ScopeProvider newScopeProvider(final Scriptable scope) {
            final IProxy proxy = new IProxy(2);
            proxy.scope = scope;
            return proxy;
        }
        
        @Override
        public Scriptable getScope() {
            if (this.type != 2) {
                Kit.codeBug();
            }
            if (this.scope == null) {
                Kit.codeBug();
            }
            return this.scope;
        }
        
        @Override
        public void run() {
            if (this.type != 1) {
                Kit.codeBug();
            }
            System.exit(0);
        }
    }
}
