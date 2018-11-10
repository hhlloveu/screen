package com.wiwj.screen.mvc;

import com.wiwj.screen.annotation.LoginRequired;
import com.wiwj.screen.model.Config;
import com.wiwj.screen.model.Task;
import com.wiwj.screen.service.ConfigService;
import com.wiwj.screen.service.TaskService;
import com.wiwj.screen.util.CommandUtil;
import com.wiwj.screen.util.Log;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Resource
    private TaskService taskService;
    @Resource
    private ConfigService configService;

    private String baseDir = System.getProperty("user.dir") + "/";

    @RequestMapping("/list")
    @LoginRequired
    public List selectList() {
        return taskService.selectList();
    }

    @RequestMapping("/list/fix")
    @LoginRequired
    public List selectFixList() {
        return taskService.selectFixList();
    }

    @RequestMapping("/save")
    @LoginRequired
    public int save(@RequestBody Task task) {
        return taskService.save(task);
    }

    @RequestMapping("/delete")
    @LoginRequired
    public int delete(Integer id) {
        return taskService.delete(id);
    }

    @RequestMapping("/upload")
    @LoginRequired
    public String upload(@RequestParam("file") MultipartFile file) {
        String filePath = doUpload(file, "breakFilePath");
        return filePath.replaceAll("\\\\", "/");
    }

    @RequestMapping("/upload/fix")
    @LoginRequired
    public String uploadFix(@RequestParam("file") MultipartFile file) {
        String filePath = doUpload(file, "fixFilePath");
        return filePath.replaceAll("\\\\", "/");
    }

    private String doUpload(MultipartFile file, String pathConfigCode) {
        File temp = null;

        Config cfg = configService.getConfig(pathConfigCode);
        if (cfg != null) {
            try {
                temp = new File(cfg.getValue());
            } catch (Exception e) {
            }
        }
        if (temp == null) {
            baseDir = System.getProperty("user.dir") + "/";
            if (CommandUtil.BinDir != null) {
                baseDir = CommandUtil.BinDir + "/";
            }
            temp = new File(baseDir + "temp");
        }

        if (!temp.exists()) {
            temp.mkdir();
        }
        String fn = file.getOriginalFilename();
        String target = temp.getAbsolutePath() + "/" + fn.replaceAll("\\s*", "");
        try {
            file.transferTo(new File(target));
            Log.log("新文件：" + target + "上传成功");
            return target;
        } catch (Exception e) {
            Log.log("新文件：" + target + "上传失败");
            e.printStackTrace();
            return null;
        }
    }
}
