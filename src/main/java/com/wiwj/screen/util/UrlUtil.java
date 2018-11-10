package com.wiwj.screen.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class UrlUtil {
    private static String catcheUrl;

    public static void check(String natappLogPath, MailUtil mailUtil) {
        new Thread(){
            @Override
            public void run() {
                String globalUrl = null;
                String check = "Tunnel established at ";
                File file = new File(natappLogPath);
                BufferedReader rd = null;
                try {
                    rd = new BufferedReader(new FileReader(file));
                    for (int i = 0; i < 4; i++) {
                        String line = rd.readLine();
                        if (line == null) break;
                        if (line.contains(check)) {
                            globalUrl = line.split(check)[1];
                            break;
                        }
                    }
                    if (globalUrl != null && !globalUrl.equalsIgnoreCase(catcheUrl)) {
                        Log.log("大屏管理系统最新网址 - "+globalUrl);
                        if (mailUtil.sendSimpleMail("大屏管理系统最新网址", globalUrl)) {
                            catcheUrl = globalUrl + "";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (rd != null) {
                        try {
                            rd.close();
                        } catch (Exception e1) {
                        }
                    }
                }
            }
        }.start();
    }
}
