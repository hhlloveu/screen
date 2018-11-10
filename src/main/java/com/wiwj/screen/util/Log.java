package com.wiwj.screen.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    public static void log(Object o) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println(time + "---" + o);
    }

    public static void err(Object o) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println(time + "-ERROR-" + o);
    }
}
