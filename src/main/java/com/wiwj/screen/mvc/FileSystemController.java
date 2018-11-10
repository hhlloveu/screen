package com.wiwj.screen.mvc;

import com.wiwj.screen.annotation.LoginRequired;
import com.wiwj.screen.util.Log;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fs")
public class FileSystemController {
    @RequestMapping("list")
    @LoginRequired
    public List<FileInfo> list(@RequestBody FileInfo info) {
        File[] fs = null;
        if (info == null) {
            fs = File.listRoots();
        } else if (StringUtils.isEmpty(info.getPath())) {
            fs = File.listRoots();
        } else {
            File p = new File(info.getPath());
            if (p.exists()) {
                fs = p.listFiles();
            }
        }
        return FileInfo.filesToInfos(fs);
    }

    @RequestMapping("download")
    @LoginRequired
    public void download(@RequestParam("path") String path, HttpServletResponse response) {
        if (!StringUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                response.setContentType("application/force-download");//设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + getDownloadName(file.getName()));// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @RequestMapping("upload")
    @LoginRequired
    public int upload(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) {
        String filePath = doUpload(file, path);
        return StringUtils.isEmpty(filePath) ? 0 : 1;
    }

    @RequestMapping("rename")
    @LoginRequired
    public int rename(@RequestBody FileInfo info) {
        if (info != null && !StringUtils.isEmpty(info.getPath())) {
            File f = new File(info.getPath());
            if (f.exists()) {
                String newName = f.getParentFile().getAbsolutePath() + File.separator + info.getName();
                File target = new File(newName);
                if (target.exists()) {
                    return 0;
                }
                try {
                    f.renameTo(target);
                } catch (Exception e) {
                    return 0;
                }
            }
            return 1;
        } else {
            return 0;
        }
    }

    @RequestMapping("create")
    @LoginRequired
    public int create(@RequestBody FileInfo info) {
        if (info != null && !StringUtils.isEmpty(info.getPath())) {
            File f = new File(info.getPath());
            if (f.exists()) {
                String newName = f.getAbsolutePath() + File.separator + info.getName();
                File target = new File(newName);
                if (target.exists() && target.isDirectory()) {
                    return 0;
                }
                try {
                    return target.mkdir() ? 1 : 0;
                } catch (Exception e) {
                    return 0;
                }
            }
            return 1;
        } else {
            return 0;
        }
    }

    @RequestMapping("delete")
    @LoginRequired
    public int delete(@RequestBody FileInfo info) {
        if (info != null && !StringUtils.isEmpty(info.getPath())) {
            File f = new File(info.getPath());
            if (f.exists()) {
                String newName = f.getParentFile().getAbsolutePath() + File.separator + "screen_trash";
                File target = new File(newName);
                if (!target.exists()) {
                    target.mkdir();
                }
                boolean ret = f.renameTo(new File(newName + File.separator + f.getName()));
                return ret ? 1 : 0;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @RequestMapping("recovery")
    @LoginRequired
    public int recovery(@RequestBody FileInfo info) {
        if (info != null && !StringUtils.isEmpty(info.getPath())) {
            File f = new File(info.getPath());
            if (f.exists()) {
                String newName = f.getParentFile().getParentFile().getAbsolutePath();
                boolean ret = f.renameTo(new File(newName + File.separator + f.getName()));
                return ret ? 1 : 0;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    private String getDownloadName(String name) {
        try {
            return new String(name.getBytes("UTF-8"), "iso-8859-1");
        } catch (Exception e) {
            return name;
        }
    }

    private String doUpload(MultipartFile file, String path) {
        File temp = new File(path);
        if (!temp.exists()) {
            temp.mkdir();
        }
        String fn = file.getOriginalFilename();
        String target = temp.getAbsolutePath() + "/" + fn;
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

class FileInfo implements Serializable {
    private String name;
    private String path;
    private String parent;
    private boolean directory;
    private boolean file;
    private long length;
    private List<FileInfo> parents;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public boolean isFile() {
        return file;
    }

    public void setFile(boolean file) {
        this.file = file;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public List<FileInfo> getParents() {
        return parents;
    }

    public void setParents(List<FileInfo> parents) {
        this.parents = parents;
    }

    public static List<FileInfo> filesToInfos(File[] fs) {
        List<FileInfo> infos = new ArrayList<>();
        if (fs != null) {
            for (File f : fs) {
                infos.add(fileToInfo(f, null));
            }
        }
        return infos;
    }

    public static FileInfo fileToInfo(File f, List<FileInfo> parents) {
        FileInfo info = new FileInfo();
        info.setDirectory(f.isDirectory());
        info.setFile(f.isFile());
        info.setLength(f.length());
        info.setPath(f.getAbsolutePath().replaceAll("\\\\", "/"));
        info.setName(!StringUtils.isEmpty(f.getName()) ? f.getName() : info.getPath());
        File p = f.getParentFile();
        if (p != null) {
            info.setParent(p.getAbsolutePath().replaceAll("\\\\", "/"));
            if (parents == null) {
                parents = new ArrayList<>();
                info.setParents(parents);
            }
            parents.add(fileToInfo(p, parents));
        }
        return info;
    }
}