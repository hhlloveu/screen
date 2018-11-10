package com.wiwj.screen.mvc;

import com.wiwj.screen.annotation.LoginRequired;
import com.wiwj.screen.model.Config;
import com.wiwj.screen.model.VideoInfo;
import com.wiwj.screen.service.ConfigService;
import com.wiwj.screen.util.CommandUtil;
import com.wiwj.screen.util.Log;
import com.wiwj.screen.util.VideoUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/fixvideo")
public class FixVideoController {
    @Resource
    private ConfigService configService;
    private String videoPath;

    @RequestMapping("/list")
    @LoginRequired
    public List selectList() {
        initVideoPath();
        return VideoUtil.getVideoFiles(videoPath);
    }

    @RequestMapping("/bak")
    @LoginRequired
    public int bak(@RequestBody VideoInfo videoInfo) {
        initVideoPath();
        videoInfo.setVideoDirPath(videoPath);
        return VideoUtil.bak(videoInfo);
    }

    @RequestMapping("/restore")
    @LoginRequired
    public int restore(@RequestBody VideoInfo videoInfo) {
        initVideoPath();
        videoInfo.setVideoDirPath(videoPath);
        return VideoUtil.restore(videoInfo);
    }

    @RequestMapping("/upload")
    @LoginRequired
    public String upload(@RequestParam("file") MultipartFile file) {
        initVideoPath();
        String ret = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\"/>" +
                "<script>window.parent.screen.afterFixVideoUpload('" +
                doUpload(file) +
                "');</script>" +
                "</head></html>";
        return ret;
    }

    private void initVideoPath() {
        if (videoPath != null) {
            return;
        }
        Config videoPathConfig = configService.getConfig("videoDirPath");
        if (videoPathConfig == null) {
            return;
        }
        videoPath = videoPathConfig.getValue();
    }

    private int doUpload(MultipartFile file) {
        File temp = new File(videoPath);
        if (!temp.exists()) {
            temp.mkdir();
        }
        String fn = file.getOriginalFilename();
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setName(fn);
        videoInfo.setVideoDirPath(videoPath);
        if (VideoUtil.checkFileName(videoInfo)) {
            return 2;
        }
        String target = temp.getAbsolutePath() + "/" + fn.replaceAll("\\s*", "");
        try {
            file.transferTo(new File(target));
            Log.log("固播视频文件：" + target + "上传成功");
            return 1;
        } catch (Exception e) {
            Log.log("固播视频文件：" + target + "上传失败");
            e.printStackTrace();
            return 0;
        }
    }
}
