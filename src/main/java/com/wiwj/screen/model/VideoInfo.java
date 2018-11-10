package com.wiwj.screen.model;

public class VideoInfo extends Config {
    private String type;
    private String videoDirPath;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideoDirPath() {
        return videoDirPath;
    }

    public void setVideoDirPath(String videoDirPath) {
        this.videoDirPath = videoDirPath;
    }
}
