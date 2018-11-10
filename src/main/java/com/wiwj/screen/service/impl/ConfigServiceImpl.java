package com.wiwj.screen.service.impl;

import com.wiwj.screen.mapper.ConfigMapper;
import com.wiwj.screen.model.Config;
import com.wiwj.screen.service.ConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {
    @Resource
    private ConfigMapper configMapper;

    @Override
    public List<Config> selectList() {
        return configMapper.selectList();
    }

    @Override
    public Config getConfig(String code) {
        return configMapper.getConfig(code);
    }

    @Override
    public int save(Config config) {
        if (config == null) {
            return 0;
        }
        return configMapper.updateByPrimaryKeySelective(config);
    }
}
