package org.mozilla.javascript.tools.shell;

import org.mozilla.javascript.*;
import java.io.*;
import java.nio.charset.*;

@Deprecated
public class ShellLine
{
    @Deprecated
    public static InputStream getStream(final Scriptable scriptable) {
        final ShellConsole console = ShellConsole.getConsole(scriptable, Charset.defaultCharset());
        if (console != null) {
            return console.getIn();
        }
        return null;
    }
}
