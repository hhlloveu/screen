package com.wiwj.screen.mapper;

import com.wiwj.screen.model.Task;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskMapper {
    List<Task> selectList();
    Task getTodo(@Param("start") String start);
    int insertSelective(Task task);
    int updateByPrimaryKeySelective(Task task);
    int delete(@Param("id") Integer id);
    List<Task> selectFixList();
    List<Task> getTodoFix();
}
