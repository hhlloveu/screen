package com.wiwj.screen.schedule;

import com.wiwj.screen.model.Config;
import com.wiwj.screen.model.Task;
import com.wiwj.screen.service.CommandService;
import com.wiwj.screen.service.ConfigService;
import com.wiwj.screen.service.TaskService;
import com.wiwj.screen.util.CommandUtil;
import com.wiwj.screen.util.MailUtil;
import com.wiwj.screen.util.UrlUtil;
import com.wiwj.screen.util.VideoUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@EnableScheduling
@Order(1)
public class Schedule implements ApplicationRunner, Lock {
    @Resource
    private TaskService taskService;
    @Resource
    private ConfigService configService;
    @Resource
    private CommandService commandService;
    @Resource
    private MailUtil mailUtil;

    private FixTask fixTask;
    private LoopTask loopTask;

    @Override
    public void run(ApplicationArguments applicationArguments) {
        try {
            CommandUtil.EndCommandStr = commandService.getCommand("end").getCommand();
            CommandUtil.BinDir = configService.getConfig("binDirPath").getValue();
            String start = configService.getConfig("start").getValue();
            String stop = configService.getConfig("stop").getValue();
            fixTask = createFixTask();
            loopTask = new LoopTask(start, stop, fixTask, taskService);

            System.out.println("--------------定时任务启动---------------");
        } catch (Exception e) {
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void schedule() {
        //UrlUtil.check(CommandUtil.BinDir + "/log/natapp.log", mailUtil);
        if (loopTask == null) {
            return;
        }
        List<Task> fixList = taskService.selectFixList();
        if (fixList != null) {
            for (Task tsk : fixList) {
                if (tsk.getId() == 2) { // 固播视频文件夹
                    long videoCostTime = tsk.getCost();
                    String videoDirPath = configService.getConfig("videoDirPath").getValue();
                    long videoTime = VideoUtil.getVideosTime(videoDirPath);
                    if (videoTime != -1 && videoCostTime != videoTime) {
                        videoCostTime = videoTime;
                        tsk.setCost(Integer.valueOf("" + videoCostTime));
                        try {
                            taskService.save(tsk);
                        } catch (Exception e) {
                        }
                    }
                    fixTask.setVideoCostTime(videoCostTime);
                    break;
                }
            }
        }
        String start = configService.getConfig("start").getValue();
        String stop = configService.getConfig("stop").getValue();
        loopTask.setStart(start);
        loopTask.setStop(stop);
        loopTask.run();
    }

    @Override
    public void lock() {
        loopTask.lock();
    }

    @Override
    public void unlock() {
        loopTask.setFixTask(createFixTask());
        loopTask.unlock();
    }

    public int getOnOff() {
        return loopTask.isLock() ? 0 : 1;
    }

    private FixTask createFixTask() {
        try {
            return new FixTask(taskService);
        } catch (Exception e) {
            return null;
        }
    }
}
