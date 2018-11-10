package com.wiwj.screen.service.impl;

import com.wiwj.screen.mapper.CommandMapper;
import com.wiwj.screen.model.Command;
import com.wiwj.screen.service.CommandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommandServiceImpl implements CommandService {
    @Resource
    private CommandMapper commandMapper;

    @Override
    public List<Command> selectList() {
        return commandMapper.selectList();
    }

    @Override
    public Command getCommand(String code) {
        return commandMapper.getCommand(code);
    }
}
