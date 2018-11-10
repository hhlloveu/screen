package com.wiwj.screen.mvc;

import com.wiwj.screen.annotation.LoginRequired;
import com.wiwj.screen.schedule.Schedule;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Resource
    private Schedule schedule;

    @RequestMapping("/on")
    @LoginRequired
    public int on() {
        schedule.unlock();
        return 1;
    }

    @RequestMapping("/off")
    @LoginRequired
    public int off() {
        schedule.lock();
        return 1;
    }

    @RequestMapping("/onoff")
    public int getOnOff() {
        return schedule.getOnOff();
    }
}
