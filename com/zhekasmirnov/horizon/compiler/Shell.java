package com.zhekasmirnov.horizon.compiler;

import android.content.*;
import com.pdaxrom.utils.*;
import android.support.annotation.*;
import com.zhekasmirnov.horizon.compiler.packages.*;
import java.io.*;
import java.util.regex.*;

public class Shell
{
    private static final String TAG = "Shell";
    
    private Shell() {
    }
    
    public static void execAndPrintResult(final Context context, final String cwd, final String cmd) {
        System.out.println("executing: " + cmd);
        System.out.println("execution result: " + exec(context, cwd, cmd));
    }
    
    public static CommandResult exec(final Context context, final File file) {
        System.out.println("can exec " + file.canExecute() + " " + file.getParentFile().canExecute());
        if (!file.canExecute()) {
            Utils.chmod(file.getAbsolutePath(), 509);
        }
        return exec(context, file.getParent(), file.getPath());
    }
    
    public static CommandResult exec2(@NonNull final Context context, @NonNull final String workingDir, @NonNull final String command) {
        Process su = null;
        try {
            su = Runtime.getRuntime().exec("/system/bin/sh");
            final DataOutputStream stream = new DataOutputStream(su.getOutputStream());
            stream.writeBytes("exec " + command + "\n");
            stream.flush();
            su.waitFor();
            System.out.println("begin process output:");
            DataInputStream in = new DataInputStream(su.getInputStream());
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in = new DataInputStream(su.getErrorStream());
            while ((line = in.readLine()) != null) {
                System.err.println(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        return null;
    }
    
    public static CommandResult exec(@NonNull final Context context, @NonNull final String workingDir, @NonNull final String command) {
        final long startTime = System.currentTimeMillis();
        try {
            final String[] env = Environment.buildDefaultEnv(context);
            final String[] argv = { "/system/bin/sh" };
            final int[] processIds = { 0 };
            final FileDescriptor fd = Utils.createSubProcess(workingDir, argv[0], argv, env, processIds);
            final int processId = processIds[0];
            if (processId <= 0) {
                return new CommandResult(-1, "Could not create sub process");
            }
            Utils.setPtyUTF8Mode(fd, true);
            Utils.setPtyWindowSize(fd, 128, 1024, 0, 0);
            final BufferedReader input = new BufferedReader(new FileReader(fd));
            final FileOutputStream output = new FileOutputStream(fd);
            output.write("export PS1=''\n".getBytes("UTF-8"));
            output.write(("exec " + command + "\n").getBytes("UTF-8"));
            output.flush();
            final int[] exitCode = { 0 };
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    exitCode[0] = Utils.waitFor(processId);
                }
            });
            thread.start();
            final StringBuilder message = new StringBuilder();
            int skipLine = 6;
            final Pattern patClearNewLine = Pattern.compile("(\\x08)\\1+");
            do {
                String errstr;
                try {
                    errstr = input.readLine();
                    errstr = errstr.replaceAll("\u001b\\[([0-9]|;)*m", "");
                    final Matcher m = patClearNewLine.matcher(errstr);
                    if (m.find()) {
                        final int length = m.end() - m.start();
                        if (m.start() > length) {
                            errstr = errstr.substring(0, m.start() - length) + errstr.substring(m.end());
                        }
                    }
                }
                catch (IOException e) {
                    break;
                }
                if (skipLine > 0) {
                    --skipLine;
                }
                else {
                    message.append(errstr).append("\n");
                }
            } while (thread.isAlive());
            output.close();
            input.close();
            final CommandResult commandResult = new CommandResult(exitCode[0], message.toString());
            final long time = System.currentTimeMillis() - startTime;
            commandResult.setTime(time);
            return commandResult;
        }
        catch (Throwable ie) {
            ie.printStackTrace();
            return new CommandResult(-1, ie.getMessage());
        }
    }
}
