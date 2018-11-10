package com.wiwj.screen.mvc;

import com.wiwj.screen.annotation.LoginRequired;
import com.wiwj.screen.util.CommandUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/command")
public class CommandController {
    @RequestMapping("run")
    @LoginRequired
    public Map runCommand(@RequestBody CommandParam param) {
        Map ret = new HashMap();
        if (param != null && !StringUtils.isEmpty(param.getCommand())) {
            ret.put("message", CommandUtil.run(param.getCommand()));
        } else {
            ret.put("message", "命令不能为空");
        }
        return ret;
    }
}

class CommandParam implements Serializable {
    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
