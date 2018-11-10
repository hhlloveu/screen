package com.wiwj.screen.mapper;

import com.wiwj.screen.model.Config;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConfigMapper {
    List<Config> selectList();
    Config getConfig(@Param("code") String code);
    int updateByPrimaryKeySelective(Config config);
}
