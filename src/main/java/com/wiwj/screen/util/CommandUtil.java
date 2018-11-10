package com.wiwj.screen.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class CommandUtil {
    public static String EndCommandStr = null;
    public static String BinDir = null;

    public static void execute(String commandStr) {
        if (commandStr == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                //Log.log("commandStr = " + commandStr + " " + BinDir);
                try {
                    Process proc = Runtime.getRuntime().exec(commandStr + " " + BinDir);
                    StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "Error");
                    StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "Output");
                    errorGobbler.start();
                    outputGobbler.start();
                    proc.waitFor();
                } catch (Exception e) {
                    Log.log("commandStr = " + commandStr + " " + BinDir);
                    Log.err("Command.execute:" + e.getMessage());
                }
            }
        }.start();
    }

    public static void end() {
        execute(EndCommandStr);
    }

    public static String run(String commandStr) {
        Runtime runtime = Runtime.getRuntime();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    runtime.exec(commandStr).getInputStream(), Charset.forName("GBK")));
            String line = null;
            StringBuffer b = new StringBuffer();
            while ((line = br.readLine()) != null) {
                b.append(line + "\n");
            }
            return b.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}

/**
 * Runtime.exec()创建的子进程公用父进程的流，
 * 不同平台上，父进程的stream buffer可能被打满导致子进程阻塞，从而永远无法返回。
 * 针对这种情况，我们只需要将子进程的stream重定向出来即可
 */
class StreamGobbler extends Thread {
    InputStream is;
    String type;

    public StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (type.equals("Error")) {
                    Log.err(line);
                } else {
                    Log.log(line);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
