package com.wiwj.screen.service.impl;

import com.wiwj.screen.mapper.UserMapper;
import com.wiwj.screen.model.User;
import com.wiwj.screen.service.UserService;
import com.wiwj.screen.util.VideoUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User login(User user) {
        User u = userMapper.userLogin(user);
        if (u != null) {
            String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
            u.setToken(VideoUtil.md5(u.getUserid() + u.getPass() + dateStr));
            return u;
        }
        return null;
    }
}
