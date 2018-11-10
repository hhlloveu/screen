package com.wiwj.screen.service;

import com.wiwj.screen.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> selectList();
    Task getTodo(String start);
    int save(Task task);
    int delete(Integer id);
    List<Task> selectFixList();
    List<Task> getTodoFix();
}
