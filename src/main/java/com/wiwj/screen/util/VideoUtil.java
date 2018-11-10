package com.wiwj.screen.util;

import java.io.*;
import java.security.MessageDigest;
import java.util.*;

import com.wiwj.screen.model.VideoInfo;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.InputFormatException;
import it.sauronsoftware.jave.MultimediaInfo;
import org.springframework.util.FileCopyUtils;

public class VideoUtil {
    public static long getVideosTime(String path) {
        //Get File paths
        File[] files = new File(path).listFiles();
        long totalTime = 0;
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].getName().endsWith(".bak")) {
                    totalTime += getVideoTime(files[i]);
                }
            }
        } else {
            return -1;
        }
        return totalTime;
    }

    public static List<VideoInfo> getVideoFiles(String path) {
        List<VideoInfo> fileList = new ArrayList<>();
        List<VideoInfo> bakList = new ArrayList<>();
        //Get File paths
        File[] files = new File(path).listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                VideoInfo fInfo = new VideoInfo();
                fInfo.setName(files[i].getName());
                fInfo.setCode(md5(files[i].getName()));
                fInfo.setVideoDirPath(path);
                if (files[i].getName().endsWith(".bak")) {
                    fInfo.setType("bak");
                    bakList.add(fInfo);
                } else {
                    fInfo.setType("video");
                    fileList.add(fInfo);
                }
            }
            fileList.addAll(bakList);
        }
        return fileList;
    }

    public static int bak(VideoInfo videoInfo) {
        int ret = 0;
        File[] files = new File(videoInfo.getVideoDirPath()).listFiles();
        for (int i = 0; i < files.length; i++) {
            if (md5(files[i].getName()).equalsIgnoreCase(videoInfo.getCode())) {
                File f = files[i];
                try {
                    File bakFile = new File(f.getAbsolutePath() + ".bak");
                    if (!bakFile.exists()) {
                        bakFile.createNewFile();
                    }
                    ret = moveFile(f, bakFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return ret;
    }

    public static int restore(VideoInfo videoInfo) {
        int ret = 0;
        File[] files = new File(videoInfo.getVideoDirPath()).listFiles();
        for (int i = 0; i < files.length; i++) {
            if (md5(files[i].getName()).equalsIgnoreCase(videoInfo.getCode())) {
                File bakFile = files[i];
                try {
                    String fn = bakFile.getAbsolutePath();
                    File f = new File(fn.substring(0, fn.lastIndexOf(".bak")));
                    if (!f.exists()) {
                        f.createNewFile();
                    }
                    ret = moveFile(bakFile, f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return ret;
    }

    public static boolean checkFileName(VideoInfo videoInfo) {
        File[] files = new File(videoInfo.getVideoDirPath()).listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().equalsIgnoreCase(videoInfo.getName())) {
                return true;
            }
        }
        return false;
    }

    public static long getVideoTime(File file) {
        try {
            Encoder encoder = new Encoder();
            MultimediaInfo multinfo = encoder.getInfo(file);
            return multinfo.getDuration() / 1000;
        } catch (InputFormatException e) {
            Log.log(file.getAbsoluteFile());
            e.printStackTrace();
            return -1;
        } catch (EncoderException e) {
            Log.log(file.getAbsoluteFile());
            e.printStackTrace();
            return -1;
        }
    }

    public static long getVideoTime(String filePath) {
        File file = new File(filePath);
        return getVideoTime(file);
    }

    public static String md5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        } catch (Exception e) {
            return "" + new Date().getTime();
        }
    }

    private static String toHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    private static int moveFile(File f, File target){
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(f);
            out = new FileOutputStream(target);
            FileCopyUtils.copy(in, out);
            f.delete();
            return 1;
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }
}

