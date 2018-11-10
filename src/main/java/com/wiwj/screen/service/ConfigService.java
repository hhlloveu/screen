package com.wiwj.screen.service;

import com.wiwj.screen.model.Config;

import java.util.List;

public interface ConfigService {
    List<Config> selectList();
    Config getConfig(String code);
    int save(Config config);
}
