package com.wiwj.screen.mvc;

import com.wiwj.screen.annotation.LoginRequired;
import com.wiwj.screen.service.SqlService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

@RestController
@RequestMapping("/sql")
public class SqlController {
    @Resource
    private SqlService sqlService;

    @RequestMapping({"", "/"})
    @LoginRequired
    public Object sql(@RequestBody SqlParam param) {
        List<Object> list = new ArrayList<>();
        Map ret = new LinkedHashMap();
        String sql = null;
        if (param != null) {
            sql = param.getSql();
        }
        try {
            if (sql == null || "".equals(sql)) {
                ret.put("error", "sql不能为空");
                list.add(ret);
            } else {
                return sqlService.execute(sql);
            }
        } catch (Exception e) {
            ret.put("error", e.getMessage());
            list.add(ret);
        }
        return list;
    }
}

class SqlParam implements Serializable {
    private String sql;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
