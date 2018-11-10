package com.wiwj.screen.schedule;

import com.wiwj.screen.model.Task;
import com.wiwj.screen.service.TaskService;
import com.wiwj.screen.util.CommandUtil;
import com.wiwj.screen.util.Log;
import com.wiwj.screen.util.VideoUtil;

import java.util.List;

public class FixTask extends Thread {
    private long videoCostTime; //秒
    private TaskService taskService;
    private boolean running = false;

    public FixTask(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void run() {
        running = true;
        Log.log("固定播放开始");
        while (!isInterrupted()) {
            try {
                List<Task> tasks = taskService.getTodoFix();
                if (tasks != null) {
                    for (Task task : tasks) {
                        if ("video".equalsIgnoreCase(task.getType())) {
                            long cost = VideoUtil.getVideoTime(task.getTarget());
                            if ((task.getCost() != null && (task.getCost() < cost || cost <= 0)) || task.getCost() == null) {
                                task.setCost(Integer.valueOf("" + cost));
                                try {
                                    taskService.save(task);
                                } catch (Exception e) {
                                }
                            }
                        }
                        if (task.getCost() > 0) {
                            CommandUtil.end();
                            CommandUtil.execute(task.getCommand());
                            nSleep(task.getCost());
                        }
                    }
                }
            } catch (InterruptedException e) {
                Log.log("固定播放中断");
                break;
            } catch (Exception e) {
                Log.err(e.getMessage());
                e.printStackTrace();
                break;
            }
        }
        Log.log("固定播放结束");
        CommandUtil.end();
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void setVideoCostTime(long videoCostTime) {
        this.videoCostTime = videoCostTime;
    }

    public FixTask getNew() {
        return new FixTask(taskService);
    }

    private void nSleep(long t) throws InterruptedException {
        Thread.sleep(t * 1000);
    }
}
