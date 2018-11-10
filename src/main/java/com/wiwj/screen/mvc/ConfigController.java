package com.wiwj.screen.mvc;

import com.wiwj.screen.annotation.LoginRequired;
import com.wiwj.screen.model.Config;
import com.wiwj.screen.service.ConfigService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/config")
public class ConfigController {
    @Resource
    private ConfigService configService;

    @RequestMapping("/list")
    @LoginRequired
    public List selectList() {
        return configService.selectList();
    }

    @RequestMapping("/save")
    @LoginRequired
    public int save(@RequestBody Config config) {
        return configService.save(config);
    }
}
