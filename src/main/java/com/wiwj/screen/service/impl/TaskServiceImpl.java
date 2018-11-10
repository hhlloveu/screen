package com.wiwj.screen.service.impl;

import com.wiwj.screen.mapper.TaskMapper;
import com.wiwj.screen.model.Task;
import com.wiwj.screen.service.TaskService;
import com.wiwj.screen.util.VideoUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Resource
    private TaskMapper taskMapper;

    @Override
    public List<Task> selectList() {
        return taskMapper.selectList();
    }

    @Override
    public Task getTodo(String start) {
        return taskMapper.getTodo(start);
    }

    @Override
    public int save(Task task) {
        long cost = VideoUtil.getVideoTime(task.getTarget());
        if ((task.getCost() != null && (task.getCost() < cost || cost <= 0)) || task.getCost() == null) {
            task.setCost(Integer.valueOf("" + cost));
        }
        if (task.getId() == null) {
            return taskMapper.insertSelective(task);
        } else {
            return taskMapper.updateByPrimaryKeySelective(task);
        }
    }
    @Override
    public int delete(Integer id) {
        if (id == null) {
            return 0;
        }
        return taskMapper.delete(id);
    }

    @Override
    public List<Task> selectFixList() {
        return taskMapper.selectFixList();
    }

    @Override
    public List<Task> getTodoFix() {
        return taskMapper.getTodoFix();
    }
}
