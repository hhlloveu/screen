package com.wiwj.screen.schedule;

import com.wiwj.screen.util.CommandUtil;
import com.wiwj.screen.util.Log;

public class BreakTask extends Thread {
    private String command;
    private long costTime; //秒
    private FixTask fixTask;

    public BreakTask (String command, long costTime, FixTask fixTask) {
        this.command = command;
        this.costTime = costTime;
        this.fixTask = fixTask;
    }

    @Override
    public void run() {
        Log.log("插播开始");
        CommandUtil.end();
        CommandUtil.execute(command);
        try {
            Thread.sleep(costTime * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CommandUtil.end();
        Log.log("插播结束");
        fixTask.start();
    }
}
