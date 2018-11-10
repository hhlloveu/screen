package com.wiwj.screen.mapper;

import com.wiwj.screen.model.Command;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommandMapper {
    List<Command> selectList();
    Command getCommand(@Param("code") String code);
}
