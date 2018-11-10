package com.wiwj.screen.service.impl;

import com.wiwj.screen.mapper.TableMapper;
import com.wiwj.screen.service.SqlService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class SqlServiceImpl implements SqlService {
    @Resource
    private TableMapper tableMapper;

    @Override
    public List<LinkedHashMap> execute(String sql) {
        return tableMapper.execute(sql);
    }
}
