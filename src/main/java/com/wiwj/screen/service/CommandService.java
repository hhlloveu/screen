package com.wiwj.screen.service;

import com.wiwj.screen.model.Command;

import java.util.List;

public interface CommandService {
    List<Command> selectList();
    Command getCommand(String code);
}
