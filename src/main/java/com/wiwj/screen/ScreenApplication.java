package com.wiwj.screen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@MapperScan("com.wiwj.screen.mapper")
public class ScreenApplication {
    private static final String DBFileName = "b8589f148b8a6c";

    public static void main(String[] args) {
        makeDB();
        SpringApplication.run(ScreenApplication.class, args);
    }

    private static void makeDB(){
        InputStream in = null;
        OutputStream out = null;
        try {
            String dbPath = System.getProperty("user.dir") + "/" + DBFileName;
            String bakPath = System.getProperty("user.dir") + "/db_bak";
            File dbFile = new File(dbPath);
            File bakDir = new File(bakPath);
            if(!dbFile.exists()) {
                dbFile = new File(dbPath);
                dbFile.createNewFile();
                in = ScreenApplication.class.getResourceAsStream("/" + DBFileName);
                out = new FileOutputStream(dbFile);
            } else {
                if (!bakDir.exists()) {
                    bakDir.mkdir();
                }
                File bakFile = new File(bakPath+"/" + DBFileName + "." + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
                bakFile.createNewFile();
                in = new FileInputStream(dbFile);
                out = new FileOutputStream(bakFile);
            }
            FileCopyUtils.copy(in, out);
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
    }
}
