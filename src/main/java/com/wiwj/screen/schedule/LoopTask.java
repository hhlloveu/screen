package com.wiwj.screen.schedule;

import com.wiwj.screen.model.Task;
import com.wiwj.screen.service.TaskService;
import com.wiwj.screen.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoopTask implements Lock{
    private String start;
    private String stop;
    private FixTask fixTask;
    private TaskService taskService;
    private boolean lock = false;

    public LoopTask(String start, String stop, FixTask fixTask, TaskService taskService) {
        this.start = start;
        this.stop = stop;
        this.fixTask = fixTask;
        this.taskService = taskService;
    }

    public void run() {
        if (lock) {
            return;
        }
        Date now = new Date();
        String time = new SimpleDateFormat("HH:mm").format(now) + ":00";
        //Log.log("time: " +time + ",start: " + start + ",stop: " + stop);
        if (time.compareTo(stop) >= 0) {
            // 晚8点之后
            //Log.log("20点至24点之间（24小时制）");
            stop();
        } else if (time.compareTo(start) >= 0) {
            Task task = taskService.getTodo(time);
            if (task != null) {
                stop();
                new BreakTask(task.getCommand(), task.getCost(), fixTask).start();
            } else {
                if (!fixTask.isInterrupted() && !fixTask.isRunning()) {
                    fixTask.start();
                }
            }
        } else {
            // 晚8点至早8点之间
            //Log.log("0点至8点之间（24小时制）");
            stop();
        }
    }

    @Override
    public void lock() {
        Log.log("锁定LoopTask");
        this.lock = true;
        stop();
    }

    @Override
    public void unlock() {
        Log.log("解锁LoopTask");
        this.lock = false;
        run();
    }

    public void stop() {
        fixTask.interrupt();
        fixTask = fixTask.getNew();
    }

    public boolean isLock() {
        return lock;
    }

    public void setFixTask(FixTask fixTask) {
        this.fixTask = fixTask;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }
}
